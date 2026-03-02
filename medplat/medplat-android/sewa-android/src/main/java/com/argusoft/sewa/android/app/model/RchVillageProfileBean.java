package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by prateek on 23/6/18.
 */

@DatabaseTable
public class RchVillageProfileBean extends BaseEntity {

    private Integer villageId;

    private String rchVillageProfileDto;

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public String getRchVillageProfileDto() {
        return rchVillageProfileDto;
    }

    public void setRchVillageProfileDto(String rchVillageProfileDto) {
        this.rchVillageProfileDto = rchVillageProfileDto;
    }

    @NonNull
    @Override
    public String toString() {
        return "RchVillageProfileBean{" +
                "\nvillageId=" + villageId +
                ", \nrchVillageProfileDto='" + rchVillageProfileDto + '\'' +
                '}';
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
