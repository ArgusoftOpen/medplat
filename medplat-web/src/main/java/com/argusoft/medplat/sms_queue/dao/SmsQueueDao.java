package com.argusoft.medplat.sms_queue.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.sms_queue.model.SmsQueue;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Defines database methods for sms queue
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
public interface SmsQueueDao extends GenericDao<SmsQueue, Integer>{

    /**
     * Returns a list of sms queue from given date
     * @param toTime A date time to fetch sms
     * @param limit A limit value
     * @return A list of SmsQueue
     */
    List<SmsQueue> retrieveSmsQueues(Date toTime, Integer limit);

    /**
     * Update status of sms queue to processed
     * @param id An id of sms queue
     */
    void markAsProcessed(Integer id);

    /**
     * Update the status of sms queue and set sms id
     * @param id An id of sms queue
     * @param smsId An id of smms
     */
    void markAsSentAndSetSmsId(Integer id, Integer smsId);

    /**
     * Updates status of sms queue from processed to new
     */
    void updateStatusFromProcessedToNewOnServerStartup();
}

