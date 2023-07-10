/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.mapper;

import com.argusoft.medplat.notification.dto.EscalationLevelMasterDto;
import com.argusoft.medplat.notification.model.EscalationLevelMaster;

import java.util.*;

/**
 *
 * <p>
 *     Mapper for escalation level in order to convert dto to model or model to dto.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public class EscalationLevelMasterMapper {
    private EscalationLevelMasterMapper(){
    }

    /**
     * Convert escalation level details into entity.
     * @param escalationLevelMasterDto Escalation level details.
     * @param escalationLevelMaster Escalation level entity.
     * @return Returns entity of escalation level.
     */
    public static EscalationLevelMaster convertEscalationLevelMasterDtoToEscalationLevelMaster(EscalationLevelMasterDto escalationLevelMasterDto, EscalationLevelMaster escalationLevelMaster) {
        if (escalationLevelMaster == null) {
            escalationLevelMaster = new EscalationLevelMaster();
            escalationLevelMaster.setId(escalationLevelMasterDto.getId());

        }
        escalationLevelMaster.setName(escalationLevelMasterDto.getName());
        escalationLevelMaster.setNotificationTypeId(escalationLevelMasterDto.getNotificationTypeId());
        escalationLevelMaster.setUuid(escalationLevelMasterDto.getUuid());

        Map<Integer, Boolean> rolesWithPerformAction = new HashMap<>();

        if (escalationLevelMasterDto.getRoles() != null) {
            for (Integer roleId : escalationLevelMasterDto.getRoles()) {
                if (escalationLevelMasterDto.getPerformAction() == null || (escalationLevelMasterDto.getPerformAction() != null && !escalationLevelMasterDto.getPerformAction().containsKey(roleId))) {
                    rolesWithPerformAction.put(roleId, Boolean.FALSE);
                } else {
                    rolesWithPerformAction.put(roleId, escalationLevelMasterDto.getPerformAction().get(roleId));
                }
            }
        }

        escalationLevelMaster.setRolesWithPerformAction(rolesWithPerformAction);
        return escalationLevelMaster;
    }

    /**
     * Convert list of escalation level entities into dto list.
     * @param escalationLevelDtoList List of escalation level entities.
     * @return Returns list of escalation level details.
     */
    public static List<EscalationLevelMasterDto> convertEscalationLevelMasterListToEscalationLevelMasterDtoList(List<EscalationLevelMaster> escalationLevelDtoList) {
        List<EscalationLevelMasterDto> escalationLevelMasterDtos = new ArrayList<>();
        for (EscalationLevelMaster escalationLevelMaster : escalationLevelDtoList) {
            EscalationLevelMasterDto escalationLevelMasterDto = new EscalationLevelMasterDto();
            escalationLevelMasterDto.setId(escalationLevelMaster.getId());
            escalationLevelMasterDto.setName(escalationLevelMaster.getName());
            escalationLevelMasterDto.setNotificationTypeId(escalationLevelMaster.getNotificationTypeId());
            escalationLevelMasterDto.setUuid(escalationLevelMaster.getUuid());

            HashSet<Integer> roles = new HashSet<>();
            for (Map.Entry<Integer, Boolean> entry : escalationLevelMaster.getRolesWithPerformAction().entrySet()) {
                roles.add(entry.getKey());
            }

            escalationLevelMasterDto.setRoles(roles);
            escalationLevelMasterDto.setPerformAction(escalationLevelMaster.getRolesWithPerformAction());
            escalationLevelMasterDtos.add(escalationLevelMasterDto);
        }
        return escalationLevelMasterDtos;
    }

}
