package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.enums.SubType;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class MemberInitialAssessmentDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date doneOn;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Integer healthInfraId;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private Status status;
    private Date followUpDate;
    private String reason;
    private Integer referredFromHealthInfrastructureId;
    private String readings;
    private SubType subType;
    private List<Integer> medicines;
    private Boolean excessThirst;
    private Boolean excessUrination;
    private Boolean excessHunger;
    private Boolean recurrentSkinGUI;
    private Boolean delayedHealingOfWounds;
    private Boolean changeInDietaryHabits;
    private Boolean suddenVisualDisturbances;
    private Boolean significantEdema;
    private Boolean breathlessness;
    private Boolean angina;
    private Boolean intermittentClaudication;
    private Boolean limpness;
    private Integer height;
    private Float weight;
    private Integer waistCircumference;
    private Float bmi;
    private Boolean consultantFlag;

    private String formType;
    private String selectedHistoryDisease;
    private String otherDisease;

    public List<Integer> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Integer> medicines) {
        this.medicines = medicines;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
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

    public Integer getLocationId() {
        return locationId;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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

    public ReferralPlace getRefTo() {
        return refTo;
    }

    public void setRefTo(ReferralPlace refTo) {
        this.refTo = refTo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ReferralPlace getRefFrom() {
        return refFrom;
    }

    public void setRefFrom(ReferralPlace refFrom) {
        this.refFrom = refFrom;
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

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getSelectedHistoryDisease() {
        return selectedHistoryDisease;
    }

    public void setSelectedHistoryDisease(String selectedHistoryDisease) {
        this.selectedHistoryDisease = selectedHistoryDisease;
    }

    public String getOtherDisease() {
        return otherDisease;
    }

    public void setOtherDisease(String otherDisease) {
        this.otherDisease = otherDisease;
    }

    @Override
    public String toString() {
        return "MemberInitialAssessmentDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", familyId=" + familyId +
                ", screeningDate=" + screeningDate +
                ", doneBy=" + doneBy +
                ", doneOn=" + doneOn +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", referralId=" + referralId +
                ", healthInfraId=" + healthInfraId +
                ", refTo=" + refTo +
                ", refFrom=" + refFrom +
                ", status=" + status +
                ", followUpDate=" + followUpDate +
                ", reason='" + reason + '\'' +
                ", referredFromHealthInfrastructureId=" + referredFromHealthInfrastructureId +
                ", readings='" + readings + '\'' +
                ", subType=" + subType +
                ", medicines=" + medicines +
                ", excessThirst=" + excessThirst +
                ", excessUrination=" + excessUrination +
                ", excessHunger=" + excessHunger +
                ", recurrentSkinGUI=" + recurrentSkinGUI +
                ", delayedHealingOfWounds=" + delayedHealingOfWounds +
                ", changeInDietaryHabits=" + changeInDietaryHabits +
                ", suddenVisualDisturbances=" + suddenVisualDisturbances +
                ", significantEdema=" + significantEdema +
                ", breathlessness=" + breathlessness +
                ", angina=" + angina +
                ", intermittentClaudication=" + intermittentClaudication +
                ", limpness=" + limpness +
                ", height=" + height +
                ", weight=" + weight +
                ", waistCircumference=" + waistCircumference +
                ", bmi=" + bmi +
                ", consultantFlag=" + consultantFlag +
                ", formType='" + formType + '\'' +
                '}';
    }
}
