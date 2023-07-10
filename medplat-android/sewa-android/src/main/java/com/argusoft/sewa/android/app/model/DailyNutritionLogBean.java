package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class DailyNutritionLogBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer locationId;

    @DatabaseField
    private Long seviceDate;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Long getSeviceDate() {
        return seviceDate;
    }

    public void setSeviceDate(Long seviceDate) {
        this.seviceDate = seviceDate;
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
