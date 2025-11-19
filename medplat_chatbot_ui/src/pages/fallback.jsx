import React, { useEffect, useState } from 'react';
import axios from 'axios';
import "../App.css";
import "./assets/fallback.css";

const FallbackClusters = () => {
  // ------------------- State -------------------
  const [clusters, setClusters] = useState([]);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  // ------------------- Fetch fallback clusters -------------------
  const fetchClusters = async () => {
    try {
      const res = await axios.get(`${process.env.REACT_APP_BACKEND_URL || 'http://localhost:5001'}/get_fallbacks`);
      const sortedClusters = (res.data.fallbacks || []).sort((a, b) => b.freq - a.freq);
      setClusters(sortedClusters);
    } catch (err) {
      console.error(err);
      setError('❌ Failed to load fallback clusters');
      setTimeout(() => setError(''), 5000); // auto-clear after 5s
    }
  };

  // ------------------- Handle training a single cluster as intent -------------------
  const handleTrainIntent = async (cluster) => {
    const intentPayload = {
      intent: `auto_intent_${cluster.cluster_id}`,
      examples: [cluster.text],
      response: cluster.gpt_reply || `Response for ${cluster.text}`
    };

    try {
      const res = await axios.post(`${process.env.REACT_APP_BACKEND_URL || 'http://localhost:5000'}/add_intent`, intentPayload);
      if (res.status === 200) {
        setSuccessMsg(`✅ Intent "${intentPayload.intent}" trained successfully!`);
        setTimeout(() => setSuccessMsg(''), 3000);
      }
    } catch (err) {
      console.error(err);
      setError('❌ Failed to train intent');
      setTimeout(() => setError(''), 5000);
    }
  };

  // ------------------- Load clusters on mount -------------------
  useEffect(() => {
    fetchClusters();
  }, []);

  // ------------------- Render -------------------
  return (
    <div className="fallback-container">
      <h2 className="fallback-title">🤖 Fallback Intents Summary</h2>

      {error && <p className="fallback-error">{error}</p>}
      {successMsg && <p className="fallback-success">{successMsg}</p>}

      {clusters.length === 0 ? (
        <p className="fallback-empty">No fallback messages recorded yet.</p>
      ) : (
        <div className="fallback-table-wrapper">
          <table className="fallback-table">
            <thead>
              <tr>
                <th>Cluster ID</th>
                <th>User Message</th>
                <th>Frequency</th>
                <th>GPT Reply</th>
                <th>Train</th>
              </tr>
            </thead>
            <tbody>
              {clusters.map((cluster) => (
                <tr key={cluster.cluster_id}>
                  <td>{cluster.cluster_id}</td>
                  <td>{cluster.text}</td>
                  <td>{cluster.freq}</td>
                  <td>{cluster.gpt_reply}</td>
                  <td>
                    <button
                      className="train-button"
                      onClick={() => handleTrainIntent(cluster)}
                    >
                      Train Intent
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default FallbackClusters;
