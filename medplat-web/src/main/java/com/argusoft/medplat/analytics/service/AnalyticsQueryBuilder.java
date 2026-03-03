package com.argusoft.medplat.analytics.service;

import java.util.Map;

public class AnalyticsQueryBuilder {

    public String buildQuery(Map<String, String> parsedData) {

        String table = parsedData.get("table");
        String operation = parsedData.get("operation");
        String condition = parsedData.get("condition");

        if (table == null) {
            throw new IllegalArgumentException("Unable to determine table from query.");
        }

        StringBuilder sql = new StringBuilder();

        if ("count".equals(operation)) {
            sql.append("SELECT COUNT(*) FROM ").append(table);
        } else {
            sql.append("SELECT * FROM ").append(table);
        }

        if (condition != null) {
            sql.append(" WHERE ").append(condition);
        }

        return sql.toString();
    }
}