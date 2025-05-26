(function () {
    function UsersController(UserDAO, USER, $uibModal, Mask, toaster, $timeout, PagingService, RoleDAO, AuthenticateService, $state, $sce, GeneralUtil) {
        var isPrintClicked = false;
        var ctrl = this;
        ctrl.env = GeneralUtil.getEnv();
        ctrl.appName = GeneralUtil.getAppName();
        ctrl.tag = null;
        ctrl.toggle = null;
        ctrl.usersList = [];
        ctrl.pagingService = PagingService.initialize();
        ctrl.criteria = {};
        ctrl.searchText = null;
        ctrl.noRolesAssignedToShow = false;
        ctrl.order = "asc";
        ctrl.orderBy = "userName";
        ctrl.getFromState = true;
        ctrl.statuses=['BOTH','ACTIVE','INACTIVE']

        // method to  show users list
        // ctrl.getUsers = function () {
        //     Mask.show();
        //     UserDAO.retrieveAllUsers().then(function (res) {
        //         ctrl.usersList = res;
        //     }).finally(function () {
        //         Mask.hide();
        //     });

        // };
        var statusDefault='BOTH';
        ctrl.getUsersByCriteria = function (reset) {
            ctrl.criteria = { roleid: null, locationId: null, searchString: null, orderby: null, order: ctrl.order, limit: ctrl.pagingService.limit, offset: ctrl.pagingService.offSet ,status: statusDefault};
            ctrl.criteria.orderby = ctrl.orderBy;
            var userList = ctrl.usersList;
            if (!reset) {
                userList = [];
                ctrl.pagingService.resetOffSetAndVariables();
                isPrintClicked = false;
            }
            commonCriteria();
            if (ctrl.getFromState) {
                if ($state.current.userSelectedRoleId != null) {
                    ctrl.criteria.roleid = $state.current.userSelectedRoleId;
                    ctrl.getFromState = false;
                }
                if ($state.current.userSelectedLocationId) {
                    ctrl.criteria.locationId = $state.current.userSelectedLocationId;
                    ctrl.getFromState = false;
                }
                if ($state.current.userSearched != null && $state.current.userSearched.length > 1) {
                    ctrl.searchText = $state.current.userSearched;
                    ctrl.criteria.searchString = $state.current.userSearched;
                    ctrl.getFromState = false;
                }
            }
            if (!isPrintClicked) {
                Mask.show();
                PagingService.getNextPage(UserDAO.retrieveByCriteria, ctrl.criteria, userList, null).then(function (res) {
                    ctrl.usersList = res;
                    angular.forEach(ctrl.usersList, function (user) {
                        if (user.title) {
                            user.fullName = user.title + ' ' + user.firstName + ' ' + user.lastName;
                        } else {
                            user.fullName = user.firstName + ' ' + user.lastName;
                        }
                        if (typeof (user.areaOfIntervention) === 'string') {
                            user.areaOfInterventionToDisplay = $sce.trustAsHtml(user.areaOfIntervention);
                        } else if (!user.areaOfIntervention) {
                            user.areaOfInterventionToDisplay = $sce.trustAsHtml('N.A');
                        }
                    });

                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                    if (ctrl.filter === 'search') {
                        $('#search').focus();
                    }
                });
            }
        };

        ctrl.getAllRoles = function () {
            Mask.show();
            RoleDAO.retieveRolesByRoleId().then(function (res) {
                ctrl.roles = res;
                if (!ctrl.roles || ctrl.roles.length <= 0) {
                    ctrl.noRolesAssignedToShow = true;
                }
            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.fetchUsers = function (id, filter) {
            ctrl.pagingService.resetOffSetAndVariables();
            isPrintClicked = false;
            if (id === 'click') {
                ctrl.toggleFilter();
            } else {
                ctrl.filterId = id;
                ctrl.filter = filter;
                $('#search').blur();
            }
            ctrl.getUsersByCriteria(id, filter);
        };


        ctrl.deleteUser = function (user) {
            var isActive = false;
            let changedState;
            if (user.displayState === USER.state.active) {
                changedState = 'inactive';

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
                        return "Are you sure you want to change the state from " + user.displayState + ' to ' + changedState + '? ';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                UserDAO.toggleActive(user, isActive).then(function () {
                    ctrl.fetchUsers(ctrl.filterId, ctrl.filter);
                    toaster.pop('success', 'State is successfully changed from ' + user.displayState + ' to ' + changedState);
                }, function (error) {
                    GeneralUtil.showMessageOnApiCallFailure(error, error.data.message);
                }).finally(function () {
                    Mask.hide();
                });

            }, function () {

            });
        };

        ctrl.createLoginCode = function (user) {

            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to Generate SOH code ?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                UserDAO.generateLoginCode(user.id).then(function (res) {
                    toaster.pop('success', 'You have successfully generated your login code. Your login code is ' + res.result);
                }, function (error) {
                    var errorMsg = JSON.parse(error.data.result);
                    toaster.pop('error', errorMsg.message);
                }).finally(function () {
                    Mask.hide();
                });
            }, function () {

            });
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

        //        ctrl.fetchUsers();
        ctrl.getAllRoles();
        AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
            ctrl.rights = res.featureJson;
            if (!ctrl.rights) {
                ctrl.rights = {};
            }
            $timeout(function () {
                $(".header-fixed").tableHeadFixer();
            });

        });
        AuthenticateService.getLoggedInUser().then(function (user) {
            ctrl.loggedInUser = user.data;
        });
        ctrl.printReport = function () {
            isPrintClicked = true;
            Mask.show();
            ctrl.criteria = { roleid: null, locationId: null, searchString: null, orderby: null, order: ctrl.order };
            ctrl.criteria.orderby = ctrl.orderBy;

            commonCriteria();
            UserDAO.retrieveByCriteria(ctrl.criteria).then(function (res) {
                ctrl.usersList = res;
                angular.forEach(ctrl.usersList, function (user) {
                    if (user.title) {
                        user.fullName = user.title + ' ' + user.firstName + ' ' + user.lastName;
                    } else {
                        user.fullName = user.firstName + ' ' + user.lastName;
                    }
                    if (user.areaOfIntervention) {
                        user.areaOfInterventionToDisplay = $sce.trustAsHtml(user.areaOfIntervention);
                    } else {
                        user.areaOfInterventionToDisplay = $sce.trustAsHtml('N.A');
                    }
                });
                ctrl.footer = "Generated by " + ctrl.loggedInUser.name + " at " + new Date().toLocaleString();
                $("thead tr th").css("position", "inherit");
                $('#printableDiv').printThis({
                    importCSS: false,
                    loadCSS: 'styles/css/printable.css',
                    header: '<h2>Users</h2>',
                    base: "./",
                    pageTitle: ctrl.appName
                });

            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.saveExcel = function () {
            Mask.show();
            ctrl.criteria = { roleid: null, locationId: null, searchString: null, orderby: null, order: ctrl.order };
            ctrl.criteria.orderby = ctrl.orderBy;

            commonCriteria()
            UserDAO.retrieveByCriteria(ctrl.criteria).then(function (res) {

                var excelData = res;
                ctrl.items = [];
                angular.forEach(excelData, function (user) {
                    if (user.title) {
                        user.fullName = user.title + ' ' + user.firstName + ' ' + user.lastName;
                    } else {
                        user.fullName = user.firstName + ' ' + user.lastName;
                    }
                    if (user.areaOfIntervention) {
                        user.areaOfInterventionToDisplay = $sce.trustAsHtml(user.areaOfIntervention);
                    } else {
                        user.areaOfInterventionToDisplay = $sce.trustAsHtml('N.A');
                    }
                });
                if (excelData.length !== 0) {
                    for (var i = 0; i < excelData.length; i++) {
                        excelData[i].areaOfIntervention = excelData[i].areaOfIntervention.replace(/<br>/g, '\n');
                        var excelObj = {
                            "Username": excelData[i].userName,
                            "Name": excelData[i].fullName,
                            "Role": excelData[i].roleName,
                            "Phone No": excelData[i].contactNumber,
                            [`${ctrl.appName} Phone No`]: excelData[i].techoPhoneNumber,
                            "Area Of Intervention": excelData[i].areaOfIntervention,
                            "Phone's IMEI No.": excelData[i].imeiNumber,
                            "User Status": excelData[i].displayState
                        };
                        ctrl.items.push(excelObj);
                    }
                    var mystyle = {
                        headers: true,
                        column: { style: { Font: { Bold: "1" } } }
                    };
                    alasql('SELECT * INTO XLSX("' + "Users List" + '",?) FROM ?', [mystyle, ctrl.items]);
                }

            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.navigateToEdit = function (id) {
            if (ctrl.criteria != null) {
                if (ctrl.criteria.roleid != null) {
                    $state.current.userSelectedRoleId = ctrl.criteria.roleid;
                }
                if (ctrl.criteria.locationId != null) {
                    $state.current.userSelectedLocationId = ctrl.criteria.locationId;
                }
            }
            if (ctrl.searchText != null) {
                $state.current.userSearched = ctrl.searchText;
            }
            $state.go("techo.manage.user", { id: id });
        }

        function commonCriteria() {
            if (ctrl.filter && ctrl.filterId) {
                if (ctrl.filter === 'sort') {
                    ctrl.criteria.orderby = ctrl.filterId;
                    ctrl.orderBy = ctrl.filterId;
                    if (ctrl.order === 'asc') {
                        ctrl.order = 'desc';
                        ctrl.criteria.order = 'desc';
                    } else {
                        ctrl.order = 'asc';
                        ctrl.criteria.order = 'asc';
                    }
                }
                if (ctrl.searchText && ctrl.searchText.length > 1) {
                    ctrl.criteria.searchString = ctrl.searchText;
                }
            }
            if (ctrl.selectedLocation && ctrl.selectedLocation.finalSelected) {
                var level = "level" + ctrl.selectedLocation.finalSelected.level;
                if (ctrl.selectedLocation[level] !== null) {
                    ctrl.criteria.locationId = ctrl.selectedLocation[level].id;
                } else {
                    if (ctrl.selectedLocation.finalSelected.level - 1 > 0) {
                        level = "level" + (ctrl.selectedLocation.finalSelected.level - 1);
                        ctrl.criteria.locationId = ctrl.selectedLocation[level].id;
                    }
                }
            }

            if (ctrl.selectedRole) {
                ctrl.criteria.roleid = ctrl.selectedRole.id;
            }
            if (ctrl.selectedStatus) {
                ctrl.criteria.status = ctrl.selectedStatus;
            }

        }

    }
    angular.module('imtecho.controllers').controller('UsersController', UsersController);
})();
