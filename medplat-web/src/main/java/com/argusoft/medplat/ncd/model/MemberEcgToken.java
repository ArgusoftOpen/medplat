package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ecg_token_metadata")
public class MemberEcgToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "token")
    private String token;

    @Column(name = "test_failure_point")
    private Integer testFailurePoint;

    @Column(name = "generated_data_points")
    private String generatedDataPoints;

    @Column(name = "ecg_position")
    private String ecgPosition;

    @Column(name = "created_on")
    private Date createdOn;
}
