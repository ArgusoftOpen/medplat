(function () {
    function ManageWidgetsController(NotificationDAO, $uibModal, toaster, Mask, $filter, RoleService, GeneralUtil) {
        var ctrl = this;
        ctrl.isFilterOpen = false;
        ctrl.counter = 1;

        ctrl.init = function () {
            ctrl.getAllRoles();
        };

        /**
         * To retrieve role name by id
         */
        ctrl.getRoleNameByID = function (id) {
            return ctrl.roleList.filter(role => {
                return role.id === id;
            }).map(function (role) {
                return role.name;
            });
        }

        /**
         * To retrieve WEB notifications
         */
        ctrl.getWebNotifications = function () {
            Mask.show();
            NotificationDAO.retrieveAllNotifications().then(function (res) {
                ctrl.notificationList = $filter('filter')(res, { notificationType: 'WEB' });
                ctrl.notificationList.forEach((notification) => {
                    notification.roles = notification.roles.filter((roleID) => {
                        var role = ctrl.roleList.filter((rl) => {
                            return rl.id == roleID
                        });
                        if (role.length != 0) {
                            return role;
                        }
                    });
                    notification.escalationLevels.forEach((level) => {
                        level.roles = level.roles.filter((roleID) => {
                            var role = ctrl.roleList.filter((rl) => {
                                return rl.id == roleID
                            });
                            if (role.length != 0) {
                                return role;
                            }
                        })
                    });
                })
            }).finally(function () {
                Mask.hide();
            });
        };

        /**
         * To retrieve all roles
         */
        ctrl.getAllRoles = function () {
            RoleService.getAllActiveRoles().then(function (res) {
                ctrl.roleList = res;
                ctrl.getWebNotifications();
            }).finally(function () {
                Mask.hide();
            });
        }

        /**
         * To create list of roles to show in Action By
         */
        ctrl.generateRoleListForActionBy = function () {
            ctrl.roleListForActionBy = ctrl.roleList.filter(function (role) {
                return !ctrl.notificationObject.roles.includes(role.id);
            });
        }

        /**
         *  To Generate roles for each escalation level based on notification roles
         */
        ctrl.generateNotificationRoleList = function () {
            ctrl.tempRoleIds = [];
            ctrl.notificationObject.escalationLevels.forEach(level => {
                level.roles.forEach(roleID => {
                    if (ctrl.notificationObject.roles.indexOf(roleID) === -1)
                        ctrl.tempRoleIds.push(roleID);
                });
                ctrl.tempRoleIds.forEach(ID => {
                    level.roles.splice(level.roles.indexOf(ID), 1);
                    delete level.performAction[ID];
                });
                ctrl.tempRoleIds = [];
            });
            ctrl.generateEscalationRoleList();
        }

        /**
         * To Delete role of Notification
         */
        ctrl.deleteRoleOfNotification = function (index) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this role? It will also delete role from each escalation level.";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                ctrl.notificationObject.roles.splice(index, 1);
                ctrl.generateRoleListForActionBy();
                ctrl.generateNotificationRoleList();
                Mask.hide();
            }, function () { });
        }

        /**
         * To add selected roles to notification Role list
         */
        ctrl.addRolestoNotificationRoleList = function () {
            Mask.show();
            ctrl.notificationObject.selectedRoles.forEach(element => {
                if (ctrl.notificationObject.roles.indexOf(element) === -1) {
                    ctrl.notificationObject.roles.push(element);
                }
            });
            ctrl.notificationObject.selectedRoles = [];
            ctrl.generateEscalationRoleList();
            Mask.hide();
        }

        /**
         * Retrieve esalcaltion role list to show in dropdown for each level
         */
        ctrl.generateEscalationRoleList = function () {
            ctrl.notificationObject.escalationLevels.forEach(level => {
                level.escalationRoleList = [];
                ctrl.notificationObject.roles.forEach(roleID => {
                    if (level.roles.indexOf(roleID) == -1)
                        level.escalationRoleList.push(ctrl.roleList.filter(function (role) {
                            return role.id == roleID;
                        })[0]);
                })
            });
        }

        /**
         * To add selected roles to level roles
         */
        ctrl.addRolesInPerformer = function (levelObj, notificationObj) {
            Mask.show();
            levelObj.levelPerformers.forEach(element => {
                if (levelObj.roles.indexOf(element) === -1) {
                    levelObj.roles.push(element);
                }
            });
            levelObj.levelPerformers = [];
            ctrl.generateEscalationRoleList();
            Mask.hide();
        }

        ctrl.deleteRoleOFEscalationLevel = function (levelObj, index, notificationObj) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this role?";
                    }
                }
            });

            modalInstance.result.then(function () {
                Mask.show();
                delete levelObj.performAction[levelObj.roles[index]];
                levelObj.roles.splice(index, 1);
                ctrl.generateEscalationRoleList();
                Mask.hide();
            }, function () {
            });
        }

        ctrl.toggleFilter = function (assingMode, notificationObject) {
            if (assingMode) {
                ctrl.notificationObject = notificationObject;
                ctrl.escalationRoleList = ctrl.roleList.filter(function (role) {
                    return ctrl.notificationObject.roles.includes(role.id);
                });
                ctrl.generateRoleListForActionBy();
                ctrl.generateEscalationRoleList();
            }
            else
                ctrl.notificationObject = {};

            ctrl.isFilterOpen = !ctrl.isFilterOpen;
        };

        ctrl.updateWebNotification = function () {
            Mask.show();
            NotificationDAO.createOrUpdate(ctrl.notificationObject).then(function (res) {
                toaster.pop('success', 'Widget updated successfully');
                Mask.hide();
                ctrl.toggleFilter(true, ctrl.notificationObject);
                ctrl.getWebNotifications();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.init();

    }
    angular.module('imtecho.controllers').controller('ManageWidgetsController', ManageWidgetsController);
})();
