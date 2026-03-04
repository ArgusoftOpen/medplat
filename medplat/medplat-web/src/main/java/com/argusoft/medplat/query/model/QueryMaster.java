/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Defines fields of query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Entity
@Table(name = "query_master")
public class QueryMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;
    
    @Column(name = "query")
    private String query;
    @Column(name = "description")
    private String description;
    @Column(name = "code", length = 20)
    private String code;
    @Column(name = "returns_result_set")
    private Boolean returnsResultSet;
    @Column(name = "params")
    private String params;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "is_public")
    private Boolean isPublic;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
     * An enumeration for state
     */
    public enum State {
        ACTIVE, INACTIVE, ARCHIVED
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields(){

        }
        public static final String QUERY = "query";
        public static final String CODE = "code";
        public static final String STATE = "state";
        public static final String UUID = "uuid";
        public static final String CREATED_ON = "createdOn";
        public static final String AADHAR_NUMBER = "aadharNumber";
    }
}
