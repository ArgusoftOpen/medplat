(function () {
    function ManageLocationTypeController(Mask, LocationService, GeneralUtil, toaster, $state, AuthenticateService, QueryDAO, $q) {
        let ctrl = this;
        const FEATURE = 'techo.manage.locationtype';
        const LOCATION_TYPE_FORM_CONFIGURATION_KEY = 'MANAGE_LOCATION_TYPE';
        ctrl.formData = {};

        ctrl.init = () => {
            Mask.show();
            LocationService.retrieveLocationTypes().then((response) => {
                ctrl.locationTypes = response;
                if (!!$state.params.id) {
                    ctrl.updateMode = true;
                    ctrl.headerText = 'Update Location Type'
                    ctrl.formButtonText = 'Update';
                    ctrl.getlocationType($state.params.id);
                } else {
                    ctrl.updateMode = false;
                    ctrl.formData.isSohEnable = true;
                    ctrl.headerText = 'Add Location Type';
                    ctrl.formButtonText = 'Save';
                    ctrl.types = ctrl.locationTypes.filter(r => r.type !== ctrl.formData.type).map(r => r.type);
                    ctrl.names = ctrl.locationTypes.filter(r => r.name !== ctrl.formData.name).map(r => r.name);
                }
                let promiseList = [];
                promiseList.push(AuthenticateService.getLoggedInUser());
                promiseList.push(AuthenticateService.getAssignedFeature(FEATURE));
                return $q.all(promiseList);
            }).then((response) => {
                ctrl.loggedInUser = response[0].data;
                ctrl.formConfigurations = response[1].systemConstraintConfigs[LOCATION_TYPE_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response[1].webTemplateConfigs[LOCATION_TYPE_FORM_CONFIGURATION_KEY];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.locationtype');
            }).finally(() => {
                Mask.hide();
            });
        };

        ctrl.getlocationType = (id) => {
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_location_type_by_id',
                parameters: { id }
            }).then((response) => {
                ctrl.formData = Object.assign(ctrl.formData, {
                    id: response.result[0].id,
                    isActive: response.result[0].isActive,
                    isSohEnable: response.result[0].isSohEnable,
                    level: response.result[0].level,
                    name: response.result[0].name,
                    type: response.result[0].type,
                })
                ctrl.types = ctrl.locationTypes.filter(r => r.type !== ctrl.formData.type).map(r => r.type);
                ctrl.names = ctrl.locationTypes.filter(r => r.name !== ctrl.formData.name).map(r => r.name);
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.save = () => {
            ctrl.locationTypeForm.$setSubmitted();
            if (ctrl.locationTypeForm.$valid) {
                let queryDto = {};
                if (ctrl.updateMode) {
                    queryDto = {
                        code: 'update_location_type',
                        parameters: {
                            id: ctrl.formData.id,
                            type: ctrl.formData.type,
                            name: ctrl.formData.name,
                            level: ctrl.formData.level,
                            isSohEnable: ctrl.formData.isSohEnable || false
                        }
                    }
                } else {
                    queryDto = {
                        code: 'create_location_type',
                        parameters: {
                            type: ctrl.formData.type,
                            name: ctrl.formData.name,
                            level: ctrl.formData.level,
                            isSohEnable: ctrl.formData.isSohEnable || false
                        }
                    }
                }
                QueryDAO.execute(queryDto).then(() => {
                    toaster.pop('success', `Location type ${ctrl.updateMode ? 'updated' : 'created'} successfully`);
                    ctrl.resetFormData();
                    ctrl.locationTypeForm.$setPristine();
                    $state.go('techo.manage.locationtype');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.typeChanged = (_, uuid) => {
            if (ctrl.formData.type) {
                let index = ctrl.types.findIndex(t => t.toUpperCase() === ctrl.formData.type.toUpperCase());
                if (index !== -1) {
                    ctrl.locationTypeForm[uuid].$setValidity('This type is already in use', false);
                } else {
                    ctrl.locationTypeForm[uuid].$setValidity('This type is already in use', true);
                }
            } else {
                ctrl.locationTypeForm[uuid].$setValidity('This type is already in use', true);
            }
        }

        ctrl.nameChanged = (_, uuid) => {
            if (ctrl.formData.name) {
                let index = ctrl.names.findIndex(t => t.toUpperCase() === ctrl.formData.name.toUpperCase());
                if (index !== -1) {
                    ctrl.locationTypeForm[uuid].$setValidity('This name is already in use', false);
                } else {
                    ctrl.locationTypeForm[uuid].$setValidity('This name is already in use', true);
                }
            } else {
                ctrl.locationTypeForm[uuid].$setValidity('This name is already in use', true);
            }
        }

        ctrl.resetFormData = () => Object.keys(ctrl.formData).forEach(key => delete ctrl.formData[key]);

        ctrl.goBack = () => {
            $state.go('techo.manage.locationtype');
        };

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('ManageLocationTypeController', ManageLocationTypeController);
})();
