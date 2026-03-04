/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * Define anganwadi_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "anganwadi_master")
public class Anganwadi extends EntityAuditInfo implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "parent")
    private Integer parent;
    @Column
    private String type;
    @Column
    private String state;
    @Column(name = "emamta_id")
    private String emamtaId;
    @Column(name = "alias_name")
    private String aliasName;
    @Column(name = "icds_code")
    private String icdsCode;

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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmamtaId() {
        return emamtaId;
    }

    public void setEmamtaId(String emamtaId) {
        this.emamtaId = emamtaId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getIcdsCode() {
        return icdsCode;
    }

    public void setIcdsCode(String icdsCode) {
        this.icdsCode = icdsCode;
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PARENT = "parent";
        public static final String CREATED_ON = "createdOn";
        public static final String TYPE = "type";
        public static final String STATE = "state";
        public static final String ALIAS_NAME = "aliasName";
        public static final String ICDS_CODE = "icdsCode";
    }
}
