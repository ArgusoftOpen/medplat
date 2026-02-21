from flask import Flask, request, jsonify
from sarvamai import SarvamAI
import requests
import logging
import os
from dotenv import load_dotenv
from pathlib import Path

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load environment variables
env_path = Path(__file__).parent.parent / "rasa_project"/"actions" / "ai.env"
if env_path.exists():
    load_dotenv(dotenv_path=env_path)
else:
    raise FileNotFoundError(f"Environment file not found at {env_path}")

# === Read variables ===
SARVAM_API_KEY = os.getenv("SARVAM_API_KEY")
RASA_BASE_URL = os.getenv("RASA_BASE_URL", "http://localhost:5005")
RASA_SERVER_URL = f"{RASA_BASE_URL}/webhooks/rest/webhook"


# Initialize SarvamAI
sarvam_client = SarvamAI(api_subscription_key=SARVAM_API_KEY)

# Initialize Flask
app = Flask(__name__)
@app.route("/chat", methods=["POST"])
def chat():
    try:
        data = request.get_json()
        user_input = (data.get("message") or "").strip()
        sender_id = data.get("sender", "default")

        if not user_input:
            return jsonify({"error": "Message is required"}), 400

        # --------- Step 1: Language detection ---------
        lang_result = sarvam_client.text.identify_language(input=user_input)
        lang_code = lang_result.language_code
        logger.info(f"[Language Detection] Language Code: {lang_code}")

        if not lang_code:
            raise ValueError("Language code could not be detected.")

        # --------- Step 2: Translate to English ---------
        translated_input = user_input
        if lang_code != "en-IN":
            translation = sarvam_client.text.translate(
                input=user_input,
                source_language_code=lang_code,
                target_language_code="en-IN",
                speaker_gender="Male",
                mode="classic-colloquial",
                model="mayura:v1",
                enable_preprocessing=False
            )
            translated_input = translation.translated_text or user_input
        logger.info(f"[Translated to English] {translated_input}")

        # --------- Step 3: Set user_lang slot in Rasa ---------
        tracker_url = f"{RASA_BASE_URL}/conversations/{sender_id}/tracker/events"
        tracker_payload = {"event": "slot", "name": "user_lang", "value": lang_code}
        tracker_response = requests.post(tracker_url, json=tracker_payload)
        if not tracker_response.ok:
            logger.error(f"[Tracker Error] Status: {tracker_response.status_code}, Body: {tracker_response.text}")
            raise Exception("Failed to set slot in Rasa tracker.")

        # --------- Step 4: Send message to Rasa REST webhook ---------
        rasa_response = requests.post(RASA_SERVER_URL, json={"sender": sender_id, "message": translated_input})
        rasa_response.raise_for_status()
        messages = rasa_response.json()

        # --------- Step 5: Translate Rasa response back to user's language ---------
        if lang_code != "en-IN":
            for msg in messages:
                if "text" in msg:
                    translated_text = sarvam_client.text.translate(
                        input=msg["text"],
                        source_language_code="en-IN",
                        target_language_code=lang_code,
                        speaker_gender="Male",
                        mode="classic-colloquial",
                        model="mayura:v1",
                        enable_preprocessing=False
                    )
                    msg["text"] = translated_text.translated_text or msg["text"]

        return jsonify(messages)

    except Exception as e:
        logger.error(f"[ERROR in /chat]: {str(e)}")
        return jsonify({"error": str(e)}), 500
    
    
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)
