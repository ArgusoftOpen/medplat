(function () {
    function EventConfigurationsController($filter, Mask, GeneralUtil, toaster, $uibModal, EventConfigDAO ,syncWithServerService) {
        var eventConfigs = this;
        var init = function () {
            eventConfigs.retrieveAll();
        };

        eventConfigs.retrieveAll = function () {
            Mask.show();
            EventConfigDAO.retrieveAll().then(function (res) {
                eventConfigs.configuredEvents = res;
                eventConfigs.configuredEvents = $filter('orderBy')(eventConfigs.configuredEvents, ['-trigerWhen', 'day', 'hour', 'minute']);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        eventConfigs.runConfig = function (dto) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to run <b>" + dto.name + "</b> event config now?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                EventConfigDAO.runEvent(dto.id).then(function (res) {
                    toaster.pop("success", "Event configured for run now");
                    eventConfigs.retrieveAll();
                }).catch(function () {
                }).then(function () {
                    Mask.hide();
                });
            }, function () {
            });
        };

        eventConfigs.syncWithServer = function(eventConfig) {
            eventConfigs.syncModel = syncWithServerService.syncWithServer(eventConfig.uuid);
        }

        eventConfigs.delete = function (dto) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this event config?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                EventConfigDAO.toggleState(dto).then(function (res) {
                    toaster.pop("success", "Event configured status changed");
                    eventConfigs.retrieveAll();
                }).catch(function () {
                }).then(function () {
                    Mask.hide();
                });
            }, function () {
            });
        };

        eventConfigs.openExceptionsModal=function(dto){
            $uibModal.open({
                templateUrl: 'app/admin/applicationmanagement/event/views/event-exception.modal.html',
                controller: 'EventExceptionModalController',
                windowClass: 'cst-modal',
                size: 'xl',
                resolve: {
                    eventConfigId: function () {
                        return dto.id;
                    }
                }
            });
        }
        init();
    }
    angular.module('imtecho.controllers').controller('EventConfigurationsController', EventConfigurationsController);
})();
