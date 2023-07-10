package com.argusoft.medplat.fcm.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nihar
 * @since 02/08/22 2:55 PM
 */
@Entity
@Table(name = "techo_push_notification_type")
@Data
public class TechoPushNotificationType extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "message")
    private String message;

    @Column(name = "heading")
    private String heading;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "media_id")
    private Integer mediaId;

    public static class Fields {

        private Fields() {

        }

        public static final String TYPE = "type";
        public static final String STATE = "state";
        public static final String MEDIA_ID = "mediaId";

    }
}
