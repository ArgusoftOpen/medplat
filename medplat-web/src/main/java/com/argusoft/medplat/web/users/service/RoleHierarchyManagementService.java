
package com.argusoft.medplat.web.users.service;

import java.util.Map;

/**
 * <p>
 *     Define methods for role hierarchy management
 * </p>
 * @author vaishali
 * @since 27/08/2020 4:30
 */
public interface RoleHierarchyManagementService {
     /**
      * Returns a map of location hierarchy and role of given role id
      * @param roleId An id of role
      * @return map of location hierarchy and role object
      */
     Map<String, Object> retrieveLocationHierarchyByRoleId(Integer roleId);
}
