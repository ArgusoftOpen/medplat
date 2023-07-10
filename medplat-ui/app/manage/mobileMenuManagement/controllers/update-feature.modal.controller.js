(function (angular) {
    var UpdateFeatureModalController = function ($rootScope, Mask, GeneralUtil, QueryDAO, toaster, $state, $q, $uibModalInstance, feature) {
        var ctrl = this;

        ctrl.feature = {};

        if (feature) {
            ctrl.isEdit = true;
            ctrl.feature.mobileConstant = feature.mobile_constant;
            ctrl.feature.featureName = feature.feature_name;
            ctrl.feature.mobileDisplayName = feature.mobile_display_name;
        }

        const isValidMobileConstant = function () {
            var defer = $q.defer();
            var queryDto = {
                code: 'check_constant_validity_mobile_feature_master',
                parameters: {
                    mobile_constant: ctrl.feature.mobileConstant,
                    feature_name: ctrl.feature.featureName,
                    is_edit: ctrl.isEdit
                }
            };
            Mask.show();
            QueryDAO.executeQuery(queryDto).then(function (res) {
                defer.resolve(res.result[0].isValid);
            }, GeneralUtil.showMessageOnApiCallFailure)
                .finally(function () {
                    Mask.hide();
                });
            return defer.promise;
        }

        ctrl.ok = function () {

            ctrl.mobileFeatureForm.$setSubmitted();

            if (ctrl.mobileFeatureForm.$valid) {
                isValidMobileConstant().then(res => {
                    if (res) {
                        var queryDto = {
                            code: 'insert_update_mobile_menu_master',
                            parameters: {
                                mobile_constant: ctrl.feature.mobileConstant,
                                feature_name: ctrl.feature.featureName,
                                mobile_display_name: ctrl.feature.mobileDisplayName,
                                userId: $rootScope.loggedInUserId || -1
                            }
                        };

                        Mask.show();
                        QueryDAO.executeQuery(queryDto).then(function () {
                            if (ctrl.isEdit) {
                                toaster.pop('success', 'Feature Edited Successfully');
                            } else {
                                toaster.pop('success', 'Feature Added Successfully.');
                            }
                            $uibModalInstance.close();
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                        });
                    } else {
                        toaster.pop('error', 'Please change the Mobile Constant or Feature Name as it is already taken.');
                    }
                });
            }
        };

        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('UpdateFeatureModalController', UpdateFeatureModalController);
})(window.angular);
