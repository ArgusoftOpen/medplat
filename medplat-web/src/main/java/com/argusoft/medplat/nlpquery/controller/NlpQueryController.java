package com.argusoft.medplat.nlpquery.controller;

import com.argusoft.medplat.nlpquery.dto.NlpQueryConfirmRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryResponse;
import com.argusoft.medplat.nlpquery.service.NlpQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the NLP Query Builder.
 * Provides endpoints for parsing, previewing, and executing natural language queries.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>POST /api/nlp-query/parse - Parse and preview a query</li>
 *   <li>POST /api/nlp-query/execute - Execute a confirmed query</li>
 *   <li>POST /api/nlp-query/parse-and-execute - Parse and execute in one step</li>
 *   <li>GET  /api/nlp-query/examples - Get example queries</li>
 *   <li>GET  /api/nlp-query/schema - Get available schema info</li>
 * </ul>
 * </p>
 *
 * @author medplat
 * @since 02/03/2026
 */
@RestController
@RequestMapping("/api/nlp-query")
@Tag(name = "NLP Query Builder", description = "Natural Language Query Processing and Analytics")
public class NlpQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NlpQueryController.class);

    @Autowired
    private NlpQueryService nlpQueryService;

    /**
     * Parse a natural language query and return a SQL preview.
     * The generated SQL is NOT executed - it's returned for user review.
     *
     * @param request The NLP query request
     * @return Response with generated SQL and preview information
     */
    @PostMapping("/parse")
    @Operation(summary = "Parse natural language query and preview generated SQL")
    public ResponseEntity<NlpQueryResponse> parseQuery(@RequestBody NlpQueryRequest request) {
        LOGGER.info("Received NLP parse request: {}", request.getNaturalLanguageQuery());

        NlpQueryResponse response = nlpQueryService.parseAndPreview(request);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Execute a previously previewed and confirmed SQL query.
     *
     * @param confirmRequest The confirmed query request
     * @return Response with query results
     */
    @PostMapping("/execute")
    @Operation(summary = "Execute a confirmed SQL query after preview")
    public ResponseEntity<NlpQueryResponse> executeQuery(@RequestBody NlpQueryConfirmRequest confirmRequest) {
        LOGGER.info("Received NLP execute request for query: {}", confirmRequest.getOriginalQuery());

        NlpQueryResponse response = nlpQueryService.executeConfirmed(confirmRequest);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Parse and immediately execute a natural language query.
     *
     * @param request The NLP query request
     * @return Response with generated SQL and results
     */
    @PostMapping("/parse-and-execute")
    @Operation(summary = "Parse natural language and execute in one step")
    public ResponseEntity<NlpQueryResponse> parseAndExecute(@RequestBody NlpQueryRequest request) {
        LOGGER.info("Received NLP parse-and-execute request: {}", request.getNaturalLanguageQuery());

        request.setExecuteImmediately(true);
        NlpQueryResponse response = nlpQueryService.parseAndExecute(request);

        if (response.isValid()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get example natural language queries that users can try.
     *
     * @return List of example queries
     */
    @GetMapping("/examples")
    @Operation(summary = "Get example natural language queries")
    public ResponseEntity<List<String>> getExamples() {
        return ResponseEntity.ok(nlpQueryService.getExampleQueries());
    }

    /**
     * Get information about available tables and columns.
     *
     * @return Schema information
     */
    @GetMapping("/schema")
    @Operation(summary = "Get available database schema information")
    public ResponseEntity<NlpQueryResponse> getSchemaInfo() {
        NlpQueryResponse response = nlpQueryService.getSchemaInfo();
        return ResponseEntity.ok(response);
    }
}
