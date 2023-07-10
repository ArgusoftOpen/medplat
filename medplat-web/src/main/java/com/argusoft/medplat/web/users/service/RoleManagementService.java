
package com.argusoft.medplat.web.users.service;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;

import java.util.List;

/**
 * <p>
 *     Define methods for role management
 * </p>
 * @author vaishali
 * @since 27/08/2020 4:30
 */
public interface RoleManagementService {
     /**
      * Returns a list of RoleMasterDto managed by given role id
      * @param roleId An id of role
      * @return A list of RoleMasterDto
      */
     List<RoleMasterDto> retrieveRolesManagedByRoleId(Integer roleId);
    
}
