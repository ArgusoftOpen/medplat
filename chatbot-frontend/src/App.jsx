import React, { createContext, useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import ChatPage from './pages/ChatPage';
import DashboardPage from './pages/DashboardPage';

// Create context for ticket management
export const TicketContext = createContext();

// Create context for language management
export const LanguageContext = createContext();

function App() {
  const [tickets, setTickets] = useState([]);
  const [appLanguage, setAppLanguage] = useState('EN');

  const addTicket = (ticket) => {
    setTickets((prev) => [...prev, ticket]);
  };

  return (
    <LanguageContext.Provider value={{ appLanguage, setAppLanguage }}>
      <TicketContext.Provider value={{ tickets, addTicket }}>
        <div className="min-h-screen bg-gradient-to-b from-white to-sky-50 text-gray-800">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/chat" element={<ChatPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
          </Routes>
        </div>
      </TicketContext.Provider>
    </LanguageContext.Provider>
  );
}

export default App;
