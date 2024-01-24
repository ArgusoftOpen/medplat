package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DoneBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_member_general_detail")
public class MemberGeneralDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "clinical_observation")
    private String clinicalObservation;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "comment")
    private String comment;

    @Column(name = "consultant_flag")
    private Boolean consultantFlag;

    @Column(name = "other_details")
    private String otherDetails;

    @Column(name = "mark_review")
    private Boolean markReview;

    @Column(name = "does_required_ref")
    private Boolean doesRequiredRef;

    @Column(name ="refferral_reason")
    private String  refferralReason;

    @Column(name = "is_suspected")
    private Boolean isSuspected;

    @Column(name = "followup_place")
    private String followupPlace;

    @Column(name = "followup_date")
    private Date followUpDate;

    @Column(name = "category", length = 50)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "master_id")
    private Integer masterId;

    @Column(name = "take_medicine")
    private Boolean takeMedicine;

    @Column(name = "treatment_status")
    private String treatmentStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getMobileStartDate() {
        return mobileStartDate;
    }

    public void setMobileStartDate(Date mobileStartDate) {
        this.mobileStartDate = mobileStartDate;
    }

    public Date getMobileEndDate() {
        return mobileEndDate;
    }

    public void setMobileEndDate(Date mobileEndDate) {
        this.mobileEndDate = mobileEndDate;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public com.argusoft.medplat.ncd.enums.DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(com.argusoft.medplat.ncd.enums.DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public String getRefferalPlace() {
        return refferalPlace;
    }

    public void setRefferalPlace(String refferalPlace) {
        this.refferalPlace = refferalPlace;
    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getClinicalObservation() {
        return clinicalObservation;
    }

    public void setClinicalObservation(String clinicalObservation) {
        this.clinicalObservation = clinicalObservation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Boolean getMarkReview() {
        return markReview;
    }

    public void setMarkReview(Boolean markReview) {
        this.markReview = markReview;
    }

    public Boolean getDoesRequiredRef() {
        return doesRequiredRef;
    }

    public void setDoesRequiredRef(Boolean doesRequiredRef) {
        this.doesRequiredRef = doesRequiredRef;
    }

    public String getRefferralReason() {
        return refferralReason;
    }

    public void setRefferralReason(String refferralReason) {
        this.refferralReason = refferralReason;
    }

    public Boolean getSuspected() {
        return isSuspected;
    }

    public void setSuspected(Boolean suspected) {
        isSuspected = suspected;
    }

    public String getFollowupPlace() {
        return followupPlace;
    }

    public void setFollowupPlace(String followupPlace) {
        this.followupPlace = followupPlace;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
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
        public static final String REFERRED_ON = "screeningDate";
        public static final String HEALTH_INFRA_ID = "healthInfraId";
    }

    public enum Category {
        RCH, COMMUNICABLEDISEASE, MINORAILMENTS, OPHTHALMIC, ENT, ORALCARE, GERIATRICMEDICINE, EMERGENCY_TRAUMA, MENTALHEALTH, OTHER
    }

}
