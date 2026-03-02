(function () {
    function ManageSystemConfigController($state, $stateParams, Mask, GeneralUtil, toaster, QueryDAO) {
        var ctrl = this;

        var initPage = function () {
            if ($stateParams.key) {
                ctrl.updateMode = true;
                ctrl.headerText = 'Update System Config';
                ctrl.retrieveSystemConfigByKey($stateParams.key);
            }
        }

        ctrl.retrieveSystemConfigByKey = function (key) {
            var dto = {
                code: 'retrieve_system_configuration_by_key',
                parameters: {
                    key: key
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                ctrl.config = res.result[0]
                ctrl.config.oldKey = ctrl.config.key;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.onSave = function (form) {
            if (form.$valid && !!ctrl.updateMode) {
                ctrl.checkIfKeyExists().then((res) => {
                    if (res.result[0].count > 0 && ctrl.config.key!=ctrl.config.oldKey) {
                        toaster.pop("error", "Same key already exists.");
                        return;
                    }
                var dtoUpdate = {
                    code: 'system_config_update',
                    parameters: {
                        key: ctrl.config.key,
                        value: ctrl.config.value,
                        isActive: ctrl.config.isActive,
                        oldKey: ctrl.config.oldKey
                    }
                };
                Mask.show();
                QueryDAO.execute(dtoUpdate).then(function (res) {
                    toaster.pop('success', 'System Configuration updated successfully!');
                    ctrl.chagneState();
                    }, GeneralUtil.showMessageOnApiCallFailure)
                        .finally(() => {
                            Mask.hide();
                        });

                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        
            else if (form.$valid && !ctrl.updateMode) {
                ctrl.checkIfKeyExists().then((res) => {
                    if (res.result[0].count > 0) {
                        toaster.pop("error", "Same key already exists.");
                        return;
                    }
                    var dtoAdd = {
                        code: 'system_config_add',
                        parameters: {
                            key: ctrl.config.key,
                            value: ctrl.config.value
                        }
                    };
                    Mask.show();
                    QueryDAO.execute(dtoAdd).then(function (res) {
                        toaster.pop('success', 'System Configuration added successfully!');
                        ctrl.chagneState();
                    }, GeneralUtil.showMessageOnApiCallFailure)
                        .finally(() => {
                            Mask.hide();
                        });

                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        }

        ctrl.checkIfKeyExists = function () {
            var dtoCheckExisting = {
                code: 'check_if_system_configuration_exists',
                parameters: {
                    key: ctrl.config.key,
                }
            };
            return QueryDAO.execute(dtoCheckExisting);
        }

        ctrl.chagneState = function () {
            $state.go('techo.manage.systemconfigs');
        }

        initPage();
    }
    angular.module('imtecho.controllers').controller('ManageSystemConfigController', ManageSystemConfigController);
})();
