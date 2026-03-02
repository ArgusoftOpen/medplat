/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define timer_event entity and its fields.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "timer_event")
public class TimerEvent implements Serializable {

    private static final int serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "event_config_id")
    private Integer eventConfigId;

    @Column(name = "processed")
    private boolean processed;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private STATUS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TYPE type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "system_trigger_on")
    private Date systemTriggerOn;

    @Column(name = "processed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processedOn;

    @Column(name = "completed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;

    @Column(name = "notification_config_id")
    private String notificationConfigId;

    @Column(name = "json_data")
    private String jsonData;

    @Column(name = "exception_string")
    private String exceptionString;

    public static int getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getEventConfigId() {
        return eventConfigId;
    }

    public void setEventConfigId(Integer eventConfigId) {
        this.eventConfigId = eventConfigId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Date getSystemTriggerOn() {
        return systemTriggerOn;
    }

    public void setSystemTriggerOn(Date systemTriggerOn) {
        this.systemTriggerOn = systemTriggerOn;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public String getNotificationConfigId() {
        return notificationConfigId;
    }

    public void setNotificationConfigId(String notificationConfigId) {
        this.notificationConfigId = notificationConfigId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getExceptionString() {
        return exceptionString;
    }

    public void setExceptionString(String exceptionString) {
        this.exceptionString = exceptionString;
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
        if (!(object instanceof TimerEvent)) {
            return false;
        }
        TimerEvent other = (TimerEvent) object;
        return !((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Define fields name for timer_event entity.
     */
    public class TimerEventFields {
        private TimerEventFields(){}

        public static final String ID = "id";
        public static final String REF_ID = "refId";
        public static final String IS_PROCESSED = "processed";
        public static final String STATUS = "status";
        public static final String TYPE = "type";
        public static final String EXCEPTION_STRING = "exceptionString";
        public static final String COMPLETED_ON = "completedOn";
        public static final String SYSTEM_TRIGGER_ON = "systemTriggerOn";
        public static final String PROCESSED_ON = "processedOn";
        public static final String EVENT_CONFIG_ID = "eventConfigId";
    }

    /**
     * Define type of event.
     */
    public enum TYPE {
        EMAIL,
        MOBILE_NOTIFICATION,
        SMS,
        TIMER_EVENT,
        QUERY,
        UNLOCK_USER,
        STAFF_SMS,
        SYSTEM_FUNCTION,
        PUSH_NOTIFICATION,
        CONFIG_PUSH_NOTIFY,
        ANNOUNCEMENT_PUSH_NOTIFICATION
    }

    /**
     * Define status for event.
     */
    public enum STATUS {

        NEW,
        PROCESSED,
        COMPLETED,
        EXCEPTION
    }
}
