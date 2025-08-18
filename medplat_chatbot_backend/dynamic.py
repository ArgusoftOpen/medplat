from flask import Flask, request, jsonify
import yaml
import subprocess
from flask_cors import CORS
import os
import re

app = Flask(__name__)
CORS(app)

# Define paths
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
NLU_FILE = os.path.join(BASE_DIR, "MED_PLAT", "rasa_project", "data", "nlu.yml")
DOMAIN_FILE = os.path.join(BASE_DIR, "MED_PLAT", "rasa_project", "domain.yml")
ACTIONS_FILE = os.path.join(BASE_DIR, "MED_PLAT", "rasa_project", "actions", "actions.py")

# ----------------- Helper Functions -----------------


def is_valid_intent_name(name):
    """Validates intent naming convention."""
    return re.match(r"^[a-zA-Z_][a-zA-Z0-9_]*$", name)

def is_intent_already_present(intent_name):
    """Checks if intent already exists in NLU."""
    with open(NLU_FILE, "r", encoding='utf-8') as file:
        return f"- intent: {intent_name}" in file.read()

def append_to_nlu(intent_name, examples):
    """Appends new examples to an existing intent or creates a new one."""
    unique_examples = list(set(ex.strip() for ex in examples if ex.strip()))
    if not unique_examples:
        return False

    with open(NLU_FILE, "r", encoding="utf-8") as file:
        lines = file.readlines()

    new_lines = []
    inside_target_intent = False
    intent_found = False

    for line in lines:
        stripped = line.strip()
        if stripped.startswith(f"- intent: {intent_name}"):
            intent_found = True
            new_lines.append(line)
            inside_target_intent = True
            continue

        if inside_target_intent:
            if stripped.startswith("- intent:") or stripped == "":
                inside_target_intent = False
                # insert new examples before closing the block
                for ex in unique_examples:
                    new_lines.append(f"    - {ex}\n")
            else:
                new_lines.append(line)
            continue

        new_lines.append(line)

    # If intent not found, add it at the end
    if not intent_found:
        new_lines.append(f"\n- intent: {intent_name}\n  examples: |\n")
        for ex in unique_examples:
            new_lines.append(f"    - {ex}\n")

    with open(NLU_FILE, "w", encoding="utf-8") as file:
        file.writelines(new_lines)

    return True
@app.route("/update_intent_examples", methods=["POST"])
def update_intent_examples():
    data = request.json
    intent_name = data.get("intent", "").strip()
    examples = data.get("examples", [])

    if not intent_name or not examples:
        return jsonify({"error": "Missing intent or examples"}), 400

    if not is_valid_intent_name(intent_name):
        return jsonify({"error": "Invalid intent name"}), 400

    try:
        if not append_to_nlu(intent_name, examples):
            return jsonify({"error": "No valid training examples provided"}), 400

        result = subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )
        print(result.stdout)

    except subprocess.CalledProcessError as e:
        print("⚠️ Rasa training failed!")
        print("STDOUT:", e.stdout)
        print("STDERR:", e.stderr)
        return jsonify({
            "error": "Rasa training failed",
            "stdout": e.stdout,
            "stderr": e.stderr
    }), 500



def add_intent_to_domain(intent_name):
    """Adds intent to domain.yml"""
    with open(DOMAIN_FILE, "r+", encoding='utf-8') as file:
        data = yaml.safe_load(file)
        if "intents" not in data:
            data["intents"] = []
        if intent_name not in data["intents"]:
            data["intents"].append(intent_name)
        file.seek(0)
        yaml.dump(data, file, sort_keys=False, allow_unicode=True)
        file.truncate()

def add_response_to_actions_py(intent_name, response):
    """Adds intent and response to actions.py INTENTS dict."""
    with open(ACTIONS_FILE, "r+", encoding='utf-8') as file:
        code = file.read()
        marker = "# Intent responses\nINTENTS = {"
        start = code.find(marker)
        if start == -1:
            return
        end = code.find("}", start)
        insert_pos = end
        new_entry = f'    "{intent_name}": "{response}",\n'
        if new_entry not in code:
            code = code[:insert_pos] + new_entry + code[insert_pos:]
        file.seek(0)
        file.write(code)
        file.truncate()

# ----------------- Route -----------------

@app.route("/add_intent", methods=["POST"])
def add_intent():
    data = request.json
    intent_name = data.get("intent", "").strip()
    examples = data.get("examples", [])
    response = data.get("response", "").strip()

    if not intent_name or not examples or not response:
        return jsonify({"error": "Missing intent, examples, or response"}), 400

    if not is_valid_intent_name(intent_name):
        return jsonify({"error": "Invalid intent name. Use only letters, numbers, and underscores."}), 400

    if is_intent_already_present(intent_name):
        return jsonify({"error": f"Intent '{intent_name}' already exists."}), 400

    try:
        if not append_to_nlu(intent_name, examples):
            return jsonify({"error": "No valid training examples provided."}), 400

        add_intent_to_domain(intent_name)
        add_response_to_actions_py(intent_name, response)

        result = subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )
        print(result.stdout)

    except subprocess.CalledProcessError as e:
        return jsonify({"error": "Rasa training failed", "details": e.stderr}), 500

    return jsonify({"message": f"Intent '{intent_name}' added successfully and model trained."})

@app.route("/intents_with_examples", methods=["GET"])
def intents_with_examples():
    intents = {}
    with open(NLU_FILE, "r", encoding="utf-8") as file:
        lines = file.readlines()

    current_intent = None
    for line in lines:
        if line.strip().startswith("- intent:"):
            current_intent = line.strip().split(":")[1].strip()
            intents[current_intent] = []
        elif current_intent and line.strip().startswith("- "):
            example = line.strip()[2:].strip()
            intents[current_intent].append(example)

    return jsonify(intents)


@app.route("/get_intents", methods=["GET"])
def get_intents():
    try:
        if not os.path.exists(NLU_FILE):
            return jsonify({"intents": []})

        with open(NLU_FILE, "r", encoding="utf-8") as f:
            nlu_data = yaml.safe_load(f)

        intents = []
        if nlu_data and "nlu" in nlu_data:
            for item in nlu_data["nlu"]:
                if isinstance(item, dict) and "intent" in item:
                    examples = []
                    if "examples" in item and isinstance(item["examples"], str):
                        # split into list, strip "- " prefix
                        examples = [
                            ex.strip()[2:]
                            for ex in item["examples"].split("\n")
                            if ex.strip().startswith("- ")
                        ]
                    intents.append({"intent": item["intent"], "examples": examples})

        return jsonify({"intents": intents})

    except Exception as e:
        print("❌ Error in /get_intents:", e)  # show full error in Flask console
        return jsonify({"error": "Failed to load intents", "details": str(e)}), 500




@app.route("/intent/<intent_name>", methods=["DELETE"])
def delete_intent(intent_name):
    intent_name = intent_name.strip()
    if not intent_name:
        return jsonify({"error": "Intent name is required"}), 400

    try:
        with open(NLU_FILE, "r", encoding="utf-8") as file:
            lines = file.readlines()

        new_lines = []
        skip = False
        for line in lines:
            if line.strip().startswith(f"- intent: {intent_name}"):
                skip = True
            elif skip and (line.strip().startswith("- intent:") or line.strip() == ""):
                skip = False
            if not skip:
                new_lines.append(line)

        with open(NLU_FILE, "w", encoding="utf-8") as file:
            file.writelines(new_lines)

        with open(DOMAIN_FILE, "r+", encoding='utf-8') as file:
            domain_data = yaml.safe_load(file)
            domain_data["intents"] = [i for i in domain_data.get("intents", []) if i != intent_name]
            file.seek(0)
            yaml.dump(domain_data, file, sort_keys=False, allow_unicode=True)
            file.truncate()

        with open(ACTIONS_FILE, "r+", encoding='utf-8') as file:
            code = file.read()
            pattern = f'"{intent_name}": ".*?",\n'
            code = re.sub(pattern, "", code)
            file.seek(0)
            file.write(code)
            file.truncate()

        subprocess.run([
            "rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )

        return jsonify({"message": f"Intent '{intent_name}' deleted and model retrained."})

    except Exception as e:
        return jsonify({"error": "Failed to delete intent", "details": str(e)}), 500

@app.route("/upload_file", methods=["POST"])
def upload_file():
    file = request.files.get("file")
    file_type = request.form.get("type")

    if not file or not file_type:
        return jsonify({"error": "File and type are required."}), 400

    try:
        content = file.read().decode("utf-8")

        if file_type == "txt":
            lines = content.strip().splitlines()
            current_intent = None
            examples = []

            for line in lines:
                line = line.strip()
                if not line:
                    continue
                if line.startswith("## intent:"):
                    if current_intent and examples:
                        append_to_nlu(current_intent, examples)
                        add_intent_to_domain(current_intent)
                        add_response_to_actions_py(current_intent, f"Default response for {current_intent}")
                    current_intent = line.replace("## intent:", "").strip()
                    examples = []
                else:
                    examples.append(line)

            if current_intent and examples:
                append_to_nlu(current_intent, examples)
                add_intent_to_domain(current_intent)
                add_response_to_actions_py(current_intent, f"Default response for {current_intent}")

        else:
            return jsonify({"error": "Invalid file type"}), 400

        subprocess.run(["rasa", "train"], check=True, cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"))

        return jsonify({"message": "File uploaded, intents added, and model trained successfully."})

    except Exception as e:
        return jsonify({"error": "Upload failed", "details": str(e)}), 500

@app.route("/intent/<intent_name>/example", methods=["DELETE"])
def delete_intent_example(intent_name):
    """
    Delete a single example from an intent in nlu.yml
    Body: { "example": "sentence to delete" }
    """
    try:
        data = request.json
        example_to_delete = data.get("example", "").strip()
        if not example_to_delete:
            return jsonify({"error": "Example text is required"}), 400

        with open(NLU_FILE, "r", encoding="utf-8") as f:
            lines = f.readlines()

        new_lines = []
        inside_target_intent = False

        for line in lines:
            stripped = line.strip()
            if stripped.startswith(f"- intent: {intent_name}"):
                inside_target_intent = True
                new_lines.append(line)
                continue

            if inside_target_intent:
                if stripped.startswith("- "):
                    if stripped[2:].strip() == example_to_delete:
                        continue  # skip deleting this example
                elif stripped.startswith("- intent:") or stripped == "":
                    inside_target_intent = False
                new_lines.append(line)
            else:
                new_lines.append(line)

        with open(NLU_FILE, "w", encoding="utf-8") as f:
            f.writelines(new_lines)

        subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )

        return jsonify({"message": f"Example '{example_to_delete}' removed from intent '{intent_name}'."})

    except Exception as e:
        return jsonify({"error": "Failed to delete example", "details": str(e)}), 500


@app.route("/upload_yaml", methods=["POST"])
def upload_yaml():
    try:
        data = request.get_json()
        yaml_type = data.get("type")
        yaml_content = data.get("yaml")

        if yaml_type not in ("nlu", "domain", "both"):
            return jsonify({"error": "Invalid YAML type. Must be 'nlu', 'domain', or 'both'"}), 400

        if not yaml_content:
            return jsonify({"error": "Invalid or empty YAML content"}), 400

        if yaml_type == "nlu" or yaml_type == "both":
            with open(NLU_FILE, "w", encoding="utf-8") as f:
                f.write(yaml_content)

        if yaml_type == "domain" or yaml_type == "both":
            with open(DOMAIN_FILE, "w", encoding="utf-8") as f:
                f.write(yaml_content)

        subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )

        return jsonify({"message": f"{yaml_type} YAML uploaded and model trained."})

    except Exception as e:
        return jsonify({"error": "YAML upload failed", "details": str(e)}), 500
@app.route("/update_intent", methods=["POST"])
def update_intent():
    """
    Update intent name in nlu.yml, domain.yml, and actions.py
    """
    data = request.json
    old_intent = data.get("old_intent", "").strip()
    new_intent = data.get("new_intent", "").strip()

    if not old_intent or not new_intent:
        return jsonify({"error": "Both old_intent and new_intent are required"}), 400

    try:
        # --- Update NLU ---
        with open(NLU_FILE, "r", encoding="utf-8") as f:
            content = f.read()
        content = content.replace(f"- intent: {old_intent}", f"- intent: {new_intent}")
        with open(NLU_FILE, "w", encoding="utf-8") as f:
            f.write(content)

        # --- Update Domain ---
        with open(DOMAIN_FILE, "r+", encoding="utf-8") as f:
            domain_data = yaml.safe_load(f)
            intents = domain_data.get("intents", [])
            if old_intent in intents:
                intents[intents.index(old_intent)] = new_intent
            domain_data["intents"] = intents
            f.seek(0)
            yaml.dump(domain_data, f, sort_keys=False, allow_unicode=True)
            f.truncate()

        # --- Update Actions.py ---
        with open(ACTIONS_FILE, "r+", encoding="utf-8") as f:
            code = f.read()
            code = code.replace(f'"{old_intent}"', f'"{new_intent}"')
            f.seek(0)
            f.write(code)
            f.truncate()

        # --- Retrain Rasa model ---
        subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )

        return jsonify({"message": f"Intent '{old_intent}' updated to '{new_intent}' and model retrained."})

    except Exception as e:
        return jsonify({"error": "Failed to update intent", "details": str(e)}), 500

@app.route("/delete_intent", methods=["POST"])
def delete_intent_post():
    """
    Delete intent via POST { "intent": "<intent_name>" }
    (so frontend can just call POST instead of DELETE)
    """
    data = request.json
    intent_name = data.get("intent", "").strip()
    if not intent_name:
        return jsonify({"error": "Intent name is required"}), 400

    try:
        # --- Remove from NLU ---
        with open(NLU_FILE, "r", encoding="utf-8") as file:
            lines = file.readlines()

        new_lines = []
        skip = False
        for line in lines:
            if line.strip().startswith(f"- intent: {intent_name}"):
                skip = True
            elif skip and (line.strip().startswith("- intent:") or line.strip() == ""):
                skip = False
            if not skip:
                new_lines.append(line)

        with open(NLU_FILE, "w", encoding="utf-8") as file:
            file.writelines(new_lines)

        # --- Remove from Domain ---
        with open(DOMAIN_FILE, "r+", encoding="utf-8") as file:
            domain_data = yaml.safe_load(file)
            domain_data["intents"] = [i for i in domain_data.get("intents", []) if i != intent_name]
            file.seek(0)
            yaml.dump(domain_data, file, sort_keys=False, allow_unicode=True)
            file.truncate()

        # --- Remove from Actions.py ---
        with open(ACTIONS_FILE, "r+", encoding="utf-8") as file:
            code = file.read()
            pattern = f'"{intent_name}": ".*?",\n'
            code = re.sub(pattern, "", code)
            file.seek(0)
            file.write(code)
            file.truncate()

        # --- Retrain Rasa ---
        subprocess.run(
            ["rasa", "train"],
            check=True,
            cwd=os.path.join(BASE_DIR, "MED_PLAT", "rasa_project"),
            capture_output=True,
            text=True
        )

        return jsonify({"message": f"Intent '{intent_name}' deleted and model retrained."})

    except Exception as e:
        return jsonify({"error": "Failed to delete intent", "details": str(e)}), 500

# ----------------- Main -----------------

if __name__ == "__main__":
    app.run(port=5000)





