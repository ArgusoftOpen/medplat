package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_member_cvc_home_visit_detail")
@Data
public class MemberCvcHomeVisitDetails extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "location_id")
    private Integer locationId;

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

    @Column(name = "clinic_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clinicDate;

    @Column(name = "patient_taking_medicine")
    private Boolean patientTakingMedicine;

    @Column(name = "any_adverse_effect")
    private Boolean anyAdverseEffect;

    @Column(name = "adverse_effect")
    private String adverseEffect;

    @Column(name = "required_reference")
    private Boolean requiredReference;

    @Column(name = "given_consent")
    private Boolean givenConsent;

    @Column(name = "referral_place")
    private String referralPlace;

    @Column(name = "other_referral_place")
    private String otherReferralPlace;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "reference_reason")
    private String referenceReason;

    @Column(name = "refer_status")
    private String referStatus;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "flag_reason")
    private String flagReason;

    @Column(name = "other_reason")
    private String otherReason;

    @Column(name = "followup_visit_type")
    private String followupVisitType;

    @Column(name = "followup_date")
    private Date followupDate;

    @Column(name = "death_date")
    @Temporal(TemporalType.DATE)
    private Date deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    public enum DoneBy {
        MO, FHW, CHO, MPHW
    }

    public enum Status {
        NORMAL, MILD, SEVERE, CONFIRMED, UNCONTROLLED
    }
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String FAMILY_ID = "familyId";
        public static final String LOCATION_ID = "locationId";
        public static final String SCREENING_DATE = "screeningDate";
        public static final String REFERRAL_PLACE = "refferalplace";
        public static final String REMARKS = "remarks";
        public static final String DONE_BY = "doneBy";
    }
}
