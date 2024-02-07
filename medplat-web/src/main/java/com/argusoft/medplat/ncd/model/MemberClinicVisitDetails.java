package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DoneBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_member_clinic_visit_detail")
public class MemberClinicVisitDetails extends EntityAuditInfo implements Serializable {

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

    @Column(name = "clinic_type")
    private String clinicType;

    @Column(name = "systolic_bp")
    private Integer systolicBp;

    @Column(name = "diastolic_bp")
    private Integer diastolicBp;

    @Column(name = "pulse_rate")
    private Integer pulseRate;

    @Column(name = "hypertension_result")
    private String hypertensionResult;

    @Column(name = "mental_health_result")
    private String mentalHealthResult;

    @Column(name = "talk")
    private Integer talk;

    @Column(name = "own_daily_work")
    private Integer ownDailyWork;

    @Column(name = "social_work")
    private Integer socialWork;

    @Column(name = "understanding")
    private Integer understanding;

    @Column(name = "diabetes_result")
    private String diabetesResult;

    @Column(name = "blood_sugar")
    private Integer bloodSugar;

    @Column(name = "patient_taking_medicine")
    private Boolean patientTakingMedicine;

    @Column(name = "required_reference")
    private Boolean requiredReference;

    @Column(name = "reference_reason")
    private String referenceReason;

    @Column(name = "referral_place")
    private String referralPlace;

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

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "death_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "bmi", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float bmi;

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

    public Date getClinicDate() {
        return clinicDate;
    }

    public void setClinicDate(Date clinicDate) {
        this.clinicDate = clinicDate;
    }

    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getHypertensionResult() {
        return hypertensionResult;
    }

    public void setHypertensionResult(String hypertensionResult) {
        this.hypertensionResult = hypertensionResult;
    }

    public String getMentalHealthResult() {
        return mentalHealthResult;
    }

    public void setMentalHealthResult(String mentalHealthResult) {
        this.mentalHealthResult = mentalHealthResult;
    }

    public Integer getTalk() {
        return talk;
    }

    public void setTalk(Integer talk) {
        this.talk = talk;
    }

    public Integer getOwnDailyWork() {
        return ownDailyWork;
    }

    public void setOwnDailyWork(Integer ownDailyWork) {
        this.ownDailyWork = ownDailyWork;
    }

    public Integer getSocialWork() {
        return socialWork;
    }

    public void setSocialWork(Integer socialWork) {
        this.socialWork = socialWork;
    }

    public Integer getUnderstanding() {
        return understanding;
    }

    public void setUnderstanding(Integer understanding) {
        this.understanding = understanding;
    }

    public String getDiabetesResult() {
        return diabetesResult;
    }

    public void setDiabetesResult(String diabetesResult) {
        this.diabetesResult = diabetesResult;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Boolean getPatientTakingMedicine() {
        return patientTakingMedicine;
    }

    public void setPatientTakingMedicine(Boolean patientTakingMedicine) {
        this.patientTakingMedicine = patientTakingMedicine;
    }

    public Boolean getRequiredReference() {
        return requiredReference;
    }

    public void setRequiredReference(Boolean requiredReference) {
        this.requiredReference = requiredReference;
    }

    public String getReferenceReason() {
        return referenceReason;
    }

    public void setReferenceReason(String referenceReason) {
        this.referenceReason = referenceReason;
    }

    public String getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(String referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getReferStatus() {
        return referStatus;
    }

    public void setReferStatus(String referStatus) {
        this.referStatus = referStatus;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getFlagReason() {
        return flagReason;
    }

    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public String getFollowupVisitType() {
        return followupVisitType;
    }

    public void setFollowupVisitType(String followupVisitType) {
        this.followupVisitType = followupVisitType;
    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

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
