(function () {
    function NotificationController($timeout, NotificationDAO, Mask, toaster, $uibModal, RoleService, SelectizeGenerator, GeneralUtil,syncWithServerService) {
        var notificationcontroller = this;
        notificationcontroller.counter = 1;
        notificationcontroller.notificationList = [];
        notificationcontroller.toggle = null;
        notificationcontroller.isFilterOpen = false
        notificationcontroller.updateMode = false;
        notificationcontroller.notificationObject = {};

        notificationcontroller.getNotifications = function () {
            Mask.show();
            NotificationDAO.retrieveAllNotifications().then(function (res) {
                notificationcontroller.notificationList = res;
            }).finally(function () {
                Mask.hide();
            });
        };

        notificationcontroller.addLevel = function () {
            var lastEscalationLevel = notificationcontroller.notificationObject.escalationLevels[notificationcontroller.notificationObject.escalationLevels.length - 1]
            if (lastEscalationLevel.name != null && lastEscalationLevel.roles != null && lastEscalationLevel.roles.length > 0) {
                notificationcontroller.counter++;
                notificationcontroller.notificationObject.escalationLevels.push({
                    counter: notificationcontroller.counter
                })
            }
        }

        notificationcontroller.removeEscalationLevel = function (level) {
            notificationcontroller.notificationObject.escalationLevels.forEach(function (iteratedLevel, index) {
                if (iteratedLevel.counter === level.counter) {
                    notificationcontroller.notificationObject.escalationLevels.splice(index, 1);
                }
            })
        }

        notificationcontroller.generateEscalationRoleList = function () {
            notificationcontroller.escalationRoleList = notificationcontroller.roleList.filter(function (role) {
                return notificationcontroller.notificationObject.roles.includes(role.id);
            });
            var selectizeObject = SelectizeGenerator.generateUserSelectize(notificationcontroller.notificationObject.roles);
            notificationcontroller.selectizeOptions = selectizeObject.config;
        }

        notificationcontroller.toggleNotification = function (notification) {
            let changedState, isActive;
            if (notification.state === "ACTIVE") {
                changedState = 'inactive';
                isActive = false;
            } else {
                isActive = true;
                changedState = 'active';
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state " + notification.state.toLowerCase() + ' to ' + changedState + '?';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                NotificationDAO.toggleActive(notification.id, isActive).then(function () {
                    notificationcontroller.getNotifications();
                    toaster.pop('success', 'State is successfully changed ' + notification.state.toLowerCase() + ' to ' + changedState);
                }).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        notificationcontroller.changeFiledsOnTypeChange = function () {
            if (notificationcontroller.notificationObject.notificationType != 'WEB')
                notificationcontroller.notificationObject.isLocationFilterRequired = false;
        }

        notificationcontroller.saveOrUpdateNotification = function () {
            if (notificationcontroller.notificationUpdateForm.$valid) {
                if (notificationcontroller.notificationObject.actionBase === 'MODAL') {
                    notificationcontroller.notificationObject.modalBasedAction = true;
                } else if (notificationcontroller.notificationObject.actionBase === 'URL') {
                    notificationcontroller.notificationObject.urlBasedAction = true;
                } else {
                    notificationcontroller.notificationObject.modalBasedAction = false;
                    notificationcontroller.notificationObject.urlBasedAction = false;
                }

                if (notificationcontroller.notificationObject.notificationType != 'WEB') {
                    notificationcontroller.notificationObject.isLocationFilterRequired = false;
                    notificationcontroller.notificationObject.fetchUptoLevel = null;
                    notificationcontroller.notificationObject.requiredUptoLevel = null;
                    notificationcontroller.notificationObject.isFetchAccordingAOI = true;
                }
                Mask.show();
                NotificationDAO.createOrUpdate(notificationcontroller.notificationObject).then(function (res) {
                    if (notificationcontroller.updateMode) {
                        toaster.pop('success', 'Notification updated successfully');
                    } else {
                        toaster.pop('success', 'Notification added successfully');
                    }
                    Mask.hide();
                    notificationcontroller.toggleFilter();
                    notificationcontroller.getNotifications();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        notificationcontroller.orderByNameFlag = true;
        notificationcontroller.orderByName = function () {
            if (notificationcontroller.orderByNameFlag) {
                notificationcontroller.orderByNameFlag = false;
            } else {
                notificationcontroller.orderByNameFlag = true;
            }
        };

        notificationcontroller.orderByCodeFlag = true;
        notificationcontroller.orderByCode = function () {
            if (notificationcontroller.orderByCodeFlag) {
                notificationcontroller.orderByCodeFlag = false;
            } else {
                notificationcontroller.orderByCodeFlag = true;
            }
        };

        notificationcontroller.orderByStateFlag = true;
        notificationcontroller.orderByState = function () {
            if (notificationcontroller.orderByStateFlag) {
                notificationcontroller.orderByStateFlag = false;
            } else {
                notificationcontroller.orderByStateFlag = true;
            }
        };

        notificationcontroller.toggleFilter = function (updateMode, notificationObject) {
            if (updateMode) {
                notificationcontroller.updateMode = true;
                notificationcontroller.notificationObject = notificationObject;
                if (notificationcontroller.notificationObject.modalBasedAction) {
                    notificationcontroller.notificationObject.actionBase = 'MODAL'
                } else if (notificationcontroller.notificationObject.urlBasedAction) {
                    notificationcontroller.notificationObject.actionBase = 'URL'
                } else {
                    notificationcontroller.notificationObject.actionBase = 'ACTION'
                }
                notificationcontroller.generateEscalationRoleList();
            } else {
                notificationcontroller.updateMode = false;
                notificationcontroller.notificationObject = {};
                notificationcontroller.notificationObject.notificationType = 'MO';
                notificationcontroller.notificationObject.escalationLevels = [];
                notificationcontroller.notificationObject.escalationLevels.push({
                    counter: notificationcontroller.counter,
                    name: 'Default'
                });
            }
            notificationcontroller.isFilterOpen = !notificationcontroller.isFilterOpen
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        $timeout(function () {
            $(".header-fixed").tableHeadFixer();
        });

        
        notificationcontroller.syncWithServer = function(notification){
            notificationcontroller.syncModel = syncWithServerService.syncWithServer(notification.uuid);
        }


        notificationcontroller.init = function () {
            var selectizeObject = SelectizeGenerator.generateUserSelectize();
            notificationcontroller.selectizeOptions = selectizeObject.config;
            notificationcontroller.getNotifications();
            RoleService.getAllRoles().then(function (res) {
                notificationcontroller.roleList = res;
            }).finally(function () {
                Mask.hide();

            });
        };

        notificationcontroller.init();
    }
    angular.module('imtecho.controllers').controller('NotificationController', NotificationController);
})();
