(function (angular) {
    function NcdMemberDetailController($state, NcdDAO, $filter, toaster, $uibModal, GeneralUtil, AuthenticateService, Mask, $q, QueryDAO, NdhmHipDAO, $window) {
        var ncdmd = this;
        ncdmd.appName = GeneralUtil.getAppName();
        ncdmd.diabetesDetected = false;
        ncdmd.oralDetected = false;
        ncdmd.breastDetected = false;
        ncdmd.cervicalDetected = false;
        ncdmd.keyFn = Object.keys;
        ncdmd.isArray = angular.isArray
        ncdmd.today = new Date();
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

        ncdmd.tabs = [
            'initialassessment', 'general', 'oral-main', 'breast-main', 'cervical-main', 'diabetes-main', 'mental-health', 'hypertension-main', 'labtest-main'
        ];
        var init = function () {
            ncdmd.isUserHealthFetch = false
            ncdmd.navigateToTab('initialassessment');
            AuthenticateService.getLoggedInUser().then(function (res) {
                ncdmd.loggedInUser = res.data;
                ncdmd.retrieveLoggedInUserHealthInfra(res.data.id);
                ncdmd.retrieveLoggedInUserFeatureJson();
            })

            NcdDAO.retrieveAllMedicines().then(function (res) {
                ncdmd.medicines = res;
            }, GeneralUtil.showMessageOnApiCallFailure);
            ncdmd.alcoholConsumptions =
                ['Daily or almost daily', 'Weekly', 'Monthly', 'Less than monthly', 'Never']

            if ($state.params.id) {
                // ncdmd.screeningDate = moment();
                ncdmd.retrieveMemberDetails();
            }

            NdhmHipDAO.getAllCareContextMasterDetails(parseInt($state.params.id)).then((res) => {
                if (res.length > 0) {
                    ncdmd.healthIdsData = res;
                    ncdmd.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
                    let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
                    ncdmd.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
                }
            }).catch((error) => {
            })
        };

        //max date
        ncdmd.numberOfDaysToAdd = 6;
        ncdmd.result = ncdmd.today.setDate(ncdmd.today.getDate() + ncdmd.numberOfDaysToAdd);
        console.log(new Date(ncdmd.result));

        //back button
        ncdmd.$window = $window;
        ncdmd.goBack = function () {
            $window.history.back();
        };

        //show hypertension_button
        ncdmd.IsVisible = false;
        ncdmd.ShowButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.IsVisible = value == "N";
        };

        //show General_button
        ncdmd.IsVisibleGeneral = false;
        ncdmd.ShowButtonGeneral = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.IsVisibleGeneral = value == "N";
        };

        //show cervical
        ncdmd.showCervicalOther = false;
        ncdmd.ShowOtherButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.showCervicalOther = value == "Y";
        };

        ncdmd.showCervicalontherFindings = false;
        ncdmd.ShowontherFindingsButton = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.showCervicalontherFindings = value == "Y";
        };

        ncdmd.showCervicalImage = false;
        ncdmd.ShowCervicalImageFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.showCervicalImage = value == "Y";
        };

        ncdmd.showCervicalPoint = false;
        ncdmd.ShowCervicalPointFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.showCervicalPoint = value == "Y";
        };

        //show breast
        ncdmd.showBreastButton = false;
        ncdmd.ShowBreastButtonFun = function (value) {
            //If DIV is visible it will be hidden and vice versa.
            ncdmd.showBreastButton = value == "Y";
        };

        ncdmd.showBreastPoint = false;
        ncdmd.showBreastPoints = false;
        ncdmd.ShowBreastPointFun = function (value) {
            if (value == "Y") {
                ncdmd.showBreastPoint = true;
                ncdmd.showBreastPoints = false;
            } else {
                ncdmd.showBreastPoint = false;
                ncdmd.showBreastPoints = true;
            }
        };


        //dikhega
        ncdmd.showOral = true;

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
            if (ncdmd.member.memberOtherInfoDto.familyHistoryOfStroke) {
                familyHistory['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.familyHistoryOfStroke, 'Stroke', '');

            }
            if (ncdmd.member.memberOtherInfoDto.familyHistoryOfPrematureMi) {
                familyHistory['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.familyHistoryOfPrematureMi, 'Premature Mi', '');

            }
            if (ncdmd.member.memberOtherInfoDto.familyHistoryOfDiabetes) {
                familyHistory['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.familyHistoryOfDiabetes, 'Diabetes', '');

            }
            if (ncdmd.member.memberOtherInfoDto.historyOfHeartAttack) {
                history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.historyOfHeartAttack, 'Heart Attack', '');

            }
            if (ncdmd.member.memberOtherInfoDto.historyOfStroke) {
                history['historyOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.historyOfStroke, 'Stroke', '');

            }

            ncdmd.member.history = history;
            ncdmd.member.familyHistory = familyHistory;
        }

        var setSymptoms = function () {
            let symptoms = {};
            let general = {};
            if (ncdmd.member.memberOtherInfoDto) {
                if (ncdmd.member.memberOtherInfoDto.excessThirst) {
                    general['excessThirst'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.excessThirst, 'Excess Thirst', '');
                }
                if (ncdmd.member.memberOtherInfoDto.excessUrination) {
                    general['excessUrination'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.excessUrination, 'Excess Urination', '');
                }
                if (ncdmd.member.memberOtherInfoDto.excessHunger) {
                    general['excessHunger'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.excessHunger, 'Excess Hunger', '');
                }
                if (ncdmd.member.memberOtherInfoDto.angina) {
                    general['angina'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.angina, 'Angina', '');
                }
                if (ncdmd.member.memberOtherInfoDto.recurrentSkin) {
                    general['recurrentSkin'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.recurrentSkin, 'Recurrent Skin', '');
                }
                if (ncdmd.member.memberOtherInfoDto.delayInHealing) {
                    general['delayInHealing'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.delayInHealing, 'Delay in Wound Healing', '');
                }
                if (ncdmd.member.memberOtherInfoDto.changeInDieteryHabits) {
                    general['changeInDieteryHabits'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.changeInDieteryHabits, 'Dietary Habits Change', '');
                }
                if (ncdmd.member.memberOtherInfoDto.limpness) {
                    general['limpness'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.limpness, 'Limpness', '');
                }
                if (ncdmd.member.memberOtherInfoDto.intermittentClaudication) {
                    general['intermittentClaudication'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.intermittentClaudication, 'Claudication', '');
                }
                if (ncdmd.member.memberOtherInfoDto.significantEdema) {
                    general['significantEdema'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfoDto.significantEdema, 'Significant Edema', '');
                }




            }
            if (ncdmd.member.memberCbacDetail) {
                if (ncdmd.member.memberCbacDetail.lossOfWeight) {
                    general['lossOfWeight'] = $filter('yesOrNo')(ncdmd.member.memberCbacDetail.lossOfWeight, 'Weight Loss', '');
                }
                if (ncdmd.member.memberCbacDetail.shortnessOfBreath) {
                    general['shortnessOfBreath'] = $filter('yesOrNo')(ncdmd.member.memberCbacDetail.shortnessOfBreath, 'Breathlessness', '');
                }

            }

            symptoms.general = general;

            if (ncdmd.member.memberOralDto) {
                let oral = {};
                if (ncdmd.member.memberOralDto.difficultyInOpeningMouth) {
                    oral['difficultyInOpeningMouth'] = $filter('yesOrNo')(ncdmd.member.memberOralDto.difficultyInOpeningMouth, 'Difficulty in Opening Mouth', '');
                }
                if (ncdmd.member.memberOralDto.threeWeeksMouthUlcer) {
                    oral['threeWeeksMouthUlcer'] = $filter('yesOrNo')(ncdmd.member.memberOralDto.threeWeeksMouthUlcer, 'Ulceration', '');
                }
                if (ncdmd.member.memberOralDto.whiteRedPatchOralCavity) {
                    oral['whiteRedPatchOralCavity'] = $filter('yesOrNo')(ncdmd.member.memberOralDto.whiteRedPatchOralCavity, 'Oral Cavity Patch', '');
                }
                if (ncdmd.member.memberOralDto.difficultyInSpicyFood) {
                    oral['difficultyInSpicyFood'] = $filter('yesOrNo')(ncdmd.member.memberOralDto.difficultyInSpicyFood, 'Difficulty Tolerating Spicy Food', '');
                }
                if (ncdmd.member.memberOralDto.voiceChange) {
                    oral['voiceChange'] = $filter('yesOrNo')(ncdmd.member.memberOralDto.voiceChange, 'Sudden Voice Change', '');
                }




                symptoms.oral = oral;
            }

            if (ncdmd.member.memberBreastDto) {
                let breast = {};
                if (ncdmd.member.memberBreastDto.lumpInBreast) {
                    breast['lumpInBreast'] = $filter('yesOrNo')(ncdmd.member.memberBreastDto.lumpInBreast, 'Lump', '');

                }
                if (ncdmd.member.memberBreastDto.anyRetractionOfNipple) {
                    breast['anyRetractionOfNipple'] = $filter('yesOrNo')(ncdmd.member.memberBreastDto.anyRetractionOfNipple, 'Retraction Of Nipple', '');

                }
                if (ncdmd.member.memberBreastDto.dischargeFromNipple) {
                    breast['dischargeFromNipple'] = $filter('yesOrNo')(ncdmd.member.memberBreastDto.dischargeFromNipple, 'Discharge from Nipples', '');
                }


                symptoms.breast = breast;
            }

            if (ncdmd.member.memberCervicalDto) {
                let cervical = {};
                if (ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['excessiveBleedingDuringPeriods'] = $filter('yesOrNo')(ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods, 'Excess Bleeding During Periods', '');
                }
                if (ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['bleedingBetweenPeriods'] = $filter('yesOrNo')(ncdmd.member.memberCervicalDto.bleedingBetweenPeriods, 'Bleeding Between Periods', '');

                }
                if (ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['postmenopausalBleeding'] = $filter('yesOrNo')(ncdmd.member.memberCervicalDto.postmenopausalBleeding, 'Postmensual Bleeding', '');

                }
                if (ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['bleedingDfterIntercourse'] = $filter('yesOrNo')(ncdmd.member.memberCervicalDto.bleedingDfterIntercourse, 'Post Coital Bleeding', '');

                }
                if (ncdmd.member.memberCervicalDto.excessiveBleedingDuringPeriods) {
                    cervical['excessiveSmellingVaginalDischarge'] = $filter('yesOrNo')(ncdmd.member.memberCervicalDto.excessiveSmellingVaginalDischarge, 'Excess Smelling Vaginal Discharge', '');

                }
                symptoms.cervical = cervical;
            }



            ncdmd.member.symptoms = symptoms;
        }

        // var setMedications = function () {
        //     let history = [];
        //     history['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfStroke, 'Stroke (Family History)', '');
        //     history['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfPrematureMi, 'Premature Mi (Family History)', '');
        //     history['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfDiabetes, 'Diabetes (Family History)', '');
        //     history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.historyOfHeartAttack, 'Heart Attack', '');
        //     history['historyOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.historyOfStroke, 'Stroke', '');;
        //     ncdmd.member.history = history;
        // }

        // var setExamination = function () {
        //     let history = [];
        //     history['familyHistoryOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfStroke, 'Stroke (Family History)', '');
        //     history['familyHistoryOfPrematureMi'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfPrematureMi, 'Premature Mi (Family History)', '');
        //     history['familyHistoryOfDiabetes'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.familyHistoryOfDiabetes, 'Diabetes (Family History)', '');
        //     history['historyOfHeartAttack'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.historyOfHeartAttack, 'Heart Attack', '');
        //     history['historyOfStroke'] = $filter('yesOrNo')(ncdmd.member.memberOtherInfo.historyOfStroke, 'Stroke', '');;
        //     ncdmd.member.history = history;
        // }

        var setVitals = function () {
            let vitals = {};
            if (ncdmd.member.memberHypertensionDto && ncdmd.member.memberHypertensionDto.systolicBloodPressure && ncdmd.member.memberHypertensionDto.diastolicBloodPressure) {
                vitals['BP'] = ncdmd.member.memberHypertensionDto.systolicBloodPressure + '/' + ncdmd.member.memberHypertensionDto.diastolicBloodPressure + 'mmHg';
                vitals.sbp = ncdmd.member.memberHypertensionDto.systolicBloodPressure;
                vitals.dbp = ncdmd.member.memberHypertensionDto.diastolicBloodPressure;
            }
            if (ncdmd.member.memberHypertensionDto && ncdmd.member.memberHypertensionDto.heartRate) {
                vitals['HR'] = ncdmd.member.memberHypertensionDto.heartRate;
            }
            if (ncdmd.member.memberDiabetesDto && ncdmd.member.memberDiabetesDto.bloodSugar) {
                vitals['bloodSugar'] = ncdmd.member.memberDiabetesDto.bloodSugar;
            }
            if (ncdmd.member.memberCbacDetail && ncdmd.member.memberCbacDetail.bmi) {
                vitals['BMI'] = ncdmd.member.memberCbacDetail.bmi.toFixed(0);
            }
            ncdmd.member.vitals = vitals;


        }

        ncdmd.saveVitals = function () {

            saveDetails().then(function () {
                ncdmd.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "Vitals saved successfully");
                    setVitals();
                })
                // toaster.pop('success', "Vitals saved successfully");
                // setVitals();
                // ncdmd.retrieveMemberDetails();

                $('#patientHistory').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        ncdmd.saveHistory = function () {


            saveDetails().then(function () {
                ncdmd.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "History saved successfully");
                    setHistory();
                })

                // ncdmd.retrieveMemberDetails();
                $('#symptoms').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        ncdmd.saveSymptoms = function () {

            saveDetails().then(function () {

                ncdmd.retrieveMemberDetails().then(function () {
                    toaster.pop('success', "Symptoms saved successfully");
                    setSymptoms();
                })
                // $('#sympt').tab('show');
            }, GeneralUtil.showMessageOnApiCallFailure)



        }
        var setDoneBy = function () {
            if (ncdmd.member.memberBreastDto && !ncdmd.member.memberBreastDto.doneBy) {
                ncdmd.member.memberBreastDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberDiabetesDto && !ncdmd.member.memberDiabetesDto.doneBy) {
                ncdmd.member.memberDiabetesDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberInitialAssessmentDto && !ncdmd.member.memberInitialAssessmentDto.doneBy) {
                ncdmd.member.memberInitialAssessmentDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberMentalHealthDto && !ncdmd.member.memberMentalHealthDto.doneBy) {
                ncdmd.member.memberMentalHealthDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberGeneralDto && !ncdmd.member.memberGeneralDto.doneBy) {
                ncdmd.member.memberGeneralDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberHypertensionDto && !ncdmd.member.memberHypertensionDto.doneBy) {
                ncdmd.member.memberHypertensionDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberCervicalDto && !ncdmd.member.memberCervicalDto.doneBy) {
                ncdmd.member.memberCervicalDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberOralDto && !ncdmd.member.memberOralDto.doneBy) {
                ncdmd.member.memberOralDto.doneBy = "FHW";
            }
            if (ncdmd.member.memberCbacDetail && !ncdmd.member.memberCbacDetail.doneBy) {
                ncdmd.member.memberCbacDetail.doneBy = "ASHA";
            }

        }

        ncdmd.calculateBmi = function () {
            if (ncdmd.member.memberCbacDetail) {
                if (ncdmd.member.memberCbacDetail.height) {
                    let heightInM = ncdmd.member.memberCbacDetail.height / 100;
                    let heightInM2 = heightInM * heightInM;
                    ncdmd.member.memberCbacDetail.bmi = (ncdmd.member.memberCbacDetail.weight / heightInM2).toFixed(0);
                }

            }
        }
        ncdmd.calculateBmiForInitial = function () {
            if (ncdmd.member.memberInitialAssessmentDto.height) {
                let heightInM = ncdmd.member.memberInitialAssessmentDto.height / 100;
                let heightInM2 = heightInM * heightInM;
                ncdmd.member.memberInitialAssessmentDto.bmi = (ncdmd.member.memberInitialAssessmentDto.weight / heightInM2).toFixed(0);
            }
        }

        ncdmd.viewCBACReport = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/cbac-report.modal.html',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    cbac: function () {
                        return ncdmd.member.memberCbacDetail;
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

        ncdmd.viewNcdEnrollment = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/cbac-report.modal.html',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    cbac: function () {
                        return ncdmd.member.memberCbacDetail;
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
        
        ncdmd.retrieveMemberDetails = function (isSaveAction) {
            Mask.show();
            ncdmd.showHypertensionSubType = false;
            ncdmd.askHypertensionTreatmentQuestion = true;
            ncdmd.showHypertensionMedicines = false;
            ncdmd.showHypertensionReferral = false;

            ncdmd.showInitialAssessmentSubType = false;
            ncdmd.askInitialAssessmentTreatmentQuestion = true;
            ncdmd.showInitialAssessmentMedicines = false;
            ncdmd.showInitialAssessmentReferral = false;

            ncdmd.showGeneralSubType = false;
            ncdmd.askGeneralTreatmentQuestion = true;
            ncdmd.showGeneralMedicines = false;
            ncdmd.showGeneralReferral = false;

            ncdmd.showMentalHealthSubType = false;
            ncdmd.askMentalHealthTreatmentQuestion = true;
            ncdmd.showMentalHealthMedicines = false;
            ncdmd.showMentalHealthReferral = false;

            ncdmd.showDiabetesSubType = false;
            ncdmd.askDiabetesTreatmentQuestion = true;
            ncdmd.showDiabetesMedicines = false;
            ncdmd.showDiabetesReferral = false;

            ncdmd.showOralSubStatus = false;
            ncdmd.showOralDirectStatus = false;
            ncdmd.showOralReferral = false;

            ncdmd.showBreastSubStatus = false;
            ncdmd.showBreastDirectStatus = false;
            ncdmd.showBreastReferral = false;

            ncdmd.showCervicalSubStatus = false;
            ncdmd.showCervicalDirectStatus = false;
            ncdmd.showCervicalReferral = false;

            ncdmd.showAssignedInfrastructuresForHypertension = true;
            ncdmd.showAssignedInfrastructuresForInitialAssessment = true;
            ncdmd.showAssignedInfrastructuresForMentalHealth = true;
            ncdmd.showAssignedInfrastructuresForGeneral = true;
            ncdmd.showAssignedInfrastructuresForDiabetes = true;
            ncdmd.showAssignedInfrastructuresForOral = true;
            ncdmd.showAssignedInfrastructuresForBreast = true;
            ncdmd.showAssignedInfrastructuresForCervical = true;
            return NcdDAO.retrieveDetails($state.params.id).then(function (res) {
                ncdmd.member = {};
                ncdmd.member = res;
                ncdmd.member.basicDetails.age = getAge(ncdmd.member.basicDetails.dob)
                if (ncdmd.member.memberGeneralDto != null) {
                    NcdDAO.retrieveLastRecordForGeneralByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfGeneral = response;
                        ncdmd.member.memberGeneralDto.medicines = ncdmd.lastRecordOfGeneral != null && ncdmd.lastRecordOfGeneral.medicineMasters != null ? ncdmd.lastRecordOfGeneral.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.generalAlreadyFilled = true;
                            ncdmd.member.memberGeneralDto.screeningDate = moment(response.screeningDate)
                            ncdmd.member.memberGeneralDto.symptoms = response.symptoms
                            ncdmd.member.memberGeneralDto.clinicalObservation = response.clinicalObservation
                            ncdmd.member.memberGeneralDto.diagnosis = response.diagnosis
                            ncdmd.member.memberGeneralDto.remarks = response.remarks
                        } else {
                            ncdmd.generalAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberGeneralDto.previousFollowUpDate = ncdmd.member.memberGeneralDto.followUpDate;
                    ncdmd.member.memberGeneralDto.followUpDate = null;
                    ncdmd.member.memberGeneralDto.referralId = ncdmd.member.memberGeneralDto.id
                    switch (ncdmd.member.memberGeneralDto.status) {
                        case 'CONFIRMED':
                            ncdmd.showGeneralSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdmd.showGeneralSubType = true;
                            ncdmd.askGeneralTreatmentQuestion = false;
                            ncdmd.showGeneralMedicines = true;
                            ncdmd.showGeneralReferral = true;
                            break;
                        case 'REFERRED':
                            ncdmd.showGeneralSubType = true;
                            ncdmd.askGeneralTreatmentQuestion = false;
                            ncdmd.showGeneralMedicines = false;
                            ncdmd.showGeneralReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.GeneralForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberMentalHealthDto != null) {
                    NcdDAO.retrieveLastRecordForMentalHealthByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfMentalHealth = response;
                        ncdmd.member.memberMentalHealthDto.medicines = ncdmd.lastRecordOfMentalHealth != null && ncdmd.lastRecordOfMentalHealth.medicineMasters != null ? ncdmd.lastRecordOfMentalHealth.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.MentalHealthAlreadyFilled = true;
                            ncdmd.member.memberMentalHealthDto.screeningDate = moment(response.screeningDate)
                            ncdmd.member.memberMentalHealthDto.talk = response.talk
                            ncdmd.member.memberMentalHealthDto.ownDailyWork = response.ownDailyWork
                            ncdmd.member.memberMentalHealthDto.socialWork = response.socialWork
                            ncdmd.member.memberMentalHealthDto.understanding = response.understanding
                        } else {
                            ncdmd.MentalHealthAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberMentalHealthDto.previousFollowUpDate = ncdmd.member.memberMentalHealthDto.followUpDate;
                    ncdmd.member.memberMentalHealthDto.followUpDate = null;
                    ncdmd.member.memberMentalHealthDto.referralId = ncdmd.member.memberMentalHealthDto.id
                    switch (ncdmd.member.memberMentalHealthDto.status) {
                        case 'CONFIRMED':
                            ncdmd.showMentalHealthSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdmd.showMentalHealthSubType = true;
                            ncdmd.askMentalHealthTreatmentQuestion = false;
                            ncdmd.showMentalHealthMedicines = true;
                            ncdmd.showMentalHealthReferral = true;
                            break;
                        case 'REFERRED':
                            ncdmd.showMentalHealthSubType = true;
                            ncdmd.askMentalHealthTreatmentQuestion = false;
                            ncdmd.showMentalHealthMedicines = false;
                            ncdmd.showMentalHealthReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.MentalHealthForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberInitialAssessmentDto != null) {
                    NcdDAO.retrieveLastRecordForInitialAssessmentByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfInitialAssessment = response;
                        console.log("ye ");
                        console.log(ncdmd.lastRecordOfInitialAssessment);
                        ncdmd.member.memberInitialAssessmentDto.medicines = ncdmd.lastRecordOfInitialAssessment != null && ncdmd.lastRecordOfInitialAssessment.medicineMasters != null ? ncdmd.lastRecordOfInitialAssessment.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.initialAssessmentAlreadyFilled = true;
                            ncdmd.member.memberInitialAssessmentDto.screeningDate = moment(response.screeningDate)
                            ncdmd.member.memberInitialAssessmentDto.height = response.height
                            ncdmd.member.memberInitialAssessmentDto.weight = response.weight
                            ncdmd.member.memberInitialAssessmentDto.waistCircumference = response.waistCircumference
                            ncdmd.member.memberInitialAssessmentDto.bmi = response.bmi
                        } else {
                            ncdmd.initialAssessmentAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberInitialAssessmentDto.previousFollowUpDate = ncdmd.member.memberInitialAssessmentDto.followUpDate;
                    ncdmd.member.memberInitialAssessmentDto.followUpDate = null;
                    ncdmd.member.memberInitialAssessmentDto.referralId = ncdmd.member.memberInitialAssessmentDto.id
                    switch (ncdmd.member.memberInitialAssessmentDto.status) {
                        case 'CONFIRMED':
                            ncdmd.showInitialAssessmentSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdmd.showInitialAssessmentSubType = true;
                            ncdmd.askInitialAssessmentTreatmentQuestion = false;
                            ncdmd.showInitialAssessmentMedicines = true;
                            ncdmd.showInitialAssessmentReferral = true;
                            break;
                        case 'REFERRED':
                            ncdmd.showInitialAssessmentSubType = true;
                            ncdmd.askInitialAssessmentTreatmentQuestion = false;
                            ncdmd.showInitialAssessmentMedicines = false;
                            ncdmd.showInitialAssessmentReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.initialAssessmentForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberHypertensionDto != null) {
                    NcdDAO.retrieveLastRecordForHypertensionByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfHypertension = response[0];
                        ncdmd.member.memberHypertensionDto.medicines = ncdmd.lastRecordOfHypertension != null && ncdmd.lastRecordOfHypertension.medicineMasters != null ? ncdmd.lastRecordOfHypertension.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response[0].id != null && (response[0].doneBy === 'FHW' || response[0].doneBy === 'MPHW' || response[0].doneBy === 'CHO')) {
                            ncdmd.hypertensionAlreadyFilled = true;
                            ncdmd.member.memberHypertensionDto.screeningDate = moment(response[0].screeningDate)
                            ncdmd.member.memberHypertensionDto.systolicBloodPressure = response[0].systolicBloodPressure
                            ncdmd.member.memberHypertensionDto.diastolicBloodPressure = response[0].diastolicBloodPressure
                            ncdmd.member.memberHypertensionDto.heartRate = response[0].heartRate
                        } else {
                            ncdmd.hypertensionAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberHypertensionDto.previousFollowUpDate = ncdmd.member.memberHypertensionDto.followUpDate;
                    ncdmd.member.memberHypertensionDto.followUpDate = null;
                    ncdmd.member.memberHypertensionDto.referralId = ncdmd.member.memberHypertensionDto.id
                    switch (ncdmd.member.memberHypertensionDto.status) {
                        case 'CONFIRMED':
                            ncdmd.showHypertensionSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdmd.showHypertensionSubType = true;
                            ncdmd.askHypertensionTreatmentQuestion = false;
                            ncdmd.showHypertensionMedicines = true;
                            ncdmd.showHypertensionReferral = true;
                            break;
                        case 'REFERRED':
                            ncdmd.showHypertensionSubType = true;
                            ncdmd.askHypertensionTreatmentQuestion = false;
                            ncdmd.showHypertensionMedicines = false;
                            ncdmd.showHypertensionReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.hypertensionForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberDiabetesDto != null) {
                    NcdDAO.retrieveLastRecordForDiabetesByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfDiabetes = response;
                        ncdmd.member.memberDiabetesDto.medicines = ncdmd.lastRecordOfDiabetes != null && ncdmd.lastRecordOfDiabetes.medicineMasters != null ? ncdmd.lastRecordOfDiabetes.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.diabetesAlreadyFilled = true;
                            ncdmd.member.memberDiabetesDto.screeningDate = moment(response.screeningDate);
                            ncdmd.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar
                            ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar
                            ncdmd.member.memberDiabetesDto.bloodSugar = response.bloodSugar
                            ncdmd.member.memberDiabetesDto.dka = response.dka
                            ncdmd.member.memberDiabetesDto.hba1c = response.hba1c
                        } else {
                            ncdmd.diabetesAlreadyFilled = false;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberDiabetesDto.previousFollowUpDate = ncdmd.member.memberDiabetesDto.followUpDate;
                    ncdmd.member.memberDiabetesDto.followUpDate = null;
                    ncdmd.member.memberDiabetesDto.referralId = ncdmd.member.memberDiabetesDto.id
                    switch (ncdmd.member.memberDiabetesDto.status) {
                        case 'CONFIRMED':
                            ncdmd.showDiabetesSubType = true;
                            break;
                        case 'TREATMENT_STARTED':
                            ncdmd.showDiabetesSubType = true;
                            ncdmd.askDiabetesTreatmentQuestion = false;
                            ncdmd.showDiabetesMedicines = true;
                            ncdmd.showDiabetesReferral = true;
                            break;
                        case 'REFERRED':
                            ncdmd.showDiabetesSubType = true;
                            ncdmd.askDiabetesTreatmentQuestion = false;
                            ncdmd.showDiabetesMedicines = false;
                            ncdmd.showDiabetesReferral = true;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.diabetesForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberOralDto != null) {
                    NcdDAO.retrieveLastRecordForOralByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfOral = response;
                        if (ncdmd.lastRecordOfOral != null) {
                            ncdmd.oralHistoryArray = [];
                            if (ncdmd.lastRecordOfOral.restrictedMouthOpening) {
                                ncdmd.oralHistoryArray.push("Restricted Mouth Opening");
                            }
                            if (ncdmd.lastRecordOfOral.whitePatches) {
                                ncdmd.oralHistoryArray.push("White Patches");
                            }
                            if (ncdmd.lastRecordOfOral.redPatches) {
                                ncdmd.oralHistoryArray.push("Red Patches");
                            }
                            if (ncdmd.lastRecordOfOral.nonHealingUlcers) {
                                ncdmd.oralHistoryArray.push("Non Healing Ulcers");
                            }
                            if (ncdmd.lastRecordOfOral.growthOfRecentOrigins) {
                                ncdmd.oralHistoryArray.push("Growth of recent origins");
                            }
                            ncdmd.oralHistoryDisplay = ncdmd.oralHistoryArray.join();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberOralDto.previousFollowUpDate = ncdmd.member.memberOralDto.followUpDate;
                    ncdmd.member.memberOralDto.followUpDate = null;
                    ncdmd.member.memberOralDto.referralId = ncdmd.member.memberOralDto.id
                    let temp = ncdmd.member.memberOralDto.status;
                    ncdmd.member.memberOralDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdmd.showOralSubStatus = true;
                            ncdmd.showOralReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdmd.showOralDirectStatus = true;
                            ncdmd.showOralReferral = true;
                            ncdmd.member.memberOralDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.oralForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberBreastDto != null) {
                    NcdDAO.retrieveLastRecordForBreastByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfBreast = response;
                        if (ncdmd.lastRecordOfBreast != null) {
                            ncdmd.breastHistoryArray = [];
                            if (ncdmd.lastRecordOfBreast.sizeChange) {
                                ncdmd.breastHistoryArray.push("Change in shape /size of the breast");
                            }
                            if (ncdmd.lastRecordOfBreast.nippleNotOnSameLevel) {
                                ncdmd.breastHistoryArray.push("Nipples not at same level");
                            }
                            if (ncdmd.lastRecordOfBreast.anyRetractionOfNipple) {
                                ncdmd.breastHistoryArray.push("Retraction of nipples");
                            }
                            if (ncdmd.lastRecordOfBreast.lymphadenopathy) {
                                ncdmd.breastHistoryArray.push("Lymphadenopathy");
                            }
                            if (ncdmd.lastRecordOfBreast.dischargeFromNipple) {
                                ncdmd.breastHistoryArray.push("Discharge from nipples");
                            }
                            if (ncdmd.lastRecordOfBreast.visualSkinDimplingRetraction) {
                                ncdmd.breastHistoryArray.push("Skin dimpling/puckering");
                            }
                            if (ncdmd.lastRecordOfBreast.visualLumpInBreast) {
                                ncdmd.breastHistoryArray.push("Lump in breasts");
                            }
                            ncdmd.breastHistoryDisplay = ncdmd.breastHistoryArray.join()
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberBreastDto.previousFollowUpDate = ncdmd.member.memberBreastDto.followUpDate;
                    ncdmd.member.memberBreastDto.followUpDate = null;
                    ncdmd.member.memberBreastDto.referralId = ncdmd.member.memberBreastDto.id
                    let temp = ncdmd.member.memberBreastDto.status;
                    ncdmd.member.memberBreastDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdmd.showBreastSubStatus = true;
                            ncdmd.showBreastReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdmd.showBreastDirectStatus = true;
                            ncdmd.showBreastReferral = true;
                            ncdmd.member.memberBreastDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.breastForm.$setPristine();
                    }
                }
                if (ncdmd.member.memberCervicalDto != null) {
                    NcdDAO.retrieveLastRecordForCervicalByMemberId($state.params.id).then(function (response) {
                        ncdmd.lastRecordOfCervical = response;
                        if (ncdmd.lastRecordOfCervical != null) {
                            ncdmd.cervicalHistoryArray = [];
                            if (ncdmd.lastRecordOfCervical.papsmearTest) {
                                ncdmd.cervicalHistoryArray.push("PAP Smear positive")
                            }
                            if (ncdmd.lastRecordOfCervical.viaTest) {
                                ncdmd.cervicalHistoryArray.push("VIA positive")
                            }
                            ncdmd.cervicalHistoryDisplay = ncdmd.cervicalHistoryArray.join();
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                    ncdmd.member.memberCervicalDto.previousFollowUpDate = ncdmd.member.memberCervicalDto.followUpDate;
                    ncdmd.member.memberCervicalDto.followUpDate = null;
                    ncdmd.member.memberCervicalDto.referralId = ncdmd.member.memberCervicalDto.id
                    let temp = ncdmd.member.memberCervicalDto.status;
                    ncdmd.member.memberCervicalDto.status = null;
                    switch (temp) {
                        case 'SUSPECTED':
                        case 'CONFIRMATION_PENDING':
                            ncdmd.showCervicalSubStatus = true;
                            ncdmd.showCervicalReferral = true
                            break;
                        case 'CONFIRMED':
                        case 'REFERRED':
                            ncdmd.showCervicalDirectStatus = true;
                            ncdmd.showCervicalReferral = true;
                            ncdmd.member.memberCervicalDto.status = temp;
                            break;
                    }
                    if (isSaveAction) {
                        ncdmd.cervicalForm.$setPristine();
                    }
                }

                if (ncdmd.member.basicDetails.gender == 'M') {
                    ncdmd.wc = wcForMales;
                } else {
                    ncdmd.wc = wcForFemales;
                }
                ncdmd.member.basicDetails.age = getAge(ncdmd.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }
        ncdmd.saveHyperTension = function () {
            ncdmd.hypertensionForm.$setSubmitted();
            if (ncdmd.hypertensionForm.$valid) {
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
                    ncdmd.member.memberHypertensionDto.referredFromInstitute = JSON.parse(ncdmd.member.memberHypertensionDto.referredFromInstitute);
                    ncdmd.member.memberHypertensionDto.referredFromHealthInfrastructureId = ncdmd.member.memberHypertensionDto.referredFromInstitute.id
                    if (!ncdmd.member.memberHypertensionDto.memberId) {
                        ncdmd.member.memberHypertensionDto.memberId = Number($state.params.id);
                    }
                    if (ncdmd.member.memberHypertensionDto.startTreatment || ncdmd.showHypertensionMedicines) {
                        ncdmd.member.memberHypertensionDto.status = 'TREATMENT_STARTED';
                    }
                    if (ncdmd.member.memberHypertensionDto.referralDto != null && ncdmd.member.memberHypertensionDto.referralDto.isReferred) {
                        ncdmd.member.memberHypertensionDto.status = 'REFERRED';
                        ncdmd.member.memberHypertensionDto.reason = ncdmd.member.memberHypertensionDto.referralDto.reason;
                        ncdmd.member.memberHypertensionDto.healthInfraId = ncdmd.member.memberHypertensionDto.referralDto.healthInfraId;
                    }
                    NcdDAO.saveHyperTension(ncdmd.member.memberHypertensionDto).then(() => {
                        toaster.pop('success', "Hypertension Details saved successfully");
                        ncdmd.hypertensionForm.$setPristine();
                        if (ncdmd.rights.isShowHIPModal) {
                            ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('HT', ncdmd.member.memberHypertensionDto.screeningDate);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                        ncdmd.retrieveMemberDetails(true);
                    })
                });
            }
        };
        ncdmd.saveGeneral = function () {
            ncdmd.GeneralForm.$setSubmitted();
            if (ncdmd.GeneralForm.$valid) {
                ncdmd.member.memberGeneralDto.referredFromInstitute = JSON.parse(ncdmd.member.memberGeneralDto.referredFromInstitute);
                ncdmd.member.memberGeneralDto.referredFromHealthInfrastructureId = ncdmd.member.memberGeneralDto.referredFromInstitute.id
                if (!ncdmd.member.memberGeneralDto.memberId) {
                    ncdmd.member.memberGeneralDto.memberId = Number($state.params.id);
                }
                if (ncdmd.member.memberGeneralDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    ncdmd.member.memberGeneralDto.status = 'TREATMENT_STARTED';
                }
                if (ncdmd.member.memberGeneralDto.referralDto != null && ncdmd.member.memberGeneralDto.referralDto.isReferred) {
                    ncdmd.member.memberGeneralDto.status = 'REFERRED';
                    ncdmd.member.memberGeneralDto.reason = ncdmd.member.memberGeneralDto.referralDto.reason;
                    ncdmd.member.memberGeneralDto.healthInfraId = ncdmd.member.memberGeneralDto.referralDto.healthInfraId;

                }
                NcdDAO.saveGeneral(ncdmd.member.memberGeneralDto).then(function () {
                    toaster.pop('success', "General Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('G', ncdmd.member.memberGeneralDto.screeningDate);
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.GeneralForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveInitialAssessment = function () {
            ncdmd.initialAssessmentForm.$setSubmitted();
            if (ncdmd.initialAssessmentForm.$valid) {
                ncdmd.member.memberInitialAssessmentDto.referredFromInstitute = JSON.parse(ncdmd.member.memberInitialAssessmentDto.referredFromInstitute);
                ncdmd.member.memberInitialAssessmentDto.referredFromHealthInfrastructureId = ncdmd.member.memberInitialAssessmentDto.referredFromInstitute.id
                if (!ncdmd.member.memberInitialAssessmentDto.memberId) {
                    ncdmd.member.memberInitialAssessmentDto.memberId = Number($state.params.id);
                }
                if (ncdmd.member.memberInitialAssessmentDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    ncdmd.member.memberInitialAssessmentDto.status = 'TREATMENT_STARTED';
                }
                if (ncdmd.member.memberInitialAssessmentDto.referralDto != null && ncdmd.member.memberInitialAssessmentDto.referralDto.isReferred) {
                    ncdmd.member.memberInitialAssessmentDto.status = 'REFERRED';
                    ncdmd.member.memberInitialAssessmentDto.reason = ncdmd.member.memberInitialAssessmentDto.referralDto.reason;
                    ncdmd.member.memberInitialAssessmentDto.healthInfraId = ncdmd.member.memberInitialAssessmentDto.referralDto.healthInfraId;

                }
                NcdDAO.saveInitialAssessment(ncdmd.member.memberInitialAssessmentDto).then(function () {
                    toaster.pop('success', "InitialAssessment Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('IA', ncdmd.member.memberInitialAssessmentDto.screeningDate);
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.initialAssessmentForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveMentalHealth = function () {
            ncdmd.MentalHealthForm.$setSubmitted();
            if (ncdmd.MentalHealthForm.$valid) {
                ncdmd.member.memberMentalHealthDto.referredFromInstitute = JSON.parse(ncdmd.member.memberMentalHealthDto.referredFromInstitute);
                ncdmd.member.memberMentalHealthDto.referredFromHealthInfrastructureId = ncdmd.member.memberMentalHealthDto.referredFromInstitute.id
                if (!ncdmd.member.memberMentalHealthDto.memberId) {
                    ncdmd.member.memberMentalHealthDto.memberId = Number($state.params.id);
                }
                if (ncdmd.member.memberMentalHealthDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    ncdmd.member.memberMentalHealthDto.status = 'TREATMENT_STARTED';
                }
                if (ncdmd.member.memberMentalHealthDto.referralDto != null && ncdmd.member.memberMentalHealthDto.referralDto.isReferred) {
                    ncdmd.member.memberMentalHealthDto.status = 'REFERRED';
                    ncdmd.member.memberMentalHealthDto.reason = ncdmd.member.memberMentalHealthDto.referralDto.reason;
                    ncdmd.member.memberMentalHealthDto.healthInfraId = ncdmd.member.memberMentalHealthDto.referralDto.healthInfraId;

                }
                NcdDAO.saveMentalHealth(ncdmd.member.memberMentalHealthDto).then(function () {
                    toaster.pop('success', "MentalHealth Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('MH', ncdmd.member.memberMentalHealthDto.screeningDate);
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.MentalHealthForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveDiabetes = function () {
            ncdmd.diabetesForm.$setSubmitted();
            if (ncdmd.diabetesForm.$valid) {
                ncdmd.member.memberDiabetesDto.referredFromInstitute = JSON.parse(ncdmd.member.memberDiabetesDto.referredFromInstitute);
                ncdmd.member.memberDiabetesDto.referredFromHealthInfrastructureId = ncdmd.member.memberDiabetesDto.referredFromInstitute.id
                if (!ncdmd.member.memberDiabetesDto.memberId) {
                    ncdmd.member.memberDiabetesDto.memberId = Number($state.params.id);
                }
                if (ncdmd.member.memberDiabetesDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    ncdmd.member.memberDiabetesDto.status = 'TREATMENT_STARTED';
                }
                if (ncdmd.member.memberDiabetesDto.referralDto != null && ncdmd.member.memberDiabetesDto.referralDto.isReferred) {
                    ncdmd.member.memberDiabetesDto.status = 'REFERRED';
                    ncdmd.member.memberDiabetesDto.reason = ncdmd.member.memberDiabetesDto.referralDto.reason;
                    ncdmd.member.memberDiabetesDto.healthInfraId = ncdmd.member.memberDiabetesDto.referralDto.healthInfraId;

                }
                NcdDAO.saveDiabetes(ncdmd.member.memberDiabetesDto).then(function () {
                    toaster.pop('success', "Diabetes Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('D', ncdmd.member.memberDiabetesDto.screeningDate);
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.diabetesForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveCervical = function () {
            if (ncdmd.cervicalForm.$valid) {
                ncdmd.member.memberCervicalDto.referredFromInstitute = JSON.parse(ncdmd.member.memberCervicalDto.referredFromInstitute);
                ncdmd.member.memberCervicalDto.referredFromHealthInfrastructureId = ncdmd.member.memberCervicalDto.referredFromInstitute.id
                if (!ncdmd.member.memberCervicalDto.memberId) {
                    ncdmd.member.memberCervicalDto.memberId = Number($state.params.id);
                }
                setCervicalVisualDto();
                if (ncdmd.member.memberCervicalDto.referralDto != null && ncdmd.member.memberCervicalDto.referralDto.isReferred) {
                    ncdmd.member.memberCervicalDto.reason = ncdmd.member.memberCervicalDto.referralDto.reason;
                    ncdmd.member.memberCervicalDto.healthInfraId = ncdmd.member.memberCervicalDto.referralDto.healthInfraId;
                }
                if (ncdmd.member.memberCervicalDto.referralDto != null && ncdmd.member.memberCervicalDto.referralDto.isReferred && ncdmd.member.memberCervicalDto.status != 'SUSPECTED' && ncdmd.member.memberCervicalDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberCervicalDto.status = 'REFERRED';
                }
                NcdDAO.saveCervical(ncdmd.member.memberCervicalDto).then(function () {
                    toaster.pop('success', "Cervical Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('C', ncdmd.member.memberCervicalDto.screeningDate);
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.cervicalForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveOral = function () {
            if (ncdmd.oralForm.$valid) {
                ncdmd.member.memberOralDto.referredFromInstitute = JSON.parse(ncdmd.member.memberOralDto.referredFromInstitute);
                ncdmd.member.memberOralDto.referredFromHealthInfrastructureId = ncdmd.member.memberOralDto.referredFromInstitute.id
                if (!ncdmd.member.memberOralDto.memberId) {
                    ncdmd.member.memberOralDto.memberId = Number($state.params.id);
                }
                setOralTests();
                if (ncdmd.member.memberOralDto.referralDto != null && ncdmd.member.memberOralDto.referralDto.isReferred) {
                    ncdmd.member.memberOralDto.reason = ncdmd.member.memberOralDto.referralDto.reason;
                    ncdmd.member.memberOralDto.healthInfraId = ncdmd.member.memberOralDto.referralDto.healthInfraId;
                }
                if (ncdmd.member.memberOralDto.referralDto != null && ncdmd.member.memberOralDto.referralDto.isReferred && ncdmd.member.memberOralDto.status != 'SUSPECTED' && ncdmd.member.memberOralDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberOralDto.status = 'REFERRED';
                }
                NcdDAO.saveOral(ncdmd.member.memberOralDto).then(function () {
                    toaster.pop('success', "Oral Details saved successfully");
                    ncdmd.showOral = false;
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('O', ncdmd.member.memberOralDto.screeningDate)
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.oralForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };
        ncdmd.saveBreast = function () {
            ncdmd.breastForm.$setSubmitted();
            if (ncdmd.breastForm.$valid) {
                ncdmd.member.memberBreastDto.referredFromInstitute = JSON.parse(ncdmd.member.memberBreastDto.referredFromInstitute);
                ncdmd.member.memberBreastDto.referredFromHealthInfrastructureId = ncdmd.member.memberBreastDto.referredFromInstitute.id
                if (!ncdmd.member.memberBreastDto.memberId) {
                    ncdmd.member.memberBreastDto.memberId = Number($state.params.id);
                }
                setBreastVisualDto();
                if (ncdmd.member.memberBreastDto.referralDto != null && ncdmd.member.memberBreastDto.referralDto.isReferred) {
                    ncdmd.member.memberBreastDto.reason = ncdmd.member.memberBreastDto.referralDto.reason;
                    ncdmd.member.memberBreastDto.healthInfraId = ncdmd.member.memberBreastDto.referralDto.healthInfraId;
                }
                if (ncdmd.member.memberBreastDto.referralDto != null && ncdmd.member.memberBreastDto.referralDto.isReferred && ncdmd.member.memberBreastDto.status != 'SUSPECTED' && ncdmd.member.memberBreastDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberBreastDto.status = 'REFERRED';
                }
                NcdDAO.saveBreast(ncdmd.member.memberBreastDto).then(function () {
                    toaster.pop('success', "Breast Details saved successfully");
                    if (ncdmd.rights.isShowHIPModal) {
                        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode('B', ncdmd.member.memberBreastDto.screeningDate)
                    }
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.breastForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ncdmd.getMemberReferralIdByMemberIdAndDiseaseCode = (diseaseCode, screeningDate) => {
            QueryDAO.execute({
                code: 'get_member_referral_id_by_member_id_and_disease_code',
                parameters: {
                    member_id: ncdmd.member.basicDetails.id,
                    disease_code: diseaseCode
                }
            }).then(function (res) {
                ncdmd.saveHealthIdDetails(screeningDate, res.result[0].id);
            })
        }

        ncdmd.saveHealthIdDetails = (screeningDate, serviceId) => {
            let memberObj = {
                memberId: ncdmd.member.basicDetails.id,
                mobileNumber: ncdmd.member.basicDetails.mobileNumber,
                name: ncdmd.member.basicDetails.firstName + " " + ncdmd.member.basicDetails.lastName,
                preferredHealthId: ncdmd.prefferedHealthId,
                healthIdsData: ncdmd.healthIdsData
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
                            preferredHealthId: ncdmd.prefferedHealthId || null,
                            healthIdsData: ncdmd.healthIdsData || [],
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
            if (!ncdmd.member.memberOtherInfoDto) {
                ncdmd.member.memberOtherInfoDto = { doneBy: 'FHW' };
            }
            ncdmd.member.memberOtherInfoDto.isMoScreeningDone = true;
            let todayDate = new Date();

            todayDate.setHours(0, 0, 0, 0);
            ncdmd.member.memberOtherInfoDto.doneOn = todayDate;
            setDoneBy();
            return NcdDAO.save(ncdmd.member);
        };

        ncdmd.setAllOralTest = function () {
            ncdmd.subMenuSelected = 'all';
            if (!ncdmd.member.memberOralDto.displayList) {
                ncdmd.member.memberOralDto.displayList = {};
            }
            ncdmd.member.memberOralDto.displayList['all'] = {};
            ncdmd.member.memberOralDto[ncdmd.subMenuSelected] = {};
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];
            let keys = _.keys(ncdmd.member.memberOralDto.oralList);
            ncdmd.member.memberOralDto.oralList['all'] = {}
            _.each(keys, function (keyTest) {
                if (keyTest != 'all') {
                    _.each(points, function (point) {
                        if (ncdmd.member.memberOralDto.oralList[keyTest][point]) {
                            if (!ncdmd.member.memberOralDto.displayList['all'][point]) {
                                ncdmd.member.memberOralDto.displayList['all'][point] = []
                            }
                            ncdmd.member.memberOralDto.oralList['all'][point] = true;
                            let stringToBeReplaced = keyTest.replace(/([A-Z])/g, ' $1').replace(/^./, function (str) { return str.toUpperCase() });
                            ncdmd.member.memberOralDto.displayList['all'][point].push(stringToBeReplaced);
                        }
                    });
                }


            })
        };
        ncdmd.setDisplayList = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];
            if (ncdmd.subMenuSelected != 'all') {
                if (!ncdmd.member.memberOralDto.displayList) {
                    ncdmd.member.memberOralDto.displayList = {};
                }
                ncdmd.member.memberOralDto.displayList[ncdmd.subMenuSelected] = {};
                _.each(points, function (point) {
                    if (ncdmd.member.memberOralDto.oralList[ncdmd.subMenuSelected][point]) {
                        if (!ncdmd.member.memberOralDto.displayList[ncdmd.subMenuSelected][point]) {
                            ncdmd.member.memberOralDto.displayList[ncdmd.subMenuSelected][point] = []
                        }
                        let stringToBeReplaced = ncdmd.subMenuSelected.replace(/([A-Z])/g, ' $1').replace(/^./, function (str) { return str.toUpperCase() });
                        ncdmd.member.memberOralDto.displayList[ncdmd.subMenuSelected][point].push(stringToBeReplaced);
                    }

                })
            }

        }

        ncdmd.saveComplaint = function () {
            if (ncdmd.member.complaint) {
                NcdDAO.saveComplaint({ complaint: ncdmd.member.complaint, memberId: ncdmd.member.id }).then(function (res) {
                    toaster.pop('success', "Complaint/Diagnosis Saved Successfully");
                    if (!ncdmd.member.complaints) {
                        ncdmd.member.complaints = [];
                    }
                    ncdmd.member.complaints.push({ complaint: angular.copy(ncdmd.member.complaint), doneOn: new Date(), doneBy: ncdmd.loggedInUser.name });
                    ncdmd.member.complaint = '';
                })
            }


        };
        var setOralTests = function () {
            _.each(ncdmd.member.memberOralDto.oralList, function (value, key) {
                ncdmd.member.memberOralDto[key] = [];
                _.each(value, function (v, k) {
                    if (value[k]) {
                        ncdmd.member.memberOralDto[key].push(k);
                    }
                })
                ncdmd.member.memberOralDto[key] = ncdmd.member.memberOralDto[key].join();
            })
        };
        var setOralTestsForDisplay = function () {
            if (ncdmd.member.memberOralDto) {
                ncdmd.member.memberOralDto.displayList = {};
                ncdmd.member.memberOralDto.oralList = {};
                if (ncdmd.member.memberOralDto.whitePatches) {
                    let res = ncdmd.member.memberOralDto.whitePatches.split(",");
                    ncdmd.member.memberOralDto.oralList.whitePatches = {};

                    ncdmd.member.memberOralDto.displayList.whitePatches = {}
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.whitePatches[point] = true;
                        ncdmd.member.memberOralDto.displayList.whitePatches[point] = ["White Patches"];
                    })

                }
                if (ncdmd.member.memberOralDto.redPatches) {
                    let res = ncdmd.member.memberOralDto.redPatches.split(",");
                    ncdmd.member.memberOralDto.oralList.redPatches = {}
                    ncdmd.member.memberOralDto.displayList.redPatches = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.redPatches[point] = true;
                        ncdmd.member.memberOralDto.displayList.redPatches[point] = ["Red Patches"];
                    })
                }
                if (ncdmd.member.memberOralDto.nonHealingUlcers) {
                    let res = ncdmd.member.memberOralDto.nonHealingUlcers.split(",");
                    ncdmd.member.memberOralDto.oralList.nonHealingUlcers = {};

                    ncdmd.member.memberOralDto.displayList.nonHealingUlcers = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.nonHealingUlcers[point] = true;
                        ncdmd.member.memberOralDto.displayList.nonHealingUlcers[point] = ["Non Healing Ulcers"];
                    })
                }
                if (ncdmd.member.memberOralDto.growthOfRecentOrigins) {
                    let res = ncdmd.member.memberOralDto.growthOfRecentOrigins.split(",");
                    ncdmd.member.memberOralDto.oralList.growthOfRecentOrigins = {}

                    ncdmd.member.memberOralDto.displayList.growthOfRecentOrigins = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.growthOfRecentOrigins[point] = true;
                        ncdmd.member.memberOralDto.displayList.growthOfRecentOrigins[point] = ["Growth Of Recent Origins"];
                    })
                }
                if (ncdmd.member.memberOralDto.lichenPlanus) {
                    let res = ncdmd.member.memberOralDto.lichenPlanus.split(",");
                    ncdmd.member.memberOralDto.oralList.lichenPlanus = {}

                    ncdmd.member.memberOralDto.displayList.lichenPlanus = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.lichenPlanus[point] = true;
                        ncdmd.member.memberOralDto.displayList.lichenPlanus[point] = ["Lichen Planus"];
                    })
                }
                if (ncdmd.member.memberOralDto.smokersPalate) {
                    let res = ncdmd.member.memberOralDto.smokersPalate.split(",");
                    ncdmd.member.memberOralDto.oralList.smokersPalate = {}

                    ncdmd.member.memberOralDto.displayList.smokersPalate = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.smokersPalate[point] = true;
                        ncdmd.member.memberOralDto.displayList.smokersPalate[point] = ["Smokers palate"];
                    })
                }
                if (ncdmd.member.memberOralDto.submucousFibrosis) {
                    let res = ncdmd.member.memberOralDto.submucousFibrosis.split(",");
                    ncdmd.member.memberOralDto.oralList.submucousFibrosis = {}

                    ncdmd.member.memberOralDto.displayList.submucousFibrosis = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.submucousFibrosis[point] = true;
                        ncdmd.member.memberOralDto.displayList.submucousFibrosis[point] = ["Submucous fibrosis"];
                    })
                }
                if (ncdmd.member.memberOralDto.restrictedMouthOpening) {
                    let res = ncdmd.member.memberOralDto.restrictedMouthOpening.split(",");
                    ncdmd.member.memberOralDto.oralList.restrictedMouthOpening = {};

                    ncdmd.member.memberOralDto.displayList.restrictedMouthOpening = [];
                    _.each(res, function (point) {
                        ncdmd.member.memberOralDto.oralList.restrictedMouthOpening[point] = true;
                        ncdmd.member.memberOralDto.displayList.restrictedMouthOpening[point] = ["Restricted Mouth Opening"];
                    })
                }


            }
        }
        var setBreastVisualDto = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26];
            let keys = _.keys(ncdmd.member.memberBreastDto.breastList)
            _.each(keys, function (keyTest) {
                ncdmd.member.memberBreastDto[keyTest] = [];
                _.each(points, function (point) {

                    if (ncdmd.member.memberBreastDto.breastList[keyTest][point]) {

                        ncdmd.member.memberBreastDto[keyTest].push(point);

                    }
                });
                ncdmd.member.memberBreastDto[keyTest] = ncdmd.member.memberBreastDto[keyTest].join(',');

            })

        };
        var setBreastVisual = function () {
            if (ncdmd.member.memberBreastDto) {
                ncdmd.member.memberBreastDto.breastList = {};
                if (ncdmd.member.memberBreastDto.visualSkinDimplingRetraction) {
                    let res = ncdmd.member.memberBreastDto.visualSkinDimplingRetraction.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualSkinDimplingRetraction = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualSkinDimplingRetraction[point] = true;

                    })
                }
                if (ncdmd.member.memberBreastDto.visualNippleRetractionDistortion) {
                    let res = ncdmd.member.memberBreastDto.visualNippleRetractionDistortion.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualNippleRetractionDistortion = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualNippleRetractionDistortion[point] = true;

                    })
                }
                if (ncdmd.member.memberBreastDto.visualDischargeFromNipple) {
                    let res = ncdmd.member.memberBreastDto.visualDischargeFromNipple.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualDischargeFromNipple = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualDischargeFromNipple[point] = true;

                    })
                }
                if (ncdmd.member.memberBreastDto.visualUlceration) {
                    let res = ncdmd.member.memberBreastDto.visualUlceration.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualUlceration = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualUlceration[point] = true;

                    })
                }
                if (ncdmd.member.memberBreastDto.visualSkinRetraction) {
                    let res = ncdmd.member.memberBreastDto.visualSkinRetraction.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualSkinRetraction = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualSkinRetraction[point] = true;

                    })
                }
                if (ncdmd.member.memberBreastDto.visualLumpInBreast) {
                    let res = ncdmd.member.memberBreastDto.visualLumpInBreast.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualLumpInBreast = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualLumpInBreast[point] = true;

                    })
                }
            }
        };

        // var setCervicalVisualDto = function () {
        //     let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        //     if (ncdmd.member.memberCervicalDto.viaExamPoints !== 'negative') {
        //         ncdmd.member.memberCervicalDto.viaExamPoints = [];
        //         _.each(points, function (point) {
        //             if (!ncdmd.member.memberCervicalDto.examPoints) {
        //                 ncdmd.member.memberCervicalDto.examPoints = [];
        //             }
        //             if (ncdmd.member.memberCervicalDto.examPoints[point]) {
        //                 ncdmd.member.memberCervicalDto.viaExamPoints.push(point);
        //             }
        //         });
        //         ncdmd.member.memberCervicalDto.viaExamPoints = ncdmd.member.memberCervicalDto.viaExamPoints.join(',');
        //     } else {
        //         ncdmd.member.memberCervicalDto.viaExamPoints = '';
        //     }
        // };



        var setCervicalVisualDto = function () {
            let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
            let keys = _.keys(ncdmd.member.memberCervicalDto.examPoints)
            _.each(keys, function (keyTest) {
                ncdmd.member.memberCervicalDto[keyTest] = [];
                _.each(points, function (point) {

                    if (ncdmd.member.memberCervicalDto.examPoints[keyTest][point]) {
                        ncdmd.member.memberCervicalDto[keyTest].push(point);
                    }
                });
                ncdmd.member.memberCervicalDto[keyTest] = ncdmd.member.memberCervicalDto[keyTest].join(',');
            })
        };



        var setCervicalVisual = function () {
            if (ncdmd.member.memberCervicalDto) {
                ncdmd.member.memberCervicalDto.examPoints = {};
                // if (ncdmd.member.memberCervicalDto.viaExamPoints !== 'negative') {
                if (ncdmd.member.memberCervicalDto.viaExamPoints) {
                    let res = ncdmd.member.memberCervicalDto.viaExamPoints.split(",");
                    ncdmd.member.memberCervicalDto.examPoints.viaExamPoints = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberCervicalDto.examPoints[point] = true;
                    })
                }
                // }
            }
        };

        ncdmd.navigateToTab = function (id) {
            // if (id == "oral-main") {
            //     ncdmd.subMenuSelected = 'whitePatches'
            //     ncdmd.oralForm.$setPristine();
            // } else if (id == "breast-main") {
            //     ncdmd.breastForm.$setPristine();
            // } else if (id == "cervical-main") {
            //     ncdmd.cervicalForm.$setPristine();
            // }
            // //$('#' + id).tab('show');
            $state.go("techo.ncd.memberdetails.initialassessment")
        };
        ncdmd.retrieveComplaints = function () {
            Mask.show();
            NcdDAO.retrieveComplaintsByMemberId($state.params.id).then(function (res) {
                ncdmd.member.complaints = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });

        };
        ncdmd.saveDiagnosis = function (diagnosisDto, diseaseCode, idToNavigate) {
            Mask.show();
            diagnosisDto.diseaseCode = diseaseCode;
            diagnosisDto.memberId = Number($state.params.id);
            switch (diseaseCode) {

                case 'HT':
                    ncdmd.member.memberHypertensionDto.diagnosisDto.readings = ncdmd.member.memberHypertensionDto.systolicBloodPressure + '/' + ncdmd.member.memberHypertensionDto.diastolicBloodPressure;
                    break;
                case 'D':
                    ncdmd.member.memberDiabetesDto.diagnosisDto.readings = 'Fasting: ' + ncdmd.member.memberDiabetesDto.fastingBloodSugar + ' mg/dl Post Prandial: ' + ncdmd.member.memberDiabetesDto.postPrandialBloodSugar + ' mg/dl';
                    break;
            }
            NcdDAO.saveDiagnosis(diagnosisDto).then(function (res) {
                toaster.pop('success', 'Diagnosis Saved Successfully!');
                ncdmd.retrieveTreatmentHistory(diseaseCode);
                switch (diseaseCode) {
                    case 'IA':
                        ncdmd.member.memberInitialAssessmentDto.diagnosisDto = {};
                        break;
                    case 'G':
                        ncdmd.member.memberGeneralDto.diagnosisDto = {};
                        break;
                    case 'MH':
                        ncdmd.member.memberMentalHealthDto.diagnosisDto = {};
                        break;
                    case 'HT':
                        ncdmd.member.memberHypertensionDto.diagnosisDto = {};
                        break;
                    case 'D':
                        ncdmd.member.memberDiabetesDto.diagnosisDto = {};
                        break;
                    case 'B':
                        ncdmd.member.memberBreastDto.diagnosisDto = {};
                        break;
                    case 'C':
                        ncdmd.member.memberCervicalDto.diagnosisDto = {};
                        break;
                    case 'O':
                        ncdmd.member.memberOralDto.diagnosisDto = {};
                        break;
                }
                ncdmd.navigateToTab(idToNavigate)
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

        ncdmd.retrieveTreatmentHistory = function (diseaseCode) {
            return NcdDAO.retrieveTreatmentHistory(Number($state.params.id), diseaseCode).then(function (res) {
                switch (diseaseCode) {
                    case 'IA':
                        if (!ncdmd.member.memberInitialAssessmentDto) {
                            ncdmd.member.memberInitialAssessmentDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberInitialAssessmentDto.treatmentHistory = res;
                        break;
                    case 'MH':
                        if (!ncdmd.member.memberMentalHealthDto) {
                            ncdmd.member.memberMentalHealthDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberMentalHealthDto.treatmentHistory = res;
                        break;
                    case 'G':
                        if (!ncdmd.member.memberGeneralDto) {
                            ncdmd.member.memberGeneralDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberGeneralDto.treatmentHistory = res;
                        break;
                    case 'HT':
                        if (!ncdmd.member.memberHypertensionDto) {
                            ncdmd.member.memberHypertensionDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberHypertensionDto.treatmentHistory = res;
                        break;
                    case 'D':
                        if (!ncdmd.member.memberDiabetesDto) {
                            ncdmd.member.memberDiabetesDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberDiabetesDto.treatmentHistory = res;
                        break;
                    case 'O':
                        if (!ncdmd.member.memberOralDto) {
                            ncdmd.member.memberOralDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberOralDto.treatmentHistory = res;
                        break;
                    case 'B':
                        if (!ncdmd.member.memberBreastDto) {
                            ncdmd.member.memberBreastDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberBreastDto.treatmentHistory = res;
                        break;
                    case 'C':
                        if (!ncdmd.member.memberCervicalDto) {
                            ncdmd.member.memberCervicalDto = {};
                        }
                        res.forEach(e => e.diagnosis = _convertConstToValue(e.diagnosis));
                        ncdmd.member.memberCervicalDto.treatmentHistory = res;
                        break;
                }
            })
        };

        ncdmd.setDiagnosis = function (diseaseCode) {
            if (diseaseCode == 'HT') {
                if (ncdmd.member.memberHypertensionDto) {
                    if (!ncdmd.member.memberHypertensionDto.diagnosisDto) {
                        ncdmd.member.memberHypertensionDto.diagnosisDto = {};
                    }
                    // Old logic based on set diagnosis which having textarea to fill
                    /* if ((ncdmd.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90) && ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 120 && ncdmd.member.memberHypertensionDto.systolicBloodPressure < 140) || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 80 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90))) {
                        ncdmd.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Pre-Hypertension';
                    } else if ((ncdmd.member.memberHypertensionDto.systolicBloodPressure < 160 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 100) && ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 140 && ncdmd.member.memberHypertensionDto.systolicBloodPressure < 160) || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 90 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 100))) {
                        ncdmd.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Stage 1 Treatment';
                    } else if ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 160) || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure > 100)) {
                        ncdmd.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Stage 2 Treatment';
                    } */
                    // New logic based on set diagnosis which having dropdown value to select
                    if (((ncdmd.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90)
                        && ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 120 && ncdmd.member.memberHypertensionDto.systolicBloodPressure < 140)
                            || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 80 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90)))
                        || ((ncdmd.member.memberHypertensionDto.systolicBloodPressure < 160 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 100)
                            && ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 140 && ncdmd.member.memberHypertensionDto.systolicBloodPressure < 160)
                                || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 90 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 100)))
                        || ((ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 160) || (ncdmd.member.memberHypertensionDto.diastolicBloodPressure > 100))) {
                        ncdmd.member.memberHypertensionDto.diagnosisDto.diagnosis = 'Confirmed';
                    } else {
                        ncdmd.member.memberHypertensionDto.diagnosisDto.diagnosis = 'No Abnormality Detected';
                    }
                }
            } else if (diseaseCode == 'D') {
                if (ncdmd.member.memberDiabetesDto) {
                    if (!ncdmd.member.memberDiabetesDto.diagnosisDto) {
                        ncdmd.member.memberDiabetesDto.diagnosisDto = {};
                    }
                    // Old logic based on set diagnosis which having textarea to fill
                    /* if (ncdmd.member.memberDiabetesDto.fastingBloodSugar >= 126 || ncdmd.member.memberDiabetesDto.postPrandialBloodSugar >= 200) {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'Diabetes Mellitus';
                    } else {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'No Abnormality';
                    } */
                    // New logic based on set diagnosis which having dropdown value to select
                    if (ncdmd.member.memberDiabetesDto.fastingBloodSugar >= 126 || ncdmd.member.memberDiabetesDto.postPrandialBloodSugar >= 200) {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'Confirmed';
                    } else {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'No Abnormality Detected';
                    }
                }
            }
        };

        ncdmd.saveReferral = function (referralDto, diseaseCode, previousRefId) {
            referralDto.diseaseCode = diseaseCode;
            referralDto.referredFrom = $state.params.type;
            referralDto.referredBy = ncdmd.loggedInUser.id;
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

        ncdmd.openFollowupModal = function () {
            var followupObj = {
                userHealthInfras: ncdmd.userHealthInfras,
                followup: {}
            };
            followup.followup.memberId = Number($state.params.id);
            followup.followup.referralFrom = $state.params.type
            followup.followup.isFemale = ncdmd.member.basicDetails.gender == 'M' ? false : true
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
        ncdmd.retrieveHealthInfras = function (type) {
            if (type) {
                QueryDAO.execute({
                    code: 'health_infra_retrival_by_type',
                    parameters: {
                        type: type
                    }
                }).then(function (res) {
                    ncdmd.healthInfras = res;
                })
            }
        };
        ncdmd.printPatientSummary = function () {
            var header = "<div class='print_header display-none'><img class='print_logo' src=\"img/techo_plus.png\" ><h2 class='page_title'>" + "Patient Summary " + "</h2></div>";
            ncdmd.footer = "Generated by " + ncdmd.loggedInUser.name + " at " + new Date().toLocaleString();
            setSymptoms();
            let promiseList = [];
            promiseList.push(ncdmd.retrieveTreatmentHistory('HT'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('O'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('C'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('B'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('D'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('IA'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('G'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('MH'));
            promiseList.push(ncdmd.retrieveReffForToday($state.params.id));
            promiseList.push(ncdmd.retrieveNextFollowup($state.params.id));
            $q.all(promiseList).then(function (res) {
                $('#printableDiv').printThis({
                    debug: false,
                    importCSS: false,
                    loadCSS: ['styles/css/printable.css', 'styles/css/ncd_printable.css'],
                    header: header,
                    printDelay: 333,
                    base: "./",
                    pageTitle: ncdmd.appName
                });
            })


        };

        ncdmd.retrieveReffForToday = function (memberid) {
            Mask.show()
            return NcdDAO.retrieveReffForToday(memberid).then(function (res) {
                ncdmd.member.reffForToday = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
        }
        ncdmd.retrieveNextFollowup = function (memberid) {
            Mask.show()
            return NcdDAO.retrieveNextFollowup(memberid).then(function (res) {
                ncdmd.member.nextFollowup = {};
                // ncdmd.member.nextFollowup=res;
                _.map(res, function (followup) {
                    ncdmd.member.nextFollowup[followup.diseaseCode] = followup.followupDate;
                })


            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
        };

        ncdmd.retrieveLoggedInUserHealthInfra = function (userId) {
            if (userId) {
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: userId
                    }
                }).then(function (res) {
                    ncdmd.isUserHealthFetch = true
                    ncdmd.userHealthInfras = res.result;
                })
            }
        };

        ncdmd.retrieveLoggedInUserFeatureJson = function () {
            Mask.show()
            AuthenticateService.getAssignedFeature("techo.ncd.members").then(function (res) {
                ncdmd.rights = res.featureJson;
                if (!ncdmd.rights) {
                    ncdmd.rights = {};
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        };

        ncdmd.hypertensionExaminedValuesChanged = function () {
            ncdmd.member.memberHypertensionDto.startTreatment = null;
            ncdmd.member.memberHypertensionDto.referralDto = null;
            if (!ncdmd.showHypertensionSubType) {
                if (ncdmd.member.memberHypertensionDto.systolicBloodPressure >= 140 || ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 90) {
                    ncdmd.member.memberHypertensionDto.status = 'CONFIRMED'
                } else {
                    ncdmd.member.memberHypertensionDto.status = 'NO_ABNORMALITY'
                }
            } else if (ncdmd.showHypertensionSubType) {
                if (ncdmd.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90) {
                    ncdmd.member.memberHypertensionDto.subType = 'CONTROLLED'
                } else {
                    ncdmd.member.memberHypertensionDto.subType = 'UNCONTROLLED'
                }
            }

        }

        ncdmd.mentalHealthExaminedValuesChanged = function () {
            ncdmd.member.memberMentalHealthDto.startTreatment = null;
            ncdmd.member.memberMentalHealthDto.referralDto = null;
            if (ncdmd.member.memberMentalHealthDto.talk > 1
                || ncdmd.member.memberMentalHealthDto.ownDailyWork > 1
                || ncdmd.member.memberMentalHealthDto.socialWork > 1
                || ncdmd.member.memberMentalHealthDto.understanding > 1) {
                ncdmd.member.memberMentalHealthDto.status = 'UNCONTROLLED';
            } else {
                ncdmd.member.memberMentalHealthDto.status = 'CONTROLLED';
            }
        }

        ncdmd.diabetesExaminedValuesChanged = function () {
            ncdmd.member.memberDiabetesDto.startTreatment = null;
            ncdmd.member.memberDiabetesDto.referralDto = null;
            if (!ncdmd.showDiabetesSubType) {
                if (ncdmd.member.memberDiabetesDto.fastingBloodSugar >= 126
                    || ncdmd.member.memberDiabetesDto.postPrandialBloodSugar >= 200
                    || ncdmd.member.memberDiabetesDto.bloodSugar >= 200
                    || ncdmd.member.memberDiabetesDto.dka
                    || ncdmd.member.memberDiabetesDto.hba1c >= 6.5) {
                    ncdmd.member.memberDiabetesDto.status = 'CONFIRMED';
                } else {
                    ncdmd.member.memberDiabetesDto.status = 'NO_ABNORMALITY';
                }
            } else if (ncdmd.showDiabetesSubType) {
                if (ncdmd.member.memberDiabetesDto.fastingBloodSugar < 130
                    && ncdmd.member.memberDiabetesDto.postPrandialBloodSugar < 200
                    && ncdmd.member.memberDiabetesDto.bloodSugar < 200
                    && ncdmd.member.memberDiabetesDto.hba1c < 7) {
                    ncdmd.member.memberDiabetesDto.subType = 'CONTROLLED';
                } else {
                    ncdmd.member.memberDiabetesDto.subType = 'UNCONTROLLED';
                }
            }
        }

        ncdmd.retrieveHypertensionDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveHypertensionDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberHypertensionDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.hypertensionAlreadyFilled = true;
                    ncdmd.member.memberHypertensionDto.systolicBloodPressure = response.systolicBp
                    ncdmd.member.memberHypertensionDto.diastolicBloodPressure = response.diastolicBp
                    ncdmd.member.memberHypertensionDto.heartRate = response.pulseRate
                } else {
                    ncdmd.hypertensionAlreadyFilled = false;
                    ncdmd.member.memberHypertensionDto.systolicBloodPressure = null
                    ncdmd.member.memberHypertensionDto.diastolicBloodPressure = null
                    ncdmd.member.memberHypertensionDto.heartRate = null;
                    ncdmd.minHypertensionFollowUpDate = moment(ncdmd.member.memberHypertensionDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveGeneralDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveGeneralDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberGeneralDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.generalAlreadyFilled = true;
                    ncdmd.member.memberGeneralDto.symptoms = response.symptoms
                    ncdmd.member.memberGeneralDto.clinicalObservation = response.clinicalObservation
                    ncdmd.member.memberGeneralDto.diagnosis = response.diagnosis
                    ncdmd.member.memberGeneralDto.remarks = response.remarks
                } else {
                    ncdmd.generalAlreadyFilled = false;
                    ncdmd.member.memberGeneralDto.symptoms = null
                    ncdmd.member.memberGeneralDto.clinicalObservation = null
                    ncdmd.member.memberGeneralDto.diagnosis = null
                    ncdmd.member.memberGeneralDto.remarks = null
                    ncdmd.minGeneralFollowUpDate = moment(ncdmd.member.memberGeneralDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveInitialAssessmentDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveInitialAssessmentDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberInitialAssessmentDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    initialAssessmentAlreadyFilled = true;
                    ncdmd.member.memberInitialAssessmentDto.height = response.height
                    ncdmd.member.memberInitialAssessmentDto.weight = response.weight
                    ncdmd.member.memberInitialAssessmentDto.waistCircumference = response.waistCircumference
                    ncdmd.member.memberInitialAssessmentDto.bmi = response.bmi
                } else {
                    initialAssessmentAlreadyFilled = false;
                    ncdmd.member.memberInitialAssessmentDto.height = null
                    ncdmd.member.memberInitialAssessmentDto.weight = null
                    ncdmd.member.memberInitialAssessmentDto.waistCircumference = null
                    ncdmd.member.memberInitialAssessmentDto.bmi = null
                    ncdmd.minInitialAssessmentFollowUpDate = moment(ncdmd.member.memberInitialAssessmentDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveMentalHealthDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveMentalHealthDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberMentalHealthDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    MentalHealthAlreadyFilled = true;
                    ncdmd.member.memberMentalHealthDto.talk = response.talk
                    ncdmd.member.memberMentalHealthDto.ownDailyWork = response.ownDailyWork
                    ncdmd.member.memberMentalHealthDto.socialWork = response.socialWork
                    ncdmd.member.memberMentalHealthDto.understanding = response.understanding
                } else {
                    MentalHealthAlreadyFilled = false;
                    ncdmd.member.memberMentalHealthDto.talk = null
                    ncdmd.member.memberMentalHealthDto.ownDailyWork = null
                    ncdmd.member.memberMentalHealthDto.socialWork = null
                    ncdmd.member.memberMentalHealthDto.understanding = null
                    ncdmd.minMentalHealthFollowUpDate = moment(ncdmd.member.memberMentalHealthDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveDiabetesDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveDiabetesDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberDiabetesDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.diabetesAlreadyFilled = true;
                    ncdmd.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar
                    ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar
                    ncdmd.member.memberDiabetesDto.bloodSugar = response.bloodSugar
                    ncdmd.member.memberDiabetesDto.dka = response.dka
                    ncdmd.member.memberDiabetesDto.hba1c = response.hba1c
                } else {
                    ncdmd.diabetesAlreadyFilled = false;
                    ncdmd.member.memberDiabetesDto.fastingBloodSugar = null
                    ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = null
                    ncdmd.member.memberDiabetesDto.bloodSugar = null
                    ncdmd.member.memberDiabetesDto.dka = null
                    ncdmd.member.memberDiabetesDto.hba1c = null
                    ncdmd.minDiabetesFollowUpDate = moment(ncdmd.member.memberDiabetesDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveOralDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveOralDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberOralDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.oralAlreadyFilled = true;
                    ncdmd.member.memberOralDto.restrictedMouthOpening = response.restrictedMouthOpening;
                    ncdmd.member.memberOralDto.whitePatches = response.whitePatches;
                    ncdmd.member.memberOralDto.redPatches = response.redPatches;
                    ncdmd.member.memberOralDto.nonHealingUlcers = response.nonHealingUlcers;
                    ncdmd.member.memberOralDto.growthOfRecentOrigins = response.growthOfRecentOrigins;
                    setOralTestsForDisplay();
                } else {
                    ncdmd.oralAlreadyFilled = false;
                    ncdmd.member.memberOralDto.restrictedMouthOpening = null;
                    ncdmd.member.memberOralDto.whitePatches = null;
                    ncdmd.member.memberOralDto.redPatches = null;
                    ncdmd.member.memberOralDto.nonHealingUlcers = null;
                    ncdmd.member.memberOralDto.growthOfRecentOrigins = null;
                    setOralTestsForDisplay();
                    ncdmd.minOralFollowUpDate = moment(ncdmd.member.memberOralDto.screeningDate).add(90, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveBreastDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveBreastDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberBreastDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.breastAlreadyFilled = true;
                    ncdmd.member.memberBreastDto.sizeChange = response.sizeChange
                    ncdmd.member.memberBreastDto.sizeChangeLeft = response.sizeChangeLeft
                    ncdmd.member.memberBreastDto.sizeChangeRight = response.sizeChangeRight

                    ncdmd.member.memberBreastDto.nippleNotOnSameLevel = response.nippleNotOnSameLevel
                    ncdmd.member.memberBreastDto.anyRetractionOfNipple = response.anyRetractionOfNipple
                    ncdmd.member.memberBreastDto.retractionOfLeftNipple = response.retractionOfLeftNipple
                    ncdmd.member.memberBreastDto.retractionOfRightNipple = response.retractionOfRightNipple

                    ncdmd.member.memberBreastDto.lymphadenopathy = response.lymphadenopathy

                    ncdmd.member.memberBreastDto.leftLymphadenopathy = response.leftLymphadenopathy
                    ncdmd.member.memberBreastDto.rightLymphadenopathy = response.rightLymphadenopathy

                    ncdmd.member.memberBreastDto.dischargeFromNipple = response.dischargeFromNipple
                    ncdmd.member.memberBreastDto.dischargeFromLeftNipple = response.dischargeFromLeftNipple
                    ncdmd.member.memberBreastDto.dischargeFromRightNipple = response.dischargeFromRightNipple

                    ncdmd.member.memberBreastDto.visualSkinDimplingRetraction = response.visualSkinDimplingRetraction
                    ncdmd.member.memberBreastDto.visualNippleRetractionDistortion = response.visualNippleRetractionDistortion
                    ncdmd.member.memberBreastDto.visualLumpInBreast = response.visualLumpInBreast
                    setBreastVisual();
                } else {
                    ncdmd.breastAlreadyFilled = false;
                    ncdmd.member.memberBreastDto.sizeChange = null
                    ncdmd.member.memberBreastDto.sizeChangeLeft = null
                    ncdmd.member.memberBreastDto.sizeChangeRight = null
                    ncdmd.member.memberBreastDto.nippleNotOnSameLevel = null
                    ncdmd.member.memberBreastDto.anyRetractionOfNipple = null
                    ncdmd.member.memberBreastDto.retractionOfLeftNipple = null
                    ncdmd.member.memberBreastDto.retractionOfRightNipple = null
                    ncdmd.member.memberBreastDto.lymphadenopathy = null
                    ncdmd.member.memberBreastDto.leftLymphadenopathy = null
                    ncdmd.member.memberBreastDto.rightLymphadenopathy = null
                    ncdmd.member.memberBreastDto.dischargeFromNipple = null
                    ncdmd.member.memberBreastDto.dischargeFromLeftNipple = null
                    ncdmd.member.memberBreastDto.dischargeFromRightNipple = null
                    ncdmd.member.memberBreastDto.visualSkinDimplingRetraction = null
                    ncdmd.member.memberBreastDto.visualNippleRetractionDistortion = null
                    ncdmd.member.memberBreastDto.visualLumpInBreast = null
                    setBreastVisual();
                    ncdmd.minBreastFollowUpDate = moment(ncdmd.member.memberBreastDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveCervicalDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveCervicalDetailsByMemberAndDate($state.params.id, moment(ncdmd.member.memberCervicalDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.cervicalAlreadyFilled = true;
                    ncdmd.member.memberCervicalDto.papsmearTest = response.papsmearTest
                    ncdmd.member.memberCervicalDto.viaTest = response.viaTest
                    ncdmd.member.memberCervicalDto.viaExamPoints = response.viaExamPoints
                    ncdmd.member.memberCervicalDto.externalGenitaliaHealthy = response.externalGenitaliaHealthy
                    setCervicalVisual();
                } else {
                    ncdmd.cervicalAlreadyFilled = false;
                    ncdmd.member.memberCervicalDto.papsmearTest = null
                    ncdmd.member.memberCervicalDto.viaTest = null
                    ncdmd.member.memberCervicalDto.viaExamPoints = null
                    setCervicalVisual();
                    ncdmd.minCervicalFollowUpDate = moment(ncdmd.member.memberCervicalDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }



        ncdmd.showTreatmentHistory = function (diseaseCodeObj) {
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
    angular.module('imtecho.controllers').controller('NcdMemberDetailController', NcdMemberDetailController);
})(window.angular);
