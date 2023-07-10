package com.argusoft.sewa.android.app.model;

import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable
public class MemberMoConfirmedDetailBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long memberId;

    @DatabaseField
    private Long familyId;

    @DatabaseField
    private Long locationId;

    @DatabaseField
    private String moComment;

    @DatabaseField
    private Boolean confirmedForDiabetes;

    @DatabaseField
    private Boolean confirmedForHypertension;

    @DatabaseField
    private Boolean confirmedForMentalHealth;

    @DatabaseField
    private String hypertensionDetails;

    @DatabaseField
    private String diabetesDetails;

    @DatabaseField
    private String mentalHealthDetails;

    @DatabaseField
    private String medicineDetails;

    @DatabaseField
    private Boolean referenceDue;

    @DatabaseField
    private String diabetesTreatmentStatus;

    @DatabaseField
    private String hypertensionTreatmentStatus;

    @DatabaseField
    private String mentalHealthTreatmentStatus;

    public MemberMoConfirmedDetailBean() {
    }

    public MemberMoConfirmedDetailBean(MemberMoConfirmedDataBean moConfirmedDataBean) {
        this.memberId = moConfirmedDataBean.getMemberId();
        this.familyId = moConfirmedDataBean.getFamilyId();
        this.locationId = moConfirmedDataBean.getLocationId();
        this.moComment = moConfirmedDataBean.getMoComment();
        this.confirmedForDiabetes = moConfirmedDataBean.getConfirmedForDiabetes();
        this.confirmedForHypertension = moConfirmedDataBean.getConfirmedForHypertension();
        this.confirmedForMentalHealth = moConfirmedDataBean.getConfirmedForMentalHealth();
        this.referenceDue = moConfirmedDataBean.getReferenceDue();
        this.diabetesTreatmentStatus = moConfirmedDataBean.getDiabetesTreatmentStatus();
        this.hypertensionTreatmentStatus = moConfirmedDataBean.getHypertensionTreatmentStatus();
        this.mentalHealthTreatmentStatus = moConfirmedDataBean.getMentalHealthTreatmentStatus();

        if (moConfirmedDataBean.getDiabetesDetails() != null && !moConfirmedDataBean.getDiabetesDetails().isEmpty()) {
            this.diabetesDetails = new Gson().toJson(moConfirmedDataBean.getDiabetesDetails());
        }
        if (moConfirmedDataBean.getHypertensionDetails() != null && !moConfirmedDataBean.getHypertensionDetails().isEmpty()) {
            this.hypertensionDetails = new Gson().toJson(moConfirmedDataBean.getHypertensionDetails());
        }
        if (moConfirmedDataBean.getMentalHealthDetails() != null && !moConfirmedDataBean.getMentalHealthDetails().isEmpty()) {
            this.mentalHealthDetails = new Gson().toJson(moConfirmedDataBean.getMentalHealthDetails());
        }
        if (moConfirmedDataBean.getMedicineDetails() != null && !moConfirmedDataBean.getMedicineDetails().isEmpty()) {
            this.medicineDetails = new Gson().toJson(moConfirmedDataBean.getMedicineDetails());
        }
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getMoComment() {
        return moComment;
    }

    public void setMoComment(String moComment) {
        this.moComment = moComment;
    }

    public Boolean getConfirmedForDiabetes() {
        return confirmedForDiabetes;
    }

    public void setConfirmedForDiabetes(Boolean confirmedForDiabetes) {
        this.confirmedForDiabetes = confirmedForDiabetes;
    }

    public Boolean getConfirmedForHypertension() {
        return confirmedForHypertension;
    }

    public void setConfirmedForHypertension(Boolean confirmedForHypertension) {
        this.confirmedForHypertension = confirmedForHypertension;
    }

    public Boolean getConfirmedForMentalHealth() {
        return confirmedForMentalHealth;
    }

    public void setConfirmedForMentalHealth(Boolean confirmedForMentalHealth) {
        this.confirmedForMentalHealth = confirmedForMentalHealth;
    }

    public String getHypertensionDetails() {
        return hypertensionDetails;
    }

    public void setHypertensionDetails(String hypertensionDetails) {
        this.hypertensionDetails = hypertensionDetails;
    }

    public String getDiabetesDetails() {
        return diabetesDetails;
    }

    public void setDiabetesDetails(String diabetesDetails) {
        this.diabetesDetails = diabetesDetails;
    }

    public String getMentalHealthDetails() {
        return mentalHealthDetails;
    }

    public void setMentalHealthDetails(String mentalHealthDetails) {
        this.mentalHealthDetails = mentalHealthDetails;
    }

    public String getMedicineDetails() {
        return medicineDetails;
    }

    public void setMedicineDetails(String medicineDetails) {
        this.medicineDetails = medicineDetails;
    }

    public Boolean getReferenceDue() {
        return referenceDue;
    }

    public void setReferenceDue(Boolean referenceDue) {
        this.referenceDue = referenceDue;
    }

    public String getDiabetesTreatmentStatus() {
        return diabetesTreatmentStatus;
    }

    public void setDiabetesTreatmentStatus(String diabetesTreatmentStatus) {
        this.diabetesTreatmentStatus = diabetesTreatmentStatus;
    }

    public String getHypertensionTreatmentStatus() {
        return hypertensionTreatmentStatus;
    }

    public void setHypertensionTreatmentStatus(String hypertensionTreatmentStatus) {
        this.hypertensionTreatmentStatus = hypertensionTreatmentStatus;
    }

    public String getMentalHealthTreatmentStatus() {
        return mentalHealthTreatmentStatus;
    }

    public void setMentalHealthTreatmentStatus(String mentalHealthTreatmentStatus) {
        this.mentalHealthTreatmentStatus = mentalHealthTreatmentStatus;
    }
}