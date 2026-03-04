package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Add description here
 * </p>
 *
 * @author namrata
 * @since 28/04/2022 9:56 AM
 */
@Entity
@Table(name = "ncd_member_mental_health_detail")
@Data
public class MemberMentalHealthDetails extends EntityAuditInfo implements Serializable {

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

    @Column(name = "screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;

    @Column(name = "referral_done")
    private Boolean referralDone;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "suffering_earlier")
    private Boolean sufferingEarlier;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "currently_under_treatement")
    private Boolean currentlyUnderTreatement;

    @Column(name = "current_treatment_place")
    private String currentTreatmentPlace;

    @Column(name = "current_treatment_place_other")
    private String currentTreatmentPlaceOther;

    @Column(name = "is_continue_treatment_from_current_place")
    private Boolean isContinueTreatmentFromCurrentPlace;

    @Column(name = "observation")
    private String observation;

    @Column(name = "talk")
    private Integer talk;

    @Column(name = "own_daily_work")
    private Integer ownDailyWork;

    @Column(name = "social_work")
    private Integer socialWork;

    @Column(name = "understanding")
    private Integer understanding;

    @Column(name = "today_result")
    @Enumerated(EnumType.STRING)
    private MemberMentalHealthDetails.Status todayResult;

    @Column(name = "is_suffering")
    private Boolean isSuffering;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "status")
    private String status;

    @Column(name = "other_diagnosis")
    private String otherDiagnosis;

    @Column(name = "other_problems")
    private String otherProblems;

    @Column(name = "note")
    private String note;

    @Column(name = "master_id")
    private Integer masterId;

    @Column(name = "take_medicine")
    private Boolean takeMedicine;

    @Column(name = "treatment_status")
    private String treatmentStatus;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "hyper_dia_mental_master_id")
    private Integer hyperDiaMentalMasterId;

    @Column(name = "govt_facility_id")
    private Integer govtFacilityId;

    @Column(name = "private_facility")
    private String privateFacility;

    @Column(name = "out_of_territory_facility")
    private String outOfTerritoryFacility;

    @Column(name = "is_suspected")
    private Boolean isSuspected;
    public enum DoneBy {
        MO, FHW, CHO, MPHW
    }

    public enum Status {
        NORMAL, MILD, SEVERE, CONFIRMED, CONTROLLED, UNCONTROLLED
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
        public static final String VISIT_TYPE = "visitType";
        public static final String HEALTH_INFRA_ID = "healthInfraId";
    }
}
