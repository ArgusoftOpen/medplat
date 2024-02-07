package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberInvestigationDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MemberInvestigationDto {

    private Integer id;
    private Integer memberId;
    private Date screeningDate;
    private List<MemberInvestigationDetail> reports;
    private Integer healthInfraId;
    private DoneBy doneBy;
}
