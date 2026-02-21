import React, { useState } from "react";
import axios from "axios";

export default function SqlQueryPage() {
  const [query, setQuery] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const runQuery = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setResult(null);
    try {
      const res = await axios.post("http://localhost:8181/api/ddb/run-sql", {
        query,
      });
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error || err.message);
    } finally {
      setLoading(false);
    }
  };

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
          SQL Query Console
        </h1>
        <p
          style={{
            color: "#718096",
            fontSize: 16,
            margin: 0,
            lineHeight: 1.5,
          }}
        >
          Execute SQL queries and view results in real-time
        </p>
      </div>

      {/* Query Input Panel */}
      <div
        style={{
          maxWidth: 1000,
          margin: "0 auto 40px auto",
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Panel Header */}
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
            Query Editor
          </h2>
        </div>

        {/* Query Form */}
        <form onSubmit={runQuery} style={{ padding: "24px" }}>
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
              SQL Query
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
              <strong>Note:</strong> Enter your SQL query below. Use proper
              syntax and ensure queries are safe for execution.
            </div>
            <textarea
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              rows={8}
              style={{
                width: "100%",
                padding: "12px 16px",
                fontSize: 14,
                fontFamily: "ui-monospace, monospace",
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                resize: "vertical",
                transition: "border-color 0.15s ease-in-out",
                lineHeight: 1.5,
              }}
              placeholder="SELECT * FROM your_table_name LIMIT 10;"
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>

          <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
            <button
              type="submit"
              disabled={loading || !query.trim()}
              style={{
                padding: "12px 24px",
                fontSize: 14,
                fontWeight: 500,
                borderRadius: 6,
                background: loading || !query.trim() ? "#e5e7eb" : "#1e40af",
                color: loading || !query.trim() ? "#9ca3af" : "#ffffff",
                border: "none",
                cursor: loading || !query.trim() ? "not-allowed" : "pointer",
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
              {loading ? "Executing Query..." : "Execute Query"}
            </button>

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
                  flex: 1,
                }}
              >
                {error}
              </div>
            )}
          </div>
        </form>
      </div>

      {/* Results Panel */}
      {result && (
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
          {/* Results Header */}
          <div
            style={{
              background: "#f7fafc",
              borderBottom: "1px solid #e2e8f0",
              padding: "20px 24px",
            }}
          >
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
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
                Query Results
              </h2>
              {result.rows && (
                <div
                  style={{
                    fontSize: 14,
                    color: "#6b7280",
                    background: "#e5e7eb",
                    padding: "4px 12px",
                    borderRadius: 12,
                    fontWeight: 500,
                  }}
                >
                  {result.rows.length} rows returned
                </div>
              )}
            </div>
          </div>

          {/* Results Content */}
          <div>
            {result.fields && result.fields.length > 0 ? (
              <div
                style={{
                  maxHeight: 500,
                  overflowY: "auto",
                  overflowX: "auto",
                }}
              >
                <table
                  style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    fontSize: 13,
                    minWidth: "max-content",
                  }}
                >
                  <thead
                    style={{
                      background: "#f9fafb",
                      position: "sticky",
                      top: 0,
                      zIndex: 1,
                    }}
                  >
                    <tr>
                      {result.fields.map((field, idx) => (
                        <th
                          key={idx}
                          style={{
                            padding: "12px 16px",
                            textAlign: "left",
                            fontWeight: 500,
                            color: "#374151",
                            borderBottom: "2px solid #e5e7eb",
                            background: "#f9fafb",
                            whiteSpace: "nowrap",
                            minWidth: "120px",
                          }}
                        >
                          {field}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {result.rows.length === 0 ? (
                      <tr>
                        <td
                          colSpan={result.fields.length}
                          style={{
                            padding: "48px 24px",
                            textAlign: "center",
                            color: "#9ca3af",
                            fontSize: 14,
                          }}
                        >
                          No rows returned by the query
                        </td>
                      </tr>
                    ) : (
                      result.rows.map((row, rowIdx) => (
                        <tr
                          key={rowIdx}
                          style={{
                            borderBottom: "1px solid #f3f4f6",
                            transition: "background-color 0.15s ease-in-out",
                          }}
                          onMouseEnter={(e) =>
                            (e.currentTarget.style.backgroundColor = "#f9fafb")
                          }
                          onMouseLeave={(e) =>
                            (e.currentTarget.style.backgroundColor =
                              "transparent")
                          }
                        >
                          {result.fields.map((field, colIdx) => (
                            <td
                              key={colIdx}
                              style={{
                                padding: "12px 16px",
                                color: "#374151",
                                verticalAlign: "top",
                                maxWidth: "300px",
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap",
                                fontFamily:
                                  typeof row[field] === "number"
                                    ? "ui-monospace, monospace"
                                    : "inherit",
                              }}
                              title={String(row[field])}
                            >
                              {row[field] !== null &&
                              row[field] !== undefined ? (
                                String(row[field])
                              ) : (
                                <span
                                  style={{
                                    color: "#9ca3af",
                                    fontStyle: "italic",
                                  }}
                                >
                                  NULL
                                </span>
                              )}
                            </td>
                          ))}
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            ) : result.fields && result.fields.length === 0 ? (
              <div
                style={{
                  padding: "48px 24px",
                  textAlign: "center",
                  color: "#6b7280",
                  fontSize: 14,
                }}
              >
                Query executed successfully but returned no columns
              </div>
            ) : null}
          </div>
        </div>
      )}
    </div>
  );
}
