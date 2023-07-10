(function () {
    function EditLocationController(AuthenticateService, Mask, toaster, LocationService, id, location,
        $uibModalInstance, rights, GeneralUtil, QueryDAO) {
        var ctrl = this;
        ctrl.env = GeneralUtil.getEnv();
        ctrl.isUpdate = false;
        ctrl.requiredLevel = null;
        ctrl.selectedLocation = null;
        ctrl.rights = rights;

        AuthenticateService.getAssignedFeature("techo.manage.location").then(function (res) {
            ctrl.rights = res.featureJson;
            if (!ctrl.rights) {
                ctrl.rights = {};
            }
        });

        Mask.show();
        QueryDAO.execute({
            code: 'retrive_location_type_by_level',
            parameters: {
                level: location.level
            }
        }).then((response) => {
            ctrl.locationTypes = response.result;
        }).catch((error) => {
            GeneralUtil.showMessageOnApiCallFailure(error);
        }).finally(() => {
            Mask.hide();
        });

        if (id && location) {
            ctrl.locationType = location.type;
            ctrl.isUpdate = true;
            ctrl.locationName = location.name;
            ctrl.emamta = location.locationCode;
            ctrl.locationHierarchy = location.locationHierarchy;
            ctrl.flag = location.locationFlag != null ? true : false;
            ctrl.containsCmtcCenter = location.containsCmtcCenter;
            ctrl.cerebralPalsyModule = location.cerebralPalsyModule;
            ctrl.geoFencing = location.geoFencing;
            ctrl.containsNrcCenter = location.containsNrcCenter;
            ctrl.englishName = location.englishName;
            ctrl.lgdCode = location.lgdCode;
            ctrl.mddsCode = location.mddsCode;
            ctrl.isTaaho = location.isTaaho;
        }

        ctrl.update = function (form) {
            if (form.$valid) {
                if ((ctrl.selectedLocationId && ctrl.updatedLocation.finalSelected.level >= ctrl.requiredLevel - 1) || !ctrl.isChangeLocation) {
                    var submitObj = angular.copy(location);
                    submitObj.name = ctrl.locationName;
                    submitObj.type = ctrl.locationType;
                    submitObj.locationCode = ctrl.emamta;
                    if (ctrl.selectedLocationId) {
                        submitObj.parent = ctrl.selectedLocationId;
                    }
                    if (ctrl.flag && ctrl.locationType == 'D') {
                        submitObj.locationFlag = 'ASPIRATIONAL';
                    } else if (!ctrl.flag && ctrl.locationType == 'D') {
                        submitObj.locationFlag = null;
                    } else if (ctrl.flag && ctrl.locationType == 'B') {
                        submitObj.locationFlag = 'PRIORITY_TALUKA';
                    } else if (!ctrl.flag && ctrl.locationType == 'B') {
                        submitObj.locationFlag = null;
                    }
                    if (ctrl.containsCmtcCenter != null && ctrl.containsCmtcCenter) {
                        submitObj.containsCmtcCenter = true;
                    } else {
                        submitObj.containsCmtcCenter = false;
                    }
                    if (ctrl.containsNrcCenter != null && ctrl.containsNrcCenter) {
                        submitObj.containsNrcCenter = true;
                    } else {
                        submitObj.containsNrcCenter = false;
                    }
                    if (ctrl.cerebralPalsyModule != null && ctrl.cerebralPalsyModule) {
                        submitObj.cerebralPalsyModule = true;
                    } else {
                        submitObj.cerebralPalsyModule = false;
                    }
                    if (ctrl.geoFencing != null && ctrl.geoFencing) {
                        submitObj.geoFencing = true;
                    } else {
                        submitObj.geoFencing = false;
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
                        toaster.pop('success', 'Location updated successfully');
                        ctrl.cancel('success');
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                } else if (!ctrl.selectedLocationId) {
                    toaster.pop('error', "To add location, Parent needs to be selected.")
                }
            }
        }

        ctrl.cancel = function (msg) {
            if (msg == undefined) {
                msg = 'cancel';
            }
            $uibModalInstance.dismiss(msg);
        }

        ctrl.onChangeLocation = function () {
            if (ctrl.isChangeLocation) {
                Mask.show();
                LocationService.retrieveLocationTypes().then(function (res) {
                    var levelObj = _.findWhere(res, { type: location.type });
                    ctrl.requiredLevel = levelObj.level - 1;
                }).finally(function () {
                    Mask.hide();
                })
            }
        }
    }
    angular.module('imtecho.controllers').controller('EditLocationController', EditLocationController);
})();
