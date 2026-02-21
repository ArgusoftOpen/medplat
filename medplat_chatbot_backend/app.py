from flask import Flask, request, jsonify
import requests
from datetime import datetime
from flask_cors import CORS
import base64
from PIL import Image
import pytesseract
from io import BytesIO
import logging
import os

app = Flask(__name__)
CORS(app)  # Enable CORS for frontend communication
logging.basicConfig(level=logging.DEBUG)

# ------------------- Configuration -------------------
MIDDLEWARE_CHAT_URL = os.environ.get("MIDDLEWARE_CHAT_URL", "http://127.0.0.1:8080/chat")
chat_memory = []  # In-memory chat memory (use DB in production)

# ------------------- Chat Endpoint -------------------
@app.route("/send_message", methods=["POST"])
def send_message():
    data = request.json
    user_msg = (data.get("message") or "").strip()
    now = datetime.now().strftime("%H:%M")

    chat_memory.append({"sender": "user", "text": user_msg, "time": now})

    try:
        payload = {"sender": "web_user", "message": user_msg}
        response = requests.post(MIDDLEWARE_CHAT_URL, json=payload, timeout=80)
        response.raise_for_status()
        bot_msgs = response.json()
        responses = []

        for bot_msg in bot_msgs:
            msg = {
                "sender": "bot",
                "text": bot_msg.get("text", ""),
                "time": datetime.now().strftime("%H:%M"),
            }
            if "buttons" in bot_msg:
                msg["buttons"] = bot_msg["buttons"]

            chat_memory.append(msg)
            responses.append(msg)

        return jsonify(responses)

    except requests.exceptions.RequestException as req_err:
        logging.exception("Error calling middleware /chat")
        error_msg = {
            "sender": "bot",
            "text": f"⚠️ Error: {req_err}",
            "time": datetime.now().strftime("%H:%M")
        }
        chat_memory.append(error_msg)
        return jsonify([error_msg]), 500

# ------------------- OCR Endpoint -------------------
@app.route("/process_ocr", methods=["POST"])
def process_ocr():
    try:
        data = request.json
        logging.debug(f"Received data keys: {list(data.keys())}")

        base64_image = data.get("image")
        if not base64_image:
            return jsonify({"error": "No image data received"}), 400

        # Decode base64 and convert to image
        try:
            image_data = base64.b64decode(base64_image)
            image = Image.open(BytesIO(image_data))
        except Exception:
            logging.exception("Image decoding failed")
            return jsonify({"error": "Failed to decode image"}), 400

        # OCR using pytesseract
        try:
            # Cross-platform: use environment variable if set, otherwise default detection
            pytesseract_path = os.environ.get("TESSERACT_CMD")
            if pytesseract_path:
                pytesseract.pytesseract.tesseract_cmd = pytesseract_path

            extracted_text = pytesseract.image_to_string(image)
        except Exception:
            logging.exception("OCR failed")
            return jsonify({"error": "OCR processing failed"}), 500

        return jsonify({"text": extracted_text.strip()})

    except Exception:
        logging.exception("Unexpected error in /process_ocr")
        return jsonify({"error": "Internal server error"}), 500

# ------------------- Main -------------------
if __name__ == "__main__":
    port = int(os.environ.get("PORT", 3000))
    debug_mode = os.environ.get("DEBUG", "True").lower() == "true"
    app.run(host="0.0.0.0", port=port, debug=debug_mode)
