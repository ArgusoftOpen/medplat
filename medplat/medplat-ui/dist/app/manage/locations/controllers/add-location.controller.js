(function () {
    function AddLocationController(Mask, toaster, LocationService, GeneralUtil, QueryDAO, $uibModal) {
        var ctrl = this;
        ctrl.env = GeneralUtil.getEnv();
        ctrl.focus = 'false';
        ctrl.isAspirationalDistrict = null;
        ctrl.isHighPriorityTaluka = null;

        Mask.show();
        LocationService.retrieveLocationTypes().then(function (res) {
            ctrl.locationTypes = res;
        }).finally(function () {
            Mask.hide();
        })

        ctrl.onChangeLocation = function (type) {
            if (type) {
                ctrl.selectedType = type.type;
                delete ctrl.selectedLocationId;
            }
            if (ctrl.selectedLocation != null) {
                ctrl.selectedLocation = {};
            }
        }

        ctrl.createOrUpdateLocation = (form) => {

            var submitObj = {
                name: ctrl.locationName,
                locationCode: ctrl.emamta,
                type: ctrl.selectedType,
                locationFlag: null,
                parent: ctrl.selectedLocationId
            }
            if (ctrl.isAspirationalDistrict != null && ctrl.isAspirationalDistrict == true) {
                submitObj.locationFlag = 'ASPIRATIONAL';
            }
            else if (ctrl.isHighPriorityTaluka != null && ctrl.isHighPriorityTaluka == true) {
                submitObj.locationFlag = 'PRIORITY_TALUKA';
            }
            if (ctrl.containsCmtcCenter != null && ctrl.containsCmtcCenter) {
                submitObj.containsCmtcCenter = true;
            }
            if (ctrl.containsNrcCenter != null && ctrl.containsNrcCenter) {
                submitObj.containsNrcCenter = true;
            }
            if (ctrl.cerebralPalsyModule != null && ctrl.cerebralPalsyModule) {
                submitObj.cerebralPalsyModule = true;
            }
            if (ctrl.geoFencing != null && ctrl.geoFencing) {
                submitObj.geoFencing = true;
            }
            if (ctrl.englishName != null) {
                submitObj.englishName = ctrl.englishName;
            }
            if (ctrl.lgdCode != null) {
                submitObj.lgdCode = ctrl.lgdCode;
            }
            if (ctrl.mddsCode != null) {
                submitObj.mddsCode = ctrl.mddsCode;
            }
            if (ctrl.isTaaho != null && ctrl.isTaaho) {
                submitObj.isTaaho = true;
            } else {
                submitObj.isTaaho = null;
            }

            Mask.show();
            LocationService.createOrUpdate(submitObj).then(function (res) {
                toaster.pop('success', 'Location created successfully');
                // ctrl.requiredLevel = null; // to prevent location hierarchy to get reset after saving the form
                ctrl.locationName = '';
                ctrl.englishName = '';
                ctrl.emamta = '';
                ctrl.lgdCode = '';
                ctrl.mddsCode = '';
                ctrl.focus = 'true';
                ctrl.isAspirationalDistrict = null;
                ctrl.isHighPriorityTaluka = null;
                form.$setPristine();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.create = function (form) {
            if (form.$valid) {
                if (ctrl.selectedLocationId && (ctrl.selectedLocation.finalSelected.level >= ctrl.requiredLevel.level - 1)) {
                    Mask.show();
                    let queryDto = {
                        code: 'location_lgd_code_check',
                        parameters: {
                            lgdCode: ctrl.lgdCode
                        }
                    };
                    QueryDAO.execute(queryDto).then(function (resForData) {
                        if (resForData.result[0] && resForData.result[0].is_present) {
                            ctrl.confirmAction(form);
                        } else {
                            ctrl.createOrUpdateLocation(form);
                        }
                    }, function (err) {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                    }).finally(function () {
                        Mask.hide();
                    });
                } else if (!ctrl.selectedLocationId) {
                    toaster.pop('error', "To add location, Parent needs to be selected.")
                }
            }
        }

        ctrl.confirmAction = function (form) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to save a new location with existing LGD code?";
                    },
                    title: function () {
                        return "Warning";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                ctrl.createOrUpdateLocation(form);
                Mask.hide();
            }, function () { });
        }

    }
    angular.module('imtecho.controllers').controller('AddLocationController', AddLocationController);
})();
