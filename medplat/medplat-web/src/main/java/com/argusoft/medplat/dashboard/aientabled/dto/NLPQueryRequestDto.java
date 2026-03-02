package com.argusoft.medplat.dashboard.aientabled.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NLPQueryRequestDto implements Serializable {
    private String query;
    private Long userId;
    private Long dashboardId;
    private List<String> allowedDataSources;
    private String language = "en";
    @JsonProperty("context")
    private Map<String, Object> context;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NLPQueryResponseDto implements Serializable {
    private Boolean success;
    private String message;
    private String queryType; 
    @JsonProperty("suggestedWidget")
    private Map<String, Object> suggestedWidget;
    @JsonProperty("queryData")
    private Map<String, Object> queryData;
    private Double confidenceScore;
    private List<String> clarificationQuestions;
    @JsonProperty("insights")
    private List<DashboardAIInsightDto> insights;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataResponseDto implements Serializable {
    private Boolean success;
    private String message;
    @JsonProperty("data")
    private List<Map<String, Object>> data;
    @JsonProperty("metadata")
    private DashboardDataMetadataDto metadata;
    @JsonProperty("insights")
    private List<DashboardAIInsightDto> insights;
    @JsonProperty("anomalies")
    private List<Map<String, Object>> anomalies;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataMetadataDto implements Serializable {
    private Integer totalRecords;
    private Integer returnedRecords;
    private Integer pageNumber;
    private Integer pageSize;
    private Long executionTimeMs;
    private String dataSource;
    private String lastUpdated;
    private Map<String, Object> aggregateStats;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboardRecommendationDto implements Serializable {
    private String recommendationType; 
    private String title;
    private String description;
    @JsonProperty("recommendationData")
    private Map<String, Object> recommendationData;
    private Double relevanceScore;
    private String reason;
    private Long widgetId;
}
