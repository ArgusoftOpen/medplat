package com.argusoft.medplat.reportconfig.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * A table to store report offline request
 * </p>
 *
 * @author sneha
 * @since 11-01-2021 01:45
 */
@Entity
@Table(name = "report_offline_details")
@Data
public class ReportOfflineDetails extends EntityAuditInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "report_parameters")
    private String reportParameters;

    @Column(name = "file_id")
    private Long fileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FILE_TYPE fileType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private STATE state;

    @Column(name = "error")
    private String error;

    @Column(name = "completed_on")
    private Date completedOn;

    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String USER_ID = "userId";
        public static final String STATUS = "status";
        public static final String STATE = "state";
        public static final String ERROR = "error";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String COMPLETED_ON = "completedOn";
        public static final String FILE_ID = "fileId";
    }

    public enum STATUS {
        REQUESTED,
        PROCESSING,
        READY_FOR_DOWNLOAD,
        ERROR,
        ARCHIVED
    }

    public enum STATE {
        ACTIVE,
        INACTIVE
    }

    public enum FILE_TYPE {
        PDF,
        EXCEL
    }
}