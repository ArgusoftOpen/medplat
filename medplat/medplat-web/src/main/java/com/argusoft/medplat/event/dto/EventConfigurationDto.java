/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

import com.argusoft.medplat.event.model.EventConfiguration;
import com.argusoft.medplat.event.model.EventConfiguration.State;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 *     Used for event configuration.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class EventConfigurationDto {

    private Integer id;
    private String name;
    private String description;
    private String eventType;
    private Integer eventTypeDetailId;
    private Integer formTypeId;
    private String eventTypeDetailCode;
    private EventConfiguration.TriggerWhen trigerWhen; // minute/hourly/daily/monthly/yearly/immidetly
    private Short day;
    private Short hour;
    private Short minute;
    private State state;
    private Integer createdBy;
    private Date createdOn;
    private List<EventConfigurationDetailDto> notificationConfigDetails;
    private String status ;
    private Timestamp completionTime ;
    private String configJson;
    private UUID uuid;

    
    public EventConfigurationDto() {
    }

    public EventConfigurationDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public EventConfiguration.TriggerWhen getTrigerWhen() {
        return trigerWhen;
    }

    public void setTrigerWhen(EventConfiguration.TriggerWhen trigerWhen) {
        this.trigerWhen = trigerWhen;
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

    public void setMinute(Short miniute) {
        this.minute = miniute;
    }

    public List<EventConfigurationDetailDto> getNotificationConfigDetails() {
        return notificationConfigDetails;
    }

    public void setNotificationConfigDetails(List<EventConfigurationDetailDto> notificationConfigDetails) {
        this.notificationConfigDetails = notificationConfigDetails;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getEventTypeDetailCode() {
        return eventTypeDetailCode;
    }

    public void setEventTypeDetailCode(String eventTypeDetailCode) {
        this.eventTypeDetailCode = eventTypeDetailCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Timestamp completionTime) {
        this.completionTime = completionTime;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }
    
    
    @Override
    public String toString() {
        return "NotificationConfigurationDto{" + "id=" + id + ", name=" + name + ", description=" + description + ", eventType=" + eventType + ", eventTypeDetailId=" + eventTypeDetailId + ", formTypeId=" + formTypeId + ", trigerWhen=" + trigerWhen + ", day=" + day + ", hour=" + hour + ", minute=" + minute + ", notificationConfigDetails=" + notificationConfigDetails + '}';
    }
}
