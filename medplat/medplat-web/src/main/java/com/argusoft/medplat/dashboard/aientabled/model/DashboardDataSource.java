package com.argusoft.medplat.dashboard.aientabled.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "dashboard_data_source")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataSource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "source_name", nullable = false)
    private String sourceName;
    @Column(name = "source_type", nullable = false)
    private String sourceType;
    @Column(name = "description")
    private String description;
    @Column(name = "connection_config", columnDefinition = "jsonb")
    private String connectionConfig;
    @Column(name = "query", columnDefinition = "text")
    private String query;
    @Column(name = "schema_mapping", columnDefinition = "jsonb")
    private String schemaMapping;
    @Column(name = "caching_strategy")
    private String cachingStrategy;
    @Column(name = "cache_ttl_seconds")
    private Integer cacheTtlSeconds = 3600;
    @Column(name = "retry_attempts")
    private Integer retryAttempts = 3;
    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds = 30;
    @Column(name = "pagination_enabled")
    private Boolean paginationEnabled = false;
    @Column(name = "page_size")
    private Integer pageSize = 1000;
    @Column(name = "validation_rules", columnDefinition = "jsonb")
    private String validationRules;
    @Column(name = "data_transformation", columnDefinition = "jsonb")
    private String dataTransformation;
    @Column(name = "real_time_enabled")
    private Boolean realTimeEnabled = false;
    @Column(name = "streaming_config", columnDefinition = "jsonb")
    private String streamingConfig;
    @Column(name = "required_role")
    private String requiredRole;
    @Column(name = "is_public")
    private Boolean isPublic = false;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "sample_data", columnDefinition = "jsonb")
    private String sampleData;
    @Column(name = "total_records")
    private Long totalRecords;
    @Column(name = "last_synced")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSynced;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();
    @Column(name = "created_by")
    private Long createdBy;
}
