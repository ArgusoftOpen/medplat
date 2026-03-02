package com.argusoft.medplat.fcm.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author nihar
 * @since 02/08/22 2:55 PM
 */
@Entity
@Table(name = "techo_push_notification_master")
public class TechoPushNotificationMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "type")
    private String type;

    @Column(name = "response")
    private String response;

    @Column(name = "exception")
    private String exception;

    @Column(name = "is_sent")
    private Boolean isSent;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    @Column(name = "completed_on")
    private Date completedOn;

    @Column(name = "processed_on")
    private Date processedOn;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "message")
    private String message;

    @Column(name = "heading")
    private String heading;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public static class Fields {

        private Fields() {

        }

        public static final String TYPE = "type";
        public static final String ID = "id";
        public static final String IS_PROCESSED = "isProcessed";
        public static final String RESPONSE = "response";
        public static final String EXCEPTION = "exception";
        public static final String IS_SENT = "isSent";
        public static final String COMPLETED_ON = "completedOn";
        public static final String PROCESSED_ON = "processedOn";
    }
}
