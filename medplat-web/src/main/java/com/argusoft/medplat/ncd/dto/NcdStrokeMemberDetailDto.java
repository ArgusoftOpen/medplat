package com.argusoft.medplat.ncd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NcdStrokeMemberDetailDto {
    private Integer memberId;
    private Integer userId;
    private boolean strokePresent;
    private Date screeningDate;
}
