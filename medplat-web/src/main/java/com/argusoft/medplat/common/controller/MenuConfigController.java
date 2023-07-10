package com.argusoft.medplat.common.controller;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.service.MenuConfigService;
import com.argusoft.medplat.common.util.CommonConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.web.users.dto.UserMenuItemDto;
import com.argusoft.medplat.web.users.model.UserMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *<p>Define rest endpoints for menu configuration</p>
 * @author charmi
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/menuconfig")
public class MenuConfigController {

    @Autowired
    private MenuConfigService menuConfigService;

    @Autowired
    private ImtechoSecurityUser currenUser;

    /**
     * Returns a list of menu configuration type
     * @return A list of string
     */
    @GetMapping(value = "/configtypes")
    public List<String> getConfigurationTypes() {
        return menuConfigService.getConfigurationTypes();
    }

    /**
     * Returns a menu configuration of given id
     * @param menuId An Id of menu configuration
     * @return An instance of MenuConfigDto
     */
    @GetMapping(value = "/configtypes/{id}")
    public MenuConfigDto getMenuDtoById(@PathVariable("id") Integer menuId) {
        return menuConfigService.getMenuConfigByIdWithUserList(menuId);
    }

    /**
     * Returns list of menu of given type
     * @param type A menu type
     * @return A list of MenuConfigDto
     */
    @GetMapping(value = "/{type}")
    public List<MenuConfigDto> getMenuConfigByType(@PathVariable("type") String type) {
        return menuConfigService.getMenuConfigListByType(type);
    }

    /**
     * Saves user menu item
     * @param allocationMap A map of user id or role id
     * @param menuConfigId An id of menu configuration
     */
    @PostMapping(value = "/menuitem")
    public void saveMenuItem(@RequestBody Map<String, String> allocationMap, @RequestParam(required = false) Integer menuConfigId) {
        if (!CollectionUtils.isEmpty(allocationMap)) {
            List<UserMenuItem> userMenuItems = new ArrayList<>();
            if (allocationMap.containsKey(CommonConstantUtil.DESIGNATION_ID)
                    || allocationMap.containsKey(CommonConstantUtil.USER_ID)) {

                if (allocationMap.get(CommonConstantUtil.DESIGNATION_ID) != null) {
                    setDataMenuItem(userMenuItems, allocationMap, menuConfigId, Boolean.FALSE);
                }
                if (allocationMap.get(CommonConstantUtil.USER_ID) != null) {
                    setDataMenuItem(userMenuItems, allocationMap,menuConfigId, Boolean.TRUE);
                }
            }
            menuConfigService.saveUserMenuItem(userMenuItems);
        }
    }

    /**
     * Assign new feature to particular role
     * @param allocationMap List of feature ids.
     * @param roleId Role id
     */
    @PostMapping(value = "/assignnewfeature")
    public void assignNewFeatureToRole(@RequestBody Map<String, String> allocationMap, @RequestParam(required = false) Integer roleId) {
        if (!CollectionUtils.isEmpty(allocationMap)) {
            List<UserMenuItem> userMenuItems = new ArrayList<>();
            if (allocationMap.get(CommonConstantUtil.FEATURE_ID) != null) {
                List<String> featureIdList = Arrays.asList(allocationMap.get(CommonConstantUtil.FEATURE_ID).split(","));
                for (String featureId : featureIdList) {
                    UserMenuItem userMenuItem = new UserMenuItem();
                    userMenuItem.setRoleId(roleId);
                    userMenuItem.setMenuConfigId(Integer.parseInt(featureId));
                    userMenuItems.add(userMenuItem);
                }
            }
            menuConfigService.saveUserMenuItem(userMenuItems);
        }
    }
    
    /*
     * Deletes menu item of given id
     * @param empMenuItemId An id of menu item
     */
    @DeleteMapping(value = "/usermenuitem/{empMenuItemId}")
    public void deleteMenuItem(@PathVariable("empMenuItemId") Integer empMenuItemId) {
        menuConfigService.deleteUserMenuItem(empMenuItemId);
    }

    /**
     * Update user menu itsm
     * @param userMenuItemDto An instance of UserMenuItemDto
     */
    @PostMapping(value = "/updateusermenuitem")
    public void updateEmpMenuItem(@RequestBody UserMenuItemDto userMenuItemDto) {
        if (userMenuItemDto != null) {
            List<UserMenuItem> userMenuItems = new ArrayList<>();
            UserMenuItem userMenuItem = new UserMenuItem();
            userMenuItem.setId(userMenuItemDto.getId());
            userMenuItem.setMenuConfigId(userMenuItemDto.getMenuConfigId());
            userMenuItem.setRoleId(userMenuItemDto.getDesignationId());
            userMenuItem.setUserId(userMenuItemDto.getUserId());
            userMenuItem.setFeatureJson(userMenuItemDto.getFeatureJson());
            userMenuItems.add(userMenuItem);
            menuConfigService.saveUserMenuItem(userMenuItems);
        }
    }

    private void setDataMenuItem(List<UserMenuItem> userMenuItems, Map<String, String> allocationMap,Integer menuConfigId, boolean isUser){
        List<String> idList;
        if(Boolean.TRUE.equals(isUser)){
            idList = Arrays.asList(allocationMap.get(CommonConstantUtil.USER_ID).split(","));
        }else {
            idList = Arrays.asList(allocationMap.get(CommonConstantUtil.DESIGNATION_ID).split(","));
        }
        for (String id : idList) {
            UserMenuItem userMenuItem = new UserMenuItem();
            if(Boolean.TRUE.equals(isUser)){
                userMenuItem.setUserId(Integer.valueOf(id));
            }else{
                userMenuItem.setRoleId(Integer.parseInt(id));
            }
            userMenuItem.setMenuConfigId(menuConfigId);
            userMenuItems.add(userMenuItem);
        }
    }
}
