import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";

const operations = [
  { label: "Addition (+)", value: "+", symbol: "+" },
  { label: "Subtraction (-)", value: "-", symbol: "-" },
  { label: "Multiplication (×)", value: "*", symbol: "×" },
  { label: "Division (÷)", value: "/", symbol: "÷" },
];

export default function DerivedAttributePage() {
  const [tables, setTables] = useState([]);
  const [selectedTable, setSelectedTable] = useState("");
  const [columns, setColumns] = useState([]);
  const [indicators, setIndicators] = useState([]);
  const [options, setOptions] = useState([]);
  const [attr1, setAttr1] = useState("");
  const [attr2, setAttr2] = useState("");
  const [val1, setVal1] = useState(null);
  const [val2, setVal2] = useState(null);
  const [operation, setOperation] = useState("+");
  const [derivedName, setDerivedName] = useState("");
  const [formula, setFormula] = useState("");
  const [result, setResult] = useState(null);
  const [saveMsg, setSaveMsg] = useState("");
  const [error, setError] = useState("");
  const [derivedList, setDerivedList] = useState([]);

  // Fetch tables and derived attributes
  useEffect(() => {
    axios
      .get("/api/tables")
      .then((res) => setTables(res.data))
      .catch(() => setTables([]));
    axios
      .get("/api/indicator-master")
      .then((res) => setIndicators(res.data))
      .catch(() => setIndicators([]));
    fetchDerived();
  }, []);

  const fetchDerived = async () => {
    try {
      const res = await axios.get("/api/derived-attributes");
      setDerivedList(res.data);
    } catch {
      setDerivedList([]);
    }
  };

  // Fetch columns when table changes
  useEffect(() => {
    if (selectedTable) {
      axios
        .get(`/api/columns/${selectedTable}`)
        .then((res) => setColumns(res.data))
        .catch(() => setColumns([]));
    } else {
      setColumns([]);
    }
  }, [selectedTable]);

  // Build dropdown options
  useEffect(() => {
    setOptions([
      ...columns.map((col) => ({ label: col, value: col, type: "column" })),
      ...indicators.map((ind) => ({
        label: ind.indicator_name,
        value: ind.indicator_name,
        type: "indicator",
      })),
    ]);
  }, [columns, indicators]);

  // Fetch value for an attribute - use useCallback to memoize the function
  const fetchValue = useCallback(
    async (attr, setter) => {
      if (!attr) return setter(null);
      const found = options.find((opt) => opt.value === attr);
      if (!found) return setter(null);
      if (found.type === "column") {
        if (!selectedTable) return setter(null);
        try {
          const res = await axios.post("/api/attribute-value", {
            table: selectedTable,
            column: attr,
          });
          setter(res.data.value);
        } catch (error) {
          setter(null);
        }
      } else if (found.type === "indicator") {
        try {
          const res = await axios.post("/api/attribute-value", {
            indicator: attr,
          });
          setter(res.data.value);
        } catch (error) {
          setter(null);
        }
      }
    },
    [options, selectedTable]
  );

  // Update values when attributes change
  useEffect(() => {
    if (attr1) fetchValue(attr1, setVal1);
    else setVal1(null);
  }, [attr1, fetchValue]);
  useEffect(() => {
    if (attr2) fetchValue(attr2, setVal2);
    else setVal2(null);
  }, [attr2, fetchValue]);

  // Update formula and result
  useEffect(() => {
    if (attr1 && attr2 && operation && val1 !== null && val2 !== null) {
      let res = null;
      try {
        switch (operation) {
          case "+":
            res = val1 + val2;
            break;
          case "-":
            res = val1 - val2;
            break;
          case "*":
            res = val1 * val2;
            break;
          case "/":
            res = val2 !== 0 ? parseFloat((val1 / val2).toFixed(6)) : null;
            break;
          default:
            res = null;
        }
      } catch {
        res = null;
      }
      setFormula(`${attr1} ${operation} ${attr2}`);
      setResult(res);
    } else {
      setFormula("");
      setResult(null);
    }
  }, [attr1, attr2, operation, val1, val2]);

  const handleSave = async (e) => {
    e.preventDefault();
    setError("");
    setSaveMsg("");
    if (!derivedName || !formula || result === null) {
      setError("Please fill all fields and ensure result is valid.");
      return;
    }
    try {
      await axios.post("/api/derived-attribute", {
        derived_name: derivedName,
        formula,
        result,
      });
      setSaveMsg("Derived attribute saved!");
      setDerivedName("");
      fetchDerived();
    } catch (err) {
      setError(err.response?.data?.error || "Save failed");
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
          Derived Attribute Builder
        </h1>
        <p
          style={{
            color: "#718096",
            fontSize: 16,
            margin: 0,
            lineHeight: 1.5,
          }}
        >
          Create calculated attributes by combining existing data columns and
          indicators
        </p>
      </div>

      {/* Main Configuration Panel */}
      <div
        style={{
          maxWidth: 1000,
          margin: "0 auto",
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Header Bar */}
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
            Configuration
          </h2>
        </div>

        {/* Content Area */}
        <div style={{ padding: "24px" }}>
          {/* Data Source Selection */}
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
              Data Source Table
            </label>
            <select
              value={selectedTable}
              onChange={(e) => setSelectedTable(e.target.value)}
              style={{
                width: "100%",
                maxWidth: 400,
                padding: "10px 12px",
                fontSize: 14,
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                transition: "border-color 0.15s ease-in-out",
              }}
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            >
              <option value="">Select a table...</option>
              {tables.map((table) => (
                <option key={table} value={table}>
                  {table}
                </option>
              ))}
            </select>
            {selectedTable && (
              <div
                style={{
                  marginTop: 8,
                  fontSize: 13,
                  color: "#059669",
                  fontWeight: 500,
                }}
              >
                Selected: {selectedTable} ({columns.length} columns available)
              </div>
            )}
          </div>

          {/* Attribute Configuration Grid */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr auto 1fr",
              gap: 24,
              marginBottom: 32,
            }}
          >
            {/* First Attribute */}
            <div
              style={{
                border: "1px solid #e5e7eb",
                borderRadius: 8,
                padding: 20,
                background: "#fafafa",
              }}
            >
              <label
                style={{
                  display: "block",
                  fontSize: 14,
                  fontWeight: 500,
                  color: "#374151",
                  marginBottom: 8,
                }}
              >
                First Attribute
              </label>
              <select
                value={attr1}
                onChange={(e) => setAttr1(e.target.value)}
                style={{
                  width: "100%",
                  padding: "10px 12px",
                  fontSize: 14,
                  border: "1px solid #d1d5db",
                  borderRadius: 6,
                  outline: "none",
                  background: "#ffffff",
                  marginBottom: 12,
                }}
                onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
                onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
              >
                <option value="">Select attribute...</option>
                {options.map((opt) => (
                  <option key={opt.value} value={opt.value}>
                    {opt.type === "indicator" ? "[Indicator] " : "[Column] "}
                    {opt.label}
                  </option>
                ))}
              </select>
              <div
                style={{
                  padding: 12,
                  background: "#ffffff",
                  border: "1px solid #e5e7eb",
                  borderRadius: 6,
                  textAlign: "center",
                  fontSize: 18,
                  fontWeight: 600,
                  color: val1 !== null ? "#065f46" : "#9ca3af",
                  fontFamily: "ui-monospace, monospace",
                }}
              >
                {val1 !== null ? val1.toLocaleString() : "—"}
              </div>
            </div>

            {/* Operation Selector */}
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
                minWidth: 140,
                padding: "20px 16px",
                background: "#1e40af",
                borderRadius: 8,
                color: "#ffffff",
              }}
            >
              <label
                style={{
                  fontSize: 12,
                  fontWeight: 500,
                  marginBottom: 12,
                  textTransform: "uppercase",
                  letterSpacing: "0.05em",
                  opacity: 0.9,
                }}
              >
                Operation
              </label>
              <select
                value={operation}
                onChange={(e) => setOperation(e.target.value)}
                style={{
                  width: "100%",
                  padding: "8px 12px",
                  fontSize: 14,
                  fontWeight: 500,
                  border: "none",
                  borderRadius: 6,
                  background: "#ffffff",
                  color: "#1e40af",
                  cursor: "pointer",
                  outline: "none",
                  marginBottom: 12,
                }}
              >
                {operations.map((op) => (
                  <option key={op.value} value={op.value}>
                    {op.label}
                  </option>
                ))}
              </select>
              <div
                style={{
                  fontSize: 24,
                  fontWeight: 700,
                  fontFamily: "ui-monospace, monospace",
                }}
              >
                {operations.find((op) => op.value === operation)?.symbol || "+"}
              </div>
            </div>

            {/* Second Attribute */}
            <div
              style={{
                border: "1px solid #e5e7eb",
                borderRadius: 8,
                padding: 20,
                background: "#fafafa",
              }}
            >
              <label
                style={{
                  display: "block",
                  fontSize: 14,
                  fontWeight: 500,
                  color: "#374151",
                  marginBottom: 8,
                }}
              >
                Second Attribute
              </label>
              <select
                value={attr2}
                onChange={(e) => setAttr2(e.target.value)}
                style={{
                  width: "100%",
                  padding: "10px 12px",
                  fontSize: 14,
                  border: "1px solid #d1d5db",
                  borderRadius: 6,
                  outline: "none",
                  background: "#ffffff",
                  marginBottom: 12,
                }}
                onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
                onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
              >
                <option value="">Select attribute...</option>
                {options.map((opt) => (
                  <option key={opt.value} value={opt.value}>
                    {opt.type === "indicator" ? "[Indicator] " : "[Column] "}
                    {opt.label}
                  </option>
                ))}
              </select>
              <div
                style={{
                  padding: 12,
                  background: "#ffffff",
                  border: "1px solid #e5e7eb",
                  borderRadius: 6,
                  textAlign: "center",
                  fontSize: 18,
                  fontWeight: 600,
                  color: val2 !== null ? "#065f46" : "#9ca3af",
                  fontFamily: "ui-monospace, monospace",
                }}
              >
                {val2 !== null ? val2.toLocaleString() : "—"}
              </div>
            </div>
          </div>

          {/* Formula and Result Display */}
          <div
            style={{
              border: "1px solid #e5e7eb",
              borderRadius: 8,
              padding: 24,
              background: "#f8fafc",
              marginBottom: 32,
            }}
          >
            <div style={{ marginBottom: 16 }}>
              <label
                style={{
                  display: "block",
                  fontSize: 14,
                  fontWeight: 500,
                  color: "#374151",
                  marginBottom: 8,
                }}
              >
                Formula
              </label>
              <div
                style={{
                  padding: 12,
                  background: "#ffffff",
                  border: "1px solid #e5e7eb",
                  borderRadius: 6,
                  fontFamily: "ui-monospace, monospace",
                  fontSize: 16,
                  color: formula ? "#1f2937" : "#9ca3af",
                }}
              >
                {formula || "Select attributes and operation"}
              </div>
            </div>
            <div>
              <label
                style={{
                  display: "block",
                  fontSize: 14,
                  fontWeight: 500,
                  color: "#374151",
                  marginBottom: 8,
                }}
              >
                Calculated Result
              </label>
              <div
                style={{
                  padding: 16,
                  background: "#ffffff",
                  border:
                    result !== null ? "2px solid #10b981" : "1px solid #e5e7eb",
                  borderRadius: 6,
                  textAlign: "center",
                  fontSize: 24,
                  fontWeight: 700,
                  color: result !== null ? "#065f46" : "#9ca3af",
                  fontFamily: "ui-monospace, monospace",
                }}
              >
                {result !== null ? result.toLocaleString() : "—"}
              </div>
            </div>
          </div>

          {/* Save Form */}
          <form onSubmit={handleSave}>
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
                Derived Attribute Name
              </label>
              <input
                type="text"
                value={derivedName}
                onChange={(e) => setDerivedName(e.target.value)}
                style={{
                  width: "100%",
                  maxWidth: 500,
                  padding: "10px 12px",
                  fontSize: 14,
                  border: "1px solid #d1d5db",
                  borderRadius: 6,
                  outline: "none",
                  background: "#ffffff",
                }}
                placeholder="Enter a descriptive name..."
                required
                onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
                onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
              />
            </div>

            <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
              <button
                type="submit"
                disabled={!derivedName || !formula || result === null}
                style={{
                  padding: "12px 24px",
                  fontSize: 14,
                  fontWeight: 500,
                  borderRadius: 6,
                  background:
                    !derivedName || !formula || result === null
                      ? "#e5e7eb"
                      : "#1e40af",
                  color:
                    !derivedName || !formula || result === null
                      ? "#9ca3af"
                      : "#ffffff",
                  border: "none",
                  cursor:
                    !derivedName || !formula || result === null
                      ? "not-allowed"
                      : "pointer",
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
                Save Derived Attribute
              </button>

              {saveMsg && (
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
                  {saveMsg}
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
      </div>

      {/* Saved Attributes Table */}
      <div
        style={{
          marginTop: 40,
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
            Saved Derived Attributes
          </h2>
          <p
            style={{
              fontSize: 14,
              color: "#718096",
              margin: 0,
            }}
          >
            Manage your calculated attributes
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
                    padding: "12px 24px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                  }}
                >
                  Name
                </th>
                <th
                  style={{
                    padding: "12px 24px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                  }}
                >
                  Formula
                </th>
                <th
                  style={{
                    padding: "12px 24px",
                    textAlign: "right",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                  }}
                >
                  Result
                </th>
                <th
                  style={{
                    padding: "12px 24px",
                    textAlign: "left",
                    fontWeight: 500,
                    color: "#374151",
                    borderBottom: "1px solid #e5e7eb",
                  }}
                >
                  Created
                </th>
              </tr>
            </thead>
            <tbody>
              {derivedList.length === 0 ? (
                <tr>
                  <td
                    colSpan={4}
                    style={{
                      padding: "48px 24px",
                      textAlign: "center",
                      color: "#9ca3af",
                      fontSize: 14,
                    }}
                  >
                    No derived attributes created yet
                  </td>
                </tr>
              ) : (
                derivedList.map((row, index) => (
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
                        padding: "16px 24px",
                        fontWeight: 500,
                        color: "#1f2937",
                      }}
                    >
                      {row.derived_name}
                    </td>
                    <td
                      style={{
                        padding: "16px 24px",
                        fontFamily: "ui-monospace, monospace",
                        color: "#6b7280",
                        fontSize: 13,
                      }}
                    >
                      {row.formula}
                    </td>
                    <td
                      style={{
                        padding: "16px 24px",
                        textAlign: "right",
                        fontWeight: 600,
                        color: "#059669",
                        fontFamily: "ui-monospace, monospace",
                      }}
                    >
                      {row.result !== null && row.result !== undefined
                        ? Number(row.result).toLocaleString()
                        : "—"}
                    </td>
                    <td
                      style={{
                        padding: "16px 24px",
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
