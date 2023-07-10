package com.argusoft.medplat.common.dto;

/**
 * <p>Defines fields related to constant field value</p>
 * @author shrey
 * @since 26/08/2020 5:30
 */
public class FieldValueMasterDto {

    private Integer id;

    private Integer fieldId;

    private String fieldValue;

    public FieldValueMasterDto() {
        // public constructor
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    
}
