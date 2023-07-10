package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.MenuGroupDto;
import com.argusoft.medplat.common.model.MenuGroup;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<p>
 *    An util class for menu group to convert modal to dto  or dto to modal
 *</p>
 * @author jaynam
 * @since 26/08/2020 5:30
 */
public class MenuGroupMapper {

    private MenuGroupMapper() {

    }

    /**
     * Converts a list of menu group dto to menu group modal
     * @param menuGroups A list of MenuGroup
     * @param parentGroup A list of parentGroup
     * @return A list of MenuGroupDto
     */
    public static List<MenuGroupDto> getGroupMasterDtoList(List<MenuGroup> menuGroups, List<MenuGroup> parentGroup) {
        List<MenuGroupDto> groupMasterDtos = null;
        if (!CollectionUtils.isEmpty(menuGroups)) {
            groupMasterDtos = new ArrayList<>();
            Map<Integer, MenuGroup> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(parentGroup)) {
                parentGroup.stream().forEach(groupMaster ->
                    map.put(groupMaster.getId(), groupMaster)
                );
            }
            for (MenuGroup groupMaster : menuGroups) {
                groupMasterDtos.add(getGroupMasterDto(groupMaster, map));
            }
        }
        return groupMasterDtos;
    }

    /**
     * Converts menu group dto to menu group modal
     * @param menuGroup An instance of MenuGroup
     * @param map Map of integer and MenuGroup
     * @return An instance of MenuGroupDto
     */
    public static MenuGroupDto getGroupMasterDto(MenuGroup menuGroup, Map<Integer, MenuGroup> map) {
        MenuGroupDto groupMasterDto = new MenuGroupDto();
        if (menuGroup != null) {
            groupMasterDto.setId(menuGroup.getId());
            groupMasterDto.setParentGroup(menuGroup.getParentGroup());
            groupMasterDto.setGroupName(menuGroup.getGroupName());
            groupMasterDto.setType(menuGroup.getType());
            groupMasterDto.setIsActive(menuGroup.getIsActive());
            if (!CollectionUtils.isEmpty(map)) {
                MenuGroup groupMasterObj = map.get(menuGroup.getParentGroup());
                if (groupMasterObj != null) {
                    groupMasterDto.setParentGroupName(groupMasterObj.getGroupName());
                }
            }
        }
        return groupMasterDto;
    }

    /**
     * Converts menu group dto to menu group modal
     * @param groupMasterDto An instance of MenuGroupDto
     * @return An instance of MenuGroup
     */
    public static MenuGroup getGroupMasterModel(MenuGroupDto groupMasterDto) {
        return getGroupMasterModel(groupMasterDto, null);
    }

    /**
     * Converts menu group dto to menu group modal
     * @param groupMasterDto An instance of MenuGroupDto
     * @param menuGroup An instance of MenuGroup
     * @return An instance of
     */
    public static MenuGroup getGroupMasterModel(MenuGroupDto groupMasterDto, MenuGroup menuGroup) {
        if (menuGroup == null) {
            menuGroup = new MenuGroup();
        }
        if (groupMasterDto.getId() != null) {
            menuGroup.setId(groupMasterDto.getId());
        }
        menuGroup.setIsActive(Boolean.TRUE);
        menuGroup.setGroupName(groupMasterDto.getGroupName());
        menuGroup.setParentGroup(groupMasterDto.getParentGroup());
        menuGroup.setType(groupMasterDto.getType());
        return menuGroup;
    }

}
