
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.model.SystemBuildHistory;

/**
 * <p>
 *     Define methods for system build history
 * </p>
 * @author smeet
 * @since 27/08/2020 4:30
 */
public interface SystemBuildHistoryService {
     /**
      * Returns a instance of last build history
      * @return An instance of SystemBuildHistory
      */
     SystemBuildHistory retrieveLastBuildHistory();

     /**
      * Creates system build history
      * @param systemBuildHistory An instance of SystemBuildHistory
      */
     void create(SystemBuildHistory systemBuildHistory);

}
