(function () {
    'use strict';
    angular.module('imtecho')
        .controller('AdminChatbotDashboardController', AdminChatbotDashboardController);

    AdminChatbotDashboardController.$inject = ['$scope', 'ChatbotService'];

    function AdminChatbotDashboardController($scope, ChatbotService) {
        var vm = this;

        vm.stats = {
            totalTickets: 154,
            resolvedTickets: 120,
            avgResolutionTime: '4.2 hrs',
            userSatisfaction: '4.8/5'
        };

        vm.trends = [
            { category: 'Technical', count: 45 },
            { category: 'Clinical', count: 82 },
            { category: 'Administrative', count: 12 },
            { category: 'Other', count: 15 }
        ];
    }
})();
