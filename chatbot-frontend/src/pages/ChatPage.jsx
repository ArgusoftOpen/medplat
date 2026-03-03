import React, { useState, useEffect, useContext, useRef } from 'react';
import { motion } from 'framer-motion';
import { FaArrowRight, FaArrowLeft } from 'react-icons/fa';
import Sidebar from '../components/Sidebar';
import ChatHeader from '../components/ChatHeader';
import ChatBubble from '../components/ChatBubble';
import ChatInput from '../components/ChatInput';
import TicketTable from '../components/TicketTable';
import TicketSummaryCard from '../components/TicketSummaryCard';
import { generateTicketId } from '../utils/generateTicketId';
import { TicketContext, LanguageContext } from '../App';

const WELCOME_TEXT = {
  EN: 'Hello! I am the AI ANM Assistant. How can I help you today?',
  HI: 'नमस्ते! मैं AI ANM सहायक हूँ। मैं आपकी कैसे मदद कर सकता हूँ?',
  TE: 'నమస్కారం! నేను AI ANM సహాయకుడు. మీకు ఎలా సహాయం చేయగలను?'
};

const BOT_PROMPTS = {
  askName: {
    EN: 'May I have your name, please?',
    HI: 'कृपया अपना नाम बताएं।',
    TE: 'దయచేసి మీ పేరు చెప్పండి.'
  },
  askIssue: {
    EN: 'What is the main issue or problem you are facing?',
    HI: 'आप किस समस्या का सामना कर रहे हैं?',
    TE: 'మీరు ఎలాంటి సమస్యను ఎదుర్కొంటున్నారు?'
  },
  acknowledgeIssue: (issue) => ({
    EN: `I understand you are experiencing ${issue}. Let me help you with this. Please describe it in more detail.`,
    HI: `मैं समझता हूँ कि आप ${issue} का सामना कर रहे हैं। कृपया विस्तार से बताएं।`,
    TE: `నేను ${issue} గురించి అర్థం చేసుకున్నాను. దయచేసి మరిన్ని వివరాలు ఇవ్వండి.`
  }),
  ticketCreated: (id, name) => ({
    EN: `Thank you ${name}. Your ticket has been created. Ticket ID: ${id}. Our team will review your issue shortly.`,
    HI: `धन्यवाद ${name}। आपका टिकट बनाया गया है। टिकट आईडी: ${id}। हमारी टीम जल्द ही आपके मुद्दे की समीक्षा करेगी।`,
    TE: `ధన్యవాదాలు ${name}. మీ టికెట్ తయారైంది. టికెట్ ID: ${id}. మా టీమ్ త్వరలో మీ సమస్యను సమీక్షిస్తుంది.`
  })
};

const FORM_LABELS = {
  step1: {
    EN: 'Step 1: What is your issue?',
    HI: 'चरण 1: आपकी समस्या क्या है?',
    TE: 'దశ 1: మీ సమస్య ఏమిటి?'
  },
  step2: {
    EN: 'Step 2: Describe your issue in detail',
    HI: 'चरण 2: अपने मुद्दे का विस्तार से विवरण दें',
    TE: 'దశ 2: మీ సమస్యను వివరంగా వర్ణించండి'
  },
  step3: {
    EN: 'Step 3: Confirm and Submit',
    HI: 'चरण 3: पुष्टि करें और सबमिट करें',
    TE: 'దశ 3: నిర్ధారించండి మరియు సమర్పించండి'
  },
  submit: {
    EN: 'Create Ticket',
    HI: 'टिकट बनाएं',
    TE: 'టికెట్ సృష్టించండి'
  },
  next: {
    EN: 'Next',
    HI: 'अगला',
    TE: 'తర్వాత'
  },
  back: {
    EN: 'Back',
    HI: 'पीछे',
    TE: 'వెనుకకు'
  }
};

const STEP_INDICATORS = {
  1: {
    EN: 'Issue Type',
    HI: 'समस्या का प्रकार',
    TE: 'సమస్య రకం'
  },
  2: {
    EN: 'Description',
    HI: 'विवरण',
    TE: 'వివరణ'
  },
  3: {
    EN: 'Confirm',
    HI: 'पुष्टि करें',
    TE: 'నిర్ధారించండి'
  }
};

const ChatPage = () => {
  const [messages, setMessages] = useState([]);
  const [step, setStep] = useState('welcome');
  const [userName, setUserName] = useState('');
  const [currentIssue, setCurrentIssue] = useState('');
  const [issueDescription, setIssueDescription] = useState('');
  const [typing, setTyping] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false); // Start with sidebar closed for main area chat
  const [formStep, setFormStep] = useState(1);
  const [filter, setFilter] = useState('All');
  const [createdTicket, setCreatedTicket] = useState(null);

  const { tickets, addTicket } = useContext(TicketContext);
  const { appLanguage } = useContext(LanguageContext);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    startConversation();
  }, [appLanguage]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const startConversation = () => {
    setMessages([{ sender: 'bot', text: WELCOME_TEXT[appLanguage] }]);
    setUserName('');
    setCurrentIssue('');
    setIssueDescription('');
    setFormStep(1);
    setCreatedTicket(null);
    setStep('name');
  };

  const handleUserMessage = (text) => {
    if (!text.trim()) return;
    
    addMessage('user', text);

    if (step === 'name') {
      setUserName(text);
      botReply(BOT_PROMPTS.askIssue[appLanguage]);
      setStep('issueType');
    } else if (step === 'issueType') {
      setCurrentIssue(text);
      const acknowledgement = BOT_PROMPTS.acknowledgeIssue(text.toLowerCase())[appLanguage];
      botReply(acknowledgement);
      setStep('description');
    } else if (step === 'description') {
      setIssueDescription(text);
      const id = generateTicketId();
      const newTicket = {
        id,
        userName: userName,
        issue: currentIssue,
        description: text,
        status: 'Open',
        date: new Date().toISOString().split('T')[0]
      };
      addTicket(newTicket);
      setCreatedTicket(newTicket);
      const confirmationMsg = BOT_PROMPTS.ticketCreated(id, userName)[appLanguage];
      botReply(confirmationMsg);
      setStep('completed');
    }
  };

  const handleFormSubmit = () => {
    if (formStep === 1 && !currentIssue.trim()) return;
    if (formStep === 2 && !issueDescription.trim()) return;

    if (formStep < 3) {
      setFormStep(formStep + 1);
    } else {
      const id = generateTicketId();
      const newTicket = {
        id,
        userName: userName || 'User',
        issue: currentIssue,
        description: issueDescription,
        status: 'Open',
        date: new Date().toISOString().split('T')[0]
      };
      addTicket(newTicket);
      setCreatedTicket(newTicket);
      setFormStep(1);
      setCurrentIssue('');
      setIssueDescription('');
    }
  };

  const addMessage = (sender, text) => {
    setMessages((prev) => [...prev, { sender, text }]);
  };

  const botReply = (text) => {
    setTyping(true);
    setTimeout(() => {
      addMessage('bot', text);
      setTyping(false);
    }, 1000);
  };

  const handleSidebarAction = (action) => {
    if (action === 'new') {
      startConversation();
    } else if (action === 'clear') {
      setMessages([]);
      setStep('welcome');
    }
  };

  const filteredTickets = tickets.filter(
    t => filter === 'All' || t.status === filter
  );

  const counts = {
    Open: tickets.filter(t => t.status === 'Open').length,
    'In Progress': tickets.filter(t => t.status === 'In Progress').length,
    Closed: tickets.filter(t => t.status === 'Closed').length
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar onAction={handleSidebarAction} visible={sidebarOpen} />
      <div className="flex flex-col flex-1">
        <ChatHeader
          onMenuToggle={() => setSidebarOpen((o) => !o)}
        />

        <div className="flex-1 overflow-y-auto" onClick={() => sidebarOpen && setSidebarOpen(false)}>
          {/* ===== CHAT SECTION ===== */}
          <motion.div 
            className="bg-white p-6 mb-6 border-b-2 border-blue-200"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
          >
            <h2 className="text-2xl font-bold text-blue-600 mb-4">💬 Chat with AI Assistant</h2>
            <div className="bg-gradient-to-b from-gray-50 to-white rounded-lg p-4 h-80 overflow-y-auto border border-gray-200 shadow-md mb-4">
              {messages.length === 0 ? (
                <div className="flex items-center justify-center h-full text-gray-400">
                  Start a conversation...
                </div>
              ) : (
                <>
                  {messages.map((m, idx) => (
                    <ChatBubble key={idx} sender={m.sender} text={m.text} />
                  ))}
                  {typing && (
                    <div className="flex items-center space-x-2 mb-3">
                      <div className="w-3 h-3 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0s' }}></div>
                      <div className="w-3 h-3 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
                      <div className="w-3 h-3 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.4s' }}></div>
                    </div>
                  )}
                  <div ref={messagesEndRef} />
                </>
              )}
            </div>
            <ChatInput onSend={handleUserMessage} language={appLanguage} />
          </motion.div>

          {/* ===== ISSUE REPORTING FORM ===== */}
          <motion.div 
            className="bg-white p-6 mb-6 border-b-2 border-green-200"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
          >
            <h2 className="text-2xl font-bold text-green-600 mb-4">📋 Quick Issue Report</h2>
            
            {createdTicket ? (
              <motion.div 
                className="bg-green-50 border-l-4 border-green-500 p-4 rounded-lg"
                initial={{ scale: 0.95 }}
                animate={{ scale: 1 }}
              >
                <h3 className="text-lg font-semibold text-green-700 mb-2">✅ Ticket Created Successfully!</h3>
                <p className="text-sm text-gray-700 mb-2"><strong>Ticket ID:</strong> {createdTicket.id}</p>
                <p className="text-sm text-gray-700 mb-2"><strong>Issue:</strong> {createdTicket.issue}</p>
                <p className="text-sm text-gray-700"><strong>Status:</strong> {createdTicket.status}</p>
              </motion.div>
            ) : (
              <div>
                <div className="mb-6">
                  {/* Step Indicator Text - VISUALLY PROMINENT */}
                  <div className="flex items-center justify-between gap-3 mb-6">
                    {[1, 2, 3].map((stepNum) => (
                      <div key={stepNum} className="flex-1">
                        <div className="flex items-center gap-2">
                          <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-white transition-all ${
                            formStep >= stepNum ? 'bg-green-500 shadow-lg' : 'bg-gray-400'
                          }`}>
                            {formStep > stepNum ? '✓' : stepNum}
                          </div>
                          <div className="flex-1">
                            <p className={`text-sm font-semibold transition-colors ${
                              formStep === stepNum ? 'text-green-600 text-base' : 'text-gray-600'
                            }`}>
                              {STEP_INDICATORS[stepNum][appLanguage]}
                            </p>
                            {formStep === stepNum && (
                              <p className="text-xs text-green-500 font-bold">← Current</p>
                            )}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>

                  {/* Progress Bar */}
                  <div className="flex gap-2 mb-6">
                    {[1, 2, 3].map(s => (
                      <div 
                        key={s}
                        className={`flex-1 h-3 rounded-full transition-all ${s <= formStep ? 'bg-green-500' : 'bg-gray-300'}`}
                      />
                    ))}
                  </div>
                  
                  {formStep === 1 && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
                      <label className="block text-sm font-semibold text-gray-700 mb-2">
                        {FORM_LABELS.step1[appLanguage]}
                      </label>
                      <input
                        type="text"
                        value={currentIssue}
                        onChange={(e) => setCurrentIssue(e.target.value)}
                        placeholder="e.g., Fever, Headache, Cough..."
                        className="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
                      />
                    </motion.div>
                  )}
                  
                  {formStep === 2 && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
                      <label className="block text-sm font-semibold text-gray-700 mb-2">
                        {FORM_LABELS.step2[appLanguage]}
                      </label>
                      <textarea
                        value={issueDescription}
                        onChange={(e) => setIssueDescription(e.target.value)}
                        placeholder="Describe your issue in detail..."
                        className="w-full border border-gray-300 rounded-lg px-4 py-2 h-32 focus:outline-none focus:ring-2 focus:ring-green-500 resize-none"
                      />
                    </motion.div>
                  )}
                  
                  {formStep === 3 && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-3">
                      <h3 className="text-sm font-semibold text-gray-700">{FORM_LABELS.step3[appLanguage]}</h3>
                      <div className="bg-blue-50 rounded-lg p-4 text-sm">
                        <p className="mb-2"><strong>Issue:</strong> {currentIssue}</p>
                        <p><strong>Description:</strong> {issueDescription}</p>
                      </div>
                    </motion.div>
                  )}
                </div>

                <div className="flex gap-3">
                  {formStep > 1 && (
                    <button
                      onClick={() => setFormStep(formStep - 1)}
                      className="flex items-center gap-2 px-4 py-2 bg-gray-500 hover:bg-gray-600 text-white rounded-lg transition"
                    >
                      <FaArrowLeft size={14} /> {FORM_LABELS.back[appLanguage]}
                    </button>
                  )}
                  <button
                    onClick={handleFormSubmit}
                    className="flex-1 flex items-center justify-center gap-2 px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition font-semibold"
                  >
                    {formStep < 3 ? (
                      <>
                        {FORM_LABELS.next[appLanguage]} <FaArrowRight size={14} />
                      </>
                    ) : (
                      FORM_LABELS.submit[appLanguage]
                    )}
                  </button>
                </div>
              </div>
            )}
          </motion.div>

          {/* ===== TICKET DASHBOARD ===== */}
          <motion.div 
            className="bg-white p-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
          >
            <h2 className="text-2xl font-bold text-blue-600 mb-4">📊 Ticket Dashboard</h2>
            
            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
              <TicketSummaryCard label="Open" count={counts.Open} />
              <TicketSummaryCard label="In Progress" count={counts['In Progress']} />
              <TicketSummaryCard label="Closed" count={counts.Closed} />
            </div>

            {/* Filter */}
            <div className="mb-4 flex gap-2 items-center flex-wrap">
              <label className="font-semibold text-gray-700">Filter by Status:</label>
              <select
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
                className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option>All</option>
                <option>Open</option>
                <option>In Progress</option>
                <option>Closed</option>
              </select>
            </div>

            {/* Table */}
            <TicketTable tickets={filteredTickets} />
          </motion.div>

          <div className="h-6" />
        </div>
      </div>
    </div>
  );
};

export default ChatPage;
