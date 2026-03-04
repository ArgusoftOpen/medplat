package com.argusoft.medplat.dashboard.aientabled.service;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryRequestDto;
import com.argusoft.medplat.dashboard.aientabled.dto.NLPQueryResponseDto;
import java.util.Map;
public interface DashboardNLPService {
    NLPQueryResponseDto processQuery(NLPQueryRequestDto request);
    String extractIntent(String query);
    Map<String, Object> extractEntities(String query);
    Map<String, Object> suggestWidgetConfiguration(String description);
    Map<String, Object> generateChartFromQuery(String query, Long dataSourceId);
    Map<String, Object> queryKPIValues(String query);
    Map<String, Object> suggestFilters(String query, Long dataSourceId);
    Map<String, Object> parseDateExpression(String dateExpression);
    Boolean validateQuery(String query);
    String[] generateClarifyingQuestions(String query);
    Double getConfidenceScore(String query);
    void provideFeedback(String query, String correctInterpretation);
    String[] suggestSimilarQueries(String query);
    NLPQueryResponseDto processQueryInLanguage(NLPQueryRequestDto request, String languageCode);
}
