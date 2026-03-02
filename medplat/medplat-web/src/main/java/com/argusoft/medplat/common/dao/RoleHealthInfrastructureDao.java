package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.RoleHealthInfrastructure;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for role health infrastructure</p>
 * @author vaishali
 * @since 31/08/2020 10:30
 */

public interface RoleHealthInfrastructureDao extends GenericDao<RoleHealthInfrastructure, Integer>{

    /**
     * Returns a list of role health infrastructure of given id of role
     * @param roleId An id of role
     * @return A list of RoleHealthInfrastructure
     */
    List<RoleHealthInfrastructure> retrieveByRoleId(Integer roleId);
}
