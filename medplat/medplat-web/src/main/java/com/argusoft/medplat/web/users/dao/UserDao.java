package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.OptionTagDto;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.List;
import java.util.Set;

/**
 * <p>Defines database method for user</p>
 *
 * @author vaishali
 * @since 31/08/2020 10:30
 */
public interface UserDao extends GenericDao<UserMaster, Integer> {
    /**
     * Create or update user
     *
     * @param user An instance of UserMaster
     */
    void createOrUpdate(UserMaster user);

    /**
     * Returns a user of given user name
     *
     * @param userName A username of user
     * @return An  instance of UserMaster
     */
    UserMaster retrieveByUserName(String userName);


    /**
     * Returns a user of given user name
     *
     * @param userName A username of user
     * @return An instance of UserMaster
     */
    UserMaster retrieveUserByUserNAme(String userName);

    /**
     * Returns a user of given login code
     *
     * @param code A login code
     * @return An instance of UserMaster
     */
    UserMaster retrieveByLoginCode(String code);

    /**
     * Returns user of given id
     *
     * @param id An id of user
     * @return An instance of UserMaster
     */
    UserMaster retrieveById(Integer id);

    /**
     * Returns all users.
     *
     * @param isActive A boolean value for state of user
     * @return A list of UserMaster
     */
    List<UserMaster> retrieveAll(Boolean isActive);

    /**
     * Returns users of given ids
     *
     * @param userId A set of ids of users
     * @return A list of UserMaster
     */
    List<UserMaster> getUsersByIds(Set<Integer> userId);

    /**
     * Returns a list of user based on given locations and roles
     *
     * @param locationIds A list of location id
     * @param roleIds     A list of role id
     * @return A list of UserMaster
     */
    List<UserMaster> getUsersByLocationsAndRoles(List<Integer> locationIds, List<Integer> roleIds);

    /**
     * Returns a list of user based on given parent locations and roles
     *
     * @param locationIds A list of location id
     * @param roleIds     A list of role id
     * @return A list of UserMaster
     */
    List<UserMaster> getUsersByLocationsAndRolesUsingParentLocation(List<Integer> locationIds, List<Integer> roleIds);

    /**
     * Returns users based on criteria
     *
     * @param userId       An id of user
     * @param roles        A list of id of role
     * @param locationId   An id of location
     * @param searchString A string value for search
     * @param orderBy      A string value for order by field
     * @param order        A value of order it can be asc or desc
     * @param limit        A value for limit
     * @param offset       A value for offset
     * @return A list of UserMasterDto
     */
    public List<UserMasterDto> retrieveByCriteria(Integer userId, List<Integer> roles, Integer locationId, String searchString, String orderBy, Integer limit, Integer offset, String order,String status);
    /**
     * Returns a list of user based on given aadhar number
     *
     * @param aadharNumber An aadhar number
     * @return A list of UserMaster
     */
    List<UserMaster> retrieveByAadhar(String aadharNumber);

    /**
     * Returns a list of user based on given contact number
     *
     * @param contactNumber A contact number
     * @param roleId        A role id
     * @return A list of UserMaster
     */
    List<UserMaster> retrieveByPhone(String contactNumber, Integer roleId);

    /**
     * Returns a list of user based on given locations and roles
     *
     * @param roleId     A role id
     * @param locationId A location id
     * @param userId     An id of user
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> retrieveUsersByRoleAndLocation(Integer roleId, Integer locationId, Integer userId);

    /**
     * Returns Full name and User name only of active users
     *
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> getAllActiveUsers();

    /**
     * Checks username is available or not
     *
     * @param userName A username of user
     * @param userId   An id of user
     * @return A boolean value for username is available or not
     */
    Boolean validateUserName(String userName, Integer userId);

    /**
     * Returns a list of user based on given contact number and user id
     *
     * @param contactNumber A contact number
     * @param userId        An id of user
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> conflictUserOfContactNumber(String contactNumber, Integer userId);

    /**
     * Returns a list of user based on given user id
     *
     * @param userId An id of user
     * @return A list of UserMasterDto
     */
    List<UserMasterDto> conflictUserOfLocation(Integer userId);

    /**
     * Returns an instance of user based on location id and role code
     *
     * @param locationId A location id
     * @param roleCode   A role code
     * @return an instance of UserMaster
     */
    UserMaster retrieveUserByRoleCodeNLocation(Integer locationId, String roleCode);

    List<UserMaster> retrieveEmergencyRespondersForChardhamByRoleAndDiscardedUsers(Integer roleId, List<Integer> discardedUsers);

    List<OptionTagDto> getListValuesFromFieldKey(String key);

    boolean hasLocationAccess(int loggedInUserId, int userToBeAccessedId);
}
