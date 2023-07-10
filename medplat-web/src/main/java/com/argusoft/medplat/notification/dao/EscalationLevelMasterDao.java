/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.notification.model.EscalationLevelMaster;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Define methods for escalation level.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public interface EscalationLevelMasterDao extends GenericDao<EscalationLevelMaster, Integer> {

    /**
     * Retrieves escalation level details by notification id.
     * @param id Notification id.
     * @return Returns list of escalation level details.
     */
    List<EscalationLevelMaster> retrieveByNotificationId(Integer id);

    /**
     * Retrieves escalation level details by UUID.
     * @param uuid UUID.
     * @return Returns escalation level details.
     */
    EscalationLevelMaster retrieveByUUID(UUID uuid);
}
