(function () {
    function FacilityPerformanceDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/facilitydata/:hid', {}, {
            createOrUpdate: {
                method: 'POST'
            },
            getFacilityPerformaceByHidAndDate: {
                method: 'GET'
            }
        });
        return {
            createOrUpdate: function (facilityPerformanceDto) {
                return api.createOrUpdate(facilityPerformanceDto).$promise;
            },
            getFacilityPerformaceByHidAndDate: function (hid, performaceDate) {
                return api.getFacilityPerformaceByHidAndDate({ hid: hid, performaceDate: performaceDate }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('FacilityPerformanceDAO', ['$resource', 'APP_CONFIG', FacilityPerformanceDAO]);
})();
