from flask import Flask, request, jsonify, render_template, session
import uuid
import json
import os
import requests
from datetime import datetime
import time
from flask_sqlalchemy import SQLAlchemy
from translate import Translator
import re

app = Flask(__name__)
app.secret_key = "medplat_chatbot_secret_key"
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///tickets.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

# OpenAI API configuration
OPENAI_API_KEY = os.environ.get("OPENAI_API_KEY", "")  # Get from environment or leave empty
OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"

# Ticket model
class Ticket(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    ticket_id = db.Column(db.String(20), unique=True)
    user_id = db.Column(db.String(100))
    user_name = db.Column(db.String(100))
    issue = db.Column(db.Text)
    description = db.Column(db.Text)
    status = db.Column(db.String(20), default="Pending")  # Pending, In Progress, Resolved
    language = db.Column(db.String(20))
    device_type = db.Column(db.String(50))
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    
    def to_dict(self):
        return {
            'id': self.id,
            'ticket_id': self.ticket_id,
            'user_name': self.user_name,
            'issue': self.issue,
            'description': self.description,
            'status': self.status,
            'language': self.language,
            'device_type': self.device_type,
            'created_at': self.created_at.strftime('%Y-%m-%d %H:%M:%S'),
            'updated_at': self.updated_at.strftime('%Y-%m-%d %H:%M:%S')
        }

# Create the database and tables
with app.app_context():
    db.create_all()

# Common issues and solutions in different languages
FAQs = {
    "en": {
        "login_issues": {
            "question": "I'm having trouble logging in",
            "answer": "Have you tried resetting your password? If not, I can help you with that."
        },
        "data_sync": {
            "question": "My data is not syncing",
            "answer": "Please check your internet connection and try again. If the problem persists, try clearing the app cache."
        }
    },
    "hi": {
        "login_issues": {
            "question": "मैं लॉगिन नहीं कर पा रहा हूं",
            "answer": "क्या आपने पासवर्ड रीसेट करने की कोशिश की? अगर नहीं, मैं आपकी मदद कर सकती हूं।"
        },
        "data_sync": {
            "question": "मेरा डेटा सिंक नहीं हो रहा है",
            "answer": "कृपया अपने इंटरनेट कनेक्शन की जांच करें और फिर से प्रयास करें। यदि समस्या बनी रहती है, तो ऐप कैश साफ़ करने का प्रयास करें।"
        }
    }
}

# Language detection using patterns (simplified)
def detect_language(text):
    # Hindi characters range
    hindi_pattern = re.compile(r'[\u0900-\u097F]')
    if hindi_pattern.search(text):
        return "hi"
    return "en"  # Default to English

# Translate text between languages
def translate_text(text, source_lang, target_lang):
    if source_lang == target_lang:
        return text
    
    try:
        translator = Translator(from_lang=source_lang, to_lang=target_lang)
        return translator.translate(text)
    except Exception as e:
        print(f"Translation error: {e}")
        return text  # Return original if translation fails

# Function to call OpenAI API
def ask_openai(prompt, language="en"):
    if not OPENAI_API_KEY:
        # Simulate response if no API key is provided
        return simulate_ai_response(prompt, language)
    
    headers = {
        "Authorization": f"Bearer {OPENAI_API_KEY}",
        "Content-Type": "application/json"
    }
    
    data = {
        "model": "gpt-3.5-turbo",  # Use appropriate model
        "messages": [
            {"role": "system", "content": f"You are a helpful assistant for healthcare workers using the MedPlat application. Respond in {language} language only."},
            {"role": "user", "content": prompt}
        ],
        "temperature": 0.7,
        "max_tokens": 500
    }
    
    try:
        response = requests.post(OPENAI_API_URL, headers=headers, json=data)
        if response.status_code == 200:
            return response.json()["choices"][0]["message"]["content"]
        else:
            print(f"API Error: {response.status_code}, {response.text}")
            return simulate_ai_response(prompt, language)
    except Exception as e:
        print(f"OpenAI API error: {e}")
        return simulate_ai_response(prompt, language)

# Simulate AI response for when API is not available
def simulate_ai_response(prompt, language="en"):
    responses = {
        "en": [
            "I understand you need help with the MedPlat system. Our support team has been notified.",
            "Thank you for your question. This is a simulated response as our AI service is currently in maintenance mode.",
            "I'm here to help with basic MedPlat questions. For more complex issues, please submit a support ticket.",
            "I've recorded your question about MedPlat. A support representative will contact you shortly.",
            "Could you provide more details about your issue with MedPlat? This will help our support team assist you better."
        ],
        "hi": [
            "मैं समझता हूं कि आपको MedPlat सिस्टम से मदद की आवश्यकता है। हमारी सहायता टीम को सूचित कर दिया गया है।",
            "आपके प्रश्न के लिए धन्यवाद। यह एक सिमुलेटेड प्रतिक्रिया है क्योंकि हमारी AI सेवा वर्तमान में रखरखाव मोड में है।",
            "मैं बुनियादी MedPlat प्रश्नों में मदद करने के लिए यहां हूं। अधिक जटिल मुद्दों के लिए, कृपया एक सपोर्ट टिकट जमा करें।",
            "मैंने MedPlat के बारे में आपका सवाल दर्ज कर लिया है। एक सहायता प्रतिनिधि जल्द ही आपसे संपर्क करेगा।",
            "क्या आप MedPlat के साथ अपनी समस्या के बारे में अधिक जानकारी प्रदान कर सकते हैं? इससे हमारी सहायता टीम को आपकी बेहतर सहायता करने में मदद मिलेगी।"
        ]
    }
    
    # Use English responses as fallback if language not available
    available_responses = responses.get(language, responses["en"])
    
    # Simple deterministic response based on prompt length to keep responses consistent
    index = sum(ord(c) for c in prompt) % len(available_responses)
    return available_responses[index]

# Generate response based on user input
def generate_response(user_input, language="en"):
    # Check for common issues
    for issue_key, issue_data in FAQs.get(language, FAQs["en"]).items():
        if issue_data["question"].lower() in user_input.lower():
            return issue_data["answer"]
    
    # If no predefined answer, use OpenAI API
    prompt = f"The user is a healthcare worker using MedPlat application and said: '{user_input}'. Provide a helpful response."
    return ask_openai(prompt, language)

# Create a new ticket
def create_ticket(user_id, user_name, issue, description, language, device_type):
    ticket_id = f"TKT-{int(time.time())}"
    new_ticket = Ticket(
        ticket_id=ticket_id,
        user_id=user_id,
        user_name=user_name,
        issue=issue,
        description=description,
        status="Pending",
        language=language,
        device_type=device_type
    )
    
    db.session.add(new_ticket)
    db.session.commit()
    return new_ticket.to_dict()

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/dashboard')
def dashboard():
    return render_template('dashboard.html')

@app.route('/api/chat', methods=['POST'])
def chat():
    data = request.json
    user_input = data.get('message', '')
    user_id = data.get('user_id', 'anonymous')
    user_name = data.get('user_name', 'Anonymous User')
    device_type = data.get('device_type', 'Unknown')
    
    # Detect language
    language = detect_language(user_input)
    
    # Generate response
    response = generate_response(user_input, language)
    
    # Check if this is an issue that needs a ticket
    issue_keywords = ["issue", "problem", "error", "not working", "help", "समस्या", "सहायता", "मदद", "त्रुटि"]
    needs_ticket = any(keyword in user_input.lower() for keyword in issue_keywords)
    
    result = {
        "response": response,
        "language": language,
        "needs_ticket": needs_ticket
    }
    
    return jsonify(result)

@app.route('/api/create_ticket', methods=['POST'])
def api_create_ticket():
    data = request.json
    user_id = data.get('user_id', 'anonymous')
    user_name = data.get('user_name', 'Anonymous User')
    issue = data.get('issue', '')
    description = data.get('description', '')
    language = data.get('language', 'en')
    device_type = data.get('device_type', 'Unknown')
    
    ticket = create_ticket(user_id, user_name, issue, description, language, device_type)
    return jsonify({"status": "success", "ticket": ticket})

@app.route('/api/tickets', methods=['GET'])
def get_tickets():
    tickets = Ticket.query.all()
    return jsonify([ticket.to_dict() for ticket in tickets])

@app.route('/api/ticket/<ticket_id>', methods=['GET'])
def get_ticket(ticket_id):
    ticket = Ticket.query.filter_by(ticket_id=ticket_id).first()
    if not ticket:
        return jsonify({"status": "error", "message": "Ticket not found"}), 404
    return jsonify(ticket.to_dict())

@app.route('/api/ticket/<ticket_id>/update', methods=['POST'])
def update_ticket(ticket_id):
    data = request.json
    ticket = Ticket.query.filter_by(ticket_id=ticket_id).first()
    
    if not ticket:
        return jsonify({"status": "error", "message": "Ticket not found"}), 404
    
    if 'status' in data:
        ticket.status = data['status']
    
    db.session.commit()
    return jsonify({"status": "success", "ticket": ticket.to_dict()})

if __name__ == '__main__':
    app.run(debug=True)