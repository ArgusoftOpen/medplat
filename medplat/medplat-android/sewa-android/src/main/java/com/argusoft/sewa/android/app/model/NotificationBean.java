/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author kelvin
 */
@DatabaseTable
public class NotificationBean extends BaseEntity {

    @DatabaseField
    private Long notificationId;
    @DatabaseField
    private Date dateOfNotification;
    @DatabaseField
    private String notificationCode;
    @DatabaseField
    private Long beneficiaryId;
    @DatabaseField
    private String beneficiaryName;
    @DatabaseField
    private String expectedDueDateString;
    @DatabaseField
    private Boolean overdueFlag;
    @DatabaseField
    private int noOfNotificationType;
    @DatabaseField
    private boolean skipNotification;
    @DatabaseField
    private String notificationOf;
    @DatabaseField
    private String customField;
    @DatabaseField
    private Long overDueDate;
    @DatabaseField
    private String diagnosisDate;
    @DatabaseField
    private String ashaName;
    @DatabaseField
    private Long ashaId;
    @DatabaseField
    private Date expectedDueDate;
    @DatabaseField
    private String positiveFindings;
    @DatabaseField
    private Date expiryDate;
    @DatabaseField
    private Long locationId;
    @DatabaseField
    private Long memberId;
    @DatabaseField
    private Long familyId;
    @DatabaseField
    private String otherDetails;
    @DatabaseField
    private Long migrationId;
    @DatabaseField
    private Date modifiedOn;
    @DatabaseField
    private String state;
    @DatabaseField
    private String header;
    @DatabaseField
    private Date scheduledDate;

    public String getPositiveFindings() {
        return positiveFindings;
    }

    public void setPositiveFindings(String positiveFindings) {
        this.positiveFindings = positiveFindings;
    }

    public Date getExpectedDueDate() {
        return expectedDueDate;
    }

    public void setExpectedDueDate(Date expectedDueDate) {
        this.expectedDueDate = expectedDueDate;
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

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationCode() {
        return notificationCode;
    }

    public void setNotificationCode(String notificationCode) {
        this.notificationCode = notificationCode;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Date getDateOfNotification() {
        return dateOfNotification;
    }

    public void setDateOfNotification(Date dateOfNotification) {
        this.dateOfNotification = dateOfNotification;
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

    public int getNoOfNotificationType() {
        return noOfNotificationType;
    }

    public void setNoOfNotificationType(int noOfNotificationType) {
        this.noOfNotificationType = noOfNotificationType;
    }

    public boolean isSkipNotification() {
        return skipNotification;
    }

    public void setSkipNotification(boolean skipNotification) {
        this.skipNotification = skipNotification;
    }

    public String getNotificationOf() {
        return notificationOf;
    }

    public void setNotificationOf(String notificationOf) {
        this.notificationOf = notificationOf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
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

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationBean{" + "notificationId=" + notificationId + ", dateOfNotification=" + dateOfNotification + ", notificationCode=" + notificationCode + ", beneficiaryId=" + beneficiaryId + ", beneficiaryName=" + beneficiaryName + ", expectedDueDateString=" + expectedDueDateString + ", overdueFlag=" + overdueFlag + ", noOfNotificationType=" + noOfNotificationType + ", skipNotification=" + skipNotification + ", notificationOf=" + notificationOf + '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
