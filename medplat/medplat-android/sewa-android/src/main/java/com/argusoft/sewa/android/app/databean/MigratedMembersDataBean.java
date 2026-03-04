package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 26/2/19.
 */
public class MigratedMembersDataBean {

    private Long migrationId;
    private Long memberId;
    private String name;
    private String healthId;
    private String familyMigratedFrom;
    private String familyMigratedTo;
    private String locationMigratedFrom;
    private String locationMigratedTo;
    private Long fromLocationId;
    private Long toLocationId;
    private Long modifiedOn;
    private Boolean isOut;
    private Boolean outOfState;
    private Boolean isConfirmed;
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

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getOut() {
        return isOut;
    }

    public void setOut(Boolean out) {
        isOut = out;
    }

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getLfu() {
        return isLfu;
    }

    public void setLfu(Boolean lfu) {
        isLfu = lfu;
    }
}
