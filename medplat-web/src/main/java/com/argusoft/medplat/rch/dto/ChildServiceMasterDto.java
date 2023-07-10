/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Used for child service master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class ChildServiceMasterDto {

    private Integer id;
    private Integer memberId;
    private String contactNumber;
    private Integer familyId;
    private Date mobileStartDate;
    private Date mobileEndDate;
    private Integer locationId;
    private String memberStatus;
    private Boolean isAlive;
    private Date deathDate;
    private String placeOfDeath;
    private String deathReason;
    private String otherDeathReason;
    private Float weight;
    private Boolean ifaSyrupGiven;
    private Boolean complementaryFeedingStarted;
    private String complementaryFeedingStartPeriod;
    private Set<Integer> dieseases;
    private String otherDiseases;
    private String isTreatementDone;
    private Float midArmCircumference;
    private Integer height;
    private Boolean havePedalEdema;
    private Boolean exclusivelyBreastfeded;
    private Boolean anyVaccinationPending;
    private Date serviceDate;
    private String sdScore;
    private String immunisationGiven;
    private List<ImmunisationDto> immunisationDtos;
    private Boolean isFromWeb;
    private String deliveryPlace;
    private Integer typeOfHospital;
    private Integer healthInfrastructureId;
    private String deliveryDoneBy;
    private Integer deliveryPerson;
    private String deliveryPersonName;
    private Integer deathInfrastructureId;
    private ChildCerebralPalsyMasterDto cerebralDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
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

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getIfaSyrupGiven() {
        return ifaSyrupGiven;
    }

    public void setIfaSyrupGiven(Boolean ifaSyrupGiven) {
        this.ifaSyrupGiven = ifaSyrupGiven;
    }

    public Boolean getComplementaryFeedingStarted() {
        return complementaryFeedingStarted;
    }

    public void setComplementaryFeedingStarted(Boolean complementaryFeedingStarted) {
        this.complementaryFeedingStarted = complementaryFeedingStarted;
    }

    public String getComplementaryFeedingStartPeriod() {
        return complementaryFeedingStartPeriod;
    }

    public void setComplementaryFeedingStartPeriod(String complementaryFeedingStartPeriod) {
        this.complementaryFeedingStartPeriod = complementaryFeedingStartPeriod;
    }

    public Set<Integer> getDieseases() {
        return dieseases;
    }

    public void setDieseases(Set<Integer> dieseases) {
        this.dieseases = dieseases;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public String getIsTreatementDone() {
        return isTreatementDone;
    }

    public void setIsTreatementDone(String isTreatementDone) {
        this.isTreatementDone = isTreatementDone;
    }

    public Float getMidArmCircumference() {
        return midArmCircumference;
    }

    public void setMidArmCircumference(Float midArmCircumference) {
        this.midArmCircumference = midArmCircumference;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getHavePedalEdema() {
        return havePedalEdema;
    }

    public void setHavePedalEdema(Boolean havePedalEdema) {
        this.havePedalEdema = havePedalEdema;
    }

    public Boolean getExclusivelyBreastfeded() {
        return exclusivelyBreastfeded;
    }

    public void setExclusivelyBreastfeded(Boolean exclusivelyBreastfeded) {
        this.exclusivelyBreastfeded = exclusivelyBreastfeded;
    }

    public Boolean getAnyVaccinationPending() {
        return anyVaccinationPending;
    }

    public void setAnyVaccinationPending(Boolean anyVaccinationPending) {
        this.anyVaccinationPending = anyVaccinationPending;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public List<ImmunisationDto> getImmunisationDtos() {
        return immunisationDtos;
    }

    public void setImmunisationDtos(List<ImmunisationDto> immunisationDtos) {
        this.immunisationDtos = immunisationDtos;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
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

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
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

    public ChildCerebralPalsyMasterDto getCerebralDetails() {
        return cerebralDetails;
    }

    public void setCerebralDetails(ChildCerebralPalsyMasterDto cerebralDetails) {
        this.cerebralDetails = cerebralDetails;
    }

    public Integer getDeathInfrastructureId() {
        return deathInfrastructureId;
    }

    public void setDeathInfrastructureId(Integer deathInfrastructureId) {
        this.deathInfrastructureId = deathInfrastructureId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
