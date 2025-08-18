import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import logging

def send_reset_email(recipient_email: str) -> bool:
    try:
        smtp_server = "smtp-relay.brevo.com"
        smtp_port = 587
        smtp_username = "904c84001@smtp-brevo.com"
        smtp_password = "c4s2WPR91tbAXaKv"  # 🔴 ROTATE THIS IMMEDIATELY after testing

        sender_email ="prishamedplat@gmail.com"
        subject = "Password Reset Request"
        body = f"""
        Hi,

        We received a request to reset your password.
        Please click the link below to reset it:
        https://yourdomain.com/reset-password?email={recipient_email}

        If you did not request this, you can ignore this email.

        Thanks,
        Support Team
        """

        # Prepare email
        message = MIMEMultipart()
        message["From"] = sender_email
        message["To"] = recipient_email
        message["Subject"] = subject
        message.attach(MIMEText(body, "plain"))

        # Send via SMTP
        server = smtplib.SMTP(smtp_server, smtp_port)
        server.starttls()
        server.login(smtp_username, smtp_password)
        server.send_message(message)
        server.quit()

        logging.info(f"Reset email sent to {recipient_email}")
        return True

    except Exception as e:
        logging.error(f"Failed to send reset email to {recipient_email}: {e}")
        return False