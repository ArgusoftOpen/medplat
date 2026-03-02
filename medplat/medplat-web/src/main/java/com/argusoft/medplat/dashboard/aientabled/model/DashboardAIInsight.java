package com.argusoft.medplat.dashboard.aientabled.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "dashboard_ai_insight")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAIInsight implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "widget_id", nullable = false)
    private Long widgetId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "insight_type", nullable = false)
    private String insightType; 
    @Column(name = "severity")
    private String severity; 
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "insight_data", columnDefinition = "jsonb")
    private String insightData; 
    @Column(name = "confidence_score")
    private Double confidenceScore; 
    @Column(name = "relevant_metrics", columnDefinition = "jsonb")
    private String relevantMetrics; 
    @Column(name = "recommended_action")
    private String recommendedAction;
    @Column(name = "reference_link")
    private String referenceLink; 
    @Column(name = "ai_model_used")
    private String aiModelUsed; 
    @Column(name = "is_acknowledged")
    private Boolean isAcknowledged = false;
    @Column(name = "acknowledged_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acknowledgedDate;
    @Column(name = "is_resolved")
    private Boolean isResolved = false;
    @Column(name = "resolved_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedDate;
    @Column(name = "user_feedback")
    private String userFeedback; 
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    @Column(name = "expires_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresDate;
}
