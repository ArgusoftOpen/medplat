(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.service('TicketService', ['$window', '$q', '$timeout', '$http', function ($window, $q, $timeout, $http) {
        var STORAGE_KEY = 'chat_tickets_v1';
        var API = window.TICKET_API_ENDPOINT || null;

        function loadLocal() {
            var raw = $window.localStorage.getItem(STORAGE_KEY);
            return raw ? JSON.parse(raw) : [];
        }

        function saveLocal(list) {
            $window.localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
        }

        this.list = function (opts) {
            opts = opts || {};
            if (API) {
                // support server-side filtering/paging if backend supports it
                return $http.get(API, { params: opts }).then(function (r) { return r.data || []; });
            }
            return $q.resolve(loadLocal());
        };

        this.create = function (data) {
            if (API) {
                return $http.post(API, data).then(function (r) { return r.data; });
            }
            var tickets = loadLocal();
            var id = 'TKT-' + Date.now();
            var ticket = angular.extend({ id: id, status: 'open', createdAt: Date.now() }, data);
            tickets.unshift(ticket);
            saveLocal(tickets);
            return $timeout(function () { return ticket; }, 300);
        };

        this.updateStatus = function (id, status) {
            if (API) {
                return $http.patch((API + '/' + encodeURIComponent(id)), { status: status }).then(function (r) { return r.data; });
            }
            var tickets = loadLocal();
            var t = tickets.find(function (x) { return x.id === id; });
            if (t) t.status = status;
            saveLocal(tickets);
            return $q.resolve(t);
        };

        this.clear = function () { saveLocal([]); return $q.resolve(); };
    }]);
}());
