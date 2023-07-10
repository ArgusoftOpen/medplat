/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Define methods for notification type.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface NotificationTypeMasterDao extends GenericDao<NotificationTypeMaster, Integer> {

    /**
     * Retrieves all notifications.
     * @param isActive Is notification active or not.
     * @return Returns list of notifications.
     */
    List<NotificationTypeMaster> retrieveAll(Boolean isActive);

    /**
     * Retrieves notifications by code.
     * @param code Notification code.
     * @return Returns notification details by code.
     */
    NotificationTypeMaster retrieveByCode(String code);

    /**
     * Retrieves notification by UUID.
     * @param uuid UUID.
     * @return Returns notification details by UUID.
     */
    NotificationTypeMaster retrieveByUUID(UUID uuid);
}
