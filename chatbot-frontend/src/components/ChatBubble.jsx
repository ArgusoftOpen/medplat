import React from 'react';
import { motion } from 'framer-motion';

const ChatBubble = ({ sender, text }) => {
  const isUser = sender === 'user';
  return (
    <motion.div
      initial={{ opacity: 0, y: 10, x: isUser ? 20 : -20 }}
      animate={{ opacity: 1, y: 0, x: 0 }}
      transition={{ duration: 0.3 }}
      className={`mb-4 flex ${isUser ? 'justify-end' : 'justify-start'}`}
    >
      <div
        className={`max-w-xs px-4 py-3 rounded-lg shadow-md break-words text-sm font-medium ${
          isUser
            ? 'bg-blue-500 text-white rounded-br-none'
            : 'bg-gray-200 text-gray-800 rounded-bl-none'
        }`}
      >
        {text}
      </div>
    </motion.div>
  );
};

export default ChatBubble;