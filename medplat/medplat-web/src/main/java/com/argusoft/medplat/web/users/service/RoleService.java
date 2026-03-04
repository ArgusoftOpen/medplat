
package com.argusoft.medplat.web.users.service;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *     Define methods for role
 * </p>
 * @author vaishali
 * @since 27/08/2020 4:30
 */
public interface RoleService {
     /**
      * Returns a list of all role
      * @param isActive A boolean value for status
      * @return a list of RoleMasterDto
      */
     List<RoleMasterDto> retrieveAll(Boolean isActive);

     /**
      * Returns a list role of given role ids
      * @param trainerRoleIds A set of role ids
      * @return A list of RoleMasterDto
      */
     List<RoleMasterDto> getRolesByIds(Set<Integer> trainerRoleIds);

     /**
      * Create or update given role
      * @param roleDto An instance of RoleMasterDto
      */
     void createOrUpdate(RoleMasterDto roleDto) ;

     /**
      * Returns role of given id
      * @param id An id of role
      * @return An instance of RoleMasterDto
      */
     RoleMasterDto retrieveById(Integer id);

     /**
      * Updates status of given role
      * @param roleDto An instance of RoleMasterDto
      * @param isActive A status value for role
      */
     void toggleActive(RoleMasterDto roleDto, Boolean isActive);
}
