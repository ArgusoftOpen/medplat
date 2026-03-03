import React, { useState } from 'react';
import { FaPaperclip, FaPaperPlane } from 'react-icons/fa';

const placeholders = {
  EN: 'Type your message...',
  HI: 'अपना संदेश लिखें...',
  TE: 'మీ సందేశం టైప్ చేయండి...'
};

const ChatInput = ({ onSend, language }) => {
  const [text, setText] = useState('');

  const handleSend = () => {
    if (text.trim() === '') return;
    onSend(text.trim());
    setText('');
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="flex items-end gap-3 p-4 bg-white border-t border-gray-200 shadow-lg">
      <button className="text-blue-500 hover:text-blue-600 transition text-xl">
        <FaPaperclip />
      </button>
      <div className="flex-1 relative">
        <textarea
          className="w-full border border-gray-300 rounded-lg px-4 py-2 resize-none focus:outline-none focus:ring-2 focus:ring-blue-500 min-h-[40px] max-h-[120px]"
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder={placeholders[language] || placeholders.EN}
          rows="1"
        />
      </div>
      <button
        onClick={handleSend}
        className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg transition font-medium flex items-center gap-2"
      >
        <FaPaperPlane size={16} />
        Send
      </button>
    </div>
  );
};

export default ChatInput;