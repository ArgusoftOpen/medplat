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
@Table(name = "ncd_retinopathy_test_detail")
public class MemberRetinopathyTestDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "on_diabetes_treatment")
    private Boolean onDiabetesTreatment;

    @Column(name = "diabetes_treatment")
    private String diabetesTreatment;

    @Column(name = "past_eye_operation")
    private Boolean pastEyeOperation;

    @Column(name = "eye_operation_type")
    private String eyeOperationType;

    @Column(name = "vision_problem_flag")
    private Boolean visionProblemFlag;

    @Column(name = "vision_problem")
    private String visionProblem;

    @Column(name = "vision_problem_duration")
    private Integer visionProblemDuration;

    @Column(name = "absent_eyeball")
    private String absentEyeball;

    @Column(name = "retinopathy_test_flag")
    private Boolean retinopathyTestFlag;

    @Column(name = "remidio_id")
    private String remidioId;

    @Column(name = "right_eye_retinopathy_detected")
    private Boolean rightEyeRetinopathyDetected;

    @Column(name = "left_eye_retinopathy_detected")
    private Boolean leftEyeRetinopathyDetected;

}
