package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class DrugInventoryDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer healthInfraId;
    private Integer appMonthly;
    private Integer parentHealthId;
    private Date createdOn;
    private Date issuedDate;
    private DoneBy doneBy;
    private Integer createdBy;
    private String medicineName;
    private Integer medicineId;
    private Integer quantityIssued;
    private Integer quantityReceived;
    private Integer balanceInHand;
    private Boolean flag;
    private Boolean isIssued;
    private Boolean isReceived;
    private Boolean isReturn;
}
