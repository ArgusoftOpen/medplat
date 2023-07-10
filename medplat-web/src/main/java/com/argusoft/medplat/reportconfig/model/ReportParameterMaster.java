/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * <p>
 *     Define report_parameter_master entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "report_parameter_master")
public class ReportParameterMaster extends EntityAuditInfo implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;   

    @Size(max = 200)
    @Column(name = "label")
    private String label;

    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @Size(max = 200)
    @Column(name = "type")
    private String type;

    @Size(max = 2147483647)
    @Column(name = "options")
    private String options;

    @Size(max = 200)
    @Column(name = "implicit_parameter")
    private String implicitParameter;

    @Size(max = 200)
    @Column(name = "default_value")
    private String defaultValue;

    @Size(max = 2147483647)
    @Column(name = "query")
    private String query;

    @JoinColumn(name = "report_master_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReportMaster reportMasterId;

    @Column(name = "report_master_id", insertable = false, updatable = false)
    private Integer reportId;

    @Size(max = 200)
    @Column(name = "rpt_data_type")
    private String rptDataType;

    @Size(max = 500)
    @Column(name = "report_name")
    private String reportName;

    @Column(name = "is_query")
    private Boolean isQuery;

    @Column(name = "is_required")
    private Boolean isRequired;

    public Integer getId() {
        return id;
    }    
    
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getImplicitParameter() {
        return implicitParameter;
    }

    public void setImplicitParameter(String implicitParameter) {
        this.implicitParameter = implicitParameter;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ReportMaster getReportMasterId() {
        return reportMasterId;
    }

    public void setReportMasterId(ReportMaster reportMasterId) {
        this.reportMasterId = reportMasterId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getRptDataType() {
        return rptDataType;
    }

    public void setRptDataType(String rptDataType) {
        this.rptDataType = rptDataType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Boolean getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(Boolean isQuery) {
        this.isQuery = isQuery;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    

    public ReportParameterMaster() {
    }

    public ReportParameterMaster(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportParameterMaster)) {
            return false;
        }
        ReportParameterMaster other = (ReportParameterMaster) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "ReportParameterMaster[ id=" + id + " ]";
    }

}
