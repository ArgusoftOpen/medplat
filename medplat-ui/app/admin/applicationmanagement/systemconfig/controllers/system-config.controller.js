(function () {
    function SystemConfigListController($state, Mask, GeneralUtil, toaster, QueryDAO, $uibModal, AuthenticateService) {
        var ctrl = this;

        ctrl.canAdd = true;
        ctrl.currentUser = {};

        var initPage = function () {
            ctrl.headerText = "";
            ctrl.orderByField = 'dateTime'
            ctrl.reverseOrder = true;
            ctrl.retrieveAllSystemConfigs();
            ctrl.getRights();
        }

        ctrl.retrieveAllSystemConfigs = function () {
            var dto = {
                code: 'system_configs_retrieve_all',
                parameters: {
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                ctrl.configs = res.result
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        AuthenticateService.getLoggedInUser().then(function (res) {
            ctrl.currentUser = res.data;
        });

        ctrl.getRights = function () {
            Mask.show();
            var stateName = 'techo.manage.systemconfigs';
            AuthenticateService.getAssignedFeature(stateName).then(function (res) {
                if (!!res) {
                    ctrl.rights = res.featureJson;
                    ctrl.rights.canAdd ? ctrl.canAdd = true : ctrl.canAdd = false;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.orderField = function (orderByField) {
            ctrl.orderByField = orderByField;
            ctrl.reverseOrder = !ctrl.reverseOrder;
        };

        ctrl.onAddEditClick = function (key) {
            $state.go('techo.manage.systemconfig', { key: key });
        }

        ctrl.toggleState = function (config) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state of this System configuration ?";
                    }
                }
            });
            modalInstance.result.then(function () {
                ctrl.toggle = config.state == false ? true : false;
                Mask.show();
                var dto = {
                    code: 'system_config_update',
                    parameters: {
                        key: config.key,
                        value: config.value,
                        isActive: ctrl.toggle,
                        oldKey: config.key
                    }
                };
                Mask.show();
                QueryDAO.execute(dto).then(function (res) {
                    toaster.pop('success', "System Configuration state changed successfully.");
                    ctrl.retrieveAllSystemConfigs();
                    Mask.hide();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        }

        initPage();
    }
    angular.module('imtecho.controllers').controller('SystemConfigListController', SystemConfigListController);
})();
