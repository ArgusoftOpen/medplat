package com.argusoft.medplat.analytics.validator;

public class QueryValidator {

    public void validate(String sql) {

        if (sql.contains("DROP") || sql.contains("DELETE") || sql.contains("UPDATE")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed.");
        }

        if (!sql.trim().toUpperCase().startsWith("SELECT")) {
            throw new IllegalArgumentException("Invalid query generated.");
        }
    }
}