/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.mapper;

import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.model.MemberEntity;

/**
 * <p>
 * Mapper for member in order to convert dto to model or model to dto.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 11:00 AM
 */
public class MemberMapper {

    private MemberMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert member entity to dto.
     *
     * @param memberEntity Entity of member.
     * @return Returns details of member.
     */
    public static MemberDto getMemberDto(MemberEntity memberEntity) {
        if (memberEntity != null) {
            MemberDto memberDto = new MemberDto();
            memberDto.setId(memberEntity.getId());

            memberDto.setFamilyId(memberEntity.getFamilyId());
            memberDto.setUniqueHealthId(memberEntity.getUniqueHealthId());
            memberDto.setEmamtaHealthId(memberEntity.getEmamtaHealthId());
            memberDto.setMotherId(memberEntity.getMotherId());

            memberDto.setFirstName(memberEntity.getFirstName());
            memberDto.setMiddleName(memberEntity.getMiddleName());
            memberDto.setLastName(memberEntity.getLastName());
            memberDto.setHusbandName(memberEntity.getHusbandName());
            memberDto.setGrandfatherName(memberEntity.getGrandfatherName());
            memberDto.setNameAsPerAadhar(memberEntity.getNameAsPerAadhar());
            memberDto.setGender(memberEntity.getGender());
            memberDto.setDob(memberEntity.getDob());

            memberDto.setMaritalStatus(memberEntity.getMaritalStatus() != null ? memberEntity.getMaritalStatus() : null);
            memberDto.setYearOfWedding(String.valueOf(memberEntity.getYearOfWedding()));
            memberDto.setAadharNumber(memberEntity.getAadharNumber());
            memberDto.setMobileNumber(memberEntity.getMobileNumber());
            memberDto.setIfsc(memberEntity.getIfsc());
            memberDto.setAccountNumber(memberEntity.getAccountNumber());
            memberDto.setFamilyHeadFlag(memberEntity.getFamilyHeadFlag());

            memberDto.setIsPregnantFlag(memberEntity.getIsPregnantFlag());
            memberDto.setLmpDate(memberEntity.getLmpDate());
            memberDto.setEdd(memberEntity.getEdd());
            memberDto.setNormalCycleDays(memberEntity.getNormalCycleDays());
            memberDto.setFamilyPlanningMethod(memberEntity.getFamilyPlanningMethod() != null ? memberEntity.getFamilyPlanningMethod() : null);
            memberDto.setState(memberEntity.getState());
            memberDto.setIsAadharVerified(memberEntity.getIsAadharVerified());
            memberDto.setIsMobileNumberVerified(memberEntity.getIsMobileNumberVerified());
            memberDto.setIsNativeFlag(memberEntity.getIsNativeFlag());
            memberDto.setEducationStatus(memberEntity.getEducationStatus() != null ? memberEntity.getEducationStatus() : null);
            memberDto.setIsReport(memberEntity.getIsReport());
            memberDto.setIsEarlyRegistration(memberEntity.getIsEarlyRegistration());
            memberDto.setIsJsyBeneficiary(memberEntity.getJsyBeneficiary());
            memberDto.setIsJsyPaymentDone(memberEntity.getJsyPaymentGiven());
            memberDto.setIsChiranjeeviYojnaBeneficiary(memberEntity.getChiranjeeviYojnaBeneficiary());
            memberDto.setIsKpsyBeneficiary(memberEntity.getKpsyBeneficiary());
            memberDto.setIsIayBeneficiary(memberEntity.getIayBeneficiary());

            memberDto.setImmunisationGiven(memberEntity.getImmunisationGiven());
            memberDto.setBloodGroup(memberEntity.getBloodGroup());
            memberDto.setWeight(memberEntity.getWeight());
            memberDto.setHaemoglobin(memberEntity.getHaemoglobin());
            memberDto.setAncVisitDates(memberEntity.getAncVisitDates());
            memberDto.setCurPregRegDetId(memberEntity.getCurPregRegDetId());
            memberDto.setCurPregRegDate(memberDto.getCurPregRegDate());
            memberDto.setLastDeliveryDate(memberEntity.getLastDeliveryDate());
            memberDto.setLastDeliveryOutcome(memberEntity.getLastDeliveryOutcome());
            memberDto.setHealthInsurance(memberEntity.getHealthInsurance());
            memberDto.setSchemeDetail(memberEntity.getSchemeDetail());

            memberDto.setAdditionalInfo(memberEntity.getAdditionalInfo());
            memberDto.setEligibleCoupleDate(memberEntity.getEligibleCoupleDate());
            memberDto.setFamilyPlanningHealthInfrastructure(memberEntity.getFamilyPlanningHealthInfrastructure());
            memberDto.setBasicState(memberEntity.getBasicState());
            memberDto.setLastMethodOfContraception(memberEntity.getLastMethodOfContraception());
            memberDto.setRelationWithHof(memberEntity.getRelationWithHof());

            return memberDto;
        }
        return null;
    }

    /**
     * Convert member dto to entity.
     *
     * @param memberDto           Details of member.
     * @param memberEntityDefault Default entity of member.
     * @return Returns entity of member.
     */
    public static MemberEntity getMemberEntity(MemberDto memberDto, MemberEntity memberEntityDefault) {
        if (memberDto != null) {
            MemberEntity memberEntity = memberEntityDefault;
            if (memberEntityDefault == null) {
                memberEntity = new MemberEntity();
            }

            memberEntity.setId(memberDto.getId());
            memberEntity.setFamilyId(memberDto.getFamilyId());
            memberEntity.setFirstName(memberDto.getFirstName());
            memberEntity.setMiddleName(memberDto.getMiddleName());
            memberEntity.setLastName(memberDto.getLastName());
            memberEntity.setHusbandName(memberDto.getHusbandName());
            memberEntity.setGender(memberDto.getGender());
            memberEntity.setMaritalStatus(memberDto.getMaritalStatus());
            memberEntity.setAadharNumber(memberDto.getAadharNumber());
            memberEntity.setMobileNumber(memberDto.getMobileNumber());
            memberEntity.setFamilyHeadFlag(memberDto.getFamilyHeadFlag());
            memberEntity.setDob(memberDto.getDob());
            memberEntity.setUniqueHealthId(memberDto.getUniqueHealthId());
            memberEntity.setIfsc(memberDto.getIfsc());
            memberEntity.setAccountNumber(memberDto.getAccountNumber());
            memberEntity.setIsPregnantFlag(memberDto.getIsPregnantFlag());
            memberEntity.setLmpDate(memberDto.getLmpDate());
            memberEntity.setNormalCycleDays(memberDto.getNormalCycleDays());
            memberEntity.setFamilyPlanningMethod(memberDto.getFamilyPlanningMethod());
            memberEntity.setState(memberDto.getState());
            memberEntity.setGrandfatherName(memberDto.getGrandfatherName());
            memberEntity.setEmamtaHealthId(memberDto.getEmamtaHealthId());
            memberEntity.setIsAadharVerified(memberDto.getIsAadharVerified());
            memberEntity.setIsMobileNumberVerified(memberDto.getIsMobileNumberVerified());
            memberEntity.setIsNativeFlag(memberDto.getIsNativeFlag());
            memberEntity.setEducationStatus(memberDto.getEducationStatus());
            memberEntity.setIsReport(memberDto.getIsReport());
            memberEntity.setNameAsPerAadhar(memberDto.getNameAsPerAadhar());
            memberEntity.setChronicDiseaseDetails(memberDto.getChronicDiseaseDetails());
            memberEntity.setCongenitalAnomalyDetails(memberDto.getCongenitalAnomalyDetails());
            memberEntity.setCurrentDiseaseDetails(memberDto.getCurrentDiseaseDetails());
            memberEntity.setEyeIssueDetails(memberDto.getEyeIssueDetails());
            memberEntity.setEligibleCoupleDate(memberDto.getEligibleCoupleDate());
            memberEntity.setFamilyPlanningHealthInfrastructure(memberDto.getFamilyPlanningHealthInfrastructure());

            return memberEntity;
        }
        return null;
    }
}
