/* global moment */
(function () {
    function ManageChildServiceController(ManageChildServiceDAO, QueryDAO, Mask, $stateParams, toaster, AnganwadiService, $state, GeneralUtil, AuthenticateService, SelectizeGenerator) {
        var managechildservicecontroller = this;
        managechildservicecontroller.todayDate = moment();

        managechildservicecontroller.isDiseaseOther = () => {
            managechildservicecontroller.diseaseOtherSelected = managechildservicecontroller.manageChildServiceObject.dieseases.some(disease => disease == -1);
            managechildservicecontroller.manageChildServiceObject.otherDiseases = null;
        }

        managechildservicecontroller.saveChildVisit = () => {
            if (managechildservicecontroller.managechildServiceForm.$valid) {
                managechildservicecontroller.manageChildServiceObject.memberStatus = managechildservicecontroller.manageChildServiceObject.isAlive ? "AVAILABLE" : "DEATH";
                if (managechildservicecontroller.manageChildServiceObject.dieseases != null && managechildservicecontroller.manageChildServiceObject.dieseases[0] === 'NONE') {
                    delete managechildservicecontroller.manageChildServiceObject.dieseases;
                }
                managechildservicecontroller.manageChildServiceObject.mobileEndDate = new Date();
                managechildservicecontroller.manageChildServiceObject.mobileStartDate = managechildservicecontroller.manageChildServiceObject.mobileStartDate || managechildservicecontroller.mobileStartDate
                if (managechildservicecontroller.manageChildServiceObject.deliveryPlace === 'HOSP') {
                    managechildservicecontroller.manageChildServiceObject.typeOfHospital = managechildservicecontroller.manageChildServiceObject.institutionType;
                    managechildservicecontroller.manageChildServiceObject.healthInfrastructureId = managechildservicecontroller.manageChildServiceObject.institute;
                } else if (managechildservicecontroller.manageChildServiceObject.deliveryPlace === 'THISHOSP') {
                    managechildservicecontroller.manageChildServiceObject.typeOfHospital = managechildservicecontroller.manageChildServiceObject.institute.type;
                    managechildservicecontroller.manageChildServiceObject.healthInfrastructureId = managechildservicecontroller.manageChildServiceObject.institute.id;
                } else {
                    delete managechildservicecontroller.manageChildServiceObject.typeOfHospital;
                    delete managechildservicecontroller.manageChildServiceObject.healthInfrastructureId;
                }
                managechildservicecontroller.manageChildServiceObject.isFromWeb = true;
                managechildservicecontroller.manageChildServiceObject.immunisationDtos = [];
                managechildservicecontroller.manageChildServiceObject.dueImmunisations.forEach((immunisation) => {
                    if (managechildservicecontroller.manageChildServiceObject.immunisation != null && managechildservicecontroller.manageChildServiceObject.immunisation[immunisation].given) {
                        managechildservicecontroller.manageChildServiceObject.immunisationDtos.push({
                            immunisationGiven: immunisation,
                            immunisationDate: moment(managechildservicecontroller.manageChildServiceObject.immunisation[immunisation].date).toDate()
                        });
                    }
                });
                Mask.show();
                ManageChildServiceDAO.createOrUpdate(managechildservicecontroller.manageChildServiceObject).then((response) => {
                    toaster.pop('success', 'Child visit saved');
                    $state.go('techo.manage.childServiceSearch');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        managechildservicecontroller.init = () => {
            managechildservicecontroller.manageChildServiceObject = {};
            managechildservicecontroller.manageChildServiceObject.motherDetails = {};
            managechildservicecontroller.mobileStartDate = new Date();
            managechildservicecontroller.manageChildServiceObject.mobileStartDate = new Date();
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                managechildservicecontroller.loggedInUser = user.data;
                return QueryDAO.execute({
                    code: 'child_service_by_id',
                    parameters: {
                        childId: Number($stateParams.id)
                    }
                })
            }).then((response) => {
                if (!response.result || response.result.length === 0) {
                    return Promise.reject({ data: { message: 'Member not found.' } });
                }
                managechildservicecontroller.manageChildServiceObject = response.result[0];
                managechildservicecontroller.manageChildServiceObject.memberStatus = "AVAILABLE";
                managechildservicecontroller.manageChildServiceObject.ageOfChild = managechildservicecontroller.dateDiffInDays(new Date(managechildservicecontroller.manageChildServiceObject.dob), new Date());
                if (managechildservicecontroller.manageChildServiceObject.ageOfChild <= 6) {
                    managechildservicecontroller.showMuac = false;
                    managechildservicecontroller.showHeight = false;
                    managechildservicecontroller.showEdema = false;
                } else {
                    managechildservicecontroller.showMuac = true;
                    managechildservicecontroller.showHeight = true;
                    managechildservicecontroller.showEdema = true;
                }
                managechildservicecontroller.additionalInfo = JSON.parse(managechildservicecontroller.manageChildServiceObject.additionalInfo);
                if (managechildservicecontroller.additionalInfo != null && managechildservicecontroller.additionalInfo.lastServiceLongDate != null) {
                    managechildservicecontroller.minDeathDate = moment(managechildservicecontroller.additionalInfo.lastServiceLongDate)
                    if (moment(managechildservicecontroller.additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                        return Promise.reject({ data: { message: 'Child Service Visit for this child has already been done today.' } });
                    }
                    managechildservicecontroller.minServiceDate = moment(managechildservicecontroller.additionalInfo.lastServiceLongDate).add(1, 'days');
                } else {
                    managechildservicecontroller.minServiceDate = managechildservicecontroller.manageChildServiceObject.dob
                    managechildservicecontroller.minDeathDate = managechildservicecontroller.minServiceDate;
                }
                if (managechildservicecontroller.minServiceDate != null && managechildservicecontroller.minServiceDate < (moment().subtract(15, 'days'))) {
                    managechildservicecontroller.minServiceDate = moment().subtract(15, 'days')
                }
                managechildservicecontroller.manageChildServiceObject.dobDisplay = moment(managechildservicecontroller.manageChildServiceObject.dob).format("DD-MM-YYYY");
                return ManageChildServiceDAO.getDueImmunisationsForChild(moment(managechildservicecontroller.manageChildServiceObject.dob).valueOf(), managechildservicecontroller.manageChildServiceObject.immunisationGiven || null);
            }).then((response) => {
                managechildservicecontroller.manageChildServiceObject.dueImmunisations = response;
                let retrieveDataList = [];
                retrieveDataList.push({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: managechildservicecontroller.loggedInUser.id
                    },
                    sequence: 1
                }, {
                    code: 'retrieve_worker_info_by_location_id',
                    parameters: {
                        locationId: managechildservicecontroller.manageChildServiceObject.locationId
                    },
                    sequence: 2
                }, {
                    code: 'retrieve_worker_info_by_location_id',
                    parameters: {
                        locationId: managechildservicecontroller.manageChildServiceObject.areaId
                    },
                    sequence: 3
                }, {
                    code: 'cerebral_palsy_module_check',
                    parameters: {
                        locationId: managechildservicecontroller.manageChildServiceObject.locationId
                    },
                    sequence: 4
                }, {
                    code: 'check_last_cp_status',
                    parameters: {
                        id: Number($stateParams.id)
                    },
                    sequence: 5
                }, {
                    code: 'retrieve_last_cp_questions_list',
                    parameters: {
                        id: Number($stateParams.id)
                    },
                    sequence: 6
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'childDiseaseFhwCs'
                    },
                    sequence: 7
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'childDeathReasonsFhwCs'
                    },
                    sequence: 8
                }, {
                    code: 'fetch_listvalue_detail_from_field',
                    parameters: {
                        field: 'Health Infrastructure Type'
                    },
                    sequence: 9
                });
                return QueryDAO.executeAll(retrieveDataList);
            }).then((response) => {
                managechildservicecontroller.institutes = response[0].result
                if (response[1].result[0].workerDetails != null) {
                    var fhwJson = JSON.parse(response[1].result[0].workerDetails);
                    managechildservicecontroller.manageChildServiceObject.fhwName = fhwJson[0].name
                    managechildservicecontroller.manageChildServiceObject.fhwNumber = fhwJson[0].mobileNumber;
                }
                if (response[2].result[0].workerDetails != null) {
                    var ashaJson = JSON.parse(response[2].result[0].workerDetails);
                    managechildservicecontroller.manageChildServiceObject.ashaName = ashaJson[0].name
                    managechildservicecontroller.manageChildServiceObject.ashaNumber = ashaJson[0].mobileNumber;
                }
                if (response[3].result[0] != null && response[3].result[0].cerebral_palsy_module != null && response[3].result[0].cerebral_palsy_module) {
                    if (response[4].result[0] != null && response[4].result[0].status === 'TREATMENT_COMMENCED') {
                        managechildservicecontroller.showCpQuestions = false;
                    } else {
                        managechildservicecontroller.showCpQuestions = true;
                        if (response[5].result[0] != null) {
                            managechildservicecontroller.cpQuestionsList = response[5].result[0];
                        }else {
                            managechildservicecontroller.isAnyCpQuestionDisplay = true;
                        }
                    }
                }
                managechildservicecontroller.diseasesList = response[6].result;
                managechildservicecontroller.deathReasons = response[7].result;
                managechildservicecontroller.institutionTypes = response[8].result;
                return ManageChildServiceDAO.getLastChildVisit($stateParams.id);
            }).then((response) => {
                if (response != null) {
                    if (response.complementaryFeedingStarted != null && response.complementaryFeedingStarted) {
                        managechildservicecontroller.showComplementaryFeeding = false
                    } else {
                        if (managechildservicecontroller.manageChildServiceObject.ageOfChild <= 6) {
                            managechildservicecontroller.showComplementaryFeeding = false
                        } else {
                            managechildservicecontroller.showComplementaryFeeding = true
                        }
                    }
                } else {
                    managechildservicecontroller.showComplementaryFeeding = false
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.childServiceSearch');
            }).finally(() => {
                Mask.hide();
            });
        }

        managechildservicecontroller.checkForQuestion = (list) => {
            list.forEach(question => {
                if(!managechildservicecontroller.cpQuestionsList[question]){
                    managechildservicecontroller.isAnyCpQuestionDisplay = true;
                }
            });
        }

        managechildservicecontroller.initializeQuestionList = () =>{
            managechildservicecontroller.manageChildServiceObject.cerebralDetails = {};
            if (managechildservicecontroller.cpQuestionsList.avoid_strangers) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.avoidStrangers = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.avoidStrangersCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.climb_updown_stairs) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.climbUpDownStairs = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.climbUpDownStairsCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.drink_from_glass) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.drinkFromGlass = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.drinkFromGlassCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.enjoy_peekaboo) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.enjoyPeekaboo = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.enjoyPeekabooCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.flip_pages) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.flipPages = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.flipPagesCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.hands_in_mouth) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.handsInMouth = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.handsInMouthCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.hold_head_straight) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.holdHeadStraight = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.holdHeadStraightCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.hold_things_with_finger) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.holdThingsWithFinger = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.holdThingsWithFingerCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.kick_ball) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.kickBall = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.kickBallCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.kneel_down) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.kneelDown = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.kneelDownCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.lifts_toys) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.liftToys = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.liftToysCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.like_playing_other_children) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.likePlayingWithOtherChildren = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.likePlayingWithOtherChildrenCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.look_in_direc_of_sound) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookInDirectionOfSound = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookInDirectionOfSoundCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.look_when_name_called) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookWhenNameCalled = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookWhenNameCalledCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.look_when_speak) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookWhenSpeak = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.lookWhenSpeakCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.make_noise_when_speak) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.makeNoiseWhenSpeak = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.makeNoiseWhenSpeakCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.mimic_others) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.mimicOthers = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.mimicOthersCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.responds_on_name_calling) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.respondsOnNameCalling = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.respondsOnNameCallingCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.run_independently) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.runIndependently = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.runIndependentlyCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.sit_without_help) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.sitWithoutHelp = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.sitWithoutHelpCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.speak_simple_words) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.speakSimpleWords = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.speakSimpleWordsCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.speak_two_sentences) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.speakTwoSentences = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.speakTwoSentencesCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.tell_name_of_things) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.tellNameOfThings = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.tellNameOfThingsCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.understand_instructions) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.understandInstructions = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.understandInstructionsCompleted = true
            }
            if (managechildservicecontroller.cpQuestionsList.understand_no) {
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.understandNo = true
                managechildservicecontroller.manageChildServiceObject.cerebralDetails.understandNoCompleted = true
            }
            const age3 = ['hold_head_straight', 'hands_in_mouth', 'look_when_speak', 'make_noise_when_speak', 'look_in_direc_of_sound'];
            const age9 = ['sit_without_help', 'kneel_down', 'avoid_strangers', 'understand_no', 'enjoy_peekaboo', 'responds_on_name_calling', 'lifts_toys'];
            const age15 = ['mimic_others', 'drink_from_glass', 'run_independently', 'hold_things_with_finger', 'look_when_name_called', 'speak_simple_words',
                'understand_instructions'];
            const age24 = ['tell_name_of_things', 'flip_pages', 'kick_ball', 'climb_updown_stairs', 'speak_two_sentences', 'like_playing_other_children'];
            const age = managechildservicecontroller.manageChildServiceObject.ageOfChild;
            if (age >= 3 && age <= 8) {
                managechildservicecontroller.checkForQuestion(age3);
            } else if (age >= 9 && age <= 14) {
                managechildservicecontroller.checkForQuestion(age9);
            } else if (age >= 15 && age <= 23) {
                managechildservicecontroller.checkForQuestion(age15);
            } else if (age >= 24 && age <= 30) {
                managechildservicecontroller.checkForQuestion(age24);
            }
        }

        managechildservicecontroller.calculateSdScore = () => {
            if (managechildservicecontroller.manageChildServiceObject.gender != null) {
                if (managechildservicecontroller.manageChildServiceObject.height != null && managechildservicecontroller.manageChildServiceObject.weight != null) {
                    if (Number.isInteger(managechildservicecontroller.manageChildServiceObject.height)) {
                        Mask.show();
                        ManageChildServiceDAO.retrieveSdScore(managechildservicecontroller.manageChildServiceObject.gender, managechildservicecontroller.manageChildServiceObject.height, managechildservicecontroller.manageChildServiceObject.weight).then((response) => {
                            switch (response.sdScore) {
                                case 'SD4':
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "SD4";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "Less than -4";
                                    break;
                                case 'SD3':
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "SD3";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "-4 to -3";
                                    break;
                                case 'SD2':
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "SD2";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "-3 to -2";
                                    break;
                                case 'SD1':
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "SD1";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "-2 to -1";
                                    break;
                                case 'MEDIAN':
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "MEDIAN";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "MEDIAN";
                                    break;
                                default:
                                    managechildservicecontroller.manageChildServiceObject.sdScore = "NONE";
                                    managechildservicecontroller.manageChildServiceObject.sdScoreDisplay = "NONE";
                                    break;
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        })
                    } else {
                        toaster.pop('error', 'Height not allowed in decimal value');
                        managechildservicecontroller.manageChildServiceObject.height = null;
                    }
                }
            } else {
                toaster.pop('error', 'Gender Not Found');
            }
        }

        managechildservicecontroller.deliveryPlaceChanged = () => {
            managechildservicecontroller.manageChildServiceObject.institute = null;
            if (managechildservicecontroller.manageChildServiceObject.deliveryPlace === 'THISHOSP') {
                if (managechildservicecontroller.institutes.length === 1) {
                    managechildservicecontroller.manageChildServiceObject.institute = managechildservicecontroller.institutes[0];
                } else if (managechildservicecontroller.institutes.length === 0) {
                    toaster.pop('error', 'No health infrastructure assigned');
                }
            }
        }

        managechildservicecontroller.retrieveHospitalsByInstitutionType = () => {
            Mask.show();
            managechildservicecontroller.showOtherInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managechildservicecontroller.manageChildServiceObject.institutionType)
                }
            }).then((res) => {
                managechildservicecontroller.showOtherInstitutes = true;
                managechildservicecontroller.otherInstitutes = res.result;
                var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(managechildservicecontroller.otherInstitutes);
                managechildservicecontroller.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })
        }

        managechildservicecontroller.retrieveDeathHospitalsByInstitutionType = () => {
            Mask.show();
            managechildservicecontroller.showDeathInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managechildservicecontroller.manageChildServiceObject.deathInfrastructureType)
                }
            }).then((res) => {
                managechildservicecontroller.showDeathInstitutes = true;
                managechildservicecontroller.deathInstitutes = res.result;
                managechildservicecontroller.manageChildServiceObject.deathInfrastructureId = null;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })
        }

        managechildservicecontroller.setUsersForInstitute = () => {
            managechildservicecontroller.usersForInstitute = [];
            delete managechildservicecontroller.manageChildServiceObject.deliveryPerson;
            if (managechildservicecontroller.manageChildServiceObject.deliveryPlace === 'HOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managechildservicecontroller.manageChildServiceObject.institute),
                        code: managechildservicecontroller.manageChildServiceObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managechildservicecontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            } else if (managechildservicecontroller.manageChildServiceObject.institute && managechildservicecontroller.manageChildServiceObject.deliveryPlace === 'THISHOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managechildservicecontroller.manageChildServiceObject.institute.id),
                        code: managechildservicecontroller.manageChildServiceObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managechildservicecontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        managechildservicecontroller.immunisationGivenChanged = (immunisation, dob) => {
            managechildservicecontroller.manageChildServiceObject.immunisation[immunisation].date = null;
            Object.keys(managechildservicecontroller.manageChildServiceObject.immunisation).forEach((key) => {
                if (!managechildservicecontroller.manageChildServiceObject.immunisation[key].given) {
                    if (managechildservicecontroller.manageChildServiceObject.immunisationGiven.includes(key)) {
                        let givenImmunisationsMap = managechildservicecontroller.manageChildServiceObject.immunisationGiven.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        managechildservicecontroller.manageChildServiceObject.immunisationGiven = givenImmunisationsMap.join(',');
                    }
                } else if (managechildservicecontroller.manageChildServiceObject.immunisation[key].given && managechildservicecontroller.manageChildServiceObject.immunisation[key].date != null) {
                    var givenImmunisation = managechildservicecontroller.manageChildServiceObject.immunisationGiven;
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
                    ManageChildServiceDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(managechildservicecontroller.manageChildServiceObject.immunisation[key].date).valueOf(), key, givenImmunisation).then((response) => {
                        if (response.result != "" && response.result != null) {
                            managechildservicecontroller.managechildServiceForm[key + 'date'].$setValidity('vaccine', false);
                        } else {
                            managechildservicecontroller.managechildServiceForm[key + 'date'].$setValidity('vaccine', true);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                }
            });
        }

        managechildservicecontroller.setCurrentlyGivenVaccines = (vaccine, date) => {
            if (managechildservicecontroller.manageChildServiceObject.immunisationGiven.includes(vaccine)) {
                var givenImmunisationsMap = managechildservicecontroller.manageChildServiceObject.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((immunisation, index) => {
                    if (immunisation.includes(vaccine)) {
                        givenImmunisationsMap.splice(index, 1);
                    }
                });
                managechildservicecontroller.manageChildServiceObject.immunisationGiven = givenImmunisationsMap.join(',');
            }
            if (managechildservicecontroller.manageChildServiceObject.immunisationGiven == null || managechildservicecontroller.manageChildServiceObject.immunisationGiven == "") {
                managechildservicecontroller.manageChildServiceObject.immunisationGiven = vaccine + "#" + moment(date).format("DD/MM/YYYY");
            } else {
                managechildservicecontroller.manageChildServiceObject.immunisationGiven += "," + vaccine + "#" + moment(date).format("DD/MM/YYYY");
            }
        }

        managechildservicecontroller.checkImmunisationValidation = (dob, givenDate, currentVaccine, givenImmunisations) => {
            if (givenImmunisations == null || givenImmunisations == "") {
                givenImmunisations = "";
            }
            if (givenImmunisations.includes(currentVaccine)) {
                let givenImmunisationsMap = givenImmunisations.split(',');
                givenImmunisationsMap.forEach((immunisation, index) => {
                    if (immunisation.includes(currentVaccine)) {
                        givenImmunisationsMap.splice(index, 1);
                    }
                });
                givenImmunisations = givenImmunisationsMap.join(',');
            }
            Mask.show();
            ManageChildServiceDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(givenDate).valueOf(), currentVaccine, givenImmunisations).then((response) => {
                if (response.result != "" && response.result != null) {
                    managechildservicecontroller.managechildServiceForm[currentVaccine + 'date'].$setValidity('vaccine', false);
                } else {
                    managechildservicecontroller.managechildServiceForm[currentVaccine + 'date'].$setValidity('vaccine', true);
                    managechildservicecontroller.setCurrentlyGivenVaccines(currentVaccine, givenDate);
                }
                Object.keys(managechildservicecontroller.manageChildServiceObject.immunisation).forEach((key) => {
                    let givenImmunisations2 = managechildservicecontroller.manageChildServiceObject.immunisationGiven
                    if (managechildservicecontroller.manageChildServiceObject.immunisation[key].given && managechildservicecontroller.manageChildServiceObject.immunisation[key].date != null) {
                        if (givenImmunisations2.includes(key)) {
                            let givenImmunisationsMap = givenImmunisations2.split(',');
                            givenImmunisationsMap.forEach((immunisation, index) => {
                                if (immunisation.includes(key)) {
                                    givenImmunisationsMap.splice(index, 1);
                                }
                            });
                            givenImmunisations2 = givenImmunisationsMap.join(',');
                        }
                        Mask.show();
                        ManageChildServiceDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(managechildservicecontroller.manageChildServiceObject.immunisation[key].date).valueOf(), key, givenImmunisations2).then((res) => {
                            if (res.result != "" && res.result != null) {
                                managechildservicecontroller.managechildServiceForm[key + 'date'].$setValidity('vaccine', false);
                            } else {
                                managechildservicecontroller.managechildServiceForm[key + 'date'].$setValidity('vaccine', true);
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
        };

        managechildservicecontroller.childAliveChanged = () => {
            managechildservicecontroller.manageChildServiceObject = {
                ...managechildservicecontroller.manageChildServiceObject,
                deathDate: null,
                placeOfDeath: null,
                deathInfrastructureType: null,
                deathInfrastructureId: null,
                deathReason: null,
                weight: null,
                height: null,
                sdScore: null,
                midArmCircumference: null,
                havePedalEdema: null,
                immunisation: null,
                ifaSyrupGiven: null,
                complementaryFeedingStarted: null,
                complementaryFeedingStartPeriod: null,
                dieseases: null,
                otherDiseases: null,
                isTreatementDone: null,
                exclusivelyBreastfeded: null,
                cerebralDetails: null
            }
            if (managechildservicecontroller.manageChildServiceObject.isAlive &&
                managechildservicecontroller.cpQuestionsList) {
                managechildservicecontroller.initializeQuestionList();
            } else {
                managechildservicecontroller.manageChildServiceObject = {
                    ...managechildservicecontroller.manageChildServiceObject,
                    cerebralDetails: null
                }
            }
        }

        managechildservicecontroller.dateDiffInDays = (a, b) => {
            var date1 = moment(a);
            var date2 = moment(b);

            return date2.diff(date1, 'months');
        }

        managechildservicecontroller.goBack = () => {
            window.history.back();
        }

        managechildservicecontroller.init();
    }
    angular.module('imtecho.controllers').controller('ManageChildServiceController', ManageChildServiceController);
})();
