(function () {
    function RoleController(RoleService, Mask, MenuConfigDAO, toaster, $uibModal, $timeout, $filter, $q, AuthenticateService, GeneralUtil) {
        var ctrl = this;
        ctrl.roleList = [];
        ctrl.featureList = [];
        ctrl.toggle = null;
        ctrl.showAllRolesFlag = false;
        ctrl.selectedFeatures = [];

        ctrl.showAllRoles = function () {
            let promises = [];
            if (ctrl.showAllRolesFlag) {// if show all role checkbox checked
                promises.push(RoleService.getAllRoles());
            } else {
                promises.push(RoleService.getAllActiveRoles());
            }
            promises.push(AuthenticateService.getLoggedInUser()); // To get assigned feature list for logged in user
            Mask.show();
            $q.all(promises).then(function (results) {
                ctrl.roleList = results[0]; // Role list
                ctrl.roleList.forEach(element => {
                    element.assignedFeaturesList = element.assignedFeatures && element.assignedFeatureList
                });
                let selectedRoleConfig = ctrl.loadedConfig ? ctrl.roleList.find(role => role.id === ctrl.loadedConfig.id) : null;
                // If any role selected then after update role, unassigned feature and active/inactive operations selected role should be display otherwise first role of the list.
                if (!selectedRoleConfig || (!ctrl.showAllRolesFlag && selectedRoleConfig.state === 'INACTIVE')) {
                    ctrl.search = '';
                    selectedRoleConfig = ctrl.roleList[0];
                }
                ctrl.loadConfig(selectedRoleConfig);
                ctrl.handleFeatureDetails(results[1]['data']['features']);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };


        /**
         * Handle feature details
         */
        ctrl.handleFeatureDetails = function (data) {
            ctrl.featureList = [];
            angular.forEach(data, function (value, key) {
                ctrl.withoutGroups = [];
                ctrl.withoutGroups = [];
                ctrl.withGroups = $filter('filter')(value, { isGroup: '!!' });
                ctrl.withGroups = $filter('orderBy')(ctrl.withGroups, 'displayOrder');
                ctrl.withoutGroups = $filter('filter')(value, { isGroup: '!' });
                ctrl.withoutGroups = $filter('orderBy')(ctrl.withoutGroups, 'displayOrder');
                ctrl.featureList = ctrl.featureList.concat(ctrl.withoutGroups.map(function (val) { // Feature list
                    val.groupName = key;
                    return val;
                }));

                ctrl.withGroups.map(function (val) {
                    var subgropus = val.subGroups.map(function (sgroup) {
                        // Retrieve only those property that define in withoutGroups otherewise it generates 'maximum-call-stack-size-exceeded' error
                        return {
                            featureJson: sgroup.featureJson,
                            groupName: key,
                            id: sgroup.id,
                            name: sgroup.name,
                            navigationState: sgroup.navigationState,
                            description: sgroup.description
                        }
                    });
                    ctrl.featureList = ctrl.featureList.concat(subgropus);
                });
            })
        }

        /**
         * Close modal
         */
        ctrl.cancel = function () {
            ctrl.selectedFeatures = [];
            ctrl.assignFeatureForm.$setPristine();
            $("#addFeature").modal('hide');
        }

        /**
         * Show assigned new feature modal
         */
        ctrl.showAddFeatureModal = function () {
            $("#addFeature").modal({ backdrop: 'static', keyboard: false });
            if (ctrl.loadedConfig.assignedFeaturesList != null) {
                ctrl.assignedFeatureIds = ctrl.loadedConfig.assignedFeaturesList.map(feature => feature.featureId);
                ctrl.availableFeatures = ctrl.featureList.filter(feature => !ctrl.assignedFeatureIds.includes(feature.id));
            } else {
                ctrl.availableFeatures = ctrl.featureList
            }
        }

        /**
         * Unassigned feature from selected role
         */
        ctrl.unassignedFeatureFromRole = function (feature) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to delete this?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                MenuConfigDAO.deleteConfig({ id: feature.userMenuItemId }).then(function () {
                    ctrl.showAllRoles();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        }

        /**
         * Assign feature to selected role
         */
        ctrl.addFeature = function () {
            ctrl.assignFeatureForm.$setSubmitted();
            if (ctrl.assignFeatureForm.$valid) {
                var rights = {};
                rights.featureId = ctrl.selectedFeatures.toString();
                Mask.show();
                MenuConfigDAO.assignNewFeature(ctrl.loadedConfig.id, rights).then(function (res) {
                    toaster.pop('success', 'Feature successfully assigned to selected role')
                    ctrl.cancel();
                    ctrl.selectedFeatures = [];
                    ctrl.assignFeatureForm.$setPristine();
                    ctrl.showAllRoles();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }


        ctrl.toggleRole = function (role) {
            let changedState, isActive;
            if (role.state === "ACTIVE") {
                changedState = 'inactive';
                isActive = false;
            } else {
                isActive = true;
                changedState = 'active';
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state from " + role.state.toLowerCase() + ' to ' + changedState + '?';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                RoleService.toggleActive(role, isActive).then(function () {
                    ctrl.showAllRoles();
                    toaster.pop('success', 'State is successfully changed from ' + role.state.toLowerCase() + ' to ' + changedState);
                }).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        ctrl.updateRole = function (roleId) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/role/views/role.modal.html',
                controller: 'UpdateRoleModalController',
                controllerAs: '$ctrl',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    roleId: function () {
                        return roleId;
                    }
                }
            });
            modalInstance.result.then(function () {
                ctrl.showAllRoles();
            }, function () { });
        };

        // In order to disply description of feature
        ctrl.getSelectedFeaturList = function () {
            ctrl.selectedFeatureList = ctrl.featureList.filter(feature => ctrl.selectedFeatures && ctrl.selectedFeatures.includes(feature.id));
        }

        /**
         * Define selected role
         */
        ctrl.loadConfig = function (role) {
            Mask.show();
            ctrl.loadedConfig = role;
            Mask.hide();
        }

        ctrl.showAllRoles();
        $timeout(function () {
            $(".header-fixed").tableHeadFixer();
        });

    }
    angular.module('imtecho.controllers').controller('RoleController', RoleController);
})();
