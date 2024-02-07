package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_opthamologist_data")
@Data
public class NcdOphthalmologistData extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;
    @Column(name = "lefteye_feedback")
    private String leftEyeFeedback;
    @Column(name = "lefteye_other_type")
    private String leftEyeOtherType;
    @Column(name = "righteye_feedback")
    private String rightEyeFeedback;
    @Column(name = "righteye_other_type")
    private String rightEyeOtherType;

    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String SCREENING_DATE = "screeningDate";
    }
}
