package com.argusoft.medplat.fcm.service.impl;

import com.argusoft.medplat.fcm.dao.TechoPushNotificationDao;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationTypeDao;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDto;
import com.argusoft.medplat.fcm.service.FirebaseMessagingService;
import com.argusoft.medplat.fcm.service.TechoPushNotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author nihar
 * @since 02/08/22 2:24 PM
 */
@Service
public class TechoPushNotificationHandlerImpl extends Thread implements TechoPushNotificationHandler {

//    private AppTenantMaster appTenantMaster;
    private final long SLEEP_TIME = 200l;
    private final long EVENT_TIME_THRESHOLD = 1 * 60 * 1000 / SLEEP_TIME;
    private long loopCounter = -1;
    public List<TechoPushNotificationDto> notificationList = new LinkedList<>();

    @Autowired
    private TechoPushNotificationDao techoPushNotificationDao;

    @Autowired
    private TechoPushNotificationTypeDao techoPushNotificationTypeDao;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    @Qualifier("smsTaskExecutor")
    private ThreadPoolTaskExecutor threadPolltaskExecutor;

//    @Override
//    public void startPushNotification(AppTenantMaster tenantMaster) {
//        this.appTenantMaster = tenantMaster;
//        this.start();
//    }

    @Override
    public void startPushNotification( ) {
        this.start();
    }

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
            }

            try {
                List<TechoPushNotificationDto> fetchedNotificationQueues = null;
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
                    fetchedNotificationQueues = this.retrievePushNotificationQueue(new Date(toTime), limit);
                    loopCounter = 0;
                }

                List<TechoPushNotificationDto> notificationToBeHandled = new LinkedList<>();
                synchronized (notificationList) {
                    if (fetchedNotificationQueues != null) {
                        notificationList.addAll(fetchedNotificationQueues);
                    }

                    if (CollectionUtils.isEmpty(notificationList)) {

                    } else {
                        for (int i = 0; i < notificationList.size(); i++) {
                            try {
                                TechoPushNotificationDto techoPushNotificationDto = notificationList.get(i);
                                if (techoPushNotificationDto.getCreatedOn().before(new Date())) {
                                    notificationToBeHandled.add(techoPushNotificationDto);
                                    notificationList.remove(i);
                                    i--;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                for (TechoPushNotificationDto notification : notificationToBeHandled) {
                    try {
                        this.markAsProcessed(notification.getId());
                        this.handleQueue(notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Transactional
    private List<TechoPushNotificationDto> retrievePushNotificationQueue(Date toTime, int limit) {
        return techoPushNotificationDao.getPushNotification(toTime, limit);
    }

    @Transactional
    private void markAsProcessed(Long id) {
        techoPushNotificationDao.markAsProcessed(id);
    }

    @Async("smsTaskExecutor")
    private void handleQueue(TechoPushNotificationDto techoPushNotificationDto) {
        try {
            String response = firebaseMessagingService.sendNotification(techoPushNotificationDto.getType(), techoPushNotificationDto.getHeading(),
                    techoPushNotificationDto.getMessage(),
                    null, techoPushNotificationDto.getToken(), techoPushNotificationDto.getMediaId(), techoPushNotificationDto.getEventId(), techoPushNotificationDto.getMessageEvent(),
                    techoPushNotificationDto.getHeadingEvent());
            techoPushNotificationDao.markAsSentAndSetResponse(techoPushNotificationDto.getId()
                    , response, null, true);
        } catch (Exception e) {
            techoPushNotificationDao.markAsSentAndSetResponse(techoPushNotificationDto.getId()
                    , null, e.getMessage(), false);
        }
    }
}
