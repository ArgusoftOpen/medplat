package com.argusoft.medplat.ncd.dto;

import java.util.List;

public class MemberMoConfirmedDetailDataBean {

    private Integer memberId;

    private Integer familyId;

    private Integer locationId;

    private String moComment;

    private Boolean confirmedForDiabetes;

    private Boolean confirmedForHypertension;

    private Boolean confirmedForMentalHealth;

    private List<NcdHypertensionDetailDataBean> hypertensionDetails;

    private List<NcdDiabetesDetailDataBean> diabetesDetails;

    private List<NcdMentalHealthDetailDataBean> mentalHealthDetails;

    private List<NcdMemberMedicineDataBean> medicineDetails;

    private Boolean referenceDue;

    private String diabetesTreatmentStatus;

    private String hypertensionTreatmentStatus;

    private String mentalHealthTreatmentStatus;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
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

    public List<NcdHypertensionDetailDataBean> getHypertensionDetails() {
        return hypertensionDetails;
    }

    public void setHypertensionDetails(List<NcdHypertensionDetailDataBean> hypertensionDetails) {
        this.hypertensionDetails = hypertensionDetails;
    }

    public List<NcdDiabetesDetailDataBean> getDiabetesDetails() {
        return diabetesDetails;
    }

    public void setDiabetesDetails(List<NcdDiabetesDetailDataBean> diabetesDetails) {
        this.diabetesDetails = diabetesDetails;
    }

    public List<NcdMentalHealthDetailDataBean> getMentalHealthDetails() {
        return mentalHealthDetails;
    }

    public void setMentalHealthDetails(List<NcdMentalHealthDetailDataBean> mentalHealthDetails) {
        this.mentalHealthDetails = mentalHealthDetails;
    }

    public List<NcdMemberMedicineDataBean> getMedicineDetails() {
        return medicineDetails;
    }

    public void setMedicineDetails(List<NcdMemberMedicineDataBean> medicineDetails) {
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
