(function (angular) {
    var UpdateHealthInfraMappingeModalController = function ($rootScope, Mask, GeneralUtil, QueryDAO, toaster, $state, $q, $uibModalInstance, mapping) {
        var ctrl = this;

        ctrl.mapping = {
            locationType: []
        };

        if (mapping) {
            ctrl.isEdit = true;
            ctrl.mapping.healthInfraType = mapping.value;
            ctrl.mapping.location_level = mapping.location_level;
            if (mapping.allowedFacilities != null) {
                ctrl.mapping.allowedFacilities = mapping.allowedFacilities.split(',')
            }
        }

        ctrl.ok = function () {
            ctrl.mappingInfraForm.$setSubmitted();

            if (ctrl.mappingInfraForm.$valid) {

                var queryDto = {
                    code: 'insert_health_infra_type',
                    parameters: {
                        location_type: JSON.stringify(ctrl.mapping.locationType),
                        code: ctrl.mapping.healthInfraType,
                        value: ctrl.mapping.healthInfraType,
                        userId: "" + $rootScope.loggedInUserId || "-1",
                        allowedFacilities: ctrl.mapping.allowedFacilities.join()
                    }
                };

                if (ctrl.isEdit) {
                    queryDto.code = 'update_health_infra_mapping';
                    queryDto.parameters = {
                        userId: "" + $rootScope.loggedInUserId || "-1",
                        value: ctrl.mapping.healthInfraType,
                        id: mapping.health_infra_type_id,
                        location_type: JSON.stringify(ctrl.mapping.locationType),
                        allowedFacilities: ctrl.mapping.allowedFacilities.join()
                    }
                }
                Mask.show();
                QueryDAO.execute(queryDto).then(function (res) {
                    if (ctrl.isEdit) {
                        toaster.pop('success', 'Mapping Edited Successfully');
                    } else {
                        toaster.pop('success', 'Mapping Added Successfully.');
                    }
                    $uibModalInstance.close();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        var init = function () {
            let queries = [{
                code: 'get_locations_type',
                parameters: {},
                sequence: 1
            }, {
                code: 'retrival_listvalue_values_acc_field',
                parameters: {
                    fieldKey: 'health_infra_facilities'
                },
                sequence: 2
            }]
            Mask.show();
            QueryDAO.executeAll(queries).then((res) => {
                ctrl.allLocation = res[0].result;
                if (ctrl.mapping.location_level) {
                    const selectedLocations = mapping.location_type ? mapping.location_type.split(",") : [];
                    ctrl.mapping.locationType = res[0].result.filter(res => selectedLocations.indexOf(res.type) !== -1);
                }
                ctrl.allFacilities = res[1].result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        init();
    };
    angular.module('imtecho.controllers').controller('UpdateHealthInfraMappingeModalController', UpdateHealthInfraMappingeModalController);
})(window.angular);