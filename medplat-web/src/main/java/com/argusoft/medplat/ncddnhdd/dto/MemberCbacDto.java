/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.ncddnhdd.model.MemberCbacDetail;
import com.argusoft.medplat.ncddnhdd.enums.ReferralPlace;

import java.util.Date;

/**
 *
 * @author vaishali
 */
public class MemberCbacDto {

    private Integer id;

    private Integer memberId;

    private String smokeOrConsumeGutka;

    private String waist;

    private Boolean consumeAlcoholDaily;

    private String physicalActivity150Min;

    private Boolean bpDiabetesHeartHistory;

    private Boolean shortnessOfBreath;

    private Boolean fitsHistory;

    private Boolean twoWeeksCoughing;

    private Boolean mouthOpeningDifficulty;

    private Boolean bloodInSputum;

    private Boolean twoWeeksUlcersInMouth;

    private Boolean twoWeeksFever;

    private Boolean changeInToneOfVoice;

    private Boolean lossOfWeight;

    private Boolean patchOnSkin;

    private Boolean nightSweats;

    private Boolean takingAntiTbDrugs;

    private Boolean difficultyHoldingObjects;

    private Boolean sensationLossPalm;

    private Boolean familyMemberSufferingFromTb;

    private Boolean historyOfTb;

    private Boolean lumpInBreast;

    private Boolean bleedingAfterMenopause;

    private Boolean nippleBloodStainedDischarge;

    private Boolean bleedingAfterIntercourse;

    private Boolean changeInSizeOfBreast;

    private Boolean foulVaginalDischarge;

    private Boolean bleedingBetweenPeriods;

    private String occupationalExposure;

    private String latitude;

    private String longitude;

    private Date mobileStartDate;

    private Date mobileEndDate;

    private Integer score;

    private Integer ageAtMenarche;

    private Boolean menopauseArrived;

    private Integer durationOfMenoapuse;

    private Boolean pregnant;

    private Boolean lactating;

    private Boolean regularPeriods;

    private Date lmp;

    private String bleeding;

    private String associatedWith;

    private String remarks;

    private Boolean diagnosedForHypertension;

    private Boolean underTreatementForHypertension;

    private Boolean diagnosedForDiabetes;

    private Boolean underTreatementForDiabetes;

    private Boolean diagnosedForHeartDiseases;

    private Boolean underTreatementForHeartDiseases;

    private Boolean diagnosedForStroke;

    private Boolean underTreatementForStroke;

    private Boolean diagnosedForKidneyFailure;

    private Boolean underTreatementForKidneyFailure;

    private Boolean diagnosedForNonHealingWound;

    private Boolean underTreatementForNonHealingWound;

    private Boolean diagnosedForCOPD;

    private Boolean underTreatementForCOPD;

    private Boolean diagnosedForAsthama;

    private Boolean underTreatementForAsthama;

    private Boolean diagnosedForOralCancer;

    private Boolean underTreatementForOralCancer;

    private Boolean diagnosedForBreastCancer;

    private Boolean underTreatementForBreastCancer;

    private Boolean diagnosedForCervicalCancer;

    private Boolean underTreatementForCervicalCancer;

    private Integer height;

    private Float weight;

    private Float bmi;

    private MemberCbacDetail.DoneBy doneBy;

    private Date createdOn;

    private Integer createdBy;

    private Integer referralId;
    
    private ReferralPlace refferralFrom;
    
    
    private ReferralPlace refTo;
    private ReferralPlace refFrom;

    public ReferralPlace getRefTo() {
        return refTo;
    }

    public void setRefTo(ReferralPlace refTo) {
        this.refTo = refTo;
    }

    public ReferralPlace getRefFrom() {
        return refFrom;
    }

    public void setRefFrom(ReferralPlace refFrom) {
        this.refFrom = refFrom;
    }
    


    public ReferralPlace getRefferralFrom() {
        return refferralFrom;
    }
//
//    public void setRefferalPlace(Integer referralFrom) {
//        this.referralPlace = referralFrom;
//    }
//    
//    public Integer getRefferalPlace() {
//        return referralId;
//    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

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

    public String getSmokeOrConsumeGutka() {
        return smokeOrConsumeGutka;
    }

    public void setSmokeOrConsumeGutka(String smokeOrConsumeGutka) {
        this.smokeOrConsumeGutka = smokeOrConsumeGutka;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public Boolean getConsumeAlcoholDaily() {
        return consumeAlcoholDaily;
    }

    public void setConsumeAlcoholDaily(Boolean consumeAlcoholDaily) {
        this.consumeAlcoholDaily = consumeAlcoholDaily;
    }

    public String getPhysicalActivity150Min() {
        return physicalActivity150Min;
    }

    public void setPhysicalActivity150Min(String physicalActivity150Min) {
        this.physicalActivity150Min = physicalActivity150Min;
    }

    public Boolean getBpDiabetesHeartHistory() {
        return bpDiabetesHeartHistory;
    }

    public void setBpDiabetesHeartHistory(Boolean bpDiabetesHeartHistory) {
        this.bpDiabetesHeartHistory = bpDiabetesHeartHistory;
    }

    public Boolean getShortnessOfBreath() {
        return shortnessOfBreath;
    }

    public void setShortnessOfBreath(Boolean shortnessOfBreath) {
        this.shortnessOfBreath = shortnessOfBreath;
    }

    public Boolean getFitsHistory() {
        return fitsHistory;
    }

    public void setFitsHistory(Boolean fitsHistory) {
        this.fitsHistory = fitsHistory;
    }

    public Boolean getTwoWeeksCoughing() {
        return twoWeeksCoughing;
    }

    public void setTwoWeeksCoughing(Boolean twoWeeksCoughing) {
        this.twoWeeksCoughing = twoWeeksCoughing;
    }

    public Boolean getMouthOpeningDifficulty() {
        return mouthOpeningDifficulty;
    }

    public void setMouthOpeningDifficulty(Boolean mouthOpeningDifficulty) {
        this.mouthOpeningDifficulty = mouthOpeningDifficulty;
    }

    public Boolean getBloodInSputum() {
        return bloodInSputum;
    }

    public void setBloodInSputum(Boolean bloodInSputum) {
        this.bloodInSputum = bloodInSputum;
    }

    public Boolean getTwoWeeksUlcersInMouth() {
        return twoWeeksUlcersInMouth;
    }

    public void setTwoWeeksUlcersInMouth(Boolean twoWeeksUlcersInMouth) {
        this.twoWeeksUlcersInMouth = twoWeeksUlcersInMouth;
    }

    public Boolean getTwoWeeksFever() {
        return twoWeeksFever;
    }

    public void setTwoWeeksFever(Boolean twoWeeksFever) {
        this.twoWeeksFever = twoWeeksFever;
    }

    public Boolean getChangeInToneOfVoice() {
        return changeInToneOfVoice;
    }

    public void setChangeInToneOfVoice(Boolean changeInToneOfVoice) {
        this.changeInToneOfVoice = changeInToneOfVoice;
    }

    public Boolean getLossOfWeight() {
        return lossOfWeight;
    }

    public void setLossOfWeight(Boolean lossOfWeight) {
        this.lossOfWeight = lossOfWeight;
    }

    public Boolean getPatchOnSkin() {
        return patchOnSkin;
    }

    public void setPatchOnSkin(Boolean patchOnSkin) {
        this.patchOnSkin = patchOnSkin;
    }

    public Boolean getNightSweats() {
        return nightSweats;
    }

    public void setNightSweats(Boolean nightSweats) {
        this.nightSweats = nightSweats;
    }

    public Boolean getTakingAntiTbDrugs() {
        return takingAntiTbDrugs;
    }

    public void setTakingAntiTbDrugs(Boolean takingAntiTbDrugs) {
        this.takingAntiTbDrugs = takingAntiTbDrugs;
    }

    public Boolean getDifficultyHoldingObjects() {
        return difficultyHoldingObjects;
    }

    public void setDifficultyHoldingObjects(Boolean difficultyHoldingObjects) {
        this.difficultyHoldingObjects = difficultyHoldingObjects;
    }

    public Boolean getSensationLossPalm() {
        return sensationLossPalm;
    }

    public void setSensationLossPalm(Boolean sensationLossPalm) {
        this.sensationLossPalm = sensationLossPalm;
    }

    public Boolean getFamilyMemberSufferingFromTb() {
        return familyMemberSufferingFromTb;
    }

    public void setFamilyMemberSufferingFromTb(Boolean familyMemberSufferingFromTb) {
        this.familyMemberSufferingFromTb = familyMemberSufferingFromTb;
    }

    public Boolean getHistoryOfTb() {
        return historyOfTb;
    }

    public void setHistoryOfTb(Boolean historyOfTb) {
        this.historyOfTb = historyOfTb;
    }

    public Boolean getLumpInBreast() {
        return lumpInBreast;
    }

    public void setLumpInBreast(Boolean lumpInBreast) {
        this.lumpInBreast = lumpInBreast;
    }

    public Boolean getBleedingAfterMenopause() {
        return bleedingAfterMenopause;
    }

    public void setBleedingAfterMenopause(Boolean bleedingAfterMenopause) {
        this.bleedingAfterMenopause = bleedingAfterMenopause;
    }

    public Boolean getNippleBloodStainedDischarge() {
        return nippleBloodStainedDischarge;
    }

    public void setNippleBloodStainedDischarge(Boolean nippleBloodStainedDischarge) {
        this.nippleBloodStainedDischarge = nippleBloodStainedDischarge;
    }

    public Boolean getBleedingAfterIntercourse() {
        return bleedingAfterIntercourse;
    }

    public void setBleedingAfterIntercourse(Boolean bleedingAfterIntercourse) {
        this.bleedingAfterIntercourse = bleedingAfterIntercourse;
    }

    public Boolean getChangeInSizeOfBreast() {
        return changeInSizeOfBreast;
    }

    public void setChangeInSizeOfBreast(Boolean changeInSizeOfBreast) {
        this.changeInSizeOfBreast = changeInSizeOfBreast;
    }

    public Boolean getFoulVaginalDischarge() {
        return foulVaginalDischarge;
    }

    public void setFoulVaginalDischarge(Boolean foulVaginalDischarge) {
        this.foulVaginalDischarge = foulVaginalDischarge;
    }

    public Boolean getBleedingBetweenPeriods() {
        return bleedingBetweenPeriods;
    }

    public void setBleedingBetweenPeriods(Boolean bleedingBetweenPeriods) {
        this.bleedingBetweenPeriods = bleedingBetweenPeriods;
    }

    public String getOccupationalExposure() {
        return occupationalExposure;
    }

    public void setOccupationalExposure(String occupationalExposure) {
        this.occupationalExposure = occupationalExposure;
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

    public Date getMobileStartDate() {
        return mobileStartDate;
    }

    public void setMobileStartDate(Date mobileStartDate) {
        this.mobileStartDate = mobileStartDate;
    }

    public Date getMobileEndDate() {
        return mobileEndDate;
    }

    public void setMobileEndDate(Date mobileEndDate) {
        this.mobileEndDate = mobileEndDate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public MemberCbacDetail.DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(MemberCbacDetail.DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

}
