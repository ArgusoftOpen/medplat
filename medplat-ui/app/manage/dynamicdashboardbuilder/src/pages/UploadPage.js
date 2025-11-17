import React, { useState, useEffect } from "react";
import XlsxUploader from "../components/XlsxUploader";
import axios from "axios";

export default function UploadPage() {
  const [tables, setTables] = useState([]);

  const fetchTables = () => {
    axios
      .get("http://localhost:8181/api/ddb/tables")
      .then((res) => setTables(res.data))
      .catch(() => setTables([]));
  };

  useEffect(fetchTables, []);

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
          Data Upload Center
        </h1>
        <p
          style={{
            color: "#718096",
            fontSize: 16,
            margin: 0,
            lineHeight: 1.5,
          }}
        >
          Upload Excel files to create new database tables or append to existing
          ones
        </p>
      </div>

      {/* Main Upload Panel */}
      <div
        style={{
          maxWidth: 800,
          margin: "0 auto",
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
              margin: "0 0 4px 0",
            }}
          >
            Excel File Upload
          </h2>
          <p
            style={{
              fontSize: 14,
              color: "#718096",
              margin: 0,
            }}
          >
            Upload .xlsx or .xls files to import data into your database
          </p>
        </div>

        {/* Upload Component */}
        <div style={{ padding: "24px" }}>
          <XlsxUploader tables={tables} onUpload={fetchTables} />
        </div>
      </div>

      {/* Information Panel */}
      <div
        style={{
          maxWidth: 800,
          margin: "40px auto 0 auto",
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Info Header */}
        <div
          style={{
            background: "#f7fafc",
            borderBottom: "1px solid #e2e8f0",
            padding: "20px 24px",
          }}
        >
          <h3
            style={{
              fontSize: 16,
              fontWeight: 600,
              color: "#2d3748",
              margin: 0,
            }}
          >
            Upload Guidelines
          </h3>
        </div>

        {/* Info Content */}
        <div style={{ padding: "24px" }}>
          <div
            style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 24 }}
          >
            <div>
              <h4
                style={{
                  fontSize: 14,
                  fontWeight: 600,
                  color: "#374151",
                  margin: "0 0 12px 0",
                }}
              >
                Supported File Formats
              </h4>
              <ul
                style={{
                  margin: "0 0 0 16px",
                  padding: 0,
                  fontSize: 14,
                  color: "#6b7280",
                  lineHeight: 1.6,
                }}
              >
                <li>Excel files (.xlsx, .xls)</li>
                <li>CSV files with proper formatting</li>
                <li>Maximum file size: 10MB</li>
              </ul>
            </div>

            <div>
              <h4
                style={{
                  fontSize: 14,
                  fontWeight: 600,
                  color: "#374151",
                  margin: "0 0 12px 0",
                }}
              >
                Data Requirements
              </h4>
              <ul
                style={{
                  margin: "0 0 0 16px",
                  padding: 0,
                  fontSize: 14,
                  color: "#6b7280",
                  lineHeight: 1.6,
                }}
              >
                <li>First row should contain column headers</li>
                <li>Avoid special characters in column names</li>
                <li>Empty cells will be stored as NULL values</li>
              </ul>
            </div>
          </div>

          <div
            style={{
              marginTop: 24,
              padding: 16,
              background: "#f0f9ff",
              border: "1px solid #bae6fd",
              borderRadius: 6,
            }}
          >
            <div
              style={{
                display: "flex",
                alignItems: "flex-start",
                gap: 12,
              }}
            >
              <div
                style={{
                  width: 20,
                  height: 20,
                  borderRadius: "50%",
                  background: "#0284c7",
                  color: "#ffffff",
                  fontSize: 12,
                  fontWeight: 600,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  flexShrink: 0,
                  marginTop: 2,
                }}
              >
                i
              </div>
              <div>
                <h5
                  style={{
                    fontSize: 14,
                    fontWeight: 600,
                    color: "#075985",
                    margin: "0 0 4px 0",
                  }}
                >
                  Processing Information
                </h5>
                <p
                  style={{
                    fontSize: 13,
                    color: "#0c4a6e",
                    margin: 0,
                    lineHeight: 1.5,
                  }}
                >
                  When you upload a file, the system will automatically create a
                  new table with the name you specify, or append data to an
                  existing table if the structure matches. All data is stored as
                  text initially and can be converted to appropriate data types
                  later.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Existing Tables Panel */}
      {tables.length > 0 && (
        <div
          style={{
            maxWidth: 800,
            margin: "40px auto 0 auto",
            background: "#ffffff",
            border: "1px solid #e2e8f0",
            borderRadius: 8,
            overflow: "hidden",
            boxShadow:
              "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
          }}
        >
          {/* Tables Header */}
          <div
            style={{
              background: "#f7fafc",
              borderBottom: "1px solid #e2e8f0",
              padding: "20px 24px",
            }}
          >
            <h3
              style={{
                fontSize: 16,
                fontWeight: 600,
                color: "#2d3748",
                margin: "0 0 4px 0",
              }}
            >
              Existing Tables
            </h3>
            <p
              style={{
                fontSize: 14,
                color: "#718096",
                margin: 0,
              }}
            >
              {tables.length} table{tables.length !== 1 ? "s" : ""} available in
              the database
            </p>
          </div>

          {/* Tables List */}
          <div style={{ padding: "24px" }}>
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))",
                gap: 12,
                maxHeight: 200,
                overflowY: "auto",
              }}
            >
              {tables.map((table) => (
                <div
                  key={table}
                  style={{
                    padding: "8px 12px",
                    background: "#f9fafb",
                    border: "1px solid #e5e7eb",
                    borderRadius: 4,
                    fontSize: 13,
                    color: "#374151",
                    fontFamily: "ui-monospace, monospace",
                  }}
                >
                  {table}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
