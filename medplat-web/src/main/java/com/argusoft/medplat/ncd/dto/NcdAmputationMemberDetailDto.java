package com.argusoft.medplat.ncd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NcdAmputationMemberDetailDto {
    private Integer memberId;
    private Integer userId;
    private boolean amputationPresent;
    private Date screeningDate;
}
