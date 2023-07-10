package com.argusoft.medplat.fcm.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author nihar
 * @since 13/10/22 5:38 PM
 */
@Entity
@Table(name = "techo_push_notification_config_master")
@Data
public class TechoPushNotificationConfig extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "notification_type_id")
    private Integer notificationTypeId;

    @Column(name = "description")
    private String description;

    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType configType;

    @Column(name = "trigger_type")
    @Enumerated(EnumType.STRING)
    private TRIGGER_TYPE triggerType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "schedule_date_time")
    private Date scheduleDateTime;

    @Column(name = "query_uuid")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID queryUUID;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    public interface Fields {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String DESCRIPTION = "description";
        public static final String CONFIG_TYPE = "configType";
        public static final String TRIGGER_TYPE = "triggerType";
        public static final String SCHEDULE_DATE_TIME = "scheduleDateTime";
        public static final String STATE = "state";
    }

    public enum Status {
        NEW,
        SENT
    }

    public enum ConfigType {
        ROLE_LOCATION_BASED,
        QUERY_BASED
    }

    public enum TRIGGER_TYPE {
        IMMEDIATELY,
        SCHEDULE_TIME,
        TIMER
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }
}
