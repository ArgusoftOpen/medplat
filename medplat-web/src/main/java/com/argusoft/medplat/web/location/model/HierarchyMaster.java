/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define HierarchyMaster entity and its fields.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
//@Entity
//@Table(name = "hierarchy_master")
public class HierarchyMaster implements Serializable {

    @Id
    @Column(name = "hierarchy_type", nullable = false)
    private String hierarchyType;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "code", nullable = false)
    private String code;

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

    /**
     * Define fields name for HierarchyMaster
     */
    public static class Fields {
        private Fields(){
        }
        public static final String HIERARCHY_TYPE = "hierarchyType";
        public static final String NAME = "name";
        public static final String CODE = "code";
    }
}
