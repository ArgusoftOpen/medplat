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
 * Defines fields of report query
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Entity
@Table(name = "report_query_master")
public class ReportQueryMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "query")
    private String query;
    @Column(name = "returns_result_set")
    private Boolean returnsResultSet;
    @Column(name = "params")
    private String params;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
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

        public static final String ID = "id";
        public static final String QUERY = "query";
        public static final String CODE = "code";
        public static final String STATE = "state";
    }
}
