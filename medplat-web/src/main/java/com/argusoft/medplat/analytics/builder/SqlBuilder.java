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

        // Aggregation
        if ("COUNT".equals(plan.getAggregation())) {
            sql.append("SELECT COUNT(*) ");
        } else {
            sql.append("SELECT * ");
        }

        sql.append("FROM users");

        // Filters
        if (!plan.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            boolean first = true;
            for (var entry : plan.getFilters().entrySet()) {
                if (!first) sql.append(" AND ");
                first = false;

                String column = entry.getKey();
                Object value = entry.getValue();

                switch (column) {
                    case "state":
                        sql.append(column).append(" = '").append(value).append("'");
                        break;
                    case "age":
                        
                        sql.append("age ").append(value);
                        break;
                    case "date":
                       
                        sql.append("date ").append(value);
                        break;
                    default:
                        // fallback for any future 
                        sql.append(column).append(" = '").append(value).append("'");
                        break;
                }
            }
        }

        return sql.toString().trim();
    }
}