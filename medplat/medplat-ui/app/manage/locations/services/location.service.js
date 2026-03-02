'use strict';
(function () {
    function LocationService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/location/:action/:subaction/:query/:id', {}, {});
        return {
            retrieveNextLevel: function (locationIds, level, fetchAccordingToUserAOI, languagePreference) {
                let ids = [];
                if (!Array.isArray(locationIds)) {
                    ids.push(locationIds);
                } else {
                    ids = locationIds;
                }
                return api.query({ action: 'hierarchy', locationIds: ids, level: level, fetchAccordingToUserAOI: fetchAccordingToUserAOI, languagePreference: languagePreference }).$promise;
            },
            retrieveLocationByRoleId: function (roleId) {
                return api.get({ action: 'byroleid', role_id: roleId }).$promise;
            },
            retrieveLocationTypes: function () {
                return api.query({ action: 'locationtypes' }).$promise;
            },
            retrieveLocationType: function (type) {
                return api.query({ action: 'locationtype', type: type }).$promise;
            },
            retrieveNextLevelOfGivenLocationId: function (locationId) {
                return api.query({ action: 'childlocations', locationId: locationId }).$promise;
            },
            createOrUpdate: function (location) {
                return api.save(location).$promise;
            },
            getParent: function (locationId, languagePreference) {
                return api.get({ action: 'parent', locationId: locationId, languagePreference: languagePreference }).$promise;
            },
            retrieveById: function (id) {
                return api.get({ action: 'byid', location_id: id }).$promise;
            },
            createOrUpdateWardUPHCs: function (id, dto) {
                return api.save({ action: 'ward', subaction: 'uphcmapping', id }, dto).$promise;
            },
            getenghiererchystring: function (id) {
                return api.get({ action: 'getenghiererchystring', locationId: id }).$promise;
            },
            createOrUpdateHealthInfrastructureWards: function (dto) {
                return api.save({ action: 'healthInfrastructure', subaction: 'wardDetails' }, dto).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('LocationService', LocationService);
})();
