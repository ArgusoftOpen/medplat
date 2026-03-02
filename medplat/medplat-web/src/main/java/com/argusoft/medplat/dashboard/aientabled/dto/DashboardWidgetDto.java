package com.argusoft.medplat.dashboard.aientabled.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardWidgetDto implements Serializable {
    private Long id;
    private Long dashboardId;
    private String widgetName;
    private String widgetType;
    private Integer position;
    private Integer gridRow;
    private Integer gridColumn;
    private Integer gridWidth;
    private Integer gridHeight;
    private String title;
    private String description;
    private Long dataSourceId;
    @JsonProperty("queryConfig")
    private Map<String, Object> queryConfig;
    @JsonProperty("visualizationConfig")
    private Map<String, Object> visualizationConfig;
    @JsonProperty("displayOptions")
    private Map<String, Object> displayOptions;
    private String aggregationType;
    private Boolean timeSeriesEnabled;
    private String timeGranularity;
    private Integer cacheDurationSeconds;
    private Integer refreshIntervalSeconds;
    private Boolean aiAnomalyDetection;
    private Boolean aiForecast;
    private Boolean aiInsights;
    @JsonProperty("aiConfig")
    private Map<String, Object> aiConfig;
    private Boolean isActive;
    private Date createdDate;
    private Date modifiedDate;
    @JsonProperty("widgetData")
    private Map<String, Object> widgetData;
    @JsonProperty("insights")
    private java.util.List<DashboardAIInsightDto> insights;
}
