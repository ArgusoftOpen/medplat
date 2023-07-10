
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) admission
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_admission_detail")
public class ChildCmtcNrcAdmission extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "case_id")
    private Integer caseId;

    @Column(name = "referred_by")
    private Integer referredBy;

    @Column(name = "medical_officer_visit_flag")
    private Boolean medicalOfficerVisit;

    @Column(name = "specialist_pediatrician_visit_flag")
    private Boolean specialistPediatricianVisit;

    @Column(name = "admission_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date admissionDate;

    @Column(name = "apetite_test")
    private String apetiteTest;

    @Column(name = "bilateral_pitting_oedema")
    private String bilateralPittingOedema;

    @Column(name = "type_of_admission")
    private String typeOfAdmission;

    @Column(name = "weight_at_admission", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weightAtAdmission;

    @Column(name = "height")
    private Integer height;

    @Column(name = "mid_upper_arm_circumference", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float midUpperArmCircumference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "child_cmtc_nrc_admission_illness_detail", joinColumns = @JoinColumn(name = "admission_id"))
    @Column(name = "illness")
    private Set<String> illness;

    @Column(name = "other_illness")
    private String otherIllness;

    @Column(name = "sd_score")
    private String sdScore;

    @Column(name = "state")
    private String state;

    @Column(name = "death_date")
    private Date deathDate;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "other_death_place")
    private String otherDeathPlace;

    @Column(name = "defaulter_date")
    private Date defaulterDate;

    @Column(name = "screening_center")
    private Integer screeningCenter;

    @Column(name = "breast_feeding")
    private Boolean breastFeeding;

    @Column(name = "complementary_feeding")
    private Boolean complementaryFeeding;

    @Column(name = "problem_in_breast_feeding")
    private Boolean problemInBreastFeeding;

    @Column(name = "problem_in_milk_injection")
    private Boolean problemInMilkInjection;

    @Column(name = "visible_wasting")
    private Boolean visibleWasting;

    @Column(name = "kmc_provided")
    private Boolean kmcProvided;

    @Column(name = "no_of_times_kmc_done")
    private Integer noOfTimesKmcDone;

    @Column(name = "no_of_times_amoxicillin_given")
    private Integer noOfTimesAmoxicillinGiven;

    @Column(name = "consecutive_3_days_weight_gain")
    private Boolean consecutive3DaysWeightGain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public Boolean getMedicalOfficerVisit() {
        return medicalOfficerVisit;
    }

    public void setMedicalOfficerVisit(Boolean medicalOfficerVisit) {
        this.medicalOfficerVisit = medicalOfficerVisit;
    }

    public Boolean getSpecialistPediatricianVisit() {
        return specialistPediatricianVisit;
    }

    public void setSpecialistPediatricianVisit(Boolean specialistPediatricianVisit) {
        this.specialistPediatricianVisit = specialistPediatricianVisit;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getApetiteTest() {
        return apetiteTest;
    }

    public void setApetiteTest(String apetiteTest) {
        this.apetiteTest = apetiteTest;
    }

    public String getBilateralPittingOedema() {
        return bilateralPittingOedema;
    }

    public void setBilateralPittingOedema(String bilateralPittingOedema) {
        this.bilateralPittingOedema = bilateralPittingOedema;
    }

    public String getTypeOfAdmission() {
        return typeOfAdmission;
    }

    public void setTypeOfAdmission(String typeOfAdmission) {
        this.typeOfAdmission = typeOfAdmission;
    }

    public Float getWeightAtAdmission() {
        return weightAtAdmission;
    }

    public void setWeightAtAdmission(Float weightAtAdmission) {
        this.weightAtAdmission = weightAtAdmission;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getMidUpperArmCircumference() {
        return midUpperArmCircumference;
    }

    public void setMidUpperArmCircumference(Float midUpperArmCircumference) {
        this.midUpperArmCircumference = midUpperArmCircumference;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Date getDefaulterDate() {
        return defaulterDate;
    }

    public void setDefaulterDate(Date defaulterDate) {
        this.defaulterDate = defaulterDate;
    }

    public String getOtherIllness() {
        return otherIllness;
    }

    public void setOtherIllness(String otherIllness) {
        this.otherIllness = otherIllness;
    }

    public String getOtherDeathPlace() {
        return otherDeathPlace;
    }

    public void setOtherDeathPlace(String otherDeathPlace) {
        this.otherDeathPlace = otherDeathPlace;
    }

    public Set<String> getIllness() {
        return illness;
    }

    public void setIllness(Set<String> illness) {
        this.illness = illness;
    }

    public Integer getScreeningCenter() {
        return screeningCenter;
    }

    public void setScreeningCenter(Integer screeningCenter) {
        this.screeningCenter = screeningCenter;
    }

    public Boolean getBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(Boolean breastFeeding) {
        this.breastFeeding = breastFeeding;
    }

    public Boolean getComplementaryFeeding() {
        return complementaryFeeding;
    }

    public void setComplementaryFeeding(Boolean complementaryFeeding) {
        this.complementaryFeeding = complementaryFeeding;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Boolean getProblemInBreastFeeding() {
        return problemInBreastFeeding;
    }

    public void setProblemInBreastFeeding(Boolean problemInBreastFeeding) {
        this.problemInBreastFeeding = problemInBreastFeeding;
    }

    public Boolean getProblemInMilkInjection() {
        return problemInMilkInjection;
    }

    public void setProblemInMilkInjection(Boolean problemInMilkInjection) {
        this.problemInMilkInjection = problemInMilkInjection;
    }

    public Boolean getVisibleWasting() {
        return visibleWasting;
    }

    public void setVisibleWasting(Boolean visibleWasting) {
        this.visibleWasting = visibleWasting;
    }

    public Boolean getKmcProvided() {
        return kmcProvided;
    }

    public void setKmcProvided(Boolean kmcProvided) {
        this.kmcProvided = kmcProvided;
    }

    public Integer getNoOfTimesKmcDone() {
        return noOfTimesKmcDone;
    }

    public void setNoOfTimesKmcDone(Integer noOfTimesKmcDone) {
        this.noOfTimesKmcDone = noOfTimesKmcDone;
    }

    public Integer getNoOfTimesAmoxicillinGiven() {
        return noOfTimesAmoxicillinGiven;
    }

    public void setNoOfTimesAmoxicillinGiven(Integer noOfTimesAmoxicillinGiven) {
        this.noOfTimesAmoxicillinGiven = noOfTimesAmoxicillinGiven;
    }

    public Boolean getConsecutive3DaysWeightGain() {
        return consecutive3DaysWeightGain;
    }

    public void setConsecutive3DaysWeightGain(Boolean consecutive3DaysWeightGain) {
        this.consecutive3DaysWeightGain = consecutive3DaysWeightGain;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {

        }

        public static final String CHILD_ID = "childId";
        public static final String ADMISSION_DATE = "admissionDate";
        public static final String STATE = "state";
        public static final String HEALTH_INFRA_ID = "screeningCenter";

    }
}
