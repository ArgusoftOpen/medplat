package com.argusoft.medplat.fcm.service;

import com.argusoft.medplat.fcm.model.TechoPushNotificationType;

import java.util.List;

/**
 * @author nihar
 * @since 12/10/22 6:01 PM
 */
public interface TechoPushNotificationTypeService {

    void createOrUpdate(TechoPushNotificationType techoPushNotificationType);

    List<TechoPushNotificationType> getNotificationTypeList();

    TechoPushNotificationType getNotificationTypeById(Integer id);

//    File getPushNotificationFile(Integer id) throws FileNotFoundException;
}
