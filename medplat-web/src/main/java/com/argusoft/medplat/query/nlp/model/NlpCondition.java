package com.argusoft.medplat.query.nlp.model;

public class NlpCondition {

    private String columnKey;
    private NlpOperator operator;
    private Object value;

    public NlpCondition(String columnKey, NlpOperator operator, Object value) {
        this.columnKey = columnKey;
        this.operator = operator;
        this.value = value;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public NlpOperator getOperator() {
        return operator;
    }

    public void setOperator(NlpOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
