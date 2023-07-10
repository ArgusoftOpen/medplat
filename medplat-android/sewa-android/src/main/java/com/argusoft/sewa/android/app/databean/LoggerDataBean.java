/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * @author kelvin
 */
public class LoggerDataBean {

    private String checkSum;
    private Long notificationId;
    private Long date;
    private String beneficiaryName;
    private String taskName;
    private int noOfAttempt = 0;
    private String status;
    private String formType;
    private String recordUrl;

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

    @NonNull
    @Override
    public String toString() {
        return "LoggerBean{" + "checkSum=" + checkSum + ", notificationId=" + notificationId + ", date=" + date + ", beneficiaryName=" + beneficiaryName + ", taskName=" + taskName + ", noOfAttempt=" + noOfAttempt + ", status=" + status + ", formType=" + formType + '}';
    }
}
