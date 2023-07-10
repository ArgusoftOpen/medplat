/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fcm.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fcm.model.TechoPushNotificationRoleUserDetail;

import java.util.List;

/**
 * @author nihar
 */
public interface TechoPushNotificationRoleUserDetailDao extends
        GenericDao<TechoPushNotificationRoleUserDetail, Integer> {

    List<TechoPushNotificationRoleUserDetail> findByNotificationConfigId(Integer id);

    void deleteByConfigId(Integer id);
}
