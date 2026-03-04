/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.service;

import com.argusoft.medplat.timer.dto.TimerEventDto;

/**
 *
 * <p>
 *     Define services for timer event.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public interface TimerEventService {

    /**
     * Schedule timer event.
     * @param timerEventDto Timer event details.
     */
    void scheduleTimerEvent(TimerEventDto timerEventDto);


    /**
     * Start thread of timer.
     * @param tenantMaster Tenant master.
     */
    void startTimerThread();

    /**
     * Set new status to processing events.
     */
    void setNewStatusToProcessingEvents();

    /**
     * Update status of sms queue to processed
     * @param id An id of sms queue
     */
    void markAsProcessed(Integer id);
}
