package com.argusoft.medplat.analytics;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @PostMapping("/query")
    public String generateQuery(@RequestBody String input) {
        return NLPParser.parse(input);
    }
}