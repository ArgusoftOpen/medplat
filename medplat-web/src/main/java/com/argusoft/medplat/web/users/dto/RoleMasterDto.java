/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

import com.argusoft.medplat.common.dto.AssignRoleWithFeatureDto;
import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.users.model.RoleMaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Defines fields related to role</p>
 *
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class RoleMasterDto {

    private Integer id;
    private String name;
    private String code;
    private String description;
    private boolean canSelfManage;
    private Integer maxPosition;
    private Integer maxHealthInfra;
    private Integer createdBy;
    private Date createdOn;
    private RoleMaster.State state;
    private List<Integer> manageByRoleIds;
    private List<Integer> manageByTeamTypeIds;
    private List<LocationTypeMasterDto> locationTypes;
    private Boolean isLastNameMandatory;
    private Boolean isEmailMandatory;
    private Boolean isContactNumMandatory;
    private Boolean isAadharNumMandatory;
    private Boolean isConvoxIdMandatory;
    private Boolean isHealthInfraMandatory;
    private Boolean isGeolocationMandatory;
    private String assignedFeatures;
    private List<Integer> healthInfrastructureIds;
    private List<Integer> categoryIds;
    private List<AssignRoleWithFeatureDto> assignedFeatureList;
    private String roleType;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleMaster.State getState() {
        return state;
    }

    public void setState(RoleMaster.State state) {
        this.state = state;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public List<Integer> getRoleIds() {
        return manageByRoleIds == null ? new LinkedList<>() : manageByRoleIds;
    }

    public void setRoleIds(List<Integer> manageByRoleIds) {
        this.manageByRoleIds = manageByRoleIds;
    }

    public List<LocationTypeMasterDto> getLocationTypes() {
        return locationTypes == null ? new ArrayList<>() : locationTypes;
    }

    public void setLocationTypes(List<LocationTypeMasterDto> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public boolean isCanSelfManage() {
        return canSelfManage;
    }

    public void setCanSelfManage(boolean canSelfManage) {
        this.canSelfManage = canSelfManage;
    }

    public Integer getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
    }

    public Integer getMaxHealthInfra() {
        return maxHealthInfra;
    }

    public void setMaxHealthInfra(Integer maxHealthInfra) {
        this.maxHealthInfra = maxHealthInfra;
    }

    public List<Integer> getManageByTeamTypeIds() {
        return manageByTeamTypeIds == null ? new LinkedList<>() : manageByTeamTypeIds;
    }

    public void setManageByTeamTypeIds(List<Integer> manageByTeamTypeIds) {
        this.manageByTeamTypeIds = manageByTeamTypeIds;
    }

    public Boolean getIsLastNameMandatory() {
        return isLastNameMandatory;
    }

    public void setIsLastNameMandatory(Boolean isLastNameMandatory) {
        this.isLastNameMandatory = isLastNameMandatory;
    }

    public Boolean getIsEmailMandatory() {
        return isEmailMandatory;
    }

    public void setIsEmailMandatory(Boolean isEmailMandatory) {
        this.isEmailMandatory = isEmailMandatory;
    }

    public Boolean getIsContactNumMandatory() {
        return isContactNumMandatory;
    }

    public void setIsContactNumMandatory(Boolean isContactNumMandatory) {
        this.isContactNumMandatory = isContactNumMandatory;
    }

    public Boolean getIsAadharNumMandatory() {
        return isAadharNumMandatory;
    }

    public void setIsAadharNumMandatory(Boolean isAadharNumMandatory) {
        this.isAadharNumMandatory = isAadharNumMandatory;
    }

    public String getAssignedFeatures() {
        return assignedFeatures;
    }

    public void setAssignedFeatures(String assignedFeatures) {
        this.assignedFeatures = assignedFeatures;
    }

    public List<Integer> getHealthInfrastructureIds() {
        return healthInfrastructureIds;
    }

    public void setHealthInfrastructureIds(List<Integer> healthInfrastructureIds) {
        this.healthInfrastructureIds = healthInfrastructureIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Boolean getIsConvoxIdMandatory() {
        return isConvoxIdMandatory;
    }

    public void setIsConvoxIdMandatory(Boolean isConvoxIdMandatory) {
        this.isConvoxIdMandatory = isConvoxIdMandatory;
    }

    public List<AssignRoleWithFeatureDto> getAssignedFeatureList() {
        return assignedFeatureList;
    }

    public void setAssignedFeatureList(List<AssignRoleWithFeatureDto> assignedFeatureList) {
        this.assignedFeatureList = assignedFeatureList;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Boolean getIsHealthInfraMandatory() {
        return isHealthInfraMandatory;
    }

    public void setIsHealthInfraMandatory(Boolean healthInfraMandatory) {
        isHealthInfraMandatory = healthInfraMandatory;
    }

    public Boolean getIsGeolocationMandatory() {
        return isGeolocationMandatory;
    }

    public void setIsGeolocationMandatory(Boolean geolocationMandatory) {
        isGeolocationMandatory = geolocationMandatory;
    }
}
