package com.argusoft.medplat.fcm.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fcm.model.TechoPushNotificationType;

/**
 * @author nihar
 * @since 02/08/22 3:46 PM
 */
public interface TechoPushNotificationTypeDao extends GenericDao<TechoPushNotificationType, Integer> {

    TechoPushNotificationType getTechoPushNotificationTypeByType(String type);

    boolean checkFileExists(Integer id);
}
