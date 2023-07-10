package com.argusoft.medplat.systemconstraint.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SystemConstraintStandardFieldMasterDto {

    private UUID uuid;
    private String fieldKey;
    private String fieldName;
    private String fieldType;
    private String appName;
    private Integer categoryId;
    private String categoryName;
    private UUID standardFieldMappingMasterUuid;
    private String state;
    private Date createdOn;
    private Integer createdBy;
    private List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtos;
    private List<UUID> standardFieldValueUuidsToBeRemoved;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UUID getStandardFieldMappingMasterUuid() {
        return standardFieldMappingMasterUuid;
    }

    public void setStandardFieldMappingMasterUuid(UUID standardFieldMappingMasterUuid) {
        this.standardFieldMappingMasterUuid = standardFieldMappingMasterUuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public List<SystemConstraintStandardFieldValueMasterDto> getSystemConstraintStandardFieldValueMasterDtos() {
        return systemConstraintStandardFieldValueMasterDtos;
    }

    public void setSystemConstraintStandardFieldValueMasterDtos(List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtos) {
        this.systemConstraintStandardFieldValueMasterDtos = systemConstraintStandardFieldValueMasterDtos;
    }

    public List<UUID> getStandardFieldValueUuidsToBeRemoved() {
        return standardFieldValueUuidsToBeRemoved;
    }

    public void setStandardFieldValueUuidsToBeRemoved(List<UUID> standardFieldValueUuidsToBeRemoved) {
        this.standardFieldValueUuidsToBeRemoved = standardFieldValueUuidsToBeRemoved;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
