package com.argusoft.medplat.systemconstraint.dto;

import java.util.UUID;

public class SystemConstraintMobileTemplateNextField {

    private String key;
    private String value;
    private UUID nextField;

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

    public UUID getNextField() {
        return nextField;
    }

    public void setNextField(UUID nextField) {
        this.nextField = nextField;
    }

}
