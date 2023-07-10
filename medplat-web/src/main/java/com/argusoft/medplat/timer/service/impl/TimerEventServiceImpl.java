/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.timer.service.impl;

import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.dto.TimerEventDto;
import com.argusoft.medplat.timer.mapper.TimerEventMapper;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.timer.service.TimerEventHadlerService;
import com.argusoft.medplat.timer.service.TimerEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * <p>
 *     Define services for timer event.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Service("timerEventServiceDefault")
public class TimerEventServiceImpl extends Thread implements TimerEventService {

//    private AppTenantMaster appTenantMaster;
    private static final Logger log = LoggerFactory.getLogger(TimerEventServiceImpl.class);
    private static final int SLEEP_TIME = 200;
    private static final int EVENT_TIME_THRESOLD = 60 * 1000 / SLEEP_TIME;
    private int loopCounter = -1;
    private static final String DATE_PATTERN = "HH:mm:ss";
    private List<TimerEventDto> timerEvents = new LinkedList<>();

    @Autowired
    private TimerEventDao timerEventsDao;

    @Autowired
    @Lazy
    private TimerEventHadlerService timerEventHadlerService;

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void startTimerThread(AppTenantMaster tenantMaster) {
//        this.appTenantMaster = tenantMaster;
//        this.start();
//    }

    @Override
    public void startTimerThread() {
        this.start();
    }

    /**
     * Run timer event.
     */
    @Transactional
    @Override
    public void run() {
//        TenantThreadLocal.setTenant(appTenantMaster);
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
                  Timer Event Management Start
                  **********************************
                 */
                List<TimerEventDto> fetchedTimerEvents = null;
                if (loopCounter % EVENT_TIME_THRESOLD == 0) {

                    //Need to retrieve order by
                    long toTime = new Date().getTime() + 60000;
                    fetchedTimerEvents = retrieveTimerEvents(new Date(toTime));
                    loopCounter = 0;
                }

                List<TimerEventDto> timerEventToBeHandled = synchronizedTimerEvent(fetchedTimerEvents);
                // to avoid DB call in synchronization

                for (TimerEventDto timerEventDto : timerEventToBeHandled) {
                    try {
                        this.markAsProcessed(timerEventDto.getId());
                        timerEventDto.setStatus(TimerEvent.STATUS.PROCESSED);
                        timerEventDto.setProcessedOn(new Date());
                        timerEventDto.setProcessed(false);
                        timerEventHadlerService.handleTimerEvent(timerEventDto);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);

                    }

                }
            } catch (Exception e) {
                log.error(":: Timer Event Exception ::",e);
            }

            /*
              Timer Event Management END ***********************************
             */
        }
//        TenantThreadLocal.clear();

    }

    /**
     * Synchronized timer event.
     * @param fetchedTimerEvents List of timer events.
     * @return Returns list of timer events details.
     */
    private List<TimerEventDto> synchronizedTimerEvent( List<TimerEventDto> fetchedTimerEvents){
        List<TimerEventDto> timerEventToBeHandled = new LinkedList<>();
        synchronized (timerEvents) {
            if (fetchedTimerEvents != null) {

                timerEvents.addAll(fetchedTimerEvents);

            }

            if (CollectionUtils.isEmpty(timerEvents)) {
                //Need to work on this when it's empty don't call query after few second
            } else {

                for (int i = 0; i < timerEvents.size(); i++) {
                    try {
                        TimerEventDto timerEventDto = timerEvents.get(i);
                        if (timerEventDto.getSystemTriggerOn().before(new Date())) {
                            timerEventToBeHandled.add(timerEventDto);
                            timerEvents.remove(i);
                            i--;
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage(),ex);
                    }
                }
            }
        }
        return timerEventToBeHandled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void scheduleTimerEvent(TimerEventDto timerEventDto) {
        TimerEvent events = TimerEventMapper.convertTimerEventDtoToTimerEvents(timerEventDto);
        if (events.getId() != null) {
            timerEventsDao.merge(events);
        } else {
            timerEventsDao.create(events);
        }
        timerEventDto.setId(events.getId());
        boolean toAdd = false;
        if (timerEventDto.getSystemTriggerOn().before(new Date(new Date().getTime() + 59000)/* 2 Minite*/)) {
            toAdd = true;
        }

        if (toAdd) {
            synchronized (timerEvents) {
                timerEvents.add(timerEventDto);
            }
        }

    }

    /**
     * Retrieves all timer events.
     * @param toTime Time details.
     * @return Returns list of timer events.
     */
    @Transactional
    public List<TimerEventDto> retrieveTimerEvents(Date toTime) {
        List<TimerEvent> timerEventsList = timerEventsDao.retrieveTimerEvents(toTime);
        if (!CollectionUtils.isEmpty(timerEventsList)) {
            return TimerEventMapper.convertTimerEventsToTimerEventsDtos(timerEventsList);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void markAsProcessed(Integer id) {
        //Need to refactor this code
        timerEventsDao.changeTimeEventsStatus(Arrays.asList(id), TimerEvent.STATUS.PROCESSED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewStatusToProcessingEvents() {
        timerEventsDao.setNewStatusToProcessingEvents();
    }

    /**
     * Check time between 20 to 23.
     * @return Returns true/false based on time between 20 to 23.
     */
    private boolean checkTimeBetween20to23() {
        try {
            String string1 = "18:00:00";
            Date time1 = new SimpleDateFormat(DATE_PATTERN).parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);

            String string2 = "23:55:00";
            Date time2 = new SimpleDateFormat(DATE_PATTERN).parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String currentTime = new SimpleDateFormat(DATE_PATTERN).format(new Date());
            Date d = new SimpleDateFormat(DATE_PATTERN).parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                return true;
            }
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return false;
    }

}
