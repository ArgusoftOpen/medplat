import React, { useEffect, useState } from "react";
import TableSelector from "../components/TableSelector";
import ColumnSelector from "../components/ColumnSelector";
import ChartDisplay from "../components/ChartDisplay";
import axios from "axios";

export default function VisualizePage() {
  const [tables, setTables] = useState([]);
  const [selectedTable, setSelectedTable] = useState("");
  const [columns, setColumns] = useState([]);
  const [xAxis, setXAxis] = useState("");
  const [yAxis, setYAxis] = useState("");
  const [chartType, setChartType] = useState("bar");
  const [data, setData] = useState([]);

  const fetchTables = () => {
    axios
      .get("http://localhost:8181/api/tables")
      .then((res) => setTables(res.data))
      .catch(() => setTables([]));
  };
  useEffect(fetchTables, []);

  useEffect(() => {
    if (selectedTable) {
      axios
        .get(`http://localhost:8181/api/columns/${selectedTable}`)
        .then((res) => setColumns(res.data))
        .catch(() => setColumns([]));
      setXAxis("");
      setYAxis("");
      setData([]);
    }
  }, [selectedTable]);

  useEffect(() => {
    if (selectedTable && xAxis) {
      const body =
        chartType === "pie"
          ? { table: selectedTable, xAxis, chartType }
          : { table: selectedTable, xAxis, indicator: yAxis, chartType };
      axios
        .post("http://localhost:8181/api/grouped-count", body)
        .then((res) => setData(res.data))
        .catch(() => setData([]));
    }
  }, [selectedTable, xAxis, yAxis, chartType]);

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
          Data Visualization
        </h1>
        <p
          style={{
            color: "#718096",
            fontSize: 16,
            margin: 0,
            lineHeight: 1.5,
          }}
        >
          Create interactive charts and visualizations from your database tables
        </p>
      </div>

      {/* Configuration Panel */}
      <div
        style={{
          maxWidth: 1200,
          margin: "0 auto 40px auto",
          background: "#ffffff",
          border: "1px solid #e2e8f0",
          borderRadius: 8,
          overflow: "hidden",
          boxShadow:
            "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        }}
      >
        {/* Configuration Header */}
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
            Chart Configuration
          </h2>
          <p
            style={{
              fontSize: 14,
              color: "#718096",
              margin: 0,
            }}
          >
            Select your data source and configure chart parameters
          </p>
        </div>

        {/* Configuration Content */}
        <div style={{ padding: "24px" }}>
          {/* Table Selection */}
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
              Data Source
            </label>
            <TableSelector
              tables={tables}
              selectedTable={selectedTable}
              onSelect={setSelectedTable}
            />
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

          {/* Column Configuration */}
          {columns.length > 0 && (
            <div
              style={{
                padding: 20,
                background: "#f9fafb",
                border: "1px solid #e5e7eb",
                borderRadius: 8,
                marginBottom: 24,
              }}
            >
              <h3
                style={{
                  fontSize: 16,
                  fontWeight: 600,
                  color: "#374151",
                  margin: "0 0 16px 0",
                }}
              >
                Chart Parameters
              </h3>
              <ColumnSelector
                columns={columns}
                xAxis={xAxis}
                setXAxis={setXAxis}
                yAxis={yAxis}
                setYAxis={setYAxis}
                chartType={chartType}
                setChartType={setChartType}
              />
            </div>
          )}
        </div>
      </div>

      {/* Chart Display Panel */}
      {data.length > 0 && (
        <div
          style={{
            maxWidth: 1200,
            margin: "0 auto",
            background: "#ffffff",
            border: "1px solid #e2e8f0",
            borderRadius: 8,
            overflow: "hidden",
            boxShadow:
              "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
          }}
        >
          {/* Chart Header */}
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
                Visualization Results
              </h2>
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
                {data.length} data points
              </div>
            </div>
            {selectedTable && xAxis && (
              <div
                style={{
                  marginTop: 8,
                  fontSize: 14,
                  color: "#6b7280",
                }}
              >
                <span style={{ fontWeight: 500 }}>Source:</span> {selectedTable}{" "}
                |<span style={{ fontWeight: 500 }}> X-Axis:</span> {xAxis}
                {yAxis && (
                  <>
                    {" | "}
                    <span style={{ fontWeight: 500 }}>Y-Axis:</span> {yAxis}
                  </>
                )}
                {" | "}
                <span style={{ fontWeight: 500 }}>Type:</span>{" "}
                {chartType.charAt(0).toUpperCase() + chartType.slice(1)} Chart
              </div>
            )}
          </div>

          {/* Chart Content */}
          <div style={{ padding: "24px" }}>
            <ChartDisplay
              data={data}
              chartType={chartType}
              xAxis={xAxis}
              yAxis={yAxis}
            />
          </div>
        </div>
      )}

      {/* Empty State */}
      {!selectedTable && (
        <div
          style={{
            maxWidth: 600,
            margin: "0 auto",
            padding: "48px 24px",
            textAlign: "center",
            background: "#ffffff",
            border: "1px solid #e2e8f0",
            borderRadius: 8,
            boxShadow:
              "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
          }}
        >
          <div
            style={{
              width: 64,
              height: 64,
              background: "#f3f4f6",
              borderRadius: "50%",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              margin: "0 auto 16px auto",
              fontSize: 24,
              color: "#9ca3af",
            }}
          >
            📊
          </div>
          <h3
            style={{
              fontSize: 18,
              fontWeight: 600,
              color: "#374151",
              margin: "0 0 8px 0",
            }}
          >
            Get Started with Data Visualization
          </h3>
          <p
            style={{
              fontSize: 14,
              color: "#6b7280",
              margin: 0,
              lineHeight: 1.6,
            }}
          >
            Select a table from your database to begin creating interactive
            charts and visualizations. You can choose from various chart types
            including bar charts, line charts, and pie charts.
          </p>
        </div>
      )}

      {/* No Data State */}
      {selectedTable && columns.length > 0 && xAxis && data.length === 0 && (
        <div
          style={{
            maxWidth: 600,
            margin: "0 auto",
            padding: "48px 24px",
            textAlign: "center",
            background: "#ffffff",
            border: "1px solid #e2e8f0",
            borderRadius: 8,
            boxShadow:
              "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
          }}
        >
          <div
            style={{
              width: 64,
              height: 64,
              background: "#fef3c7",
              borderRadius: "50%",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              margin: "0 auto 16px auto",
              fontSize: 24,
              color: "#d97706",
            }}
          >
            📈
          </div>
          <h3
            style={{
              fontSize: 18,
              fontWeight: 600,
              color: "#374151",
              margin: "0 0 8px 0",
            }}
          >
            No Data Available
          </h3>
          <p
            style={{
              fontSize: 14,
              color: "#6b7280",
              margin: 0,
              lineHeight: 1.6,
            }}
          >
            The selected table and columns don't have data to visualize, or
            there might be an issue with the data query. Please check your
            selections and try again.
          </p>
        </div>
      )}
    </div>
  );
}
