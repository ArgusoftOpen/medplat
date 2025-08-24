package com.argusoft.medplat.web.ddb.service.impl;

import com.argusoft.medplat.web.ddb.dao.DdbDao;
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
 * Implements methods of DdbService
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Service("DdbService")
public class DdbServiceImpl implements DdbService {

    private static final Logger log = LoggerFactory.getLogger(DdbServiceImpl.class);

    @Autowired
    private DdbDao ddbDao;

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
        if (sql == null || !sql.trim().toLowerCase().startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed.");
        }

        // Additional security check
        if (containsForbiddenKeywords(sql.trim().toLowerCase())) {
            throw new IllegalArgumentException("Query contains forbidden operations or keywords.");
        }

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

        ddbDao.createOrUpdate(derivedAttribute);
        return Collections.singletonMap("success", true);
    }

    @Override
    public List<Map<String, Object>> getDerivedAttributes() {
        List<DerivedAttribute> entities = ddbDao.getAllDerivedAttributes();
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

        ddbDao.saveIndicatorMaster(indicatorMaster);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("query_result", queryResult);
        return response;
    }

    @Override
    public List<Map<String, Object>> getIndicatorMaster() {
        List<IndicatorMaster> entities = ddbDao.getAllIndicatorMaster();
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

        // SECURITY: Validate that only SELECT queries are allowed
        String trimmedQuery = query.trim().toLowerCase();
        if (!trimmedQuery.startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed. DELETE, INSERT, UPDATE, DROP, ALTER operations are forbidden.");
        }

        // Additional security checks
        if (containsForbiddenKeywords(trimmedQuery)) {
            throw new IllegalArgumentException("Query contains forbidden operations or keywords.");
        }

        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(query);
        List<String> fields = CollectionUtils.isEmpty(rows) ?
                new ArrayList<>() : new ArrayList<>(rows.get(0).keySet());

        Map<String, Object> response = new HashMap<>();
        response.put("rows", rows);
        response.put("fields", fields);
        return response;
    }

    /**
     * Additional security method to check for forbidden SQL keywords
     * @param query The SQL query to validate
     * @return true if query contains forbidden keywords
     */
    private boolean containsForbiddenKeywords(String query) {
        String[] forbiddenKeywords = {
                "delete", "insert", "update", "drop", "alter", "create", "truncate",
                "grant", "revoke", "commit", "rollback", "savepoint", "exec", "execute",
                "sp_", "xp_", "into outfile", "load_file", "dumpfile"
        };

        for (String keyword : forbiddenKeywords) {
            if (query.contains(keyword)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Map<String, Object> getDatasetMasterById(Integer id) {
        DatasetMaster entity = ddbDao.getDatasetMasterById(id);
        if (entity == null) {
            throw new NoSuchElementException("Dataset not found");
        }
        return DdbMapper.convertDatasetMasterToMap(entity);
    }

    @Override
    public List<Map<String, Object>> getDatasetMaster() {
        List<DatasetMaster> entities = ddbDao.getAllDatasetMaster();
        return DdbMapper.convertDatasetMastersToMaps(entities);
    }

    @Override
    @Transactional
    public Map<String, Object> saveDataset(Map<String, Object> requestBody) {
        String tableName = (String) requestBody.get("tableName");
        String sql = (String) requestBody.get("sql");
        Integer userId = (Integer) requestBody.getOrDefault("userId", 1);

        if (tableName == null || sql == null || !sql.trim().toLowerCase().startsWith("select")) {
            throw new IllegalArgumentException("Invalid tableName or only SELECT queries are allowed.");
        }

        DatasetMaster datasetMaster = new DatasetMaster();
        datasetMaster.setDatasetName(tableName);
        datasetMaster.setSqlQuery(sql);
        datasetMaster.setCreatedBy(userId);
        datasetMaster.setModifiedBy(userId);

        ddbDao.saveDatasetMaster(datasetMaster);

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

        List<Map<String, Object>> rows = ddbDao.executeNativeQuery(sql);
        if (CollectionUtils.isEmpty(rows)) {
            return new ArrayList<>();
        }

        return processChartData(rows);
    }

    // Helper methods
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
