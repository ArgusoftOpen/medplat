package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.model.MemberMoConfirmedDetailBean;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MemberMoConfirmedDataBean {

    private Long memberId;

    private Long familyId;

    private Long locationId;

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

    public MemberMoConfirmedDataBean() {
    }

    public MemberMoConfirmedDataBean(MemberMoConfirmedDetailBean moConfirmedDetailBean) {
        this.memberId = moConfirmedDetailBean.getMemberId();
        this.familyId = moConfirmedDetailBean.getFamilyId();
        this.locationId = moConfirmedDetailBean.getLocationId();
        this.moComment = moConfirmedDetailBean.getMoComment();
        this.confirmedForDiabetes = moConfirmedDetailBean.getConfirmedForDiabetes();
        this.confirmedForHypertension = moConfirmedDetailBean.getConfirmedForHypertension();
        this.confirmedForMentalHealth = moConfirmedDetailBean.getConfirmedForMentalHealth();
        this.referenceDue = moConfirmedDetailBean.getReferenceDue();
        this.diabetesTreatmentStatus = moConfirmedDetailBean.getDiabetesTreatmentStatus();
        this.hypertensionTreatmentStatus = moConfirmedDetailBean.getHypertensionTreatmentStatus();
        this.mentalHealthTreatmentStatus = moConfirmedDetailBean.getMentalHealthTreatmentStatus();

        if (moConfirmedDetailBean.getDiabetesDetails() != null && !moConfirmedDetailBean.getDiabetesDetails().isEmpty()) {
            this.diabetesDetails = new Gson().fromJson(moConfirmedDetailBean.getDiabetesDetails(), new TypeToken<List<NcdDiabetesDetailDataBean>>() {
            }.getType());
        }
        if (moConfirmedDetailBean.getHypertensionDetails() != null && !moConfirmedDetailBean.getHypertensionDetails().isEmpty()) {
            this.hypertensionDetails = new Gson().fromJson(moConfirmedDetailBean.getHypertensionDetails(), new TypeToken<List<NcdHypertensionDetailDataBean>>() {
            }.getType());
        }
        if (moConfirmedDetailBean.getMentalHealthDetails() != null && !moConfirmedDetailBean.getMentalHealthDetails().isEmpty()) {
            this.mentalHealthDetails = new Gson().fromJson(moConfirmedDetailBean.getMentalHealthDetails(), new TypeToken<List<NcdMentalHealthDetailDataBean>>() {
            }.getType());
        }
        if (moConfirmedDetailBean.getMedicineDetails() != null && !moConfirmedDetailBean.getMedicineDetails().isEmpty()) {
            this.medicineDetails = new Gson().fromJson(moConfirmedDetailBean.getMedicineDetails(), new TypeToken<List<NcdMemberMedicineDataBean>>() {
            }.getType());
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
