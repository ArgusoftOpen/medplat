package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class LocationMasterBean extends BaseEntity {

    @DatabaseField(index = true)
    private Long actualID;
    @DatabaseField
    private String name;
    @DatabaseField(index = true)
    private Integer parent;
    @DatabaseField
    private Integer level;
    @DatabaseField
    private String type;
    @DatabaseField
    private Boolean isActive;
    @DatabaseField
    private String fhwDetailString;
    @DatabaseField
    private Date modifiedOn;

    public Long getActualID() {
        return actualID;
    }

    public void setActualID(Long actualID) {
        this.actualID = actualID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getFhwDetailString() {
        return fhwDetailString;
    }

    public void setFhwDetailString(String fhwDetailString) {
        this.fhwDetailString = fhwDetailString;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return "LocationMasterBean{" +
                "actualID=" + actualID +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", level=" + level +
                ", type='" + type + '\'' +
                ", isActive=" + isActive +
                ", fhwDetailString='" + fhwDetailString + '\'' +
                '}';
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
