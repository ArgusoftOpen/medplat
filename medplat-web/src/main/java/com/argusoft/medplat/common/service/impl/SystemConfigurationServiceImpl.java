
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements methods of SystemConfigurationService
 * @author harsh
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    @Autowired
    private SystemConfigurationDao systemConfigurationDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSystemConfiguration(SystemConfiguration systemConfiguration) {
        systemConfiguration.setIsActive(true);
        systemConfigurationDao.createOrUpdate(systemConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(value = "systemCache", key = "#systemConfiguration.systemKey")
    public void updateSystemConfiguration(SystemConfiguration systemConfiguration) {
        systemConfigurationDao.update(systemConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "systemCache", key = "#key")
    public SystemConfiguration retrieveSystemConfigurationByKey(String key) {
        return systemConfigurationDao.retrieveSystemConfigurationByKey(key);
    }

    /**
     * Returns system configuration of given key
     * @param key A key of system configuration
     * @return A instance of SystemConfiguration
     */
    public SystemConfiguration retrieveSysConfigurationByKey(String key) {
        return systemConfigurationDao.retrieveSystemConfigurationByKey(key);
    }

}
