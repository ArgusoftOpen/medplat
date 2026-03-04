
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.model.SystemConfiguration;

/**
 * <p>
 *     Define methods for system configuration
 * </p>
 * @author harsh
 * @since 27/08/2020 4:30
 */
public interface SystemConfigurationService {
    /**
     * Creates system configuration
     * @param systemConfiguration An instance of SystemConfiguration
     */
    void createSystemConfiguration(SystemConfiguration systemConfiguration);

    /**
     * updateSystemConfiguration method updates an Object of Class SystemConfiguration, by calling updateEntity method of its parent.
     * @param systemConfiguration Takes an Object of Class SystemConfiguration, which is to be updated.
     */
    void updateSystemConfiguration(SystemConfiguration systemConfiguration);

    /**
     * retrieveSystemConfigurationByKey method retrieves the SystemConfiguration Object, by calling getEntityById method of its parent.
     * @param key Takes PK of the SystemConfiguration
     * @return Returns the Object of SystemConfiguration.
     */
    SystemConfiguration retrieveSystemConfigurationByKey(String key);

    /**
     * Returns system configuration of given key
     * @param key A key of system configuration
     * @return An instance of SystemConfiguration
     */
    SystemConfiguration retrieveSysConfigurationByKey(String key);

}
