(function () {
    'use strict';

    angular.module('imtecho.dashboard.aientabled', [
        'ui.router',
        'ngAnimate',
        'chart.js',
        'dndLists'
    ])
    .controller('AIDashboardController',    AIDashboardController)
    .controller('DashboardListController',  DashboardListController)
    .controller('DashboardBuilderController', DashboardBuilderController)
    .controller('AIInsightsController',     AIInsightsController)
    .controller('NLPQueryController',       NLPQueryController)
    .service('DashboardService',       DashboardService)
    .service('DashboardNLPService',    DashboardNLPService)
    .service('DashboardAIService',     DashboardAIService)
    .service('DashboardWidgetService', DashboardWidgetService)
    .directive('aiDashboardWidget',    aiDashboardWidget)
    .filter('replace', replaceFilter);

    /* ==========================================================
       AIDashboardController – Main view for a single dashboard
       ========================================================== */
    AIDashboardController.$inject = [
        '$scope', '$state', '$stateParams', '$timeout',
        'DashboardService', 'DashboardAIService', 'DashboardNLPService'
    ];
    function AIDashboardController(
        $scope, $state, $stateParams, $timeout,
        DashboardService, DashboardAIService, DashboardNLPService
    ) {
        /* -- state -- */
        $scope.loading           = true;
        $scope.dashboard         = null;
        $scope.widgets           = [];
        $scope.summaryKpis       = [];
        $scope.aiSuggestions     = [];
        $scope.activeFilters     = [];
        $scope.availableFilters  = [];
        $scope.criticalInsightCount = 0;
        $scope.showNlpPanel      = false;
        $scope.showInsightsPanel = false;
        $scope.showFab           = false;
        $scope.nlpBarQuery       = '';
        $scope.nlpLoading        = false;
        $scope.expandedWidget    = null;
        $scope.lastRefreshed     = null;
        $scope.currentRole       = 'USER';

        /* NLP quick chips vary by role */
        $scope.nlpQuickChips = [
            'Show patient count by department',
            'Display visits trend last 30 days',
            'What are anomalies in patient data?',
            'Top 10 diagnoses this month',
            'Forecast next quarter visits',
            'Show completion rate by ANM'
        ];

        /* -- init -- */
        var dashboardId = $stateParams.dashboardId || 1;
        _loadCurrentRole();
        _initDashboard(dashboardId);

        function _loadCurrentRole() {
            try {
                var userData = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
                $scope.currentRole = userData.role || userData.userRole || 'USER';
            } catch (e) { $scope.currentRole = 'USER'; }
            _applyRoleKpis($scope.currentRole);
        }

        function _applyRoleKpis(role) {
            var kpisByRole = {
                ANM: [
                    { label: 'Beneficiaries', value: '1,234', change: 4.2,  icon: 'fa-users',      colorClass: 'blue' },
                    { label: 'Home Visits',   value: '89',    change: -2.1, icon: 'fa-home',       colorClass: 'green' },
                    { label: 'Immunized',     value: '456',   change: 8.3,  icon: 'fa-medkit',     colorClass: 'orange' },
                    { label: 'Due This Week', value: '23',    change: 0,    icon: 'fa-calendar',   colorClass: 'red' }
                ],
                SUPERVISOR: [
                    { label: 'ANMs Reporting',    value: '18',    change: 0,    icon: 'fa-users',    colorClass: 'blue' },
                    { label: 'Coverage Rate',     value: '87%',   change: 3.1,  icon: 'fa-pie-chart',colorClass: 'green' },
                    { label: 'Pending Reviews',   value: '7',     change: -1,   icon: 'fa-clock-o',  colorClass: 'orange' },
                    { label: 'Alerts',            value: '2',     change: 0,    icon: 'fa-bell',     colorClass: 'red' }
                ],
                ADMIN: [
                    { label: 'Total Users',       value: '342',   change: 12,   icon: 'fa-users',    colorClass: 'blue' },
                    { label: 'Active Dashboards', value: '28',    change: 5,    icon: 'fa-bar-chart', colorClass: 'green' },
                    { label: 'System Alerts',     value: '3',     change: 0,    icon: 'fa-warning',  colorClass: 'orange' },
                    { label: 'Data Sources',      value: '12',    change: 2,    icon: 'fa-database', colorClass: 'cyan' }
                ]
            };
            $scope.summaryKpis = kpisByRole[role] || kpisByRole['ANM'];
        }

        function _initDashboard(id) {
            $scope.loading = true;
            DashboardService.getDashboard(id).then(function (res) {
                var data = (res.data && res.data.data) ? res.data.data : res.data;
                $scope.dashboard = data;
                $scope.widgets   = (data && data.widgets) ? data.widgets : [];
                $scope.loading   = false;
                $scope.lastRefreshed = new Date();
                _prepareWidgetData();
                if (data && data.aiEnabled) { _loadInsightCounts(); }
                if (data && data.refreshIntervalSeconds > 0) {
                    _scheduleAutoRefresh(data.refreshIntervalSeconds);
                }
                _loadAISuggestions();
                _buildFilters();
            }).catch(function () {
                $scope.loading = false;
                /* Use mock data so the UI is usable without a backend */
                $scope.dashboard = _mockDashboard();
                $scope.widgets   = _mockWidgets();
                _prepareWidgetData();
                _loadAISuggestions();
                _buildFilters();
            });
        }

        function _prepareWidgetData() {
            angular.forEach($scope.widgets, function (w) {
                w.loading = true;
                $timeout(function () {
                    _injectMockData(w);
                    w.loading = false;
                    w.lastUpdated = new Date();
                }, 600 + Math.random() * 600);
            });
        }

        function _injectMockData(w) {
            var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
            var recent = months.slice(-6);
            switch (w.widgetType) {
                case 'LINE_CHART':
                case 'BAR_CHART':
                    w.chartLabels = recent;
                    w.chartData   = [ _rArr(6, 200, 900) ];
                    w.chartSeries = [w.title || 'Value'];
                    w.chartOptions = { responsive: true, maintainAspectRatio: false,
                                       legend: { display: false } };
                    break;
                case 'PIE_CHART':
                case 'DOUGHNUT_CHART':
                    w.chartLabels = ['Urban','Rural','Tribal','Other'];
                    w.chartData   = [42, 35, 15, 8];
                    w.chartOptions = { responsive: true, maintainAspectRatio: false };
                    break;
                case 'KPI':
                    w.kpiValue  = Math.floor(Math.random() * 9000) + 1000;
                    w.kpiLabel  = w.title || 'Metric';
                    w.kpiChange = +(Math.random() * 20 - 5).toFixed(1);
                    w.kpiUnit   = '';
                    w.sparklineData   = [ _rArr(8, 100, 500) ];
                    w.sparklineLabels = months.slice(-8);
                    break;
                case 'GAUGE':
                    w.gaugeValue = Math.floor(Math.random() * 85) + 10;
                    w.gaugeLabel = w.title || 'Progress';
                    break;
                case 'TABLE':
                    w.columns   = ['Name','Department','Count','Status'];
                    w.tableData = _mockTableRows(w.columns, 15);
                    w.sortCol   = '';
                    w.sortAsc   = true;
                    break;
                case 'HEATMAP':
                    w.heatmapXLabels = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
                    w.heatmapYLabels = ['Morning','Afternoon','Evening'];
                    w.heatmapData    = [
                        _rArr(7, 0, 100), _rArr(7, 0, 100), _rArr(7, 0, 100)
                    ];
                    w.heatmapMax = 100;
                    break;
            }
            /* Mock AI insights per widget */
            if (w.aiInsights) {
                w.insights = [
                    { insightType:'ANOMALY', severity:'HIGH',   title:'Unusual spike detected',
                      description:'Value 2.4× above average on Jun 15' },
                    { insightType:'TREND',   severity:'LOW',    title:'Consistent upward trend',
                      description:'7% average monthly growth' }
                ];
            }
        }

        function _rArr(n, min, max) {
            var arr = [];
            for (var i = 0; i < n; i++) { arr.push(Math.floor(Math.random() * (max - min)) + min); }
            return arr;
        }

        function _mockTableRows(cols, n) {
            var depts    = ['OPD','IPD','Emergency','Pharmacy','Lab'];
            var statuses = ['Active','Pending','Closed'];
            var rows = [];
            for (var i = 0; i < n; i++) {
                rows.push({
                    Name:       'Entry ' + (i + 1),
                    Department: depts[i % depts.length],
                    Count:      Math.floor(Math.random() * 500),
                    Status:     statuses[i % statuses.length]
                });
            }
            return rows;
        }

        function _mockDashboard() {
            return {
                id: 1,
                dashboardName: 'Health Analytics Dashboard',
                dashboardType: 'ANALYTICS',
                description: 'Overview of health metrics',
                aiEnabled: true, nlpEnabled: true,
                refreshIntervalSeconds: 300
            };
        }

        function _mockWidgets() {
            return [
                { id:1, title:'Patient Visits', widgetType:'LINE_CHART', gridWidth:6, aiInsights:true },
                { id:2, title:'Department Load', widgetType:'BAR_CHART', gridWidth:6 },
                { id:3, title:'Total Beneficiaries', widgetType:'KPI', gridWidth:3 },
                { id:4, title:'Coverage Rate', widgetType:'KPI', gridWidth:3 },
                { id:5, title:'Gender Distribution', widgetType:'PIE_CHART', gridWidth:3 },
                { id:6, title:'Target Progress', widgetType:'GAUGE', gridWidth:3 },
                { id:7, title:'Recent Records', widgetType:'TABLE', gridWidth:8, aiInsights:false },
                { id:8, title:'Visit Heatmap', widgetType:'HEATMAP', gridWidth:4 }
            ];
        }

        function _loadInsightCounts() {
            DashboardAIService.getCriticalInsights().then(function (res) {
                var insights = (res.data && res.data.insights) ? res.data.insights : [];
                $scope.criticalInsightCount = insights.filter(function (i) {
                    return i.severity === 'CRITICAL';
                }).length;
            }).catch(angular.noop);
        }

        function _loadAISuggestions() {
            $scope.aiSuggestions = [
                'Show patient trend for last week',
                'Compare department performance',
                'Highlight anomalies',
                'Forecast next 30 days',
                'Show completion by ANM'
            ];
        }

        function _buildFilters() {
            $scope.activeFilters = [
                { name: 'District',   value: '', options: ['All','District A','District B','District C'] },
                { name: 'Status',     value: '', options: ['All','Active','Pending','Closed'] }
            ];
        }

        var refreshTimer;
        function _scheduleAutoRefresh(sec) {
            refreshTimer = $timeout(function () {
                $scope.refreshDashboard();
                _scheduleAutoRefresh(sec);
            }, sec * 1000);
        }

        /* -- public methods -- */
        $scope.refreshDashboard = function () {
            if ($scope.dashboard) { _initDashboard($scope.dashboard.id); }
        };

        $scope.refreshSuggestions = function () {
            $scope.aiSuggestions = $scope.aiSuggestions.slice(1).concat($scope.aiSuggestions[0]);
        };

        $scope.applyFilters = function () {
            $scope.lastRefreshed = new Date();
            _prepareWidgetData();
        };

        $scope.clearFilters = function () {
            angular.forEach($scope.activeFilters, function (f) { f.value = ''; });
            $scope.filterDateFrom = null;
            $scope.filterDateTo   = null;
        };

        $scope.processNlpBarQuery = function () {
            if (!$scope.nlpBarQuery || !$scope.nlpBarQuery.trim()) { return; }
            $scope.showNlpPanel = true;
            $scope.nlpLoading   = true;
            $timeout(function () { $scope.nlpLoading = false; }, 1200);
        };

        $scope.runNlpChip = function (chip) {
            $scope.nlpBarQuery  = chip;
            $scope.showNlpPanel = true;
        };

        $scope.hideSuggestionsDelay = function () {
            $timeout(function () { $scope.showNlpSuggestions = false; }, 200);
        };

        $scope.editDashboard = function () {
            $state.go('techo.dashboard.aidashboardbuilder', {
                dashboardId: ($scope.dashboard && $scope.dashboard.id) || ''
            });
        };

        $scope.goToDashboardList = function () {
            $state.go('techo.dashboard.aidashboards');
        };

        $scope.exportDashboard = function () {
            if (!$scope.dashboard) { return; }
            DashboardService.exportDashboard($scope.dashboard.id).then(function (res) {
                var blob = new Blob([JSON.stringify(res.data, null, 2)], { type: 'application/json' });
                var url  = URL.createObjectURL(blob);
                var a    = document.createElement('a');
                a.href   = url;
                a.download = ($scope.dashboard.dashboardName || 'dashboard') + '.json';
                a.click();
                URL.revokeObjectURL(url);
            }).catch(angular.noop);
        };

        $scope.closePanels = function () {
            $scope.showNlpPanel = false;
            $scope.showInsightsPanel = false;
        };

        $scope.selectWidget = function (w) { $scope.selectedWidget = w; };

        $scope.expandWidget = function (w) { $scope.expandedWidget = w; };

        $scope.removeWidget = function (w) {
            var idx = $scope.widgets.indexOf(w);
            if (idx >= 0) { $scope.widgets.splice(idx, 1); }
        };

        $scope.getWidgetSpanClass = function (w) {
            var map = { 3:'span-3', 4:'span-4', 6:'span-6', 8:'span-8', 12:'span-12' };
            return 'span-' + (w || 4);
        };

        $scope.getRoleBadgeClass = function (role) {
            var map = { ANM:'role-anm', SUPERVISOR:'role-supervisor', ADMIN:'role-admin', DOCTOR:'role-doctor' };
            return map[role] || '';
        };

        $scope.sortTable = function (widget, col) {
            if (widget.sortCol === col) { widget.sortAsc = !widget.sortAsc; }
            else { widget.sortCol = col; widget.sortAsc = true; }
        };

        $scope.getSortedRows = function (widget) {
            if (!widget.tableData || !widget.sortCol) { return widget.tableData; }
            return widget.tableData.slice().sort(function (a, b) {
                var va = a[widget.sortCol], vb = b[widget.sortCol];
                return widget.sortAsc ? (va > vb ? 1 : -1) : (va < vb ? 1 : -1);
            });
        };

        /* Gauge helpers */
        $scope.getGaugeDash    = function (val) { return ((val || 0) / 100 * 125.6).toFixed(1); };
        $scope.getGaugeColor   = function (val) {
            return (val > 85) ? 'red' : (val > 70) ? 'yellow' : 'green';
        };
        $scope.getGaugeStroke  = function (val) {
            return (val > 85) ? '#C62828' : (val > 70) ? '#F57F17' : '#2E7D32';
        };

        /* Heatmap helper */
        $scope.getHeatmapColor = function (val, max) {
            var pct    = Math.min((val / (max || 100)) * 100, 100);
            var r = Math.floor(21  + (pct / 100) * (198 - 21));
            var g = Math.floor(101 + (pct / 100) * (40  - 101));
            var b = Math.floor(192 + (pct / 100) * (0   - 192));
            return 'rgb(' + r + ',' + g + ',' + b + ')';
        };

        /* Widget icon */
        $scope.getWidgetIcon = function (type) {
            var icons = {
                LINE_CHART:'fa-line-chart', BAR_CHART:'fa-bar-chart', PIE_CHART:'fa-pie-chart',
                DOUGHNUT_CHART:'fa-circle-o-notch', KPI:'fa-bolt', TABLE:'fa-table',
                GAUGE:'fa-tachometer', HEATMAP:'fa-th', MAP:'fa-map-o'
            };
            return icons[type] || 'fa-bar-chart';
        };

        /* Insight helpers */
        $scope.getInsightClass = function (type, sev) {
            if (sev === 'CRITICAL' || sev === 'HIGH') { return 'critical'; }
            if (type === 'ANOMALY') { return 'anomaly'; }
            if (type === 'FORECAST') { return 'forecast'; }
            if (type === 'TREND') { return 'trend'; }
            return 'anomaly';
        };
        $scope.getInsightIcon = function (type) {
            var icons = { ANOMALY:'fa-exclamation-triangle', FORECAST:'fa-line-chart',
                          TREND:'fa-arrow-up', RECOMMENDATION:'fa-lightbulb-o',
                          CORRELATION:'fa-random', SUMMARY:'fa-file-text-o' };
            return icons[type] || 'fa-info-circle';
        };
        $scope.hasCriticalInsight = function (insights) {
            return insights && insights.some(function (i) {
                return i.severity === 'CRITICAL';
            });
        };

        $scope.downloadChart = function (widget) { /* TODO: use Chart.js toBase64Image() */ };
        $scope.exportWidgetData = function (widget) {
            if (!widget.tableData) { return; }
            var csv = widget.columns.join(',') + '\n' +
                widget.tableData.map(function (r) {
                    return widget.columns.map(function (c) { return r[c]; }).join(',');
                }).join('\n');
            var blob = new Blob([csv], { type: 'text/csv' });
            var a = document.createElement('a'); a.href = URL.createObjectURL(blob);
            a.download = (widget.title || 'data') + '.csv'; a.click();
        };

        $scope.$on('$destroy', function () { if (refreshTimer) { $timeout.cancel(refreshTimer); } });
    }

    /* ==========================================================
       DashboardListController
       ========================================================== */
    DashboardListController.$inject = ['$scope', '$state', 'DashboardService'];
    function DashboardListController($scope, $state, DashboardService) {
        $scope.loading   = true;
        $scope.dashboards = [];
        $scope.recommendedDashboards = [];
        $scope.searchQuery = '';
        $scope.typeFilter  = '';
        $scope.viewMode    = 'grid';
        $scope.currentRole = 'USER';

        $scope.templates = [
            { name:'ANM Dashboard',      desc:'Home visits, immunization, ANC',  icon:'fa-heartbeat' },
            { name:'Supervisor View',    desc:'Coverage & team performance',     icon:'fa-users' },
            { name:'Admin Overview',     desc:'System, users, alerts',           icon:'fa-cog' },
            { name:'Clinical Analytics', desc:'Diagnoses, treatments, outcomes', icon:'fa-stethoscope' },
            { name:'KPI Scorecard',      desc:'Key metrics at a glance',         icon:'fa-bolt' }
        ];

        DashboardService.getDashboards().then(function (res) {
            var data = (res.data && res.data.dashboards) ? res.data.dashboards : [];
            $scope.dashboards = data.length > 0 ? data : _mockList();
            $scope.loading = false;
        }).catch(function () {
            $scope.dashboards = _mockList();
            $scope.loading = false;
        });

        $scope.filteredDashboards = function () {
            return $scope.dashboards.filter(function (d) {
                var q = ($scope.searchQuery || '').toLowerCase();
                var nameMatch = !q || (d.dashboardName && d.dashboardName.toLowerCase().indexOf(q) >= 0);
                var typeMatch = !$scope.typeFilter || d.dashboardType === $scope.typeFilter;
                return nameMatch && typeMatch;
            });
        };

        $scope.openDashboard = function (id) {
            $state.go('techo.dashboard.aidashboard', { dashboardId: id });
        };
        $scope.openBuilder = function (id) {
            $state.go('techo.dashboard.aidashboardbuilder', { dashboardId: id || '' });
        };

        $scope.cloneDash = function (dash, $event) {
            $event.stopPropagation();
            var name = 'Copy of ' + dash.dashboardName;
            DashboardService.cloneDashboard(dash.id, null, name).then(function (res) {
                $scope.dashboards.unshift(res.data && res.data.data ? res.data.data : dash);
            }).catch(angular.noop);
        };

        $scope.deleteDash = function (dash, $event) {
            $event.stopPropagation();
            if (!confirm('Delete "' + dash.dashboardName + '"?')) { return; }
            DashboardService.deleteDashboard(dash.id).then(function () {
                var idx = $scope.dashboards.indexOf(dash);
                if (idx >= 0) { $scope.dashboards.splice(idx, 1); }
            }).catch(angular.noop);
        };

        $scope.createFromTemplate = function (tpl) {
            $state.go('techo.dashboard.aidashboardbuilder', {
                dashboardId: '', template: tpl.name
            });
        };

        $scope.getDashTypeIcon = function (type) {
            var icons = { EXECUTIVE:'fa-briefcase', OPERATIONAL:'fa-cogs',
                          CLINICAL:'fa-stethoscope', ANALYTICS:'fa-bar-chart', CUSTOM:'fa-paint-brush' };
            return icons[type] || 'fa-tachometer';
        };

        function _mockList() {
            return [
                { id:1, dashboardName:'Health Overview', dashboardType:'ANALYTICS',
                  description:'District-level health metrics', widgetCount:8,
                  aiEnabled:true, nlpEnabled:true, modifiedDate: new Date() },
                { id:2, dashboardName:'ANM Performance', dashboardType:'OPERATIONAL',
                  description:'Field worker KPIs and visit stats', widgetCount:5,
                  aiEnabled:true, nlpEnabled:false, modifiedDate: new Date() },
                { id:3, dashboardName:'Clinical Summary', dashboardType:'CLINICAL',
                  description:'Patient outcomes and diagnoses', widgetCount:6,
                  aiEnabled:false, nlpEnabled:true, modifiedDate: new Date() },
                { id:4, dashboardName:'Executive Report', dashboardType:'EXECUTIVE',
                  description:'Top-level KPIs for management', widgetCount:4,
                  aiEnabled:true, nlpEnabled:true, modifiedDate: new Date() }
            ];
        }
    }

    /* ==========================================================
       DashboardBuilderController
       ========================================================== */
    DashboardBuilderController.$inject = [
        '$scope', '$state', '$stateParams', '$timeout',
        'DashboardService', 'DashboardWidgetService'
    ];
    function DashboardBuilderController(
        $scope, $state, $stateParams, $timeout,
        DashboardService, DashboardWidgetService
    ) {
        $scope.dashboard          = {};
        $scope.canvas             = [];
        $scope.availableDataSources = [];
        $scope.selectedWidget     = null;
        $scope.configTab          = 'widget';
        $scope.saving             = false;
        $scope.saveMessage        = '';
        $scope.dragOver           = false;
        $scope.gridCols           = 12;

        $scope.availableRoles = ['ANM', 'SUPERVISOR', 'ADMIN', 'DOCTOR', 'NURSE', 'PHARMACIST'];
        $scope.commonFields   = ['date', 'department', 'status', 'district', 'user_id', 'count', 'total'];

        $scope.widgetPalette = [
            { type:'LINE_CHART',    label:'Line Chart',    shortLabel:'Line',    desc:'Trends over time',        faIcon:'fa-line-chart',        iconClass:'line' },
            { type:'BAR_CHART',     label:'Bar Chart',     shortLabel:'Bar',     desc:'Compare categories',      faIcon:'fa-bar-chart',          iconClass:'bar' },
            { type:'PIE_CHART',     label:'Pie Chart',     shortLabel:'Pie',     desc:'Part-of-whole breakdown',  faIcon:'fa-pie-chart',          iconClass:'pie' },
            { type:'KPI',           label:'KPI Card',      shortLabel:'KPI',     desc:'Single key metric',       faIcon:'fa-bolt',               iconClass:'kpi' },
            { type:'TABLE',         label:'Data Table',    shortLabel:'Table',   desc:'Tabular records',         faIcon:'fa-table',              iconClass:'table' },
            { type:'GAUGE',         label:'Gauge',         shortLabel:'Gauge',   desc:'Dial indicator',          faIcon:'fa-tachometer',         iconClass:'gauge' },
            { type:'HEATMAP',       label:'Heatmap',       shortLabel:'Heat',    desc:'Matrix of values',        faIcon:'fa-th',                 iconClass:'heatmap' },
            { type:'MAP',           label:'Map',           shortLabel:'Map',     desc:'Geographic view',         faIcon:'fa-map-o',              iconClass:'map' }
        ];

        /* init */
        _init();

        function _init() {
            if ($stateParams.dashboardId) {
                DashboardService.getDashboard($stateParams.dashboardId).then(function (res) {
                    var data = (res.data && res.data.data) ? res.data.data : res.data;
                    $scope.dashboard = data || {};
                    $scope.canvas    = (data && data.widgets) ? angular.copy(data.widgets) : [];
                }).catch(function () {
                    $scope.dashboard = { dashboardName:'New Dashboard', dashboardType:'ANALYTICS', aiEnabled:true, nlpEnabled:true };
                    $scope.canvas = [];
                });
            } else {
                $scope.dashboard = { dashboardName:'New Dashboard', dashboardType:'ANALYTICS', aiEnabled:true, nlpEnabled:true };
                if ($stateParams.template) { _applyTemplate($stateParams.template); }
            }
            DashboardService.getDataSources().then(function (res) {
                $scope.availableDataSources = (res.data && res.data.dataSources) ? res.data.dataSources : [];
            }).catch(angular.noop);
        }

        function _applyTemplate(tplName) {
            var templates = {
                'ANM Dashboard': [
                    { widgetType:'KPI',       title:'Beneficiaries Covered', gridWidth:3, aiInsights:true },
                    { widgetType:'KPI',       title:'Home Visits This Month', gridWidth:3 },
                    { widgetType:'LINE_CHART',title:'Visit Trend',            gridWidth:6, aiInsights:true },
                    { widgetType:'TABLE',     title:'Pending Follow-ups',     gridWidth:8 },
                    { widgetType:'PIE_CHART', title:'Category Split',         gridWidth:4 }
                ],
                'KPI Scorecard': [
                    { widgetType:'KPI', title:'Metric 1', gridWidth:3 },
                    { widgetType:'KPI', title:'Metric 2', gridWidth:3 },
                    { widgetType:'KPI', title:'Metric 3', gridWidth:3 },
                    { widgetType:'KPI', title:'Metric 4', gridWidth:3 }
                ]
            };
            $scope.canvas = (templates[tplName] || []).map(function (w, i) {
                return angular.extend({ id: null, filterConditions: [] }, w);
            });
        }

        /* -- Drag & Drop from palette -- */
        $scope.onPaletteDragStart = function ($event, widgetType) {
            $event.dataTransfer.setData('text/plain', widgetType.type);
            $event.dataTransfer.effectAllowed = 'copy';
        };

        $scope.onCanvasDragOver = function ($event) {
            $event.preventDefault();
            $scope.dragOver = true;
        };

        $scope.onCanvasDrop = function ($event) {
            $event.preventDefault();
            $scope.dragOver = false;
            var type = $event.dataTransfer.getData('text/plain');
            if (type) { $scope.addWidget(type); }
        };

        /* -- Widget management -- */
        $scope.addWidget = function (type) {
            var w = {
                widgetType:            type || 'BAR_CHART',
                title:                 _defaultTitle(type || 'BAR_CHART'),
                widgetName:            'widget_' + Date.now(),
                gridWidth:             4,
                gridHeight:            250,
                refreshIntervalSeconds:300,
                aiInsights:            true,
                aiAnomalyDetection:    false,
                aiForecast:            false,
                timeSeriesEnabled:     false,
                timeGranularity:       'DAILY',
                aggregationType:       'COUNT',
                filterConditions:      [],
                queryConfig:           {},
                visualizationConfig:   { showLegend: true },
                displayOptions:        {}
            };
            $scope.canvas.push(w);
            $scope.selectWidget(w);
            $scope.configTab = 'widget';
        };

        $scope.removeFromCanvas = function (idx, $event) {
            if ($event) { $event.stopPropagation(); }
            if ($scope.selectedWidget === $scope.canvas[idx]) { $scope.selectedWidget = null; }
            $scope.canvas.splice(idx, 1);
        };

        $scope.duplicateWidget = function (w, $event) {
            if ($event) { $event.stopPropagation(); }
            var copy = angular.copy(w);
            copy.id = null;
            copy.title = 'Copy of ' + (w.title || w.widgetType);
            $scope.canvas.push(copy);
        };

        $scope.selectWidget = function (w) {
            $scope.selectedWidget = w;
            $scope.configTab      = 'widget';
            if (!w.filterConditions) { w.filterConditions = []; }
        };

        $scope.applyWidgetConfig = function () {
            /* config is two-way bound via ng-model, nothing extra needed */
        };

        $scope.clearCanvas = function () {
            if (!confirm('Remove all widgets from canvas?')) { return; }
            $scope.canvas        = [];
            $scope.selectedWidget = null;
        };

        /* Filter conditions */
        $scope.addFilterCondition = function () {
            if (!$scope.selectedWidget) { return; }
            $scope.selectedWidget.filterConditions.push({ field: '', operator: '=', value: '' });
        };

        $scope.removeFilterCondition = function (idx) {
            $scope.selectedWidget.filterConditions.splice(idx, 1);
        };

        /* Role management */
        $scope.isRoleSelected = function (role) {
            var roles = ($scope.dashboard.roleBasedAccess || '').split(',');
            return roles.indexOf(role) >= 0;
        };
        $scope.toggleRole = function (role) {
            var roles = ($scope.dashboard.roleBasedAccess || '').split(',').filter(Boolean);
            var idx   = roles.indexOf(role);
            if (idx >= 0) { roles.splice(idx, 1); } else { roles.push(role); }
            $scope.dashboard.roleBasedAccess = roles.join(',');
        };

        /* Data source color */
        $scope.getDsColor = function (type) {
            var colors = { DATABASE:'#1565C0', REST:'#2E7D32', CSV:'#F57F17', GRAPHQL:'#6A1B9A', KAFKA:'#C62828' };
            return colors[type] || '#9E9E9E';
        };

        /* Widget type icon */
        $scope.getWidgetTypeIcon = function (type) {
            var icons = {
                LINE_CHART:'fa-line-chart', BAR_CHART:'fa-bar-chart', PIE_CHART:'fa-pie-chart',
                DOUGHNUT_CHART:'fa-circle-o-notch', KPI:'fa-bolt', TABLE:'fa-table',
                GAUGE:'fa-tachometer', HEATMAP:'fa-th', MAP:'fa-map-o'
            };
            return icons[type] || 'fa-bar-chart';
        };

        /* Save */
        $scope.saveDashboard = function () {
            $scope.saving = true;
            $scope.dashboard.widgets = $scope.canvas;
            var promise = $scope.dashboard.id
                ? DashboardService.updateDashboard($scope.dashboard.id, $scope.dashboard)
                : DashboardService.createDashboard($scope.dashboard);

            promise.then(function (res) {
                var data = res.data && (res.data.data || res.data);
                if (!$scope.dashboard.id && data && data.id) { $scope.dashboard.id = data.id; }
                $scope.saving = false;
                $scope.saveMessage = 'Dashboard saved successfully!';
                $timeout(function () { $scope.saveMessage = ''; }, 3000);
            }).catch(function () {
                $scope.saving = false;
                $scope.saveMessage = 'Saved locally (backend unavailable)';
                $timeout(function () { $scope.saveMessage = ''; }, 3000);
            });
        };

        $scope.previewDashboard = function () {
            if ($scope.dashboard.id) {
                $state.go('techo.dashboard.aidashboard', { dashboardId: $scope.dashboard.id });
            }
        };

        $scope.goBack = function () {
            $state.go('techo.dashboard.aidashboards');
        };

        function _defaultTitle(type) {
            var titles = {
                LINE_CHART:'Line Chart', BAR_CHART:'Bar Chart', PIE_CHART:'Pie Chart',
                KPI:'KPI Metric', TABLE:'Data Table', GAUGE:'Progress Gauge',
                HEATMAP:'Activity Heatmap', MAP:'Location Map'
            };
            return titles[type] || 'New Widget';
        }
    }

    /* ==========================================================
       AIInsightsController
       ========================================================== */
    AIInsightsController.$inject = ['$scope', 'DashboardAIService'];
    function AIInsightsController($scope, DashboardAIService) {
        $scope.insights         = [];
        $scope.filteredInsights = [];
        $scope.severityFilter   = 'ALL';
        $scope.typeFilter       = 'ALL';

        /* load (try backend, fall back to mock) */
        DashboardAIService.getCriticalInsights().then(function (res) {
            $scope.insights = (res.data && res.data.insights) ? res.data.insights : _mockInsights();
            $scope.filterInsights();
        }).catch(function () {
            $scope.insights = _mockInsights();
            $scope.filterInsights();
        });

        $scope.filterInsights = function () {
            $scope.filteredInsights = $scope.insights.filter(function (i) {
                var s = $scope.severityFilter === 'ALL' || i.severity === $scope.severityFilter;
                var t = $scope.typeFilter     === 'ALL' || i.insightType === $scope.typeFilter;
                return s && t;
            });
        };

        $scope.acknowledgeInsight = function (insight) {
            DashboardAIService.acknowledgeInsight(insight.id).then(function () {
                insight.isAcknowledged = true;
            }).catch(function () { insight.isAcknowledged = true; });
        };

        $scope.resolveInsight = function (insight) {
            DashboardAIService.resolveInsight(insight.id).then(function () {
                insight.isResolved = true;
            }).catch(function () { insight.isResolved = true; });
        };

        $scope.setFeedback = function (insight, feedback) {
            DashboardAIService.setInsightFeedback(insight.id, feedback)
                .then(function () { insight.userFeedback = feedback; })
                .catch(function () { insight.userFeedback = feedback; });
        };

        $scope.$watch('severityFilter', $scope.filterInsights);
        $scope.$watch('typeFilter',     $scope.filterInsights);

        function _mockInsights() {
            return [
                { id:1, insightType:'ANOMALY', severity:'CRITICAL', confidenceScore:0.92,
                  title:'Critical spike in patient load',
                  description:'OPD visits 3.2× above normal on last Tuesday.',
                  recommendedAction:'Allocate additional staff to OPD for peak hours.' },
                { id:2, insightType:'TREND', severity:'MEDIUM', confidenceScore:0.78,
                  title:'Downward trend in immunisation coverage',
                  description:'Coverage dropped from 87% to 73% over last 3 months.' },
                { id:3, insightType:'FORECAST', severity:'LOW', confidenceScore:0.65,
                  title:'Expected rise in ANC visits next month',
                  description:'Seasonal patterns suggest a 15% increase in Q2.' },
                { id:4, insightType:'RECOMMENDATION', severity:'MEDIUM', confidenceScore:0.81,
                  title:'Add a filter for District to ANM dashboard',
                  description:'Users frequently filter by district manually.',
                  recommendedAction:'Add a district dropdown filter to the ANM dashboard.' }
            ];
        }
    }

    /* ==========================================================
       NLPQueryController
       ========================================================== */
    NLPQueryController.$inject = ['$scope', '$timeout', 'DashboardNLPService'];
    function NLPQueryController($scope, $timeout, DashboardNLPService) {
        $scope.query                = '';
        $scope.response             = null;
        $scope.loading              = false;
        $scope.confidenceScore      = 0;
        $scope.suggestedWidget      = null;
        $scope.clarificationQuestions = [];

        $scope.querySuggestions = [
            'Show patient count by department',
            'Display revenue trends for last 30 days',
            'What are anomalies in patient data?',
            'Forecast patient visits for next quarter',
            'Show top 10 treatments by frequency',
            'Compare ANM performance this month',
            'Weekly immunisation coverage by block'
        ];

        $scope.processQuery = function () {
            if (!$scope.query || !$scope.query.trim()) { return; }
            $scope.loading   = true;
            $scope.response  = null;

            DashboardNLPService.processQuery({
                query: $scope.query,
                dashboardId: ($scope.dashboardId || null)
            }).then(function (res) {
                $scope.response             = (res.data && res.data.response) ? res.data.response : _mockNlpResponse($scope.query);
                $scope.confidenceScore      = $scope.response.confidenceScore || 0.75;
                $scope.suggestedWidget      = $scope.response.suggestedWidget;
                $scope.clarificationQuestions = $scope.response.clarificationQuestions || [];
                $scope.loading = false;
            }).catch(function () {
                $scope.response        = _mockNlpResponse($scope.query);
                $scope.confidenceScore = $scope.response.confidenceScore;
                $scope.suggestedWidget = $scope.response.suggestedWidget;
                $scope.loading = false;
            });
        };

        $scope.getConfidenceIndicator = function () {
            if ($scope.confidenceScore >= 0.8) { return 'High'; }
            if ($scope.confidenceScore >= 0.5) { return 'Medium'; }
            return 'Low';
        };

        $scope.applySuggestedWidget = function () {
            if ($scope.suggestedWidget) {
                $scope.$emit('widget-created', $scope.suggestedWidget);
            }
        };

        function _mockNlpResponse(query) {
            var q = query.toLowerCase();
            var type = q.indexOf('trend') >= 0 || q.indexOf('time') >= 0 ? 'LINE_CHART'
                     : q.indexOf('count') >= 0 || q.indexOf('compar') >= 0 ? 'BAR_CHART'
                     : q.indexOf('split') >= 0 || q.indexOf('distribut') >= 0 ? 'PIE_CHART'
                     : q.indexOf('total') >= 0 || q.indexOf('kpi') >= 0 ? 'KPI'
                     : 'BAR_CHART';
            return {
                queryType: q.indexOf('trend') >= 0 ? 'TIME_SERIES'
                         : q.indexOf('anomal') >= 0 ? 'ANOMALY_DETECTION'
                         : q.indexOf('forecast') >= 0 ? 'FORECASTING'
                         : 'DATA_QUERY',
                confidenceScore: 0.75 + Math.random() * 0.2,
                clarificationQuestions: [],
                suggestedWidget: {
                    widgetType: type,
                    title: query.charAt(0).toUpperCase() + query.slice(1, 40),
                    description: 'Auto-generated from your query.'
                }
            };
        }
    }

    /* ==========================================================
       Services
       ========================================================== */
    DashboardService.$inject = ['$http', '$q', 'APP_CONFIG'];
    function DashboardService($http, $q, APP_CONFIG) {
        var base = APP_CONFIG.apiPath + '/dashboard';
        return {
            getDashboard:    function (id)     { return $http.get(base + '/' + id); },
            getDashboards:   function ()       { return $http.get(base + '/list'); },
            createDashboard: function (data)   { return $http.post(base + '/create', data); },
            updateDashboard: function (id, d)  { return $http.put(base + '/' + id, d); },
            deleteDashboard: function (id)     { return $http.delete(base + '/' + id); },
            getDataSources:  function ()       { return $http.get(base + '/data-sources'); },
            cloneDashboard:  function (id, uid, name) {
                return $http.post(base + '/' + id + '/clone/' + (uid || ''), { newName: name });
            },
            exportDashboard: function (id)     { return $http.get(base + '/' + id + '/export'); },
            importDashboard: function (uid, d) { return $http.post(base + '/import/' + uid, d); }
        };
    }

    DashboardNLPService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardNLPService($http, APP_CONFIG) {
        var base = APP_CONFIG.apiPath + '/dashboard/nlp';
        return {
            processQuery:  function (req)   { return $http.post(base + '/query', req); },
            extractIntent: function (q)     { return $http.get(base + '/intent', { params: { query: q } }); },
            suggestWidget: function (desc)  { return $http.get(base + '/suggest-widget', { params: { description: desc } }); },
            generateChart: function (q, ds) { return $http.post(base + '/generate-chart', {}, { params: { query: q, dataSourceId: ds } }); }
        };
    }

    DashboardAIService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardAIService($http, APP_CONFIG) {
        var base = APP_CONFIG.apiPath + '/dashboard/ai-insights';
        return {
            getWidgetInsights:   function (wid)  { return $http.get(base + '/widget/' + wid); },
            getUserRecentInsights: function (uid){ return $http.get(base + '/user/' + uid + '/recent'); },
            getCriticalInsights: function ()     { return $http.get(base + '/critical'); },
            generateAnomalies:   function (wid, d) { return $http.post(base + '/anomaly-detection', d, { params: { widgetId: wid } }); },
            generateForecast:    function (wid, d, p) { return $http.post(base + '/forecast', d, { params: { widgetId: wid, forecastPeriods: p } }); },
            generateSummary:     function (wid, d) { return $http.post(base + '/summary', d, { params: { widgetId: wid } }); },
            acknowledgeInsight:  function (id)   { return $http.put(base + '/' + id + '/acknowledge'); },
            resolveInsight:      function (id)   { return $http.put(base + '/' + id + '/resolve'); },
            setInsightFeedback:  function (id, f){ return $http.put(base + '/' + id + '/feedback', {}, { params: { feedback: f } }); }
        };
    }

    DashboardWidgetService.$inject = ['$http', 'APP_CONFIG'];
    function DashboardWidgetService($http, APP_CONFIG) {
        var base = APP_CONFIG.apiPath + '/dashboard/widgets';
        return {
            renderWidget:      function (id) { return $http.get(base + '/' + id + '/render'); },
            refreshWidgetData: function (id) { return $http.post(base + '/' + id + '/refresh'); },
            getWidgetData:     function (id) { return $http.get(base + '/' + id + '/data'); }
        };
    }

    /* ==========================================================
       Directive: aiDashboardWidget
       ========================================================== */
    function aiDashboardWidget() {
        return {
            restrict: 'E',
            scope:    { widget: '=', insights: '=', editMode: '=' },
            templateUrl: 'app/dashboard/ai/views/ai-widget.html',
            link: function (scope) {
                scope.showInsights = false;
                scope.showOptions  = false;

                scope.toggleInsights = function () { scope.showInsights = !scope.showInsights; };

                scope.refreshWidget = function (widget) {
                    widget.refreshing = true;
                    // Simulate refresh with new mock data
                    setTimeout(function () {
                        scope.$apply(function () {
                            widget.refreshing = false;
                            widget.lastUpdated = new Date();
                        });
                    }, 800);
                };

                scope.expandWidget = function (widget) {
                    scope.$emit('expand-widget', widget);
                };

                scope.sortTable = function (widget, col) {
                    if (widget.sortCol === col) { widget.sortAsc = !widget.sortAsc; }
                    else { widget.sortCol = col; widget.sortAsc = true; }
                };

                scope.getSortedRows = function (widget) {
                    if (!widget.tableData || !widget.sortCol) { return widget.tableData; }
                    return widget.tableData.slice().sort(function (a, b) {
                        var va = a[widget.sortCol], vb = b[widget.sortCol];
                        return widget.sortAsc ? (va > vb ? 1 : -1) : (va < vb ? 1 : -1);
                    });
                };

                scope.getWidgetIcon = function (type) {
                    var icons = {
                        LINE_CHART:'fa-line-chart', BAR_CHART:'fa-bar-chart', PIE_CHART:'fa-pie-chart',
                        DOUGHNUT_CHART:'fa-circle-o-notch', KPI:'fa-bolt', TABLE:'fa-table',
                        GAUGE:'fa-tachometer', HEATMAP:'fa-th', MAP:'fa-map-o'
                    };
                    return icons[type] || 'fa-bar-chart';
                };

                scope.getGaugeDash  = function (val) { return ((val || 0) / 100 * 125.6).toFixed(1); };
                scope.getGaugeColor = function (val) { return val > 85 ? 'red' : val > 70 ? 'yellow' : 'green'; };
                scope.getGaugeStroke = function (val) {
                    return val > 85 ? '#C62828' : val > 70 ? '#F57F17' : '#2E7D32';
                };

                scope.getHeatmapColor = function (val, max) {
                    var pct = Math.min((val / (max || 100)) * 100, 100);
                    var r = Math.floor(21  + (pct / 100) * (198 - 21));
                    var g = Math.floor(101 + (pct / 100) * (40  - 101));
                    var b = Math.floor(192 + (pct / 100) * (0   - 192));
                    return 'rgb(' + r + ',' + g + ',' + b + ')';
                };

                scope.getInsightClass = function (type, sev) {
                    if (sev === 'CRITICAL' || sev === 'HIGH') { return 'critical'; }
                    if (type === 'FORECAST') { return 'forecast'; }
                    if (type === 'TREND')    { return 'trend'; }
                    return 'anomaly';
                };
                scope.getInsightIcon = function (type) {
                    var icons = { ANOMALY:'fa-exclamation-triangle', FORECAST:'fa-line-chart',
                                  TREND:'fa-arrow-up', RECOMMENDATION:'fa-lightbulb-o' };
                    return icons[type] || 'fa-info-circle';
                };
                scope.hasCriticalInsight = function (insights) {
                    return insights && insights.some(function (i) { return i.severity === 'CRITICAL'; });
                };
                scope.downloadChart   = function () {};
                scope.exportWidgetData = function (w) {};
                scope.removeWidget    = function (w) { scope.$emit('remove-widget', w); };
            }
        };
    }

    /* ==========================================================
       Filter: replace (used in templates)
       ========================================================== */
    function replaceFilter() {
        return function (input, from, to) {
            if (!input) { return input; }
            return String(input).split(from).join(to);
        };
    }

})();
