'use strict';
(function () {
    function RoleService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/role/:action/:id', {}, {
            retrieveFeaturesByRole: {
                method: 'GET',
                isArray: true
            },
            getRoleByRoleName: {
                method: 'GET'
            },
            getAllActiveRoles: {
                method: 'GET',
                params: {
                    is_active: 'true'
                },
                isArray: true
            },
            getAllRoles: {
                method: 'GET',
                isArray: true
            },
            toggleActive: {
                method: 'PUT'
            },
            createOrUpdate: {
                method: 'POST',
                isArray: true
            },
            retrieveById: {
                method: 'GET'
            }
        });
        return {
            retrieveFeaturesByRole: function (ids) {
                return api.retrieveFeaturesByRole({ action: 'feature', roleId: ids }, {}).$promise;
            },
            getRoleByRoleName: function (name) {
                return api.getRoleByRoleName({ action: 'role', subaction: name }).$promise;
            },
            getAllActiveRoles: function () {
                return api.getAllActiveRoles().$promise;
            },
            getAllRoles: function () {
                return api.getAllRoles().$promise;
            },
            toggleActive: function (roleDto, isActive) {
                return api.toggleActive({ is_active: isActive }, roleDto).$promise;
            },
            createOrUpdate: function (roleDto) {
                return api.createOrUpdate({}, roleDto).$promise;
            },
            retrieveById: function (id) {
                return api.retrieveById({ id: id }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('RoleService', RoleService);
})();
