package com.argusoft.medplat.event.dto;

/**
 * @author nihar
 * @since 15/11/22 11:52 AM
 */
public class PushNotificationConfigDto {

    private String userFieldName;
    private Integer notificationTypeId;
    private String heading;
    private String template;

    public String getUserFieldName() {
        return userFieldName;
    }

    public void setUserFieldName(String userFieldName) {
        this.userFieldName = userFieldName;
    }

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
