package com.argusoft.medplat.nlpquery.service;

import com.argusoft.medplat.nlpquery.dto.NlpQueryConfirmRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryResponse;

import java.util.List;

/**
 * Service interface for the NLP Query Builder.
 * Defines the contract for parsing, previewing, and executing NL queries.
 *
 * <p>Modular design to support future ML-based or AI-driven enhancements
 * by implementing this interface with different strategies.</p>
 *
 * @author medplat
 * @since 02/03/2026
 */
public interface NlpQueryService {

    /**
     * Parse a natural language query and generate SQL preview.
     * Does NOT execute the query - only parses and generates SQL for review.
     *
     * @param request The NLP query request
     * @return Response with generated SQL and preview information
     */
    NlpQueryResponse parseAndPreview(NlpQueryRequest request);

    /**
     * Execute a previously generated and confirmed SQL query.
     * The user must have reviewed the SQL preview before confirming execution.
     *
     * @param confirmRequest The confirmed query with generated SQL
     * @return Response with query results
     */
    NlpQueryResponse executeConfirmed(NlpQueryConfirmRequest confirmRequest);

    /**
     * Parse, preview, and immediately execute a query in one step.
     * Use with caution - bypasses the preview step.
     *
     * @param request The NLP query request
     * @return Response with generated SQL and results
     */
    NlpQueryResponse parseAndExecute(NlpQueryRequest request);

    /**
     * Get example queries that users can try.
     *
     * @return List of example NL queries
     */
    List<String> getExampleQueries();

    /**
     * Get a list of available tables and their descriptions.
     *
     * @return Response with schema information
     */
    NlpQueryResponse getSchemaInfo();
}
