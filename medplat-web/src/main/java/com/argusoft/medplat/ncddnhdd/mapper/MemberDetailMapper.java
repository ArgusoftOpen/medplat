/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.mapper;

import com.argusoft.medplat.ncddnhdd.dto.*;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDto;
import com.argusoft.medplat.ncddnhdd.enums.ReferralPlace;
import com.argusoft.medplat.ncddnhdd.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**MemberOralDetail
 * @author vaishali
 */
public class MemberDetailMapper {

    public static MemberHyperTensionDto entityToDtoForHyperTension(MemberHypertensionDetail hypertensionDetail) {

        MemberHyperTensionDto memberHyperTensionDto = new MemberHyperTensionDto();
        memberHyperTensionDto.setId(hypertensionDetail.getId());
        memberHyperTensionDto.setDiastolicBloodPressure(hypertensionDetail.getDiastolicBp());
        memberHyperTensionDto.setHeartRate(hypertensionDetail.getPulseRate());
        memberHyperTensionDto.setSystolicBloodPressure(hypertensionDetail.getSystolicBp());
        memberHyperTensionDto.setScreeningDate(hypertensionDetail.getScreeningDate());
        memberHyperTensionDto.setMemberId(hypertensionDetail.getMemberId());
        memberHyperTensionDto.setLocationId(hypertensionDetail.getLocationId());
        memberHyperTensionDto.setFamilyId(hypertensionDetail.getFamilyId());
        memberHyperTensionDto.setDoneBy(hypertensionDetail.getDoneBy());
        return memberHyperTensionDto;
    }

    public static MemberHypertensionDetail dtoToEntityForHyperTension(MemberHyperTensionDto hyperTensionDto) {

        MemberHypertensionDetail memberHypertensionDetail = new MemberHypertensionDetail();
        memberHypertensionDetail.setDiastolicBp(hyperTensionDto.getDiastolicBloodPressure());
        memberHypertensionDetail.setPulseRate(hyperTensionDto.getHeartRate());
        memberHypertensionDetail.setSystolicBp(hyperTensionDto.getSystolicBloodPressure());
        memberHypertensionDetail.setScreeningDate(hyperTensionDto.getScreeningDate());
        memberHypertensionDetail.setMemberId(hyperTensionDto.getMemberId());
        memberHypertensionDetail.setLocationId(hyperTensionDto.getLocationId());
        memberHypertensionDetail.setFamilyId(hyperTensionDto.getFamilyId());
        memberHypertensionDetail.setDoneBy(hyperTensionDto.getDoneBy());
        memberHypertensionDetail.setMobileStartDate(new Date(-1L));
        memberHypertensionDetail.setMobileEndDate(new Date(-1L));
        memberHypertensionDetail.setDoneOn(new Date());
        return memberHypertensionDetail;
    }

    public static MemberCervicalDto entityToDtoForCervical(MemberCervicalDetail memberCervicalDetail) {

        MemberCervicalDto cervicalDto = new MemberCervicalDto();

        cervicalDto.setBleedingBetweenPeriods(memberCervicalDetail.getBleedingBetweenPeriods());
        cervicalDto.setBleedingDfterIntercourse(memberCervicalDetail.getBleedingAfterIntercourse());
        cervicalDto.setBleedsOnTouch(memberCervicalDetail.getBleedsOnTouch());
        cervicalDto.setCervicalRelatedSymptoms(memberCervicalDetail.getCervicalRelatedSymptoms());
        cervicalDto.setEctopy(memberCervicalDetail.getEctopy());
        cervicalDto.setExcessiveBleedingDuringPeriods(memberCervicalDetail.getExcessiveBleedingDuringPeriods());
        cervicalDto.setExcessiveSmellingVaginalDischarge(memberCervicalDetail.getExcessiveSmellingVaginalDischarge());
        cervicalDto.setFrankMalignancy(memberCervicalDetail.getFrankMalignancy());
        cervicalDto.setHypertrophy(memberCervicalDetail.getHypertrophy());
        cervicalDto.setOtherSymptoms(memberCervicalDetail.getOtherSymptoms());
        cervicalDto.setOtherDesc(memberCervicalDetail.getOtherDesc());
        cervicalDto.setViaExam(memberCervicalDetail.getViaExam());
        cervicalDto.setPolyp(memberCervicalDetail.getPolyp());
        cervicalDto.setPostmenopausalBleeding(memberCervicalDetail.getPostmenopausalBleeding());
        cervicalDto.setProlapseUterus(memberCervicalDetail.getProlapseUterus());
        cervicalDto.setSuspiciousLookingCervix(memberCervicalDetail.getProlapseUterus());
        cervicalDto.setUnhealthyCervix(memberCervicalDetail.getUnhealthyCervix());
        cervicalDto.setId(memberCervicalDetail.getId());
        cervicalDto.setDoneBy(memberCervicalDetail.getDoneBy());
        cervicalDto.setMemberId(memberCervicalDetail.getMemberId());
        cervicalDto.setOtherClinicalExamination(memberCervicalDetail.getOtherClinicalExamination());
        cervicalDto.setViaExamPoints(memberCervicalDetail.getViaExamPoints());
        cervicalDto.setReferralId(memberCervicalDetail.getReferralId());
        cervicalDto.setRefferralPlace(memberCervicalDetail.getRefferalPlace());
        cervicalDto.setPapsmearTest(memberCervicalDetail.getPapsmearTest());

        return cervicalDto;

    }

    public static MemberCervicalDetail dtoToEntityForCervical(MemberCervicalDto cervicalDto) {
        MemberCervicalDetail memberCervicalDetail = new MemberCervicalDetail();
        memberCervicalDetail.setPapsmearTest(cervicalDto.getPapsmearTest());
//        memberCervicalDetail.setViaTest(cervicalDto.getViaTest());
        memberCervicalDetail.setScreeningDate(cervicalDto.getScreeningDate());
        memberCervicalDetail.setMemberId(cervicalDto.getMemberId());
        memberCervicalDetail.setLocationId(cervicalDto.getLocationId());
        memberCervicalDetail.setFamilyId(cervicalDto.getFamilyId());
        memberCervicalDetail.setDoneBy(cervicalDto.getDoneBy());
        memberCervicalDetail.setMobileStartDate(new Date(-1L));
        memberCervicalDetail.setMobileEndDate(new Date(-1L));
        memberCervicalDetail.setDoneOn(new Date());
        return memberCervicalDetail;

    }

    public static MemberDiabetesDto entityToDtoForDiabetes(MemberDiabetesDetail memberDiabetesDetail) {
        MemberDiabetesDto memberDiabetesDto = new MemberDiabetesDto();
        memberDiabetesDto.setId(memberDiabetesDetail.getId());
        memberDiabetesDto.setFastingBloodSugar(memberDiabetesDetail.getFastingBloodSugar());
        memberDiabetesDto.setPostPrandialBloodSugar(memberDiabetesDetail.getPostPrandialBloodSugar());
        memberDiabetesDto.setBloodSugar(memberDiabetesDetail.getBloodSugar());
        memberDiabetesDto.setDka(memberDiabetesDetail.getDka());
        memberDiabetesDto.setHba1c(memberDiabetesDetail.getHba1c());
        memberDiabetesDto.setScreeningDate(memberDiabetesDetail.getScreeningDate());
        memberDiabetesDto.setMemberId(memberDiabetesDetail.getMemberId());
        memberDiabetesDto.setLocationId(memberDiabetesDetail.getLocationId());
        memberDiabetesDto.setFamilyId(memberDiabetesDetail.getFamilyId());
        memberDiabetesDto.setDoneBy(memberDiabetesDetail.getDoneBy());
        return memberDiabetesDto;
    }

    public static MemberDiabetesDetail dtoToEntityForDiabetes(MemberDiabetesDto memberDiabetesDto) {
        MemberDiabetesDetail memberDiabetesDetail = new MemberDiabetesDetail();
        memberDiabetesDetail.setFastingBloodSugar(memberDiabetesDto.getFastingBloodSugar());
        memberDiabetesDetail.setPostPrandialBloodSugar(memberDiabetesDto.getPostPrandialBloodSugar());
        memberDiabetesDetail.setBloodSugar(memberDiabetesDto.getBloodSugar());
        memberDiabetesDetail.setDka(memberDiabetesDto.getDka());
        memberDiabetesDetail.setHba1c(memberDiabetesDto.getHba1c());
        memberDiabetesDetail.setScreeningDate(memberDiabetesDto.getScreeningDate());
        memberDiabetesDetail.setMemberId(memberDiabetesDto.getMemberId());
        memberDiabetesDetail.setLocationId(memberDiabetesDto.getLocationId());
        memberDiabetesDetail.setFamilyId(memberDiabetesDto.getFamilyId());
        memberDiabetesDetail.setDoneBy(memberDiabetesDto.getDoneBy());
        memberDiabetesDetail.setMobileStartDate(new Date(-1L));
        memberDiabetesDetail.setMobileEndDate(new Date(-1L));
        memberDiabetesDetail.setDoneOn(new Date());
        return memberDiabetesDetail;
    }

    public static MemberBreastDto entityToDtoForBreast(MemberBreastDetail memberBreastDetail) {

        MemberBreastDto breastDto = new MemberBreastDto();
        breastDto.setAgreedForSelfBreastExam(memberBreastDetail.getAgreedForSelfBreastExam());
        breastDto.setAnyBreastRelatedSymptoms(memberBreastDetail.getAnyBreastRelatedSymptoms());
        breastDto.setAnyRetractionOfNipple(memberBreastDetail.getAnyRetractionOfNipple());
        breastDto.setDischargeFromNipple(memberBreastDetail.getDischargeFromNipple());
        breastDto.setErosionsOfNipple(memberBreastDetail.getErosionsOfNipple());
        breastDto.setId(memberBreastDetail.getId());
        breastDto.setLumpInBreast(memberBreastDetail.getLumpInBreast());
        breastDto.setLymphadenopathy(memberBreastDetail.getLymphadenopathy());
        breastDto.setNippleShapeAndPositionChange(memberBreastDetail.getNippleShapeAndPositionChange());
        breastDto.setRednessOfSkinOverNipple(memberBreastDetail.getRednessOfSkinOverNipple());
        breastDto.setRemarks(memberBreastDetail.getRemarks());
        breastDto.setSizeChange(memberBreastDetail.getSizeChange());
//        breastDto.setVisualDischargeFromNipple(memberBreastDetail.getVisualDischargeFromNipple());
        breastDto.setVisualLumpInBreast(memberBreastDetail.getVisualLumpInBreast());
        breastDto.setVisualNippleRetractionDistortion(memberBreastDetail.getVisualNippleRetractionDistortion());
        breastDto.setVisualRemarks(memberBreastDetail.getVisualRemarks());
        breastDto.setVisualSkinDimplingRetraction(memberBreastDetail.getVisualSkinDimplingRetraction());
        breastDto.setVisualSwellingInArmpit(memberBreastDetail.getVisualSwellingInArmpit());
        breastDto.setVisualUlceration(memberBreastDetail.getVisualUlceration());
        breastDto.setDoneBy(memberBreastDetail.getDoneBy());
        breastDto.setMemberId(memberBreastDetail.getMemberId());
        breastDto.setCreatedBy(memberBreastDetail.getCreatedBy());
        breastDto.setCreatedOn(memberBreastDetail.getCreatedOn());
//        breastDto.setOtherSymptoms(memberBreastDetail.getOtherSymptoms());
//        breastDto.setSkinDimpling(memberBreastDetail.getSkinDimpling());
        breastDto.setSizeChange(memberBreastDetail.getSizeChange());
        breastDto.setAxillary(memberBreastDetail.getAxillary());
        breastDto.setInfraClavicularArea(memberBreastDetail.getInfraClavicularArea());
        breastDto.setSuperClavicularArea(memberBreastDetail.getSuperClavicularArea());
        breastDto.setNippleNotOnSameLevel(memberBreastDetail.getNippleNotOnSameLevel());
//        breastDto.setRetractionOfSkin(memberBreastDetail.getRetractionOfSkin());
        breastDto.setUlceration(memberBreastDetail.getUlceration());
//        breastDto.setConsistencyOfLumps(memberBreastDetail.getConsistencyOfLumps());
        breastDto.setReferralId(memberBreastDetail.getReferralId());
        breastDto.setReferralPlace(memberBreastDetail.getRefferalPlace());

        return breastDto;

    }

    public static MemberBreastDetail dtoToEntityForBreast(MemberBreastDto breastDto) {
        MemberBreastDetail memberBreastDetail = new MemberBreastDetail();
        memberBreastDetail.setSizeChange(breastDto.getSizeChange());
        memberBreastDetail.setNippleNotOnSameLevel(breastDto.getNippleNotOnSameLevel());
        memberBreastDetail.setAnyRetractionOfNipple(breastDto.getAnyRetractionOfNipple());
        memberBreastDetail.setLymphadenopathy(breastDto.getLymphadenopathy());
        memberBreastDetail.setDischargeFromNipple(breastDto.getDischargeFromNipple());
        memberBreastDetail.setVisualSkinDimplingRetraction(breastDto.getVisualSkinDimplingRetraction());
        memberBreastDetail.setVisualNippleRetractionDistortion(breastDto.getVisualNippleRetractionDistortion());
        memberBreastDetail.setVisualLumpInBreast(breastDto.getVisualLumpInBreast());
        memberBreastDetail.setScreeningDate(breastDto.getScreeningDate());
        memberBreastDetail.setMemberId(breastDto.getMemberId());
        memberBreastDetail.setLocationId(breastDto.getLocationId());
        memberBreastDetail.setFamilyId(breastDto.getFamilyId());
        memberBreastDetail.setDoneBy(breastDto.getDoneBy());
        memberBreastDetail.setMobileStartDate(new Date(-1L));
        memberBreastDetail.setMobileEndDate(new Date(-1L));
        memberBreastDetail.setDoneOn(new Date());
//        memberBreastDetail.setSizeChangeLeft(breastDto.getSizeChangeLeft());
//        memberBreastDetail.setSizeChangeRight(breastDto.getSizeChangeRight());
//        memberBreastDetail.setRetractionOfLeftNipple(breastDto.getRetractionOfLeftNipple());
//        memberBreastDetail.setRetractionOfRightNipple(breastDto.getRetractionOfRightNipple());
//        memberBreastDetail.setDischargeFromLeftNipple(breastDto.getDischargeFromLeftNipple());
//        memberBreastDetail.setDischargeFromRightNipple(breastDto.getDischargeFromRightNipple());
//        memberBreastDetail.setLeftLymphadenopathy(breastDto.getLeftLymphadenopathy());
//        memberBreastDetail.setRightLymphadenopathy(breastDto.getRightLymphadenopathy());
        return memberBreastDetail;

    }

    public static MemberOtherInfoDto entityToDtoForOtherInfo(MemberOtherInfo memberOtherInfoDetail) {

        MemberOtherInfoDto otherInfoDto = new MemberOtherInfoDto();
        otherInfoDto.setAngina(memberOtherInfoDetail.getAngina());
        otherInfoDto.setChangeInDieteryHabits(memberOtherInfoDetail.getChangeInDieteryHabits());
        otherInfoDto.setDelayInHealing(memberOtherInfoDetail.getDelayInHealing());
        otherInfoDto.setExcessHunger(memberOtherInfoDetail.getExcessHunger());
        otherInfoDto.setExcessThirst(memberOtherInfoDetail.getExcessThirst());
        otherInfoDto.setExcessUrination(memberOtherInfoDetail.getExcessUrination());
        otherInfoDto.setId(memberOtherInfoDetail.getId());
        otherInfoDto.setIsMoScreeningDone(memberOtherInfoDetail.getIsMoScreeningDone());
        otherInfoDto.setLimpness(memberOtherInfoDetail.getLimpness());
        otherInfoDto.setRecurrentSkin(memberOtherInfoDetail.getRecurrentSkin());
        otherInfoDto.setSignificantEdema(memberOtherInfoDetail.getSignificantEdema());
        otherInfoDto.setVisualDisturbancesHistoryOrPresent(memberOtherInfoDetail.getVisualDisturbancesHistoryOrPresent());
        otherInfoDto.setIntermittentClaudication(memberOtherInfoDetail.getIntermittentClaudication());
        otherInfoDto.setDoneBy(memberOtherInfoDetail.getDoneBy());
        otherInfoDto.setMemberId(memberOtherInfoDetail.getMemberId());
        otherInfoDto.setConfirmedCaseOfCopd(memberOtherInfoDetail.getConfirmedCaseOfCopd());
        otherInfoDto.setConfirmedCaseOfDiabetes(memberOtherInfoDetail.getConfirmedCaseOfDiabetes());
        otherInfoDto.setConfirmedCaseOfHyperTension(memberOtherInfoDetail.getConfirmedCaseOfDiabetes());
        otherInfoDto.setFamilyHistoryOfDiabetes(memberOtherInfoDetail.getFamilyHistoryOfDiabetes());
        otherInfoDto.setFamilyHistoryOfPrematureMi(memberOtherInfoDetail.getFamilyHistoryOfPrematureMi());
        otherInfoDto.setFamilyHistoryOfStroke(memberOtherInfoDetail.getFamilyHistoryOfStroke());
        otherInfoDto.setHistoryOfHeartAttack(memberOtherInfoDetail.getHistoryOfHeartAttack());
        otherInfoDto.setHistoryOfStroke(memberOtherInfoDetail.getHistoryOfStroke());
        otherInfoDto.setIsReportVerified(memberOtherInfoDetail.getIsReportVerified());
        otherInfoDto.setSmokeTobacco(memberOtherInfoDetail.getSmokeTobacco());
        otherInfoDto.setSmokelessTobacco(memberOtherInfoDetail.getSmokelessTobacco());
        otherInfoDto.setAlcoholConsumption(memberOtherInfoDetail.getAlcoholConsumption());
        otherInfoDto.setReferralId(memberOtherInfoDetail.getReferralId());
        return otherInfoDto;
    }

    public static MemberOtherInfo dtoToEntityForOtherInfo(MemberOtherInfoDto otherInfoDto) {

        MemberOtherInfo memberOtherInfoDetail = new MemberOtherInfo();
        memberOtherInfoDetail.setAngina(otherInfoDto.getAngina());
        memberOtherInfoDetail.setChangeInDieteryHabits(otherInfoDto.getChangeInDieteryHabits());
        memberOtherInfoDetail.setDelayInHealing(otherInfoDto.getDelayInHealing());
        memberOtherInfoDetail.setExcessHunger(otherInfoDto.getExcessHunger());
        memberOtherInfoDetail.setExcessThirst(otherInfoDto.getExcessThirst());
        memberOtherInfoDetail.setExcessUrination(otherInfoDto.getExcessUrination());
        memberOtherInfoDetail.setId(otherInfoDto.getId());
        memberOtherInfoDetail.setIsMoScreeningDone(otherInfoDto.getIsMoScreeningDone());
        memberOtherInfoDetail.setLimpness(otherInfoDto.getLimpness());
        memberOtherInfoDetail.setRecurrentSkin(otherInfoDto.getRecurrentSkin());
        memberOtherInfoDetail.setSignificantEdema(otherInfoDto.getSignificantEdema());
        memberOtherInfoDetail.setVisualDisturbancesHistoryOrPresent(otherInfoDto.getVisualDisturbancesHistoryOrPresent());
        memberOtherInfoDetail.setIntermittentClaudication(otherInfoDto.getIntermittentClaudication());
        memberOtherInfoDetail.setDoneBy(otherInfoDto.getDoneBy());
        memberOtherInfoDetail.setMemberId(otherInfoDto.getMemberId());
        memberOtherInfoDetail.setConfirmedCaseOfCopd(otherInfoDto.getConfirmedCaseOfCopd());
        memberOtherInfoDetail.setConfirmedCaseOfDiabetes(otherInfoDto.getConfirmedCaseOfDiabetes());
        memberOtherInfoDetail.setConfirmedCaseOfHyperTension(otherInfoDto.getConfirmedCaseOfDiabetes());
        memberOtherInfoDetail.setFamilyHistoryOfDiabetes(otherInfoDto.getFamilyHistoryOfDiabetes());
        memberOtherInfoDetail.setFamilyHistoryOfPrematureMi(otherInfoDto.getFamilyHistoryOfPrematureMi());
        memberOtherInfoDetail.setFamilyHistoryOfStroke(otherInfoDto.getFamilyHistoryOfStroke());
        memberOtherInfoDetail.setIsReportVerified(otherInfoDto.getIsReportVerified());
        memberOtherInfoDetail.setHistoryOfHeartAttack(otherInfoDto.getHistoryOfHeartAttack());
        memberOtherInfoDetail.setHistoryOfStroke(otherInfoDto.getHistoryOfStroke());
        memberOtherInfoDetail.setDoneOn(new Date());
        memberOtherInfoDetail.setSmokeTobacco(otherInfoDto.getSmokeTobacco());
        memberOtherInfoDetail.setSmokelessTobacco(otherInfoDto.getSmokelessTobacco());
        memberOtherInfoDetail.setAlcoholConsumption(otherInfoDto.getAlcoholConsumption());
        memberOtherInfoDetail.setReferralId(otherInfoDto.getReferralId());
        return memberOtherInfoDetail;
    }

    public static MemberOralDto entityToDtoForOral(MemberOralDetail memberOralDetail) {
        MemberOralDto oralDto = new MemberOralDto();
        oralDto.setId(memberOralDetail.getId());
        oralDto.setAnyIssuesInMouth(memberOralDetail.getAnyIssuesInMouth());
        oralDto.setDifficultyInOpeningMouth(memberOralDetail.getDifficultyInOpeningMouth());
        oralDto.setDifficultyInSpicyFood(memberOralDetail.getDifficultyInSpicyFood());
        oralDto.setThreeWeeksMouthUlcer(memberOralDetail.getThreeWeeksMouthUlcer());
        oralDto.setGrowthOfRecentOrigins(memberOralDetail.getGrowthOfRecentOrigins());
        oralDto.setNonHealingUlcers(memberOralDetail.getNonHealingUlcers());
        oralDto.setRedPatches(memberOralDetail.getRedPatches());
        oralDto.setWhitePatches(memberOralDetail.getWhitePatches());
        oralDto.setWhiteRedPatchOralCavity(memberOralDetail.getWhiteRedPatchOralCavity());
//        oralDto.setRestrictedMouthOpening(memberOralDetail.getRestrictedMouthOpening());
        oralDto.setDoneBy(memberOralDetail.getDoneBy());
        oralDto.setMemberId(memberOralDetail.getMemberId());
        oralDto.setCreatedBy(memberOralDetail.getCreatedBy());
        oralDto.setCreatedOn(memberOralDetail.getCreatedOn());
        oralDto.setOtherSymptoms(memberOralDetail.getOtherSymptoms());
        oralDto.setScreeningDate(memberOralDetail.getScreeningDate());
        oralDto.setRefferalPlace(memberOralDetail.getRefferalPlace());
        oralDto.setReferralId(memberOralDetail.getReferralId());
        oralDto.setRefferalPlace(memberOralDetail.getRefferalPlace());

        return oralDto;
    }

    public static MemberOralDetail dtoToEntityForOral(MemberOralDto oralDto) {
        MemberOralDetail memberOralDetail = new MemberOralDetail();
//        memberOralDetail.setRestrictedMouthOpening(oralDto.getRestrictedMouthOpening());
        memberOralDetail.setWhitePatches(oralDto.getWhitePatches());
        memberOralDetail.setRedPatches(oralDto.getRedPatches());
        memberOralDetail.setNonHealingUlcers(oralDto.getNonHealingUlcers());
        memberOralDetail.setGrowthOfRecentOrigins(oralDto.getGrowthOfRecentOrigins());
        memberOralDetail.setScreeningDate(oralDto.getScreeningDate());
        memberOralDetail.setMemberId(oralDto.getMemberId());
        memberOralDetail.setLocationId(oralDto.getLocationId());
        memberOralDetail.setFamilyId(oralDto.getFamilyId());
        memberOralDetail.setDoneBy(oralDto.getDoneBy());
        memberOralDetail.setMobileStartDate(new Date(-1L));
        memberOralDetail.setMobileEndDate(new Date(-1L));
        memberOralDetail.setDoneOn(new Date());

        return memberOralDetail;
    }

    public static MemberCbacDto entityToDtoForCbac(MemberCbacDetail memberCbacDetail) {
        MemberCbacDto memberCbacDto = new MemberCbacDto();
        memberCbacDto.setMemberId(memberCbacDetail.getMemberId());
        memberCbacDto.setSmokeOrConsumeGutka(memberCbacDetail.getSmokeOrConsumeGutka());
        memberCbacDto.setWaist(memberCbacDetail.getWaist());
        memberCbacDto.setConsumeAlcoholDaily(memberCbacDetail.getConsumeAlcoholDaily());
        memberCbacDto.setPhysicalActivity150Min(memberCbacDetail.getPhysicalActivity150Min());
        memberCbacDto.setBpDiabetesHeartHistory(memberCbacDetail.getBpDiabetesHeartHistory());
        memberCbacDto.setShortnessOfBreath(memberCbacDetail.getShortnessOfBreath());
        memberCbacDto.setFitsHistory(memberCbacDetail.getFitsHistory());
        memberCbacDto.setTwoWeeksCoughing(memberCbacDetail.getTwoWeeksCoughing());
        memberCbacDto.setMouthOpeningDifficulty(memberCbacDetail.getMouthOpeningDifficulty());
        memberCbacDto.setBloodInSputum(memberCbacDetail.getBloodInSputum());
        memberCbacDto.setTwoWeeksUlcersInMouth(memberCbacDetail.getTwoWeeksUlcersInMouth());
        memberCbacDto.setTwoWeeksFever(memberCbacDetail.getTwoWeeksFever());
        memberCbacDto.setChangeInToneOfVoice(memberCbacDetail.getChangeInToneOfVoice());
        memberCbacDto.setLossOfWeight(memberCbacDetail.getLossOfWeight());
        memberCbacDto.setPatchOnSkin(memberCbacDetail.getPatchOnSkin());
        memberCbacDto.setNightSweats(memberCbacDetail.getNightSweats());
        memberCbacDto.setTakingAntiTbDrugs(memberCbacDetail.getTakingAntiTbDrugs());
        memberCbacDto.setDifficultyHoldingObjects(memberCbacDetail.getDifficultyHoldingObjects());
        memberCbacDto.setSensationLossPalm(memberCbacDetail.getSensationLossPalm());
        memberCbacDto.setFamilyMemberSufferingFromTb(memberCbacDetail.getFamilyMemberSufferingFromTb());
        memberCbacDto.setHistoryOfTb(memberCbacDetail.getHistoryOfTb());
        memberCbacDto.setLumpInBreast(memberCbacDetail.getLumpInBreast());
        memberCbacDto.setBleedingAfterMenopause(memberCbacDetail.getBleedingAfterMenopause());
        memberCbacDto.setNippleBloodStainedDischarge(memberCbacDetail.getNippleBloodStainedDischarge());
        memberCbacDto.setBleedingAfterIntercourse(memberCbacDetail.getBleedingAfterIntercourse());
        memberCbacDto.setChangeInSizeOfBreast(memberCbacDetail.getChangeInSizeOfBreast());
        memberCbacDto.setFoulVaginalDischarge(memberCbacDetail.getFoulVaginalDischarge());
        memberCbacDto.setBleedingBetweenPeriods(memberCbacDetail.getBleedingBetweenPeriods());
        memberCbacDto.setOccupationalExposure(memberCbacDetail.getOccupationalExposure());
        memberCbacDto.setScore(memberCbacDetail.getScore());
        memberCbacDto.setAgeAtMenarche(memberCbacDetail.getAgeAtMenarche());
        memberCbacDto.setMenopauseArrived(memberCbacDetail.getMenopauseArrived());
        memberCbacDto.setDurationOfMenoapuse(memberCbacDetail.getDurationOfMenoapuse());
        memberCbacDto.setPregnant(memberCbacDetail.getPregnant());
        memberCbacDto.setLactating(memberCbacDetail.getLactating());
        memberCbacDto.setRegularPeriods(memberCbacDetail.getRegularPeriods());
        memberCbacDto.setBleeding(memberCbacDetail.getBleeding());
        memberCbacDto.setAssociatedWith(memberCbacDetail.getAssociatedWith());
        memberCbacDto.setRemarks(memberCbacDetail.getRemarks());
        memberCbacDto.setDiagnosedForHypertension(memberCbacDetail.getDiagnosedForHypertension());
        memberCbacDto.setUnderTreatementForHypertension(memberCbacDetail.getUnderTreatementForHypertension());
        memberCbacDto.setDiagnosedForDiabetes(memberCbacDetail.getDiagnosedForDiabetes());
        memberCbacDto.setUnderTreatementForDiabetes(memberCbacDetail.getUnderTreatementForDiabetes());
        memberCbacDto.setDiagnosedForHeartDiseases(memberCbacDetail.getDiagnosedForHeartDiseases());
        memberCbacDto.setUnderTreatementForHeartDiseases(memberCbacDetail.getUnderTreatementForHeartDiseases());
        memberCbacDto.setDiagnosedForStroke(memberCbacDetail.getDiagnosedForStroke());
        memberCbacDto.setUnderTreatementForStroke(memberCbacDetail.getUnderTreatementForStroke());
        memberCbacDto.setDiagnosedForKidneyFailure(memberCbacDetail.getDiagnosedForKidneyFailure());
        memberCbacDto.setUnderTreatementForKidneyFailure(memberCbacDetail.getUnderTreatementForKidneyFailure());
        memberCbacDto.setDiagnosedForNonHealingWound(memberCbacDetail.getDiagnosedForNonHealingWound());
        memberCbacDto.setUnderTreatementForNonHealingWound(memberCbacDetail.getUnderTreatementForNonHealingWound());
        memberCbacDto.setDiagnosedForCOPD(memberCbacDetail.getDiagnosedForCOPD());
        memberCbacDto.setUnderTreatementForCOPD(memberCbacDetail.getUnderTreatementForCOPD());
        memberCbacDto.setDiagnosedForAsthama(memberCbacDetail.getDiagnosedForAsthama());
        memberCbacDto.setUnderTreatementForAsthama(memberCbacDetail.getUnderTreatementForAsthama());
        memberCbacDto.setDiagnosedForOralCancer(memberCbacDetail.getDiagnosedForOralCancer());
        memberCbacDto.setUnderTreatementForOralCancer(memberCbacDetail.getUnderTreatementForOralCancer());
        memberCbacDto.setDiagnosedForBreastCancer(memberCbacDetail.getDiagnosedForBreastCancer());
        memberCbacDto.setUnderTreatementForBreastCancer(memberCbacDetail.getUnderTreatementForBreastCancer());
        memberCbacDto.setDiagnosedForCervicalCancer(memberCbacDetail.getDiagnosedForCervicalCancer());
        memberCbacDto.setUnderTreatementForCervicalCancer(memberCbacDetail.getUnderTreatementForCervicalCancer());
        memberCbacDto.setHeight(memberCbacDetail.getHeight());
        memberCbacDto.setWeight(memberCbacDetail.getWeight());
        memberCbacDto.setBmi(memberCbacDetail.getBmi());
        memberCbacDto.setLmp(memberCbacDetail.getLmp());
        memberCbacDto.setDoneBy(memberCbacDetail.getDoneBy());
        memberCbacDto.setId(memberCbacDetail.getId());
        memberCbacDto.setId(memberCbacDetail.getId());
        memberCbacDto.setReferralId(memberCbacDetail.getReferralId());

        return memberCbacDto;
    }

    public static MemberCbacDetail dtoToEntityForCbac(MemberCbacDto memberCbacDto) {
        MemberCbacDetail memberCbacDetail = new MemberCbacDetail();
        memberCbacDetail.setMemberId(memberCbacDto.getMemberId());
        memberCbacDetail.setSmokeOrConsumeGutka(memberCbacDto.getSmokeOrConsumeGutka());
        memberCbacDetail.setWaist(memberCbacDto.getWaist());
        memberCbacDetail.setConsumeAlcoholDaily(memberCbacDto.getConsumeAlcoholDaily());
        memberCbacDetail.setPhysicalActivity150Min(memberCbacDto.getPhysicalActivity150Min());
        memberCbacDetail.setBpDiabetesHeartHistory(memberCbacDto.getBpDiabetesHeartHistory());
        memberCbacDetail.setShortnessOfBreath(memberCbacDto.getShortnessOfBreath());
        memberCbacDetail.setFitsHistory(memberCbacDto.getFitsHistory());
        memberCbacDetail.setTwoWeeksCoughing(memberCbacDto.getTwoWeeksCoughing());
        memberCbacDetail.setMouthOpeningDifficulty(memberCbacDto.getMouthOpeningDifficulty());
        memberCbacDetail.setBloodInSputum(memberCbacDto.getBloodInSputum());
        memberCbacDetail.setTwoWeeksUlcersInMouth(memberCbacDto.getTwoWeeksUlcersInMouth());
        memberCbacDetail.setTwoWeeksFever(memberCbacDto.getTwoWeeksFever());
        memberCbacDetail.setChangeInToneOfVoice(memberCbacDto.getChangeInToneOfVoice());
        memberCbacDetail.setLossOfWeight(memberCbacDto.getLossOfWeight());
        memberCbacDetail.setPatchOnSkin(memberCbacDto.getPatchOnSkin());
        memberCbacDetail.setNightSweats(memberCbacDto.getNightSweats());
        memberCbacDetail.setTakingAntiTbDrugs(memberCbacDto.getTakingAntiTbDrugs());
        memberCbacDetail.setDifficultyHoldingObjects(memberCbacDto.getDifficultyHoldingObjects());
        memberCbacDetail.setSensationLossPalm(memberCbacDto.getSensationLossPalm());
        memberCbacDetail.setFamilyMemberSufferingFromTb(memberCbacDto.getFamilyMemberSufferingFromTb());
        memberCbacDetail.setHistoryOfTb(memberCbacDto.getHistoryOfTb());
        memberCbacDetail.setLumpInBreast(memberCbacDto.getLumpInBreast());
        memberCbacDetail.setBleedingAfterMenopause(memberCbacDto.getBleedingAfterMenopause());
        memberCbacDetail.setNippleBloodStainedDischarge(memberCbacDto.getNippleBloodStainedDischarge());
        memberCbacDetail.setBleedingAfterIntercourse(memberCbacDto.getBleedingAfterIntercourse());
        memberCbacDetail.setChangeInSizeOfBreast(memberCbacDto.getChangeInSizeOfBreast());
        memberCbacDetail.setFoulVaginalDischarge(memberCbacDto.getFoulVaginalDischarge());
        memberCbacDetail.setBleedingBetweenPeriods(memberCbacDto.getBleedingBetweenPeriods());
        memberCbacDetail.setOccupationalExposure(memberCbacDto.getOccupationalExposure());
        memberCbacDetail.setScore(memberCbacDto.getScore());
        memberCbacDetail.setAgeAtMenarche(memberCbacDto.getAgeAtMenarche());
        memberCbacDetail.setMenopauseArrived(memberCbacDto.getMenopauseArrived());
        memberCbacDetail.setDurationOfMenoapuse(memberCbacDto.getDurationOfMenoapuse());
        memberCbacDetail.setPregnant(memberCbacDto.getPregnant());
        memberCbacDetail.setLactating(memberCbacDto.getLactating());
        memberCbacDetail.setRegularPeriods(memberCbacDto.getRegularPeriods());
        memberCbacDetail.setBleeding(memberCbacDto.getBleeding());
        memberCbacDetail.setAssociatedWith(memberCbacDto.getAssociatedWith());
        memberCbacDetail.setRemarks(memberCbacDto.getRemarks());
        memberCbacDetail.setDiagnosedForHypertension(memberCbacDto.getDiagnosedForHypertension());
        memberCbacDetail.setUnderTreatementForHypertension(memberCbacDto.getUnderTreatementForHypertension());
        memberCbacDetail.setDiagnosedForDiabetes(memberCbacDto.getDiagnosedForDiabetes());
        memberCbacDetail.setUnderTreatementForDiabetes(memberCbacDto.getUnderTreatementForDiabetes());
        memberCbacDetail.setDiagnosedForHeartDiseases(memberCbacDto.getDiagnosedForHeartDiseases());
        memberCbacDetail.setUnderTreatementForHeartDiseases(memberCbacDto.getUnderTreatementForHeartDiseases());
        memberCbacDetail.setDiagnosedForStroke(memberCbacDto.getDiagnosedForStroke());
        memberCbacDetail.setUnderTreatementForStroke(memberCbacDto.getUnderTreatementForStroke());
        memberCbacDetail.setDiagnosedForKidneyFailure(memberCbacDto.getDiagnosedForKidneyFailure());
        memberCbacDetail.setUnderTreatementForKidneyFailure(memberCbacDto.getUnderTreatementForKidneyFailure());
        memberCbacDetail.setDiagnosedForNonHealingWound(memberCbacDto.getDiagnosedForNonHealingWound());
        memberCbacDetail.setUnderTreatementForNonHealingWound(memberCbacDto.getUnderTreatementForNonHealingWound());
        memberCbacDetail.setDiagnosedForCOPD(memberCbacDto.getDiagnosedForCOPD());
        memberCbacDetail.setUnderTreatementForCOPD(memberCbacDto.getUnderTreatementForCOPD());
        memberCbacDetail.setDiagnosedForAsthama(memberCbacDto.getDiagnosedForAsthama());
        memberCbacDetail.setUnderTreatementForAsthama(memberCbacDto.getUnderTreatementForAsthama());
        memberCbacDetail.setDiagnosedForOralCancer(memberCbacDto.getDiagnosedForOralCancer());
        memberCbacDetail.setUnderTreatementForOralCancer(memberCbacDto.getUnderTreatementForOralCancer());
        memberCbacDetail.setDiagnosedForBreastCancer(memberCbacDto.getDiagnosedForBreastCancer());
        memberCbacDetail.setUnderTreatementForBreastCancer(memberCbacDto.getUnderTreatementForBreastCancer());
        memberCbacDetail.setDiagnosedForCervicalCancer(memberCbacDto.getDiagnosedForCervicalCancer());
        memberCbacDetail.setUnderTreatementForCervicalCancer(memberCbacDto.getUnderTreatementForCervicalCancer());
        memberCbacDetail.setHeight(memberCbacDto.getHeight());
        memberCbacDetail.setWeight(memberCbacDto.getWeight());
        memberCbacDetail.setBmi(memberCbacDto.getBmi());
        memberCbacDetail.setLmp(memberCbacDto.getLmp());
        memberCbacDetail.setDoneBy(memberCbacDto.getDoneBy());
        memberCbacDetail.setId(memberCbacDto.getId());
        memberCbacDetail.setMobileStartDate(new Date(-1L));
        memberCbacDetail.setMobileEndDate(new Date(-1L));
        memberCbacDetail.setDoneOn(new Date());
        memberCbacDetail.setReferralId(memberCbacDto.getReferralId());
        return memberCbacDetail;
    }

    public static MemberComplaintsDto entityToDtoForComplaints(MemberComplaints memberComplaints) {
        MemberComplaintsDto complaintsDto = new MemberComplaintsDto();
        complaintsDto.setComplaint(memberComplaints.getComplaint());
        complaintsDto.setId(memberComplaints.getId());
        complaintsDto.setCreatedBy(memberComplaints.getCreatedBy());
        complaintsDto.setCreatedOn(memberComplaints.getCreatedOn());
        complaintsDto.setModifiedBy(memberComplaints.getModifiedBy());
        complaintsDto.setModifiedOn(memberComplaints.getModifiedOn());
        complaintsDto.setMemberId(memberComplaints.getMemberId());

        return complaintsDto;
    }

    public static MemberComplaints dtoToEntityForComplaints(MemberComplaintsDto complaintsDto) {
        MemberComplaints memberComplaints = new MemberComplaints();
        memberComplaints.setComplaint(complaintsDto.getComplaint());
        memberComplaints.setId(complaintsDto.getId());
        memberComplaints.setCreatedBy(complaintsDto.getCreatedBy());
        memberComplaints.setCreatedOn(complaintsDto.getCreatedOn());
        memberComplaints.setModifiedBy(complaintsDto.getModifiedBy());
        memberComplaints.setModifiedOn(complaintsDto.getModifiedOn());
        memberComplaints.setMemberId(complaintsDto.getMemberId());

        return memberComplaints;
    }

    public static MemberDiseaseDiagnosis convertDtoToEntityForDiagnosis(MemberDiagnosisDto diagnosisDto) {
        MemberDiseaseDiagnosis mdd = new MemberDiseaseDiagnosis();
        mdd.setDiagnosis(diagnosisDto.getDiagnosis());
        mdd.setStatus(diagnosisDto.getStatus());
        mdd.setRemarks(diagnosisDto.getRemarks());
        mdd.setMemberId(diagnosisDto.getMemberId());
        mdd.setId(diagnosisDto.getId());
        mdd.setDiagnosedOn(diagnosisDto.getDiagnosedOn());
        mdd.setDiseaseCode(diagnosisDto.getDiseaseCode());
        mdd.setReadings(diagnosisDto.getReadings());
        return mdd;

    }

    public static List<MemberDiseaseMedicine> convertDtoToEntityForMedicines(MemberDiagnosisDto diagnosisDto) {
        List<MemberDiseaseMedicine> mdms = new LinkedList<>();
        if (diagnosisDto.getMedicineIds() != null && !diagnosisDto.getMedicineIds().isEmpty()) {
            for (Integer id : diagnosisDto.getMedicineIds()) {
                MemberDiseaseMedicine mdm = new MemberDiseaseMedicine();
                mdm.setMedicineId(id);
                mdm.setMemberId(diagnosisDto.getMemberId());
                mdm.setDiseaseCode(diagnosisDto.getDiseaseCode());
                mdm.setDiagnosedOn(diagnosisDto.getDiagnosedOn());
                mdms.add(mdm);
            }
        }
        return mdms;
    }

    public static MemberReferral convertDtoToEntityForReferral(MemberReferralDto memberReferralDto) {
        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setMemberId(memberReferralDto.getMemberId());
        memberReferral.setDiseaseCode(memberReferralDto.getDiseaseCode());
        memberReferral.setReferredFrom(ReferralPlace.MO);
        memberReferral.setReferredBy(memberReferralDto.getReferredBy());
        memberReferral.setReferredOn(memberReferralDto.getReferredOn());
        memberReferral.setReferredTo(memberReferralDto.getReferredTo());
        memberReferral.setLocationId(memberReferralDto.getLocationId());
        memberReferral.setReason(memberReferralDto.getReason());
        memberReferral.setState(memberReferralDto.getState());
        memberReferral.setHealthInfraId(memberReferralDto.getHealthInfraId());
        return memberReferral;
    }

    public static MemberDiseaseFollowup dtoToEntityForFollowup(MemberDiseaseFollowupDto memberDiseaseFollowupDto) {
        MemberDiseaseFollowup memberDiseaseFollowup = new MemberDiseaseFollowup();
        memberDiseaseFollowup.setFollowupDate(memberDiseaseFollowupDto.getFollowupDate());
        memberDiseaseFollowup.setDiseaseCode(memberDiseaseFollowupDto.getDiseaseCode());
        memberDiseaseFollowup.setMemberId(memberDiseaseFollowupDto.getMemberId());
        memberDiseaseFollowup.setId(memberDiseaseFollowupDto.getId());
        memberDiseaseFollowup.setCreatedBy(memberDiseaseFollowupDto.getCreatedBy());
        memberDiseaseFollowup.setCreatedOn(memberDiseaseFollowupDto.getCreatedOn());
        memberDiseaseFollowup.setReferralFrom(memberDiseaseFollowupDto.getReferralFrom());
        memberDiseaseFollowup.setHealthInfraId(memberDiseaseFollowupDto.getHealthInfraId());
        return memberDiseaseFollowup;
    }
}
