/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.dto;

import java.util.List;
import java.util.UUID;

/**
 * <p>Defines fields related to sync with server management</p>
 * @author ashish
 * @since 26/08/2020 5:30
 */
public class SyncWithServerDto {
    private UUID featureUUID;
    private List<Integer> serverlist;
    private List<Integer> configIds;
    private String type;

    private boolean selectAll;

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public List<Integer> getConfigIds() {
        return configIds;
    }

    public void setConfigIds(List<Integer> configIds) {
        this.configIds = configIds;
    }
    public UUID getFeatureUUID() {
        return featureUUID;
    }

    public void setFeatureUUID(UUID featureUUID) {
        this.featureUUID = featureUUID;
    }

    public List<Integer> getServerlist() {
        return serverlist;
    }

    public void setServerlist(List<Integer> serverlist) {
        this.serverlist = serverlist;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
