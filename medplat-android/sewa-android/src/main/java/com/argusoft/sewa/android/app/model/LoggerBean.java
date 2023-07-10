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
public class LoggerBean extends BaseEntity {

    @DatabaseField
    private String checkSum;
    @DatabaseField
    private Long notificationId;
    @DatabaseField
    private Long date;
    @DatabaseField
    private String beneficiaryName;
    @DatabaseField
    private String taskName;
    @DatabaseField
    private int noOfAttempt = 0;
    @DatabaseField
    private String status;
    @DatabaseField
    private String formType;
    @DatabaseField
    private String recordUrl;
    @DatabaseField
    private String message;
    @DatabaseField
    private Date modifiedOn;

    public LoggerBean() {
    }

    public LoggerBean(String checkSum, Long date, String beneficiaryName, String taskName) {
        this.checkSum = checkSum;
        this.date = date;
        this.beneficiaryName = beneficiaryName;
        this.taskName = taskName;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getNoOfAttempt() {
        return noOfAttempt;
    }

    public void setNoOfAttempt(int noOfAttempt) {
        this.noOfAttempt = noOfAttempt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoggerBean{" + "checkSum=" + checkSum + ", notificationId=" + notificationId + ", date=" + date + ", beneficiaryName=" + beneficiaryName + ", taskName=" + taskName + ", noOfAttempt=" + noOfAttempt + ", status=" + status + ", formType=" + formType + "}\n";
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
