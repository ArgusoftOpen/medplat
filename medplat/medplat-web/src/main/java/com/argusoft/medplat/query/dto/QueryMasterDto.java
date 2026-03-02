/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.dto;

import com.argusoft.medplat.query.model.QueryMaster.State;

import java.util.Date;
import java.util.UUID;

/**
 * Defines fields of query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public class QueryMasterDto {

    private String query;
    private String code;
    private State state;
    private Boolean returnsResultSet;
    private Date createdOn;
    private Integer createdBy;
    private String params;
    private String description;
    private Boolean isPublic;
    private UUID uuid;
    

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return returnsResultSet == null ? Boolean.FALSE : returnsResultSet;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "QueryMasterDto{" + "query=" + query + ", code=" + code + ", state=" + state + ", returnsResultSet=" + returnsResultSet + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", params=" + params + ", description=" + description + ", isPublic=" + isPublic + ", uuid=" + uuid + '}';
    }
}
