/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.task.service.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.task.dto.WebTaskDetailDto;
import com.argusoft.medplat.dashboard.task.dto.WebTaskMasterDto;
import com.argusoft.medplat.dashboard.task.service.WebTaskDashboardService;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dao.TechoWebNotificationMasterDao;
import com.argusoft.medplat.notification.dao.TechoWebNotificationResponseDetailDao;
import com.argusoft.medplat.notification.mapper.TechoWebNotificationResponseDetailMapper;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;
import com.argusoft.medplat.notification.model.TechoWebNotificationMaster;
import com.argusoft.medplat.notification.model.TechoWebNotificationResponseDetail;
import com.argusoft.medplat.query.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * <p>Method implementation for web task dashboard service</p>
 * @author kunjan
 * @since 31/08/20 11:15 PM
 * 
 */
@Service
@Transactional
public class WebTaskDashboardServiceImpl implements WebTaskDashboardService {

    private static final String TASK_ID="taskId";
    private static final String IS_ACTION_REQUIRED="isActionRequired";
    private static final String IS_OTHER_DETAILS_REQUIRED="isOtherDetailsRequired";

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    TableService tableService;

    @Autowired
    private NotificationTypeMasterDao notificationTypeMasterDao;

    @Autowired
    private TechoWebNotificationMasterDao webNotificationMasterDao;

    @Autowired
    private TechoWebNotificationResponseDetailDao techoWebNotificationResponseDetailDao;

    @Autowired
    private EventHandler eventHandler;

    @Override
    public List<WebTaskMasterDto> getWebTaskCount() {
        return webNotificationMasterDao.getWebTaskCount(user.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebTaskDetailDto> getWebTaskDetailByType(Integer taskTypeId, Integer limit, Integer offset, Integer taskId, Integer loggedInUserId, Integer userId, Integer memberId, Integer locationId) {
        List<WebTaskDetailDto> webTaskDetailDtos = new LinkedList<>();
        String parameterCommaSeparatedString = "taskTypeId,limit,offset,userId,memberId,loggedInUserId,locationId";
        LinkedHashMap<String, Object> parameterValue = new LinkedHashMap<>();
        parameterValue.put("taskTypeId", taskTypeId);
        parameterValue.put("limit", limit);
        parameterValue.put("offset", offset);
        parameterValue.put("userId", userId);
        parameterValue.put("memberId", memberId);
        parameterValue.put("loggedInUserId", loggedInUserId);
        parameterValue.put("locationId", locationId);

        NotificationTypeMaster notificationTypeMaster = notificationTypeMasterDao.retrieveById(taskTypeId);

        if (notificationTypeMaster == null || notificationTypeMaster.getDataQuery() == null) {
            return Collections.emptyList();
        }

        String dataQuery = notificationTypeMaster.getDataQuery();

        dataQuery = EventFunctionUtil.replaceParameterWithValue(dataQuery, parameterCommaSeparatedString, parameterValue);

        List<LinkedHashMap<String, Object>> queryResults = tableService.executeQuery(dataQuery);

        queryResults.forEach(aRow -> {

            if (aRow.get(TASK_ID) == null) {
                throw new ImtechoSystemException("Field taskId is found to be null in the result set."
                        + " Parameter taskTypeId : " + taskTypeId + ",limit : " + limit + ",offset :" + offset + ",taskId : " + taskId + ",loggedInUserId : " + loggedInUserId
                        + ",userId : " + userId + ",memberId : " + memberId, 101);
            }
            Integer bigInteger = (Integer) aRow.get(TASK_ID);
            if (taskId == null || !taskId.equals(bigInteger)) {
                WebTaskDetailDto webTaskDetailDto = new WebTaskDetailDto();
                webTaskDetailDto.setTaskId(bigInteger);
                aRow.remove(TASK_ID);

                if (aRow.get(IS_ACTION_REQUIRED) == null) {
                    webTaskDetailDto.setIsActionRequired(Boolean.FALSE);
                } else {
                    webTaskDetailDto.setIsActionRequired((Boolean) aRow.get(IS_ACTION_REQUIRED));
                    aRow.remove(IS_ACTION_REQUIRED);
                }

                if (aRow.get(IS_OTHER_DETAILS_REQUIRED) == null) {
                    webTaskDetailDto.setIsOtherDetailsRequired(Boolean.FALSE);
                } else {
                    webTaskDetailDto.setIsOtherDetailsRequired((Boolean) aRow.get(IS_OTHER_DETAILS_REQUIRED));
                    aRow.remove(IS_OTHER_DETAILS_REQUIRED);
                }

                webTaskDetailDto.setDetails(aRow);
                webTaskDetailDtos.add(webTaskDetailDto);
            }
        });

        return webTaskDetailDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<WebTaskMasterDto>> getSelectedTaskDetailAndOtherRelatedTasksWithAction(Integer taskId) {
        Map<String, List<WebTaskMasterDto>> result = new LinkedHashMap<>();
        List<WebTaskMasterDto> taskMasterDtos = new LinkedList<>();
        TechoWebNotificationMaster techoWebNotificationMaster = webNotificationMasterDao.retrieveById(taskId);
        NotificationTypeMaster notificationTypeForSelectedTask = notificationTypeMasterDao.retrieveById(techoWebNotificationMaster.getNotificationTypeId());
        WebTaskMasterDto webTaskMasterDto = new WebTaskMasterDto();
        webTaskMasterDto.setName(notificationTypeForSelectedTask.getNotificationName());
        webTaskMasterDto.setWebTasks(new LinkedList<>());
        WebTaskDetailDto webTaskDetailDto = new WebTaskDetailDto();
        webTaskDetailDto.setTaskId(taskId);
        webTaskDetailDto.setActions(getActionFromTaskType(taskId, notificationTypeForSelectedTask));
        webTaskMasterDto.getWebTasks().add(webTaskDetailDto);
        taskMasterDtos.add(webTaskMasterDto);
        result.put("selected", taskMasterDtos);

        List<WebTaskMasterDto> otherTaskMasterDtos = new LinkedList<>();
        WebTaskMasterDto taskMaster = new WebTaskMasterDto();
        taskMaster.setId(1);
        taskMaster.setName(notificationTypeForSelectedTask.getNotificationName());

        List<WebTaskDetailDto> webTaskDetailByType = null;
        if (notificationTypeForSelectedTask.getNotificationFor().toString().equals("MEMBER")) {
            webTaskDetailByType = this.getWebTaskDetailByType(notificationTypeForSelectedTask.getId(), null, null, taskId, user.getId(), null, techoWebNotificationMaster.getMemberId(), null);
        } else if (notificationTypeForSelectedTask.getNotificationFor().toString().equals("USER")) {
            webTaskDetailByType = this.getWebTaskDetailByType(notificationTypeForSelectedTask.getId(), null, null, taskId, user.getId(), techoWebNotificationMaster.getUserId(), null, null);
        }

        List<LinkedHashMap<String, Object>> actionFromTaskType = getActionFromTaskType(taskId, notificationTypeForSelectedTask);
        if (!CollectionUtils.isEmpty(webTaskDetailByType)) {
            for (WebTaskDetailDto taskDetailDto : webTaskDetailByType) {
                taskDetailDto.setActions(actionFromTaskType);
            }

            taskMaster.setWebTasks(webTaskDetailByType);
            otherTaskMasterDtos.add(taskMaster);
        }
        result.put("other", otherTaskMasterDtos);
        return result;
    }

    /**
     * Returns action from task type
     * @param taskId type id
     * @param notificationType notification type
     * @return List of hash map
     */
    private List<LinkedHashMap<String, Object>> getActionFromTaskType(Integer taskId, NotificationTypeMaster notificationType) {
        if (notificationType.getModalBasedAction() || notificationType.getUrlBasedAction() || notificationType.getActionQuery() == null) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, Object> parameterValue = new LinkedHashMap<>();
        parameterValue.put(TASK_ID, taskId);
        String dataQuery = notificationType.getActionQuery();
        dataQuery = EventFunctionUtil.replaceParameterWithValue(dataQuery, TASK_ID, parameterValue);
        return tableService.executeQuery(dataQuery);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveActions(List<WebTaskDetailDto> taskDetailDtos) {
        List<Integer> taskIds = new LinkedList<>();
        Map<Integer, WebTaskDetailDto> mapOfTasksWithIdAsKey = new LinkedHashMap<>();
        for (WebTaskDetailDto taskDetailDto : taskDetailDtos) {
            if (taskDetailDto.getSelectedAction() != null && !taskDetailDto.getSelectedAction().isEmpty()) {
                taskIds.add(taskDetailDto.getTaskId());
                mapOfTasksWithIdAsKey.put(taskDetailDto.getTaskId(), taskDetailDto);
            }
        }

        List<TechoWebNotificationMaster> notificationMasters = webNotificationMasterDao.retriveByIds("id", taskIds);
        List<TechoWebNotificationResponseDetail> notificationResponseDetails = new LinkedList<>();

        for (TechoWebNotificationMaster notificationMaster : notificationMasters) {
            notificationResponseDetails.add(TechoWebNotificationResponseDetailMapper.convertToTechoWebNotificationResposeDetailEntity(notificationMaster,
                    mapOfTasksWithIdAsKey.get(notificationMaster.getId()).getSelectedAction(),
                    mapOfTasksWithIdAsKey.get(notificationMaster.getId()).getOtherDetail(),
                    TechoWebNotificationMaster.State.COMPLETED));
            notificationMaster.setActionTaken(mapOfTasksWithIdAsKey.get(notificationMaster.getId()).getSelectedAction());
            notificationMaster.setOtherDetails(mapOfTasksWithIdAsKey.get(notificationMaster.getId()).getOtherDetail());
            notificationMaster.setState(TechoWebNotificationMaster.State.COMPLETED);
            notificationMaster.setActionBy(user.getId());
        }
        techoWebNotificationResponseDetailDao.createOrUpdateAll(notificationResponseDetails);
        webNotificationMasterDao.updateAll(notificationMasters);
        techoWebNotificationResponseDetailDao.flush();
        for (TechoWebNotificationMaster notificationMaster : notificationMasters) {
            Event event = new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, notificationMaster.getNotificationTypeMaster().getCode(), notificationMaster.getId());
            eventHandler.handle(event);
        }
    }

}
