(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.controller('TicketsController', ['TicketService', '$interval', function (TicketService, $interval) {
        var vm = this;
        vm.tickets = [];
        vm.filter = 'all';
        vm.search = '';
        vm.page = 1;
        vm.pageSize = 10;
        vm.total = 0;

        function applyFilter(list) {
            var filtered = list.filter(function (x) {
                var ok = vm.filter === 'all' ? true : x.status === vm.filter;
                if (vm.search) {
                    var s = vm.search.toLowerCase();
                    ok = ok && ((x.title && x.title.toLowerCase().indexOf(s) !== -1) || (x.description && x.description.toLowerCase().indexOf(s) !== -1) || (x.id && x.id.toLowerCase().indexOf(s) !== -1));
                }
                return ok;
            });
            vm.total = filtered.length;
            var start = (vm.page - 1) * vm.pageSize;
            return filtered.slice(start, start + vm.pageSize);
        }

        function load() {
            // request server list with optional paging if API supports it
            var opts = { page: vm.page, pageSize: vm.pageSize, status: vm.filter, q: vm.search };
            TicketService.list(opts).then(function (list) {
                vm._all = list;
                vm.tickets = applyFilter(list);
            });
        }

        vm.changeStatus = function (t, status) { TicketService.updateStatus(t.id, status).then(load); };
        vm.setPage = function (p) { vm.page = p; load(); };
        vm.setPageSize = function (s) { vm.pageSize = s; vm.page = 1; load(); };
        vm.searchChanged = function () { vm.page = 1; load(); };

        // polling for realtime-ish updates; if backend supports websockets, replace with socket logic
        var poll = $interval(load, 5000);

        load();

        // cleanup interval
        var dereg = function () { if (poll) $interval.cancel(poll); };
        // AngularJS: listen for $destroy on scope via $rootScope? Use $scope if available via DI; fallback to window unload
        window.addEventListener('beforeunload', dereg);
    }]);
}());
