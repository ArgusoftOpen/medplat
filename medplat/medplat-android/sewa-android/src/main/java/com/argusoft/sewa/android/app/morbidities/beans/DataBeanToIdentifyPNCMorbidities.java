/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class DataBeanToIdentifyPNCMorbidities {

    private String healthId = "";
    private String beneficiaryName = "";
    private String beneficiaryType = "";
    private boolean isChildFirstPncDone;
    private boolean isBeneficiaryAlive = true;
    //    Whether baby has watery, loose motions (stools)
    private Boolean babyHasWateryOrLooseMotion;//(PNCHV 28)
    //    Pads are changed in 24 hours
    private Integer noOfPadsChangedIn24Hours;//(PNCHV 12)
    //    Foul smelling discharge
    private Boolean foulSmellingDischarge;//(PNCHV )
    //    Fever
    private Boolean feverForMother;//(PNCHV 16)
    //    Abnormal talk or behaviour or mood changes
    private Boolean abnormalTalkOrBehaviourOrMoodChanges;//(PNCHV 15)
    //    Has headache with visual disturbances?
    private Boolean hasHeadacheWithVisualDisturbances;//(PNCHV 17)
    //    Any difficulty in breastfeeding
    private Boolean anyDifficultyInBreastfeeding;//(PNCHV 18)
    //    Baby's hands and feet cold to touch
    private Boolean areBabyHandsAndFeetColdToTouch;//(PNCHV 31)
    //    Temperature
    private Float temperature;//(PNCHV 46)
    //    (Rate of respiration
    private Integer rateOfRespiration;//(PNCHV 43)
    //    How is the skin of the baby
    private List<String> howIsTheSkinOfTheBaby;//(PNCHV 39)
    //    Is there bleeding from any part of the body
    private List<String> isThereBleedingFromAnyPartOfTheBody;//(PNCHV 34)(MS)
    //    Skin pustules
    private Boolean skinPustules;//(PNCHV 40)
    //    Umbilicus
    private String umbilicus;//(PNCHV 44)
    //    When did baby cry?
    private String whenDidBabyCry;//(POV 16)
    //    Does mother have any of following problems
    private List<String> doesMotherHaveAnyOfFollowingProblems;//(PNCHV 19) Multi select multi value is there
    //   Age of child
    private Long ageOfChild;//(POV )
    //  Weight of child
    private Float weightOfChild;
    //  Baby's consciousness in child related ques(How's the baby's consciousness)
    private String howsTheBabyConsciousness;//(PNC 29)(CBDS)
    //  consciousness
    private String consciousness;//(PNC 49)(CBDS)
    private String wasBabyFedLessThanUsual;//(PNC 24)(RB but not boolean)
    private String howIsBabySuckling;//(PNC 25)(CBDS)
    private String howIsBabyCry;//(PNC 23)(CBDS)
    private String cry;//(PNC 50)(CBDS)  
    private String abdomen;//(PNC 45)(CBDS)
    private Boolean chestIndrawing;//(PNC 42)(RB bool)
    private Boolean whetherBabyVomits;//(PNC 27)(RB bool)
    private List<String> anySkinProblems;//(PNC 33)(MS)
    private List<String> doesMotherHaveAnyOfFollowingOnBREAST;//(PNCHV 20) Multi select multi value is there
    private Long deliveryDate;
    private Long lmpDate;
    //Newly added
    private String doesBabyChinTouchTheBreast;//(PNC 56)
    private String isMouthWidelyOpen;//(PNC 57)
    private String isLowerLipTurnedOutward;//(PNC 58)
    private String isAreolaMoreAboveTheMouthAndLessBeneathTheMouth;//(PNC 59)
    private String howAreBabyEyes;//Phase - II(PNCVF 118)
    private String whetherBabyLimbsAndNeckMoreLimpThanBefore;//Phase - II(PNCVF 117)

    // added in phase 3
    private boolean whetherBabyLimbsNeckLastState = false; // phase- II ..required to check sepsis condition
    private boolean anyDifficultyInBreastfeedingLastStatus = false;
    private boolean whetherBabyFedLessLastStatus = false;
    private boolean howIsBabySucklingLastStatus = false;
    private boolean howIsBabyCryLastStatus = false;
    //Weight of child as recorded during POV
    private Float newBornWeight;

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public Float getNewBornWeight() {
        return newBornWeight;
    }

    public void setNewBornWeight(Float newBornWeight) {
        this.newBornWeight = newBornWeight;
    }

    public boolean getWhetherBabyLimbsNeckLastState() {
        return whetherBabyLimbsNeckLastState;
    }

    public void setWhetherBabyLimbsNeckLastState(boolean whetherBabyLimbsNeckLastState) {
        this.whetherBabyLimbsNeckLastState = whetherBabyLimbsNeckLastState;
    }

    public boolean getAnyDifficultyInBreastfeedingLastStatus() {
        return anyDifficultyInBreastfeedingLastStatus;
    }

    public void setAnyDifficultyInBreastfeedingLastStatus(boolean anyDifficultyInBreastfeedingLastStatus) {
        this.anyDifficultyInBreastfeedingLastStatus = anyDifficultyInBreastfeedingLastStatus;
    }

    public boolean getWhetherBabyFedLessLastStatus() {
        return whetherBabyFedLessLastStatus;
    }

    public void setWhetherBabyFedLessLastStatus(boolean whetherBabyFedLessLastStatus) {
        this.whetherBabyFedLessLastStatus = whetherBabyFedLessLastStatus;
    }

    public boolean getHowIsBabySucklingLastStatus() {
        return howIsBabySucklingLastStatus;
    }

    public void setHowIsBabySucklingLastStatus(boolean sepsisCondi4) {
        this.howIsBabySucklingLastStatus = sepsisCondi4;
    }

    public boolean getHowIsBabyCryLastStatus() {
        return howIsBabyCryLastStatus;
    }

    public void setHowIsBabyCryLastStatus(boolean howIsBabyCryLastStatus) {
        this.howIsBabyCryLastStatus = howIsBabyCryLastStatus;
    }

    public String getWhetherBabyLimbsAndNeckMoreLimpThanBefore() {
        return whetherBabyLimbsAndNeckMoreLimpThanBefore;
    }

    public void setWhetherBabyLimbsAndNeckMoreLimpThanBefore(String whetherBabyLimbsAndNeckMoreLimpThanBefore) {
        this.whetherBabyLimbsAndNeckMoreLimpThanBefore = whetherBabyLimbsAndNeckMoreLimpThanBefore;
    }

    public String getHowAreBabyEyes() {
        return howAreBabyEyes;
    }

    public void setHowAreBabyEyes(String howAreBabyEyes) {
        this.howAreBabyEyes = howAreBabyEyes;
    }

    public String getDoesBabyChinTouchTheBreast() {
        return doesBabyChinTouchTheBreast;
    }

    public void setDoesBabyChinTouchTheBreast(String doesBabyChinTouchTheBreast) {
        this.doesBabyChinTouchTheBreast = doesBabyChinTouchTheBreast;
    }

    public String getIsMouthWidelyOpen() {
        return isMouthWidelyOpen;
    }

    public void setIsMouthWidelyOpen(String isMouthWidelyOpen) {
        this.isMouthWidelyOpen = isMouthWidelyOpen;
    }

    public String getIsLowerLipTurnedOutward() {
        return isLowerLipTurnedOutward;
    }

    public void setIsLowerLipTurnedOutward(String isLowerLipTurnedOutward) {
        this.isLowerLipTurnedOutward = isLowerLipTurnedOutward;
    }

    public String getIsAreolaMoreAboveTheMouthAndLessBeneathTheMouth() {
        return isAreolaMoreAboveTheMouthAndLessBeneathTheMouth;
    }

    public void setIsAreolaMoreAboveTheMouthAndLessBeneathTheMouth(String isAreolaMoreAboveTheMouthAndLessBeneathTheMouth) {
        this.isAreolaMoreAboveTheMouthAndLessBeneathTheMouth = isAreolaMoreAboveTheMouthAndLessBeneathTheMouth;
    }

    public Long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Long lmpDate) {
        this.lmpDate = lmpDate;
    }

    public boolean getIsChildFirstPncDone() {
        return isChildFirstPncDone;
    }

    public void setIsChildFirstPncDone(boolean isChildFirstPncDone) {
        this.isChildFirstPncDone = isChildFirstPncDone;
    }

    public List<String> getDoesMotherHaveAnyOfFollowingOnBREAST() {
        return doesMotherHaveAnyOfFollowingOnBREAST;
    }

    public void setDoesMotherHaveAnyOfFollowingOnBREAST(List<String> doesMotherHaveAnyOfFollowingOnBREAST) {
        this.doesMotherHaveAnyOfFollowingOnBREAST = doesMotherHaveAnyOfFollowingOnBREAST;
    }

    public List<String> getAnySkinProblems() {
        return anySkinProblems;
    }

    public void setAnySkinProblems(List<String> anySkinProblems) {
        this.anySkinProblems = anySkinProblems;
    }

    public boolean getIsBeneficiaryAlive() {
        return isBeneficiaryAlive;
    }

    public void setIsBeneficiaryAlive(boolean isBeneficiaryAlive) {
        this.isBeneficiaryAlive = isBeneficiaryAlive;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public Boolean getBabyHasWateryOrLooseMotion() {
        return babyHasWateryOrLooseMotion;
    }

    public void setBabyHasWateryOrLooseMotion(Boolean babyHasWateryOrLooseMotion) {
        this.babyHasWateryOrLooseMotion = babyHasWateryOrLooseMotion;
    }

    public List<String> getHowIsTheSkinOfTheBaby() {
        return howIsTheSkinOfTheBaby;
    }

    public void setHowIsTheSkinOfTheBaby(List<String> howIsTheSkinOfTheBaby) {
        this.howIsTheSkinOfTheBaby = howIsTheSkinOfTheBaby;
    }

    public List<String> getIsThereBleedingFromAnyPartOfTheBody() {
        return isThereBleedingFromAnyPartOfTheBody;
    }

    public void setIsThereBleedingFromAnyPartOfTheBody(List<String> isThereBleedingFromAnyPartOfTheBody) {
        this.isThereBleedingFromAnyPartOfTheBody = isThereBleedingFromAnyPartOfTheBody;
    }

    public Integer getNoOfPadsChangedIn24Hours() {
        return noOfPadsChangedIn24Hours;
    }

    public void setNoOfPadsChangedIn24Hours(Integer noOfPadsChangedIn24Hours) {
        this.noOfPadsChangedIn24Hours = noOfPadsChangedIn24Hours;
    }

    public Boolean getFoulSmellingDischarge() {
        return foulSmellingDischarge;
    }

    public void setFoulSmellingDischarge(Boolean foulSmellingDischarge) {
        this.foulSmellingDischarge = foulSmellingDischarge;
    }

    public Boolean getFeverForMother() {
        return feverForMother;
    }

    public void setFeverForMother(Boolean feverForMother) {
        this.feverForMother = feverForMother;
    }

    public Boolean getAbnormalTalkOrBehaviourOrMoodChanges() {
        return abnormalTalkOrBehaviourOrMoodChanges;
    }

    public void setAbnormalTalkOrBehaviourOrMoodChanges(Boolean abnormalTalkOrBehaviourOrMoodChanges) {
        this.abnormalTalkOrBehaviourOrMoodChanges = abnormalTalkOrBehaviourOrMoodChanges;
    }

    public Boolean getHasHeadacheWithVisualDisturbances() {
        return hasHeadacheWithVisualDisturbances;
    }

    public void setHasHeadacheWithVisualDisturbances(Boolean hasHeadacheWithVisualDisturbances) {
        this.hasHeadacheWithVisualDisturbances = hasHeadacheWithVisualDisturbances;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperatur) {
        this.temperature = temperatur;
    }

    public Integer getRateOfRespiration() {
        return rateOfRespiration;
    }

    public void setRateOfRespiration(Integer rateOfRespiration) {
        this.rateOfRespiration = rateOfRespiration;
    }

    public Boolean getSkinPustules() {
        return skinPustules;
    }

    public void setSkinPustules(Boolean skinPustules) {
        this.skinPustules = skinPustules;
    }

    public String getUmbilicus() {
        return umbilicus;
    }

    public void setUmbilicus(String umbilicus) {
        this.umbilicus = umbilicus;
    }

    public String getWhenDidBabyCry() {
        return whenDidBabyCry;
    }

    public void setWhenDidBabyCry(String whenDidBabyCry) {
        this.whenDidBabyCry = whenDidBabyCry;
    }

    public Boolean getAnyDifficultyInBreastfeeding() {
        return anyDifficultyInBreastfeeding;
    }

    public void setAnyDifficultyInBreastfeeding(Boolean anyDifficultyInBreastfeeding) {
        this.anyDifficultyInBreastfeeding = anyDifficultyInBreastfeeding;
    }

    public Long getAgeOfChild() {
        return ageOfChild;
    }

    public void setAgeOfChild(Long ageOfChild) {
        this.ageOfChild = ageOfChild;
    }

    public Float getWeightOfChild() {
        return weightOfChild;
    }

    public void setWeightOfChild(Float weightOfChild) {
        this.weightOfChild = weightOfChild;
    }

    public String getHowsTheBabyConsciousness() {
        return howsTheBabyConsciousness;
    }

    public void setHowsTheBabyConsciousness(String howsTheBabyConsciousness) {
        this.howsTheBabyConsciousness = howsTheBabyConsciousness;
    }

    public String getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(String consciousness) {
        this.consciousness = consciousness;
    }

    public String getWasBabyFedLessThanUsual() {
        return wasBabyFedLessThanUsual;
    }

    public void setWasBabyFedLessThanUsual(String wasBabyFedLessThanUsual) {
        this.wasBabyFedLessThanUsual = wasBabyFedLessThanUsual;
    }

    public String getHowIsBabySuckling() {
        return howIsBabySuckling;
    }

    public void setHowIsBabySuckling(String howIsBabySuckling) {
        this.howIsBabySuckling = howIsBabySuckling;
    }

    public String getHowIsBabyCry() {
        return howIsBabyCry;
    }

    public void setHowIsBabyCry(String howIsBabyCry) {
        this.howIsBabyCry = howIsBabyCry;
    }

    public String getCry() {
        return cry;
    }

    public void setCry(String cry) {
        this.cry = cry;
    }

    public Boolean getAreBabyHandsAndFeetColdToTouch() {
        return areBabyHandsAndFeetColdToTouch;
    }

    public void setAreBabyHandsAndFeetColdToTouch(Boolean areBabyHandsAndFeetColdToTouch) {
        this.areBabyHandsAndFeetColdToTouch = areBabyHandsAndFeetColdToTouch;
    }

    public String getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(String abdomen) {
        this.abdomen = abdomen;
    }

    public Boolean getChestIndrawing() {
        return chestIndrawing;
    }

    public void setChestIndrawing(Boolean chestIndrawing) {
        this.chestIndrawing = chestIndrawing;
    }

    public Boolean getWhetherBabyVomits() {
        return whetherBabyVomits;
    }

    public void setWhetherBabyVomits(Boolean whetherBabyVomits) {
        this.whetherBabyVomits = whetherBabyVomits;
    }

    public List<String> getDoesMotherHaveAnyOfFollowingProblems() {
        return doesMotherHaveAnyOfFollowingProblems;
    }

    public void setDoesMotherHaveAnyOfFollowingProblems(List<String> doesMotherHaveAnyOfFollowingProblems) {
        this.doesMotherHaveAnyOfFollowingProblems = doesMotherHaveAnyOfFollowingProblems;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataBeanToIdentifyPNCMorbidities{" +
                "healthId='" + healthId + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", beneficiaryType='" + beneficiaryType + '\'' +
                ", isChildFirstPncDone=" + isChildFirstPncDone +
                ", isBeneficiaryAlive=" + isBeneficiaryAlive +
                ", babyHasWateryOrLooseMotion=" + babyHasWateryOrLooseMotion +
                ", noOfPadsChangedIn24Hours=" + noOfPadsChangedIn24Hours +
                ", foulSmellingDischarge=" + foulSmellingDischarge +
                ", feverForMother=" + feverForMother +
                ", abnormalTalkOrBehaviourOrMoodChanges=" + abnormalTalkOrBehaviourOrMoodChanges +
                ", hasHeadacheWithVisualDisturbances=" + hasHeadacheWithVisualDisturbances +
                ", anyDifficultyInBreastfeeding=" + anyDifficultyInBreastfeeding +
                ", areBabyHandsAndFeetColdToTouch=" + areBabyHandsAndFeetColdToTouch +
                ", temperature=" + temperature +
                ", rateOfRespiration=" + rateOfRespiration +
                ", howIsTheSkinOfTheBaby=" + howIsTheSkinOfTheBaby +
                ", isThereBleedingFromAnyPartOfTheBody=" + isThereBleedingFromAnyPartOfTheBody +
                ", skinPustules=" + skinPustules +
                ", umbilicus='" + umbilicus + '\'' +
                ", whenDidBabyCry='" + whenDidBabyCry + '\'' +
                ", doesMotherHaveAnyOfFollowingProblems=" + doesMotherHaveAnyOfFollowingProblems +
                ", ageOfChild=" + ageOfChild +
                ", weightOfChild=" + weightOfChild +
                ", howsTheBabyConsciousness='" + howsTheBabyConsciousness + '\'' +
                ", consciousness='" + consciousness + '\'' +
                ", wasBabyFedLessThanUsual='" + wasBabyFedLessThanUsual + '\'' +
                ", howIsBabySuckling='" + howIsBabySuckling + '\'' +
                ", howIsBabyCry='" + howIsBabyCry + '\'' +
                ", cry='" + cry + '\'' +
                ", abdomen='" + abdomen + '\'' +
                ", chestIndrawing=" + chestIndrawing +
                ", whetherBabyVomits=" + whetherBabyVomits +
                ", anySkinProblems=" + anySkinProblems +
                ", doesMotherHaveAnyOfFollowingOnBREAST=" + doesMotherHaveAnyOfFollowingOnBREAST +
                ", deliveryDate=" + deliveryDate +
                ", lmpDate=" + lmpDate +
                ", doesBabyChinTouchTheBreast='" + doesBabyChinTouchTheBreast + '\'' +
                ", isMouthWidelyOpen='" + isMouthWidelyOpen + '\'' +
                ", isLowerLipTurnedOutward='" + isLowerLipTurnedOutward + '\'' +
                ", isAreolaMoreAboveTheMouthAndLessBeneathTheMouth='" + isAreolaMoreAboveTheMouthAndLessBeneathTheMouth + '\'' +
                ", howAreBabyEyes='" + howAreBabyEyes + '\'' +
                ", whetherBabyLimbsAndNeckMoreLimpThanBefore='" + whetherBabyLimbsAndNeckMoreLimpThanBefore + '\'' +
                ", whetherBabyLimbsNeckLastState=" + whetherBabyLimbsNeckLastState +
                ", anyDifficultyInBreastfeedingLastStatus=" + anyDifficultyInBreastfeedingLastStatus +
                ", whetherBabyFedLessLastStatus=" + whetherBabyFedLessLastStatus +
                ", howIsBabySucklingLastStatus=" + howIsBabySucklingLastStatus +
                ", howIsBabyCryLastStatus=" + howIsBabyCryLastStatus +
                ", newBornWeight=" + newBornWeight +
                '}';
    }
}
