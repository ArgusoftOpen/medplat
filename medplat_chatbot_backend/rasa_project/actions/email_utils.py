import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import logging
import os
from dotenv import load_dotenv

# Load variables from ai.env
load_dotenv(dotenv_path="ai.env")

SMTP_SERVER = os.getenv("SMTP_SERVER")
SMTP_PORT = int(os.getenv("SMTP_PORT", 587))
SMTP_USERNAME = os.getenv("SMTP_USERNAME")
SMTP_PASSWORD = os.getenv("SMTP_PASSWORD")
SENDER_EMAIL = os.getenv("SENDER_EMAIL")
RESET_PASSWORD_URL = os.getenv("RESET_PASSWORD_URL")


def send_reset_email(recipient_email: str) -> bool:
    """
    Sends a password reset email to the specified recipient.
    Returns True if successful, False otherwise.
    """
    try:
        subject = "Password Reset Request"
        body = f"""
Hi,

We received a request to reset your password.
Please click the link below to reset it:
{RESET_PASSWORD_URL}?email={recipient_email}

If you did not request this, you can ignore this email.

Thanks,
Support Team
        """

        # Prepare email
        message = MIMEMultipart()
        message["From"] = SENDER_EMAIL
        message["To"] = recipient_email
        message["Subject"] = subject
        message.attach(MIMEText(body, "plain"))

        # Send via SMTP
        with smtplib.SMTP(SMTP_SERVER, SMTP_PORT) as server:
            server.starttls()
            server.login(SMTP_USERNAME, SMTP_PASSWORD)
            server.send_message(message)

        logging.info(f"Reset email sent to {recipient_email}")
        return True

    except Exception as e:
        logging.error(f"Failed to send reset email to {recipient_email}: {e}")
        return False
