/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

/**
 *
 * <p>
 *     Used for location details.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public class LocationDetailDto {

    private Integer id;
    private String name;
    private String type;
    private String locType;

    public LocationDetailDto() {
    }

    public LocationDetailDto(Integer id, String name) {
        this.name = name;
        this.id = id;
    }

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

}
