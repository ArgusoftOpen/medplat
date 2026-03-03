package com.argusoft.medplat.nlpquery.service.impl;

import com.argusoft.medplat.nlpquery.dto.NlpQueryConfirmRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryRequest;
import com.argusoft.medplat.nlpquery.dto.NlpQueryResponse;
import com.argusoft.medplat.nlpquery.generator.SqlGenerator;
import com.argusoft.medplat.nlpquery.parser.NlpQueryParser;
import com.argusoft.medplat.nlpquery.parser.ParsedQuery;
import com.argusoft.medplat.nlpquery.schema.SchemaMapping;
import com.argusoft.medplat.nlpquery.service.NlpQueryService;
import com.argusoft.medplat.nlpquery.validator.SqlValidator;
import com.argusoft.medplat.query.dao.TableDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of NlpQueryService.
 * Orchestrates the full NLP query pipeline: parse -> validate -> generate -> preview/execute.
 *
 * @author medplat
 * @since 02/03/2026
 */
@Service
@Transactional
public class NlpQueryServiceImpl implements NlpQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NlpQueryServiceImpl.class);

    @Autowired
    private NlpQueryParser nlpQueryParser;

    @Autowired
    private SqlGenerator sqlGenerator;

    @Autowired
    private SqlValidator sqlValidator;

    @Autowired
    private SchemaMapping schemaMapping;

    @Autowired
    private TableDao tableDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public NlpQueryResponse parseAndPreview(NlpQueryRequest request) {
        NlpQueryResponse response = new NlpQueryResponse();
        response.setOriginalQuery(request.getNaturalLanguageQuery());

        try {
            // Step 1: Validate input
            SqlValidator.ValidationResult inputValidation = sqlValidator.validateInput(request.getNaturalLanguageQuery());
            if (!inputValidation.isValid()) {
                response.setValid(false);
                response.setErrorMessage(inputValidation.getErrorMessage());
                response.setSuggestions(nlpQueryParser.getExampleQueries());
                return response;
            }

            // Step 2: Parse the natural language query
            ParsedQuery parsedQuery = nlpQueryParser.parse(request.getNaturalLanguageQuery());
            if (!parsedQuery.isValid()) {
                response.setValid(false);
                response.setErrorMessage(parsedQuery.getErrorMessage());
                response.setSuggestions(parsedQuery.getSuggestions());
                return response;
            }

            // Step 3: Generate SQL
            SqlGenerator.GeneratedSql generatedSql = sqlGenerator.generate(parsedQuery);
            if (!generatedSql.isValid()) {
                response.setValid(false);
                response.setErrorMessage(generatedSql.getErrorMessage());
                return response;
            }

            // Step 4: Populate response for preview
            response.setGeneratedSql(generatedSql.getSql());
            response.setPreviewDescription(generatedSql.getPreviewDescription());
            response.setInterpretedIntent(parsedQuery.getIntent());
            response.setTargetTable(parsedQuery.getTableName());
            response.setSelectedColumns(parsedQuery.getColumns());

            if (parsedQuery.getConditions() != null) {
                response.setConditions(parsedQuery.getConditions().stream()
                        .map(ParsedQuery.WhereCondition::toString)
                        .collect(Collectors.toList()));
            }

            response.setValid(true);
            response.setExecuted(false);

            LOGGER.info("Successfully parsed and generated SQL preview for query: {}", request.getNaturalLanguageQuery());

        } catch (Exception e) {
            LOGGER.error("Error processing NLP query: {}", request.getNaturalLanguageQuery(), e);
            response.setValid(false);
            response.setErrorMessage("An unexpected error occurred while processing your query. " +
                    "Please try rephrasing it or use a simpler query.");
            response.setSuggestions(nlpQueryParser.getExampleQueries());
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NlpQueryResponse executeConfirmed(NlpQueryConfirmRequest confirmRequest) {
        NlpQueryResponse response = new NlpQueryResponse();
        response.setOriginalQuery(confirmRequest.getOriginalQuery());
        response.setGeneratedSql(confirmRequest.getGeneratedSql());

        try {
            // Re-validate the SQL before execution
            SqlValidator.ValidationResult validation = sqlValidator.validate(confirmRequest.getGeneratedSql());
            if (!validation.isValid()) {
                response.setValid(false);
                response.setErrorMessage("SQL validation failed: " + validation.getErrorMessage());
                return response;
            }

            // Execute the query
            List<LinkedHashMap<String, Object>> results = tableDao.executeQuery(confirmRequest.getGeneratedSql());

            response.setResults(results);
            response.setResultCount(results.size());
            response.setExecuted(true);
            response.setValid(true);

            LOGGER.info("Successfully executed confirmed query. Results: {} rows", results.size());

        } catch (Exception e) {
            LOGGER.error("Error executing confirmed query: {}", confirmRequest.getGeneratedSql(), e);
            response.setValid(false);
            response.setErrorMessage("Error executing query: " + e.getMessage());
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NlpQueryResponse parseAndExecute(NlpQueryRequest request) {
        // First parse and preview
        NlpQueryResponse previewResponse = parseAndPreview(request);

        if (!previewResponse.isValid()) {
            return previewResponse;
        }

        // If executeImmediately flag is set, execute directly
        if (request.isExecuteImmediately()) {
            NlpQueryConfirmRequest confirmRequest = new NlpQueryConfirmRequest();
            confirmRequest.setGeneratedSql(previewResponse.getGeneratedSql());
            confirmRequest.setOriginalQuery(request.getNaturalLanguageQuery());
            return executeConfirmed(confirmRequest);
        }

        return previewResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getExampleQueries() {
        return nlpQueryParser.getExampleQueries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NlpQueryResponse getSchemaInfo() {
        NlpQueryResponse response = new NlpQueryResponse();
        response.setValid(true);

        List<LinkedHashMap<String, Object>> schemaData = new ArrayList<>();

        for (String tableName : schemaMapping.getAllTableNames()) {
            LinkedHashMap<String, Object> tableInfo = new LinkedHashMap<>();
            tableInfo.put("table_name", tableName);
            tableInfo.put("columns", String.join(", ", schemaMapping.getAllowedColumns(tableName)));

            // Find keywords for this table
            List<String> keywords = schemaMapping.getTableKeywordMap().entrySet().stream()
                    .filter(e -> e.getValue().equals(tableName))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            tableInfo.put("keywords", String.join(", ", keywords));

            schemaData.add(tableInfo);
        }

        response.setResults(schemaData);
        response.setResultCount(schemaData.size());
        response.setPreviewDescription("Available tables and their columns for NLP querying");
        return response;
    }
}
