/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dto;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 *     Used for report parameter config.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class ReportParameterConfigDto {

    private Integer id;
    private String type;
    private String model;
    private String label;
    private Boolean isEditable = true;
    private String value;
    private String rptParameterName;
    private String rptDataType;
    private String query;
    private String availableOptions;
    private String implicitParameter;
    private String reportName;
    private Boolean isQuery;
    private Boolean isRequired;
    private List<Map<String, Object>> optionsByQuery;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRptParameterName() {
        return rptParameterName;
    }

    public void setRptParameterName(String rptParameterName) {
        this.rptParameterName = rptParameterName;
    }

    public String getRptDataType() {
        return rptDataType;
    }

    public void setRptDataType(String rptDataType) {
        this.rptDataType = rptDataType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAvailableOptions() {
        return availableOptions;
    }

    public void setAvailableOptions(String availableOptions) {
        this.availableOptions = availableOptions;
    }

    public String getImplicitParameter() {
        return implicitParameter;
    }

    public void setImplicitParameter(String implicitParameter) {
        this.implicitParameter = implicitParameter;
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

    public List<Map<String, Object>> getOptionsByQuery() {
        return optionsByQuery;
    }

    public void setOptionsByQuery(List<Map<String, Object>> optionsByQuery) {
        this.optionsByQuery = optionsByQuery;
    }

    

}
