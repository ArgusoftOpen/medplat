/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.task.service;

import com.argusoft.medplat.dashboard.task.dto.WebTaskDetailDto;
import com.argusoft.medplat.dashboard.task.dto.WebTaskMasterDto;

import java.util.List;
import java.util.Map;

/**
 * <p>Methods signature for web task dashboard service</p>
 * @author kunjan
 * @since 31/08/20 11:15 PM
 * 
 */
public interface WebTaskDashboardService {
    
    /**
     * Returns web task details
     * @return List of web task detail
     */
    List<WebTaskMasterDto> getWebTaskCount();
    
    /**
     * Returns web task detail by type
     * @param taskTypeId task type id
     * @param offset offset
     * @param limit limit
     * @param taskId task id
     * @param loggedInUserId logged in user id
     * @param userId user id
     * @param locationId location id
     * @param memberId member id
     * @return List of web task detail
     */
    List<WebTaskDetailDto> getWebTaskDetailByType(Integer taskTypeId, Integer limit, Integer offset, Integer taskId, Integer loggedInUserId, Integer userId, Integer memberId,Integer locationId);
    
    /**
     * Returns selected task detail and related task detail
     * @param taskId type id
     * @return Map of string and list of web task
     */
    Map<String,List<WebTaskMasterDto>> getSelectedTaskDetailAndOtherRelatedTasksWithAction(Integer taskId);
    
    /**
     * Save actions
     * @param taskDetailDtos list of task actions
     */
    void saveActions(List<WebTaskDetailDto> taskDetailDtos);
    
}
