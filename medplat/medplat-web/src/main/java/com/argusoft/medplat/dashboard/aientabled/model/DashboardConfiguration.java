package com.argusoft.medplat.dashboard.aientabled.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "dashboard_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardConfiguration implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "dashboard_name", nullable = false)
    private String dashboardName;
    @Column(name = "description")
    private String description;
    @Column(name = "dashboard_type")
    private String dashboardType;
    @Column(name = "configuration", columnDefinition = "jsonb")
    private String configuration;
    @Column(name = "is_default")
    private Boolean isDefault = false;
    @Column(name = "is_public")
    private Boolean isPublic = false;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "role_based_access")
    private String roleBasedAccess;
    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds = 300;
    @Column(name = "ai_enabled")
    private Boolean aiEnabled = true;
    @Column(name = "nlp_enabled")
    private Boolean nlpEnabled = true;
    @Column(name = "anomaly_detection_enabled")
    private Boolean anomalyDetectionEnabled = false;
    @Column(name = "predictive_analytics_enabled")
    private Boolean predictiveAnalyticsEnabled = false;
    @Column(name = "recommendation_enabled")
    private Boolean recommendationEnabled = true;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();
    @Column(name = "created_by")
    private Long createdBy;
    @Version
    @Column(name = "version")
    private Long version;
}
