(function () {
    function ManageLocationTypeController(Mask, LocationService, GeneralUtil, $stateParams, toaster, $state, AuthenticateService, QueryDAO) {
        let manageLocationTypeCtrl = this;
        manageLocationTypeCtrl.controls = [];

        manageLocationTypeCtrl.init = () => {
            Mask.show();
            LocationService.retrieveLocationTypes().then((res) => {
                manageLocationTypeCtrl.locationTypes = res;
                if (!!$stateParams.id) {
                    manageLocationTypeCtrl.updateMode = true;
                    manageLocationTypeCtrl.headerText = 'Update Location Type'
                    manageLocationTypeCtrl.getlocationType($stateParams.id);
                } else {
                    manageLocationTypeCtrl.updateMode = false;
                    manageLocationTypeCtrl.locationType = {};
                    manageLocationTypeCtrl.locationType.isSohEnable = true;
                    manageLocationTypeCtrl.headerText = 'Add Location Type';
                    manageLocationTypeCtrl.types = manageLocationTypeCtrl.locationTypes.filter(r => r.type !== manageLocationTypeCtrl.locationType.type).map(r => r.type);
                    manageLocationTypeCtrl.names = manageLocationTypeCtrl.locationTypes.filter(r => r.name !== manageLocationTypeCtrl.locationType.name).map(r => r.name);
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.locationtype');
            }).finally(() => {
                Mask.hide();
            });
        };

        manageLocationTypeCtrl.getlocationType = (id) => {
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_location_type_by_id',
                parameters: { id }
            }).then((response) => {
                manageLocationTypeCtrl.locationType = response.result[0];
                manageLocationTypeCtrl.types = manageLocationTypeCtrl.locationTypes.filter(r => r.type !== manageLocationTypeCtrl.locationType.type).map(r => r.type);
                manageLocationTypeCtrl.names = manageLocationTypeCtrl.locationTypes.filter(r => r.name !== manageLocationTypeCtrl.locationType.name).map(r => r.name);
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        manageLocationTypeCtrl.save = () => {
            manageLocationTypeCtrl.locationTypeForm.$setSubmitted();
            if (manageLocationTypeCtrl.locationTypeForm.$valid) {
                QueryDAO.execute({
                    code: 'create_location_type',
                    parameters: {
                        type: manageLocationTypeCtrl.locationType.type,
                        name: manageLocationTypeCtrl.locationType.name,
                        level: manageLocationTypeCtrl.locationType.level,
                        isSohEnable: manageLocationTypeCtrl.locationType.isSohEnable
                    }
                }).then(() => {
                    toaster.pop('success', 'Location type created successfully');
                    manageLocationTypeCtrl.locationType = {};
                    manageLocationTypeCtrl.locationTypeForm.$setPristine();
                    $state.go('techo.manage.locationtype');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        manageLocationTypeCtrl.update = () => {
            manageLocationTypeCtrl.locationTypeForm.$setSubmitted();
            if (manageLocationTypeCtrl.locationTypeForm.$valid) {
                Mask.show();
                QueryDAO.execute({
                    code: 'update_location_type',
                    parameters: {
                        id: manageLocationTypeCtrl.locationType.id,
                        type: manageLocationTypeCtrl.locationType.type,
                        name: manageLocationTypeCtrl.locationType.name,
                        level: manageLocationTypeCtrl.locationType.level,
                        isSohEnable: manageLocationTypeCtrl.locationType.isSohEnable
                    }
                }).then(() => {
                    toaster.pop('success', 'Location type updated successfully');
                    manageLocationTypeCtrl.locationType = {};
                    manageLocationTypeCtrl.locationTypeForm.$setPristine();
                    $state.go('techo.manage.locationtype');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        manageLocationTypeCtrl.typeChanged = () => {
            if (manageLocationTypeCtrl.locationType.type) {
                let index = manageLocationTypeCtrl.types.findIndex(t => t.toUpperCase() === manageLocationTypeCtrl.locationType.type.toUpperCase());
                if (index !== -1) {
                    manageLocationTypeCtrl.locationTypeForm.type.$setValidity('duplicate', false);
                } else {
                    manageLocationTypeCtrl.locationTypeForm.type.$setValidity('duplicate', true);
                }
            } else {
                manageLocationTypeCtrl.locationTypeForm.type.$setValidity('duplicate', true);
            }
        }

        manageLocationTypeCtrl.nameChanged = () => {
            if (manageLocationTypeCtrl.locationType.name) {
                let index = manageLocationTypeCtrl.names.findIndex(t => t.toUpperCase() === manageLocationTypeCtrl.locationType.name.toUpperCase());
                if (index !== -1) {
                    manageLocationTypeCtrl.locationTypeForm.name.$setValidity('duplicate', false);
                } else {
                    manageLocationTypeCtrl.locationTypeForm.name.$setValidity('duplicate', true);
                }
            } else {
                manageLocationTypeCtrl.locationTypeForm.name.$setValidity('duplicate', true);
            }
        }

        manageLocationTypeCtrl.cancel = () => {
            $state.go('techo.manage.locationtype');
        };

        manageLocationTypeCtrl.init();
    }
    angular.module('imtecho.controllers').controller('ManageLocationTypeController', ManageLocationTypeController);
})();
