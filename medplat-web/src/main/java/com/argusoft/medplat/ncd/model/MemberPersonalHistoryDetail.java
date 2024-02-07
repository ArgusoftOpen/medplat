/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_personal_history")
public class MemberPersonalHistoryDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "service_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "age_at_menarche")
    private Integer ageAtMenarche;

    @Column(name = "menopause_arrived")
    private Boolean menopauseArrived;

    @Column(name = "duration_of_menopause")
    private Integer durationOfMenoapuse;

    @Column(name = "pregnant")
    private Boolean pregnant;

    @Column(name = "lactating")
    private Boolean lactating;

    @Column(name = "regular_periods")
    private Boolean regularPeriods;

    @Column(name = "lmp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lmp;

    @Column(name = "bleeding")
    private String bleeding;

    @Column(name = "associated_with")
    private String associatedWith;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "diagnosed_for_hypertension")
    private Boolean diagnosedForHypertension;

    @Column(name = "under_treatement_for_hypertension")
    private Boolean underTreatementForHypertension;

    @Column(name = "diagnosed_for_diabetes")
    private Boolean diagnosedForDiabetes;

    @Column(name = "under_treatement_for_diabetes")
    private Boolean underTreatementForDiabetes;

    @Column(name = "diagnosed_for_heart_diseases")
    private Boolean diagnosedForHeartDiseases;

    @Column(name = "under_treatement_for_heart_diseases")
    private Boolean underTreatementForHeartDiseases;

    @Column(name = "diagnosed_for_stroke")
    private Boolean diagnosedForStroke;

    @Column(name = "under_treatement_for_stroke")
    private Boolean underTreatementForStroke;

    @Column(name = "diagnosed_for_kidney_failure")
    private Boolean diagnosedForKidneyFailure;

    @Column(name = "under_treatement_for_kidney_failure")
    private Boolean underTreatementForKidneyFailure;

    @Column(name = "diagnosed_for_non_healing_wound")
    private Boolean diagnosedForNonHealingWound;

    @Column(name = "under_treatement_for_non_healing_wound")
    private Boolean underTreatementForNonHealingWound;

    @Column(name = "diagnosed_for_copd")
    private Boolean diagnosedForCOPD;

    @Column(name = "under_treatement_for_copd")
    private Boolean underTreatementForCOPD;

    @Column(name = "diagnosed_for_asthama")
    private Boolean diagnosedForAsthama;

    @Column(name = "under_treatement_for_asthama")
    private Boolean underTreatementForAsthama;

    @Column(name = "diagnosed_for_oral_cancer")
    private Boolean diagnosedForOralCancer;

    @Column(name = "under_treatement_for_oral_cancer")
    private Boolean underTreatementForOralCancer;

    @Column(name = "diagnosed_for_breast_cancer")
    private Boolean diagnosedForBreastCancer;

    @Column(name = "under_treatement_for_breast_cancer")
    private Boolean underTreatementForBreastCancer;

    @Column(name = "diagnosed_for_cervical_cancer")
    private Boolean diagnosedForCervicalCancer;

    @Column(name = "under_treatement_for_cervical_cancer")
    private Boolean underTreatementForCervicalCancer;

    @Column(name = "any_other_examination")
    private Boolean anyOtherExamination;

    @Column(name = "specify_other_examination")
    private String specifyOtherExamination;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "bmi", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float bmi;

//    @Column(name = "menopause_duration_in_years")
//    private Integer menopauseDurationInYears;
//
//    @Column(name = "menopause_duration_in_months")
//    private Integer menopauseDurationInMonths;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getAgeAtMenarche() {
        return ageAtMenarche;
    }

    public void setAgeAtMenarche(Integer ageAtMenarche) {
        this.ageAtMenarche = ageAtMenarche;
    }

    public Boolean getMenopauseArrived() {
        return menopauseArrived;
    }

    public void setMenopauseArrived(Boolean menopauseArrived) {
        this.menopauseArrived = menopauseArrived;
    }

    public Integer getDurationOfMenoapuse() {
        return durationOfMenoapuse;
    }

    public void setDurationOfMenoapuse(Integer durationOfMenoapuse) {
        this.durationOfMenoapuse = durationOfMenoapuse;
    }

    public Boolean getPregnant() {
        return pregnant;
    }

    public void setPregnant(Boolean pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getLactating() {
        return lactating;
    }

    public void setLactating(Boolean lactating) {
        this.lactating = lactating;
    }

    public Boolean getRegularPeriods() {
        return regularPeriods;
    }

    public void setRegularPeriods(Boolean regularPeriods) {
        this.regularPeriods = regularPeriods;
    }

    public Date getLmp() {
        return lmp;
    }

    public void setLmp(Date lmp) {
        this.lmp = lmp;
    }

    public String getBleeding() {
        return bleeding;
    }

    public void setBleeding(String bleeding) {
        this.bleeding = bleeding;
    }

    public String getAssociatedWith() {
        return associatedWith;
    }

    public void setAssociatedWith(String associatedWith) {
        this.associatedWith = associatedWith;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getDiagnosedForHypertension() {
        return diagnosedForHypertension;
    }

    public void setDiagnosedForHypertension(Boolean diagnosedForHypertension) {
        this.diagnosedForHypertension = diagnosedForHypertension;
    }

    public Boolean getUnderTreatementForHypertension() {
        return underTreatementForHypertension;
    }

    public void setUnderTreatementForHypertension(Boolean underTreatementForHypertension) {
        this.underTreatementForHypertension = underTreatementForHypertension;
    }

    public Boolean getDiagnosedForDiabetes() {
        return diagnosedForDiabetes;
    }

    public void setDiagnosedForDiabetes(Boolean diagnosedForDiabetes) {
        this.diagnosedForDiabetes = diagnosedForDiabetes;
    }

    public Boolean getUnderTreatementForDiabetes() {
        return underTreatementForDiabetes;
    }

    public void setUnderTreatementForDiabetes(Boolean underTreatementForDiabetes) {
        this.underTreatementForDiabetes = underTreatementForDiabetes;
    }

    public Boolean getDiagnosedForHeartDiseases() {
        return diagnosedForHeartDiseases;
    }

    public void setDiagnosedForHeartDiseases(Boolean diagnosedForHeartDiseases) {
        this.diagnosedForHeartDiseases = diagnosedForHeartDiseases;
    }

    public Boolean getUnderTreatementForHeartDiseases() {
        return underTreatementForHeartDiseases;
    }

    public void setUnderTreatementForHeartDiseases(Boolean underTreatementForHeartDiseases) {
        this.underTreatementForHeartDiseases = underTreatementForHeartDiseases;
    }

    public Boolean getDiagnosedForStroke() {
        return diagnosedForStroke;
    }

    public void setDiagnosedForStroke(Boolean diagnosedForStroke) {
        this.diagnosedForStroke = diagnosedForStroke;
    }

    public Boolean getUnderTreatementForStroke() {
        return underTreatementForStroke;
    }

    public void setUnderTreatementForStroke(Boolean underTreatementForStroke) {
        this.underTreatementForStroke = underTreatementForStroke;
    }

    public Boolean getDiagnosedForKidneyFailure() {
        return diagnosedForKidneyFailure;
    }

    public void setDiagnosedForKidneyFailure(Boolean diagnosedForKidneyFailure) {
        this.diagnosedForKidneyFailure = diagnosedForKidneyFailure;
    }

    public Boolean getUnderTreatementForKidneyFailure() {
        return underTreatementForKidneyFailure;
    }

    public void setUnderTreatementForKidneyFailure(Boolean underTreatementForKidneyFailure) {
        this.underTreatementForKidneyFailure = underTreatementForKidneyFailure;
    }

    public Boolean getDiagnosedForNonHealingWound() {
        return diagnosedForNonHealingWound;
    }

    public void setDiagnosedForNonHealingWound(Boolean diagnosedForNonHealingWound) {
        this.diagnosedForNonHealingWound = diagnosedForNonHealingWound;
    }

    public Boolean getUnderTreatementForNonHealingWound() {
        return underTreatementForNonHealingWound;
    }

    public void setUnderTreatementForNonHealingWound(Boolean underTreatementForNonHealingWound) {
        this.underTreatementForNonHealingWound = underTreatementForNonHealingWound;
    }

    public Boolean getDiagnosedForCOPD() {
        return diagnosedForCOPD;
    }

    public void setDiagnosedForCOPD(Boolean diagnosedForCOPD) {
        this.diagnosedForCOPD = diagnosedForCOPD;
    }

    public Boolean getUnderTreatementForCOPD() {
        return underTreatementForCOPD;
    }

    public void setUnderTreatementForCOPD(Boolean underTreatementForCOPD) {
        this.underTreatementForCOPD = underTreatementForCOPD;
    }

    public Boolean getDiagnosedForAsthama() {
        return diagnosedForAsthama;
    }

    public void setDiagnosedForAsthama(Boolean diagnosedForAsthama) {
        this.diagnosedForAsthama = diagnosedForAsthama;
    }

    public Boolean getUnderTreatementForAsthama() {
        return underTreatementForAsthama;
    }

    public void setUnderTreatementForAsthama(Boolean underTreatementForAsthama) {
        this.underTreatementForAsthama = underTreatementForAsthama;
    }

    public Boolean getDiagnosedForOralCancer() {
        return diagnosedForOralCancer;
    }

    public void setDiagnosedForOralCancer(Boolean diagnosedForOralCancer) {
        this.diagnosedForOralCancer = diagnosedForOralCancer;
    }

    public Boolean getUnderTreatementForOralCancer() {
        return underTreatementForOralCancer;
    }

    public void setUnderTreatementForOralCancer(Boolean underTreatementForOralCancer) {
        this.underTreatementForOralCancer = underTreatementForOralCancer;
    }

    public Boolean getDiagnosedForBreastCancer() {
        return diagnosedForBreastCancer;
    }

    public void setDiagnosedForBreastCancer(Boolean diagnosedForBreastCancer) {
        this.diagnosedForBreastCancer = diagnosedForBreastCancer;
    }

    public Boolean getUnderTreatementForBreastCancer() {
        return underTreatementForBreastCancer;
    }

    public void setUnderTreatementForBreastCancer(Boolean underTreatementForBreastCancer) {
        this.underTreatementForBreastCancer = underTreatementForBreastCancer;
    }

    public Boolean getDiagnosedForCervicalCancer() {
        return diagnosedForCervicalCancer;
    }

    public void setDiagnosedForCervicalCancer(Boolean diagnosedForCervicalCancer) {
        this.diagnosedForCervicalCancer = diagnosedForCervicalCancer;
    }

    public Boolean getUnderTreatementForCervicalCancer() {
        return underTreatementForCervicalCancer;
    }

    public void setUnderTreatementForCervicalCancer(Boolean underTreatementForCervicalCancer) {
        this.underTreatementForCervicalCancer = underTreatementForCervicalCancer;
    }

    public Boolean getAnyOtherExamination() {
        return anyOtherExamination;
    }

    public void setAnyOtherExamination(Boolean anyOtherExamination) {
        this.anyOtherExamination = anyOtherExamination;
    }

    public String getSpecifyOtherExamination() {
        return specifyOtherExamination;
    }

    public void setSpecifyOtherExamination(String specifyOtherExamination) {
        this.specifyOtherExamination = specifyOtherExamination;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

//    public Integer getMenopauseDurationInYears() {
//        return menopauseDurationInYears;
//    }
//
//    public void setMenopauseDurationInYears(Integer menopauseDurationInYears) {
//        this.menopauseDurationInYears = menopauseDurationInYears;
//    }
//
//    public Integer getMenopauseDurationInMonths() {
//        return menopauseDurationInMonths;
//    }
//
//    public void setMenopauseDurationInMonths(Integer menopauseDurationInMonths) {
//        this.menopauseDurationInMonths = menopauseDurationInMonths;
//    }

    /**
     * Define fields name for ncd_member_cbac_detail.
     */
    public static class Fields {
        private Fields(){
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String FAMILY_ID = "familyId";
        public static final String LOCATION_ID = "locationId";
        public static final String PREGNANT = "pregnant";
        public static final String LMP = "lmp";
        public static final String REMARKS = "remarks";
        public static final String DONE_BY = "doneBy";
    }
}
