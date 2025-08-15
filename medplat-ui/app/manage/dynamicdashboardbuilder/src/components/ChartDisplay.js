import React from "react";
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
  ScatterChart,
  CartesianGrid,
  Scatter,
  ZAxis,
} from "recharts";

// Professional color palette
const COLORS = [
  "#1e40af",
  "#059669",
  "#dc2626",
  "#d97706",
  "#7c3aed",
  "#0891b2",
  "#be185d",
  "#374151",
];

export default function ChartDisplay({ data, chartType, xAxis, yAxis }) {
  // Enhanced validation
  if (!data || data.length === 0) {
    return (
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          minHeight: 400,
          background: "#f9fafb",
          border: "1px dashed #d1d5db",
          borderRadius: 8,
          padding: 32,
          textAlign: "center",
        }}
      >
        <div
          style={{
            width: 64,
            height: 64,
            background: "#e5e7eb",
            borderRadius: "50%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            marginBottom: 16,
            fontSize: 24,
            color: "#9ca3af",
          }}
        >
          📊
        </div>
        <h3
          style={{
            fontSize: 16,
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
            maxWidth: 300,
            lineHeight: 1.5,
          }}
        >
          Please select a data source and configure chart parameters to display
          your visualization.
        </p>
      </div>
    );
  }

  if (!xAxis) {
    return (
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          minHeight: 400,
          background: "#fef3c7",
          border: "1px solid #fbbf24",
          borderRadius: 8,
          padding: 32,
          textAlign: "center",
          color: "#92400e",
        }}
      >
        <div>
          <h3 style={{ fontSize: 16, fontWeight: 600, margin: "0 0 8px 0" }}>
            Missing X-Axis Configuration
          </h3>
          <p style={{ fontSize: 14, margin: 0 }}>
            Please select an X-axis column to generate the chart.
          </p>
        </div>
      </div>
    );
  }

  // For bar chart: if data has average/count, use that as Y
  const isMonthGrouped = data.length > 0 && data[0].year_month !== undefined;
  const isGrouped = data.length > 0 && data[0].xaxisvalue !== undefined;
  const hasAverage = data.length > 0 && data[0].average !== undefined;
  const hasCount = data.length > 0 && data[0].count !== undefined;
  const chartX = isMonthGrouped
    ? "year_month"
    : isGrouped
    ? "xaxisvalue"
    : xAxis;
  const chartY = hasAverage ? "average" : hasCount ? "count" : yAxis;

  // Helper to display N/A for missing/null/empty
  const displayValue = (v) =>
    v === null || v === undefined || v === "" ? "N/A" : v;

  // Helper for histogram binning
  function getHistogramBins(data, xKey, numBins = 10) {
    if (!data || data.length === 0) return [];
    const values = data
      .map((d) => parseFloat(d[xKey]))
      .filter((v) => !isNaN(v));
    if (values.length === 0) return [];
    const min = Math.min(...values);
    const max = Math.max(...values);
    const binSize = (max - min) / numBins;
    const bins = Array(numBins)
      .fill(0)
      .map((_, i) => ({
        bin: `${(min + i * binSize).toFixed(2)} - ${(
          min +
          (i + 1) * binSize
        ).toFixed(2)}`,
        count: 0,
      }));
    values.forEach((v) => {
      let idx = Math.floor((v - min) / binSize);
      if (idx === numBins) idx = numBins - 1;
      bins[idx].count++;
    });
    return bins;
  }

  // Custom tooltip component
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div
          style={{
            background: "#ffffff",
            border: "1px solid #e5e7eb",
            borderRadius: 6,
            padding: "12px 16px",
            boxShadow:
              "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)",
            fontSize: 13,
          }}
        >
          <p style={{ margin: "0 0 8px 0", fontWeight: 600, color: "#374151" }}>
            {displayValue(label)}
          </p>
          {payload.map((entry, index) => (
            <p
              key={index}
              style={{
                margin: "4px 0",
                color: entry.color,
                fontWeight: 500,
              }}
            >
              {entry.name}: {displayValue(entry.value)}
            </p>
          ))}
        </div>
      );
    }
    return null;
  };

  // Professional chart container style
  const chartContainerStyle = {
    background: "#ffffff",
    borderRadius: 8,
    padding: 16,
    minHeight: 400,
    border: "1px solid #e5e7eb",
  };

  return (
    <div style={chartContainerStyle}>
      {chartType === "bar" && (
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            data={data}
            margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#f3f4f6" />
            <XAxis
              dataKey={chartX}
              angle={-45}
              textAnchor="end"
              height={80}
              tickFormatter={displayValue}
              tick={{ fontSize: 12, fill: "#6b7280" }}
            />
            <YAxis
              tickFormatter={displayValue}
              tick={{ fontSize: 12, fill: "#6b7280" }}
              axisLine={{ stroke: "#e5e7eb" }}
              tickLine={{ stroke: "#e5e7eb" }}
            />
            <Tooltip content={<CustomTooltip />} />
            <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="rect" />
            <Bar
              dataKey={chartY}
              fill={COLORS[0]}
              radius={[4, 4, 0, 0]}
              stroke={COLORS[0]}
              strokeWidth={1}
            />
          </BarChart>
        </ResponsiveContainer>
      )}

      {chartType === "line" && (
        <ResponsiveContainer width="100%" height={400}>
          <LineChart
            data={data}
            margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#f3f4f6" />
            <XAxis
              dataKey={chartX}
              angle={-45}
              textAnchor="end"
              height={80}
              tickFormatter={displayValue}
              tick={{ fontSize: 12, fill: "#6b7280" }}
            />
            <YAxis
              tickFormatter={displayValue}
              tick={{ fontSize: 12, fill: "#6b7280" }}
              axisLine={{ stroke: "#e5e7eb" }}
              tickLine={{ stroke: "#e5e7eb" }}
            />
            <Tooltip content={<CustomTooltip />} />
            <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="line" />
            <Line
              type="monotone"
              dataKey={chartY}
              stroke={COLORS[1]}
              strokeWidth={3}
              dot={{ fill: COLORS[1], strokeWidth: 2, r: 4 }}
              activeDot={{
                r: 6,
                stroke: COLORS[1],
                strokeWidth: 2,
                fill: "#ffffff",
              }}
            />
          </LineChart>
        </ResponsiveContainer>
      )}

      {chartType === "pie" && (
        <ResponsiveContainer width="100%" height={400}>
          <PieChart>
            <Pie
              data={data}
              dataKey={chartY}
              nameKey={chartX}
              cx="50%"
              cy="50%"
              outerRadius={120}
              fill={COLORS[0]}
              label={({ name, percent }) =>
                `${name}: ${(percent * 100).toFixed(1)}%`
              }
              labelLine={false}
              stroke="#ffffff"
              strokeWidth={2}
            >
              {data.map((entry, idx) => (
                <Cell key={`cell-${idx}`} fill={COLORS[idx % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip content={<CustomTooltip />} />
            <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="circle" />
          </PieChart>
        </ResponsiveContainer>
      )}

      {chartType === "histogram" && (
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            data={getHistogramBins(data, chartX)}
            margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#f3f4f6" />
            <XAxis
              dataKey="bin"
              angle={-45}
              textAnchor="end"
              height={80}
              tick={{ fontSize: 12, fill: "#6b7280" }}
            />
            <YAxis
              tick={{ fontSize: 12, fill: "#6b7280" }}
              axisLine={{ stroke: "#e5e7eb" }}
              tickLine={{ stroke: "#e5e7eb" }}
            />
            <Tooltip content={<CustomTooltip />} />
            <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="rect" />
            <Bar
              dataKey="count"
              fill={COLORS[2]}
              radius={[4, 4, 0, 0]}
              stroke={COLORS[2]}
              strokeWidth={1}
            />
          </BarChart>
        </ResponsiveContainer>
      )}

      {chartType === "heatmap" && (
        <ResponsiveContainer width="100%" height={400}>
          <ScatterChart margin={{ top: 20, right: 30, left: 20, bottom: 60 }}>
            <CartesianGrid strokeDasharray="3 3" stroke="#f3f4f6" />
            <XAxis
              dataKey={chartX}
              name={chartX}
              tick={{ fontSize: 12, fill: "#6b7280" }}
            />
            <YAxis
              dataKey={chartY}
              name={chartY}
              tick={{ fontSize: 12, fill: "#6b7280" }}
            />
            <ZAxis type="number" range={[50, 400]} />
            <Tooltip
              cursor={{ strokeDasharray: "3 3" }}
              content={<CustomTooltip />}
            />
            <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="circle" />
            <Scatter
              data={data}
              fill={COLORS[4]}
              stroke={COLORS[4]}
              strokeWidth={1}
            />
          </ScatterChart>
        </ResponsiveContainer>
      )}
    </div>
  );
}
