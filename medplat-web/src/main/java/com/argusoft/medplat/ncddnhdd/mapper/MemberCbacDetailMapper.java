/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.mapper;

import com.argusoft.medplat.ncddnhdd.dto.MemberCbacDetailDataBean;
import com.argusoft.medplat.ncddnhdd.model.MemberCbacDetail;

/**
 *
 * @author prateek
 */
public class MemberCbacDetailMapper {

    public static MemberCbacDetailDataBean convertMemberCbacDetailEntityToDataBean(MemberCbacDetail memberCbacDetail) {
        MemberCbacDetailDataBean memberCbacDetailDataBean = new MemberCbacDetailDataBean();
        memberCbacDetailDataBean.setActualId(memberCbacDetail.getId());
        memberCbacDetailDataBean.setMemberId(memberCbacDetail.getMemberId());
        memberCbacDetailDataBean.setFamilyId(memberCbacDetail.getFamilyId());
        memberCbacDetailDataBean.setSmokeOrConsumeGutka(memberCbacDetail.getSmokeOrConsumeGutka());
        memberCbacDetailDataBean.setWaist(memberCbacDetail.getWaist());
        memberCbacDetailDataBean.setConsumeAlcoholDaily(memberCbacDetail.getConsumeAlcoholDaily());
        memberCbacDetailDataBean.setPhysicalActivity150Min(memberCbacDetail.getPhysicalActivity150Min());
        memberCbacDetailDataBean.setBpDiabetesHeartHistory(memberCbacDetail.getBpDiabetesHeartHistory());
        memberCbacDetailDataBean.setShortnessOfBreath(memberCbacDetail.getShortnessOfBreath());
        memberCbacDetailDataBean.setFitsHistory(memberCbacDetail.getFitsHistory());
        memberCbacDetailDataBean.setTwoWeeksCoughing(memberCbacDetail.getTwoWeeksCoughing());
        memberCbacDetailDataBean.setMouthOpeningDifficulty(memberCbacDetail.getMouthOpeningDifficulty());
        memberCbacDetailDataBean.setBloodInSputum(memberCbacDetail.getBloodInSputum());
        memberCbacDetailDataBean.setTwoWeeksUlcersInMouth(memberCbacDetail.getTwoWeeksUlcersInMouth());
        memberCbacDetailDataBean.setTwoWeeksFever(memberCbacDetail.getTwoWeeksFever());
        memberCbacDetailDataBean.setChangeInToneOfVoice(memberCbacDetail.getChangeInToneOfVoice());
        memberCbacDetailDataBean.setLossOfWeight(memberCbacDetail.getLossOfWeight());
        memberCbacDetailDataBean.setPatchOnSkin(memberCbacDetail.getPatchOnSkin());
        memberCbacDetailDataBean.setNightSweats(memberCbacDetail.getNightSweats());
        memberCbacDetailDataBean.setTakingAntiTbDrugs(memberCbacDetail.getTakingAntiTbDrugs());
        memberCbacDetailDataBean.setDifficultyHoldingObjects(memberCbacDetail.getDifficultyHoldingObjects());
        memberCbacDetailDataBean.setSensationLossPalm(memberCbacDetail.getSensationLossPalm());
        memberCbacDetailDataBean.setFamilyMemberSufferingFromTb(memberCbacDetail.getFamilyMemberSufferingFromTb());
        memberCbacDetailDataBean.setHistoryOfTb(memberCbacDetail.getHistoryOfTb());
        memberCbacDetailDataBean.setLumpInBreast(memberCbacDetail.getLumpInBreast());
        memberCbacDetailDataBean.setBleedingAfterMenopause(memberCbacDetail.getBleedingAfterMenopause());
        memberCbacDetailDataBean.setNippleBloodStainedDischarge(memberCbacDetail.getNippleBloodStainedDischarge());
        memberCbacDetailDataBean.setBleedingAfterIntercourse(memberCbacDetail.getBleedingAfterIntercourse());
        memberCbacDetailDataBean.setChangeInSizeOfBreast(memberCbacDetail.getChangeInSizeOfBreast());
        memberCbacDetailDataBean.setFoulVaginalDischarge(memberCbacDetail.getFoulVaginalDischarge());
        memberCbacDetailDataBean.setBleedingBetweenPeriods(memberCbacDetail.getBleedingBetweenPeriods());
        memberCbacDetailDataBean.setOccupationalExposure(memberCbacDetail.getOccupationalExposure());
        memberCbacDetailDataBean.setScore(memberCbacDetail.getScore());
        memberCbacDetailDataBean.setAgeAtMenarche(memberCbacDetail.getAgeAtMenarche());
        memberCbacDetailDataBean.setMenopauseArrived(memberCbacDetail.getMenopauseArrived());
        memberCbacDetailDataBean.setDurationOfMenoapuse(memberCbacDetail.getDurationOfMenoapuse());
        memberCbacDetailDataBean.setPregnant(memberCbacDetail.getPregnant());
        memberCbacDetailDataBean.setLactating(memberCbacDetail.getLactating());
        memberCbacDetailDataBean.setRegularPeriods(memberCbacDetail.getRegularPeriods());
        memberCbacDetailDataBean.setBleeding(memberCbacDetail.getBleeding());
        memberCbacDetailDataBean.setAssociatedWith(memberCbacDetail.getAssociatedWith());
        memberCbacDetailDataBean.setRemarks(memberCbacDetail.getRemarks());
        memberCbacDetailDataBean.setDiagnosedForHypertension(memberCbacDetail.getDiagnosedForHypertension());
        memberCbacDetailDataBean.setUnderTreatementForHypertension(memberCbacDetail.getUnderTreatementForHypertension());
        memberCbacDetailDataBean.setDiagnosedForDiabetes(memberCbacDetail.getDiagnosedForDiabetes());
        memberCbacDetailDataBean.setUnderTreatementForDiabetes(memberCbacDetail.getUnderTreatementForDiabetes());
        memberCbacDetailDataBean.setDiagnosedForHeartDiseases(memberCbacDetail.getDiagnosedForHeartDiseases());
        memberCbacDetailDataBean.setUnderTreatementForHeartDiseases(memberCbacDetail.getUnderTreatementForHeartDiseases());
        memberCbacDetailDataBean.setDiagnosedForStroke(memberCbacDetail.getDiagnosedForStroke());
        memberCbacDetailDataBean.setUnderTreatementForStroke(memberCbacDetail.getUnderTreatementForStroke());
        memberCbacDetailDataBean.setDiagnosedForKidneyFailure(memberCbacDetail.getDiagnosedForKidneyFailure());
        memberCbacDetailDataBean.setUnderTreatementForKidneyFailure(memberCbacDetail.getUnderTreatementForKidneyFailure());
        memberCbacDetailDataBean.setDiagnosedForNonHealingWound(memberCbacDetail.getDiagnosedForNonHealingWound());
        memberCbacDetailDataBean.setUnderTreatementForNonHealingWound(memberCbacDetail.getUnderTreatementForNonHealingWound());
        memberCbacDetailDataBean.setDiagnosedForCOPD(memberCbacDetail.getDiagnosedForCOPD());
        memberCbacDetailDataBean.setUnderTreatementForCOPD(memberCbacDetail.getUnderTreatementForCOPD());
        memberCbacDetailDataBean.setDiagnosedForAsthama(memberCbacDetail.getDiagnosedForAsthama());
        memberCbacDetailDataBean.setUnderTreatementForAsthama(memberCbacDetail.getUnderTreatementForAsthama());
        memberCbacDetailDataBean.setDiagnosedForOralCancer(memberCbacDetail.getDiagnosedForOralCancer());
        memberCbacDetailDataBean.setUnderTreatementForOralCancer(memberCbacDetail.getUnderTreatementForOralCancer());
        memberCbacDetailDataBean.setDiagnosedForBreastCancer(memberCbacDetail.getDiagnosedForBreastCancer());
        memberCbacDetailDataBean.setUnderTreatementForBreastCancer(memberCbacDetail.getUnderTreatementForBreastCancer());
        memberCbacDetailDataBean.setDiagnosedForCervicalCancer(memberCbacDetail.getDiagnosedForCervicalCancer());
        memberCbacDetailDataBean.setUnderTreatementForCervicalCancer(memberCbacDetail.getUnderTreatementForCervicalCancer());
        memberCbacDetailDataBean.setHeight(memberCbacDetail.getHeight());
        memberCbacDetailDataBean.setWeight(memberCbacDetail.getWeight());
        memberCbacDetailDataBean.setBmi(memberCbacDetail.getBmi());
        memberCbacDetailDataBean.setLmp(memberCbacDetail.getLmp());
        memberCbacDetailDataBean.setModifiedOn(memberCbacDetail.getModifiedOn());

        return memberCbacDetailDataBean;
    }
}
