package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 9/24/19
 */
public class AshaEventRejectionDataBean {

    private Long notificationId;
    private Long rejectedOn;
    private String eventType;
    private Long memberId;
    private Long familyId;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getRejectedOn() {
        return rejectedOn;
    }

    public void setRejectedOn(Long rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
}
