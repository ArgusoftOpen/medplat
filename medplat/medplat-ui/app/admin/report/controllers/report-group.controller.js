(function (angular) {
    function ReportGroupController($state, $uibModal, toaster, GroupDAO, Mask, GeneralUtil) {
        var reportGroup = this;
        reportGroup.initializing = true;

        var loadGroups = function () {
            Mask.show();
            var params = {
                groupType: reportGroup.groupType,
                subGroupRequired: true
            };
            return GroupDAO.getReportGroups(params).then(function (res) {
                reportGroup.allGroups = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        var init = function () {
            reportGroup.reportGroups = [];
            reportGroup.groupList = true;
            if ($state.params) {
                reportGroup.groupType = $state.params.type;
            }
            loadGroups().finally(function () {
                reportGroup.initializing = false;
            });
        };

        reportGroup.addGroup = function (reportGroupObject) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/report/views/report-group.modal.html',
                controller: 'ReportGroupModalController as groupModal',
                size: 'md',
                windowClass: 'cst-modal',
                resolve: {
                    reportGroupObject: function () {
                        return reportGroupObject;
                    }
                }
            });
            modalInstance.result.then(function (res) {
                loadGroups();
                if (reportGroupObject) {
                    toaster.success('', 'Group updated.');
                } else {
                    toaster.success('', 'Group created.');
                }
            }, function () { });
        };

        reportGroup.deleteGroupModal = function (groupId) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                size: 'med',
                windowClass: 'cst-modal',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this group?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                GroupDAO.deleteReportGroup({ id: groupId }).then(function () {
                    loadGroups();
                    toaster.success('', 'Group deleted.');
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () {
            });
        };

        init();
    }
    angular.module('imtecho.controllers').controller('ReportGroupController', ReportGroupController);
})(window.angular);
