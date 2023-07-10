/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.service;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.dto.EventConfigurationDto;

import java.io.IOException;
import java.util.List;

/**
 *
 * <p>
 *     Define services for event configuration.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public interface EventConfigurationService {

    /**
     * Add/Update event configuration.
     * @param notificationConfig Event configuration details.
     * @param isMetodCallBySyncFunction Is method called by sync function.
     * @throws IOException If an I/O error occurs when reading or writing.
     */
    void saveOrUpdate(EventConfigurationDto notificationConfig,boolean isMetodCallBySyncFunction) throws IOException;

    /**
     * Retrieves all events.
     * @return Returns list of all events.
     */
    List<EventConfigurationDto> retrieveAll();

    /**
     * Retrieves event details by id.
     * @param id Event id.
     * @return Returns event configuration details by id.
     */
    EventConfigurationDto retrieveById(Integer id);

    /**
     * Retrieve notification config type dto by id.
     * @param id Notification id.
     * @return Returns event config type details.
     */
    EventConfigTypeDto retrieveNotificationConfigTypeDtoById(String id);

    /**
     * Retrieves events by event type and event type details code.
     * @param name Event type like MANUAL, TIMER_BASED, FORM_SUBMITTED etc.
     * @param eventTypeDetailCode Query code to fetch details.
     * @return Returns list of event configuration based on defined params.
     * @throws IOException An I/O error occurs when reading or writing.
     */
    List<EventConfigurationDto> retrieveEventConfigsByEventTypeAndEventTypeDetailCode(String name, String eventTypeDetailCode) throws IOException;

    /**
     * Make event active or inactive.
     * @param notificationConfigurationDto Event configuration details.
     */
    void toggleState(EventConfigurationDto notificationConfigurationDto);

    /**
     * Run event by event id.
     * @param notificationConfigId Event id.
     */
    void runEvent(Integer notificationConfigId);
}
