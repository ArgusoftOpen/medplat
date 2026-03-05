(function () {
    'use strict';

    angular.module('imtecho')
        .controller('NavigationDashboardController', NavigationDashboardController);

    NavigationDashboardController.$inject = ['$scope', '$state', 'GeneralUtilService'];

    function NavigationDashboardController($scope, $state, GeneralUtilService) {
        var dashboardCtrl = this;

        // Initialize dashboard data
        dashboardCtrl.activeUsers = 0;
        dashboardCtrl.pendingTasks = 0;
        dashboardCtrl.todayActivities = 0;

        // Initialize the controller
        function init() {
            loadDashboardData();
        }

        // Load dashboard statistics
        function loadDashboardData() {
            // Mock data for demonstration - replace with actual API calls
            dashboardCtrl.activeUsers = 1247;
            dashboardCtrl.pendingTasks = 38;
            dashboardCtrl.todayActivities = 156;

            // In a real implementation, you would make API calls like:
            // GeneralUtilService.getDashboardStats().then(function(response) {
            //     dashboardCtrl.activeUsers = response.activeUsers;
            //     dashboardCtrl.pendingTasks = response.pendingTasks;
            //     dashboardCtrl.todayActivities = response.todayActivities;
            // });
        }

        // Navigate to specific module
        dashboardCtrl.navigateToModule = function(moduleState) {
            if (moduleState) {
                $state.go(moduleState);
            }
        };

        // Refresh dashboard data
        dashboardCtrl.refresh = function() {
            loadDashboardData();
        };

        // Initialize on load
        init();
    }
})();
