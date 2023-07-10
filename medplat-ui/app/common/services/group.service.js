(function () {
    function GroupDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/menugroup/:action/:id', {}, {
            getReportGroups: {
                method: 'GET',
                isArray: true
            },
            saveReportGroup: {
                method: 'POST',
                transformResponse: function (id) {
                    return { res: id };
                }
            },
            deleteReportGroup: {
                method: 'DELETE',
            }
        });
        return {
            getReportGroups: function (data) {
                return api.getReportGroups(data).$promise;
            },
            saveReportGroup: function (data) {
                return api.saveReportGroup(data).$promise;
            },
            deleteReportGroup: function (data) {
                return api.deleteReportGroup(data).$promise;
            },
            getGroupsByMenuType: function (menutype) {
                return api.query({ action: 'retrieveGroupByMenuType', id: menutype }).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('GroupDAO', GroupDAO);
})();
