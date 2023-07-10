package com.argusoft.medplat.sms_queue.dto;

import com.argusoft.medplat.sms_queue.model.SmsQueue;

import java.util.Date;

/**
 * <p>
 *     Defines fields for sms queue
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
public class SmsQueueDto {

    private Integer id;
    private String mobileNumber;
    private String message;
    private String messageType;
    private Boolean isProcessed;
    private Boolean isSent;
    private Date processedOn;
    private Date createdOn;
    private Date completedOn;
    private SmsQueue.STATUS status;
    private String exceptionString;
    private Integer smsId;
    private Boolean isBlocked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public SmsQueue.STATUS getStatus() {
        return status;
    }

    public void setStatus(SmsQueue.STATUS status) {
        this.status = SmsQueue.STATUS.NEW;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public String getExceptionString() {
        return exceptionString;
    }

    public void setExceptionString(String exceptionString) {
        this.exceptionString = exceptionString;
    }
    
    public Integer getSmsId() {
        return smsId;
    }

    public void setSmsId(Integer smsId) {
        this.smsId = smsId;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
