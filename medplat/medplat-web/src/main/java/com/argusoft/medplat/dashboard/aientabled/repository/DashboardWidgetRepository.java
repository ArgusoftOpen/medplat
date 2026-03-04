package com.argusoft.medplat.dashboard.aientabled.repository;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, Long> {
    List<DashboardWidget> findByDashboardId(Long dashboardId);
    List<DashboardWidget> findByDashboardIdAndIsActive(Long dashboardId, Boolean isActive);
    List<DashboardWidget> findByDataSourceId(Long dataSourceId);
    List<DashboardWidget> findByWidgetType(String widgetType);
    List<DashboardWidget> findByAiAnomalyDetectionAndIsActive(Boolean anomalyDetection, Boolean isActive);
    List<DashboardWidget> findByAiForecastAndIsActive(Boolean forecast, Boolean isActive);
}
