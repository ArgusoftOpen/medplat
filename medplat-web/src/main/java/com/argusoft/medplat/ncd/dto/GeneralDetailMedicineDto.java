package com.argusoft.medplat.ncd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class GeneralDetailMedicineDto {

    public Integer medicineId;
    private String medicineName;
    private Integer frequency;
    private Integer quantity;
    private Integer duration;
    private String specialInstruction;
    private Date expiryDate;
    private Integer id;
    private Date issuedDate;
    private Date startDate;
    @JsonProperty(value = "isReturn")
    private Boolean isReturn;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

    @Override
    public String toString() {
        return "GeneralDetailMedicineDto{" +
                "medicineId=" + medicineId +
                ", medicineName='" + medicineName + '\'' +
                ", frequency=" + frequency +
                ", quantity=" + quantity +
                ", duration=" + duration +
                ", specialInstruction='" + specialInstruction + '\'' +
                ", expiryDate=" + expiryDate +
                ", id=" + id +
                '}';
    }
}
