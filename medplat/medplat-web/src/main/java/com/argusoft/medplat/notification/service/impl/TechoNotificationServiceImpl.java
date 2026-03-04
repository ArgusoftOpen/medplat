/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service.impl;

import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import com.argusoft.medplat.notification.service.TechoNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 *
 * <p>
 *     Define services for techo notification.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class TechoNotificationServiceImpl implements TechoNotificationService {

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void markNotificationAsRead(List<Integer> notificationIds, Integer userId) {
        if (notificationIds != null && !notificationIds.isEmpty()) {
            List<TechoNotificationMaster> retrieveByIds = techoNotificationMasterDao.retriveByIds("id", notificationIds);
            for (TechoNotificationMaster notification : retrieveByIds) {
                notification.setState(TechoNotificationMaster.State.COMPLETED);
                notification.setActionBy(Objects.requireNonNullElse(userId, -1));
            }
            techoNotificationMasterDao.updateAll(retrieveByIds);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(TechoNotificationMaster techoNotificationMaster) {
        techoNotificationMasterDao.update(techoNotificationMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TechoNotificationMaster retrieveById(Integer id) {
        return techoNotificationMasterDao.retrieveById(id);
    }

}
