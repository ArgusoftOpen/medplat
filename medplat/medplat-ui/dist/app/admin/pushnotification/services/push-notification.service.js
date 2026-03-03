(function () {
    function PushNotificationDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/push/:action/:id', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    isArray: false
                },
                createOrUpdateConfig: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'manageconfig'
                    }
                },
                getNotificationTypeList: {
                    method: 'get',
                    isArray: true
                },
                getById: {
                    method: 'get',
                    isArray: false,
                    params: {
                        action: 'getById'
                    }
                },
                getNotifications: {
                    method: 'get',
                    isArray: true,
                    params: {
                        action: 'getnotifications'
                    }
                },
                getNotificationConfigById: {
                    method: 'get',
                    params: {
                        action: 'getconfigbyid'
                    }
                },
                toggleNotificationConfigState: {
                    method: 'GET',
                    params: {
                        action: 'toggleconfigstate'
                    }
                }
            });

        return {
            createOrUpdate: function (pushNotificationTypeMaster) {
                return api.createOrUpdate({}, pushNotificationTypeMaster).$promise;
            },
            createOrUpdateConfig: function (techoPushNotificationConfigDto) {
                return api.createOrUpdateConfig({}, techoPushNotificationConfigDto).$promise;
            },
            getNotificationTypeList: function () {
                return api.getNotificationTypeList().$promise;
            },
            getById: function (id) {
                return api.getById({ typeId: id }).$promise;
            },
            getNotifications: function (params) {
                return api.getNotifications(params).$promise;
            },
            getNotificationConfigById: function (id) {
                return api.getNotificationConfigById({ id: parseInt(id) }).$promise;
            },
            toggleNotificationConfigState: function (id) {
                return api.toggleNotificationConfigState({ id: parseInt(id) }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('PushNotificationDAO', ['$resource', 'APP_CONFIG', PushNotificationDAO]);
})();
