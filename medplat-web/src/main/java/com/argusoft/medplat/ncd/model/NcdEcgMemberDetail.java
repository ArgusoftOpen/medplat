/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ncd_ecg_member_detail")
public class NcdEcgMemberDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "satisfactory_image")
    private Boolean satisfactoryImage;

    @Column(name = "screening_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;

    @Column(name = "old_mi")
    private Integer oldMi;

    @Column(name = "lvh")
    private Integer lvh;

    @Column(name = "type")
    private String type;


}
