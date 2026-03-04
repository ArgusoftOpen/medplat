package com.argusoft.medplat.fcm.dto;

import com.argusoft.medplat.common.dto.LocationDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationConfig;
import com.argusoft.medplat.web.users.dto.RoleHierarchyDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author nihar
 * @since 13/10/22 5:21 PM
 */
public class TechoPushNotificationConfigDto {

    private Integer id;
    private String name;
    private String description;
    private TechoPushNotificationConfig.ConfigType configType;
    private Integer notificationTypeId;
    private TechoPushNotificationConfig.TRIGGER_TYPE triggerType;
    private TechoPushNotificationConfig.Status status;
    private Date dateTime;
    private List<LocationDto> locations;
    private List<RoleHierarchyDto> roles;
    private TechoPushNotificationConfig.State state;
    private Integer createdBy;
    private Date createdOn;
    private UUID queryUUID;

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

    public TechoPushNotificationConfig.ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(TechoPushNotificationConfig.ConfigType configType) {
        this.configType = configType;
    }

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public TechoPushNotificationConfig.TRIGGER_TYPE getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TechoPushNotificationConfig.TRIGGER_TYPE triggerType) {
        this.triggerType = triggerType;
    }

    public TechoPushNotificationConfig.Status getStatus() {
        return status;
    }

    public void setStatus(TechoPushNotificationConfig.Status status) {
        this.status = status;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }

    public List<RoleHierarchyDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleHierarchyDto> roles) {
        this.roles = roles;
    }

    public TechoPushNotificationConfig.State getState() {
        return state;
    }

    public void setState(TechoPushNotificationConfig.State state) {
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

    public UUID getQueryUUID() {
        return queryUUID;
    }

    public void setQueryUUID(UUID queryUUID) {
        this.queryUUID = queryUUID;
    }
}
