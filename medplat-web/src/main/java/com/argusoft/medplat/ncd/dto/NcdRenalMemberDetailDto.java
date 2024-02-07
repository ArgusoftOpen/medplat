package com.argusoft.medplat.ncd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class NcdRenalMemberDetailDto {
    private Integer memberId;
    private int userId;
    private Boolean isSCreatinineDone;
    @JsonProperty("sCreatinineValue")
    private Float sCreatinineValue;
    private Boolean isRenalComplicationPresent;
    private Date screeningDate;
}

