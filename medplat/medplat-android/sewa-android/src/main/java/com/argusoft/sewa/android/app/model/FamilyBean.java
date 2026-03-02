/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kunjan
 */
@DatabaseTable
public class FamilyBean extends BaseEntity implements Serializable {

    @DatabaseField
    private String actualId;

    @DatabaseField(index = true)
    private String familyId;

    @DatabaseField
    private String houseNumber;

    @DatabaseField(indexName = "family_bean_state_location_id_idx")
    private String locationId;

    @DatabaseField
    private String address1;

    @DatabaseField
    private String address2;

    @DatabaseField
    private String religion;

    @DatabaseField
    private String caste;

    @DatabaseField
    private Boolean bplFlag;

    @DatabaseField
    private String anganwadiId;

    @DatabaseField
    private Boolean toiletAvailableFlag;

    @DatabaseField
    private Boolean isVerifiedFlag;

    @DatabaseField
    private String type;

    @DatabaseField(indexName = "family_bean_state_location_id_idx")
    private String state;

    @DatabaseField
    private String createdBy;

    @DatabaseField
    private String updatedBy;

    @DatabaseField
    private Date createdOn;

    @DatabaseField
    private Date updatedOn;

    @DatabaseField
    private Long assignedTo;

    @DatabaseField
    private String maaVatsalyaNumber;

    @DatabaseField
    private String rsbyCardNumber;

    @DatabaseField
    private String comment;

    @DatabaseField
    private Boolean vulnerableFlag;

    @DatabaseField
    private Boolean seasonalMigrantFlag;

    @DatabaseField
    private Boolean isReverificationFlag;

    @DatabaseField(index = true)
    private String areaId;

    @DatabaseField
    private Long contactPersonId;

    @DatabaseField
    private Boolean anganwadiUpdateFlag;

    @DatabaseField
    private Boolean anyMemberCbacDone;

    @DatabaseField
    private Long lastFhsDate;

    @DatabaseField
    private Long lastMemberNcdScreeningDate;

    @DatabaseField
    private String rationCardNumber;

    @DatabaseField
    private String bplCardNumber;

    @DatabaseField
    private Long lastIdspScreeningDate;

    @DatabaseField
    private Long lastIdsp2ScreeningDate;

    @DatabaseField
    private Boolean isProvidingConsent;

    @DatabaseField
    private String vulnerabilityCriteria;

    @DatabaseField
    private Boolean eligibleForPmjay;

    @DatabaseField
    private String typeOfHouse;

    @DatabaseField
    private String drinkingWaterSource;

    @DatabaseField
    private String otherTypeOfHouse;

    @DatabaseField
    private String typeOfToilet;

    @DatabaseField
    private String fuelForCooking;

    @DatabaseField
    private String electricityAvailability;

    @DatabaseField
    private String vehicleDetails;

    @DatabaseField
    private String houseOwnershipStatus;

    @DatabaseField
    private String annualIncome;

    @DatabaseField
    private int pendingAbhaCount;

    public FamilyBean() {
    }

    public FamilyBean(FamilyDataBean familyDataBean) {
        this.actualId = familyDataBean.getId();
        this.familyId = familyDataBean.getFamilyId();
        this.houseNumber = familyDataBean.getHouseNumber();
        this.locationId = familyDataBean.getLocationId();
        this.address1 = familyDataBean.getAddress1();
        this.address2 = familyDataBean.getAddress2();
        this.religion = familyDataBean.getReligion();
        this.caste = familyDataBean.getCaste();
        this.bplFlag = familyDataBean.getBplFlag();
        this.anganwadiId = familyDataBean.getAnganwadiId();
        this.toiletAvailableFlag = familyDataBean.getToiletAvailableFlag();
        this.isVerifiedFlag = familyDataBean.getIsVerifiedFlag();
        this.type = familyDataBean.getType();
        this.state = familyDataBean.getState();
        this.createdBy = familyDataBean.getCreatedBy();
        this.updatedBy = familyDataBean.getUpdatedBy();
        this.createdOn = familyDataBean.getCreatedOn();
        this.updatedOn = familyDataBean.getUpdatedOn();
        this.assignedTo = familyDataBean.getAssignedTo();
        this.maaVatsalyaNumber = familyDataBean.getMaaVatsalyaNumber();
        this.rsbyCardNumber = familyDataBean.getRsbyCardNumber();
        this.comment = familyDataBean.getComment();
        this.vulnerableFlag = familyDataBean.getVulnerableFlag();
        this.seasonalMigrantFlag = familyDataBean.getSeasonalMigrantFlag();
        this.isReverificationFlag = familyDataBean.getReverificationFlag();
        this.areaId = familyDataBean.getAreaId();
        this.contactPersonId = familyDataBean.getContactPersonId();
        this.anganwadiUpdateFlag = familyDataBean.getAnganwadiUpdateFlag();
        this.anyMemberCbacDone = familyDataBean.getAnyMemberCbacDone();
        this.lastFhsDate = familyDataBean.getLastFhsDate();
        this.lastMemberNcdScreeningDate = familyDataBean.getLastMemberNcdScreeningDate();
        this.rationCardNumber = familyDataBean.getRationCardNumber();
        this.bplCardNumber = familyDataBean.getBplCardNumber();
        this.lastIdspScreeningDate = familyDataBean.getLastIdspScreeningDate();
        this.lastIdsp2ScreeningDate = familyDataBean.getLastIdsp2ScreeningDate();
        this.isProvidingConsent = familyDataBean.getProvidingConsent();
        this.vulnerabilityCriteria = familyDataBean.getVulnerabilityCriteria();
        this.eligibleForPmjay = familyDataBean.getEligibleForPmjay();
        this.typeOfHouse = familyDataBean.getTypeOfHouse();
        this.drinkingWaterSource = familyDataBean.getDrinkingWaterSource();
        this.vehicleDetails = familyDataBean.getVehicleDetails();
        this.otherTypeOfHouse = familyDataBean.getOtherTypeOfHouse();
        this.typeOfToilet = familyDataBean.getTypeOfToilet();
        this.fuelForCooking = familyDataBean.getFuelForCooking();
        this.electricityAvailability = familyDataBean.getElectricityAvailability();
        this.annualIncome = familyDataBean.getAnnualIncome();
        this.electricityAvailability = familyDataBean.getElectricityAvailability();
        this.pendingAbhaCount = familyDataBean.getPendingAbhaCount();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
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
        return this.lastMemberNcdScreeningDate;
    }

    public void setLastMemberNcdScreeningDate(Long lastMemberNCDScreeningDate) {
        this.lastMemberNcdScreeningDate = lastMemberNCDScreeningDate;
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

    public int getPendingAbhaCount() {return pendingAbhaCount;}

    public void setPendingAbhaCount(int pendingAbhaCount) { this.pendingAbhaCount = pendingAbhaCount;}

    @NonNull
    @Override
    public String toString() {
        return "FamilyBean{" + "actualId=" + actualId + ", familyId=" + familyId + ", houseNumber=" + houseNumber + ", locationId=" + locationId + ", address1=" + address1 + ", address2=" + address2 + ", religion=" + religion + ", caste=" + caste + ", bplFlag=" + bplFlag + ", anganwadiId=" + anganwadiId + ", toiletAvailableFlag=" + toiletAvailableFlag + ", isVerifiedFlag=" + isVerifiedFlag + ", type=" + type + ", state=" + state + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", assignedTo=" + assignedTo + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof FamilyDataBean) {
            FamilyDataBean that = (FamilyDataBean) o;
            return this.getFamilyId().equals(that.getFamilyId());
        }

        if (o instanceof FamilyBean) {
            FamilyBean that = (FamilyBean) o;
            return this.getFamilyId().equals(that.getFamilyId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return familyId.hashCode();
    }
}
