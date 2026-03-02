'use strict';
(function () {
    function DistrictPerformanceDashboardDao($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/districtfactsheet/:action/:subaction/:id', {},
            {
                downloadMedia: {
                    method: 'GET',
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                },
            });
        return {
            downloadMedia: function (locationId, financialYear) {
                return api.downloadMedia({ action: 'downloadexcel', locationId, financialYear }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('DistrictPerformanceDashboardDao', DistrictPerformanceDashboardDao);
})();
