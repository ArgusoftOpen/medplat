(function (angular) {
    var AddNotificationConfigurationController = function (RoleService, GeneralUtil, NotificationDAO, toaster, Mask) {
        /* var $ctrl = this;
        $ctrl.notificationForUpdate;
        $ctrl.createNotificationFlag = false;
        $ctrl.updateNotificationFlag = false;
        if (id === undefined) {
            $ctrl.createNotificationFlag = true;
        } else {
            $ctrl.updateNotificationFlag = true;
        }

        if (id !== undefined) {
            Mask.show();
            NotificationDAO.retrieveById(id).then(function (res) {
                $ctrl.notificationForUpdate = res;
            }).finally(function () {
                Mask.hide();
            });
        }
        $ctrl.getRoles = function () {
            Mask.show();
            RoleService.getAllRoles().then(function (res) {
                $ctrl.RoleList = res;
            }).finally(function () {
                Mask.hide();

            });
        };

        $ctrl.ok = function () {
            $ctrl.notificationUpdateForm.$setSubmitted();
            if ($ctrl.notificationUpdateForm.$valid) {
                Mask.show();
                NotificationDAO.createOrUpdate($ctrl.notificationForUpdate).then(function (res) {
                    if (res) {
                        if ($ctrl.notificationForUpdate.id === undefined) {
                            toaster.pop('success', 'Notification added successfully');
                        } else {
                            toaster.pop('success', 'Notification updated successfully');
                        }
                    }
                    // $uibModalInstance.close($ctrl.notificationForUpdate);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {

                    Mask.hide();

                });
            }
        };

        $ctrl.cancel = function () {
            // $uibModalInstance.dismiss('cancel');
        };

        $ctrl.getRoles(); */
    };
    angular.module('imtecho.controllers').controller('AddNotificationConfigurationController', AddNotificationConfigurationController);
})(window.angular);
