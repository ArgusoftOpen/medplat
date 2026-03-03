import React from 'react';
import { motion } from 'framer-motion';

const TicketSummaryCard = ({ label, count }) => {
  const colors = {
    'Open': 'from-yellow-500 to-orange-500',
    'In Progress': 'from-blue-500 to-blue-600',
    'Closed': 'from-green-500 to-emerald-500'
  };

  const bgColor = colors[label] || 'from-gray-500 to-gray-600';

  return (
    <motion.div 
      className={`flex-1 bg-gradient-to-br ${bgColor} p-6 rounded-lg shadow-lg text-white text-center min-w-[150px]`}
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      whileHover={{ scale: 1.05 }}
      transition={{ duration: 0.3 }}
    >
      <div className="text-4xl font-bold mb-2">{count}</div>
      <div className="text-sm font-semibold opacity-90">{label} Tickets</div>
    </motion.div>
  );
};

export default TicketSummaryCard;