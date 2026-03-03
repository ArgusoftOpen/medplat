package com.argusoft.medplat.analytics.parser;

import java.util.HashMap;
import java.util.Map;

public class NlpQueryParser {

    public Map<String, String> parse(String naturalQuery) {

        Map<String, String> parsedResult = new HashMap<>();

        naturalQuery = naturalQuery.toLowerCase();

        if (naturalQuery.contains("patients")) {
            parsedResult.put("table", "patients");
        }

        if (naturalQuery.contains("appointments")) {
            parsedResult.put("table", "appointments");
        }

        if (naturalQuery.contains("count")) {
            parsedResult.put("operation", "count");
        }

        if (naturalQuery.contains("today")) {
            parsedResult.put("condition", "date = CURRENT_DATE");
        }

        return parsedResult;
    }
}