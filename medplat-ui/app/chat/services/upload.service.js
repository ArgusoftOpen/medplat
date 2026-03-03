(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.service('UploadService', ['$q', '$window', '$http', function ($q, $window, $http) {
        var STORE = 'chat_uploads_v1';
        var API = window.UPLOAD_API_ENDPOINT || null;
        var TOKEN = window.UPLOAD_API_TOKEN || null;
        function load() { var raw = $window.localStorage.getItem(STORE); return raw ? JSON.parse(raw) : {}; }
        function save(s) { $window.localStorage.setItem(STORE, JSON.stringify(s)); }

        this.saveFile = function (file) {
            var deferred = $q.defer();
            if (API) {
                var fd = new FormData();
                fd.append('file', file);
                var cfg = { headers: { 'Content-Type': undefined } };
                if (TOKEN) cfg.headers.Authorization = 'Bearer ' + TOKEN;
                $http.post(API, fd, cfg).then(function (r) {
                    // assume server returns metadata
                    deferred.resolve(r.data);
                }, function (err) { deferred.reject(err); });
                return deferred.promise;
            }

            // fallback: save metadata only for mock
            var s = load();
            var id = 'UP-' + Date.now() + '-' + Math.floor(Math.random() * 1000);
            s[id] = { id: id, name: file.name, size: file.size, type: file.type, ts: Date.now() };
            save(s);
            deferred.resolve(s[id]);
            return deferred.promise;
        };

        this.list = function () { return $q.resolve(Object.values(load())); };
    }]);
}());
