package com.argusoft.medplat.fcm.dto;

import java.util.Date;

/**
 * @author nihar
 * @since 02/08/22 4:10 PM
 */
public class TechoPushNotificationDto {

    private Long id;
    private Integer userId;
    private String type;
    private String message;
    private String description;
    private String heading;
    private String token;
    private Integer mediaId;
    private Date createdOn;
    private Integer createdBy;
    private String eventId;
    private String messageEvent;
    private String headingEvent;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(String messageEvent) {
        this.messageEvent = messageEvent;
    }

    public String getHeadingEvent() {
        return headingEvent;
    }

    public void setHeadingEvent(String headingEvent) {
        this.headingEvent = headingEvent;
    }

    @Override
    public String toString() {
        return "TechoPushNotificationDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                ", heading='" + heading + '\'' +
                ", token='" + token + '\'' +
                ", mediaId=" + mediaId +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", eventId='" + eventId + '\'' +
                ", messageEvent='" + messageEvent + '\'' +
                ", headingEvent='" + headingEvent + '\'' +
                '}';
    }
}
