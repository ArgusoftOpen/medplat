(function () {
    function FacilityPerformanceController(FacilityPerformanceDAO, toaster, AuthenticateService, QueryDAO, Mask, GeneralUtil, $q) {
        var ctrl = this;
        // const FEATURE = 'techo.manage.facilityPerformancedynamic';
        const FACILITY_PERFORMANCE_FORM_CONFIGURATION_KEY = 'FACILITY_PERFORMANCE';
        ctrl.today = moment().startOf('day');
        ctrl.formData = {};
        ctrl.institutes = [];
        ctrl.minDate = moment().startOf('day').subtract(4, 'days');

        ctrl.init = () => {
            Mask.show();
            AuthenticateService.getLoggedInUser().then((response) => {
                ctrl.loggedInUser = response.data;
                return QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: ctrl.loggedInUser.id
                    }
                });
            }).then((response) => {
                if (Array.isArray(response.result) && response.result.length) {
                    ctrl.institutes = response.result;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure assigned' } });
                }
                // return AuthenticateService.getAssignedFeature(FEATURE);
            }).then((response) => {
                ctrl.formConfigurations = response.systemConstraintConfigs[FACILITY_PERFORMANCE_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response.webTemplateConfigs[FACILITY_PERFORMANCE_FORM_CONFIGURATION_KEY];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        ctrl.onAllComponentsLoaded = () => {
            if (Array.isArray(ctrl.institutes) && ctrl.institutes.length && 'healthInfrastructureIdList' in ctrl.formData) {
                ctrl.formData.healthInfrastructureIdList.data = ctrl.transformArrayToKeyValue(ctrl.institutes, 'id', 'name');
            }
        }

        ctrl.healthInfrastructureChanged = () => ctrl.getData(true);

        ctrl.performanceDateChanged = () => ctrl.getData(true);

        ctrl.getData = (showMessage) => {
            if (ctrl.formData.performanceDate && ctrl.formData.healthInfrastructureId) {
                ctrl.isUpdateMode = false;
                ctrl.formButtonText = 'Save';
                ctrl.isDataFetched = false;
                Mask.show();
                FacilityPerformanceDAO.getFacilityPerformaceByHidAndDate(ctrl.formData.healthInfrastructureId, moment(ctrl.formData.performanceDate).valueOf()).then((response) => {
                    if (response.id) {
                        if (showMessage) {
                            toaster.pop('warning', 'Facility performance already recorded for the selected date');
                        }
                        ctrl.isUpdateMode = true;
                        ctrl.formButtonText = 'Update';
                        ctrl.formData = Object.assign(ctrl.formData, {
                            id: response.id,
                            noOfOpdAttended: response.noOfOpdAttended,
                            noOfIpdAttended: response.noOfIpdAttended,
                            noOfLaboratoryTestConducted: response.noOfLaboratoryTestConducted,
                            noOfMajorOperationConducted: response.noOfMajorOperationConducted,
                            noOfMinorOperationConducted: response.noOfMinorOperationConducted,
                        })
                    } else {
                        ctrl.formData = Object.assign(ctrl.formData, {
                            id: null,
                            noOfOpdAttended: null,
                            noOfIpdAttended: null,
                            noOfLaboratoryTestConducted: null,
                            noOfMajorOperationConducted: null,
                            noOfMinorOperationConducted: null,
                        })
                        ctrl.facilityPerformanceForm.$setPristine();
                    }
                    return QueryDAO.executeAll([{
                        code: 'no_of_deliveries_conducted',
                        parameters: {
                            hid: Number(ctrl.formData.healthInfrastructureId),
                            performanceDate: moment(ctrl.formData.performanceDate).format('DD-MM-YYYY HH:mm:mm')
                        },
                        sequence: 1
                    }, {
                        code: 'no_of_caesarean_deliveries_conducted',
                        parameters: {
                            hid: Number(ctrl.formData.healthInfrastructureId),
                            performanceDate: moment(ctrl.formData.performanceDate).format('DD-MM-YYYY HH:mm:mm')
                        },
                        sequence: 2
                    }]);
                }).then((response) => {
                    ctrl.formData.noOfDeliveresConducted = response[0].result[0].count;
                    ctrl.formData.noOfSectionConducted = response[1].result[0].count;
                    ctrl.isDataFetched = true;
                }).catch((error) => {
                    ctrl.formData.performanceDate = null;
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.saveFacilityPerformance = () => {
            if (ctrl.facilityPerformanceForm.$valid) {
                Mask.show();
                FacilityPerformanceDAO.createOrUpdate(ctrl.formData).then(() => {
                    if (ctrl.isUpdateMode) {
                        toaster.pop('success', 'Facility Performance Updated Successfully');
                    } else {
                        toaster.pop('success', 'Facility Performance Saved Successfully');
                    }
                    ctrl.getData(false)
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.transformArrayToKeyValue = (array, keyProperty, valueProperty) => {
            return array.map((element) => {
                return {
                    key: element[keyProperty],
                    value: element[valueProperty]
                }
            });
        }

        ctrl.init();

    }
    angular.module('imtecho.controllers').controller('FacilityPerformanceController', FacilityPerformanceController);
})();
