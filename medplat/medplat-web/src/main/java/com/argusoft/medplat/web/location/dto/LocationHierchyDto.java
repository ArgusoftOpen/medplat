/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

import java.util.List;

/**
 *
 * <p>
 *     Used for location hierarchy.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public class LocationHierchyDto {

    private String locationLabel;
    private List<LocationDetailDto> locationDetails;
    private Integer level;

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public List<LocationDetailDto> getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(List<LocationDetailDto> locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
