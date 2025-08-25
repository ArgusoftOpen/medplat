import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  askGemini,
  getAutomaticChartRecommendation,
} from "../utils/geminiHelper";
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
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

/*
 * Expected API Endpoints:
 *
 * 1. GET /api/ddb/dataset-master
 *    Returns: Array of dataset objects with fields: id, dataset_name, sql_query, created_on, created_by
 *
 * 2. POST /api/ddb/dataset-chart (optional)
 *    Body: { sql: "SELECT column, COUNT(*) as count FROM table GROUP BY column", datasetId: 123 }
 *    Returns: Array of objects in format: [{ label: "category1", count: 5 }, { label: "category2", count: 3 }]
 *
 * 3. POST /api/ddb/preview-sql (fallback)
 *    Body: { sql: "SELECT column, COUNT(*) as count FROM table GROUP BY column" }
 *    Returns: { rows: [{ column: "value1", count: 5 }, { column: "value2", count: 3 }] }
 *    This is used as fallback if /api/dataset-chart is not available
 *
 * The component will automatically transform preview-sql results into chart format:
 * - 2 columns: first = label, second = count
 * - More columns: first = label, looks for count/total/sum column
 * - 1 column: counts occurrences of values
 */

export default function DatasetMasterListPage() {
  const [datasets, setDatasets] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [chartLoading, setChartLoading] = useState(false);
  const [chartError, setChartError] = useState("");
  const [selectedDataset, setSelectedDataset] = useState(null);
  const [showChart, setShowChart] = useState(false);
  const [showFilters, setShowFilters] = useState(false);
  const [filters, setFilters] = useState([]); // [{ column, operator, value, logic }]
  const [availableColumns, setAvailableColumns] = useState([]);
  const [chartType, setChartType] = useState("bar"); // New state for chart type

  // Gemini Q&A state
  const [fullData, setFullData] = useState([]); // Full SQL result for Q&A
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [isAsking, setIsAsking] = useState(false);
  const [geminiResponse, setGeminiResponse] = useState(null); // Store full Gemini response
  const [showAutoChartOption, setShowAutoChartOption] = useState(false);
  const GEMINI_API_KEY = "AIzaSyCsEMKIlTlDe1S3FOehpKIV5eoXuodwKRk"; // User-provided, for demo/dev only

  const operators = [
    "=",
    "!=",
    ">",
    ">=",
    "<",
    "<=",
    "LIKE",
    "ILIKE",
    "IN",
    "NOT IN",
    "IS NULL",
    "IS NOT NULL",
    "BETWEEN",
  ];

  // Function to extract columns from SQL query (basic parsing)
  const extractColumnsFromSQL = (sql) => {
    try {
      // Remove extra whitespace and normalize the SQL
      const normalizedSQL = sql.replace(/\s+/g, " ").trim();

      // Extract the SELECT clause
      const selectMatch = normalizedSQL.match(/SELECT\s+(.*?)\s+FROM/i);
      if (!selectMatch) {
        console.log("Could not find SELECT clause");
        return [];
      }

      const selectClause = selectMatch[1];
      console.log("SELECT clause:", selectClause);

      // Handle SELECT * case
      if (selectClause.trim() === "*") {
        // For SELECT *, we need to extract table names and assume common columns
        // This is a limitation - in a real app, you'd query the database schema
        const tableMatches = normalizedSQL.match(/(?:FROM|JOIN)\s+(\w+)/gi);
        const tables = tableMatches
          ? tableMatches.map((match) =>
              match.replace(/(?:FROM|JOIN)\s+/i, "").trim()
            )
          : [];

        // Return table-prefixed common columns as fallback
        const commonCols = ["id", "name", "status", "type", "date"];
        return tables.length > 0
          ? tables.flatMap((table) =>
              commonCols.map((col) => `${table}.${col}`)
            )
          : commonCols;
      }

      // Parse individual columns from SELECT clause
      const columns = [];

      // Split by comma, but be careful with function calls like COUNT(*)
      const parts = [];
      let currentPart = "";
      let parenthesesCount = 0;

      for (let i = 0; i < selectClause.length; i++) {
        const char = selectClause[i];
        if (char === "(") {
          parenthesesCount++;
        } else if (char === ")") {
          parenthesesCount--;
        } else if (char === "," && parenthesesCount === 0) {
          parts.push(currentPart.trim());
          currentPart = "";
          continue;
        }
        currentPart += char;
      }
      if (currentPart.trim()) {
        parts.push(currentPart.trim());
      }

      console.log("Column parts:", parts);

      // Process each column part
      parts.forEach((part) => {
        // Remove AS aliases to get the base column/expression
        const withoutAs = part
          .replace(/\s+AS\s+["`']?[\w\s]+["`']?$/i, "")
          .trim();

        // Check if it's an aggregate function
        const aggMatch = withoutAs.match(
          /^(COUNT|SUM|AVG|MIN|MAX|COUNT\s+DISTINCT)\s*\(/i
        );
        if (aggMatch) {
          // Extract the column inside the aggregate function
          const innerMatch = withoutAs.match(/\((.*?)\)/);
          if (innerMatch) {
            const innerContent = innerMatch[1].trim();
            if (
              innerContent !== "*" &&
              innerContent.toLowerCase() !== "distinct *"
            ) {
              // Remove DISTINCT keyword if present
              const cleanColumn = innerContent
                .replace(/^DISTINCT\s+/i, "")
                .trim();
              if (
                cleanColumn &&
                !cleanColumn.includes("(") &&
                !cleanColumn.includes(")")
              ) {
                columns.push(cleanColumn);
              }
            }
          }
        } else {
          // Regular column - remove quotes and check if it's a valid column reference
          const cleanColumn = withoutAs.replace(/^["`']|["`']$/g, "").trim();
          if (
            cleanColumn &&
            !cleanColumn.includes("(") &&
            !cleanColumn.includes(")") &&
            !cleanColumn.match(/^\d+$/) && // Not a number
            !cleanColumn.match(/^['"].*['"]$/) // Not a string literal
          ) {
            columns.push(cleanColumn);
          }
        }
      });

      console.log("Extracted columns:", columns);

      // Also extract table names to provide table.column options
      const tableMatches = normalizedSQL.match(/(?:FROM|JOIN)\s+(\w+)/gi);
      const tables = tableMatches
        ? tableMatches.map((match) =>
            match.replace(/(?:FROM|JOIN)\s+/i, "").trim()
          )
        : [];

      // Combine direct columns with table-prefixed versions
      const allColumns = [...columns];

      // Add table-prefixed versions of extracted columns
      tables.forEach((table) => {
        columns.forEach((col) => {
          // If column doesn't already have table prefix, add it
          if (!col.includes(".")) {
            allColumns.push(`${table}.${col}`);
          }
        });
      });

      // Remove duplicates and return
      const uniqueColumns = [...new Set(allColumns)];
      console.log("Final available columns:", uniqueColumns);

      return uniqueColumns.length > 0
        ? uniqueColumns
        : ["id", "name", "status"]; // Fallback
    } catch (err) {
      console.log("Error extracting columns:", err);
      return ["id", "name", "status", "type", "category", "date"]; // Fallback
    }
  };

  // Function to apply filters to SQL query
  const applyFiltersToSQL = (originalSQL, filters) => {
    if (filters.length === 0) return originalSQL;

    let sql = originalSQL.trim();

    // Check if SQL already has WHERE clause
    const hasWhere = sql.toLowerCase().includes("where");

    // Build filter conditions
    const filterConditions = filters
      .map((filter, idx) => {
        // Properly handle column names with table prefixes and quotes
        let columnName = filter.column;

        // Handle table.column format
        if (columnName.includes('.')) {
          const parts = columnName.split('.');
          if (parts.length === 2) {
            const [table, column] = parts;
            // Clean and properly format: table."column"
            const cleanTable = table.replace(/["`]/g, '').trim();
            const cleanColumn = column.replace(/["`]/g, '').trim();
            columnName = `${cleanTable}."${cleanColumn}"`;
          }
        } else {
          // Single column name - add quotes if it contains special characters or spaces
          const cleanColumn = columnName.replace(/["`]/g, '').trim();
          if (cleanColumn.includes(' ') || cleanColumn.includes('-') || cleanColumn.toLowerCase() !== cleanColumn) {
            columnName = `"${cleanColumn}"`;
          } else {
            columnName = cleanColumn;
          }
        }

        let condition = `${columnName} `;

        if (filter.operator === "IS NULL" || filter.operator === "IS NOT NULL") {
          condition += filter.operator;
        } else if (filter.operator === "IN" || filter.operator === "NOT IN") {
          // Handle IN operator - ensure values are properly quoted
          const values = filter.value
            .split(',')
            .map(v => `'${v.trim().replace(/'/g, "''")}'`)
            .join(', ');
          condition += `${filter.operator} (${values})`;
        } else if (filter.operator === "LIKE" || filter.operator === "ILIKE") {
          const escapedValue = filter.value.replace(/'/g, "''");
          condition += `${filter.operator} '%${escapedValue}%'`;
        } else if (filter.operator === "BETWEEN") {
          if (Array.isArray(filter.value) && filter.value.length === 2) {
            const val1 = filter.value[0].replace(/'/g, "''");
            const val2 = filter.value[1].replace(/'/g, "''");
            condition += `BETWEEN '${val1}' AND '${val2}'`;
          } else {
            condition += "BETWEEN '' AND ''";
          }
        } else {
          // Standard operators (=, !=, >, <, etc.)
          const escapedValue = filter.value.replace(/'/g, "''");
          condition += `${filter.operator} '${escapedValue}'`;
        }

        const logic = idx > 0 ? ` ${filter.logic || "AND"} ` : "";
        return logic + condition;
      })
      .join("");

    // Add filters to SQL
    if (hasWhere) {
      // Find common SQL clauses that come after WHERE
      const afterWherePattern = /\s+(GROUP\s+BY|ORDER\s+BY|HAVING|LIMIT|OFFSET)/i;
      const match = sql.match(afterWherePattern);

      if (match) {
        // Insert filter conditions before the matched clause
        sql = sql.replace(afterWherePattern, ` AND ${filterConditions} $1`);
      } else {
        // No GROUP BY, ORDER BY etc., just append to end
        sql += ` AND ${filterConditions}`;
      }
    } else {
      // Add WHERE clause
      const afterWherePattern = /\s+(GROUP\s+BY|ORDER\s+BY|HAVING|LIMIT|OFFSET)/i;
      const match = sql.match(afterWherePattern);

      if (match) {
        // Insert WHERE clause before the matched clause
        sql = sql.replace(afterWherePattern, ` WHERE ${filterConditions} $1`);
      } else {
        // No GROUP BY, ORDER BY etc., just append to end
        sql += ` WHERE ${filterConditions}`;
      }
    }

    return sql;
  };

  // Separate function to fetch chart data (can be called with filters)
  const fetchChartData = async (dataset, currentFilters) => {
    setChartLoading(true);
    setChartError("");

    try {
      // Apply filters to the SQL query
      const filteredSQL = applyFiltersToSQL(dataset.sql_query, currentFilters);
      console.log("Original SQL:", dataset.sql_query);
      console.log("Filtered SQL:", filteredSQL);

      // Use the /api/run-sql endpoint to get SQL results
      const response = await axios.post("/api/ddb/run-sql", {
        query: filteredSQL,
      });

      // Store the SQL result data for Gemini
      const sqlResultData = response.data.rows || [];
      console.log("SQL Result data:", sqlResultData);

      if (sqlResultData.length === 0) {
        setChartData([]);
        setChartError("No data returned from SQL query");
        setFullData([]); // Clear Gemini data too
        return;
      }

      // Get the column names
      const columns = Object.keys(sqlResultData[0]);
      console.log("Available columns:", columns);

      // Transform the data into chart format
      let transformedChartData = [];

      // FIXED: Enhanced logic to properly identify label and count columns
      if (columns.length === 2) {
        // Identify which column is likely the category (label) and which is the count/metric
        const countColumnPattern = /^(count|total|sum|cnt|avg|quantity|number|frequency)$/i;
        let labelCol = columns[0];
        let countCol = columns[1];

        if (countColumnPattern.test(columns[0]) || columns[0].toLowerCase().includes('count') ||
            columns[0].toLowerCase().includes('total') || columns[0].toLowerCase().includes('sum')) {
          // If first column looks like count, swap
          labelCol = columns[1];
          countCol = columns[0];
        } else if (countColumnPattern.test(columns[1]) || columns[1].toLowerCase().includes('count') ||
            columns[1].toLowerCase().includes('total') || columns[1].toLowerCase().includes('sum')) {
          // Otherwise if second column looks like count, leave as is
          // (labelCol and countCol already set above)
        } else {
          // Fallback: if both are ambiguous, use type check on first row
          const row = sqlResultData[0];
          const col0Numeric = typeof row[columns[0]] === 'number' || !isNaN(Number(row[columns[0]]));
          const col1Numeric = typeof row[columns[1]] === 'number' || !isNaN(Number(row[columns[1]]));
          if (col0Numeric && !col1Numeric) {
            labelCol = columns[1];
            countCol = columns[0];
          } else if (!col0Numeric && col1Numeric) {
            // already correct
          }
          // if both same type leave as is (positional fallback)
        }

        transformedChartData = sqlResultData.map((row) => ({
          label: String(row[labelCol]),
          count: Number(row[countCol]) || 0,
        }));
        console.log("Chart data (2 columns):", transformedChartData);
      }
      // If we have more than 2 columns, try to find a count column
      else if (columns.length > 2) {
        // Look for common count column names
        const countColumn = columns.find(
          (col) =>
            col.toLowerCase().includes("count") ||
            col.toLowerCase().includes("total") ||
            col.toLowerCase().includes("sum") ||
            col.toLowerCase() === "cnt"
        );

        if (countColumn) {
          // Use first column as label and found count column as count
          const labelCol = columns[0];
          transformedChartData = sqlResultData.map((row) => ({
            label: String(row[labelCol]),
            count: Number(row[countColumn]) || 0,
          }));
          console.log("Chart data (count column found):", transformedChartData);
        } else {
          // If no count column found, count occurrences of first column
          const labelCol = columns[0];
          const groupedData = sqlResultData.reduce((acc, row) => {
            const label = String(row[labelCol]);
            acc[label] = (acc[label] || 0) + 1;
            return acc;
          }, {});

          transformedChartData = Object.entries(groupedData).map(
            ([label, count]) => ({
              label,
              count,
            })
          );
          console.log("Chart data (grouped):", transformedChartData);
        }
      }
      // If only 1 column, count occurrences
      else if (columns.length === 1) {
        const labelCol = columns[0];
        const groupedData = sqlResultData.reduce((acc, row) => {
          const label = String(row[labelCol]);
          acc[label] = (acc[label] || 0) + 1;
          return acc;
        }, {});

        transformedChartData = Object.entries(groupedData).map(
          ([label, count]) => ({
            label,
            count,
          })
        );
      } else {
        throw new Error("No data columns found");
      }

      setChartData(transformedChartData);

      // Update fullData with the current (possibly filtered) SQL result for Gemini Q&A
      setFullData(sqlResultData);
      console.log("Updated fullData for Gemini:", sqlResultData.length, "rows");
    } catch (err) {
      setChartError(err.response?.data?.error || err.message);
      setChartData([]);
      setFullData([]); // Clear Gemini data on error
    } finally {
      setChartLoading(false);
    }
  };

  // Function to handle dataset selection and fetch chart data
  const handleDatasetClick = async (dataset) => {
    setSelectedDataset(dataset);
    setShowChart(true);
    setShowFilters(false);
    setFilters([]);
    setChartLoading(true);
    setChartError("");
    setAnswer(""); // Reset Q&A answer
    setQuestion("");
    setChartType("bar"); // Reset to default chart type

    // Extract available columns for filtering
    const columns = extractColumnsFromSQL(dataset.sql_query);
    setAvailableColumns(columns);

    // Note: fullData will be populated by fetchChartData
    await fetchChartData(dataset, []);
  };

  // Enhanced Gemini Q&A handler with auto-chart creation
  const handleAskGemini = async () => {
    if (!question.trim() || !selectedDataset) return;

    console.log("Asking Gemini with data:", {
      question,
      dataRows: fullData.length,
      availableColumns: availableColumns.length,
      selectedDataset: selectedDataset.dataset_name,
    });

    setIsAsking(true);
    setAnswer("");
    setGeminiResponse(null);
    setShowAutoChartOption(false);

    try {
      const columns = availableColumns;
      const sql = selectedDataset.sql_query;
      const data = fullData;

      if (!data || data.length === 0) {
        setAnswer(
          "No data available for analysis. Please make sure the SQL query returns results."
        );
        return;
      }

      const geminiResult = await askGemini({
        apiKey: GEMINI_API_KEY,
        question,
        columns,
        sql,
        data,
      });

      // Store the full response
      setGeminiResponse(geminiResult);
      setAnswer(geminiResult.text);

      // Show auto-chart option if Gemini recommended a chart
      if (geminiResult.hasRecommendation && geminiResult.recommendedChart) {
        setShowAutoChartOption(true);
      }
    } catch (err) {
      setAnswer("Error getting answer from Gemini: " + err.message);
      setGeminiResponse(null);
    } finally {
      setIsAsking(false);
    }
  };

  // Function to apply Gemini's chart recommendation
  const applyGeminiChartRecommendation = () => {
    if (geminiResponse && geminiResponse.recommendedChart) {
      setChartType(geminiResponse.recommendedChart);
      setShowAutoChartOption(false);

      // Show a brief confirmation
      const originalAnswer = answer;
      setAnswer(
        originalAnswer +
          "\n\n✅ Chart type automatically updated to " +
          geminiResponse.recommendedChart.charAt(0).toUpperCase() +
          geminiResponse.recommendedChart.slice(1) +
          " Chart based on AI recommendation!"
      );

      // Reset the message after 3 seconds
      setTimeout(() => {
        setAnswer(originalAnswer);
      }, 3000);
    }
  };

  // Function to get smart chart recommendation without asking Gemini
  const getSmartChartRecommendation = () => {
    if (fullData && fullData.length > 0) {
      const recommendedChart = getAutomaticChartRecommendation(
        fullData,
        availableColumns
      );
      setChartType(recommendedChart);

      // Show feedback
      setAnswer(
        (prev) =>
          (prev || "") +
          `\n\n🤖 Smart recommendation: Switched to ${
            recommendedChart.charAt(0).toUpperCase() + recommendedChart.slice(1)
          } Chart based on your data characteristics.`
      );
    }
  };

  // Function to handle filter changes and refresh chart
  const handleFilterChange = async () => {
    if (selectedDataset) {
      await fetchChartData(selectedDataset, filters);
    }
  };

  // Function to add a new filter
  const addFilter = () => {
    setFilters([
      ...filters,
      {
        column: availableColumns[0] || "",
        operator: "=",
        value: "",
        logic: "AND",
      },
    ]);
  };

  // Function to remove a filter
  const removeFilter = (index) => {
    const newFilters = filters.filter((_, i) => i !== index);
    setFilters(newFilters);
    // Auto-refresh chart when filter is removed
    setTimeout(() => {
      if (selectedDataset) {
        fetchChartData(selectedDataset, newFilters);
      }
    }, 100);
  };

  // Function to update a filter
  const updateFilter = (index, field, value) => {
    const newFilters = filters.map((filter, i) =>
      i === index ? { ...filter, [field]: value } : filter
    );
    setFilters(newFilters);
  };

  // Function to close chart
  const handleCloseChart = () => {
    setShowChart(false);
    setSelectedDataset(null);
    setChartData([]);
    setChartError("");
    setShowFilters(false);
    setFilters([]);
    setAvailableColumns([]);
  };

  useEffect(() => {
    // Fetch datasets list
    axios
      .get("/api/ddb/dataset-master")
      .then((res) => {
        setDatasets(res.data);
      })
      .catch((err) => setError(err.response?.data?.error || err.message))
      .finally(() => setLoading(false));
  }, []);

  // Function to render the selected chart type
  const renderChart = () => {
    if (!chartData || chartData.length === 0) return null;

    const colors = ["#314b89", "#5a7bb5", "#8da4d1", "#b8c8e1", "#e0e7ef"];

    switch (chartType) {
      case "line":
        return (
          <ResponsiveContainer width="100%" height="100%">
            <LineChart
              data={chartData}
              margin={{
                top: 20,
                right: 30,
                left: 20,
                bottom: 60,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e0e7ef" />
              <XAxis
                dataKey="label"
                angle={-45}
                textAnchor="end"
                height={80}
                fontSize={12}
                stroke="#314b89"
              />
              <YAxis
                label={{
                  value: "Count",
                  angle: -90,
                  position: "insideLeft",
                }}
                fontSize={12}
                stroke="#314b89"
              />
              <Tooltip
                formatter={(value, name) => [value, "Count"]}
                labelFormatter={(label) => `Category: ${label}`}
                contentStyle={{
                  backgroundColor: "#f8f9fa",
                  border: "1px solid #314b89",
                  borderRadius: "4px",
                }}
              />
              <Legend />
              <Line
                type="monotone"
                dataKey="count"
                stroke="#314b89"
                strokeWidth={3}
                dot={{ fill: "#314b89", strokeWidth: 2, r: 4 }}
                name="Count"
              />
            </LineChart>
          </ResponsiveContainer>
        );

      case "pie":
        return (
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={chartData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ label, percent }) =>
                  `${label} (${(percent * 100).toFixed(0)}%)`
                }
                outerRadius={120}
                fill="#314b89"
                dataKey="count"
              >
                {chartData.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={colors[index % colors.length]}
                  />
                ))}
              </Pie>
              <Tooltip
                formatter={(value, name) => [value, "Count"]}
                contentStyle={{
                  backgroundColor: "#f8f9fa",
                  border: "1px solid #314b89",
                  borderRadius: "4px",
                }}
              />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        );

      case "bar":
      default:
        return (
          <ResponsiveContainer width="100%" height="100%">
            <BarChart
              data={chartData}
              margin={{
                top: 20,
                right: 30,
                left: 20,
                bottom: 60,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e0e7ef" />
              <XAxis
                dataKey="label"
                angle={-45}
                textAnchor="end"
                height={80}
                fontSize={12}
                stroke="#314b89"
              />
              <YAxis
                label={{
                  value: "Count",
                  angle: -90,
                  position: "insideLeft",
                }}
                fontSize={12}
                stroke="#314b89"
              />
              <Tooltip
                formatter={(value, name) => [value, "Count"]}
                labelFormatter={(label) => `Category: ${label}`}
                contentStyle={{
                  backgroundColor: "#f8f9fa",
                  border: "1px solid #314b89",
                  borderRadius: "4px",
                }}
              />
              <Legend />
              <Bar
                dataKey="count"
                fill="#314b89"
                name="Count"
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        );
    }
  };

  return (
    <div className="page-main-card">
      {/* Chart Section - Only show when dataset is selected */}
      {showChart && (
        <div
          className="section-card"
          style={{ maxWidth: 900, margin: "0 auto", marginBottom: 30 }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginBottom: 20,
            }}
          >
            <h2 className="section-header">
              Chart for: {selectedDataset?.dataset_name}
            </h2>
            <div style={{ display: "flex", gap: 10, alignItems: "center" }}>
              {/* Chart Type Selection */}
              <select
                value={chartType}
                onChange={(e) => setChartType(e.target.value)}
                style={{
                  padding: "8px 12px",
                  borderRadius: 4,
                  border: "1px solid #314b89",
                  background: "#fff",
                  color: "#314b89",
                  cursor: "pointer",
                  fontSize: 14,
                  fontWeight: 500,
                }}
              >
                <option value="bar">Bar Chart</option>
                <option value="line">Line Chart</option>
                <option value="pie">Pie Chart</option>
              </select>

              {/* Smart Chart Recommendation Button */}
              <button
                onClick={getSmartChartRecommendation}
                style={{
                  padding: "8px 12px",
                  borderRadius: 4,
                  border: "1px solid #28a745",
                  background: "#28a745",
                  color: "#fff",
                  cursor: "pointer",
                  fontSize: 12,
                  fontWeight: 500,
                }}
                title="Get automatic chart recommendation based on your data"
              >
                🤖 Smart Chart
              </button>

              <button
                onClick={() => setShowFilters(!showFilters)}
                style={{
                  padding: "8px 16px",
                  borderRadius: 4,
                  border: "1px solid #314b89",
                  background: showFilters ? "#314b89" : "#fff",
                  color: showFilters ? "#fff" : "#314b89",
                  cursor: "pointer",
                  fontSize: 14,
                  fontWeight: 500,
                }}
              >
                {showFilters ? "Hide Filters" : "Configure Filters"}
              </button>
              <button
                onClick={handleCloseChart}
                style={{
                  padding: "8px 16px",
                  borderRadius: 4,
                  border: "1px solid #dc3545",
                  background: "#dc3545",
                  color: "#fff",
                  cursor: "pointer",
                  fontSize: 14,
                  fontWeight: 500,
                }}
              >
                Close Chart
              </button>
            </div>
          </div>

          {/* Filter Configuration Section */}
          {showFilters && (
            <div
              style={{
                marginBottom: 20,
                padding: 15,
                border: "1px solid #e0e7ef",
                borderRadius: 6,
                backgroundColor: "#f8f9fa",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  marginBottom: 15,
                }}
              >
                <h3 style={{ margin: 0, color: "#314b89" }}>
                  Filter Configuration
                </h3>
                <div style={{ display: "flex", gap: 10 }}>
                  <button
                    onClick={addFilter}
                    style={{
                      padding: "6px 12px",
                      borderRadius: 4,
                      border: "1px solid #28a745",
                      background: "#28a745",
                      color: "#fff",
                      cursor: "pointer",
                      fontSize: 13,
                      fontWeight: 500,
                    }}
                  >
                    + Add Filter
                  </button>
                  <button
                    onClick={handleFilterChange}
                    style={{
                      padding: "6px 12px",
                      borderRadius: 4,
                      border: "1px solid #314b89",
                      background: "#314b89",
                      color: "#fff",
                      cursor: "pointer",
                      fontSize: 13,
                      fontWeight: 500,
                    }}
                  >
                    Apply Filters
                  </button>
                </div>
              </div>

              {filters.length === 0 ? (
                <div style={{ color: "#888", fontStyle: "italic" }}>
                  No filters configured. Click "Add Filter" to create WHERE
                  conditions.
                </div>
              ) : (
                <div>
                  {filters.map((filter, idx) => (
                    <div
                      key={idx}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        gap: 8,
                        marginBottom: 10,
                        padding: 8,
                        backgroundColor: "#fff",
                        borderRadius: 4,
                        border: "1px solid #e0e7ef",
                      }}
                    >
                      {idx > 0 && (
                        <select
                          value={filter.logic || "AND"}
                          onChange={(e) =>
                            updateFilter(idx, "logic", e.target.value)
                          }
                          style={{
                            fontSize: 13,
                            fontWeight: 600,
                            padding: 4,
                            borderRadius: 3,
                          }}
                        >
                          <option value="AND">AND</option>
                          <option value="OR">OR</option>
                        </select>
                      )}
                      <select
                        value={filter.column}
                        onChange={(e) =>
                          updateFilter(idx, "column", e.target.value)
                        }
                        style={{ fontSize: 13, padding: 4, minWidth: 120 }}
                      >
                        <option value="">Select Column</option>
                        {availableColumns.map((col) => (
                          <option key={col} value={col}>
                            {col}
                          </option>
                        ))}
                      </select>
                      <select
                        value={filter.operator}
                        onChange={(e) =>
                          updateFilter(idx, "operator", e.target.value)
                        }
                        style={{ fontSize: 13, padding: 4 }}
                      >
                        {operators.map((op) => (
                          <option key={op} value={op}>
                            {op}
                          </option>
                        ))}
                      </select>
                      {filter.operator &&
                        !["IS NULL", "IS NOT NULL"].includes(filter.operator) &&
                        (filter.operator === "BETWEEN" ? (
                          <>
                            <input
                              type="text"
                              value={
                                Array.isArray(filter.value)
                                  ? filter.value[0] || ""
                                  : ""
                              }
                              placeholder="Start"
                              onChange={(e) => {
                                const start = e.target.value;
                                updateFilter(idx, "value", [
                                  start,
                                  Array.isArray(filter.value)
                                    ? filter.value[1]
                                    : "",
                                ]);
                              }}
                              style={{
                                width: 60,
                                marginRight: 4,
                                padding: 4,
                                fontSize: 13,
                                borderRadius: 3,
                                border: "1px solid #ddd",
                              }}
                            />
                            <span>and</span>
                            <input
                              type="text"
                              value={
                                Array.isArray(filter.value)
                                  ? filter.value[1] || ""
                                  : ""
                              }
                              placeholder="End"
                              onChange={(e) => {
                                const end = e.target.value;
                                updateFilter(idx, "value", [
                                  Array.isArray(filter.value)
                                    ? filter.value[0]
                                    : "",
                                  end,
                                ]);
                              }}
                              style={{
                                width: 60,
                                marginLeft: 4,
                                padding: 4,
                                fontSize: 13,
                                borderRadius: 3,
                                border: "1px solid #ddd",
                              }}
                            />
                          </>
                        ) : (
                          <input
                            type="text"
                            value={filter.value || ""}
                            placeholder="Value"
                            onChange={(e) =>
                              updateFilter(idx, "value", e.target.value)
                            }
                            style={{
                              width: 120,
                              padding: 4,
                              fontSize: 13,
                              borderRadius: 3,
                              border: "1px solid #ddd",
                            }}
                          />
                        ))}
                      <button
                        onClick={() => removeFilter(idx)}
                        style={{
                          color: "#fff",
                          background: "#dc3545",
                          border: "none",
                          padding: "4px 8px",
                          borderRadius: 3,
                          cursor: "pointer",
                          fontSize: 12,
                        }}
                      >
                        Remove
                      </button>
                    </div>
                  ))}
                  <div
                    style={{
                      marginTop: 10,
                      padding: 8,
                      backgroundColor: "#e7f3ff",
                      borderRadius: 4,
                      fontSize: 12,
                      color: "#0066cc",
                    }}
                  >
                    <strong>Preview:</strong> The filters will be added as WHERE
                    conditions to your SQL query. Click "Apply Filters" to
                    refresh the chart with filtered data.
                  </div>
                </div>
              )}
            </div>
          )}

          {chartLoading && <div>Loading chart...</div>}
          {chartError && (
            <div style={{ color: "red" }}>Chart Error: {chartError}</div>
          )}
          {!chartLoading && !chartError && chartData.length > 0 && (
            <div>
              <div style={{ marginBottom: 15, fontSize: 14, color: "#666" }}>
                Data visualization from SQL query results ({chartData.length}{" "}
                data points) -{" "}
                {chartType.charAt(0).toUpperCase() + chartType.slice(1)} Chart
              </div>
              <div style={{ width: "100%", height: "400px" }}>
                {renderChart()}
              </div>

              {/* Enhanced Gemini Q&A Section */}
              <div
                style={{
                  marginTop: 32,
                  padding: 20,
                  background:
                    "linear-gradient(135deg, #f9f9fc 0%, #f0f4ff 100%)",
                  borderRadius: 8,
                  border: "1px solid #e0e7ef",
                  boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                }}
              >
                <div
                  style={{
                    fontWeight: 600,
                    color: "#314b89",
                    marginBottom: 12,
                    fontSize: 16,
                    display: "flex",
                    alignItems: "center",
                    gap: 8,
                  }}
                >
                  🤖 AI Data Analyst
                  <span
                    style={{ fontSize: 12, fontWeight: 400, color: "#666" }}
                  >
                    Ask questions and get automatic chart recommendations
                  </span>
                </div>

                <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
                  <input
                    type="text"
                    value={question}
                    onChange={(e) => setQuestion(e.target.value)}
                    style={{
                      flex: 1,
                      padding: "10px 12px",
                      fontSize: 15,
                      borderRadius: 6,
                      border: "1px solid #b8c3e1",
                      outline: "none",
                      transition: "border-color 0.3s ease",
                    }}
                    placeholder="e.g., What patterns do you see? Which chart type is best for this data?"
                    onKeyDown={(e) => {
                      if (e.key === "Enter") handleAskGemini();
                    }}
                    onFocus={(e) => (e.target.style.borderColor = "#314b89")}
                    onBlur={(e) => (e.target.style.borderColor = "#b8c3e1")}
                  />
                  <button
                    onClick={handleAskGemini}
                    style={{
                      padding: "10px 20px",
                      borderRadius: 6,
                      background: isAsking
                        ? "linear-gradient(135deg, #9ca3af 0%, #6b7280 100%)"
                        : "linear-gradient(135deg, #314b89 0%, #1e40af 100%)",
                      color: "#fff",
                      border: "none",
                      fontWeight: 600,
                      fontSize: 15,
                      cursor: isAsking ? "wait" : "pointer",
                      transition: "all 0.3s ease",
                      minWidth: 80,
                    }}
                    disabled={isAsking || !question.trim()}
                  >
                    {isAsking ? "🤔" : "Ask AI"}
                  </button>
                </div>

                {/* Auto-chart recommendation notification */}
                {showAutoChartOption && geminiResponse && (
                  <div
                    style={{
                      background:
                        "linear-gradient(135deg, #10b981 0%, #059669 100%)",
                      color: "#ffffff",
                      padding: "12px 16px",
                      borderRadius: 6,
                      marginBottom: 12,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "space-between",
                      boxShadow: "0 2px 8px rgba(16, 185, 129, 0.3)",
                    }}
                  >
                    <span style={{ fontWeight: 500, fontSize: 14 }}>
                      🎯 AI recommends:{" "}
                      <strong>
                        {geminiResponse.recommendedChart.toUpperCase()} CHART
                      </strong>{" "}
                      for your data
                    </span>
                    <button
                      onClick={applyGeminiChartRecommendation}
                      style={{
                        background: "rgba(255, 255, 255, 0.2)",
                        color: "#ffffff",
                        border: "1px solid rgba(255, 255, 255, 0.3)",
                        padding: "6px 12px",
                        borderRadius: 4,
                        fontSize: 12,
                        fontWeight: 600,
                        cursor: "pointer",
                        transition: "all 0.2s ease",
                      }}
                      onMouseEnter={(e) => {
                        e.target.style.background = "rgba(255, 255, 255, 0.3)";
                      }}
                      onMouseLeave={(e) => {
                        e.target.style.background = "rgba(255, 255, 255, 0.2)";
                      }}
                    >
                      Apply Now
                    </button>
                  </div>
                )}

                {answer && (
                  <div
                    style={{
                      background: "#ffffff",
                      borderRadius: 6,
                      padding: 16,
                      color: "#2d3748",
                      fontSize: 15,
                      whiteSpace: "pre-wrap",
                      border: "1px solid #e0e7ef",
                      lineHeight: 1.6,
                      boxShadow: "inset 0 1px 3px rgba(0,0,0,0.05)",
                    }}
                  >
                    <div
                      style={{
                        fontWeight: 600,
                        color: "#314b89",
                        marginBottom: 8,
                        borderBottom: "1px solid #e0e7ef",
                        paddingBottom: 8,
                      }}
                    >
                      🤖 AI Analysis:
                    </div>
                    {answer}
                  </div>
                )}

                {/* Quick question suggestions */}
                {!answer && (
                  <div style={{ marginTop: 12 }}>
                    <div
                      style={{ fontSize: 13, color: "#666", marginBottom: 8 }}
                    >
                      💡 Quick suggestions:
                    </div>
                    <div style={{ display: "flex", flexWrap: "wrap", gap: 6 }}>
                      {[
                        "What patterns do you see in this data?",
                        "Which chart type is best for this data?",
                        "What insights can you provide?",
                        "Are there any trends or outliers?",
                      ].map((suggestion, idx) => (
                        <button
                          key={idx}
                          onClick={() => setQuestion(suggestion)}
                          style={{
                            background: "rgba(49, 75, 137, 0.1)",
                            color: "#314b89",
                            border: "1px solid rgba(49, 75, 137, 0.2)",
                            padding: "4px 8px",
                            borderRadius: 4,
                            fontSize: 12,
                            cursor: "pointer",
                            transition: "all 0.2s ease",
                          }}
                          onMouseEnter={(e) => {
                            e.target.style.background =
                              "rgba(49, 75, 137, 0.15)";
                          }}
                          onMouseLeave={(e) => {
                            e.target.style.background =
                              "rgba(49, 75, 137, 0.1)";
                          }}
                        >
                          {suggestion}
                        </button>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {!chartLoading && !chartError && chartData.length === 0 && (
            <div style={{ textAlign: "center", color: "#888", padding: 20 }}>
              No chart data available for this dataset.
              <br />
              <small style={{ fontSize: 12, color: "#aaa" }}>
                Make sure the SQL query returns data and check the browser
                console for details.
              </small>
            </div>
          )}
        </div>
      )}

      {/* Table Section */}
      <div className="section-card" style={{ maxWidth: 900, margin: "0 auto" }}>
        <h2 className="section-header">Dataset Master Table Entries</h2>
        <div
          style={{
            marginBottom: 15,
            fontSize: 14,
            color: "#666",
            fontStyle: "italic",
          }}
        >
          Click on a dataset name to view its chart visualization
        </div>
        {loading && <div>Loading...</div>}
        {error && <div style={{ color: "red" }}>{error}</div>}
        {!loading && !error && (
          <table
            style={{ width: "100%", borderCollapse: "collapse", marginTop: 18 }}
          >
            <thead>
              <tr style={{ background: "#e0e7ef" }}>
                <th style={{ padding: 8, border: "1px solid #b8c3e1" }}>ID</th>
                <th style={{ padding: 8, border: "1px solid #b8c3e1" }}>
                  Dataset Name
                </th>
                <th style={{ padding: 8, border: "1px solid #b8c3e1" }}>
                  SQL Query
                </th>
                <th style={{ padding: 8, border: "1px solid #b8c3e1" }}>
                  Created On
                </th>
                <th style={{ padding: 8, border: "1px solid #b8c3e1" }}>
                  Created By
                </th>
              </tr>
            </thead>
            <tbody>
              {datasets.length === 0 ? (
                <tr>
                  <td
                    colSpan={5}
                    style={{ textAlign: "center", color: "#888", padding: 18 }}
                  >
                    No entries found.
                  </td>
                </tr>
              ) : (
                datasets.map((ds) => (
                  <tr key={ds.id}>
                    <td style={{ padding: 8, border: "1px solid #e0e7ef" }}>
                      {ds.id}
                    </td>
                    <td style={{ padding: 8, border: "1px solid #e0e7ef" }}>
                      <button
                        onClick={() => handleDatasetClick(ds)}
                        style={{
                          background: "none",
                          border: "none",
                          color: "#314b89",
                          textDecoration: "underline",
                          cursor: "pointer",
                          fontSize: "inherit",
                          fontFamily: "inherit",
                          padding: 0,
                        }}
                      >
                        {ds.dataset_name}
                      </button>
                    </td>
                    <td
                      style={{
                        padding: 8,
                        border: "1px solid #e0e7ef",
                        maxWidth: 300,
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                      }}
                      title={ds.sql_query}
                    >
                      {ds.sql_query}
                    </td>
                    <td style={{ padding: 8, border: "1px solid #e0e7ef" }}>
                      {ds.created_on
                        ? new Date(ds.created_on).toLocaleString()
                        : ""}
                    </td>
                    <td style={{ padding: 8, border: "1px solid #e0e7ef" }}>
                      {ds.created_by ?? ""}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
