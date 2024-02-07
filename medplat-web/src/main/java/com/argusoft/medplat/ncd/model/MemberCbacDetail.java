/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <p>
 * Define ncd_member_cbac_detail entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "ncd_member_cbac_detail")
public class MemberCbacDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "smoke_or_consume_gutka")
    private String smokeOrConsumeGutka;

    @Column(name = "waist")
    private String waist;

    @Column(name = "consume_alcohol_daily")
    private Boolean consumeAlcoholDaily;

    @Column(name = "physical_activity_150_min")
    private String physicalActivity150Min;

    @Column(name = "bp_diabetes_heart_history")
    private Boolean bpDiabetesHeartHistory;

    @Column(name = "shortness_of_breath")
    private Boolean shortnessOfBreath;

    @Column(name = "fits_history")
    private Boolean fitsHistory;

    @Column(name = "two_weeks_coughing")
    private Boolean twoWeeksCoughing;

    @Column(name = "mouth_opening_difficulty")
    private Boolean mouthOpeningDifficulty;

    @Column(name = "blood_in_sputum")
    private Boolean bloodInSputum;

    @Column(name = "two_weeks_ulcers_in_mouth")
    private Boolean twoWeeksUlcersInMouth;

    @Column(name = "two_weeks_fever")
    private Boolean twoWeeksFever;

    @Column(name = "change_in_tone_of_voice")
    private Boolean changeInToneOfVoice;

    @Column(name = "loss_of_weight")
    private Boolean lossOfWeight;

    @Column(name = "patch_on_skin")
    private Boolean patchOnSkin;

    @Column(name = "night_sweats")
    private Boolean nightSweats;

    @Column(name = "taking_anti_tb_drugs")
    private Boolean takingAntiTbDrugs;

    @Column(name = "difficulty_holding_objects")
    private Boolean difficultyHoldingObjects;

    @Column(name = "sensation_loss_palm")
    private Boolean sensationLossPalm;

    @Column(name = "family_member_suffering_from_tb")
    private Boolean familyMemberSufferingFromTb;

    @Column(name = "history_of_tb")
    private Boolean historyOfTb;

    @Column(name = "lump_in_breast")
    private Boolean lumpInBreast;

    @Column(name = "bleeding_after_menopause")
    private Boolean bleedingAfterMenopause;

    @Column(name = "nipple_blood_stained_discharge")
    private Boolean nippleBloodStainedDischarge;

    @Column(name = "bleeding_after_intercourse")
    private Boolean bleedingAfterIntercourse;

    @Column(name = "change_in_size_of_breast")
    private Boolean changeInSizeOfBreast;

    @Column(name = "foul_vaginal_discharge")
    private Boolean foulVaginalDischarge;

    @Column(name = "bleeding_between_periods")
    private Boolean bleedingBetweenPeriods;

    @Column(name = "occupational_exposure")
    private String occupationalExposure;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "score")
    private Integer score;

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

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "bmi", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float bmi;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "manual_verification")
    private Boolean manualVerification;

    @Column(name = "recurrent_ulceration")
    private Boolean recurrentUlceration;

    @Column(name = "recurrent_tingling")
    private Boolean recurrentTingling;

    @Column(name = "cloudy_vision")
    private Boolean cloudyVision;

    @Column(name = "reading_difficluty")
    private Boolean readingDifficulty;

    @Column(name = "eye_pain")
    private Boolean eyePain;

    @Column(name = "eye_redness")
    private Boolean eyeRedness;

    @Column(name = "hearing_difficulty")
    private Boolean hearingDifficulty;

    @Column(name = "chewing_pain")
    private Boolean chewingPain;

    @Column(name = "mouth_ulcers")
    private Boolean mouthUlcers;

    @Column(name = "mouth_patch")
    private Boolean mouthPatch;

    @Column(name = "thick_skin")
    private Boolean thickSkin;

    @Column(name = "nodules_on_skin")
    private Boolean nodulesOnSkin;

    @Column(name = "clawing_of_fingers")
    private Boolean clawingOfFingers;

    @Column(name = "tingling_in_hand")
    private Boolean tinglingInHand;

    @Column(name = "inability_close_eyelid")
    private Boolean inabilityCloseEyelid;

    @Column(name = "feet_weakness")
    private Boolean feetWeakness;

    @Column(name = "crop_residue_burning")
    private Boolean cropResidueBurning;

    @Column(name = "garbage_burning")
    private Boolean garbageBurning;

    @Column(name = "working_industry")
    private Boolean workingIndustry;

    @Column(name = "interest_doing_things")
    private String interestDoingThings;

    @Column(name = "feeling_down")
    private String feelingDown;

    @Column(name = "feeling_unsteady")
    private Boolean feelingUnsteady;

    @Column(name = "physical_disability")
    private Boolean physicalDisability;

    @Column(name = "need_help_from_others")
    private Boolean needHelpFromOthers;

    @Column(name = "forget_names")
    private Boolean forgetNames;
    @Column(name = "hmis_id")
    private Long hmisId;
    @Column(name = "growth_in_mouth")
    private Boolean growthInMouth;
    @Column(name = "known_disabilities")
    private String knownDisabilities;
    @Column(name = "blurred_vision_eye")
    private String blurredVisionEye;
    @Column(name = "physical_activity_30_min")
    private String physicalActivity30min;

    @Column(name = "cbac_and_nutrition_master_id")
    private Integer cbacAndNutritionMasterId;

    @Column(name = "treatment_for_leprosy")
    private Boolean treatmentForLeprosy;

    @Column(name = "consume_alcohol")
    private String consumeAlcohol;

    @Column(name = "smoke")
    private String smoke;

    @Column(name = "consume_gutka")
    private String consumeGutka;


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

    public Boolean getChangeInToneOfVoice() {
        return changeInToneOfVoice;
    }

    public void setChangeInToneOfVoice(Boolean changeInToneOfVoice) {
        this.changeInToneOfVoice = changeInToneOfVoice;
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

    public DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public Boolean getManualVerification() {
        return manualVerification;
    }

    public void setManualVerification(Boolean manualVerification) {
        this.manualVerification = manualVerification;
    }

    public Boolean getRecurrentUlceration() {
        return recurrentUlceration;
    }

    public void setRecurrentUlceration(Boolean recurrentUlceration) {
        this.recurrentUlceration = recurrentUlceration;
    }

    public Boolean getRecurrentTingling() {
        return recurrentTingling;
    }

    public void setRecurrentTingling(Boolean recurrentTingling) {
        this.recurrentTingling = recurrentTingling;
    }

    public Boolean getCloudyVision() {
        return cloudyVision;
    }

    public void setCloudyVision(Boolean cloudyVision) {
        this.cloudyVision = cloudyVision;
    }

    public Boolean getReadingDifficulty() {
        return readingDifficulty;
    }

    public void setReadingDifficulty(Boolean readingDifficulty) {
        this.readingDifficulty = readingDifficulty;
    }

    public Boolean getEyePain() {
        return eyePain;
    }

    public void setEyePain(Boolean eyePain) {
        this.eyePain = eyePain;
    }

    public Boolean getEyeRedness() {
        return eyeRedness;
    }

    public void setEyeRedness(Boolean eyeRedness) {
        this.eyeRedness = eyeRedness;
    }

    public Boolean getHearingDifficulty() {
        return hearingDifficulty;
    }

    public void setHearingDifficulty(Boolean hearingDifficulty) {
        this.hearingDifficulty = hearingDifficulty;
    }

    public Boolean getChewingPain() {
        return chewingPain;
    }

    public void setChewingPain(Boolean chewingPain) {
        this.chewingPain = chewingPain;
    }

    public Boolean getMouthUlcers() {
        return mouthUlcers;
    }

    public void setMouthUlcers(Boolean mouthUlcers) {
        this.mouthUlcers = mouthUlcers;
    }

    public Boolean getMouthPatch() {
        return mouthPatch;
    }

    public void setMouthPatch(Boolean mouthPatch) {
        this.mouthPatch = mouthPatch;
    }

    public Boolean getThickSkin() {
        return thickSkin;
    }

    public void setThickSkin(Boolean thickSkin) {
        this.thickSkin = thickSkin;
    }

    public Boolean getNodulesOnSkin() {
        return nodulesOnSkin;
    }

    public void setNodulesOnSkin(Boolean nodulesOnSkin) {
        this.nodulesOnSkin = nodulesOnSkin;
    }

    public Boolean getClawingOfFingers() {
        return clawingOfFingers;
    }

    public void setClawingOfFingers(Boolean clawingOfFingers) {
        this.clawingOfFingers = clawingOfFingers;
    }

    public Boolean getTinglingInHand() {
        return tinglingInHand;
    }

    public void setTinglingInHand(Boolean tinglingInHand) {
        this.tinglingInHand = tinglingInHand;
    }

    public Boolean getInabilityCloseEyelid() {
        return inabilityCloseEyelid;
    }

    public void setInabilityCloseEyelid(Boolean inabilityCloseEyelid) {
        this.inabilityCloseEyelid = inabilityCloseEyelid;
    }

    public Boolean getFeetWeakness() {
        return feetWeakness;
    }

    public void setFeetWeakness(Boolean feetWeakness) {
        this.feetWeakness = feetWeakness;
    }

    public Boolean getCropResidueBurning() {
        return cropResidueBurning;
    }

    public void setCropResidueBurning(Boolean cropResidueBurning) {
        this.cropResidueBurning = cropResidueBurning;
    }

    public Boolean getGarbageBurning() {
        return garbageBurning;
    }

    public void setGarbageBurning(Boolean garbageBurning) {
        this.garbageBurning = garbageBurning;
    }

    public Boolean getWorkingIndustry() {
        return workingIndustry;
    }

    public void setWorkingIndustry(Boolean workingIndustry) {
        this.workingIndustry = workingIndustry;
    }

    public String getInterestDoingThings() {
        return interestDoingThings;
    }

    public void setInterestDoingThings(String interestDoingThings) {
        this.interestDoingThings = interestDoingThings;
    }

    public String getFeelingDown() {
        return feelingDown;
    }

    public void setFeelingDown(String feelingDown) {
        this.feelingDown = feelingDown;
    }

    public Boolean getFeelingUnsteady() {
        return feelingUnsteady;
    }

    public void setFeelingUnsteady(Boolean feelingUnsteady) {
        this.feelingUnsteady = feelingUnsteady;
    }

    public Boolean getPhysicalDisability() {
        return physicalDisability;
    }

    public void setPhysicalDisability(Boolean physicalDisability) {
        this.physicalDisability = physicalDisability;
    }

    public Boolean getNeedHelpFromOthers() {
        return needHelpFromOthers;
    }

    public void setNeedHelpFromOthers(Boolean needHelpFromOthers) {
        this.needHelpFromOthers = needHelpFromOthers;
    }

    public Boolean getForgetNames() {
        return forgetNames;
    }

    public void setForgetNames(Boolean forgetNames) {
        this.forgetNames = forgetNames;
    }

    public Long getHmisId() {
        return hmisId;
    }

    public void setHmisId(Long hmisId) {
        this.hmisId = hmisId;
    }

    public Boolean getGrowthInMouth() {
        return growthInMouth;
    }

    public void setGrowthInMouth(Boolean growthInMouth) {
        this.growthInMouth = growthInMouth;
    }

    public String getKnownDisabilities() {
        return knownDisabilities;
    }

    public void setKnownDisabilities(String knownDisabilities) {
        this.knownDisabilities = knownDisabilities;
    }

    public String getBlurredVisionEye() {
        return blurredVisionEye;
    }

    public void setBlurredVisionEye(String blurredVisionEye) {
        this.blurredVisionEye = blurredVisionEye;
    }

    public String getPhysicalActivity30min() {
        return physicalActivity30min;
    }

    public void setPhysicalActivity30min(String physicalActivity30min) {
        this.physicalActivity30min = physicalActivity30min;
    }

    public Integer getCbacAndNutritionMasterId() {
        return cbacAndNutritionMasterId;
    }

    public void setCbacAndNutritionMasterId(Integer cbacAndNutritionMasterId) {
        this.cbacAndNutritionMasterId = cbacAndNutritionMasterId;
    }

    public Boolean getTreatmentForLeprosy() {
        return treatmentForLeprosy;
    }

    public void setTreatmentForLeprosy(Boolean treatmentForLeprosy) {
        this.treatmentForLeprosy = treatmentForLeprosy;
    }

    public String getConsumeAlcohol() {
        return consumeAlcohol;
    }

    public void setConsumeAlcohol(String consumeAlcohol) {
        this.consumeAlcohol = consumeAlcohol;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getConsumeGutka() {
        return consumeGutka;
    }

    public void setConsumeGutka(String consumeGutka) {
        this.consumeGutka = consumeGutka;
    }

    public enum DoneBy {
        MO, FHW, ASHA, CHO, MPW, RBSK
    }

    /**
     * Define fields name for ncd_member_cbac_detail.
     */
    public static class Fields {
        private Fields() {
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
