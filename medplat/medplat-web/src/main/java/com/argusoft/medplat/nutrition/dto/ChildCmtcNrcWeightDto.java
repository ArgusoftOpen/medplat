
package com.argusoft.medplat.nutrition.dto;

import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) weight
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcWeightDto {

    private Integer id;
    private Integer childId;
    private Integer admissionId;
    private Date weightDate;
    private Float weight;
    private Boolean isMotherCouncelling;
    private Boolean isAmoxicillin;
    private Boolean isVitaminA;
    private Boolean isSugarSolution;
    private Boolean isAlbendazole;
    private Boolean isFolicAcid;
    private Boolean isPotassium;
    private Boolean isMagnesium;
    private Boolean isZinc;
    private Boolean multiVitaminSyrup;
    private Boolean isIron;
    private String bilateralPittingOedema;
    private String formulaGiven;
    private Boolean otherHigherNutrientsGiven;
    private Float midUpperArmCircumference;
    private Integer height;
    private String higherFacilityReferral;
    private String referralReason;
    private Boolean nightStay;
    private Integer higherFacilityReferralPlace;
    private Boolean kmcProvided;
    private Integer noOfTimesKmcDone;
    private Boolean weightGain5Gm1Day;
    private Boolean weightGain5Gm2Day;
    private Boolean weightGain5Gm3Day;
    private Date createdOn;

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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
