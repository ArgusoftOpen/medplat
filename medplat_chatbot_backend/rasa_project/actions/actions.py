from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
from sarvamai import SarvamAI
import openai
import os
import logging
import re
import random
from email_utils import send_reset_email
from dotenv import load_dotenv
from pathlib import Path
import requests



# Set up logging
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

# Explicitly load ai.env
env_path = Path(__file__).parent / "ai.env"
load_dotenv(dotenv_path=env_path)

# Initialize SarvamAI and OpenAI
client = SarvamAI(api_subscription_key=os.getenv("SARVAM_API_KEY"))
openai.api_key = os.environ.get("OPENAI_API_KEY")

# Supported language codes for SarvamAI
VALID_LANGS = {
    "bn-IN", "en-IN", "gu-IN", "hi-IN", "kn-IN", "ml-IN", "mr-IN", "od-IN",
    "pa-IN", "ta-IN", "te-IN", "as-IN", "brx-IN", "doi-IN", "kok-IN",
    "ks-IN", "mai-IN", "mni-IN", "ne-IN", "sa-IN", "sat-IN", "sd-IN", "ur-IN"
}

# Email validation
def is_valid_email(email: str) -> bool:
    return re.match(r"[^@]+@[^@]+\\.[^@]+", email) is not None

# Intent responses
INTENTS = {
    "login_issue": "Please try resetting your password. Do you need help?",
    "password_reset": "Please provide your registered email to receive the reset link.",
    "still_issue": "Sorry about that. I’ll escalate this issue to support.",
    "issue_not_fixed": "Understood. I’ll collect more details for support.",
    "need_human_support": "I’m connecting you to a support agent.",
    "thank_you": "You're welcome!",
    "greet": "Hello!",
    "goodbye": "Goodbye! Have a great day!",
    "how_are_you": "I'm doing great, thank you for asking!",
    "affirm": "Awesome!",
    "deny": "Alright, let me know if you need anything else.",
    "fallback": "Sorry, I didn’t understand that. \n Can you rephrase?",
    "work": "It should be working now! 😊\n But if you still face any issues, let me know — I’ll escalate it to human support for you.",
   
    }

FOLLOW_UPS = {
    "login_issue": ["Can you confirm your email or username for password reset?"],
    "password_reset": ["Please wait while we process the reset.", "Is there anything else I can help you with?"],
    "still_issue": ["Did you receive any error message?", "Should I escalate this to support?"],
    "issue_not_fixed": ["Can you share your phone type or OS version?", "Do you see any error messages?"],
    "need_human_support": ["Connecting you now...", "Please stay online."],
    "thank_you": ["Happy to help!", "Feel free to ask anything anytime."],
    "goodbye": ["Talk to you soon.", "Hope your issue is resolved."],
    "greet": ["Nice to meet you!", "How can I assist you today?"],
    "how_are_you": ["Hope you're doing well too!", "Let me know how I can assist."],
    "fallback": ["Try asking in a different way.", "I'm learning constantly – help me improve!"]
}

class ActionMultilingualResponse(Action):
    def name(self) -> Text:
        return "action_multilingual_response"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        user_message = tracker.latest_message.get("text", "")
        detected_intent = tracker.latest_message.get("intent", {}).get("name", "")
        lang_code = tracker.get_slot("user_lang") or "hi-IN"
        target_lang = lang_code if lang_code in VALID_LANGS else "hi-IN"

        logger.info(f"[Action] Received: {user_message}")
        logger.info(f"[Action] Intent (initial): {detected_intent}")
        logger.info(f"[Action] Lang: {lang_code}")

        try:
            if detected_intent not in INTENTS:
                logger.info("[Action] Unknown intent – using fallback response.")
                intent = "fallback"
            else:
                intent = detected_intent

            if intent == "password_reset":
                email = tracker.get_slot("user_email")
                if not email or not is_valid_email(email):
                    prompt = INTENTS["password_reset"]
                    translated_prompt = prompt if target_lang == "en-IN" else client.text.translate(
                        input=prompt,
                        source_language_code="auto",
                        target_language_code=target_lang,
                        speaker_gender="Male",
                        mode="classic-colloquial",
                        model="mayura:v1",
                        enable_preprocessing=False
                    ).translated_text or prompt
                    dispatcher.utter_message(text=translated_prompt)
                    return []
                success = send_reset_email(email)
                confirmation = "We’ve sent a password reset link to your email." if success else "Failed to send reset email. Please try again later."
                translated_confirm = confirmation if target_lang == "en-IN" else client.text.translate(
                    input=confirmation,
                    source_language_code="auto",
                    target_language_code=target_lang,
                    speaker_gender="Male",
                    mode="classic-colloquial",
                    model="mayura:v1",
                    enable_preprocessing=False
                ).translated_text or confirmation
                dispatcher.utter_message(text=translated_confirm)
                return []

            if intent == "fallback":
                logger.info("[Fallback] Calling GPT-4o for freeform multilingual help.")

                system_prompt = (
                    "You are MedPlat Shashakti Assistant, helping health workers troubleshoot issues in Hindi, English, or local dialects. "
                    "If you're not sure, escalate to human. Your tone is supportive and clear.You always say """"namaste Shashakti aapke liya ready hai""""!!. You are giving the answer in 5-6 lines not more than that.Always try to reming that you are a helping bot for the system issues not for the general purpose task in polite way but give the answer"
                )

                gpt_reply = openai.ChatCompletion.create(
                    model="gpt-4o-mini",
                    messages=[
                        {"role": "system", "content": system_prompt},
                        {"role": "user", "content": user_message}
                    ],
                    temperature=0.7,
                    max_tokens=300
                )

                openai_reply = gpt_reply.choices[0].message['content'].strip()
                logger.info(f"[OpenAI Fallback Reply] {openai_reply}")

                fallback_payload = {
                    "original_text": user_message,
                    "lang": target_lang,
                    "openai_reply": openai_reply
                }

                try:
                    response = requests.post("http://localhost:5001/fallback_", json=fallback_payload)
                    if response.status_code == 200:
                        logger.info("[Fallback] Successfully sent to /fallback_ endpoint.")
                    else:
                        logger.warning(f"[Fallback] Endpoint /fallback_ returned status {response.status_code}")
                except Exception as e:
                    logger.error(f"[Fallback] Failed to send to /fallback_ endpoint: {e}")

                if target_lang == "en-IN":
                    dispatcher.utter_message(text=openai_reply)
                else:
                    translated_reply = client.text.translate(
                        input=openai_reply,
                        source_language_code="en-IN",
                        target_language_code=target_lang,
                        speaker_gender="Male",
                        mode="classic-colloquial",
                        model="mayura:v1",
                        enable_preprocessing=False
                    ).translated_text or openai_reply
                    dispatcher.utter_message(text=translated_reply)
                return []

            response_text = INTENTS[intent]
            translated_response = response_text if target_lang == "en-IN" else client.text.translate(
                input=response_text,
                source_language_code="auto",
                target_language_code=target_lang,
                speaker_gender="Male",
                mode="classic-colloquial",
                model="mayura:v1",
                enable_preprocessing=False
            ).translated_text or response_text
            dispatcher.utter_message(text=translated_response)

            followups = FOLLOW_UPS.get(intent, [])
            if followups:
                followup = random.choice(followups)
                followup_translation = followup if target_lang == "en-IN" else client.text.translate(
                    input=followup,
                    source_language_code="auto",
                    target_language_code=target_lang,
                    speaker_gender="Male",
                    mode="classic-colloquial",
                    model="mayura:v1",
                    enable_preprocessing=False
                ).translated_text or followup
                dispatcher.utter_message(text=followup_translation)

        except Exception as e:
            logger.error(f"[Action] Error in multilingual response: {e}")
            dispatcher.utter_message(text="Sorry, something went wrong while processing your request.")
        return []







class ActionSendResetEmail(Action):
    def name(self) -> Text:
        return "action_send_reset_email"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        lang_code = tracker.get_slot("user_lang") or "hi-IN"
        target_lang = lang_code if lang_code in VALID_LANGS else "hi-IN"
        email = tracker.get_slot("user_email")

        if not email or not is_valid_email(email):
            message = "Invalid or missing email. Please provide a valid registered email."
        else:
            success = send_reset_email(email)
            message = "We’ve sent a password reset link to your email." if success else "Failed to send reset email. Please try again later."

        try:
            translated_message = message if target_lang == "en-IN" else client.text.translate(
                input=message,
                source_language_code="auto",
                target_language_code=target_lang,
                speaker_gender="Male",
                mode="classic-colloquial",
                model="mayura:v1",
                enable_preprocessing=False
            ).translated_text or message
            dispatcher.utter_message(text=translated_message)
        except Exception as e:
            logger.error(f"[ActionSendResetEmail] Translation or send failed: {e}")
            dispatcher.utter_message(text=message)

        return []
    
    
    
    
 