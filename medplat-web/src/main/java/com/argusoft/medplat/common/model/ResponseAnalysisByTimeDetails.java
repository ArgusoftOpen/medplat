package com.argusoft.medplat.common.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Model for response analysis
 */
@Entity
@Table(name = "response_analysis_by_time_details")
@Data
public class ResponseAnalysisByTimeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "requested_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedTime;

    @Column(name = "requested_url")
    private String requestedUrl;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "request_param")
    private String requestParam;

    @Column(name = "time_taken_in_ms")
    private String timeTakenInMs;
}
