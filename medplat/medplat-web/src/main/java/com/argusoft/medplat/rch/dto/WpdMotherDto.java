/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;

/**
 * <p>
 * Used for wpd mother.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
public class WpdMotherDto {

    private Integer id;
    private Integer familyId;
    private Date dateOfDelivery;
    private String memberStatus;
    private Boolean hasDeliveryHappened;
    private Boolean corticoSteroidGiven;
    private Boolean isPretermBirth;
    private String deliveryPlace;
    private String institutionalDeliveryPlace;
    private Integer typeOfHospital;
    private String deliveryDoneBy;
    private String typeOfDelivery;
    private Boolean motherAlive;
    private Integer healthInfrastructureId;
    private String otherDangerSigns;
    private String referralDone;
    private Integer referralPlace;
    private Boolean isDischarged;
    private Date dischargeDate;
    private Boolean breastFeedingInOneHour;
    private Integer mtpDoneAt;
    private String mtpPerformedBy;
    private Date deathDate;
    private String placeOfDeath;
    private String deathReason;
    private Boolean isHighRiskCase;
    private Integer pregnancyRegDetId;
    private String pregnancyOutcome;
    private Boolean misoprostolGiven;
    private String freeDropDelivery;
    private Integer deliveryPerson;
    private String deliveryPersonName;
    private Date createdOn;
    private Date modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer motherReferralInfraId;

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(Date dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Boolean getHasDeliveryHappened() {
        return hasDeliveryHappened;
    }

    public void setHasDeliveryHappened(Boolean hasDeliveryHappened) {
        this.hasDeliveryHappened = hasDeliveryHappened;
    }

    public Boolean getCorticoSteroidGiven() {
        return corticoSteroidGiven;
    }

    public void setCorticoSteroidGiven(Boolean corticoSteroidGiven) {
        this.corticoSteroidGiven = corticoSteroidGiven;
    }

    public Boolean getIsPretermBirth() {
        return isPretermBirth;
    }

    public void setIsPretermBirth(Boolean isPretermBirth) {
        this.isPretermBirth = isPretermBirth;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Integer getTypeOfHospital() {
        return typeOfHospital;
    }

    public void setTypeOfHospital(Integer typeOfHospital) {
        this.typeOfHospital = typeOfHospital;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public Boolean getMotherAlive() {
        return motherAlive;
    }

    public void setMotherAlive(Boolean motherAlive) {
        this.motherAlive = motherAlive;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getOtherDangerSigns() {
        return otherDangerSigns;
    }

    public void setOtherDangerSigns(String otherDangerSigns) {
        this.otherDangerSigns = otherDangerSigns;
    }

    public String getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(String referralDone) {
        this.referralDone = referralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public Boolean getIsDischarged() {
        return isDischarged;
    }

    public void setIsDischarged(Boolean isDischarged) {
        this.isDischarged = isDischarged;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Boolean getBreastFeedingInOneHour() {
        return breastFeedingInOneHour;
    }

    public void setBreastFeedingInOneHour(Boolean breastFeedingInOneHour) {
        this.breastFeedingInOneHour = breastFeedingInOneHour;
    }

    public Integer getMtpDoneAt() {
        return mtpDoneAt;
    }

    public void setMtpDoneAt(Integer mtpDoneAt) {
        this.mtpDoneAt = mtpDoneAt;
    }

    public String getMtpPerformedBy() {
        return mtpPerformedBy;
    }

    public void setMtpPerformedBy(String mtpPerformedBy) {
        this.mtpPerformedBy = mtpPerformedBy;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public String getPregnancyOutcome() {
        return pregnancyOutcome;
    }

    public void setPregnancyOutcome(String pregnancyOutcome) {
        this.pregnancyOutcome = pregnancyOutcome;
    }

    public Boolean getMisoprostolGiven() {
        return misoprostolGiven;
    }

    public void setMisoprostolGiven(Boolean misoprostolGiven) {
        this.misoprostolGiven = misoprostolGiven;
    }

    public String getFreeDropDelivery() {
        return freeDropDelivery;
    }

    public void setFreeDropDelivery(String freeDropDelivery) {
        this.freeDropDelivery = freeDropDelivery;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public String getInstitutionalDeliveryPlace() {
        return institutionalDeliveryPlace;
    }

    public void setInstitutionalDeliveryPlace(String institutionalDeliveryPlace) {
        this.institutionalDeliveryPlace = institutionalDeliveryPlace;
    }

    public Integer getMotherReferralInfraId() {
        return motherReferralInfraId;
    }

    public void setMotherReferralInfraId(Integer motherReferralInfraId) {
        this.motherReferralInfraId = motherReferralInfraId;
    }
}
