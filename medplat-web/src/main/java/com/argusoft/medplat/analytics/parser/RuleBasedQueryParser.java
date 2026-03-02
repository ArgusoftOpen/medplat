package com.argusoft.medplat.analytics.parser;

import com.argusoft.medplat.analytics.plan.QueryPlan;
import org.springframework.stereotype.Component;

@Component
public class RuleBasedQueryParser implements QueryParser {
    @Override
    public QueryPlan parse(String input) {
        String normalized = input.toLowerCase();

        QueryPlan plan = new QueryPlan();

        // Aggregation
        if (normalized.contains("count") || normalized.contains("number")) {
            plan.setAggregation("COUNT");
        }

        // Entity mapping (basic)
        if (normalized.contains("user")) {
            plan.setEntity("users");
        }

        // Dynamic location filter
        if (normalized.contains(" in ")) {

            String[] parts = normalized.split(" in ");

            if (parts.length > 1) {
                String stateRaw = parts[1].trim();
                String[] words = stateRaw.split(" ");
                StringBuilder properCase = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        properCase.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1))
                                .append(" ");
                    }
                }
                plan.addFilter("state", properCase.toString().trim());
            }
        }

        return plan;

    }
}