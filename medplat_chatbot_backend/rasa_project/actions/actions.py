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
from rasa_sdk.events import SlotSet


# --------------------- Logging ---------------------
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

# --------------------- Load Env ---------------------
env_path = Path(__file__).parent / "ai.env"
if env_path.exists():
    load_dotenv(dotenv_path=env_path)

SARVAM_API_KEY = os.environ.get("SARVAM_API_KEY")
OPENAI_API_KEY = os.environ.get("OPENAI_API_KEY")
FALLBACK_ENDPOINT = os.environ.get("FALLBACK_ENDPOINT", "http://localhost:5001/fallback_")

# --------------------- Initialize Clients ---------------------
client = SarvamAI(api_subscription_key=SARVAM_API_KEY)
openai.api_key = OPENAI_API_KEY

# --------------------- GPT / AI Parameters ---------------------
GPT_MODEL = "gpt-4o-mini"
GPT_TEMPERATURE = 0.7
GPT_MAX_TOKENS = 300
SYSTEM_PROMPT = (
    "You are MedPlat Shashakti Assistant, helping health workers troubleshoot issues in Hindi, English, or local dialects. "
    "If unsure, escalate to human. Your tone is supportive and clear. "
    "Always say 'namaste Shashakti aapke liya ready hai'!! Keep answer concise (5-6 lines)."
)

# --------------------- Supported Languages ---------------------
VALID_LANGS = {
    "bn-IN", "en-IN", "gu-IN", "hi-IN", "kn-IN", "ml-IN", "mr-IN", "od-IN",
    "pa-IN", "ta-IN", "te-IN", "as-IN", "brx-IN", "doi-IN", "kok-IN",
    "ks-IN", "mai-IN", "mni-IN", "ne-IN", "sa-IN", "sat-IN", "sd-IN", "ur-IN"
}

# --------------------- Email Validation ---------------------
def is_valid_email(email: str) -> bool:
    return re.match(r"[^@]+@[^@]+\.[^@]+", email) is not None

# --------------------- Translation Helper ---------------------
def translate(text: str, target_lang: str) -> str:
    if target_lang == "en-IN":
        return text
    try:
        return client.text.translate(
            input=text,
            source_language_code="auto",
            target_language_code=target_lang,
            speaker_gender="Male",
            mode="classic-colloquial",
            model="mayura:v1",
            enable_preprocessing=False
        ).translated_text or text
    except Exception as e:
        logger.error(f"[Translate] Failed for text: {text}, error: {e}")
        return text

# --------------------- Intent Responses ---------------------
INTENTS = {
    "login_issue": "Please try resetting your password. Do you need help?",
    "password_reset": "Please provide your registered email to receive the reset link.",
    "login_issue_still": "Sorry you're still facing login issues. Can you provide more details?",
    "login_followup_device": "Thanks! Which device are you using?",
    "login_followup_error": "Got it. Can you share the error message exactly?",
    "password_reset_not_working": "I see. The reset link didn’t work. Let’s try again or escalate.",
    "escalate_to_support": "Connecting you to a support agent now.",
    "share_additional_details": "Thanks for the info. It will help support resolve the issue faster.",
    "greet": "Hello!",
    "goodbye": "Goodbye! Have a great day!",
    "how_are_you": "I'm doing great, thank you for asking!",
    "affirm": "Awesome!",
    "deny": "Alright, let me know if you need anything else.",
    "thank_you": "You're welcome!",
    "fallback": "Sorry, I didn’t understand that. Can you rephrase?",
    "intent_11": "Namaste! Shashakti aapke liya ready hai!!"
}

FOLLOW_UPS = {
    "login_issue": ["Can you confirm your email or username for password reset?"],
    "password_reset": ["Please wait while we process the reset.", "Is there anything else I can help you with?"],
    "login_issue_still": ["Please provide more details about your login issue."],
    "login_followup_device": ["Thank you! What is your device model?"],
    "login_followup_error": ["Can you share the exact error message?"],
    "password_reset_not_working": ["Let’s try sending the reset link again or escalate."],
    "escalate_to_support": ["Please stay online while I connect you to support."],
    "share_additional_details": ["Thanks! This will help support."],
    "thank_you": ["Happy to help!", "Feel free to ask anything anytime."],
    "goodbye": ["Talk to you soon.", "Hope your issue is resolved."],
    "greet": ["Nice to meet you!", "How can I assist you today?"],
    "how_are_you": ["Hope you're doing well too!", "Let me know how I can assist."],
    "fallback": ["Try asking in a different way.", "I'm learning constantly – help me improve!"]
}

# --------------------- Actions ---------------------
class ActionMultilingualResponse(Action):
    def name(self) -> Text:
        return "action_multilingual_response"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        try:
            user_message = tracker.latest_message.get("text", "")
            detected_intent_data = tracker.latest_message.get("intent", {})
            detected_intent = detected_intent_data.get("name", "")
            confidence = detected_intent_data.get("confidence", 0.0)
            
            logger.info(f"[Action] User message: {user_message}")
            logger.info(f"[Action] Detected intent: {detected_intent} with confidence {confidence}")

            # ----------- Language Handling -----------
            lang_code = tracker.get_slot("user_lang") or "en-IN"
            target_lang = lang_code if lang_code in VALID_LANGS else "en-IN"
            logger.info(f"[Action] User language slot: {lang_code}, using target_lang: {target_lang}")

            # ----------- Determine intent -----------
            if detected_intent == "nlu_fallback" or confidence < 0.95 or detected_intent not in INTENTS:
                intent = "fallback"
            else:
                intent = detected_intent
            logger.info(f"[Action] Final intent used: {intent}")

            # ----------- Password Reset / Email Handling -----------
            if intent in ["password_reset", "provide_email"]:
                email = tracker.get_slot("user_email")
                logger.info(f"[Action] Email slot: {email}")
                if not email or not is_valid_email(email):
                    msg = translate(INTENTS["password_reset"], target_lang)
                    logger.info(f"[Action] Sending response: {msg}")
                    dispatcher.utter_message(text=msg)
                    return []
                success = send_reset_email(email)
                message = "We’ve sent a password reset link to your email." if success else "Failed to send reset email. Please try again later."
                logger.info(f"[Action] Email send status: {success}, sending message: {message}")
                dispatcher.utter_message(text=translate(message, target_lang))
                return []

            # ----------- GPT Fallback -----------
            if intent == "fallback":
                logger.info(f"[Action] Calling OpenAI GPT for fallback response")
                gpt_reply = openai.ChatCompletion.create(
                    model=GPT_MODEL,
                    messages=[{"role": "system", "content": SYSTEM_PROMPT},
                              {"role": "user", "content": user_message}],
                    temperature=GPT_TEMPERATURE,
                    max_tokens=GPT_MAX_TOKENS
                )
                openai_reply = gpt_reply.choices[0].message['content'].strip()
                logger.info(f"[OpenAI Fallback Reply] {openai_reply}")

                try:
                    requests.post(FALLBACK_ENDPOINT, json={"original_text": user_message, "lang": target_lang, "openai_reply": openai_reply})
                    logger.info(f"[Action] Sent fallback to endpoint: {FALLBACK_ENDPOINT}")
                except Exception as e:
                    logger.error(f"[Fallback] Failed to send to endpoint: {e}")

                dispatcher.utter_message(text=translate(openai_reply, target_lang))
                return []

            # ----------- Standard Intent Response -----------
            response_text = translate(INTENTS[intent], target_lang)
            logger.info(f"[Action] Sending response: {response_text}")
            dispatcher.utter_message(text=response_text)

            followups = FOLLOW_UPS.get(intent, [])
            if followups:
                followup_text = translate(random.choice(followups), target_lang)
                logger.info(f"[Action] Sending follow-up: {followup_text}")
                dispatcher.utter_message(text=followup_text)

        except Exception as e:
            logger.error(f"[Action] Error: {e}")
            dispatcher.utter_message(text=translate("Sorry, something went wrong while processing your request.", target_lang))

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

        dispatcher.utter_message(text=translate(message, target_lang))
        return []
