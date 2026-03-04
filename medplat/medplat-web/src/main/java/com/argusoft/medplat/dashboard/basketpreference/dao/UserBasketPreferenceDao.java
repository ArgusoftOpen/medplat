/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.dao;

import com.argusoft.medplat.dashboard.basketpreference.model.UserBasketPreference;
import com.argusoft.medplat.database.common.GenericDao;

/**
 * <p>Database method signature for user basket preference</p>
 * @author kunjan
 * @since 26/08/20 11:30 AM
 *
 */
public interface UserBasketPreferenceDao extends GenericDao<UserBasketPreference, Integer> {

    /**
     * Returns UserBasketPreference based on given userId
     * @param userId user id
     * @return UserBasketPreference
     */
    UserBasketPreference retrievePreferenceByUserId(Integer userId);

}
