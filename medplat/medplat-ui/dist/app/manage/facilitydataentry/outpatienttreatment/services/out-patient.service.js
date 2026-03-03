(function () {
    function OutPatientService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/rchopd/:action/:subaction/:id', {}, {
            create: {
                method: 'POST',
            },
            update: {
                method: 'PUT',
            },
            getRchOpdMemberRegistration: {
                method: 'GET'
            }
        });
        return {
            create: (rchOpdDto) => {
                return api.create({}, rchOpdDto).$promise;
            },
            update: (rchOpdDto) => {
                return api.update({}, rchOpdDto).$promise;
            },
            getRchOpdMemberRegistration: (opdRegistrationId) => {
                return api.getRchOpdMemberRegistration({action: "getOpdRegistrationDetails", opdRegistrationId}, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('OutPatientService', ['$resource', 'APP_CONFIG', OutPatientService]);
})();
