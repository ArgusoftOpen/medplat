(function () {
    function ExpectedTargetDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/cmdashboardui/expected_target/:action', {}, {
            createOrUpdate: {
                method: 'POST',
            },
            expectedTargetByLocationIdAndYear: {
                method: 'GET'
            },
            expectedTargetByLocationIdAndYearOfParent: {
                method: 'GET',
                isArray: true
            },
            unlockLocations: {
                method: 'PUT'
            }
        });
        return {
            createOrUpdate: function (expectedTargetDto) {
                return api.createOrUpdate(expectedTargetDto).$promise;
            },
            expectedTargetByLocationIdAndYear: function (locationId, financialYear) {
                return api.expectedTargetByLocationIdAndYear({ action: 'targetbylocation', locationId, financialYear }).$promise;
            },
            expectedTargetByLocationIdAndYearOfParent: function (locationId, financialYear) {
                return api.expectedTargetByLocationIdAndYearOfParent({ action: 'targetbyparent', locationId, financialYear }).$promise;
            },
            unlockLocations: function (unlockDtos) {
                return api.unlockLocations({ action: 'unlocklocations' }, unlockDtos).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('ExpectedTargetDAO', ['$resource', 'APP_CONFIG', ExpectedTargetDAO]);
})();
