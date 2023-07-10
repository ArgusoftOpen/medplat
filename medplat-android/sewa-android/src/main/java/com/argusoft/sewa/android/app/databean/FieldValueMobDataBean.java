/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * @author kelvin
 */
public class FieldValueMobDataBean {

    private int idOfValue;
    private String formCode;
    private String field;
    private String fieldType;
    private String value;
    private Long lastUpdateOfFieldValue;
    private Boolean isActive;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getLastUpdateOfFieldValue() {
        return lastUpdateOfFieldValue;
    }

    public void setLastUpdateOfFieldValue(Long lastUpdateOfFieldValue) {
        this.lastUpdateOfFieldValue = lastUpdateOfFieldValue;
    }

    public int getIdOfValue() {
        return idOfValue;
    }

    public void setIdOfValue(int idOfValue) {
        this.idOfValue = idOfValue;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @NonNull
    @Override
    public String toString() {
        return "FieldValueMobDataBean{" + "idOfValue=" + idOfValue + ", formCode=" + formCode + ", field=" + field + ", fieldType=" + fieldType + ", value=" + value + '}';
    }

}
