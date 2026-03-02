package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.model.UserLoginDetailMaster;

/**
 * <p>
 *     Defines database method for user login detail
 * </p>
 * @author shrey
 * @since 31/08/2020 10:30
 */
public interface UserLoginDetailMasterDao extends GenericDao<UserLoginDetailMaster, Integer> {

    UserLoginDetailMaster getLastLoginDetailByUserId(Integer userId);

}

