/* global moment, toster */

(function () {
    function ManagePncController(ManagePncDAO, QueryDAO, Mask, $stateParams, toaster, $state, GeneralUtil, AuthenticateService,AnganwadiService, SelectizeGenerator) {
        var managepnccontroller = this;
        managepnccontroller.MS_PER_DAY = 1000 * 60 * 60 * 24;
        managepnccontroller.todayDate = moment();
        managepnccontroller.childIndex = 0;

        managepnccontroller.isMotherDangerSignOther = () => {
            managepnccontroller.showOtherMotherDangerSign = managepnccontroller.managePncObject.motherDetails.motherDangerSigns.some(motherDangerSign => motherDangerSign == -1);
            managepnccontroller.managePncObject.motherDetails.otherDangerSign = null
        }

        managepnccontroller.isChildDangerSignOther = (childDetail) => {
            childDetail.showOtherChildDangerSign = childDetail.childDangerSigns.some(dangerSign => dangerSign == -1);
            childDetail.otherDangerSign = null
        }

        managepnccontroller.savePnc = () => {
            if (managepnccontroller.managepncForm.$valid) {
                if (managepnccontroller.managePncObject.motherDetails.motherDangerSigns != null && managepnccontroller.managePncObject.motherDetails.motherDangerSigns[0] === 'NONE') {
                    delete managepnccontroller.managePncObject.motherDetails.motherDangerSigns;
                }
                managepnccontroller.managePncObject.mobileEndDate = new Date();
                if (managepnccontroller.managePncObject.deliveryPlace === 'HOSP') {
                    managepnccontroller.managePncObject.typeOfHospital = managepnccontroller.managePncObject.institutionType;
                    managepnccontroller.managePncObject.healthInfrastructureId = managepnccontroller.managePncObject.institute;
                } else if (managepnccontroller.managePncObject.deliveryPlace === 'THISHOSP') {
                    managepnccontroller.managePncObject.typeOfHospital = managepnccontroller.managePncObject.institute.type;
                    managepnccontroller.managePncObject.healthInfrastructureId = managepnccontroller.managePncObject.institute.id;
                } else {
                    delete managepnccontroller.managePncObject.typeOfHospital;
                    delete managepnccontroller.managePncObject.healthInfrastructureId;
                }
                managepnccontroller.managePncObject.serviceDate = managepnccontroller.managePncObject.motherDetails.serviceDate;
                managepnccontroller.managePncObject.motherDetails.memberStatus = "AVAILABLE";
                if (managepnccontroller.motherReferralInfraDetails) {
                    managepnccontroller.managePncObject.motherDetails.motherReferralDone = 'YES';
                    managepnccontroller.managePncObject.motherDetails.referralInfraId = managepnccontroller.motherReferralInfraDetails ? Number(managepnccontroller.motherReferralInfraDetails.institute) : null;
                } else {
                    managepnccontroller.managePncObject.motherDetails.motherReferralDone = 'NO';
                    managepnccontroller.managePncObject.motherDetails.referralInfraId = null;
                }
                managepnccontroller.managePncObject.childDetails.forEach((child) => {
                    if (child.childReferralInfraDetails) {
                        child.childReferralDone = 'YES';
                        child.referralInfraId = child.childReferralInfraDetails.institute ? Number(child.childReferralInfraDetails.institute) : null;
                        delete child.childReferralInfraDetails;
                    } else {
                        child.childReferralDone = 'NO';
                        child.referralInfraId = null;
                        delete child.childReferralInfraDetails;
                    }
                    if (child.childDangerSigns != null && child.childDangerSigns[0] === 'NONE') {
                        delete child.childDangerSigns;
                    }
                    if (child.dueImmunisations != null) {
                        child.dueImmunisations.forEach((immunisation) => {
                            child.immunisationDtos = [];
                            if (child.immunisation != null && child.immunisation[immunisation].given) {
                                child.immunisationDtos.push({
                                    immunisationGiven: immunisation,
                                    immunisationDate: moment(child.immunisation[immunisation].date).toDate()
                                });
                            }
                        });
                    }
                });
                Mask.show();
                ManagePncDAO.createOrUpdate(managepnccontroller.managePncObject).then((response) => {
                    toaster.pop('success', 'PNC visit added');
                    $state.go('techo.manage.pncSearch');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        managepnccontroller.getDueImmunisation = () => {
            managepnccontroller.childIndex++;
            if (managepnccontroller.childIndex <= managepnccontroller.managePncObject.childDetails.length) {
                Mask.show();
                ManagePncDAO.getDueImmunisationsForChild(moment(managepnccontroller.managePncObject.childDetails[managepnccontroller.childIndex - 1].dob).valueOf(), managepnccontroller.managePncObject.childDetails[managepnccontroller.childIndex - 1].immunisationGiven).then((response) => {
                    managepnccontroller.managePncObject.childDetails[managepnccontroller.childIndex - 1].dueImmunisations = response;
                    managepnccontroller.getDueImmunisation();
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        managepnccontroller.ironDefAnemiaInjChanged = () => managepnccontroller.managePncObject.motherDetails.ironDefAnemiaInjDueDate = null;

        managepnccontroller.serviceDateChanged = () => {
            if (managepnccontroller.managePncObject.motherDetails.deathDate && !(managepnccontroller.managePncObject.motherDetails.deathDate <= managepnccontroller.managePncObject.motherDetails.serviceDate)) {
                managepnccontroller.managePncObject.motherDetails.deathDate = null;
            }

            // after service date , Death date should be reset

            _.forEach(managepnccontroller.managePncObject.childDetails, (d) => {
                if (d.deathDate && !(d.deathDate <= managepnccontroller.managePncObject.motherDetails.serviceDate)) {
                    d.deathDate = null;
                }
            });


            managepnccontroller.managePncObject.motherDetails.ironDefAnemiaInjDueDate = null
            managepnccontroller.minInjDate = moment(managepnccontroller.managePncObject.motherDetails.serviceDate).add(1, 'days');
            managepnccontroller.maxInjDate = moment(managepnccontroller.managePncObject.motherDetails.serviceDate).add(30, 'days');
        }

        managepnccontroller.init = () => {
            managepnccontroller.managePncObject = {};
            managepnccontroller.managePncObject.motherDetails = {};
            managepnccontroller.managePncObject.mobileStartDate = new Date();
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                managepnccontroller.loggedInUser = user.data;
                return QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: managepnccontroller.loggedInUser.id
                    }
                });
            }).then((response) => {
                managepnccontroller.institutes = response.result
                return AnganwadiService.searchMembers($stateParams.id);
            })
            .then((response) => {
                managepnccontroller.managePncObject.uniqueHealthId = response.uniqueHealthId;
                managepnccontroller.managePncObject.motherMobileNumber = response.mobileNumber;
                managepnccontroller.managePncObject.memberId = response.id;
                managepnccontroller.managePncObject.motherDetails.motherId = response.id;
                managepnccontroller.managePncObject.motherDetails.familyPlanningMethod = response.lastMethodOfContraception;
                managepnccontroller.managePncObject.familyId = response.fid;
                managepnccontroller.managePncObject.familyUniqueId = response.familyId;
                managepnccontroller.managePncObject.locationId = response.areaId == null ? response.locationId : response.areaId;
                managepnccontroller.managePncObject.areaId = response.areaId !== null ? response.areaId : null;
                managepnccontroller.managePncObject.memberStatus = "AVAILABLE";
                managepnccontroller.managePncObject.isFromWeb = true;
                managepnccontroller.managePncObject.memberName = response.firstName + " " + response.middleName + " " + response.lastName;
                managepnccontroller.managePncObject.locationHierarchy = response.locationHierarchy;
                managepnccontroller.managePncObject.bplFlag = response.bplFlag ? 'Yes' : 'No';
                managepnccontroller.managePncObject.caste = response.caste;
                managepnccontroller.managePncObject.benefits = [];
                managepnccontroller.managePncObject.additionalInfo = JSON.parse(response.additionalInfo);
                managepnccontroller.managePncObject.motherDetails.dateOfDelivery = response.lastDeliveryDate;
                managepnccontroller.managePncObject.motherDetails.dateOfDeliveryDisplay = moment(managepnccontroller.managePncObject.motherDetails.dateOfDelivery).format("DD-MM-YYYY");
                if (managepnccontroller.managePncObject.additionalInfo != null && managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate != null) {
                    managepnccontroller.minDeathDate = moment(managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate);
                    if (moment(managepnccontroller.managePncObject.motherDetails.dateOfDelivery).isSame(moment(managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate).format("YYYY-MM-DD"))) {
                        managepnccontroller.minServiceDate = moment(managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate);
                    } else {
                        managepnccontroller.minServiceDate = moment(managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate).add(1, 'days');
                        if (moment(managepnccontroller.managePncObject.additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                            return Promise.reject({ data: { message: 'PNC Visit for this member has already been done today.' } });
                        }
                    }
                } else {
                    managepnccontroller.minServiceDate = managepnccontroller.managePncObject.motherDetails.dateOfDelivery
                    managepnccontroller.minDeathDate = managepnccontroller.minServiceDate;
                }
                if (managepnccontroller.minServiceDate != null && managepnccontroller.minServiceDate < (moment().subtract(15, 'days'))) {
                    managepnccontroller.minServiceDate = moment().subtract(15, 'days')
                }
                if (response.isChiranjeeviYojnaBeneficiary) {
                    managepnccontroller.managePncObject.benefits.push('Chiranjeevi Yojna');
                }
                if (response.isIayBeneficiary) {
                    managepnccontroller.managePncObject.benefits.push('IAY');
                }
                if (response.isJsyBeneficiary) {
                    managepnccontroller.managePncObject.benefits.push("JSY");
                }
                if (response.isKpsyBeneficiary) {
                    managepnccontroller.managePncObject.benefits.push("KPSY");
                }
                if (managepnccontroller.managePncObject.benefits.length === 0) {
                    managepnccontroller.managePncObject.benefits = "None"
                } else {
                    managepnccontroller.managePncObject.benefits = managepnccontroller.managePncObject.benefits.join();
                }
                if (managepnccontroller.managePncObject.motherDetails.dateOfDelivery != null) {
                    var dateDiff = managepnccontroller.dateDiffInDays(new Date(managepnccontroller.managePncObject.motherDetails.dateOfDelivery), new Date());
                    if (dateDiff <= 6) {
                        managepnccontroller.managePncObject.pncNo = "1"
                    } else if (dateDiff >= 7 && dateDiff <= 13) {
                        managepnccontroller.managePncObject.pncNo = "2"
                    } else if (dateDiff >= 14 && dateDiff <= 20) {
                        managepnccontroller.managePncObject.pncNo = "3"
                    } else if (dateDiff >= 21 && dateDiff <= 27) {
                        managepnccontroller.managePncObject.pncNo = "4"
                    } else if (dateDiff >= 28 && dateDiff <= 41) {
                        managepnccontroller.managePncObject.pncNo = "5"
                    } else if (dateDiff >= 42 && dateDiff <= 60) {
                        managepnccontroller.managePncObject.pncNo = "6"
                    }
                }
                let dtoList = [];
                if (response.locationId != null) {
                    var fhwDto = {
                        code: 'retrieve_worker_info_by_location_id',
                        parameters: {
                            locationId: response.locationId
                        },
                        sequence: 1
                    };
                    dtoList.push(fhwDto);
                }
                if (response.areaId != null) {
                    var ashaDto = {
                        code: 'retrieve_worker_info_by_location_id',
                        parameters: {
                            locationId: Number(response.areaId)
                        },
                        sequence: 2
                    };
                    dtoList.push(ashaDto);
                }
                return QueryDAO.executeAll(dtoList);
            }).then((response) => {
                if (response[0] != null && response[0].result[0].workerDetails != null) {
                    var fhwJson = JSON.parse(response[0].result[0].workerDetails);
                    managepnccontroller.managePncObject.fhwName = fhwJson[0].name
                    managepnccontroller.managePncObject.fhwNumber = fhwJson[0].mobileNumber;
                }
                if (response[1] != null && response[1].result[0].workerDetails != null) {
                    var ashaJson = JSON.parse(response[1].result[0].workerDetails);
                    managepnccontroller.managePncObject.ashaName = ashaJson[0].name
                    managepnccontroller.managePncObject.ashaNumber = ashaJson[0].mobileNumber;
                }
                return QueryDAO.execute({
                    code: 'pnc_retrieve_childs_by_member_id',
                    parameters: {
                        memberId: Number($stateParams.id)
                    }
                });
            }).then((response) => {
                managepnccontroller.managePncObject.childDetails = response.result;
                managepnccontroller.managePncObject.childDetails.forEach((child) => {
                    child.immunisationGiven = child.immunisation_given != null ? child.immunisation_given : "";
                    child.childId = child.id
                    child.memberStatus = 'AVAILABLE';
                });
                managepnccontroller.getDueImmunisation();
                let retrieveDataDtoList = [];
                retrieveDataDtoList.push({
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'deathReasonsFhwAnc'
                    },
                    sequence: 1
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'dangerSignsMotherFhwPnc'
                    },
                    sequence: 2
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'referralPlaceFhwAnc'
                    },
                    sequence: 3
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'deathReasonsChildFhwPnc'
                    },
                    sequence: 4
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'dangerSignsChildFhwPnc'
                    },
                    sequence: 5
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'Health Infrastructure Type'
                    },
                    sequence: 5
                });
                return QueryDAO.executeAll(retrieveDataDtoList);
            }).then((response) => {
                managepnccontroller.motherDeathReasons = response[0].result;
                managepnccontroller.motherDangerSigns = response[1].result;
                managepnccontroller.referralPlaces = response[2].result;
                managepnccontroller.childDeathReasons = response[3].result;
                managepnccontroller.childDangerSigns = response[4].result;
                managepnccontroller.institutionTypes = response[5].result;
            })
            .catch((error) => {
                // GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.pncSearch');
            }).finally(() => {
                Mask.hide();
            });
        }

        // managepnccontroller.deliveryPlaceChanged = () => {
        //     managepnccontroller.managePncObject.institute = null;
        //     if (managepnccontroller.managePncObject.deliveryPlace === 'THISHOSP') {
        //         if (managepnccontroller.institutes.length === 1) {
        //             managepnccontroller.managePncObject.institute = managepnccontroller.institutes[0];
        //         } else if (managepnccontroller.institutes.length === 0) {
        //             toaster.pop('error', 'No health infrastructure assigned');
        //         }
        //     }
        // }

        managepnccontroller.retrieveHospitalsByInstitutionType = () => {
            Mask.show();
            managepnccontroller.showOtherInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managepnccontroller.managePncObject.institutionType)
                }
            }).then((res) => {
                managepnccontroller.showOtherInstitutes = true;
                managepnccontroller.otherInstitutes = res.result;
                var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(managepnccontroller.otherInstitutes);
                managepnccontroller.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        managepnccontroller.retrieveDeathHospitalsByInstitutionType = () => {
            Mask.show();
            managepnccontroller.showDeathInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managepnccontroller.managePncObject.motherDetails.deathInfrastructureType)
                }
            }).then((res) => {
                managepnccontroller.showDeathInstitutes = true;
                managepnccontroller.deathInstitutes = res.result;
                managepnccontroller.managePncObject.motherDetails.deathInfrastructureId = null;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        managepnccontroller.setUsersForInstitute = () => {
            managepnccontroller.usersForInstitute = [];
            delete managepnccontroller.managePncObject.deliveryPerson;
            if (managepnccontroller.managePncObject.deliveryPlace === 'HOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managepnccontroller.managePncObject.institute),
                        code: managepnccontroller.managePncObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managepnccontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            } else if (managepnccontroller.managePncObject.institute && managepnccontroller.managePncObject.deliveryPlace === 'THISHOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managepnccontroller.managePncObject.institute.id),
                        code: managepnccontroller.managePncObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managepnccontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        managepnccontroller.immunisationGivenChanged = (childDetail, immunisation, dob, childIndex) => {
            childDetail.immunisation[immunisation].date = null;
            Object.keys(childDetail.immunisation).forEach((key) => {
                if (!childDetail.immunisation[key].given) {
                    if (childDetail.immunisationGiven.includes(key)) {
                        let givenImmunisationsMap = childDetail.immunisationGiven.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        childDetail.immunisationGiven = givenImmunisationsMap.join(',');
                    }
                } else if (childDetail.immunisation[key].given && childDetail.immunisation[key].date != null) {
                    var givenImmunisation = childDetail.immunisationGiven;
                    if (givenImmunisation.includes(key)) {
                        let givenImmunisationsMap = givenImmunisation.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        givenImmunisation = givenImmunisationsMap.join(',');
                    }
                    Mask.show();
                    ManagePncDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(childDetail.immunisation[key].date).valueOf(), key, givenImmunisation).then((response) => {
                        if (response.result != "" && response.result != null) {
                            managepnccontroller.managepncForm[childIndex + key + 'date'].$setValidity('vaccine', false);
                        } else {
                            managepnccontroller.managepncForm[childIndex + key + 'date'].$setValidity('vaccine', true);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                }
            });
        }

        managepnccontroller.setCurrentlyGivenVaccines = (vaccine, date, childDetail) => {
            if (childDetail.immunisationGiven.includes(vaccine)) {
                var givenImmunisationsMap = childDetail.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((immunisation, index) => {
                    if (immunisation.includes(vaccine)) {
                        givenImmunisationsMap.splice(index, 1);
                    }
                });
                childDetail.immunisationGiven = givenImmunisationsMap.join(',');
            }

            if (childDetail.immunisationGiven == null || childDetail.immunisationGiven == "") {
                childDetail.immunisationGiven = vaccine + "#" + moment(date).format("DD/MM/YYYY");
            } else {
                childDetail.immunisationGiven += "," + vaccine + "#" + moment(date).format("DD/MM/YYYY");
            }
        }

        managepnccontroller.checkImmunisationValidation = (dob, givenDate, currentVaccine, givenImmunisations, childDetail, index) => {
            if (givenImmunisations == null || givenImmunisations == "") {
                givenImmunisations = "";
            }
            if (givenImmunisations.includes(currentVaccine)) {
                let givenImmunisationsMap = givenImmunisations.split(',');
                givenImmunisationsMap.forEach((immunisation, i) => {
                    if (immunisation.includes(currentVaccine)) {
                        givenImmunisationsMap.splice(i, 1);
                    }
                });
                givenImmunisations = givenImmunisationsMap.join(',');
            }
            Mask.show();
            ManagePncDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(givenDate).valueOf(), currentVaccine, givenImmunisations).then((response) => {
                if (response.result != "" && response.result != null) {
                    managepnccontroller.managepncForm[index + currentVaccine + 'date'].$setValidity('vaccine', false);
                } else {
                    managepnccontroller.managepncForm[index + currentVaccine + 'date'].$setValidity('vaccine', true);
                    managepnccontroller.setCurrentlyGivenVaccines(currentVaccine, givenDate, childDetail);
                }
                Object.keys(childDetail.immunisation).forEach((key) => {
                    let givenImmunisations2 = childDetail.immunisationGiven
                    if (childDetail.immunisation[key].given && childDetail.immunisation[key].date != null) {
                        if (givenImmunisations2.includes(key)) {
                            let givenImmunisationsMap = givenImmunisations2.split(',');
                            givenImmunisationsMap.forEach((immunisation, i) => {
                                if (immunisation.includes(key)) {
                                    givenImmunisationsMap.splice(i, 1);
                                }
                            });
                            givenImmunisations2 = givenImmunisationsMap.join(',');
                        }
                        Mask.show();
                        ManagePncDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(childDetail.immunisation[key].date).valueOf(), key, givenImmunisations2).then((res) => {
                            if (res.result != "" && res.result != null) {
                                managepnccontroller.managepncForm[index + key + 'date'].$setValidity('vaccine', false);
                            } else {
                                managepnccontroller.managepncForm[index + key + 'date'].$setValidity('vaccine', true);
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        });
                    }
                })
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        managepnccontroller.dateDiffInDays = (a, b) => {
            var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
            var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

            return Math.floor((utc2 - utc1) / managepnccontroller.MS_PER_DAY);
        }

        managepnccontroller.goBack = () => {
            window.history.back();
        }

        managepnccontroller.init();
    }
    angular.module('imtecho.controllers').controller('ManagePncController', ManagePncController);
})();
