(function (angular) {
    function ReportGroupModalController($state, $uibModalInstance, reportGroupObject, GroupDAO, GeneralUtil, Mask, APP_CONFIG) {
        var groupModal = this;

        var init = function () {
            groupModal.menuTypes = APP_CONFIG.menuTypes;
            groupModal.groupParam = {};
            if ($state.params) {
                groupModal.groupParam.type = $state.params.type;
            }
            if (reportGroupObject) {
                groupModal.groupParam = angular.copy(reportGroupObject);
            }
        };

        groupModal.retrieveGroups = function () {
            Mask.show();
            var params = {
                subGroupRequired: false,
                groupType: groupModal.groupParam.type
            };
            GroupDAO.getReportGroups(params).then(function (res) {
                groupModal.options = res;
                if (reportGroupObject) {
                    angular.forEach(groupModal.options, function (itr, index) {
                        if (itr.id === groupModal.groupParam.id) {
                            groupModal.options.splice(index, 1);
                        }
                    });
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        groupModal.save = function () {
            if (groupModal.groupForm.$valid) {
                Mask.show();
                GroupDAO.saveReportGroup(groupModal.groupParam).then(function (res) {
                    $uibModalInstance.close(res.res);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        groupModal.close = function () {
            $uibModalInstance.dismiss();
        };

        init();
    }
    angular.module('imtecho.controllers').controller('ReportGroupModalController', ReportGroupModalController);
})(window.angular);
