(function () {
    function MobileMenuConfigController($rootScope, Mask, GeneralUtil, QueryDAO, toaster, $state, $q, $uibModal) {
        var ctrl = this;


        ctrl.list = [];
        ctrl.menu = {};
        ctrl.selected = null;

        var getFeaturesList = function () {
            var defer = $q.defer();
            var queryDto = {
                code: 'mobile_feature_list'
            };
            Mask.show();
            QueryDAO.executeQuery(queryDto).then(function (response) {
                ctrl.allFeatures = response.result;
                defer.resolve();
            }, function (err) {
                defer.reject();
                GeneralUtil.showMessageOnApiCallFailure(err);
            }).finally(function () {
                Mask.hide();
            });
            return defer.promise;
        }

        var init = function () {
            getFeaturesList().then(function (response) {
                Mask.show();
                var roleQueryDto = {
                    code: 'retrieve_menu_config_role',
                    parameters: {
                        id: $state.params.id || -1
                    }
                }
                QueryDAO.executeQuery(roleQueryDto).then(function (role) {
                    ctrl.allDesignation = role.result;
                    setTimeout(() => {
                        $('#designation').trigger("chosen:updated");
                    });
                    if ($state.params.id) {
                        var queryDto = {
                            code: 'retrive_mobile_menu_master',
                            parameters: {
                                id: $state.params.id
                            }
                        }
                        Mask.show();
                        QueryDAO.executeQuery(queryDto).then(function (res) {
                            if (res.result.length > 0) {
                                ctrl.list = res.result;
                                const tmpFeatures = ctrl.list ?
                                    ctrl.list.map(item => item.mobile_constant) : [];
                                ctrl.allFeatures = ctrl.allFeatures.filter((item) =>
                                    !tmpFeatures.includes(item.mobile_constant));
                                ctrl.menu.name = res.result[0].menu_name;
                                ctrl.isEdit = true;
                                ctrl.menu.designationIds = [res.result[0].role_id];
                            }
                        }, function (err) {
                            GeneralUtil.showMessageOnApiCallFailure(err);
                        }).finally(function () {
                            Mask.hide();
                        });
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                }).finally(function () {
                    Mask.hide();
                });
            }, function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
            })
        };

        ctrl.addFeature = function (feature) {
            if (feature && feature.feature_name && !ctrl.list.some(e => e.feature_name === feature.feature_name)) {
                ctrl.allFeatures = ctrl.allFeatures.filter((item) => item.mobile_constant !== feature.mobile_constant)
                ctrl.list.push(feature);
            }
        }

        ctrl.deleteFeature = function (index, feature) {
            ctrl.allFeatures.push(feature);
            ctrl.list.splice(index, 1);
        }

        ctrl.saveMenuConfiguration = function () {
            ctrl.menuConfigForm.$setSubmitted();
            if (ctrl.menuConfigForm.$valid && ctrl.list.length > 0) {
                let config_json = [];
                for (let index = 0; index < ctrl.list.length; index++) {
                    let element = {};
                    element.mobile_constant = ctrl.list[index].mobile_constant;
                    element.order = index + 1;
                    config_json.push(element);
                }
                var queryDto = {
                    code: 'update_insert_mobile_menu_master',
                    parameters: {
                        menu_name: ctrl.menu.name,
                        config_json: JSON.stringify(config_json),
                        id: $state.params.id,
                        designationIds: ctrl.menu.designationIds.toString(),
                        userId: $rootScope.loggedInUserId || -1
                    }
                };

                Mask.show();
                QueryDAO.executeQuery(queryDto).then(function (res) {
                    if (ctrl.isEdit) {
                        toaster.pop('success', 'Menu Configuration Updated Successfully');
                    } else {
                        toaster.pop('success', 'Menu Configuration Saved Successfully');
                    }

                    $state.go('techo.manage.mobileMenuManagement');
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else if (!ctrl.menuConfigForm.$valid && !ctrl.menuConfigForm.designation.$valid) {
                toaster.pop('error', 'Please Select At List One Role');
            } else if (ctrl.list.length == 0) {
                toaster.pop('error', 'Please Select At List One Feature');
            }else {
                toaster.pop('error', 'Some Error While Saving Configuration');
            }
        }

        var openAddConfigurationModal = function (feature) {
            var defer = $q.defer();

            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/mobileMenuManagement/views/update-feature.modal.html',
                controller: 'UpdateFeatureModalController',
                controllerAs: 'ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    feature: function () {
                        return angular.copy(feature);
                    }
                }
            });
            modalInstance.result.then(function (type) {
                defer.resolve();

            }, function () {
                defer.reject();
            });
            return defer.promise;
        };

        ctrl.saveFeatureConfiguration = function (feature) {
            openAddConfigurationModal(feature).then(function () {
                getFeaturesList();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('MobileMenuConfigController', MobileMenuConfigController);
})();
