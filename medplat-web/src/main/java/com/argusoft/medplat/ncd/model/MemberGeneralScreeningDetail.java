/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ncd_general_screening")
public class MemberGeneralScreeningDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "service_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "duration_of_hypertension")
    private Integer durationOfHypertension;

    @Column(name = "duration_of_diabetes")
    private Integer durationOfDiabetes;

    @Column(name = "stroke_history")
    private Boolean strokeHistory;

    @Column(name = "stroke_duration_years")
    private Integer strokeDurationYears;

    @Column(name = "stroke_duration_months")
    private Integer strokeDurationMonths;

    @Column(name = "stroke_symptoms")
    private String strokeSymptoms;

    @Column(name = "foot_problem_history")
    private Boolean footProblemHistory;

    @Column(name = "foot_problem_cause")
    private String footProblemCause;

    @Column(name = "amputated_body_part")
    private String amputatedBodyPart;

    @Column(name = "image")
    private String image;

    @Column(name = "image_uuid")
    private String imageUuid;

    @Column(name = "had_cervical_cancer_test")
    private Boolean hadCervicalCancerTest;

    @Column(name = "had_breast_cancer_test")
    private Boolean hadBreastCancerTest;

    @Column(name = "had_oral_cancer_test")
    private Boolean hadOralCancerTest;

    public static class Fields {
        private Fields(){}
        public static final String IMAGE_UUID = "imageUuid";
    }
}
