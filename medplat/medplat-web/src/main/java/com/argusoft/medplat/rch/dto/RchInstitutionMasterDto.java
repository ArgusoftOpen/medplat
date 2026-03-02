/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

/**
 * <p>
 * Used for rch institution master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class RchInstitutionMasterDto {

    Integer id;
    String name;
    Integer locationId;
    String type;
    Boolean isLocation;
    String state;

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

    public Boolean getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Boolean isLocation) {
        this.isLocation = isLocation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RchInstitutionMasterDto{" + "id=" + id + ", name=" + name + ", locationId=" + locationId + ", type=" + type + ", isLocation=" + isLocation + ", state=" + state + '}';
    }

}
