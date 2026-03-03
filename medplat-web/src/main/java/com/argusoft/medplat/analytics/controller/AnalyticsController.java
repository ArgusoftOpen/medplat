package com.argusoft.medplat.analytics.controller;

import com.argusoft.medplat.analytics.model.AnalyticsQueryRequest;
import com.argusoft.medplat.analytics.model.AnalyticsQueryResponse;
import com.argusoft.medplat.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService = new AnalyticsService();

    @PostMapping("/query")
    public AnalyticsQueryResponse generateQuery(@RequestBody AnalyticsQueryRequest request) {

        String sql = analyticsService.processQuery(request.getNaturalLanguageQuery());

        return new AnalyticsQueryResponse(sql, "Query generated successfully. Please review before execution.");
    }
}