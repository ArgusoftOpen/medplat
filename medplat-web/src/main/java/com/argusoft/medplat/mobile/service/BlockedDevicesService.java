package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.model.BlockedDevicesMaster;

/**
 *
 * @author prateek on 12 Feb, 2019
 */
public interface BlockedDevicesService {

    Boolean checkIfDeviceIsBlocked(String imei);

    BlockedDevicesMaster checkIfDeviceIsBlockedOrDeleteDatabase(String imei);
    
    void removeEntryForDeviceOfIMEI(String imei);
}
