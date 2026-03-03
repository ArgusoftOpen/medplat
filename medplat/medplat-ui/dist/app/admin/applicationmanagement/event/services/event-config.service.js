(function () {
    function EventConfigDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/eventconfig/:action/:id', {}, {
            saveOrUpdate: {
                method: 'POST'
            },
            retrieveManualEvents: {
                method: 'GET',
                params: {
                    action: 'manualevents'
                }
            },
            retrieveById: {
                method: 'GET'
            },
            runEvent: {
                method: 'GET',
                params: {
                    action: 'run'
                }
            },
            toggleState: {
                method: 'PUT'
            }
        });
        return {
            saveOrUpdate: function (dto) {
                return api.saveOrUpdate(dto).$promise;
            },
            retrieveManualEvents: function () {
                return api.retrieveManualEvents().$promise;
            },
            retrieveAll: function () {
                return api.query().$promise;
            },
            retrieveById: function (id) {
                return api.retrieveById({ id: id }).$promise;
            },
            runEvent: function (id) {
                return api.runEvent({ id: id }).$promise;
            },
            run: function (id) {
                return api.get({ id: id }).$promise;
            },
            toggleState: function (dto) {
                return api.toggleState({}, dto).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('EventConfigDAO', EventConfigDAO);
})();
