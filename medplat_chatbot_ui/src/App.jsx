// App.js
import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LandingPage from "./pages/landing";
import SplashWalk from "./pages/SplashWalk"; // 👈 your splash screen
import './index.css';

function App() {
  const [showSplash, setShowSplash] = useState(true);

  return (
    <Router>
      {showSplash ? (
        <SplashWalk onFinish={() => setShowSplash(false)} />
      ) : (
        <Routes>
          <Route path="/" element={<LandingPage />} />
          {/* Later you can add: <Route path="/chat" ... />, etc. */}
        </Routes>
      )}
    </Router>
  );
}

export default App;
