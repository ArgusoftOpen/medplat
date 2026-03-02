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
@Table(name = "techo_push_notification_location_detail")
@Data
public class TechoPushNotificationLocationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "push_config_id")
    private Integer pushConfigId;

    @Column(name = "location_id")
    private Integer locationId;

    public interface Fields {
        public static final String ID = "id";
        public static final String PUSH_CONFIG_ID = "pushConfigId";
        public static final String LOCATION_ID = "locationId";
    }
}
