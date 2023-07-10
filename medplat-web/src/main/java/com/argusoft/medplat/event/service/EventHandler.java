/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.service;

import com.argusoft.medplat.event.dto.*;
import com.argusoft.medplat.event.model.EventConfiguration;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.query.dao.TableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>
 *     Define methods for event handler.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Service("EventHandler")
@Transactional // requirement of this could be seen later
public class EventHandler {

    @Autowired
    private TableDao tableDao;

    @Autowired
    private NotificationHandler notificationHandler;

    @Autowired
    private EventConfigurationService notificationConfigurationService;

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * Handle event.
     * @param e Event details.
     */
    public void handle(Event e) {
        try {
            List<EventConfigurationDto> eventConfigs = fetchEventConfig(e);
            if (eventConfigs != null) {
                for (EventConfigurationDto eventConfig : eventConfigs) {
                    processEventConfigs(e, eventConfig);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retrieves event configuration details by type.
     * @param e Event configuration details.
     * @return Returns list of event configuration details.
     * @throws IOException If an I/O error occurs when reading or writing.
     */
    public List<EventConfigurationDto> fetchEventConfig(Event e) throws IOException {
        List<EventConfigurationDto> eventConfigs = null;
        switch (e.geteType()) {
            case TIMER_EVENT:
                EventConfigurationDto eventConfigurationDto = notificationConfigurationService.retrieveById(e.getEventConfigId());
                if (eventConfigurationDto != null && eventConfigurationDto.getState().equals(EventConfiguration.State.ACTIVE)) {
                    eventConfigs = new LinkedList<>();
                    eventConfigs.add(eventConfigurationDto);
                }
                break;
            case FORM_SUBMITTED:
            case MANUAL:
                eventConfigs = notificationConfigurationService.retrieveEventConfigsByEventTypeAndEventTypeDetailCode(e.geteType().getName(), e.getResourceDto().getType());
                break;
        }
        return eventConfigs;
    }

    /**
     * Process event configuration.
     * @param e Event details.
     * @param notificationConfigurationDto Notification configuration details.
     */
    public void processEventConfigs(Event e, EventConfigurationDto notificationConfigurationDto) {

        for (EventConfigurationDetailDto notificationConfigDetail : notificationConfigurationDto.getNotificationConfigDetails()) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("resourceId", e.getResourceDto().getResourceDetailId());
            String query = EventFunctionUtil.replaceParameterWithValue(notificationConfigDetail.getQuery(), notificationConfigDetail.getQueryParam(), map);
            List<LinkedHashMap<String, Object>> queryDataLists = null;
            List<EventConditionDetailDto> notificationConditions = notificationConfigDetail.getConditions();
            if (!CollectionUtils.isEmpty(notificationConditions)) {
                queryDataLists = tableDao.executeQuery(query);
            } else {
                tableDao.executeUpdate(query);
            }
            handleEvent(notificationConditions,queryDataLists);
        }

    }

    /**
     * Handle event.
     * @param notificationConditions Notification conditions details.
     * @param queryDataLists Query data lists.
     */
    private void handleEvent(List<EventConditionDetailDto> notificationConditions,List<LinkedHashMap<String, Object>> queryDataLists){
        if (!CollectionUtils.isEmpty(notificationConditions) && !CollectionUtils.isEmpty(queryDataLists)) {
            for (EventConditionDetailDto notificationCondition : notificationConditions) {
                for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
                    boolean isConditionSatisfy = isConditionSatisfy(notificationCondition,queryDataList);
                    saveNotificationHandler(isConditionSatisfy,notificationCondition,queryDataList);
                }

            }
        }
    }

    /**
     * Check for condition satisfy or not.
     * @param notificationCondition Notification condition.
     * @param queryDataList List of query data.
     * @return Returns true/false based on condition satisfy or not.
     */
    private boolean isConditionSatisfy(EventConditionDetailDto notificationCondition,LinkedHashMap<String, Object> queryDataList){
        boolean isConditionSatisfy = true;
        if (Boolean.TRUE.equals(notificationCondition.getIsConditionReq())) {
            try {
                String condition = EventFunctionUtil.replaceParameterWithValue(notificationCondition.getCondition(),
                        notificationCondition.getConditionParam(), queryDataList);
                isConditionSatisfy = getConditionResult(condition);
            } catch (ScriptException ex) {
                throw new ImtechoSystemException("Condition Exception : " + notificationCondition.getCondition(), ex);
            }
        }
        return isConditionSatisfy;
    }

    /**
     * Save notification handler details.
     * @param isConditionSatisfy Is condition satisfy or not.
     * @param notificationCondition Notification condition.
     * @param queryDataList List of query data.
     */
    private void saveNotificationHandler(boolean isConditionSatisfy,EventConditionDetailDto notificationCondition,
                                         LinkedHashMap<String, Object> queryDataList){
        if (isConditionSatisfy && !CollectionUtils.isEmpty(notificationCondition.getNotificaitonConfigsType())) {
            for (EventConfigTypeDto notificationConfigTypeDto : notificationCondition.getNotificaitonConfigsType()) {
                List<LinkedHashMap<String, Object>> queryDataListsHashMaps = new LinkedList<>();
                queryDataListsHashMaps.add(queryDataList);
                notificationHandler.handle(notificationConfigTypeDto, queryDataListsHashMaps);
            }
        }
    }

    /**
     * Retrieves conditional result.
     * @param expr Expression.
     * @return Returns result based on expression.
     * @throws ScriptException Exception occurred on script.
     */
    private Boolean getConditionResult(String expr) throws ScriptException {
        return Boolean.valueOf(engine.eval(expr).toString());
    }

}
