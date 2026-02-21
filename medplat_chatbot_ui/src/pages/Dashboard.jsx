import { useEffect, useState } from "react";
import axios from "axios";
import './assets/Dashboard.css';
import DefineIntent from "./DefineIntent";
import ViewIntents from "./ViewIntents";
import FallbackUI from "./fallback"; // ✅ GPT Fallback UI

export default function Dashboard() {
  // ------------------- State -------------------
  const [intents, setIntents] = useState([]);
  const [activeTab, setActiveTab] = useState("view"); // default tab

  // ------------------- Fetch Intents -------------------
  const fetchIntents = async () => {
    try {
      const res = await axios.get(
  `${import.meta.env.VITE_BACKEND_URL || "http://127.0.0.1:5000"}/intents_with_examples`
);

      // Convert object → array of { intent, examples }
      const formatted = Object.entries(res.data).map(([intent, examples]) => ({
        intent,
        examples
      }));

      setIntents(formatted);
    } catch (error) {
      console.error("❌ Failed to load intents", error);
    }
  };

  // ------------------- Load intents on mount -------------------
  useEffect(() => {
    fetchIntents();
  }, []);

  // ------------------- Render -------------------
  return (
    <div className="dashboard-container">
      <div className="dashboard-card">
        <h1 className="dashboard-title">MedPlat Dashboard</h1>

        {/* Tab Navigation */}
        <div className="tab-buttons">
          <button
            className={`tab-button ${activeTab === "add" ? "active-add" : ""}`}
            onClick={() => setActiveTab("add")}
          >
            ➕ Add Intent
          </button>

          <button
            className={`tab-button ${activeTab === "view" ? "active-view" : ""}`}
            onClick={() => setActiveTab("view")}
          >
            📋 View / Delete Intents
          </button>

          <button
            className={`tab-button ${activeTab === "fallback" ? "active-fallback" : ""}`}
            onClick={() => setActiveTab("fallback")}
          >
            🛠️ GPT Fallback
          </button>
        </div>

        {/* Tab Content */}
        <div className="tab-content">
          {activeTab === "add" && <DefineIntent fetchIntents={fetchIntents} />}
          {activeTab === "view" && <ViewIntents intents={intents} fetchIntents={fetchIntents} />}
          {activeTab === "fallback" && <FallbackUI />}
        </div>
      </div>
    </div>
  );
}
