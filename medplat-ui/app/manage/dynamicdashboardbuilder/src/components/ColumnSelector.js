import React, { useState } from "react";

export default function ColumnSelector({
  columns,
  xAxis,
  setXAxis,
  yAxis,
  setYAxis,
  chartType,
  setChartType,
}) {
  const [xAxisSearch, setXAxisSearch] = useState("");
  const [xAxisShow, setXAxisShow] = useState(false);
  const [yAxisSearch, setYAxisSearch] = useState("");
  const [yAxisShow, setYAxisShow] = useState(false);

  const filteredX = columns.filter((col) =>
    col.toLowerCase().includes((xAxisSearch || "").toLowerCase())
  );
  const filteredY = columns.filter((col) =>
    col.toLowerCase().includes((yAxisSearch || "").toLowerCase())
  );

  const handleXInputChange = (e) => {
    setXAxisSearch(e.target.value);
    setXAxisShow(true);
  };
  const handleYInputChange = (e) => {
    setYAxisSearch(e.target.value);
    setYAxisShow(true);
  };
  const handleXClick = (col) => {
    setXAxis(col);
    setXAxisSearch(col);
    setXAxisShow(false);
  };
  const handleYClick = (col) => {
    setYAxis(col);
    setYAxisSearch(col);
    setYAxisShow(false);
  };
  const handleXBlur = () => setTimeout(() => setXAxisShow(false), 100);
  const handleYBlur = () => setTimeout(() => setYAxisShow(false), 100);
  const handleXKeyDown = (e) => {
    if (e.key === "Enter") {
      const match = columns.find(
        (c) => c.toLowerCase() === xAxisSearch.toLowerCase()
      );
      if (match) {
        setXAxis(match);
        setXAxisShow(false);
      }
    }
  };
  const handleYKeyDown = (e) => {
    if (e.key === "Enter") {
      const match = columns.find(
        (c) => c.toLowerCase() === yAxisSearch.toLowerCase()
      );
      if (match) {
        setYAxis(match);
        setYAxisShow(false);
      }
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
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr auto",
          gap: 24,
          alignItems: "end",
        }}
      >
        {/* X-Axis Selection */}
        <div style={{ position: "relative" }}>
          <label
            htmlFor="x-axis-search"
            style={{
              display: "block",
              fontSize: 14,
              fontWeight: 500,
              color: "#374151",
              marginBottom: 8,
            }}
          >
            X-Axis Column
          </label>
          <input
            id="x-axis-search"
            type="text"
            value={xAxisSearch || ""}
            onChange={handleXInputChange}
            onBlur={handleXBlur}
            onFocus={() => setXAxisShow(true)}
            onKeyDown={handleXKeyDown}
            placeholder="Search columns..."
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
          {xAxis && (
            <div
              style={{
                marginTop: 4,
                fontSize: 12,
                color: "#059669",
                fontWeight: 500,
              }}
            >
              Selected: {xAxis}
            </div>
          )}
          {xAxisShow && xAxisSearch && filteredX.length > 0 && (
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
              {filteredX.map((col) => (
                <li
                  key={col}
                  onMouseDown={() => handleXClick(col)}
                  style={{
                    padding: "10px 12px",
                    cursor: "pointer",
                    background: col === xAxis ? "#eff6ff" : "#ffffff",
                    borderBottom: "1px solid #f3f4f6",
                    fontSize: 14,
                    color: "#374151",
                    transition: "background-color 0.15s ease-in-out",
                  }}
                  onMouseEnter={(e) => {
                    if (col !== xAxis) {
                      e.target.style.backgroundColor = "#f9fafb";
                    }
                  }}
                  onMouseLeave={(e) => {
                    e.target.style.backgroundColor =
                      col === xAxis ? "#eff6ff" : "#ffffff";
                  }}
                >
                  {col}
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Y-Axis Selection */}
        <div style={{ position: "relative" }}>
          <label
            htmlFor="indicator-search"
            style={{
              display: "block",
              fontSize: 14,
              fontWeight: 500,
              color: "#374151",
              marginBottom: 8,
            }}
          >
            Y-Axis Column (Optional)
          </label>
          <input
            id="indicator-search"
            type="text"
            value={yAxisSearch || ""}
            onChange={handleYInputChange}
            onBlur={handleYBlur}
            onFocus={() => setYAxisShow(true)}
            onKeyDown={handleYKeyDown}
            placeholder="Search columns..."
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
          {yAxis && (
            <div
              style={{
                marginTop: 4,
                fontSize: 12,
                color: "#059669",
                fontWeight: 500,
              }}
            >
              Selected: {yAxis}
            </div>
          )}
          {yAxisShow && yAxisSearch && filteredY.length > 0 && (
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
              {filteredY.map((col) => (
                <li
                  key={col}
                  onMouseDown={() => handleYClick(col)}
                  style={{
                    padding: "10px 12px",
                    cursor: "pointer",
                    background: col === yAxis ? "#eff6ff" : "#ffffff",
                    borderBottom: "1px solid #f3f4f6",
                    fontSize: 14,
                    color: "#374151",
                    transition: "background-color 0.15s ease-in-out",
                  }}
                  onMouseEnter={(e) => {
                    if (col !== yAxis) {
                      e.target.style.backgroundColor = "#f9fafb";
                    }
                  }}
                  onMouseLeave={(e) => {
                    e.target.style.backgroundColor =
                      col === yAxis ? "#eff6ff" : "#ffffff";
                  }}
                >
                  {col}
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Chart Type Selection */}
        <div>
          <label
            htmlFor="chart-type"
            style={{
              display: "block",
              fontSize: 14,
              fontWeight: 500,
              color: "#374151",
              marginBottom: 8,
            }}
          >
            Chart Type
          </label>
          <select
            id="chart-type"
            value={chartType}
            onChange={(e) => setChartType(e.target.value)}
            style={{
              minWidth: 140,
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
            <option value="bar">Bar Chart</option>
            <option value="line">Line Chart</option>
            <option value="pie">Pie Chart</option>
            <option value="heatmap">Heatmap</option>
            <option value="histogram">Histogram</option>
          </select>
        </div>
      </div>

      {/* Information Panel */}
      <div
        style={{
          marginTop: 20,
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
              Chart Configuration Guide
            </h5>
            <p
              style={{
                fontSize: 12,
                color: "#0c4a6e",
                margin: 0,
                lineHeight: 1.5,
              }}
            >
              <strong>X-Axis:</strong> Select the column for categorization or
              grouping. <strong>Y-Axis:</strong> Optional column for specific
              data measurement (defaults to count). <strong>Chart Type:</strong>{" "}
              Choose visualization that best represents your data.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
