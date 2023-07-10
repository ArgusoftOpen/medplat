
package com.argusoft.medplat.web.users.service;

import com.argusoft.medplat.exception.ImtechoResponseEntity;
import com.argusoft.medplat.mobile.dto.LoggedInUserPrincipleDto;
import com.argusoft.medplat.mobile.dto.MobUserInfoDataBean;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Define methods for user
 * </p>
 *
 * @author vaishali
 * @since 27/08/2020 4:30
 */
public interface UserService {

    /**
     * Create or update user
     *
     * @param user An instance of UserMasterDto
     * @return A map of created user id
     */
    Map<String, Integer> createOrUpdate(UserMasterDto user);

    /**
     * Returns a user of given user name
     *
     * @param userName A username of user
     * @return An  instance of UserMasterDto
     */
    UserMasterDto retrieveByUserName(String userName);

    /**
     * Returns a user of given user name
     *
     * @param userName A username of user
     * @return An instance of UserMaster
     */
    UserMaster retriveUserByUserName(String userName);

    /**
     * Returns a user of given login code
     *
     * @param loginCode A login code
     * @return An instance of UserMasterDto
     */
    UserMasterDto retrieveByLoginCode(String loginCode);

    /**
     * Checks username is available or not
     *
     * @param userName A username of user
     * @param userId   An id of user
     * @return A boolean value for username is available or not
     */
    Boolean validateUserName(String userName, Integer userId);

    /**
     * Validates given mobile number is already registered or not
     *
     * @param mobileNumber A mobile number
     * @return Whether a mobile number is already registered or not
     */
    Boolean validatePhoneNumberForDrTecho(String mobileNumber);

    /**
     * Returns user of given id
     *
     * @param id An id of user
     * @return An instance of UserMasterDto
     */
    UserMasterDto retrieveById(Integer id);

    /**
     * Returns all users.
     *
     * @param isActive A boolean value for state of user
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> retrieveAll(Boolean isActive);

    /**
     * Updates state of given user
     *
     * @param user     An instance of UserMasterDto
     * @param isActive A boolean value for state of user
     */
    void toggleActive(UserMasterDto user, Boolean isActive);

    /**
     * Returns users based on criteria
     *
     * @param userId       An id of user
     * @param roleId       An id of role
     * @param locationId   An id of location
     * @param searchString A string value for search
     * @param orderBy      A string value for order by field
     * @param order        A value of order it can be asc or desc
     * @param limit        A value for limit
     * @param offset       A value for offset
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> retrieveByCriteria(Integer userId, Integer roleId, Integer locationId, String searchString, String orderBy, Integer limit, Integer offset, String order,String status);

    /**
     * Returns users of given ids
     *
     * @param userId A set of ids of users
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> getUsersByIds(Set<Integer> userId);

    /**
     * Returns a list of user based on given locations and roles
     *
     * @param locationIds A list of location id
     * @param roleIds     A list of role id
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> getUsersByLocationsAndRoles(List<Integer> locationIds, List<Integer> roleIds);

    /**
     * Returns a list of user based on given location, role and course
     *
     * @param locationId An id of location
     * @param roleId     An id of role
     * @param courseId   An id of course
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> getUsersByLocationAndRoleAndCourse(Integer locationId, Integer roleId, Integer courseId);

    /**
     * Verifies password of user
     *
     * @param oldPassword An old password
     * @param userId      An id or user
     * @return boolean value regarding password is valid or not
     */
    Boolean verifyPassword(String oldPassword, Integer userId);

    /**
     * Changes old password of user with new password
     *
     * @param newPassword A value of new password
     * @param userId      An id or user
     */
    void changePassword(String newPassword, Integer userId);

    /**
     * Changes old password of user with new password
     *
     * @param oldPassword An old password
     * @param newPassword A value of new password
     */
    void changePasswordOldtoNew(String oldPassword, String newPassword);

    /**
     * Generates otp in case of forgot password
     *
     * @param username A user name
     */
    void validateAndGenerateOtp(String username);

    /**
     * Verifies otp
     *
     * @param username     A user name
     * @param otp          A value of otp
     * @param noOfAttempts A noOfAttempts of wrong otp. If it is greater than 3 then mark user as inactive
     */
    void verifyOtp(String username, String otp, Integer noOfAttempts);

    /**
     * Resets password of user
     *
     * @param username A user name
     * @param otp      A value of otp
     * @param password A value of password
     */
    void resetPassword(String username, String otp, String password);

    /**
     * Checks if user exists with same aadhar number or phone number
     *
     * @param aadharNumber  An aadhar number to be check
     * @param contactNumber A phone number to be check
     * @param userId        An id of user
     * @param roleId        An id of role
     * @return A boolean value regarding user with same aadhar number or phone number exists or not
     */
    Boolean checkIfUserExistsWithSameAadharOrPhone(String aadharNumber, String contactNumber, Integer userId, Integer roleId);

    /**
     * Validates area of interventions for given userId
     *
     * @param userId      An id of user
     * @param roleId      An id of role
     * @param locationIds Ids of location
     * @param toBeAdded   Ids of locations to be added
     * @return ImtechoResponseEntity with data object and string message
     * regarding locations to be added is valid or not
     */
    ImtechoResponseEntity validateAOI(Integer userId, Integer roleId, List<Integer> locationIds, Integer toBeAdded);

    /**
     * Validates new mobile user
     *
     * @param username          A user name
     * @param password          A value of password
     * @param firstTimeLoggedIn Is first time logged in.
     * @return An instance of MobUserInfoDataBean
     */
    MobUserInfoDataBean validateMobileUserNew(String username, String password, String firebaseToken, boolean firstTimeLoggedIn);

    /**
     * Returns Full name and User name only of active users
     *
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> getAllActiveUsers();

    /**
     * Returns available username based on given username
     *
     * @param givenUserName A user name
     * @return An available user name
     */
    String fetchAvailableUsername(String givenUserName);

    /**
     * Returns full name of user
     *
     * @param attendeeId An id of user
     * @return A full name of user
     */
    String retrieveFullNameById(Integer attendeeId);

    /**
     * Updates number of login attempts of given user name
     *
     * @param username        A name of user
     * @param isPasswordValid Is password valid or not
     */
    void updateNoOfAttempts(String username, Boolean isPasswordValid);

    /**
     * Activates account of given user id
     *
     * @param id An id of user
     */
    void activateAccount(Integer id);

    /**
     * Checks given token is valid or not
     *
     * @param token A token to verify
     * @return A boolean value of given token is valid or not
     */
    boolean isUserTokenValid(String token);

    /**
     * Returns a instance of LoggedInUserPrincipleDto based on given map of token
     *
     * @param userPassMap A list of user name
     * @return An instance of LoggedInUserPrincipleDto
     */
    LoggedInUserPrincipleDto revalidateUserToken(String[] userPassMap);

    /**
     * Returns a map of user id and token based on given list of token
     *
     * @param userTokens A list of token
     * @return A map of user id and token
     */
    Map<String, String[]> getUserIdAndActiveTokenFromToken(List<String> userTokens);

    /**
     * Returns a user based on given token
     *
     * @param token A token value
     * @return An instance of UserMaster
     */
    UserMaster getUserByValidToken(String token);

    /**
     * Validates health infrastructure for user
     *
     * @param toBeRemoved    A id of health infra to be remove
     * @param healthInfraIds A list of health infrastructure Ids
     * @return ImtechoResponseEntity An ImtechoResponseEntity with data object and string message
     * regarding health infra to be remove is valid or not
     */
    ImtechoResponseEntity validateHealthInfra(Integer toBeRemoved, List<Integer> healthInfraIds);

    /**
     * Generates random login code for given userId
     *
     * @return A string of generated login code
     */
    String getRandomLoginCode();

    /**
     * Generates login code for given userId
     *
     * @param userId An id of user
     * @return A string of generated login code
     */
    String generateLoginCode(Integer userId);

    /**
     * Checks user is currently logged in or not
     *
     * @return Is user logged in or not
     */
    boolean isAnonymous();

    Integer getLoginAttempts();

    Integer getLoginTimeout();

    void updateLanguagePreference(String userToken, String languageCode);

    void addFirebaseToken(Integer id, String firebaseToken);

}
