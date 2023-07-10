package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.model.MenuConfig.MenuConfigJoin;
import com.argusoft.medplat.web.users.dto.UserMenuItemDto;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>
 *    An util class for menu configuration to convert modal to dto  or dto to modal
 *</p>
 * @author charmi
 * @since 26/08/2020 5:30
 */
public class MenuConfigMapper {

    private MenuConfigMapper() {
            
     }

    /**
     * Converts menu config modal to menu config dto
     * @param menuConfig An instance of MenuConfig
     * @param setReportName Whether to set report name or not
     * @return An instance of MenuConfigDto
     */
    private static MenuConfigDto getMenuDto(MenuConfig menuConfig, boolean setReportName) {
        MenuConfigDto menuDto = new MenuConfigDto();
        menuDto.setNavigationState(menuConfig.getNavigationState());
        menuDto.setFeatureJson(menuConfig.getFeatureJson());
        menuDto.setDescription(menuConfig.getDescription());
        menuDto.setIsDynamicReport(menuConfig.getIsDynamicReport());
        menuDto.setDisplayOrder(menuConfig.getDisplayOrder());
        if (setReportName) {
            menuDto.setId(menuConfig.getId());
            menuDto.setName(menuConfig.getName());
        } else if (menuConfig.getSubGroup() != null) {
            menuDto.setId(menuConfig.getSubGroupId());
            menuDto.setName(menuConfig.getSubGroup().getGroupName());
            menuDto.setIsSubGroup(Boolean.TRUE);
        } else if (menuConfig.getGroupId() != null) {
            menuDto.setId(menuConfig.getGroupId());
            menuDto.setName(menuConfig.getMenuGroup().getGroupName());
            menuDto.setDisplayOrder(menuConfig.getMenuGroup().getDisplayOrder());
            menuDto.setIsGroup(Boolean.TRUE);
        }
        return menuDto;

    }

    /**
     * Converts menu config modal to menu config dto
     * @param menuConfig An instance of MenuConfig
     * @param joinEntities An instance of MenuConfigJoin
     * @return An instance of MenuConfigDto
     */
    public static MenuConfigDto getMenuDto(MenuConfig menuConfig, MenuConfigJoin... joinEntities) {
        MenuConfigDto menuConfigDto = new MenuConfigDto();
        if (menuConfig != null) {
            menuConfigDto.setNavigationState(menuConfig.getNavigationState());
            menuConfigDto.setDescription(menuConfig.getDescription());
            menuConfigDto.setId(menuConfig.getId());
            menuConfigDto.setName(menuConfig.getName());
            menuConfigDto.setFeatureJson(menuConfig.getFeatureJson());
            menuConfigDto.setIsDynamicReport(menuConfig.getIsDynamicReport());
            for (MenuConfigJoin joinEntity : joinEntities) {
                switch (joinEntity) {
                    case MENU_GROUP:
                        if (menuConfig.getMenuGroup() != null) {
                            menuConfigDto.setGroupName(menuConfig.getMenuGroup().getGroupName());
                        }
                        break;
                    case SUB_GROUP:
                        if (menuConfig.getSubGroup() != null) {
                            menuConfigDto.setSubGroupName(menuConfig.getSubGroup().getGroupName());
                        }
                        break;
                    case USER_MENU_ENTITY:
                        setUserMenu(menuConfig,menuConfigDto);
                        break;
                    default:
                        break;
                }
            }
        }
        return menuConfigDto;
    }

    /**
     * A util method to convert user menu item modal to user menu item dto
     * @param menuConfig An instance of MenuConfig
     * @param menuConfigDto An instance of menuConfigDto
     */
    private static void setUserMenu(MenuConfig menuConfig, MenuConfigDto menuConfigDto){
        if (!CollectionUtils.isEmpty(menuConfig.getUserMenuItemList())) {
            List<UserMenuItemDto> userMenuItemDtos = new ArrayList<>();
            menuConfig.getUserMenuItemList().stream().map(userMenuItem -> {
                UserMenuItemDto userMenuItemDto = new UserMenuItemDto();
                userMenuItemDto.setId(userMenuItem.getId());
                userMenuItemDto.setMenuConfigId(userMenuItem.getMenuConfigId());
                userMenuItemDto.setDesignationId(userMenuItem.getRoleId());
                userMenuItemDto.setUserId(userMenuItem.getUserId());
                userMenuItemDto.setFeatureJson(userMenuItem.getFeatureJson());
                return userMenuItemDto;
            }).forEach(userMenuItemDtos::add
            );
            menuConfigDto.setUserMenuItemDtos(userMenuItemDtos);
        }
    }

    /**
     * Converts a list of menu config modal to menu config dto
     * @param menuConfigList A list of MenuConfig
     * @param joinEntities An instance of MenuConfigJoin
     * @return A list of MenuConfigDto
     */
    public static List<MenuConfigDto> getMenuDtoList(List<MenuConfig> menuConfigList, MenuConfigJoin... joinEntities) {
        List<MenuConfigDto> menuDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(menuConfigList)) {
            for (MenuConfig menuConfig : menuConfigList) {
                menuDtos.add(getMenuDto(menuConfig, joinEntities));
            }
        }
        return menuDtos;
    }

    /**
     * Converts a list of menu config modal to menu config dto
     * @param menuConfigList A list of MenuConfig
     * @param menuConfigDtos A list of MenuConfigDto
     * @param parentId An id of parent
     * @param subGroupId An id of sub group
     * @return A list of MenuConfigDto
     */
    public static List<MenuConfigDto> getMenuConfigAsMenuDtoList(List<MenuConfig> menuConfigList, List<MenuConfigDto> menuConfigDtos, Integer parentId, Integer subGroupId) {
        if (!CollectionUtils.isEmpty(menuConfigList)) {
            List<Integer> addedGroupIds = new ArrayList<>();
            for (MenuConfig menuConfig : menuConfigList) {
                //Set groups for all the parent groups

                if (menuConfig.getGroupId() != null && menuConfig.getSubGroupId() != null && menuConfig.getGroupId().equals(parentId)) {
                    if (!addedGroupIds.contains(menuConfig.getSubGroupId())) {
                        addedGroupIds.add(menuConfig.getSubGroupId());
                        MenuConfigDto menuDto = getMenuDto(menuConfig, false);
                        menuDto.setSubGroups(getMenuConfigAsMenuDtoList(menuConfigList, new ArrayList<>(), null, menuConfig.getSubGroupId()));
                        menuConfigDtos.add(menuDto);
                    }
                    //Set parent Groups
                } else if (menuConfig.getGroupId() != null && parentId == null && subGroupId == null) {
                    if (!addedGroupIds.contains(menuConfig.getGroupId())) {
                        addedGroupIds.add(menuConfig.getGroupId());
                        MenuConfigDto menuDto = getMenuDto(menuConfig, false);
                        menuDto.setName(menuConfig.getMenuGroup().getGroupName());             
                        menuDto.setIsGroup(Boolean.TRUE);
                        menuDto.setId(menuConfig.getGroupId());
                        menuDto.setIsDynamicReport(menuConfig.getIsDynamicReport());
                        menuDto.setSubGroups(getMenuConfigAsMenuDtoList(menuConfigList, new ArrayList<>(), menuConfig.getGroupId(), null));
                        menuConfigDtos.add(menuDto);
                    }
                    //set all subgroup reports
                } else if ((menuConfig.getSubGroupId() != null && menuConfig.getSubGroupId().equals(subGroupId))
                        || (menuConfig.getSubGroupId() == null && menuConfig.getGroupId() != null && menuConfig.getGroupId().equals(parentId) && subGroupId == null)
                        || (menuConfig.getGroupId() == null && parentId == null && subGroupId == null)) {
                    MenuConfigDto menuDto = getMenuDto(menuConfig, true);
                    menuConfigDtos.add(menuDto);
                }
            }
        }
        return menuConfigDtos;
    }
}
