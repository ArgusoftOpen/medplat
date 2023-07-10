package com.argusoft.medplat.rch.dto;

/**
 * <p>
 * Used for asha event rejection.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class AshaEventRejectionDataBean {

    private Integer notificationId;
    private Long rejectedOn;
    private String eventType;
    private Integer memberId;
    private Integer familyId;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }
}
