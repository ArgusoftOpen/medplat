
package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.web.users.dao.UserTokenDao;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserToken;
import com.argusoft.medplat.web.users.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements methods of UserTokenService
 * @author prateek
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    UserTokenDao userTokenDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken getUserTokenByUserToken(String token) {
        return userTokenDao.retrieveByUserToken(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken getUserTokenByUserToken(String token, boolean allTokens) {
        return userTokenDao.retrieveByUserToken(token, allTokens);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createUserToken(UserToken userToken) {
        return userTokenDao.create(userToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken getUserTokenByUserId(Integer userId) {
        return userTokenDao.retrieveByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivateAllActiveUserTokens(Integer userId) {
        userTokenDao.deactivateAllActiveUserTokens(userId);
    }

    @Override
    public UserTokenDto retrieveDtoByUserToken(String token) {
        return userTokenDao.retrieveDtoByUserToken(token);
    }

}
