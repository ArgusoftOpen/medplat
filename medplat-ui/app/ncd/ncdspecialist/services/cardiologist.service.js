(function () {
    function NcdCardiologistDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/ncd/:action/:subaction/:id', {}, {
            saveCardiologistResponse: {
                method: 'POST',
                params: {
                    action: 'saveCardiologistResponse',
                }
            },
            retrieveCardiologistReponse: {
                method: 'GET',
                params: {
                    action: 'retrieveCardiologistReponse'
                }
            }
        });
        return {
            saveCardiologistResponse: function (data) {
                return api.saveCardiologistResponse({}, data).$promise;
            },
            retrieveCardiologistReponse: function (memberId, screeningDate) {
                return api.retrieveCardiologistReponse({ memberId: memberId, screeningDate: screeningDate}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NcdCardiologistDAO', NcdCardiologistDAO);
})();
