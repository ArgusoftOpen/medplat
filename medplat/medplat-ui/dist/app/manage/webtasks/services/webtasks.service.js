'use strict';
(function () {
    function WebTasksService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/webtasks/:action/:query/:subquery', {}, {
            getWebTaskCount: {
                method: 'GET',
                isArray: true
            },
            getWebTaskDetailByType: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'basketdetail'
                }
            },
            getTaskDetailWithAction: {
                method: 'GET',
                isArray: false
            },
            saveActions: {
                method: 'POST'
            },
            saveBasketPreference: {
                method: 'POST'
            },
            retrievePreferenceByUserId: {
                method: 'GET',
                isArray: false
            }
        });
        return {
            getWebTaskCount: function () {
                return api.getWebTaskCount({ action: 'count' }, {}).$promise;
            },
            getWebTaskDetailByType: function (params) {
                return api.getWebTaskCount({ action: 'basketdetail', taskTypeId: params.taskTypeId, limit: params.limit, offset: params.offset, locationId: params.locationId }, {}).$promise;
            },
            getTaskDetailWithAction: function (taskId) {
                return api.getTaskDetailWithAction({ action: 'getaction', taskId: taskId }, {}).$promise;
            },
            saveActions: function (tasks) {
                return api.saveActions({ action: 'saveaction' }, tasks).$promise;
            },
            saveBasketPreference: function (basketPreferenceDto) {
                return api.saveBasketPreference({ action: 'savebasketpreference' }, basketPreferenceDto).$promise;
            },
            retrievePreferenceByUserId: function (userId) {
                return api.retrievePreferenceByUserId({ action: 'retrievebasketpreference', userId: userId }, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('WebTasksService', WebTasksService);
})();
