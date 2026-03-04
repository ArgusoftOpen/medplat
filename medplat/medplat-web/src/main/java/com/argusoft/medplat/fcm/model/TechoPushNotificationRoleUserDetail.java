/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fcm.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nihar
 */
@Entity
@Table(name = "techo_push_notification_role_user_detail")
@Data
public class TechoPushNotificationRoleUserDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "push_config_id")
    private Integer pushConfigId;

    @Column(name = "role_id")
    private Integer roleId;

    public interface Fields {
        public static final String ID = "id";
        public static final String PUSH_CONFIG_ID = "pushConfigId";
        public static final String ROLE_ID = "roleId";
    }
}
