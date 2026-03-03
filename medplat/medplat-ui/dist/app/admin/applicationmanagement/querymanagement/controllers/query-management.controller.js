/* global moment */
(function () {
    function QueryManagement(QueryManagementDao, GeneralUtil, Mask, toaster, $uibModal, $q, QueryDAO, AuthenticateService, ReportDAO, $timeout) {
        var querymanagement = this;
        querymanagement.errorData = null;
        querymanagement.dBstateQuery = "select pid,query,wait_event_type,state_change,cast (now()-state_change as TEXT) as Since from pg_stat_activity where state = 'active' order by state_change";
        querymanagement.isTableLoading = false;
        querymanagement.isHistoryLoading = false;

        querymanagement.downloadExcel = function () {
            if (!!querymanagement.query) {
                var paramObject = {
                    query: querymanagement.query
                };
                var reportExcelDto = {
                    paramObj: paramObject
                };
                Mask.show();
                ReportDAO.downloadExcel(208, reportExcelDto).then(function (res) {
                    if (res.data !== null && navigator.msSaveBlob) {
                        return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                    }
                    var a = $("<a style='display: none;'/>");
                    var url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/vnd.ms-excel' }));
                    a.attr("href", url);
                    a.attr("download", (!!querymanagement.fileName ? querymanagement.fileName : "rename") + "_" + new Date().getTime() + ".xlsx");
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();
                }).catch(function (error) {
                    $timeout(function () {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    });
                }).finally(function () {
                    $timeout(function () {
                        Mask.hide();
                    });
                });
            } else {
                toaster.pop('error', 'Query can not be null');
            }
        };

        querymanagement.loadQueryHistory = function () {
            querymanagement.isHistoryLoading = true;
            AuthenticateService.getLoggedInUser().then(function (user) {
                Mask.show();
                var queryDto = {
                    code: 'retrieve_limited_query_history',
                    parameters: {
                        userId: user.data.id,
                        limit: 100,
                        searchKey : querymanagement.searchHistory || null
                    }
                };

                QueryDAO.execute(queryDto).then(function (res) {
                    querymanagement.historyTables = res.result;
                    Mask.hide();
                    querymanagement.isHistoryLoading = false;
                }).catch(function (error) {
                    $timeout(function () {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    });
                }).finally(function () {
                    Mask.hide();
                    querymanagement.isHistoryLoading = false;
                });
            });
        };

        querymanagement.init = function () {
            Mask.show();
            querymanagement.isTableLoading = true;
            QueryDAO.execute({ code: 'retrieve_all_tables' }).then(function (res) {
                querymanagement.tables = res.result;
                querymanagement.loadQueryHistory();
                querymanagement.isTableLoading = false;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
                delete querymanagement.tableData;
                querymanagement.isTableLoading = false;
            });
        };

        querymanagement.showWarning = function (msg) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return msg;
                    }
                }
            });
            return modalInstance.result.then(function () {
                return $q(function (resolve) {
                    resolve(true);
                });
            }, function () {
                return $q(function (resolve) {
                    resolve(false);
                });
            });
        };

        querymanagement.killProcess = function (pid) {
            var str = "Are yoy sure you want to kill process id " + pid + " ?";
            querymanagement.showWarning(str).then(function (res) {
                if (res) {
                    querymanagement.query = "select pg_terminate_backend( " + pid + " )";
                    querymanagement.retrieveFinalQuery(querymanagement.query);
                }
            });
        };
        querymanagement.popuateDBstateQuery = function () {
            querymanagement.query = angular.copy(querymanagement.dBstateQuery);
            querymanagement.retrieveFinalQuery(querymanagement.query);
        };


        querymanagement.tableHistoryClicked = function (data) {
            querymanagement.query = data;
        };

        querymanagement.tableClicked = function (tablename) {
            querymanagement.query = "select * from " + tablename + " limit 100";
            querymanagement.retrieveFinalQuery(querymanagement.query);
        };

        querymanagement.isWhereIncluded = function () {
            if (querymanagement.query.toUpperCase().includes("WHERE")) {
                return $q(function (resolve) {
                    resolve(true);
                });
            } else {
                var msg = "The query does not include where clause. Are you sure you want to proceed ?";
                return querymanagement.showWarning(msg);
            }
        };

        querymanagement.executeQuery = function () {
            if (querymanagement.query !== null) {
                querymanagement.isWhereIncluded().then(function (res) {
                    if (res) {
                        querymanagement.executeFinalQuery(querymanagement.query);
                    }
                });
            } else {
                toaster.pop('error', "Please enter query");
                delete querymanagement.tableData;
            }
        };
        querymanagement.retrieveQuery = function () {
            if (querymanagement.query !== null) {
                querymanagement.isWhereIncluded().then(function (res) {
                    if (res) {
                        querymanagement.errorData = null;
                        querymanagement.retrieveFinalQuery(querymanagement.query);
                    }
                });
            } else {
                toaster.pop('error', "Please enter query");
                delete querymanagement.tableData;
            }
        };

        querymanagement.executeFinalQuery = function (query) {
            querymanagement.errorData = null;
            Mask.show();

            QueryManagementDao.execute(query).then(function (response) {
                toaster.pop('success', "Query Executed Successfully");
                if (response !== null && response.length > 0) {
                    querymanagement.tableData = response;
                    querymanagement.headers = Object.keys(querymanagement.tableData[0]);
                    querymanagement.loadQueryHistory();
                } else {
                    delete querymanagement.tableData;
                    delete querymanagement.headers;
                }
                Mask.hide();

            }, function (error) {
                Mask.hide();
                toaster.pop('error', error.data.message);
                if (error.data.errorcode !== 1) {
                    querymanagement.errorData = error.data.message;
                }
                delete querymanagement.tableData;
                delete querymanagement.headers;
            });
        };

        querymanagement.retrieveFinalQuery = function (query) {
            Mask.show();
            QueryManagementDao.retrieveQuery(query).then(function (response) {
                if (response !== null && response.length > 0) {
                    querymanagement.tableData = response;
                    querymanagement.headers = Object.keys(querymanagement.tableData[0]);
                    toaster.pop('success', "Query Executed Successfully");
                    querymanagement.loadQueryHistory();
                } else {
                    toaster.pop('info', "No data found");
                    delete querymanagement.tableData;
                }
                Mask.hide();
            }, function (error) {
                toaster.pop('error', error.data.message);
                if (error.data.errorcode !== 1) {
                    querymanagement.errorData = error.data.message;
                }
                delete querymanagement.tableData;
                Mask.hide();
            });
        };

        /* querymanagement.calcTime = function () {
            for (var i = 0; i < querymanagement.tableData.length; i++) {
                var temp = (new Date() - new Date(querymanagement.tableData[i].state_change));
                if (temp > 1000) {
                    querymanagement.tableData[i].since = temp / 1000 + " seconds";
                } else {
                    querymanagement.tableData[i].since = temp + " ms";
                }
            }
        } */

        querymanagement.init();
    }
    angular.module('imtecho.controllers').controller('QueryManagement', QueryManagement);
})();
