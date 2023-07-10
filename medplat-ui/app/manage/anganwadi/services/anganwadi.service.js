'use strict';
(function () {
    function AnganwadiService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/fhs/:action/:query/:subquery', {}, {
            retrieveAllAnganwadis: {
                method: 'GET',
                params: {
                    action: 'anganwadilist'
                },
                isArray: true
            },
            toggleActive: {
                method: 'GET',
                params: {
                    action: 'anganwadi',
                    query: 'toggleactive'
                },
            },
            createAnganwadi: {
                method: 'POST',
                params: {
                    action: 'anganwadi'
                }
            },
            getAnganwadiById: {
                method: 'GET',
                params: {
                    action: 'anganwadi'
                },
            },
            searchMembersByUniqueHealthId: {
                method: 'GET',
                params: {
                    action: 'membersearchbyuniquehealthid'
                },
            },
            updateAnganwadi: {
                method: 'PUT',
                params: {
                    action: 'anganwadi'
                }
            },
            updateVerifiedFamilyLocation: {
                method: 'PUT'
            },
            searchMembers: {
                method: 'GET'
            }
        });
        return {
            retrieveAllAnganwadis: function (params) {
                return api.retrieveAllAnganwadis({ query: params.limit, subquery: params.offset, locationId: params.locationId }, {}).$promise;
            },
            toggleActive: function (id, isActive) {
                var params = {
                    id: id,
                    isActive: isActive
                }
                return api.toggleActive(params).$promise;
            },
            createAnganwadi: function (anganwadi) {
                return api.createAnganwadi(anganwadi).$promise;
            },
            getAnganwadiById: function (id) {
                var params = {
                    id: id
                }
                return api.getAnganwadiById(params).$promise;
            },
            updateAnganwadi: function (anganwadi) {
                return api.updateAnganwadi(anganwadi).$promise;
            },
            searchFamily: function (userId, searchString, searchByFamilyId, searchByLocationId, isArchivedFamily, isVerifiedFamily) {
                return api.query({ action: 'familysearch', searchByFamilyId: searchByFamilyId, userId: userId, searchString: searchString, searchByLocationId: searchByLocationId, isArchivedFamily: isArchivedFamily, isVerifiedFamily: isVerifiedFamily }).$promise;
            },
            searchMembers: function (memberId) {
                return api.searchMembers({ action: 'membersearch', memberId: memberId }).$promise;
            },
            getMemberById: function (memberId) {
                return api.get({ action: 'memberbyid', query: memberId }).$promise;
            },
            getMemberDetailsByUniqueHealthId: function (uniqueHealthId) {
                return api.get({ action: 'getMemberDetailsByUniqueHealthId', uniqueHealthId: uniqueHealthId }).$promise;
            },
            searchMembersByUniqueHealthId: function (uniqueHealthId) {
                var params = {
                    uniqueHealthId: uniqueHealthId
                }
                return api.searchMembersByUniqueHealthId(params).$promise;
            },
            updateVerifiedFamilyLocation: function (familyList, selectedMoveAnganwadiAreaId, selectedMoveAshaAreaId) {
                return api.updateVerifiedFamilyLocation({ action: 'updateVerifiedFamilyLocation', selectedMoveAnganwadiAreaId: selectedMoveAnganwadiAreaId, selectedMoveAshaAreaId: selectedMoveAshaAreaId }, familyList).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('AnganwadiService', AnganwadiService);
})();
