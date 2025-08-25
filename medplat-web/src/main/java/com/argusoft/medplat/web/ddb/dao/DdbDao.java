package com.argusoft.medplat.web.ddb.dao;

import java.util.List;
import java.util.Map;

public interface DdbDao {

    // Database introspection methods (native queries only)
    List<String> getAllTables();
    List<String> getColumnsByTable(String tableName);
    List<Map<String, Object>> getTableMetadata(String tableName);
    List<Map<String, Object>> getSampleData(String tableName, int limit);
    List<Map<String, Object>> executeNativeQuery(String query);
    List<Map<String, Object>> getDataByTableAndColumns(String table, List<String> columns);
    List<Map<String, Object>> getGroupedCount(String table, String xAxis, String indicator);
    Integer getAttributeValueByIndicator(String indicator);
    Integer getAttributeValueByTableAndColumn(String table, String column);
}
