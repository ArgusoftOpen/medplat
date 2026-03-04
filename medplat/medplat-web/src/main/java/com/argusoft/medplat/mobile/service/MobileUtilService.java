/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.dto.UncaughtExceptionBean;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.model.UserInputDuration;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author prateek
 */
public interface MobileUtilService {

    /**
     * createSyncStatus method creates an Object of Class SyncStatus, by calling
     * saveEntity method of its parent.
     *
     * @param syncStatus Takes an Object of Class SyncStatus, which is to be
     * created.
     */
    void createSyncStatus(SyncStatus syncStatus);

    /**
     * updateSyncStatus method updates an Object of Class SyncStatus, by calling
     * updateEntity method of its parent.
     *
     * @param syncStatus Takes an Object of Class SyncStatus, which is to be
     * updated.
     */
    void updateSyncStatus(SyncStatus syncStatus);

    /**
     * retrieveSyncStatusPK method retrieves an Object of Class SyncStatus, by
     * calling retrieveSyncStatusPK method of its parent.
     *
     * @param id Takes PK of the SyncStatus.
     * @return Returns the Object of SyncStatus.
     */
    SyncStatus retrieveSyncStatusById(String id);

    /**
     * createUserInputDuration method creates an Object of Class SyncStatus, by
     * calling saveEntity method of its parent.
     *
     * @param userInputDuration Takes an Object of Class SyncStatus, which is to
     * be created.
     * @return
     */
    Integer createUserInputDuration(UserInputDuration userInputDuration);

    /**
     * updateUserInputDuration method updates an Object of Class SyncStatus, by
     * calling updateEntity method of its parent.
     *
     * @param userInputDuration Takes an Object of Class SyncStatus, which is to
     * be updated.
     */
    void updateUserInputDuration(UserInputDuration userInputDuration);

    /**
     * retrieveUserInputDuration method retrieves an Object of Class SyncStatus,
     * by calling retrieveUserInputDurationById method of its parent.
     *
     * @param id Takes PK of the UserInputDuration.
     * @return Returns the Object of UserInputDuration.
     */
    UserInputDuration retrieveUserInputDurationById(Integer id);

    /**
     *
     * @param allExceptionDetails
     * @param uid
     * @return
     */
    String storeUncaughtExceptionDetails(List<UncaughtExceptionBean> allExceptionDetails, Integer uid);

    void updateProcessingStringsToPendingOnServerStartup();

    List<SyncStatus> retrieveSyncStatusBetweenActionDates(Date from, Date to);

    List<SyncStatus> retrieveSyncStatusByCriteria(Date from, Date to, String recordString, Integer userId, String status);

    LinkedHashMap<String,Object> getTechoPlusUserCount();

    List<String> retrieveFeaturesAssignedToTheAoi(Integer userId);
}
