package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <p>
 * Define ncd_member_hypertension_detail entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "ncd_member_hypertension_detail")
public class MemberHypertensionDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "bp_machine_available")
    private Boolean bpMachineAvailable;

    @Column(name = "systolic_bp")
    private Integer systolicBp;

    @Column(name = "diastolic_bp")
    private Integer diastolicBp;

    @Column(name = "pulse_rate")
    private Integer pulseRate;

    @Column(name = "diagnosed_earlier")
    private Boolean diagnosedEarlier;

    @Column(name = "currently_under_treatement")
    private Boolean currentlyUnderTreatement;

    @Column(name = "refferal_done")
    private Boolean refferaldone;

    @Column(name = "refferal_place")
    private String refferalplace;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "is_regular_rythm")
    private Boolean isRegularRythm;

    @Column(name = "murmur")
    private Boolean murmur;

    @Column(name = "bilateral_clear")
    private Boolean bilateralClear;

    @Column(name = "bilateral_basal_crepitation")
    private Boolean bilateralBasalCrepitation;

    @Column(name = "rhonchi")
    private Boolean rhonchi;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "current_treatment_place")
    private String currentTreatmentPlace;

    @Column(name = "current_treatment_place_other")
    private String currentTreatmentPlaceOther;

    @Column(name = "is_continue_treatment_from_current_place")
    private Boolean isContinueTreatmentFromCurrentPlace;

    @Column(name = "systolic_bp2")
    private Integer systolicBp2;

    @Column(name = "diastolic_bp2")
    private Integer diastolicBp2;

    @Column(name = "pulse_rate2")
    private Integer pulseRate2;

    @Column(name = "is_suspected")
    private Boolean isSuspected;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "status")
    private String status;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "bmi", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float bmi;

    @Column(name = "waist")
    private Integer waist;

    @Column(name = "disease_history")
    private String diseaseHistory;

    @Column(name = "other_disease")
    private String otherDisease;

    @Column(name = "risk_factor")
    private String riskFactor;

    @Column(name = "undertake_physical_activity")
    private Boolean undertakePhysicalActivity;

    @Column(name = "have_family_history")
    private Boolean haveFamilyHistory;

    @Column(name = "note")
    private String note;
    @Column(name = "master_id")
    private Integer masterId;
    @Column(name = "take_medicine")
    private Boolean takeMedicine;
    @Column(name = "treatment_status")
    private String treatmentStatus;
    @Column(name = "visit_type")
    private String visitType;
    @Column(name = "pedal_oedema")
    private Boolean pedalOedema;
    @Column(name = "hmis_health_infra_id")
    private Integer hmisHealthInfraId;

    @Column(name = "hyper_dia_mental_master_id")
    private Integer hyperDiaMentalMasterId;

    @Column(name = "govt_facility_id")
    private Integer govtFacilityId;

    @Column(name = "private_facility")
    private String privateFacility;

    @Column(name = "out_of_territory_facility")
    private String outOfTerritoryFacility;

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

    public Boolean getBpMachineAvailable() {
        return bpMachineAvailable;
    }

    public void setBpMachineAvailable(Boolean bpMachineAvailable) {
        this.bpMachineAvailable = bpMachineAvailable;
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

    public Boolean getDiagnosedEarlier() {
        return diagnosedEarlier;
    }

    public void setDiagnosedEarlier(Boolean diagnosedEarlier) {
        this.diagnosedEarlier = diagnosedEarlier;
    }

    public Boolean getCurrentlyUnderTreatement() {
        return currentlyUnderTreatement;
    }

    public void setCurrentlyUnderTreatement(Boolean currentlyUnderTreatement) {
        this.currentlyUnderTreatement = currentlyUnderTreatement;
    }

    public Boolean getRegularRythm() {
        return isRegularRythm;
    }

    public void setRegularRythm(Boolean regularRythm) {
        isRegularRythm = regularRythm;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public Boolean getRefferaldone() {
        return refferaldone;
    }

    public void setRefferaldone(Boolean refferaldone) {
        this.refferaldone = refferaldone;
    }

    public String getRefferalplace() {
        return refferalplace;
    }

    public void setRefferalplace(String refferalplace) {
        this.refferalplace = refferalplace;
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

    public Boolean getIsRegularRythm() {
        return isRegularRythm;
    }

    public void setIsRegularRythm(Boolean isRegularRythm) {
        this.isRegularRythm = isRegularRythm;
    }

    public Boolean getMurmur() {
        return murmur;
    }

    public void setMurmur(Boolean murmur) {
        this.murmur = murmur;
    }

    public Boolean getBilateralClear() {
        return bilateralClear;
    }

    public void setBilateralClear(Boolean bilateralClear) {
        this.bilateralClear = bilateralClear;
    }

    public Boolean getBilateralBasalCrepitation() {
        return bilateralBasalCrepitation;
    }

    public void setBilateralBasalCrepitation(Boolean bilateralBasalCrepitation) {
        this.bilateralBasalCrepitation = bilateralBasalCrepitation;
    }

    public Boolean getRhonchi() {
        return rhonchi;
    }

    public void setRhonchi(Boolean rhonchi) {
        this.rhonchi = rhonchi;
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

    public String getCurrentTreatmentPlace() {
        return currentTreatmentPlace;
    }

    public void setCurrentTreatmentPlace(String currentTreatmentPlace) {
        this.currentTreatmentPlace = currentTreatmentPlace;
    }

    public Boolean getContinueTreatmentFromCurrentPlace() {
        return isContinueTreatmentFromCurrentPlace;
    }

    public void setContinueTreatmentFromCurrentPlace(Boolean continueTreatmentFromCurrentPlace) {
        isContinueTreatmentFromCurrentPlace = continueTreatmentFromCurrentPlace;
    }

    public Integer getSystolicBp2() {
        return systolicBp2;
    }

    public void setSystolicBp2(Integer systolicBp2) {
        this.systolicBp2 = systolicBp2;
    }

    public Integer getDiastolicBp2() {
        return diastolicBp2;
    }

    public void setDiastolicBp2(Integer diastolicBp2) {
        this.diastolicBp2 = diastolicBp2;
    }

    public Integer getPulseRate2() {
        return pulseRate2;
    }

    public void setPulseRate2(Integer pulseRate2) {
        this.pulseRate2 = pulseRate2;
    }

    public Boolean getSuspected() {
        return isSuspected;
    }

    public void setSuspected(Boolean suspected) {
        isSuspected = suspected;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getCurrentTreatmentPlaceOther() {
        return currentTreatmentPlaceOther;
    }

    public void setCurrentTreatmentPlaceOther(String currentTreatmentPlaceOther) {
        this.currentTreatmentPlaceOther = currentTreatmentPlaceOther;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        this.waist = waist;
    }

    public String getDiseaseHistory() {
        return diseaseHistory;
    }

    public void setDiseaseHistory(String diseaseHistory) {
        this.diseaseHistory = diseaseHistory;
    }

    public String getOtherDisease() {
        return otherDisease;
    }

    public void setOtherDisease(String otherDisease) {
        this.otherDisease = otherDisease;
    }

    public String getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(String riskFactor) {
        this.riskFactor = riskFactor;
    }

    public Boolean getUndertakePhysicalActivity() {
        return undertakePhysicalActivity;
    }

    public void setUndertakePhysicalActivity(Boolean undertakePhysicalActivity) {
        this.undertakePhysicalActivity = undertakePhysicalActivity;
    }

    public Boolean getHaveFamilyHistory() {
        return haveFamilyHistory;
    }

    public void setHaveFamilyHistory(Boolean haveFamilyHistory) {
        this.haveFamilyHistory = haveFamilyHistory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Boolean getPedalOedema() {
        return pedalOedema;
    }

    public void setPedalOedema(Boolean pedalOedema) {
        this.pedalOedema = pedalOedema;
    }

    public Integer getHmisHealthInfraId() {
        return hmisHealthInfraId;
    }

    public void setHmisHealthInfraId(Integer hmisHealthInfraId) {
        this.hmisHealthInfraId = hmisHealthInfraId;
    }

    public Integer getHyperDiaMentalMasterId() {
        return hyperDiaMentalMasterId;
    }

    public void setHyperDiaMentalMasterId(Integer hyperDiaMentalMasterId) {
        this.hyperDiaMentalMasterId = hyperDiaMentalMasterId;
    }

    public Integer getGovtFacilityId() {
        return govtFacilityId;
    }

    public void setGovtFacilityId(Integer govtFacilityId) {
        this.govtFacilityId = govtFacilityId;
    }

    public String getPrivateFacility() {
        return privateFacility;
    }

    public void setPrivateFacility(String privateFacility) {
        this.privateFacility = privateFacility;
    }

    public String getOutOfTerritoryFacility() {
        return outOfTerritoryFacility;
    }

    public void setOutOfTerritoryFacility(String outOfTerritoryFacility) {
        this.outOfTerritoryFacility = outOfTerritoryFacility;
    }

    @Override
    public String toString() {
        return "MemberHypertensionDetail{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", familyId=" + familyId +
                ", locationId=" + locationId +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", mobileStartDate=" + mobileStartDate +
                ", mobileEndDate=" + mobileEndDate +
                ", screeningDate=" + screeningDate +
                ", bpMachineAvailable=" + bpMachineAvailable +
                ", systolicBp=" + systolicBp +
                ", diastolicBp=" + diastolicBp +
                ", pulseRate=" + pulseRate +
                ", diagnosedEarlier=" + diagnosedEarlier +
                ", currentlyUnderTreatement=" + currentlyUnderTreatement +
                ", refferaldone=" + refferaldone +
                ", refferalplace='" + refferalplace + '\'' +
                ", remarks='" + remarks + '\'' +
                ", doneBy=" + doneBy +
                ", doneOn=" + doneOn +
                ", isRegularRythm=" + isRegularRythm +
                ", murmur=" + murmur +
                ", bilateralClear=" + bilateralClear +
                ", bilateralBasalCrepitation=" + bilateralBasalCrepitation +
                ", rhonchi=" + rhonchi +
                ", referralId=" + referralId +
                ", healthInfraId=" + healthInfraId +
                ", currentTreatmentPlace='" + currentTreatmentPlace + '\'' +
                ", currentTreatmentPlaceOther='" + currentTreatmentPlaceOther + '\'' +
                ", isContinueTreatmentFromCurrentPlace=" + isContinueTreatmentFromCurrentPlace +
                ", systolicBp2=" + systolicBp2 +
                ", diastolicBp2=" + diastolicBp2 +
                ", pulseRate2=" + pulseRate2 +
                ", isSuspected=" + isSuspected +
                ", doesSuffering=" + doesSuffering +
                ", flag=" + flag +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * Define fields name for ncd_member_hypertension_detail.
     */
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
        public static final String HMIS_HEALTH_INFRA_ID = "hmisHealthInfraId";
        public static final String REFERRED_ON = "screeningDate";
    }

}
