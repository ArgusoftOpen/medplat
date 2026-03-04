package com.argusoft.medplat.web.users.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.users.model.UserLocation;

import java.util.List;

/**
 * <p>
 *     Defines database method for user location
 * </p>
 * @author Harshit
 * @since 31/08/2020 10:30
 */
public interface UserLocationDao extends GenericDao<UserLocation, Integer> {
    /**
     * Returns a minimum user location level
     * @param userId A user id
     * @return A minimum user location level
     */
    Integer getUserMinLevel(Integer userId);

    /**
     * Mark in active user location
     * @param userLocation An instance of UserLocation
     */
    void markAsInactive(UserLocation userLocation);

    /**
     * Returns a list of all user location based on given user id
     * @param userId A user id
     * @return A list of UserLocation
     */
    List<UserLocation> retrieveAllLocationsByUserId(Integer userId);

    /**
     * Returns a list of user location based on given user id
     * @param userId A user id
     * @return A list of UserLocation
     */
    List<UserLocation> retrieveLocationsByUserId(Integer userId);

    /**
     * Returns a list of user location based on given location id
     * @param locationIds A list of location id
     * @return A list of UserLocation
     */
    List<UserLocation> getUserLocationByLocationId(List<Integer> locationIds);

    /**
     * Returns a list of user location id based on user id
     * @param userId A user id
     * @return A list of user location id
     */
    List<Integer> retrieveLocationIdsByUserId(Integer userId);

    /**
     * Returns a user location based on user id and location id
     * @param id A user id
     * @param locationId A location id
     * @return An instance of UserLocation
     */
    UserLocation getUserLocationByUserIdLocationId(Integer id, Integer locationId);
}
