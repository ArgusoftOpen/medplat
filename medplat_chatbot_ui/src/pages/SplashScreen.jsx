import { useEffect } from "react";
import ShashaktiAvatar from "./ShashaktiAvatar"; // your avatar component
import "./splash.css";
export default function SplashScreen({ onFinish }) {
  useEffect(() => {
    const timer = setTimeout(onFinish, 2500); // hide after 2.5s
    return () => clearTimeout(timer);
  }, [onFinish]);

  return (
    <div className="splash-container">
      <div className="splash-content">
        <div className="avatar-wrapper animate-bounce">
          <ShashaktiAvatar size={120} />
        </div>
        <h1 className="splash-title">Welcome to Shashakti</h1>
        <p className="splash-subtitle">Empowering Field Workers</p>
      </div>
    </div>
  );
}
