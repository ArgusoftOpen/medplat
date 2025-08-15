package com.argusoft.medplat.web.BackendController.entity;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import javax.persistence.*;

@Entity
@Table(name = "indicator_master")
public class IndicatorMaster extends EntityAuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "indicator_name", nullable = false)
    private String indicatorName;

    @Column(name = "description")
    private String description;

    @Column(name = "sql_query", nullable = false)
    private String sqlQuery;

    @Column(name = "query_result")
    private Integer queryResult;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public Integer getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(Integer queryResult) {
        this.queryResult = queryResult;
    }
}

