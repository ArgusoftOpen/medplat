/* global moment */
(function () {
    function ManageChildServiceController(ManageChildServiceDAO, QueryDAO, Mask, toaster, $state, GeneralUtil, AuthenticateService, $q) {
        var ctrl = this;
        const FEATURE = 'techo.manage.childServiceSearch';
        const CHILD_SERVICE_FORM_CONFIGURATION_KEY = 'RCH_FACILITY_CHILD_SERVICE';
        ctrl.formData = {};
        ctrl.formUuids = {};
        ctrl.today = moment().startOf('day');


        ctrl.init = () => {
            ctrl.formData.mobileStartDate = moment();
            Mask.show();
            QueryDAO.execute({
                code: 'child_service_by_id',
                parameters: {
                    childId: Number($state.params.id)
                }
            }).then((response) => {
                if (!response.result || response.result.length === 0) {
                    return Promise.reject({ data: { message: 'Member not found.' } });
                }
                ctrl.formData = response.result[0];
                ctrl.formData.cerebralDetails = {};
                ctrl.formData.ageOfChild = moment().diff(moment(ctrl.formData.dob), 'day');
                ctrl.additionalInfo = JSON.parse(ctrl.formData.additionalInfo);
                if (ctrl.additionalInfo && ctrl.additionalInfo.lastServiceLongDate) {
                    if (moment(ctrl.additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                        return Promise.reject({ data: { message: 'Child Service Visit for this child has already been done today.' } });
                    }
                    ctrl.minDeathDate = moment(ctrl.additionalInfo.lastServiceLongDate);
                    ctrl.minServiceDate = moment(ctrl.additionalInfo.lastServiceLongDate).add(1, 'days');
                } else {
                    ctrl.minServiceDate = ctrl.formData.dob
                    ctrl.minDeathDate = ctrl.minServiceDate;
                }
                if (ctrl.minServiceDate && ctrl.minServiceDate < (moment().subtract(15, 'days'))) {
                    ctrl.minServiceDate = moment().subtract(15, 'days')
                }
                return ManageChildServiceDAO.getDueImmunisationsForChild(moment(ctrl.formData.dob).valueOf(), ctrl.formData.immunisationGiven || null);
            }).then((response) => {
                ctrl.formData.dueImmunisations = response;
                ctrl.formData.immunisation = {};
                ctrl.formData.dueImmunisations.forEach((immu) => {
                    ctrl.formData.immunisation[immu] = {};
                });
                let queryDtoList = [{
                    code: 'retrieve_worker_info_by_location_id',
                    parameters: {
                        locationId: ctrl.formData.locationId
                    },
                    sequence: 1
                }, {
                    code: 'retrieve_worker_info_by_location_id',
                    parameters: {
                        locationId: ctrl.formData.areaId
                    },
                    sequence: 2
                }, {
                    code: 'cerebral_palsy_module_check',
                    parameters: {
                        locationId: ctrl.formData.locationId
                    },
                    sequence: 3
                }, {
                    code: 'check_last_cp_status',
                    parameters: {
                        id: Number($state.params.id)
                    },
                    sequence: 4
                }, {
                    code: 'retrieve_last_cp_questions_list',
                    parameters: {
                        id: Number($state.params.id)
                    },
                    sequence: 5
                }];
                return QueryDAO.executeAll(queryDtoList);
            }).then((response) => {
                if (response[0].result[0].workerDetails) {
                    let fhwJson = JSON.parse(response[0].result[0].workerDetails);
                    ctrl.formData.anmInfo = `${fhwJson[0].name} (${fhwJson[0].mobileNumber})`;
                }
                if (response[1].result[0].workerDetails != null) {
                    let ashaJson = JSON.parse(response[1].result[0].workerDetails);
                    ctrl.formData.ashaInfo = `${ashaJson[0].name} (${ashaJson[0].mobileNumber})`;
                }
                if (response[2].result[0] && response[2].result[0].cerebral_palsy_module && (!response[3].result[0] || (response[3].result[0] && response[3].result[0].status !== 'TREATMENT_COMMENCED'))) {
                    ctrl.formData.showCpQuestions = true;
                } else {
                    ctrl.formData.showCpQuestions = false;
                }
                if (response[4].result[0]) {
                    ctrl.cpQuestionsList = response[4].result[0];
                }
                return ManageChildServiceDAO.getLastChildVisit($state.params.id);
            }).then((response) => {
                if ((response && response.complementaryFeedingStarted) || ctrl.formData.ageOfChild <= 180) {
                    ctrl.formData.showComplementaryFeeding = false;
                } else {
                    ctrl.formData.showComplementaryFeeding = true;
                }
                let promiseList = [];
                promiseList.push(AuthenticateService.getLoggedInUser());
                promiseList.push(AuthenticateService.getAssignedFeature(FEATURE));
                return $q.all(promiseList);
            }).then((response) => {
                ctrl.loggedInUser = response[0].data;
                ctrl.formConfigurations = response[1].systemConstraintConfigs[CHILD_SERVICE_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response[1].webTemplateConfigs[CHILD_SERVICE_FORM_CONFIGURATION_KEY];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.childServiceSearch');
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.serviceDateChanged = () => {
            ctrl.formData.deathDate = null;
        }

        ctrl.placeOfVisitChanged = () => {
            ctrl.formData.hospitalByUserList.data = [];
            ctrl.formData.hospitalByTypeList.data = [];
            ctrl.formData.hospitalByType = null;
            ctrl.formData.hospitalByUser = null;
            ctrl.formData.institutionType = null;
            if (ctrl.formData.deliveryPlace === 'THISHOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: ctrl.loggedInUser.id
                    }
                }).then((response) => {
                    ctrl.formData.hospitalByUserList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    if (ctrl.formData.hospitalByUserList.data.length == 1) {
                        ctrl.formData.hospitalByUser = ctrl.formData.hospitalByUserList.data[0].key;
                    } else if (ctrl.formData.hospitalByUserList.data.length == 0) {
                        return Promise.reject({ data: { message: 'No health infrastructure assigned' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.retrieveHospitalsByInstitutionType = () => {
            ctrl.formData.hospitalByUserList.data = [];
            ctrl.formData.hospitalByTypeList.data = [];
            ctrl.formData.hospitalByType = null;
            ctrl.formData.hospitalByUser = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.institutionType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.hospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.isChildAliveChanged = () => {
            Object.assign(ctrl.formData, {
                ...ctrl.formData,
                deathDate: null,
                placeOfDeath: null,
                deathInstitutionType: null,
                deathHospitalByType: null,
                deathReason: null,
                otherDeathReason: null,
                weight: null,
                height: null,
                sdScore: null,
                midArmCircumference: null,
                havePedalEdema: null,
                ifaSyrupGiven: null,
                complementaryFeedingStarted: null,
                complementaryFeedingStartPeriod: null,
                dieseases: null,
                otherDiseases: null,
                isTreatementDone: null,
                exclusivelyBreastfeded: null,
            });
            Object.keys(ctrl.formData.immunisation).forEach((key) => {
                ctrl.formData.immunisation[key].given = null;
                ctrl.formData.immunisation[key].date = null;
            });
            Object.keys(ctrl.formData.cerebralDetails).forEach((key) => {
                delete ctrl.formData.cerebralDetails[key];
            });
            ctrl.isAnyCpQuestionDisplay = false;
            if (ctrl.formData.showCpQuestions && ctrl.formData.isAlive) {
                ctrl.initializeQuestionList();
            }
            if (ctrl.formData.isAlive && ctrl.formData.dueImmunisations.length) {
                ctrl.showImmunisationSection = true;
            } else {
                ctrl.showImmunisationSection = false;
            }
        }

        ctrl.initializeQuestionList = () => {
            if (ctrl.cpQuestionsList) {
                if (ctrl.cpQuestionsList.avoid_strangers) {
                    ctrl.formData.cerebralDetails.avoidStrangers = true
                    ctrl.formData.cerebralDetails.avoidStrangersCompleted = true
                }
                if (ctrl.cpQuestionsList.climb_updown_stairs) {
                    ctrl.formData.cerebralDetails.climbUpDownStairs = true
                    ctrl.formData.cerebralDetails.climbUpDownStairsCompleted = true
                }
                if (ctrl.cpQuestionsList.drink_from_glass) {
                    ctrl.formData.cerebralDetails.drinkFromGlass = true
                    ctrl.formData.cerebralDetails.drinkFromGlassCompleted = true
                }
                if (ctrl.cpQuestionsList.enjoy_peekaboo) {
                    ctrl.formData.cerebralDetails.enjoyPeekaboo = true
                    ctrl.formData.cerebralDetails.enjoyPeekabooCompleted = true
                }
                if (ctrl.cpQuestionsList.flip_pages) {
                    ctrl.formData.cerebralDetails.flipPages = true
                    ctrl.formData.cerebralDetails.flipPagesCompleted = true
                }
                if (ctrl.cpQuestionsList.hands_in_mouth) {
                    ctrl.formData.cerebralDetails.handsInMouth = true
                    ctrl.formData.cerebralDetails.handsInMouthCompleted = true
                }
                if (ctrl.cpQuestionsList.hold_head_straight) {
                    ctrl.formData.cerebralDetails.holdHeadStraight = true
                    ctrl.formData.cerebralDetails.holdHeadStraightCompleted = true
                }
                if (ctrl.cpQuestionsList.hold_things_with_finger) {
                    ctrl.formData.cerebralDetails.holdThingsWithFinger = true
                    ctrl.formData.cerebralDetails.holdThingsWithFingerCompleted = true
                }
                if (ctrl.cpQuestionsList.kick_ball) {
                    ctrl.formData.cerebralDetails.kickBall = true
                    ctrl.formData.cerebralDetails.kickBallCompleted = true
                }
                if (ctrl.cpQuestionsList.kneel_down) {
                    ctrl.formData.cerebralDetails.kneelDown = true
                    ctrl.formData.cerebralDetails.kneelDownCompleted = true
                }
                if (ctrl.cpQuestionsList.lifts_toys) {
                    ctrl.formData.cerebralDetails.liftToys = true
                    ctrl.formData.cerebralDetails.liftToysCompleted = true
                }
                if (ctrl.cpQuestionsList.like_playing_other_children) {
                    ctrl.formData.cerebralDetails.likePlayingWithOtherChildren = true
                    ctrl.formData.cerebralDetails.likePlayingWithOtherChildrenCompleted = true
                }
                if (ctrl.cpQuestionsList.look_in_direc_of_sound) {
                    ctrl.formData.cerebralDetails.lookInDirectionOfSound = true
                    ctrl.formData.cerebralDetails.lookInDirectionOfSoundCompleted = true
                }
                if (ctrl.cpQuestionsList.look_when_name_called) {
                    ctrl.formData.cerebralDetails.lookWhenNameCalled = true
                    ctrl.formData.cerebralDetails.lookWhenNameCalledCompleted = true
                }
                if (ctrl.cpQuestionsList.look_when_speak) {
                    ctrl.formData.cerebralDetails.lookWhenSpeak = true
                    ctrl.formData.cerebralDetails.lookWhenSpeakCompleted = true
                }
                if (ctrl.cpQuestionsList.make_noise_when_speak) {
                    ctrl.formData.cerebralDetails.makeNoiseWhenSpeak = true
                    ctrl.formData.cerebralDetails.makeNoiseWhenSpeakCompleted = true
                }
                if (ctrl.cpQuestionsList.mimic_others) {
                    ctrl.formData.cerebralDetails.mimicOthers = true
                    ctrl.formData.cerebralDetails.mimicOthersCompleted = true
                }
                if (ctrl.cpQuestionsList.responds_on_name_calling) {
                    ctrl.formData.cerebralDetails.respondsOnNameCalling = true
                    ctrl.formData.cerebralDetails.respondsOnNameCallingCompleted = true
                }
                if (ctrl.cpQuestionsList.run_independently) {
                    ctrl.formData.cerebralDetails.runIndependently = true
                    ctrl.formData.cerebralDetails.runIndependentlyCompleted = true
                }
                if (ctrl.cpQuestionsList.sit_without_help) {
                    ctrl.formData.cerebralDetails.sitWithoutHelp = true
                    ctrl.formData.cerebralDetails.sitWithoutHelpCompleted = true
                }
                if (ctrl.cpQuestionsList.speak_simple_words) {
                    ctrl.formData.cerebralDetails.speakSimpleWords = true
                    ctrl.formData.cerebralDetails.speakSimpleWordsCompleted = true
                }
                if (ctrl.cpQuestionsList.speak_two_sentences) {
                    ctrl.formData.cerebralDetails.speakTwoSentences = true
                    ctrl.formData.cerebralDetails.speakTwoSentencesCompleted = true
                }
                if (ctrl.cpQuestionsList.tell_name_of_things) {
                    ctrl.formData.cerebralDetails.tellNameOfThings = true
                    ctrl.formData.cerebralDetails.tellNameOfThingsCompleted = true
                }
                if (ctrl.cpQuestionsList.understand_instructions) {
                    ctrl.formData.cerebralDetails.understandInstructions = true
                    ctrl.formData.cerebralDetails.understandInstructionsCompleted = true
                }
                if (ctrl.cpQuestionsList.understand_no) {
                    ctrl.formData.cerebralDetails.understandNo = true
                    ctrl.formData.cerebralDetails.understandNoCompleted = true
                }
                let age90 = ['hold_head_straight', 'hands_in_mouth', 'look_when_speak', 'make_noise_when_speak', 'look_in_direc_of_sound'];
                let age270 = ['sit_without_help', 'kneel_down', 'avoid_strangers', 'understand_no', 'enjoy_peekaboo', 'responds_on_name_calling', 'lifts_toys'];
                let age450 = ['mimic_others', 'drink_from_glass', 'run_independently', 'hold_things_with_finger', 'look_when_name_called', 'speak_simple_words',
                    'understand_instructions'];
                let age720 = ['tell_name_of_things', 'flip_pages', 'kick_ball', 'climb_updown_stairs', 'speak_two_sentences', 'like_playing_other_children'];
                let age = ctrl.formData.ageOfChild
                if (age >= 90 && age <= 270) {
                    ctrl.checkForQuestion(age90);
                } else if (age >= 270 && age <= 450) {
                    ctrl.checkForQuestion(age270);
                } else if (age >= 450 && age <= 720) {
                    ctrl.checkForQuestion(age450);
                } else if (age >= 720 && age <= 900) {
                    ctrl.checkForQuestion(age720);
                }
            } else if (ctrl.formData.ageOfChild >= 90 && ctrl.formData.ageOfChild <= 900) {
                ctrl.isAnyCpQuestionDisplay = true;
            }
        }

        ctrl.checkForQuestion = (list) => {
            list.forEach(question => {
                if (!ctrl.cpQuestionsList[question]) {
                    ctrl.isAnyCpQuestionDisplay = true;
                }
            });
        }

        ctrl.deathPlaceChanged = () => {
            ctrl.formData.deathInstitutionType = null;
            ctrl.formData.deathHospitalByType = null;
        }

        ctrl.retrieveDeathHospitalsByInstitutionType = () => {
            ctrl.formData.deathHospitalByTypeList.data = [];
            ctrl.formData.deathHospitalByType = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.deathInstitutionType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.deathHospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.deathReasonChanged = () => {
            ctrl.formData.otherDeathReason = null;
        }

        ctrl.calculateSdScore = () => {
            if (ctrl.formData.gender && ctrl.formData.weight && ctrl.formData.height) {
                Mask.show();
                ManageChildServiceDAO.retrieveSdScore(ctrl.formData.gender, ctrl.formData.height, ctrl.formData.weight).then((response) => {
                    switch (response.sdScore) {
                        case 'SD4':
                            ctrl.formData.sdScore = "SD4";
                            break;
                        case 'SD3':
                            ctrl.formData.sdScore = "SD3";
                            break;
                        case 'SD2':
                            ctrl.formData.sdScore = "SD2";
                            break;
                        case 'SD1':
                            ctrl.formData.sdScore = "SD1";
                            break;
                        case 'MEDIAN':
                            ctrl.formData.sdScore = "MEDIAN";
                            break;
                        default:
                            ctrl.formData.sdScore = "NONE";
                            break;
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            } else if (!ctrl.formData.gender) {
                toaster.pop('error', 'Could not calculate SD Score. Gender not found');
            }
        }

        ctrl.complementaryFeedingStartedChanged = () => {
            ctrl.formData.complementaryFeedingStartPeriod = null;
        }

        ctrl.diseasesChanged = () => {
            ctrl.formData.otherDiseases = null;
        }

        ctrl.hepatitisB0GivenChanged = () => ctrl.immunisationGivenChanged('HEPATITIS_B_0');

        ctrl.hepatitisB0DateChanged = (_, uuid) => {
            ctrl.formUuids[`HEPATITIS_B_0`] = uuid;
            ctrl.immunisationDateChanged('HEPATITIS_B_0');
        }

        ctrl.vitaminKGivenChanged = () => ctrl.immunisationGivenChanged('VITAMIN_K');

        ctrl.vitaminKDateChanged = (_, uuid) => {
            ctrl.formUuids[`VITAMIN_K`] = uuid;
            ctrl.immunisationDateChanged('VITAMIN_K');
        }

        ctrl.bcgGivenChanged = () => ctrl.immunisationGivenChanged('BCG');

        ctrl.bcgDateChanged = (_, uuid) => {
            ctrl.formUuids[`BCG`] = uuid;
            ctrl.immunisationDateChanged('BCG');
        }

        ctrl.opv0GivenChanged = () => ctrl.immunisationGivenChanged('OPV_0');

        ctrl.opv0DateChanged = (_, uuid) => {
            ctrl.formUuids[`OPV_0`] = uuid;
            ctrl.immunisationDateChanged('OPV_0');
        }

        ctrl.opv1GivenChanged = () => ctrl.immunisationGivenChanged('OPV_1');

        ctrl.opv1DateChanged = (_, uuid) => {
            ctrl.formUuids[`OPV_1`] = uuid;
            ctrl.immunisationDateChanged('OPV_1');
        }

        ctrl.opv2GivenChanged = () => ctrl.immunisationGivenChanged('OPV_2');

        ctrl.opv2DateChanged = (_, uuid) => {
            ctrl.formUuids[`OPV_2`] = uuid;
            ctrl.immunisationDateChanged('OPV_2');
        }

        ctrl.opv3GivenChanged = () => ctrl.immunisationGivenChanged('OPV_3');

        ctrl.opv3DateChanged = (_, uuid) => {
            ctrl.formUuids[`OPV_3`] = uuid;
            ctrl.immunisationDateChanged('OPV_3');
        }

        ctrl.opvBoosterGivenChanged = () => ctrl.immunisationGivenChanged('OPV_BOOSTER');

        ctrl.opvBoosterDateChanged = (_, uuid) => {
            ctrl.formUuids[`OPV_BOOSTER`] = uuid;
            ctrl.immunisationDateChanged('OPV_BOOSTER');
        }

        ctrl.penta1GivenChanged = () => ctrl.immunisationGivenChanged('PENTA_1');

        ctrl.penta1DateChanged = (_, uuid) => {
            ctrl.formUuids[`PENTA_1`] = uuid;
            ctrl.immunisationDateChanged('PENTA_1');
        }

        ctrl.penta2GivenChanged = () => ctrl.immunisationGivenChanged('PENTA_2');

        ctrl.penta2DateChanged = (_, uuid) => {
            ctrl.formUuids[`PENTA_2`] = uuid;
            ctrl.immunisationDateChanged('PENTA_2');
        }

        ctrl.penta3GivenChanged = () => ctrl.immunisationGivenChanged('PENTA_3');

        ctrl.penta3DateChanged = (_, uuid) => {
            ctrl.formUuids[`PENTA_3`] = uuid;
            ctrl.immunisationDateChanged('PENTA_3');
        }

        ctrl.dpt1GivenChanged = () => ctrl.immunisationGivenChanged('DPT_1');

        ctrl.dpt1DateChanged = (_, uuid) => {
            ctrl.formUuids[`DPT_1`] = uuid;
            ctrl.immunisationDateChanged('DPT_1');
        }

        ctrl.dpt2GivenChanged = () => ctrl.immunisationGivenChanged('DPT_2');

        ctrl.dpt2DateChanged = (_, uuid) => {
            ctrl.formUuids[`DPT_2`] = uuid;
            ctrl.immunisationDateChanged('DPT_2');
        }

        ctrl.dpt3GivenChanged = () => ctrl.immunisationGivenChanged('DPT_3');

        ctrl.dpt3DateChanged = (_, uuid) => {
            ctrl.formUuids[`DPT_3`] = uuid;
            ctrl.immunisationDateChanged('DPT_3');
        }

        ctrl.dptBoosterGivenChanged = () => ctrl.immunisationGivenChanged('DPT_BOOSTER');

        ctrl.dptBoosterDateChanged = (_, uuid) => {
            ctrl.formUuids[`DPT_BOOSTER`] = uuid;
            ctrl.immunisationDateChanged('DPT_BOOSTER');
        }

        ctrl.rotaVirus1GivenChanged = () => ctrl.immunisationGivenChanged('ROTA_VIRUS_1');

        ctrl.rotaVirus1DateChanged = (_, uuid) => {
            ctrl.formUuids[`ROTA_VIRUS_1`] = uuid;
            ctrl.immunisationDateChanged('ROTA_VIRUS_1');
        }

        ctrl.rotaVirus2GivenChanged = () => ctrl.immunisationGivenChanged('ROTA_VIRUS_2');

        ctrl.rotaVirus2DateChanged = (_, uuid) => {
            ctrl.formUuids[`ROTA_VIRUS_2`] = uuid;
            ctrl.immunisationDateChanged('ROTA_VIRUS_2');
        }

        ctrl.rotaVirus3GivenChanged = () => ctrl.immunisationGivenChanged('ROTA_VIRUS_3');

        ctrl.rotaVirus3DateChanged = (_, uuid) => {
            ctrl.formUuids[`ROTA_VIRUS_3`] = uuid;
            ctrl.immunisationDateChanged('ROTA_VIRUS_3');
        }

        ctrl.measles1GivenChanged = () => ctrl.immunisationGivenChanged('MEASLES_1');

        ctrl.measles1DateChanged = (_, uuid) => {
            ctrl.formUuids[`MEASLES_1`] = uuid;
            ctrl.immunisationDateChanged('MEASLES_1');
        }

        ctrl.measles2GivenChanged = () => ctrl.immunisationGivenChanged('MEASLES_2');

        ctrl.measles2DateChanged = (_, uuid) => {
            ctrl.formUuids[`MEASLES_2`] = uuid;
            ctrl.immunisationDateChanged('MEASLES_2');
        }

        ctrl.measlesRubella1GivenChanged = () => ctrl.immunisationGivenChanged('MEASLES_RUBELLA_1');

        ctrl.measlesRubella1DateChanged = (_, uuid) => {
            ctrl.formUuids[`MEASLES_RUBELLA_1`] = uuid;
            ctrl.immunisationDateChanged('MEASLES_RUBELLA_1');
        }

        ctrl.measlesRubella2GivenChanged = () => ctrl.immunisationGivenChanged('MEASLES_RUBELLA_2');

        ctrl.measlesRubella2DateChanged = (_, uuid) => {
            ctrl.formUuids[`MEASLES_RUBELLA_2`] = uuid;
            ctrl.immunisationDateChanged('MEASLES_RUBELLA_2');
        }

        ctrl.fIpv101GivenChanged = () => ctrl.immunisationGivenChanged('F_IPV_1_01');

        ctrl.fIpv101DateChanged = (_, uuid) => {
            ctrl.formUuids[`F_IPV_1_01`] = uuid;
            ctrl.immunisationDateChanged('F_IPV_1_01');
        }

        ctrl.fIpv201GivenChanged = () => ctrl.immunisationGivenChanged('F_IPV_2_01');

        ctrl.fIpv201DateChanged = (_, uuid) => {
            ctrl.formUuids[`F_IPV_2_01`] = uuid;
            ctrl.immunisationDateChanged('F_IPV_2_01');
        }

        ctrl.fIpv205GivenChanged = () => ctrl.immunisationGivenChanged('F_IPV_2_05');

        ctrl.fIpv205DateChanged = (_, uuid) => {
            ctrl.formUuids[`F_IPV_2_05`] = uuid;
            ctrl.immunisationDateChanged('F_IPV_2_05');
        }

        ctrl.vitaminAGivenChanged = () => ctrl.immunisationGivenChanged('VITAMIN_A');

        ctrl.vitaminADateChanged = (_, uuid) => {
            ctrl.formUuids[`VITAMIN_A`] = uuid;
            ctrl.immunisationDateChanged('VITAMIN_A');
        }

        ctrl.immunisationGivenChanged = (immunisation) => {
            ctrl.formData.immunisation[immunisation].date = null;
            Object.keys(ctrl.formData.immunisation).forEach((key) => {
                if (!ctrl.formData.immunisation[key].given) {
                    if (ctrl.formData.immunisationGiven.includes(key)) {
                        let givenImmunisationsMap = ctrl.formData.immunisationGiven.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        ctrl.formData.immunisationGiven = givenImmunisationsMap.join(',');
                    }
                } else if (ctrl.formData.immunisation[key].given && ctrl.formData.immunisation[key].date !== null) {
                    let givenImmunisation = ctrl.formData.immunisationGiven;
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
                    ManageChildServiceDAO.vaccinationValidationChild(moment(ctrl.formData.dob).valueOf(), moment(ctrl.formData.immunisation[key].date).valueOf(), key, givenImmunisation).then((response) => {
                        if (response.result !== "" && response.result !== null) {
                            ctrl.childServiceForm[ctrl.formUuids[key]].$setValidity('Invalid vaccine date', false);
                        } else {
                            ctrl.childServiceForm[ctrl.formUuids[key]].$setValidity('Invalid vaccine date', true);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                }
            });
        }

        ctrl.immunisationDateChanged = (immunisation) => {
            if (!ctrl.formData.immunisationGiven) {
                ctrl.formData.immunisationGiven = "";
            }
            if (ctrl.formData.immunisationGiven.includes(immunisation)) {
                let givenImmunisationsMap = ctrl.formData.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((imm, i) => {
                    if (imm.includes(immunisation)) {
                        givenImmunisationsMap.splice(i, 1);
                    }
                });
                ctrl.formData.immunisationGiven = givenImmunisationsMap.join(',');
            }
            Mask.show();
            ManageChildServiceDAO.vaccinationValidationChild(moment(ctrl.formData.dob).valueOf(), moment(ctrl.formData.immunisation[immunisation].date).valueOf(), immunisation, ctrl.formData.immunisationGiven).then((response) => {
                if (response.result !== "" && response.result !== null) {
                    ctrl.childServiceForm[ctrl.formUuids[immunisation]].$setValidity('Invalid vaccine date', false);
                } else {
                    ctrl.childServiceForm[ctrl.formUuids[immunisation]].$setValidity('Invalid vaccine date', true);
                    ctrl.setCurrentlyGivenVaccines(immunisation);
                }
                Object.keys(ctrl.formData.immunisation).forEach((key) => {
                    let givenImmunisations = ctrl.formData.immunisationGiven
                    if (ctrl.formData.immunisation[key].given && ctrl.formData.immunisation[key].date !== null) {
                        if (givenImmunisations.includes(key)) {
                            let givenImmunisationsMap = givenImmunisations.split(',');
                            givenImmunisationsMap.forEach((imm, i) => {
                                if (imm.includes(key)) {
                                    givenImmunisationsMap.splice(i, 1);
                                }
                            });
                            givenImmunisations = givenImmunisationsMap.join(',');
                        }
                        Mask.show();
                        ManageChildServiceDAO.vaccinationValidationChild(moment(ctrl.formData.dob).valueOf(), moment(ctrl.formData.immunisation[key].date).valueOf(), key, givenImmunisations).then((res) => {
                            if (res.result !== "" && res.result !== null) {
                                ctrl.childServiceForm[ctrl.formUuids[key]].$setValidity('Invalid vaccine date', false);
                            } else {
                                ctrl.childServiceForm[ctrl.formUuids[key]].$setValidity('Invalid vaccine date', true);
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        });
                    }
                });
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.setCurrentlyGivenVaccines = (immunisation) => {
            if (ctrl.formData.immunisationGiven.includes(immunisation)) {
                let givenImmunisationsMap = ctrl.formData.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((imm, index) => {
                    if (imm.includes(immunisation)) {
                        givenImmunisationsMap.splice(index, 1);
                    }
                });
                ctrl.formData.immunisationGiven = givenImmunisationsMap.join(',');
            }

            if (ctrl.formData.immunisationGiven === null || ctrl.formData.immunisationGiven === "") {
                ctrl.formData.immunisationGiven = immunisation + "#" + moment(ctrl.formData.immunisation[immunisation].date).format("DD/MM/YYYY");
            } else {
                ctrl.formData.immunisationGiven += "," + immunisation + "#" + moment(ctrl.formData.immunisation[immunisation].date).format("DD/MM/YYYY");
            }
        }

        ctrl.saveChildVisit = () => {
            if (ctrl.childServiceForm.$valid) {
                ctrl.formData.memberStatus = ctrl.formData.isAlive ? "AVAILABLE" : "DEATH";
                ctrl.formData.mobileEndDate = moment();
                ctrl.formData.typeOfHospital = ctrl.formData.institutionType;
                if (ctrl.formData.deliveryPlace === 'HOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByType;
                } else if (ctrl.formData.deliveryPlace === 'THISHOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByUser;
                }
                ctrl.formData.deathInfrastructureId = ctrl.formData.deathHospitalByType;
                ctrl.formData.isFromWeb = true;
                ctrl.formData.immunisationDtos = [];
                ctrl.formData.dueImmunisations.forEach((immunisation) => {
                    if (ctrl.formData.immunisation !== null && ctrl.formData.immunisation[immunisation].given) {
                        ctrl.formData.immunisationDtos.push({
                            immunisationGiven: immunisation,
                            immunisationDate: moment(ctrl.formData.immunisation[immunisation].date).toDate()
                        });
                    }
                });
                Mask.show();
                ManageChildServiceDAO.createOrUpdate(ctrl.formData).then(() => {
                    toaster.pop('success', 'Child visit saved');
                    $state.go('techo.manage.childServiceSearch');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.transformArrayToKeyValue = (array, keyProperty, valueProperty) => {
            return array.map((element) => {
                return {
                    key: element[keyProperty],
                    value: element[valueProperty]
                }
            });
        }

        ctrl.goBack = () => {
            window.history.back();
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('ManageChildServiceController', ManageChildServiceController);
})();
