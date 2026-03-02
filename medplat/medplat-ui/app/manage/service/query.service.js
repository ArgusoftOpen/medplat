(function () {
    function QueryDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/resource/:action/:id', {},
            {
                toggleState: {
                    method: 'PUT'
                },
                executeQuery: {
                    method: 'POST',
                    params: {
                        action: 'getdata'
                    }
                },
                execute: {
                    method: 'POST',
                },
                executeQueryForFamilyReport: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'getdata'
                    },
                    transformResponse: function (res) {
                        res = JSON.parse(res);
                        return res.result;
                    }
                },
                executeAllQuery: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'getdatas'
                    }
                },
                executeAll: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'getalldata'
                    }
                },
                runquery: {
                    method: 'POST',
                    params: {
                        action: 'runquery'
                    }
                }
            });
        return {
            retrieveAllConfigured: function (isActive) {
                return api.query({ is_active: isActive }).$promise;
            },
            saveOrUpdate: function (dto) {
                return api.save(dto).$promise;
            },
            toggleState: function (dto) {
                return api.toggleState({}, dto).$promise;
            },
            executeQuery: function (dto) {
                return api.executeQuery(dto).$promise;
            },
            execute: function (params) {
                return api.execute({action: 'getdata', id: params.code }, params).$promise;
            },
            executeQueryForFamilyReport: function (dto) {
                return api.executeQueryForFamilyReport(dto).$promise;
            },
            executeAllQuery: function (dtoList) {
                return api.executeAllQuery(dtoList).$promise;
            },
            executeAll: function (dtoList) {
                return api.executeAll(dtoList).$promise;
            },
            runquery: function (query) {
                return api.runquery({}, query).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('QueryDAO', QueryDAO);
})();