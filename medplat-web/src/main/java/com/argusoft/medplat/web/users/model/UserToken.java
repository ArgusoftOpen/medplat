/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author prateek
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "user_token")
@NamedQuery(name = "UserToken.findAll", query = "SELECT u FROM UserToken u")
public class UserToken implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_token")
    private String userToken;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "is_archieve")
    private boolean isArchieve;

    public UserToken() {
    }

    public UserToken(Integer userId) {
        this.userId = userId;
    }

    public UserToken(Integer userId, boolean isActive, boolean isArchieve) {
        this.userId = userId;
        this.isActive = isActive;
        this.isArchieve = isArchieve;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsArchieve() {
        return isArchieve;
    }

    public void setIsArchieve(boolean isArchieve) {
        this.isArchieve = isArchieve;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserToken)) {
            return false;
        }
        UserToken other = (UserToken) object;
        return !((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "com.argusoft.medplat.web.users.model.UserToken[ userId=" + userId + " ]";
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String USER_ID = "userId";
        public static final String USER_TOKEN = "userToken";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIEVE = "isArchieve";
        public static final String ID = "id";
    }
}
