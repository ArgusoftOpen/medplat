/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.model.FamilyBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kunjan
 */
public class FamilyDataBean {

    private String id;

    private String familyId;

    private String houseNumber;

    private String locationId;

    private String address1;

    private String address2;

    private String religion;

    private String caste;

    private Boolean bplFlag;

    private String anganwadiId;

    private Boolean toiletAvailableFlag;

    private Boolean isVerifiedFlag;

    private List<MemberDataBean> members = new ArrayList<>();

    private String type;

    private String state;

    private String createdBy;

    private String updatedBy;

    private Date createdOn;

    private Date updatedOn;

    private Long assignedTo;

    private String maaVatsalyaNumber;

    private String rsbyCardNumber;

    private String comment;

    private Boolean vulnerableFlag;

    private Boolean seasonalMigrantFlag;

    private Boolean isReverificationFlag;

    private String headMemberName;

    private String areaId;

    private Long contactPersonId;

    private Boolean anganwadiUpdateFlag;

    private Boolean anyMemberCbacDone;

    private Long lastFhsDate;

    private Long lastMemberNcdScreeningDate;

    private String rationCardNumber;

    private String bplCardNumber;

    private Long lastIdspScreeningDate;

    private Long lastIdsp2ScreeningDate;

    private Boolean isProvidingConsent;

    private String vulnerabilityCriteria;

    private Boolean eligibleForPmjay;

    private String typeOfHouse;

    private String drinkingWaterSource;

    private String otherTypeOfHouse;

    private String typeOfToilet;

    private String fuelForCooking;

    private String electricityAvailability;

    private String vehicleDetails;

    private String houseOwnershipStatus;

    private String annualIncome;

    private Boolean eveningAvailability;

    private int pendingAbhaCount;

    public FamilyDataBean() {
    }

    public FamilyDataBean(FamilyBean familyBean, List<MemberDataBean> memberDataBeans) {
        this.id = familyBean.getActualId();
        this.familyId = familyBean.getFamilyId();
        this.houseNumber = familyBean.getHouseNumber();
        this.locationId = familyBean.getLocationId();
        this.address1 = familyBean.getAddress1();
        this.address2 = familyBean.getAddress2();
        this.religion = familyBean.getReligion();
        this.caste = familyBean.getCaste();
        this.bplFlag = familyBean.getBplFlag();
        this.anganwadiId = familyBean.getAnganwadiId();
        this.toiletAvailableFlag = familyBean.getToiletAvailableFlag();
        this.isVerifiedFlag = familyBean.getIsVerifiedFlag();
        this.type = familyBean.getType();
        this.state = familyBean.getState();
        this.createdBy = familyBean.getCreatedBy();
        this.updatedBy = familyBean.getUpdatedBy();
        this.createdOn = familyBean.getCreatedOn();
        this.updatedOn = familyBean.getUpdatedOn();
        this.assignedTo = familyBean.getAssignedTo();
        this.members = memberDataBeans;
        this.maaVatsalyaNumber = familyBean.getMaaVatsalyaNumber();
        this.rsbyCardNumber = familyBean.getRsbyCardNumber();
        this.comment = familyBean.getComment();
        this.vulnerableFlag = familyBean.getVulnerableFlag();
        this.seasonalMigrantFlag = familyBean.getSeasonalMigrantFlag();
        this.isReverificationFlag = familyBean.getReverificationFlag();
        this.areaId = familyBean.getAreaId();
        this.contactPersonId = familyBean.getContactPersonId();
        this.anganwadiUpdateFlag = familyBean.getAnganwadiUpdateFlag();
        this.anyMemberCbacDone = familyBean.getAnyMemberCbacDone();
        this.lastFhsDate = familyBean.getLastFhsDate();
        this.lastMemberNcdScreeningDate = familyBean.getLastMemberNcdScreeningDate();
        this.rationCardNumber = familyBean.getRationCardNumber();
        this.bplCardNumber = familyBean.getBplCardNumber();
        this.lastIdspScreeningDate = familyBean.getLastIdspScreeningDate();
        this.lastIdsp2ScreeningDate = familyBean.getLastIdsp2ScreeningDate();
        this.isProvidingConsent = familyBean.getProvidingConsent();
        this.vulnerabilityCriteria = familyBean.getVulnerabilityCriteria();
        this.eligibleForPmjay = familyBean.getEligibleForPmjay();
        this.typeOfHouse = familyBean.getTypeOfHouse();
        this.drinkingWaterSource = familyBean.getDrinkingWaterSource();
        this.vehicleDetails = familyBean.getVehicleDetails();
        this.otherTypeOfHouse = familyBean.getOtherTypeOfHouse();
        this.typeOfToilet = familyBean.getTypeOfToilet();
        this.fuelForCooking = familyBean.getFuelForCooking();
        this.electricityAvailability = familyBean.getElectricityAvailability();
        this.houseOwnershipStatus = familyBean.getHouseOwnershipStatus();
        this.annualIncome = familyBean.getAnnualIncome();
        this.pendingAbhaCount = familyBean.getPendingAbhaCount();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
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

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
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

    public String getHeadMemberName() {
        return headMemberName;
    }

    public void setHeadMemberName(String headMemberName) {
        this.headMemberName = headMemberName;
    }

    public Boolean getReverificationFlag() {
        return isReverificationFlag;
    }

    public void setReverificationFlag(Boolean reverificationFlag) {
        isReverificationFlag = reverificationFlag;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Long getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Long contactPersonId) {
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

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getBplCardNumber() {
        return bplCardNumber;
    }

    public void setBplCardNumber(String bplCardNumber) {
        this.bplCardNumber = bplCardNumber;
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

    public String getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(String typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public String getDrinkingWaterSource() {
        return drinkingWaterSource;
    }

    public void setDrinkingWaterSource(String drinkingWaterSource) {
        this.drinkingWaterSource = drinkingWaterSource;
    }

    public String getOtherTypeOfHouse() {
        return otherTypeOfHouse;
    }

    public void setOtherTypeOfHouse(String otherTypeOfHouse) {
        this.otherTypeOfHouse = otherTypeOfHouse;
    }

    public String getTypeOfToilet() {
        return typeOfToilet;
    }

    public void setTypeOfToilet(String typeOfToilet) {
        this.typeOfToilet = typeOfToilet;
    }

    public String getFuelForCooking() {
        return fuelForCooking;
    }

    public void setFuelForCooking(String fuelForCooking) {
        this.fuelForCooking = fuelForCooking;
    }

    public String getElectricityAvailability() {
        return electricityAvailability;
    }

    public void setElectricityAvailability(String electricityAvailability) {
        this.electricityAvailability = electricityAvailability;
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

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Boolean getEveningAvailability() {
        return eveningAvailability;
    }

    public void setEveningAvailability(Boolean eveningAvailability) {
        this.eveningAvailability = eveningAvailability;
    }

    public int getPendingAbhaCount() {return pendingAbhaCount;}

    public void setPendingAbhaCount(int pendingAbhaCount) { this.pendingAbhaCount = pendingAbhaCount;}

    @NonNull
    @Override
    public String toString() {
        return "FamilyDataBean{" +
                "id='" + id + '\'' +
                ", familyId='" + familyId + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", locationId='" + locationId + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", religion='" + religion + '\'' +
                ", caste='" + caste + '\'' +
                ", bplFlag=" + bplFlag +
                ", anganwadiId='" + anganwadiId + '\'' +
                ", toiletAvailableFlag=" + toiletAvailableFlag +
                ", isVerifiedFlag=" + isVerifiedFlag +
                ", members=" + members +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", assignedTo=" + assignedTo +
                ", maaVatsalyaNumber='" + maaVatsalyaNumber + '\'' +
                ", rsbyCardNumber='" + rsbyCardNumber + '\'' +
                ", comment='" + comment + '\'' +
                ", vulnerableFlag=" + vulnerableFlag +
                ", seasonalMigrantFlag=" + seasonalMigrantFlag +
                ", isReverificationFlag=" + isReverificationFlag +
                ", headMemberName='" + headMemberName + '\'' +
                ", areaId='" + areaId + '\'' +
                ", contactPersonId=" + contactPersonId +
                ", anganwadiUpdateFlag=" + anganwadiUpdateFlag +
                ", anyMemberCbacDone=" + anyMemberCbacDone +
                ", lastFhsDate=" + lastFhsDate +
                ", lastMemberNcdScreeningDate=" + lastMemberNcdScreeningDate +
                ", rationCardNumber='" + rationCardNumber + '\'' +
                ", bplCardNumber='" + bplCardNumber + '\'' +
                ", lastIdspScreeningDate=" + lastIdspScreeningDate +
                ", lastIdsp2ScreeningDate=" + lastIdsp2ScreeningDate +
                '}';
    }
}
