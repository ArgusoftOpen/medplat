(function () {
    function QueryController($timeout, QueryDAO, Mask, toaster, $uibModal, GeneralUtil,syncWithServerService) {
        let querycontroller = this;

        let retrieveAll = function () {
            Mask.show();
            QueryDAO.retrieveAllConfigured().then(function (res) {
                querycontroller.configuredQueries = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        querycontroller.openAddEditModal = function (configuredQuery) {
            if (configuredQuery) {
                querycontroller.newQueryConfig = angular.copy(configuredQuery);
                querycontroller.isEditMode = true;
            } else {
                querycontroller.isEditMode = false;
            }
            toggleFilter();
        };

        const toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        querycontroller.saveQuery = function () {
            querycontroller.queryConfigurationForm.$setSubmitted();
            if (querycontroller.queryConfigurationForm.$valid) {
                Mask.show();
                QueryDAO.saveOrUpdate(querycontroller.newQueryConfig).then(function () {
                    if (querycontroller.isEditMode) {
                        toaster.pop('success', 'Query Configuration Updated Successfully');
                    } else {
                        toaster.pop('success', 'Query Configuration Saved Successfully');
                    }
                    querycontroller.close();
                    retrieveAll();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        querycontroller.close = function () {
            querycontroller.newQueryConfig = {};
            querycontroller.queryConfigurationForm.$setPristine();
            toggleFilter();
        };

        querycontroller.toggleState = function (configuredQuery) {
            let changedState;
            if (configuredQuery.state === "ACTIVE") {
                changedState = 'inactive';
            } else {
                changedState = 'active';
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state from " + configuredQuery.state.toLowerCase() + ' to ' + changedState + '?';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                QueryDAO.toggleState(configuredQuery).then(function () {
                    toaster.pop('success', 'Query configuration state  changed from ' + configuredQuery.state.toLowerCase() + ' to ' + changedState + ' successfully');
                    retrieveAll();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        };

        querycontroller.showQueryHistory = function (query) {
            var queryDto = {
                code: 'retrieve_logged_actions_by_table_name_query_code',
                parameters: {
                    tableName: 'query_master',
                    queryCode: query.code
                }
            };
            Mask.show();
            QueryDAO.execute(queryDto).then(function (response) {
                Mask.hide();
                if (response.result.length === 0) {
                    $timeout(function () {
                        toaster.pop('warning', "No Query History found for Query code " + query.code);
                    });
                } else {
                    var modalInstance = $uibModal.open({
                        templateUrl: 'app/admin/applicationmanagement/querybuilder/views/show-query-history.modal.html',
                        controller: 'ShowQueryHistoryController',
                        windowClass: 'cst-modal',
                        controllerAs: 'mdctrl',
                        size: 'xl',
                        resolve: {
                            list: function () {
                                return response.result;
                            }
                        }
                    });
                    modalInstance.result.then(function () {

                    });
                }
            }, function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
            }).finally(function () {
                Mask.hide();
            });
        };
        querycontroller.showFlywayQuery = function (query) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/applicationmanagement/querybuilder/views/show-flyway-query.modal.html',
                controller: 'ShowFlywayQueryController',
                windowClass: 'cst-modal',
                controllerAs: 'mdctrl',
                size: 'xl',
                resolve: {
                    configuredQuery: function () {
                        return query;
                    }
                }
            });
            modalInstance.result.then(function () {
            });
        };

        querycontroller.syncWithServer = function(queryConfig){
            querycontroller.syncModel = syncWithServerService.syncWithServer(queryConfig.uuid);
        }

        querycontroller.executeQuery = function () {
            QueryDAO.runquery(querycontroller.newQueryConfig.query
            ).then(function () {
                toaster.pop('success', "Run Successfully");
            });
        };

        retrieveAll();
    }
    angular.module('imtecho.controllers').controller('QueryController', QueryController);
})();

// $uib model
(function () {
    function ShowQueryHistoryController(list, $uibModalInstance, toaster) {
        var mdctrl = this;
        mdctrl.historyList = list;
        mdctrl.loaded = false;

        mdctrl.init = function () {
            mdctrl.loaded = true;
        };
        mdctrl.cancel = function () {
            $uibModalInstance.close();
        };

        mdctrl.copyQuery = function (text) {
            const selBox = document.createElement('textarea');
            selBox.style.position = 'fixed';
            selBox.style.left = '0';
            selBox.style.top = '0';
            selBox.style.opacity = '0';
            selBox.value = text;
            document.body.appendChild(selBox);
            selBox.focus();
            selBox.select();
            document.execCommand('copy');
            document.body.removeChild(selBox);
            toaster.pop('success', 'Query Copied');
        };

        mdctrl.init();
    }
    angular.module('imtecho.controllers').controller('ShowQueryHistoryController', ShowQueryHistoryController);
})();

// $uib model
(function () {
    function ShowFlywayQueryController(configuredQuery, $uibModalInstance,$uibModal, toaster) {
        var mdctrl = this;
        mdctrl.configuredQuery = configuredQuery;
        mdctrl.isPublicKeyword = false;

        mdctrl.init = function () {
            mdctrl.isPublicKeyword = mdctrl.configuredQuery.query.includes('public');
            mdctrl.text = 'DELETE FROM QUERY_MASTER WHERE CODE=\'' + mdctrl.configuredQuery.code + '\';';
            mdctrl.text += '\n';
            mdctrl.text += '\n';
            mdctrl.text += 'INSERT INTO QUERY_MASTER (uuid, created_by, created_on, modified_by, modified_on, code, params, query, description, returns_result_set, state ) ';
            mdctrl.text += '\n';
            mdctrl.text += 'VALUES ( ';
            mdctrl.text += '\n';
            mdctrl.text +=  '\'' + mdctrl.configuredQuery.uuid + '\'' + ', ';
            mdctrl.text += mdctrl.configuredQuery.createdBy + ', ';
            mdctrl.text += ' current_date ' + ', ';
            mdctrl.text += mdctrl.configuredQuery.createdBy + ', ';
            mdctrl.text += ' current_date ' + ', ';
            mdctrl.text += '\'' + mdctrl.configuredQuery.code + '\', ';
            mdctrl.text += '\n';
            if (mdctrl.configuredQuery.params === undefined || mdctrl.configuredQuery.params === null) {
                mdctrl.text += ' null, ';
            } else {
                mdctrl.text += '\'' + mdctrl.configuredQuery.params + '\', ';
            }
            mdctrl.text += '\n';
            mdctrl.text += '\'' + mdctrl.configuredQuery.query.replace(/'/g, "''") + '\', ';
            mdctrl.text += '\n';
            if (mdctrl.configuredQuery.description === undefined || mdctrl.configuredQuery.description === null) {
                mdctrl.text += 'null, ';
            } else {
                mdctrl.text += '\'' + mdctrl.configuredQuery.description + '\', ';
            }
            mdctrl.text += '\n';
            if (mdctrl.configuredQuery.returnsResultSet === undefined || mdctrl.configuredQuery.returnsResultSet === null) {
                mdctrl.text += 'false, ';
            } else {
                mdctrl.text += mdctrl.configuredQuery.returnsResultSet + ', ';
            }

            if (mdctrl.configuredQuery.state === undefined || mdctrl.configuredQuery.state === null) {
                mdctrl.text += ' \'ACTIVE\', ';
            } else {
                mdctrl.text += '\'' + mdctrl.configuredQuery.state + '\'';
            }

            mdctrl.text += ');';
        };

        mdctrl.copyQuery = function () {
            const selBox = document.createElement('textarea');
            selBox.style.position = 'fixed';
            selBox.style.left = '0';
            selBox.style.top = '0';
            selBox.style.opacity = '0';
            selBox.value = mdctrl.text;
            document.body.appendChild(selBox);
            selBox.focus();
            selBox.select();
            document.execCommand('copy');
            document.body.removeChild(selBox);
            $uibModalInstance.close();
            toaster.pop('success', 'Query Copied');
        };

        mdctrl.askForConfirmation = function(){
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "This Flyway contains keyword 'public'. Are You sure want to download?";
                    }
                }
            });
            modalInstance.result.then(function () {
                mdctrl.download();
            },function(){});
        }

        mdctrl.downloadCheckForPublicKeyword = function(){
            if(mdctrl.isPublicKeyword== true){
                mdctrl.askForConfirmation();
            }
            else{
                mdctrl.download();
            }
        }

        mdctrl.download = function () {
            var a = window.document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob([mdctrl.text], { type: 'text/plain' }));
            var name = "V" + new Date().getTime() + "__CHANGE_IN_" + mdctrl.configuredQuery.code + '.sql';
            a.download = name;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            $uibModalInstance.close();
        };

        mdctrl.cancel = function () {
            $uibModalInstance.close();
        };

        mdctrl.init();
    }
    angular.module('imtecho.controllers').controller('ShowFlywayQueryController', ShowFlywayQueryController);
})();
