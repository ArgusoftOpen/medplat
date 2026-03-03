package com.argusoft.medplat.query.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NlpQueryService {

    private static final Map<String, String> TABLE_MAP = Map.of(
            "patients", "patients",
            "users", "users"
    );

    private static final Set<String> ALLOWED_COLUMNS = Set.of(
            "id", "name", "age", "gender"
    );

    public String buildQuery(String input) {

        input = input.toLowerCase();

        if (!input.startsWith("show")) {
            throw new IllegalArgumentException("Only SELECT queries supported.");
        }

        String table = null;
        for (String key : TABLE_MAP.keySet()) {
            if (input.contains(key)) {
                table = TABLE_MAP.get(key);
                break;
            }
        }

        if (table == null) {
            throw new IllegalArgumentException("No valid table found.");
        }

        String query = "SELECT * FROM " + table;

        if (input.contains("where")) {
            String conditionPart = input.split("where")[1].trim();

            String[] tokens = conditionPart.split(" ");
            if (tokens.length >= 3) {
                String column = tokens[0];
                String operator = tokens[1];
                String value = tokens[2];

                if (!ALLOWED_COLUMNS.contains(column)) {
                    throw new IllegalArgumentException("Invalid column.");
                }

                query += " WHERE " + column + " " + operator + " ?";
            }
        }

        return query;
    }
}