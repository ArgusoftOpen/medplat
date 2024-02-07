package com.argusoft.medplat.ncd.dto;

import lombok.Data;


@Data
public class NcdMasterDto {

    private Integer id;
    private Integer memberId;
    private Integer healthInfraId;
    private Integer locationId;
    private String diseaseCode;
    private String status;
    private String subStatus;
    private Boolean active;

}
