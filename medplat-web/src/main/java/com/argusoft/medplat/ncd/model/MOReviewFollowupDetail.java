package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_mo_review_followup_detail")
@Data
public class MOReviewFollowupDetail extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "location_id")
    private Integer locationId;
    @Column(name = "health_infra_id")
    private Integer healthInfraId;
    @Column(name = "screening_date")
    private Date screeningDate;
    @Column(name = "followup_date")
    private Date followUpDate;
    @Column(name = "refferral_place")
    private String refferralPlace;
    @Column(name = "does_required_ref")
    private Boolean doesRequiredRef;
    @Column(name = "refferral_reason")
    private String  refferralReason;
    @Column(name = "followup_place")
    private String followupPlace;
    @Column(name = "comment")
    private String comment;
    @Column(name = "is_remove")
    private Boolean isRemove;
    @Column(name = "diseases")
    private String diseases;
    @Column(name = "other_reason")
    private String otherReason;

    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String SCREENING_DATE = "screeningDate";
    }
}
