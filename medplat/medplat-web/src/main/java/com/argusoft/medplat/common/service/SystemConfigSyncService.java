
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.dto.SystemConfigSyncDto;

import java.util.List;

/**
 * <p>
 *     Define methods for system sync configuration
 * </p>
 * @author Hiren Morzariya
 * @since 27/08/2020 4:30
 */
public interface SystemConfigSyncService {
     /**
      * Creates of update system sync configuration of given feature type
      * @param obj An instance of object
      * @param featureType A value of feature type
      * @return An id or created or updated row
      */
     Integer createOrUpdate(Object obj,String featureType);

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
     List<SystemConfigSyncDto> retriveSystemConfigBasedOnAccess(Integer id, String serverName);

     /**
      * Deletes all sync configuration
      * @return A string response
      */
     String clearAndResetSync();
    
}
