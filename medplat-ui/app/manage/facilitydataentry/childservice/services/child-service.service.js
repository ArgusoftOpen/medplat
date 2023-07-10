(function () {
    function ManageChildServiceDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/childservice/:action/:subaction/:id', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    isArray: true
                },
                fetchMotherDetails: {
                    method: 'GET',
                },
                fetchAnganwadiId: {
                    method: 'GET',
                },
                fetchAnganwadiArea: {
                    method: 'GET',
                },
                fetchChildDetails: {
                    method: 'GET',
                    isArray: true
                },
                getDueImmunisationsForChild: {
                    method: 'GET',
                    isArray: true
                },
                vaccinationValidationChild: {
                    method: 'GET',
                    transformResponse: function (res) {
                        return { result: res };
                    }
                },
                retrieveSdScore: {
                    method: 'GET'
                },
                getLastChildVisit: {
                    method: 'GET'
                },
            }
        );
        return {
            createOrUpdate: function (pncDto) {
                return api.createOrUpdate({}, pncDto).$promise;
            },
            fetchMotherDetails: function (id) {
                return api.fetchMotherDetails({ action: 'fetchmotherdetails', id: id }).$promise;
            },
            fetchAnganwadiId: function (familyId) {
                return api.fetchAnganwadiId({ action: 'fetchanganwadiid', familyId: familyId }).$promise;
            },
            fetchAnganwadiArea: function (anganwadiId) {
                return api.fetchAnganwadiArea({ action: 'fetchanganwadiarea', anganwadiId: anganwadiId }).$promise;
            },
            fetchChildDetails: function (id) {
                return api.fetchChildDetails({ action: 'fetchchilddetails', id: id }).$promise;
            },
            getDueImmunisationsForChild: function (dateOfBirth, givenImmunisations) {
                return api.fetchChildDetails({ action: 'getimmunisationsforchild', dateOfBirth: dateOfBirth, givenImmunisations: givenImmunisations }).$promise;
            },
            vaccinationValidationChild: function (dob, givenDate, currentVaccine, givenImmunisations) {
                return api.vaccinationValidationChild({ action: 'vaccinationvalidationforchild', dob: dob, givenDate: givenDate, currentVaccine: currentVaccine, givenImmunisations: givenImmunisations }).$promise;
            },
            retrieveSdScore: function (gender, height, weight) {
                return api.retrieveSdScore({ action: 'getsdscore', gender: gender, height: height, weight: weight }).$promise;
            },
            getLastChildVisit: function (memberId) {
                return api.getLastChildVisit({ action: 'getlastchildvisit', memberId: memberId }).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('ManageChildServiceDAO', ['$resource', 'APP_CONFIG', ManageChildServiceDAO]);
})();
