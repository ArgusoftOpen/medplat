package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class MigratedFamilyBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer migrationId;
    @DatabaseField
    private Integer familyId;
    @DatabaseField
    private String familyIdString;
    @DatabaseField
    private String locationMigratedFrom;
    @DatabaseField
    private String locationMigratedTo;
    @DatabaseField
    private Integer fromLocationId;
    @DatabaseField
    private Integer toLocationId;
    @DatabaseField
    private Date modifiedOn;
    @DatabaseField
    private Boolean isConfirmed;
    @DatabaseField
    private Boolean outOfState;
    @DatabaseField
    private Boolean isLfu;
    @DatabaseField
    private Boolean isSplitFamily;
    @DatabaseField
    private Integer sFamilyId;
    @DatabaseField
    private String sFamilyIdString;
    @DatabaseField
    private String splitMembersDetail;

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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Boolean getLfu() {
        return isLfu;
    }

    public void setLfu(Boolean lfu) {
        isLfu = lfu;
    }

    public Boolean getSplitFamily() {
        return isSplitFamily;
    }

    public void setSplitFamily(Boolean splitFamily) {
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
}
