
package com.argusoft.medplat.common.service.impl;


import com.argusoft.medplat.common.dao.SystemConfigSyncDao;
import com.argusoft.medplat.common.dto.SystemConfigSyncDto;
import com.argusoft.medplat.common.mapper.SystemConfigSyncMapper;
import com.argusoft.medplat.common.model.SystemConfigSync;
import com.argusoft.medplat.common.service.SystemConfigSyncService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.service.EventConfigurationService;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.notification.service.NotificationMasterService;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.reportconfig.dao.ReportMasterDao;
import com.argusoft.medplat.reportconfig.dto.ReportConfigDto;
import com.argusoft.medplat.reportconfig.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Implements methods of SystemConfigSyncService
 * @author Hiren Morzariya
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class SystemConfigSyncServiceImpl implements SystemConfigSyncService {

    @Autowired
    private SystemConfigSyncDao systemConfigSyncDao;


    @Autowired
    private NotificationMasterService notificationMasterService;

    @Autowired
    private EventConfigurationService eventConfigurationService;
    
    @Autowired
    private QueryMasterService queryMasterService;
    
    @Autowired
    private ReportService reportService;
    
   @Autowired
    private ReportMasterDao reportMasterDao;
    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemConfigSyncDto> retriveSystemConfigBasedOnAccess(Integer id, String serverName) {
        List<SystemConfigSync> list = systemConfigSyncDao.retrieveSystemConfigBasedOnAccess(id, serverName);
        return SystemConfigSyncMapper.convertModalToDto(list);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String clearAndResetSync() {
        Gson gson = new Gson();
        int count = systemConfigSyncDao.deleteAllRow();
        JsonObject json = new JsonObject();
        json.addProperty("count", count);

        List<NotificationTypeMasterDto> allNotification = notificationMasterService.retrieveAll(null);

        for (NotificationTypeMasterDto dto : allNotification) {
            systemConfigSyncDao.createOrUpdate(SystemConfigSyncMapper.convertNotificationDtoToMaster(dto, ConstantUtil.SYNC_NOTIFICATION));
        }
        json.addProperty("allNotification", allNotification.size());

        List<EventConfigurationDto> allEvents = eventConfigurationService.retrieveAll();

        for (EventConfigurationDto dto : allEvents) {
            EventConfigurationDto eventDto = eventConfigurationService.retrieveById(dto.getId());
            systemConfigSyncDao.createOrUpdate(SystemConfigSyncMapper.convertEventConfigDtoToMaster(eventDto, ConstantUtil.SYNC_EVENT));
        }
        json.addProperty("allEvents", allEvents.size());

        List<QueryMasterDto> allQuery = queryMasterService.retrieveAll(null);

        for (QueryMasterDto dto : allQuery) {
            systemConfigSyncDao.createOrUpdate(SystemConfigSyncMapper.convertDtoToMaster(dto, ConstantUtil.SYNC_QUERY_BUILDER));
        }
        json.addProperty("allQuery", allQuery.size());

        List<Integer> allIds = reportMasterDao.getAllIds();
        for (Integer id : allIds) {
            ReportConfigDto dto = reportService.getDynamicReportDetailByIdOrCode(id, null, Boolean.FALSE);
            systemConfigSyncDao.createOrUpdate(SystemConfigSyncMapper.convertDtoToMaster(dto, ConstantUtil.SYNC_REPORT_MASTER));
        }
        json.addProperty("allReports", allIds.size());

        return gson.toJson(json);

    }

    

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createOrUpdate(Object obj, String featureType) {
        Integer id = null;
        String jsonObject;
        UUID featureUUID = null;
        
        switch (featureType) {
            case "QUERY_BUILDER":
                QueryMasterDto queryMasterDto = (QueryMasterDto) obj;
                try {
                    jsonObject = ImtechoUtil.convertObjectToJson(queryMasterDto);
                    featureUUID = queryMasterDto.getUuid();
                    id = systemConfigSyncDao.create(SystemConfigSyncMapper.convertDtoToMaster(featureType, queryMasterDto.getUuid(), queryMasterDto.getCode(), jsonObject, queryMasterDto.getCreatedBy()));

                } catch (JsonProcessingException e) {
                    throw new ImtechoUserException("JSON parsing from Query Master Object Exception: ", e);
                }
                break;

            case "REPORT_CONFIGURATION":
                ReportConfigDto reportConfigDto = (ReportConfigDto) obj;

                try {
                    jsonObject = ImtechoUtil.convertObjectToJson(reportConfigDto);
                    featureUUID = reportConfigDto.getUuid();
                    id = systemConfigSyncDao.create(SystemConfigSyncMapper.convertDtoToMaster(featureType, reportConfigDto.getUuid(), reportConfigDto.getName(), jsonObject, reportConfigDto.getCreatedBy()));

                } catch (JsonProcessingException e) {
                    throw new ImtechoUserException("JSON parsing from Report Configuration Object Exception: ", e);
                }
                break;
            
            case "NOTIFICATION_BUILDER":
                NotificationTypeMasterDto notificationMasterDto = (NotificationTypeMasterDto) obj;                
                try {
                    jsonObject = ImtechoUtil.convertObjectToJson(notificationMasterDto);
                    featureUUID = notificationMasterDto.getUuid();
                    id = systemConfigSyncDao.create(SystemConfigSyncMapper.convertDtoToMaster(featureType, notificationMasterDto.getUuid(), notificationMasterDto.getCode(), jsonObject,null));

                } catch (JsonProcessingException e) {
                    throw new ImtechoUserException("JSON parsing from Notification Builder Object Exception: ", e);
                }
                break;
                
            case "EVENT_BUILDER":
                EventConfigurationDto eventConfigurationDto = (EventConfigurationDto) obj;                
                try {
                    jsonObject = ImtechoUtil.convertObjectToJson(eventConfigurationDto);
                    featureUUID = eventConfigurationDto.getUuid();
                    id = systemConfigSyncDao.create(SystemConfigSyncMapper.convertDtoToMaster(featureType, eventConfigurationDto.getUuid(), eventConfigurationDto.getName(), jsonObject,eventConfigurationDto.getCreatedBy()));

                } catch (JsonProcessingException e) {
                    throw new ImtechoUserException("JSON parsing from Event configuration Object Exception: ", e);
                }
                break;

            default:
        }
        
//      After inserting record,will check for respective feature item is in sync or not
//      if it is then will insert record in access table also
//        List<Integer> serverIds = serverManagementDao.getActiveServerIdFromFeature(featureUUID);
//        for (Integer serverId : serverIds) {
//            serverManagementDao.insertSystemSyncWith(serverId, id);
//        }
     
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPassword(String serverName, String password) {
        return systemConfigSyncDao.checkPassword(serverName,password);
    }

}
