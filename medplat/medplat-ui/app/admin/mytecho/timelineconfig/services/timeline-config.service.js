(function () {
    function TimelineConfigDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/timeline/config/:action/:id', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    isArray: true

                }
            });
        var documentApi = $resource(APP_CONFIG.apiPath + '/document/:action/:id', {},
            {
                removeFile: {
                    method: 'PUT',
                    params: {
                        action: 'removedocument'
                    }
                }
            });
        return {
            createOrUpdate: function (timelineConfigMasterDto) {
                return api.createOrUpdate({}, timelineConfigMasterDto).$promise;
            },
            removeFile: function (id) {
                return documentApi.removeFile({ id: id }, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('TimelineConfigDAO', ['$resource', 'APP_CONFIG', TimelineConfigDAO]);
})();
