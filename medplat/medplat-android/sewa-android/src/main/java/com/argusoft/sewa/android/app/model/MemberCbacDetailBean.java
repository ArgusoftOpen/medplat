package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by prateek on 26/11/18.
 */

@DatabaseTable
public class MemberCbacDetailBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long actualId;

    @DatabaseField
    private Long memberId;

    @DatabaseField
    private Long familyId;

    @DatabaseField
    private String smokeOrConsumeGutka;

    @DatabaseField
    private String waist;

    @DatabaseField
    private Boolean consumeAlcoholDaily;

    @DatabaseField
    private String physicalActivity150Min;

    @DatabaseField
    private Boolean bpDiabetesHeartHistory;

    @DatabaseField
    private Boolean shortnessOfBreath;

    @DatabaseField
    private Boolean fitsHistory;

    @DatabaseField
    private Boolean twoWeeksCoughing;

    @DatabaseField
    private Boolean mouthOpeningDifficulty;

    @DatabaseField
    private Boolean bloodInSputum;

    @DatabaseField
    private Boolean twoWeeksUlcersInMouth;

    @DatabaseField
    private Boolean twoWeeksFever;

    @DatabaseField
    private Boolean changeInToneOfVoice;

    @DatabaseField
    private Boolean lossOfWeight;

    @DatabaseField
    private Boolean patchOnSkin;

    @DatabaseField
    private Boolean nightSweats;

    @DatabaseField
    private Boolean takingAntiTbDrugs;

    @DatabaseField
    private Boolean difficultyHoldingObjects;

    @DatabaseField
    private Boolean sensationLossPalm;

    @DatabaseField
    private Boolean familyMemberSufferingFromTb;

    @DatabaseField
    private Boolean historyOfTb;

    @DatabaseField
    private Boolean lumpInBreast;

    @DatabaseField
    private Boolean bleedingAfterMenopause;

    @DatabaseField
    private Boolean nippleBloodStainedDischarge;

    @DatabaseField
    private Boolean bleedingAfterIntercourse;

    @DatabaseField
    private Boolean changeInSizeOfBreast;

    @DatabaseField
    private Boolean foulVaginalDischarge;

    @DatabaseField
    private Boolean bleedingBetweenPeriods;

    @DatabaseField
    private String occupationalExposure;

    @DatabaseField
    private Integer score;

    @DatabaseField
    private Integer ageAtMenarche;

    @DatabaseField
    private Boolean menopauseArrived;

    @DatabaseField
    private Integer durationOfMenoapuse;

    @DatabaseField
    private Boolean pregnant;

    @DatabaseField
    private Boolean lactating;

    @DatabaseField
    private Boolean regularPeriods;

    @DatabaseField
    private Date lmp;

    @DatabaseField
    private String bleeding;

    @DatabaseField
    private String associatedWith;

    @DatabaseField
    private String remarks;

    @DatabaseField
    private Boolean diagnosedForHypertension;

    @DatabaseField
    private Boolean underTreatementForHypertension;

    @DatabaseField
    private Boolean diagnosedForDiabetes;

    @DatabaseField
    private Boolean underTreatementForDiabetes;

    @DatabaseField
    private Boolean diagnosedForHeartDiseases;

    @DatabaseField
    private Boolean underTreatementForHeartDiseases;

    @DatabaseField
    private Boolean diagnosedForStroke;

    @DatabaseField
    private Boolean underTreatementForStroke;

    @DatabaseField
    private Boolean diagnosedForKidneyFailure;

    @DatabaseField
    private Boolean underTreatementForKidneyFailure;

    @DatabaseField
    private Boolean diagnosedForNonHealingWound;

    @DatabaseField
    private Boolean underTreatementForNonHealingWound;

    @DatabaseField
    private Boolean diagnosedForCOPD;

    @DatabaseField
    private Boolean underTreatementForCOPD;

    @DatabaseField
    private Boolean diagnosedForAsthama;

    @DatabaseField
    private Boolean underTreatementForAsthama;

    @DatabaseField
    private Boolean diagnosedForOralCancer;

    @DatabaseField
    private Boolean underTreatementForOralCancer;

    @DatabaseField
    private Boolean diagnosedForBreastCancer;

    @DatabaseField
    private Boolean underTreatementForBreastCancer;

    @DatabaseField
    private Boolean diagnosedForCervicalCancer;

    @DatabaseField
    private Boolean underTreatementForCervicalCancer;

    @DatabaseField
    private Integer height;

    @DatabaseField
    private Float weight;

    @DatabaseField
    private Float bmi;

    @DatabaseField
    private Date modifiedOn;

    public Long getActualId() {
        return actualId;
    }

    public void setActualId(Long actualId) {
        this.actualId = actualId;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}