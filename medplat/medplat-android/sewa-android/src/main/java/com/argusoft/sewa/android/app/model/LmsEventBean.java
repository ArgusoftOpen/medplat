package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class LmsEventBean extends BaseEntity {

    @DatabaseField
    private String checksum;

    @DatabaseField
    private String recordUrl;

    @DatabaseField
    private String token;

    @DatabaseField
    private Integer userId;

    @DatabaseField
    private Date mobileDate;

    @DatabaseField
    private String eventType;

    @DatabaseField
    private String eventData;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @Override
    public String toString() {
        return "LmsEventBean{" +
                "checksum='" + checksum + '\'' +
                ", recordUrl='" + recordUrl + '\'' +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", mobileDate=" + mobileDate +
                ", eventType='" + eventType + '\'' +
                ", eventData='" + eventData + '\'' +
                '}';
    }
}
