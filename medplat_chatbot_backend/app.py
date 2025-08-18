from flask import Flask, request, jsonify
import requests
from datetime import datetime
from flask_cors import CORS
import base64
from PIL import Image
import pytesseract
from io import BytesIO

app = Flask(__name__)
CORS(app)  # Enable CORS for frontend communication

MIDDLEWARE_CHAT_URL = "http://127.0.0.1:8080/chat"
chat_memory = []

@app.route("/send_message", methods=["POST"])
def send_message():
    data = request.json
    user_msg = data.get("message")
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

    except Exception as e:
        error_msg = {
            "sender": "bot",
            "text": f"⚠️ Error: {e}",
            "time": datetime.now().strftime("%H:%M")
        }
        chat_memory.append(error_msg)
        return jsonify([error_msg])

import logging
logging.basicConfig(level=logging.DEBUG)

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
        except Exception as decode_err:
            logging.exception("Image decoding failed")
            return jsonify({"error": "Failed to decode image"}), 400

        # OCR using pytesseract
        try:
            pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

            extracted_text = pytesseract.image_to_string(image)
        except Exception as ocr_err:
            logging.exception("OCR failed")
            return jsonify({"error": "OCR processing failed"}), 500

        return jsonify({"text": extracted_text.strip()})

    except Exception as e:
        logging.exception("Unexpected error in /process_ocr")
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, port=3000)
