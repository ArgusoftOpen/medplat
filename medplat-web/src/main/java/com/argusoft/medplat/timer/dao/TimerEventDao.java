/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.timer.model.TimerEvent;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for timer event.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
public interface TimerEventDao extends GenericDao<TimerEvent, Integer> {
    /**
     * Retrieves all timer events.
     * @param toTime Time details.
     * @return Returns list of timer events.
     */
    List<TimerEvent> retrieveTimerEvents(Date toTime);
      
    /**
     * Retrieves timer events by event config id.
     * @param eventConfigId Event config id.
     * @return Returns list of timer events by event config id.
     */
    List<TimerEvent> retrieveTimerEventsByConfig(Integer eventConfigId);

    /**
     * Add/Update timer event.
     * @param timerEvent Timer event details.
     * @return Returns timer event details.
     */
    TimerEvent createUpdate(TimerEvent timerEvent);

    /**
     * Change status of timer event.
     * @param timerEventIds List of timer events ids.
     * @param status Define status of timer event.
     */
    void changeTimeEventsStatus(Collection<Integer> timerEventIds, TimerEvent.STATUS status);

    /**
     * Set is processed true.
     * @param timerEventId Timer event id.
     */
    void setIsProcessedTrue(Integer timerEventId);

    /**
     * Mark as exception in event.
     * @param id Timer event id.
     * @param excetion Exception message.
     */
    void markAsExcetion(Integer id, String excetion);

    /**
     * Mark event as complete.
     * @param id Timer event id.
     */
    void markAsComplete(Integer id);

    /**
     * Set new status to processing events.
     */
    void setNewStatusToProcessingEvents();

    /**
     * Retrieves timer event by ref id and timer event type.
     * @param refId Ref id.
     * @param type Timer event type.
     * @return Returns timer event by ref id and timer event type.
     */
    TimerEvent findByRefIdAndType(Integer refId,TimerEvent.TYPE type );

    /**
     * Delete timer event by ref id and timer event type.
     * @param refId Ref id.
     * @param type Type of timer event.
     */
    void deleteByRefIdAndType(Integer refId,TimerEvent.TYPE type);

}
