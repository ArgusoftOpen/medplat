/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

/**
 *
 * <p>
 *     Used for hierarchy master.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class HierarchyMasterDto {

    private String hierarchyType;
    private String name;
    private String code;

    public HierarchyMasterDto() {
    }

    public HierarchyMasterDto(String hierarchyType, String name, String code) {
        this.name = name;
        this.hierarchyType = hierarchyType;
        this.code = code;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
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
}
