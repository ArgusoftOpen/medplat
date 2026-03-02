package com.argusoft.medplat.dashboard.aientabled.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataSourceDto implements Serializable {
    private Long id;
    private String sourceName;
    private String sourceType;
    private String description;
    private String query;
    private Map<String, Object> schemaMapping;
    private String cachingStrategy;
    private Integer cacheTtlSeconds;
    private Integer retryAttempts;
    private Integer timeoutSeconds;
    private Boolean paginationEnabled;
    private Integer pageSize;
    private Map<String, Object> validationRules;
    private Map<String, Object> dataTransformation;
    private Boolean realTimeEnabled;
    private String requiredRole;
    private Boolean isPublic;
    private Boolean isActive;
    private String sampleData;
    private Long totalRecords;
    private Date lastSynced;
    private Date createdDate;
    private Date modifiedDate;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class DashboardFilterDto implements Serializable {
    private Long id;
    private Long dashboardId;
    private String filterName;
    private String filterType; 
    private String dataField;
    private Object defaultValue;
    private List<String> allowedValues;
    private Boolean isRequired;
    private Boolean isVisible;
    private Integer displayOrder;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class DashboardAIInsightDto implements Serializable {
    private Long id;
    private Long widgetId;
    private String insightType;
    private String severity;
    private String title;
    private String description;
    private Map<String, Object> insightData;
    private Double confidenceScore;
    private Map<String, Object> relevantMetrics;
    private String recommendedAction;
    private Boolean isAcknowledged;
    private String userFeedback;
    private Date createdDate;
}
