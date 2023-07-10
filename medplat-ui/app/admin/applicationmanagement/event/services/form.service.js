(function () {
    function FormDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/form/:action/:id', {}, {});
        return {
            retrieveAll: function (dto) {
                return api.query(dto).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('FormDAO', FormDAO);
})();
