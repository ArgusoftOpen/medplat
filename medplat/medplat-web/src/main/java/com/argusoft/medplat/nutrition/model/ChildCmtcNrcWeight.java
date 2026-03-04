
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) weight
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_weight_detail")
public class ChildCmtcNrcWeight extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "weight_date")
    @Temporal(TemporalType.DATE)
    private Date weightDate;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "is_mother_councelling")
    private Boolean isMotherCouncelling;

    @Column(name = "is_amoxicillin")
    private Boolean isAmoxicillin;

    @Column(name = "is_vitaminA")
    private Boolean isVitaminA;

    @Column(name = "is_sugar_solution")
    private Boolean isSugarSolution;

    @Column(name = "is_albendazole")
    private Boolean isAlbendazole;

    @Column(name = "is_folic_acid")
    private Boolean isFolicAcid;

    @Column(name = "is_potassium")
    private Boolean isPotassium;

    @Column(name = "is_magnesium")
    private Boolean isMagnesium;

    @Column(name = "is_zinc")
    private Boolean isZinc;

    @Column(name = "multi_vitamin_syrup")
    private Boolean multiVitaminSyrup;

    @Column(name = "is_iron")
    private Boolean isIron;

    @Column(name = "bilateral_pitting_oedema")
    private String bilateralPittingOedema;

    @Column(name = "formula_given")
    private String formulaGiven;

    @Column(name = "other_higher_nutrients_given")
    private Boolean otherHigherNutrientsGiven;

    @Column(name = "mid_upper_arm_circumference", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float midUpperArmCircumference;

    @Column(name = "height")
    private Integer height;

    @Column(name = "higher_facility_referral")
    private String higherFacilityReferral;

    @Column(name = "referral_reason")
    private String referralReason;

    @Column(name = "night_stay")
    private Boolean nightStay;

    @Column(name = "higher_facility_referral_place")
    private Integer higherFacilityReferralPlace;

    @Column(name = "kmc_provided")
    private Boolean kmcProvided;

    @Column(name = "no_of_times_kmc_done")
    private Integer noOfTimesKmcDone;

    @Column(name = "weight_gain_5_gm_1_day")
    private Boolean weightGain5Gm1Day;

    @Column(name = "weight_gain_5_gm_2_day")
    private Boolean weightGain5Gm2Day;

    @Column(name = "weight_gain_5_gm_3_day")
    private Boolean weightGain5Gm3Day;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Date getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getIsMotherCouncelling() {
        return isMotherCouncelling;
    }

    public void setIsMotherCouncelling(Boolean isMotherCouncelling) {
        this.isMotherCouncelling = isMotherCouncelling;
    }

    public Boolean getIsAmoxicillin() {
        return isAmoxicillin;
    }

    public void setIsAmoxicillin(Boolean isAmoxicillin) {
        this.isAmoxicillin = isAmoxicillin;
    }

    public Boolean getIsVitaminA() {
        return isVitaminA;
    }

    public void setIsVitaminA(Boolean isVitaminA) {
        this.isVitaminA = isVitaminA;
    }

    public Boolean getIsAlbendazole() {
        return isAlbendazole;
    }

    public void setIsAlbendazole(Boolean isAlbendazole) {
        this.isAlbendazole = isAlbendazole;
    }

    public Boolean getIsFolicAcid() {
        return isFolicAcid;
    }

    public void setIsFolicAcid(Boolean isFolicAcid) {
        this.isFolicAcid = isFolicAcid;
    }

    public Boolean getIsPotassium() {
        return isPotassium;
    }

    public void setIsPotassium(Boolean isPotassium) {
        this.isPotassium = isPotassium;
    }

    public Boolean getIsMagnesium() {
        return isMagnesium;
    }

    public void setIsMagnesium(Boolean isMagnesium) {
        this.isMagnesium = isMagnesium;
    }

    public Boolean getIsZinc() {
        return isZinc;
    }

    public void setIsZinc(Boolean isZinc) {
        this.isZinc = isZinc;
    }

    public Boolean getIsIron() {
        return isIron;
    }

    public void setIsIron(Boolean isIron) {
        this.isIron = isIron;
    }

    public String getBilateralPittingOedema() {
        return bilateralPittingOedema;
    }

    public void setBilateralPittingOedema(String bilateralPittingOedema) {
        this.bilateralPittingOedema = bilateralPittingOedema;
    }

    public String getFormulaGiven() {
        return formulaGiven;
    }

    public void setFormulaGiven(String formulaGiven) {
        this.formulaGiven = formulaGiven;
    }

    public Boolean getOtherHigherNutrientsGiven() {
        return otherHigherNutrientsGiven;
    }

    public void setOtherHigherNutrientsGiven(Boolean otherHigherNutrientsGiven) {
        this.otherHigherNutrientsGiven = otherHigherNutrientsGiven;
    }

    public Float getMidUpperArmCircumference() {
        return midUpperArmCircumference;
    }

    public void setMidUpperArmCircumference(Float midUpperArmCircumference) {
        this.midUpperArmCircumference = midUpperArmCircumference;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getHigherFacilityReferral() {
        return higherFacilityReferral;
    }

    public void setHigherFacilityReferral(String higherFacilityReferral) {
        this.higherFacilityReferral = higherFacilityReferral;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public Boolean getMultiVitaminSyrup() {
        return multiVitaminSyrup;
    }

    public void setMultiVitaminSyrup(Boolean multiVitaminSyrup) {
        this.multiVitaminSyrup = multiVitaminSyrup;
    }

    public Boolean getIsSugarSolution() {
        return isSugarSolution;
    }

    public void setIsSugarSolution(Boolean isSugarSolution) {
        this.isSugarSolution = isSugarSolution;
    }

    public Boolean getNightStay() {
        return nightStay;
    }

    public void setNightStay(Boolean nightStay) {
        this.nightStay = nightStay;
    }

    public Integer getHigherFacilityReferralPlace() {
        return higherFacilityReferralPlace;
    }

    public void setHigherFacilityReferralPlace(Integer higherFacilityReferralPlace) {
        this.higherFacilityReferralPlace = higherFacilityReferralPlace;
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

    public Boolean getWeightGain5Gm1Day() {
        return weightGain5Gm1Day;
    }

    public void setWeightGain5Gm1Day(Boolean weightGain5Gm1Day) {
        this.weightGain5Gm1Day = weightGain5Gm1Day;
    }

    public Boolean getWeightGain5Gm2Day() {
        return weightGain5Gm2Day;
    }

    public void setWeightGain5Gm2Day(Boolean weightGain5Gm2Day) {
        this.weightGain5Gm2Day = weightGain5Gm2Day;
    }

    public Boolean getWeightGain5Gm3Day() {
        return weightGain5Gm3Day;
    }

    public void setWeightGain5Gm3Day(Boolean weightGain5Gm3Day) {
        this.weightGain5Gm3Day = weightGain5Gm3Day;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public static class Fields {
        private Fields(){
        }
        public static final String ID = "id";
        public static final String ADMISSION_ID = "admissionId";
        public static final String CHILD_ID = "childId";
        public static final String WEIGHT_DATE = "weightDate";
    }
}
