(function () {
    function ReportDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/report/:action/:subaction', {},
            {
                getReportDetails: {
                    method: 'GET',
                    params: {
                        action: 'detail',
                        code: '@code'
                    }
                },
                getReports: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'getreportlist'
                    }
                },
                deleteReport: {
                    method: 'DELETE',
                    params: {
                        action: 'deletereport'
                    }
                },
                executeQuery: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'executeQuery'
                    }
                },
                retrieveComboData: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievecombodatabyqueryid'
                    }
                },
                retrieveComboDataByUUID: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievecombodatabyqueryuuid'
                    }
                },
                retrieveDataByQueryId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievedatabyqueryid'
                    }
                },
                retrieveDataByQueryUUID: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievedatabyqueryuuid'
                    }
                },
                downloadPdf: {
                    method: 'POST',
                    params: {
                        action: 'downloadpdf'
                    },
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                },
                downloadExcel: {
                    method: 'POST',
                    params: {
                        action: 'downloadexcel'
                    },
                    responseType: 'arraybuffer',
                    transformResponse: function (res) {
                        return {
                            data: res
                        };
                    }
                },
                downloadOffline: {
                    method: 'POST',
                    params: {
                        action: 'offline-report'
                    }
                },
                retrieveOfflineReportsByUserId: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve-offline-reports'
                    }
                },
                retrieveOfflineReportId: {
                    method: 'GET',
                    params: {
                        action: 'retrieve-offline-report'
                    }
                }
            });
            var documentApi = $resource(APP_CONFIG.apiPath + '/document/:action/:id', {}, {
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
            saveConfigDynamicDetail: function (dto) {
                return api.save({ action: 'configdynamicdetail' }, dto).$promise;
            },
            getDynamicDetail: function (id, type) {
                if (type == 'code') {
                    return api.get({ action: 'dynamicdetail', code: id, fetchQueryOptions: true }).$promise;
                } else {
                    return api.get({ action: 'dynamicdetail', id: id, fetchQueryOptions: true }).$promise;
                }
            },
            getDymaicReport: function (configDto) {
                return api.save({ action: 'getdynamicreport' }, configDto).$promise;
            },
            getReportDetails: function (data) {
                return api.getReportDetails(data).$promise;
            },
            getReports: function (data) {
                return api.getReports(data).$promise;
            },
            deleteReport: function (data) {
                return api.deleteReport(data).$promise;
            },
            getAll: function () {
                return api.query({ action: 'all' }).$promise;
            },
            executeQuery: function (query) {
                return api.executeQuery(query).$promise;
            },
            retrieveComboData: function (queryId, paramMap) {
                return api.retrieveComboData({ subaction: queryId }, paramMap).$promise;
            },
            retrieveComboDataByUUID: function (queryUUID, paramMap) {
                return api.retrieveComboDataByUUID({ subaction: queryUUID }, paramMap).$promise;
            },
            retrieveDataByQueryId: function (queryId, paramMap) {
                for (var key in paramMap) {
                    if (paramMap[key] == null || paramMap[key] == "")
                        delete paramMap[key];
                }
                return api.retrieveDataByQueryId({ subaction: queryId }, paramMap).$promise;
            },
            retrieveDataByQueryUUID: function (queryUUID, paramMap) {
                for (var key in paramMap) {
                    if (paramMap[key] == null || paramMap[key] == "")
                        delete paramMap[key];
                }
                return api.retrieveDataByQueryUUID({ subaction: queryUUID }, paramMap).$promise;
            },
            downloadPdf: function (queryUUID, reportExcelDto) {
                return api.downloadPdf({ subaction: queryUUID }, reportExcelDto).$promise;
            },
            downloadExcel: function (queryUUID, reportExcelDto) {
                return api.downloadExcel({ subaction: queryUUID }, reportExcelDto).$promise;
            },
            downloadOffline: function (queryId, reportExcelDto) {
                return api.downloadOffline({ subaction: queryId }, reportExcelDto).$promise;
            },
            retrieveOfflineReportsByUserId: function (userId) {
                return api.retrieveOfflineReportsByUserId({ subaction: userId }).$promise;
            },
            retrieveOfflineReportId: function (id) {
                return api.retrieveOfflineReportId({ subaction: id }).$promise;
            },
            getFileById: function (id) {
                return documentApi.getFile({action: 'getfile', id}, {}).$promise;
            },
        };
    }
    angular.module('imtecho.service').factory('ReportDAO', ReportDAO);
})();
