import React, { useContext } from 'react'
import { motion } from 'framer-motion'
import { useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import { LanguageContext } from '../App'
import { FaComments, FaTicketAlt, FaHeadset } from 'react-icons/fa'

const HOME_CONTENT = {
  title: {
    EN: 'Field Worker Support System',
    HI: 'फील्ड वर्कर सहायता प्रणाली',
    TE: 'ఫీల్డ్ వర్కర్ సపోర్ట్ सिस्टम'
  },
  subtitle: {
    EN: 'Get instant AI-powered assistance for your field work challenges. Report issues, track tickets, and receive support in your preferred language.',
    HI: 'अपनी फील्ड वर्क चुनौतियों के लिए तत्काल AI-संचालित सहायता प्राप्त करें। समस्याएं रिपोर्ट करें, टिकट ट्रैक करें, और अपनी पसंदीदा भाषा में समर्थन प्राप्त करें।',
    TE: 'मी फील्ड वर्क सवालांसाठी त्वरित AI-शक्तीची मदत मिळवा. समस्या नोंदवा, टिकट ट्रैक करा आणि तुमच्या पसंतीच्या भाषेत समर्थन मिळवा.'
  },
  whyChoose: {
    EN: 'Why Choose Our Platform?',
    HI: 'हमारे प्लेटफॉर्म को क्यों चुनें?',
    TE: 'मन प्लॅटफॉर्म का निवड?'
  },
  features: {
    aiChat: {
      EN: 'AI Chat Support',
      HI: 'AI चैट समर्थन',
      TE: 'AI चॅट समर्थन'
    },
    aiChatDesc: {
      EN: 'Chat with our AI ANM Assistant to report issues and get real-time assistance.',
      HI: 'समस्याएं रिपोर्ट करने और तत्काल सहायता प्राप्त करने के लिए हमारे AI ANM सहायक के साथ चैट करें।',
      TE: 'समस्या नोंदवण्यासाठी आणि रिअल-टाइम मदत मिळवण्यासाठी आमच्या AI ANM सहाय्यकाशी चॅट करा.'
    },
    ticketMgmt: {
      EN: 'Ticket Management',
      HI: 'टिकट प्रबंधन',
      TE: 'टिकट व्यवस्थापन'
    },
    ticketMgmtDesc: {
      EN: 'Create, track, and manage support tickets with step-by-step guidance.',
      HI: 'चरण-दर-चरण मार्गदर्शन के साथ समर्थन टिकट बनाएं, ट्रैक करें और प्रबंधित करें।',
      TE: 'चरण-दर-चरण मार्गदर्शनासह सपोर्ट टिकट तयार करा, ट्रैक करा आणि व्यवस्थापित करा.'
    },
    instantSupport: {
      EN: 'Instant Support',
      HI: 'तत्काल समर्थन',
      TE: 'त्वरित समर्थन'
    },
    instantSupportDesc: {
      EN: 'Get quick responses in English, Hindi, or Telugu.',
      HI: 'अंग्रेजी, हिंदी या तेलुगु में त्वरित प्रतिक्रिया प्राप्त करें।',
      TE: 'इंग्रजी, हिंदी किंवा तेलुगूमध्ये त्वरित प्रतिक्रिया मिळवा.'
    }
  },
  howItWorks: {
    EN: 'How It Works',
    HI: 'यह कैसे काम करता है',
    TE: 'हे कसे कार्य करते'
  },
  steps: {
    EN: [
      { title: 'Start Chat', desc: 'Begin conversation with AI' },
      { title: 'Report Issue', desc: 'Describe your problem' },
      { title: 'Get Ticket ID', desc: 'Receive ticket confirmation' },
      { title: 'Track Status', desc: 'Monitor your ticket' }
    ],
    HI: [
      { title: 'चैट शुरू करें', desc: 'AI के साथ बातचीत शुरू करें' },
      { title: 'समस्या रिपोर्ट करें', desc: 'अपनी समस्या का वर्णन करें' },
      { title: 'टिकट ID प्राप्त करें', desc: 'टिकट की पुष्टि प्राप्त करें' },
      { title: 'स्थिति ट्रैक करें', desc: 'अपना टिकट मॉनिटर करें' }
    ],
    TE: [
      { title: 'चॅट सुरू करा', desc: 'AI शी संवाद सुरू करा' },
      { title: 'समस्या नोंदवा', desc: 'तुमची समस्या वर्णन करा' },
      { title: 'टिकट ID मिळवा', desc: 'टिकट पुष्टी मिळवा' },
      { title: 'स्थिती ट्रैक करा', desc: 'तुमचा टिकट मॉनिटर करा' }
    ]
  },
  multilingualSupport: {
    EN: 'Multilingual Support',
    HI: 'बहुभाषी समर्थन',
    TE: 'बहुभाषिक समर्थन'
  },
  ready: {
    EN: 'Ready to Get Support?',
    HI: 'समर्थन प्राप्त करने के लिए तैयार?',
    TE: 'समर्थन मिळण्यास तयार?'
  },
  readyDesc: {
    EN: 'Click below to start your chat session now',
    HI: 'अभी अपना चैट सत्र शुरू करने के लिए नीचे क्लिक करें',
    TE: 'आता तुमचा चॅट सत्र सुरू करण्यासाठी खाली क्लिक करा'
  },
  startChat: {
    EN: '💬 Start Chat Now',
    HI: '💬 अब चैट शुरू करें',
    TE: '💬 आता चॅट सुरू करा'
  },
  viewTickets: {
    EN: '📊 View Tickets',
    HI: '📊 टिकट देखें',
    TE: '📊 टिकट पहा'
  },
  launchChat: {
    EN: '🚀 Launch Chat Now',
    HI: '🚀 अब चैट लॉन्च करें',
    TE: '🚀 आता चॅट लॉन्च करा'
  },
  footer: {
    EN: 'Field Worker Support System',
    HI: 'फील्ड वर्कर सहायता प्रणाली',
    TE: 'फील्ड वर्कर समर्थन सिस्टम'
  },
  version: {
    EN: 'Version 1.0 | Powered by AI ANM Assistant',
    HI: 'संस्करण 1.0 | AI ANM सहायक द्वारा संचालित',
    TE: 'आवृत्ती 1.0 | AI ANM सहाय्यकाद्वारे चालित'
  },
  copyright: {
    EN: '© 2026 All Rights Reserved',
    HI: '© 2026 सर्वाधिकार सुरक्षित',
    TE: '© 2026 सर्व हक्क राखीव'
  }
}

export default function Home(){
  const navigate = useNavigate()
  const { appLanguage } = useContext(LanguageContext)

  const features = [
    {
      icon: <FaComments className="text-4xl text-blue-500" />,
      title: HOME_CONTENT.features.aiChat[appLanguage],
      description: HOME_CONTENT.features.aiChatDesc[appLanguage]
    },
    {
      icon: <FaTicketAlt className="text-4xl text-green-500" />,
      title: HOME_CONTENT.features.ticketMgmt[appLanguage],
      description: HOME_CONTENT.features.ticketMgmtDesc[appLanguage]
    },
    {
      icon: <FaHeadset className="text-4xl text-purple-500" />,
      title: HOME_CONTENT.features.instantSupport[appLanguage],
      description: HOME_CONTENT.features.instantSupportDesc[appLanguage]
    },
  ]

  return (
    <div className="min-h-screen bg-gradient-to-b from-white via-blue-50 to-white">
      <Navbar />

      <main className="container mx-auto px-4 py-16">
        {/* Hero Section */}
        <section className="text-center mb-20">
          <motion.h1
            initial={{ opacity: 0, y: -30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="text-5xl md:text-6xl font-extrabold text-gray-900 mb-6"
          >
            {HOME_CONTENT.title[appLanguage]}
          </motion.h1>
          
          <motion.p
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2, duration: 0.6 }}
            className="text-xl md:text-2xl text-gray-700 max-w-3xl mx-auto mb-8 font-medium"
          >
            {HOME_CONTENT.subtitle[appLanguage]}
          </motion.p>

          <motion.div 
            className="flex flex-col sm:flex-row items-center justify-center gap-6 mt-12"
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.4, duration: 0.6 }}
          >
            <motion.button 
              whileHover={{ scale: 1.08, boxShadow: '0 20px 40px rgba(59, 130, 246, 0.3)' }} 
              whileTap={{ scale: 0.96 }}
              onClick={() => navigate('/chat')}
              className="bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white px-10 py-4 rounded-lg shadow-lg font-bold text-lg transition-all"
            >
              {HOME_CONTENT.startChat[appLanguage]}
            </motion.button>
            
            <motion.button 
              whileHover={{ scale: 1.08, boxShadow: '0 20px 40px rgba(16, 185, 129, 0.3)' }} 
              whileTap={{ scale: 0.96 }}
              onClick={() => navigate('/chat')}
              className="bg-gradient-to-r from-green-600 to-green-700 hover:from-green-700 hover:to-green-800 text-white px-10 py-4 rounded-lg shadow-lg font-bold text-lg transition-all"
            >
              {HOME_CONTENT.viewTickets[appLanguage]}
            </motion.button>
          </motion.div>
        </section>

        {/* Features Section */}
        <section className="mb-20">
          <motion.h2 
            className="text-4xl font-bold text-center text-gray-900 mb-12"
            initial={{ opacity: 0, y: -20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
          >
            {HOME_CONTENT.whyChoose[appLanguage]}
          </motion.h2>

          <motion.div 
            className="grid grid-cols-1 md:grid-cols-3 gap-8"
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
            transition={{ staggerChildren: 0.2, delayChildren: 0.1 }}
          >
            {features.map((feature, idx) => (
              <motion.div 
                key={idx}
                className="bg-white rounded-lg shadow-lg p-8 text-center hover:shadow-2xl transition-shadow"
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6 }}
              >
                <div className="mb-4 flex justify-center">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-bold text-gray-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600">
                  {feature.description}
                </p>
              </motion.div>
            ))}
          </motion.div>
        </section>

        {/* How It Works */}
        <section className="mb-20 bg-blue-50 rounded-xl p-12">
          <motion.h2 
            className="text-4xl font-bold text-center text-gray-900 mb-12"
            initial={{ opacity: 0, y: -20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
          >
            {HOME_CONTENT.howItWorks[appLanguage]}
          </motion.h2>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            {HOME_CONTENT.steps[appLanguage].map((step, idx) => {
              const stepNum = String(idx + 1)
              return (
                <motion.div 
                  key={idx}
                  className="text-center"
                  initial={{ opacity: 0, scale: 0.8 }}
                  whileInView={{ opacity: 1, scale: 1 }}
                  viewport={{ once: true }}
                  transition={{ delay: idx * 0.1 }}
                >
                  <div className="bg-gradient-to-br from-blue-600 to-blue-700 text-white rounded-full w-16 h-16 flex items-center justify-center text-2xl font-bold mx-auto mb-4 shadow-lg">
                    {stepNum}
                  </div>
                  <h3 className="font-bold text-gray-900 mb-2">{step.title}</h3>
                  <p className="text-sm text-gray-700">{step.desc}</p>
                </motion.div>
              )
            })}
          </div>
        </section>

        {/* Languages Supported */}
        <section className="text-center mb-20">
          <motion.h2 
            className="text-4xl font-bold text-gray-900 mb-8"
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
          >
            {HOME_CONTENT.multilingualSupport[appLanguage]}
          </motion.h2>

          <motion.div 
            className="flex justify-center items-center gap-8 md:gap-16 flex-wrap"
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
            transition={{ staggerChildren: 0.1 }}
          >
            {[
              { flag: '🇱🇷', lang: 'English' },
              { flag: '🇮🇳', lang: 'हिंदी' },
              { flag: '🇹🇱', lang: 'తెలుగు' }
            ].map((item, idx) => (
              <motion.div 
                key={idx}
                className="text-center"
                initial={{ opacity: 0, scale: 0.8 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                transition={{ delay: idx * 0.1 }}
              >
                <div className="text-5xl mb-2">{item.flag}</div>
                <p className="text-lg font-semibold text-gray-800">{item.lang}</p>
              </motion.div>
            ))}
          </motion.div>
        </section>

        {/* CTA Section */}
        <section className="bg-gradient-to-r from-blue-600 to-green-600 rounded-xl p-12 text-center text-white mb-20">
          <motion.h2 
            className="text-4xl font-bold mb-4"
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
          >
            {HOME_CONTENT.ready[appLanguage]}
          </motion.h2>
          <motion.p 
            className="text-xl mb-8 opacity-90"
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
            transition={{ delay: 0.1 }}
          >
            {HOME_CONTENT.readyDesc[appLanguage]}
          </motion.p>
          <motion.button 
            whileHover={{ scale: 1.1 }} 
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/chat')}
            className="bg-white text-blue-600 font-bold px-10 py-4 rounded-lg shadow-xl hover:shadow-2xl transition-all text-lg"
          >
            {HOME_CONTENT.launchChat[appLanguage]}
          </motion.button>
        </section>
      </main>

      {/* Footer */}
      <footer className="bg-gray-900 text-white text-center py-8">
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
        >
          <p className="text-gray-400 mb-2">{HOME_CONTENT.footer[appLanguage]}</p>
          <p className="text-gray-500 text-sm">{HOME_CONTENT.version[appLanguage]}</p>
          <p className="text-gray-600 text-xs mt-4">{HOME_CONTENT.copyright[appLanguage]}</p>
        </motion.div>
      </footer>
    </div>
  )
}

