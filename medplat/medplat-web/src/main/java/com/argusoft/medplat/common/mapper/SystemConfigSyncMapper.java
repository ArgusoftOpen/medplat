/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.SystemConfigSyncDto;
import com.argusoft.medplat.common.model.SystemConfigSync;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.reportconfig.dto.ReportConfigDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *<p>
 *    An util class for system configuration to convert modal to dto  or dto to modal
 *</p>
 * @author ashish
 * @since 26/08/2020 5:30
 */
public class SystemConfigSyncMapper {

    private SystemConfigSyncMapper() {
            
    }

    /**
     * Converts even configuration dto to system config sync modal
     * @param eventConfigurationDto An instance of EventConfigurationDto
     * @param featureType A type of feature
     * @return An instance of SystemConfigSync
     */
    public static SystemConfigSync convertEventConfigDtoToMaster(EventConfigurationDto eventConfigurationDto, String featureType) {
        SystemConfigSync systemConfigSync = new SystemConfigSync();

        try {
            systemConfigSync.setFeatureUUID(eventConfigurationDto.getUuid());
            systemConfigSync.setFeatureName(eventConfigurationDto.getName());
            systemConfigSync.setFeatureType(featureType);
            systemConfigSync.setConfigJson(ImtechoUtil.convertObjectToJson(eventConfigurationDto));
            systemConfigSync.setCreatedOn(new Date());
            systemConfigSync.setCreatedBy(eventConfigurationDto.getCreatedBy());

        } catch (Exception e) {
            Logger.getLogger(SystemConfigSyncMapper.class.getName()).log(Level.INFO, e.getMessage(), e);
        }
        return systemConfigSync;
    }

    /**
     * Converts notification master dto to system config sync modal
     * @param notificationMasterDto An instance of NotificationTypeMasterDto
     * @param featureType A type of feature
     * @return An instance of SystemConfigSync
     */
     public static SystemConfigSync convertNotificationDtoToMaster(NotificationTypeMasterDto notificationMasterDto, String featureType) {
        SystemConfigSync systemConfigSync = new SystemConfigSync();

        try {
            systemConfigSync.setFeatureUUID(notificationMasterDto.getUuid());
            systemConfigSync.setFeatureName(notificationMasterDto.getNotificationName());
            systemConfigSync.setFeatureType(featureType);
            systemConfigSync.setConfigJson(ImtechoUtil.convertObjectToJson(notificationMasterDto));
            systemConfigSync.setCreatedOn(new Date());
        } catch (Exception e) {
            Logger.getLogger(SystemConfigSyncMapper.class.getName()).log(Level.INFO, e.getMessage(), e);
        }
        return systemConfigSync;
    }

    /**
     * Converts instance of given feature to system config sync modal
     * @param obj An instance of Object
     * @param featureType A type of feature
     * @return An instance of SystemConfigSync
     */
    public static SystemConfigSync convertDtoToMaster(Object obj, String featureType) {
        SystemConfigSync systemConfigSync = new SystemConfigSync();
        
        switch(featureType) {
            case "QUERY_BUILDER" :
                QueryMasterDto queryMasterDto = (QueryMasterDto) obj;
                try {
                    systemConfigSync.setFeatureUUID(queryMasterDto.getUuid());
                    systemConfigSync.setFeatureName(queryMasterDto.getCode());
                    systemConfigSync.setFeatureType(featureType);
                    systemConfigSync.setConfigJson(ImtechoUtil.convertObjectToJson(queryMasterDto));
                    systemConfigSync.setCreatedBy(queryMasterDto.getCreatedBy());
                    systemConfigSync.setCreatedOn(new Date());

                } catch (Exception e) {
                    Logger.getLogger(SystemConfigSyncMapper.class.getName()).log(Level.INFO, e.getMessage(), e);
                }
                break;
                
            case "REPORT_CONFIGURATION" :
                ReportConfigDto reportConfigDto = (ReportConfigDto) obj;
                try {
                    systemConfigSync.setFeatureUUID(reportConfigDto.getUuid());
                    systemConfigSync.setFeatureName(reportConfigDto.getName());
                    systemConfigSync.setFeatureType(featureType);
                    systemConfigSync.setConfigJson(ImtechoUtil.convertObjectToJson(reportConfigDto));
                    systemConfigSync.setCreatedBy(reportConfigDto.getCreatedBy());
                    systemConfigSync.setCreatedOn(new Date());

                } catch (Exception e) {
                    Logger.getLogger(SystemConfigSyncMapper.class.getName()).log(Level.INFO, e.getMessage(), e);
                }
                break;
            default:                
        }        
        return systemConfigSync;
    }

    /**
     * Returns SystemConfigSync modal based on given parameter
     * @param featureType A type of feature
     * @param uuid A value for uuid
     * @param featureName A name of feature
     * @param jsonObject A value for json object
     * @param createdBy An id of user
     * @return An instance of SystemConfigSync
     */
    public static SystemConfigSync convertDtoToMaster(String featureType, UUID uuid, String featureName, String jsonObject, Integer createdBy) {
        SystemConfigSync systemConfigSync = new SystemConfigSync();
        systemConfigSync.setFeatureType(featureType);
        systemConfigSync.setFeatureUUID(uuid);
        systemConfigSync.setFeatureName(featureName);
        systemConfigSync.setConfigJson(jsonObject);
        if (createdBy != null) {
            systemConfigSync.setCreatedBy(createdBy);
        }
        systemConfigSync.setCreatedOn(new Date());
        return systemConfigSync;
    }

    /**
     * Converts a list of system config modal to system config dto
     * @param list A list of SystemConfigSync
     * @return A list of SystemConfigSyncDto
     */
    public static List<SystemConfigSyncDto> convertModalToDto(List<SystemConfigSync> list) {
        List<SystemConfigSyncDto> results = new ArrayList<>();
        for (SystemConfigSync item : list) {
            SystemConfigSyncDto dto = new SystemConfigSyncDto();
            dto.setId(item.getId());
            dto.setFeatureUUID(item.getFeatureUUID());
            dto.setFeatureName(item.getFeatureName());
            dto.setConfigJson(item.getConfigJson());
            dto.setCreatedBy(item.getCreatedBy());
            dto.setCreatedOn(item.getCreatedOn());
            dto.setFeatureType(item.getFeatureType());
            results.add(dto);
        }
        return results;
    }
}
