(function () {
    function InternationalizationDAO($resource, APP_CONFIG) {
        let api = $resource(APP_CONFIG.apiPath + '/internationalization/:action', {},
            {
                updateLabelsMap: {
                    method: 'POST',
                    params: {
                        action: 'updateLabelsMap'
                    }
                }
            });
        return {
            updateLabelsMap: function () {
                return api.updateLabelsMap().$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('InternationalizationDAO', ['$resource', 'APP_CONFIG', InternationalizationDAO]);
})();
