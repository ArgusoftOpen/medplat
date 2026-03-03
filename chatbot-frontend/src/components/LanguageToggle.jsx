import React from 'react';
import { FaGlobeAmericas } from 'react-icons/fa';

const languages = [
  { code: 'EN', label: 'English', flag: '🇱🇷' },
  { code: 'HI', label: 'हिन्दी', flag: '🇮🇳' },
  { code: 'TE', label: 'తెలుగు', flag: '🇹🇱' }
];

const LanguageToggle = ({ language, setLanguage }) => {
  const currentLang = languages.find(l => l.code === language) || languages[0];

  return (
    <div className="flex items-center bg-white rounded-lg border border-gray-200 shadow-sm">
      <FaGlobeAmericas className="mx-2 text-blue-600 text-sm" />
      <select
        className="border-0 rounded-lg px-2 py-1 text-sm font-medium focus:outline-none cursor-pointer"
        value={language}
        onChange={(e) => setLanguage(e.target.value)}
      >
        {languages.map((lang) => (
          <option key={lang.code} value={lang.code}>
            {lang.flag} {lang.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default LanguageToggle;