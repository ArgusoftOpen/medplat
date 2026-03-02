/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

import com.argusoft.medplat.web.users.model.RoleHierarchyManagement;

/**
 * <p>Defines fields related to role hierarchy</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class RoleHierarchyDto {
    
    private String locationType;
    private Integer roleId;
    private Integer userId;
    private Integer id;
    private int level;
    private RoleHierarchyManagement.State state;

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public RoleHierarchyManagement.State getState() {
        return state;
    }

    public void setState(RoleHierarchyManagement.State state) {
        this.state = state;
    }
    
    
    
    
            
}
