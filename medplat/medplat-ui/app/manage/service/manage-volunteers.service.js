(function () {
    function VolunteersDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/volunteers/:action/:subaction/:id', {},
            {
                createOrUpdate: {
                    method: 'POST'
                },
                retrieveData: {
                    method: 'GET'
                }
            });
        return {
            createOrUpdate: function (volunteersDto) {
                return api.createOrUpdate({}, volunteersDto).$promise;
            },
            retrieveData: function (healthInfrastructureId, monthYear) {
                return api.retrieveData({
                    action: 'retrievedata',
                    healthInfrastructureId: healthInfrastructureId,
                    monthYear: monthYear
                }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('VolunteersDAO', ['$resource', 'APP_CONFIG', VolunteersDAO]);
})();
