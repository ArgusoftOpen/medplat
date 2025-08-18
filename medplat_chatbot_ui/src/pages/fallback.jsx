import React, { useEffect, useState } from 'react';
import axios from 'axios';
import "./fallback.css";
import "../App.css";

const FallbackClusters = () => {
  const [clusters, setClusters] = useState([]);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const fetchClusters = async () => {
    try {
      const res = await axios.get('http://localhost:5001/get_fallbacks');
      const sortedClusters = (res.data.fallbacks || []).sort((a, b) => b.freq - a.freq);
      setClusters(sortedClusters);
    } catch (err) {
      console.error(err);
      setError('Failed to load fallback clusters');
    }
  };

  const handleTrainIntent = async (cluster) => {
    const intentPayload = {
      intent: `intent_${cluster.cluster_id}`,
      examples: [cluster.text],
      response: cluster.gpt_reply || `Response for ${cluster.text}`
    };

    try {
      const res = await axios.post('http://localhost:5000/add_intent', intentPayload);
      if (res.status === 200) {
        setSuccessMsg(`✅ Intent ${intentPayload.intent} trained successfully!`);
        setTimeout(() => setSuccessMsg(''), 3000);
      }
    } catch (err) {
      setError('❌ Failed to train intent');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchClusters();
  }, []);

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
              {clusters.map((item) => (
                <tr key={item.cluster_id}>
                  <td>{item.cluster_id}</td>
                  <td>{item.text}</td>
                  <td>{item.freq}</td>
                  <td>{item.gpt_reply}</td>
                  <td>
                    <button
                      className="train-button"
                      onClick={() => handleTrainIntent(item)}
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
