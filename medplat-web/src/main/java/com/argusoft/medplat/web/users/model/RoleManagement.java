/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.IJoinEnum;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "role_management")
public class RoleManagement extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "managed_by_role_id")
    private Integer managedByRoleId;

    @Column(name = "managed_by_user_id")
    private Integer managedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RoleMaster roleMaster;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getManagedByRoleId() {
        return managedByRoleId;
    }

    public void setManagedByRoleId(Integer managedRoleId) {
        this.managedByRoleId = managedRoleId;
    }

    public Integer getManagedByUserId() {
        return managedByUserId;
    }

    public void setManagedByUserId(Integer managedUserId) {
        this.managedByUserId = managedUserId;
    }

    public RoleMaster getRoleMaster() {
        return roleMaster;
    }

    public void setRoleMaster(RoleMaster roleMaster) {
        this.roleMaster = roleMaster;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String ROLE_ID = "roleId";
        public static final String MANAGED_BY_ROLE_ID = "managedByRoleId";
        public static final String MANAGED_BY_USER_ID = "managedByUserId";
        public static final String STATE = "state";
        public static final String ROLE_MASTER = "roleMaster";
    }

    /**
     * Defines value of states
     */
    public enum State {

        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    /**
     * Defines join entity of role management
     */
    public enum RoleManagementJoin implements IJoinEnum {

        ROLE_MASTER(Fields.ROLE_MASTER,Fields.ROLE_MASTER, JoinType.LEFT);
        
        private String value;
        private String alias;
        private JoinType joinType;

        public String getValue() {
            return value;
        }

        public String getAlias() {
            return alias;
        }

        public JoinType getJoinType() {
            return joinType;
        }

        RoleManagementJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
