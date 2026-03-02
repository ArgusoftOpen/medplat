/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * <p>
 *     Define escalation_level_master entity and its fields.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "escalation_level_master")
public class EscalationLevelMaster extends EntityAuditInfo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "notification_type_id")
    private Integer notificationTypeId;
    
    @JoinColumn(name = "notification_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private NotificationTypeMaster notificationTypeMaster;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "escalation_level_role_rel",joinColumns=@JoinColumn(name = "escalation_level_id"))
    @MapKeyColumn(name = "role_id")
    @Column(name="can_perform_action")
    private Map<Integer, Boolean> rolesWithPerformAction;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "escalation_level_user_rel", joinColumns = @JoinColumn(name = "escalation_level_id"))
    @Column(name = "user_id")
    private Set<Integer> users;
    
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

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

    public Integer getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(Integer notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public NotificationTypeMaster getNotificationTypeMaster() {
        return notificationTypeMaster;
    }

    public void setNotificationTypeMaster(NotificationTypeMaster notificationTypeMaster) {
        this.notificationTypeMaster = notificationTypeMaster;
    }

    public Set<Integer> getUsers() {
        return users;
    }

    public void setUsers(Set<Integer> users) {
        this.users = users;
    }

    public Map<Integer, Boolean> getRolesWithPerformAction() {
        return rolesWithPerformAction;
    }

    public void setRolesWithPerformAction(Map<Integer, Boolean> rolesWithPerformAction) {
        this.rolesWithPerformAction = rolesWithPerformAction;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Define fields name for escalation_level_master entity.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String UUID = "uuid";
    }
    
    
}
