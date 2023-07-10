/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service;

import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;

import java.util.List;

/**
 *
 * <p>
 *     Define services for notification master.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public interface NotificationMasterService {

    /**
     * Add/Update notification.
     * @param notificationMasterDto Notification details.
     * @param isMetodCallBySyncFunction Is method called by sync function.
     */
    void createOrUpdate(NotificationTypeMasterDto notificationMasterDto,boolean isMetodCallBySyncFunction);

    /**
     * Retrieves notification by id.
     * @param id Notification id.
     * @return Returns notification details by id.
     */
    NotificationTypeMasterDto retrieveById(Integer id);

    /**
     * Retrieves all notifications.
     * @param isActive Is notification active or not.
     * @return Returns list of notifications.
     */
    List<NotificationTypeMasterDto> retrieveAll(Boolean isActive);

    /**
     * Make notification active or inactive.
     * @param id Notification id.
     * @param isActive Is notification active or not.
     */
    void toggleActive(Integer id, Boolean isActive);

    /**
     * Retrieves notifications by code.
     * @param code Notification code.
     * @return Returns notification details by code.
     */
    NotificationTypeMaster retrieveByCode(String code);

}
