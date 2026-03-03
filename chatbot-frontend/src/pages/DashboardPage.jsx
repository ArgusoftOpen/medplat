import React, { useContext, useState, useMemo } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { FaArrowLeft } from 'react-icons/fa';
import { TicketContext } from '../App';
import Sidebar from '../components/Sidebar';
import TicketSummaryCard from '../components/TicketSummaryCard';
import TicketTable from '../components/TicketTable';

const DashboardPage = () => {
  const { tickets } = useContext(TicketContext);
  const [filter, setFilter] = useState('All');
  const [search, setSearch] = useState('');
  const navigate = useNavigate();

  const filteredTickets = useMemo(() => {
    return tickets.filter((t) => {
      const matchesStatus = filter === 'All' || t.status === filter;
      const matchesSearch =
        t.id.toLowerCase().includes(search.toLowerCase()) ||
        t.issue.toLowerCase().includes(search.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }, [tickets, filter, search]);

  const counts = useMemo(() => {
    const c = { Open: 0, 'In Progress': 0, Closed: 0 };
    tickets.forEach((t) => {
      if (c[t.status] !== undefined) c[t.status]++;
    });
    return c;
  }, [tickets]);

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <div className="bg-white border-b shadow-sm p-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <button
                onClick={() => navigate('/chat')}
                className="p-2 hover:bg-gray-100 rounded-lg transition text-gray-600"
              >
                <FaArrowLeft size={20} />
              </button>
              <h1 className="text-3xl font-bold text-gray-800">Ticket Dashboard</h1>
            </div>
            <div className="text-sm text-gray-500">Total Tickets: {tickets.length}</div>
          </div>
        </div>

        {/* Main Content */}
        <div className="flex-1 overflow-auto p-6">
          {/* Summary Cards */}
          <motion.div 
            className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ staggerChildren: 0.1 }}
          >
            <TicketSummaryCard label="Open" count={counts.Open} />
            <TicketSummaryCard label="In Progress" count={counts['In Progress']} />
            <TicketSummaryCard label="Closed" count={counts.Closed} />
          </motion.div>

          {/* Search and Filter */}
          <motion.div 
            className="mb-6 flex flex-col md:flex-row gap-4"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2 }}
          >
            <input
              type="text"
              placeholder="Search by ID or issue..."
              className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <select
              className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              <option>All</option>
              <option>Open</option>
              <option>In Progress</option>
              <option>Closed</option>
            </select>
          </motion.div>

          {/* Tickets Table */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
          >
            <TicketTable tickets={filteredTickets} />
          </motion.div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
