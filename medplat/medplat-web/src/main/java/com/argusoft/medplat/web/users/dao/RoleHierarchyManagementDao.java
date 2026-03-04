package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.model.RoleHierarchyManagement;

import java.util.List;

/**
 * <p>Defines database method for role hierarchy management</p>
 * @author vaishali
 * @since 31/08/2020 10:30
 */
public interface RoleHierarchyManagementDao  extends GenericDao<RoleHierarchyManagement, Integer>{
    /**
     * Returns a list of role hierarchy of given role id
     * @param roleId An id of role
     * @return A list of RoleHierarchyManagement
     */
    List<RoleHierarchyManagement> retrieveLocationByRoleId(Integer roleId);

    /**
     * Returns a list of active role hierarchy of given role id
     * @param roleId An id of role
     * @return A list of RoleHierarchyManagement
     */
    List<RoleHierarchyManagement> retrieveActiveLocationByRoleId(Integer roleId);
}
