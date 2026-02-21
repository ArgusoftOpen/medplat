import React, { useState } from "react";
import axios from "axios";

export default function XlsxUploader({ tables, onUpload }) {
  const [file, setFile] = useState(null);
  const [table, setTable] = useState("");
  const [status, setStatus] = useState("");
  const [isUploading, setIsUploading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleTableChange = (e) => {
    setTable(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file || !table) {
      setStatus("Please select a table and a file.");
      return;
    }
    setIsUploading(true);
    setStatus("Uploading...");
    const formData = new FormData();
    formData.append("file", file);
    formData.append("table", table);
    try {
      await axios.post("http://localhost:8181/api/upload-xlsx", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setStatus("Upload successful!");
      setFile(null);
      setTable("");
      if (onUpload) onUpload();
    } catch (err) {
      setStatus("Upload failed: " + (err.response?.data?.error || err.message));
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div
      style={{
        background: "#ffffff",
        borderRadius: 8,
        padding: 24,
        border: "1px solid #e5e7eb",
      }}
    >
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 24 }}>
          <label
            htmlFor="table-xlsx"
            style={{
              display: "block",
              fontSize: 14,
              fontWeight: 500,
              color: "#374151",
              marginBottom: 8,
            }}
          >
            Target Table
          </label>
          <select
            id="table-xlsx"
            value={table}
            onChange={handleTableChange}
            style={{
              width: "100%",
              maxWidth: 300,
              padding: "10px 12px",
              fontSize: 14,
              border: "1px solid #d1d5db",
              borderRadius: 6,
              outline: "none",
              background: "#ffffff",
              cursor: "pointer",
              transition: "border-color 0.15s ease-in-out",
            }}
            onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
            onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
          >
            <option value="">Select a table...</option>
            {tables.map((t) => (
              <option key={t} value={t}>
                {t}
              </option>
            ))}
          </select>
          <div
            style={{
              marginTop: 4,
              fontSize: 12,
              color: "#6b7280",
            }}
          >
            Choose an existing table or create a new one by typing a name
          </div>
        </div>

        <div style={{ marginBottom: 24 }}>
          <label
            htmlFor="file-upload"
            style={{
              display: "block",
              fontSize: 14,
              fontWeight: 500,
              color: "#374151",
              marginBottom: 8,
            }}
          >
            Excel File
          </label>
          <div
            style={{
              position: "relative",
              display: "inline-block",
              width: "100%",
              maxWidth: 400,
            }}
          >
            <input
              id="file-upload"
              type="file"
              accept=".xlsx,.xls"
              onChange={handleFileChange}
              style={{
                width: "100%",
                padding: "10px 12px",
                fontSize: 14,
                border: "1px solid #d1d5db",
                borderRadius: 6,
                outline: "none",
                background: "#ffffff",
                cursor: "pointer",
                transition: "border-color 0.15s ease-in-out",
              }}
              onFocus={(e) => (e.target.style.borderColor = "#3b82f6")}
              onBlur={(e) => (e.target.style.borderColor = "#d1d5db")}
            />
          </div>
          {file && (
            <div
              style={{
                marginTop: 8,
                fontSize: 12,
                color: "#059669",
                fontWeight: 500,
              }}
            >
              Selected: {file.name} ({(file.size / 1024).toFixed(1)} KB)
            </div>
          )}
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
          <button
            type="submit"
            disabled={!file || !table || isUploading}
            style={{
              padding: "12px 24px",
              fontSize: 14,
              fontWeight: 500,
              borderRadius: 6,
              background:
                !file || !table || isUploading ? "#e5e7eb" : "#1e40af",
              color: !file || !table || isUploading ? "#9ca3af" : "#ffffff",
              border: "none",
              cursor:
                !file || !table || isUploading ? "not-allowed" : "pointer",
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
            {isUploading ? "Uploading..." : "Upload Excel File"}
          </button>

          {status && (
            <div
              style={{
                padding: "8px 16px",
                background:
                  status.includes("failed") || status.includes("Please select")
                    ? "#fee2e2"
                    : status.includes("successful")
                    ? "#d1fae5"
                    : "#fef3c7",
                color:
                  status.includes("failed") || status.includes("Please select")
                    ? "#dc2626"
                    : status.includes("successful")
                    ? "#065f46"
                    : "#d97706",
                borderRadius: 6,
                fontSize: 14,
                fontWeight: 500,
                border:
                  status.includes("failed") || status.includes("Please select")
                    ? "1px solid #fca5a5"
                    : status.includes("successful")
                    ? "1px solid #a7f3d0"
                    : "1px solid #fbbf24",
              }}
            >
              {status}
            </div>
          )}
        </div>
      </form>

      {/* Information Panel */}
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
              width: 16,
              height: 16,
              borderRadius: "50%",
              background: "#0284c7",
              color: "#ffffff",
              fontSize: 10,
              fontWeight: 600,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              flexShrink: 0,
              marginTop: 1,
            }}
          >
            i
          </div>
          <div>
            <h5
              style={{
                fontSize: 13,
                fontWeight: 600,
                color: "#075985",
                margin: "0 0 4px 0",
              }}
            >
              Upload Instructions
            </h5>
            <ul
              style={{
                fontSize: 12,
                color: "#0c4a6e",
                margin: 0,
                paddingLeft: 16,
                lineHeight: 1.5,
              }}
            >
              <li>Select an existing table or type a new table name</li>
              <li>Choose an Excel file (.xlsx or .xls format)</li>
              <li>First row should contain column headers</li>
              <li>Data will be inserted as text and can be converted later</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
