package com.argusoft.medplat.analytics.service;

import com.argusoft.medplat.analytics.parser.NlpQueryParser;
import com.argusoft.medplat.analytics.validator.QueryValidator;

import java.util.Map;

public class AnalyticsService {

    private final NlpQueryParser parser = new NlpQueryParser();
    private final AnalyticsQueryBuilder queryBuilder = new AnalyticsQueryBuilder();
    private final QueryValidator validator = new QueryValidator();

    public String processQuery(String naturalLanguageQuery) {

        Map<String, String> parsedData = parser.parse(naturalLanguageQuery);

        String sql = queryBuilder.buildQuery(parsedData);

        validator.validate(sql);

        return sql;
    }
}