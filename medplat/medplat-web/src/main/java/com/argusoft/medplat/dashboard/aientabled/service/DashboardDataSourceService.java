package com.argusoft.medplat.dashboard.aientabled.service;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardDataSourceDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardDataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface DashboardDataSourceService {
    DashboardDataSource createDataSource(DashboardDataSourceDto dataSourceDto);
    DashboardDataSource updateDataSource(Long sourceId, DashboardDataSourceDto dataSourceDto);
    Optional<DashboardDataSource> getDataSourceById(Long sourceId);
    Optional<DashboardDataSource> getDataSourceByName(String sourceName);
    List<DashboardDataSource> getDataSourcesByType(String sourceType);
    List<DashboardDataSource> getActiveDataSources();
    Map<String, Object> executeQuery(Long sourceId, Map<String, Object> filterParams);
    Map<String, Object> executeQueryWithPagination(Long sourceId, Map<String, Object> filterParams, 
                                                     int pageNumber, int pageSize);
    List<Map<String, Object>> getSampleData(Long sourceId, int sampleSize);
    Boolean testConnection(Long sourceId);
    Map<String, Object> getSchemaMetadata(Long sourceId);
    void deleteDataSource(Long sourceId);
    Map<String, Object> validateData(Long sourceId, List<Map<String, Object>> data);
    List<Map<String, Object>> applyTransformation(Long sourceId, List<Map<String, Object>> data);
    void syncRealTimeData(Long sourceId);
}
