package com.argusoft.medplat.dashboard.aientabled.service.impl;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryRequestDto;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryResponseDto;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardNLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@Slf4j
@Transactional
public class DashboardNLPServiceImpl implements DashboardNLPService {
    @Override
    public NLPQueryResponseDto processQuery(NLPQueryRequestDto request) {
        log.info("Processing NLP query: {}", request.getQuery());
        NLPQueryResponseDto response = new NLPQueryResponseDto();
        try {
            String intent = extractIntent(request.getQuery());
            response.setQueryType(intent);
            Map<String, Object> entities = extractEntities(request.getQuery());
            Double confidence = getConfidenceScore(request.getQuery());
            response.setConfidenceScore(confidence);
            if (confidence < 0.5) {
                response.setClarificationQuestions(Arrays.asList(generateClarifyingQuestions(request.getQuery())));
                response.setSuccess(false);
                response.setMessage("Query is ambiguous. Please clarify.");
            } else {
                response.setSuccess(true);
                response.setMessage("Query processed successfully");
                Map<String, Object> widget = suggestWidgetConfiguration(request.getQuery());
                response.setSuggestedWidget(widget);
                Map<String, Object> queryData = new HashMap<>();
                queryData.putAll(entities);
                response.setQueryData(queryData);
            }
        } catch (Exception e) {
            log.error("Error processing NLP query", e);
            response.setSuccess(false);
            response.setMessage("Error processing query: " + e.getMessage());
        }
        return response;
    }
    @Override
    public String extractIntent(String query) {
        String lowerQuery = query.toLowerCase();
        if (matchesPattern(lowerQuery, "(show|display|create|make).*chart|graph|plot")) {
            return "CHART_REQUEST";
        } else if (matchesPattern(lowerQuery, "kpi|key performance|metric|value")) {
            return "KPI_REQUEST";
        } else if (matchesPattern(lowerQuery, "summary|overview|top|trending|popular")) {
            return "SUMMARY_REQUEST";
        } else if (matchesPattern(lowerQuery, "filter|where|between|after|before")) {
            return "FILTERED_DATA_REQUEST";
        } else if (matchesPattern(lowerQuery, "anomaly|outlier|unusual|abnormal")) {
            return "ANOMALY_REQUEST";
        } else if (matchesPattern(lowerQuery, "forecast|predict|trend|projection")) {
            return "FORECAST_REQUEST";
        } else if (matchesPattern(lowerQuery, "compare|difference|vs|between")) {
            return "COMPARISON_REQUEST";
        } else {
            return "GENERIC_REQUEST";
        }
    }
    @Override
    public Map<String, Object> extractEntities(String query) {
        Map<String, Object> entities = new HashMap<>();
        Map<String, Object> dateRange = parseDateExpression(query);
        if (!dateRange.isEmpty()) {
            entities.putAll(dateRange);
        }
        List<String> metrics = extractMetrics(query);
        if (!metrics.isEmpty()) {
            entities.put("metrics", metrics);
        }
        List<String> dimensions = extractDimensions(query);
        if (!dimensions.isEmpty()) {
            entities.put("dimensions", dimensions);
        }
        List<String> filters = extractFilters(query);
        if (!filters.isEmpty()) {
            entities.put("filters", filters);
        }
        return entities;
    }
    @Override
    public Map<String, Object> suggestWidgetConfiguration(String description) {
        Map<String, Object> config = new HashMap<>();
        String intent = extractIntent(description);
        config.put("title", generateTitle(description));
        config.put("description", description);
        if (intent.equals("CHART_REQUEST")) {
            if (description.toLowerCase().contains("pie")) {
                config.put("widgetType", "PIE_CHART");
            } else if (description.toLowerCase().contains("bar")) {
                config.put("widgetType", "BAR_CHART");
            } else if (description.toLowerCase().contains("line") || description.toLowerCase().contains("trend")) {
                config.put("widgetType", "LINE_CHART");
            } else {
                config.put("widgetType", "BAR_CHART");
            }
        } else if (intent.equals("KPI_REQUEST")) {
            config.put("widgetType", "KPI");
        } else if (intent.equals("SUMMARY_REQUEST")) {
            config.put("widgetType", "TABLE");
        } else {
            config.put("widgetType", "TABLE");
        }
        config.put("gridWidth", 4);
        config.put("gridHeight", 200);
        config.put("refreshInterval", 300);
        return config;
    }
    @Override
    public Map<String, Object> generateChartFromQuery(String query, Long dataSourceId) {
        Map<String, Object> chart = new HashMap<>();
        chart.put("dataSourceId", dataSourceId);
        chart.put("config", suggestWidgetConfiguration(query));
        chart.put("entities", extractEntities(query));
        return chart;
    }
    @Override
    public Map<String, Object> queryKPIValues(String query) {
        Map<String, Object> kpiData = new HashMap<>();
        try {
            List<String> metrics = extractMetrics(query);
            kpiData.put("metrics", metrics);
            kpiData.put("filters", extractFilters(query));
            kpiData.put("dateRange", parseDateExpression(query));
        } catch (Exception e) {
            log.error("Error querying KPI", e);
        }
        return kpiData;
    }
    @Override
    public Map<String, Object> suggestFilters(String query, Long dataSourceId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("dateRange", parseDateExpression(query));
        filters.put("conditions", extractFilters(query));
        filters.put("dataSourceId", dataSourceId);
        return filters;
    }
    @Override
    public Map<String, Object> parseDateExpression(String dateExpression) {
        Map<String, Object> dateRange = new HashMap<>();
        String lower = dateExpression.toLowerCase();
        LocalDate now = LocalDate.now();
        LocalDate startDate = null;
        LocalDate endDate = now;
        if (lower.contains("last 7 day") || lower.contains("past week")) {
            startDate = now.minusDays(7);
        } else if (lower.contains("last 30 day") || lower.contains("past month")) {
            startDate = now.minusDays(30);
        } else if (lower.contains("last 90 day") || lower.contains("past quarter")) {
            startDate = now.minusDays(90);
        } else if (lower.contains("last 365 day") || lower.contains("past year")) {
            startDate = now.minusYears(1);
        } else if (lower.contains("this month")) {
            startDate = now.withDayOfMonth(1);
        } else if (lower.contains("this quarter")) {
            int quarter = (now.getMonthValue() - 1) / 3;
            startDate = now.withMonth(quarter * 3 + 1).withDayOfMonth(1);
        } else if (lower.contains("this year")) {
            startDate = now.withDayOfYear(1);
        } else if (lower.contains("today")) {
            startDate = now;
        } else if (lower.contains("yesterday")) {
            startDate = endDate = now.minusDays(1);
        }
        if (startDate != null) {
            dateRange.put("startDate", startDate.toString());
            dateRange.put("endDate", endDate.toString());
        }
        return dateRange;
    }
    @Override
    public Boolean validateQuery(String query) {
        return query != null && !query.trim().isEmpty() && query.length() > 3;
    }
    @Override
    public String[] generateClarifyingQuestions(String query) {
        List<String> questions = new ArrayList<>();
        String intent = extractIntent(query);
        switch (intent) {
            case "CHART_REQUEST":
                questions.add("What type of chart would you prefer (line, bar, pie)?");
                questions.add("What time period should be included?");
                break;
            case "KPI_REQUEST":
                questions.add("Which specific KPI metrics are you interested in?");
                questions.add("What time period should be used for comparison?");
                break;
            case "FILTERED_DATA_REQUEST":
                questions.add("What are the specific filter criteria?");
                questions.add("Which fields should be displayed?");
                break;
            default:
                questions.add("Could you provide more specific details about what you need?");
        }
        return questions.toArray(new String[0]);
    }
    @Override
    public Double getConfidenceScore(String query) {
        double score = 0.5;
        String intent = extractIntent(query);
        if (!intent.equals("GENERIC_REQUEST")) {
            score += 0.3;
        }
        if (extractEntities(query).size() > 0) {
            score += 0.2;
        }
        String lower = query.toLowerCase();
        int keywordCount = 0;
        keywordCount += countOccurrences(lower, "show|display|create");
        keywordCount += countOccurrences(lower, "chart|graph|table|metric");
        keywordCount += countOccurrences(lower, "by|group|filter|where");
        score = Math.min(0.95, score + (keywordCount * 0.05));
        return Math.max(0.0, Math.min(1.0, score));
    }
    @Override
    public void provideFeedback(String query, String correctInterpretation) {
        log.info("Feedback for query '{}': {}", query, correctInterpretation);
    }
    @Override
    public String[] suggestSimilarQueries(String query) {
        return new String[0];
    }
    @Override
    public NLPQueryResponseDto processQueryInLanguage(NLPQueryRequestDto request, String languageCode) {
        log.info("Processing query in language: {}", languageCode);
        return processQuery(request);
    }
    private boolean matchesPattern(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.find();
    }
    private List<String> extractMetrics(String query) {
        List<String> metrics = new ArrayList<>();
        String[] commonMetrics = {"revenue", "cost", "count", "total", "average", "sum", "patient", "visit", "appointment"};
        String lower = query.toLowerCase();
        for (String metric : commonMetrics) {
            if (lower.contains(metric)) {
                metrics.add(metric);
            }
        }
        return metrics;
    }
    private List<String> extractDimensions(String query) {
        List<String> dimensions = new ArrayList<>();
        String[] commonDimensions = {"by department", "by region", "by date", "by category", "by status"};
        String lower = query.toLowerCase();
        for (String dim : commonDimensions) {
            if (lower.contains(dim)) {
                dimensions.add(dim);
            }
        }
        return dimensions;
    }
    private List<String> extractFilters(String query) {
        List<String> filters = new ArrayList<>();
        if (query.toLowerCase().contains("where ")) {
            int startIdx = query.toLowerCase().indexOf("where ") + 6;
            filters.add(query.substring(startIdx));
        }
        return filters;
    }
    private String generateTitle(String description) {
        String[] words = description.split("\\s+");
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < Math.min(5, words.length); i++) {
            if (title.length() > 0) title.append(" ");
            title.append(words[i]);
        }
        return title.toString().replaceAll("[?!.]", "");
    }
    private int countOccurrences(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }
}
