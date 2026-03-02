(function() {
    'use strict';
    angular.module('imtecho.dashboard.aientabled', [
        'ui.router',
        'ngAnimate',
        'chart.js',
        'angular-loading-bar'
    ])
    .controller('AIDashboardController', AIDashboardController)
    .controller('DashboardBuilderController', DashboardBuilderController)
    .controller('AIInsightsController', AIInsightsController)
    .controller('NLPQueryController', NLPQueryController)
    .service('DashboardService', DashboardService)
    .service('DashboardNLPService', DashboardNLPService)
    .service('DashboardAIService', DashboardAIService)
    .service('DashboardWidgetService', DashboardWidgetService)
    .directive('aiDashboardWidget', aiDashboardWidget)
    .directive('aiDashboardBuilder', aiDashboardBuilder)
    .filter('trustAsHtml', trustAsHtml);
    AIDashboardController.$inject = ['$scope', '$state', 'DashboardService', 'DashboardAIService', '$timeout'];
    function AIDashboardController($scope, $state, DashboardService, DashboardAIService, $timeout) {
        $scope.dashboard = null;
        $scope.widgets = [];
        $scope.insights = [];
        $scope.loading = true;
        $scope.selectedWidget = null;
        $scope.initDashboard = function(dashboardId) {
            DashboardService.getDashboard(dashboardId).then(function(response) {
                $scope.dashboard = response.data.data;
                $scope.widgets = response.data.data.widgets || [];
                $scope.loading = false;
                if ($scope.dashboard.aiEnabled) {
                    $scope.loadAIInsights();
                }
                if ($scope.dashboard.refreshIntervalSeconds) {
                    $scope.setupAutoRefresh($scope.dashboard.refreshIntervalSeconds);
                }
            }).catch(function(error) {
                console.error('Error loading dashboard', error);
                $scope.loading = false;
            });
        };
        $scope.loadAIInsights = function() {
            if ($scope.widgets && $scope.widgets.length > 0) {
                angular.forEach($scope.widgets, function(widget) {
                    DashboardAIService.getWidgetInsights(widget.id).then(function(response) {
                        widget.insights = response.data.insights;
                    });
                });
            }
        };
        $scope.setupAutoRefresh = function(intervalSeconds) {
            $timeout(function() {
                $scope.refreshDashboard();
                $scope.setupAutoRefresh(intervalSeconds);
            }, intervalSeconds * 1000);
        };
        $scope.refreshDashboard = function() {
            if ($scope.dashboard && $scope.dashboard.id) {
                $scope.initDashboard($scope.dashboard.id);
            }
        };
        $scope.selectWidget = function(widget) {
            $scope.selectedWidget = widget;
        };
        $scope.editDashboard = function() {
            $state.go('dashboard.builder', { dashboardId: $scope.dashboard.id });
        };
        $scope.$on('$destroy', function() {
            if ($scope.refreshTimer) {
                $timeout.cancel($scope.refreshTimer);
            }
        });
    }
    DashboardBuilderController.$inject = ['$scope', '$stateParams', 'DashboardService', 'DashboardWidgetService', '$q'];
    function DashboardBuilderController($scope, $stateParams, DashboardService, DashboardWidgetService, $q) {
        $scope.dashboard = {};
        $scope.widgets = [];
        $scope.availableDataSources = [];
        $scope.widgetTypes = ['LINE_CHART', 'BAR_CHART', 'PIE_CHART', 'TABLE', 'KPI', 'GAUGE', 'MAP', 'HEATMAP'];
        $scope.gridLayout = { columns: 12, rowHeight: 50 };
        $scope.init = function() {
            if ($stateParams.dashboardId) {
                DashboardService.getDashboard($stateParams.dashboardId).then(function(response) {
                    $scope.dashboard = response.data.data;
                    $scope.widgets = response.data.data.widgets || [];
                });
            } else {
                $scope.dashboard = {
                    dashboardName: 'New Dashboard',
                    dashboardType: 'ANALYTICS',
                    aiEnabled: true,
                    nlpEnabled: true,
                    anomalyDetectionEnabled: false,
                    predictiveAnalyticsEnabled: false
                };
            }
            DashboardService.getDataSources().then(function(response) {
                $scope.availableDataSources = response.data.dataSources;
            });
        };
        $scope.addWidget = function() {
            var newWidget = {
                widgetName: 'New Widget',
                widgetType: 'TABLE',
                gridWidth: 4,
                gridHeight: 200,
                title: 'Widget Title',
                refreshIntervalSeconds: 300
            };
            $scope.widgets.push(newWidget);
        };
        $scope.removeWidget = function(index) {
            $scope.widgets.splice(index, 1);
        };
        $scope.saveDashboard = function() {
            $scope.dashboard.widgets = $scope.widgets;
            if ($scope.dashboard.id) {
                DashboardService.updateDashboard($scope.dashboard.id, $scope.dashboard)
                    .then(function() {
                        alert('Dashboard updated successfully');
                    });
            } else {
                DashboardService.createDashboard($scope.dashboard)
                    .then(function(response) {
                        alert('Dashboard created successfully');
                        $scope.dashboard.id = response.data.dashboardId;
                    });
            }
        };
        $scope.configureWidget = function(widget) {
            $scope.selectedWidget = widget;
        };
        $scope.init();
    }
    AIInsightsController.$inject = ['$scope', 'DashboardAIService'];
    function AIInsightsController($scope, DashboardAIService) {
        $scope.insights = [];
        $scope.filteredInsights = [];
        $scope.severityFilter = 'ALL';
        $scope.typeFilter = 'ALL';
        $scope.loadInsights = function(userId) {
            DashboardAIService.getUserRecentInsights(userId).then(function(response) {
                $scope.insights = response.data.insights;
                $scope.filterInsights();
            });
        };
        $scope.filterInsights = function() {
            $scope.filteredInsights = $scope.insights.filter(function(insight) {
                var severityMatch = $scope.severityFilter === 'ALL' || insight.severity === $scope.severityFilter;
                var typeMatch = $scope.typeFilter === 'ALL' || insight.insightType === $scope.typeFilter;
                return severityMatch && typeMatch;
            });
        };
        $scope.acknowledgeInsight = function(insight) {
            DashboardAIService.acknowledgeInsight(insight.id).then(function() {
                insight.isAcknowledged = true;
            });
        };
        $scope.resolveInsight = function(insight) {
            DashboardAIService.resolveInsight(insight.id).then(function() {
                insight.isResolved = true;
            });
        };
        $scope.setFeedback = function(insight, feedback) {
            DashboardAIService.setInsightFeedback(insight.id, feedback).then(function() {
                insight.userFeedback = feedback;
            });
        };
        $scope.$watch('severityFilter', $scope.filterInsights);
        $scope.$watch('typeFilter', $scope.filterInsights);
    }
    NLPQueryController.$inject = ['$scope', 'DashboardNLPService', '$timeout'];
    function NLPQueryController($scope, DashboardNLPService, $timeout) {
        $scope.query = '';
        $scope.response = null;
        $scope.loading = false;
        $scope.confidenceScore = 0;
        $scope.suggestedWidget = null;
        $scope.clarificationQuestions = [];
        $scope.processQuery = function() {
            if (!$scope.query || $scope.query.trim().length === 0) {
                return;
            }
            $scope.loading = true;
            DashboardNLPService.processQuery({
                query: $scope.query,
                userId: $scope.userId,
                dashboardId: $scope.dashboardId
            }).then(function(response) {
                $scope.response = response.data.response;
                $scope.confidenceScore = $scope.response.confidenceScore;
                $scope.suggestedWidget = $scope.response.suggestedWidget;
                $scope.clarificationQuestions = $scope.response.clarificationQuestions;
                $scope.loading = false;
            }).catch(function(error) {
                console.error('Error processing query', error);
                $scope.loading = false;
            });
        };
        $scope.getQuerySuggestions = function(searchText) {
            return [
                'Show me patient count by department',
                'Display revenue trends for last 30 days',
                'What are anomalies in patient data?',
                'Forecast patient visits for next quarter',
                'Show top 10 treatments by frequency'
            ].filter(function(item) {
                return item.toLowerCase().indexOf(searchText.toLowerCase()) > -1;
            });
        };
        $scope.getConfidenceIndicator = function() {
            if ($scope.confidenceScore < 0.5) return 'Low';
            if ($scope.confidenceScore < 0.8) return 'Medium';
            return 'High';
        };
        $scope.applySuggestedWidget = function() {
            if ($scope.suggestedWidget) {
                $scope.$emit('widget-created', $scope.suggestedWidget);
            }
        };
    }
    DashboardService.$inject = ['$http', '$q', 'APP_CONFIG'];
    function DashboardService($http, $q, APP_CONFIG) {
        var service = {
            getDashboard: getDashboard,
            getDashboards: getDashboards,
            createDashboard: createDashboard,
            updateDashboard: updateDashboard,
            deleteDashboard: deleteDashboard,
            getDataSources: getDataSources,
            cloneDashboard: cloneDashboard,
            exportDashboard: exportDashboard,
            importDashboard: importDashboard
        };
        function getDashboard(dashboardId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/' + dashboardId);
        }
        function getDashboards(userId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/user/' + userId);
        }
        function createDashboard(dashboardData) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/create', dashboardData);
        }
        function updateDashboard(dashboardId, dashboardData) {
            return $http.put(APP_CONFIG.apiPath + '/dashboard/' + dashboardId, dashboardData);
        }
        function deleteDashboard(dashboardId) {
            return $http.delete(APP_CONFIG.apiPath + '/dashboard/' + dashboardId);
        }
        function getDataSources() {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/data-sources');
        }
        function cloneDashboard(dashboardId, userId, newName) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/' + dashboardId + '/clone/' + userId, { newName: newName });
        }
        function exportDashboard(dashboardId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/' + dashboardId + '/export');
        }
        function importDashboard(userId, jsonData) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/import/' + userId, jsonData);
        }
        return service;
    }
    DashboardNLPService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardNLPService($http, APP_CONFIG) {
        var service = {
            processQuery: processQuery,
            extractIntent: extractIntent,
            suggestWidget: suggestWidget,
            generateChart: generateChart,
            queryKPI: queryKPI
        };
        function processQuery(queryRequest) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/nlp/query', queryRequest);
        }
        function extractIntent(query) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/nlp/intent', { params: { query: query } });
        }
        function suggestWidget(description) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/nlp/suggest-widget', { params: { description: description } });
        }
        function generateChart(query, dataSourceId) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/nlp/generate-chart', {}, { params: { query: query, dataSourceId: dataSourceId } });
        }
        function queryKPI(query) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/nlp/kpi', { params: { query: query } });
        }
        return service;
    }
    DashboardAIService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardAIService($http, APP_CONFIG) {
        var service = {
            getWidgetInsights: getWidgetInsights,
            getUserRecentInsights: getUserRecentInsights,
            getCriticalInsights: getCriticalInsights,
            generateAnomalies: generateAnomalies,
            generateForecast: generateForecast,
            generateSummary: generateSummary,
            acknowledgeInsight: acknowledgeInsight,
            resolveInsight: resolveInsight,
            setInsightFeedback: setInsightFeedback
        };
        function getWidgetInsights(widgetId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/ai-insights/widget/' + widgetId);
        }
        function getUserRecentInsights(userId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/ai-insights/user/' + userId + '/recent');
        }
        function getCriticalInsights() {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/ai-insights/critical');
        }
        function generateAnomalies(widgetId, data) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/ai-insights/anomaly-detection', data, { params: { widgetId: widgetId } });
        }
        function generateForecast(widgetId, data, periods) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/ai-insights/forecast', data, { params: { widgetId: widgetId, forecastPeriods: periods } });
        }
        function generateSummary(widgetId, data) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/ai-insights/summary', data, { params: { widgetId: widgetId } });
        }
        function acknowledgeInsight(insightId) {
            return $http.put(APP_CONFIG.apiPath + '/dashboard/ai-insights/' + insightId + '/acknowledge');
        }
        function resolveInsight(insightId) {
            return $http.put(APP_CONFIG.apiPath + '/dashboard/ai-insights/' + insightId + '/resolve');
        }
        function setInsightFeedback(insightId, feedback) {
            return $http.put(APP_CONFIG.apiPath + '/dashboard/ai-insights/' + insightId + '/feedback', {}, { params: { feedback: feedback } });
        }
        return service;
    }
    DashboardWidgetService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardWidgetService($http, APP_CONFIG) {
        var service = {
            renderWidget: renderWidget,
            refreshWidgetData: refreshWidgetData,
            getWidgetData: getWidgetData
        };
        function renderWidget(widgetId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/widgets/' + widgetId + '/render');
        }
        function refreshWidgetData(widgetId) {
            return $http.post(APP_CONFIG.apiPath + '/dashboard/widgets/' + widgetId + '/refresh');
        }
        function getWidgetData(widgetId) {
            return $http.get(APP_CONFIG.apiPath + '/dashboard/widgets/' + widgetId + '/data');
        }
        return service;
    }
    function aiDashboardWidget() {
        return {
            restrict: 'E',
            scope: {
                widget: '=',
                insights: '='
            },
            templateUrl: 'app/dashboard/ai/views/ai-widget.html',
            link: function(scope) {
                scope.toggleInsights = function() {
                    scope.showInsights = !scope.showInsights;
                };
            }
        };
    }
    function aiDashboardBuilder() {
        return {
            restrict: 'E',
            scope: {
                dashboard: '=',
                widgets: '='
            },
            templateUrl: 'app/dashboard/ai/views/dashboard-builder.html',
            link: function(scope) {
                scope.draggedWidget = null;
            }
        };
    }
    function trustAsHtml() {
        return function(input) {
            return input;
        };
    }
})();
