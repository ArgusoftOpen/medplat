package com.argusoft.sewa.android.app.model;

import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable
public class DrugInventoryBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long locationId;

    @DatabaseField
    private Integer medicineId;

    @DatabaseField
    private String medicineName;

    @DatabaseField
    private Integer balanceInHand;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getBalanceInHand() {
        return balanceInHand;
    }

    public void setBalanceInHand(Integer balanceInHand) {
        this.balanceInHand = balanceInHand;
    }
}