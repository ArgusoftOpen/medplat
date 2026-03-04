package com.argusoft.medplat.dashboard.aientabled.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class DashboardConfigurationDto implements Serializable {
    private Long id;
    private Long userId;
    private String dashboardName;
    private String description;
    private String dashboardType;
    private String roleBasedAccess;
    private Boolean isDefault;
    private Boolean isPublic;
    private Boolean isActive;
    private Integer refreshIntervalSeconds;
    private Boolean aiEnabled;
    private Boolean nlpEnabled;
    private Boolean anomalyDetectionEnabled;
    private Boolean predictiveAnalyticsEnabled;
    private Boolean recommendationEnabled;
    @JsonProperty("layoutConfig")
    private Map<String, Object> layoutConfig;
    @JsonProperty("widgets")
    private List<DashboardWidgetDto> widgets;
    @JsonProperty("filters")
    private List<DashboardFilterDto> filters;
    private Date createdDate;
    private Date modifiedDate;
    private Long createdBy;
}
