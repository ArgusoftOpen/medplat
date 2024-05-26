/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 * @author vaishali
 */
@Getter
@Setter
public class MemberReferralDnhddDto {
    private Integer id;
    private String uniqueHealthId;
    private String familyId;
    private String referredForDiseases;
    private String referredForHypertension;
    private String referredForDiabetes;
    private String referredForBreast;
    private String referredForOral;
    private String referredForCervical;
    private String name;
    private Integer locationId;
    private String locationHierarchy;
    private String locationName;
    private String mobileNumber;
    private Date referredDate;
    private Date dob;
    private String gender;
    private Long hmisId;
}
