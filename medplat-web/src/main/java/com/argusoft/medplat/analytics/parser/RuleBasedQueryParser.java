package com.argusoft.medplat.analytics.parser;

import com.argusoft.medplat.analytics.plan.QueryPlan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RuleBasedQueryParser implements QueryParser {
    @Override
    public QueryPlan parse(String input) {
        String normalized = input.toLowerCase();
        LocalDate today = LocalDate.now();

        QueryPlan plan = new QueryPlan();

        // Aggregation
        if (normalized.contains("count") || normalized.contains("number") ||
            normalized.contains("how many") || normalized.contains("total")) {
            plan.setAggregation("COUNT");
        }

        // Entity mapping (basic)
        if (normalized.contains("user") || normalized.contains("person")) {
            plan.setEntity("users");
        }

        //location
        Pattern statePattern = Pattern.compile("\\b(?:in|from)\\s+([a-zA-Z\\s]+?)(?=\\s+(age|today|yesterday|this|last|$))");
        Matcher stateMatcher = statePattern.matcher(normalized);

        if (stateMatcher.find()) {
            String state = stateMatcher.group(1).trim();

            // Convert to proper case
            StringBuilder properCase = new StringBuilder();
            for (String word : state.split("\\s+")) {
                if (!word.isEmpty()) {
                    properCase.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1))
                            .append(" ");
                }
            }

            plan.addFilter("state", properCase.toString().trim());
        }
        //age
        Pattern agePattern = Pattern.compile("age\\s*(greater than|less than|=)\\s*(\\d+)");
        Matcher matcher = agePattern.matcher(normalized);
        if (matcher.find()) {
            String op = matcher.group(1)
                    .replace("greater than", ">")
                    .replace("less than", "<");
            String value = matcher.group(2);
            plan.addFilter("age", op + " " + value);
        }

        //times
        Pattern timePattern = Pattern.compile("(today|yesterday|this|last)\\s*(week|month|year)?");
        Matcher timeMatcher = timePattern.matcher(normalized);
        if (timeMatcher.find()) {
            String keyword = timeMatcher.group(1); // today, yesterday, this, last
            String period = timeMatcher.group(2);  // week, month, year 

            LocalDate startDate = null;
            LocalDate endDate = null;

            switch (keyword) {
                case "today":
                    startDate = today;
                    endDate = today;
                    break;
                case "yesterday":
                    startDate = today.minusDays(1);
                    endDate = today.minusDays(1);
                    break;
                case "this":
                    if ("week".equals(period)) {
                        startDate = today.with(java.time.DayOfWeek.MONDAY);
                        endDate = today.with(java.time.DayOfWeek.SUNDAY);
                    } else if ("month".equals(period)) {
                        startDate = today.withDayOfMonth(1);
                        endDate = today.withDayOfMonth(today.lengthOfMonth());
                    } else if ("year".equals(period)) {
                        startDate = today.withDayOfYear(1);
                        endDate = today.withDayOfYear(today.lengthOfYear());
                    }
                    break;
                case "last":
                    if ("week".equals(period)) {
                        startDate = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                        endDate = startDate.plusDays(6);
                    } else if ("month".equals(period)) {
                        startDate = today.minusMonths(1).withDayOfMonth(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    } else if ("year".equals(period)) {
                        startDate = today.minusYears(1).withDayOfYear(1);
                        endDate = startDate.withDayOfYear(startDate.lengthOfYear());
                    }
                    break;
            }

            if (startDate != null && endDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                plan.addFilter("date", "BETWEEN '" + startDate.format(formatter) +
                                            "' AND '" + endDate.format(formatter) + "'");
            }
        }

        return plan;
    }
}