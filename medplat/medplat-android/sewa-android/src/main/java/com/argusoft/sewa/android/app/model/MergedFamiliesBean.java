package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by kunjan on 27/3/18.
 */

@DatabaseTable
public class MergedFamiliesBean extends BaseEntity implements Serializable {
    @DatabaseField
    private String mergedFamilyId;
    @DatabaseField
    private String expandedFamilyId;
    @DatabaseField
    private boolean isSyncedWithServer;

    public MergedFamiliesBean() {

    }

    public MergedFamiliesBean(String mergedFamilyId, String expandedFamilyId, boolean isSyncedWithServer) {
        this.mergedFamilyId = mergedFamilyId;
        this.expandedFamilyId = expandedFamilyId;
        this.isSyncedWithServer = isSyncedWithServer;
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

    public boolean isSyncedWithServer() {
        return isSyncedWithServer;
    }

    public void setSyncedWithServer(boolean syncedWithServer) {
        isSyncedWithServer = syncedWithServer;
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
