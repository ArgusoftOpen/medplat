
package com.argusoft.medplat.web.users.service;

import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserToken;

/**
 * <p>
 *     Define methods for user token
 * </p>
 * @author prateek
 * @since 27/08/2020 4:30
 */
public interface UserTokenService {

     /**
      * Returns a user token
      * @param token A token value
      * @return An instance of UserToken
      */
     UserToken getUserTokenByUserToken(String token);

     /**
      * Returns a user token
      * @param token A token value
      * @param allTokens Whether to return all tokens or not
      * @return An instance of UserToken
      */
     UserToken getUserTokenByUserToken(String token, boolean allTokens);

     /**
      * Creates user token
      * @param userToken An instance of UserToken
      * @return An id of created row
      */
     Integer createUserToken(UserToken userToken);

     /**
      * Returns a user token of given user id
      * @param userId An id of user
      * @return An instance of UserToken
      */
     UserToken getUserTokenByUserId(Integer userId);

     /**
      * Deactivates all user token of given user id
      * @param userId An id user
      */
     void deactivateAllActiveUserTokens(Integer userId);

     UserTokenDto retrieveDtoByUserToken(String token);
}
