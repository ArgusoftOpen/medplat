/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.mapper;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.web.users.model.RoleMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * An util class for role to convert modal to dto  or dto to modal
 * </p>
 *
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class RoleMapper {

    private RoleMapper() {

    }

    /**
     * Converts role modal to role dto
     *
     * @param role An instance of RoleMaster
     * @return An instance of RoleMasterDto
     */
    public static RoleMasterDto convertRoleMasterToDto(RoleMaster role) {
        RoleMasterDto roleDto = new RoleMasterDto();
        roleDto.setCode(role.getCode());
        roleDto.setName(role.getName());
        roleDto.setDescription(role.getDescription());
        roleDto.setId(role.getId());
        roleDto.setState(role.getState());
        roleDto.setCreatedBy(role.getCreatedBy());
        roleDto.setCreatedOn(role.getCreatedOn());
        roleDto.setMaxPosition(role.getMaxPosition());
        roleDto.setMaxHealthInfra(role.getMaxHealthInfra());
        roleDto.setIsLastNameMandatory(role.getIsLastNameMandatory());
        roleDto.setIsAadharNumMandatory(role.getIsAadharNumMandatory());
        roleDto.setIsContactNumMandatory(role.getIsContactNumMandatory());
        roleDto.setIsEmailMandatory(role.getIsEmailMandatory());
        roleDto.setIsConvoxIdMandatory(role.getIsConvoxIdMandatory());
        roleDto.setRoleType(role.getRoleType());
        roleDto.setIsHealthInfraMandatory(role.getIsHealthInfraMandatory());
        roleDto.setIsGeolocationMandatory(role.getIsGeolocationMandatory());

        return roleDto;
    }

    /**
     * Converts a list of role modal to role dto
     *
     * @param roles A list of RoleMaster
     * @return A list of RoleMasterDto
     */
    public static List<RoleMasterDto> convertMasterListToDto(List<RoleMaster> roles) {
        List<RoleMasterDto> roleMasterDtos = new LinkedList<>();
        for (RoleMaster role : roles) {
            roleMasterDtos.add(convertRoleMasterToDto(role));
        }
        return roleMasterDtos;
    }

    /**
     * Converts role dto to role modal
     *
     * @param roleDto An instance of RoleMasterDto
     * @return An instance of RoleMaster
     */
    public static RoleMaster convertRoleDtoToMaster(RoleMasterDto roleDto) {

        RoleMaster role = new RoleMaster();
        role.setCode(roleDto.getCode());
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        role.setId(roleDto.getId());
        role.setState(roleDto.getState());
        role.setMaxPosition(roleDto.getMaxPosition());
        role.setMaxHealthInfra(roleDto.getMaxHealthInfra());
        role.setIsLastNameMandatory(roleDto.getIsLastNameMandatory());
        role.setIsAadharNumMandatory(roleDto.getIsAadharNumMandatory() == null ? Boolean.FALSE : roleDto.getIsAadharNumMandatory());
        role.setIsContactNumMandatory(roleDto.getIsContactNumMandatory() == null ? Boolean.FALSE : roleDto.getIsContactNumMandatory());
        role.setIsEmailMandatory(roleDto.getIsEmailMandatory() == null ? Boolean.FALSE : roleDto.getIsEmailMandatory());
        role.setIsConvoxIdMandatory(roleDto.getIsConvoxIdMandatory() == null ? Boolean.FALSE : roleDto.getIsConvoxIdMandatory());
        role.setRoleType(roleDto.getRoleType());
        role.setIsHealthInfraMandatory(roleDto.getIsHealthInfraMandatory() == null ? Boolean.FALSE : roleDto.getIsHealthInfraMandatory());
        role.setIsGeolocationMandatory(roleDto.getIsGeolocationMandatory() == null ? Boolean.FALSE : roleDto.getIsGeolocationMandatory());

        return role;
    }
}
