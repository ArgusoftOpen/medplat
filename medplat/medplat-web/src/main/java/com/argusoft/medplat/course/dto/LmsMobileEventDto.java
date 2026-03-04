package com.argusoft.medplat.course.dto;

import java.util.Date;

public class LmsMobileEventDto {

    private String checksum;

    private Integer userId;

    private Date mobileDate;

    private String eventType;

    private String eventData;

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
}
