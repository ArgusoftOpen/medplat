package com.argusoft.medplat.analytics.controller;

import com.argusoft.medplat.analytics.builder.SqlBuilder;
import com.argusoft.medplat.analytics.dto.QueryPreviewResponse;
import com.argusoft.medplat.analytics.dto.QueryRequest;
import com.argusoft.medplat.analytics.parser.QueryParser;
import com.argusoft.medplat.analytics.plan.QueryPlan;
import com.argusoft.medplat.analytics.validator.QueryValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final QueryParser parser;
    private final SqlBuilder builder;
    private final QueryValidator validator;

    public AnalyticsController(QueryParser parser,
                               SqlBuilder builder,
                               QueryValidator validator) {
        this.parser = parser;
        this.builder = builder;
        this.validator = validator;
    }

    @PostMapping("/preview")
    public ResponseEntity<QueryPreviewResponse> preview(@RequestBody QueryRequest request) {

        QueryPlan plan = parser.parse(request.getQuery());
        validator.validate(plan);

        String sql = builder.build(plan);

        return ResponseEntity.ok(new QueryPreviewResponse(sql));
    }
}