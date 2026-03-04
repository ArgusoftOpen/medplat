package com.argusoft.medplat.rch.dto;

/**
 * <p>
 * Used for asha reported event.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class AshaReportedEventDataBean {

    private String eventType;
    private Integer familyId;
    private Integer memberId;
    private Integer locationId;
    private Long reportedOn;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Long getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Long reportedOn) {
        this.reportedOn = reportedOn;
    }

}
