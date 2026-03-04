/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.infrastructure.dto;

import java.util.Date;

/**
 *
 * <p>
 *     Used for school master.
 * </p>
 * @author rahul
 * @since 26/08/20 11:00 AM
 *
 */
public class SchoolDataBean {
    private Integer actualId;
    private String name;
    private String code;
    private Integer grantType;
    private Integer schoolType;
    private Integer locationId;
    private Boolean isPrimarySchool;
    private Boolean isHigherSecondarySchool;
    private Boolean isMadresa;
    private Boolean isGurukul;
    private Date modifiedOn;

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

    public Boolean getIsPrimarySchool() {
        return isPrimarySchool;
    }

    public void setIsPrimarySchool(Boolean isPrimarySchool) {
        this.isPrimarySchool = isPrimarySchool;
    }

    public Boolean getIsHigherSecondarySchool() {
        return isHigherSecondarySchool;
    }

    public void setIsHigherSecondarySchool(Boolean isHigherSecondarySchool) {
        this.isHigherSecondarySchool = isHigherSecondarySchool;
    }

    public Boolean getIsMadresa() {
        return isMadresa;
    }

    public void setIsMadresa(Boolean isMadresa) {
        this.isMadresa = isMadresa;
    }

    public Boolean getIsGurukul() {
        return isGurukul;
    }

    public void setIsGurukul(Boolean isGurukul) {
        this.isGurukul = isGurukul;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
