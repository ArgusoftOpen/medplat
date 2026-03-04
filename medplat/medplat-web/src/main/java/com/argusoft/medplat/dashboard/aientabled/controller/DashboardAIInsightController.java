package com.argusoft.medplat.dashboard.aientabled.controller;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardAIInsight;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardAIInsightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/dashboard/ai-insights")
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardAIInsightController {
    @Autowired
    private DashboardAIInsightService insightService;
    @GetMapping("/widget/{widgetId}")
    public ResponseEntity<?> getWidgetInsights(@PathVariable Long widgetId) {
        try {
            List<DashboardAIInsight> insights = insightService.getWidgetInsights(widgetId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("widgetId", widgetId);
            response.put("insightCount", insights.size());
            response.put("insights", insights);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching widget insights", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch insights: " + e.getMessage()));
        }
    }
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<?> getUserRecentInsights(@PathVariable Long userId, 
                                                   @RequestParam(defaultValue = "1") int daysBack) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -daysBack);
            Date fromDate = calendar.getTime();
            List<DashboardAIInsight> insights = insightService.getUserRecentInsights(userId, fromDate);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("insightCount", insights.size());
            response.put("insights", insights);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user recent insights", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch recent insights: " + e.getMessage()));
        }
    }
    @GetMapping("/critical")
    public ResponseEntity<?> getCriticalInsights() {
        try {
            List<DashboardAIInsight> insights = insightService.getCriticalInsights();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("criticalCount", insights.size());
            response.put("insights", insights);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching critical insights", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch critical insights: " + e.getMessage()));
        }
    }
    @PutMapping("/{insightId}/acknowledge")
    public ResponseEntity<?> acknowledgeInsight(@PathVariable Long insightId) {
        try {
            insightService.acknowledgeInsight(insightId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Insight acknowledged successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error acknowledging insight", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to acknowledge insight: " + e.getMessage()));
        }
    }
    @PutMapping("/{insightId}/resolve")
    public ResponseEntity<?> resolveInsight(@PathVariable Long insightId) {
        try {
            insightService.resolveInsight(insightId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Insight marked as resolved");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error resolving insight", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to resolve insight: " + e.getMessage()));
        }
    }
    @PutMapping("/{insightId}/feedback")
    public ResponseEntity<?> setInsightFeedback(@PathVariable Long insightId, 
                                                @RequestParam String feedback) {
        try {
            insightService.setInsightFeedback(insightId, feedback);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Feedback recorded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error recording feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to record feedback: " + e.getMessage()));
        }
    }
    @PostMapping("/anomaly-detection")
    public ResponseEntity<?> generateAnomalyDetection(@RequestParam Long widgetId, 
                                                      @RequestBody List<Map<String, Object>> data) {
        try {
            List<DashboardAIInsight> anomalies = insightService.generateAnomalyDetection(widgetId, data);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("anomalyCount", anomalies.size());
            response.put("anomalies", anomalies);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating anomaly detection", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to generate anomalies: " + e.getMessage()));
        }
    }
    @PostMapping("/forecast")
    public ResponseEntity<?> generateForecast(@RequestParam Long widgetId, 
                                              @RequestBody List<Map<String, Object>> historicalData,
                                              @RequestParam(defaultValue = "12") int forecastPeriods) {
        try {
            Map<String, Object> forecast = insightService.generateForecast(widgetId, historicalData, forecastPeriods);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("forecast", forecast);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating forecast", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to generate forecast: " + e.getMessage()));
        }
    }
    @PostMapping("/summary")
    public ResponseEntity<?> generateSummary(@RequestParam Long widgetId, 
                                             @RequestBody List<Map<String, Object>> data) {
        try {
            String summary = insightService.generateSummary(widgetId, data);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("summary", summary);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating summary", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to generate summary: " + e.getMessage()));
        }
    }
    @PostMapping("/detect-trends")
    public ResponseEntity<?> detectTrends(@RequestParam Long widgetId, 
                                          @RequestBody List<Map<String, Object>> data) {
        try {
            Map<String, Object> trends = insightService.detectTrends(widgetId, data);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("trends", trends);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error detecting trends", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to detect trends: " + e.getMessage()));
        }
    }
    @PostMapping("/correlations")
    public ResponseEntity<?> analyzeCorrelations(@RequestBody List<Map<String, Object>> data) {
        try {
            Map<String, Object> correlations = insightService.analyzeCorrelations(data);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("correlations", correlations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error analyzing correlations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to analyze correlations: " + e.getMessage()));
        }
    }
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
