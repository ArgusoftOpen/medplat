package com.argusoft.medplat.mobile.service.impl;

import com.argusoft.medplat.mobile.dao.BlockedDevicesDao;
import com.argusoft.medplat.mobile.model.BlockedDevicesMaster;
import com.argusoft.medplat.mobile.service.BlockedDevicesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author prateek on 12 Feb, 2019
 */
@Service
@Transactional
public class BlockedDevicesServiceImpl implements BlockedDevicesService {

    @Autowired
    BlockedDevicesDao blockedDevicesDao;

    @Override
    public Boolean checkIfDeviceIsBlocked(String imei) {
        BlockedDevicesMaster bdm = null;
        if (StringUtils.isNotBlank(imei)) {
            bdm = blockedDevicesDao.retrieveById(imei);
        }
        return bdm != null;
    }

    @Override
    public BlockedDevicesMaster checkIfDeviceIsBlockedOrDeleteDatabase(String imei) {
        BlockedDevicesMaster bdm = null;
        if (StringUtils.isNotBlank(imei)) {
            bdm = blockedDevicesDao.retrieveById(imei);
        }
        if (bdm == null)
            return new BlockedDevicesMaster();
        return bdm;
    }

    @Override
    public void removeEntryForDeviceOfIMEI(String imei) {
        if (StringUtils.isNotBlank(imei)) {
            blockedDevicesDao.deleteById(imei);
        }
    }
}
