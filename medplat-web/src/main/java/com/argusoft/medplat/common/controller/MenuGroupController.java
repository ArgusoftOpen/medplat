package com.argusoft.medplat.common.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.common.dto.MenuGroupDto;
import com.argusoft.medplat.common.mapper.MenuGroupMapper;
import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.common.service.MenuGroupService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 *<p>Defines rest endpoints for menu group</p>
 * @author jaynam
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/menugroup")
@Tag(name = "Menu Group Controller", description = "")
public class MenuGroupController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MenuGroupController.class);

    @Autowired
    private MenuGroupService menuGroupService;

    /**
     * Create or update given menu group
     * @param groupMasterDto An instance of MenuGroupDto
     * @return An id of created or updated row
     */
    @PostMapping()
    public Integer saveGroup(@RequestBody MenuGroupDto groupMasterDto) {
        MenuGroup groupMasterModel = MenuGroupMapper.getGroupMasterModel(groupMasterDto);
        return menuGroupService.saveGroup(groupMasterModel);

    }

    /**
     * Returns a list of menu group based on given criteria
     * @param groupName A name of menu group
     * @param offset A value for offset
     * @param limit A value for limit
     * @param subGroupRequired A boolean value for whether sub group is required or not
     * @param groupType A type of group
     * @param response An instance of HttpServletResponse
     * @return A list of MenuGroupDto
     */
    @GetMapping()
    public List<MenuGroupDto> getGroups(
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "subGroupRequired", required = false) Boolean subGroupRequired,
            @RequestParam(value = "groupType", required = false) String groupType,
            HttpServletResponse response) {
        List<MenuGroup> groupMasters = menuGroupService.retrieveGroups(groupName, offset, limit, subGroupRequired, groupType);
        List<MenuGroup> parentGroup = menuGroupService.retrieveGroups(groupName, null, null, Boolean.FALSE, groupType);
        if (!CollectionUtils.isEmpty(groupMasters)) {
            return MenuGroupMapper.getGroupMasterDtoList(groupMasters, parentGroup);
        } else {
            LOGGER.info("GroupMaster is null");
        }
        return Collections.emptyList();
    }

    /**
     * Deletes menu group of given id
     * @param groupId An id of menu group
     */
    @DeleteMapping(value = "/{id}")
    public void deleteGroupById(@PathVariable("id") Integer groupId) {
        menuGroupService.deleteGroupById(groupId);
    }

}