import React, { useState, useContext } from 'react'
import { motion } from 'framer-motion'
import { HiOutlineMenu, HiOutlineX } from 'react-icons/hi'
import { useNavigate } from 'react-router-dom'
import { LanguageContext } from '../App'

export default function Navbar(){
  const [open, setOpen] = useState(false)
  const navigate = useNavigate()
  const { appLanguage, setAppLanguage } = useContext(LanguageContext)

  return (
    <nav className="bg-gradient-to-r from-blue-600 to-blue-700 shadow-lg sticky top-0 z-50">
      <div className="container mx-auto px-4 py-5 flex items-center justify-between">
        {/* Logo Section */}
        <motion.div 
          className="flex items-center gap-3 cursor-pointer"
          onClick={() => navigate('/')}
          whileHover={{ scale: 1.05 }}
        >
          <div className="w-12 h-12 bg-white rounded-lg flex items-center justify-center text-blue-600 font-bold text-xl shadow-lg">
            🤖
          </div>
          <div>
            <div className="font-bold text-white text-lg">ANM Assistant</div>
            <div className="text-blue-100 text-xs font-semibold">Field Worker Support</div>
          </div>
        </motion.div>

        {/* Desktop Links */}
        <div className="hidden md:flex items-center gap-8">
          <motion.button 
            whileHover={{ y: -3 }} 
            onClick={() => navigate('/chat')}
            className="text-white hover:text-blue-100 transition font-semibold flex items-center gap-2"
          >
            💬 Chat
          </motion.button>
          <motion.button 
            whileHover={{ y: -3 }} 
            onClick={() => navigate('/chat')}
            className="text-white hover:text-blue-100 transition font-semibold flex items-center gap-2"
          >
            📊 Tickets
          </motion.button>
          <motion.button 
            whileHover={{ y: -3 }} 
            className="text-white hover:text-blue-100 transition font-semibold"
          >
            ℹ️ About
          </motion.button>
        </div>

        {/* Desktop Actions */}
        <div className="hidden md:flex items-center gap-6">
          {/* Notification Bell */}
          <motion.div 
            className="relative cursor-pointer"
            whileHover={{ scale: 1.1 }}
          >
            <button className="text-white text-2xl hover:text-blue-100 transition">
              🔔
            </button>
            <motion.div 
              className="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold shadow-lg"
              animate={{ pulse: [1, 1.2, 1] }}
              transition={{ duration: 2, repeat: Infinity }}
            >
              3
            </motion.div>
          </motion.div>

          {/* Language Selector - HIGHLY VISIBLE */}
          <select 
            value={appLanguage}
            onChange={(e) => setAppLanguage(e.target.value)}
            className="bg-white text-blue-700 px-4 py-2 rounded-lg font-bold text-sm cursor-pointer hover:shadow-xl transition shadow-md border-2 border-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="EN">🇱🇷 English</option>
            <option value="HI">🇮🇳 हिंदी</option>
            <option value="TE">🇹🇱 তেলুগু</option>
          </select>
        </div>

        {/* Mobile Menu Button */}
        <div className="md:hidden flex items-center gap-4">
          {/* Mobile Notifications */}
          <motion.div className="relative" whileHover={{ scale: 1.1 }}>
            <button className="text-white text-2xl hover:text-blue-100">
              🔔
            </button>
            <div className="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center text-xs font-bold">3</div>
          </motion.div>

          {/* Mobile Language - HIGHLY VISIBLE */}
          <select 
            value={appLanguage}
            onChange={(e) => setAppLanguage(e.target.value)}
            className="bg-white text-blue-700 px-2 py-1 rounded font-bold text-xs cursor-pointer border-2 border-blue-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="EN">🇱🇷 EN</option>
            <option value="HI">🇮🇳 HI</option>
            <option value="TE">🇹🇱 TE</option>
          </select>

          {/* Mobile Menu Toggle */}
          <button onClick={()=>setOpen(v=>!v)} aria-label="menu" className="text-white text-2xl">
            {open ? <HiOutlineX /> : <HiOutlineMenu />}
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      <motion.div 
        initial={{ height: 0 }} 
        animate={{ height: open ? 'auto' : 0 }} 
        className="md:hidden overflow-hidden bg-blue-700 border-t border-blue-600"
      >
        <div className="px-4 pb-4 flex flex-col gap-3">
          <motion.button 
            onClick={() => { navigate('/chat'); setOpen(false); }}
            className="text-white py-3 border-b border-blue-600 text-left font-semibold hover:text-blue-200 transition flex items-center gap-2"
          >
            💬 Chat with AI
          </motion.button>
          <motion.button 
            onClick={() => { navigate('/chat'); setOpen(false); }}
            className="text-white py-3 border-b border-blue-600 text-left font-semibold hover:text-blue-200 transition flex items-center gap-2"
          >
            📊 View Tickets
          </motion.button>
          <motion.button 
            className="text-white py-3 text-left font-semibold hover:text-blue-200 transition"
          >
            ℹ️ About
          </motion.button>
        </div>
      </motion.div>
    </nav>
  )
}
