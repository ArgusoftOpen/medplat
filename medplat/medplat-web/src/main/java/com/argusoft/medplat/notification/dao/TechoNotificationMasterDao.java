/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.TechoNotificationDataBean;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for techo notification master.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
public interface TechoNotificationMasterDao extends GenericDao<TechoNotificationMaster, Integer> {

    /**
     * Retrieves deleted notifications for user by last modified on date.
     * @param userId User id.
     * @param lastModifiedOn Last modified on date.
     * @return Returns list of deleted notifications's id.
     */
    List<Integer> getDeletedNotificationsForUserByLastModifiedOn(Integer userId, Date lastModifiedOn);

    /**
     * Retrieves all notifications for user.
     * @param userId User id.
     * @param roleId Role id.
     * @return Returns list of techo notifications.
     */
    List<TechoNotificationDataBean> retrieveAllNotificationsForUser(Integer userId, Integer roleId);

    /**
     * Retrieves notifications for user by last modified on date.
     * @param userId User id.
     * @param roleId Role id.
     * @param lastModifiedOn Last modified on date.
     * @return Returns list of notifications's id.
     */
    List<TechoNotificationDataBean> retrieveNotificationsForUserByLastModifiedOn(Integer userId, Integer roleId, Date lastModifiedOn);

    /**
     * Mark notification as completed.
     * @param notificationId Notification id.
     * @param userId User id.
     */
    void markNotificationAsCompleted(Integer notificationId, Integer userId);

    /**
     * Retrieves notification for migration.
     * @param memberId Member id.
     * @param migrationId Migration id.
     * @param notificationTypeId Notification type id.
     * @param state Define state of notification like COMPLETED, PENDING.
     * @return Returns list of notifications.
     */
    List<TechoNotificationMaster> retrieveNotificationForMigration(Integer memberId, Integer migrationId, Integer notificationTypeId, Enum state);

    List<TechoNotificationMaster> retrieveNotificationByTypeAndMemberIdAndState(Integer memberId, List<Integer> notificationTypeIds, TechoNotificationMaster.State state);

    void markOlderNotificationAsMissed(Integer memberId, Integer notificationTypeId);

    String getNotificationTypeCodeFromTypeId(Integer notificationTypeId);

    void markOlderNotificationStateAsAdmitted(Integer memberId, Integer userId);

    void markNotificationStateAsDischarged(Integer memberId, Integer userId);

    void markNotificationAsCompletedByType(Integer memberId, Integer notificationTypeId);

}
