package com.argusoft.medplat.ncd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCbcDataBean {

    private Integer memberId;

    private Integer labId;

    private String wbc;

    private String rbc;

    private String hgb;

    private String hct;

    private String mcv;

    private String mch;

    private String mchc;

    private String plt;

    private String rdwCv;

    private String errorMessage;

    private String status;

}
