/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>Defines fields related to user</p>
 *
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "um_role_master")
public class RoleMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "description", length = 500)
    private String description;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "code", length = 255)
    private String code;
    @Column(name = "max_position", length = 255)
    private Integer maxPosition;

    @Column(name = "max_health_infra")
    private Integer maxHealthInfra;

    @OneToMany(mappedBy = "roleId", fetch = FetchType.LAZY)
    private List<UserMaster> userList;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 255)
    private State state;
    @Column(name = "is_last_name_mandatory")
    private Boolean isLastNameMandatory;
    @Column(name = "is_email_mandatory")
    private Boolean isEmailMandatory;
    @Column(name = "is_contact_num_mandatory")
    private Boolean isContactNumMandatory;
    @Column(name = "is_aadhar_num_mandatory")
    private Boolean isAadharNumMandatory;

    @Column(name = "is_convox_id_mandatory")
    private Boolean isConvoxIdMandatory;

    @Column(name = "role_type")
    private String roleType;

    @Column(name = "is_health_infra_mandatory")
    private Boolean isHealthInfraMandatory;

    @Column(name = "is_geolocation_mandatory")
    private Boolean isGeolocationMandatory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserMaster> getUserList() {
        return userList;
    }

    public void setUserList(List<UserMaster> userList) {
        this.userList = userList;
    }

    public Integer getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
    }

    public Integer getMaxHealthInfra() {
        return maxHealthInfra;
    }

    public void setMaxHealthInfra(Integer maxHealthInfra) {
        this.maxHealthInfra = maxHealthInfra;
    }

    public Boolean getIsLastNameMandatory() {
        return isLastNameMandatory;
    }

    public void setIsLastNameMandatory(Boolean isLastNameMandatory) {
        this.isLastNameMandatory = isLastNameMandatory;
    }

    public Boolean getIsEmailMandatory() {
        return isEmailMandatory;
    }

    public void setIsEmailMandatory(Boolean isEmailMandatory) {
        this.isEmailMandatory = isEmailMandatory;
    }

    public Boolean getIsContactNumMandatory() {
        return isContactNumMandatory;
    }

    public void setIsContactNumMandatory(Boolean isContactNumMandatory) {
        this.isContactNumMandatory = isContactNumMandatory;
    }

    public Boolean getIsAadharNumMandatory() {
        return isAadharNumMandatory;
    }

    public void setIsAadharNumMandatory(Boolean isAadharNumMandatory) {
        this.isAadharNumMandatory = isAadharNumMandatory;
    }

    public Boolean getIsConvoxIdMandatory() {
        return isConvoxIdMandatory;
    }

    public void setIsConvoxIdMandatory(Boolean isConvoxIdMandatory) {
        this.isConvoxIdMandatory = isConvoxIdMandatory;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Boolean getIsHealthInfraMandatory() {
        return isHealthInfraMandatory;
    }

    public void setIsHealthInfraMandatory(Boolean healthInfraMandatory) {
        isHealthInfraMandatory = healthInfraMandatory;
    }

    public Boolean getIsGeolocationMandatory() {
        return isGeolocationMandatory;
    }

    public void setIsGeolocationMandatory(Boolean geolocationMandatory) {
        isGeolocationMandatory = geolocationMandatory;
    }

    /**
     * Defines value of states
     */
    public enum State {

        NEW,
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {

        }

        public static final String ID = "id";
        public static final String DESCRIPTION = "description";
        public static final String NAME = "name";
        public static final String CODE = "code";
        public static final String MAX_POSITION = "maxPosition";
        public static final String USER_LIST = "userList";
        public static final String STATE = "state";
    }
}
