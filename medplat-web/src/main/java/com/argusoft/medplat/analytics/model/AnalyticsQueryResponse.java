package com.argusoft.medplat.analytics.model;

public class AnalyticsQueryResponse {

    private String generatedSql;
    private String message;

    public AnalyticsQueryResponse(String generatedSql, String message) {
        this.generatedSql = generatedSql;
        this.message = message;
    }

    public String getGeneratedSql() {
        return generatedSql;
    }

    public String getMessage() {
        return message;
    }
}