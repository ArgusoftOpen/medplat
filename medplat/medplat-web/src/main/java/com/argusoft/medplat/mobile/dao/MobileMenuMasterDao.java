package com.argusoft.medplat.mobile.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.MenuDataBean;
import com.argusoft.medplat.mobile.model.SohElementModuleMaster;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Defines methods for MobileMenuMasterDao
 * </p>
 *
 * @author rahul
 * @since 13/01/21 5:40 PM
 */
public interface MobileMenuMasterDao extends GenericDao<SohElementModuleMaster, Integer> {

    /**
     * Retrieves all mobile menu for user by it's role
     * @param lastUpdatedOn A last updated time for menuBean in mobile
     * @param roleId A roleId of user
     * @return fetch menu by user role
     */
    public List<MenuDataBean> retrieveAllMenusForMobile(Date lastUpdatedOn, int roleId);
}
