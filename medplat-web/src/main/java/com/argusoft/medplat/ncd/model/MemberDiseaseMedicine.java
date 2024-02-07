/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis.DiseaseCode;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * <p>
 *     Define ncd_member_disesase_medicine entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_disesase_medicine")
public class MemberDiseaseMedicine extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "medicine_id")
    private Integer medicineId;

    @Column(name = "disease_code")
    @Enumerated(EnumType.STRING)
    private DiseaseCode diseaseCode;

    @Column(name = "diagnosed_on")
    private Date diagnosedOn;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "special_instruction")
    private String specialInstruction;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private Date deletedOn;

    @Column(name = "start_date")
    private Date startDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public DiseaseCode getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(DiseaseCode diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public Date getDiagnosedOn() {
        return diagnosedOn;
    }

    public void setDiagnosedOn(Date diagnosedOn) {
        this.diagnosedOn = diagnosedOn;
    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Define fields name for ncd_member_disesase_medicine.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String REFERENCE_ID = "referenceId";
        public static final String IS_ACTIVE = "active";
    }
}
