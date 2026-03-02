/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 *
 * @author kunjan
 */
public class LocationMasterDataBean {
    
    private Integer actualID;
    
    private String name;
    
    private Integer parent;
    
    private Integer level;
    
    private String type;
    
    private String fhwDetailString;
    
    private Date modifiedOn;

    public Integer getActualID() {
        return actualID;
    }

    public void setActualID(Integer actualID) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFhwDetailString() {
        return fhwDetailString;
    }

    public void setFhwDetailString(String fhwDetailString) {
        this.fhwDetailString = fhwDetailString;
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

}
