package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for menu group</p>
 * @author jaynam
 * @since 31/08/2020 10:30
 */
public interface MenuGroupDao extends GenericDao<MenuGroup, Integer> {

    /**
     * Returns menu group of given group type and name
     * @param groupType A group type
     * @param groupName A group name
     * @return An instance of MenuGroup
     */
    MenuGroup retrieveMenuGroupByGroupNameAndType(String groupType, String groupName);

    /**
     * Returns a list of active menu groups
     * @param groupName A group name
     * @param offset An offset value
     * @param limit A limit value
     * @param subGroupRequired Whether sub group is required or not
     * @param groupType A group type
     * @return A list of MenuGroup
     */
    List<MenuGroup> getActiveGroups(String groupName, Integer offset, Integer limit, Boolean subGroupRequired, String groupType);

    /**
     * Checks whether any report is associated with given id of group or its subgroup or not
     * @param groupId An id of group
     * @return Whether any report is associated with group or not
     */
    Boolean isReportAssociatedWithGrouporSubGroup(Integer groupId);

    /**
     * Checks whether any subgroup is associated with given id of group or not
     * @param groupId An id of group
     * @return Whether any sub group is associated with group or not
     */
    Boolean isSubgroupAssociatedWithGroup(Integer groupId);
}
