package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_visit_history")
@Data
public class NcdVisitHistory extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "visit_date")
    private Date visitDate;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "disease_code", length = 50)
    @Enumerated(EnumType.STRING)
    private DiseaseCode diseaseCode;

    @Column(name = "status")
    private String status;

    @Column(name = "visit_by", length = 50)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy visitBy;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "master_id")
    private Integer masterId;

    @Column(name = "reading")
    private String reading;
}
