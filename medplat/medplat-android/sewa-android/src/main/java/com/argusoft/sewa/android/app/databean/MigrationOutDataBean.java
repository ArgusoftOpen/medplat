package com.argusoft.sewa.android.app.databean;

import java.util.List;

/**
 * Created by prateek on 15/3/19.
 */
public class MigrationOutDataBean {

    private Long memberId;
    private Long fromFamilyId;
    private Long fromLocationId;
    private Boolean outOfState;
    private Boolean locationknown;
    private Long toLocationId;
    private List<Long> childrensUnder5;
    private String otherInfo;
    private Long reportedOn;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFromFamilyId() {
        return fromFamilyId;
    }

    public void setFromFamilyId(Long fromFamilyId) {
        this.fromFamilyId = fromFamilyId;
    }

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public Boolean getLocationknown() {
        return locationknown;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public void setLocationknown(Boolean locationknown) {
        this.locationknown = locationknown;
    }

    public Long getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(Long toLocationId) {
        this.toLocationId = toLocationId;
    }

    public List<Long> getChildrensUnder5() {
        return childrensUnder5;
    }

    public void setChildrensUnder5(List<Long> childrensUnder5) {
        this.childrensUnder5 = childrensUnder5;
    }

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Long getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Long reportedOn) {
        this.reportedOn = reportedOn;
    }
}
