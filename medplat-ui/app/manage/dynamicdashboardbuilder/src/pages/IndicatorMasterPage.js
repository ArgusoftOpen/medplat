import React, { useState } from "react";
import axios from "axios";

export default function IndicatorMasterPage() {
  const [indicatorName, setIndicatorName] = useState("");
  const [description, setDescription] = useState("");
  const [sqlQuery, setSqlQuery] = useState("");
  const [createdBy, setCreatedBy] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [history, setHistory] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setResult(null);
    setLoading(true);
    try {
      const res = await axios.post("/api/ddb/indicator-master", {
        indicator_name: indicatorName,
        description,
        sql_query: sqlQuery,
        created_by: createdBy || null,
      });
      setResult(res.data);
      fetchHistory();
    } catch (err) {
      setError(err.response?.data?.error || err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchHistory = async () => {
    try {
      const res = await axios.get("/api/ddb/indicator-master");
      setHistory(res.data);
    } catch {
      setHistory([]);
    }
  };

  React.useEffect(() => {
    fetchHistory();
  }, []);

  return (
    <div className="page-main-card">
      {/* Professional Header */}
      <div style={{ marginBottom: 40 }}>
        <h1
          style={{
            color: "#1a365d",
            fontSize: 28,
            fontWeight: 600,
            margin: "0 0 8px 0",
            letterSpacing: "-0.025em",
          }}
        >
          Indicator Master
        </h1>
        <p
          style={{
            color: "#718096",
            fontSize: 16,
            margin: 0,
            lineHeight: 1.5,
          }}
        >
          Create and manage SQL-based indicators for data analysis and reporting
        </p>
      </div>

      {/* Main Form Panel */}
      <div
        style={{
          maxWidth: 800,
          margin: "0 auto 40px auto",
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Form Header */}
        <div
          style={{
            background: "#f7fafc",
            borderBottom: "1px solid #e2e8f0",
            padding: "20px 24px",
          }}
        >
          <h2
            style={{
              fontSize: 18,
              fontWeight: 600,
              color: "#2d3748",
              margin: 0,
            }}
          >
            Create New Indicator
          </h2>
        </div>

        {/* Form Content */}
        <form onSubmit={handleSubmit} style={{ padding: "24px" }}>
          <div style={{ marginBottom: 24 }}>
            <label
              style={{
                display: "block",
                fontSize: 14,
                fontWeight: 500,
                color: "#374151",
                marginBottom: 8,
              }}
            >
              Indicator Name *
            </label>
            <input
              type="text"
              value={indicatorName}
              onChange={(e) => setIndicatorName(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "10px 12px",
                fontSize: 14,
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                transition: "border-color 0.15s ease-in-out",
              }}
              placeholder="Enter a descriptive name for the indicator"
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>

          <div style={{ marginBottom: 24 }}>
            <label
              style={{
                display: "block",
                fontSize: 14,
                fontWeight: 500,
                color: "#374151",
                marginBottom: 8,
              }}
            >
              Description
            </label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
              style={{
                width: "100%",
                padding: "10px 12px",
                fontSize: 14,
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                resize: "vertical",
                transition: "border-color 0.15s ease-in-out",
              }}
              placeholder="Describe what this indicator measures"
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>

          <div style={{ marginBottom: 24 }}>
            <label
              style={{
                display: "block",
                fontSize: 14,
                fontWeight: 500,
                color: "#374151",
                marginBottom: 8,
              }}
            >
              SQL Query *
            </label>
            <div
              style={{
                fontSize: 13,
                color: "#6b7280",
                marginBottom: 8,
                padding: 8,
                background: "#f9fafb",
                borderRadius: 4,
                border: "1px solid #e5e7eb",
              }}
            >
              <strong>Note:</strong> Query should return a single numeric value
              (e.g., COUNT, SUM, AVG)
            </div>
            <textarea
              value={sqlQuery}
              onChange={(e) => setSqlQuery(e.target.value)}
              rows={5}
              required
              style={{
                width: "100%",
                padding: "10px 12px",
                fontSize: 13,
                fontFamily: "ui-monospace, monospace",
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                resize: "vertical",
                transition: "border-color 0.15s ease-in-out",
              }}
              placeholder="SELECT COUNT(*) FROM table_name WHERE condition..."
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>

          <div style={{ marginBottom: 32 }}>
            <label
              style={{
                display: "block",
                fontSize: 14,
                fontWeight: 500,
                color: "#374151",
                marginBottom: 8,
              }}
            >
              Created By (User ID)
            </label>
            <input
              type="number"
              value={createdBy}
              onChange={(e) => setCreatedBy(e.target.value)}
              style={{
                width: "200px",
                padding: "10px 12px",
                fontSize: 14,
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                transition: "border-color 0.15s ease-in-out",
              }}
              placeholder="Optional"
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>

          <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
            <button
              type="submit"
              disabled={loading}
              style={{
                padding: "12px 24px",
                fontSize: 14,
                fontWeight: 500,
                borderRadius: 6,
                background: loading ? "#e5e7eb" : "#1e40af",
                color: loading ? "#9ca3af" : "#ffffff",
                border: "none",
                cursor: loading ? "not-allowed" : "pointer",
                transition: "all 0.15s ease-in-out",
              }}
              onMouseEnter={(e) => {
                if (!e.target.disabled) {
                  e.target.style.background = "#1d4ed8";
                }
              }}
              onMouseLeave={(e) => {
                if (!e.target.disabled) {
                  e.target.style.background = "#1e40af";
                }
              }}
            >
              {loading ? "Creating Indicator..." : "Create Indicator"}
            </button>

            {result && result.success && (
              <div
                style={{
                  padding: "8px 16px",
                  background: "#d1fae5",
                  color: "#065f46",
                  borderRadius: 6,
                  fontSize: 14,
                  fontWeight: 500,
                  border: "1px solid #a7f3d0",
                }}
              >
                Success! Query Result: <strong>{result.query_result}</strong>
              </div>
            )}

            {error && (
              <div
                style={{
                  padding: "8px 16px",
                  background: "#fee2e2",
                  color: "#dc2626",
                  borderRadius: 6,
                  fontSize: 14,
                  fontWeight: 500,
                  border: "1px solid #fca5a5",
                }}
              >
                {error}
              </div>
            )}
          </div>
        </form>
      </div>

      {/* Saved Indicators Table */}
      <div
        style={{
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Table Header */}
        <div
          style={{
            background: "#f7fafc",
            borderBottom: "1px solid #e2e8f0",
            padding: "20px 24px",
          }}
        >
          <h2
            style={{
              fontSize: 18,
              fontWeight: 600,
              color: "#2d3748",
              margin: "0 0 4px 0",
            }}
          >
            Saved Indicators
          </h2>
          <p
            style={{
              fontSize: 14,
              color: "#718096",
              margin: 0,
            }}
          >
            Manage your SQL-based indicators
          </p>
        </div>

        {/* Table Content */}
        <div style={{ overflowX: "auto" }}>
          <table
            style={{
              width: "100%",
              borderCollapse: "collapse",
              fontSize: 14,
            }}
          >
            <thead>
              <tr style={{ background: "#f9fafb" }}>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    minWidth: "140px",
                  }}
                >
                  Name
                </th>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    minWidth: "200px",
                  }}
                >
                  Description
                </th>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    minWidth: "300px",
                  }}
                >
                  SQL Query
                </th>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "center",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    width: "100px",
                  }}
                >
                  Result
                </th>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "center",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    width: "100px",
                  }}
                >
                  Created By
                </th>
                <th
                  style={{
                    padding: "12px 16px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                    width: "140px",
                  }}
                >
                  Created On
                </th>
              </tr>
            </thead>
            <tbody>
              {history.length === 0 ? (
                <tr>
                  <td
                    colSpan={6}
                    style={{
                      padding: "48px 24px",
                      textAlign: "center",
                      color: "#9ca3af",
                      fontSize: 14,
                    }}
                  >
                    No indicators created yet
                  </td>
                </tr>
              ) : (
                history.map((row, index) => (
                  <tr
                    key={row.id}
                    style={{
                      borderBottom: "1px solid #f3f4f6",
                      transition: "background-color 0.15s ease-in-out",
                    }}
                    onMouseEnter={(e) =>
                      (e.currentTarget.style.backgroundColor = "#f9fafb")
                    }
                    onMouseLeave={(e) =>
                      (e.currentTarget.style.backgroundColor = "transparent")
                    }
                  >
                    <td
                      style={{
                        padding: "16px",
                        fontWeight: 500,
                        color: "#1f2937",
                      }}
                    >
                      {row.indicator_name}
                    </td>
                    <td
                      style={{
                        padding: "16px",
                        color: "#6b7280",
                        maxWidth: "200px",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                      }}
                      title={row.description}
                    >
                      {row.description || "—"}
                    </td>
                    <td
                      style={{
                        padding: "16px",
                        fontFamily: "ui-monospace, monospace",
                        fontSize: 12,
                        color: "#6b7280",
                        background: "#f9fafb",
                        maxWidth: "300px",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                      }}
                      title={row.sql_query}
                    >
                      {row.sql_query}
                    </td>
                    <td
                      style={{
                        padding: "16px",
                        textAlign: "center",
                        fontWeight: 600,
                        color: "#059669",
                        fontFamily: "ui-monospace, monospace",
                      }}
                    >
                      {row.query_result !== null &&
                      row.query_result !== undefined
                        ? row.query_result.toLocaleString()
                        : "—"}
                    </td>
                    <td
                      style={{
                        padding: "16px",
                        textAlign: "center",
                        color: "#6b7280",
                      }}
                    >
                      {row.created_by || "—"}
                    </td>
                    <td
                      style={{
                        padding: "16px",
                        color: "#6b7280",
                        fontSize: 13,
                      }}
                    >
                      {row.created_on
                        ? new Date(row.created_on).toLocaleDateString("en-US", {
                            year: "numeric",
                            month: "short",
                            day: "numeric",
                            hour: "2-digit",
                            minute: "2-digit",
                          })
                        : "—"}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
