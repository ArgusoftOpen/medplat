(function () {
    function AncService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/manageanc/:action/:subaction/', {},
            {
                createOrUpdate: {
                    method: 'POST',
                    transformResponse: function (res) {
                        return {
                            result: res
                        };
                    }
                },
                memberById: {
                    method: 'GET',
                    params: {
                        action: 'memberbyid'
                    }
                }
            }
        );
        return {
            createOrUpdate: function (ancque) {
                return api.createOrUpdate({}, ancque).$promise;
            },
            memberById: function (memberId) {
                var params = {
                    memberId: memberId
                };
                return api.memberById(params).$promise;
            }, searchMembers: function (params) {
                return api.query({ action: 'membersearch', id: params.byId, memberId: params.byMemberId, familyId: params.byFamilyId, mobileNumber: params.byMobileNumber, name: params.byName, lmp: params.byLmp, edd: params.byEdd, organizationUnit: params.byOrganizationUnit, 'abha number': params.byAbhaNumber, 'abha address': params.byAbhaAddress,locationId: params.locationId, searchString: params.searchString, byFamilyMobileNumber: params.byFamilyMobileNumber, limit: params.limit, offSet: params.offSet }, {}).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('AncService', ['$resource', 'APP_CONFIG', AncService]);
})();
