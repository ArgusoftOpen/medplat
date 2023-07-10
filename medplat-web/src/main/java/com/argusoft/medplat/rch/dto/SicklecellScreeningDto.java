package com.argusoft.medplat.rch.dto;

/**
 * <p>
 * Used for sickle cell screening.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 11:00 AM
 */
public class SicklecellScreeningDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Boolean anemiaTestDone;
    private String dttTestResult;
    private Boolean hplcTestDone;
    private String hplcTestResult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getAnemiaTestDone() {
        return anemiaTestDone;
    }

    public void setAnemiaTestDone(Boolean anemiaTestDone) {
        this.anemiaTestDone = anemiaTestDone;
    }

    public String getDttTestResult() {
        return dttTestResult;
    }

    public void setDttTestResult(String dttTestResult) {
        this.dttTestResult = dttTestResult;
    }

    public Boolean getHplcTestDone() {
        return hplcTestDone;
    }

    public void setHplcTestDone(Boolean hplcTestDone) {
        this.hplcTestDone = hplcTestDone;
    }

    public String getHplcTestResult() {
        return hplcTestResult;
    }

    public void setHplcTestResult(String hplcTestResult) {
        this.hplcTestResult = hplcTestResult;
    }

    @Override
    public String toString() {
        return "SicklecellScreeningDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", anemiaTestDone=" + anemiaTestDone +
                ", dttTestResult='" + dttTestResult + '\'' +
                ", hplcTestDone=" + hplcTestDone +
                ", hplcTestResult='" + hplcTestResult + '\'' +
                '}';
    }
}
