package com.argusoft.medplat.dashboard.aientabled.service;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardWidgetDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardWidget;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface DashboardWidgetService {
    DashboardWidget createWidget(DashboardWidgetDto widgetDto);
    DashboardWidget updateWidget(Long widgetId, DashboardWidgetDto widgetDto);
    Optional<DashboardWidget> getWidgetById(Long widgetId);
    List<DashboardWidget> getDashboardWidgets(Long dashboardId);
    void deleteWidget(Long widgetId);
    Map<String, Object> renderWidget(Long widgetId);
    List<Map<String, Object>> getWidgetData(Long widgetId);
    void refreshWidgetData(Long widgetId);
    void reorderWidgets(Long dashboardId, List<Long> widgetIds);
    DashboardWidget cloneWidget(Long sourceWidgetId, Long targetDashboardId);
}
