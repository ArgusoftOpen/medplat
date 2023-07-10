/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.task.dto;

import java.util.List;

/**
 * <p>Web task master dto</p>
 * @author kunjan
 * @since 31/08/20 11:15 PM
 * 
 */
public class WebTaskMasterDto {

    Integer id;
    String name;
    Integer count;
    Integer dueCount;
    String colorCode;
    Integer orderNo;
    List<WebTaskDetailDto> webTasks;
    Boolean isLocationBasedFilterRequired;
    Integer fetchUptoLevel;
    Integer requiredUptoLevel;
    Boolean isFetchAccordingAOI;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDueCount() {
        return dueCount;
    }

    public void setDueCount(Integer dueCount) {
        this.dueCount = dueCount;
    }

    public List<WebTaskDetailDto> getWebTasks() {
        return webTasks;
    }

    public void setWebTasks(List<WebTaskDetailDto> webTasks) {
        this.webTasks = webTasks;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getIsLocationBasedFilterRequired() {
        return isLocationBasedFilterRequired;
    }

    public void setIsLocationBasedFilterRequired(Boolean isLocationBasedFilterRequired) {
        this.isLocationBasedFilterRequired = isLocationBasedFilterRequired;
    }

    public Integer getFetchUptoLevel() {
        return fetchUptoLevel;
    }

    public void setFetchUptoLevel(Integer fetchUptoLevel) {
        this.fetchUptoLevel = fetchUptoLevel;
    }

    public Integer getRequiredUptoLevel() {
        return requiredUptoLevel;
    }

    public void setRequiredUptoLevel(Integer requiredUptoLevel) {
        this.requiredUptoLevel = requiredUptoLevel;
    }

    public Boolean getIsFetchAccordingAOI() {
        return isFetchAccordingAOI;
    }

    public void setIsFetchAccordingAOI(Boolean isFetchAccordingAOI) {
        this.isFetchAccordingAOI = isFetchAccordingAOI;
    }
    
    
}
