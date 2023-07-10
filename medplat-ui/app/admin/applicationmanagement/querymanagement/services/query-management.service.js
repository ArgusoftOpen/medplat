(function () {
    function QueryManagementDao($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/querymanagement/:action/:subaction/:id', {}, {
            execute: {
                method: 'POST',
                isArray: true
            },
            retrieveQuery: {
                method: 'POST',
                isArray: true
            }
        });
        return {
            execute: function (query) {
                return api.execute({ action: 'execute' }, query).$promise;
            },
            retrieveQuery: function (query) {
                return api.retrieveQuery({ action: 'retrieve' }, query).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('QueryManagementDao', ['$resource', 'APP_CONFIG', QueryManagementDao]);
})();
