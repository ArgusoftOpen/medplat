(function () {
    function LocationTypeController(Mask, LocationService, GeneralUtil, $uibModal, toaster, $state, QueryDAO) {
        let locationTypeCtrl = this;
        locationTypeCtrl.locationTypes = [];

        locationTypeCtrl.init = () => {
            Mask.show();
            LocationService.retrieveLocationTypes().then((res) => {
                locationTypeCtrl.locationTypes = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        locationTypeCtrl.onAddEditClick = (id) => {
            $state.go('techo.manage.managelocationtypedynamic', { id });
        };

        locationTypeCtrl.togglelocationType = (locationType) => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: () => {
                        return "Are you sure you want toggle the state of this location type ?";
                    }
                }
            });
            modalInstance.result.then(() => {
                Mask.show();
                QueryDAO.execute({
                    code: 'toggle_location_type_status',
                    parameters: {
                        id: locationType.id
                    }
                }).then(() => {
                    locationType.isActive = !locationType.isActive;
                    toaster.pop('success', 'Location Type updated successfully');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }, () => {
            });
        }

        locationTypeCtrl.init();

    }
    angular.module('imtecho.controllers').controller('LocationTypeController', LocationTypeController);
})();
