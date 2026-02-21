package com.argusoft.medplat.web.ddb.service;

import java.util.List;
import java.util.Map;

/**
 * Defines methods for ddb
 * @author ashwin
 * @since 23/08/2025 15:30
 */
public interface DdbService {

    Map<String, Object> getAttributeValue(Map<String, Object> requestBody);
    List<String> getAllTables();
    List<String> getColumnsByTable(String tableName);
    List<Map<String, Object>> getTableMetadata(String tableName);
    List<Map<String, Object>> getSampleData(String tableName, int limit);
    List<Map<String, Object>> getDataByTableAndColumns(Map<String, Object> requestBody);
    Map<String, Object> previewSql(Map<String, Object> requestBody);
    Map<String, Object> saveDerivedAttribute(Map<String, Object> requestBody);
    List<Map<String, Object>> getDerivedAttributes();
    Map<String, Object> saveIndicatorMaster(Map<String, Object> requestBody);
    List<Map<String, Object>> getIndicatorMaster();
    List<Map<String, Object>> getGroupedCount(Map<String, Object> requestBody);
    Map<String, Object> runSql(Map<String, Object> requestBody);
    Map<String, Object> getDatasetMasterById(Integer id);
    List<Map<String, Object>> getDatasetMaster();
    Map<String, Object> saveDataset(Map<String, Object> requestBody);
    List<Map<String, Object>> getDatasetChart(Map<String, Object> requestBody);
}
