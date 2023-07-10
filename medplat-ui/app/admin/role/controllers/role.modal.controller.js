(function (angular) {
    var UpdateRoleModalController = function ($uibModalInstance, LocationService, roleId, GeneralUtil, RoleService, toaster, Mask, QueryDAO) {
        var $ctrl = this;
        $ctrl.roleForUpdate;
        $ctrl.createRoleFlag = false;
        $ctrl.updateRoleFlag = false;
        $ctrl.roleTypeList = ["WEB", "MOBILE", "BOTH"];
        if (roleId === undefined) {
            $ctrl.createRoleFlag = true;
        } else {
            $ctrl.updateRoleFlag = true;
        }

        if (roleId !== undefined) {
            Mask.show();
            RoleService.retrieveById(roleId).then(function (res) {
                $ctrl.roleForUpdate = res;
            }).finally(function () {
                Mask.hide();
            });
        }

        $ctrl.getManagedRoles = function () {
            Mask.show();
            RoleService.getAllActiveRoles().then(function (res) {
                $ctrl.managedRoleList = res;
            }).finally(function () {
                Mask.hide();

            });
        };

        $ctrl.getAllTeams = function () {
            Mask.show();
            QueryDAO.execute({
                code: "team_all_types_for_roles",
                parameters: {
                }
            }).then(function (res) {
                $ctrl.teamTypes = res.result;
            }).finally(function () {
                Mask.hide();

            });
        };

        $ctrl.getAllInfrastructureType = function () {
            Mask.show();
            QueryDAO.execute({
                code: "retrieve_field_values_for_form_field",
                parameters: {
                    form: 'WEB',
                    fieldKey: 'infra_type'
                }
            }).then(function (res) {
                $ctrl.infrastructureList = res.result;
            }).finally(function () {
                Mask.hide();
            });
        };

        $ctrl.getAllCategory = function () {
            Mask.show();
            QueryDAO.execute({
                code: "retrieve_field_values_for_form_field",
                parameters: {
                    form: 'WEB',
                    fieldKey: 'role_catg'
                }
            }).then(function (res) {
                $ctrl.categoryList = res.result;
            }).finally(function () {
                Mask.hide();
            });
        };

        $ctrl.getLocationTypes = function () {
            Mask.show();
            LocationService.retrieveLocationTypes().then(function (res) {
                $ctrl.locationTypeList = res;
            }).finally(function () {
                Mask.hide();
            });
        };

        $ctrl.ok = function () {
            $ctrl.roleUpdateForm.$setSubmitted();
            if ($ctrl.roleUpdateForm.$valid) {
                Mask.show();
                RoleService.createOrUpdate($ctrl.roleForUpdate).then(function (res) {
                    if (res) {
                        if ($ctrl.roleForUpdate.id === undefined) {
                            toaster.pop('success', 'Role added successfully');
                        } else {
                            toaster.pop('success', 'Role updated successfully');
                        }
                    }
                    $uibModalInstance.close($ctrl.roleForUpdate);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        $ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $ctrl.getManagedRoles();
        $ctrl.getAllTeams();
        $ctrl.getLocationTypes();
        $ctrl.getAllInfrastructureType();
        $ctrl.getAllCategory();
    };
    angular.module('imtecho.controllers').controller('UpdateRoleModalController', UpdateRoleModalController);
})(window.angular);
