package com.argusoft.medplat.common.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Model for query analysis
 */
@Entity
@Table(name = "query_analysis_details")
@Data
public class QueryAnalysisDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "execution_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionTime;

    @Column(name = "query_string")
    private String queryString;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "total_rows")
    private Integer totalRows;

}
