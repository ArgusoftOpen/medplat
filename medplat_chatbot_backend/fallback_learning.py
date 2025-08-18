from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
from pymongo import MongoClient
import numpy as np
from flask_cors import CORS
import os
import requests

# === CONFIG ===
MONGO_URI = os.environ.get("MONGO_URI", "mongodb://localhost:27017/")
DB_NAME = "medplat"
COLLECTION_NAME = "fallback_clusters"
SIMILARITY_THRESHOLD = 0.75

# === APP SETUP ===
app = Flask(__name__)
CORS(app)

model = SentenceTransformer('all-MiniLM-L6-v2')
mongo_client = MongoClient(MONGO_URI)
db = mongo_client[DB_NAME]
collection = db[COLLECTION_NAME]

def get_next_cluster_id():
    last = collection.find_one(sort=[("cluster_id", -1)])
    return (last["cluster_id"] + 1) if last else 1

@app.route("/fallback_", methods=["POST"])
def fallback_capture():
    data = request.get_json()
    text = data.get("original_text", "")
    gpt_reply = data.get("openai_reply", "")

    if not text:
        return jsonify({"error": "Missing original_text"}), 400

    new_embedding = model.encode([text])[0].tolist()

    all_fallbacks = list(collection.find())
    if not all_fallbacks:
        cluster_id = get_next_cluster_id()
        collection.insert_one({
            "text": text,
            "embedding": new_embedding,
            "gpt_reply": gpt_reply,
            "cluster_id": cluster_id,
            "freq": 1
        })
        return jsonify({"status": "cluster_created", "cluster_id": cluster_id})

    embeddings = [entry["embedding"] for entry in all_fallbacks]
    sims = cosine_similarity([new_embedding], embeddings)[0]
    max_sim = np.max(sims)
    best_idx = int(np.argmax(sims))

    if max_sim >= SIMILARITY_THRESHOLD:
        matched = all_fallbacks[best_idx]
        updated_freq = matched["freq"] + 1

        # Update existing cluster
        collection.update_one(
            {"_id": matched["_id"]},
            {
                "$inc": {"freq": 1},
                "$set": {"gpt_reply": gpt_reply}
            }
        )

        # === AUTO INTENT CREATION ===
        if updated_freq >= 12:
            intent_name = f"auto_intent_{matched['cluster_id']}"
            examples = [
                 entry["text"] for entry in all_fallbacks
                 if entry["cluster_id"] == matched["cluster_id"]
            ][:5]
            intent_data = {
                "intent": intent_name,
                "examples": examples,
                "response": (gpt_reply or "Thanks for reaching out. We’ve added support for this!").replace("\n", "\\n") + ","

            }
            try:
               
                res = requests.post("http://localhost:5000/add_intent", json=intent_data)
                if res.status_code == 200:
                    print(f"[✔] Auto intent '{intent_name}' added.")
                    # Optional: trigger retrain
                    
                else:
                    print(f"[✖] Failed to add intent: {res.text}")
            except Exception as e:
                print(f"[!] Error calling /add_intent: {e}")

        return jsonify({
            "status": "cluster_updated",
            "cluster_id": matched["cluster_id"],
            "similarity": float(max_sim)
        })

    else:
        # Create new cluster
        cluster_id = get_next_cluster_id()
        collection.insert_one({
            "text": text,
            "embedding": new_embedding,
            "gpt_reply": gpt_reply,
            "cluster_id": cluster_id,
            "freq": 1
        })
        return jsonify({"status": "cluster_created", "cluster_id": cluster_id})


@app.route("/get_fallbacks", methods=["GET"])
def get_fallbacks():
    all = list(collection.find())
    simplified_data = [
        {
            "cluster_id": entry["cluster_id"],
            "text": entry["text"],
            "freq": entry["freq"],
            "gpt_reply": entry.get("gpt_reply", "")
        }
        for entry in all
    ]
    return jsonify({"fallbacks": simplified_data})

if __name__ == "__main__":
    app.run(port=5001, debug=True)
