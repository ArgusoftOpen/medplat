package com.argusoft.medplat.dashboard.aientabled.controller;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryRequestDto;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryResponseDto;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardNLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/dashboard/nlp")
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardNLPController {
    @Autowired
    private DashboardNLPService nlpService;
    @PostMapping("/query")
    public ResponseEntity<?> processQuery(@RequestBody NLPQueryRequestDto request) {
        try {
            log.info("Processing NLP query: {}", request.getQuery());
            NLPQueryResponseDto response = nlpService.processQuery(request);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("response", response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error processing NLP query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to process query: " + e.getMessage()));
        }
    }
    @GetMapping("/intent")
    public ResponseEntity<?> extractIntent(@RequestParam String query) {
        try {
            String intent = nlpService.extractIntent(query);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("intent", intent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error extracting intent", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to extract intent: " + e.getMessage()));
        }
    }
    @GetMapping("/entities")
    public ResponseEntity<?> extractEntities(@RequestParam String query) {
        try {
            Map<String, Object> entities = nlpService.extractEntities(query);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("entities", entities);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error extracting entities", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to extract entities: " + e.getMessage()));
        }
    }
    @GetMapping("/suggest-widget")
    public ResponseEntity<?> suggestWidget(@RequestParam String description) {
        try {
            Map<String, Object> config = nlpService.suggestWidgetConfiguration(description);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("suggestedWidget", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error suggesting widget", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to suggest widget: " + e.getMessage()));
        }
    }
    @PostMapping("/generate-chart")
    public ResponseEntity<?> generateChartFromQuery(@RequestParam String query, 
                                                     @RequestParam Long dataSourceId) {
        try {
            Map<String, Object> chart = nlpService.generateChartFromQuery(query, dataSourceId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("chart", chart);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating chart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to generate chart: " + e.getMessage()));
        }
    }
    @GetMapping("/kpi")
    public ResponseEntity<?> queryKPIValues(@RequestParam String query) {
        try {
            Map<String, Object> kpiData = nlpService.queryKPIValues(query);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("kpiData", kpiData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error querying KPI", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to query KPI: " + e.getMessage()));
        }
    }
    @PostMapping("/suggest-filters")
    public ResponseEntity<?> suggestFilters(@RequestParam String query, 
                                            @RequestParam Long dataSourceId) {
        try {
            Map<String, Object> filters = nlpService.suggestFilters(query, dataSourceId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("suggestedFilters", filters);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error suggesting filters", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to suggest filters: " + e.getMessage()));
        }
    }
    @GetMapping("/parse-date")
    public ResponseEntity<?> parseDateExpression(@RequestParam String dateExpression) {
        try {
            Map<String, Object> dateRange = nlpService.parseDateExpression(dateExpression);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("dateRange", dateRange);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error parsing date expression", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to parse date: " + e.getMessage()));
        }
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateQuery(@RequestParam String query) {
        try {
            Boolean isValid = nlpService.validateQuery(query);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("isValid", isValid);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error validating query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to validate query: " + e.getMessage()));
        }
    }
    @GetMapping("/confidence")
    public ResponseEntity<?> getConfidenceScore(@RequestParam String query) {
        try {
            Double confidence = nlpService.getConfidenceScore(query);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("confidenceScore", confidence);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting confidence score", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to get confidence score: " + e.getMessage()));
        }
    }
    @PostMapping("/feedback")
    public ResponseEntity<?> provideFeedback(@RequestParam String query, 
                                             @RequestParam String correctInterpretation) {
        try {
            nlpService.provideFeedback(query, correctInterpretation);
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
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
