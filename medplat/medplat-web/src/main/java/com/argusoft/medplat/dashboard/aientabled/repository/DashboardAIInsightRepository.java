package com.argusoft.medplat.dashboard.aientabled.repository;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardAIInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
@Repository
public interface DashboardAIInsightRepository extends JpaRepository<DashboardAIInsight, Long> {
    List<DashboardAIInsight> findByUserId(Long userId);
    List<DashboardAIInsight> findByWidgetId(Long widgetId);
    List<DashboardAIInsight> findByInsightType(String insightType);
    List<DashboardAIInsight> findBySeverity(String severity);
    List<DashboardAIInsight> findByUserIdAndIsAcknowledged(Long userId, Boolean isAcknowledged);
    @Query("SELECT a FROM DashboardAIInsight a WHERE a.userId = :userId AND a.createdDate >= :startDate ORDER BY a.createdDate DESC")
    List<DashboardAIInsight> findRecentInsights(@Param("userId") Long userId, @Param("startDate") Date startDate);
    @Query("SELECT a FROM DashboardAIInsight a WHERE a.severity = 'HIGH' OR a.severity = 'CRITICAL' AND a.isResolved = false")
    List<DashboardAIInsight> findCriticalInsights();
    @Query("SELECT a FROM DashboardAIInsight a WHERE a.widgetId = :widgetId AND a.expiresDate > CURRENT_TIMESTAMP")
    List<DashboardAIInsight> findActiveInsightsForWidget(@Param("widgetId") Long widgetId);
}
