package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 9/21/19
 */
public class AshaReportedEventDataBean {

    private String eventType;
    private Long familyId;
    private Long memberId;
    private Long locationId;
    private Long reportedOn;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Long reportedOn) {
        this.reportedOn = reportedOn;
    }
}
