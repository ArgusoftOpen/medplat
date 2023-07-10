/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author alpesh
 */
public class NotificationMobDataBean implements Serializable {

    private Long id;
    private Long ashaId;
    private String ashaName;
    private String beneficiaryName;
    private Long beneficiaryId;
    private int noOfNotificationType;
    private String task;
    private Long expectedDueDate;
    private String expectedDueDateString;
    private Boolean overdueFlag;
    private Boolean isChild;
    private String customField;
    private Long overDueDate;
    private String diagnosisDate;
    private String positiveFindings;
    private Long expiryDate;
    private Long locationId;
    private Long memberId;
    private Long familyId;
    private String otherDetails;
    private Long migrationId;
    private Long modifiedOn;
    private String state;
    private String header;
    private Long scheduledDate;

    public String getPositiveFindings() {
        return positiveFindings;
    }

    public void setPositiveFindings(String positiveFindings) {
        this.positiveFindings = positiveFindings;
    }

    public String getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public Long getOverDueDate() {
        return overDueDate;
    }

    public void setOverDueDate(Long overDueDate) {
        this.overDueDate = overDueDate;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNoOfNotificationType() {
        return noOfNotificationType;
    }

    public void setNoOfNotificationType(int noOfNotificationType) {
        this.noOfNotificationType = noOfNotificationType;
    }

    public Boolean getIsChild() {
        return isChild;
    }

    public void setIsChild(Boolean isChild) {
        this.isChild = isChild;
    }

    public String getExpectedDueDateString() {
        return expectedDueDateString;
    }

    public void setExpectedDueDateString(String expectedDueDateString) {
        this.expectedDueDateString = expectedDueDateString;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Long getAshaId() {
        return ashaId;
    }

    public void setAshaId(Long ashaId) {
        this.ashaId = ashaId;
    }

    public String getAshaName() {
        return ashaName;
    }

    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Long getExpectedDueDate() {
        return expectedDueDate;
    }

    public void setExpectedDueDate(Long expectedDueDate) {
        this.expectedDueDate = expectedDueDate;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Long migrationId) {
        this.migrationId = migrationId;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Long scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationMobDataBean{" +
                "id=" + id +
                ", ashaId=" + ashaId +
                ", ashaName='" + ashaName + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", beneficiaryId=" + beneficiaryId +
                ", noOfNotificationType=" + noOfNotificationType +
                ", task='" + task + '\'' +
                ", expectedDueDate=" + expectedDueDate +
                ", expectedDueDateString='" + expectedDueDateString + '\'' +
                ", overdueFlag=" + overdueFlag +
                ", isChild=" + isChild +
                ", customField='" + customField + '\'' +
                ", overDueDate=" + overDueDate +
                ", diagnosisDate='" + diagnosisDate + '\'' +
                ", positiveFindings='" + positiveFindings + '\'' +
                ", expiryDate=" + expiryDate +
                ", locationId=" + locationId +
                ", memberId=" + memberId +
                ", familyId=" + familyId +
                ", otherDetails='" + otherDetails + '\'' +
                ", migrationId=" + migrationId +
                '}';
    }
}
