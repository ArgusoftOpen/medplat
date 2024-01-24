package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NcdCVCFormDto {
    private Integer id;
    private Integer memberId;
    private Date screeningDate;
    private Integer healthInfraId;
    private Boolean takeMedicine;
    private String treatementStatus;
    private DoneBy doneBy;
    private List<GeneralDetailMedicineDto> medicineDetail;
}
