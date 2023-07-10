package com.argusoft.medplat.fcm.dto;

import com.argusoft.medplat.fcm.model.TechoPushNotificationConfig;

/**
 * @author nihar
 * @since 17/10/22 1:08 PM
 */
public class TechoPushNotificationDisplayDto {

    private Integer id;
    private String name;
    private String description;
    private TechoPushNotificationConfig.ConfigType configType;
    private TechoPushNotificationConfig.TRIGGER_TYPE triggerType;
    private TechoPushNotificationConfig.Status status;
    private TechoPushNotificationConfig.State state;
    private String notificationType;

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

    public TechoPushNotificationConfig.State getState() {
        return state;
    }

    public void setState(TechoPushNotificationConfig.State state) {
        this.state = state;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
