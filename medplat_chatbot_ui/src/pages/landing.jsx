import React, { useState } from "react";
import ChatApp from "./chat";
import Dashboard from "./Dashboard";
import "./assets/landing.css"; // Ensure correct path

export default function LandingPage() {
  const [role, setRole] = useState(null);

  // Handle role selection
  const handleRoleSelection = (selectedRole) => setRole(selectedRole);

  // Render based on role
  if (role === "user") return <ChatApp />;
  if (role === "admin") return <Dashboard />;

  // Landing page for role selection
  return (
    <div className="landing-container">
      <div className="landing-box">
        <h1 className="landing-title">Welcome to MedPlat</h1>
        <p className="landing-subtext">Please select your role to continue:</p>

        <div className="button-group">
          <button
            onClick={() => handleRoleSelection("user")}
            className="role-btn user-btn"
          >
            I am a User
          </button>
          <button
            onClick={() => handleRoleSelection("admin")}
            className="role-btn admin-btn"
          >
            I am an Admin
          </button>
        </div>
      </div>
    </div>
  );
}
