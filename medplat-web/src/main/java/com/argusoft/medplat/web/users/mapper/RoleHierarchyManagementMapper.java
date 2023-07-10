/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.mapper;

import com.argusoft.medplat.web.users.dto.RoleHierarchyDto;
import com.argusoft.medplat.web.users.model.RoleHierarchyManagement;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 *    An util class for role hierarchy to convert modal to dto  or dto to modal
 *</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class RoleHierarchyManagementMapper {

    private RoleHierarchyManagementMapper() {
            
    }

    /**
     * Converts role hierarchy modal to role hierarchy dto
     * @param roleHierarchy An instance of RoleHierarchyManagement
     * @return An instance of RoleHierarchyDto
     */
    public static RoleHierarchyDto convertMasterToDto(RoleHierarchyManagement roleHierarchy) {

        RoleHierarchyDto roleHierarchyDto = new RoleHierarchyDto();
        roleHierarchyDto.setId(roleHierarchy.getId());
        roleHierarchyDto.setUserId(roleHierarchy.getUserId());
        roleHierarchyDto.setRoleId(roleHierarchy.getRoleId());
        roleHierarchyDto.setLocationType(roleHierarchy.getLocationType());
        roleHierarchyDto.setLevel(roleHierarchy.getLevel());
        return roleHierarchyDto;
    }

    /**
     * Converts a list of role hierarchy modal to role hierarchy dto
     * @param roleHierarchylist A list of RoleHierarchyManagement
     * @return A list of RoleHierarchyDto
     */
    public static List<RoleHierarchyDto> convertMasterListToDtoList(List<RoleHierarchyManagement> roleHierarchylist) {
        LinkedList<RoleHierarchyDto> roleHierarchyDtoList = new LinkedList<>();
        for (RoleHierarchyManagement roleHierarchy : roleHierarchylist) {
            roleHierarchyDtoList.push(convertMasterToDto(roleHierarchy));
        }
        return roleHierarchyDtoList;
    }
}
