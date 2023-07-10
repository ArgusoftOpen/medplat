package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for system configuration</p>
 * @author harsh
 * @since 31/08/2020 10:30
 */
public interface SystemConfigurationDao extends GenericDao<SystemConfiguration, Integer> {

    /**
     * Returns system configuration of given key
     * @param key A key of system configuration
     * @return An instance of SystemConfiguration
     */
    SystemConfiguration retrieveSystemConfigurationByKey(String key);

    /**
     * Returns a list of system configuration of given key search
     * @param startsWithString A key search of system configuration
     * @return An instance of SystemConfiguration
     */
    List<SystemConfiguration> retrieveSystemConfigurationsBasedOnLikeKeySearch(String startsWithString);
    
}
