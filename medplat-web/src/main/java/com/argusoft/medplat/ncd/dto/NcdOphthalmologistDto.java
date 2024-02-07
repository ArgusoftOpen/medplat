package com.argusoft.medplat.ncd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NcdOphthalmologistDto {
    private Integer id;
    private Integer memberId;
    private Date screeningDate;
    private String leftEyeFeedback;
    private String leftEyeOtherType;
    private String rightEyeFeedback;
    private String rightEyeOtherType;
}
