/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kelvin
 */
@Entity
@Table(name = "system_sync_status")
@NamedQueries({
        @NamedQuery(name = "SyncStatus.findAll", query = "SELECT s FROM SyncStatus s")})
public class SyncStatus implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "mobile_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileDate;
    @Basic(optional = false)
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "relative_id")
    private Integer relativeId;
    @Column(name = "record_string", length = 10000)
    private String recordString;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "device")
    private String device;
    //added for still birth verification    
    @Column(name = "lastmodified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Column(name = "lastmodified_by")
    private Integer lastModifiedBy;
    @Column(name = "client_id")
    private Integer clientId;
    @Column(name = "duration_of_processing")
    private Integer durationOfProcessing;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "exception")
    private String exception;
    @Column(name = "mail_sent")
    private Boolean mailSent;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public SyncStatus() {
    }

    public SyncStatus(String id) {
        this.id = id;
    }

    public SyncStatus(String id, Date mobileDate, Date actionDate, String status, Integer relativeId, String recordString) {
        this.id = id;
        this.mobileDate = mobileDate;
        this.actionDate = actionDate;
        this.status = status;
        this.relativeId = relativeId;
        this.recordString = recordString;
    }

    public SyncStatus(String id, Date mobileDate, Date actionDate, String status, String recordString, Integer userId, String device) {
        this.id = id;
        this.mobileDate = mobileDate;
        this.actionDate = actionDate;
        this.status = status;
        this.recordString = recordString;
        this.userId = userId;
        this.device = device;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Date getMobileDate() {
        return mobileDate;
    }

    public void setMobileDate(Date mobileDate) {
        this.mobileDate = mobileDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(Integer relativeId) {
        this.relativeId = relativeId;
    }

    public String getRecordString() {
        return recordString;
    }

    public void setRecordString(String recordString) {
        this.recordString = recordString;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getDurationOfProcessing() {
        return durationOfProcessing;
    }

    public void setDurationOfProcessing(Integer durationOfProcessing) {
        this.durationOfProcessing = durationOfProcessing;
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
        return mailSent != null && mailSent;
    }

    public void setMailSent(Boolean mailSent) {
        this.mailSent = mailSent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SyncStatus)) {
            return false;
        }
        SyncStatus other = (SyncStatus) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "SyncStatus{" + "id=" + id + ", mobileDate=" + mobileDate + ", actionDate=" + actionDate + ", status=" + status + ", relativeId=" + relativeId + ", recordString=" + recordString + ", userId=" + userId + ", device=" + device + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy + ", clientId=" + clientId + '}';
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String ID = "id";
        public static final String MOBILE_DATE = "mobileDate";
        public static final String ACTION_DATE = "actionDate";
        public static final String STATUS = "status";
        public static final String RELATIVE_ID = "relativeId";
        public static final String RECORD_STRING = "recordString";
        public static final String USER_ID = "userId";
        public static final String DEVICE = "device";
        public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
        public static final String LAST_MODIFIED_BY = "lastModifiedBy";
        public static final String CLIENT_ID = "clientId";
        public static final String DURATION_OF_PROCESSING = "durationOfProcessing";
        public static final String ERROR_MESSAGE = "errorMessage";
        public static final String EXCEPTION = "exception";
    }
}
