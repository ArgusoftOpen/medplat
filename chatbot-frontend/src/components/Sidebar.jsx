import React from 'react';
import { FaHome, FaTasks, FaTrash, FaComment } from 'react-icons/fa';
import { Link, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';

const Sidebar = ({ onAction, visible = true }) => {
  const location = useLocation();

  const handleClick = (action) => {
    if (onAction) onAction(action);
  };

  const menuItems = [
    { icon: <FaComment />, label: 'New Chat', action: 'new' },
    { icon: <FaTasks />, label: 'Dashboard', href: '/dashboard' },
    { icon: <FaTrash />, label: 'Clear Chat', action: 'clear' }
  ];

  return (
    <motion.div 
      initial={{ x: -250 }}
      animate={{ x: visible ? 0 : -250 }}
      transition={{ type: 'spring', stiffness: 300, damping: 30 }}
      className={`fixed md:static w-64 h-screen bg-gradient-to-b from-blue-600 to-blue-700 border-r border-blue-800 shadow-lg text-white flex flex-col z-50`}
    >      
      <div className="p-4 border-b border-blue-800">
        <div className="text-xl font-bold flex items-center gap-2">
          <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center text-blue-600 font-bold">A</div>
          ANM Helper
        </div>
        <div className="text-xs text-blue-200 mt-1">Field Worker Assistant</div>
      </div>

      <nav className="flex-1 px-2 space-y-2 py-4">
        {menuItems.map((item, idx) => {
          if (item.href) {
            return (
              <Link
                key={idx}
                to={item.href}
                className={`flex items-center px-3 py-3 rounded-lg transition font-medium ${
                  location.pathname === item.href
                    ? 'bg-white text-blue-600 shadow-md'
                    : 'hover:bg-blue-500 text-white'
                }`}
              >
                {item.icon && <span className="w-5 mr-3 flex justify-center">{item.icon}</span>}
                {item.label}
              </Link>
            );
          }

          return (
            <button
              key={idx}
              onClick={() => handleClick(item.action)}
              className="flex items-center w-full px-3 py-3 rounded-lg hover:bg-blue-500 text-white transition font-medium"
            >
              {item.icon && <span className="w-5 mr-3 flex justify-center">{item.icon}</span>}
              {item.label}
            </button>
          );
        })}
      </nav>

      <div className="p-4 border-t border-blue-800 text-xs text-blue-200">
        <p>Version 1.0</p>
      </div>
    </motion.div>
  );
};

export default Sidebar;

