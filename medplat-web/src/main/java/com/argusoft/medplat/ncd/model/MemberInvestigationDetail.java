package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="ncd_member_investigation_detail")
@Data
public class MemberInvestigationDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;

    @Column(name = "done_by", length = 50)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "report")
    private String report;

    @Column(name = "type")
    private String type;

    /**
     * Define fields name for ncd_member_investigation_detail.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String SCREENING_DATE = "screeningDate";
        public static final String DONE_BY = "doneBy";
    }
}
