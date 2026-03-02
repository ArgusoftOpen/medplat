package com.argusoft.medplat.analytics.builder;

import com.argusoft.medplat.analytics.plan.QueryPlan;
import org.springframework.stereotype.Component;

@Component
public class SqlBuilder {

    public String build(QueryPlan plan) {

        if (!"users".equals(plan.getEntity())) {
            throw new IllegalArgumentException("Unsupported entity");
        }

        StringBuilder sql = new StringBuilder();

        if ("COUNT".equals(plan.getAggregation())) {
            sql.append("SELECT COUNT(*) ");
        } else {
            sql.append("SELECT * ");
        }

        sql.append("FROM users");

        if (!plan.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            plan.getFilters().forEach((k, v) -> {
                if ("state".equals(k)) {
                    sql.append("state = '").append(v).append("' ");
                }
            });
        }

        return sql.toString().trim();
    }
}