package com.argusoft.medplat.web.ddb.dto;

import java.util.Date;

/**
 * Defines fields for derived attribute dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
public class DerivedAttributeDto {

    private Integer id;
    private String derivedName;
    private String formula;
    private Double result;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDerivedName() { return derivedName; }
    public void setDerivedName(String derivedName) { this.derivedName = derivedName; }

    public String getFormula() { return formula; }
    public void setFormula(String formula) { this.formula = formula; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    public Integer getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(Integer modifiedBy) { this.modifiedBy = modifiedBy; }

    public Date getModifiedOn() { return modifiedOn; }
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }
}
