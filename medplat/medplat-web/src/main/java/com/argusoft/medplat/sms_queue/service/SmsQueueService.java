
package com.argusoft.medplat.sms_queue.service;

/**
 * <p>
 *     Defines methods for sms queue
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
public interface SmsQueueService {

    void startSmsThread();


    /**
     * Updates status of sms queue from processed to new
     */
    void updateStatusFromProcessedToNewOnServerStartup();
    
}
