(function () {
    function SohElementConfigurationDAO($resource, APP_CONFIG) {
        const api = $resource(APP_CONFIG.apiPath + '/mobile/:action/:subaction/:id', {}, {
            getElements: {
                method: 'GET',
                isArray: true
            },
            getElementById: {
                method: 'GET'
            },
            createOrUpdateElement: {
                method: 'POST'
            },
            getCharts: {
                method: 'GET',
                isArray: true
            },
            getChartById: {
                method: 'GET'
            },
            createOrUpdateChart: {
                method: 'POST'
            },
            getElementModules: {
                method: 'GET',
                isArray: true
            },
            getElementModuleById: {
                method: 'GET'
            },
            createOrUpdateElementModule: {
                method: 'POST'
            },
        });
        const attachmentApi = $resource(APP_CONFIG.apiPath + '/document/:action/:id', {}, {
            removeFile: {
                method: 'PUT'
            },
            getFile: {
                method: 'GET',
                responseType: 'blob',
                transformResponse: function (res) {
                    return {
                        data: res
                    };
                }
            }
        });
        return {
            getElements: function () {
                return api.getElements({
                    action: 'soh',
                    subaction: 'elements'
                }, {}).$promise;
            },
            getElementById: function (id) {
                return api.getElementById({
                    action: 'soh',
                    subaction: 'element',
                    id
                }, {}).$promise;
            },
            createOrUpdateElement: function (dto) {
                return api.createOrUpdateElement({
                    action: 'soh',
                    subaction: 'element'
                }, dto).$promise;
            },
            getCharts: function () {
                return api.getCharts({
                    action: 'soh',
                    subaction: 'chartsJson'
                }, {}).$promise;
            },
            getChartById: function (id) {
                return api.getChartById({
                    action: 'soh',
                    subaction: 'chart',
                    id
                }, {}).$promise;
            },
            createOrUpdateChart: function (dto) {
                return api.createOrUpdateChart({
                    action: 'soh',
                    subaction: 'chart'
                }, dto).$promise;
            },
            getElementModules: function (retrieveActiveOnly) {
                return api.getElementModules({
                    action: 'soh',
                    subaction: 'elementModules',
                    retrieveActiveOnly
                }, {}).$promise;
            },
            getElementModuleById: function (id) {
                return api.getElementModuleById({
                    action: 'soh',
                    subaction: 'elementModule',
                    id
                }, {}).$promise;
            },
            createOrUpdateElementModule: function (dto) {
                return api.createOrUpdateElementModule({
                    action: 'soh',
                    subaction: 'elementModule'
                }, dto).$promise;
            },
            getFileById: function (id) {
                return attachmentApi.getFile({
                    action: 'getfile', id
                }, {}).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('SohElementConfigurationDAO', ['$resource', 'APP_CONFIG', SohElementConfigurationDAO]);
})();
