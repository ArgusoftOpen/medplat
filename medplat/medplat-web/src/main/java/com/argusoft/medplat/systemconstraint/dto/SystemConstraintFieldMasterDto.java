package com.argusoft.medplat.systemconstraint.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SystemConstraintFieldMasterDto {

    private UUID uuid;
    private UUID formMasterUuid;
    private String fieldKey;
    private String fieldName;
    private String fieldType;
    private String ngModel;
    private String appName;
    private UUID standardFieldMasterUuid;
    private Date createdOn;
    private Integer createdBy;
    private List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtos;
    private List<UUID> fieldValueUuidsToBeRemoved;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFormMasterUuid() {
        return formMasterUuid;
    }

    public void setFormMasterUuid(UUID formMasterUuid) {
        this.formMasterUuid = formMasterUuid;
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

    public UUID getStandardFieldMasterUuid() {
        return standardFieldMasterUuid;
    }

    public void setStandardFieldMasterUuid(UUID standardFieldMasterUuid) {
        this.standardFieldMasterUuid = standardFieldMasterUuid;
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

    public List<SystemConstraintFieldValueMasterDto> getSystemConstraintFieldValueMasterDtos() {
        return systemConstraintFieldValueMasterDtos;
    }

    public void setSystemConstraintFieldValueMasterDtos(List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtos) {
        this.systemConstraintFieldValueMasterDtos = systemConstraintFieldValueMasterDtos;
    }

    public List<UUID> getFieldValueUuidsToBeRemoved() {
        return fieldValueUuidsToBeRemoved;
    }

    public void setFieldValueUuidsToBeRemoved(List<UUID> fieldValueUuidsToBeRemoved) {
        this.fieldValueUuidsToBeRemoved = fieldValueUuidsToBeRemoved;
    }

    public String getNgModel() {
        return ngModel;
    }

    public void setNgModel(String ngModel) {
        this.ngModel = ngModel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
