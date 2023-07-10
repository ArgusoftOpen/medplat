/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * @author abhipsa
 */
public class SurveyLocationMobDataBean {

    private Integer id;
    private Integer level;
    private String name;
    private Integer parent;
    private Boolean isActive;
    private String lgdCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    @NonNull
    @Override
    public String toString() {
        return "SurveyLocationMobDataBean{" + "id=" + id + ", level=" + level + ", name=" + name + ", parent=" + parent + ", isActive=" + isActive + '}';
    }
}
