/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>Defines fields related to location</p>
 * @author smeet
 * @since 26/08/2020 5:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDto {
    private Integer locationId;
    private String type;
    private Integer level;
    private String name;
    private String locationFullName;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationFullName() {
        return locationFullName;
    }

    public void setLocationFullName(String locationFullName) {
        this.locationFullName = locationFullName;
    }

    @Override
    public String toString() {
        return "LocationDto{" + "locationId=" + locationId + ", type=" + type + ", level=" + level + ", name=" + name + ", locationFullName=" + locationFullName + '}';
    }
    
}
