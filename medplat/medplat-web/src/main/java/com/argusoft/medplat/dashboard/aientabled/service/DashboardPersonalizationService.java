package com.argusoft.medplat.dashboard.aientabled.service;
import java.util.List;
import java.util.Map;
public interface DashboardPersonalizationService {
    List<Map<String, Object>> getRoleBasedRecommendations(Long userId, String userRole);
    List<Map<String, Object>> getUsageBasedSuggestions(Long userId);
    List<Map<String, Object>> getTrendingDashboards(Long organizationId, int limit);
    List<Map<String, Object>> getRecommendedKPIs(Long userId, String userRole);
    List<Map<String, Object>> getSimilarDashboards(Long currentDashboardId, int limit);
    Double predictWidgetInterest(Long userId, Long widgetId);
    void trackUserInteraction(Long userId, Long dashboardId, String actionType, Map<String, Object> metadata);
    List<Map<String, Object>> getUserInteractionHistory(Long userId, int limit);
    List<Map<String, Object>> getCollaborativeRecommendations(Long userId, int limit);
    Double scoreContentRelevance(Long userId, Long dashboardId);
    List<Map<String, Object>> getPersonalizedAlerts(Long userId);
    void updateUserPreferences(Long userId, Map<String, Object> preferences);
    Map<String, Object> getUserPreferences(Long userId);
}
