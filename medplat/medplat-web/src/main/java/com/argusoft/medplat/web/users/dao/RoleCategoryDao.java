package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.model.RoleCategoryMaster;

import java.util.List;

/**
 * <p>Defines database method for role category</p>
 * @author vaishali
 * @since 31/08/2020 10:30
 */
public interface RoleCategoryDao  extends GenericDao<RoleCategoryMaster, Integer>{
    /**
     * Returns a list of role category of given id of role
     * @param roleId An id of role
     * @return A list of RoleCategoryMaster
     */
    List<RoleCategoryMaster> retrieveByRoleId(Integer roleId);
    
}
