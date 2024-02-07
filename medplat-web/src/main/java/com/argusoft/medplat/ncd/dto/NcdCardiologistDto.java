package com.argusoft.medplat.ncd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NcdCardiologistDto {

    private Integer id;
    private Integer memberId;
    private Date screeningDate;
    private Boolean caseConfirmed;
    private String note;
    private Boolean satisfactoryImage;
    private Integer oldMI;
    private Integer lvh;
    private String type;
}
