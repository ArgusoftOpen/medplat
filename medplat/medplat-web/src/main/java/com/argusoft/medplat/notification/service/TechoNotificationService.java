/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service;

import com.argusoft.medplat.notification.model.TechoNotificationMaster;

import java.util.List;

/**
 *
 * <p>
 *     Define services for techo notification.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public interface TechoNotificationService {

    /**
     * Mark list of notifications as read.
     * @param notificationIds List of notifications ids.
     * @param userId User id.
     */
    void markNotificationAsRead(List<Integer> notificationIds, Integer userId);

    /**
     * Update techo notification master.
     * @param techoNotificationMaster Techo notification master details.
     */
    void update(TechoNotificationMaster techoNotificationMaster);

    /**
     * Retrieves notification master details by id.
     * @param id Id of notification.
     * @return Returns notification details by id.
     */
    TechoNotificationMaster retrieveById(Integer id);
}
