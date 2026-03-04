package com.argusoft.medplat.sms_queue.service.impl;

import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.sms_queue.dao.SmsQueueDao;
import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;
import com.argusoft.medplat.sms_queue.service.SmsQueueHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * <p>
 *     Implements methods of SmsQueueHandlerService
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
@Service("SmsQueueHandlerService")
public class SmsQueueHandlerServiceImpl implements SmsQueueHandlerService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsQueueDao smsQueueDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("smsTaskExecutor")
    @Transactional
    public void handleSmsQueue(SmsQueueDto smsQueueDto) {
        Integer smsId = smsService.sendSms(smsQueueDto.getMobileNumber(), smsQueueDto.getMessage(), true, smsQueueDto.getMessageType());
        this.markAsSentAndSetSmsId(smsQueueDto.getId(), smsId);
    }

    /**
     * Update the status of sms queue and set sms id
     * @param id An id of sms queue
     * @param smsId An id of smms
     */
    @Transactional
    public void markAsSentAndSetSmsId(Integer id, Integer smsId) {
        smsQueueDao.markAsSentAndSetSmsId(id, smsId);
    }

}
