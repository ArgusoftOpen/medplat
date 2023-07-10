'use strict';
(function () {
    function ConstantService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/constants/:action/:subaction/:query/:id', {}, {});
        return {
            getConstantsByKey: function (key) {
                return api.query({ key: key }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('ConstantService', ConstantService);
})();
