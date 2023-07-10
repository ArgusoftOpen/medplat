package com.argusoft.medplat.fcm.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationMaster;

import java.util.Date;
import java.util.List;

/**
 * @author nihar
 * @since 02/08/22 2:30 PM
 */
public interface TechoPushNotificationDao extends GenericDao<TechoPushNotificationMaster, Long> {

   List<TechoPushNotificationDto> getPushNotificationsByType(String type);

   List<TechoPushNotificationDto> getPushNotifications();

   List<TechoPushNotificationDto> getPushNotification(Date toTime, int limit);
   void markAsProcessed(Long id);
   void markAsSentAndSetResponse(Long id, String response, String exception, boolean b);
}
