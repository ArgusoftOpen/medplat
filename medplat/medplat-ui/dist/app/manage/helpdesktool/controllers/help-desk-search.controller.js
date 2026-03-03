(function () {
    function SearchFeatureController(Mask, GeneralUtil, QueryDAO, $stateParams, toaster, $timeout, AuthenticateService, $state, $uibModal, $filter
        ,WrongActionType) {
        var ctrl = this;
        ctrl.memberObj = {};
        ctrl.userObj = {};
        ctrl.familyObj = {};
        // for member information
        ctrl.immunisationGiven = [];
        ctrl.immunisationGivenList = [];
        ctrl.childServiceVisitDatesList = [];
        ctrl.ancVisitDatesList = [];
        ctrl.isDataSetFlag = false;
        ctrl.ancInfoObj = {};
        ctrl.searchBy = 'FAMILYID';
        ctrl.wrongActionType = WrongActionType;    
        if (ctrl.searchBy == 'FAMILYID') {
            ctrl.searchParam = 'FM/'
        } else if (ctrl.searchBy == 'MEMBERID') {
            ctrl.searchParam = 'A'
        }

        var init = function () {
            AuthenticateService.getLoggedInUser().then(function (user) {
                ctrl.loggedInUser = user.data;
            });
            AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
                ctrl.rights = res.featureJson;
                if (!ctrl.rights) {
                    ctrl.rights = {};
                }
            });
            if ($stateParams.uniqueHealthId) {
                ctrl.searchParam = $stateParams.uniqueHealthId;
                ctrl.searchBy = 'MEMBERID';
                ctrl.showBackButton = true;
                ctrl.getMemberData(false);
            } else if ($state.current.helpDeskSearchBy === 'FAMILYID') {
                ctrl.searchParam = $state.current.helpDeskFamilyIdParam;
                delete $state.current.helpDeskFamilyIdParam;
                ctrl.searchBy = 'FAMILYID';
                ctrl.showBackButton = false;
                ctrl.getFamilyData();
            } else if ($state.current.helpDeskSearchBy === 'NAME') {
                ctrl.firstName = $state.current.helpDeskFirstNameParam;
                ctrl.lastName = $state.current.helpDeskLastNameParam;
                ctrl.searchedLocation = $state.current.helpDeskLocationIdParam;
                delete $state.current.helpDeskFirstNameParam;
                delete $state.current.helpDeskLastNameParam;
                delete $state.current.helpDeskLocationIdParam;
                ctrl.searchBy = 'NAME';
                ctrl.showBackButton = false;
                ctrl.getNameData();
            }

            $timeout(function () {
                ctrl.toggleFilter();
            }, 10)
        };

        ctrl.resetField = function () {
            ctrl.memberObj = {};
            ctrl.userObj = {};
            ctrl.familyObj = {};
            ctrl.immunisationGivenList = [];
            ctrl.immunisationGiven = [];
            ctrl.childServiceVisitDatesList = [];
            ctrl.ancVisitDatesList = [];
            ctrl.ancInfoObj = {};
        }

        ctrl.isObjectEmpty = function (obj) {
            return Object.keys(obj).length === 0;
        }
        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.submit = function (toggleFilter) {
            if (ctrl.searchBy === 'FAMILYID') {
                if (!ctrl.searchParam) {
                    toaster.pop("error", "FamilyId can not be blank. Please enter valid familyId");
                    ctrl.isDataSetFlag = false;
                    ctrl.resetField();
                    return;
                } else {
                    ctrl.getFamilyData(toggleFilter);
                    ctrl.isDataSetFlag = true;
                }
            } else if (ctrl.searchBy === 'MEMBERID') {
                if (!ctrl.searchParam) {
                    toaster.pop("error", "Member Id can not be blank. Please enter valid memberId");
                    ctrl.isDataSetFlag = false;
                    ctrl.resetField();
                    return;
                } else {
                    ctrl.isDataSetFlag = true;
                    ctrl.getMemberData(toggleFilter);
                }
            } else if (ctrl.searchBy === 'USERNAME') {
                if (!ctrl.searchParam) {
                    toaster.pop("error", "UserName can not be blank. Please enter valid UserName");
                    ctrl.isDataSetFlag = false;
                    ctrl.resetField();
                    return;
                } else {
                    ctrl.getUserData(toggleFilter);
                    ctrl.isDataSetFlag = true;
                }
            } else if (ctrl.searchBy === 'NAME') {
                if (ctrl.searchForm.$valid) {
                    if (!ctrl.searchedLocation) {
                        toaster.pop('error', "Please Select Location")
                    } else {
                        ctrl.getNameData(toggleFilter);
                        ctrl.isDataSetFlag = true;
                    }
                }
            }
        };

        ctrl.getNameData = function (toggleFilter) {
            Mask.show();
            ctrl.resetField();

            var queryDto = {
                code: 'helpdesk_name_search',
                parameters: {
                    locationId: Number(ctrl.searchedLocation),
                    firstName: ctrl.firstName,
                    lastName: ctrl.lastName
                }
            };
            QueryDAO.execute(queryDto).then(function (res) {
                if (res.result.length < 1) {
                    toaster.pop("error", "Record Not Found");
                    ctrl.isDataSetFlag = false;
                    if (toggleFilter) {
                        ctrl.toggleFilter();
                    }
                } else {
                    ctrl.isDataSetFlag = true;
                    ctrl.familyObj = res.result;
                    if (!toggleFilter) {
                        ctrl.toggleFilter();
                    }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.getMemberData = function (toggleFilter) {
            Mask.show();
            ctrl.resetField();
            var dtoList = [];

            var queryDto1 = {
                code: 'retrieve_member_info_by_health_id',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 1
            };
            dtoList.push(queryDto1);

            var queryDto2 = {
                code: 'retrieve_rch_child_service_master_info',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 2
            };
            dtoList.push(queryDto2);

            var queryDto3 = {
                code: 'retrieve_rch_wpd_mother_master_info',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 3
            };
            dtoList.push(queryDto3);

            var queryDto4 = {
                code: 'retrieve_rch_wpd_child_master_info',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 4
            };
            dtoList.push(queryDto4);

            var queryDto5 = {
                code: 'retrieve_rch_pnc_mother_master_info',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 5
            };
            dtoList.push(queryDto5);

            var queryDto6 = {
                code: 'retrieve_rch_pnc_child_master_info',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 6
            };
            dtoList.push(queryDto6);

            var queryDto7 = {
                code: 'retrieve_rch_pregnancy_registration_det',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 7
            };
            dtoList.push(queryDto7);

            var queryDto8 = {
                code: 'retrieve_anc_information',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 8
            };

            dtoList.push(queryDto8);

            var queryDto9 = {
                code: 'retrieve_rch_immunisation_master_by_health_id',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 9
            };

            dtoList.push(queryDto9);

            let queryDto10 = {
                code: 'help_desk_nutrition_screening_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 10
            }

            dtoList.push(queryDto10);

            let queryDto11 = {
                code: 'help_desk_nutrition_fsam_screening_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 11
            };

            dtoList.push(queryDto11);

            let queryDto12 = {
                code: 'help_desk_nutrition_fsam_admission_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 12
            };

            dtoList.push(queryDto12);

            let queryDto13 = {
                code: 'help_desk_nutrition_fsam_weight_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 13
            };

            dtoList.push(queryDto13);

            let queryDto14 = {
                code: 'help_desk_nutrition_fsam_discharge_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 14
            };

            dtoList.push(queryDto14);

            let queryDto15 = {
                code: 'help_desk_nutrition_fsam_follow_up_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 15
            };

            dtoList.push(queryDto15);

            let queryDto16 = {
                code: 'help_desk_nutrition_cmam_admission_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 16
            };

            dtoList.push(queryDto16);

            let queryDto17 = {
                code: 'help_desk_nutrition_cmam_follow_up_details_retrieve',
                parameters: {
                    healthid: ctrl.searchParam
                },
                sequence: 17
            };

            dtoList.push(queryDto17);

            QueryDAO.executeAll(dtoList).then(function (res) {
                if (!res[0].result[0]) {
                    toaster.pop("error", "This memberId is not valid. Please enter valid memberId");
                    ctrl.isDataSetFlag = false;
                    if (toggleFilter) {
                        ctrl.toggleFilter();
                    }
                } else {
                    ctrl.isDataSetFlag = true;
                    ctrl.memberObj = res[0].result[0];
                    ctrl.memberObj.childServiceMasterDtos = res[1].result;
                    // Removed wpd having state MARK_AS_FALSE_DELIVERY
                    ctrl.memberObj.wpdMotherDtos = (res[2].result).filter(obj => obj.state != ctrl.wrongActionType.WRONG_DELIVERY.key);
                    ctrl.memberObj.wpdChildDtos = res[3].result;
                    ctrl.memberObj.pncMotherDtos = res[4].result;
                    ctrl.memberObj.pncChildDtos = res[5].result;
                    ctrl.memberObj.pregnancyRegistrationDetailDtos = res[6].result;
                    ctrl.memberObj.ancInfoObj = res[7].result;
                    ctrl.memberObj.immunisationGivenList = res[8].result
                    ctrl.memberObj.nutritionScreeningList = res[9].result;
                    ctrl.memberObj.nutritionFsamScreeningList = res[10].result
                    ctrl.memberObj.nutritionFsamAdmissionList = res[11].result
                    ctrl.memberObj.nutritionFsamWeightList = res[12].result
                    ctrl.memberObj.nutritionFsamDischargeList = res[13].result
                    ctrl.memberObj.nutritionFsamFollowUpList = res[14].result
                    ctrl.memberObj.nutritionCmamAdmissionList = res[15].result
                    ctrl.memberObj.nutritionCmamFollowUpList = res[16].result
                    if (ctrl.memberObj.childServiceVisitDatesList != null) {
                        ctrl.childServiceVisitDatesList = ctrl.memberObj.childServiceVisitDatesList.split(",");
                    }
                    if (ctrl.memberObj.ancVisitDatesList != null) {
                        ctrl.ancVisitDatesList = ctrl.memberObj.ancVisitDatesList.split(",");
                    }
                    if (!toggleFilter) {
                        ctrl.toggleFilter();
                    }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };
        ctrl.markAsEligibleCouple = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to mark this member as eligible couple?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                var queryDto = {
                    code: 'remove_last_method_of_contraception',
                    parameters: {
                        unique_health_id: ctrl.memberObj.uniqueHealthId,
                        reason_for_change: 'Mark as Eligible Couple Form Help Desk'
                    }
                };

                QueryDAO.execute(queryDto).then(function (res) {
                    toaster.pop("success", "Successfully marked As Eligible Couple ");
                    ctrl.getMemberData();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        };

        ctrl.getFamilyData = function (toggleFilter) {
            Mask.show();
            ctrl.resetField();

            var queryDto = {
                code: 'retrieve_family_and_member_info',
                parameters: {
                    familyid: ctrl.searchParam
                }
            };

            QueryDAO.execute(queryDto).then(function (res) {
                if (res.result.length < 1) {
                    toaster.pop("error", "This familyId is not valid. Please enter valid familyId");
                    ctrl.isDataSetFlag = false;
                    if (toggleFilter) {
                        ctrl.toggleFilter();
                    }
                } else {
                    ctrl.isDataSetFlag = true;
                    ctrl.familyObj = res.result;
                    if (!toggleFilter) {
                        ctrl.toggleFilter();
                    }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.getUserData = function (toggleFilter) {
            Mask.show();
            ctrl.resetField();
            var dtoList = [];

            var queryDto1 = {
                code: 'retrieve_all_information_of_user',
                parameters: {
                    username: ctrl.searchParam
                },
                sequence: 1
            };
            dtoList.push(queryDto1);

            var queryDto2 = {
                code: 'retrieve_user_login_details',
                parameters: {
                    username: ctrl.searchParam
                },
                sequence: 2
            };
            dtoList.push(queryDto2);

            QueryDAO.executeAll(dtoList).then(function (res) {

                if (res[0].result.length < 1) {
                    toaster.pop("error", "User not found. Please enter correct username");
                    ctrl.isDataSetFlag = false;
                    if (toggleFilter) {
                        ctrl.toggleFilter();
                    }
                } else {
                    ctrl.isDataSetFlag = true;
                    ctrl.userObj = res[0].result;
                    ctrl.userLoginDetail = res[1].result;
                    if (!toggleFilter) {
                        ctrl.toggleFilter();
                    }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.locationSelectizeSearch = {
            create: false,
            valueField: 'id',
            labelField: 'hierarchy',
            dropdownParent: 'body',
            highlight: true,
            searchField: ['_searchField'],
            maxItems: 1,
            render: {
                item: function (location, escape) {
                    var returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                },
                option: function (location, escape) {
                    var returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                }
            },
            onFocus: function () {
                this.onSearchChange("");
            },
            onBlur: function () {
                var selectize = this;
                var value = this.getValue();
                setTimeout(function () {
                    if (!value) {
                        selectize.clearOptions();
                        selectize.refreshOptions();
                    }
                }, 200);
            },
            load: function (query, callback) {
                var selectize = this;
                var value = this.getValue();
                if (!value) {
                    selectize.clearOptions();
                    selectize.refreshOptions();
                }
                var promise;
                var queryDto = {
                    code: 'location_search_for_web',
                    parameters: {
                        locationString: query,
                    }
                };
                promise = QueryDAO.execute(queryDto);
                promise.then(function (res) {
                    angular.forEach(res.result, function (result) {
                        result._searchField = query;
                    });
                    callback(res.result);
                }, function () {
                    callback();
                });
            }
        }

        ctrl.editMemberDob = function (memberObj) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: memberObj.memberId,
                            date: memberObj.dob,
                            title: 'Birth date',
                            maxDate: new Date(),
                            notes: 'for the child, System will delete all the vaccinations given to that child.'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_member_dob',
                                parameters: {
                                    dob: ctrl.convertToDateFormat(date),
                                    memberId: id
                                }
                            };
                            Mask.show();
                            let promise = QueryDAO.execute(queryDto);
                            promise.then(() => {
                                Mask.hide();
                                memberObj.dob = date;
                                toaster.pop("success", "Birth date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }

        ctrl.saveMemberDob = function () {
            //ctrl.updatedMemberDob
            //ctrl.memberObj.memberId
            //Dob will be changed here
        }

        ctrl.editMemberGender = function (member) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: member.memberId,
                            oldGender: member.gender,
                            isGenderChange: true,
                            title: 'Change Gender',
                            note1: 'Are you sure you want to change the Gender ?. It will delete all the pregnancy related service data for female to male gender change.',
                            note2: 'Are you sure you want to change the Gender ?'
                        }
                    },
                    save: function () {
                        return {};
                    }
                }
            });
            modalInstance.result.then(function (gender) {
                if (gender != null) {
                    member.gender = gender;
                }
            }, function () {
            });
        }

        ctrl.markAsWrongAction = function (dataObj, actionType) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-mark-as-wrong.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            isWrongMarkAction:true,
                            wrongMarkObj: dataObj,
                            actionType:actionType,
                            loggedInUser: ctrl.loggedInUser,
                        }
                    },
                    save: function () {
                        return {};
                    }
                }
            });
            modalInstance.result.then(function (gender) {
                init();
            }, function () {
            });
        }

        ctrl.editMemberPregRegDate = function (women) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: women.pregId,
                            date: women.registrationDate,
                            minDate: women.lmpDate,
                            maxDate: new Date(),
                            title: 'Pregnancy Registration Date'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_pregregdetails_reg_date',
                                parameters: {
                                    regDate: ctrl.convertToDateFormat(date),
                                    rchPregnancyRegistrationDetId: id,
                                    memberId: women.member_id
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                women.registrationDate = date;
                                toaster.pop("success", "Registration date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                toaster.pop("error", "Problem while updating data.");
                                Mask.hide();
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.editMemberLMPDate = function (women) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: women.pregId,
                            date: women.lmpDate,
                            title: 'LMP Date'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_pregregdetails_lmp_date',
                                parameters: {
                                    memberId: women.member_id,
                                    lmpDate: ctrl.convertToDateFormat(date),
                                    rchPregnancyRegistrationDetId: id
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                women.lmpDate = date;
                                toaster.pop("success", "LMP date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.editVaccinesGivenDate = function (child) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: child.id,
                            date: child.given_on,
                            title: 'Vaccines given date'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_immu_update_given_on',
                                parameters: {
                                    rchImmuId: id,
                                    givenOn: ctrl.convertToDateFormat(date),
                                    memberId: child.member_id
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                child.given_on = date;
                                toaster.pop("success", "Vaccines given date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }

        ctrl.editANCServiceDate = function (ancInfo) {
            var lmpDate = ctrl.getLMPDateByPrgRegId(ancInfo.pregnancy_reg_det_id);

            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: ancInfo.id,
                            date: ancInfo.service_date,
                            maxDate: new Date(),
                            minDate: lmpDate,
                            title: 'ANC Service Date'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_anc_service_date',
                                parameters: {
                                    ancMasterId: id,
                                    serviceDate: ctrl.convertToDateFormat(date),
                                    memberId: ancInfo.member_id,
                                    rchPregnancyRegistrationDetId: ancInfo.pregnancy_reg_det_id
                                }
                            };
                            Mask.show();
                            let promise = QueryDAO.execute(queryDto);
                            promise.then(() => {
                                Mask.hide();
                                ancInfo.service_date = date;
                                toaster.pop("success", "ANC service date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.getLMPDateByPrgRegId = function (prgId) {
            for (var index = 0; index < ctrl.memberObj.pregnancyRegistrationDetailDtos.length; index++) {
                if (ctrl.memberObj.pregnancyRegistrationDetailDtos[index].pregId == prgId) {
                    return ctrl.memberObj.pregnancyRegistrationDetailDtos[index].lmpDate;
                }
            }
        }
        ctrl.editWDPDeliveryDate = function (wpdMother) {
            var lmpDate = ctrl.getLMPDateByPrgRegId(wpdMother.pregnancyRegDetId);
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: wpdMother.wpdMotherId,
                            date: wpdMother.dateOfDelivery,
                            maxDate: new Date(),
                            minDate: lmpDate,
                            title: 'Delivery date',
                            notes: 'It will also change child\'s date of birth and delete all immunization given to that child.'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_wpd_date_of_delivery',
                                parameters: {
                                    wpdMotherId: id,
                                    dateOfDelivery: ctrl.convertToDateFormat(date),
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                wpdMother.dateOfDelivery = date;
                                toaster.pop("success", "Delivery date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.convertToDateFormat = function (date) {
            return $filter('date')(date, "dd-MM-yyyy HH:mm:ss")
        }
        ctrl.editPNCServiceDate = function (pncMother, memberId) {
            var lmpDate = ctrl.getLMPDateByPrgRegId(pncMother.pregnancyRegDetId);
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: pncMother.wpdMotherId,
                            date: pncMother.serviceDate,
                            minDate: lmpDate,
                            maxDate: new Date(),
                            title: 'Service date'
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_pnc_service_date',
                                parameters: {
                                    pncMasterId: pncMother.pncMasterId,
                                    serviceDate: ctrl.convertToDateFormat(date),
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                pncMother.serviceDate = date;
                                toaster.pop("success", "PNC service date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.editChildServiceDate = function (childService) {

            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: childService.csmId,
                            date: childService.serviceDate,
                            title: 'Service date',
                            maxDate: new Date(),
                            minDate: ctrl.memberObj.dob
                        }
                    },
                    save: function () {
                        return function (id, date, model) {
                            var queryDto = {
                                code: 'helpdesk_update_child_service_date',
                                parameters: {
                                    rchChildServiceMasterId: id,
                                    serviceDate: ctrl.convertToDateFormat(date),
                                    memberId: childService.memberId
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                childService.serviceDate = date;
                                toaster.pop("success", "Child service date updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        ctrl.editWDPDeliveryPlace = function (wpdMother) {

            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/helpdesktool/views/help-desk-edit.modal.html',
                controller: 'HelpDeskEdit as editwpdcontroller',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    data: function () {
                        return {
                            id: wpdMother.wpdMotherId,
                            title: 'Delivery Place',
                            isDeliveryUpdateLocation: true,
                            loggedInUser: ctrl.loggedInUser,
                            notes: 'It will also change child\'s place of birth.'
                        }
                    },
                    save: function () {
                        return function (id, deliveryPlace, healthInfrastructureId, typeOfHospital, infraType, typeOfHospitalValue, model) {
                            var queryDto = {
                                code: 'helpdesk_update_wpd_mother_delivery_place',
                                parameters: {
                                    id: id,
                                    deliveryPlace: deliveryPlace,
                                    healthInfrastructureId: Number(healthInfrastructureId),
                                    typeOfHospital: Number(typeOfHospital),
                                    userId: ctrl.loggedInUser.id
                                }
                            };
                            let promise = QueryDAO.execute(queryDto);
                            Mask.show();
                            promise.then(() => {
                                Mask.hide();
                                wpdMother.deliveryPlace = deliveryPlace;
                                wpdMother.typeOfHospital = typeOfHospital;
                                wpdMother.healthInfrastructureId = healthInfrastructureId;
                                wpdMother.typeofhospitalvalue = typeOfHospitalValue;
                                toaster.pop("success", "Delivery Place updated successfully");
                                model.dismiss('cancel');
                            }, function () {
                                Mask.hide();
                                toaster.pop("error", "Problem while updating data.");
                            })
                        }
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }

        ctrl.navigateToMemberInformation = (uniqueHealthId) => {
            $state.current.helpDeskSearchBy = ctrl.searchBy;
            $state.current.helpDeskFamilyIdParam = ctrl.searchParam;
            $state.current.helpDeskLocationIdParam = ctrl.searchedLocation;
            $state.current.helpDeskFirstNameParam = ctrl.firstName;
            $state.current.helpDeskLastNameParam = ctrl.lastName;
            // $state.go('techo.manage.searchfeature', { uniqueHealthId })
        }

        ctrl.goBack = () => window.history.back();

        init();
    }
    angular.module('imtecho.controllers').controller('SearchFeatureController', SearchFeatureController);
})();
