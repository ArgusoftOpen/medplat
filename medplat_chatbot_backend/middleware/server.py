from flask import Flask, request, jsonify
from sarvamai import SarvamAI
import requests
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

sarvam_client = SarvamAI(api_subscription_key="b5d9635d-8168-411e-9ed8-0c2e33114f5a")

# Option 1: HARDCODED ngrok URL (update this every time ngrok restarts)

RASA_BASE_URL = "http://localhost:5005"


# Option 2: Auto-fetch ngrok (uncomment if you want automation)
# def get_ngrok_url():
#     try:
#         tunnels = requests.get("http://localhost:4040/api/tunnels").json()
#         for tunnel in tunnels["tunnels"]:
#             if tunnel["proto"] == "https":
#                 return tunnel["public_url"]
#     except Exception as e:
#         logging.error(f"[NGROK] Failed to get ngrok URL: {e}")
#     return None
# 
# RASA_BASE_URL = get_ngrok_url()
RASA_SERVER_URL = f"{RASA_BASE_URL}/webhooks/rest/webhook"

@app.route("/chat", methods=["POST"])
def chat():
    try:
        data = request.get_json()
        user_input = data.get("message", "").strip()
        sender_id = data.get("sender", "default")

        if not user_input:
            return jsonify({"error": "Message is required"}), 400

        # Step 1: Language detection
        lang_result = sarvam_client.text.identify_language(input=user_input)
        lang_code = lang_result.language_code
        logging.info(f"[Language Detection] Language Code: {lang_code}")

        if not lang_code:
            raise ValueError("Language code could not be detected.")

        # Step 2: Translate to English if needed
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
        else:
            translated_input = user_input

        logging.info(f"[Translated to English] {translated_input}")

        # Step 3: Set slot in Rasa tracker
        tracker_url = f"{RASA_BASE_URL}/conversations/{sender_id}/tracker/events"
        tracker_payload = {
            "event": "slot",
            "name": "user_lang",
            "value": lang_code
        }
        tracker_response = requests.post(tracker_url, json=tracker_payload)

        if not tracker_response.ok:
            logging.error(f"[Tracker Error] Status: {tracker_response.status_code}, Body: {tracker_response.text}")
            raise Exception("Failed to set slot in Rasa tracker.")

        # Step 4: Send translated message to Rasa
        rasa_response = requests.post(RASA_SERVER_URL, json={
            "sender": sender_id,
            "message": translated_input
        })

        rasa_response.raise_for_status()  # raises error if HTTP != 200
        return jsonify(rasa_response.json())

    except Exception as e:
        logging.error(f"[ERROR in /chat]: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)