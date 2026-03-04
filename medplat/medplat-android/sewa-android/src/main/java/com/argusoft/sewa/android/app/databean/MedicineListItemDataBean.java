package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class MedicineListItemDataBean {

    private Integer medicineId;

    private String medicineName;

    private Integer frequency;

    private Integer duration;

    private Integer quantity;

    private String specialInstruction;

    private Integer stock;

    private Date expiryDate;

    public MedicineListItemDataBean(Integer medicineId, String medicineName, Integer frequency, Integer duration, Integer quantity, String specialInstruction, Integer stock, Date expiryDate) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.frequency = frequency;
        this.duration = duration;
        this.quantity = quantity;
        this.specialInstruction = specialInstruction;
        this.stock = stock;
        this.expiryDate = expiryDate;
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
