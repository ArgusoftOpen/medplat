package com.argusoft.sewa.android.app.databean;

import java.util.List;

/**
 * Created by prateek on 8/14/19
 */
public class FamilyMigrationOutDataBean {

    private Long familyId;
    private Boolean isSplit;
    private List<Long> memberIds;
    private Long newHead;
    private Boolean isCurrentLocation;
    private Boolean outOfState;
    private Boolean locationKnown;
    private Long fromLocationId;
    private Long toLocationId;
    private String otherInfo;
    private Long reportedOn;

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Boolean getSplit() {
        return isSplit;
    }

    public void setSplit(Boolean split) {
        isSplit = split;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public Boolean getCurrentLocation() {
        return isCurrentLocation;
    }

    public Long getNewHead() {
        return newHead;
    }

    public void setNewHead(Long newHead) {
        this.newHead = newHead;
    }

    public void setCurrentLocation(Boolean currentLocation) {
        isCurrentLocation = currentLocation;
    }

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Boolean getLocationKnown() {
        return locationKnown;
    }

    public void setLocationKnown(Boolean locationKnown) {
        this.locationKnown = locationKnown;
    }

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public Long getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(Long toLocationId) {
        this.toLocationId = toLocationId;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Long getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Long reportedOn) {
        this.reportedOn = reportedOn;
    }
}
