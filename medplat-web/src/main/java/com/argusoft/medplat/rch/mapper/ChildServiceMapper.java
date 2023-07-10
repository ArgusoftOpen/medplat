/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.ChildServiceMasterDto;
import com.argusoft.medplat.rch.model.ChildServiceMaster;

/**
 * <p>
 * Mapper for child service in order to convert dto to model or model to dto.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class ChildServiceMapper {
    private ChildServiceMapper() {
    }

    /**
     * Convert child service entity into details.
     *
     * @param childServiceMaster Entity child service.
     * @return Returns child service details.
     */
    public static ChildServiceMasterDto convertEntityToDto(ChildServiceMaster childServiceMaster) {
        ChildServiceMasterDto childServiceMasterDto = new ChildServiceMasterDto();
        childServiceMasterDto.setId(childServiceMaster.getId());
        childServiceMasterDto.setMemberId(childServiceMaster.getMemberId());
        childServiceMasterDto.setMemberStatus(childServiceMaster.getMemberStatus());
        childServiceMasterDto.setIsAlive(childServiceMaster.getIsAlive());
        childServiceMasterDto.setDeathDate(childServiceMaster.getDeathDate());
        childServiceMasterDto.setPlaceOfDeath(childServiceMaster.getPlaceOfDeath());
        childServiceMasterDto.setDeathReason(childServiceMaster.getDeathReason());
        childServiceMasterDto.setOtherDeathReason(childServiceMaster.getOtherDeathReason());
        childServiceMasterDto.setWeight(childServiceMaster.getWeight());
        childServiceMasterDto.setIfaSyrupGiven(childServiceMaster.getIfaSyrupGiven());
        childServiceMasterDto.setComplementaryFeedingStarted(childServiceMaster.getComplementaryFeedingStarted());
        childServiceMasterDto.setComplementaryFeedingStartPeriod(childServiceMaster.getComplementaryFeedingStartPeriod());
        childServiceMasterDto.setDieseases(childServiceMaster.getDieseases());
        childServiceMasterDto.setOtherDiseases(childServiceMaster.getOtherDiseases());
        childServiceMasterDto.setIsTreatementDone(childServiceMaster.getIsTreatementDone());
        childServiceMasterDto.setMidArmCircumference(childServiceMaster.getMidArmCircumference());
        childServiceMasterDto.setHeight(childServiceMaster.getHeight());
        childServiceMasterDto.setHavePedalEdema(childServiceMaster.getHavePedalEdema());
        childServiceMasterDto.setExclusivelyBreastfeded(childServiceMaster.getExclusivelyBreastfeded());
        childServiceMasterDto.setAnyVaccinationPending(childServiceMaster.getAnyVaccinationPending());
        childServiceMasterDto.setServiceDate(childServiceMaster.getServiceDate());
        childServiceMasterDto.setSdScore(childServiceMaster.getSdScore());
        childServiceMasterDto.setIsFromWeb(childServiceMaster.getIsFromWeb());
        childServiceMasterDto.setDeliveryPlace(childServiceMaster.getDeliveryPlace());
        childServiceMasterDto.setTypeOfHospital(childServiceMaster.getTypeOfHospital());
        childServiceMasterDto.setHealthInfrastructureId(childServiceMaster.getHealthInfrastructureId());
        childServiceMasterDto.setDeliveryDoneBy(childServiceMaster.getDeliveryDoneBy());
        childServiceMasterDto.setDeliveryPerson(childServiceMaster.getDeliveryPerson());
        childServiceMasterDto.setDeliveryPersonName(childServiceMaster.getDeliveryPersonName());
        childServiceMasterDto.setDeathInfrastructureId(childServiceMaster.getDeathInfrastructureId());
        return childServiceMasterDto;
    }

    /**
     * Convert child service details into entity.
     *
     * @param childServiceMasterDto Child service details.
     * @return Returns entity of child service.
     */
    public static ChildServiceMaster convertDtoToEntity(ChildServiceMasterDto childServiceMasterDto) {
        ChildServiceMaster childServiceMaster = new ChildServiceMaster();
        childServiceMaster.setMemberId(childServiceMasterDto.getMemberId());
        childServiceMaster.setMemberStatus(childServiceMasterDto.getMemberStatus());
        childServiceMaster.setIsAlive(childServiceMasterDto.getIsAlive());
        childServiceMaster.setDeathDate(childServiceMasterDto.getDeathDate());
        childServiceMaster.setPlaceOfDeath(childServiceMasterDto.getPlaceOfDeath());
        childServiceMaster.setDeathReason(childServiceMasterDto.getDeathReason());
        childServiceMaster.setOtherDeathReason(childServiceMasterDto.getOtherDeathReason());
        childServiceMaster.setWeight(childServiceMasterDto.getWeight());
        childServiceMaster.setIfaSyrupGiven(childServiceMasterDto.getIfaSyrupGiven());
        childServiceMaster.setComplementaryFeedingStarted(childServiceMasterDto.getComplementaryFeedingStarted());
        childServiceMaster.setComplementaryFeedingStartPeriod(childServiceMasterDto.getComplementaryFeedingStartPeriod());
        childServiceMaster.setDieseases(childServiceMasterDto.getDieseases());
        childServiceMaster.setOtherDiseases(childServiceMasterDto.getOtherDiseases());
        childServiceMaster.setIsTreatementDone(childServiceMasterDto.getIsTreatementDone());
        childServiceMaster.setMidArmCircumference(childServiceMasterDto.getMidArmCircumference());
        childServiceMaster.setHeight(childServiceMasterDto.getHeight());
        childServiceMaster.setHavePedalEdema(childServiceMasterDto.getHavePedalEdema());
        childServiceMaster.setExclusivelyBreastfeded(childServiceMasterDto.getExclusivelyBreastfeded());
        childServiceMaster.setAnyVaccinationPending(childServiceMasterDto.getAnyVaccinationPending());
        childServiceMaster.setServiceDate(childServiceMasterDto.getServiceDate());
        childServiceMaster.setSdScore(childServiceMasterDto.getSdScore());
        childServiceMaster.setFamilyId(childServiceMasterDto.getFamilyId());
        childServiceMaster.setMobileStartDate(childServiceMasterDto.getMobileStartDate());
        childServiceMaster.setMobileEndDate(childServiceMasterDto.getMobileEndDate());
        childServiceMaster.setLocationId(childServiceMasterDto.getLocationId());
        childServiceMaster.setIsFromWeb(childServiceMasterDto.getIsFromWeb());
        childServiceMaster.setDeliveryPlace(childServiceMasterDto.getDeliveryPlace());
        childServiceMaster.setTypeOfHospital(childServiceMasterDto.getTypeOfHospital());
        childServiceMaster.setHealthInfrastructureId(childServiceMasterDto.getHealthInfrastructureId());
        childServiceMaster.setDeliveryDoneBy(childServiceMasterDto.getDeliveryDoneBy());
        childServiceMaster.setDeliveryPerson(childServiceMasterDto.getDeliveryPerson());
        childServiceMaster.setDeliveryPersonName(childServiceMasterDto.getDeliveryPersonName());
        childServiceMaster.setDeathInfrastructureId(childServiceMasterDto.getDeathInfrastructureId());
        return childServiceMaster;
    }

}
