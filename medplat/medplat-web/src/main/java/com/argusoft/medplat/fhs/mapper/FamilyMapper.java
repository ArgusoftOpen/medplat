/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.mapper;

import com.argusoft.medplat.fhs.dto.FamilyDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;

/**
 * <p>
 * Mapper for family in order to convert dto to model or model to dto.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 11:00 AM
 */
public class FamilyMapper {

    private FamilyMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert family entity to dto.
     *
     * @param familyEntity Entity of family.
     * @return Returns details of family.
     */
    public static FamilyDto getFamilyDto(FamilyEntity familyEntity) {
        if (familyEntity != null) {
            FamilyDto familyDto = new FamilyDto();
            familyDto.setId(familyEntity.getId());
            familyDto.setFamilyId(familyEntity.getFamilyId());
            familyDto.setHouseNumber(familyEntity.getHouseNumber());
            familyDto.setLocationId(familyEntity.getLocationId());
            familyDto.setReligion(familyEntity.getReligion());
            familyDto.setCaste(familyEntity.getCaste());
            familyDto.setBplFlag(familyEntity.getBplFlag());
            familyDto.setAnganwadiId(familyEntity.getAnganwadiId());
            familyDto.setToiletAvailableFlag(familyEntity.getToiletAvailableFlag());
            familyDto.setIsVerifiedFlag(familyEntity.getIsVerifiedFlag());
            familyDto.setState(familyEntity.getState());
            familyDto.setAssignedTo(familyEntity.getAssignedTo());
            familyDto.setAddress1(familyEntity.getAddress1());
            familyDto.setAddress2(familyEntity.getAddress2());
            familyDto.setMaaVatsalyaNumber(familyEntity.getMaaVatsalyaNumber());
            familyDto.setAreaId(familyEntity.getAreaId());
            familyDto.setVulnerableFlag(familyEntity.getVulnerableFlag());
            familyDto.setMigratoryFlag(familyEntity.getMigratoryFlag());
            familyDto.setLatitude(familyEntity.getLatitude());
            familyDto.setLongitude(familyEntity.getLongitude());
            familyDto.setRsbyCardNumber(familyEntity.getRsbyCardNumber());
            familyDto.setComment(familyEntity.getComment());
            familyDto.setCurrentState(familyEntity.getCurrentState());
            familyDto.setIsReport(familyEntity.getIsReport());
            familyDto.setContactPersonId(familyEntity.getContactPersonId());
            return familyDto;
        }
        return null;
    }

    /**
     * Convert family dto to entity.
     *
     * @param familyDto           Family details.
     * @param familyEntityDefault Default family entity.
     * @return Returns family entity.
     */
    public static FamilyEntity getFamilyEntity(FamilyDto familyDto, FamilyEntity familyEntityDefault) {
        if (familyDto != null) {
            FamilyEntity familyEntity = familyEntityDefault;

            if (familyEntity == null) {
                familyEntity = new FamilyEntity();
            }

            familyEntity.setId(familyDto.getId());
            familyEntity.setFamilyId(familyDto.getFamilyId());
            familyEntity.setHouseNumber(familyDto.getHouseNumber());
            familyEntity.setLocationId(familyDto.getLocationId());
            familyEntity.setReligion(familyDto.getReligion());
            familyEntity.setCaste(familyDto.getCaste());
            familyEntity.setBplFlag(familyDto.getBplFlag());
            familyEntity.setAnganwadiId(familyDto.getAnganwadiId());
            familyEntity.setToiletAvailableFlag(familyDto.getToiletAvailableFlag());
            familyEntity.setIsVerifiedFlag(familyDto.getIsVerifiedFlag());
            familyEntity.setState(familyDto.getState());
            familyEntity.setAssignedTo(familyDto.getAssignedTo());
            familyEntity.setAddress1(familyDto.getAddress1());
            familyEntity.setAddress2(familyDto.getAddress2());
            familyEntity.setMaaVatsalyaNumber(familyDto.getMaaVatsalyaNumber());
            familyEntity.setAreaId(familyDto.getAreaId());
            familyEntity.setVulnerableFlag(familyDto.getVulnerableFlag());
            familyEntity.setMigratoryFlag(familyDto.getMigratoryFlag());
            familyEntity.setLatitude(familyDto.getLatitude());
            familyEntity.setLongitude(familyDto.getLongitude());
            familyEntity.setRsbyCardNumber(familyDto.getRsbyCardNumber());
            familyEntity.setComment(familyDto.getComment());
            familyEntity.setIsReport(familyDto.getIsReport());
            familyEntity.setContactPersonId(familyDto.getContactPersonId());
            return familyEntity;
        }
        return null;
    }
}
