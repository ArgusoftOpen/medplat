package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.AncMasterDto;
import com.argusoft.medplat.rch.model.AncVisit;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;

import java.util.Objects;

/**
 * <p>
 * Mapper for anc details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class AncMapper {
    private AncMapper() {
    }

    /**
     * Convert anc master details into entity.
     *
     * @param ancMasterDto Anc master details.
     * @return Returns anc master entity.
     */
    public static AncVisit convertAncMasterDtoToAncMaster(AncMasterDto ancMasterDto) {

        AncVisit ancVisit = new AncVisit();

        ancVisit.setId(ancMasterDto.getId());
        ancVisit.setMemberId(ancMasterDto.getMemberId());
        ancVisit.setFamilyId(ancMasterDto.getFamilyId());
        ancVisit.setLatitude(ancMasterDto.getLatitude());
        ancVisit.setLongitude(ancMasterDto.getLongitude());
        ancVisit.setLocationId(ancMasterDto.getLocationId());
        ancVisit.setMobileStartDate(ancMasterDto.getMobileStartDate());
        ancVisit.setMobileEndDate(ancMasterDto.getMobileEndDate());
        ancVisit.setCreatedBy(ancMasterDto.getCreatedBy());
        ancVisit.setCreatedOn(ancMasterDto.getCreatedOn());
        ancVisit.setModifiedBy(ancMasterDto.getModifiedBy());
        ancVisit.setModifiedOn(ancMasterDto.getModifiedOn());
        ancVisit.setMemberStatus(ancMasterDto.getMemberStatus());
        ancVisit.setLmp(ancMasterDto.getLmp());
        ancVisit.setEdd(ancMasterDto.getEdd());
        ancVisit.setMemberHeight(ancMasterDto.getMemberHeight());
        ancVisit.setWeight(ancMasterDto.getWeight());
        ancVisit.setNotificationId(ancMasterDto.getNotificationId());
        ancVisit.setAlbendazoleGiven(ancMasterDto.getAlbendazoleGiven());
        ancVisit.setAncPlace(ancMasterDto.getAncPlace());
        ancVisit.setBloodGroup(ancMasterDto.getBloodGroup());
        ancVisit.setBloodSugarTest(ancMasterDto.getBloodSugarTest());
        ancVisit.setCalciumTabletsGiven(ancMasterDto.getCalciumTabletsGiven());
        ancVisit.setDangerousSignIds(ancMasterDto.getDangerousSignIds());
        ancVisit.setOtherDangerousSign(ancMasterDto.getOtherDangerousSign());
        ancVisit.setExpectedDeliveryPlace(ancMasterDto.getExpectedDeliveryPlace());
        ancVisit.setFaTabletsGiven(ancMasterDto.getFaTabletsGiven());
        ancVisit.setFamilyPlanningMethod(ancMasterDto.getFamilyPlanningMethod());
        ancVisit.setFoetalHeartSound(ancMasterDto.getFoetalHeartSound());
        ancVisit.setFoetalHeight(ancMasterDto.getFoetalHeight());
        ancVisit.setFoetalMovement(ancMasterDto.getFoetalMovement());
        ancVisit.setFoetalPosition(ancMasterDto.getFoetalPosition());
        ancVisit.setHaemoglobinCount(ancMasterDto.getHaemoglobinCount());
        ancVisit.setHbsagTest(ancMasterDto.getHbsagTest());
        ancVisit.setHivTest(ancMasterDto.getHivTest());
        ancVisit.setIsHighRiskCase(ancMasterDto.getIsHighRiskCase());
        ancVisit.setIfaTabletsGiven(ancMasterDto.getIfaTabletsGiven());
        ancVisit.setJsyBeneficiary(ancMasterDto.getJsyBeneficiary());
        ancVisit.setJsyPaymentDone(ancMasterDto.getJsyPaymentDone());
        ancVisit.setIayBeneficiary(ancMasterDto.getIayBeneficiary());
        ancVisit.setKpsyBeneficiary(ancMasterDto.getKpsyBeneficiary());
        ancVisit.setChiranjeeviYojnaBeneficiary(ancMasterDto.getChiranjeeviYojnaBeneficiary());
        ancVisit.setLastDeliveryOutcome(ancMasterDto.getLastDeliveryOutcome());
        ancVisit.setPregnancyRegDetId(ancMasterDto.getPregnancyRegDetId());
        ancVisit.setPreviousPregnancyComplication(ancMasterDto.getPreviousPregnancyComplication());
        ancVisit.setOtherPreviousPregnancyComplication(ancMasterDto.getOtherPreviousPregnancyComplication());
        ancVisit.setReferralDone(ancMasterDto.getReferralDone());
        ancVisit.setReferralPlace(ancMasterDto.getReferralPlace());
        ancVisit.setServiceDate(ancMasterDto.getServiceDate());
        ancVisit.setSickleCellTest(ancMasterDto.getSickleCellTest());
        ancVisit.setSugarTestAfterFoodValue(ancMasterDto.getSugarTestAfterFoodValue());
        ancVisit.setSugarTestBeforeFoodValue(ancMasterDto.getSugarTestBeforeFoodValue());
        ancVisit.setSystolicBp(ancMasterDto.getSystolicBp());
        ancVisit.setDiastolicBp(ancMasterDto.getDiastolicBp());
        ancVisit.setUrineTestDone(ancMasterDto.getUrineTestDone());
        ancVisit.setUrineAlbumin(ancMasterDto.getUrineAlbumin());
        ancVisit.setUrineSugar(ancMasterDto.getUrineSugar());
        ancVisit.setVdrlTest(ancMasterDto.getVdrlTest());
        ancVisit.setDeadFlag(ancMasterDto.getDeadFlag());
        ancVisit.setDeathDate(ancMasterDto.getDeathDate());
        ancVisit.setDeathReason(ancMasterDto.getDeathReason());
        ancVisit.setPlaceOfDeath(ancMasterDto.getPlaceOfDeath());
        ancVisit.setIsFromWeb(ancMasterDto.getIsFromWeb());
        ancVisit.setDeliveryPlace(ancMasterDto.getDeliveryPlace());
        ancVisit.setTypeOfHospital(ancMasterDto.getTypeOfHospital());
        ancVisit.setHealthInfrastructureId(ancMasterDto.getHealthInfrastructureId());
        ancVisit.setDeliveryDoneBy(ancMasterDto.getDeliveryDoneBy());
        ancVisit.setDeliveryPerson(ancMasterDto.getDeliveryPerson());
        ancVisit.setDeliveryPersonName(ancMasterDto.getDeliveryPersonName());
        ancVisit.setBloodTransfusion(ancMasterDto.getBloodTransfusion());
        ancVisit.setIronDefAnemiaInj(ancMasterDto.getIronDefAnemiaInj());
        ancVisit.setIronDefAnemiaInjDueDate(ancMasterDto.getIronDefAnemiaInjDueDate());
        ancVisit.setExaminedByGynecologist(ancMasterDto.getExaminedByGynecologist());
        ancVisit.setIsInjCorticosteroidGiven(ancMasterDto.isInjCorticosteroidGiven());
        ancVisit.setReferralInfraId(ancMasterDto.getReferralInfraId());

        return ancVisit;
    }

    public static AncMasterDto convertAncMasterToAncMasterDto(AncVisit ancVisit, HealthInfrastructureDetails healthInfrastructureDetails) {
        AncMasterDto ancMasterDto = new AncMasterDto();
        ancMasterDto.setId(ancVisit.getId());
        ancMasterDto.setServiceDate(ancVisit.getServiceDate());
        ancMasterDto.setHealthInfrastructureId(ancVisit.getHealthInfrastructureId());
        if (Objects.nonNull(healthInfrastructureDetails)) {
            ancMasterDto.setHealthInfraName(healthInfrastructureDetails.getName());
        }
        ancMasterDto.setIsLinkedToAbha(ancVisit.getIsLinkedToAbha());
        return ancMasterDto;
    }
}
