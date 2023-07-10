package com.argusoft.medplat.course.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tr_mobile_event_master")
public class LmsMobileEventMaster {

    @Id
    @Basic(optional = false)
    @Column(name = "checksum")
    private String checksum;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "mobile_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileDate;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_data")
    private String eventData;

    @Basic(optional = false)
    @Column(name = "status")
    private String status;

    @Basic(optional = false)
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "exception")
    private String exception;

    @Column(name = "mail_sent")
    private Boolean mailSent;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getMobileDate() {
        return mobileDate;
    }

    public void setMobileDate(Date mobileDate) {
        this.mobileDate = mobileDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Boolean getMailSent() {
        return mailSent;
    }

    public void setMailSent(Boolean mailSent) {
        this.mailSent = mailSent;
    }
}
