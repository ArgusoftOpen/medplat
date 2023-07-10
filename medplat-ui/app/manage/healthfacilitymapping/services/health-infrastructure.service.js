(function () {
    function HealthInfraService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/health-infra/:action/:id', {}, {
            getData: {
                method: 'GET'
            },
            postData: {
                method: 'POST'
            },
            deleteHealthInfra: {
                method: 'DELETE',
                params: {
                    action: 'deleteHealthInfra'
                }
            }
        });
        return {
            getHealthInfrastructureById: function (healthInfraId) {
                return api.getData({ action: 'get-health-infra-by-id',id: healthInfraId }).$promise
            },
            saveAndLinkHFRId: function (healthInfraId, hfrFacilityId) {
                return api.postData({ action: 'store-link-hfr-id', id: healthInfraId, hfrFacilityId },{}).$promise
            },
            linkHFRIdToBridge: function (healthInfraId) {
                return api.postData({ action: 'link-hfr-id-to-bridge',id: healthInfraId }, {}).$promise
            },
            deleteHealthInfra: function (id) {
                return api.deleteHealthInfra(id).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('HealthInfraService', HealthInfraService);
})();