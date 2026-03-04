package com.argusoft.medplat.systemconstraint.dto;

import java.util.Date;
import java.util.UUID;

public class SystemConstraintFieldValueMasterDto {

    private UUID uuid;
    private UUID fieldMasterUuid;
    private String valueType;
    private String key;
    private String value;
    private String defaultValue;
    private String enTranslationOfLabel;
    private String enTranslationOfDefaultLabel;
    private Date createdOn;
    private Integer createdBy;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFieldMasterUuid() {
        return fieldMasterUuid;
    }

    public void setFieldMasterUuid(UUID fieldMasterUuid) {
        this.fieldMasterUuid = fieldMasterUuid;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEnTranslationOfLabel() {
        return enTranslationOfLabel;
    }

    public void setEnTranslationOfLabel(String enTranslationOfLabel) {
        this.enTranslationOfLabel = enTranslationOfLabel;
    }

    public String getEnTranslationOfDefaultLabel() {
        return enTranslationOfDefaultLabel;
    }

    public void setEnTranslationOfDefaultLabel(String enTranslationOfDefaultLabel) {
        this.enTranslationOfDefaultLabel = enTranslationOfDefaultLabel;
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
}
