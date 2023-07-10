package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.model.MenuGroup;

import java.util.List;

/**
 * <p>
 *     Define methods for menu group
 * </p>
 * @author jaynam
 * @since 27/08/2020 4:30
 */
public interface MenuGroupService {

    /**
     * Create or update given menu group
     * @param menuGroup An instance of MenuGroup
     * @return An id of created or updated row
     */
     Integer saveGroup(MenuGroup menuGroup);

    /**
     * Returns a list of menu group based on given criteria
     * @param groupName A name of menu group
     * @param offset A value for offset
     * @param limit A value for limit
     * @param subGroupRequired A boolean value for whether sub group is required or not
     * @param groupType A type of group
     * @return A list of MenuGroupDto
     */
     List<MenuGroup> retrieveGroups(String groupName, Integer offset, Integer limit, Boolean subGroupRequired, String groupType);

    /**
     * Deletes menu group of given id by making its status inactive
     * @param id An id of menu group
     */
     void deleteGroupById(Integer id);

}
