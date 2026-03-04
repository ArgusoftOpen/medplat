/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dto;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.dto.MenuGroupDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * <p>
 *     Used for report config.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class ReportConfigDto {

    private Integer id;
    private String name;
    private String fileName;
    private MenuConfigDto menuConfig;
    private Integer parentGroupId;
    private Integer subGroupId;
    private MenuGroupDto parentGroup;
    private MenuGroupDto subGroup;
    private String reportType;
    private String menuType;
    private List<ReportParameterConfigDto> parameterDtos;
    private List<Map<String, String>> fileList;
    private JsonNode configJson;
    private Integer createdBy;
    private Date createdOn;
    private String code;
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MenuConfigDto getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(MenuConfigDto menuConfig) {
        this.menuConfig = menuConfig;
    }

    public Integer getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(Integer parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public Integer getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(Integer subGroupId) {
        this.subGroupId = subGroupId;
    }

    public MenuGroupDto getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(MenuGroupDto parentGroup) {
        this.parentGroup = parentGroup;
    }

    public MenuGroupDto getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(MenuGroupDto subGroup) {
        this.subGroup = subGroup;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public List<ReportParameterConfigDto> getParameterDtos() {
        return parameterDtos;
    }

    public void setParameterDtos(List<ReportParameterConfigDto> parameterDtos) {
        this.parameterDtos = parameterDtos;
    }

    public List<Map<String, String>> getFileList() {
        return fileList;
    }

    public void setFileList(List<Map<String, String>> fileList) {
        this.fileList = fileList;
    }

    public JsonNode getConfigJson() {
        return configJson;
    }

    public void setConfigJson(JsonNode configJson) {
        this.configJson = configJson;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}