(function () {
    function NotificationDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/notificationtype/:action/:id', {}, {
            retrieveAllNotifications: {
                method: 'GET',
                isArray: true
            },
            createOrUpdate: {
                method: 'POST',
                isArray: true
            },
            retrieveById: {
                method: 'GET'
            },
            toggleActive: {
                method: 'PUT'
            }
        });
        return {
            retrieveAllNotifications: function (isActive) {
                return api.query({ is_active: isActive }).$promise;
            },
            createOrUpdate: function (notificationMasterDto) {
                return api.createOrUpdate({}, notificationMasterDto).$promise;
            },
            retrieveById: function (id) {
                console.log("check service id", id)
                return api.retrieveById({ id: id }).$promise;
            },
            toggleActive: function (id, isActive) {
                var params = {
                    id: id,
                    is_active: isActive
                };
                return api.toggleActive(params, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NotificationDAO', ['$resource', 'APP_CONFIG', NotificationDAO]);
})();
