/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dto;

import com.argusoft.medplat.query.model.ReportQueryMaster.State;

import java.util.Date;
import java.util.UUID;

/**
 * Defines fields of report query
 * @author vaishali
 * @since 02/09/2020 10:30
 */

public class ReportQueryMasterDto {
    
    private Integer id;
    private String query;
    private State state;
    private Boolean returnsResultSet;
    private Date createdOn;
    private Integer createdBy;
    private String params;
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getReturnsResultSet() {
        return returnsResultSet;
    }

    public void setReturnsResultSet(Boolean returnsResultSet) {
        this.returnsResultSet = returnsResultSet;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    
    
    
}
