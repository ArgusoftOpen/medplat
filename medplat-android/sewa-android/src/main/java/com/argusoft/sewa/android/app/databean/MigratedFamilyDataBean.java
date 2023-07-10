package com.argusoft.sewa.android.app.databean;

public class MigratedFamilyDataBean {
    private Integer migrationId;
    private Integer familyId;
    private String familyIdString;
    private String locationMigratedFrom;
    private String locationMigratedTo;
    private Integer fromLocationId;
    private Integer toLocationId;
    private Long modifiedOn;
    private Boolean isConfirmed;
    private Boolean outOfState;
    private Boolean isLfu;
    private Boolean isSplitFamily;
    private Integer sFamilyId;
    private String sFamilyIdString;
    private String splitMembersDetail;
    private Boolean isOut;

    public Integer getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Integer migrationId) {
        this.migrationId = migrationId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getFamilyIdString() {
        return familyIdString;
    }

    public void setFamilyIdString(String familyIdString) {
        this.familyIdString = familyIdString;
    }

    public String getLocationMigratedFrom() {
        return locationMigratedFrom;
    }

    public void setLocationMigratedFrom(String locationMigratedFrom) {
        this.locationMigratedFrom = locationMigratedFrom;
    }

    public String getLocationMigratedTo() {
        return locationMigratedTo;
    }

    public void setLocationMigratedTo(String locationMigratedTo) {
        this.locationMigratedTo = locationMigratedTo;
    }

    public Integer getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Integer fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public Integer getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(Integer toLocationId) {
        this.toLocationId = toLocationId;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Boolean getIsLfu() {
        return isLfu;
    }

    public void setIsLfu(Boolean lfu) {
        isLfu = lfu;
    }

    public Boolean getIsSplitFamily() {
        return isSplitFamily;
    }

    public void setIsSplitFamily(Boolean splitFamily) {
        isSplitFamily = splitFamily;
    }

    public Integer getsFamilyId() {
        return sFamilyId;
    }

    public void setsFamilyId(Integer sFamilyId) {
        this.sFamilyId = sFamilyId;
    }

    public String getsFamilyIdString() {
        return sFamilyIdString;
    }

    public void setsFamilyIdString(String sFamilyIdString) {
        this.sFamilyIdString = sFamilyIdString;
    }

    public String getSplitMembersDetail() {
        return splitMembersDetail;
    }

    public void setSplitMembersDetail(String splitMembersDetail) {
        this.splitMembersDetail = splitMembersDetail;
    }

    public Boolean getOut() {
        return isOut;
    }

    public void setOut(Boolean out) {
        isOut = out;
    }
}
