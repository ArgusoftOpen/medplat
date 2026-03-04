/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import com.argusoft.medplat.web.users.model.RoleMaster;

import javax.persistence.*;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "role_health_infrastructure_type")
public class RoleHealthInfrastructure  extends EntityAuditInfo implements Serializable{
      
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="role_id")
    private Integer roleId;
    
    @Column(name="health_infrastructure__type_id")
    private Integer healthInfrastructureTypeId;
  
    @Column(name="state")
    @Enumerated(EnumType.STRING)
    private State state;
    
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RoleMaster roleMaster;
    
    public enum State{
        ACTIVE,INACTIVE
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getHealthInfrastructureTypeId() {
        return healthInfrastructureTypeId;
    }

    public void setHealthInfrastructureTypeId(Integer healthInfrastructureTypeId) {
        this.healthInfrastructureTypeId = healthInfrastructureTypeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public RoleMaster getRoleMaster() {
        return roleMaster;
    }

    public void setRoleMaster(RoleMaster roleMaster) {
        this.roleMaster = roleMaster;
    }
    
    
    
}
