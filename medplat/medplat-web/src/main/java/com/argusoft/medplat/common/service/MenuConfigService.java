package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.web.users.model.UserMenuItem;

import java.util.List;

/**
 * <p>
 *     Define methods for menu configuration
 * </p>
 * @author charmi
 * @since 27/08/2020 4:30
 */
public interface MenuConfigService {

    /**
     * Get active menu of user by its details[userId, designation, group]
     * @param fetchGroupObjects Whether to fetch group or not
     * @param userId An id of user
     * @param designationId An id of user role
     * @return A list of MenuConfig
     */
     List<MenuConfig> getActiveMenusByUserIdAndDesignationAndGroup(Boolean fetchGroupObjects, Integer userId, Integer designationId);

    /**
     * Returns a list of menu configuration type
     * @return A list of string
     */
     List<String> getConfigurationTypes();

    /**
     * Retrieve assigned list of user with menu by id.
     * @param menuConfigId An id of menu configuration
     * @return An instance of MenuConfigDto
     */
     MenuConfigDto getMenuConfigByIdWithUserList(Integer menuConfigId);

    /**
     * Retrieve sub menu from main menu.
     * @param type A type of menu configuration
     * @return A list of MenuConfigDto
     */
     List<MenuConfigDto> getMenuConfigListByType(String type);

    /**
     * Save user association with menu item.
     * @param empMenuItems A list of UserMenuItem
     */
     void saveUserMenuItem(List<UserMenuItem> empMenuItems);

    /**
     * Delete user association with menu item.
     * @param id An id of user menu item
     */
     void deleteUserMenuItem(Integer id);

}
