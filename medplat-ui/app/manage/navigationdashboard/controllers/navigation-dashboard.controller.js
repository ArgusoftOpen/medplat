(function () {
    function NavigationDashboardController(Mask, QueryDAO, GeneralUtil, $scope, $state) {
        var navDashboardCtrl = this;
        
        // Initialize dashboard data
        navDashboardCtrl.quickAccessItems = [
            {
                title: 'Performance Dashboard',
                description: 'View performance metrics and analytics',
                icon: 'fa-chart-line',
                state: 'techo.manage.performancedashboard',
                color: 'primary'
            },
            {
                title: 'District Performance',
                description: 'District-wise performance data',
                icon: 'fa-map-marked-alt',
                state: 'techo.manage.districtperformancedashboard',
                color: 'success'
            },
            {
                title: 'FHS Dashboard',
                description: 'Field Health Worker dashboard',
                icon: 'fa-user-md',
                state: 'techo.fhs.dashboard',
                color: 'info'
            },
            {
                title: 'NCD Dashboard',
                description: 'Non-communicable diseases data',
                icon: 'fa-heartbeat',
                state: 'techo.ncd.dashboard',
                color: 'warning'
            }
        ];

        navDashboardCtrl.recentActivities = [
            {
                title: 'Data Sync Completed',
                time: '2 hours ago',
                type: 'success',
                icon: 'fa-sync'
            },
            {
                title: 'New Report Generated',
                time: '5 hours ago',
                type: 'info',
                icon: 'fa-file-alt'
            },
            {
                title: 'System Update Available',
                time: '1 day ago',
                type: 'warning',
                icon: 'fa-download'
            }
        ];

        navDashboardCtrl.systemStats = {
            totalUsers: 1250,
            activeUsers: 892,
            dataSynced: '98.5%',
            systemHealth: 'Good'
        };

        navDashboardCtrl.navigateTo = function(state) {
            if (state && $state.get(state)) {
                $state.go(state);
            } else {
                console.warn('State not found:', state);
            }
        };

        navDashboardCtrl.refreshData = function() {
            Mask.show();
            // Simulate data refresh
            setTimeout(function() {
                navDashboardCtrl.systemStats.activeUsers = Math.floor(Math.random() * 200) + 800;
                Mask.hide();
            }, 1000);
        };

        navDashboardCtrl.init = function() {
            // Initialize any required data
            console.log('Navigation Dashboard initialized');
        };

        navDashboardCtrl.init();
    }

    angular.module('imtecho.controllers').controller('NavigationDashboardController', NavigationDashboardController);
})();
