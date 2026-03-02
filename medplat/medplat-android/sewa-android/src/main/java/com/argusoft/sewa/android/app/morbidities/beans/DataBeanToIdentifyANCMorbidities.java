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
public class DataBeanToIdentifyANCMorbidities {

    private String beneficiaryName;
    private String beneficiaryType;
    //Static constant which is used in the identification    
    private boolean isFirstAncHomeVisitDone;
    private Integer systolicBP;//(Mamtaday 4)
    private Integer diastolicBP;//(Mamtaday 4)
    private Boolean headache;//(ANC 48)
    private Boolean visionDisturbance;//(ANC 49)
    private Integer urineProtein;//(Mamtaday 7)
    private Boolean vaginalBleedingSinceLastVisit;//(ANC 59)
    private Boolean severeAbdominalPain;//(ANC 53)
    private Boolean jaundice;//(ANC 56)
    private Boolean severeJointPain;//(ANC 57)
    private Boolean leakingPerVaginally;//(ANC 62)    
    private Integer haemoglobin;//(Mamtaday 9)
    //Confirm what is this
    private Integer gestationalWeek;
    //This field comes from registration form
    private Long age;//(EDP 6)
    private Integer gravida;//(EDP 14)
    private Boolean fever;//(ANC 54)
    private Boolean chillsOrRigours;//(ANC 55)
    private Boolean burningUrination;//(ANC 60)
    private Boolean nightBlindness;//(ANC 52)
    private Boolean doYouGetTiredEasily;//(ANC 63)
    private Boolean conjunctivaAndPalmsPale;//(ANC 72)
    private Boolean shortOfBreathDuringRoutingHouseholdWork;//(ANC 64)
    private Boolean vomiting;//(ANC 58)
    private String vaginalDischarge;//(ANC 61)
    //This is key value field 
    private String presenceOfCough;//(ANC 51)
    private String feelingOfFoetalMovement;//(ANC 65)
    private String breastExamination;//(ANC 73)
    private String maritalStatus;//(EDP 11)
    private List<String> complicationPresentDuringPreviousPregnancy;//(EDP 19)
    private String perAbdomenExam;//(Mamta day 8)
    //Newly added
    private Boolean hasConvulsion;//(ANC 50)
    //Phase 3
    private Boolean swellingOfFaceHandsOrFeet;//

    public Boolean isSwellingOfFaceHandsOrFeet() {
        return swellingOfFaceHandsOrFeet;
    }

    public void setSwellingOfFaceHandsOrFeet(Boolean swellingOfFaceHandsOrFeet) {
        this.swellingOfFaceHandsOrFeet = swellingOfFaceHandsOrFeet;
    }

    public Boolean getHasConvulsion() {
        return hasConvulsion;
    }

    public void setHasConvulsion(Boolean hasConvulsion) {
        this.hasConvulsion = hasConvulsion;
    }

    public boolean isIsFirstAncHomeVisitDone() {
        return isFirstAncHomeVisitDone;
    }

    public void setIsFirstAncHomeVisitDone(boolean isFirstAncHomeVisitDone) {
        this.isFirstAncHomeVisitDone = isFirstAncHomeVisitDone;
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

    public Integer getSystolicBP() {
        return systolicBP;
    }

    public void setSystolicBP(Integer systolicBP) {
        this.systolicBP = systolicBP;
    }

    public Integer getDiastolicBP() {
        return diastolicBP;
    }

    public void setDiastolicBP(Integer diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    public Integer getUrineProtein() {
        return urineProtein;
    }

    public void setUrineProtein(Integer urineProtein) {
        this.urineProtein = urineProtein;
    }

    public Integer getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Integer haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public Integer getGestationalWeek() {
        return gestationalWeek;
    }

    public void setGestationalWeek(Integer gestationalWeek) {
        this.gestationalWeek = gestationalWeek;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Integer getGravida() {
        return gravida;
    }

    public void setGravida(Integer gravida) {
        this.gravida = gravida;
    }

    public Boolean isHeadache() {
        return headache;
    }

    public void setHeadache(Boolean headache) {
        this.headache = headache;
    }

    public Boolean isVisionDisturbance() {
        return visionDisturbance;
    }

    public void setVisionDisturbance(Boolean visionDisturbance) {
        this.visionDisturbance = visionDisturbance;
    }

    public Boolean isVaginalBleedingSinceLastVisit() {
        return vaginalBleedingSinceLastVisit;
    }

    public void setVaginalBleedingSinceLastVisit(Boolean vaginalBleedingSinceLastVisit) {
        this.vaginalBleedingSinceLastVisit = vaginalBleedingSinceLastVisit;
    }

    public Boolean isSevereAbdominalPain() {
        return severeAbdominalPain;
    }

    public void setSevereAbdominalPain(Boolean severeAbdominalPain) {
        this.severeAbdominalPain = severeAbdominalPain;
    }

    public Boolean isJaundice() {
        return jaundice;
    }

    public void setJaundice(Boolean jaundice) {
        this.jaundice = jaundice;
    }

    public Boolean isSevereJointPain() {
        return severeJointPain;
    }

    public void setSevereJointPain(Boolean severeJointPain) {
        this.severeJointPain = severeJointPain;
    }

    public Boolean isLeakingPerVaginally() {
        return leakingPerVaginally;
    }

    public void setLeakingPerVaginally(Boolean leakingPerVaginally) {
        this.leakingPerVaginally = leakingPerVaginally;
    }

    public String getPerAbdomenExam() {
        return perAbdomenExam;
    }

    public void setPerAbdomenExam(String perAbdomenExam) {
        this.perAbdomenExam = perAbdomenExam;
    }

    public String getFeelingOfFoetalMovement() {
        return feelingOfFoetalMovement;
    }

    public void setFeelingOfFoetalMovement(String feelingOfFoetalMovement) {
        this.feelingOfFoetalMovement = feelingOfFoetalMovement;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<String> getComplicationPresentDuringPreviousPregnancy() {
        return complicationPresentDuringPreviousPregnancy;
    }

    public void setComplicationPresentDuringPreviousPregnancy(List<String> complicationPresentDuringPreviousPregnancy) {
        this.complicationPresentDuringPreviousPregnancy = complicationPresentDuringPreviousPregnancy;
    }

    public Boolean isFever() {
        return fever;
    }

    public void setFever(Boolean fever) {
        this.fever = fever;
    }

    public Boolean isChillsOrRigours() {
        return chillsOrRigours;
    }

    public void setChillsOrRigours(Boolean chillsOrRigours) {
        this.chillsOrRigours = chillsOrRigours;
    }

    public Boolean isBurningUrination() {
        return burningUrination;
    }

    public void setBurningUrination(Boolean burningUrination) {
        this.burningUrination = burningUrination;
    }

    public String getPresenceOfCough() {
        return presenceOfCough;
    }

    public void setPresenceOfCough(String presenceOfCough) {
        this.presenceOfCough = presenceOfCough;
    }

    public String getVaginalDischarge() {
        return vaginalDischarge;
    }

    public void setVaginalDischarge(String vaginalDischarge) {
        this.vaginalDischarge = vaginalDischarge;
    }

    public Boolean isNightBlindness() {
        return nightBlindness;
    }

    public void setNightBlindness(Boolean nightBlindness) {
        this.nightBlindness = nightBlindness;
    }

    public Boolean isDoYouGetTiredEasily() {
        return doYouGetTiredEasily;
    }

    public void setDoYouGetTiredEasily(Boolean doYouGetTiredEasily) {
        this.doYouGetTiredEasily = doYouGetTiredEasily;
    }

    public Boolean isConjunctivaAndPalmsPale() {
        return conjunctivaAndPalmsPale;
    }

    public void setConjunctivaAndPalmsPale(Boolean conjunctivaAndPalmsPale) {
        this.conjunctivaAndPalmsPale = conjunctivaAndPalmsPale;
    }

    public Boolean isShortOfBreathDuringRoutingHouseholdWork() {
        return shortOfBreathDuringRoutingHouseholdWork;
    }

    public void setShortOfBreathDuringRoutingHouseholdWork(Boolean shortOfBreathDuringRoutingHouseholdWork) {
        this.shortOfBreathDuringRoutingHouseholdWork = shortOfBreathDuringRoutingHouseholdWork;
    }

    public Boolean isVomiting() {
        return vomiting;
    }

    public void setVomiting(Boolean vomiting) {
        this.vomiting = vomiting;
    }

    public String getBreastExamination() {
        return breastExamination;
    }

    public void setBreastExamination(String breastExamination) {
        this.breastExamination = breastExamination;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataBeanToIdentifyANCMorbidities{" +
                "beneficiaryName='" + beneficiaryName + '\'' +
                ", beneficiaryType='" + beneficiaryType + '\'' +
                ", isFirstAncHomeVisitDone=" + isFirstAncHomeVisitDone +
                ", systolicBP=" + systolicBP +
                ", diastolicBP=" + diastolicBP +
                ", headache=" + headache +
                ", visionDisturbance=" + visionDisturbance +
                ", urineProtein=" + urineProtein +
                ", vaginalBleedingSinceLastVisit=" + vaginalBleedingSinceLastVisit +
                ", severeAbdominalPain=" + severeAbdominalPain +
                ", jaundice=" + jaundice +
                ", severeJointPain=" + severeJointPain +
                ", leakingPerVaginally=" + leakingPerVaginally +
                ", haemoglobin=" + haemoglobin +
                ", gestationalWeek=" + gestationalWeek +
                ", age=" + age +
                ", gravida=" + gravida +
                ", fever=" + fever +
                ", chillsOrRigours=" + chillsOrRigours +
                ", burningUrination=" + burningUrination +
                ", nightBlindness=" + nightBlindness +
                ", doYouGetTiredEasily=" + doYouGetTiredEasily +
                ", conjunctivaAndPalmsPale=" + conjunctivaAndPalmsPale +
                ", shortOfBreathDuringRoutingHouseholdWork=" + shortOfBreathDuringRoutingHouseholdWork +
                ", vomiting=" + vomiting +
                ", vaginalDischarge='" + vaginalDischarge + '\'' +
                ", presenceOfCough='" + presenceOfCough + '\'' +
                ", feelingOfFoetalMovement='" + feelingOfFoetalMovement + '\'' +
                ", breastExamination='" + breastExamination + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", complicationPresentDuringPreviousPregnancy=" + complicationPresentDuringPreviousPregnancy +
                ", perAbdomenExam='" + perAbdomenExam + '\'' +
                ", hasConvulsion=" + hasConvulsion +
                ", swellingOfFaceHandsOrFeet=" + swellingOfFaceHandsOrFeet +
                '}';
    }
}
