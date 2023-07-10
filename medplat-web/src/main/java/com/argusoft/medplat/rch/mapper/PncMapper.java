/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.PncChildDto;
import com.argusoft.medplat.rch.dto.PncMasterDto;
import com.argusoft.medplat.rch.dto.PncMotherDto;
import com.argusoft.medplat.rch.model.PncChildMaster;
import com.argusoft.medplat.rch.model.PncMaster;
import com.argusoft.medplat.rch.model.PncMotherMaster;

/**
 * <p>
 * Mapper for pnc in order to convert dto to model or model to dto.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class PncMapper {
    private PncMapper() {
    }

    /**
     * Convert pnc master details into entity.
     *
     * @param pncMasterDto Pnc master details.
     * @return Returns entity of pnc master.
     */
    public static PncMaster convertPncMasterDtoToPncMaster(PncMasterDto pncMasterDto) {
        PncMaster pncMaster = new PncMaster();
        pncMaster.setMemberId(pncMasterDto.getMemberId());
        pncMaster.setFamilyId(pncMasterDto.getFamilyId());
        pncMaster.setLatitude(pncMasterDto.getLatitude());
        pncMaster.setLongitude(pncMasterDto.getLongitude());
        pncMaster.setMobileStartDate(pncMasterDto.getMobileStartDate());
        pncMaster.setMobileEndDate(pncMasterDto.getMobileEndDate());
        pncMaster.setLocationId(pncMasterDto.getLocationId());
        pncMaster.setMemberStatus(pncMasterDto.getMemberStatus());
        pncMaster.setPncNo(pncMasterDto.getPncNo());
        pncMaster.setIsFromWeb(pncMasterDto.getIsFromWeb());
        pncMaster.setServiceDate(pncMasterDto.getServiceDate());
        pncMaster.setDeliveryPlace(pncMasterDto.getDeliveryPlace());
        pncMaster.setTypeOfHospital(pncMasterDto.getTypeOfHospital());
        pncMaster.setHealthInfrastructureId(pncMasterDto.getHealthInfrastructureId());
        pncMaster.setDeliveryDoneBy(pncMasterDto.getDeliveryDoneBy());
        pncMaster.setDeliveryPerson(pncMasterDto.getDeliveryPerson());
        pncMaster.setDeliveryPersonName(pncMasterDto.getDeliveryPersonName());

        return pncMaster;
    }

    /**
     * Convert pnc master entity into details.
     *
     * @param pncMotherDto Entity of pnc master.
     * @return Returns pnc master details.
     */
    public static PncMotherMaster convertPncMotherDtoToPncMotherMaster(PncMotherDto pncMotherDto) {
        PncMotherMaster pncMotherMaster = new PncMotherMaster();
        pncMotherMaster.setMotherId(pncMotherDto.getMotherId());
        pncMotherMaster.setDateOfDelivery(pncMotherDto.getDateOfDelivery());
        pncMotherMaster.setServiceDate(pncMotherDto.getServiceDate());
        pncMotherMaster.setIsAlive(pncMotherDto.getIsAlive());
        pncMotherMaster.setIfaTabletsGiven(pncMotherDto.getIfaTabletsGiven());
        pncMotherMaster.setCalciumTabletsGiven(pncMotherDto.getCalciumGiven());
        pncMotherMaster.setOtherDangerSign(pncMotherDto.getOtherDangerSign());
        pncMotherMaster.setReferralPlace(pncMotherDto.getReferralPlace());
        pncMotherMaster.setMemberStatus(pncMotherDto.getMemberStatus());
        pncMotherMaster.setDeathDate(pncMotherDto.getDeathDate());
        pncMotherMaster.setDeathReason(pncMotherDto.getDeathReason());
        pncMotherMaster.setPlaceOfDeath(pncMotherDto.getPlaceOfDeath());
        pncMotherMaster.setFpInsertOperateDate(pncMotherDto.getFpInsertOperateDate());
        pncMotherMaster.setFamilyPlanningMethod(pncMotherDto.getFamilyPlanningMethod());
        pncMotherMaster.setOtherDeathReason(pncMotherDto.getOtherDeathReason());
        pncMotherMaster.setIsHighRiskCase(pncMotherDto.getIsHighRiskCase());
        pncMotherMaster.setMotherReferralDone(pncMotherDto.getMotherReferralDone());
        pncMotherMaster.setMotherDangerSigns(pncMotherDto.getMotherDangerSigns());
        pncMotherMaster.setDeathInfrastructureId(pncMotherDto.getDeathInfrastructureId());
        pncMotherMaster.setBloodTransfusion(pncMotherDto.getBloodTransfusion());
        pncMotherMaster.setIronDefAnemiaInj(pncMotherDto.getIronDefAnemiaInj());
        pncMotherMaster.setIronDefAnemiaInjDueDate(pncMotherDto.getIronDefAnemiaInjDueDate());
        pncMotherMaster.setReferralInfraId(pncMotherDto.getReferralInfraId());

        return pncMotherMaster;
    }

    /**
     * Convert pnc child details into entity.
     *
     * @param pncChildDto Pnc child details.
     * @return Returns entity of pnc child.
     */
    public static PncChildMaster convertPncChildDtoToPncChildMaster(PncChildDto pncChildDto) {
        PncChildMaster pncChildMaster = new PncChildMaster();
        pncChildMaster.setChildId(pncChildDto.getChildId());
        pncChildMaster.setIsAlive(pncChildDto.getIsAlive());
        pncChildMaster.setOtherDangerSign(pncChildDto.getOtherDangerSign());
        pncChildMaster.setChildWeight(pncChildDto.getChildWeight());
        pncChildMaster.setMemberStatus(pncChildDto.getMemberStatus());
        pncChildMaster.setDeathDate(pncChildDto.getDeathDate());
        pncChildMaster.setDeathReason(pncChildDto.getDeathReason());
        pncChildMaster.setPlaceOfDeath(pncChildDto.getPlaceOfDeath());
        pncChildMaster.setReferralPlace(pncChildDto.getReferralPlace());
        pncChildMaster.setOtherDeathReason(pncChildDto.getOtherDeathReason());
        pncChildMaster.setIsHighRiskCase(pncChildDto.getIsHighRiskCase());
        pncChildMaster.setChildReferralDone(pncChildDto.getChildReferralDone());
        pncChildMaster.setChildDangerSigns(pncChildDto.getChildDangerSigns());
        pncChildMaster.setReferralInfraId(pncChildDto.getReferralInfraId());

        return pncChildMaster;
    }

    /**
     * Convert pnc mother master entity into details.
     *
     * @param pncMotherMaster Entity of pnc mother master.
     * @return Returns pnc mother master details.
     */
    public static PncMotherDto convertPncMotherMasterToPncMotherDto(PncMotherMaster pncMotherMaster) {
        PncMotherDto pncMotherDto = new PncMotherDto();
        pncMotherDto.setMotherId(pncMotherMaster.getMotherId());
        pncMotherDto.setDateOfDelivery(pncMotherMaster.getDateOfDelivery());
        pncMotherDto.setServiceDate(pncMotherMaster.getServiceDate());
        pncMotherDto.setIsAlive(pncMotherMaster.getIsAlive());
        pncMotherDto.setIfaTabletsGiven(pncMotherMaster.getIfaTabletsGiven());
        pncMotherDto.setCalciumGiven(pncMotherMaster.getCalciumTabletsGiven());
        pncMotherDto.setOtherDangerSign(pncMotherMaster.getOtherDangerSign());
        pncMotherDto.setReferralPlace(pncMotherMaster.getReferralPlace());
        pncMotherDto.setMemberStatus(pncMotherMaster.getMemberStatus());
        pncMotherDto.setDeathDate(pncMotherMaster.getDeathDate());
        pncMotherDto.setDeathReason(pncMotherMaster.getDeathReason());
        pncMotherDto.setPlaceOfDeath(pncMotherMaster.getPlaceOfDeath());
        pncMotherDto.setFpInsertOperateDate(pncMotherMaster.getFpInsertOperateDate());
        pncMotherDto.setFamilyPlanningMethod(pncMotherMaster.getFamilyPlanningMethod());
        pncMotherDto.setOtherDeathReason(pncMotherMaster.getOtherDeathReason());
        pncMotherDto.setIsHighRiskCase(pncMotherMaster.getIsHighRiskCase());
        pncMotherDto.setMotherReferralDone(pncMotherMaster.getMotherReferralDone());
        pncMotherDto.setMotherDangerSigns(pncMotherMaster.getMotherDangerSigns());
        pncMotherDto.setDeathInfrastructureId(pncMotherMaster.getDeathInfrastructureId());
        pncMotherDto.setReferralInfraId(pncMotherMaster.getReferralInfraId());

        return pncMotherDto;
    }

    /**
     * Convert pnc child master entity into dto.
     *
     * @param pncChildMaster Entity of pnc child master.
     * @return Returns pnc child master entity.
     */
    public static PncChildDto convertPncChildMasterToPncChildDto(PncChildMaster pncChildMaster) {
        PncChildDto pncChildDto = new PncChildDto();
        pncChildDto.setChildId(pncChildMaster.getChildId());
        pncChildDto.setIsAlive(pncChildMaster.getIsAlive());
        pncChildDto.setOtherDangerSign(pncChildMaster.getOtherDangerSign());
        pncChildDto.setChildWeight(pncChildMaster.getChildWeight());
        pncChildDto.setMemberStatus(pncChildMaster.getMemberStatus());
        pncChildDto.setDeathDate(pncChildMaster.getDeathDate());
        pncChildDto.setDeathReason(pncChildMaster.getDeathReason());
        pncChildDto.setPlaceOfDeath(pncChildMaster.getPlaceOfDeath());
        pncChildDto.setReferralPlace(pncChildMaster.getReferralPlace());
        pncChildDto.setOtherDeathReason(pncChildMaster.getOtherDeathReason());
        pncChildDto.setIsHighRiskCase(pncChildMaster.getIsHighRiskCase());
        pncChildDto.setChildReferralDone(pncChildMaster.getChildReferralDone());
        pncChildDto.setChildDangerSigns(pncChildMaster.getChildDangerSigns());
        pncChildDto.setReferralInfraId(pncChildMaster.getReferralInfraId());

        return pncChildDto;
    }
}
