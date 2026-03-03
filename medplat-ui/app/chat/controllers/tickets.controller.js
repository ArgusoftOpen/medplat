(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.controller('TicketsController', ['TicketService', function (TicketService) {
        var vm = this;
        vm.tickets = [];
        vm.filter = 'all';

        function load() { TicketService.list().then(function (list) { vm.tickets = list; }); }
        vm.changeStatus = function (t, status) { TicketService.updateStatus(t.id, status).then(load); };
        vm.filtered = function () { return vm.tickets.filter(function (x) { return vm.filter === 'all' ? true : x.status === vm.filter; }); };
        load();
    }]);
}());
