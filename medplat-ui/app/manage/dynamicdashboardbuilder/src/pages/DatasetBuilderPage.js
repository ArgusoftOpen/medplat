import React, { useState, useEffect } from "react";
import axios from "axios";
import Select from "react-select";

export default function DatasetBuilderPage() {
  const [tables, setTables] = useState([]);
  const [selectedTables, setSelectedTables] = useState([]);
  const [metadata, setMetadata] = useState({});
  const [sampleData, setSampleData] = useState({});
  const [joins, setJoins] = useState([]);
  // Column selection and renaming
  const [columnSelections, setColumnSelections] = useState({}); // { table: [{ column_name, selected, alias, order }] }

  // Derived columns (formulas)
  const [derivedColumns, setDerivedColumns] = useState([]); // [{ name, formula }]
  const [newDerived, setNewDerived] = useState({ name: "", formula: "" });

  // SQL preview/export and data preview
  const [sqlPreview, setSqlPreview] = useState("");
  const [previewData, setPreviewData] = useState([]);
  const [previewLoading, setPreviewLoading] = useState(false);
  const [previewError, setPreviewError] = useState("");

  // WHERE clause builder
  const [whereClauses, setWhereClauses] = useState([]); // [{ table, column, operator, value, logic }]
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
    "BETWEEN",
    "IS NULL",
    "IS NOT NULL",
  ];

  // Group By state
  const [groupByColumns, setGroupByColumns] = useState([]); // [{ table, column_name }]

  // Helper: Generate SQL
  function generateSQL() {
    if (selectedTables.length === 0) return "";

    // Get all selected columns with their order
    const allSelectedColumns = selectedTables.flatMap((table) =>
      (
        columnSelections[table] ||
        (metadata[table] || []).map((c, index) => ({
          column_name: c.column_name,
          selected: true,
          alias: c.column_name,
          aggregate: "",
          order: index,
          table: table,
        }))
      )
        .filter((c) => c.selected !== false)
        .map((c) => ({ ...c, table }))
    );

    // Sort columns by their order
    allSelectedColumns.sort((a, b) => (a.order || 0) - (b.order || 0));

    const selectCols = allSelectedColumns.map((c) => {
      // eslint-disable-next-line no-unused-vars
      const isGrouped = groupByColumns.some(
        (g) => g.table === c.table && g.column_name === c.column_name
      );
      let colExpr = `${c.table}."${c.column_name}"`;

      // Apply aggregate function if specified
      if (c.aggregate && c.aggregate !== "") {
        if (c.aggregate === "COUNT DISTINCT") {
          colExpr = `COUNT(DISTINCT ${colExpr})`;
        } else {
          colExpr = `${c.aggregate}(${colExpr})`;
        }
      }

      // Apply alias if different from column name
      if (c.alias && c.alias !== c.column_name) {
        colExpr += ` AS "${c.alias}"`;
      }

      return colExpr;
    });

    const derivedCols = derivedColumns.map(
      (dc) => `${dc.formula} AS "${dc.name}"`
    );
    const selectClause = [...selectCols, ...derivedCols].join(", ") || "*";
    // GROUP BY clause
    let groupByClause = "";
    if (groupByColumns.length > 0) {
      groupByClause =
        " GROUP BY " +
        groupByColumns.map((c) => `${c.table}."${c.column_name}"`).join(", ");
    }
    // FROM and JOINs
    let fromClause = selectedTables[0];
    let joinClauses = "";
    joins.forEach((j) => {
      if (!j.leftTable || !j.leftColumn || !j.rightTable || !j.rightColumn)
        return;
      joinClauses += ` ${j.joinType} JOIN ${j.rightTable} ON ${j.leftTable}."${j.leftColumn}" = ${j.rightTable}."${j.rightColumn}"`;
    });
    // WHERE clause
    let whereClause = "";
    if (whereClauses.length > 0) {
      whereClause =
        " WHERE " +
        whereClauses
          .map((w, idx) => {
            let expr = `${w.table}."${w.column}"`;
            if (w.operator === "IS NULL" || w.operator === "IS NOT NULL") {
              expr += ` ${w.operator}`;
            } else if (w.operator === "IN" || w.operator === "NOT IN") {
              expr += ` ${w.operator} (${w.value})`;
            } else if (w.operator === "LIKE" || w.operator === "ILIKE") {
              expr += ` ${w.operator} '%${w.value}%'`;
            } else if (w.operator === "BETWEEN") {
              if (Array.isArray(w.value) && w.value.length === 2) {
                expr += ` BETWEEN '${w.value[0]}' AND '${w.value[1]}'`;
              } else {
                expr += ` BETWEEN '' AND ''`;
              }
            } else {
              expr += ` ${w.operator} '${w.value}'`;
            }
            const logic = idx > 0 ? ` ${w.logic || "AND"} ` : "";
            return logic + expr;
          })
          .join("");
    }
    return `SELECT ${selectClause} FROM ${fromClause}${joinClauses}${whereClause}${groupByClause}`;
  }

  // Preview handler
  const handlePreview = async () => {
    setSqlPreview("");
    setPreviewData([]);
    setPreviewError("");
    setPreviewLoading(true);
    try {
      const sql = generateSQL();
      setSqlPreview(sql);
      const res = await axios.post("/api/ddb/preview-sql", { sql });
      setPreviewData(res.data.rows || []);
    } catch (err) {
      setPreviewError(err.response?.data?.error || err.message);
    } finally {
      setPreviewLoading(false);
    }
  };

  // Save handler (placeholder)
  const handleSave = async () => {
    const tableName = `dataset_${new Date()
      .toISOString()
      .replace(/[:.]/g, "_")}`;
    try {
      const res = await axios.post("http://localhost:8181/api/ddb/save-dataset", {
        sql: sqlPreview,
        tableName,
      });
      if (res.data.success) {
        alert(`Table '${tableName}' created.`);
      } else {
        alert(res.data.error);
      }
    } catch (err) {
      alert(err.response?.data?.error || err.message);
    }
  };

  // Fetch all tables on mount
  useEffect(() => {
    axios
      .get("/api/ddb/tables")
      .then((res) => setTables(res.data))
      .catch(() => setTables([]));
  }, []);

  // Fetch metadata and sample data for selected tables
  useEffect(() => {
    selectedTables.forEach((table) => {
      if (!metadata[table]) {
        axios
          .get(`/api/ddb/table-metadata/${table}`)
          .then((res) => setMetadata((m) => ({ ...m, [table]: res.data })));
      }
      if (!sampleData[table]) {
        axios
          .get(`/api/ddb/sample-data/${table}?limit=10`)
          .then((res) => setSampleData((d) => ({ ...d, [table]: res.data })));
      }
    });
  }, [selectedTables, metadata, sampleData]);

  // Stepper state
  const [step, setStep] = useState(1);
  const steps = [
    { label: "Select Tables" },
    { label: "Columns & Joins" },
    { label: "Filters" },
    { label: "Group By & Derived" },
    { label: "Preview & Save" },
  ];

  // Helper function to move column up in order
  const moveColumnUp = (table, columnName) => {
    setColumnSelections((cs) => {
      const columns = cs[table] || [];
      const selectedColumns = columns.filter((c) => c.selected !== false);
      const sortedSelected = [...selectedColumns].sort(
        (a, b) => (a.order || 0) - (b.order || 0)
      );

      const currentIndex = sortedSelected.findIndex(
        (c) => c.column_name === columnName
      );
      if (currentIndex > 0) {
        const currentCol = sortedSelected[currentIndex];
        const prevCol = sortedSelected[currentIndex - 1];

        const newColumns = columns.map((c) => {
          if (c.column_name === currentCol.column_name) {
            return { ...c, order: prevCol.order };
          }
          if (c.column_name === prevCol.column_name) {
            return { ...c, order: currentCol.order };
          }
          return c;
        });

        return { ...cs, [table]: newColumns };
      }
      return cs;
    });
  };

  // Helper function to move column down in order
  const moveColumnDown = (table, columnName) => {
    setColumnSelections((cs) => {
      const columns = cs[table] || [];
      const selectedColumns = columns.filter((c) => c.selected !== false);
      const sortedSelected = [...selectedColumns].sort(
        (a, b) => (a.order || 0) - (b.order || 0)
      );

      const currentIndex = sortedSelected.findIndex(
        (c) => c.column_name === columnName
      );
      if (currentIndex < sortedSelected.length - 1 && currentIndex >= 0) {
        const currentCol = sortedSelected[currentIndex];
        const nextCol = sortedSelected[currentIndex + 1];

        const newColumns = columns.map((c) => {
          if (c.column_name === currentCol.column_name) {
            return { ...c, order: nextCol.order };
          }
          if (c.column_name === nextCol.column_name) {
            return { ...c, order: currentCol.order };
          }
          return c;
        });

        return { ...cs, [table]: newColumns };
      }
      return cs;
    });
  };

  // Helper function to get sorted columns for display
  const getSortedColumns = (table) => {
    const columns =
      columnSelections[table] ||
      (metadata[table] || []).map((c, index) => ({
        column_name: c.column_name,
        selected: true,
        alias: c.column_name,
        aggregate: "",
        order: index,
      }));
    return [...columns].sort((a, b) => (a.order || 0) - (b.order || 0));
  };

  return (
    <div className="page-main-card">
      {/* Stepper Navigation */}
      <div
        className="wizard-stepper"
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          gap: 16,
          marginBottom: 36,
        }}
      >
        {steps.map((s, idx) => {
          const isStepEnabled =
            idx === 0 ||
            (idx === 1 && selectedTables.length > 0) ||
            (idx === 2 && selectedTables.length > 0) ||
            (idx === 3 && selectedTables.length > 0) ||
            (idx === 4 && selectedTables.length > 0);
          return (
            <React.Fragment key={s.label}>
              <button
                className={
                  step === idx + 1
                    ? "step-active"
                    : isStepEnabled
                    ? "step-enabled"
                    : "step-disabled"
                }
                style={{
                  background:
                    step === idx + 1
                      ? "#314b89"
                      : isStepEnabled
                      ? "#e0e7ef"
                      : "#f2f2f2",
                  color: step === idx + 1 ? "#fff" : "#314b89",
                  border: "none",
                  borderRadius: 18,
                  padding: "8px 22px",
                  fontWeight: 600,
                  fontSize: 16,
                  cursor: isStepEnabled ? "pointer" : "not-allowed",
                  boxShadow: step === idx + 1 ? "0 2px 8px #b8c3e1" : "none",
                  outline: step === idx + 1 ? "2px solid #314b89" : "none",
                  transition: "all 0.18s",
                }}
                disabled={!isStepEnabled}
                onClick={() => isStepEnabled && setStep(idx + 1)}
              >
                Step {idx + 1}: {s.label}
              </button>
              {idx !== steps.length - 1 && (
                <span
                  style={{ fontWeight: 700, color: "#b8c3e1", fontSize: 22 }}
                >
                  -
                </span>
              )}
            </React.Fragment>
          );
        })}
      </div>

      {/* Step 1: Select Tables */}
      {step === 1 && (
        <div
          className="section-card"
          style={{
            marginBottom: 24,
            maxWidth: 600,
            marginLeft: "auto",
            marginRight: "auto",
          }}
        >
          <label>
            <b>Select Tables:</b>
          </label>
          <div style={{ maxWidth: 450, marginBottom: 10 }}>
            <Select
              isMulti
              isSearchable
              placeholder="Select tables..."
              options={tables.map((t) => ({ value: t, label: t }))}
              value={selectedTables.map((t) => ({ value: t, label: t }))}
              onChange={(opts) => {
                setSelectedTables(opts ? opts.map((o) => o.value) : []);
              }}
              styles={{ menu: (base) => ({ ...base, zIndex: 9999 }) }}
            />
          </div>
          <div
            style={{
              display: "flex",
              justifyContent: "flex-end",
              marginTop: 24,
            }}
          >
            <button
              className="btn-next"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#314b89",
                color: "#fff",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: selectedTables.length > 0 ? "pointer" : "not-allowed",
              }}
              disabled={selectedTables.length === 0}
              onClick={() => setStep(2)}
            >
              Next
            </button>
          </div>
        </div>
      )}

      {/* Step 2: Columns & Joins */}
      {step === 2 && (
        <div>
          {/* Table Preview and Column Selection */}
          {selectedTables.map((table) => (
            <div
              key={table}
              className="section-card"
              style={{ marginBottom: 40 }}
            >
              <h3 className="section-header-table">{table}</h3>
              <div style={{ marginBottom: 10 }}>
                <b className="section-label">Columns (Select & Rename):</b>
                <div style={{ marginBottom: 10, maxWidth: 450 }}>
                  <Select
                    isMulti
                    isSearchable
                    placeholder="Select columns..."
                    options={(metadata[table] || []).map((col) => ({
                      value: col.column_name,
                      label: `${col.column_name} (${col.data_type})`,
                    }))}
                    value={(columnSelections[table] || [])
                      .filter((c) => c.selected)
                      .map((c) => ({
                        value: c.column_name,
                        label: `${c.column_name} (${
                          (metadata[table] || []).find(
                            (mc) => mc.column_name === c.column_name
                          )?.data_type || ""
                        })`,
                      }))}
                    onChange={(selectedOpts) => {
                      const selectedCols = (selectedOpts || []).map(
                        (opt) => opt.value
                      );
                      setColumnSelections((cs) => ({
                        ...cs,
                        [table]: (metadata[table] || []).map((c, index) => ({
                          column_name: c.column_name,
                          selected: selectedCols.includes(c.column_name),
                          alias:
                            (cs[table] || []).find(
                              (col) => col.column_name === c.column_name
                            )?.alias || c.column_name,
                          aggregate:
                            (cs[table] || []).find(
                              (col) => col.column_name === c.column_name
                            )?.aggregate || "",
                          order:
                            (cs[table] || []).find(
                              (col) => col.column_name === c.column_name
                            )?.order ?? index,
                        })),
                      }));
                    }}
                    styles={{ menu: (base) => ({ ...base, zIndex: 9999 }) }}
                  />
                  <div style={{ marginTop: 8, display: "flex", gap: 8 }}>
                    <button
                      type="button"
                      style={{
                        padding: "3px 12px",
                        borderRadius: 4,
                        border: "1px solid #bbb",
                        background: "#314b89",
                        color: "#fff",
                        cursor: "pointer",
                        fontWeight: 500,
                      }}
                      onClick={() =>
                        setColumnSelections((cs) => ({
                          ...cs,
                          [table]: (metadata[table] || []).map((c, index) => ({
                            column_name: c.column_name,
                            selected: true,
                            alias: c.column_name,
                            aggregate: "",
                            order: index,
                          })),
                        }))
                      }
                    >
                      Select All
                    </button>
                    <button
                      type="button"
                      style={{
                        padding: "3px 12px",
                        borderRadius: 4,
                        border: "1px solid #bbb",
                        background: "#dc3545",
                        color: "#fff",
                        cursor: "pointer",
                        fontWeight: 500,
                      }}
                      onClick={() =>
                        setColumnSelections((cs) => ({
                          ...cs,
                          [table]: (metadata[table] || []).map((c, index) => ({
                            column_name: c.column_name,
                            selected: false,
                            alias:
                              (cs[table] || []).find(
                                (col) => col.column_name === c.column_name
                              )?.alias || c.column_name,
                            aggregate:
                              (cs[table] || []).find(
                                (col) => col.column_name === c.column_name
                              )?.aggregate || "",
                            order:
                              (cs[table] || []).find(
                                (col) => col.column_name === c.column_name
                              )?.order ?? index,
                          })),
                        }))
                      }
                    >
                      Clear All
                    </button>
                  </div>
                </div>
                <div>
                  <b className="section-label">Configure Selected Columns:</b>
                  <table
                    style={{
                      borderCollapse: "collapse",
                      fontSize: 15,
                      margin: "10px 0",
                      width: "100%",
                    }}
                  >
                    <thead>
                      <tr>
                        <th
                          style={{
                            border: "1px solid #eee",
                            padding: 5,
                            background: "#f5e9e9ff",
                          }}
                        >
                          Order
                        </th>
                        <th
                          style={{
                            border: "1px solid #eee",
                            padding: 5,
                            background: "#ece1e1ff",
                          }}
                        >
                          Column
                        </th>
                        <th
                          style={{
                            border: "1px solid #eee",
                            padding: 5,
                            background: "#ecdbdbff",
                          }}
                        >
                          Aggregate Function
                        </th>
                        <th
                          style={{
                            border: "1px solid #eee",
                            padding: 5,
                            background: "#e8d6d6ff",
                          }}
                        >
                          Alias
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {getSortedColumns(table)
                        .filter((c) => c.selected !== false)
                        .map((col, index) => {
                          const sortedColumns = getSortedColumns(table).filter(
                            (c) => c.selected !== false
                          );
                          const isFirst = index === 0;
                          const isLast = index === sortedColumns.length - 1;
                          return (
                            <tr key={col.column_name}>
                              <td
                                style={{
                                  border: "1px solid #eee",
                                  padding: 5,
                                  textAlign: "center",
                                }}
                              >
                                <div
                                  style={{
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    gap: 5,
                                  }}
                                >
                                  <button
                                    type="button"
                                    onClick={() =>
                                      moveColumnUp(table, col.column_name)
                                    }
                                    disabled={isFirst}
                                    style={{
                                      padding: "2px 6px",
                                      fontSize: 12,
                                      border: "1px solid #ddd",
                                      background: isFirst
                                        ? "#be2121ff"
                                        : "#be2121ff",
                                      cursor: isFirst
                                        ? "not-allowed"
                                        : "pointer",
                                      borderRadius: 3,
                                    }}
                                    title="Move up"
                                  >
                                    ↑
                                  </button>
                                  <span
                                    style={{
                                      minWidth: 20,
                                      textAlign: "center",
                                      fontWeight: 600,
                                    }}
                                  >
                                    {index + 1}
                                  </span>
                                  <button
                                    type="button"
                                    onClick={() =>
                                      moveColumnDown(table, col.column_name)
                                    }
                                    disabled={isLast}
                                    style={{
                                      padding: "2px 6px",
                                      fontSize: 12,
                                      border: "1px solid #ddd",
                                      background: isLast
                                        ? "#be2121ff"
                                        : "#be2121ff",
                                      cursor: isLast
                                        ? "not-allowed"
                                        : "pointer",
                                      borderRadius: 3,
                                    }}
                                    title="Move down"
                                  >
                                    ↓
                                  </button>
                                </div>
                              </td>
                              <td
                                style={{
                                  border: "1px solid #eee",
                                  padding: 5,
                                  fontFamily: "monospace",
                                }}
                              >
                                {col.column_name}
                              </td>
                              <td
                                style={{ border: "1px solid #eee", padding: 5 }}
                              >
                                <select
                                  value={col.aggregate || ""}
                                  onChange={(e) => {
                                    setColumnSelections((cs) => ({
                                      ...cs,
                                      [table]: (
                                        cs[table] ||
                                        (metadata[table] || []).map(
                                          (c, idx) => ({
                                            column_name: c.column_name,
                                            selected: true,
                                            alias: c.column_name,
                                            aggregate: "",
                                            order: idx,
                                          })
                                        )
                                      ).map((c) =>
                                        c.column_name === col.column_name
                                          ? { ...c, aggregate: e.target.value }
                                          : c
                                      ),
                                    }));
                                  }}
                                  style={{
                                    width: "100%",
                                    padding: 4,
                                    fontSize: 14,
                                  }}
                                >
                                  <option value="">None</option>
                                  <option value="COUNT">COUNT</option>
                                  <option value="SUM">SUM</option>
                                  <option value="AVG">AVG</option>
                                  <option value="MIN">MIN</option>
                                  <option value="MAX">MAX</option>
                                  <option value="COUNT DISTINCT">
                                    COUNT DISTINCT
                                  </option>
                                </select>
                              </td>
                              <td
                                style={{ border: "1px solid #eee", padding: 5 }}
                              >
                                <input
                                  type="text"
                                  value={col.alias}
                                  style={{
                                    width: "100%",
                                    padding: 4,
                                    fontSize: 14,
                                  }}
                                  onChange={(e) => {
                                    setColumnSelections((cs) => ({
                                      ...cs,
                                      [table]: (
                                        cs[table] ||
                                        (metadata[table] || []).map(
                                          (c, idx) => ({
                                            column_name: c.column_name,
                                            selected: true,
                                            alias: c.column_name,
                                            aggregate: "",
                                            order: idx,
                                          })
                                        )
                                      ).map((c) =>
                                        c.column_name === col.column_name
                                          ? { ...c, alias: e.target.value }
                                          : c
                                      ),
                                    }));
                                  }}
                                />
                              </td>
                            </tr>
                          );
                        })}
                    </tbody>
                  </table>
                  <div
                    style={{
                      marginTop: 10,
                      padding: 8,
                      background: "#f0f8ff",
                      borderRadius: 4,
                      fontSize: 13,
                    }}
                  >
                    <b>Note:</b> Use the ↑↓ arrows to reorder columns. The order
                    here determines the column order in your final dataset. When
                    using aggregate functions, consider adding Group By columns
                    in Step 4 to group your data appropriately.
                  </div>
                </div>
              </div>
              <div>
                <b>Sample Data:</b>
                <div
                  className="table-scroll"
                  style={{
                    maxHeight: 300,
                    overflowY: "auto",
                    overflowX: "auto",
                    border: "1px solid #e0e7ef",
                    borderRadius: 4,
                    marginTop: 8,
                  }}
                >
                  <table
                    className="data-table"
                    style={{
                      width: "100%",
                      borderCollapse: "collapse",
                      fontSize: 13,
                      minWidth: "max-content",
                    }}
                  >
                    <thead>
                      <tr>
                        {sampleData[table] && sampleData[table][0]
                          ? Object.keys(sampleData[table][0]).map((col) => (
                              <th
                                key={col}
                                style={{
                                  border: "1px solid #eee",
                                  padding: "6px 8px",
                                  background: "#f8f8f8",
                                  fontSize: 12,
                                  fontWeight: 600,
                                  whiteSpace: "nowrap",
                                  maxWidth: 150,
                                  overflow: "hidden",
                                  textOverflow: "ellipsis",
                                }}
                                title={col}
                              >
                                {col}
                              </th>
                            ))
                          : null}
                      </tr>
                    </thead>
                    <tbody>
                      {(sampleData[table] || []).length === 0 ? (
                        <tr>
                          <td
                            colSpan={
                              sampleData[table] && sampleData[table][0]
                                ? Object.keys(sampleData[table][0]).length
                                : 1
                            }
                            style={{
                              color: "#aaa",
                              textAlign: "center",
                              padding: 20,
                              fontStyle: "italic",
                            }}
                          >
                            No data available
                          </td>
                        </tr>
                      ) : (
                        sampleData[table].slice(0, 5).map((row, i) => (
                          <tr key={i}>
                            {Object.keys(row).map((col) => (
                              <td
                                key={col}
                                style={{
                                  border: "1px solid #eee",
                                  padding: "4px 8px",
                                  fontSize: 12,
                                  maxWidth: 150,
                                  overflow: "hidden",
                                  textOverflow: "ellipsis",
                                  whiteSpace: "nowrap",
                                }}
                                title={String(row[col])}
                              >
                                {String(row[col])}
                              </td>
                            ))}
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          ))}

          {/* --- Join Configuration Section --- */}
          {selectedTables.length > 1 && (
            <div className="section-card" style={{ marginBottom: 32 }}>
              <h3
                className="section-header-accent"
                style={{ marginBottom: 10 }}
              >
                Join Configuration
              </h3>
              <button
                type="button"
                onClick={() =>
                  setJoins([
                    ...joins,
                    {
                      leftTable: selectedTables[0],
                      leftColumn:
                        metadata[selectedTables[0]]?.[0]?.column_name || "",
                      rightTable: selectedTables[1],
                      rightColumn:
                        metadata[selectedTables[1]]?.[0]?.column_name || "",
                      joinType: "INNER",
                    },
                  ])
                }
                style={{
                  marginBottom: 12,
                  padding: "5px 14px",
                  borderRadius: 4,
                  border: "1px solid #bbb",
                  background: "#28a745",
                  color: "#fff",
                  cursor: "pointer",
                  fontWeight: 500,
                }}
              >
                + Add Join
              </button>
              {joins.length === 0 && (
                <div style={{ color: "#aaa", marginBottom: 10 }}>
                  No joins defined yet.
                </div>
              )}
              {joins.map((join, idx) => (
                <div
                  key={idx}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    gap: 10,
                    marginBottom: 10,
                  }}
                >
                  <select
                    value={join.leftTable}
                    onChange={(e) => {
                      const val = e.target.value;
                      setJoins((js) =>
                        js.map((j, i) =>
                          i === idx
                            ? {
                                ...j,
                                leftTable: val,
                                leftColumn:
                                  metadata[val]?.[0]?.column_name || "",
                              }
                            : j
                        )
                      );
                    }}
                    style={{ fontSize: 15, padding: 4 }}
                  >
                    {selectedTables.map((t) => (
                      <option key={t} value={t}>
                        {t}
                      </option>
                    ))}
                  </select>
                  <span>.</span>
                  <select
                    value={join.leftColumn}
                    onChange={(e) => {
                      const val = e.target.value;
                      setJoins((js) =>
                        js.map((j, i) =>
                          i === idx ? { ...j, leftColumn: val } : j
                        )
                      );
                    }}
                    style={{ fontSize: 15, padding: 4 }}
                  >
                    {(metadata[join.leftTable] || []).map((col) => (
                      <option key={col.column_name} value={col.column_name}>
                        {col.column_name}
                      </option>
                    ))}
                  </select>
                  <select
                    value={join.joinType}
                    onChange={(e) => {
                      const val = e.target.value;
                      setJoins((js) =>
                        js.map((j, i) =>
                          i === idx ? { ...j, joinType: val } : j
                        )
                      );
                    }}
                    style={{ fontSize: 15, padding: 4, fontWeight: 600 }}
                  >
                    <option value="INNER">INNER</option>
                    <option value="LEFT">LEFT</option>
                    <option value="RIGHT">RIGHT</option>
                    <option value="FULL">FULL</option>
                  </select>
                  <select
                    value={join.rightTable}
                    onChange={(e) => {
                      const val = e.target.value;
                      setJoins((js) =>
                        js.map((j, i) =>
                          i === idx
                            ? {
                                ...j,
                                rightTable: val,
                                rightColumn:
                                  metadata[val]?.[0]?.column_name || "",
                              }
                            : j
                        )
                      );
                    }}
                    style={{ fontSize: 15, padding: 4 }}
                  >
                    {selectedTables.map((t) => (
                      <option key={t} value={t}>
                        {t}
                      </option>
                    ))}
                  </select>
                  <span>.</span>
                  <select
                    value={join.rightColumn}
                    onChange={(e) => {
                      const val = e.target.value;
                      setJoins((js) =>
                        js.map((j, i) =>
                          i === idx ? { ...j, rightColumn: val } : j
                        )
                      );
                    }}
                    style={{ fontSize: 15, padding: 4 }}
                  >
                    {(metadata[join.rightTable] || []).map((col) => (
                      <option key={col.column_name} value={col.column_name}>
                        {col.column_name}
                      </option>
                    ))}
                  </select>
                  <button
                    type="button"
                    onClick={() =>
                      setJoins((js) => js.filter((_, i) => i !== idx))
                    }
                    style={{
                      color: "#fff",
                      background: "#e53935",
                      border: "none",
                      padding: "3px 10px",
                      borderRadius: 4,
                      marginLeft: 10,
                      cursor: "pointer",
                    }}
                  >
                    Remove
                  </button>
                </div>
              ))}
              {joins.length > 0 && (
                <div style={{ marginTop: 10, fontSize: 15 }}>
                  <b>Current Joins:</b>
                  <ul style={{ margin: "6px 0 0 10px" }}>
                    {joins.map((j, i) => (
                      <li key={i}>
                        {j.leftTable}.{j.leftColumn} <b>{j.joinType}</b>{" "}
                        {j.rightTable}.{j.rightColumn}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          )}

          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              marginTop: 32,
            }}
          >
            <button
              className="btn-back"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#e0e7ef",
                color: "#314b89",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: "pointer",
              }}
              onClick={() => setStep(1)}
            >
              Back
            </button>
            <button
              className="btn-next"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#314b89",
                color: "#fff",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: Object.values(columnSelections).some(
                  (cols) => cols && cols.some((c) => c.selected)
                )
                  ? "pointer"
                  : "not-allowed",
              }}
              disabled={
                !Object.values(columnSelections).some(
                  (cols) => cols && cols.some((c) => c.selected)
                )
              }
              onClick={() => setStep(3)}
            >
              Next
            </button>
          </div>
        </div>
      )}

      {/* Step 3: Filters (WHERE clause) */}
      {step === 3 && (
        <div
          className="section-card"
          style={{
            marginBottom: 24,
            maxWidth: 700,
            marginLeft: "auto",
            marginRight: "auto",
          }}
        >
          <b className="section-label">Filter Rows (WHERE Clause):</b>
          {whereClauses.map((w, idx) => (
            <div
              key={idx}
              style={{
                display: "flex",
                alignItems: "center",
                gap: 8,
                margin: "8px 0",
              }}
            >
              {idx > 0 && (
                <select
                  value={w.logic || "AND"}
                  onChange={(e) =>
                    setWhereClauses((clauses) =>
                      clauses.map((c, i) =>
                        i === idx ? { ...c, logic: e.target.value } : c
                      )
                    )
                  }
                  style={{ fontSize: 15, fontWeight: 600 }}
                >
                  <option value="AND">AND</option>
                  <option value="OR">OR</option>
                </select>
              )}
              <select
                value={w.table}
                onChange={(e) =>
                  setWhereClauses((clauses) =>
                    clauses.map((c, i) =>
                      i === idx
                        ? { ...c, table: e.target.value, column: "" }
                        : c
                    )
                  )
                }
                style={{ fontSize: 15 }}
              >
                <option value="Table">Table</option>
                {selectedTables.map((t) => (
                  <option key={t} value={t}>
                    {t}
                  </option>
                ))}
              </select>
              <select
                value={w.column}
                onChange={(e) =>
                  setWhereClauses((clauses) =>
                    clauses.map((c, i) =>
                      i === idx ? { ...c, column: e.target.value } : c
                    )
                  )
                }
                style={{ fontSize: 15 }}
              >
                <option value="">Column</option>
                {(metadata[w.table] || []).map((col) => (
                  <option key={col.column_name} value={col.column_name}>
                    {col.column_name}
                  </option>
                ))}
              </select>
              <select
                value={w.operator}
                onChange={(e) =>
                  setWhereClauses((clauses) =>
                    clauses.map((c, i) =>
                      i === idx ? { ...c, operator: e.target.value } : c
                    )
                  )
                }
                style={{ fontSize: 15 }}
              >
                <option value="">Operator</option>
                {operators.map((op) => (
                  <option key={op} value={op}>
                    {op}
                  </option>
                ))}
              </select>
              {w.operator &&
                !["IS NULL", "IS NOT NULL"].includes(w.operator) &&
                (w.operator === "BETWEEN" ? (
                  <>
                    <input
                      type="text"
                      value={Array.isArray(w.value) ? w.value[0] || "" : ""}
                      placeholder="Start"
                      onChange={(e) => {
                        const start = e.target.value;
                        setWhereClauses((clauses) =>
                          clauses.map((c, i) =>
                            i === idx
                              ? {
                                  ...c,
                                  value: [
                                    start,
                                    Array.isArray(c.value) ? c.value[1] : "",
                                  ],
                                }
                              : c
                          )
                        );
                      }}
                      style={{ width: 60, marginRight: 4 }}
                    />
                    <span>and</span>
                    <input
                      type="text"
                      value={Array.isArray(w.value) ? w.value[1] || "" : ""}
                      placeholder="End"
                      onChange={(e) => {
                        const end = e.target.value;
                        setWhereClauses((clauses) =>
                          clauses.map((c, i) =>
                            i === idx
                              ? {
                                  ...c,
                                  value: [
                                    Array.isArray(c.value) ? c.value[0] : "",
                                    end,
                                  ],
                                }
                              : c
                          )
                        );
                      }}
                      style={{ width: 60, marginLeft: 4 }}
                    />
                  </>
                ) : (
                  <input
                    type="text"
                    value={w.value || ""}
                    placeholder="Value"
                    onChange={(e) =>
                      setWhereClauses((clauses) =>
                        clauses.map((c, i) =>
                          i === idx ? { ...c, value: e.target.value } : c
                        )
                      )
                    }
                    style={{ width: 120 }}
                  />
                ))}
              <button
                type="button"
                onClick={() =>
                  setWhereClauses((clauses) =>
                    clauses.filter((_, i) => i !== idx)
                  )
                }
                style={{
                  color: "#fff",
                  background: "#e53935",
                  border: "none",
                  padding: "2px 8px",
                  borderRadius: 4,
                  marginLeft: 4,
                  cursor: "pointer",
                  fontSize: 13,
                }}
              >
                Remove
              </button>
            </div>
          ))}
          <button
            type="button"
            onClick={() =>
              setWhereClauses((clauses) => [
                ...clauses,
                {
                  table: selectedTables[0] || "",
                  column: "",
                  operator: "",
                  value: "",
                  logic: "AND",
                },
              ])
            }
            style={{
              marginTop: 8,
              padding: "4px 14px",
              borderRadius: 4,
              border: "1px solid #bbb",
              background: "#fff",
              cursor: "pointer",
              fontWeight: 500,
            }}
          >
            + Add Condition
          </button>
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              marginTop: 32,
            }}
          >
            <button
              className="btn-back"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#e0e7ef",
                color: "#314b89",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: "pointer",
              }}
              onClick={() => setStep(2)}
            >
              Back
            </button>
            <button
              className="btn-next"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#314b89",
                color: "#fff",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: "pointer",
              }}
              onClick={() => setStep(4)}
            >
              Next
            </button>
          </div>
        </div>
      )}

      {/* Step 4: Group By & Derived Columns */}
      {step === 4 && (
        <div>
          <div
            className="section-card"
            style={{
              marginBottom: 24,
              maxWidth: 700,
              marginLeft: "auto",
              marginRight: "auto",
            }}
          >
            <b className="section-label">Group By Columns:</b>
            <div
              style={{
                marginBottom: 10,
                padding: 8,
                background: "#fff3cd",
                borderRadius: 4,
                fontSize: 13,
              }}
            >
              <b>Tip:</b> When you have aggregate functions (SUM, COUNT, AVG,
              etc.) applied to columns in Step 2, you should group by
              non-aggregated columns to get meaningful results.
            </div>
            <div style={{ margin: "10px 0" }}>
              <Select
                isMulti
                isSearchable
                placeholder="Select columns to group by..."
                options={selectedTables.flatMap((table) =>
                  (metadata[table] || []).map((col) => ({
                    value: `${table}.${col.column_name}`,
                    label: `${table}.${col.column_name}`,
                  }))
                )}
                value={groupByColumns.map((g) => ({
                  value: `${g.table}.${g.column_name}`,
                  label: `${g.table}.${g.column_name}`,
                }))}
                onChange={(opts) => {
                  setGroupByColumns(
                    (opts || []).map((opt) => {
                      const [table, column_name] = opt.value.split(".");
                      return { table, column_name };
                    })
                  );
                }}
                styles={{ menu: (base) => ({ ...base, zIndex: 9999 }) }}
              />
            </div>
          </div>

          {/* Derived Columns (Formula Builder) */}
          <div
            className="section-card"
            style={{
              margin: "30px 0",
              background: "#fff",
              padding: 20,
              borderRadius: 8,
              boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
            }}
          >
            <b className="section-label">Derived Columns (Formula Builder):</b>
            <div
              style={{
                margin: "10px 0 16px 0",
                display: "flex",
                alignItems: "center",
                gap: 10,
              }}
            >
              <input
                type="text"
                placeholder="Column Name"
                value={newDerived.name}
                style={{ width: 180, padding: 6, fontSize: 15 }}
                onChange={(e) =>
                  setNewDerived((d) => ({ ...d, name: e.target.value }))
                }
              />
              <input
                type="text"
                placeholder="Formula (e.g. col1 + col2)"
                value={newDerived.formula}
                style={{ width: 280, padding: 6, fontSize: 15 }}
                onChange={(e) =>
                  setNewDerived((d) => ({ ...d, formula: e.target.value }))
                }
              />
              <button
                type="button"
                onClick={() => {
                  if (newDerived.name && newDerived.formula) {
                    setDerivedColumns((cols) => [...cols, newDerived]);
                    setNewDerived({ name: "", formula: "" });
                  }
                }}
                style={{
                  padding: "6px 18px",
                  borderRadius: 4,
                  border: "1px solid #ddd",
                  background: "#fff",
                  cursor: "pointer",
                  fontWeight: 500,
                }}
              >
                + Add Derived Column
              </button>
            </div>
            {derivedColumns.length > 0 && (
              <div style={{ marginTop: 10 }}>
                <b>Derived Columns:</b>
                <ul style={{ margin: "8px 0 0 16px", fontSize: 15 }}>
                  {derivedColumns.map((col, i) => (
                    <li key={i}>
                      <b>{col.name}</b> ={" "}
                      <span style={{ fontFamily: "monospace" }}>
                        {col.formula}
                      </span>{" "}
                      <button
                        type="button"
                        onClick={() =>
                          setDerivedColumns((cols) =>
                            cols.filter((_, idx) => idx !== i)
                          )
                        }
                        style={{
                          color: "#fff",
                          background: "#e53935",
                          border: "none",
                          padding: "2px 8px",
                          borderRadius: 4,
                          marginLeft: 8,
                          cursor: "pointer",
                          fontSize: 13,
                        }}
                      >
                        Remove
                      </button>
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>

          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              marginTop: 32,
            }}
          >
            <button
              className="btn-back"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#e0e7ef",
                color: "#314b89",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: "pointer",
              }}
              onClick={() => setStep(3)}
            >
              Back
            </button>
            <button
              className="btn-next"
              style={{
                padding: "8px 22px",
                borderRadius: 6,
                background: "#314b89",
                color: "#fff",
                fontWeight: 600,
                fontSize: 16,
                border: "none",
                cursor: "pointer",
              }}
              onClick={() => setStep(5)}
            >
              Next
            </button>
          </div>
        </div>
      )}

      {/* Step 5: Preview & Save */}
      {step === 5 && (
        <div
          className="section-card"
          style={{
            margin: "30px 0",
            background: "#fff",
            padding: 20,
            borderRadius: 8,
          }}
        >
          <b className="section-label">SQL Preview:</b>
          <pre
            style={{
              background: "#f9f9f9",
              padding: 12,
              borderRadius: 4,
              fontSize: 13,
              overflow: "auto",
              maxHeight: 200,
              border: "1px solid #e0e7ef",
            }}
          >
            {sqlPreview}
          </pre>
          <button
            type="button"
            onClick={handlePreview}
            style={{
              marginBottom: 16,
              padding: "8px 16px",
              borderRadius: 4,
              border: "1px solid #314b89",
              background: "#314b89",
              color: "#fff",
              cursor: "pointer",
              fontWeight: 500,
            }}
          >
            Preview Data
          </button>
          <b className="section-label">Data Preview:</b>
          {previewLoading ? (
            <div style={{ color: "#888", margin: "10px 0" }}>Loading...</div>
          ) : previewError ? (
            <div
              style={{
                color: "red",
                margin: "10px 0",
                padding: 10,
                background: "#ffebee",
                borderRadius: 4,
              }}
            >
              <strong>Error:</strong> {previewError}
            </div>
          ) : previewData && previewData.length > 0 ? (
            <div>
              <div style={{ marginBottom: 8, fontSize: 14, color: "#666" }}>
                Showing {previewData.length} rows
              </div>
              <div
                className="table-scroll"
                style={{
                  maxHeight: 400,
                  overflowY: "auto",
                  overflowX: "auto",
                  marginBottom: 12,
                  border: "1px solid #e0e7ef",
                  borderRadius: 4,
                }}
              >
                <table
                  className="data-table"
                  style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    fontSize: 13,
                    minWidth: "max-content",
                  }}
                >
                  <thead
                    style={{
                      position: "sticky",
                      top: 0,
                      background: "#f8f8f8",
                      zIndex: 1,
                    }}
                  >
                    <tr>
                      {Object.keys(previewData[0]).map((col) => (
                        <th
                          key={col}
                          style={{
                            border: "1px solid #eee",
                            padding: "8px 10px",
                            background: "#f8f8f8",
                            fontSize: 12,
                            fontWeight: 600,
                            whiteSpace: "nowrap",
                            maxWidth: 200,
                            overflow: "hidden",
                            textOverflow: "ellipsis",
                          }}
                          title={col}
                        >
                          {col}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {previewData.map((row, i) => (
                      <tr
                        key={i}
                        style={{ background: i % 2 === 0 ? "#fff" : "#f9f9f9" }}
                      >
                        {Object.keys(row).map((col) => (
                          <td
                            key={col}
                            style={{
                              border: "1px solid #eee",
                              padding: "6px 10px",
                              fontSize: 12,
                              maxWidth: 200,
                              overflow: "hidden",
                              textOverflow: "ellipsis",
                              whiteSpace: "nowrap",
                            }}
                            title={String(row[col])}
                          >
                            {String(row[col])}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <div style={{ color: "#888", margin: "10px 0" }}>
              No data to preview.
            </div>
          )}
          <button
            type="button"
            onClick={handleSave}
            style={{
              marginTop: 16,
              background: "#314b89",
              color: "#fff",
              padding: "10px 24px",
              border: "none",
              borderRadius: 4,
              fontWeight: 500,
              fontSize: 16,
              cursor: "pointer",
            }}
          >
            Save Dataset
          </button>
        </div>
      )}

      {/* Column Selection Summary - Show on all steps after step 2 */}
      {selectedTables.length > 0 && step > 2 && (
        <div className="section-card" style={{ margin: "30px 0" }}>
          <b>Selected Columns for Output Dataset (in order):</b>
          <ul style={{ margin: "10px 0 0 16px", fontSize: 15 }}>
            {selectedTables.flatMap((table) => {
              const sortedColumns = getSortedColumns(table).filter(
                (c) => c.selected !== false
              );
              return sortedColumns.map((c, index) => (
                <li key={table + "." + c.column_name}>
                  <span style={{ color: "#888", fontSize: 13, marginRight: 8 }}>
                    #{index + 1}
                  </span>
                  <span style={{ color: "#314b89" }}>{table}</span>.
                  <span style={{ fontFamily: "monospace" }}>
                    {c.aggregate && c.aggregate !== ""
                      ? `${c.aggregate}(${c.column_name})`
                      : c.column_name}
                  </span>
                  {c.alias && c.alias !== c.column_name ? (
                    <span>
                      {" "}
                      as <b>{c.alias}</b>
                    </span>
                  ) : null}
                </li>
              ));
            })}
          </ul>
        </div>
      )}
    </div>
  );
}
