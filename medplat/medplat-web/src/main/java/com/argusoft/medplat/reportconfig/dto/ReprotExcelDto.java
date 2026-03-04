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
 *     Used for report excel.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class ReprotExcelDto {

    private List<FilterObj> filterObj;
    private Map<String, String> paramObj;
    private String reportName;
    private Boolean isLandscape;
    private Integer numberOfColumnPerPage;
    private Integer numberOfRecordsPerPage;
    private List<String> referenceColumns;
    private Integer userId;  // This will required to generated offline
    private String fileType; // This will required to generated offline. It can be pdf or excel

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<FilterObj> getFilterObj() {
        return filterObj;
    }

    public void setFilterObj(List<FilterObj> filterObj) {
        this.filterObj = filterObj;
    }

    public Map<String, String> getParamObj() {
        return paramObj;
    }

    public void setParamObj(Map<String, String> paramObj) {
        this.paramObj = paramObj;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Boolean getIsLandscape() {
        return isLandscape;
    }

    public void setIsLandscape(Boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    public Integer getNumberOfColumnPerPage() {
        return numberOfColumnPerPage;
    }

    public void setNumberOfColumnPerPage(Integer numberOfColumnPerPage) {
        this.numberOfColumnPerPage = numberOfColumnPerPage;
    }

    public Integer getNumberOfRecordsPerPage() {
        return numberOfRecordsPerPage;
    }

    public void setNumberOfRecordsPerPage(Integer numberOfRecordsPerPage) {
        this.numberOfRecordsPerPage = numberOfRecordsPerPage;
    }

    public List<String> getReferenceColumns() {
        return referenceColumns;
    }

    public void setReferenceColumns(List<String> referenceColumns) {
        this.referenceColumns = referenceColumns;
    }
}
