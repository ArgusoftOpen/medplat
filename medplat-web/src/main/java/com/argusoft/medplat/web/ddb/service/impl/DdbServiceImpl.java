package com.argusoft.medplat.web.ddb.service.impl;

import com.argusoft.medplat.web.ddb.dao.DdbDao;
import com.argusoft.medplat.web.ddb.dao.DerivedAttributeDao;
import com.argusoft.medplat.web.ddb.dao.IndicatorMasterDao;
import com.argusoft.medplat.web.ddb.dao.DatasetMasterDao;
import com.argusoft.medplat.web.ddb.mapper.DdbMapper;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;
import com.argusoft.medplat.web.ddb.service.DdbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Implements methods of DdbService for healthcare data analysis
 * @author argusoft
 * @since 23/08/2025 15:30
 */
@Service("DdbService")
public class DdbServiceImpl implements DdbService {

    private static final Logger log = LoggerFactory.getLogger(DdbServiceImpl.class);

    @Autowired
    private DdbDao ddbDao;

    @Autowired
    private DerivedAttributeDao derivedAttributeDao;

    @Autowired
    private IndicatorMasterDao indicatorMasterDao;

    @Autowired
    private DatasetMasterDao datasetMasterDao;

    @Override
    public Map<String, Object> getAttributeValue(Map<String, Object> requestBody) {
        String indicator = (String) requestBody.get("indicator");
        String table = (String) requestBody.get("table");
        String column = (String) requestBody.get("column");

        if (indicator != null) {
            Integer value = ddbDao.getAttributeValueByIndicator(indicator);
            return Collections.singletonMap("value", value != null ? value : 0);
        } else if (table != null && column != null) {
            Integer value = ddbDao.getAttributeValueByTableAndColumn(table, column);
            return Collections.singletonMap("value", value);
        } else {
            throw new IllegalArgumentException("Missing table/column or indicator");
        }
    }

    @Override
    public List<String> getAllTables() {
        return ddbDao.getAllTables();
    }

    @Override
    public List<String> getColumnsByTable(String tableName) {
        return ddbDao.getColumnsByTable(tableName);
    }

    @Override
    public List<Map<String, Object>> getTableMetadata(String tableName) {
        return ddbDao.getTableMetadata(tableName);
    }

    @Override
    public List<Map<String, Object>> getSampleData(String tableName, int limit) {
        return ddbDao.getSampleData(tableName, limit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDataByTableAndColumns(Map<String, Object> requestBody) {
        String table = (String) requestBody.get("table");
        List<String> columns = (List<String>) requestBody.get("columns");

        if (table == null || CollectionUtils.isEmpty(columns)) {
            throw new IllegalArgumentException("Invalid table or columns");
        }
        return ddbDao.getDataByTableAndColumns(table, columns);
    }

    @Override
    public Map<String, Object> previewSql(Map<String, Object> requestBody) {
        String sql = (String) requestBody.get("sql");
        if (sql == null) {
            throw new IllegalArgumentException("Missing SQL query");
        }

        // Security validation for healthcare data
        validateSqlSecurity(sql);

        String previewSql = sql.trim().replaceAll(";*$", "");
        if (!previewSql.toLowerCase().contains("limit")) {
            previewSql += " LIMIT 20";
        }

        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(previewSql);
        return Collections.singletonMap("rows", rows);
    }

    @Override
    @Transactional
    public Map<String, Object> saveDerivedAttribute(Map<String, Object> requestBody) {
        String derivedName = (String) requestBody.get("derived_name");
        String formula = (String) requestBody.get("formula");
        Object resultObj = requestBody.get("result");
        Object createdByObj = requestBody.get("created_by");
        Object modifiedByObj = requestBody.get("modified_by");

        if (derivedName == null || formula == null || resultObj == null) {
            throw new IllegalArgumentException("Missing derived_name, formula, or result");
        }

        Double resultVal = parseDoubleValue(resultObj);
        Integer createdBy = parseIntegerValue(createdByObj, 1);
        Integer modifiedBy = parseIntegerValue(modifiedByObj, createdBy);

        DerivedAttribute derivedAttribute = new DerivedAttribute();
        derivedAttribute.setDerivedName(derivedName);
        derivedAttribute.setFormula(formula);
        derivedAttribute.setResult(resultVal);
        derivedAttribute.setCreatedBy(createdBy);
        derivedAttribute.setModifiedBy(modifiedBy);

        derivedAttributeDao.createOrUpdate(derivedAttribute);
        return Collections.singletonMap("success", true);
    }

    @Override
    public List<Map<String, Object>> getDerivedAttributes() {
        List<DerivedAttribute> entities = derivedAttributeDao.getAllDerivedAttributes();
        return DdbMapper.convertDerivedAttributesToMaps(entities);
    }

    @Override
    @Transactional
    public Map<String, Object> saveIndicatorMaster(Map<String, Object> requestBody) {
        String indicatorName = (String) requestBody.get("indicator_name");
        String description = (String) requestBody.get("description");
        String sqlQuery = (String) requestBody.get("sql_query");
        Object createdByObj = requestBody.get("created_by");
        Object modifiedByObj = requestBody.get("modified_by");

        if (indicatorName == null || sqlQuery == null) {
            throw new IllegalArgumentException("Missing indicator_name or sql_query");
        }

        // Security validation for healthcare data
        validateSqlSecurity(sqlQuery);

        // Execute the SQL query to get result
        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(sqlQuery);
        Integer queryResult = extractQueryResult(rows);

        Integer createdBy = parseIntegerValue(createdByObj, 1);
        Integer modifiedBy = parseIntegerValue(modifiedByObj, createdBy);

        IndicatorMaster indicatorMaster = new IndicatorMaster();
        indicatorMaster.setIndicatorName(indicatorName);
        indicatorMaster.setDescription(description);
        indicatorMaster.setSqlQuery(sqlQuery);
        indicatorMaster.setQueryResult(queryResult);
        indicatorMaster.setCreatedBy(createdBy);
        indicatorMaster.setModifiedBy(modifiedBy);

        indicatorMasterDao.createOrUpdate(indicatorMaster);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("query_result", queryResult);
        return response;
    }

    @Override
    public List<Map<String, Object>> getIndicatorMaster() {
        List<IndicatorMaster> entities = indicatorMasterDao.getAllIndicatorMaster();
        return DdbMapper.convertIndicatorMastersToMaps(entities);
    }

    @Override
    public List<Map<String, Object>> getGroupedCount(Map<String, Object> requestBody) {
        String table = (String) requestBody.get("table");
        String xAxis = (String) requestBody.get("xAxis");
        String indicator = (String) requestBody.get("indicator");

        if (table == null || xAxis == null) {
            throw new IllegalArgumentException("Missing table or xAxis");
        }
        return ddbDao.getGroupedCount(table, xAxis, indicator);
    }

    @Override
    public Map<String, Object> runSql(Map<String, Object> requestBody) {
        String query = (String) requestBody.get("query");
        if (query == null) {
            throw new IllegalArgumentException("Missing SQL query");
        }

        // Enhanced security validation for healthcare data
        validateSqlSecurity(query);

        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(query);
        List<String> fields = CollectionUtils.isEmpty(rows) ?
                new ArrayList<>() : new ArrayList<>(rows.get(0).keySet());

        Map<String, Object> response = new HashMap<>();
        response.put("rows", rows);
        response.put("fields", fields);
        return response;
    }

    @Override
    public Map<String, Object> getDatasetMasterById(Integer id) {
        DatasetMaster entity = datasetMasterDao.retrieveById(id);
        if (entity == null) {
            throw new NoSuchElementException("Dataset not found");
        }
        return DdbMapper.convertDatasetMasterToMap(entity);
    }

    @Override
    public List<Map<String, Object>> getDatasetMaster() {
        List<DatasetMaster> entities = datasetMasterDao.getAllDatasetMaster();
        return DdbMapper.convertDatasetMastersToMaps(entities);
    }

    @Override
    @Transactional
    public Map<String, Object> saveDataset(Map<String, Object> requestBody) {
        String tableName = (String) requestBody.get("tableName");
        String sql = (String) requestBody.get("sql");
        Integer userId = (Integer) requestBody.getOrDefault("userId", 1);

        if (tableName == null || sql == null) {
            throw new IllegalArgumentException("Invalid tableName or sql");
        }

        // Security validation for healthcare data
        validateSqlSecurity(sql);

        DatasetMaster datasetMaster = new DatasetMaster();
        datasetMaster.setDatasetName(tableName);
        datasetMaster.setSqlQuery(sql);
        datasetMaster.setCreatedBy(userId);
        datasetMaster.setModifiedBy(userId);

        datasetMasterDao.createOrUpdate(datasetMaster);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", String.format("Dataset '%s' created.", tableName));
        return response;
    }

    @Override
    public List<Map<String, Object>> getDatasetChart(Map<String, Object> requestBody) {
        String sql = (String) requestBody.get("sql");
        if (sql == null) {
            throw new IllegalArgumentException("Invalid SQL query");
        }

        // Security validation for healthcare data
        validateSqlSecurity(sql);

        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(sql);
        if (CollectionUtils.isEmpty(rows)) {
            return new ArrayList<>();
        }

        return processChartData(rows);
    }

    // Security and Helper Methods

    /**
     * Comprehensive SQL validation for healthcare data security
     */
    private void validateSqlSecurity(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL query cannot be empty");
        }

        String sanitizedQuery = sanitizeSqlInput(query).toLowerCase();

        // Must start with SELECT for healthcare data protection
        if (!sanitizedQuery.startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed for healthcare data access");
        }

        // Check for forbidden keywords that could compromise healthcare data
        if (containsForbiddenKeywords(sanitizedQuery)) {
            throw new IllegalArgumentException("Query contains forbidden operations for healthcare data protection");
        }

        // Prevent multiple statements for healthcare security
        if (sanitizedQuery.split(";").length > 1) {
            throw new IllegalArgumentException("Multiple SQL statements are not allowed for healthcare data security");
        }

        // Length validation for healthcare queries
        if (query.length() > 10000) {
            throw new IllegalArgumentException("Query is too long. Maximum 10000 characters allowed for healthcare data queries");
        }
    }

    /**
     * Sanitize SQL input for healthcare data protection
     */
    private String sanitizeSqlInput(String query) {
        if (query == null) return null;

        // Remove multiple spaces, tabs, newlines
        query = query.replaceAll("\\s+", " ").trim();

        // Remove SQL comments for security
        query = query.replaceAll("--.*", "").replaceAll("/\\*.*?\\*/", "");

        return query;
    }

    /**
     * Check for forbidden SQL keywords in healthcare context
     */
    private boolean containsForbiddenKeywords(String query) {
        String[] forbiddenKeywords = {
                "delete", "insert", "update", "drop", "alter", "create", "truncate",
                "grant", "revoke", "commit", "rollback", "savepoint", "exec", "execute",
                "sp_", "xp_", "into outfile", "load_file", "dumpfile", "merge", "replace"
        };

        for (String keyword : forbiddenKeywords) {
            if (query.contains(keyword)) {
                log.warn("Forbidden keyword '{}' detected in healthcare data query", keyword);
                return true;
            }
        }
        return false;
    }

    private Double parseDoubleValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Result must be a valid decimal number");
            }
        }
    }

    private Integer parseIntegerValue(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            String strValue = ((String) value).trim();
            if (strValue.isEmpty()) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer value: " + strValue);
            }
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass().getSimpleName());
        }
    }

    private Integer extractQueryResult(List<Map<String, Object>> rows) {
        if (!CollectionUtils.isEmpty(rows)) {
            Map<String, Object> firstRow = rows.get(0);
            for (Object v : firstRow.values()) {
                if (v instanceof Number && ((Number) v).intValue() == ((Number) v).doubleValue()) {
                    return ((Number) v).intValue();
                }
                if (v instanceof String && v.toString().matches("\\d+")) {
                    return Integer.parseInt(v.toString());
                }
            }
        }
        return null;
    }

    private List<Map<String, Object>> processChartData(List<Map<String, Object>> rows) {
        List<String> columns = new ArrayList<>(rows.get(0).keySet());
        List<Map<String, Object>> chartData = new ArrayList<>();

        if (columns.size() == 2) {
            String labelCol = columns.get(0);
            String countCol = columns.get(1);
            for (Map<String, Object> row : rows) {
                Map<String, Object> chartRow = new HashMap<>();
                chartRow.put("label", String.valueOf(row.get(labelCol)));
                chartRow.put("count", row.get(countCol) instanceof Number ?
                        ((Number) row.get(countCol)).intValue() : 0);
                chartData.add(chartRow);
            }
        } else if (columns.size() > 2) {
            String countColumn = columns.stream()
                    .filter(col -> col.toLowerCase().contains("count") ||
                            col.toLowerCase().contains("total") ||
                            col.toLowerCase().contains("sum") ||
                            col.equalsIgnoreCase("cnt"))
                    .findFirst().orElse(null);

            String labelCol = columns.get(0);
            if (countColumn != null) {
                for (Map<String, Object> row : rows) {
                    Map<String, Object> chartRow = new HashMap<>();
                    chartRow.put("label", String.valueOf(row.get(labelCol)));
                    chartRow.put("count", row.get(countColumn) instanceof Number ?
                            ((Number) row.get(countColumn)).intValue() : 0);
                    chartData.add(chartRow);
                }
            } else {
                Map<String, Integer> groupedData = new HashMap<>();
                for (Map<String, Object> row : rows) {
                    String label = String.valueOf(row.get(labelCol));
                    groupedData.put(label, groupedData.getOrDefault(label, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : groupedData.entrySet()) {
                    Map<String, Object> chartRow = new HashMap<>();
                    chartRow.put("label", entry.getKey());
                    chartRow.put("count", entry.getValue());
                    chartData.add(chartRow);
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
                Map<String, Object> chartRow = new HashMap<>();
                chartRow.put("label", entry.getKey());
                chartRow.put("count", entry.getValue());
                chartData.add(chartRow);
            }
        }

        return chartData;
    }
}
