(function () {
    'use strict';

    var controllerId = 'NlpQueryBuilderController';

    function NlpQueryBuilderController($scope, NlpQueryService, Util) {
        var ctrl = this;

        // State
        ctrl.naturalLanguageQuery = '';
        ctrl.isLoading = false;
        ctrl.isParsing = false;
        ctrl.isExecuting = false;
        ctrl.showPreview = false;
        ctrl.showResults = false;
        ctrl.showSchema = false;
        ctrl.showExamples = true;

        // Data
        ctrl.previewResponse = null;
        ctrl.executionResponse = null;
        ctrl.examples = [];
        ctrl.schemaInfo = null;
        ctrl.errorMessage = '';
        ctrl.successMessage = '';

        // History
        ctrl.queryHistory = [];

        // Initialize
        ctrl.init = function () {
            ctrl.loadExamples();
        };

        // Load example queries
        ctrl.loadExamples = function () {
            NlpQueryService.getExamples().then(function (data) {
                ctrl.examples = data;
            }, function (error) {
                // Fallback examples
                ctrl.examples = [
                    'Show all users',
                    'Count members where gender is male',
                    'List all active members',
                    'Show families where state is active',
                    'Find locations where type is village',
                    'Get all hospitals',
                    'Show top 10 users sorted by created on descending',
                    'Count pregnant patients',
                    'How many female members are there',
                    'Show members where name contains kumar'
                ];
            });
        };

        // Load schema info
        ctrl.loadSchema = function () {
            ctrl.showSchema = !ctrl.showSchema;
            if (ctrl.showSchema && !ctrl.schemaInfo) {
                NlpQueryService.getSchema().then(function (data) {
                    ctrl.schemaInfo = data;
                }, function (error) {
                    ctrl.errorMessage = 'Failed to load schema information.';
                });
            }
        };

        // Use example query
        ctrl.useExample = function (example) {
            ctrl.naturalLanguageQuery = example;
            ctrl.showExamples = false;
            ctrl.parseQuery();
        };

        // Parse and preview the query
        ctrl.parseQuery = function () {
            if (!ctrl.naturalLanguageQuery || ctrl.naturalLanguageQuery.trim() === '') {
                ctrl.errorMessage = 'Please enter a natural language query.';
                return;
            }

            ctrl.clearMessages();
            ctrl.isParsing = true;
            ctrl.isLoading = true;
            ctrl.showResults = false;
            ctrl.executionResponse = null;

            var request = {
                naturalLanguageQuery: ctrl.naturalLanguageQuery,
                executeImmediately: false
            };

            NlpQueryService.parseQuery(request).then(function (data) {
                ctrl.previewResponse = data;
                ctrl.showPreview = true;
                ctrl.isParsing = false;
                ctrl.isLoading = false;

                if (!data.valid) {
                    ctrl.errorMessage = data.errorMessage || 'Could not understand your query. Please try again.';
                    if (data.suggestions) {
                        ctrl.examples = data.suggestions;
                        ctrl.showExamples = true;
                    }
                }
            }, function (error) {
                ctrl.isParsing = false;
                ctrl.isLoading = false;

                if (error.data && error.data.errorMessage) {
                    ctrl.errorMessage = error.data.errorMessage;
                    if (error.data.suggestions) {
                        ctrl.examples = error.data.suggestions;
                        ctrl.showExamples = true;
                    }
                } else {
                    ctrl.errorMessage = 'An error occurred while processing your query. Please try again.';
                }
            });
        };

        // Execute the confirmed query
        ctrl.executeQuery = function () {
            if (!ctrl.previewResponse || !ctrl.previewResponse.generatedSql) {
                ctrl.errorMessage = 'No query to execute. Please parse a query first.';
                return;
            }

            ctrl.clearMessages();
            ctrl.isExecuting = true;
            ctrl.isLoading = true;

            var confirmRequest = {
                generatedSql: ctrl.previewResponse.generatedSql,
                originalQuery: ctrl.naturalLanguageQuery
            };

            NlpQueryService.executeQuery(confirmRequest).then(function (data) {
                ctrl.executionResponse = data;
                ctrl.showResults = true;
                ctrl.isExecuting = false;
                ctrl.isLoading = false;

                if (data.valid && data.executed) {
                    ctrl.successMessage = 'Query executed successfully! ' + data.resultCount + ' rows returned.';
                    ctrl.addToHistory(ctrl.naturalLanguageQuery, ctrl.previewResponse.generatedSql, data.resultCount);
                } else {
                    ctrl.errorMessage = data.errorMessage || 'Query execution failed.';
                }
            }, function (error) {
                ctrl.isExecuting = false;
                ctrl.isLoading = false;
                ctrl.errorMessage = error.data && error.data.errorMessage ?
                    error.data.errorMessage : 'An error occurred while executing the query.';
            });
        };

        // Parse and execute in one step
        ctrl.parseAndExecute = function () {
            if (!ctrl.naturalLanguageQuery || ctrl.naturalLanguageQuery.trim() === '') {
                ctrl.errorMessage = 'Please enter a natural language query.';
                return;
            }

            ctrl.clearMessages();
            ctrl.isLoading = true;
            ctrl.isParsing = true;

            var request = {
                naturalLanguageQuery: ctrl.naturalLanguageQuery,
                executeImmediately: true
            };

            NlpQueryService.parseAndExecute(request).then(function (data) {
                ctrl.previewResponse = data;
                ctrl.executionResponse = data;
                ctrl.showPreview = true;
                ctrl.showResults = data.executed;
                ctrl.isLoading = false;
                ctrl.isParsing = false;

                if (data.valid && data.executed) {
                    ctrl.successMessage = 'Query executed successfully! ' + data.resultCount + ' rows returned.';
                    ctrl.addToHistory(ctrl.naturalLanguageQuery, data.generatedSql, data.resultCount);
                } else if (!data.valid) {
                    ctrl.errorMessage = data.errorMessage || 'Could not understand your query.';
                }
            }, function (error) {
                ctrl.isLoading = false;
                ctrl.isParsing = false;
                ctrl.errorMessage = error.data && error.data.errorMessage ?
                    error.data.errorMessage : 'An error occurred.';
            });
        };

        // Add to query history
        ctrl.addToHistory = function (nlQuery, sql, resultCount) {
            ctrl.queryHistory.unshift({
                naturalLanguageQuery: nlQuery,
                generatedSql: sql,
                resultCount: resultCount,
                timestamp: new Date()
            });
            // Keep only last 10
            if (ctrl.queryHistory.length > 10) {
                ctrl.queryHistory.pop();
            }
        };

        // Reuse history query
        ctrl.reuseHistoryQuery = function (historyItem) {
            ctrl.naturalLanguageQuery = historyItem.naturalLanguageQuery;
            ctrl.parseQuery();
        };

        // Clear query
        ctrl.clearQuery = function () {
            ctrl.naturalLanguageQuery = '';
            ctrl.previewResponse = null;
            ctrl.executionResponse = null;
            ctrl.showPreview = false;
            ctrl.showResults = false;
            ctrl.showExamples = true;
            ctrl.clearMessages();
            ctrl.loadExamples();
        };

        // Clear messages
        ctrl.clearMessages = function () {
            ctrl.errorMessage = '';
            ctrl.successMessage = '';
        };

        // Get result column headers
        ctrl.getResultColumns = function () {
            if (ctrl.executionResponse && ctrl.executionResponse.results && ctrl.executionResponse.results.length > 0) {
                return Object.keys(ctrl.executionResponse.results[0]);
            }
            return [];
        };

        // Export results to CSV
        ctrl.exportToCsv = function () {
            if (!ctrl.executionResponse || !ctrl.executionResponse.results || ctrl.executionResponse.results.length === 0) {
                return;
            }

            var columns = ctrl.getResultColumns();
            var csvContent = columns.join(',') + '\n';

            ctrl.executionResponse.results.forEach(function (row) {
                var values = columns.map(function (col) {
                    var val = row[col] || '';
                    // Escape commas and quotes
                    if (typeof val === 'string' && (val.indexOf(',') >= 0 || val.indexOf('"') >= 0)) {
                        val = '"' + val.replace(/"/g, '""') + '"';
                    }
                    return val;
                });
                csvContent += values.join(',') + '\n';
            });

            var blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
            var link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            link.download = 'nlp_query_results_' + new Date().getTime() + '.csv';
            link.click();
        };

        // Handle enter key
        ctrl.onKeyPress = function (event) {
            if (event.keyCode === 13) {
                ctrl.parseQuery();
            }
        };

        // Init on load
        ctrl.init();
    }

    angular.module('imtecho.controller').controller(controllerId,
        ['$scope', 'NlpQueryService', 'Util', NlpQueryBuilderController]);
})();
