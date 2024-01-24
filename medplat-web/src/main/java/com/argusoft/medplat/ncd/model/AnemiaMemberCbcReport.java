package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "anemia_member_cbc_report")
@Data
public class AnemiaMemberCbcReport extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "lab_id")
    private Integer labId;

    @Column(name = "wbc_count")
    private String wbc_count;

    @Column(name = "rbc_count")
    private String rbc_count;

    @Column(name = "hgb_count")
    private String hgb_count;

    @Column(name = "hct_count")
    private String hct_count;

    @Column(name = "mcv_count")
    private String mcv_count;

    @Column(name = "mch_count")
    private String mch_count;

    @Column(name = "mchc_count")
    private String mchc_count;

    @Column(name = "plt_count")
    private String plt_count;

    @Column(name = "rdw_cv")
    private String rdw_cv;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

}
