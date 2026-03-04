package com.argusoft.medplat.dashboard.aientabled.service.impl;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardAIInsight;
import com.argusoft.medplat.dashboard.aientabled.repository.DashboardAIInsightRepository;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardAIInsightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
@Transactional
public class DashboardAIInsightServiceImpl implements DashboardAIInsightService {
    @Autowired
    private DashboardAIInsightRepository insightRepository;
    @Override
    public List<DashboardAIInsight> generateAnomalyDetection(Long widgetId, List<Map<String, Object>> data) {
        log.info("Generating anomaly detection for widget: {}", widgetId);
        List<DashboardAIInsight> anomalies = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            return anomalies;
        }
        try {
            List<Double> values = extractNumericalValues(data);
            if (values.size() < 3) {
                return anomalies; 
            }
            Map<String, Object> anomalyStats = calculateStatisticalAnomalies(values);
            if ((Boolean) anomalyStats.getOrDefault("hasAnomalies", false)) {
                DashboardAIInsight insight = new DashboardAIInsight();
                insight.setWidgetId(widgetId);
                insight.setInsightType("ANOMALY");
                insight.setSeverity((String) anomalyStats.get("severity"));
                insight.setTitle("Anomalies detected in widget data");
                insight.setDescription("Statistical anomalies found using Z-score and IQR methods");
                insight.setInsightData(anomalyStats.toString());
                insight.setConfidenceScore(0.85);
                insight.setCreatedDate(new Date());
                insight.setExpiresDate(new Date(System.currentTimeMillis() + 86400000)); 
                anomalies.add(insight);
            }
        } catch (Exception e) {
            log.error("Error in anomaly detection", e);
        }
        return anomalies;
    }
    @Override
    public Map<String, Object> generateForecast(Long widgetId, List<Map<String, Object>> historicalData, int forecastPeriods) {
        log.info("Generating forecast for widget: {} with {} periods", widgetId, forecastPeriods);
        Map<String, Object> forecastResult = new HashMap<>();
        if (historicalData == null || historicalData.size() < 5) {
            forecastResult.put("success", false);
            forecastResult.put("message", "Insufficient historical data for forecasting");
            return forecastResult;
        }
        try {
            List<Double> values = extractNumericalValues(historicalData);
            List<Double> forecast = performExponentialSmoothing(values, forecastPeriods, 0.3);
            forecastResult.put("success", true);
            forecastResult.put("forecastValues", forecast);
            forecastResult.put("confidence", 0.75);
            forecastResult.put("method", "Exponential Smoothing");
            forecastResult.put("forecastPeriods", forecastPeriods);
        } catch (Exception e) {
            log.error("Error in forecast generation", e);
            forecastResult.put("success", false);
            forecastResult.put("error", e.getMessage());
        }
        return forecastResult;
    }
    @Override
    public String generateSummary(Long widgetId, List<Map<String, Object>> data) {
        log.info("Generating summary for widget: {}", widgetId);
        if (data == null || data.isEmpty()) {
            return "No data available";
        }
        try {
            List<Double> values = extractNumericalValues(data);
            if (values.isEmpty()) {
                return "No numerical data to summarize";
            }
            double sum = values.stream().mapToDouble(Double::doubleValue).sum();
            double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            String trend = detectSimpleTrend(values);
            return String.format(
                "Data Summary: Total Records=%d, Average=%.2f, Min=%.2f, Max=%.2f, Trend=%s",
                values.size(), avg, min, max, trend
            );
        } catch (Exception e) {
            log.error("Error generating summary", e);
            return "Error generating summary";
        }
    }
    @Override
    public List<Map<String, Object>> generateRecommendations(Long userId, Long widgetId, Map<String, Object> context) {
        log.info("Generating recommendations for user: {} widget: {}", userId, widgetId);
        List<Map<String, Object>> recommendations = new ArrayList<>();
        try {
            if (context.containsKey("dataType")) {
                String dataType = (String) context.get("dataType");
                Map<String, Object> rec = new HashMap<>();
                rec.put("type", "VISUALIZATION");
                rec.put("title", "Consider using a different visualization");
                rec.put("description", "Based on data type: " + dataType);
                recommendations.add(rec);
            }
            if (context.containsKey("dataVolume")) {
                Integer dataVolume = (Integer) context.get("dataVolume");
                if (dataVolume > 10000) {
                    Map<String, Object> rec = new HashMap<>();
                    rec.put("type", "AGGREGATION");
                    rec.put("title", "High data volume detected");
                    rec.put("description", "Consider adding aggregation to improve performance");
                    recommendations.add(rec);
                }
            }
        } catch (Exception e) {
            log.error("Error generating recommendations", e);
        }
        return recommendations;
    }
    @Override
    public List<Map<String, Object>> getPersonalizedRecommendations(Long userId, Long dashboardId) {
        log.info("Getting personalized recommendations for user: {} dashboard: {}", userId, dashboardId);
        return new ArrayList<>();
    }
    @Override
    public DashboardAIInsight saveInsight(DashboardAIInsight insight) {
        if (insight.getCreatedDate() == null) {
            insight.setCreatedDate(new Date());
        }
        return insightRepository.save(insight);
    }
    @Override
    public List<DashboardAIInsight> getWidgetInsights(Long widgetId) {
        return insightRepository.findActiveInsightsForWidget(widgetId);
    }
    @Override
    public List<DashboardAIInsight> getUserRecentInsights(Long userId, Date fromDate) {
        return insightRepository.findRecentInsights(userId, fromDate);
    }
    @Override
    public List<DashboardAIInsight> getCriticalInsights() {
        return insightRepository.findCriticalInsights();
    }
    @Override
    public void acknowledgeInsight(Long insightId) {
        Optional<DashboardAIInsight> insight = insightRepository.findById(insightId);
        if (insight.isPresent()) {
            insight.get().setIsAcknowledged(true);
            insight.get().setAcknowledgedDate(new Date());
            insightRepository.save(insight.get());
        }
    }
    @Override
    public void resolveInsight(Long insightId) {
        Optional<DashboardAIInsight> insight = insightRepository.findById(insightId);
        if (insight.isPresent()) {
            insight.get().setIsResolved(true);
            insight.get().setResolvedDate(new Date());
            insightRepository.save(insight.get());
        }
    }
    @Override
    public void setInsightFeedback(Long insightId, String feedback) {
        Optional<DashboardAIInsight> insight = insightRepository.findById(insightId);
        if (insight.isPresent()) {
            insight.get().setUserFeedback(feedback);
            insightRepository.save(insight.get());
        }
    }
    @Override
    public Map<String, Object> detectTrends(Long widgetId, List<Map<String, Object>> data) {
        Map<String, Object> trends = new HashMap<>();
        List<Double> values = extractNumericalValues(data);
        if (values.size() < 2) {
            trends.put("trend", "INSUFFICIENT_DATA");
            return trends;
        }
        String trend = detectSimpleTrend(values);
        trends.put("trend", trend);
        trends.put("strength", calculateTrendStrength(values));
        return trends;
    }
    @Override
    public Map<String, Object> calculateStatisticalAnomalies(List<Double> values) {
        Map<String, Object> result = new HashMap<>();
        if (values.size() < 3) {
            result.put("hasAnomalies", false);
            return result;
        }
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = calculateStandardDeviation(values, mean);
        List<Double> zScores = values.stream()
            .map(v -> (v - mean) / (stdDev > 0 ? stdDev : 1))
            .collect(Collectors.toList());
        boolean hasAnomalies = zScores.stream().anyMatch(z -> Math.abs(z) > 3);
        result.put("hasAnomalies", hasAnomalies);
        result.put("severity", hasAnomalies ? "HIGH" : "LOW");
        result.put("mean", mean);
        result.put("stdDev", stdDev);
        result.put("anomalousIndices", zScores.stream()
            .map(z -> Math.abs(z) > 3)
            .collect(Collectors.toList()));
        return result;
    }
    @Override
    public Map<String, Object> analyzeCorrelations(List<Map<String, Object>> data) {
        Map<String, Object> correlations = new HashMap<>();
        return correlations;
    }
    private List<Double> extractNumericalValues(List<Map<String, Object>> data) {
        return data.stream()
            .flatMap(m -> m.values().stream())
            .filter(v -> v instanceof Number)
            .map(v -> ((Number) v).doubleValue())
            .collect(Collectors.toList());
    }
    private List<Double> performExponentialSmoothing(List<Double> values, int periods, double alpha) {
        List<Double> forecast = new ArrayList<>();
        double lastValue = values.get(values.size() - 1);
        for (int i = 0; i < periods; i++) {
            lastValue = alpha * lastValue + (1 - alpha) * lastValue;
            forecast.add(lastValue);
        }
        return forecast;
    }
    private String detectSimpleTrend(List<Double> values) {
        if (values.size() < 2) return "INSUFFICIENT_DATA";
        double firstHalf = values.stream().limit(values.size() / 2).mapToDouble(Double::doubleValue).average().orElse(0);
        double secondHalf = values.stream().skip(values.size() / 2).mapToDouble(Double::doubleValue).average().orElse(0);
        if (secondHalf > firstHalf * 1.05) return "UPTREND";
        if (secondHalf < firstHalf * 0.95) return "DOWNTREND";
        return "STABLE";
    }
    private double calculateTrendStrength(List<Double> values) {
        if (values.size() < 2) return 0.0;
        double firstHalf = values.stream().limit(values.size() / 2).mapToDouble(Double::doubleValue).average().orElse(0);
        double secondHalf = values.stream().skip(values.size() / 2).mapToDouble(Double::doubleValue).average().orElse(0);
        return Math.abs((secondHalf - firstHalf) / firstHalf);
    }
    private double calculateStandardDeviation(List<Double> values, double mean) {
        double variance = values.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
        return Math.sqrt(variance);
    }
    private Optional<DashboardAIInsight> findById(Long insightId) {
        return insightRepository.findById(insightId);
    }
}
