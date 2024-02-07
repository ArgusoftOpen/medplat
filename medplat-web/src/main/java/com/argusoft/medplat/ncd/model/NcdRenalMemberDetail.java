/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.CustomLog;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ncd_renal_member_detail")
public class NcdRenalMemberDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "screening_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;

    @Column(name = "is_s_creatinine_done")
    private Boolean isSCreatinineDone;

    @Column(name = "s_creatinine_value", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float sCreatinineValue;

    @Column(name = "is_renal_complication_present")
    private Boolean isRenalComplicationPresent;
}
