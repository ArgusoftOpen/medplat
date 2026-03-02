/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * <p>
 *     Define event_configuration entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "event_configuration")
public class EventConfiguration extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;
    @Column(name = "name", nullable = false, length = 500)
    private String name;
    @Column(name = "description", nullable = true, length = 1000)
    private String description;
    @Column(name = "event_type", length = 100)
    private String eventType;
    @Column(name = "event_type_detail_id")
    private Integer eventTypeDetailId;
    @Column(name = "event_type_detail_code")
    private String eventTypeDetailCode;
    @Column(name = "form_type_id")
//    @ManyToOne()
    private Integer formTypeId;
    // miniute/hourly/daily/monthly/yearly/immidetly 
    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_when", length = 50)
    private TriggerWhen triggerWhen;

    @Column(name = "day", nullable = true)
    private Short day;
    @Column(name = "hour")
    private Short hour;
    @Column(name = "minute")
    private Short minute;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    public enum State {

        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    @Column(name = "config_json")
    private String notificationConfigurationDetailJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getEventTypeDetailId() {
        return eventTypeDetailId;
    }

    public void setEventTypeDetailId(Integer eventTypeDetailId) {
        this.eventTypeDetailId = eventTypeDetailId;
    }

    public Integer getFormTypeId() {
        return formTypeId;
    }

    public void setFormTypeId(Integer formTypeId) {
        this.formTypeId = formTypeId;
    }

    public TriggerWhen getTriggerWhen() {
        return triggerWhen;
    }

    public void setTriggerWhen(TriggerWhen triggerWhen) {
        this.triggerWhen = triggerWhen;
    }

    public Short getDay() {
        return day;
    }

    public void setDay(Short day) {
        this.day = day;
    }

    public Short getHour() {
        return hour;
    }

    public void setHour(Short hour) {
        this.hour = hour;
    }

    public Short getMinute() {
        return minute;
    }

    public void setMinute(Short minute) {
        this.minute = minute;
    }

    public String getNotificationConfigurationDetailJson() {
        return notificationConfigurationDetailJson;
    }

    public void setNotificationConfigurationDetailJson(String notificationConfigurationDetailJson) {
        this.notificationConfigurationDetailJson = notificationConfigurationDetailJson;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getEventTypeDetailCode() {
        return eventTypeDetailCode;
    }

    public void setEventTypeDetailCode(String eventTypeDetailCode) {
        this.eventTypeDetailCode = eventTypeDetailCode;
    }

    public enum TriggerWhen {

        IMMEDIATELY, AFTER, DAILY, MONTHLY, HOURLY, MINUTE
    }

    /**
     * Define fields name for event_configuration entity.
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String UUID = "uuid";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String EVENT_TYPE = "eventType";
        public static final String EVENT_TYPE_DETAIL_ID = "eventTypeDetailId";
        public static final String EVENT_TYPE_DETAIL_CODE = "eventTypeDetailCode";
        public static final String FORM_TYPE_ID = "formTypeId";
        public static final String TRIGGER_WHEN = "triggerWhen";
        public static final String DAY = "day";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String NOTIFICATION_CONFIGURATION_DETAIL_JSON = "notificationConfigurationDetailJson";
    }

}
