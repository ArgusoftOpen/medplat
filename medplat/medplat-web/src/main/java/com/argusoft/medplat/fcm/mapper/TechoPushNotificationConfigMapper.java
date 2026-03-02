/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fcm.mapper;

import com.argusoft.medplat.fcm.dto.TechoPushNotificationConfigDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationConfig;
import com.argusoft.medplat.fcm.model.TechoPushNotificationLocationDetail;
import com.argusoft.medplat.fcm.model.TechoPushNotificationRoleUserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ketul
 */
public class TechoPushNotificationConfigMapper {
    public static TechoPushNotificationConfigDto
    convertModelToDto(TechoPushNotificationConfig techoPushNotificationConfig) {
        TechoPushNotificationConfigDto dto = new TechoPushNotificationConfigDto();
        if (techoPushNotificationConfig != null) {
            dto.setId(techoPushNotificationConfig.getId());
            dto.setName(techoPushNotificationConfig.getName());
            dto.setNotificationTypeId(techoPushNotificationConfig.getNotificationTypeId());
            dto.setDescription(techoPushNotificationConfig.getDescription());
            dto.setConfigType(techoPushNotificationConfig.getConfigType());
            dto.setTriggerType(techoPushNotificationConfig.getTriggerType());
            dto.setStatus(techoPushNotificationConfig.getStatus());
            dto.setState(techoPushNotificationConfig.getState());
            dto.setDateTime(techoPushNotificationConfig.getScheduleDateTime());
            dto.setQueryUUID(techoPushNotificationConfig.getQueryUUID());
            dto.setCreatedBy(techoPushNotificationConfig.getCreatedBy());
            dto.setCreatedOn(techoPushNotificationConfig.getCreatedOn());
        }
        return dto;
    }

    public static TechoPushNotificationConfig convertPushNotificationConfigDtoTOPushNotificationConfig
            (TechoPushNotificationConfigDto dto) {
        TechoPushNotificationConfig techoPushNotificationConfig = new TechoPushNotificationConfig();
        if (techoPushNotificationConfig != null) {
            techoPushNotificationConfig.setId(dto.getId());
            techoPushNotificationConfig.setName(dto.getName());
            techoPushNotificationConfig.setNotificationTypeId(dto.getNotificationTypeId());
            techoPushNotificationConfig.setDescription(dto.getDescription());
            techoPushNotificationConfig.setConfigType(dto.getConfigType());
            techoPushNotificationConfig.setTriggerType(dto.getTriggerType());
            techoPushNotificationConfig.setStatus(dto.getStatus());
            techoPushNotificationConfig.setState(dto.getState());
            techoPushNotificationConfig.setScheduleDateTime(dto.getDateTime());
            techoPushNotificationConfig.setQueryUUID(dto.getQueryUUID());
            techoPushNotificationConfig.setCreatedBy(dto.getCreatedBy());
            techoPushNotificationConfig.setCreatedOn(dto.getCreatedOn());
        }
        return techoPushNotificationConfig;
    }

    public static List<TechoPushNotificationLocationDetail> convertPushNotificationConfigDtoTOPushNotificationLocationList(TechoPushNotificationConfigDto dto) {
        List<TechoPushNotificationLocationDetail> locationDetails = new ArrayList<>();
        if (dto != null && dto.getLocations() != null) {
            dto.getLocations().forEach((location) -> {
                TechoPushNotificationLocationDetail locationDetail = new TechoPushNotificationLocationDetail();
                locationDetail.setPushConfigId(dto.getId());
                locationDetail.setLocationId(Integer.parseInt(location.getLocationId().toString()));
                locationDetails.add(locationDetail);
            });
        }
        return locationDetails;
    }

    public static List<TechoPushNotificationRoleUserDetail> convertPushNotificationConfigDtoTOPushNotificationRoleUserDetail(TechoPushNotificationConfigDto dto) {
        List<TechoPushNotificationRoleUserDetail> rolesDetail = new ArrayList<>();
        if (dto != null && dto.getRoles() != null) {
            dto.getRoles().forEach((role) -> {
                TechoPushNotificationRoleUserDetail roleDetail = new TechoPushNotificationRoleUserDetail();
                roleDetail.setPushConfigId(dto.getId());
                roleDetail.setRoleId(Integer.parseInt(role.getRoleId().toString()));
                rolesDetail.add(roleDetail);
            });
        }
        return rolesDetail;
    }
}
