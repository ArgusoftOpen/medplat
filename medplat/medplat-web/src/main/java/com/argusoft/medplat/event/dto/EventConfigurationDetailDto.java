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
 *     Used for event configuration details.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class EventConfigurationDetailDto {
    
    private String query;
    private String queryParam;
    private List<EventConditionDetailDto> conditions;
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<EventConditionDetailDto> getConditions() {
        return conditions;
    }

    public void setConditions(List<EventConditionDetailDto> notificationConditions) {
        this.conditions = notificationConditions;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }
    
    
    
}
