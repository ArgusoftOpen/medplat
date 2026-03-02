package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * Created by prateek on 23/6/18.
 */

public class LocationDetailDataBean {

    private Integer id;
    private String name;
    private String type;
    private String locType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    @NonNull
    @Override
    public String toString() {
        return "LocationDetailDataBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", locType='" + locType + '\'' +
                '}';
    }
}
