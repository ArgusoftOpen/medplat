/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.sms_queue.service;

import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;

/**
 * <p>
 *     Defines methods for sms queue handle service
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
public interface SmsQueueHandlerService {

       /**
        * Sends a sms from sms queue
        * @param smsQueueDto An instance of SmsQueueDto
        */
       void handleSmsQueue(SmsQueueDto smsQueueDto);
}
