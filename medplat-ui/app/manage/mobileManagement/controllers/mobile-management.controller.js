(function () {
    function MobileManagementController(Mask, GeneralUtil, QueryDAO, toaster, AuthenticateService, $scope) {
        var ctrl = this;
        ctrl.userName = null;
        ctrl.userObj = null;
        ctrl.isDataSetFlag = false;
        ctrl.enterFlag = false;
        ctrl.installedDisplayList = [];
        ctrl.blockedImeis = [];

        var init = function () {
            var appInstallCountsDto = {
                code: 'version_based_app_install_counts'
            };
            Mask.show();
            QueryDAO.execute(appInstallCountsDto).then(function (res) {
                Mask.hide();
                if (res.result != null && res.result.length > 0) {
                    ctrl.versionBasedAppInstallCounts = res.result;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
            // if (ctrl.userName == null) {
            //    ctrl.toggleFilter();
            // }
            ctrl.getLoggedInUserId();
        };

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.resetField = function () {
            ctrl.userObj = {};
            ctrl.installedDisplayList = [];
            ctrl.blockedImeis = [];
        }

        ctrl.getLoggedInUserId = function () {
            AuthenticateService.getLoggedInUser().then(function (user) {
                $scope.loggedInUserId = user.data.id;
            });
        }

        ctrl.submit = function () {
            if (!ctrl.userName) {
                toaster.pop("error", "username can not be blank. Please enter valid username");
                ctrl.isDataSetFlag = false;
                ctrl.resetField();
                return;
            } else {
                if (ctrl.imei === null || ctrl.imei === "") {
                    ctrl.imei = null;
                }
                ctrl.getInstalledAppData();
            }
        };

        ctrl.enterSubmit = function () {
            ctrl.enterFlag = true;
            ctrl.submit();
        };

        ctrl.getInstalledAppData = function () {
            Mask.show();
            ctrl.resetField();
            var queryDto = {
                code: 'installed_app_info',
                parameters: {
                    user_name: ctrl.userName,
                    imei: ctrl.imei || null
                }
            };
            QueryDAO.execute(queryDto).then(function (res) {
                if (res.result.length < 1) {
                    toaster.pop("error", "This username/ imei is not valid. Please enter valid username/imei number");
                    ctrl.isDataSetFlag = false;
                    if (ctrl.enterFlag) {
                        ctrl.toggleFilter();
                    }
                    return;
                }
                ctrl.isDataSetFlag = true;
                ctrl.userObj = res.result;
                ctrl.createDisplayList();
                if (!ctrl.enterFlag) {
                    ctrl.toggleFilter();
                }
                ctrl.getBlockedImeiList();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.createDisplayList = function () {
            let unique = [...new Set(ctrl.userObj.map(item => item.imei))];
            for (var i = 0; i < unique.length; i++) {
                var imeiInstalledAppList = [];
                for (var j = 0; j < ctrl.userObj.length; j++) {
                    if (ctrl.userObj[j].imei == unique[i]) {
                        imeiInstalledAppList.push(ctrl.userObj[j]);
                    }
                }
                ctrl.installedDisplayList.push({
                    key: unique[i],
                    value: imeiInstalledAppList
                })
            }
        }

        ctrl.getBlockedImeiList = function () {
            var imeis = [...new Set(ctrl.userObj.map(item => item.imei))];
            var imeiList = "'" + imeis.join("','") + "'";
            Mask.show();
            var queryDto = {
                code: 'blocked_app_info',
                parameters: {
                    imei: imeiList
                }
            };
            QueryDAO.execute(queryDto).then(function (res) {
                if (res.result.length < 1) {
                    return;
                }
                var blockedObjList = res.result;
                ctrl.blockedImeis = [...new Set(blockedObjList.map(item => item.imei))];
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.displayButtonConditionally = function (imeiNumber) {
            if (ctrl.blockedImeis.includes(imeiNumber)) {
                return true;
            }
        }

        ctrl.unblockImei = function (imeiNumber) {
            Mask.show();
            var queryDto = {
                code: 'unblock_imei_number',
                parameters: {
                    imei: imeiNumber
                }
            };
            QueryDAO.execute(queryDto).then(function (res) {
                var index = ctrl.blockedImeis.indexOf(imeiNumber);
                if (index > -1) {
                    ctrl.blockedImeis.splice(index, 1);
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.blockImei = function (imeiNumber) {
            Mask.show();
            var queryDto = {
                code: 'block_imei_number',
                parameters: {
                    imei: imeiNumber,
                    userid: $scope.loggedInUserId
                }
            };
            QueryDAO.execute(queryDto).then(function (res) {
                //                ctrl.getInstalledAppData();
            }).catch(function (err) {
                toaster.pop("warning", "This IMEI is already blocked.");
            }).finally(function () {
                ctrl.blockedImeis.push(imeiNumber);
                Mask.hide();
            })
        }

        ctrl.deleteDtabaseByImei = function (imeiNumber) {
            Mask.show();
            var queryDto = {
                code: 'delete_imei_database',
                parameters: {
                    imei: imeiNumber,
                    userid: $scope.loggedInUserId
                }
            };
            QueryDAO.execute(queryDto).then(function () {
            }).catch(function (err) {
                toaster.pop("warning", "Something went wrong.");
            }).finally(function () {
                toaster.pop('success', 'You have deleted database sucessfully');
                Mask.hide();
            })
        }

        init();
    }
    angular.module('imtecho.controllers').controller('MobileManagementController', MobileManagementController);
})();
