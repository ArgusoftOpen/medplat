package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.common.dto.AssignRoleWithFeatureDto;
import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.model.RoleMaster;

import java.util.List;
import java.util.Set;

/**
 * <p>Defines database method for role</p>
 * @author vaishali
 * @since 31/08/2020 10:30
 */
public interface RoleDao extends GenericDao<RoleMaster, Integer> {
     /**
      * Returns a list of all role
      * @param isActive A boolean value for status
      * @return a list of RoleMaster
      */
     List<RoleMaster> retrieveAll(Boolean isActive);

     /**
      * Create or update given role
      * @param role An instance of RoleMaster
      */
     void createOrUpdate(RoleMaster role);

     /**
      * Returns role of given id
      * @param roleId An id of role
      * @return An instance of RoleMaster
      */
     RoleMaster retrieveById(Integer roleId);

     /**
      * Returns a list role of given role ids
      * @param trainerRoleIds A set of role ids
      * @return A list of RoleMaster
      */
     List<RoleMaster> getRolesByIds(Set<Integer> trainerRoleIds);

     /**
      * Returns a string of assigned feature of given role id
      * @param id An id of role
      * @return A string of assigned feature
      */
     String getAssignedFeaturesByRoleId(Integer id);

     /**
     * Retrieves assigned feature list by role id
     * @param id Id of role
     * @return Returns list of assigned features
     */
    List<AssignRoleWithFeatureDto> getAssignedFeatureList(Integer id);

     /**
      * Returns an instance of role of given code
      * @param code A code of role
      * @return An instance of RoleMaster
      */
    RoleMaster retrieveByCode(String code);
}
