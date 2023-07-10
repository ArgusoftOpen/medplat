package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.SystemBuildHistory;
import com.argusoft.medplat.database.common.GenericDao;

/**
 * <p>Defines database method for system build history</p>
 * @author smeet
 * @since 31/08/2020 10:30
 */
public interface SystemBuildHistoryDao extends GenericDao<SystemBuildHistory, Integer> {
    /**
     * Returns a instance of last build history
     * @return An instance of SystemBuildHistory
     */
    SystemBuildHistory retrieveLastSystemBuild();

}
