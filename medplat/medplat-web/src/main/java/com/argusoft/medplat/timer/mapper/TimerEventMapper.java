/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.mapper;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.timer.dto.TimerEventDto;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>
 *     Mapper for timer event in order to convert dto to model or model to dto.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public class TimerEventMapper {
    private TimerEventMapper(){}

    /**
     * Convert timer event entity into dto.
     * @param events Entity of timer event.
     * @return Returns details of timer event.
     */
    public static TimerEventDto convertTimerEventToTimerEventsDto(TimerEvent events) {
        TimerEventDto dto = new TimerEventDto();
        if (events != null) {
            dto.setId(events.getId());
            dto.setRefId(events.getRefId());
            dto.setType(events.getType());
            dto.setProcessed(events.isProcessed());
            dto.setStatus(events.getStatus());
            dto.setSystemTriggerOn(events.getSystemTriggerOn());
            dto.setProcessedOn(events.getProcessedOn());
            dto.setEventConfigId(events.getEventConfigId());
            dto.setNotificationConfigId(events.getNotificationConfigId());
            dto.setQueryDataLists(ImtechoUtil.convertJsonToObject(events.getJsonData()));
        }
        return dto;
    }

    /**
     * Convert timer event details into entity.
     * @param dto Timer event details.
     * @return Returns entity of timer event.
     */
    public static TimerEvent convertTimerEventDtoToTimerEvents(TimerEventDto dto) {
        TimerEvent events = new TimerEvent();
        events.setId(dto.getId());
        events.setRefId(dto.getRefId());
        events.setProcessed(dto.isProcessed());
        events.setStatus(dto.getStatus());
        events.setType(dto.getType());
        events.setSystemTriggerOn(dto.getSystemTriggerOn());
        events.setProcessedOn(dto.getProcessedOn());
        events.setEventConfigId(dto.getEventConfigId());
        events.setNotificationConfigId(dto.getNotificationConfigId());
        try {
            events.setJsonData(ImtechoUtil.convertObjectToJson(dto.getQueryDataLists()));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(TimerEventMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return events;
    }

    /**
     * Convert list of timer event entities into details.
     * @param events List of timer events.
     * @return Returns list of timer event details.
     */
    public static List<TimerEventDto> convertTimerEventsToTimerEventsDtos(List<TimerEvent> events) {
        List<TimerEventDto> dtos = new ArrayList<>();
        for (TimerEvent event : events) {
            dtos.add(convertTimerEventToTimerEventsDto(event));
        }
        return dtos;
    }
}
