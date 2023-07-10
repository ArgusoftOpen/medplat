/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

/**
 *
 * @author prateek
 */
public class SurveyLocationMobDataBean {
    
    private Integer id;
    private Integer actualID;
    private String name;
    private Integer parent;
    private Integer level;
    private Boolean isActive;
    private String lgdCode;

    public SurveyLocationMobDataBean(Integer id, Integer actualID, String name, Integer parent, Boolean isActive, String lgdCode) {
        this.id = id;
        this.actualID = id;
        this.name = name;
        this.parent = parent;
        this.isActive = isActive;
        this.lgdCode = lgdCode;
    }

    public SurveyLocationMobDataBean() {
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLgdCode() {
        return lgdCode;
    }

    public void setLgdCode(String lgdCode) {
        this.lgdCode = lgdCode;
    }
    

    @Override
    public String toString() {
        return "SurveyLocationMobDataBean{" + "id=" + id + ", name=" + name + ", parent=" + parent + ", level=" + level + ", isActive=" + isActive + '}';
    }
    
}
