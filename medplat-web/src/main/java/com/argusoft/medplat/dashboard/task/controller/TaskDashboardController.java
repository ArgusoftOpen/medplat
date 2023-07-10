/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.task.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.basketpreference.dto.UserBasketPreferenceDto;
import com.argusoft.medplat.dashboard.basketpreference.service.UserBasketPreferenceService;
import com.argusoft.medplat.dashboard.task.dto.WebTaskDetailDto;
import com.argusoft.medplat.dashboard.task.dto.WebTaskMasterDto;
import com.argusoft.medplat.dashboard.task.service.WebTaskDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>Web task dashboard controller</p>
 * @author kunjan
 * @since 28/08/20 12:15 PM
 * 
 */
@RestController
@RequestMapping("/api/webtasks")
public class TaskDashboardController {

    @Autowired
    private WebTaskDashboardService webTaskDashboardService;
    @Autowired
    ImtechoSecurityUser user;
    @Autowired
    UserBasketPreferenceService userBasketPreferenceService;

    /**
     * Returns web task details
     * @return List of web task detail
     */
    @GetMapping(value = "/count")
    public List<WebTaskMasterDto> getWebTaskCount() {
        return webTaskDashboardService.getWebTaskCount();
    }

    /**
     * Returns web task detail by type
     * @param taskTypeId task type id
     * @param offset offset
     * @param limit limit 
     * @param userId user id
     * @param locationId location id
     * @param memberId member id
     * @return List of web task detail
     */
    @GetMapping(value = "/basketdetail")
    public List<WebTaskDetailDto> getWebTaskDetailByType(@RequestParam("taskTypeId") Integer taskTypeId,
            @RequestParam(name = "offset") Integer offset,
            @RequestParam(name = "limit",required = false) Integer limit,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name="locationId",required = false) Integer locationId,
            @RequestParam(name = "memberId", required = false) Integer memberId) {
        return webTaskDashboardService.getWebTaskDetailByType(taskTypeId, limit, offset, null, user.getId(), null, null,locationId);
    }

    /**
     * Returns selected task detail and related task detail
     * @param taskId type id
     * @return Map of string and list of web task
     */
    @GetMapping(value = "/getaction")
    public Map<String, List<WebTaskMasterDto>> getSelectedTaskDetailAndOtherRelatedTasksWithAction(@RequestParam("taskId") Integer taskId) {
        return webTaskDashboardService.getSelectedTaskDetailAndOtherRelatedTasksWithAction(taskId);
    }

    /**
     * Save actions
     * @param tasksWithActions list of task actions
     */
    @PostMapping(value = "/saveaction")
    public void saveActions(@RequestBody List<WebTaskDetailDto> tasksWithActions) {
        webTaskDashboardService.saveActions(tasksWithActions);
    }

    /**
     * Save basket preference
     * @param userBasketPreferenceDto user basket preference
     */
    @PostMapping(value = "/savebasketpreference")
    public void saveBasketPreference(@RequestBody UserBasketPreferenceDto userBasketPreferenceDto)  {
        userBasketPreferenceService.createOrUpdate(userBasketPreferenceDto);
    }

    /**
     * Returns user preference by user id
     * @param userId user id
     * @return String
     */
    @GetMapping(value = "/retrievebasketpreference")
    public String retrievePreferenceByUserId(@RequestParam Integer userId)  {
        return userBasketPreferenceService.retrievePreferenceByUserId(userId);
    }

}
