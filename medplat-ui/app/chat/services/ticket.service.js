(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.service('TicketService', ['$window', '$q', '$timeout', function ($window, $q, $timeout) {
        var STORAGE_KEY = 'chat_tickets_v1';

        function load() {
            var raw = $window.localStorage.getItem(STORAGE_KEY);
            return raw ? JSON.parse(raw) : [];
        }

        function save(list) {
            $window.localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
        }

        this.list = function () {
            return $q.resolve(load());
        };

        this.create = function (data) {
            var tickets = load();
            var id = 'TKT-' + Date.now();
            var ticket = angular.extend({ id: id, status: 'open', createdAt: Date.now() }, data);
            tickets.unshift(ticket);
            save(tickets);
            // simulate async and notification
            return $timeout(function () { return ticket; }, 300);
        };

        this.updateStatus = function (id, status) {
            var tickets = load();
            var t = tickets.find(function (x) { return x.id === id; });
            if (t) t.status = status;
            save(tickets);
            return $q.resolve(t);
        };

        this.clear = function () { save([]); return $q.resolve(); };
    }]);
}());
