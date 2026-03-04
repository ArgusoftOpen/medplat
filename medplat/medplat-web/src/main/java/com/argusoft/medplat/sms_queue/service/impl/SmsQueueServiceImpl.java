package com.argusoft.medplat.sms_queue.service.impl;

import com.argusoft.medplat.sms_queue.dao.SmsQueueDao;
import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;
import com.argusoft.medplat.sms_queue.mapper.SmsQueueMapper;
import com.argusoft.medplat.sms_queue.model.SmsQueue;
import com.argusoft.medplat.sms_queue.service.SmsQueueHandlerService;
import com.argusoft.medplat.sms_queue.service.SmsQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     Implements methods of SmsQueueService
 * </p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
@Service("SmsQueueService")
public class SmsQueueServiceImpl extends Thread implements SmsQueueService {

    private static final Logger log = LoggerFactory.getLogger(SmsQueueServiceImpl.class);

//    private AppTenantMaster appTenantMaster;
    private static final long SLEEP_TIME = 200L;
    private static final long EVENT_TIME_THRESHOLD = 60 * 1000 / SLEEP_TIME;
    private long loopCounter = -1;
    private List<SmsQueueDto> smsQueues = new LinkedList<>();

    @Autowired
    private SmsQueueDao smsQueueDao;

    @Autowired
    @Qualifier("smsTaskExecutor")
    private ThreadPoolTaskExecutor threadPolltaskExecutor;

    @Autowired
    @Lazy
    private SmsQueueHandlerService smsQueueHandlerService;

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void startSmsThread(AppTenantMaster tenantMaster) {
//        this.appTenantMaster = tenantMaster;
//        this.start();
//    }

    @Override
    public void startSmsThread() {
        this.start();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatusFromProcessedToNewOnServerStartup(){
        smsQueueDao.updateStatusFromProcessedToNewOnServerStartup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void run() {
//        TenantThreadLocal.setTenant(this.appTenantMaster);
        boolean isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(SLEEP_TIME);
                loopCounter++;
            } catch (InterruptedException ex) {
                log.info("Thread Stop ::");
                Thread.currentThread().interrupt();
            }

            try {
                /*
                  Sms Queue Management Start
                  **********************************
                 */
                List<SmsQueueDto> fetchedSmsQueues = null;
                if (loopCounter % EVENT_TIME_THRESHOLD == 0) {
                    long toTime = new Date().getTime() + 60000;
                    
                    if (threadPolltaskExecutor.getThreadPoolExecutor().getQueue().size() >= 100000) {
                        threadPolltaskExecutor.setCorePoolSize(50);
                        threadPolltaskExecutor.setMaxPoolSize(50);
                    }
                    if (threadPolltaskExecutor.getThreadPoolExecutor().getQueue().size() < 100000) {
                        threadPolltaskExecutor.setCorePoolSize(5);
                        threadPolltaskExecutor.setMaxPoolSize(5);
                    }
                    int limit = threadPolltaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity();
                    fetchedSmsQueues = retrieveSmsQueue(new Date(toTime), limit);
                     loopCounter = 0;
                }

                List<SmsQueueDto> smsQueueToBeHandled = synchronizedSms(fetchedSmsQueues);
                // to avoid DB call in synchronization
                markAsProcessed(smsQueueToBeHandled);
            } catch (Exception e) {
                log.error(":: Sms Queue Exception ::", e);
            }

            /*
              Sms Queue Management END ***********************************
             */
        }
//        TenantThreadLocal.clear();

    }

    private List<SmsQueueDto> synchronizedSms(List<SmsQueueDto> fetchedSmsQueues){
        List<SmsQueueDto> smsQueueToBeHandled = new LinkedList<>();
        synchronized (smsQueues) {
            if (fetchedSmsQueues != null) {

                smsQueues.addAll(fetchedSmsQueues);
            }

            if (CollectionUtils.isEmpty(smsQueues)) {
                //Need to work on this when it's empty don't call query after few second
            } else {

                for (int i = 0; i < smsQueues.size(); i++) {
                    try {
                        SmsQueueDto smsQueueDto = smsQueues.get(i);
                        if (smsQueueDto.getCreatedOn().before(new Date())) {
                            smsQueueToBeHandled.add(smsQueueDto);
                            smsQueues.remove(i);
                            i--;
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        }
        return smsQueueToBeHandled;
    }

    /**
     * Returns a list of sms queue from given date
     * @param toTime A date time to fetch sms
     * @param limit A limit value
     * @return A list of SmsQueueDto
     */
    @Transactional
    public List<SmsQueueDto> retrieveSmsQueue(Date toTime, Integer limit) {
        List<SmsQueue> queues = smsQueueDao.retrieveSmsQueues(toTime, limit);
        if (!CollectionUtils.isEmpty(queues)) {
            return SmsQueueMapper.convertSmsQueuesToSmsQueueDtos(queues);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Update status of sms queue to processed
     * @param smsQueueToBeHandled A list of SmsQueueDto
     */
    @Transactional
    public void markAsProcessed(List<SmsQueueDto> smsQueueToBeHandled) {
        for (SmsQueueDto smsQueueDto : smsQueueToBeHandled) {
            try {
                smsQueueDao.markAsProcessed(smsQueueDto.getId());
                smsQueueHandlerService.handleSmsQueue(smsQueueDto);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }

    }
}
