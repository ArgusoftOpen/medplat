/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.model.EventConfiguration;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Define methods for event configuration.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface EventConfigurationDao extends GenericDao<EventConfiguration, Integer> {

    /**
     * Retrieves all active events.
     * @param isActive Is event active or not.
     * @return Returns list of all active events.
     */
    List<EventConfigurationDto> retrieveAll(Boolean isActive);

    /**
     * Retrieves event details by id.
     * @param id Event id.
     * @return Returns event configuration details by id.
     */
    EventConfiguration retrieveById(Integer id);

    /**
     * Retrieves event details by UUID.
     * @param uuid UUID.
     * @return Returns event configuration details by uuid.
     */
    EventConfiguration retrieveByUUID(UUID uuid);

    /**
     * Retrieves events by event type and event type details code.
     * @param eventType Event type like MANUAL, TIMER_BASED, FORM_SUBMITTED etc.
     * @param eventTypeDetailCode Query code to fetch details.
     * @return Returns list of event configuration based on defined params.
     */
    List<EventConfiguration> retrieveEventConfigsByEventTypeAndEventTypeDetailCode(String eventType, String eventTypeDetailCode);
}
