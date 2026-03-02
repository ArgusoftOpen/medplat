/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 *
 * @author kunjan
 */
public class TechoNotificationDataBean {

    private Integer id;
    private Integer ashaId;
    private String ashaName;
    private String beneficiaryName;
    private Integer beneficiaryId;
    private Integer noOfNotificationType;
    private String task;
    private Long expectedDueDate;
    private String expectedDueDateString;
    private Integer overDueDate;
    private Boolean overdueFlag;
    private String customField;
    private String diagnosisDate;
    private String positiveFindings;
    private Long expiryDate;
    private Integer locationId;
    private Integer familyId;
    private Integer memberId;
    private String otherDetails;
    private Integer migrationId;
    private Long modifiedOn;
    private String state;
    private String header;
    private Long scheduledDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAshaId() {
        return ashaId;
    }

    public void setAshaId(Integer ashaId) {
        this.ashaId = ashaId;
    }

    public String getAshaName() {
        return ashaName;
    }

    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Integer getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Integer beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Integer getNoOfNotificationType() {
        return noOfNotificationType;
    }

    public void setNoOfNotificationType(Integer noOfNotificationType) {
        this.noOfNotificationType = noOfNotificationType;
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

    public void setExpectedDueDate(Date expectedDueDate) {
        if (expectedDueDate != null) {
            this.expectedDueDate = expectedDueDate.getTime();
        } else {
            this.expectedDueDate = null;
        }
    }

    public String getExpectedDueDateString() {
        return expectedDueDateString;
    }

    public void setExpectedDueDateString(String expectedDueDateString) {
        this.expectedDueDateString = expectedDueDateString;
    }

    public Integer getOverDueDate() {
        return overDueDate;
    }

    public void setOverDueDate(Integer overDueDate) {
        this.overDueDate = overDueDate;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public String getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public String getPositiveFindings() {
        return positiveFindings;
    }

    public void setPositiveFindings(String positiveFindings) {
        this.positiveFindings = positiveFindings;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        if (expiryDate != null) {
            this.expiryDate = expiryDate.getTime();
        } else {
            this.expiryDate = null;
        }
    }

    public Integer getLocationId() {
        return locationId;
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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Integer getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Integer migrationId) {
        this.migrationId = migrationId;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        if (modifiedOn != null) {
            this.modifiedOn = modifiedOn.getTime();
        } else {
            this.modifiedOn = null;
        }
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

    public void setScheduledDate(Date scheduledDate) {
        if (scheduledDate != null) {
            this.scheduledDate = scheduledDate.getTime();
        } else {
            this.scheduledDate = null;
        }
    }
    
}
