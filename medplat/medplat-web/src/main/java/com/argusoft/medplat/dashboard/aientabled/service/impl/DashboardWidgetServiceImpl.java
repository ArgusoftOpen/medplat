package com.argusoft.medplat.dashboard.aientabled.service.impl;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardWidgetDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardWidget;
import com.argusoft.medplat.dashboard.aientabled.repository.DashboardWidgetRepository;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardWidgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
@Slf4j
@Transactional
public class DashboardWidgetServiceImpl implements DashboardWidgetService {
    @Autowired
    private DashboardWidgetRepository widgetRepository;
    @Override
    public DashboardWidget createWidget(DashboardWidgetDto widgetDto) {
        log.info("Creating widget: {}", widgetDto.getWidgetName());
        DashboardWidget widget = new DashboardWidget();
        widget.setDashboardId(widgetDto.getDashboardId());
        widget.setWidgetName(widgetDto.getWidgetName());
        widget.setWidgetType(widgetDto.getWidgetType());
        widget.setPosition(widgetDto.getPosition());
        widget.setGridRow(widgetDto.getGridRow());
        widget.setGridColumn(widgetDto.getGridColumn());
        widget.setGridWidth(widgetDto.getGridWidth() != null ? widgetDto.getGridWidth() : 4);
        widget.setGridHeight(widgetDto.getGridHeight() != null ? widgetDto.getGridHeight() : 200);
        widget.setTitle(widgetDto.getTitle());
        widget.setDescription(widgetDto.getDescription());
        widget.setDataSourceId(widgetDto.getDataSourceId());
        widget.setAggregationType(widgetDto.getAggregationType());
        widget.setTimeSeriesEnabled(widgetDto.getTimeSeriesEnabled());
        widget.setTimeGranularity(widgetDto.getTimeGranularity());
        widget.setCacheDurationSeconds(widgetDto.getCacheDurationSeconds());
        widget.setRefreshIntervalSeconds(widgetDto.getRefreshIntervalSeconds());
        widget.setAiAnomalyDetection(widgetDto.getAiAnomalyDetection());
        widget.setAiForecast(widgetDto.getAiForecast());
        widget.setAiInsights(widgetDto.getAiInsights());
        widget.setIsActive(true);
        widget.setCreatedDate(new Date());
        widget.setModifiedDate(new Date());
        return widgetRepository.save(widget);
    }
    @Override
    public DashboardWidget updateWidget(Long widgetId, DashboardWidgetDto widgetDto) {
        log.info("Updating widget: {}", widgetId);
        Optional<DashboardWidget> existing = widgetRepository.findById(widgetId);
        if (existing.isEmpty()) {
            throw new RuntimeException("Widget not found: " + widgetId);
        }
        DashboardWidget widget = existing.get();
        widget.setWidgetName(widgetDto.getWidgetName());
        widget.setWidgetType(widgetDto.getWidgetType());
        widget.setTitle(widgetDto.getTitle());
        widget.setDescription(widgetDto.getDescription());
        widget.setDataSourceId(widgetDto.getDataSourceId());
        widget.setGridWidth(widgetDto.getGridWidth());
        widget.setGridHeight(widgetDto.getGridHeight());
        widget.setModifiedDate(new Date());
        return widgetRepository.save(widget);
    }
    @Override
    public Optional<DashboardWidget> getWidgetById(Long widgetId) {
        return widgetRepository.findById(widgetId);
    }
    @Override
    public List<DashboardWidget> getDashboardWidgets(Long dashboardId) {
        return widgetRepository.findByDashboardIdAndIsActive(dashboardId, true);
    }
    @Override
    public void deleteWidget(Long widgetId) {
        log.info("Deleting widget: {}", widgetId);
        Optional<DashboardWidget> widget = widgetRepository.findById(widgetId);
        if (widget.isPresent()) {
            widget.get().setIsActive(false);
            widgetRepository.save(widget.get());
        }
    }
    @Override
    public Map<String, Object> renderWidget(Long widgetId) {
        log.info("Rendering widget: {}", widgetId);
        Map<String, Object> renderedWidget = new HashMap<>();
        Optional<DashboardWidget> widget = widgetRepository.findById(widgetId);
        if (widget.isEmpty()) {
            return renderedWidget;
        }
        DashboardWidget w = widget.get();
        renderedWidget.put("id", w.getId());
        renderedWidget.put("title", w.getTitle());
        renderedWidget.put("widgetType", w.getWidgetType());
        renderedWidget.put("gridWidth", w.getGridWidth());
        renderedWidget.put("gridHeight", w.getGridHeight());
        renderedWidget.put("data", getWidgetData(widgetId));
        return renderedWidget;
    }
    @Override
    public List<Map<String, Object>> getWidgetData(Long widgetId) {
        log.info("Getting data for widget: {}", widgetId);
        return new ArrayList<>();
    }
    @Override
    public void refreshWidgetData(Long widgetId) {
        log.info("Refreshing data for widget: {}", widgetId);
    }
    @Override
    public void reorderWidgets(Long dashboardId, List<Long> widgetIds) {
        log.info("Reordering widgets for dashboard: {}", dashboardId);
        List<DashboardWidget> widgets = widgetRepository.findByDashboardId(dashboardId);
        for (int i = 0; i < widgetIds.size(); i++) {
            for (DashboardWidget widget : widgets) {
                if (widget.getId().equals(widgetIds.get(i))) {
                    widget.setPosition(i);
                    widgetRepository.save(widget);
                    break;
                }
            }
        }
    }
    @Override
    public DashboardWidget cloneWidget(Long sourceWidgetId, Long targetDashboardId) {
        log.info("Cloning widget: {} to dashboard: {}", sourceWidgetId, targetDashboardId);
        Optional<DashboardWidget> sourceOpt = widgetRepository.findById(sourceWidgetId);
        if (sourceOpt.isEmpty()) {
            throw new RuntimeException("Source widget not found");
        }
        DashboardWidget source = sourceOpt.get();
        DashboardWidget clone = new DashboardWidget();
        clone.setDashboardId(targetDashboardId);
        clone.setWidgetName(source.getWidgetName() + " (Copy)");
        clone.setWidgetType(source.getWidgetType());
        clone.setGridRow(source.getGridRow());
        clone.setGridColumn(source.getGridColumn());
        clone.setGridWidth(source.getGridWidth());
        clone.setGridHeight(source.getGridHeight());
        clone.setTitle(source.getTitle());
        clone.setDescription(source.getDescription());
        clone.setDataSourceId(source.getDataSourceId());
        clone.setAggregationType(source.getAggregationType());
        clone.setTimeSeriesEnabled(source.getTimeSeriesEnabled());
        clone.setAiAnomalyDetection(source.getAiAnomalyDetection());
        clone.setAiForecast(source.getAiForecast());
        clone.setAiInsights(source.getAiInsights());
        clone.setIsActive(true);
        clone.setCreatedDate(new Date());
        clone.setModifiedDate(new Date());
        return widgetRepository.save(clone);
    }
}
