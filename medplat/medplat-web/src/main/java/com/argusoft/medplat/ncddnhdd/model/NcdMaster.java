package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_master")
@Data
public class NcdMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "disease_code")
    private String diseaseCode;

    @Column(name = "status")
    private String status;

    @Column(name = "sub_status")
    private String subStatus;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "last_visit_date")
    private Date lastVisitDate;
}
