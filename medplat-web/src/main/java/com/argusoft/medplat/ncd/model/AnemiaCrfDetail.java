package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "anemia_crf_detail")
@Data
public class AnemiaCrfDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "event_detail")
    private String eventDetail;

    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @Column(name = "event_time")
    private String eventTime;

    @Column(name = "reported_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;

    @Column(name = "severity")
    private String severity;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "other_treatment")
    private String otherTreatment;

    @Column(name = "relationship_to_study")
    private String relationshipToStudy;

    @Column(name = "expected_flag")
    private Boolean expectedFlag;

    @Column(name = "reported_to_iec")
    private Boolean reportedToIec;

    @Column(name = "outcome")
    private String outcome;

    @Column(name = "resolution_description")
    private String resolutionDescription;

    @Column(name = "resolution_ongoing")
    private Boolean resolutionOngoing;

    @Column(name = "resolution_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolutionDate;

    @Column(name = "serious_ae_flag")
    private Boolean seriousAeFlag;

}
