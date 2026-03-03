(function () {
    function NlpQueryService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/nlp-query/:action', {},
            {
                parse: {
                    method: 'POST',
                    params: {
                        action: 'parse'
                    }
                },
                execute: {
                    method: 'POST',
                    params: {
                        action: 'execute'
                    }
                },
                parseAndExecute: {
                    method: 'POST',
                    params: {
                        action: 'parse-and-execute'
                    }
                },
                getExamples: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'examples'
                    }
                },
                getSchema: {
                    method: 'GET',
                    params: {
                        action: 'schema'
                    }
                }
            });
        return {
            parseQuery: function (request) {
                return api.parse(request).$promise;
            },
            executeQuery: function (request) {
                return api.execute(request).$promise;
            },
            parseAndExecute: function (request) {
                return api.parseAndExecute(request).$promise;
            },
            getExamples: function () {
                return api.getExamples().$promise;
            },
            getSchema: function () {
                return api.getSchema().$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NlpQueryService', NlpQueryService);
})();
