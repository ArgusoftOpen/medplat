package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DoneBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_member_initial_assessment_detail")
public class MemberInitialAssessmentDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "refferal_done")
    private Boolean refferalDone;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "currently_under_treatement")
    private Boolean currentlyUnderTreatement;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "excess_thirst")
    private Boolean excessThirst;

    @Column(name = "excess_urination")
    private Boolean excessUrination;

    @Column(name = "excess_hunger")
    private Boolean excessHunger;

    @Column(name = " recurrent_skin_GUI")
    private Boolean recurrentSkinGUI;

    @Column(name = "delayed_healing_of_wounds")
    private Boolean delayedHealingOfWounds;

    @Column(name = "change_in_dietary_habits")
    private Boolean changeInDietaryHabits;

    @Column(name = "sudden_visual_disturbances")
    private Boolean suddenVisualDisturbances;

    @Column(name = "significant_edema")
    private Boolean significantEdema;

    @Column(name = "breathlessness")
    private Boolean breathlessness;

    @Column(name = "angina")
    private Boolean angina;

    @Column(name = "consultant_flag")
    private Boolean consultantFlag;

    @Column(name = "intermittent_claudication")
    private Boolean intermittentClaudication;

    @Column(name = "limpness")
    private Boolean limpness;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float weight;

    @Column(name = "waist_circumference")
    private Integer waistCircumference;

    @Column(name = "bmi", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float bmi;

    @Column(name = "form_type")
    private String formType;

    @Column(name = "master_id")
    private Integer masterId;
    @Column(name = "history_disease")
    private String historyDisease;
    @Column(name = "other_disease")
    private String otherDisease;

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

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
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

    public Boolean getExcessThirst() {
        return excessThirst;
    }

    public void setExcessThirst(Boolean excessThirst) {
        this.excessThirst = excessThirst;
    }

    public Boolean getExcessUrination() {
        return excessUrination;
    }

    public void setExcessUrination(Boolean excessUrination) {
        this.excessUrination = excessUrination;
    }

    public Boolean getExcessHunger() {
        return excessHunger;
    }

    public void setExcessHunger(Boolean excessHunger) {
        this.excessHunger = excessHunger;
    }

    public Boolean getRecurrentSkinGUI() {
        return recurrentSkinGUI;
    }

    public void setRecurrentSkinGUI(Boolean recurrentSkinGUI) {
        this.recurrentSkinGUI = recurrentSkinGUI;
    }

    public Boolean getDelayedHealingOfWounds() {
        return delayedHealingOfWounds;
    }

    public void setDelayedHealingOfWounds(Boolean delayedHealingOfWounds) {
        this.delayedHealingOfWounds = delayedHealingOfWounds;
    }

    public Boolean getChangeInDietaryHabits() {
        return changeInDietaryHabits;
    }

    public void setChangeInDietaryHabits(Boolean changeInDietaryHabits) {
        this.changeInDietaryHabits = changeInDietaryHabits;
    }

    public Boolean getSuddenVisualDisturbances() {
        return suddenVisualDisturbances;
    }

    public void setSuddenVisualDisturbances(Boolean suddenVisualDisturbances) {
        this.suddenVisualDisturbances = suddenVisualDisturbances;
    }

    public Boolean getCurrentlyUnderTreatement() {
        return currentlyUnderTreatement;
    }

    public void setCurrentlyUnderTreatement(Boolean currentlyUnderTreatement) {
        this.currentlyUnderTreatement = currentlyUnderTreatement;
    }

    public Boolean getSignificantEdema() {
        return significantEdema;
    }

    public void setSignificantEdema(Boolean significantEdema) {
        this.significantEdema = significantEdema;
    }

    public Boolean getBreathlessness() {
        return breathlessness;
    }

    public void setBreathlessness(Boolean breathlessness) {
        this.breathlessness = breathlessness;
    }

    public Boolean getAngina() {
        return angina;
    }

    public void setAngina(Boolean angina) {
        this.angina = angina;
    }

    public Boolean getIntermittentClaudication() {
        return intermittentClaudication;
    }

    public void setIntermittentClaudication(Boolean intermittentClaudication) {
        this.intermittentClaudication = intermittentClaudication;
    }

    public Boolean getLimpness() {
        return limpness;
    }

    public void setLimpness(Boolean limpness) {
        this.limpness = limpness;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getWaistCircumference() {
        return waistCircumference;
    }

    public void setWaistCircumference(Integer waistCircumference) {
        this.waistCircumference = waistCircumference;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getHistoryDisease() {
        return historyDisease;
    }

    public void setHistoryDisease(String historyDisease) {
        this.historyDisease = historyDisease;
    }

    public String getOtherDisease() {
        return otherDisease;
    }

    public void setOtherDisease(String otherDisease) {
        this.otherDisease = otherDisease;
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
        public static final String HEALTH_INFRA_ID = "healthInfraId";
        public static final String REFERRED_ON = "screeningDate";
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
