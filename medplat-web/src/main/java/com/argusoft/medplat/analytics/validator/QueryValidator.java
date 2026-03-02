package com.argusoft.medplat.analytics.validator;

import com.argusoft.medplat.analytics.plan.QueryPlan;
import org.springframework.stereotype.Component;

@Component
public class QueryValidator {

    public void validate(QueryPlan plan) {

        if (plan.getEntity() == null) {
            throw new IllegalArgumentException("Could not determine entity from query.");
        }

        if (!"users".equals(plan.getEntity())) {
            throw new IllegalArgumentException("Only 'users' entity supported in MVP.");
        }
    }
}