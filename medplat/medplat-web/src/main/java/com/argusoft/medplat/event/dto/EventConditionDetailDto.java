/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

import java.util.List;

/**
 *
 * <p>
 *     Used for event condition details.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class EventConditionDetailDto {
    private Boolean isConditionReq;
    private String condition;
    private String conditionParam;
    private String description;
    private List<EventConfigTypeDto> notificaitonConfigsType;
    

    public Boolean getIsConditionReq() {
        return isConditionReq == null ? Boolean.FALSE : isConditionReq;
    }

    public void setIsConditionReq(Boolean isConditionReq) {
        this.isConditionReq = isConditionReq;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<EventConfigTypeDto> getNotificaitonConfigsType() {
        return notificaitonConfigsType;
    }

    public void setNotificaitonConfigsType(List<EventConfigTypeDto> notificaitonConfigsType) {
        this.notificaitonConfigsType = notificaitonConfigsType;
    }

    public String getConditionParam() {
        return conditionParam;
    }

    public void setConditionParam(String conditionParam) {
        this.conditionParam = conditionParam;
    }
    
    

}
