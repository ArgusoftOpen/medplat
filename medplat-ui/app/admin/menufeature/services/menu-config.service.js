(function () {
    function MenuConfigDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/menuconfig/:action/:id', {}, {
            getConfigurationTypes: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'configtypes'
                }
            },
            getConfigurationTypeById: {
                method: 'GET',
                params: {
                    action: 'configtypes'
                }
            },
            getMenuConfigByType: {
                method: 'GET',
                isArray: true
            },
            saveMenuItem: {
                method: 'POST',
                params: {
                    action: 'menuitem'
                }
            },
            assignNewFeature: {
                method: 'POST',
                params: {
                    action: 'assignnewfeature'
                }
            },
            deleteConfig: {
                method: 'DELETE',
                params: {
                    action: 'usermenuitem'
                }
            },
            getMenuItemsForUser: {
                method: 'GET',
            },
            updateUserMenuItem: {
                method: 'POST',
                params: {
                    action: 'updateusermenuitem'
                }
            }
        });
        return {
            getConfigurationTypes: function () {
                return api.getConfigurationTypes().$promise;
            },
            getConfigurationTypeById: function (data) {
                return api.getConfigurationTypeById(data).$promise;
            },
            getMenuConfigByType: function (data) {
                return api.getMenuConfigByType(data).$promise;
            },
            saveMenuItem: function (menuConfigId, data) {
                var params = {
                    menuConfigId: menuConfigId
                };
                return api.saveMenuItem(params, data).$promise;
            },
            assignNewFeature: function (roleId, data) {
                var params = {
                    roleId: roleId
                };
                return api.assignNewFeature(params, data).$promise;
            },
            deleteConfig: function (id) {
                return api.deleteConfig(id).$promise;
            },
            getMenuItemsForUser: function () {
                return api.getMenuItemsForUser().$promise;
            },
            updateUserMenuItem: function (userMenuItem) {
                return api.updateUserMenuItem(userMenuItem).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('MenuConfigDAO', ['$resource', 'APP_CONFIG', MenuConfigDAO]);
})();
