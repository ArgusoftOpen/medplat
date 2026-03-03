(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.service('UploadService', ['$q', '$window', function ($q, $window) {
        var STORE = 'chat_uploads_v1';
        function load() { var raw = $window.localStorage.getItem(STORE); return raw ? JSON.parse(raw) : {}; }
        function save(s) { $window.localStorage.setItem(STORE, JSON.stringify(s)); }

        this.saveFile = function (file) {
            // save metadata only for mock; use FileReader to store base64 if needed
            var deferred = $q.defer();
            var s = load();
            var id = 'UP-' + Date.now() + '-' + Math.floor(Math.random()*1000);
            s[id] = { id: id, name: file.name, size: file.size, type: file.type, ts: Date.now() };
            save(s);
            deferred.resolve(s[id]);
            return deferred.promise;
        };

        this.list = function () { return $q.resolve(Object.values(load())); };
    }]);
}());
