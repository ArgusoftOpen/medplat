import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Bar, Line, Pie } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  ArcElement,
  Tooltip,
  Legend
);

export default function DatasetGraphPage() {
  const { id } = useParams();
  const [dataset, setDataset] = useState(null);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [chartType, setChartType] = useState("bar");

  useEffect(() => {
    axios.get(`/api/dataset-master/${id}`)
      .then(res => setDataset(res.data))
      .catch(err => setError("Dataset not found"));
  }, [id]);

  useEffect(() => {
    if (dataset && dataset.sql_query) {
      setLoading(true);
      axios.post("/api/run-sql", { query: dataset.sql_query })
        .then(res => setData(res.data))
        .catch(err => setError("Error running SQL: " + (err.response?.data?.error || err.message)))
        .finally(() => setLoading(false));
    }
  }, [dataset]);

  const renderChart = () => {
    if (!data || !data.rows || !data.fields) return null;
    if (data.rows.length === 0) return <div>No data returned by SQL.</div>;
    // Default: first column is x, second column is y
    const labels = data.rows.map(row => row[data.fields[0]]);
    const values = data.rows.map(row => row[data.fields[1]]);
    const chartData = {
      labels,
      datasets: [
        {
          label: data.fields[1],
          data: values,
          backgroundColor: "#314b89",
        },
      ],
    };
    if (chartType === "bar") return <Bar data={chartData} />;
    if (chartType === "line") return <Line data={chartData} />;
    if (chartType === "pie") return <Pie data={chartData} />;
    return null;
  };

  return (
    <div className="page-main-card">
      <div className="section-card" style={{ maxWidth: 900, margin: '0 auto', marginTop: 36 }}>
        <h2 className="section-header">Dataset Visualization</h2>
        {loading && <div>Loading...</div>}
        {error && <div style={{ color: 'red' }}>{error}</div>}
        {dataset && (
          <div style={{ marginBottom: 18 }}>
            <b>Dataset Name:</b> {dataset.dataset_name}
            <br />
            <b>SQL:</b> <span style={{ fontFamily: 'monospace', fontSize: 15 }}>{dataset.sql_query}</span>
          </div>
        )}
        {data && data.fields && data.fields.length >= 2 && (
          <div style={{ marginBottom: 18 }}>
            <label>Chart Type: </label>
            <select value={chartType} onChange={e => setChartType(e.target.value)}>
              <option value="bar">Bar</option>
              <option value="line">Line</option>
              <option value="pie">Pie</option>
            </select>
          </div>
        )}
        <div style={{ minHeight: 350 }}>
          {renderChart()}
        </div>
      </div>
    </div>
  );
}
