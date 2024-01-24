/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.mapper;


import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Mapper for member details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 11:00 AM
 */
public class MemberDetailMapper {
    private MemberDetailMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert hypertension entity of member into details.
     *
     * @param hypertensionDetail Entity of hypertension.
     * @return Returns details of hypertension.
     */
    public static MemberHyperTensionDto entityToDtoForHyperTension(MemberHypertensionDetail hypertensionDetail) {

        MemberHyperTensionDto memberHyperTensionDto = new MemberHyperTensionDto();
        memberHyperTensionDto.setId(hypertensionDetail.getId());
        memberHyperTensionDto.setFlag(hypertensionDetail.getFlag());
        memberHyperTensionDto.setDiastolicBloodPressure(hypertensionDetail.getDiastolicBp());
        memberHyperTensionDto.setHeartRate(hypertensionDetail.getPulseRate());
        memberHyperTensionDto.setSystolicBloodPressure(hypertensionDetail.getSystolicBp());
        memberHyperTensionDto.setScreeningDate(hypertensionDetail.getScreeningDate());
        memberHyperTensionDto.setMemberId(hypertensionDetail.getMemberId());
        memberHyperTensionDto.setLocationId(hypertensionDetail.getLocationId());
        memberHyperTensionDto.setFamilyId(hypertensionDetail.getFamilyId());
        memberHyperTensionDto.setDoneBy(hypertensionDetail.getDoneBy());
        memberHyperTensionDto.setDoesSuffering(hypertensionDetail.getDoesSuffering());
        memberHyperTensionDto.setHealthInfraId(hypertensionDetail.getHealthInfraId());
        if(hypertensionDetail.getStatus() != null){
            if(hypertensionDetail.getStatus().equals("CONTROLLED")){
                memberHyperTensionDto.setStatus(Status.CONTROLLED);
            }
            else if(hypertensionDetail.getStatus().equals("UNCONTROLLED")){
                memberHyperTensionDto.setStatus(Status.UNCONTROLLED);
            }
            else{
            }
        }
        memberHyperTensionDto.setDiagnosedEarlier(hypertensionDetail.getDiagnosedEarlier());
        memberHyperTensionDto.setTakeMedicine(hypertensionDetail.getTakeMedicine());
        memberHyperTensionDto.setPedalOedema(hypertensionDetail.getPedalOedema());
        memberHyperTensionDto.setCreatedBy(hypertensionDetail.getCreatedBy());
        memberHyperTensionDto.setCreatedOn(hypertensionDetail.getCreatedOn());
        memberHyperTensionDto.setHmisHealthInfraId(hypertensionDetail.getHmisHealthInfraId());
        memberHyperTensionDto.setCurrentlyUnderTreatment(hypertensionDetail.getCurrentlyUnderTreatement());

        return memberHyperTensionDto;
    }

    /**
     * Convert hypertension details of member into entity.
     *
     * @param hyperTensionDto Details of hypertension.
     * @return Returns entity of hypertension.
     */
    public static MemberHypertensionDetail dtoToEntityForHyperTension(MemberHyperTensionDto hyperTensionDto) {

        MemberHypertensionDetail memberHypertensionDetail = new MemberHypertensionDetail();
        memberHypertensionDetail.setDiastolicBp(hyperTensionDto.getDiastolicBloodPressure());
        memberHypertensionDetail.setPulseRate(hyperTensionDto.getHeartRate());
        memberHypertensionDetail.setFlag(hyperTensionDto.getFlag());
        memberHypertensionDetail.setSystolicBp(hyperTensionDto.getSystolicBloodPressure());
        memberHypertensionDetail.setScreeningDate(hyperTensionDto.getScreeningDate());
        memberHypertensionDetail.setMemberId(hyperTensionDto.getMemberId());
        memberHypertensionDetail.setBilateralClear(hyperTensionDto.getBilateralClear());
        memberHypertensionDetail.setBilateralBasalCrepitation(hyperTensionDto.getBilateralBasalCrepitation());
        memberHypertensionDetail.setIsRegularRythm(hyperTensionDto.getIsRegularRythm());
        memberHypertensionDetail.setMurmur(hyperTensionDto.getMurmur());
        memberHypertensionDetail.setRhonchi(hyperTensionDto.getRhonchi());
        memberHypertensionDetail.setIsRegularRythm(hyperTensionDto.getIsRegularRythm());
        memberHypertensionDetail.setDoesSuffering(hyperTensionDto.getDoesSuffering());
        memberHypertensionDetail.setLocationId(hyperTensionDto.getLocationId());
        memberHypertensionDetail.setFamilyId(hyperTensionDto.getFamilyId());
        memberHypertensionDetail.setDoneBy(hyperTensionDto.getDoneBy());
        memberHypertensionDetail.setMobileStartDate(new Date(-1L));
        memberHypertensionDetail.setMobileEndDate(new Date(-1L));
        memberHypertensionDetail.setDoneOn(new Date());
        memberHypertensionDetail.setHealthInfraId((hyperTensionDto.getHealthInfraId()));
        memberHypertensionDetail.setStatus(hyperTensionDto.getStatus().toString());
        memberHypertensionDetail.setDiagnosedEarlier(hyperTensionDto.getDiagnosedEarlier());
        memberHypertensionDetail.setTakeMedicine(hyperTensionDto.getTakeMedicine());
        memberHypertensionDetail.setPedalOedema(hyperTensionDto.getPedalOedema());
        return memberHypertensionDetail;
    }

    /**
     * Convert cervical details of member into entity.
     *
     * @param cervicalDto Details of cervical.
     * @return Returns entity of cervical.
     */
//    public static MemberCervicalDetail dtoToEntityForCervical(MemberCervicalDto cervicalDto) {
//        MemberCervicalDetail memberCervicalDetail = new MemberCervicalDetail();
//        memberCervicalDetail.setExternalGenitaliaHealthy(cervicalDto.getExternalGenitaliaHealthy());
//        memberCervicalDetail.setPolyp(cervicalDto.getPolyp());
//        memberCervicalDetail.setEctopy(cervicalDto.getEctopy());
//        memberCervicalDetail.setHypertrophy(cervicalDto.getHypertrophy());
//        memberCervicalDetail.setExcessiveDischarge(cervicalDto.getExcessiveDischarge());
//        memberCervicalDetail.setProlapseUterus(cervicalDto.getProlapseUterus());
//        memberCervicalDetail.setConsultantFlag(cervicalDto.getConsultantFlag());
//        memberCervicalDetail.setBleedsOnTouch(cervicalDto.getBleedsOnTouch());
//        memberCervicalDetail.setUnhealthyCervix(cervicalDto.getUnhealthyCervix());
//        memberCervicalDetail.setSuspiciousLookingCervix(cervicalDto.getSuspiciousLookingCervix());
//        memberCervicalDetail.setFrankMalignancy(cervicalDto.getFrankMalignancy());
//        memberCervicalDetail.setOtherSymptoms(cervicalDto.getOtherSymptoms());
//        memberCervicalDetail.setViaExamPoints(cervicalDto.getViaExamPoints());
//        memberCervicalDetail.setOtherSymptomsDescription(cervicalDto.getOtherSymptomsDescription());
//        memberCervicalDetail.setOtherDesc(cervicalDto.getOtherDesc());
//        memberCervicalDetail.setViaExam(cervicalDto.getViaExam());
//        if (Objects.nonNull(cervicalDto.getPapsmearTest())) {
//            memberCervicalDetail.setPapsmearTest(cervicalDto.getPapsmearTest());
//        }
//        memberCervicalDetail.setViaTest(cervicalDto.getViaTest());
//        memberCervicalDetail.setScreeningDate(cervicalDto.getScreeningDate());
//        memberCervicalDetail.setMemberId(cervicalDto.getMemberId());
//        memberCervicalDetail.setLocationId(cervicalDto.getLocationId());
//        memberCervicalDetail.setFamilyId(cervicalDto.getFamilyId());
//        memberCervicalDetail.setDoneBy(cervicalDto.getDoneBy());
//        memberCervicalDetail.setMobileStartDate(new Date(-1L));
//        memberCervicalDetail.setMobileEndDate(new Date(-1L));
//        memberCervicalDetail.setDoneOn(new Date());
//        memberCervicalDetail.setBimanualExamination(cervicalDto.getBimanualExamination());
//        memberCervicalDetail.setOtherFinding(cervicalDto.getOtherFinding());
//        memberCervicalDetail.setOtherFindingDescription(cervicalDto.getOtherFindingDescription());
//        memberCervicalDetail.setDoesSuffering(cervicalDto.getDoesSuffering());
//        memberCervicalDetail.setStatus(cervicalDto.getStatus().toString());
//        memberCervicalDetail.setHealthInfraId(cervicalDto.getHealthInfraId());
//        memberCervicalDetail.setTakeMedicine(cervicalDto.getTakeMedicine());
//        return memberCervicalDetail;
//
//    }

    /**
     * Convert diabetes entity of member into dto.
     *
     * @param memberDiabetesDetail Entity of diabetes.
     * @return Returns details of diabetes.
     */
    public static MemberDiabetesDto entityToDtoForDiabetes(MemberDiabetesDetail memberDiabetesDetail) {
        MemberDiabetesDto memberDiabetesDto = new MemberDiabetesDto();
        memberDiabetesDto.setId(memberDiabetesDetail.getId());
        memberDiabetesDto.setFastingBloodSugar(memberDiabetesDetail.getFastingBloodSugar());
        memberDiabetesDto.setPostPrandialBloodSugar(memberDiabetesDetail.getPostPrandialBloodSugar());
        memberDiabetesDto.setBloodSugar(memberDiabetesDetail.getBloodSugar());
        memberDiabetesDto.setFlag(memberDiabetesDetail.getFlag());
        memberDiabetesDto.setDka(memberDiabetesDetail.getDka());
        memberDiabetesDto.setHba1c(memberDiabetesDetail.getHba1c());
        memberDiabetesDto.setScreeningDate(memberDiabetesDetail.getScreeningDate());
        memberDiabetesDto.setMemberId(memberDiabetesDetail.getMemberId());
        memberDiabetesDto.setLocationId(memberDiabetesDetail.getLocationId());
        memberDiabetesDto.setFamilyId(memberDiabetesDetail.getFamilyId());
        memberDiabetesDto.setDoneBy(memberDiabetesDetail.getDoneBy());
        memberDiabetesDto.setUrineSugar(memberDiabetesDetail.getUrineSugar());
        memberDiabetesDto.setHealthInfraId(memberDiabetesDetail.getHealthInfraId());
        memberDiabetesDto.setDoesSuffering(memberDiabetesDetail.isDoesSuffering());
        if(memberDiabetesDetail.getStatus() != null){
            if(memberDiabetesDetail.getStatus().equals("CONTROLLED")){
                memberDiabetesDto.setStatus(Status.CONTROLLED);
            }
            else if(memberDiabetesDetail.getStatus().equals("UNCONTROLLED")){
                memberDiabetesDto.setStatus(Status.UNCONTROLLED);
            }
            else{
            }
        }
        memberDiabetesDto.setEarlierDiabetesDiagnosis(memberDiabetesDetail.getEarlierDiabetesDiagnosis());
        memberDiabetesDto.setMeasurementType(memberDiabetesDetail.getMeasurementType());
        memberDiabetesDto.setTakeMedicine(memberDiabetesDetail.getTakeMedicine());
        memberDiabetesDto.setHeight(memberDiabetesDetail.getHeight());
        memberDiabetesDto.setWeight(memberDiabetesDetail.getWeight());
        memberDiabetesDto.setBmi(memberDiabetesDetail.getBmi());
        memberDiabetesDto.setCurrentlyUnderTreatment(memberDiabetesDetail.getCurrentlyUnderTreatment());
        return memberDiabetesDto;
    }

    public static MemberInitialAssessmentDetail dtotoEntityForInitialDetail(MemberInitialAssessmentDto memberInitialAssessmentDto){
        MemberInitialAssessmentDetail memberInitialAssessmentDetail=new MemberInitialAssessmentDetail();
        memberInitialAssessmentDetail.setMemberId(memberInitialAssessmentDto.getMemberId());
        memberInitialAssessmentDetail.setLocationId(memberInitialAssessmentDto.getLocationId());
        memberInitialAssessmentDetail.setFamilyId(memberInitialAssessmentDto.getFamilyId());
        memberInitialAssessmentDetail.setScreeningDate(memberInitialAssessmentDto.getScreeningDate());
        memberInitialAssessmentDetail.setDoneBy(memberInitialAssessmentDto.getDoneBy());
        memberInitialAssessmentDetail.setDoneOn(new Date());
        memberInitialAssessmentDetail.setConsultantFlag(memberInitialAssessmentDto.getConsultantFlag());
        memberInitialAssessmentDetail.setMobileStartDate(new Date(-1L));
        memberInitialAssessmentDetail.setMobileEndDate(new Date(-1L));
        memberInitialAssessmentDetail.setExcessThirst(memberInitialAssessmentDto.getExcessThirst());
        memberInitialAssessmentDetail.setExcessHunger(memberInitialAssessmentDto.getExcessHunger());
        memberInitialAssessmentDetail.setExcessUrination(memberInitialAssessmentDto.getExcessUrination());
        memberInitialAssessmentDetail.setBreathlessness(memberInitialAssessmentDto.getBreathlessness());
        memberInitialAssessmentDetail.setChangeInDietaryHabits(memberInitialAssessmentDto.getChangeInDietaryHabits());
        memberInitialAssessmentDetail.setDelayedHealingOfWounds(memberInitialAssessmentDto.getDelayedHealingOfWounds());
        memberInitialAssessmentDetail.setLimpness(memberInitialAssessmentDto.getLimpness());
        memberInitialAssessmentDetail.setIntermittentClaudication(memberInitialAssessmentDto.getIntermittentClaudication());
        memberInitialAssessmentDetail.setAngina(memberInitialAssessmentDto.getAngina());
        memberInitialAssessmentDetail.setSignificantEdema(memberInitialAssessmentDto.getSignificantEdema());
        memberInitialAssessmentDetail.setSuddenVisualDisturbances(memberInitialAssessmentDto.getSuddenVisualDisturbances());
        memberInitialAssessmentDetail.setRecurrentSkinGUI(memberInitialAssessmentDto.getRecurrentSkinGUI());
        memberInitialAssessmentDetail.setHeight(memberInitialAssessmentDto.getHeight());
        memberInitialAssessmentDetail.setWeight(memberInitialAssessmentDto.getWeight());
        memberInitialAssessmentDetail.setWaistCircumference(memberInitialAssessmentDto.getWaistCircumference());
        memberInitialAssessmentDetail.setBmi(memberInitialAssessmentDto.getBmi());
        memberInitialAssessmentDetail.setFormType(memberInitialAssessmentDto.getFormType());
        memberInitialAssessmentDetail.setHealthInfraId(memberInitialAssessmentDto.getHealthInfraId());
        memberInitialAssessmentDetail.setOtherDisease(memberInitialAssessmentDto.getOtherDisease());
        return memberInitialAssessmentDetail;
    }
    /**
     * Convert diabetes details of member into entity.
     *
     * @param memberGeneralDto Diabetes details of member.
     * @return Returns diabetes entity of member.
     */
    public static MemberGeneralDetail dtoToEntityForGeneral(MemberGeneralDto memberGeneralDto){
        MemberGeneralDetail memberGeneralDetail=new MemberGeneralDetail();
        memberGeneralDetail.setMemberId(memberGeneralDto.getMemberId());
        memberGeneralDetail.setLocationId(memberGeneralDto.getLocationId());
        memberGeneralDetail.setFamilyId(memberGeneralDto.getFamilyId());
        memberGeneralDetail.setDoneBy(memberGeneralDto.getDoneBy());
        memberGeneralDetail.setDoneOn(new Date());
        memberGeneralDetail.setConsultantFlag(memberGeneralDto.getConsultantFlag());
        memberGeneralDetail.setScreeningDate(memberGeneralDto.getScreeningDate());
        memberGeneralDetail.setMobileEndDate(new Date(-1L));
        memberGeneralDetail.setMobileStartDate(new Date(-1L));
        memberGeneralDetail.setClinicalObservation(memberGeneralDto.getClinicalObservation());
        memberGeneralDetail.setDiagnosis(memberGeneralDto.getDiagnosis());
        memberGeneralDetail.setSymptoms(memberGeneralDto.getSymptoms());
        memberGeneralDetail.setRemarks(memberGeneralDto.getRemarks());
        memberGeneralDetail.setRefferalPlace(memberGeneralDto.getRefferralPlace());
        memberGeneralDetail.setOtherDetails(memberGeneralDto.getOtherDetails());
        memberGeneralDetail.setDoesRequiredRef(memberGeneralDto.getDoesRequiredRef());
        memberGeneralDetail.setRefferralReason(memberGeneralDto.getRefferralReason());
        memberGeneralDetail.setComment(memberGeneralDto.getComment());
        memberGeneralDetail.setMarkReview(memberGeneralDto.getMarkReview());
        memberGeneralDetail.setFollowUpDate(memberGeneralDto.getFollowUpDate());
        memberGeneralDetail.setFollowupPlace(memberGeneralDto.getFollowupPlace());
        memberGeneralDetail.setHealthInfraId(memberGeneralDto.getHealthInfraId());
        memberGeneralDetail.setCategory(memberGeneralDto.getCategory());
        memberGeneralDetail.setTakeMedicine(memberGeneralDto.getTakeMedicine());
        return memberGeneralDetail;
    }
    public static MemberMentalHealthDetails dtoToEntityForMentalHealth(MemberMentalHealthDto memberMentalHealthDto) {
        MemberMentalHealthDetails memberMentalHealthDetails=new MemberMentalHealthDetails();
        memberMentalHealthDetails.setMemberId(memberMentalHealthDto.getMemberId());
        memberMentalHealthDetails.setLocationId(memberMentalHealthDto.getLocationId());
        memberMentalHealthDetails.setFamilyId(memberMentalHealthDto.getFamilyId());
        memberMentalHealthDetails.setDoneBy(memberMentalHealthDto.getDoneBy());
        memberMentalHealthDetails.setDoneOn(new Date());
        memberMentalHealthDetails.setFlag(memberMentalHealthDto.getFlag());
        memberMentalHealthDetails.setScreeningDate(memberMentalHealthDto.getScreeningDate());
        memberMentalHealthDetails.setMobileStartDate(new Date(-1L));
        memberMentalHealthDetails.setMobileEndDate(new Date(-1L));
        memberMentalHealthDetails.setTodayResult(memberMentalHealthDto.getTodayResult());
        memberMentalHealthDetails.setTalk(memberMentalHealthDto.getTalk());
        memberMentalHealthDetails.setSocialWork(memberMentalHealthDto.getSocialWork());
        memberMentalHealthDetails.setUnderstanding(memberMentalHealthDto.getUnderstanding());
        memberMentalHealthDetails.setOwnDailyWork(memberMentalHealthDto.getOwnDailyWork());
        memberMentalHealthDetails.setIsSuffering(memberMentalHealthDto.getIsSuffering());
        memberMentalHealthDetails.setSufferingEarlier(memberMentalHealthDto.getSufferingEarlier());
        memberMentalHealthDetails.setStatus(memberMentalHealthDto.getStatus().toString());
        memberMentalHealthDetails.setTakeMedicine(memberMentalHealthDto.getTakeMedicine());
        memberMentalHealthDetails.setDoesSuffering(memberMentalHealthDto.getDoesSuffering());
        return memberMentalHealthDetails;
    }

    public static DrugInventoryDetail dtoToEntityForDrugInventoryDetail(DrugInventoryDto drugInventoryDto){
        DrugInventoryDetail drugInventoryDetail=new DrugInventoryDetail();
        drugInventoryDetail.setId(drugInventoryDto.getId());
        drugInventoryDetail.setMemberId(drugInventoryDto.getMemberId());
        drugInventoryDetail.setHealthInfraId(drugInventoryDto.getHealthInfraId());
        drugInventoryDetail.setDoneBy(drugInventoryDto.getDoneBy());
        drugInventoryDetail.setMedicineId(drugInventoryDto.getMedicineId());
        drugInventoryDetail.setQuantityIssued(drugInventoryDto.getQuantityIssued());
        drugInventoryDetail.setIsIssued(drugInventoryDto.getIsIssued());
        drugInventoryDetail.setIsReceived(drugInventoryDto.getIsReceived());
        drugInventoryDetail.setQuantityReceived(drugInventoryDto.getQuantityReceived());
        drugInventoryDetail.setParentHealthId(drugInventoryDto.getParentHealthId());
        drugInventoryDetail.setMobileEndDate(new Date(-1L));
        drugInventoryDetail.setMobileStartDate(new Date(-1L));
        drugInventoryDetail.setIssuedDate(drugInventoryDto.getIssuedDate());
        drugInventoryDetail.setBalanceInHand(drugInventoryDto.getBalanceInHand());
        drugInventoryDetail.setIsReturn(drugInventoryDto.getIsReturn());
        return drugInventoryDetail;
    }

    public static DrugInventoryDto entityTOdtoForDrugInventoryDetail(DrugInventoryDetail drugInventoryDetail){
        DrugInventoryDto drugInventoryDto=new DrugInventoryDto();
        drugInventoryDto.setId(drugInventoryDetail.getId());
        drugInventoryDto.setIssuedDate(drugInventoryDetail.getIssuedDate());
        drugInventoryDto.setQuantityIssued(drugInventoryDetail.getQuantityIssued());
        drugInventoryDto.setQuantityReceived(drugInventoryDetail.getQuantityReceived());
//        drugInventoryDto.setMedicineName(drugInventoryDetail.getMedicineName());
        return drugInventoryDto;
    }

    public static MbbsMOReviewDetail dtoToEntityForMbbsMOReviewDetail(MbbsMOReviewDto mbbsMOReviewDto){
        MbbsMOReviewDetail mbbsMOReviewDetail=new MbbsMOReviewDetail();
        mbbsMOReviewDetail.setMemberId(mbbsMOReviewDto.getMemberId());
        mbbsMOReviewDetail.setLocationId(mbbsMOReviewDto.getLocationId());
        mbbsMOReviewDetail.setDoneBy(mbbsMOReviewDto.getDoneBy());
        mbbsMOReviewDetail.setMobileEndDate(new Date(-1L));
        mbbsMOReviewDetail.setMobileStartDate(new Date(-1L));
        mbbsMOReviewDetail.setApproved(mbbsMOReviewDto.getApproved());
        mbbsMOReviewDetail.setComment(mbbsMOReviewDto.getComment());
        return mbbsMOReviewDetail;
    }
    public static MemberDiabetesDetail dtoToEntityForDiabetes(MemberDiabetesDto memberDiabetesDto) {
        MemberDiabetesDetail memberDiabetesDetail = new MemberDiabetesDetail();
        memberDiabetesDetail.setFastingBloodSugar(memberDiabetesDto.getFastingBloodSugar());
        memberDiabetesDetail.setPostPrandialBloodSugar(memberDiabetesDto.getPostPrandialBloodSugar());
        memberDiabetesDetail.setBloodSugar(memberDiabetesDto.getBloodSugar());
        memberDiabetesDetail.setDka(memberDiabetesDto.getDka());
        memberDiabetesDetail.setFlag(memberDiabetesDto.getFlag());
        memberDiabetesDetail.setHba1c(memberDiabetesDto.getHba1c());
        memberDiabetesDetail.setPeripheralPulses(memberDiabetesDto.getPeripheralPulses());
        memberDiabetesDetail.setCallusesFeet(memberDiabetesDto.getCallusesFeet());
        memberDiabetesDetail.setUlcersFeet(memberDiabetesDto.getUlcersFeet());
        memberDiabetesDetail.setGangreneFeet(memberDiabetesDto.getGangreneFeet());
        memberDiabetesDetail.setProminentVeins(memberDiabetesDto.getProminentVeins());
        memberDiabetesDetail.setEdema(memberDiabetesDto.getEdema());
        memberDiabetesDetail.setAnyInjuries(memberDiabetesDto.getAnyInjuries());
        memberDiabetesDetail.setRegularRythmCardio(memberDiabetesDto.getRegularRythmCardio());
        memberDiabetesDetail.setSensoryLoss(memberDiabetesDto.getSensoryLoss());
        memberDiabetesDetail.setScreeningDate(memberDiabetesDto.getScreeningDate());
        memberDiabetesDetail.setMemberId(memberDiabetesDto.getMemberId());
        memberDiabetesDetail.setLocationId(memberDiabetesDto.getLocationId());
        memberDiabetesDetail.setFamilyId(memberDiabetesDto.getFamilyId());
        memberDiabetesDetail.setDoneBy(memberDiabetesDto.getDoneBy());
        memberDiabetesDetail.setMobileStartDate(new Date(-1L));
        memberDiabetesDetail.setMobileEndDate(new Date(-1L));
        memberDiabetesDetail.setDoneOn(new Date());
        memberDiabetesDetail.setStatus(memberDiabetesDto.getStatus().toString());
        memberDiabetesDetail.setUrineSugar(memberDiabetesDto.getUrineSugar());
        memberDiabetesDetail.setHealthInfraId(memberDiabetesDto.getHealthInfraId());
        memberDiabetesDetail.setDoesSuffering(memberDiabetesDto.getDoesSuffering());
        memberDiabetesDetail.setEarlierDiabetesDiagnosis(memberDiabetesDto.getEarlierDiabetesDiagnosis());
        memberDiabetesDetail.setMeasurementType(memberDiabetesDto.getMeasurementType());
        memberDiabetesDetail.setTakeMedicine(memberDiabetesDto.getTakeMedicine());
        memberDiabetesDetail.setHeight(memberDiabetesDto.getHeight());
        memberDiabetesDetail.setWeight(memberDiabetesDto.getWeight());
        memberDiabetesDetail.setBmi(memberDiabetesDto.getBmi());
        return memberDiabetesDetail;
    }

    /**
     * Convert breast details of member into entity.
     *
     * @param breastDto Breast details of member.
     * @return Returns breast entity of member.
     */
//    public static MemberBreastDetail dtoToEntityForBreast(MemberBreastDto breastDto) {
//        MemberBreastDetail memberBreastDetail = new MemberBreastDetail();
//        memberBreastDetail.setSizeChange(breastDto.getSizeChange());
//        memberBreastDetail.setNippleNotOnSameLevel(breastDto.getNippleNotOnSameLevel());
//        memberBreastDetail.setLymphadenopathy(breastDto.getLymphadenopathy());
//        memberBreastDetail.setVisualSkinRetraction(breastDto.getVisualSkinRetraction());
//        memberBreastDetail.setVisualDischargeFromNipple(breastDto.getVisualDischargeFromNipple());
//        memberBreastDetail.setAxillary(breastDto.getAxillary());
//        memberBreastDetail.setConsultantFlag(breastDto.getConsultantFlag());
//        memberBreastDetail.setSuperClavicularArea(breastDto.getSuperClavicularArea());
//        memberBreastDetail.setInfraClavicularArea(breastDto.getInfraClavicularArea());
//        memberBreastDetail.setSkinEdema(breastDto.getSkinEdema());
//        memberBreastDetail.setVisualUlceration(breastDto.getVisualUlceration());
//        memberBreastDetail.setVisualSkinDimplingRetraction(breastDto.getVisualSkinDimplingRetraction());
//        memberBreastDetail.setVisualNippleRetractionDistortion(breastDto.getVisualNippleRetractionDistortion());
//        memberBreastDetail.setVisualLumpInBreast(breastDto.getVisualLumpInBreast());
//        memberBreastDetail.setScreeningDate(breastDto.getScreeningDate());
//        memberBreastDetail.setMemberId(breastDto.getMemberId());
//        memberBreastDetail.setLocationId(breastDto.getLocationId());
//        memberBreastDetail.setFamilyId(breastDto.getFamilyId());
//        memberBreastDetail.setDoneBy(breastDto.getDoneBy());
//        memberBreastDetail.setMobileStartDate(new Date(-1L));
//        memberBreastDetail.setMobileEndDate(new Date(-1L));
//        memberBreastDetail.setDoneOn(new Date());
//        memberBreastDetail.setRetractionOfSkinFlag(breastDto.getVisualSkinRetraction()!=null);
//        memberBreastDetail.setDischargeFromNippleFlag(breastDto.getVisualDischargeFromNipple()!=null);
//        memberBreastDetail.setUlceration(breastDto.getVisualUlceration()!=null);
//        memberBreastDetail.setLumpInBreast(breastDto.getVisualLumpInBreast()!=null);
//        memberBreastDetail.setNippleRetractionDistortionFlag(breastDto.getVisualNippleRetractionDistortion()!=null);
//        memberBreastDetail.setSkinDimplingRetractionFlag(breastDto.getVisualSkinDimplingRetraction()!=null);
//        memberBreastDetail.setStatus(breastDto.getStatus().toString());
//        memberBreastDetail.setDoesSuffering(breastDto.getDoesSuffering());
//        memberBreastDetail.setHealthInfraId(breastDto.getHealthInfraId());
//        memberBreastDetail.setTakeMedicine(breastDto.getTakeMedicine());
//        return memberBreastDetail;
//
//    }

    /**
     * Convert oral details of member into entity.
     *
     * @param oralDto Oral details of member.
     * @return Returns oral entity of member.
     */
//    public static MemberOralDetail dtoToEntityForOral(MemberOralDto oralDto) {
//        MemberOralDetail memberOralDetail = new MemberOralDetail();
//        memberOralDetail.setDifficultyInOpeningMouth(oralDto.getDifficultyInOpeningMouth());
//        memberOralDetail.setDifficultyInSpicyFood(oralDto.getDifficultyInSpicyFood());
//        memberOralDetail.setOtherSymptoms(oralDto.getOtherSymptoms());
//        memberOralDetail.setWhiteRedPatchOralCavity(oralDto.getWhiteRedPatchOralCavity());
//        memberOralDetail.setVoiceChange(oralDto.getVoiceChange());
//        memberOralDetail.setThreeWeeksMouthUlcer(oralDto.getThreeWeeksMouthUlcer());
//        memberOralDetail.setRestrictedMouthOpening(oralDto.getRestrictedMouthOpening());
//        memberOralDetail.setLichenPlanus(oralDto.getLichenPlanus());
//        memberOralDetail.setSmokersPalate(oralDto.getSmokersPalate());
//        memberOralDetail.setSubmucousFibrosis(oralDto.getSubmucousFibrosis());
//        memberOralDetail.setAnyIssuesInMouth(oralDto.getAnyIssuesInMouth());
//        memberOralDetail.setWhitePatches(oralDto.getWhitePatches());
//        memberOralDetail.setFlag(oralDto.getFlag());
//        memberOralDetail.setRedPatches(oralDto.getRedPatches());
//        memberOralDetail.setNonHealingUlcers(oralDto.getNonHealingUlcers());
//        memberOralDetail.setGrowthOfRecentOrigins(oralDto.getGrowthOfRecentOrigins());
//        memberOralDetail.setScreeningDate(oralDto.getScreeningDate());
//        memberOralDetail.setMemberId(oralDto.getMemberId());
//        memberOralDetail.setLocationId(oralDto.getLocationId());
//        memberOralDetail.setFamilyId(oralDto.getFamilyId());
//        memberOralDetail.setDoneBy(oralDto.getDoneBy());
//        memberOralDetail.setMobileStartDate(new Date(-1L));
//        memberOralDetail.setMobileEndDate(new Date(-1L));
//        memberOralDetail.setDoneOn(new Date());
//        memberOralDetail.setDoesSuffering(oralDto.getDoesSuffering());
//        memberOralDetail.setHealthInfraId(oralDto.getHealthInfraId());
//        memberOralDetail.setStatus(oralDto.getStatus().toString());
//        memberOralDetail.setUlcer(oralDto.getUlcer());
//        memberOralDetail.setTakeMedicine(oralDto.getTakeMedicine());
//        return memberOralDetail;
//    }


    /**
     * Convert complaints details of member into entity.
     *
     * @param complaintsDto Complaint details of member.
     * @return Returns complaints entity of member.
     */
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

    /**
     * Convert diagnosis details of member into entity.
     *
     * @param diagnosisDto Diagnose details of member.
     * @return Returns diagnosis entity of member.
     */
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

    /**
     * Convert medicine details of member into entity.
     *
     * @param diagnosisDto Medicine details of member.
     * @return Returns medicine entity of member.
     */
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

    /**
     * Convert referral details of member into entity.
     *
     * @param memberReferralDto Referral details of member.
     * @return Return referral entity of member.
     */
    public static MemberReferral convertDtoToEntityForReferral(MemberReferralDto memberReferralDto) {
        MemberReferral memberReferral = new MemberReferral();
        memberReferral.setMemberId(memberReferralDto.getMemberId());
        memberReferral.setDiseaseCode(memberReferralDto.getDiseaseCode());
        memberReferral.setReferredFrom(memberReferralDto.getReferredFrom());
        memberReferral.setReferredBy(memberReferralDto.getReferredBy());
        memberReferral.setReferredOn(memberReferralDto.getReferredOn());
        memberReferral.setReferredTo(memberReferralDto.getReferredTo());
        memberReferral.setLocationId(memberReferralDto.getLocationId());
        memberReferral.setReason(memberReferralDto.getReason());
        memberReferral.setState(memberReferralDto.getState());
        memberReferral.setHealthInfraId(memberReferralDto.getHealthInfraId());
        return memberReferral;
    }

    /**
     * Convert follow up details of member into entity.
     *
     * @param memberDiseaseFollowupDto Follow up details of member.
     * @return Returns follow up entity of member.
     */
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


    public static MemberGeneralDto entityToDtoForGeneral(MemberGeneralDetail memberGeneralDetail) {
        MemberGeneralDto memberGeneralDto = new MemberGeneralDto();
        memberGeneralDto.setId(memberGeneralDetail.getId());
        memberGeneralDto.setCategory(memberGeneralDetail.getCategory());
        memberGeneralDto.setHealthInfraId(memberGeneralDetail.getHealthInfraId());
        memberGeneralDto.setSymptoms(memberGeneralDetail.getSymptoms());
        memberGeneralDto.setDiagnosis(memberGeneralDetail.getDiagnosis());
        memberGeneralDto.setClinicalObservation(memberGeneralDetail.getClinicalObservation());
        memberGeneralDto.setRemarks(memberGeneralDetail.getRemarks());
        memberGeneralDto.setDoesRequiredRef(memberGeneralDetail.getDoesRequiredRef());
        memberGeneralDto.setRefferralPlace(memberGeneralDetail.getRefferalPlace());
        memberGeneralDto.setRefferralReason(memberGeneralDetail.getRefferralReason());
        memberGeneralDto.setReferralId(memberGeneralDetail.getReferralId());
        memberGeneralDto.setFollowUpDate(memberGeneralDetail.getFollowUpDate());
        memberGeneralDto.setFollowupPlace(memberGeneralDetail.getFollowupPlace());
        memberGeneralDto.setMarkReview(memberGeneralDetail.getMarkReview());
        memberGeneralDto.setComment(memberGeneralDetail.getComment());
        memberGeneralDto.setOtherDetails(memberGeneralDetail.getOtherDetails());
        memberGeneralDto.setTakeMedicine(memberGeneralDetail.getTakeMedicine());
        return memberGeneralDto;
    }

    public static MemberInvestigationDetail dtoToEntityForInvestigation(MemberInvestigationDto memberInvestigationDto) {
        MemberInvestigationDetail memberInvestigationDetail = new MemberInvestigationDetail();
        memberInvestigationDetail.setMemberId(memberInvestigationDto.getMemberId());
        memberInvestigationDetail.setScreeningDate(memberInvestigationDto.getScreeningDate());
        memberInvestigationDetail.setDoneBy(memberInvestigationDto.getDoneBy());
        memberInvestigationDetail.setHealthInfraId(memberInvestigationDto.getHealthInfraId());
        return memberInvestigationDetail;
    }

    public static MOReviewDetail dtoToEntityForMOReviewDetail(MOReviewDto moReviewDto) {
        MOReviewDetail moReviewDetail = new MOReviewDetail();
        moReviewDetail.setId(moReviewDto.getId());
        moReviewDetail.setMemberId(moReviewDto.getMemberId());
        moReviewDetail.setLocationId(moReviewDto.getLocationId());
        moReviewDetail.setHealthInfraId(moReviewDto.getHealthInfraId());
        moReviewDetail.setScreeningDate(moReviewDto.getScreeningDate());
        moReviewDetail.setFollowupPlace(moReviewDto.getFollowupPlace());
        moReviewDetail.setFollowUpDate(moReviewDto.getFollowUpDate());
        moReviewDetail.setDoesRequiredRef(moReviewDto.getDoesRequiredRef());
        moReviewDetail.setRefferralPlace(moReviewDto.getRefferralPlace());
        moReviewDetail.setRefferralReason(moReviewDto.getRefferralReason());
        moReviewDetail.setComment(moReviewDto.getComment());
        moReviewDetail.setIsFollowup(moReviewDto.getIsFollowup());
        moReviewDetail.setDiseases(moReviewDto.getDisease());
        moReviewDetail.setOtherReason(moReviewDto.getOtherReason());
        return moReviewDetail;
    }

    public static MOReviewFollowupDetail dtoToEntityForMOReviewFollowupDetail(MOReviewFollowupDto moReviewFollowupDto){
        MOReviewFollowupDetail moReviewFollowupDetail = new MOReviewFollowupDetail();
        moReviewFollowupDetail.setId(moReviewFollowupDto.getId());
        moReviewFollowupDetail.setMemberId(moReviewFollowupDto.getMemberId());
        moReviewFollowupDetail.setLocationId(moReviewFollowupDto.getLocationId());
        moReviewFollowupDetail.setHealthInfraId(moReviewFollowupDto.getHealthInfraId());
        moReviewFollowupDetail.setScreeningDate(moReviewFollowupDto.getScreeningDate());
        moReviewFollowupDetail.setFollowupPlace(moReviewFollowupDto.getFollowupPlace());
        moReviewFollowupDetail.setFollowUpDate(moReviewFollowupDto.getFollowUpDate());
        moReviewFollowupDetail.setDoesRequiredRef(moReviewFollowupDto.getDoesRequiredRef());
        moReviewFollowupDetail.setRefferralPlace(moReviewFollowupDto.getRefferralPlace());
        moReviewFollowupDetail.setRefferralReason(moReviewFollowupDto.getRefferralReason());
        moReviewFollowupDetail.setComment(moReviewFollowupDto.getComment());
        moReviewFollowupDetail.setIsRemove(moReviewFollowupDto.getIsRemove());
        moReviewFollowupDetail.setDiseases(moReviewFollowupDto.getDisease());
        moReviewFollowupDetail.setOtherReason(moReviewFollowupDto.getOtherReason());
        return moReviewFollowupDetail;
    }

    public static NcdCVCForm cvcFormDtoToEntity (NcdCVCFormDto ncdCVCFormDto){
        NcdCVCForm ncdCVCForm = new NcdCVCForm();
        ncdCVCForm.setId(ncdCVCFormDto.getId());
        ncdCVCForm.setMemberId(ncdCVCFormDto.getMemberId());
        ncdCVCForm.setScreeningDate(ncdCVCFormDto.getScreeningDate());
        ncdCVCForm.setHealthInfraId(ncdCVCFormDto.getHealthInfraId());
        ncdCVCForm.setTakeMedicine(ncdCVCFormDto.getTakeMedicine());
        ncdCVCForm.setDoneBy(ncdCVCFormDto.getDoneBy());
        return ncdCVCForm;
    }

    public static NcdMemberMedicineDataBean convertMemberMedicineDetailEntityToDataBean(MemberDiseaseMedicine diseaseMedicine) {
        NcdMemberMedicineDataBean dataBean = new NcdMemberMedicineDataBean();
        dataBean.setMedicineId(diseaseMedicine.getMedicineId());
        dataBean.setDuration(diseaseMedicine.getDuration());
        dataBean.setFrequency(diseaseMedicine.getFrequency());
        dataBean.setQuantity(diseaseMedicine.getQuantity());
        dataBean.setSpecialInstruction(diseaseMedicine.getSpecialInstruction());
        dataBean.setExpiryDate(diseaseMedicine.getExpiryDate());

        return dataBean;
    }
}
