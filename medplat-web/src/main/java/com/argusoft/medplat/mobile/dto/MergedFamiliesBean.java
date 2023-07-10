/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

/**
 *
 * @author kunjan
 */
public class MergedFamiliesBean {

    private Integer id;
    private String mergedFamilyId;
    private String expandedFamilyId;
    private boolean isSyncedWithServer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMergedFamilyId() {
        return mergedFamilyId;
    }

    public void setMergedFamilyId(String mergedFamilyId) {
        this.mergedFamilyId = mergedFamilyId;
    }

    public String getExpandedFamilyId() {
        return expandedFamilyId;
    }

    public void setExpandedFamilyId(String expandedFamilyId) {
        this.expandedFamilyId = expandedFamilyId;
    }

    public boolean isIsSyncedWithServer() {
        return isSyncedWithServer;
    }

    public void setIsSyncedWithServer(boolean isSyncedWithServer) {
        this.isSyncedWithServer = isSyncedWithServer;
    }

    @Override
    public String toString() {
        return "MergedFamiliesBean{" + "id=" + id + ", mergedFamilyId=" + mergedFamilyId + ", expandedFamilyId=" + expandedFamilyId + ", isSyncedWithServer=" + isSyncedWithServer + '}';
    }
}
