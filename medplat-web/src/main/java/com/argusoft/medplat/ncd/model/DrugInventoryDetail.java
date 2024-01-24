package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DoneBy;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_drug_inventory_detail")
@Data
public class DrugInventoryDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "balance_in_hand")
    private Integer balanceInHand;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "is_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedDate;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "parent_health_id")
    private Integer parentHealthId;

    @Column(name = "medicine_id")
    private Integer medicineId;

    @Column(name = "quantity_received")
    private Integer quantityReceived;

    @Column(name = "quantity_issued")
    private Integer quantityIssued;

    @Column(name = "is_issued")
    private Boolean isIssued;

    @Column(name = "is_received")
    private Boolean isReceived;

    @Column(name = "is_return")
    private Boolean isReturn;

}
