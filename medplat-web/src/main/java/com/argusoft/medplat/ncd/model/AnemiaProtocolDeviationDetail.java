package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "anemia_protocol_deviation_detail")
@Data
public class AnemiaProtocolDeviationDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "major_deviation")
    private String majorDeviation;

    @Column(name = "other_major_deviation")
    private String otherMajorDeviation;

    @Column(name = "minor_deviation")
    private String minorDeviation;

    @Column(name = "other_minor_deviation")
    private String otherMinorDeviation;

    @Column(name = "additional_comment")
    private String additionalComment;

    @Column(name = "result_in_ae")
    private Boolean resultInAe;

    @Column(name = "participant_withdrawn_result")
    private Boolean participantWithdrawnResult;

    @Column(name = "reportable_to_irb")
    private Boolean reportableToIrb;

    @Column(name = "reported_to_irb")
    private Boolean reportedToIrb;

    @Column(name = "corrective_action_plan")
    private Boolean correctiveActionPlan;

    @Column(name = "action_plan_description")
    private String actionPlanDescription;

    @Column(name = "deviation_plan")
    private String deviationPlan;

}
