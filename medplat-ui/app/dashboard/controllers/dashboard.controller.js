(function () {
    'use strict';

    angular.module('imtecho').controller('DashboardController', DashboardController);

    function DashboardController($scope, $state, DashboardService, GeneralUtil) {
        var vm = this;

        vm.$onInit = function () {
            vm.loading = true;
            vm.dashboardData = {};
            vm.error = null;
            loadDashboardData();
        };

        function loadDashboardData() {
            DashboardService.getDashboardData()
                .then(function (response) {
                    vm.dashboardData = response.data;
                    vm.loading = false;
                })
                .catch(function (error) {
                    vm.error = 'Failed to load dashboard data';
                    vm.loading = false;
                    GeneralUtil.showMessageOnApiFailure(error);
                });
        }

        vm.navigateToModule = function (module) {
            switch (module) {
                case 'admin':
                    $state.go('admin');
                    break;
                case 'manage':
                    $state.go('manage');
                    break;
                case 'ncd':
                    $state.go('ncd');
                    break;
                case 'training':
                    $state.go('training');
                    break;
                default:
                    console.warn('Unknown module:', module);
            }
        };

        vm.refreshDashboard = function () {
            vm.loading = true;
            loadDashboardData();
        };

        vm.getActivityIconClass = function (activityType) {
            switch (activityType) {
                case 'user_login':
                    return 'text-success';
                case 'record_created':
                    return 'text-info';
                case 'data_sync':
                    return 'text-warning';
                default:
                    return 'text-muted';
            }
        };
    }
})();
