import { useEffect } from "react";
import "./splash-walk.css";

/**
 * Props:
 *  onFinish?: () => void  // called when the shutter finishes
 *  durationMs?: number    // total time until reveal
 */
export default function SplashWalk({ onFinish, durationMs = 6200 }) {
  useEffect(() => {
    const t = setTimeout(() => onFinish && onFinish(), durationMs);
    return () => clearTimeout(t);
  }, [onFinish, durationMs]);

  return (
    <div className="walker-wrapper">
      <div className="walk-overlay" aria-hidden="true">
        {/* Medical Cap */}
        <svg
          className="cap"
          viewBox="0 0 200 100"
          width="200"
          height="100"
          role="img"
          aria-label="Medical cap"
        >
          {/* Cap base */}
          <ellipse cx="100" cy="60" rx="90" ry="35" fill="#3b82f6" stroke="#1e40af" strokeWidth="2" />
          {/* Cap top curve */}
          <path d="M10 60 Q100 10 190 60" fill="#3b82f6" stroke="#1e40af" strokeWidth="2" />
          
          {/* Cross emblem */}
          <circle cx="100" cy="60" r="18" fill="#ffffff" stroke="#1e40af" strokeWidth="1" />
          <rect x="95" y="52" width="10" height="16" fill="#ef4444" />
          <rect x="92" y="55" width="16" height="10" fill="#ef4444" />
        </svg>

        {/* top & bottom shutters (slide away to reveal) */}
        <div className="shutter shutter-top" />
        <div className="shutter shutter-bottom" />
      </div>
    </div>
  );
}
