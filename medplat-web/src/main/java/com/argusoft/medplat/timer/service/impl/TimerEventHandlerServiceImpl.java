/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.service.impl;

import com.argusoft.medplat.common.dto.AnnouncementPushNotificationDto;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.AnnouncementService;
import com.argusoft.medplat.common.service.EmailService;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.EmailUtil;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.service.EventConfigurationService;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationDao;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationTypeDao;
import com.argusoft.medplat.fcm.model.TechoPushNotificationMaster;
import com.argusoft.medplat.fcm.model.TechoPushNotificationType;
import com.argusoft.medplat.fcm.service.TechoPushNotificationService;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.dto.TimerEventDto;
import com.argusoft.medplat.timer.mapper.TimerEventMapper;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.timer.service.TimerEventHadlerService;
import com.argusoft.medplat.timer.service.TimerEventService;
import com.argusoft.medplat.web.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

/**
 * <p>
 * Define services for timer event handler.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 11:00 AM
 */
@Service
public class TimerEventHandlerServiceImpl implements TimerEventHadlerService {

    @Autowired
    private TimerEventDao timerEventsDao;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TableService tableService;

    @Autowired
    private EventConfigurationService eventConfigurationService;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    @Lazy
    @Qualifier("timerEventServiceDefault")
    private TimerEventService timerEventService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private SmsStaffService smsStaffService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private TechoPushNotificationService techoPushNotificationService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private TechoPushNotificationTypeDao techoPushNotificationTypeDao;

    @Autowired
    private TechoPushNotificationDao techoPushNotificationDao;

    /**
     * Retrieves all timer events.
     *
     * @param toTime Time details.
     * @return Returns list of timer events.
     */
    @Transactional
    public List<TimerEventDto> retrieveTimerEvents(Date toTime) {
        List<TimerEvent> timerEvents = timerEventsDao.retrieveTimerEvents(toTime);
        if (!CollectionUtils.isEmpty(timerEvents)) {
            return TimerEventMapper.convertTimerEventsToTimerEventsDtos(timerEvents);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Update status of sms queue to processed
     *
     * @param id An id of sms queue
     */
    @Transactional
    public void markAsProcessed(Integer id) {
        //Need to refactor this code
        timerEventsDao.changeTimeEventsStatus(Collections.singletonList(id), TimerEvent.STATUS.PROCESSED);
    }

    /**
     * Mark as exception in event.
     *
     * @param id       Timer event id.
     * @param excetion Exception message.
     */
    @Transactional
    public void markAsExcetion(Integer id, String excetion) {
        //Need to refactor this code
        timerEventsDao.markAsExcetion(id, excetion);
    }

    /**
     * Mark event as complete.
     *
     * @param id Timer event id.
     */
    @Transactional
    public void markAsComplete(Integer id) {
        //Need to refactor this code
        timerEventsDao.markAsComplete(id);
    }

    /**
     * Update status of sms queue to processed
     *
     * @param timerEventIds List of timer events ids.
     */
    @Transactional
    public void markAsProcessed(Collection<Integer> timerEventIds) {
        timerEventsDao.changeTimeEventsStatus(timerEventIds, TimerEvent.STATUS.PROCESSED);
    }

    /**
     * Set is processed true.
     *
     * @param id Timer event id.
     */
    @Transactional
    public void setIsProccessedTrue(Integer id) {
        timerEventsDao.setIsProcessedTrue(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("timerEventTaskExecutor")
    public void handleTimerEvent(TimerEventDto timerEventDto) {

        try {
            switch (timerEventDto.getType()) {
                case EMAIL:
                    emailService.handle(
                            eventConfigurationService.retrieveNotificationConfigTypeDtoById(timerEventDto.getNotificationConfigId()),
                            timerEventDto.getQueryDataLists());
                    break;
                case SMS:
                    smsService.handle(eventConfigurationService.retrieveNotificationConfigTypeDtoById(timerEventDto.getNotificationConfigId()),
                            timerEventDto.getQueryDataLists());
                    break;
                case TIMER_EVENT:
                    Event event = new Event(Event.EVENT_TYPE.TIMER_EVENT, timerEventDto.getEventConfigId(), null, null);
                    EventConfigurationDto eventConfigurationDtos
                            = eventHandler.fetchEventConfig(event).get(0);
                    setIsProccessedTrue(timerEventDto.getId());
                    if (eventConfigurationDtos.getTrigerWhen() != null) {
                        switch (eventConfigurationDtos.getTrigerWhen()) {
                            case MONTHLY:
                            case DAILY:
                            case HOURLY:
                            case MINUTE:
                                Date nextDate = EventFunctionUtil.getNextDate(eventConfigurationDtos.getTrigerWhen(), eventConfigurationDtos);
                                timerEventService.scheduleTimerEvent(new TimerEventDto(null,
                                        TimerEvent.TYPE.TIMER_EVENT, nextDate, eventConfigurationDtos.getId(), null, null));

                                break;
                            default:
                        }
                    }
                    eventHandler.processEventConfigs(event, eventConfigurationDtos);

                    break;
                case QUERY:
                    tableService.queryHandler(eventConfigurationService.retrieveNotificationConfigTypeDtoById(timerEventDto.getNotificationConfigId()),
                            timerEventDto.getQueryDataLists());
                    break;
                case UNLOCK_USER:
                    userService.activateAccount(timerEventDto.getRefId());
                    break;
                case STAFF_SMS:
//                    smsStaffService.sendSms(timerEventDto);
                    break;
                case CONFIG_PUSH_NOTIFY:
                    techoPushNotificationService.sendPushNotification(timerEventDto);
                    break;
                case PUSH_NOTIFICATION:
                    techoPushNotificationService.handle(
                            eventConfigurationService.retrieveNotificationConfigTypeDtoById(timerEventDto.getNotificationConfigId()),
                            timerEventDto.getQueryDataLists());
                    break;
                case ANNOUNCEMENT_PUSH_NOTIFICATION:
                    List<TechoPushNotificationMaster> techoPushNotificationMasters = new ArrayList<>();
                    AnnouncementPushNotificationDto announcementPushNotificationDto = announcementService.getAnnouncementDetailsForPushNotification(timerEventDto.getRefId());
                    TechoPushNotificationType techoPushNotificationType = techoPushNotificationTypeDao.getTechoPushNotificationTypeByType("NEW_ANNOUNCEMENT");
                    for (Integer userId : announcementPushNotificationDto.getUsers()) {
                        TechoPushNotificationMaster techoPushNotificationMaster = new TechoPushNotificationMaster();
                        techoPushNotificationMaster.setUserId(userId);
                        techoPushNotificationMaster.setType(techoPushNotificationType.getType());
                        techoPushNotificationMaster.setMessage(announcementPushNotificationDto.getAnnouncementMaster().getSubject());
                        techoPushNotificationMaster.setHeading("New Announcement");
                        techoPushNotificationMasters.add(techoPushNotificationMaster);
                    }
                    techoPushNotificationService.createOrUpdateAll(techoPushNotificationMasters);
                    markAsComplete(timerEventDto.getId());
                    break;
                default:

            }
            this.markAsComplete(timerEventDto.getId());
        } catch (Exception e) {
//          For Timer Event (Event config) specific email sending
            if (timerEventDto.getType() == TimerEvent.TYPE.TIMER_EVENT) {
                SystemConfiguration systemConfiguration
                        = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EVENT_CONFIG_FAILED_EXECUTION_EXCEPTION_MAIL);
                if (systemConfiguration != null && "true".equalsIgnoreCase(systemConfiguration.getKeyValue())) {
                    String msg = "Event Configuration Failed !!!  Timer Event Id : " + timerEventDto.getId() + " Event config Id :" + timerEventDto.getEventConfigId();
                    e = new Exception(msg, e);
                    emailUtil.sendExceptionEmail(e, null);
                }

            }
            this.markAsExcetion(timerEventDto.getId(), org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
    }

}
