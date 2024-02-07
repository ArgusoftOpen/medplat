package com.argusoft.medplat.ncd.dto;

public class DrugInventoryDetailDataBean {

    private Integer locationId;

    private Integer medicineId;
    private String medicineName;

    private Integer balanceInHand;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
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
