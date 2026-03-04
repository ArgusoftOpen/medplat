/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author prateek
 */
public class FamilyDataBean {

    private Integer id;

    private String familyId;

    private String houseNumber;

    private Integer locationId;

    private String address1;

    private String address2;

    private String religion;

    private String caste;

    private Boolean bplFlag;

    private String anganwadiId;

    private Boolean toiletAvailableFlag;

    private Boolean isVerifiedFlag;

    private List<MemberDataBean> members = new ArrayList<>();

    private String state;

    private Integer createdBy;

    private Integer updatedBy;

    private Date createdOn;

    private Date updatedOn;

    private Integer assignedTo;

    private String maaVatsalyaNumber;

    private String rsbyCardNumber;

    private String comment;

    private Boolean vulnerableFlag;

    private Boolean seasonalMigrantFlag;

    private String areaId;

    private String headMemberName;

    private Integer contactPersonId;

    private Boolean anganwadiUpdateFlag;

    private Boolean anyMemberCbacDone;

    private String typeOfHouse;

    private String typeOfToilet;

    private String electricityAvailability;

    private String drinkingWaterSource;

    private String fuelForCooking;

    //Comma Separated
    private String vehicleDetails;

    private String houseOwnershipStatus;

    private String rationCardNumber;

    private String annualIncome;

    private String bplCardNumber;

    private Long lastFhsDate;

    private Long lastMemberNcdScreeningDate;

    private Long lastIdspScreeningDate;

    private Long lastIdsp2ScreeningDate;

    private Boolean isProvidingConsent;

    private String vulnerabilityCriteria;

    private Boolean eligibleForPmjay;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public Boolean getBplFlag() {
        return bplFlag;
    }

    public void setBplFlag(Boolean bplFlag) {
        this.bplFlag = bplFlag;
    }

    public String getAnganwadiId() {
        return anganwadiId;
    }

    public void setAnganwadiId(String anganwadiId) {
        this.anganwadiId = anganwadiId;
    }

    public Boolean getToiletAvailableFlag() {
        return toiletAvailableFlag;
    }

    public void setToiletAvailableFlag(Boolean toiletAvailableFlag) {
        this.toiletAvailableFlag = toiletAvailableFlag;
    }

    public Boolean getIsVerifiedFlag() {
        return isVerifiedFlag;
    }

    public void setIsVerifiedFlag(Boolean isVerifiedFlag) {
        this.isVerifiedFlag = isVerifiedFlag;
    }

    public List<MemberDataBean> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDataBean> members) {
        this.members = members;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getMaaVatsalyaNumber() {
        return maaVatsalyaNumber;
    }

    public void setMaaVatsalyaNumber(String maaVatsalyaNumber) {
        this.maaVatsalyaNumber = maaVatsalyaNumber;
    }

    public String getRsbyCardNumber() {
        return rsbyCardNumber;
    }

    public void setRsbyCardNumber(String rsbyCardNumber) {
        this.rsbyCardNumber = rsbyCardNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getVulnerableFlag() {
        return vulnerableFlag;
    }

    public void setVulnerableFlag(Boolean vulnerableFlag) {
        this.vulnerableFlag = vulnerableFlag;
    }

    public Boolean getSeasonalMigrantFlag() {
        return seasonalMigrantFlag;
    }

    public void setSeasonalMigrantFlag(Boolean seasonalMigrantFlag) {
        this.seasonalMigrantFlag = seasonalMigrantFlag;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getHeadMemberName() {
        return headMemberName;
    }

    public void setHeadMemberName(String headMemberName) {
        this.headMemberName = headMemberName;
    }

    public Integer getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Integer contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public Boolean getAnganwadiUpdateFlag() {
        return anganwadiUpdateFlag;
    }

    public void setAnganwadiUpdateFlag(Boolean anganwadiUpdateFlag) {
        this.anganwadiUpdateFlag = anganwadiUpdateFlag;
    }

    public Boolean getAnyMemberCbacDone() {
        return anyMemberCbacDone;
    }

    public void setAnyMemberCbacDone(Boolean anyMemberCbacDone) {
        this.anyMemberCbacDone = anyMemberCbacDone;
    }

    public String getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(String typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public String getTypeOfToilet() {
        return typeOfToilet;
    }

    public void setTypeOfToilet(String typeOfToilet) {
        this.typeOfToilet = typeOfToilet;
    }

    public String getElectricityAvailability() {
        return electricityAvailability;
    }

    public void setElectricityAvailability(String electricityAvailability) {
        this.electricityAvailability = electricityAvailability;
    }

    public String getDrinkingWaterSource() {
        return drinkingWaterSource;
    }

    public void setDrinkingWaterSource(String drinkingWaterSource) {
        this.drinkingWaterSource = drinkingWaterSource;
    }

    public String getFuelForCooking() {
        return fuelForCooking;
    }

    public void setFuelForCooking(String fuelForCooking) {
        this.fuelForCooking = fuelForCooking;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getHouseOwnershipStatus() {
        return houseOwnershipStatus;
    }

    public void setHouseOwnershipStatus(String houseOwnershipStatus) {
        this.houseOwnershipStatus = houseOwnershipStatus;
    }

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Long getLastFhsDate() {
        return lastFhsDate;
    }

    public void setLastFhsDate(Long lastFhsDate) {
        this.lastFhsDate = lastFhsDate;
    }

    public Long getLastMemberNcdScreeningDate() {
        return lastMemberNcdScreeningDate;
    }

    public void setLastMemberNcdScreeningDate(Long lastMemberNcdScreeningDate) {
        this.lastMemberNcdScreeningDate = lastMemberNcdScreeningDate;
    }

    public String getBplCardNumber() {
        return bplCardNumber;
    }

    public void setBplCardNumber(String bplCardNumber) {
        this.bplCardNumber = bplCardNumber;
    }

    public Long getLastIdspScreeningDate() {
        return lastIdspScreeningDate;
    }

    public void setLastIdspScreeningDate(Long lastIdspScreeningDate) {
        this.lastIdspScreeningDate = lastIdspScreeningDate;
    }

    public Long getLastIdsp2ScreeningDate() {
        return lastIdsp2ScreeningDate;
    }

    public void setLastIdsp2ScreeningDate(Long lastIdsp2ScreeningDate) {
        this.lastIdsp2ScreeningDate = lastIdsp2ScreeningDate;
    }

    public Boolean getProvidingConsent() {
        return isProvidingConsent;
    }

    public void setProvidingConsent(Boolean providingConsent) {
        isProvidingConsent = providingConsent;
    }

    public String getVulnerabilityCriteria() {
        return vulnerabilityCriteria;
    }

    public void setVulnerabilityCriteria(String vulnerabilityCriteria) {
        this.vulnerabilityCriteria = vulnerabilityCriteria;
    }

    public Boolean getEligibleForPmjay() {
        return eligibleForPmjay;
    }

    public void setEligibleForPmjay(Boolean eligibleForPmjay) {
        this.eligibleForPmjay = eligibleForPmjay;
    }
}
