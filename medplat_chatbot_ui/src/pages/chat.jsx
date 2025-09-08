import React, { useState, useEffect, useRef } from "react";
import { Mic, Send, Camera } from "lucide-react";
import "./assets/ChatApp.css"; 

export default function ChatApp() {
  // ------------------- State -------------------
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [buttons, setButtons] = useState([]);
  const [selectedLang, setSelectedLang] = useState("en-US");

  const chatRef = useRef(null);
  const fileInputRef = useRef();

  const API_BASE = import.meta.env.VITE_CHAT_API || "http://localhost:3000";

  // ------------------- Utilities -------------------
  const addMessage = (msg) => setMessages((prev) => [...prev, msg]);
  const scrollToBottom = () => chatRef.current?.scrollIntoView({ behavior: "smooth" });

  // ------------------- Send Message -------------------
  const handleSend = async () => {
    if (!message.trim()) return;

    const userMsg = {
      id: Date.now(),
      type: "user",
      content: message,
      timestamp: new Date().toLocaleTimeString(),
    };
    addMessage(userMsg);
    setMessage("");
    setButtons([]);

    try {
      const res = await fetch(`${API_BASE}/send_message`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message }),
      });

      const botData = await res.json();

      botData.forEach((botMsg, index) => {
        const aiMsg = {
          id: Date.now() + index + 1,
          type: "ai",
          content: botMsg.text || "",
          timestamp: new Date().toLocaleTimeString(),
        };
        addMessage(aiMsg);
        if (botMsg.buttons) setButtons(botMsg.buttons);
      });
    } catch (err) {
      const errorMsg = {
        id: Date.now(),
        type: "ai",
        content: "⚠️ Failed to connect to the bot server.",
        timestamp: new Date().toLocaleTimeString(),
      };
      addMessage(errorMsg);
    }
  };

  // ------------------- OCR Handling -------------------
  const handleImageCapture = () => fileInputRef.current.click();

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onloadend = async () => {
      const base64Image = reader.result.split(",")[1];

      addMessage({
        id: Date.now(),
        type: "ai",
        content: "Reading the image... Please wait.",
        timestamp: new Date().toLocaleTimeString(),
      });

      try {
        const res = await fetch(`${API_BASE}/process_ocr`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ image: base64Image }),
        });

        const data = await res.json();

        if (data.text) {
          const ocrMsg = {
            id: Date.now(),
            type: "user",
            content: data.text,
            timestamp: new Date().toLocaleTimeString(),
          };
          addMessage(ocrMsg);

          // Automatically send OCR text to bot
          setMessage(data.text);
          setButtons([]);
          await handleSend();
        }
      } catch {
        const errorMsg = {
          id: Date.now(),
          type: "ai",
          content: "⚠️ Unable to reach the bot server.",
          timestamp: new Date().toLocaleTimeString(),
        };
        addMessage(errorMsg);
      }
    };

    reader.readAsDataURL(file);
  };

  // ------------------- Voice Input -------------------
  const handleMic = () => {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) return alert("Speech Recognition not supported.");

    const recognition = new SpeechRecognition();
    recognition.lang = selectedLang;
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.onresult = (event) => {
      const voiceMessage = event.results[0][0].transcript;
      setMessage(voiceMessage);
      setTimeout(() => handleSend(), 300); // auto-send after recognition
    };

    recognition.start();
  };

  // ------------------- Auto-scroll -------------------
  useEffect(scrollToBottom, [messages]);

  // ------------------- Render -------------------
  return (
    <div className="chat-container">
      {/* Header */}
      <div className="chat-header">
        <h1>MedPlat</h1>
        <span className="status">● Online</span>
      </div>

      {/* Chat Body */}
      <div className="chat-body">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`message-wrapper ${msg.type === "user" ? "user" : "ai"}`}
          >
            <div className="message-bubble">
              <div className="message-content">{msg.content}</div>
              <div className="message-time">{msg.timestamp}</div>
            </div>
          </div>
        ))}

        {/* Suggestion Buttons */}
        {buttons.length > 0 && (
          <div className="button-group">
            {buttons.map((btn, idx) => (
              <button
                key={idx}
                className="suggestion-btn"
                onClick={() => {
                  setMessage(btn.payload);
                  setTimeout(() => handleSend(), 300);
                }}
              >
                {btn.title}
              </button>
            ))}
          </div>
        )}
        <div ref={chatRef}></div>
      </div>

      {/* Input Section */}
      <div className="chat-input">
        <input
          type="text"
          placeholder="Type your message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSend()}
        />

        <button className="send-btn" onClick={handleSend}><Send size={18} /></button>
        <button className="mic-btn" onClick={handleMic}><Mic size={18} /></button>
        <button className="camera-btn" onClick={handleImageCapture}><Camera size={18} /></button>

        <input
          type="file"
          accept="image/*"
          capture="environment"
          ref={fileInputRef}
          style={{ display: "none" }}
          onChange={handleImageChange}
        />

        <select
          value={selectedLang}
          onChange={(e) => setSelectedLang(e.target.value)}
          className="lang-select"
        >
          <option value="en-US">English</option>
          <option value="hi-IN">Hindi</option>
          <option value="bn-IN">Bengali</option>
          <option value="gu-IN">Gujarati</option>
          <option value="ta-IN">Tamil</option>
          <option value="te-IN">Telugu</option>
          <option value="mr-IN">Marathi</option>
          <option value="kn-IN">Kannada</option>
          <option value="ml-IN">Malayalam</option>
          <option value="or-IN">Odia</option>
          <option value="pa-IN">Punjabi</option>
        </select>
      </div>
    </div>
  );
}
