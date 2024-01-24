package com.argusoft.medplat.ncd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NcdEcgMemberDetailDto {
    private Integer memberId;
    private Integer userId;
    private Boolean needsRetake;
    private Integer oldMI;
    private Integer lvh;
    private String type;
    private Date screeningDate;
}
