package com.argusoft.medplat.dashboard.aientabled.service;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardAIInsightDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardAIInsight;
import java.util.Date;
import java.util.List;
import java.util.Map;
public interface DashboardAIInsightService {
    List<DashboardAIInsight> generateAnomalyDetection(Long widgetId, List<Map<String, Object>> data);
    Map<String, Object> generateForecast(Long widgetId, List<Map<String, Object>> historicalData, int forecastPeriods);
    String generateSummary(Long widgetId, List<Map<String, Object>> data);
    List<Map<String, Object>> generateRecommendations(Long userId, Long widgetId, Map<String, Object> context);
    List<Map<String, Object>> getPersonalizedRecommendations(Long userId, Long dashboardId);
    DashboardAIInsight saveInsight(DashboardAIInsight insight);
    List<DashboardAIInsight> getWidgetInsights(Long widgetId);
    List<DashboardAIInsight> getUserRecentInsights(Long userId, Date fromDate);
    List<DashboardAIInsight> getCriticalInsights();
    void acknowledgeInsight(Long insightId);
    void resolveInsight(Long insightId);
    void setInsightFeedback(Long insightId, String feedback);
    Map<String, Object> detectTrends(Long widgetId, List<Map<String, Object>> data);
    Map<String, Object> calculateStatisticalAnomalies(List<Double> values);
    Map<String, Object> analyzeCorrelations(List<Map<String, Object>> data);
}
