package com.argusoft.medplat.web.ddb.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;

import java.util.List;
import java.util.Map;

/**
 * Defines database methods for ddb
 * @author ashwin
 * @since 23/08/2025 15:30
 */
public interface DdbDao extends GenericDao<DerivedAttribute, Integer> {

    // Database introspection methods
    List<String> getAllTables();
    List<String> getColumnsByTable(String tableName);
    List<Map<String, Object>> getTableMetadata(String tableName);
    List<Map<String, Object>> getSampleData(String tableName, int limit);
    List<Map<String, Object>> executeNativeQuery(String query);
    List<Map<String, Object>> getDataByTableAndColumns(String table, List<String> columns);
    List<Map<String, Object>> getGroupedCount(String table, String xAxis, String indicator);
    Integer getAttributeValueByIndicator(String indicator);
    Integer getAttributeValueByTableAndColumn(String table, String column);

    // DerivedAttribute operations
    List<DerivedAttribute> getAllDerivedAttributes();

    // IndicatorMaster operations
    void saveIndicatorMaster(IndicatorMaster indicatorMaster);
    List<IndicatorMaster> getAllIndicatorMaster();

    // DatasetMaster operations
    void saveDatasetMaster(DatasetMaster datasetMaster);
    List<DatasetMaster> getAllDatasetMaster();
    DatasetMaster getDatasetMasterById(Integer id);
}
