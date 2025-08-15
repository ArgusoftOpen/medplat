import React, { useState } from "react";

export default function TableSelector({ tables, selectedTable, onSelect }) {
  const [search, setSearch] = useState("");
  const [showSuggestions, setShowSuggestions] = useState(false);
  const filteredTables = tables.filter((t) =>
    t.toLowerCase().includes(search.toLowerCase())
  );

  const handleInputChange = (e) => {
    setSearch(e.target.value);
    setShowSuggestions(true);
  };

  const handleSuggestionClick = (table) => {
    setSearch(table);
    onSelect(table);
    setShowSuggestions(false);
  };

  const handleInputBlur = () => {
    setTimeout(() => setShowSuggestions(false), 100); // Delay to allow click
  };

  const handleInputKeyDown = (e) => {
    if (e.key === "Enter") {
      const match = tables.find(
        (t) => t.toLowerCase() === search.toLowerCase()
      );
      if (match) {
        onSelect(match);
        setShowSuggestions(false);
      }
    }
  };

  return (
    <div style={{ position: "relative", width: "100%" }}>
      <label
        htmlFor="table-search"
        style={{
          display: "block",
          fontSize: 14,
          fontWeight: 500,
          color: "#374151",
          marginBottom: 8,
        }}
      >
        Database Table
      </label>
      <input
        id="table-search"
        type="text"
        value={search}
        onChange={handleInputChange}
        onBlur={handleInputBlur}
        onFocus={() => setShowSuggestions(true)}
        onKeyDown={handleInputKeyDown}
        placeholder="Search available tables..."
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
        onFocusCapture={(e) => (e.target.style.borderColor = "#3b82f6")}
        onBlurCapture={(e) => (e.target.style.borderColor = "#d1d5db")}
        autoComplete="off"
      />

      {selectedTable && (
        <div
          style={{
            marginTop: 8,
            fontSize: 12,
            color: "#059669",
            fontWeight: 500,
          }}
        >
          Selected: {selectedTable}
        </div>
      )}

      {showSuggestions && search && filteredTables.length > 0 && (
        <ul
          style={{
            position: "absolute",
            zIndex: 100,
            top: "100%",
            left: 0,
            right: 0,
            background: "#ffffff",
            border: "1px solid #d1d5db",
            borderRadius: 6,
            maxHeight: 200,
            overflowY: "auto",
            margin: 0,
            padding: 0,
            listStyle: "none",
            boxShadow:
              "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)",
          }}
        >
          {filteredTables.map((table) => (
            <li
              key={table}
              onMouseDown={() => handleSuggestionClick(table)}
              style={{
                padding: "10px 12px",
                cursor: "pointer",
                background: table === selectedTable ? "#eff6ff" : "#ffffff",
                borderBottom: "1px solid #f3f4f6",
                fontSize: 14,
                color: "#374151",
                fontFamily: "ui-monospace, monospace",
                transition: "background-color 0.15s ease-in-out",
              }}
              onMouseEnter={(e) => {
                if (table !== selectedTable) {
                  e.target.style.backgroundColor = "#f9fafb";
                }
              }}
              onMouseLeave={(e) => {
                e.target.style.backgroundColor =
                  table === selectedTable ? "#eff6ff" : "#ffffff";
              }}
            >
              {table}
            </li>
          ))}
        </ul>
      )}

      {showSuggestions && search && filteredTables.length === 0 && (
        <div
          style={{
            position: "absolute",
            zIndex: 100,
            top: "100%",
            left: 0,
            right: 0,
            background: "#ffffff",
            border: "1px solid #d1d5db",
            borderRadius: 6,
            padding: "16px 12px",
            textAlign: "center",
            fontSize: 14,
            color: "#9ca3af",
            boxShadow:
              "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)",
          }}
        >
          No tables found matching "{search}"
        </div>
      )}
    </div>
  );
}
