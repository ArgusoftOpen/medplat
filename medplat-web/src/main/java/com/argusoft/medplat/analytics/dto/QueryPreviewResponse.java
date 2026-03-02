package com.argusoft.medplat.analytics.dto;

public class QueryPreviewResponse {

    private String generatedSql;

    public QueryPreviewResponse(String generatedSql) {
        this.generatedSql = generatedSql;
    }

    public String getGeneratedSql() {
        return generatedSql;
    }
}