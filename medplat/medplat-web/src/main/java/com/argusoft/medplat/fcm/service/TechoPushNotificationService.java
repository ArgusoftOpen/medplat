package com.argusoft.medplat.fcm.service;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationMaster;
import com.argusoft.medplat.timer.dto.TimerEventDto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author nihar
 * @since 02/08/22 2:24 PM
 */
public interface TechoPushNotificationService {

    //void sendNotification();

    void sendPushNotification(TimerEventDto timerEventDto);

    void createOrUpdateAll(List<TechoPushNotificationMaster> techoPushNotificationMasters);

    void handle(EventConfigTypeDto eventConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists);
}
