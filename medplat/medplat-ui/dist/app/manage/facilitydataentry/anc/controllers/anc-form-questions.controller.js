/* global moment */
(function (angular) {
    function AncFormQuestionsController(BLOOD_GROUP, toaster, QueryDAO, Mask, AncService, $state, GeneralUtil, SelectizeGenerator, EXPECTED_DELIVERY_PLACES, PREGNANCY_COMPLICATIONS, HBsAg_TEST, BLOOD_SUGAR_TEST, ALBUMIN_SUGAR, VDRL_TEST, HIV_TEST, PREGNANCY_OUTCOME, FOETAL_POSITIONS, AuthenticateService, NdhmHipDAO, NdhmHipUtilService, $filter, $q) {
        var ancque = this;
        ancque.showCalcium = true;
        ancque.showIfa = false;
        ancque.mobileStartDate = new Date();
        var init = function () {
            Mask.show();
            let promiseList = [];
            promiseList.push(AuthenticateService.getLoggedInUser());
            promiseList.push(AuthenticateService.getAssignedFeature("techo.manage.ancSearch"));
            $q.all(promiseList).then((response) => {
                ancque.loggedInUser = response[0].data;
                ancque.rights = response[1].featureJson;
                if (!ancque.rights) {
                    ancque.rights = {};
                }
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: ancque.loggedInUser.id
                    }
                }).then(function (res) {
                    Mask.hide();
                    ancque.institutes = res.result
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally( () => {
                Mask.hide();
            });
            ancque.today = new Date().setHours(0, 0, 0, 0);
            ancque.ancQueData = {};
            ancque.bloodGroups = BLOOD_GROUP;
            ancque.pregnencyComplications = PREGNANCY_COMPLICATIONS;
            ancque.bloodSugarTestOptions = BLOOD_SUGAR_TEST;
            ancque.hbsAgtestOptions = HBsAg_TEST;
            ancque.albuminSugarTestValues = ALBUMIN_SUGAR;
            ancque.vdrlTestValues = VDRL_TEST;
            ancque.hivTestValues = HIV_TEST;
            ancque.expectedDeliveryPlaces = EXPECTED_DELIVERY_PLACES;
            ancque.pregnancyOutcomes = PREGNANCY_OUTCOME;
            ancque.foetalPositions = FOETAL_POSITIONS;
            if ($state.params.id && $state.params.id != '') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_anc_member_details',
                    parameters: {
                        id: Number($state.params.id)
                    }
                }).then(function (response) {
                    if (response != null && response.result != null && response.result.length > 0) {
                        ancque.memberDetails = response.result[0];
                        ancque.memberDetails.benefits = "";
                        if (ancque.memberDetails.lmpDate &&
                            moment().diff(ancque.memberDetails.lmpDate, 'week') > 24 &&
                            moment().diff(ancque.memberDetails.lmpDate, 'week') < 34) {
                            ancque.isApplicableForInjCorticosteroid = true;
                        } else {
                            ancque.isApplicableForInjCorticosteroid = false;
                        }
                        if (ancque.memberDetails.isChiranjeeviYojnaBeneficiary) {
                            ancque.memberDetails.benefits += "Chiranjeevi Yojna, "
                        }
                        if (ancque.memberDetails.isIayBeneficiary) {
                            ancque.memberDetails.benefits += "IAY, "
                        }
                        if (ancque.memberDetails.isJsyBeneficiary) {
                            ancque.memberDetails.benefits += "JSY, "
                        }
                        if (ancque.memberDetails.isKpsyBeneficiary) {
                            ancque.memberDetails.benefits += "KPSY"
                        }
                        if (ancque.memberDetails.benefits.length === 0) {
                            ancque.memberDetails.benefits = "None"
                        }
                        var dtoList = [];
                        var dto = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'ancVisitPlacesFhwAnc'
                            },
                            sequence: 1
                        };
                        dtoList.push(dto);
                        var dto2 = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'foetalPositionsFhwAnc'
                            },
                            sequence: 2
                        };
                        dtoList.push(dto2);
                        var dto3 = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'dangerousSignsFhwAnc'
                            },
                            sequence: 3
                        };
                        dtoList.push(dto3);
                        var dto4 = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'referralPlaceFhwAnc'
                            },
                            sequence: 4
                        };
                        dtoList.push(dto4);
                        var dto5 = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'deathReasonsFhwAnc'
                            },
                            sequence: 5
                        };
                        dtoList.push(dto5);
                        var previousAncInformation = {
                            code: 'retrieve_previous_anc_information',
                            parameters: {
                                healthid: ancque.memberDetails.uniqueHealthId,
                                curPregRegDetId: ancque.memberDetails.curPregRegDetId
                            },
                            sequence: 6
                        };
                        dtoList.push(previousAncInformation);
                        var vaccinationsGiven = {
                            code: 'retrieve_anc_vaccination_given',
                            parameters: {
                                id: Number($state.params.id),
                                pregRegDetId: ancque.memberDetails.curPregRegDetId
                            },
                            sequence: 7
                        };
                        dtoList.push(vaccinationsGiven);
                        Mask.show();
                        QueryDAO.executeAll(dtoList).then(function (res) {
                            ancque.ancVisitPlaces = res[0].result;
                            //                ancque.foetalPositions = res[1].result;
                            ancque.dangerSignsMotherValues = res[2].result;
                            ancque.dangerSignsMotherValues.push({
                                id: '-1',
                                value: 'Other'
                            })
                            ancque.referralPlaces = res[3].result;
                            ancque.deathReasons = res[4].result;
                            ancque.previousAncInfo = res[5].result;
                            ancque.previousVaccinations = res[6].result;
                        }).finally(function () {
                            Mask.hide();
                        });
                        createAncObj(ancque.memberDetails);
                    }
                    NdhmHipDAO.getAllCareContextMasterDetails(parseInt($state.params.id)).then((res) => {
                        if (res.length > 0) {
                            ancque.healthIdsData = res;
                            ancque.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
                            let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
                            ancque.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
                        }
                    }).catch((error) => {
                    })
                })
            } else {
                toaster.pop('error', 'Member Not found. Please try again');
                $state.go('techo.manage.ancSearch');
            }
        };
        var createAncObj = function (memberDetails) {
            ancque.ancQueData = {};
            ancque.additionalInfo = JSON.parse(memberDetails.additionalInfo);
            if (ancque.additionalInfo != null && ancque.additionalInfo.lastServiceLongDate != null) {
                if (moment(ancque.additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                    toaster.pop('error', 'ANC visit for this member has already been done today.')
                    $state.go('techo.manage.ancSearch');
                }
                ancque.minServiceDate = moment(ancque.additionalInfo.lastServiceLongDate).add(1, 'days');
            } else {
                ancque.minServiceDate = ancque.memberDetails.lmpDate
            }
            if (ancque.minServiceDate != null && ancque.minServiceDate < (moment().subtract(15, 'days'))) {
                ancque.minServiceDate = moment().subtract(15, 'days')
            }
            ancque.ancQueData.memberId = $state.params.id;
            ancque.ancQueData.familyId = memberDetails.fid;
            ancque.ancQueData.locationId = memberDetails.areaId != null ? memberDetails.areaId : memberDetails.locationId;
            ancque.ancQueData.memberStatus = 'AVAILABLE';
            ancque.ancQueData.lmp = memberDetails.lmpDate;
            ancque.ancQueData.bloodGroup = memberDetails.bloodGroup;
            ancque.ancQueData.lastDeliveryOutcome = memberDetails.lastDeliveryOutcome;

            ancque.ancQueData.jsyBeneficiary = memberDetails.jsyBeneficiary;
            ancque.ancQueData.jsyPaymentDone = memberDetails.jsyPaymentGiven;
            ancque.ancQueData.kpsyBeneficiary = memberDetails.kpsyBeneficiary;
            ancque.ancQueData.iayBeneficiary = memberDetails.iayBeneficiary;

            ancque.ancQueData.isMobileNumberAvailable = memberDetails.mobileNumber !== null ? true : false;
            ancque.ancQueData.isAccountNumberAvailable = memberDetails.accountNumber !== null ? true : false;
            ancque.ancQueData.isIfscAvailable = memberDetails.ifsc !== null ? true : false;
            ancque.ancQueData.eligibilityCriteria = memberDetails.bplFlag ? 'Family is BPL' : 'Family is not BPL';
            ancque.ancQueData.bpl = memberDetails.bplFlag ? 'Yes' : 'No';
            ancque.ancQueData.showLastDeliveryOutcome = memberDetails.currentGravida != null && memberDetails.currentGravida > 1 ? true : false;
            ancque.ancQueData.lmpDateIsOutside140 = new Date(moment(memberDetails.lmpDate).add(140, 'days')) < ancque.today ? true : false;
            ancque.ancQueData.visitmorethan3 = new Date(moment(memberDetails.lmpDate).add(190, 'days')) < ancque.today ? true : false;
            ancque.ancQueData.pregnantInLast3Years = new Date(moment(memberDetails.lastDeliveryDate).add(3, 'years')) < ancque.today ? true : false;
            if (moment().diff(moment(memberDetails.lmpDate), 'days') <= 90) {
                ancque.showCalcium = false;
            }
            if (moment().diff(moment(memberDetails.lmpDate), 'days') > 90) {
                ancque.showIfa = true;
            }
            if (memberDetails.immunisationGiven != null) {
                ancque.ancQueData.isTT1NotGiven = memberDetails.immunisationGiven.includes('TT1') ? false : true;
                ancque.ancQueData.isTTBoosterNotGiven = memberDetails.immunisationGiven.includes('TT_BOOSTER') ? false : true;
                ancque.ancQueData.isTT2NotGiven = memberDetails.immunisationGiven.includes('TT2') ? false : true;
            } else {
                ancque.ancQueData.isTT1NotGiven = true
                ancque.ancQueData.isTTBoosterNotGiven = true
                ancque.ancQueData.isTT2NotGiven = true
            }
            if (!ancque.ancQueData.pregnantInLast3Years && !ancque.ancQueData.isTT1NotGiven && ancque.ancQueData.isTT2NotGiven) {
                ancque.immunisations = memberDetails.immunisationGiven.split(',');
                ancque.immunisations.forEach(function (immunisation) {
                    if (immunisation.includes('TT1')) {
                        ancque.minTT2Date = moment(immunisation.split('#')[1], 'DD/MM/YYYY').add(28, 'days');
                    }
                })
            }
        };
        ancque.onBloodSugarTestChange = function (key) {
            if (key) {
                var bloodSugarTest = _.find(ancque.bloodSugarTestOptions, { key: key });
                ancque.ancQueData.bloodSugarTestIsDone = bloodSugarTest.key === 'NOT_DONE' ? false : true;
                ancque.ancQueData.bloodSugarTestIsDoneAfterFood = bloodSugarTest.key === 'NOT_EMPTY' ? true : false;
                ancque.ancQueData.bloodSugarTestIsDoneBeforeFood = bloodSugarTest.key === 'EMPTY' ? true : false;
            }
        };
        ancque.saveAnc = function () {
            ancque.ancQueData.mobileStartDate = ancque.mobileStartDate;
            ancque.ancQueData.mobileEndDate = new Date();
            if (ancque.ancQueData.deliveryPlace === 'HOSP') {
                ancque.ancQueData.typeOfHospital = ancque.ancQueData.institutionType;
                ancque.ancQueData.healthInfrastructureId = ancque.ancQueData.institute;
            } else if (ancque.ancQueData.deliveryPlace === 'THISHOSP') {
                ancque.ancQueData.typeOfHospital = ancque.ancQueData.institute.type;
                ancque.ancQueData.healthInfrastructureId = ancque.ancQueData.institute.id;
            } else {
                delete ancque.ancQueData.typeOfHospital;
                delete ancque.ancQueData.healthInfrastructureId;
            }
            ancque.ancQueData.immunisationDetails = [];
            if (ancque.ancQueData.tt1Given) {
                ancque.ancQueData.immunisationDetails.push({
                    immunisationGiven: "TT1",
                    immunisationDate: ancque.ancQueData.tt1date
                });
            }
            if (ancque.ancQueData.tt2Given) {
                ancque.ancQueData.immunisationDetails.push({
                    immunisationGiven: "TT2",
                    immunisationDate: ancque.ancQueData.tt2date
                });
            }
            if (ancque.ancQueData.ttBoosterGiven) {
                ancque.ancQueData.immunisationDetails.push({
                    immunisationGiven: "TT_BOOSTER",
                    immunisationDate: ancque.ancQueData.ttBoosterDate
                });
            }
            ancque.ancQueData.uniqueHealthID = ancque.memberDetails.uniqueHealthId;
            if (ancque.ancqueForm.$valid) {
                if (!ancque.isAlive) {
                    ancque.ancQueData.memberStatus = 'DEATH';
                } else {
                    ancque.ancQueData.memberStatus = 'AVAILABLE';
                }
                if (ancque.referralInfraDetails) {
                    ancque.ancQueData.referralInfraId = ancque.referralInfraDetails.institute ? Number(ancque.referralInfraDetails.institute) : null;
                } else {
                    ancque.ancQueData.referralInfraId = null;
                }
                Mask.show();
                AncService.createOrUpdate(ancque.ancQueData).then(function (response) {
                    Mask.hide();
                    if(ancque.isAlive && ancque.rights.isShowHIPModal){
                        ancque.handleLinkRecordInNdhm(Number($state.params.id), Number(response.result));
                    } else {
                        $state.go('techo.manage.ancSearch');// If mother is not alive then no need to link wellness record to ndhm
                    }
                    toaster.pop('success', 'Anc Registered Successfully');
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        ancque.handleLinkRecordInNdhm = (memberId, serviceId) => {
                let memberObj = {
                    memberId: memberId,
                    mobileNumber: ancque.memberDetails.mobileNumber,
                    name: ancque.memberDetails.firstName + " " + ancque.memberDetails.middleName + " " + ancque.memberDetails.lastName,
                    preferredHealthId: ancque.prefferedHealthId,
                    healthIdsData: ancque.healthIdsData
                }
                let dataForConsentRequest = {
                    title: "Link ANC Wellness Record To ABHA Address",
                    memberObj: memberObj,
                    consentRecord: "(ANC Wellness Record " + $filter('date')(ancque.ancQueData.serviceDate, "dd-MM-yyyy") + ")",
                    serviceType: "ANC_WELLNESS_RECORD",
                    serviceId: serviceId,
                    isTokenGenerate: true,
                    careContextName: "ANC Wellness Record"
                }
                NdhmHipUtilService.handleLinkRecordInNdhm(dataForConsentRequest, 'techo.manage.ancSearch');
        }

        ancque.deliveryPlaceChanged = function () {
            ancque.ancQueData.institute = null;
            if (ancque.ancQueData.deliveryPlace === 'THISHOSP') {
                if (ancque.institutes.length == 1) {
                    ancque.ancQueData.institute = ancque.institutes[0];
                } else if (ancque.institutes.length == 0) {
                    toaster.pop('error', 'No health infrastructure assigned');
                }
            }
        }

        ancque.retrieveHospitalsByInstitutionType = function () {
            Mask.show();
            ancque.showOtherInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(ancque.ancQueData.institutionType)
                }
            }).then(function (res) {
                ancque.showOtherInstitutes = true;
                ancque.otherInstitutes = res.result;
                var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ancque.otherInstitutes);
                ancque.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                Mask.hide();
            })
        }

        ancque.setUsersForInstitute = function () {
            ancque.usersForInstitute = [];
            delete ancque.ancQueData.deliveryPerson;
            if (ancque.ancQueData.deliveryPlace === 'HOSP') {
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(ancque.ancQueData.institute),
                        code: ancque.ancQueData.deliveryDoneBy || null
                    }
                }).then(function (res) {
                    ancque.usersForInstitute = res.result;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                });
            } else if (ancque.ancQueData.institute && ancque.ancQueData.deliveryPlace === 'THISHOSP') {
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(ancque.ancQueData.institute.id),
                        code: ancque.ancQueData.deliveryDoneBy || null
                    }
                }).then(function (res) {
                    ancque.usersForInstitute = res.result;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                });
            }
        }

        ancque.serviceDateChanged = function () {
            ancque.vaccinationMaxDate = ancque.ancQueData.serviceDate;
            ancque.ancQueData.ironDefAnemiaInjDueDate = null
            ancque.ancQueData.tt1Given = null;
            ancque.ancQueData.tt1date = null;
            ancque.ancQueData.tt2Given = null;
            ancque.ancQueData.tt2date = null;
            ancque.ancQueData.ttBoosterGiven = null;
            ancque.ancQueData.ttBoosterDate = null;
            ancque.ancQueData.deathDate = null;
            ancque.minInjDate = moment(ancque.ancQueData.serviceDate).add(1, 'days');
            ancque.maxInjDate = moment(ancque.ancQueData.serviceDate).add(30, 'days');
        }

        ancque.ironDefAnemiaInjChanged = function () {
            ancque.ancQueData.ironDefAnemiaInjDueDate = null
        }

        ancque.retrieveData = function (field) {
            var dto = {
                code: 'retrival_listvalue_values_acc_field',
                parameters: {
                    fieldKey: field
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                Mask.hide();
                if (field === 'infra_type') {
                    ancque.institutionTypes = res.result;
                }
            }, function (error) {
                Mask.hide();
                toaster.pop('error', 'Unable to fetch data');
            });
        };

        ancque.isAliveChanged = () => {
            createAncObj(ancque.memberDetails);
            ancque.ancqueForm.$setPristine();
        }

        ancque.retrieveData("infra_type");

        ancque.goBack = function () {
            window.history.back();
        };

        ancque.dangerSignsChanged = () => {
            ancque.showOtherDangerSign = ancque.ancQueData.dangerousSignIds.some(dangerSign => dangerSign == -1);
            ancque.ancQueData.otherDangerousSign = null;
        }

        init();
    }
    angular.module('imtecho.controllers').controller('AncFormQuestionsController', AncFormQuestionsController);
})(window.angular);
