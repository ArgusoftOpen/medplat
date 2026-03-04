/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.IJoinEnum;
import com.argusoft.medplat.web.location.model.LocationMaster;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "um_user_location")
public class UserLocation extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "type", length = 255)
    private String type;

    @Basic(optional = false)
    @Column(name = "level", length = 255)
    private Integer level;

    @Basic(optional = false)
    @Column(name = "hierarchy_type")
    private String hierarchyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "loc_id")
    private Integer locationId;

    @JoinColumn(name = "loc_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationMaster locationMaster;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public LocationMaster getLocationMaster() {
        return locationMaster;
    }

    public void setLocationMaster(LocationMaster locationMaster) {
        this.locationMaster = locationMaster;
    }

    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String LEVEL = "level";
        public static final String HIERARCHY_TYPE = "hierarchyType";
        public static final String STATE = "state";
        public static final String USER_ID = "userId";
        public static final String LOCATION_ID = "locationId";
        public static final String LOCATION_MASTER = "locationMaster";
    }

    /**
     * Defines value of states
     */
    public enum State {
        ACTIVE,
        INACTIVE
    }

    /**
     * Defines join entity of user location
     */
    public enum UserLocationJoin implements IJoinEnum {

        LOCATION_MASTER(Fields.LOCATION_MASTER, Fields.LOCATION_MASTER, JoinType.LEFT);
        
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

        UserLocationJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
