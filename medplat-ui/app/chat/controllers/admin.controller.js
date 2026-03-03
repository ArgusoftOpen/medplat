(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.controller('AdminController', ['TicketService', function (TicketService) {
        var vm = this;
        vm.summary = { open: 0, inprogress: 0, closed: 0, total: 0 };

        function refresh() {
            TicketService.list().then(function (list) {
                vm.summary.total = list.length;
                vm.summary.open = list.filter(function (x) { return x.status === 'open'; }).length;
                vm.summary.inprogress = list.filter(function (x) { return x.status === 'inprogress'; }).length;
                vm.summary.closed = list.filter(function (x) { return x.status === 'closed'; }).length;
            });
        }
        refresh();
        vm.refresh = refresh;
    }]);
}());
