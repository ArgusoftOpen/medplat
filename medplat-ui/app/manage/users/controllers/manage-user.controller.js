(function () {
    function ManageUserController(UserDAO, RoleDAO, $stateParams, USER, GeneralUtil, $state, toaster, Mask, AuthenticateService, LocationService, $uibModal, $q, QueryDAO) {
        var usercontroller = this;
        usercontroller.env = GeneralUtil.getEnv();
        var initPage = function () {
            usercontroller.localLanguage = GeneralUtil.getLocalLanguage();
            usercontroller.checkuser = null;
            usercontroller.userObj = {};
            usercontroller.userObj = {
                password: 'Abcd@123',
                retypepass: 'Abcd@123'
            };
            usercontroller.userFormSubmitted = false;
            usercontroller.userUpdate = false;
            usercontroller.userObj.gender = 'M';
            usercontroller.userObj.prefferedLanguage = USER.prefferedLanguage.english;
            usercontroller.isShowPass = true;
            usercontroller.res = {};
            usercontroller.selectedLocationsId = [];
            usercontroller.temporary = [];
            usercontroller.maxAllowedLevel;
            usercontroller.userObj.title = 'Mr';
            if ($stateParams.id) {
                usercontroller.getUser($stateParams.id);
            }
            AuthenticateService.getAssignedFeature("techo.manage.users").then(function (res) {
                usercontroller.rights = res.featureJson;
                if (!usercontroller.rights) {
                    usercontroller.rights = {};
                }
                usercontroller.getAllActiveRoles();

            });
            usercontroller.isLocationButtonDisabled = false;
        };


        // method for  checking user name is available or not.

        usercontroller.checkUsername = function () {
            var defer = $q.defer();
            if (usercontroller.userObj.userName && usercontroller.userObj.userName !== '') {

                return UserDAO.isUsernameAvailable(usercontroller.userObj.userName, usercontroller.userObj.id).then(function (res) {
                    usercontroller.res.checkuser = res.result;
                }).finally(function () {
                });
            } else {
                defer.reject();
                return defer.promise;
            }
        };

        // method for getting User Roles.

        usercontroller.getAllActiveRoles = function () {
            Mask.show();
            RoleDAO.retieveRolesByRoleId(usercontroller.rights.isAdmin).then(function (res) {
                usercontroller.roleList = res;
            }).finally(function () {
                Mask.hide();
            });

        };

        usercontroller.checkPassword = () => {
            if (usercontroller.userObj.password && usercontroller.userObj.password.length < 8) {
                usercontroller.userForm.password.$setValidity('minlength', false);
            } else {
                usercontroller.userForm.password.$setValidity('minlength', true);
            }
            GeneralUtil.checkPassword('password', usercontroller.userObj.password, usercontroller.userObj.userName);
        }

        // method for saving a user data.

        usercontroller.saveUser = function (form) {

            usercontroller.createUserForm = form;
            usercontroller.userFormSubmitted = true;
            if (usercontroller.isHealthInfraMandatory && (usercontroller.userObj.infrastructureIds.length === 0
                || !usercontroller.userObj.infrastructureIds)) {
                toaster.pop('danger', 'Please select atleast one health infrastructure');
                return;
            }
            usercontroller.checkUsername().then(function () {
                if (usercontroller.userForm.$valid && usercontroller.selectedLocationsId.length > 0
                    && usercontroller.res.checkuser === 'true'
                    && usercontroller.phoneNumberFlag === false
                    // && usercontroller.aadharNumberFlag === false
                    && ((usercontroller.userObj.contactNumber !== null && usercontroller.userObj.contactNumber.length === 10)
                        || usercontroller.userObj.contactNumber === null
                        || usercontroller.userObj.contactNumber === ''
                        || usercontroller.userObj.contactNumber === undefined)
                    // && ((usercontroller.userObj.aadharNumber != null && usercontroller.userObj.aadharNumber.length === 12 && !usercontroller.checkAadharNumber)
                    //     || usercontroller.userObj.aadharNumber === null
                    //     || usercontroller.userObj.aadharNumber === ''
                    //     || usercontroller.userObj.aadharNumber === undefined)
                    && (usercontroller.userObj.firstName.length +
                        ((usercontroller.userObj.middleName && usercontroller.userObj.middleName.length) || 0) +
                        ((usercontroller.userObj.lastName && usercontroller.userObj.lastName.length) || 0) <= 99)) {
                    var userDto = {};
                    userDto.roleId = usercontroller.userObj.roleId;
                    userDto.userName = usercontroller.userObj.userName;
                    userDto.password = usercontroller.userObj.password;
                    userDto.firstName = usercontroller.userObj.firstName;
                    userDto.lastName = usercontroller.userObj.lastName;
                    userDto.middleName = usercontroller.userObj.middleName;
                    userDto.infrastructureIds = usercontroller.userObj.infrastructureIds;
                    userDto.defaultHealthInfrastructure = usercontroller.userObj.defaultHealthInfrastructure;
                    if (usercontroller.userObj.contactNumber === '') {
                        userDto.contactNumber = null;
                    } else {
                        userDto.contactNumber = usercontroller.userObj.contactNumber;
                    }
                    userDto.gender = usercontroller.userObj.gender;
                    userDto.title = usercontroller.userObj.title;
                    userDto.addedLocations = usercontroller.selectedLocationsId;
                    if (usercontroller.userObj.prefferedLanguage === '' || usercontroller.userObj.prefferedLanguage === null) {
                        userDto.prefferedLanguage = 'GU';
                    } else {
                        userDto.prefferedLanguage = usercontroller.userObj.prefferedLanguage;
                    }
                    // if (usercontroller.userObj.aadharNumber === '') {
                    //     userDto.aadharNumber = null;
                    // } else {
                    //     userDto.aadharNumber = usercontroller.userObj.aadharNumber;
                    // }
                    if (usercontroller.userObj.emailId !== null || usercontroller.userObj.emailId !== '') {
                        userDto.emailId = usercontroller.userObj.emailId;
                    }
                    if (usercontroller.userObj.convoxId !== null || usercontroller.userObj.convoxId !== '') {
                        userDto.convoxId = usercontroller.userObj.convoxId;
                    }
                    if (usercontroller.isGeolocationMandatory && usercontroller.userObj.latitude != null && usercontroller.userObj.latitude != '') {
                        userDto.latitude = Number(usercontroller.userObj.latitude).toFixed(6);
                    }
                    if (usercontroller.isGeolocationMandatory && usercontroller.userObj.longitude != null && usercontroller.userObj.longitude != '') {
                        userDto.longitude = Number(usercontroller.userObj.longitude).toFixed(6);
                    }
                    Mask.show();

                    UserDAO.createOrUpdate(userDto).then(function (res) {
                        clearUser(userDto);
                        toaster.pop('success', 'User Details saved!');
                        //                        $state.go('techo.manage.users');

                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {

                        Mask.hide();

                    });

                } else if (usercontroller.selectedLocationsId.length === 0) {
                    toaster.pop('danger', 'Please add atleast one area of intervention');
                }
            });


        };


        // method for getting a particular id of a user.


        usercontroller.getUser = function (id) {
            usercontroller.userUpdate = true;
            Mask.show();
            UserDAO.retrieveById(id).then(function (res) {
                usercontroller.userObj = res;
                usercontroller.selectedLocationsId = usercontroller.userObj.addedLocations;
                usercontroller.retrieveLocationByRoleId().then(function () {
                    usercontroller.selectedLocationsId = res.addedLocations;
                    usercontroller.deletedLocations = [];
                    let locations = _.pluck(usercontroller.selectedLocationsId, "locationId");
                    usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations);

                });

                usercontroller.userObj.retypepass = res.password;
            }).finally(function () {
                Mask.hide();
            });
        };

        // method for updating

        usercontroller.updateUser = function (form) {
            console.log(usercontroller.userForm);
            console.log(usercontroller.userObj);
            usercontroller.userFormSubmitted = true;
            usercontroller.userObj.deletedLocations = usercontroller.deletedLocations;
            var userDto = angular.copy(usercontroller.userObj);
            userDto.addedLocations = angular.copy(usercontroller.selectedLocationsId);
            if (usercontroller.userObj.contactNumber === '') {
                userDto.contactNumber = null;
            }
            // if (usercontroller.userObj.aadharNumber === '') {
            //     userDto.aadharNumber = null;
            // }
            // if (usercontroller.userObj.aadharNumberAvailable && !usercontroller.isUpdateAadhaar) {
            //     userDto.aadharNumber = null;
            // }
            if (usercontroller.isHealthInfraMandatory && (usercontroller.userObj.infrastructureIds.length === 0
                || !usercontroller.userObj.infrastructureIds)) {
                toaster.pop('danger', 'Please select atleast one health infrastructure');
                return;
            }
            if (usercontroller.userForm.$valid && userDto.addedLocations.length > 0
                && usercontroller.phoneNumberFlag === false
                // && usercontroller.aadharNumberFlag === false
                && ((usercontroller.userObj.contactNumber != null && usercontroller.userObj.contactNumber.length === 10)
                    || usercontroller.userObj.contactNumber === null
                    || usercontroller.userObj.contactNumber === ''
                    || usercontroller.userObj.contactNumber === undefined)
                // && ((usercontroller.userObj.aadharNumber != null && usercontroller.userObj.aadharNumber.length === 12 && !usercontroller.checkAadharNumber)
                //     || usercontroller.userObj.aadharNumber === null
                //     || usercontroller.userObj.aadharNumber === ''
                //     || usercontroller.userObj.aadharNumber === undefined)
                && (usercontroller.userObj.firstName.length +
                    ((usercontroller.userObj.middleName && usercontroller.userObj.middleName.length) || 0) +
                    ((usercontroller.userObj.lastName && usercontroller.userObj.lastName.length) || 0) <= 99)) {
                Mask.show();
                if (usercontroller.isGeolocationMandatory && usercontroller.userObj.latitude != null && usercontroller.userObj.latitude != '') {
                    userDto.latitude = Number(usercontroller.userObj.latitude).toFixed(6);
                }
                if (usercontroller.isGeolocationMandatory && usercontroller.userObj.longitude != null && usercontroller.userObj.longitude != '') {
                    userDto.longitude = Number(usercontroller.userObj.longitude).toFixed(6);
                }
                UserDAO.createOrUpdate(userDto).then(function (res) {
                    if (!!res) {
                        toaster.pop('success', 'User Details updated!');
                        $state.go('techo.manage.users');

                    }

                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else if (userDto.addedLocations.length === 0) {
                toaster.pop('danger', 'Please add atleast one area of intervention');
            }

        };


        // method for setUsername and fetching the avialable using names.
        usercontroller.setUserName = function () {
            if ((!usercontroller.userObj.userName || !usercontroller.manually) && !usercontroller.userUpdate) {
                if (usercontroller.userObj.firstName && usercontroller.userObj.middleName && usercontroller.userObj.lastName) {
                    var tempusername = usercontroller.userObj.firstName.charAt(0).toLowerCase()
                        + usercontroller.userObj.middleName.charAt(0).toLowerCase()
                        + usercontroller.userObj.lastName.toLowerCase();
                    usercontroller.userObj.userName = tempusername;
                    Mask.show();
                    UserDAO.fetchAvailableUsername(tempusername).then(function (res) {
                        usercontroller.userObj.userName = res.result;
                        usercontroller.res.checkuser = 'true';
                    }).finally(function () {
                        Mask.hide();
                    });

                }
            }
        };
        // usercontroller.showusernameinaadhar = null;
        usercontroller.showusernameinphone = null;
        // usercontroller.aadharNumberFlag = false;
        usercontroller.phoneNumberFlag = false;
        usercontroller.checkphoneNumber = function (phoneNumber, userId) {
            if (phoneNumber) {
                UserDAO.checkPhone(phoneNumber, userId).then(function (res) {
                    usercontroller.checkaadharorphone = res;
                    if (phoneNumber !== null) {
                        usercontroller.phoneNumberFlag = false;
                    }
                }, function (error) {
                    if (phoneNumber !== null) {
                        usercontroller.phoneNumberFlag = true;
                        usercontroller.showusernameinphone = error.data.data;
                    }
                }).finally(function () {
                });
            }
        };

        usercontroller.checkPhoneNo = false;
        usercontroller.checkPhoneNolength = false;
        usercontroller.checkPhoneNumberOnChange = function (phoneNo,id) {
            usercontroller.checkPhoneNo = false;
            usercontroller.phoneNumberFlag = false;
            usercontroller.checkPhoneNolength = false;

            if ((phoneNo !== undefined && phoneNo.length < 10) && phoneNo) {
                usercontroller.checkPhoneNo = true;
                usercontroller.checkPhoneNolength = false;

            } else if (phoneNo !== undefined && phoneNo.length == 10) {
                usercontroller.checkPhoneNo = false;
                usercontroller.checkPhoneNolength = false;
            } else if (phoneNo !== undefined && phoneNo.length > 10) {
                usercontroller.checkPhoneNo = false;
                usercontroller.checkPhoneNolength = true;
            } else {
                usercontroller.checkPhoneNo = false;
                usercontroller.phoneNumberFlag = false;
                usercontroller.checkPhoneNolength = false;
            }
            if (isNaN(phoneNo)) {
                usercontroller.checkPhoneNo = false;
                usercontroller.phoneNumberFlag = false;
            }
            if(phoneNo.length === 10){
                usercontroller.checkphoneNumber(phoneNo,id);
            }
           
        };
        // usercontroller.checkAadharNumber = false;
        // usercontroller.checkAadharNolength = false;
        // usercontroller.checkAadharNumberOnChange = function (aadharNumber) {
        //     usercontroller.aadharNumberFlag = false;
        //     usercontroller.checkAadharNumber = false;
        //     usercontroller.checkAadharNolength = false;
        //     if (aadharNumber && aadharNumber.length != 12) {
        //         usercontroller.checkAadharNolength = true;
        //     } else if (aadharNumber && aadharNumber.length == 12) {
        //         res = /^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$/.test(aadharNumber);
        //         if (res) {
        //             usercontroller.checkAadharNumber = false;
        //             usercontroller.checkAadharNolength = false;
        //         } else {
        //             usercontroller.checkAadharNumber = true;
        //         }
        //     } else {
        //         usercontroller.aadharNumberFlag = false;
        //         usercontroller.checkAadharNumber = false;
        //         usercontroller.checkAadharNolength = false;
        //     }
        //     if (isNaN(aadharNumber)) {
        //         usercontroller.checkAadharNumber = false;
        //         usercontroller.aadharNumberFlag = false;
        //     }
        // };
        // method used for toggling the icon in the password.


        usercontroller.toggleShowPassword = function () {

            usercontroller.isShowPass = !usercontroller.isShowPass;
        };

        usercontroller.selectedArea = function () {
            if (usercontroller.userObj.roleId) {
                delete usercontroller.addedUsers;
                usercontroller.noRoleSelected = false;
                usercontroller.locationForm.$setSubmitted();
                if (usercontroller.selectedLocation.finalSelected !== null) {
                    let selectedobj;
                    let selectedobjs = [];
                    if (usercontroller.selectedLocation.finalSelected.optionSelected) {
                        if (Array.isArray(usercontroller.selectedLocation.finalSelected.optionSelected) && usercontroller.selectedLocation.finalSelected.optionSelected.length > 0) {
                            for (let option in usercontroller.selectedLocation.finalSelected.optionSelected) {
                                selectedobjs.push({
                                    locationId: usercontroller.selectedLocation.finalSelected.optionSelected[option].id,
                                    type: usercontroller.selectedLocation.finalSelected.optionSelected[option].type,
                                    level: usercontroller.selectedLocation.finalSelected.level,
                                    name: usercontroller.selectedLocation.finalSelected.optionSelected[option].name
                                });
                            }
                        }
                        else if (!Array.isArray(usercontroller.selectedLocation.finalSelected.optionSelected)) {
                            selectedobj = {
                                locationId: usercontroller.selectedLocation.finalSelected.optionSelected.id,
                                type: usercontroller.selectedLocation.finalSelected.optionSelected.type,
                                level: usercontroller.selectedLocation.finalSelected.level,
                                name: usercontroller.selectedLocation.finalSelected.optionSelected.name
                            };
                        }
                    } else {
                        selectedobj = {
                            locationId: usercontroller.selectedLocation["level" + (usercontroller.selectedLocation.finalSelected.level - 1)].id,
                            type: usercontroller.selectedLocation["level" + (usercontroller.selectedLocation.finalSelected.level - 1)].type,
                            level: usercontroller.selectedLocation.finalSelected.level - 1,
                            name: usercontroller.selectedLocation["level" + (usercontroller.selectedLocation.finalSelected.level - 1)].name
                        };
                    }
                    usercontroller.duplicateEntry = false;
                    for (const element of usercontroller.selectedLocationsId) {
                        if (!angular.isUndefined(selectedobj) && selectedobj !== null && element.locationId === selectedobj.locationId) {
                            usercontroller.duplicateEntry = true;
                            usercontroller.isLocationButtonDisabled = false;
                        }
                        else if (!angular.isUndefined(selectedobjs) && selectedobjs.length > 0) {
                            for (let obj in selectedobjs) {
                                if (obj !== null && element.locationId === selectedobjs[obj].locationId) {
                                    usercontroller.duplicateEntry = true;
                                    usercontroller.isLocationButtonDisabled = false;
                                }
                            }
                        }
                    }
                    if (!usercontroller.duplicateEntry && !angular.isUndefined(selectedobj)) {
                        usercontroller.isNotAllowedLocation = false;
                        if (!usercontroller.selectedLocationsId) {
                            usercontroller.selectedLocationsId = [];
                        }
                        if ((!usercontroller.allowedLocations || usercontroller.allowedLocations.length === 0) || (usercontroller.allowedLocations.length > 0 && usercontroller.allowedLocations.indexOf(selectedobj.type) >= 0)) {
                            let selectedLocationIds = _.pluck(usercontroller.selectedLocationsId, "locationId");
                            usercontroller.isLocationButtonDisabled = true;
                            UserDAO.validateaoi(usercontroller.userObj.roleId, selectedLocationIds, selectedobj.locationId, usercontroller.userObj.id).then(function (res) {
                                if (res.errorcode === 1) {
                                    usercontroller.errorCode = res.errorcode;
                                    if (res.data) {
                                        usercontroller.addedUsers = _.pluck(res.data, "userName").join();
                                        let modalInstance = $uibModal.open({
                                            templateUrl: 'app/common/views/confirmation.modal.html',
                                            controller: 'ConfirmModalController',
                                            windowClass: 'cst-modal',
                                            size: 'med',
                                            resolve: {
                                                message: function () {
                                                    return "User with user name " + usercontroller.addedUsers + " already added at same area of intervention.Are you sure you want to add?";
                                                }
                                            }
                                        });
                                        modalInstance.result.then(function () {
                                            selectedobj.locationFullName = res.message;
                                            usercontroller.selectedLocationsId.push(selectedobj);
                                            let locations = _.pluck(usercontroller.selectedLocationsId, "locationId")
                                            usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations)
                                        }, function () {
                                        });
                                    } else {
                                        selectedobj.locationFullName = res.message;
                                        usercontroller.selectedLocationsId.push(selectedobj);
                                        let locations = _.pluck(usercontroller.selectedLocationsId, "locationId")
                                        usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations)
                                    }
                                } else if (res.errorcode === 2) {
                                    if (res.data) {
                                        usercontroller.addedUsers = _.pluck(res.data, "userName").join();
                                        let modalInstance = $uibModal.open({
                                            templateUrl: 'app/common/views/alert.modal.html',
                                            controller: 'alertModalController',
                                            windowClass: 'cst-modal',
                                            size: 'med',
                                            resolve: {
                                                message: function () {
                                                    return "You cannot add this role under this user location as " + res.message + " Users with user name " + usercontroller.addedUsers + " already added at same area of intervention.";
                                                }
                                            }
                                        });
                                        modalInstance.result.then(function () {
                                        }, function () {
                                        });
                                    } else {
                                        usercontroller.errorMsg = res.message;
                                        usercontroller.errorCode = res.errorcode;
                                    }
                                }
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                usercontroller.isLocationButtonDisabled = false;
                            });
                            usercontroller.locationForm.$setPristine();
                        } else {
                            usercontroller.isNotAllowedLocation = true;
                        }
                    }
                    else if (!usercontroller.duplicateEntry) {
                        usercontroller.isNotAllowedLocation = false;
                        if (!usercontroller.selectedLocationsId) {
                            usercontroller.selectedLocationsId = [];
                        }
                        (async function () {
                            for (let obj in selectedobjs) {
                                if ((!usercontroller.allowedLocations || usercontroller.allowedLocations.length === 0) || (usercontroller.allowedLocations.length > 0 && usercontroller.allowedLocations.indexOf(selectedobjs[obj].type) >= 0)) {
                                    let selectedLocationIds = _.pluck(usercontroller.selectedLocationsId, "locationId");
                                    usercontroller.isLocationButtonDisabled = true;
                                    try {
                                        res = await UserDAO.validateaoi(usercontroller.userObj.roleId, selectedLocationIds, selectedobjs[obj].locationId, usercontroller.userObj.id);
                                        if (res.errorcode === 1) {
                                            usercontroller.errorCode = res.errorcode;
                                            if (res.data) {
                                                usercontroller.addedUsers = _.pluck(res.data, "userName").join();
                                                let modalInstance = $uibModal.open({
                                                    templateUrl: 'app/common/views/confirmation.modal.html',
                                                    controller: 'ConfirmModalController',
                                                    windowClass: 'cst-modal',
                                                    size: 'med',
                                                    resolve: {
                                                        message: function () {
                                                            return "User with user name " + usercontroller.addedUsers + " already added at same area of intervention.Are you sure you want to add?";
                                                        }
                                                    }
                                                });
                                                modalInstance.result.then(function () {
                                                    selectedobjs[obj].locationFullName = res.message;
                                                    usercontroller.selectedLocationsId.push(selectedobjs[obj]);
                                                    let locations = _.pluck(usercontroller.selectedLocationsId, "locationId")
                                                    usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations)
                                                }, function () {
                                                });
                                            } else {
                                                selectedobjs[obj].locationFullName = res.message;
                                                usercontroller.selectedLocationsId.push(selectedobjs[obj]);
                                                let locations = _.pluck(usercontroller.selectedLocationsId, "locationId")
                                                usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations)
                                            }
                                        } else if (res.errorcode === 2) {
                                            if (res.data) {
                                                usercontroller.addedUsers = _.pluck(res.data, "userName").join();
                                                let modalInstance = $uibModal.open({
                                                    templateUrl: 'app/common/views/alert.modal.html',
                                                    controller: 'alertModalController',
                                                    windowClass: 'cst-modal',
                                                    size: 'med',
                                                    resolve: {
                                                        message: function () {
                                                            return "You cannot add this role under this user location as " + res.message + " Users with user name " + usercontroller.addedUsers + " already added at same area of intervention.";
                                                        }
                                                    }
                                                });
                                                modalInstance.result.then(function () {
                                                }, function () {
                                                });
                                            } else {
                                                usercontroller.errorMsg = res.message;
                                                usercontroller.errorCode = res.errorcode;
                                            }
                                        }
                                    }
                                    finally {
                                        usercontroller.isLocationButtonDisabled = false;
                                    }
                                } else {
                                    usercontroller.isNotAllowedLocation = true;
                                }
                            }
                            usercontroller.locationForm.$setPristine();
                        })();
                    }
                }
            } else {
                usercontroller.noRoleSelected = true;
                usercontroller.isLocationButtonDisabled = false;
            }
        };

        usercontroller.deletedLocations = [];
        usercontroller.removeSelectedArea = function (removedLoc, index) {
            usercontroller.deletedLocations.push(removedLoc);
            if (!!usercontroller.userObj.infrastructureIds) {
                UserDAO.validateHealthInfra({
                    toBeRemoved: removedLoc.locationId,
                    healthInfraIds: usercontroller.userObj.infrastructureIds.join(),
                    action: 'validateHealthInfra'
                }).then(function (res) {
                    if (res.errorcode == 200) {
                        usercontroller.selectedLocationsId.splice(index, 1);
                        let locations = _.pluck(usercontroller.selectedLocationsId, "locationId");
                        usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations);

                    } else {

                        var modalInstance = $uibModal.open({
                            templateUrl: 'app/common/views/alert.modal.html',
                            controller: 'alertModalController',
                            windowClass: 'cst-modal',
                            title: '',
                            size: 'med',
                            resolve: {
                                message: function () {
                                    return res.message;
                                }
                            }
                        });
                        modalInstance.result.then(function () {

                        }, function () {
                        });
                    }


                }, function (err) {

                })
            } else {
                usercontroller.selectedLocationsId.splice(index, 1);
                let locations = _.pluck(usercontroller.selectedLocationsId, "locationId");
                usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations);
            }
            // } else {
            //     usercontroller.selectedLocationsId.splice(index, 1);
            //     let locations = _.pluck(usercontroller.selectedLocationsId, "locationId");
            //     usercontroller.retrieveHealthInfrastructureByRole(usercontroller.userObj.roleId, locations);
            // }
        };

        usercontroller.retrieveLocationByRoleId = function () {
            delete usercontroller.errorMsg;
            delete usercontroller.errorCode;
            if (usercontroller.userObj.roleId) {
                Mask.show();
                return LocationService.retrieveLocationByRoleId(usercontroller.userObj.roleId).then(function (res) {
                    usercontroller.allowedLocations = _.pluck(res.hierarchy, "locationType");
                    usercontroller.maxAllowedLevel = _.max(res.hierarchy, function (hierarchy) {
                        return hierarchy.level;
                    }).level;
                    if (!usercontroller.maxAllowedLevel) {
                        usercontroller.maxAllowedLevel = 10;
                    }
                    usercontroller.isLastNameMandatory = res.roles.isLastNameMandatory;
                    usercontroller.isEmailMandatory = res.roles.isEmailMandatory;
                    usercontroller.isAadharNumMandatory = res.roles.isAadharNumMandatory;
                    usercontroller.isContactNumMandatory = res.roles.isContactNumMandatory;
                    usercontroller.isConvoxIdMandatory = res.roles.isConvoxIdMandatory;
                    usercontroller.isHealthInfraMandatory = res.roles.isHealthInfraMandatory;
                    usercontroller.isGeolocationMandatory = res.roles.isGeolocationMandatory;
                    usercontroller.locationNames = [];
                    usercontroller.maxHealthInfra = res.roles.maxHealthInfra;
                    _.each(usercontroller.allowedLocations, function (location) {
                        if (location === 'D') {
                            usercontroller.locationNames.push("District");
                        } else if (location === 'S') {
                            usercontroller.locationNames.push("State");
                        } else if (location === 'C') {
                            usercontroller.locationNames.push("Corporation");
                        } else if (location === 'B') {
                            usercontroller.locationNames.push("Block");
                        } else if (location === 'Z') {
                            usercontroller.locationNames.push("Zone");
                        } else if (location === 'U') {
                            usercontroller.locationNames.push("UPHC");
                        } else if (location === 'P') {
                            usercontroller.locationNames.push("PHC");
                        } else if (location === 'SC') {
                            usercontroller.locationNames.push("Sub center");
                        } else if (location === 'UA') {
                            usercontroller.locationNames.push("Urban area");
                        } else if (location === 'V') {
                            usercontroller.locationNames.push("Village");
                        } else if (location === 'A') {
                            usercontroller.locationNames.push("Area");
                        } else {
                            usercontroller.locationNames.push(location);
                        }
                    });
                    usercontroller.deletedLocations = usercontroller.deletedLocations.concat(usercontroller.selectedLocationsId);
                    usercontroller.selectedLocationsId = [];
                    usercontroller.selectedLocation = {};



                }).finally(function () {
                    Mask.hide();
                });
            } else {
                usercontroller.deletedLocations = usercontroller.deletedLocations.concat(usercontroller.selectedLocationsId);
                usercontroller.selectedLocationsId = [];
                usercontroller.selectedLocation = {};
            }
        };

        usercontroller.updatePassword = function (userId) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/users/views/update-password.modal.html',
                controller: 'ResetPasswordModalController as resetPassword',
                size: 'lg',
                backdrop: 'static',
                resolve: {
                    user: function () {
                        var user = { id: userId, editProfile: true, userName: usercontroller.userObj.userName, showCancel: true };
                        return user;
                    }
                }
            });
            modalInstance.result.then(function () {

            }, function () {
            });
        };
        var clearUser = function (userDto) {
            usercontroller.userObj = {
                roleId: userDto.roleId,
                password: 'Abcd@123',
                retypepass: 'Abcd@123'
            };
            usercontroller.userObj.gender = 'M';
            usercontroller.userObj.prefferedLanguage = USER.prefferedLanguage.english;
            usercontroller.userObj.title = 'Mr';
            usercontroller.selectedLocationsId = [];
            usercontroller.deletedLocations = [];
            usercontroller.userObj.infrastructureIds = [];
            usercontroller.userObj.defaultHealthInfrastructure = null;
            usercontroller.userObj.latitude = null;
            usercontroller.userObj.longitude = null;
            usercontroller.userForm.$setPristine();
        };
        usercontroller.retrieveHealthInfrastructureByRole = function (roleId, locations) {
            if (locations != "" && typeof (locations) != 'undefined') {

                let parameters = {
                    roleId: roleId,
                    locationIds: locations

                }
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_infra_by_role_location',
                    parameters: parameters
                }).then(function (res) {
                    usercontroller.infrastructures = res.result;
                    if (usercontroller.userObj && Array.isArray(usercontroller.userObj.infrastructureIds)) {
                        usercontroller.defaultHealthInfrastructuresList = usercontroller.infrastructures.filter(h => usercontroller.userObj.infrastructureIds.includes(h.infraid));
                    }
                }).finally(function () {
                    Mask.hide();
                });
            } else {
                usercontroller.infrastructures = [];
                usercontroller.userObj.infrastructureIds = [];
            }

        }

        usercontroller.showPopup = function () {
            usercontroller.count++;
            if (usercontroller.userObj.infrastructureIds.length >= usercontroller.maxHealthInfra && usercontroller.maxHealthInfra != null && usercontroller.count > 2) {
                toaster.pop('error', 'Max health infrastructure limit reached')
            }
            if (usercontroller.maxHealthInfra === 0) {
                toaster.pop('error', 'Max health infrastructure limit reached')
            }
        }

        usercontroller.infrastructureIdsChanged = () => {
            if (usercontroller.maxHealthInfra != null) {
                usercontroller.isOptionDisabled = function (infra) {
                    return !usercontroller.userObj.infrastructureIds.includes(infra.infraid)
                };
                if (Array.isArray(usercontroller.userObj.infrastructureIds) && usercontroller.userObj.infrastructureIds.length >= usercontroller.maxHealthInfra) {
                    usercontroller.count = 1;
                }
            }
        }

        initPage();

    }
    angular.module('imtecho.controllers').controller('ManageUserController', ManageUserController);
})();