import React from 'react';
import { FaBell, FaBars, FaRobot } from 'react-icons/fa';

const ChatHeader = ({ onMenuToggle }) => {
  return (
    <div className="flex items-center justify-between bg-gradient-to-r from-blue-500 to-blue-600 text-white p-4 border-b shadow-md">
      <div className="flex items-center space-x-3">
        <button
          className="md:hidden text-white hover:bg-blue-700 p-2 rounded transition"
          onClick={onMenuToggle}
        >
          <FaBars size={20} />
        </button>
        <div className="flex items-center space-x-2">
          <FaRobot size={24} />
          <div>
            <div className="text-lg font-bold">ANM Helper / Field Worker Assistant</div>
            <div className="text-xs text-blue-100">Version 1.0</div>
          </div>
        </div>
      </div>
      <div className="relative">
        <FaBell className="text-white cursor-pointer hover:text-blue-200 transition text-xl" />
        <div className="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
          2
        </div>
      </div>
    </div>
  );
};

export default ChatHeader;
