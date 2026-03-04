(function (angular) {
    function ReportController(ReportDAO, $state, $uibModal, GroupDAO, Mask, PagingService, APP_CONFIG, $sessionStorage, QueryDAO, syncWithServerService) {
        var report = this;
        var paramsToPass = {
            offset: ' ',
            limit: ' ',
            reportName: ' ',
            sortBy:' ',
            sortOn:' '

        };

        var init = function () {
            report.reports = [];
            report.propertyName = 'menuType'
            report.menuTypes = APP_CONFIG.menuTypes;
            report.order = 'updatedOn';
            report.sortBy = '';
            report.pagingService = PagingService.initialize();

            if ($state.params.groupId) {
                report.parentGroupId = Number($state.params.groupId);
            }
            if ($state.params.subGroupId) {
                report.subGroupId = Number($state.params.subGroupId);
            }
            if ($state.params.reportName) {
                report.reportName = $state.params.reportName;
                report.parentGroupId = null;
                report.subGroupId = null;
            }
            report.retrieveAll(true);
        };

        report.retrieveAll = function (reset) {
            Mask.show();
            if (reset) {
                report.pagingService.resetOffSetAndVariables();
            }
            paramsToPass.limit = report.pagingService.limit;
            paramsToPass.offset = report.pagingService.offSet;
            paramsToPass.reportName = report.reportName;
            paramsToPass.parentGroupId = report.parentGroupId;
            paramsToPass.subGroupId = report.subGroupId;
            paramsToPass.menuType = report.menuType;
            paramsToPass.sortBy  = report.sortBy;
            paramsToPass.sortOn  = report.sortParam;

            PagingService.getNextPage(ReportDAO.getReports, paramsToPass, report.reports).then(function (res) {
                report.reports = res;
                //The directive code is not working so manually written
                report.reports.forEach(function (report) {
                    var state = 'techo.report.view/{"id":"' + report.id + '","queryParams":null}';
                    if ($sessionStorage.asldkfjlj) {
                        $sessionStorage.asldkfjlj[state] = true;
                    } else {
                        $sessionStorage.asldkfjlj = {};
                        $sessionStorage.asldkfjlj[state] = true;
                    }
                });
                Mask.hide();
            });
        };

        report.retrieveFilteredReports = function () {
            report.retrieveAll(true);
        };

        report.syncWithServer = function (ReportDetails) {
            report.syncModel = syncWithServerService.syncWithServer(ReportDetails.uuid);
        }

        report.navigateToState = function (reportObject) {
            let url = $state.href('techo.report.view', { "id": reportObject.id, "queryParams": null });
            window.open(url, '_blank');
        }

        report.deleteGroupModal = function (id) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this report?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                ReportDAO.deleteReport({ id: id }).then(function (res) {
                    report.retrieveFilteredReports();
                }).catch(function () {
                }).then(function () {
                    Mask.hide();
                });
            }, function () {
            });
        };

        report.parentGroupChanged = function (parentId) {
            if (parentId != null) {
                report.subGroup = [];
                angular.forEach(report.copyOfSubGroup, function (itr) {
                    if (itr.parentGroup === parentId) {
                        report.subGroup.push(itr);
                    }
                });
            } else {
                report.subGroupId = null;
                report.subGroup = angular.copy(report.copyOfSubGroup);
            }
        };

        report.subGroupChanged = function (subGroupId) {
            if (subGroupId !== null) {
                angular.forEach(report.copyOfSubGroup, function (itr) {
                    if (itr.id === subGroupId) {
                        report.parentGroupId = itr.parentGroup;
                    }
                });
            }
            report.parentGroupChanged(report.parentGroupId);
        };

        report.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        report.onMenuTypeChange = function () {
            Mask.show();
            GroupDAO.getReportGroups({ groupType: report.menuType, subGroupRequired: true }).then(function (res) {
                var parentGroup = [];
                report.subGroup = [];
                angular.forEach(res, function (itr) {
                    if (angular.isDefined(itr.parentGroup)) {
                        report.subGroup.push(itr);
                    } else {
                        parentGroup.push(itr);
                    }
                });
                report.parentGroup = parentGroup;
                report.copyOfSubGroup = angular.copy(report.subGroup);
                if (report.subGroupId !== null) {
                    report.subGroupChanged(report.subGroupId);
                }
            }).finally(function () {
                Mask.hide();
            });
        };

        report.sortBy = function (propertyName) {
            report.reverse = (report.propertyName === propertyName) ? !report.reverse : false;
            report.propertyName = propertyName;
        };

        report.sortByField = function (fieldName) {
            report.sortBy = fieldName;
            report.order = fieldName;
            if (report.sortOrder === 'asc') {
                report.sortOrder = 'desc';
            } else {
                report.sortOrder = 'asc';
            }
            report.sortParam = report.sortOrder;
            report.retrieveFilteredReports();
        };

        init();
    }
    angular.module('imtecho.controllers').controller('ReportController', ReportController);
})(window.angular);

