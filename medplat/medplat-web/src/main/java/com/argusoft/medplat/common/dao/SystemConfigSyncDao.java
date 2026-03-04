package com.argusoft.medplat.common.dao;


import com.argusoft.medplat.common.model.SystemConfigSync;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for system configuration sync</p>
 * @author ashish
 * @since 31/08/2020 10:30
 */
public interface SystemConfigSyncDao extends GenericDao<SystemConfigSync, Integer> {
    /**
     * Creates of update system sync configuration of given feature type
     * @param systemConfigSync An instance of SystemConfigSync
     */
    void createOrUpdate(SystemConfigSync systemConfigSync);

    /**
     * Checks password of given server
     * @param serverName A name of the server
     * @param password A value of password
     * @return Whether given password is correct or not
     */
    boolean checkPassword(String serverName,String password);

    /**
     * Returns a list of system configuration based on given server name and access id
     * @param id An access id
     * @param serverName A name of server
     * @return A list of SystemConfigSyncDto
     */
    List<SystemConfigSync> retrieveSystemConfigBasedOnAccess(Integer id, String serverName);

    /**
     * Deletes all system sync configuration
     * @return Number of deleted row
     */
    int  deleteAllRow();

}
