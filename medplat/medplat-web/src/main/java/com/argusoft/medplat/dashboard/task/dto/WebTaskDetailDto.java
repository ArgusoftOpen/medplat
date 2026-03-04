/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.task.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Web task detail dto</p>
 * @author kunjan
 * @since 31/08/20 11:15 PM
 * 
 */
public class WebTaskDetailDto {

    Integer taskId;
    Map<String, Object> details;
    List<LinkedHashMap<String, Object>> actions;
    Boolean isActionRequired;
    Boolean isOtherDetailsRequired;
    String selectedAction;
    String otherDetail;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public List<LinkedHashMap<String, Object>> getActions() {
        return actions;
    }

    public void setActions(List<LinkedHashMap<String, Object>> actions) {
        this.actions = actions;
    }

    public Boolean getIsActionRequired() {
        return isActionRequired;
    }

    public void setIsActionRequired(Boolean isActionRequired) {
        this.isActionRequired = isActionRequired;
    }

    public Boolean getIsOtherDetailsRequired() {
        return isOtherDetailsRequired;
    }

    public void setIsOtherDetailsRequired(Boolean isOtherDetailsRequired) {
        this.isOtherDetailsRequired = isOtherDetailsRequired;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    public String getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        this.otherDetail = otherDetail;
    }

}
