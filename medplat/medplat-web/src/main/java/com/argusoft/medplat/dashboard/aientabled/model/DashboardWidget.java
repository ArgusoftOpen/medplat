package com.argusoft.medplat.dashboard.aientabled.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "dashboard_widget")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardWidget implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dashboard_id", nullable = false)
    private Long dashboardId;
    @Column(name = "widget_name", nullable = false)
    private String widgetName;
    @Column(name = "widget_type", nullable = false)
    private String widgetType;
    @Column(name = "widget_position")
    private Integer position;
    @Column(name = "grid_row")
    private Integer gridRow;
    @Column(name = "grid_column")
    private Integer gridColumn;
    @Column(name = "grid_width")
    private Integer gridWidth = 4;
    @Column(name = "grid_height")
    private Integer gridHeight = 200;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "data_source_id")
    private Long dataSourceId;
    @Column(name = "query_configuration", columnDefinition = "jsonb")
    private String queryConfiguration;
    @Column(name = "visualization_config", columnDefinition = "jsonb")
    private String visualizationConfig;
    @Column(name = "display_options", columnDefinition = "jsonb")
    private String displayOptions;
    @Column(name = "aggregation_type")
    private String aggregationType;
    @Column(name = "time_series_enabled")
    private Boolean timeSeriesEnabled = false;
    @Column(name = "time_granularity")
    private String timeGranularity;
    @Column(name = "cache_duration_seconds")
    private Integer cacheDurationSeconds = 300;
    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds = 300;
    @Column(name = "ai_anomaly_detection")
    private Boolean aiAnomalyDetection = false;
    @Column(name = "ai_forecast")
    private Boolean aiForecast = false;
    @Column(name = "ai_insights")
    private Boolean aiInsights = true;
    @Column(name = "ai_config", columnDefinition = "jsonb")
    private String aiConfig;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();
}
