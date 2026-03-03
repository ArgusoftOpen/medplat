(function (angular) {
    function MenuConfigController($q, QueryDAO, $filter, $uibModal, Mask, MenuConfigDAO, GeneralUtil, toaster, RoleDAO, SelectizeGenerator) {
        var menuconfig = this;
        var roleList = [], userGroupList = [], userList = [], menuConfigByTypePromises = {};
        menuconfig.displayFeatureNameMap = null;

        var extendFeatureJson = function (defaultJson, assignedJson) {
            var newJson = {};
            angular.forEach(defaultJson, function (value, key) {
                if (assignedJson && assignedJson.hasOwnProperty(key)) {
                    newJson[key] = assignedJson[key];
                } else {
                    newJson[key] = value;
                }
            });
            return newJson;
        };

        var init = function () {
            var selectizeObject = SelectizeGenerator.generateUserSelectize();
            selectizeObject.config.load = function (query, callback) {
                var selectize = this;
                var value = this.getValue();
                if (!value) {
                    selectize.clearOptions();
                    selectize.refreshOptions();
                }
                var promise;
                var queryDto = {
                    code: 'user_search_for_selectize',
                    parameters: {
                        searchString: query
                    }
                };
                promise = QueryDAO.execute(queryDto);
                promise.then(function (res) {
                    angular.forEach(res.result, function (result) {
                        result._searchField = query;
                    });
                    res.result = res.result.filter((user) => {
                        return !menuconfig.loadedConfig.rightList.some(function (f) {
                            return f.code === "User" && f.userId === user.id;
                        });
                    });
                    callback(res.result);
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    callback();
                });
            }
            menuconfig.selectizeOptions = selectizeObject.config;
            menuconfig.types = ["fhs", "training", "manage", "admin","ncd"];
            menuconfig.type = menuconfig.types[0];
            menuconfig.selectedTab = menuconfig.types[0];
            menuconfig.pageSet = true;
            Mask.show();
            var rolePromise = RoleDAO.retireveAll(true).then(function (data) {
                return data;
            }, GeneralUtil.showMessageOnApiCallFailure);
            $q.all([rolePromise]).then(function (data) {
                roleList = data[0];
                angular.forEach(roleList, function (designation) {
                    designation.enabled = true;
                });
                menuconfig.getTabData(menuconfig.type);
            }).finally(function () {
                Mask.hide();
            });
        };

        menuconfig.loadConfig = function (config) {
            config.userIds = [];
            config.designationIds = [];
            config.groupId = [];
            menuconfig.configRetrivalRunning = true;
            Mask.show();
            MenuConfigDAO.getConfigurationTypeById({ id: parseInt(config.id) }).then(function (response) {
                var localDesignationList = angular.copy(roleList);
                var localUserGroupList = angular.copy(userGroupList);
                var localUserList = angular.copy(userList);
                response.featureJson = angular.fromJson(response.featureJson);
                if (response.userMenuItemDtos && response.userMenuItemDtos.length > 0) {
                    angular.forEach(response.userMenuItemDtos, function (right) {
                        right.featureJson = extendFeatureJson(response.featureJson, angular.fromJson(right.featureJson));
                        if (right.designationId) {
                            right.code = 'Role';
                            right.displayName = right.roleName;
                        } else {
                            right.code = 'User';
                            right.displayName = right.fullName;
                        }
                    });
                } else {
                    _.each(localDesignationList, function (desg) {
                        desg.enabled = true;
                    });
                    _.each(localUserGroupList, function (userGroup) {
                        userGroup.enabled = true;
                    });
                    _.each(localUserList, function (emp) {
                        emp.enabled = true;
                    });
                }
                config.allUsers = localUserList;
                config.allDesignation = localDesignationList;
                config.allUserGroups = localUserGroupList;
                config.rightList = response.userMenuItemDtos;
                config.allDesignation = config.allDesignation.filter((designation) => {
                    return !config.rightList.some(function (f) {
                        return f.code === "Role" && f.designationId === designation.id;
                    });
                });
                menuconfig.loadedConfig = config;
                if (menuconfig.loadedConfig.featureJson != '{}' && !!menuconfig.loadedConfig.featureJson) {
                    menuconfig.getCustomDisplayFeatureName();
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
                menuconfig.configRetrivalRunning = false;
            });
        };

        menuconfig.getCustomDisplayFeatureName = function () {
            if (!!menuconfig.loadedConfig.featureJson) {
                var json = JSON.parse(menuconfig.loadedConfig.featureJson);
                var item = Object.keys(json);
                var queryDto = {
                    code: 'retrieve_display_name_for_feature',
                    parameters: {
                        feature_name_list: item, // passing array of string
                        menu_id: menuconfig.loadedConfig.id
                    }
                };
                QueryDAO.execute(queryDto).then(function (res) {
                    menuconfig.displayFeatureNameMap = null;
                    var result = res.result;
                    if (result.length > 0) {
                        menuconfig.displayFeatureNameMap = result.reduce(function (map, obj) {
                            map[obj.feature_name] = obj.display_name;
                            return map;
                        }, {});
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        menuconfig.getTabData = function (type) {
            if (type) {
                Mask.show();
                menuconfig.type = type;
                if (!menuConfigByTypePromises[type]) {
                    menuConfigByTypePromises[type] = MenuConfigDAO.getMenuConfigByType({ action: type });
                }
                menuConfigByTypePromises[type].then(function (res) {
                    menuconfig.menuList = $filter('orderBy')(res, ['groupName', 'name']);
                    menuconfig.loadConfig(menuconfig.menuList[0]);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
                menuconfig.selectedTab = type;
            }
        };

        menuconfig.saveMenuConfig = function (config) {
            var rights = {};
            if (config.designationIds && config.designationIds.length > 0) {
                rights.designationId = config.designationIds.toString();
            }
            if (config.userIds && config.userIds.length > 0) {
                rights.userId = config.userIds.toString();
            }
            if (isUserOrRoleAlreadyAdded(rights)) {
                toaster.pop('error', 'This user/role is already added. Please add another');
                config.designationIds = [];
                config.userIds = [];
            } else {
                Mask.show();
                MenuConfigDAO.saveMenuItem(config.id, rights).then(function (res) {
                    menuconfig.loadConfig(config);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        menuconfig.deleteConfigModal = function (rightId, config) {
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
                MenuConfigDAO.deleteConfig({ id: rightId }).then(function () {
                    menuconfig.loadConfig(config);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        menuconfig.featureUpdated = function (right) {
            var copyOfRight = angular.copy(right);
            copyOfRight.featureJson = angular.toJson(copyOfRight.featureJson);
            copyOfRight.menuConfigId = menuconfig.loadedConfig.id;
            Mask.show();
            MenuConfigDAO.updateUserMenuItem(copyOfRight).then(function () {
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (e) {
                Mask.hide();
            });
        };

        menuconfig.checkAll = function (right) {
            angular.forEach(right.featureJson, function (value, key) {
                right.featureJson[key] = true;
            });
            menuconfig.featureUpdated(right);
        };

        menuconfig.unCheckAll = function (right) {
            angular.forEach(right.featureJson, function (value, key) {
                right.featureJson[key] = false;
            });
            menuconfig.featureUpdated(right);
        };

        menuconfig.search = function (item) {
            if (!menuconfig.searchQuery ||
                item.name.toLowerCase().indexOf(menuconfig.searchQuery.toLowerCase()) !== -1 ||
                (item.groupName && item.groupName.toLowerCase().indexOf(menuconfig.searchQuery.toLowerCase()) !== -1)) {
                return true;
            }
            return false;
        };

        const isUserOrRoleAlreadyAdded = function (rights) {
            var availableRight = _.find(menuconfig.loadedConfig.rightList, function (assignedRight) {
                if (rights.userId) {
                    return assignedRight.userId == rights.userId;
                } else if (rights.designationId) {
                    return assignedRight.designationId == rights.designationId;
                }
            });
            if (availableRight) {
                return true;
            }
            return false;
        };

        init();
    }
    angular.module('imtecho.controllers').controller('MenuConfigController', MenuConfigController);
})(window.angular);
