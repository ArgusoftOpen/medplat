package com.argusoft.medplat.fcm.service.impl;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.fcm.dao.*;
import com.argusoft.medplat.fcm.model.*;
import com.argusoft.medplat.fcm.service.TechoPushNotificationService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.dto.TimerEventDto;
import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.argusoft.medplat.fcm.service.MedplatMessagingService;

/**
 * @author nihar
 * @since 02/08/22 2:24 PM
 */
@Service
@Transactional
public class TechoPushNotificationServiceImpl implements TechoPushNotificationService {

    @Autowired
    private TechoPushNotificationConfigDao techoPushNotificationConfigDao;

    @Autowired
    private TechoPushNotificationTypeDao techoPushNotificationTypeDao;

    @Autowired
    private TechoPushNotificationLocationDetailDao techoPushNotificationLocationDetailDao;

    @Autowired
    private TechoPushNotificationRoleUserDetailDao techoPushNotificationRoleUserDetailDao;

    @Autowired
    private TechoPushNotificationDao techoPushNotificationDao;

    @Autowired
    private TimerEventDao timerEventDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private QueryMasterService queryMasterService;

    @Autowired
    private MedplatMessagingService firebaseMessagingService;

    @Autowired
    private FirebaseTokenDao FirebaseTokenDao;

@Override
public void sendPushNotification(TimerEventDto timerEventDto) {
    TechoPushNotificationConfig techoPushNotificationConfig
            = techoPushNotificationConfigDao.retrieveById(timerEventDto.getRefId());
    List<UserMaster> users = new ArrayList<>();
    if (techoPushNotificationConfig.getConfigType().equals(TechoPushNotificationConfig.ConfigType.ROLE_LOCATION_BASED)) {
        List<TechoPushNotificationLocationDetail> locations = techoPushNotificationLocationDetailDao.
                findByNotificationConfigId(techoPushNotificationConfig.getId());
        List<Integer> locationIds = new LinkedList<>();
        for (TechoPushNotificationLocationDetail location : locations) {
            locationIds.add(location.getLocationId());
        }
        List<TechoPushNotificationRoleUserDetail> roles = techoPushNotificationRoleUserDetailDao.
                findByNotificationConfigId(techoPushNotificationConfig.getId());
        List<Integer> roleIds = new LinkedList<>();
        for (TechoPushNotificationRoleUserDetail role : roles) {
            roleIds.add(role.getRoleId());
        }

        List<UserMaster> finalUserList = this.userDao.getUsersByLocationsAndRolesUsingParentLocation(locationIds, roleIds);

        //Filter out users who does not have firebase token
        if(!finalUserList.isEmpty() && finalUserList.size()>0){
            for (UserMaster userMaster:finalUserList
            ) {
                if(!FirebaseTokenDao.retrieveByUserId(userMaster.getId()).isEmpty()){
                    users.add(userMaster);
                }
            }
        }

    } else if (techoPushNotificationConfig.getConfigType().equals(TechoPushNotificationConfig.ConfigType.QUERY_BASED)) {

        QueryMasterDto queryMasterDto = queryMasterService
                .retrieveByUUID(techoPushNotificationConfig.getQueryUUID());

        QueryDto queryDto = new QueryDto();
        queryDto.setCode(queryMasterDto.getCode());
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultDtos = queryMasterService.executeQuery(queryDtos, true);
        for (LinkedHashMap<String, Object> result : resultDtos.get(0).getResult()) {
            if (result.get("user_id") != null) {
                UserMaster userMaster = new UserMaster();
                userMaster.setId(Integer.parseInt(result.get("user_id").toString()));
                users.add(userMaster);
            }
        }
    }

    TechoPushNotificationType techoPushNotificationType =
            techoPushNotificationTypeDao.retrieveById(techoPushNotificationConfig.getNotificationTypeId());

    List<TechoPushNotificationMaster> techoPushNotificationMasters = new LinkedList();
    for (UserMaster user : users) {
        TechoPushNotificationMaster techoPushNotificationMaster
                = new TechoPushNotificationMaster();
        techoPushNotificationMaster.setUserId(user.getId());
        techoPushNotificationMaster.setType(techoPushNotificationType.getType());
        techoPushNotificationMasters.add(techoPushNotificationMaster);
    }
    // to mark status completed
    timerEventDao.markAsComplete(timerEventDto.getId());

    try {
        techoPushNotificationDao.createOrUpdateAll(techoPushNotificationMasters);
    } catch (Exception ex) {
        Logger.getLogger(TechoPushNotificationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    techoPushNotificationConfig.setStatus(TechoPushNotificationConfig.Status.SENT);
    techoPushNotificationConfigDao.saveOrUpdate(techoPushNotificationConfig);

}

    @Override
    public void createOrUpdateAll(List<TechoPushNotificationMaster> techoPushNotificationMasters) {
        techoPushNotificationDao.createOrUpdateAll(techoPushNotificationMasters);
    }

    @Override
    public void handle(EventConfigTypeDto eventConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        String template = eventConfigTypeDto.getPushNotificationConfigJson().getTemplate();
        String heading = eventConfigTypeDto.getPushNotificationConfigJson().getHeading();
        List<TechoPushNotificationMaster> techoPushNotificationMasters = new LinkedList();
        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            if (!queryDataList.containsKey(eventConfigTypeDto.getPushNotificationConfigJson().getUserFieldName())) {
                throw new ImtechoSystemException("UserId field not found. Event config id : " + eventConfigTypeDto.getId(), 503);
            }
            String userId = queryDataList.get(eventConfigTypeDto.getPushNotificationConfigJson().getUserFieldName()).toString();

            if (userId != null) {
                template = EventFunctionUtil.replaceParameterWithValue(template, eventConfigTypeDto.getTemplateParameter(), queryDataList);
                heading = EventFunctionUtil.replaceParameterWithValue(heading, eventConfigTypeDto.getPushNotificationConfigJson().getHeading().replace("#",""), queryDataList);
                TechoPushNotificationType techoPushNotificationType = techoPushNotificationTypeDao.retrieveById(eventConfigTypeDto.getPushNotificationConfigJson().getNotificationTypeId());
                TechoPushNotificationMaster techoPushNotificationMaster = new TechoPushNotificationMaster();
                techoPushNotificationMaster.setUserId(Integer.parseInt(userId));
                techoPushNotificationMaster.setType(techoPushNotificationType.getType());
                techoPushNotificationMaster.setEventId(eventConfigTypeDto.getId());
                techoPushNotificationMaster.setMessage(template);
                techoPushNotificationMaster.setHeading(heading);
                techoPushNotificationMasters.add(techoPushNotificationMaster);
            }
        }
        try {
            techoPushNotificationDao.createOrUpdateAll(techoPushNotificationMasters);
        } catch (Exception ex) {
            Logger.getLogger(TechoPushNotificationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
