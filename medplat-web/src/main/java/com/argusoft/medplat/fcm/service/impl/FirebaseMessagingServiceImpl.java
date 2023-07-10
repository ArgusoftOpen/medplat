package com.argusoft.medplat.fcm.service.impl;

import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.event.dao.EventConfigurationTypeDao;
import com.argusoft.medplat.fcm.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    @Autowired(required = false)
    FirebaseMessaging firebaseMessaging;

    @Autowired
    private SystemConfigurationDao systemConfigurationDao;

    @Autowired
    private EventConfigurationTypeDao eventConfigurationTypeDao;

    @Override
    public String sendNotification(String type, String title, String message, String body, String token, Integer image, String eventId, String messageEvent, String headingEvent) throws FirebaseMessagingException {
        Notification notification = null;
        HashMap<String, String> data = new HashMap<>();
        //check if it is through event config
//        if (eventId != null) {
//            EventConfigurationType eventConfigurationType = eventConfigurationTypeDao.retrieveById(eventId);
//            EventConfigTypeDto eventConfigTypeDto = EventConfigurationMapper.convertToConfigurationTypeDto(eventConfigurationType);
//        }

        if (messageEvent != null){
            message = messageEvent;
        }

        if (headingEvent != null){
            title = headingEvent;
        }

        data.put("notificationType", type);
        data.put("title", title);
        data.put("message", message);

        if (image != null) {
            SystemConfiguration systemConfiguration = systemConfigurationDao.retrieveSystemConfigurationByKey("SERVER_URL");
            String imagePath = systemConfiguration.getKeyValue() + "/api/push/file/" + image;
            notification = new Notification(title, message, imagePath);
        } else {
            notification = new Notification(title, message);
        }
        Message mess = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .build();

        return firebaseMessaging.send(mess);
    }

}
