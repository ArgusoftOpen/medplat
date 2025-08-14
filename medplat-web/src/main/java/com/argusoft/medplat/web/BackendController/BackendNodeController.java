package com.argusoft.medplat.web.backendcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class BackendNodeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // POST /api/attribute-value
    @PostMapping("/attribute-value")
    public Map<String, Object> getAttributeValue(@RequestBody Map<String, Object> body) {
        String table = (String) body.get("table");
        String column = (String) body.get("column");
        String indicator = (String) body.get("indicator");

        if (indicator != null) {
            Integer value = jdbcTemplate.queryForObject(
                    "SELECT query_result FROM indicator_master WHERE indicator_name = ?",
                    new Object[]{indicator},
                    Integer.class
            );
            return Map.of("value", value);
        } else if (table != null && column != null) {
            Integer value = jdbcTemplate.queryForObject(
                    String.format("SELECT COUNT(%s) AS cnt FROM %s", column, table),
                    Integer.class
            );
            return Map.of("value", value);
        } else {
            throw new IllegalArgumentException("Missing table/column or indicator");
        }
    }

    // GET /api/tables
    @GetMapping("/tables")
    public List<String> getAllTables() {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // GET /api/columns/:table
    @GetMapping("/columns/{table}")
    public List<String> getColumns(@PathVariable String table) {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{table}, String.class);
    }

    // GET /api/table-metadata/:table
    @GetMapping("/table-metadata/{table}")
    public List<Map<String, Object>> getTableMetadata(@PathVariable String table) {
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ?";
        return jdbcTemplate.queryForList(sql, table);
    }

    // GET /api/sample-data/:table?limit=10
    @GetMapping("/sample-data/{table}")
    public List<Map<String, Object>> getSampleData(@PathVariable String table, @RequestParam(defaultValue = "10") int limit) {
        String sql = String.format("SELECT * FROM \"%s\" LIMIT %d", table, limit);
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/data
    @PostMapping("/data")
    public List<Map<String, Object>> getData(@RequestBody Map<String, Object> body) {
        String table = (String) body.get("table");
        List<String> columns = (List<String>) body.get("columns");
        if (table == null || columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Invalid table or columns");
        }
        String colString = String.join("\", \"", columns);
        String sql = String.format("SELECT \"%s\" FROM \"%s\"", colString, table);
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/preview-sql
    @PostMapping("/preview-sql")
    public Map<String, Object> previewSql(@RequestBody Map<String, Object> body) {
        String sql = (String) body.get("sql");
        if (sql == null || !sql.trim().toLowerCase().startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed.");
        }
        String previewSql = sql.trim().replaceAll(";*$", "");
        if (!previewSql.toLowerCase().contains("limit")) {
            previewSql += " LIMIT 20";
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(previewSql);
        return Map.of("rows", rows);
    }

    // POST /api/derived-attribute
    @PostMapping("/derived-attribute")
    public Map<String, Object> saveDerivedAttribute(@RequestBody Map<String, Object> body) {
        String derivedName = (String) body.get("derived_name");
        String formula = (String) body.get("formula");
        Object resultObj = body.get("result");
        if (derivedName == null || formula == null || resultObj == null) {
            throw new IllegalArgumentException("Missing derived_name, formula, or result");
        }
        Double resultVal;
        if (resultObj instanceof Number) {
            resultVal = ((Number) resultObj).doubleValue();
        } else {
            try {
                resultVal = Double.parseDouble(resultObj.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Result must be a valid decimal number");
            }
        }
        jdbcTemplate.update(
                "INSERT INTO derived_attributes (derived_name, formula, result) VALUES (?, ?, ?)",
                derivedName, formula, resultVal
        );
        return Map.of("success", true);
    }
    // GET /api/derived-attributes
    @GetMapping("/derived-attributes")
    public List<Map<String, Object>> getDerivedAttributes() {
        String sql = "SELECT * FROM derived_attributes ORDER BY created_on DESC";
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/indicator-master
    @PostMapping("/indicator-master")
    public Map<String, Object> saveIndicatorMaster(@RequestBody Map<String, Object> body) {
        String indicatorName = (String) body.get("indicator_name");
        String description = (String) body.get("description");
        String sqlQuery = (String) body.get("sql_query");
        Object createdBy = body.get("created_by");
        if (indicatorName == null || sqlQuery == null) {
            throw new IllegalArgumentException("Missing indicator_name or sql_query");
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery);
        Integer queryResult = null;
        if (!rows.isEmpty()) {
            Map<String, Object> firstRow = rows.get(0);
            for (Object v : firstRow.values()) {
                if (v instanceof Number && ((Number) v).intValue() == ((Number) v).doubleValue()) {
                    queryResult = ((Number) v).intValue();
                    break;
                }
                if (v instanceof String && v.toString().matches("\\d+")) {
                    queryResult = Integer.parseInt(v.toString());
                    break;
                }
            }
        }
        jdbcTemplate.update(
                "INSERT INTO indicator_master (indicator_name, description, sql_query, query_result, created_by) VALUES (?, ?, ?, ?, ?)",
                indicatorName, description, sqlQuery, queryResult, createdBy
        );
        return Map.of("success", true, "query_result", queryResult);
    }

    // GET /api/indicator-master
    @GetMapping("/indicator-master")
    public List<Map<String, Object>> getIndicatorMaster() {
        String sql = "SELECT * FROM indicator_master";
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/grouped-count
    @PostMapping("/grouped-count")
    public List<Map<String, Object>> getGroupedCount(@RequestBody Map<String, Object> body) {
        String table = (String) body.get("table");
        String xAxis = (String) body.get("xAxis");
        String indicator = (String) body.get("indicator");
        if (table == null || xAxis == null) {
            throw new IllegalArgumentException("Missing table or xAxis");
        }
        String sql;
        if (indicator != null) {
            sql = String.format(
                    "SELECT \"%s\" as xAxisValue, COUNT(\"%s\") as count FROM \"%s\" GROUP BY \"%s\" ORDER BY xAxisValue",
                    xAxis, indicator, table, xAxis
            );
        } else {
            sql = String.format(
                    "SELECT \"%s\" as xAxisValue, COUNT(*) as count FROM \"%s\" GROUP BY \"%s\" ORDER BY xAxisValue",
                    xAxis, table, xAxis
            );
        }
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/run-sql
    @PostMapping("/run-sql")
    public Map<String, Object> runSql(@RequestBody Map<String, Object> body) {
        String query = (String) body.get("query");
        if (query == null) throw new IllegalArgumentException("Missing SQL query");
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        List<String> fields = rows.isEmpty() ? new ArrayList<>() : new ArrayList<>(rows.get(0).keySet());
        return Map.of("rows", rows, "fields", fields);
    }

    // GET /api/test-db
    //testing the connection
    // @GetMapping("/test-db")
    // public Map<String, Object> testDb() {
    //     jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    //     return Map.of("success", true, "message", "Database connection successful!");
    // }

    // GET /api/dataset-master/:id
    @GetMapping("/dataset-master/{id}")
    public Map<String, Object> getDatasetMasterById(@PathVariable Integer id) {
        String sql = "SELECT * FROM dataset_master WHERE id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
        if (rows.isEmpty()) throw new NoSuchElementException("Not found");
        return rows.get(0);
    }

    // GET /api/dataset-master
    @GetMapping("/dataset-master")
    public List<Map<String, Object>> getDatasetMaster() {
        String sql = "SELECT * FROM dataset_master ORDER BY id DESC";
        return jdbcTemplate.queryForList(sql);
    }

    // POST /api/save-dataset
    @PostMapping("/save-dataset")
    public Map<String, Object> saveDataset(@RequestBody Map<String, Object> body) {
        String tableName = (String) body.get("tableName");
        String sql = (String) body.get("sql");
        if (tableName == null || sql == null || !sql.trim().toLowerCase().startsWith("select")) {
            throw new IllegalArgumentException("Invalid tableName or only SELECT queries are allowed.");
        }
        jdbcTemplate.update(
                "INSERT INTO dataset_master (dataset_name, sql_query, created_by) VALUES (?, ?, ?)",
                tableName, sql, null
        );
        return Map.of("success", true, "message", String.format("Dataset '%s' created.", tableName));
    }

    // POST /api/dataset-chart
    @PostMapping("/dataset-chart")
    public List<Map<String, Object>> getDatasetChart(@RequestBody Map<String, Object> body) {
        String sql = (String) body.get("sql");
        if (sql == null) throw new IllegalArgumentException("Invalid SQL query");
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        if (rows.isEmpty()) return new ArrayList<>();
        List<String> columns = new ArrayList<>(rows.get(0).keySet());
        List<Map<String, Object>> chartData = new ArrayList<>();
        if (columns.size() == 2) {
            String labelCol = columns.get(0);
            String countCol = columns.get(1);
            for (Map<String, Object> row : rows) {
                chartData.add(Map.of(
                        "label", String.valueOf(row.get(labelCol)),
                        "count", row.get(countCol) instanceof Number ? ((Number) row.get(countCol)).intValue() : 0
                ));
            }
        } else if (columns.size() > 2) {
            String countColumn = columns.stream().filter(col ->
                    col.toLowerCase().contains("count") ||
                            col.toLowerCase().contains("total") ||
                            col.toLowerCase().contains("sum") ||
                            col.equalsIgnoreCase("cnt")
            ).findFirst().orElse(null);
            String labelCol = columns.get(0);
            if (countColumn != null) {
                for (Map<String, Object> row : rows) {
                    chartData.add(Map.of(
                            "label", String.valueOf(row.get(labelCol)),
                            "count", row.get(countColumn) instanceof Number ? ((Number) row.get(countColumn)).intValue() : 0
                    ));
                }
            } else {
                Map<String, Integer> groupedData = new HashMap<>();
                for (Map<String, Object> row : rows) {
                    String label = String.valueOf(row.get(labelCol));
                    groupedData.put(label, groupedData.getOrDefault(label, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : groupedData.entrySet()) {
                    chartData.add(Map.of("label", entry.getKey(), "count", entry.getValue()));
                }
            }
        } else {
            String labelCol = columns.get(0);
            Map<String, Integer> groupedData = new HashMap<>();
            for (Map<String, Object> row : rows) {
                String label = String.valueOf(row.get(labelCol));
                groupedData.put(label, groupedData.getOrDefault(label, 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : groupedData.entrySet()) {
                chartData.add(Map.of("label", entry.getKey(), "count", entry.getValue()));
            }
        }
        return chartData;
    }
}