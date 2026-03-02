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

        //  location filter
        if (normalized.contains("gujarat")) {
            plan.addFilter("state", "Gujarat");
        }

        return plan;
    }
}