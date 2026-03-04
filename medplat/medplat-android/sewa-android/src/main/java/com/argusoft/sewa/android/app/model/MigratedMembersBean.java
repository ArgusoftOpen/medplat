package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by prateek on 22/2/19.
 */
@DatabaseTable
public class MigratedMembersBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long migrationId;
    @DatabaseField
    private Long memberId;
    @DatabaseField
    private String name;
    @DatabaseField
    private String healthId;
    @DatabaseField
    private String familyMigratedFrom;
    @DatabaseField
    private String familyMigratedTo;
    @DatabaseField
    private String locationMigratedFrom;
    @DatabaseField
    private String locationMigratedTo;
    @DatabaseField
    private Long fromLocationId;
    @DatabaseField
    private Long toLocationId;
    @DatabaseField
    private Date modifiedOn;
    @DatabaseField
    private Boolean isConfirmed;
    @DatabaseField
    private Boolean outOfState;
    @DatabaseField
    private Boolean isLfu;

    public Long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Long migrationId) {
        this.migrationId = migrationId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getFamilyMigratedFrom() {
        return familyMigratedFrom;
    }

    public void setFamilyMigratedFrom(String familyMigratedFrom) {
        this.familyMigratedFrom = familyMigratedFrom;
    }

    public String getFamilyMigratedTo() {
        return familyMigratedTo;
    }

    public void setFamilyMigratedTo(String familyMigratedTo) {
        this.familyMigratedTo = familyMigratedTo;
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
