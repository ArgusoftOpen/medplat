/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * <p>
 *     Used for escalation level.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public class EscalationLevelMasterDto {

    private Integer id;
    private String name;
    private Integer notificationTypeId;
    private Set<Integer> roles;
    private Map<Integer, Boolean> performAction;
    private UUID uuid;
    private Boolean isFromOtherServer;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Boolean getIsFromOtherServer() {
        return isFromOtherServer;
    }

    public void setIsFromOtherServer(Boolean isFromOtherServer) {
        this.isFromOtherServer = isFromOtherServer;
    }

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

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public Map<Integer, Boolean> getPerformAction() {
        return performAction;
    }

    public void setPerformAction(Map<Integer, Boolean> performAction) {
        this.performAction = performAction;
    }

    @Override
    public String toString() {
        return "EscalationLevelMasterDto{" + "id=" + id + ", name=" + name + ", notificationTypeId=" + notificationTypeId + ", roles=" + roles + '}';
    }

}
