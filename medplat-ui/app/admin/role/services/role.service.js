(function () {
    function RoleDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/role/:action/:id', {}, {});
        return {
            retireveAll: function (isActive) {
                return api.query({ "is_active": isActive }).$promise;
            },
            retieveRolesByRoleId: function (isAdmin) {
                return api.query({ "action": "byroleid", is_admin: isAdmin }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('RoleDAO', ['$resource', 'APP_CONFIG', RoleDAO]);
})();
