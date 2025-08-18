import { useEffect, useState } from "react";
import axios from "axios";
import './Dashboard.css';
import DefineIntent from "./DefineIntent";
import ViewIntents from "./ViewIntents";
import FallbackUI from "./fallback"; // ✅ NEW IMPORT

export default function Dashboard() {
  const [intents, setIntents] = useState([]);
  const [activeTab, setActiveTab] = useState("view");

  // ✅ Properly define fetchIntents as a standalone function
 const fetchIntents = async () => {
  try {
    const res = await axios.get("http://127.0.0.1:5000/intents_with_examples");

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

  // ✅ Call fetchIntents on mount
  useEffect(() => {
    fetchIntents();
  }, []);

  return (
    <div className="dashboard-container">
      <div className="dashboard-card">
        <h1 className="dashboard-title">MedPlat Dashboard</h1>

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

        {/* Tabs */}
        {activeTab === "add" && <DefineIntent fetchIntents={fetchIntents} />}
        {activeTab === "view" && <ViewIntents intents={intents} fetchIntents={fetchIntents} />}
        {activeTab === "fallback" && <FallbackUI />}
      </div>
    </div>
  );
}
