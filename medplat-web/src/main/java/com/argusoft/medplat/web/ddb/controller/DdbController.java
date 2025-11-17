package com.argusoft.medplat.web.ddb.controller;

import com.argusoft.medplat.web.ddb.service.DdbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Dynamic Dashboard Backend Controller
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@RestController
@RequestMapping("/api/ddb")
@Api(tags = "Dynamic Dashboard Backend APIs")
public class DdbController {

    @Autowired
    private DdbService ddbService;

    @PostMapping("/attribute-value")
    @ApiOperation(value = "Get attribute value from table/column or indicator")
    public Map<String, Object> getAttributeValue(
            @ApiParam(value = "Request body containing table, column or indicator details")
            @RequestBody Map<String, Object> body) {
        return ddbService.getAttributeValue(body);
    }

    @GetMapping("/tables")
    @ApiOperation(value = "Get all database tables")
    public List<String> getAllTables() {
        return ddbService.getAllTables();
    }

    @GetMapping("/columns/{table}")
    @ApiOperation(value = "Get columns for a specific table")
    public List<String> getColumns(
            @ApiParam(value = "Table name", required = true)
            @PathVariable String table) {
        return ddbService.getColumnsByTable(table);
    }

    @GetMapping("/table-metadata/{table}")
    @ApiOperation(value = "Get metadata for a specific table")
    public List<Map<String, Object>> getTableMetadata(
            @ApiParam(value = "Table name", required = true)
            @PathVariable String table) {
        return ddbService.getTableMetadata(table);
    }

    @GetMapping("/sample-data/{table}")
    @ApiOperation(value = "Get sample data from a table")
    public List<Map<String, Object>> getSampleData(
            @ApiParam(value = "Table name", required = true)
            @PathVariable String table,
            @ApiParam(value = "Number of records to fetch")
            @RequestParam(defaultValue = "10") int limit) {
        return ddbService.getSampleData(table, limit);
    }

    @PostMapping("/data")
    @ApiOperation(value = "Get data from table with specific columns")
    public List<Map<String, Object>> getData(
            @ApiParam(value = "Request body containing table name and columns")
            @RequestBody Map<String, Object> body) {
        return ddbService.getDataByTableAndColumns(body);
    }

    @PostMapping("/preview-sql")
    @ApiOperation(
            value = "Preview SELECT SQL query",
            notes = "Preview SQL query results with automatic limit. SECURITY: Only SELECT queries are allowed."
    )
    public Map<String, Object> previewSql(
            @ApiParam(value = "Request body containing SELECT SQL query only")
            @RequestBody Map<String, Object> body) {
        return ddbService.previewSql(body);
    }
    @PostMapping("/derived-attribute")
    @ApiOperation(value = "Save a new derived attribute")
    public Map<String, Object> saveDerivedAttribute(
            @ApiParam(value = "Request body containing derived attribute details")
            @RequestBody Map<String, Object> body) {
        return ddbService.saveDerivedAttribute(body);
    }

    @GetMapping("/derived-attributes")
    @ApiOperation(value = "Get all derived attributes")
    public List<Map<String, Object>> getDerivedAttributes() {
        return ddbService.getDerivedAttributes();
    }

    @PostMapping("/indicator-master")
    @ApiOperation(value = "Save a new indicator master")
    public Map<String, Object> saveIndicatorMaster(
            @ApiParam(value = "Request body containing indicator master details")
            @RequestBody Map<String, Object> body) {
        return ddbService.saveIndicatorMaster(body);
    }

    @GetMapping("/indicator-master")
    @ApiOperation(value = "Get all indicator masters")
    public List<Map<String, Object>> getIndicatorMaster() {
        return ddbService.getIndicatorMaster();
    }

    @PostMapping("/grouped-count")
    @ApiOperation(value = "Get grouped count data for charts")
    public List<Map<String, Object>> getGroupedCount(
            @ApiParam(value = "Request body containing table, xAxis and indicator")
            @RequestBody Map<String, Object> body) {
        return ddbService.getGroupedCount(body);
    }

    @PostMapping("/run-sql")
    @ApiOperation(
            value = "Execute SELECT SQL query",
            notes = "Executes SQL query and returns results. SECURITY: Only SELECT queries are allowed. " +
                    "DELETE, INSERT, UPDATE, DROP, ALTER and other modification operations are forbidden."
    )
    public Map<String, Object> runSql(
            @ApiParam(value = "Request body containing SELECT SQL query only")
            @RequestBody Map<String, Object> body) {
        return ddbService.runSql(body);
    }

    @GetMapping("/dataset-master/{id}")
    @ApiOperation(value = "Get dataset master by ID")
    public Map<String, Object> getDatasetMasterById(
            @ApiParam(value = "Dataset ID", required = true)
            @PathVariable Integer id) {
        return ddbService.getDatasetMasterById(id);
    }

    @GetMapping("/dataset-master")
    @ApiOperation(value = "Get all dataset masters")
    public List<Map<String, Object>> getDatasetMaster() {
        return ddbService.getDatasetMaster();
    }

    @PostMapping("/save-dataset")
    @ApiOperation(value = "Save a new dataset")
    public Map<String, Object> saveDataset(
            @ApiParam(value = "Request body containing dataset details")
            @RequestBody Map<String, Object> body) {
        return ddbService.saveDataset(body);
    }

    @PostMapping("/dataset-chart")
    @ApiOperation(value = "Get chart data from dataset")
    public List<Map<String, Object>> getDatasetChart(
            @ApiParam(value = "Request body containing SQL query")
            @RequestBody Map<String, Object> body) {
        return ddbService.getDatasetChart(body);
    }
}
