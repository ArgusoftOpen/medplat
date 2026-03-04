package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;
import java.util.UUID;

/**
 * <p>Defines database method for menu configuration</p>
 * @author charmi
 * @since 31/08/2020 10:30
 */
public interface MenuConfigDao extends GenericDao<MenuConfig, Integer> {

    /**
     * Retrieve configuration types.
     * @return A list of string
    */
     List<String> getConfigurationTypes();
    
    
    /**
     * Check menu group is available or not
     * @param menuTypeUUID An uuid of menu type
     * @param columnName A name of the column
     * @return Whether menu group is available or not
     */
     Boolean checkMenuGroups(UUID menuTypeUUID, String columnName);
    
    /**
     * Check Menu Type is present or not.
     * @param typeName A name of type
     * @return Whether type is available or not
     */
     Boolean checkTypeIsAvailable(String typeName);

    /**
     * Retrieve MenuConfig with userList.
     * @param menuConfigId An id of menu configuration
     * @param joinEntities An instance of MenuConfigJoin
     * @return An instance of MenuConfigDto
     */
     MenuConfigDto getMenuConfigByIdWithUserList(Integer menuConfigId, MenuConfig.MenuConfigJoin... joinEntities);

    /**
     * Get active menu of user by its details[userId, designation, group]
     * @param fetchGroupObjects Whether to fetch group or not
     * @param userId An id of user
     * @param designationId An id of user role
     * @return A list of MenuConfig
     */
     List<MenuConfig> getActiveMenusByUserIdAndDesignationAndGroup(Boolean fetchGroupObjects, Integer userId, Integer designationId);

    /**
     * Get MenuConfigType With status.
     * @param type A type of menu configuration
     * @param isUserAdmin Whether user is admin or not
     * @param joinEntities An instance of MenuConfigJoin
     * @return A list of MenuConfig
     */
     List<MenuConfig> getMenuConfigListByType(String type,Boolean isUserAdmin, MenuConfig.MenuConfigJoin... joinEntities);

}
