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
 *     Define services for timer event handler.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
public interface TimerEventHadlerService {

    /**
     * Handle timer event.
     * @param timerEventDto Timer event details.
     */
    void handleTimerEvent(TimerEventDto timerEventDto);
}
