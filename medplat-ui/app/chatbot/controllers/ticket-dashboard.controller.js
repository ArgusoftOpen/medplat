(function () {
    'use strict';
    angular.module('imtecho')
        .controller('TicketDashboardController', TicketDashboardController);

    TicketDashboardController.$inject = ['$scope', 'ChatbotService', 'PagingService'];

    function TicketDashboardController($scope, ChatbotService, PagingService) {
        var vm = this;

        vm.tickets = [];
        vm.loading = false;
        vm.filter = {
            status: 'ALL',
            priority: 'ALL'
        };

        vm.loadTickets = function () {
            vm.loading = true;
            ChatbotService.getTickets(vm.filter).then(function (response) {
                $scope.$apply(function () {
                    vm.tickets = response.data.content;
                    vm.loading = false;
                });
            });
        };

        vm.getStatusClass = function (status) {
            switch (status) {
                case 'OPEN': return 'badge-info';
                case 'IN_PROGRESS': return 'badge-warning';
                case 'CLOSED': return 'badge-success';
                default: return 'badge-secondary';
            }
        };

        vm.loadTickets();
    }
})();
