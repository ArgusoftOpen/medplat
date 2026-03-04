(function () {
    function ManageSchoolController($state, QueryDAO, Mask, toaster, GeneralUtil, AuthenticateService) {
        let ctrl = this;
        ctrl.schoolByCode = null;

        let _init = function () {
            ctrl.school = {
                name: '',
                code: '',
                englishName: '',
                grantType: null,
                schoolType: null,
                noOfTeachers: null,
                principalName: '',
                contactPersonName: '',
                isPrePrimarySchool: null,
                isPrimarySchool: null,
                isHigherSecondarySchool: null,
                isMadresa: null,
                isGurukul: null,
                isOther: null,
                otherSchoolType: null,
                contactNumber: null,
                childMale1To5: null,
                childFemale1To5: null,
                childMale6To8: null,
                childFemale6To8: null,
                childMale9To10: null,
                childFemale9To10: null,
                childMale11To12: null,
                childFemale11To12: null,
                rbskTeamId: '',
                locationId: null
            };
            ctrl.editMode = false;
            if ($state.params.id) {
                ctrl.editMode = true;
            }
            let dtoList = [];
            let schoolTypesDto = {
                code: 'retrieve_field_values_for_form_field',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'school_types'
                },
                sequence: 1
            };
            dtoList.push(schoolTypesDto);
            let schoolGrantTypesDto = {
                code: 'retrieve_field_values_for_form_field',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'school_grant_types'
                },
                sequence: 2
            };
            dtoList.push(schoolGrantTypesDto);
            let otherSchoolTypesDto = {
                code: 'retrieve_field_values_for_form_field',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'other_school_types'
                },
                sequence: 3
            };
            dtoList.push(otherSchoolTypesDto);
            if (ctrl.editMode) {
                let schoolByIdDto = {
                    code: 'school_retrieval_by_id',
                    parameters: {
                        id: Number($state.params.id)
                    },
                    sequence: 4
                }
                dtoList.push(schoolByIdDto);
            }
            Mask.show();
            QueryDAO.executeAll(dtoList).then(function (res) {
                ctrl.schoolTypes = res[0].result;
                ctrl.schoolGrantTypes = res[1].result;
                ctrl.schoolTypesMap = {};
                ctrl.schoolTypes.forEach(e => ctrl.schoolTypesMap[e.id] = e.value);
                ctrl.otherSchoolTypes = res[2].result;
                if (ctrl.editMode) {
                    ctrl.school = res[3].result[0];
                }
                ctrl.schoolTypeChanged(true);
                AuthenticateService.getLoggedInUser().then(function (user) {
                    ctrl.loggedInUser = user.data;
                }, function () { }).finally(function () {
                    Mask.hide();
                })
            }, function () {
                GeneralUtil.showMessageOnApiCallFailure();
                Mask.hide();
            })
        };

        let _prepareStudentCount = function () {
            switch (ctrl.schoolTypesMap[ctrl.school.schoolType]) {
                case 'Primary School':
                    ctrl.school.childMale9To10 = null;
                    ctrl.school.childFemale9To10 = null;
                    ctrl.school.childMale11To12 = null;
                    ctrl.school.childFemale11To12 = null;
                    break;
                case 'High School':
                    ctrl.school.childMale1To5 = null;
                    ctrl.school.childFemale1To5 = null;
                    ctrl.school.childMale6To8 = null;
                    ctrl.school.childFemale6To8 = null;
                    break;
                case 'Others':
                    break;
                default:
                    break;
            }
        }

        let _preparePayload = function () {
            ctrl.school.childMale1To5 = ctrl.school.childMale1To5 != '' ? ctrl.school.childMale1To5 : null;
            ctrl.school.childFemale1To5 = ctrl.school.childFemale1To5 != '' ? ctrl.school.childFemale1To5 : null;
            ctrl.school.childMale6To8 = ctrl.school.childMale6To8 != '' ? ctrl.school.childMale6To8 : null;
            ctrl.school.childFemale6To8 = ctrl.school.childFemale6To8 != '' ? ctrl.school.childFemale6To8 : null;
            ctrl.school.childMale9To10 = ctrl.school.childMale9To10 != '' ? ctrl.school.childMale9To10 : null;
            ctrl.school.childFemale9To10 = ctrl.school.childFemale9To10 != '' ? ctrl.school.childFemale9To10 : null;
            ctrl.school.childMale11To12 = ctrl.school.childMale11To12 != '' ? ctrl.school.childMale11To12 : null;
            ctrl.school.childFemale11To12 = ctrl.school.childFemale11To12 != '' ? ctrl.school.childFemale11To12 : null;
            ctrl.school.isPrePrimarySchool = ctrl.school.isPrePrimarySchool || false;
            ctrl.school.isPrimarySchool = ctrl.school.isPrimarySchool || false;
            ctrl.school.isHigherSecondarySchool = ctrl.school.isHigherSecondarySchool || false;
            ctrl.school.isMadresa = ctrl.school.isMadresa || false;
            ctrl.school.isGurukul = ctrl.school.isGurukul || false;
            ctrl.school.isOther = ctrl.school.isOther || false;
            ctrl.school.otherSchoolType = ctrl.school.isOther ? ctrl.school.otherSchoolType : null;
        }

        ctrl.schoolTypeChanged = function (onInit) {
            if (ctrl.school.isPrePrimarySchool || ctrl.school.isPrimarySchool || ctrl.school.isHigherSecondarySchool) {
                ctrl.disablePrePrimarySchool = false;
                ctrl.disablePrimarySchool = false;
                ctrl.disableHigherSecondarySchool = false;
                ctrl.disableMadresa = true;
                ctrl.disableGurukul = true;
                ctrl.disableOther = true;
            } else if (ctrl.school.isMadresa) {
                ctrl.disablePrePrimarySchool = true;
                ctrl.disablePrimarySchool = true;
                ctrl.disableHigherSecondarySchool = true;
                ctrl.disableMadresa = false;
                ctrl.disableGurukul = true;
                ctrl.disableOther = true;
            } else if (ctrl.school.isGurukul) {
                ctrl.disablePrePrimarySchool = true;
                ctrl.disablePrimarySchool = true;
                ctrl.disableHigherSecondarySchool = true;
                ctrl.disableMadresa = true;
                ctrl.disableGurukul = false;
                ctrl.disableOther = true;
            } else if (ctrl.school.isOther) {
                ctrl.disablePrePrimarySchool = true;
                ctrl.disablePrimarySchool = true;
                ctrl.disableHigherSecondarySchool = true;
                ctrl.disableMadresa = true;
                ctrl.disableGurukul = true;
                ctrl.disableOther = false;
            } else {
                ctrl.disablePrePrimarySchool = false;
                ctrl.disablePrimarySchool = false;
                ctrl.disableHigherSecondarySchool = false;
                ctrl.disableMadresa = false;
                ctrl.disableGurukul = false;
                ctrl.disableOther = false;
            }
            if (!onInit && !_validateSchoolTypes()) {
                ctrl.schoolTypeInvalid = true;
            } else {
                ctrl.schoolTypeInvalid = false;
            }
        }

        let _validateSchoolTypes = function () {
            return ctrl.school.isPrePrimarySchool || ctrl.school.isPrimarySchool || ctrl.school.isHigherSecondarySchool || ctrl.school.isMadresa || ctrl.school.isGurukul || ctrl.school.isOther;
        }

        let _escapeSingleQuote = function () {
            ctrl.school.name = ctrl.school.name.replace(/'/g, "''");
            ctrl.school.code = ctrl.school.code.replace(/'/g, "''");
            ctrl.school.englishName = ctrl.school.englishName.replace(/'/g, "''");
            ctrl.school.principalName = ctrl.school.principalName.replace(/'/g, "''");
            ctrl.school.contactPersonName = ctrl.school.contactPersonName.replace(/'/g, "''");
            ctrl.school.rbskTeamId = ctrl.school.rbskTeamId.replace(/'/g, "''");
        }

        ctrl.save = function () {
            if (ctrl.manageSchoolForm.$valid && _validateSchoolTypes()) {
                ctrl.schoolTypeInvalid = false;
                if (ctrl.editMode) {
                    ctrl.school.modifiedBy = ctrl.loggedInUser.id;
                    ctrl.school.id = Number($state.params.id);
                    if (ctrl.updateLocation) {
                        ctrl.school.locationId = ctrl.selectedLocation;
                    }
                    _prepareStudentCount();
                    _preparePayload();
                    _escapeSingleQuote();
                    Mask.show();
                    QueryDAO.execute({
                        code: 'school_retrieval_by_code',
                        parameters: {
                            id: ctrl.school.id,
                            code: ctrl.school.code
                        }
                    }).then(function (res) {
                        if (res.result && res.result.length > 0) {
                            toaster.pop('error', 'School with same code is already added. Please add another code.');
                            ctrl.schoolByCode = res.result[0];
                            Mask.hide();
                        } else {
                            ctrl.schoolByCode = null;
                            delete ctrl.school.locationHierarchy
                            QueryDAO.execute({
                                code: 'school_update',
                                parameters: ctrl.school
                            }).then(function () {
                                toaster.pop('success', 'School Updated Successfully!')
                                $state.go('techo.manage.schools');
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                Mask.hide();
                            });
                        }
                    }, function () {
                        GeneralUtil.showMessageOnApiCallFailure();
                        Mask.hide();
                    });
                } else {
                    ctrl.school.createdBy = ctrl.loggedInUser.id;
                    ctrl.school.modifiedBy = ctrl.loggedInUser.id;
                    ctrl.school.locationId = ctrl.selectedLocation;
                    _prepareStudentCount();
                    _preparePayload();
                    _escapeSingleQuote();
                    Mask.show();
                    QueryDAO.execute({
                        code: 'school_retrieval_by_code',
                        parameters: {
                            id: 0,
                            code: ctrl.school.code
                        }
                    }).then(function (res) {
                        if (res.result && res.result.length > 0) {
                            toaster.pop('error', 'School with same code is already added. Please add another code.');
                            ctrl.schoolByCode = res.result[0];
                            Mask.hide();
                        } else {
                            ctrl.schoolByCode = null;
                            QueryDAO.execute({
                                code: 'school_create',
                                parameters: ctrl.school
                            }).then(function () {
                                toaster.pop('success', 'School Created Successfully!')
                                $state.go('techo.manage.schools');
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                Mask.hide();
                            });
                        }
                    }, function () {
                        GeneralUtil.showMessageOnApiCallFailure();
                        Mask.hide();
                    });
                }
            } else {
                if (!_validateSchoolTypes()) {
                    ctrl.schoolTypeInvalid = true;
                } else {
                    ctrl.schoolTypeInvalid = false;
                }
            }
        };

        _init();
    }
    angular.module('imtecho.controllers').controller('ManageSchoolController', ManageSchoolController);
})();
