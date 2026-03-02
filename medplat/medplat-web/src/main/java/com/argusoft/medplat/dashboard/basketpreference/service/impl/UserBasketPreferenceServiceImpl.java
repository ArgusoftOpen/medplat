/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.service.impl;

import com.argusoft.medplat.dashboard.basketpreference.dao.UserBasketPreferenceDao;
import com.argusoft.medplat.dashboard.basketpreference.dto.UserBasketPreferenceDto;
import com.argusoft.medplat.dashboard.basketpreference.mapper.UserBasketPreferenceMapper;
import com.argusoft.medplat.dashboard.basketpreference.model.UserBasketPreference;
import com.argusoft.medplat.dashboard.basketpreference.service.UserBasketPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Method implementation for user Basket Preference</p>
 * @author kunjan
 * @since 26/08/20 11:30 AM
 *
 */
@Service
@Transactional
public class UserBasketPreferenceServiceImpl implements UserBasketPreferenceService {

    @Autowired
    UserBasketPreferenceDao userBasketPreferenceDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(UserBasketPreferenceDto userBasketPreferenceDto) {
        UserBasketPreference userBasketPreference = userBasketPreferenceDao.retrievePreferenceByUserId(userBasketPreferenceDto.getUserId());
        if (userBasketPreference == null) {
            UserBasketPreference userBasketPreferenceNew = UserBasketPreferenceMapper.convertUserBasketPreferenceDtoToUserBasketPreference(userBasketPreferenceDto);
            userBasketPreferenceDao.createOrUpdate(userBasketPreferenceNew);
        } else {
            userBasketPreference.setPreference(userBasketPreferenceDto.getPreference());
            userBasketPreferenceDao.createOrUpdate(userBasketPreference);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrievePreferenceByUserId(Integer userId) {
        UserBasketPreference userBasketPreference = userBasketPreferenceDao.retrievePreferenceByUserId(userId);
        if (userBasketPreference != null) {
            return userBasketPreference.getPreference();
        } else {
            return null;
        }

    }

}
