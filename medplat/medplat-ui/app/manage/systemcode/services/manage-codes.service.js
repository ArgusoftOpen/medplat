'use strict';
(function () {
    function SystemCodesService($resource, APP_CONFIG) {
        let api = $resource(APP_CONFIG.apiPath + '/systemcode/:action/:subaction/:id', {}, {
            saveOrUpdate: {
                method: 'POST'
            },
            retrieveCodes: {
                method: 'GET',
                isArray: true
            },
            retrieveById: {
                method: 'GET'
            },
            deleteById: {
                method: 'DELETE'
            },
            processICDCode : {
                method: 'GET',
                params: {
                    action: 'icd'
                }
            },
            getCodeByRefText : {
                method : 'GET',
                params : {
                    action : 'icd',
                    subaction: 'search'
                },
                isArray: true
            },
            getCodeDetailsByCodeAndCodeType : {
                method : 'GET',
                params : {
                    action : 'icd',
                    subaction : 'code'
                },
                isArray: true
            }
        });
        return {
            saveOrUpdate: function (dto) {
                return api.saveOrUpdate(dto).$promise;
            },
            retrieveAll: function () {
                return api.query().$promise;
            },
            retrieveByCodeType: function (type) {
                return api.retrieveCodes({codeType: type}).$promise;
            },
            retrieveByTableType: function (type) {
                return api.retrieveCodes({tableType: type}).$promise;
            },
            retrieveByByTypeAndCode: function (tableType, tableId, codeType) {
                return api.retrieveCodes({tableType: tableType, tableId: tableId, codeType: codeType}).$promise;
            },
            retrieveById: function (id) {
                return api.retrieveById({id: id}).$promise;
            },
            deleteById: function (id) {
                return api.deleteById({id: id}).$promise;
            },
            processICDCode : function(version,releaseYear,startingCodeCategory,uptoCodeCategory) {
                return api.processICDCode({version:version,releaseYear:releaseYear,startingCodeCategory:startingCodeCategory,uptoCodeCategory:uptoCodeCategory}).$promise;
            },
            getCodeByRefText : function(searchNameString,descTypeId,moduleId,codeType) {
                return api.getCodeByRefText({searchNameString:searchNameString,descTypeId:descTypeId,moduleId:moduleId,codeType:codeType.split('_CONCEPT')[0]}).$promise;
            },
            getCodeDetailsByCodeAndType : function(code,codeType) {
                return api.getCodeDetailsByCodeAndCodeType({code:code,codeType:codeType.split('_CONCEPT')[0]}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('SystemCodesService', SystemCodesService);
})();
