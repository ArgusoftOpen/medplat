package com.argusoft.medplat.analytics.parser;

import com.argusoft.medplat.analytics.plan.QueryPlan;

public interface QueryParser {
    QueryPlan parse(String input);
}