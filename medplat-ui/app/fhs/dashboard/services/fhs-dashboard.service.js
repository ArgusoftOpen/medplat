(function () {
    function FhsDashboardService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/familyhealthsurvey/:action/:id', {}, {
            getLastUpdateTime: {
                method: 'GET',
                params: {
                    action: 'updateTime'
                },
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            }
        });
        return {
            retireveFamiliesAndMembers: function (locationId) {
                return api.query({ action: 'familiesandmembers', "locationId": locationId }).$promise;
            },
            familiesAndMembersByLocationId: function (locationId) {
                return api.query({ action: 'familiesandmembers', id: 'locationId', "locationId": locationId }).$promise;
            },
            getVillagesName: function () {
                return api.query({
                    action: 'families', id: 'villages'
                }).$promise;
            },
            getStarPerformers: function () {
                return api.query({ action: 'familiesandmembers', id: 'starperformersoftheday' }).$promise
            },
            getLastUpdateTime: function () {
                return api.getLastUpdateTime().$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('FhsDashboardService', FhsDashboardService);
})();
