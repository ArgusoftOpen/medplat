(function () {
    function DrTechoDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/drtecho/:action/:subaction/:id', {}, {
            getHealthFacilityListByCriteria: {
                method: 'GET',
                isArray: true
            },
            disApproveFacility: {
                method: 'POST'
            },
            approveFacility: {
                method: 'POST'
            },
            disApproveUser: {
                method: 'POST'
            },
            approveUser: {
                method: 'POST'
            }
        });

        var attachmentApi = $resource(APP_CONFIG.apiPath + '/document/:action/:id', {},
            {
                removeFile: {
                    method: 'PUT'
                },
                downloadFile: {
                    method: 'GET',
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                }
            });
        return {
            getHealthFacilityListByCriteria: function (params) {
                return api.getHealthFacilityListByCriteria({
                    action: 'getHealthFacilityListByCriteria',
                    states: params.states,
                    limit: params.limit,
                    offset: params.offset,
                    orderBy: params.orderBy,
                    searchTxt: params.searchTxt
                }, {}).$promise;
            },
            disApproveFacility: function (dto) {
                return api.disApproveFacility({
                    action: 'disapprovefacility'
                }, dto).$promise;
            },
            approveFacility: function (dto) {
                return api.approveFacility({
                    action: 'approvefacility'
                }, dto).$promise;
            },
            disApproveUser: function (dto) {
                return api.disApproveUser({
                    action: 'disapproveuser'
                }, dto).$promise;
            },
            approveUser: function (dto) {
                return api.approveUser({
                    action: 'approveuser'
                }, dto).$promise;
            },
            removeFile: function (id) {
                return attachmentApi.removeFile({
                    action: 'removedocument', id
                }, {}).$promise;
            },
            downloadFile: function (id) {
                return attachmentApi.downloadFile({
                    action: 'getfile', id
                }, {}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('DrTechoDAO', ['$resource', 'APP_CONFIG', DrTechoDAO]);
})();
