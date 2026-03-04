package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserToken;

/**
 * <p>Defines database method for user token</p>
 * @author prateek
 *
 * @since 31/08/2020 10:30
 */
public interface UserTokenDao extends GenericDao<UserToken, Integer> {

    /**
     * Returns a user token
     * @param token A token value
     * @return An instance of UserToken
     */
    UserToken retrieveByUserToken(String token);

    /**
     * Returns a user token of given user id
     * @param userId An id of user
     * @return An instance of UserToken
     */
    UserToken retrieveByUserId(Integer userId);

    /**
     * Returns a user token
     * @param token A token value
     * @param allTokens Whether to return all tokens or not
     * @return An instance of UserToken
     */
    UserToken retrieveByUserToken(String token, boolean allTokens);

    /**
     * Returns a user token dto
     * @param token A token value
     * @return An instance of UserTokenDto
     */
    UserTokenDto retrieveDtoByUserToken(String token);

    /**
     * Deactivates all user token of given user id
     * @param userId An id user
     */
    void deactivateAllActiveUserTokens(Integer userId);
    
}
