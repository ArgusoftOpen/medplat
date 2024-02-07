(function (angular) {
    function NcdConsultantfollowupScreenController($state, NcdDAO, $filter, toaster,NdhmHipDAO, $uibModal, GeneralUtil, AuthenticateService, Mask, $q, QueryDAO, $window) {
        var ncdfs = this;

        ncdfs.appName = GeneralUtil.getAppName();
        ncdfs.diabetesDetected = false;
        ncdfs.oralDetected = false;
        ncdfs.breastDetected = false;
        ncdfs.cervicalDetected = false;
        ncdfs.keyFn = Object.keys;
        ncdfs.isArray = angular.isArray
        ncdfs.today = new Date();
        var wcForFemales = [
            {
                key: 'LT80',
                value: '80 cm or less'
            }, {
                key: '81TO90',
                value: '81 - 90cm'
            }, {
                key: 'GT90',
                value: 'More than 90 cm'
            }
        ];
        var wcForMales = [
            {
                key: 'LT90',
                value: '90 cm or less'
            }, {
                key: '91TO100',
                value: '91 - 100cm'
            }, {
                key: 'GT100',
                value: 'More than 100 cm'
            }
        ];

        ncdfs.tabs = [
            'initialassessment', 'general', 'oral-main', 'breast-main', 'cervical-main', 'diabetes-main', 'mental-health', 'hypertension-main', 'labtest-main'
        ];
        var init = function () {
            ncdfs.isUserHealthFetch = false
            ncdfs.navigateToTab('initialassessment');
            AuthenticateService.getLoggedInUser().then(function (res) {
                ncdfs.loggedInUser = res.data;
                ncdfs.retrieveLoggedInUserHealthInfra(res.data.id);
                ncdfs.retrieveLoggedInUserFeatureJson();
            })

            NcdDAO.retrieveAllMedicines().then(function (res) {
                ncdfs.medicines = res;
            }, GeneralUtil.showMessageOnApiCallFailure);
            ncdfs.alcoholConsumptions =
                ['Daily or almost daily', 'Weekly', 'Monthly', 'Less than monthly', 'Never']

            if ($state.params.id) {
                // ncdfs.screeningDate = moment();
                ncdfs.retrieveMemberDetails();
            }
            

            NdhmHipDAO.getAllCareContextMasterDetails(parseInt($state.params.id)).then((res) => {
                if (res.length > 0) {
                    ncdfs.healthIdsData = res;
                    ncdfs.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
                    let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
                    ncdfs.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
                }
            }).catch((error) => {
            })
        };

        //max date
        // ncdfs.numberOfDaysToAdd = 6;
        // ncdfs.result = ncdfs.today.setDate(ncdfs.today.getDate() + ncdfs.numberOfDaysToAdd);
        // console.log(new Date(ncdfs.result));

        //back button
        ncdfs.$window = $window;
        ncdfs.goBack = function () {
            $window.history.back();
        };

        //show hypertension_button
        ncdfs.IsVisible = false;
        ncdfs.ShowButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.IsVisible = value == "N";
        };

        //show General_button
        ncdfs.IsVisibleGeneral = false;
        ncdfs.ShowButtonGeneral = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.IsVisibleGeneral = value == "N";
        };

        ncdfs.IsVisibleGeneralOther = false;
        ncdfs.ShowButtonGeneralOther = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.IsVisibleGeneralOther = value == "N";
        };

        //show cervical
        ncdfs.showCervicalOther = false;
        ncdfs.ShowOtherButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.showCervicalOther = value == "Y";
        };

        ncdfs.showCervicalotherFindings = false;
        ncdfs.ShowotherFindingsButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.showCervicalotherFindings = value == "Y";
        };

        ncdfs.showCervicalImage = false;
        ncdfs.ShowCervicalImageFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.showCervicalImage = value == "Y";
        };

        ncdfs.showCervicalPoint = false;
        ncdfs.ShowCervicalPointFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.showCervicalPoint = value == "Y";
        };

        //show breast
        ncdfs.showBreastButton = false;
        ncdfs.ShowBreastButtonFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdfs.showBreastButton = value == "Y";
        };

        ncdfs.showBreastPoint = false;
        ncdfs.showBreastPoints = false;
        ncdfs.ShowBreastPointFun = function (value) {
            if (value == "Y") {
                ncdfs.showBreastPoint = true;
                ncdfs.showBreastPoints = false;
            } else {
                ncdfs.showBreastPoint = false;
                ncdfs.showBreastPoints = true;
            }
        };

        //dikhega
        ncdfs.showOral = true;

        var getAge = function (DOB) {
            var birthDate = new Date(DOB);
            var age = new Date().getFullYear() - birthDate.getFullYear();
            var m = new Date().getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && new Date().getDate() < birthDate.getDate())) {
                age = age - 1;
            }

            return age;
        }
        var setHistory = function () {
            let history = {};
            let familyHistory = {};
            if (ncdfs.member.memberOtherInfoDto.familyHistoryOfStroke) {
                familyHistory['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.familyHistoryOfStroke, 'Stroke', '');

            }
            if (ncdfs.member.memberOtherInfoDto.familyHistoryOfPrematureMi) {
                familyHistory['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.familyHistoryOfPrematureMi, 'Premature Mi', '');

            }
            if (ncdfs.member.memberOtherInfoDto.familyHistoryOfDiabetes) {
                familyHistory['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.familyHistoryOfDiabetes, 'Diabetes', '');

            }
            if (ncdfs.member.memberOtherInfoDto.historyOfHeartAttack) {
                history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.historyOfHeartAttack, 'Heart Attack', '');

            }
            if (ncdfs.member.memberOtherInfoDto.historyOfStroke) {
                history['historyOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.historyOfStroke, 'Stroke', '');

            }

            ncdfs.member.history = history;
            ncdfs.member.familyHistory = familyHistory;
        }

        var setSymptoms = function () {
            let symptoms = {};
            let general = {};
            if (ncdfs.member.memberOtherInfoDto) {
                if (ncdfs.member.memberOtherInfoDto.excessThirst) {
                    general['excessThirst'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.excessThirst, 'Excess Thirst', '');
                }
                if (ncdfs.member.memberOtherInfoDto.excessUrination) {
                    general['excessUrination'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.excessUrination, 'Excess Urination', '');
                }
                if (ncdfs.member.memberOtherInfoDto.excessHunger) {
                    general['excessHunger'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.excessHunger, 'Excess Hunger', '');
                }
                if (ncdfs.member.memberOtherInfoDto.angina) {
                    general['angina'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.angina, 'Angina', '');
                }
                if (ncdfs.member.memberOtherInfoDto.recurrentSkin) {
                    general['recurrentSkin'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.recurrentSkin, 'Recurrent Skin', '');
                }
                if (ncdfs.member.memberOtherInfoDto.delayInHealing) {
                    general['delayInHealing'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.delayInHealing, 'Delay in Wound Healing', '');
                }
                if (ncdfs.member.memberOtherInfoDto.changeInDieteryHabits) {
                    general['changeInDieteryHabits'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.changeInDieteryHabits, 'Dietary Habits Change', '');
                }
                if (ncdfs.member.memberOtherInfoDto.limpness) {
                    general['limpness'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.limpness, 'Limpness', '');
                }
                if (ncdfs.member.memberOtherInfoDto.intermittentClaudication) {
                    general['intermittentClaudication'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.intermittentClaudication, 'Claudication', '');
                }
                if (ncdfs.member.memberOtherInfoDto.significantEdema) {
                    general['significantEdema'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfoDto.significantEdema, 'Significant Edema', '');
                }




            }
            if (ncdfs.member.memberCbacDetail) {
                if (ncdfs.member.memberCbacDetail.lossOfWeight) {
                    general['lossOfWeight'] = $filter('yesOrNo')(ncdfs.member.memberCbacDetail.lossOfWeight, 'Weight Loss', '');
                }
                if (ncdfs.member.memberCbacDetail.shortnessOfBreath) {
                    general['shortnessOfBreath'] = $filter('yesOrNo')(ncdfs.member.memberCbacDetail.shortnessOfBreath, 'Breathlessness', '');
                }

            }

            symptoms.general = general;

            if (ncdfs.member.memberOralDto) {
                let oral = {};
                if (ncdfs.member.memberOralDto.difficultyInOpeningMouth) {
                    oral['difficultyInOpeningMouth'] = $filter('yesOrNo')(ncdfs.member.memberOralDto.difficultyInOpeningMouth, 'Difficulty in Opening Mouth', '');
                }
                if (ncdfs.member.memberOralDto.threeWeeksMouthUlcer) {
                    oral['threeWeeksMouthUlcer'] = $filter('yesOrNo')(ncdfs.member.memberOralDto.threeWeeksMouthUlcer, 'Ulceration', '');
                }
                if (ncdfs.member.memberOralDto.whiteRedPatchOralCavity) {
                    oral['whiteRedPatchOralCavity'] = $filter('yesOrNo')(ncdfs.member.memberOralDto.whiteRedPatchOralCavity, 'Oral Cavity Patch', '');
                }
                if (ncdfs.member.memberOralDto.difficultyInSpicyFood) {
                    oral['difficultyInSpicyFood'] = $filter('yesOrNo')(ncdfs.member.memberOralDto.difficultyInSpicyFood, 'Difficulty Tolerating Spicy Food', '');
                }
                if (ncdfs.member.memberOralDto.voiceChange) {
                    oral['voiceChange'] = $filter('yesOrNo')(ncdfs.member.memberOralDto.voiceChange, 'Sudden Voice Change', '');
                }




                symptoms.oral = oral;
            }

            if (ncdfs.member.memberBreastDto) {
                let breast = {};
                if (ncdfs.member.memberBreastDto.lumpInBreast) {
                    breast['lumpInBreast'] = $filter('yesOrNo')(ncdfs.member.memberBreastDto.lumpInBreast, 'Lump', '');

                }
                if (ncdfs.member.memberBreastDto.anyRetractionOfNipple) {
                    breast['anyRetractionOfNipple'] = $filter('yesOrNo')(ncdfs.member.memberBreastDto.anyRetractionOfNipple, 'Retraction Of Nipple', '');

                }
                if (ncdfs.member.memberBreastDto.dischargeFromNipple) {
                    breast['dischargeFromNipple'] = $filter('yesOrNo')(ncdfs.member.memberBreastDto.dischargeFromNipple, 'Discharge from Nipples', '');
                }


                symptoms.breast = breast;
            }

            if (ncdfs.member.memberCervicalDto) {
                let cervical = {};
                if (ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['excessiveBleedingDuringPeriods'] = $filter('yesOrNo')(ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods, 'Excess Bleeding During Periods', '');
                }
                if (ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['bleedingBetweenPeriods'] = $filter('yesOrNo')(ncdfs.member.memberCervicalDto.bleedingBetweenPeriods, 'Bleeding Between Periods', '');

                }
                if (ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['postmenopausalBleeding'] = $filter('yesOrNo')(ncdfs.member.memberCervicalDto.postmenopausalBleeding, 'Postmensual Bleeding', '');

                }
                if (ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['bleedingDfterIntercourse'] = $filter('yesOrNo')(ncdfs.member.memberCervicalDto.bleedingDfterIntercourse, 'Post Coital Bleeding', '');

                }
                if (ncdfs.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['excessiveSmellingVaginalDischarge'] = $filter('yesOrNo')(ncdfs.member.memberCervicalDto.excessiveSmellingVaginalDischarge, 'Excess Smelling Vaginal Discharge', '');

                }
                symptoms.cervical = cervical;
            }



            ncdfs.member.symptoms = symptoms;
        }

        // var setMedications = function () {
        //     let history = [];
        //     history['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfStroke, 'Stroke (Family History)', '');
        //     history['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfPrematureMi, 'Premature Mi (Family History)', '');
        //     history['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfDiabetes, 'Diabetes (Family History)', '');
        //     history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.historyOfHeartAttack, 'Heart Attack', '');
        //     history['historyOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.historyOfStroke, 'Stroke', '');;
        //     ncdfs.member.history = history;
        // }

        // var setExamination = function () {
        //     let history = [];
        //     history['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfStroke, 'Stroke (Family History)', '');
        //     history['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfPrematureMi, 'Premature Mi (Family History)', '');
        //     history['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.familyHistoryOfDiabetes, 'Diabetes (Family History)', '');
        //     history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.historyOfHeartAttack, 'Heart Attack', '');
        //     history['historyOfStroke'] = $filter('yesOrNo')(ncdfs.member.memberOtherInfo.historyOfStroke, 'Stroke', '');;
        //     ncdfs.member.history = history;
        // }

        var setVitals = function () {
            let vitals = {};
            if (ncdfs.member.memberHypertensionDto && ncdfs.member.memberHypertensionDto.systolicBloodPressure && ncdfs.member.memberHypertensionDto.diastolicBloodPressure) {
                vitals['BP'] = ncdfs.member.memberHypertensionDto.systolicBloodPressure + '/' + ncdfs.member.memberHypertensionDto.diastolicBloodPressure + 'mmHg';
                vitals.sbp = ncdfs.member.memberHypertensionDto.systolicBloodPressure;
                vitals.dbp = ncdfs.member.memberHypertensionDto.diastolicBloodPressure;
            }
            if (ncdfs.member.memberHypertensionDto && ncdfs.member.memberHypertensionDto.heartRate) {
                vitals['HR'] = ncdfs.member.memberHypertensionDto.heartRate;
            }
            if (ncdfs.member.memberDiabetesDto && ncdfs.member.memberDiabetesDto.bloodSugar) {
                vitals['bloodSugar'] = ncdfs.member.memberDiabetesDto.bloodSugar;
            }
            if (ncdfs.member.memberCbacDetail && ncdfs.member.memberCbacDetail.bmi) {
                vitals['BMI'] = ncdfs.member.memberCbacDetail.bmi.toFixed(0);
            }
            ncdfs.member.vitals = vitals;


        }

        ncdfs.saveVitals = function () {

            saveDetails().then(function () {
                ncdfs.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "Vitals saved successfully");
                    setVitals();
                })
                // toaster.pop('success', "Vitals saved successfully");
                // setVitals();
                // ncdfs.retrieveMemberDetails();

                $('#patientHistory').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        ncdfs.saveHistory = function () {


            saveDetails().then(function () {
                ncdfs.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "History saved successfully");
                    setHistory();
                })

                // ncdfs.retrieveMemberDetails();
                $('#symptoms').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        ncdfs.saveSymptoms = function () {

            saveDetails().then(function () {

                ncdfs.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "Symptoms saved successfully");
                    setSymptoms();
                })
                // $('#sympt').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        var setDoneBy = function () {
            if (ncdfs.member.memberBreastDto && !ncdfs.member.memberBreastDto.doneBy) {
                ncdfs.member.memberBreastDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberDiabetesDto && !ncdfs.member.memberDiabetesDto.doneBy) {
                ncdfs.member.memberDiabetesDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberInitialAssessmentDto && !ncdfs.member.memberInitialAssessmentDto.doneBy) {
                ncdfs.member.memberInitialAssessmentDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberMentalHealthDto && !ncdfs.member.memberMentalHealthDto.doneBy) {
                ncdfs.member.memberMentalHealthDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberGeneralDto && !ncdfs.member.memberGeneralDto.doneBy) {
                ncdfs.member.memberGeneralDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberHypertensionDto && !ncdfs.member.memberHypertensionDto.doneBy) {
                ncdfs.member.memberHypertensionDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberCervicalDto && !ncdfs.member.memberCervicalDto.doneBy) {
                ncdfs.member.memberCervicalDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberOralDto && !ncdfs.member.memberOralDto.doneBy) {
                ncdfs.member.memberOralDto.doneBy = "FHW";
            }
            if (ncdfs.member.memberCbacDetail && !ncdfs.member.memberCbacDetail.doneBy) {
                ncdfs.member.memberCbacDetail.doneBy = "ASHA";
            }

        }

        ncdfs.calculateBmi = function () {
            if (ncdfs.member.memberCbacDetail) {
                if (ncdfs.member.memberCbacDetail.height) {
                    let heightInM = ncdfs.member.memberCbacDetail.height / 100;
                    let heightInM2 = heightInM * heightInM;
                    ncdfs.member.memberCbacDetail.bmi = (ncdfs.member.memberCbacDetail.weight / heightInM2).toFixed(0);
                }

            }
        }
        
        ncdfs.calculateBmiForInitial = function () {
            if (ncdfs.member.memberInitialAssessmentDto.height) {
                let heightInM = ncdfs.member.memberInitialAssessmentDto.height / 100;
                let heightInM2 = heightInM * heightInM;
                ncdfs.member.memberInitialAssessmentDto.bmi = (ncdfs.member.memberInitialAssessmentDto.weight / heightInM2).toFixed(0);
            }
        }

        ncdfs.viewCBACReport = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/cbac-report.modal.html',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    cbac: function () {
                        return ncdfs.member.memberCbacDetail;
                    }
                },
                controller: function ($scope, cbac) {
                    $scope.cbac = cbac;
                }
            });
            modalInstance.result.then(function () {
                selectedobj.locationFullName = res.message;
                usercontroller.selectedLocationsId.push(selectedobj);

            }, function () {

            });
        }

        ncdfs.viewNcdEnrollment = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/cbac-report.modal.html',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    cbac: function () {
                        return ncdfs.member.memberCbacDetail;
                    }
                },
                controller: function ($scope, cbac) {
                    $scope.cbac = cbac;
                }
            });
            modalInstance.result.then(function () {
                selectedobj.locationFullName = res.message;
                usercontroller.selectedLocationsId.push(selectedobj);

            }, function () {

            });
        }

        ncdfs.retrieveMemberDetails = function (isSaveAction) {
            Mask.show();
            ncdfs.showHypertensionSubType = false;
            ncdfs.askHypertensionTreatmentQuestion = true;
            ncdfs.showHypertensionMedicines = false;
            ncdfs.showHypertensionReferral = false;

            ncdfs.showInitialAssessmentSubType = false;
            ncdfs.askInitialAssessmentTreatmentQuestion = true;
            ncdfs.showInitialAssessmentMedicines = false;
            ncdfs.showInitialAssessmentReferral = false;

            ncdfs.showGeneralSubType = false;
            ncdfs.askGeneralTreatmentQuestion = true;
            ncdfs.showGeneralMedicines = false;
            ncdfs.showGeneralReferral = false;

            ncdfs.showMentalHealthSubType = false;
            ncdfs.askMentalHealthTreatmentQuestion = true;
            ncdfs.showMentalHealthMedicines = false;
            ncdfs.showMentalHealthReferral = false;

            ncdfs.showDiabetesSubType = false;
            ncdfs.askDiabetesTreatmentQuestion = true;
            ncdfs.showDiabetesMedicines = false;
            ncdfs.showDiabetesReferral = false;

            ncdfs.showOralSubStatus = false;
            ncdfs.showOralDirectStatus = false;
            ncdfs.showOralReferral = false;

            ncdfs.showBreastSubStatus = false;
            ncdfs.showBreastDirectStatus = false;
            ncdfs.showBreastReferral = false;

            ncdfs.showCervicalSubStatus = false;
            ncdfs.showCervicalDirectStatus = false;
            ncdfs.showCervicalReferral = false;

            ncdfs.showAssignedInfrastructuresForHypertension = true;
            ncdfs.showAssignedInfrastructuresForInitialAssessment = true;
            ncdfs.showAssignedInfrastructuresForMentalHealth = true;
            ncdfs.showAssignedInfrastructuresForGeneral = true;
            ncdfs.showAssignedInfrastructuresForDiabetes = true;
            ncdfs.showAssignedInfrastructuresForOral = true;
            ncdfs.showAssignedInfrastructuresForBreast = true;
            ncdfs.showAssignedInfrastructuresForCervical = true;
            return NcdDAO.retrieveDetails($state.params.id).then(function (res) {
                ncdfs.member = {};
                ncdfs.member = res;
                ncdfs.member.basicDetails.age = getAge(ncdfs.member.basicDetails.dob)
                if (ncdfs.member.memberGeneralDto != null) {
                    NcdDAO.retrieveLastRecordForGeneralByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfGeneral = response;
                        ncdfs.member.memberGeneralDto.medicines = ncdfs.lastRecordOfGeneral != null && ncdfs.lastRecordOfGeneral.medicineMasters != null ? ncdfs.lastRecordOfGeneral.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdfs.generalAlreadyFilled = true;
                            ncdfs.member.memberGeneralDto.screeningDate = moment(response.screeningDate)
                            ncdfs.member.memberGeneralDto.symptoms = response.symptoms
                            ncdfs.member.memberGeneralDto.clinicalObservation = response.clinicalObservation
                            ncdfs.member.memberGeneralDto.diagnosis = response.diagnosis
                            ncdfs.member.memberGeneralDto.remarks = response.remarks
                        } else {
                            ncdfs.generalAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberGeneralDto.previousFollowUpDate = ncdfs.member.memberGeneralDto.followUpDate;
                    ncdfs.member.memberGeneralDto.followUpDate = null;
                    ncdfs.member.memberGeneralDto.referralId = ncdfs.member.memberGeneralDto.id
                    switch (ncdfs.member.memberGeneralDto.status) {
                        case 'CONFIRMED':
                            ncdfs.showGeneralSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdfs.showGeneralSubType = true;
                            ncdfs.askGeneralTreatmentQuestion = false;
                            ncdfs.showGeneralMedicines = true;
                            ncdfs.showGeneralReferral = true;
                            break;
                        case 'REFERRED':
                            ncdfs.showGeneralSubType = true;
                            ncdfs.askGeneralTreatmentQuestion = false;
                            ncdfs.showGeneralMedicines = false;
                            ncdfs.showGeneralReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.GeneralForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberMentalHealthDto != null) {
                    NcdDAO.retrieveLastRecordForMentalHealthByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfMentalHealth = response;
                        console.log(ncdfs.lastRecordOfMentalHealth);
                        console.log("ajaj")
                        ncdfs.member.memberMentalHealthDto.medicines = ncdfs.lastRecordOfMentalHealth != null && ncdfs.lastRecordOfMentalHealth.medicineMasters != null ? ncdfs.lastRecordOfMentalHealth.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdfs.MentalHealthAlreadyFilled = true;
                            ncdfs.member.memberMentalHealthDto.screeningDate = moment(response.screeningDate)
                            ncdfs.member.memberMentalHealthDto.talk = response.talk
                            ncdfs.member.memberMentalHealthDto.ownDailyWork = response.ownDailyWork
                            ncdfs.member.memberMentalHealthDto.socialWork = response.socialWork
                            ncdfs.member.memberMentalHealthDto.understanding = response.understanding
                        } else {
                            ncdfs.MentalHealthAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberMentalHealthDto.previousFollowUpDate = ncdfs.member.memberMentalHealthDto.followUpDate;
                    ncdfs.member.memberMentalHealthDto.followUpDate = null;
                    ncdfs.member.memberMentalHealthDto.referralId = ncdfs.member.memberMentalHealthDto.id
                    switch (ncdfs.member.memberMentalHealthDto.status) {
                        case 'CONFIRMED':
                            ncdfs.showMentalHealthSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdfs.showMentalHealthSubType = true;
                            ncdfs.askMentalHealthTreatmentQuestion = false;
                            ncdfs.showMentalHealthMedicines = true;
                            ncdfs.showMentalHealthReferral = true;
                            break;
                        case 'REFERRED':
                            ncdfs.showMentalHealthSubType = true;
                            ncdfs.askMentalHealthTreatmentQuestion = false;
                            ncdfs.showMentalHealthMedicines = false;
                            ncdfs.showMentalHealthReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.MentalHealthForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberInitialAssessmentDto != null) {
                    NcdDAO.retrieveLastRecordForInitialAssessmentByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfInitialAssessment = response;
                        console.log("ye ");
                        console.log(ncdfs.lastRecordOfInitialAssessment);
                        ncdfs.member.memberInitialAssessmentDto.medicines = ncdfs.lastRecordOfInitialAssessment != null && ncdfs.lastRecordOfInitialAssessment.medicineMasters != null ? ncdfs.lastRecordOfInitialAssessment.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdfs.initialAssessmentAlreadyFilled = true;
                            ncdfs.member.memberInitialAssessmentDto.screeningDate = moment(response.screeningDate)
                            ncdfs.member.memberInitialAssessmentDto.height = response.height
                            ncdfs.member.memberInitialAssessmentDto.weight = response.weight
                            ncdfs.member.memberInitialAssessmentDto.waistCircumference = response.waistCircumference
                            ncdfs.member.memberInitialAssessmentDto.bmi = response.bmi
                        } else {
                            ncdfs.initialAssessmentAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    // ncdfs.member.memberInitialAssessmentDto.previousFollowUpDate = ncdfs.member.memberInitialAssessmentDto.followUpDate;
                    // ncdfs.member.memberInitialAssessmentDto.followUpDate = null;
                    ncdfs.member.memberInitialAssessmentDto.referralId = ncdfs.member.memberInitialAssessmentDto.id
                    switch (ncdfs.member.memberInitialAssessmentDto.status) {
                        case 'CONFIRMED':
                            ncdfs.showInitialAssessmentSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdfs.showInitialAssessmentSubType = true;
                            ncdfs.askInitialAssessmentTreatmentQuestion = false;
                            ncdfs.showInitialAssessmentMedicines = true;
                            ncdfs.showInitialAssessmentReferral = true;
                            break;
                        case 'REFERRED':
                            ncdfs.showInitialAssessmentSubType = true;
                            ncdfs.askInitialAssessmentTreatmentQuestion = false;
                            ncdfs.showInitialAssessmentMedicines = false;
                            ncdfs.showInitialAssessmentReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.initialAssessmentForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberHypertensionDto != null) {
                    NcdDAO.retrieveLastRecordForHypertensionByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfHypertension = response[0];
                        console.log(ncdfs.lastRecordOfHypertension)
                        ncdfs.member.memberHypertensionDto.medicines = ncdfs.lastRecordOfHypertension != null && ncdfs.lastRecordOfHypertension.medicineMasters != null ? ncdfs.lastRecordOfHypertension.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response[0].id != null && (response[0].doneBy === 'FHW' || response[0].doneBy === 'MPHW' || response[0].doneBy === 'CHO')) {
                            ncdfs.hypertensionAlreadyFilled = true;
                            ncdfs.member.memberHypertensionDto.screeningDate = moment(response[0].screeningDate)
                            ncdfs.member.memberHypertensionDto.systolicBloodPressure = response[0].systolicBloodPressure
                            ncdfs.member.memberHypertensionDto.diastolicBloodPressure = response[0].diastolicBloodPressure
                            ncdfs.member.memberHypertensionDto.heartRate = response[0].heartRate
                            ncdfs.doesHypertension=response[0].doesSuffering;
                        } else {
                            ncdfs.hypertensionAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberHypertensionDto.previousFollowUpDate = ncdfs.member.memberHypertensionDto.followUpDate;
                    ncdfs.member.memberHypertensionDto.followUpDate = null;
                    ncdfs.member.memberHypertensionDto.referralId = ncdfs.member.memberHypertensionDto.id
                    switch (ncdfs.member.memberHypertensionDto.status) {
                        case 'CONFIRMED':
                            ncdfs.showHypertensionSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdfs.showHypertensionSubType = true;
                            ncdfs.askHypertensionTreatmentQuestion = false;
                            ncdfs.showHypertensionMedicines = true;
                            ncdfs.showHypertensionReferral = true;
                            break;
                        case 'REFERRED':
                            ncdfs.showHypertensionSubType = true;
                            ncdfs.askHypertensionTreatmentQuestion = false;
                            ncdfs.showHypertensionMedicines = false;
                            ncdfs.showHypertensionReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.hypertensionForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberDiabetesDto != null) {
                    NcdDAO.retrieveLastRecordForDiabetesByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfDiabetes = response;
                        ncdfs.member.memberDiabetesDto.medicines = ncdfs.lastRecordOfDiabetes != null && ncdfs.lastRecordOfDiabetes.medicineMasters != null ? ncdfs.lastRecordOfDiabetes.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdfs.diabetesAlreadyFilled = true;
                            ncdfs.member.memberDiabetesDto.screeningDate = moment(response.screeningDate);
                            ncdfs.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar
                            ncdfs.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar
                            ncdfs.member.memberDiabetesDto.bloodSugar = response.bloodSugar
                            ncdfs.member.memberDiabetesDto.dka = response.dka
                            ncdfs.member.memberDiabetesDto.hba1c = response.hba1c
                        } else {
                            ncdfs.diabetesAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberDiabetesDto.previousFollowUpDate = ncdfs.member.memberDiabetesDto.followUpDate;
                    ncdfs.member.memberDiabetesDto.followUpDate = null;
                    ncdfs.member.memberDiabetesDto.referralId = ncdfs.member.memberDiabetesDto.id
                    switch (ncdfs.member.memberDiabetesDto.status) {
                        case 'CONFIRMED':
                            ncdfs.showDiabetesSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdfs.showDiabetesSubType = true;
                            ncdfs.askDiabetesTreatmentQuestion = false;
                            ncdfs.showDiabetesMedicines = true;
                            ncdfs.showDiabetesReferral = true;
                            break;
                        case 'REFERRED':
                            ncdfs.showDiabetesSubType = true;
                            ncdfs.askDiabetesTreatmentQuestion = false;
                            ncdfs.showDiabetesMedicines = false;
                            ncdfs.showDiabetesReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.diabetesForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberOralDto != null) {
                    NcdDAO.retrieveLastRecordForOralByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfOral = response;
                        if (ncdfs.lastRecordOfOral != null) {
                            ncdfs.oralHistoryArray = [];
                            if (ncdfs.lastRecordOfOral.restrictedMouthOpening) {
                                ncdfs.oralHistoryArray.push("Restricted Mouth Opening");
                            }
                            if (ncdfs.lastRecordOfOral.whitePatches) {
                                ncdfs.oralHistoryArray.push("White Patches");
                            }
                            if (ncdfs.lastRecordOfOral.redPatches) {
                                ncdfs.oralHistoryArray.push("Red Patches");
                            }
                            if (ncdfs.lastRecordOfOral.nonHealingUlcers) {
                                ncdfs.oralHistoryArray.push("Non Healing Ulcers");
                            }
                            if (ncdfs.lastRecordOfOral.growthOfRecentOrigins) {
                                ncdfs.oralHistoryArray.push("Growth of recent origins");
                            }
                            ncdfs.oralHistoryDisplay = ncdfs.oralHistoryArray.join();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberOralDto.previousFollowUpDate = ncdfs.member.memberOralDto.followUpDate;
                    ncdfs.member.memberOralDto.followUpDate = null;
                    ncdfs.member.memberOralDto.referralId = ncdfs.member.memberOralDto.id
                    let temp = ncdfs.member.memberOralDto.status;
                    ncdfs.member.memberOralDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdfs.showOralSubStatus = true;
                            ncdfs.showOralReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdfs.showOralDirectStatus = true;
                            ncdfs.showOralReferral = true;
                            ncdfs.member.memberOralDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.oralForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberBreastDto != null) {
                    NcdDAO.retrieveLastRecordForBreastByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfBreast = response;
                        if (ncdfs.lastRecordOfBreast != null) {
                            ncdfs.breastHistoryArray = [];
                            if (ncdfs.lastRecordOfBreast.sizeChange) {
                                ncdfs.breastHistoryArray.push("Change in shape /size of the breast");
                            }
                            if (ncdfs.lastRecordOfBreast.nippleNotOnSameLevel) {
                                ncdfs.breastHistoryArray.push("Nipples not at same level");
                            }
                            if (ncdfs.lastRecordOfBreast.anyRetractionOfNipple) {
                                ncdfs.breastHistoryArray.push("Retraction of nipples");
                            }
                            if (ncdfs.lastRecordOfBreast.lymphadenopathy) {
                                ncdfs.breastHistoryArray.push("Lymphadenopathy");
                            }
                            if (ncdfs.lastRecordOfBreast.dischargeFromNipple) {
                                ncdfs.breastHistoryArray.push("Discharge from nipples");
                            }
                            if (ncdfs.lastRecordOfBreast.visualSkinDimplingRetraction) {
                                ncdfs.breastHistoryArray.push("Skin dimpling/puckering");
                            }
                            if (ncdfs.lastRecordOfBreast.visualLumpInBreast) {
                                ncdfs.breastHistoryArray.push("Lump in breasts");
                            }
                            ncdfs.breastHistoryDisplay = ncdfs.breastHistoryArray.join()
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberBreastDto.previousFollowUpDate = ncdfs.member.memberBreastDto.followUpDate;
                    ncdfs.member.memberBreastDto.followUpDate = null;
                    ncdfs.member.memberBreastDto.referralId = ncdfs.member.memberBreastDto.id
                    let temp = ncdfs.member.memberBreastDto.status;
                    ncdfs.member.memberBreastDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdfs.showBreastSubStatus = true;
                            ncdfs.showBreastReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdfs.showBreastDirectStatus = true;
                            ncdfs.showBreastReferral = true;
                            ncdfs.member.memberBreastDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.breastForm.$setPristine();
                    }
                }
                if (ncdfs.member.memberCervicalDto != null) {
                    NcdDAO.retrieveLastRecordForCervicalByMemberId($state.params.id).then(function (response) {
                        ncdfs.lastRecordOfCervical = response;
                        if (ncdfs.lastRecordOfCervical != null) {
                            ncdfs.cervicalHistoryArray = [];
                            if (ncdfs.lastRecordOfCervical.papsmearTest) {
                                ncdfs.cervicalHistoryArray.push("PAP Smear positive")
                            }
                            if (ncdfs.lastRecordOfCervical.viaTest) {
                                ncdfs.cervicalHistoryArray.push("VIA positive")
                            }
                            ncdfs.cervicalHistoryDisplay = ncdfs.cervicalHistoryArray.join();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdfs.member.memberCervicalDto.previousFollowUpDate = ncdfs.member.memberCervicalDto.followUpDate;
                    ncdfs.member.memberCervicalDto.followUpDate = null;
                    ncdfs.member.memberCervicalDto.referralId = ncdfs.member.memberCervicalDto.id
                    let temp = ncdfs.member.memberCervicalDto.status;
                    ncdfs.member.memberCervicalDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdfs.showCervicalSubStatus = true;
                            ncdfs.showCervicalReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdfs.showCervicalDirectStatus = true;
                            ncdfs.showCervicalReferral = true;
                            ncdfs.member.memberCervicalDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdfs.cervicalForm.$setPristine();
                    }
                }

                if (ncdfs.member.basicDetails.gender == 'M') {
                    ncdfs.wc = wcForMales;
                } else {
                    ncdfs.wc = wcForFemales;
                }
                ncdfs.member.basicDetails.age = getAge(ncdfs.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }
        ncdfs.saveHyperTension = function () {
            ncdfs.hypertensionForm.$setSubmitted();
            if (ncdfs.hypertensionForm.$valid) {
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/confirmation.modal.html',
                    controller: 'ConfirmModalController',
                    windowClass: 'cst-modal',
                    size: 'med',
                    resolve: {
                        message: function () {
                            return "It is recommended that blood pressure is checked atleast 3 times. Are you sure you want to proceed?";
                        }
                    }
                });
                modalInstance.result.then(function () {
                    Mask.show();
                    ncdfs.member.memberHypertensionDto.referredFromInstitute = JSON.parse(ncdfs.member.memberHypertensionDto.referredFromInstitute);
                    ncdfs.member.memberHypertensionDto.referredFromHealthInfrastructureId = ncdfs.member.memberHypertensionDto.referredFromInstitute.id
                    if (!ncdfs.member.memberHypertensionDto.memberId) {
                        ncdfs.member.memberHypertensionDto.memberId = Number($state.params.id);
                    }
                    if (ncdfs.member.memberHypertensionDto.startTreatment || ncdfs.showHypertensionMedicines) {
                        ncdfs.member.memberHypertensionDto.status = 'TREATMENT_STARTED';
                    }
                    if (ncdfs.member.memberHypertensionDto.referralDto != null && ncdfs.member.memberHypertensionDto.referralDto.isReferred) {
                        ncdfs.member.memberHypertensionDto.status = 'REFERRED';
                        ncdfs.member.memberHypertensionDto.reason = ncdfs.member.memberHypertensionDto.referralDto.reason;
                        ncdfs.member.memberHypertensionDto.healthInfraId = ncdfs.member.memberHypertensionDto.referralDto.healthInfraId;
                    }

                    
                    if(ncdfs.doesHypertension) {
                        ncdfs.member.memberHypertensionDto.doesSuffering=false;
                    }
                    else{
                        ncdfs.member.memberHypertensionDto.doesSuffering=true;
                    }
                    NcdDAO.saveHyperTension(ncdfs.member.memberHypertensionDto).then(() => {
                        toaster.pop('success', "Hypertension Details saved successfully");
                        ncdfs.hypertensionForm.$setPristine();
                        if (ncdfs.rights.isShowHIPModal) {
                            ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('HT', ncdfs.member.memberHypertensionDto.screeningDate);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                        ncdfs.retrieveMemberDetails(true);
                    })
                });
            }
        };
        ncdfs.saveGeneral = function () {
            ncdfs.GeneralForm.$setSubmitted();
            if (ncdfs.GeneralForm.$valid) {
                ncdfs.member.memberGeneralDto.referredFromInstitute = JSON.parse(ncdfs.member.memberGeneralDto.referredFromInstitute);
                ncdfs.member.memberGeneralDto.referredFromHealthInfrastructureId = ncdfs.member.memberGeneralDto.referredFromInstitute.id
                if (!ncdfs.member.memberGeneralDto.memberId) {
                    ncdfs.member.memberGeneralDto.memberId = Number($state.params.id);
                }
                // if (ncdfs.member.memberGeneralDto.startTreatment || ncdfs.showDiabetesMedicines) {
                //     ncdfs.member.memberGeneralDto.status = 'TREATMENT_STARTED';
                // }
                // if (ncdfs.member.memberGeneralDto.referralDto != null && ncdfs.member.memberGeneralDto.referralDto.isReferred) {
                //     ncdfs.member.memberGeneralDto.status = 'REFERRED';
                //     ncdfs.member.memberGeneralDto.reason = ncdfs.member.memberGeneralDto.referralDto.reason;
                //     ncdfs.member.memberGeneralDto.healthInfraId = ncdfs.member.memberGeneralDto.referralDto.healthInfraId;

                // }
                if(ncdfs.member.memberGeneralDto.doesRequiredRef){
                    ncdfs.member.memberGeneralDto.status="REFER_NO_VISIT"
                }
                NcdDAO.saveGeneral(ncdfs.member.memberGeneralDto).then(function () {
                    toaster.pop('success', "General Details saved successfully");
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('G', ncdfs.member.memberGeneralDto.screeningDate);
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.GeneralForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdfs.saveInitialAssessment = function () {
            // ncdfs.initialAssessmentForm.$setSubmitted();
            if (ncdfs.initialAssessmentForm.$valid) {
                ncdfs.member.memberInitialAssessmentDto.referredFromInstitute = JSON.parse(ncdfs.member.memberInitialAssessmentDto.referredFromInstitute);
                ncdfs.member.memberInitialAssessmentDto.referredFromHealthInfrastructureId = ncdfs.member.memberInitialAssessmentDto.referredFromInstitute.id
                if (!ncdfs.member.memberInitialAssessmentDto.memberId) {
                    ncdfs.member.memberInitialAssessmentDto.memberId = Number($state.params.id);
                }
                if (ncdfs.member.memberInitialAssessmentDto.startTreatment || ncdfs.showDiabetesMedicines) {
                    ncdfs.member.memberInitialAssessmentDto.status = 'TREATMENT_STARTED';
                }
                if (ncdfs.member.memberInitialAssessmentDto.referralDto != null && ncdfs.member.memberInitialAssessmentDto.referralDto.isReferred) {
                    ncdfs.member.memberInitialAssessmentDto.status = 'REFERRED';
                    ncdfs.member.memberInitialAssessmentDto.reason = ncdfs.member.memberInitialAssessmentDto.referralDto.reason;
                    ncdfs.member.memberInitialAssessmentDto.healthInfraId = ncdfs.member.memberInitialAssessmentDto.referralDto.healthInfraId;

                }
                if (ncdfs.member.memberInitialAssessmentDto.height <=200 && ncdfs.member.memberInitialAssessmentDto.height >=15 &&
                    ncdfs.member.memberInitialAssessmentDto.weight <= 150 && ncdfs.member.memberInitialAssessmentDto.weight >=0.5 &&
                    ncdfs.member.memberInitialAssessmentDto.waistCircumference <=200 && ncdfs.member.memberInitialAssessmentDto.waistCircumference>=15 ) {
                    
                        NcdDAO.saveInitialAssessment(ncdfs.member.memberInitialAssessmentDto).then(function () {
                            toaster.pop('success', "InitialAssessment Details saved successfully");
                            if (ncdfs.rights.isShowHIPModal) {
                                ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('IA', ncdfs.member.memberInitialAssessmentDto.screeningDate);
                            }
                            ncdfs.retrieveMemberDetails(true);
                            ncdfs.initialAssessmentForm.$setPristine();
                        }, GeneralUtil.showMessageOnApiCallFailure)

                } else {
                    toaster.pop('Error', "Height should (15-200 cms), Weight should (0.5-150 kgs), Waist circumference should (15-200 cms)");
                }
                
            }
        };
        ncdfs.saveMentalHealth = function () {
            ncdfs.MentalHealthForm.$setSubmitted();
            if (ncdfs.MentalHealthForm.$valid) {
                ncdfs.member.memberMentalHealthDto.referredFromInstitute = JSON.parse(ncdfs.member.memberMentalHealthDto.referredFromInstitute);
                ncdfs.member.memberMentalHealthDto.referredFromHealthInfrastructureId = ncdfs.member.memberMentalHealthDto.referredFromInstitute.id
                if (!ncdfs.member.memberMentalHealthDto.memberId) {
                    ncdfs.member.memberMentalHealthDto.memberId = Number($state.params.id);
                }
                if (ncdfs.member.memberMentalHealthDto.startTreatment || ncdfs.showDiabetesMedicines) {
                    ncdfs.member.memberMentalHealthDto.status = 'TREATMENT_STARTED';
                }
                if (ncdfs.member.memberMentalHealthDto.referralDto != null && ncdfs.member.memberMentalHealthDto.referralDto.isReferred) {
                    ncdfs.member.memberMentalHealthDto.status = 'REFERRED';
                    ncdfs.member.memberMentalHealthDto.reason = ncdfs.member.memberMentalHealthDto.referralDto.reason;
                    ncdfs.member.memberMentalHealthDto.healthInfraId = ncdfs.member.memberMentalHealthDto.referralDto.healthInfraId;

                }
                NcdDAO.saveMentalHealth(ncdfs.member.memberMentalHealthDto).then(function () {
                    toaster.pop('success', "MentalHealth Details saved successfully");
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('MH', ncdfs.member.memberMentalHealthDto.screeningDate);
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.MentalHealthForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdfs.saveDiabetes = function () {
            ncdfs.diabetesForm.$setSubmitted();
            if (ncdfs.diabetesForm.$valid) {
                ncdfs.member.memberDiabetesDto.referredFromInstitute = JSON.parse(ncdfs.member.memberDiabetesDto.referredFromInstitute);
                ncdfs.member.memberDiabetesDto.referredFromHealthInfrastructureId = ncdfs.member.memberDiabetesDto.referredFromInstitute.id
                if (!ncdfs.member.memberDiabetesDto.memberId) {
                    ncdfs.member.memberDiabetesDto.memberId = Number($state.params.id);
                }
                if (ncdfs.member.memberDiabetesDto.startTreatment || ncdfs.showDiabetesMedicines) {
                    ncdfs.member.memberDiabetesDto.status = 'TREATMENT_STARTED';
                }
                if (ncdfs.member.memberDiabetesDto.referralDto != null && ncdfs.member.memberDiabetesDto.referralDto.isReferred) {
                    ncdfs.member.memberDiabetesDto.status = 'REFERRED';
                    ncdfs.member.memberDiabetesDto.reason = ncdfs.member.memberDiabetesDto.referralDto.reason;
                    ncdfs.member.memberDiabetesDto.healthInfraId = ncdfs.member.memberDiabetesDto.referralDto.healthInfraId;

                }
                NcdDAO.saveDiabetes(ncdfs.member.memberDiabetesDto).then(function () {
                    toaster.pop('success', "Diabetes Details saved successfully");
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('D', ncdfs.member.memberDiabetesDto.screeningDate);
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.diabetesForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdfs.saveCervical = function () {
            if (ncdfs.cervicalForm.$valid) {
                ncdfs.member.memberCervicalDto.referredFromInstitute = JSON.parse(ncdfs.member.memberCervicalDto.referredFromInstitute);
                ncdfs.member.memberCervicalDto.referredFromHealthInfrastructureId = ncdfs.member.memberCervicalDto.referredFromInstitute.id
                if (!ncdfs.member.memberCervicalDto.memberId) {
                    ncdfs.member.memberCervicalDto.memberId = Number($state.params.id);
                }
                setCervicalVisualDto();
                if (ncdfs.member.memberCervicalDto.referralDto != null && ncdfs.member.memberCervicalDto.referralDto.isReferred) {
                    ncdfs.member.memberCervicalDto.reason = ncdfs.member.memberCervicalDto.referralDto.reason;
                    ncdfs.member.memberCervicalDto.healthInfraId = ncdfs.member.memberCervicalDto.referralDto.healthInfraId;
                }
                if (ncdfs.member.memberCervicalDto.referralDto != null && ncdfs.member.memberCervicalDto.referralDto.isReferred && ncdfs.member.memberCervicalDto.status != 'SUSPECTED' && ncdfs.member.memberCervicalDto.status != 'CONFIRMATION_PENDING') {
                    ncdfs.member.memberCervicalDto.status = 'REFERRED';
                }
                NcdDAO.saveCervical(ncdfs.member.memberCervicalDto).then(function () {
                    toaster.pop('success', "Cervical Details saved successfully");
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('C', ncdfs.member.memberCervicalDto.screeningDate);
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.cervicalForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdfs.saveOral = function () {
            if (ncdfs.oralForm.$valid) {
                ncdfs.member.memberOralDto.referredFromInstitute = JSON.parse(ncdfs.member.memberOralDto.referredFromInstitute);
                ncdfs.member.memberOralDto.referredFromHealthInfrastructureId = ncdfs.member.memberOralDto.referredFromInstitute.id
                if (!ncdfs.member.memberOralDto.memberId) {
                    ncdfs.member.memberOralDto.memberId = Number($state.params.id);
                }
                setOralTests();
                if (ncdfs.member.memberOralDto.referralDto != null && ncdfs.member.memberOralDto.referralDto.isReferred) {
                    ncdfs.member.memberOralDto.reason = ncdfs.member.memberOralDto.referralDto.reason;
                    ncdfs.member.memberOralDto.healthInfraId = ncdfs.member.memberOralDto.referralDto.healthInfraId;
                }
                if (ncdfs.member.memberOralDto.referralDto != null && ncdfs.member.memberOralDto.referralDto.isReferred && ncdfs.member.memberOralDto.status != 'SUSPECTED' && ncdfs.member.memberOralDto.status != 'CONFIRMATION_PENDING') {
                    ncdfs.member.memberOralDto.status = 'REFERRED';
                }
                NcdDAO.saveOral(ncdfs.member.memberOralDto).then(function () {
                    toaster.pop('success', "Oral Details saved successfully");
                    ncdfs.showOral = false;
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('O', ncdfs.member.memberOralDto.screeningDate)
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.oralForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdfs.saveBreast = function () {
            ncdfs.breastForm.$setSubmitted();
            if (ncdfs.breastForm.$valid) {
                ncdfs.member.memberBreastDto.referredFromInstitute = JSON.parse(ncdfs.member.memberBreastDto.referredFromInstitute);
                ncdfs.member.memberBreastDto.referredFromHealthInfrastructureId = ncdfs.member.memberBreastDto.referredFromInstitute.id
                if (!ncdfs.member.memberBreastDto.memberId) {
                    ncdfs.member.memberBreastDto.memberId = Number($state.params.id);
                }
                setBreastVisualDto();
                if (ncdfs.member.memberBreastDto.referralDto != null && ncdfs.member.memberBreastDto.referralDto.isReferred) {
                    ncdfs.member.memberBreastDto.reason = ncdfs.member.memberBreastDto.referralDto.reason;
                    ncdfs.member.memberBreastDto.healthInfraId = ncdfs.member.memberBreastDto.referralDto.healthInfraId;
                }
                if (ncdfs.member.memberBreastDto.referralDto != null && ncdfs.member.memberBreastDto.referralDto.isReferred && ncdfs.member.memberBreastDto.status != 'SUSPECTED' && ncdfs.member.memberBreastDto.status != 'CONFIRMATION_PENDING') {
                    ncdfs.member.memberBreastDto.status = 'REFERRED';
                }
                NcdDAO.saveBreast(ncdfs.member.memberBreastDto).then(function () {
                    toaster.pop('success', "Breast Details saved successfully");
                    if (ncdfs.rights.isShowHIPModal) {
                        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode('B', ncdfs.member.memberBreastDto.screeningDate)
                    }
                    ncdfs.retrieveMemberDetails(true);
                    ncdfs.breastForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ncdfs.getMemberReferralIdByMemberIdAndDiseaseCode = (diseaseCode, screeningDate) => {
            QueryDAO.execute({
                code: 'get_member_referral_id_by_member_id_and_disease_code',
                parameters: {
                    member_id: ncdfs.member.basicDetails.id,
                    disease_code: diseaseCode
                }
            }).then(function (res) {
                ncdfs.saveHealthIdDetails(screeningDate, res.result[0].id);
            })
        }

        ncdfs.saveHealthIdDetails = (screeningDate, serviceId) => {
            let memberObj = {
                memberId: ncdfs.member.basicDetails.id,
                mobileNumber: ncdfs.member.basicDetails.mobileNumber,
                name: ncdfs.member.basicDetails.firstName + " " + ncdfs.member.basicDetails.lastName,
                preferredHealthId: ncdfs.prefferedHealthId,
                healthIdsData: ncdfs.healthIdsData
            }
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/ndhm/hip/views/ndhm.consent.request.modal.html',
                controller: 'ndhmConsentRequestModalController',
                controllerAs: 'ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    dataForConsentRequest: () => {
                        return {
                            title: "Link NCD details To ABHA Address",
                            memberObj: memberObj,
                            consentRecord: "(NCD " + $filter('date')(screeningDate, "dd-MM-yyyy") + ")",
                            serviceType: "NCD_DIAGNOSTIC_REPORT",
                            serviceId: serviceId,
                            isTokenGenerate: true,
                            preferredHealthId: ncdfs.prefferedHealthId || null,
                            healthIdsData: ncdfs.healthIdsData || [],
                            careContextName: "Non-communicable diseases"
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('techo.ncd.memberdetails');
            }, () => {
            });
        }
        var saveDetails = function () {
            if (!ncdfs.member.memberOtherInfoDto) {
                ncdfs.member.memberOtherInfoDto = { doneBy: 'FHW' };
            }
            ncdfs.member.memberOtherInfoDto.isMoScreeningDone = true;
            let todayDate = new Date();

            todayDate.setHours(0, 0, 0, 0);
            ncdfs.member.memberOtherInfoDto.doneOn = todayDate;
            setDoneBy();
            return NcdDAO.save(ncdfs.member);
        };

        ncdfs.setAllOralTest = function () {
            ncdfs.subMenuSelected = 'all';
            if (!ncdfs.member.memberOralDto.displayList) {
                ncdfs.member.memberOralDto.displayList = {};
            }
            ncdfs.member.memberOralDto.displayList['all'] = {};
            ncdfs.member.memberOralDto[ncdfs.subMenuSelected] = {};
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];
            let keys = _.keys(ncdfs.member.memberOralDto.oralList);
            ncdfs.member.memberOralDto.oralList['all'] = {}
            _.each(keys, function (keyTest) {
                if (keyTest != 'all') {
                    _.each(points, function (point) {
                        if (ncdfs.member.memberOralDto.oralList[keyTest][point]) {
                            if (!ncdfs.member.memberOralDto.displayList['all'][point]) {
                                ncdfs.member.memberOralDto.displayList['all'][point] = []
                            }
                            ncdfs.member.memberOralDto.oralList['all'][point] = true;
                            let stringToBeReplaced = keyTest.replace(/([A-Z])/g, ' $1').replace(/^./, function (str) { return str.toUpperCase() });
                            ncdfs.member.memberOralDto.displayList['all'][point].push(stringToBeReplaced);
                        }
                    });
                }


            })
        };
        ncdfs.setDisplayList = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];
            if (ncdfs.subMenuSelected != 'all') {
                if (!ncdfs.member.memberOralDto.displayList) {
                    ncdfs.member.memberOralDto.displayList = {};
                }
                ncdfs.member.memberOralDto.displayList[ncdfs.subMenuSelected] = {};
                _.each(points, function (point) {
                    if (ncdfs.member.memberOralDto.oralList[ncdfs.subMenuSelected][point]) {
                        if (!ncdfs.member.memberOralDto.displayList[ncdfs.subMenuSelected][point]) {
                            ncdfs.member.memberOralDto.displayList[ncdfs.subMenuSelected][point] = []
                        }
                        let stringToBeReplaced = ncdfs.subMenuSelected.replace(/([A-Z])/g, ' $1').replace(/^./, function (str) { return str.toUpperCase() });
                        ncdfs.member.memberOralDto.displayList[ncdfs.subMenuSelected][point].push(stringToBeReplaced);
                    }

                })
            }

        }

        ncdfs.saveComplaint = function () {
            if (ncdfs.member.complaint) {
                NcdDAO.saveComplaint({ complaint: ncdfs.member.complaint, memberId: ncdfs.member.id }).then(function (res) {
                    toaster.pop('success', "Complaint/Diagnosis Saved Successfully");
                    if (!ncdfs.member.complaints) {
                        ncdfs.member.complaints = [];
                    }
                    ncdfs.member.complaints.push({ complaint: angular.copy(ncdfs.member.complaint), doneOn: new Date(), doneBy: ncdfs.loggedInUser.name });
                    ncdfs.member.complaint = '';
                })
            }


        };
        var setOralTests = function () {
            _.each(ncdfs.member.memberOralDto.oralList, function (value, key) {
                ncdfs.member.memberOralDto[key] = [];
                _.each(value, function (v, k) {
                    if (value[k]) {
                        ncdfs.member.memberOralDto[key].push(k);
                    }
                })
                ncdfs.member.memberOralDto[key] = ncdfs.member.memberOralDto[key].join();
            })
        };
        var setOralTestsForDisplay = function () {
            if (ncdfs.member.memberOralDto) {
                ncdfs.member.memberOralDto.displayList = {};
                ncdfs.member.memberOralDto.oralList = {};
                if (ncdfs.member.memberOralDto.whitePatches) {
                    let res = ncdfs.member.memberOralDto.whitePatches.split(",");
                    ncdfs.member.memberOralDto.oralList.whitePatches = {};

                    ncdfs.member.memberOralDto.displayList.whitePatches = {}
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.whitePatches[point] = true;
                        ncdfs.member.memberOralDto.displayList.whitePatches[point] = ["White Patches"];
                    })

                }
                if (ncdfs.member.memberOralDto.redPatches) {
                    let res = ncdfs.member.memberOralDto.redPatches.split(",");
                    ncdfs.member.memberOralDto.oralList.redPatches = {}
                    ncdfs.member.memberOralDto.displayList.redPatches = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.redPatches[point] = true;
                        ncdfs.member.memberOralDto.displayList.redPatches[point] = ["Red Patches"];
                    })
                }
                if (ncdfs.member.memberOralDto.nonHealingUlcers) {
                    let res = ncdfs.member.memberOralDto.nonHealingUlcers.split(",");
                    ncdfs.member.memberOralDto.oralList.nonHealingUlcers = {};

                    ncdfs.member.memberOralDto.displayList.nonHealingUlcers = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.nonHealingUlcers[point] = true;
                        ncdfs.member.memberOralDto.displayList.nonHealingUlcers[point] = ["Non Healing Ulcers"];
                    })
                }
                if (ncdfs.member.memberOralDto.growthOfRecentOrigins) {
                    let res = ncdfs.member.memberOralDto.growthOfRecentOrigins.split(",");
                    ncdfs.member.memberOralDto.oralList.growthOfRecentOrigins = {}

                    ncdfs.member.memberOralDto.displayList.growthOfRecentOrigins = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.growthOfRecentOrigins[point] = true;
                        ncdfs.member.memberOralDto.displayList.growthOfRecentOrigins[point] = ["Growth Of Recent Origins"];
                    })
                }
                if (ncdfs.member.memberOralDto.lichenPlanus) {
                    let res = ncdfs.member.memberOralDto.lichenPlanus.split(",");
                    ncdfs.member.memberOralDto.oralList.lichenPlanus = {}

                    ncdfs.member.memberOralDto.displayList.lichenPlanus = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.lichenPlanus[point] = true;
                        ncdfs.member.memberOralDto.displayList.lichenPlanus[point] = ["Lichen Planus"];
                    })
                }
                if (ncdfs.member.memberOralDto.smokersPalate) {
                    let res = ncdfs.member.memberOralDto.smokersPalate.split(",");
                    ncdfs.member.memberOralDto.oralList.smokersPalate = {}

                    ncdfs.member.memberOralDto.displayList.smokersPalate = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.smokersPalate[point] = true;
                        ncdfs.member.memberOralDto.displayList.smokersPalate[point] = ["Smokers palate"];
                    })
                }
                if (ncdfs.member.memberOralDto.submucousFibrosis) {
                    let res = ncdfs.member.memberOralDto.submucousFibrosis.split(",");
                    ncdfs.member.memberOralDto.oralList.submucousFibrosis = {}

                    ncdfs.member.memberOralDto.displayList.submucousFibrosis = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.submucousFibrosis[point] = true;
                        ncdfs.member.memberOralDto.displayList.submucousFibrosis[point] = ["Submucous fibrosis"];
                    })
                }
                if (ncdfs.member.memberOralDto.restrictedMouthOpening) {
                    let res = ncdfs.member.memberOralDto.restrictedMouthOpening.split(",");
                    ncdfs.member.memberOralDto.oralList.restrictedMouthOpening = {};

                    ncdfs.member.memberOralDto.displayList.restrictedMouthOpening = [];
                    _.each(res, function (point) {
                        ncdfs.member.memberOralDto.oralList.restrictedMouthOpening[point] = true;
                        ncdfs.member.memberOralDto.displayList.restrictedMouthOpening[point] = ["Restricted Mouth Opening"];
                    })
                }


            }
        }
        var setBreastVisualDto = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26];
            let keys = _.keys(ncdfs.member.memberBreastDto.breastList)
            _.each(keys, function (keyTest) {
                ncdfs.member.memberBreastDto[keyTest] = [];
                _.each(points, function (point) {

                    if (ncdfs.member.memberBreastDto.breastList[keyTest][point]) {

                        ncdfs.member.memberBreastDto[keyTest].push(point);

                    }
                });
                ncdfs.member.memberBreastDto[keyTest] = ncdfs.member.memberBreastDto[keyTest].join(',');

            })

        };
        var setBreastVisual = function () {
            if (ncdfs.member.memberBreastDto) {
                ncdfs.member.memberBreastDto.breastList = {};
                if (ncdfs.member.memberBreastDto.visualSkinDimplingRetraction) {
                    let res = ncdfs.member.memberBreastDto.visualSkinDimplingRetraction.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualSkinDimplingRetraction = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualSkinDimplingRetraction[point] = true;

                    })
                }
                if (ncdfs.member.memberBreastDto.visualNippleRetractionDistortion) {
                    let res = ncdfs.member.memberBreastDto.visualNippleRetractionDistortion.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualNippleRetractionDistortion = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualNippleRetractionDistortion[point] = true;

                    })
                }
                if (ncdfs.member.memberBreastDto.visualDischargeFromNipple) {
                    let res = ncdfs.member.memberBreastDto.visualDischargeFromNipple.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualDischargeFromNipple = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualDischargeFromNipple[point] = true;

                    })
                }
                if (ncdfs.member.memberBreastDto.visualUlceration) {
                    let res = ncdfs.member.memberBreastDto.visualUlceration.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualUlceration = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualUlceration[point] = true;

                    })
                }
                if (ncdfs.member.memberBreastDto.visualSkinRetraction) {
                    let res = ncdfs.member.memberBreastDto.visualSkinRetraction.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualSkinRetraction = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualSkinRetraction[point] = true;

                    })
                }
                if (ncdfs.member.memberBreastDto.visualLumpInBreast) {
                    let res = ncdfs.member.memberBreastDto.visualLumpInBreast.split(",");
                    ncdfs.member.memberBreastDto.breastList.visualLumpInBreast = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberBreastDto.breastList.visualLumpInBreast[point] = true;

                    })
                }
            }
        };

        // var setCervicalVisualDto = function () {
        //     let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        //     if (ncdfs.member.memberCervicalDto.viaExamPoints !== 'negative') {
        //         ncdfs.member.memberCervicalDto.viaExamPoints = [];
        //         _.each(points, function (point) {
        //             if (!ncdfs.member.memberCervicalDto.examPoints) {
        //                 ncdfs.member.memberCervicalDto.examPoints = [];
        //             }
        //             if (ncdfs.member.memberCervicalDto.examPoints[point]) {
        //                 ncdfs.member.memberCervicalDto.viaExamPoints.push(point);
        //             }
        //         });
        //         ncdfs.member.memberCervicalDto.viaExamPoints = ncdfs.member.memberCervicalDto.viaExamPoints.join(',');
        //     } else {
        //         ncdfs.member.memberCervicalDto.viaExamPoints = '';
        //     }
        // };



        var setCervicalVisualDto = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
            let keys = _.keys(ncdfs.member.memberCervicalDto.examPoints)
            _.each(keys, function (keyTest) {
                ncdfs.member.memberCervicalDto[keyTest] = [];
                _.each(points, function (point) {

                    if (ncdfs.member.memberCervicalDto.examPoints[keyTest][point]) {
                        ncdfs.member.memberCervicalDto[keyTest].push(point);
                    }
                });
                ncdfs.member.memberCervicalDto[keyTest] = ncdfs.member.memberCervicalDto[keyTest].join(',');
            })
        };



        var setCervicalVisual = function () {
            if (ncdfs.member.memberCervicalDto) {
                ncdfs.member.memberCervicalDto.examPoints = {};
                // if (ncdfs.member.memberCervicalDto.viaExamPoints !== 'negative') {
                if (ncdfs.member.memberCervicalDto.viaExamPoints) {
                    let res = ncdfs.member.memberCervicalDto.viaExamPoints.split(",");
                    ncdfs.member.memberCervicalDto.examPoints.viaExamPoints = {};
                    _.each(res, function (point) {
                        ncdfs.member.memberCervicalDto.examPoints[point] = true;
                    })
                }
                // }
            }
        };

        ncdfs.navigateToTab = function (id) {
            if (id == "oral-main") {
                ncdfs.subMenuSelected = 'whitePatches'
                ncdfs.oralForm.$setPristine();
            } else if (id == "breast-main") {
                ncdfs.breastForm.$setPristine();
            } else if (id == "cervical-main") {
                ncdfs.cervicalForm.$setPristine();
            }
            $('#' + id).tab('show');
        };
        ncdfs.retrieveComplaints = function () {
            Mask.show();
            NcdDAO.retrieveComplaintsByMemberId($state.params.id).then(function (res) {
                ncdfs.member.complaints = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });

        };
        ncdfs.saveDiagnosis = function (diagnosisDto, diseaseCode, idToNavigate) {
            Mask.show();
            diagnosisDto.diseaseCode = diseaseCode;
            diagnosisDto.memberId = Number($state.params.id);
            switch (diseaseCode) {

                case 'HT':
                    ncdfs.member.memberHypertensionDto.diagnosisDto.readings = ncdfs.member.memberHypertensionDto.systolicBloodPressure + '/' + ncdfs.member.memberHypertensionDto.diastolicBloodPressure;
                    break;
                case 'D':
                    ncdfs.member.memberDiabetesDto.diagnosisDto.readings = 'Fasting: ' + ncdfs.member.memberDiabetesDto.fastingBloodSugar + ' mg/dl Post Prandial: ' + ncdfs.member.memberDiabetesDto.postPrandialBloodSugar + ' mg/dl';
                    break;
            }
            NcdDAO.saveDiagnosis(diagnosisDto).then(function (res) {
                toaster.pop('success', 'Diagnosis Saved Successfully!');
                ncdfs.retrieveTreatmentHistory(diseaseCode);
                switch (diseaseCode) {
                    case 'IA':
                        ncdfs.member.memberInitialAssessmentDto.diagnosisDto = {};
                        break;
                    case 'G':
                        ncdfs.member.memberGeneralDto.diagnosisDto = {};
                        break;
                    case 'MH':
                        ncdfs.member.memberMentalHealthDto.diagnosisDto = {};
                        break;
                    case 'HT':
                        ncdfs.member.memberHypertensionDto.diagnosisDto = {};
                        break;
                    case 'D':
                        ncdfs.member.memberDiabetesDto.diagnosisDto = {};
                        break;
                    case 'B':
                        ncdfs.member.memberBreastDto.diagnosisDto = {};
                        break;
                    case 'C':
                        ncdfs.member.memberCervicalDto.diagnosisDto = {};
                        break;
                    case 'O':
                        ncdfs.member.memberOralDto.diagnosisDto = {};
                        break;
                }
                ncdfs.navigateToTab(idToNavigate)
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });

        };

        const _convertConstToValue = function (constant) {
            switch (constant) {
                case 'MOBILE_REFERRED':
                    return 'Referred from mobile';
                case 'NO_ABNORMALITY':
                    return 'No abnormality';
                case 'SUSPECTED':
                    return 'Suspected';
                case 'CONFIRMED':
                    return 'Confirmed';
                case 'TREATMENT_STARTED':
                    return 'Treatment started';
                case 'REFERRED':
                    return 'Referred to other facility';
                default:
                    return constant;
            }
        };

        ncdfs.retrieveTreatmentHistory = function (diseaseCode) {
            return NcdDAO.retrieveTreatmentHistory(Number($state.params.id), diseaseCode).then(function (res) {
                switch (diseaseCode) {
                    case 'IA':
                        if (!ncdfs.member.memberInitialAssessmentDto) {
                            ncdfs.member.memberInitialAssessmentDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberInitialAssessmentDto.treatmentHistory = res;
                        break;
                    case 'MH':
                        if (!ncdfs.member.memberMentalHealthDto) {
                            ncdfs.member.memberMentalHealthDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberMentalHealthDto.treatmentHistory = res;
                        break;
                    case 'G':
                        if (!ncdfs.member.memberGeneralDto) {
                            ncdfs.member.memberGeneralDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberGeneralDto.treatmentHistory = res;
                        break;
                    case 'HT':
                        if (!ncdfs.member.memberHypertensionDto) {
                            ncdfs.member.memberHypertensionDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberHypertensionDto.treatmentHistory = res;
                        break;
                    case 'D':
                        if (!ncdfs.member.memberDiabetesDto) {
                            ncdfs.member.memberDiabetesDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberDiabetesDto.treatmentHistory = res;
                        break;
                    case 'O':
                        if (!ncdfs.member.memberOralDto) {
                            ncdfs.member.memberOralDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberOralDto.treatmentHistory = res;
                        break;
                    case 'B':
                        if (!ncdfs.member.memberBreastDto) {
                            ncdfs.member.memberBreastDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberBreastDto.treatmentHistory = res;
                        break;
                    case 'C':
                        if (!ncdfs.member.memberCervicalDto) {
                            ncdfs.member.memberCervicalDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdfs.member.memberCervicalDto.treatmentHistory = res;
                        break;
                }
            })
        };

        ncdfs.setDiagnosis = function (diseaseCode) {
            if (diseaseCode == 'HT') {
                if (ncdfs.member.memberHypertensionDto) {
                    if (!ncdfs.member.memberHypertensionDto.diagnosisDto) {
                        ncdfs.member.memberHypertensionDto.diagnosisDto = {};
                    }
                    // Old logic based on set diagnosis which having textarea to fill
                    /* if ((ncdfs.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 90) && ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 120 && ncdfs.member.memberHypertensionDto.systolicBloodPressure < 140) || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure >= 80 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 90))) {
                        ncdfs.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Pre-Hypertension';
                    } else if ((ncdfs.member.memberHypertensionDto.systolicBloodPressure < 160 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 100) && ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 140 && ncdfs.member.memberHypertensionDto.systolicBloodPressure < 160) || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure >= 90 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 100))) {
                        ncdfs.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Stage 1 Treatment';
                    } else if ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 160) || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure > 100)) {
                        ncdfs.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Stage 2 Treatment';
                    } */
                    // New logic based on set diagnosis which having dropdown value to select
                    if (((ncdfs.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 90)
                        && ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 120 && ncdfs.member.memberHypertensionDto.systolicBloodPressure < 140)
                            || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure >= 80 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 90)))
                        || ((ncdfs.member.memberHypertensionDto.systolicBloodPressure < 160 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 100)
                            && ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 140 && ncdfs.member.memberHypertensionDto.systolicBloodPressure < 160)
                                || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure >= 90 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 100)))
                        || ((ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 160) || (ncdfs.member.memberHypertensionDto.diastolicBloodPressure > 100))) {
                        ncdfs.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Confirmed';
                    } else {
                        ncdfs.member.memberHypertensionDto.diagnosisDto.diagnosis = 'No Abnormality Detected';
                    }
                }
            } else if (diseaseCode == 'D') {
                if (ncdfs.member.memberDiabetesDto) {
                    if (!ncdfs.member.memberDiabetesDto.diagnosisDto) {
                        ncdfs.member.memberDiabetesDto.diagnosisDto = {};
                    }
                    // Old logic based on set diagnosis which having textarea to fill
                    /* if (ncdfs.member.memberDiabetesDto.fastingBloodSugar >= 126 || ncdfs.member.memberDiabetesDto.postPrandialBloodSugar >= 200) {
                        ncdfs.member.memberDiabetesDto.diagnosisDto.diagnosis = 'Diabetes Mellitus';
                    } else {
                        ncdfs.member.memberDiabetesDto.diagnosisDto.diagnosis = 'No Abnormality';
                    } */
                    // New logic based on set diagnosis which having dropdown value to select
                    if (ncdfs.member.memberDiabetesDto.fastingBloodSugar >= 126 || ncdfs.member.memberDiabetesDto.postPrandialBloodSugar >= 200) {
                        ncdfs.member.memberDiabetesDto.diagnosisDto.diagnosis = 'Confirmed';
                    } else {
                        ncdfs.member.memberDiabetesDto.diagnosisDto.diagnosis = 'No Abnormality Detected';
                    }
                }
            }
        };

        ncdfs.saveReferral = function (referralDto, diseaseCode, previousRefId) {
            referralDto.diseaseCode = diseaseCode;
            referralDto.referredFrom = $state.params.type;
            referralDto.referredBy = ncdfs.loggedInUser.id;
            referralDto.referredOn = new Date();
            referralDto.memberId = Number($state.params.id);
            referralDto.previousRefferalId = previousRefId;
            Mask.show();
            NcdDAO.saveReferral(referralDto).then(function (res) {
                toaster.pop('success', 'Referral Saved Successfully');
                referralDto = {};
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })

        };

        ncdfs.openFollowupModal = function () {
            var followupObj = {
                userHealthInfras: ncdfs.userHealthInfras,
                followup: {}
            };
            followup.followup.memberId = Number($state.params.id);
            followup.followup.referralFrom = $state.params.type
            followup.followup.isFemale = ncdfs.member.basicDetails.gender == 'M' ? false : true
            var modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/followup.modal.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    followup: function () {
                        return followupObj;
                    }
                },
                controller: function ($scope, followup, $uibModalInstance) {
                    $scope.followup = followup.followup;
                    $scope.userHealthInfras = followup.userHealthInfras;
                    if ($scope.userHealthInfras && $scope.userHealthInfras.length == 1) {
                        $scope.followup.healthInfraId = $scope.userHealthInfras[0].id;
                        $scope.followup.healthInfraName = $scope.userHealthInfras[0].name;
                    }
                    $scope.ok = function (followupForm) {
                        followupForm.$setSubmitted();
                        if (followupForm.$valid) {
                            NcdDAO.saveFollowup($scope.followup).then(function (res) {
                                toaster.pop('success', 'Followup Generated Successfully');
                                $uibModalInstance.close();

                            });
                        }
                    }
                    $scope.cancel = function (followupForm) {
                        followupForm.$setPristine();
                        $uibModalInstance.dismiss();
                    }
                }
            });
            modalInstance.result.then(function () {


            }, function () {

            });
        };
        ncdfs.retrieveHealthInfras = function (type) {
            if (type) {
                QueryDAO.execute({
                    code: 'health_infra_retrival_by_type',
                    parameters: {
                        type: type
                    }
                }).then(function (res) {
                    ncdfs.healthInfras = res;
                })
            }
        };
        ncdfs.printPatientSummary = function () {
            let imagesPath = GeneralUtil.getImagesPath();
            var header = `<div class='print_header display-none'><img class='print_logo' src="${imagesPath}web_logo.png" ><h2 class='page_title'>Patient Summary</h2></div>`;
            ncdfs.footer = "Generated by " + ncdfs.loggedInUser.name + " at " + new Date().toLocaleString();
            setSymptoms();
            let promiseList = [];
            promiseList.push(ncdfs.retrieveTreatmentHistory('HT'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('O'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('C'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('B'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('D'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('IA'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('G'));
            promiseList.push(ncdfs.retrieveTreatmentHistory('MH'));
            promiseList.push(ncdfs.retrieveReffForToday($state.params.id));
            promiseList.push(ncdfs.retrieveNextFollowup($state.params.id));
            $q.all(promiseList).then(function (res) {
                $('#printableDiv').printThis({
                    debug: false,
                    importCSS: false,
                    loadCSS: ['styles/css/printable.css', 'styles/css/ncd_printable.css'],
                    header: header,
                    printDelay: 333,
                    base: "./",
                    pageTitle: ncdfs.appName
                });
            })


        };

        ncdfs.retrieveReffForToday = function (memberid) {
            Mask.show()
            return NcdDAO.retrieveReffForToday(memberid).then(function (res) {
                ncdfs.member.reffForToday = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
        }
        ncdfs.retrieveNextFollowup = function (memberid) {
            Mask.show()
            return NcdDAO.retrieveNextFollowup(memberid).then(function (res) {
                ncdfs.member.nextFollowup = {};
                // ncdfs.member.nextFollowup=res;
                _.map(res, function (followup) {
                    ncdfs.member.nextFollowup[followup.diseaseCode] = followup.followupDate;
                })


            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
        };

        ncdfs.retrieveLoggedInUserHealthInfra = function (userId) {
            if (userId) {
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: userId
                    }
                }).then(function (res) {
                    ncdfs.isUserHealthFetch = true
                    ncdfs.userHealthInfras = res.result;
                })
            }
        };

        ncdfs.retrieveLoggedInUserFeatureJson = function () {
            Mask.show()
            AuthenticateService.getAssignedFeature("techo.ncd.members").then(function (res) {
                ncdfs.rights = res.featureJson;
                if (!ncdfs.rights) {
                    ncdfs.rights = {};
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        };

        ncdfs.hypertensionExaminedValuesChanged = function () {
            ncdfs.member.memberHypertensionDto.startTreatment = null;
            ncdfs.member.memberHypertensionDto.referralDto = null;
            if (!ncdfs.showHypertensionSubType) {
                if (ncdfs.member.memberHypertensionDto.systolicBloodPressure >= 140 || ncdfs.member.memberHypertensionDto.diastolicBloodPressure >= 90) {
                    ncdfs.member.memberHypertensionDto.status = 'CONFIRMED'
                } else {
                    ncdfs.member.memberHypertensionDto.status = 'NO_ABNORMALITY'
                }
            } else if (ncdfs.showHypertensionSubType) {
                if (ncdfs.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdfs.member.memberHypertensionDto.diastolicBloodPressure < 90) {
                    ncdfs.member.memberHypertensionDto.subType = 'CONTROLLED'
                } else {
                    ncdfs.member.memberHypertensionDto.subType = 'UNCONTROLLED'
                }
            }

        }

        ncdfs.mentalHealthExaminedValuesChanged = function () {
            ncdfs.member.memberMentalHealthDto.startTreatment = null;
            ncdfs.member.memberMentalHealthDto.referralDto = null;
            if (ncdfs.member.memberMentalHealthDto.talk > 1
                || ncdfs.member.memberMentalHealthDto.ownDailyWork > 1
                || ncdfs.member.memberMentalHealthDto.socialWork > 1
                || ncdfs.member.memberMentalHealthDto.understanding > 1) {
                ncdfs.member.memberMentalHealthDto.todayResult='CONFIRMED';
                ncdfs.member.memberMentalHealthDto.status = 'CONFIRMED';
            } else {
                ncdfs.member.memberMentalHealthDto.todayResult='NO_ABNORMALITY';
                ncdfs.member.memberMentalHealthDto.status = 'NO_ABNORMALITY';
            }
        }

        ncdfs.diabetesExaminedValuesChanged = function () {
            ncdfs.member.memberDiabetesDto.startTreatment = null;
            ncdfs.member.memberDiabetesDto.referralDto = null;
            if (!ncdfs.showDiabetesSubType) {
                if (ncdfs.member.memberDiabetesDto.fastingBloodSugar >= 126
                    || ncdfs.member.memberDiabetesDto.postPrandialBloodSugar >= 200
                    || ncdfs.member.memberDiabetesDto.bloodSugar >= 200
                    || ncdfs.member.memberDiabetesDto.dka
                    || ncdfs.member.memberDiabetesDto.hba1c >= 6.5) {
                    ncdfs.member.memberDiabetesDto.status = 'CONFIRMED';
                } else {
                    ncdfs.member.memberDiabetesDto.status = 'NO_ABNORMALITY';
                }
            } else if (ncdfs.showDiabetesSubType) {
                if (ncdfs.member.memberDiabetesDto.fastingBloodSugar < 130
                    && ncdfs.member.memberDiabetesDto.postPrandialBloodSugar < 200
                    && ncdfs.member.memberDiabetesDto.bloodSugar < 200
                    && ncdfs.member.memberDiabetesDto.hba1c < 7) {
                    ncdfs.member.memberDiabetesDto.subType = 'CONTROLLED';
                } else {
                    ncdfs.member.memberDiabetesDto.subType = 'UNCONTROLLED';
                }
            }
        }

        ncdfs.retrieveHypertensionDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveHypertensionDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberHypertensionDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.hypertensionAlreadyFilled = true;
                    console.log(response);
                    ncdfs.member.memberHypertensionDto.systolicBloodPressure = response.systolicBp
                    ncdfs.member.memberHypertensionDto.diastolicBloodPressure = response.diastolicBp
                    ncdfs.member.memberHypertensionDto.heartRate = response.pulseRate
                } else {
                    ncdfs.hypertensionAlreadyFilled = false;
                    ncdfs.member.memberHypertensionDto.systolicBloodPressure = null
                    ncdfs.member.memberHypertensionDto.diastolicBloodPressure = null
                    ncdfs.member.memberHypertensionDto.heartRate = null;
                    ncdfs.minHypertensionFollowUpDate = moment(ncdfs.member.memberHypertensionDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveGeneralDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveGeneralDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberGeneralDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.generalAlreadyFilled = true;
                    ncdfs.member.memberGeneralDto.symptoms = response.symptoms
                    ncdfs.member.memberGeneralDto.clinicalObservation = response.clinicalObservation
                    ncdfs.member.memberGeneralDto.diagnosis = response.diagnosis
                    ncdfs.member.memberGeneralDto.remarks = response.remarks
                } else {
                    ncdfs.generalAlreadyFilled = false;
                    ncdfs.member.memberGeneralDto.symptoms = null
                    ncdfs.member.memberGeneralDto.clinicalObservation = null
                    ncdfs.member.memberGeneralDto.diagnosis = null
                    ncdfs.member.memberGeneralDto.remarks = null
                    ncdfs.minGeneralFollowUpDate = moment(ncdfs.member.memberGeneralDto.screeningDate).add(1, 'days');
                    ncdfs.maxGeneralFollowUpDate = moment(ncdfs.minGeneralFollowUpDate).add(90, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveInitialAssessmentDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveInitialAssessmentDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberInitialAssessmentDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    initialAssessmentAlreadyFilled = true;
                    ncdfs.member.memberInitialAssessmentDto.height = response.height
                    ncdfs.member.memberInitialAssessmentDto.weight = response.weight
                    ncdfs.member.memberInitialAssessmentDto.waistCircumference = response.waistCircumference
                    ncdfs.member.memberInitialAssessmentDto.bmi = response.bmi
                } else {
                    initialAssessmentAlreadyFilled = false;
                    ncdfs.member.memberInitialAssessmentDto.height = null
                    ncdfs.member.memberInitialAssessmentDto.weight = null
                    ncdfs.member.memberInitialAssessmentDto.waistCircumference = null
                    ncdfs.member.memberInitialAssessmentDto.bmi = null
                    ncdfs.minInitialAssessmentFollowUpDate = moment(ncdfs.member.memberInitialAssessmentDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveMentalHealthDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveMentalHealthDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberMentalHealthDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    MentalHealthAlreadyFilled = true;
                    console.log(response);
                    ncdfs.member.memberMentalHealthDto.talk = response.talk
                    ncdfs.member.memberMentalHealthDto.ownDailyWork = response.ownDailyWork
                    ncdfs.member.memberMentalHealthDto.socialWork = response.socialWork
                    ncdfs.member.memberMentalHealthDto.understanding = response.understanding
                } else {
                    MentalHealthAlreadyFilled = false;
                    ncdfs.member.memberMentalHealthDto.talk = null
                    ncdfs.member.memberMentalHealthDto.ownDailyWork = null
                    ncdfs.member.memberMentalHealthDto.socialWork = null
                    ncdfs.member.memberMentalHealthDto.understanding = null
                    ncdfs.minMentalHealthFollowUpDate = moment(ncdfs.member.memberMentalHealthDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveDiabetesDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveDiabetesDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberDiabetesDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.diabetesAlreadyFilled = true;
                    ncdfs.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar
                    ncdfs.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar
                    ncdfs.member.memberDiabetesDto.bloodSugar = response.bloodSugar
                    ncdfs.member.memberDiabetesDto.dka = response.dka
                    ncdfs.member.memberDiabetesDto.hba1c = response.hba1c
                } else {
                    ncdfs.diabetesAlreadyFilled = false;
                    ncdfs.member.memberDiabetesDto.fastingBloodSugar = null
                    ncdfs.member.memberDiabetesDto.postPrandialBloodSugar = null
                    ncdfs.member.memberDiabetesDto.bloodSugar = null
                    ncdfs.member.memberDiabetesDto.dka = null
                    ncdfs.member.memberDiabetesDto.hba1c = null
                    ncdfs.minDiabetesFollowUpDate = moment(ncdfs.member.memberDiabetesDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveOralDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveOralDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberOralDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.oralAlreadyFilled = true;
                    ncdfs.member.memberOralDto.restrictedMouthOpening = response.restrictedMouthOpening;
                    ncdfs.member.memberOralDto.whitePatches = response.whitePatches;
                    ncdfs.member.memberOralDto.redPatches = response.redPatches;
                    ncdfs.member.memberOralDto.nonHealingUlcers = response.nonHealingUlcers;
                    ncdfs.member.memberOralDto.growthOfRecentOrigins = response.growthOfRecentOrigins;
                    setOralTestsForDisplay();
                } else {
                    ncdfs.oralAlreadyFilled = false;
                    ncdfs.member.memberOralDto.restrictedMouthOpening = null;
                    ncdfs.member.memberOralDto.whitePatches = null;
                    ncdfs.member.memberOralDto.redPatches = null;
                    ncdfs.member.memberOralDto.nonHealingUlcers = null;
                    ncdfs.member.memberOralDto.growthOfRecentOrigins = null;
                    setOralTestsForDisplay();
                    ncdfs.minOralFollowUpDate = moment(ncdfs.member.memberOralDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveBreastDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveBreastDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberBreastDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.breastAlreadyFilled = true;
                    ncdfs.member.memberBreastDto.sizeChange = response.sizeChange
                    ncdfs.member.memberBreastDto.sizeChangeLeft = response.sizeChangeLeft
                    ncdfs.member.memberBreastDto.sizeChangeRight = response.sizeChangeRight

                    ncdfs.member.memberBreastDto.nippleNotOnSameLevel = response.nippleNotOnSameLevel
                    ncdfs.member.memberBreastDto.anyRetractionOfNipple = response.anyRetractionOfNipple
                    ncdfs.member.memberBreastDto.retractionOfLeftNipple = response.retractionOfLeftNipple
                    ncdfs.member.memberBreastDto.retractionOfRightNipple = response.retractionOfRightNipple

                    ncdfs.member.memberBreastDto.lymphadenopathy = response.lymphadenopathy

                    ncdfs.member.memberBreastDto.leftLymphadenopathy = response.leftLymphadenopathy
                    ncdfs.member.memberBreastDto.rightLymphadenopathy = response.rightLymphadenopathy

                    ncdfs.member.memberBreastDto.dischargeFromNipple = response.dischargeFromNipple
                    ncdfs.member.memberBreastDto.dischargeFromLeftNipple = response.dischargeFromLeftNipple
                    ncdfs.member.memberBreastDto.dischargeFromRightNipple = response.dischargeFromRightNipple

                    ncdfs.member.memberBreastDto.visualSkinDimplingRetraction = response.visualSkinDimplingRetraction
                    ncdfs.member.memberBreastDto.visualNippleRetractionDistortion = response.visualNippleRetractionDistortion
                    ncdfs.member.memberBreastDto.visualLumpInBreast = response.visualLumpInBreast
                    setBreastVisual();
                } else {
                    ncdfs.breastAlreadyFilled = false;
                    ncdfs.member.memberBreastDto.sizeChange = null
                    ncdfs.member.memberBreastDto.sizeChangeLeft = null
                    ncdfs.member.memberBreastDto.sizeChangeRight = null
                    ncdfs.member.memberBreastDto.nippleNotOnSameLevel = null
                    ncdfs.member.memberBreastDto.anyRetractionOfNipple = null
                    ncdfs.member.memberBreastDto.retractionOfLeftNipple = null
                    ncdfs.member.memberBreastDto.retractionOfRightNipple = null
                    ncdfs.member.memberBreastDto.lymphadenopathy = null
                    ncdfs.member.memberBreastDto.leftLymphadenopathy = null
                    ncdfs.member.memberBreastDto.rightLymphadenopathy = null
                    ncdfs.member.memberBreastDto.dischargeFromNipple = null
                    ncdfs.member.memberBreastDto.dischargeFromLeftNipple = null
                    ncdfs.member.memberBreastDto.dischargeFromRightNipple = null
                    ncdfs.member.memberBreastDto.visualSkinDimplingRetraction = null
                    ncdfs.member.memberBreastDto.visualNippleRetractionDistortion = null
                    ncdfs.member.memberBreastDto.visualLumpInBreast = null
                    setBreastVisual();
                    ncdfs.minBreastFollowUpDate = moment(ncdfs.member.memberBreastDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdfs.retrieveCervicalDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveCervicalDetailsByMemberAndDate($state.params.id, moment(ncdfs.member.memberCervicalDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdfs.cervicalAlreadyFilled = true;
                    ncdfs.member.memberCervicalDto.papsmearTest = response.papsmearTest
                    ncdfs.member.memberCervicalDto.viaTest = response.viaTest
                    ncdfs.member.memberCervicalDto.viaExamPoints = response.viaExamPoints
                    ncdfs.member.memberCervicalDto.externalGenitaliaHealthy = response.externalGenitaliaHealthy
                    setCervicalVisual();
                } else {
                    ncdfs.cervicalAlreadyFilled = false;
                    ncdfs.member.memberCervicalDto.papsmearTest = null
                    ncdfs.member.memberCervicalDto.viaTest = null
                    ncdfs.member.memberCervicalDto.viaExamPoints = null
                    setCervicalVisual();
                    ncdfs.minCervicalFollowUpDate = moment(ncdfs.member.memberCervicalDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }



        ncdfs.showTreatmentHistory = function (diseaseCodeObj) {
            if (!diseaseCodeObj) return;
            const modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/treatment-history.modal.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    diseaseCode: function () {
                        return diseaseCodeObj
                    },
                    memberId: function () {
                        return Number($state.params.id);
                    }
                },
                controller: function ($scope, $uibModalInstance, diseaseCode, memberId) {
                    $scope.diseaseCode = diseaseCode;
                    const queryDto = {
                        parameters: { memberId }
                    };
                    switch (diseaseCode) {
                        case 'IA':
                            $scope.disease = 'InitialAssessment';
                            queryDto.code = 'ncd_initialAssessment_treatment_history'
                            break;
                        case 'MH':
                            $scope.disease = 'MentalHealth';
                            queryDto.code = 'ncd_mentalHealth_treatment_history'
                            break;
                        case 'HT':
                            $scope.disease = 'Hypertension';
                            queryDto.code = 'ncd_hypertension_treatment_history'
                            break;
                        case 'G':
                            $scope.disease = 'General';
                            queryDto.code = 'ncd_general_treatment_history'
                            break;
                        case 'D':
                            $scope.disease = 'Diabetes';
                            queryDto.code = 'ncd_diabetes_treatment_history'
                            break;
                        case 'O':
                            $scope.disease = 'Oral';
                            queryDto.code = 'ncd_oral_treatment_history'
                            break;
                        case 'C':
                            $scope.disease = 'Cervical';
                            queryDto.code = 'ncd_cervical_treatment_history'
                            break;
                        case 'B':
                            $scope.disease = 'Breast';
                            queryDto.code = 'ncd_breast_treatment_history'
                            break;
                    }
                    Mask.show();
                    QueryDAO.execute(queryDto).then(function (res) {
                        if (res.result.length === 0) {
                            $uibModalInstance.close();
                            toaster.pop('error', 'No data found!');
                        } else {
                            $scope.treatmentHistory = res.result;
                            console.log($scope.treatmentHistory);
                        }
                    }, function () {
                        toaster.pop('error', 'Something went wrong while retrieving history!');
                    }).finally(function () {
                        Mask.hide();
                    });
                    $scope.ok = function () {
                        $uibModalInstance.close();
                    }
                    $scope.cancel = function () {
                        $uibModalInstance.dismiss();
                    }
                }
            });
            modalInstance.result.then(function () { }, function () { });
        }



        init();
    }
    angular.module('imtecho.controllers').controller('NcdConsultantfollowupScreenController', NcdConsultantfollowupScreenController);
})(window.angular);