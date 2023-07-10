(function () {
    function ManageWpdDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/managewpd/:action/:subaction/', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    isArray: true
                },
                retrieveHospitals: {
                    method: 'GET',
                    isArray: true
                },
                retrievePendingDischargeList: {
                    method: 'GET',
                    isArray: true
                },
                saveDischargeDetails: {
                    method: 'POST'
                },
                vaccinationValidationChild: {
                    method: 'GET',
                    transformResponse: function (res) {
                        return { result: res };
                    }
                }
            }
        );
        return {
            createOrUpdate: function (wpdDto) {
                return api.createOrUpdate({}, wpdDto).$promise;
            },
            retrieveHospitals: function () {
                return api.retrieveHospitals({ action: 'retrievehospitals' }, {}).$promise;
            },
            retrievePendingDischargeList: function (userId) {
                return api.retrievePendingDischargeList({ action: 'retrievependingdischargelist', userId: userId }, {}).$promise;
            },
            saveDischargeDetails: function (dischargeDto) {
                return api.saveDischargeDetails({ action: 'savedischargedetails' }, dischargeDto).$promise;
            },
            vaccinationValidationChild: function (dob, givenDate, currentVaccine, givenImmunisations) {
                return api.vaccinationValidationChild({ action: 'vaccinationvalidationforchild', dob: dob, givenDate: givenDate, currentVaccine: currentVaccine, givenImmunisations: givenImmunisations }).$promise;
            }, searchMembers: function (params) {
                return api.query({ action: 'membersearch', id: params.byId, memberId: params.byMemberId, familyId: params.byFamilyId, mobileNumber: params.byMobileNumber, name: params.byName, lmp: params.byLmp, edd: params.byEdd, organizationUnit: params.byOrganizationUnit, 'abha number': params.byAbhaNumber, 'abha address': params.byAbhaAddress, locationId: params.locationId, searchString: params.searchString, byFamilyMobileNumber: params.byFamilyMobileNumber, limit: params.limit, offSet: params.offSet }, {}).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('ManageWpdDAO', ['$resource', 'APP_CONFIG', ManageWpdDAO]);
})();
