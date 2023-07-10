package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class LocationTypeBean extends BaseEntity {

    @DatabaseField
    private String type;
    @DatabaseField
    private String name;
    @DatabaseField
    private Integer level;
    @DatabaseField
    private Date modifiedOn;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "LocationTypeBean{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", modifiedOn=" + modifiedOn +
                '}';
    }
}
