import React from 'react';
import { motion } from 'framer-motion';

const StatusBadge = ({ status }) => {
  const statusStyles = {
    'Open': {
      bg: 'bg-gradient-to-r from-green-100 to-green-50',
      text: 'text-green-800',
      icon: '🟢'
    },
    'In Progress': {
      bg: 'bg-gradient-to-r from-orange-100 to-orange-50',
      text: 'text-orange-800',
      icon: '🟠'
    },
    'Closed': {
      bg: 'bg-gradient-to-r from-gray-100 to-gray-50',
      text: 'text-gray-800',
      icon: '⚪'
    },
    'Resolved': {
      bg: 'bg-gradient-to-r from-green-100 to-green-50',
      text: 'text-green-800',
      icon: '✅'
    }
  };

  const style = statusStyles[status] || statusStyles['Closed'];

  return (
    <span className={`inline-flex items-center gap-2 px-4 py-2 rounded-full text-sm font-bold border-2 border-current ${style.bg} ${style.text}`}>
      {style.icon} {status}
    </span>
  );
};

const TicketTable = ({ tickets }) => {
  return (
    <div className="overflow-auto bg-white rounded-lg shadow-md">
      <table className="w-full text-left">
        <thead className="bg-gradient-to-r from-blue-500 to-blue-600 text-white">
          <tr>
            <th className="px-4 py-3 font-semibold">Ticket ID</th>
            <th className="px-4 py-3 font-semibold">User Name</th>
            <th className="px-4 py-3 font-semibold">Issue</th>
            <th className="px-4 py-3 font-semibold">Status</th>
            <th className="px-4 py-3 font-semibold">Date</th>
          </tr>
        </thead>
        <tbody>
          {tickets.map((t, idx) => (
            <motion.tr 
              key={t.id} 
              className="border-t hover:bg-gray-50 transition"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: idx * 0.05 }}
            >
              <td className="px-4 py-3 font-mono text-sm text-blue-600 font-semibold">{t.id}</td>
              <td className="px-4 py-3 font-medium text-gray-700">{t.userName || 'N/A'}</td>
              <td className="px-4 py-3">{t.issue}</td>
              <td className="px-4 py-3"><StatusBadge status={t.status} /></td>
              <td className="px-4 py-3 text-sm text-gray-600">{t.date}</td>
            </motion.tr>
          ))}
          {tickets.length === 0 && (
            <tr>
              <td colSpan={5} className="px-4 py-8 text-center text-gray-500">
                No tickets found. Start a chat to create one!
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default TicketTable;
