/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author abhipsa
 */
@DatabaseTable
public class LocationBean extends BaseEntity {

    @DatabaseField
    private Integer actualID;
    @DatabaseField
    private String name;
    @DatabaseField
    private Integer parent;
    @DatabaseField
    private Integer level;
    @DatabaseField
    private Boolean isActive;
    @DatabaseField
    private String lgdCode;

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

    @NonNull
    @Override
    public String toString() {
        return "LocationBean{" + "actualID=" + actualID + ", name=" + name + ", parent=" + parent + ", level=" + level + ", isActive=" + isActive + '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
