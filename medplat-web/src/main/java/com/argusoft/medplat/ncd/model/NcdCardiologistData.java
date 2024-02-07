package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_cardiologist_data")
@Data
public class NcdCardiologistData extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;
    @Column(name = "case_confirmed")
    private Boolean caseConfirmed;
    @Column(name = "note")
    private String note;
    @Column(name = "satisfactory_image")
    private Boolean satisfactoryImage;
    @Column(name = "old_mi")
    private Integer oldMi;
    @Column(name = "lvh")
    private Integer lvh;
    @Column(name = "type")
    private String type;

    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }

        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String SCREENING_DATE = "screeningDate";
    }
}
