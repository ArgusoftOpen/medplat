package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class SchoolBean extends BaseEntity implements Serializable {
    @DatabaseField
    private Integer actualId;

    @DatabaseField
    private String name;

    @DatabaseField
    private String code;

    @DatabaseField
    private Integer grantType;

    @DatabaseField
    private Integer schoolType;

    @DatabaseField
    private Integer locationId;

    @DatabaseField
    private Date modifiedOn;

    @DatabaseField
    private Boolean isPrimarySchool;

    @DatabaseField
    private Boolean isHigherSecondarySchool;

    @DatabaseField
    private Boolean isMadresa;

    @DatabaseField
    private Boolean isGurukul;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public Integer getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(Integer schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getPrimarySchool() {
        return isPrimarySchool;
    }

    public void setPrimarySchool(Boolean primarySchool) {
        isPrimarySchool = primarySchool;
    }

    public Boolean getHigherSecondarySchool() {
        return isHigherSecondarySchool;
    }

    public void setHigherSecondarySchool(Boolean higherSecondarySchool) {
        isHigherSecondarySchool = higherSecondarySchool;
    }

    public Boolean getMadresa() {
        return isMadresa;
    }

    public void setMadresa(Boolean madresa) {
        isMadresa = madresa;
    }

    public Boolean getGurukul() {
        return isGurukul;
    }

    public void setGurukul(Boolean gurukul) {
        isGurukul = gurukul;
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
