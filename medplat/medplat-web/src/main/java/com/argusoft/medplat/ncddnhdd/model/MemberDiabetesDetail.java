package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * Define ncd_member_diabetes_detail entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "ncd_member_diabetes_detail")
public class MemberDiabetesDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "gluco_strips_available")
    private Boolean glucoStripsAvailable;

    @Column(name = "blood_sugar")
    private Integer bloodSugar;

    @Column(name = "earlier_diabetes_diagnosis")
    private Boolean earlierDiabetesDiagnosis;

    @Column(name = "currently_under_treatment")
    private Boolean currentlyUnderTreatment;

    @Column(name = "refferal_done")
    private Boolean refferalDone;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "fasting_blood_sugar")
    private Integer fastingBloodSugar;

    @Column(name = "post_prandial_blood_sugar")
    private Integer postPrandialBloodSugar;

    @Column(name = "hba1c")
    private BigDecimal hba1c;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "peripheral_pulses")
    private Boolean peripheralPulses;

    @Column(name = "calluses_feet")
    private Boolean callusesFeet;

    @Column(name = "ulcers_feet")
    private Boolean ulcersFeet;

    @Column(name = "gangrene_feet")
    private Boolean gangreneFeet;

    @Column(name = "prominent_veins")
    private Boolean prominentVeins;

    @Column(name = "edema")
    private Boolean edema;

    @Column(name = "any_injuries")
    private Boolean anyInjuries;

    @Column(name = "regular_rythm_cardio")
    private Boolean regularRythmCardio;

    @Column(name = "sensory_loss")
    private Boolean sensoryLoss;

    @Column(name = "referral_id ")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "dka")
    private Boolean dka;

    @Column(name = "current_treatment_place")
    private String currentTreatmentPlace;

    @Column(name = "current_treatment_place_other")
    private String currentTreatmentPlaceOther;

    @Column(name = "is_continue_treatment_from_current_place")
    private Boolean isContinueTreatmentFromCurrentPlace;

    @Column(name = "measurement_type")
    private String measurementType;

    @Column(name = "is_suspected")
    private Boolean isSuspected;

    @Column(name = "urine_sugar")
    private Integer urineSugar;

    @Column(name = "status")
    private String status;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

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
    @Column(name = "weight", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float weight;
    @Column(name = "height")
    private Integer height;
    @Column(name = "bmi", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float bmi;
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

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

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

    public Boolean getGlucoStripsAvailable() {
        return glucoStripsAvailable;
    }

    public void setGlucoStripsAvailable(Boolean glucoStripsAvailable) {
        this.glucoStripsAvailable = glucoStripsAvailable;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Boolean getEarlierDiabetesDiagnosis() {
        return earlierDiabetesDiagnosis;
    }

    public void setEarlierDiabetesDiagnosis(Boolean earlierDiabetesDiagnosis) {
        this.earlierDiabetesDiagnosis = earlierDiabetesDiagnosis;
    }

    public Boolean getCurrentlyUnderTreatment() {
        return currentlyUnderTreatment;
    }

    public void setCurrentlyUnderTreatment(Boolean currentlyUnderTreatment) {
        this.currentlyUnderTreatment = currentlyUnderTreatment;
    }

    public Boolean getRefferalDone() {
        return refferalDone;
    }

    public void setRefferalDone(Boolean refferalDone) {
        this.refferalDone = refferalDone;
    }

    public String getRefferalPlace() {
        return refferalPlace;
    }

    public void setRefferalPlace(String refferalPlace) {
        this.refferalPlace = refferalPlace;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getFastingBloodSugar() {
        return fastingBloodSugar;
    }

    public void setFastingBloodSugar(Integer fastingBloodSugar) {
        this.fastingBloodSugar = fastingBloodSugar;
    }

    public Integer getPostPrandialBloodSugar() {
        return postPrandialBloodSugar;
    }

    public void setPostPrandialBloodSugar(Integer postPrandialBloodSugar) {
        this.postPrandialBloodSugar = postPrandialBloodSugar;
    }

    public BigDecimal getHba1c() {
        return hba1c;
    }

    public void setHba1c(BigDecimal hba1c) {
        this.hba1c = hba1c;
    }

    public com.argusoft.medplat.ncddnhdd.enums.DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public Boolean getPeripheralPulses() {
        return peripheralPulses;
    }

    public void setPeripheralPulses(Boolean peripheralPulses) {
        this.peripheralPulses = peripheralPulses;
    }

    public Boolean getCallusesFeet() {
        return callusesFeet;
    }

    public void setCallusesFeet(Boolean callusesFeet) {
        this.callusesFeet = callusesFeet;
    }

    public Boolean getUlcersFeet() {
        return ulcersFeet;
    }

    public void setUlcersFeet(Boolean ulcersFeet) {
        this.ulcersFeet = ulcersFeet;
    }

    public Boolean getGangreneFeet() {
        return gangreneFeet;
    }

    public void setGangreneFeet(Boolean gangreneFeet) {
        this.gangreneFeet = gangreneFeet;
    }

    public Boolean getProminentVeins() {
        return prominentVeins;
    }

    public void setProminentVeins(Boolean prominentVeins) {
        this.prominentVeins = prominentVeins;
    }

    public Boolean getEdema() {
        return edema;
    }

    public void setEdema(Boolean edema) {
        this.edema = edema;
    }

    public Boolean getAnyInjuries() {
        return anyInjuries;
    }

    public void setAnyInjuries(Boolean anyInjuries) {
        this.anyInjuries = anyInjuries;
    }

    public Boolean getRegularRythmCardio() {
        return regularRythmCardio;
    }

    public void setRegularRythmCardio(Boolean regularRythmCardio) {
        this.regularRythmCardio = regularRythmCardio;
    }

    public Boolean getSensoryLoss() {
        return sensoryLoss;
    }

    public void setSensoryLoss(Boolean sensoryLoss) {
        this.sensoryLoss = sensoryLoss;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Boolean getDka() {
        return dka;
    }

    public void setDka(Boolean dka) {
        this.dka = dka;
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

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
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

    public Integer getUrineSugar() {
        return urineSugar;
    }

    public void setUrineSugar(Integer urineSugar) {
        this.urineSugar = urineSugar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
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

    /**
     * Define fields name of ncd_member_diabetes_detail.
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
        public static final String REFERRAL_PLACE = "refferalPlace";
        public static final String REMARKS = "remarks";
        public static final String DONE_BY = "doneBy";
        public static final String MEASUREMENT_TYPE = "measurementType";
        public static final String VISIT_TYPE = "visitType";
        public static final String REFERRED_ON = "screeningDate";
        public static final String HEALTH_INFRA_ID = "healthInfraId";
        public static final String HMIS_HEALTH_INFRA_ID = "hmisHealthInfraId";
    }

}
