(function () {
    function NcdophthalmologistDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/ncd/:action/:subaction/:id', {}, {
            saveOphthalmologistResponse: {
                method: 'POST',
                params: {
                    action: 'saveOphthalmologistResponse',
                }
            },
            retrieveOphthalmologistReponse: {
                method: 'GET',
                params: {
                    action: 'retrieveOphthalmologistReponse'
                }
            }
        });
        return {
            saveOphthalmologistResponse: function (data) {
                return api.saveOphthalmologistResponse({}, data).$promise;
            },
            retrieveOphthalmologistReponse: function (memberId, screeningDate) {
                return api.retrieveOphthalmologistReponse({ memberId: memberId, screeningDate: screeningDate}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NcdophthalmologistDAO', NcdophthalmologistDAO);
})();
