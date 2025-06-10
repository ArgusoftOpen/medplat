# Get the OpenAI API key from the user and start the app
import os
import sys
import socket

def check_port(port):
    """Check if a port is in use."""
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0

def main():
    print("🤖 MedPlat Support Chatbot Setup")
    print("=================================")
    
    # Check for required dependencies
    try:
        import flask
        import sqlalchemy
        from translate import Translator
        import requests
    except ImportError as e:
        print(f"Missing dependency: {e}")
        print("Please run: pip install -r requirements.txt")
        sys.exit(1)
    
    print("All dependencies are installed! 🚀")
    
    # Get OpenAI API key
    api_key = input("Enter your OpenAI API key (or leave blank to use a simulated API): ")
    if api_key:
        os.environ["OPENAI_API_KEY"] = api_key
    else:
        print("Using simulated API mode...")
    
    # Find an available port
    port = 5000
    while check_port(port) and port < 5100:
        port += 1
    
    if port >= 5100:
        print("Could not find an available port. Please close some applications and try again.")
        sys.exit(1)
    
    # Start the app
    print("Starting MedPlat Support Chatbot...")
    print(f"Open your web browser and navigate to: http://localhost:{port}")
    
    # Import and run the app
    from app import app
    app.run(debug=True, port=port)

if __name__ == "__main__":
    main()