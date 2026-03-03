package com.argusoft.medplat.query.controller;

import com.argusoft.medplat.query.service.NlpQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nlp")
public class NlpQueryController {

    @Autowired
    private NlpQueryService nlpQueryService;

    @PostMapping("/preview")
public Map<String, String> previewQuery(@RequestBody Map<String, String> request) {

    try {
        String input = request.get("query");
        String sql = nlpQueryService.buildQuery(input);
        return Map.of("previewSql", sql);
    } catch (Exception e) {
        return Map.of("error", e.getMessage());
    }
}