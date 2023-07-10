/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

/**
 * <p>
 * Used for location type master.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 11:00 AM
 */
public class LocationTypeMasterDto {
    private Integer id;
    private String type;
    private String name;
    private int level;
    private boolean isSohEnable;
    private boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSohEnable() {
        return isSohEnable;
    }

    public void setSohEnable(boolean sohEnable) {
        isSohEnable = sohEnable;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
