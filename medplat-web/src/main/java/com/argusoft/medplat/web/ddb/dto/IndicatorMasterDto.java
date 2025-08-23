package com.argusoft.medplat.web.ddb.dto;

import java.util.Date;

/**
 * Defines fields for indicator master dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
public class IndicatorMasterDto {

    private Integer id;
    private String indicatorName;
    private String description;
    private String sqlQuery;
    private Integer queryResult;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSqlQuery() { return sqlQuery; }
    public void setSqlQuery(String sqlQuery) { this.sqlQuery = sqlQuery; }

    public Integer getQueryResult() { return queryResult; }
    public void setQueryResult(Integer queryResult) { this.queryResult = queryResult; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    public Integer getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(Integer modifiedBy) { this.modifiedBy = modifiedBy; }

    public Date getModifiedOn() { return modifiedOn; }
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }
}
