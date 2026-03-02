package com.argusoft.medplat.query.nlp.model;

public enum NlpOperator {
    EQ("="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    LIKE("like");

    private final String sqlOperator;

    NlpOperator(String sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public String getSqlOperator() {
        return sqlOperator;
    }
}
