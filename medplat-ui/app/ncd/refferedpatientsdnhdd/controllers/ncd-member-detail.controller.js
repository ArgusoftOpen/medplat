(function (angular) {
    function NcdMemberDetailDnhddController($state, NcdDnhddDAO, $filter, toaster, $uibModal, GeneralUtil, AuthenticateService, $scope, Mask, $q, QueryDAO, APP_CONFIG) {
        const ncdmd = this;
        ncdmd.appName = APP_CONFIG.appName || 'TeCHO+';
        ncdmd.diabetesDetected = false;
        ncdmd.oralDetected = false;
        ncdmd.breastDetected = false;
        ncdmd.cervicalDetected = false;
        ncdmd.keyFn = Object.keys;
        ncdmd.lastScreeningDate = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
        ncdmd.lastCancerServiceDate = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
        $scope.isArray = angular.isArray
        $scope.today = new Date();
        ncdmd.maxSysBP = 140; //mmHg
        ncdmd.maxDiaBP = 90; //mmHg
        ncdmd.maxAllowedSysBP = 250; //mmHg
        ncdmd.minAllowedSysBP = 60; //mmHg
        ncdmd.maxAllowedDiaBP = 200; //mmHg
        ncdmd.minAllowedDiaBP = 40; //mmHg
        ncdmd.maxHeartRate = 250;
        ncdmd.minHeartRate = 30;
        ncdmd.maxFastingBloodSugar = 126; //mg/dL
        ncdmd.maxPP2BS = 200; //mg/dL
        ncdmd.maxBloodSugar = 200; //mg/dL
        ncdmd.env = GeneralUtil.getEnv();

        const wcForFemales = [
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

        const wcForMales = [
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
            'initialassessment', 'general', 'oral-main', 'breast-main', 'cervical-main', 'diabetes-main', 'hypertension-main', 'labtest-main'
        ];

        const bmiCategories = {
            'Underweight': { min: 0.1, max: 18.5 },
            'Normal Weight': { min: 18.5, max: 24.9 },
            'Pre-obesity': { min: 25.0, max: 29.9 },
            'Obesity class I': { min: 30.0, max: 34.9 },
            'Obesity class II': { min: 35.0, max: 39.9 },
            'Obesity class III': { min: 40, max: 100 }
        };

        const init = function () {
            ncdmd.isUserHealthFetch = false
            ncdmd.navigateToTab('hypertension-main');
            AuthenticateService.getLoggedInUser().then(function (res) {
                ncdmd.loggedInUser = res.data;
                ncdmd.retrieveLoggedInUserHealthInfra(res.data.id);
            })

            NcdDnhddDAO.retrieveAllMedicines().then(function (res) {
                ncdmd.medicines = res;
                ncdmd.medicines.sort(function(a, b) {
                    return a.name.localeCompare(b.name, undefined, { sensitivity: 'base' });
                });
            }, GeneralUtil.showMessageOnApiCallFailure);
            ncdmd.alcoholConsumptions =
                ['Daily or almost daily', 'Weekly', 'Monthly', 'Less than monthly', 'Never']

            if ($state.params.id) {
                ncdmd.patientId = $state.params.id;
                ncdmd.retrieveMemberDetails();
            }
            ncdmd.today = new Date();
            ncdmd.maxStartDate = new Date();
            ncdmd.maxStartDate.setDate(ncdmd.maxStartDate.getDate() + 60);

        };

        const getAge = function (DOB) {
            let birthDate = new Date(DOB);
            let age = new Date().getFullYear() - birthDate.getFullYear();
            let m = new Date().getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && new Date().getDate() < birthDate.getDate())) {
                age = age - 1;
            }
            return age;
        }

        const _setSymptoms = function () {
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
            symptoms.general = general;

            if (ncdmd.lastRecordOfCbac) {
                let cbacData = [];
                if (!!ncdmd.lastRecordOfCbac.score) {
                    cbacData.push({ key: 'Score', value: ncdmd.lastRecordOfCbac.score });
                }
                if (!!ncdmd.lastRecordOfCbac.bmi) {
                    cbacData.push({ key: 'BMI Category', value: ncdmd.lastRecordOfCbac.bmiCategory });
                }
                if (!!ncdmd.lastRecordOfCbac.isAlcoholic) {
                    cbacData.push({ key: 'Alcoholic', value: ncdmd.lastRecordOfCbac.isAlcoholic });
                }
                if (!!ncdmd.lastRecordOfCbac.isSmoker) {
                    cbacData.push({ key: 'Smoker', value: ncdmd.lastRecordOfCbac.isSmoker });
                }
                symptoms.cbac = cbacData;
            }

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

        ncdmd.viewCBACReport = function () {
            const modalInstance = $uibModal.open({
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
            ncdmd.showAssignedInfrastructuresForDiabetes = true;
            ncdmd.showAssignedInfrastructuresForOral = true;
            ncdmd.showAssignedInfrastructuresForBreast = true;
            ncdmd.showAssignedInfrastructuresForCervical = true;
            return NcdDnhddDAO.retrieveDetails(ncdmd.patientId).then(function (res) {
                ncdmd.member = {};
                ncdmd.member = res;
                ncdmd.member.age = getAge(ncdmd.member.basicDetails.dob);
                if (ncdmd.member.additionalInfo !== null) {
                    ncdmd.member.additionalInfo = JSON.parse(ncdmd.member.additionalInfo);
                    ncdmd.lastScreeningDate = ncdmd.member.additionalInfo.hypDiaMentalServiceDate ? moment(ncdmd.member.additionalInfo.hypDiaMentalServiceDate) : ncdmd.lastScreeningDate;
                    ncdmd.lastCancerServiceDate = ncdmd.member.additionalInfo.cancerServiceDate ? moment(ncdmd.member.additionalInfo.cancerServiceDate) : ncdmd.lastCancerServiceDate;
                }
                ncdmd.member.onTreatment = false;
                if (ncdmd.member.memberHypertensionDto != null) {
                    NcdDnhddDAO.retrieveLastRecordForHypertensionByMemberId(ncdmd.patientId).then(function (response) {
                        ncdmd.lastRecordOfHypertension = response;
                        ncdmd.showHypertensionSubType = ncdmd.lastRecordOfHypertension.diagnosedEarlier;
                        ncdmd.member.memberHypertensionDto.medicineDetail = [];
                        ncdmd.member.memberHypertensionDto.medicines = [];
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.hypertensionAlreadyFilled = true;
                            ncdmd.member.memberHypertensionDto.screeningDate = moment(response.screeningDate);
                            ncdmd.member.memberHypertensionDto.systolicBloodPressure = response.systolicBloodPressure
                            ncdmd.member.memberHypertensionDto.diastolicBloodPressure = response.diastolicBloodPressure
                            ncdmd.member.memberHypertensionDto.heartRate = response.heartRate
                        } else {
                            ncdmd.hypertensionAlreadyFilled = false;
                        }
                        ncdmd.member.onTreatment = ncdmd.member.onTreatment || ncdmd.lastRecordOfHypertension.currentlyUnderTreatment;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
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
                    NcdDnhddDAO.retrieveLastRecordForDiabetesByMemberId(ncdmd.patientId).then(function (response) {
                        ncdmd.lastRecordOfDiabetes = response;
                        ncdmd.showDiabetesSubType = ncdmd.lastRecordOfDiabetes.earlierDiabetesDiagnosis;
                        ncdmd.member.memberDiabetesDto.medicineDetail = ncdmd.lastRecordOfDiabetes?.medicineMasters != null ? ncdmd.lastRecordOfDiabetes.medicineMasters.map(function (medicine) {
                            return medicine.id
                        }) : null;
                        if (response.id != null && (response.doneBy === 'FHW' || response.doneBy === 'MPHW' || response.doneBy === 'CHO')) {
                            ncdmd.diabetesAlreadyFilled = true;
                            ncdmd.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar;
                            ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar;
                            ncdmd.member.memberDiabetesDto.bloodSugar = response.bloodSugar;
                            ncdmd.member.memberDiabetesDto.dka = response.dka;
                            ncdmd.member.memberDiabetesDto.hba1c = response.hba1c;
                        } else {
                            ncdmd.diabetesAlreadyFilled = false;
                        }
                        ncdmd.member.onTreatment = ncdmd.member.onTreatment || ncdmd.lastRecordOfDiabetes.currentlyUnderTreatment;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
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
                }
                if (ncdmd.member.memberOralDto != null) {
                    NcdDnhddDAO.retrieveLastRecordForOralByMemberId(ncdmd.patientId).then(function (response) {
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
                            ncdmd.oralHistoryDisplay = ncdmd.oralHistoryArray.join("<br>");
                            ncdmd.member.onTreatment = ncdmd.member.onTreatment || ncdmd.lastRecordOfOral.currentlyUnderTreatment;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
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
                    NcdDnhddDAO.retrieveLastRecordForBreastByMemberId(ncdmd.patientId).then(function (response) {
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
                            ncdmd.breastHistoryDisplay = ncdmd.breastHistoryArray.join("<br>");
                            ncdmd.member.onTreatment = ncdmd.member.onTreatment || ncdmd.lastRecordOfBreast.currentlyUnderTreatment;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
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
                    NcdDnhddDAO.retrieveLastRecordForCervicalByMemberId(ncdmd.patientId).then(function (response) {
                        ncdmd.lastRecordOfCervical = response;
                        if (ncdmd.lastRecordOfCervical != null) {
                            ncdmd.cervicalHistoryArray = [];
                            if (ncdmd.lastRecordOfCervical.papsmearTest) {
                                ncdmd.cervicalHistoryArray.push("PAP Smear positive")
                            }
                            if (ncdmd.lastRecordOfCervical.viaTest === 'true') {
                                ncdmd.cervicalHistoryArray.push("VIA positive")
                            }
                            ncdmd.cervicalHistoryDisplay = ncdmd.cervicalHistoryArray.join();
                            ncdmd.member.onTreatment = ncdmd.member.onTreatment || ncdmd.lastRecordOfCervical.currentlyUnderTreatment;
                        }
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
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
                ncdmd.retrieveLastCbacDetails(ncdmd.patientId);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }

        ncdmd.saveHyperTensionAndDiabetes = function () {
            if (!ncdmd.hypertensionAlreadyFilled) {
                if (ncdmd.hypertensionForm.$valid) {
                    if (!!ncdmd.userHealthInfras) {
                        let referredFromInstitute = ncdmd.isSingleInfra ? ncdmd.userHealthInfras : JSON.parse(ncdmd.member.memberHypertensionDto.referredFromInstitute);
                        ncdmd.member.memberHypertensionDto.referredFromHealthInfrastructureId = referredFromInstitute.id;
                        ncdmd.member.memberDiabetesDto.referredFromHealthInfrastructureId = referredFromInstitute.id;
                    }
                    if (!ncdmd.member.memberHypertensionDto.memberId) {
                        ncdmd.member.memberHypertensionDto.memberId = Number(ncdmd.patientId);
                    }
                    if (ncdmd.member.memberHypertensionDto.startTreatment === 'true' || ncdmd.showHypertensionMedicines) {
                        ncdmd.member.memberHypertensionDto.status = 'TREATMENT_STARTED';
                    }
                    if (ncdmd.member.memberHypertensionDto.referralDto?.isReferred) {
                        ncdmd.member.memberHypertensionDto.status = 'REFERRED';
                        ncdmd.member.memberHypertensionDto.reason = ncdmd.member.memberHypertensionDto.referralDto.reason;
                        if (ncdmd.member.memberHypertensionDto.referralDto.healthInfraId === ncdmd.member.memberHypertensionDto.referredFromHealthInfrastructureId) {
                            toaster.pop('warning', 'Cannot refer to same health infrastructure!');
                            ncdmd.onChange('SAME_INFRA');
                            return;
                        } else if (!!ncdmd.member.memberHypertensionDto.referralDto.pvtHealthInfraName) {
                            ncdmd.member.memberHypertensionDto.pvtHealthInfraName = ncdmd.member.memberHypertensionDto.referralDto.pvtHealthInfraName;
                        } else {
                            ncdmd.member.memberHypertensionDto.healthInfraId = ncdmd.member.memberHypertensionDto.referralDto.healthInfraId;
                        }
                    }
                    if(!!ncdmd.hmisId && !!ncdmd.lastRecordOfCbac.id) {
                        ncdmd.member.memberHypertensionDto.cbacId = ncdmd.lastRecordOfCbac.id;
                        ncdmd.member.memberHypertensionDto.hmisId = Number(ncdmd.hmisId);
                    }

                    ncdmd.member.memberDiabetesDto.screeningDate = ncdmd.member.memberHypertensionDto.screeningDate;
                    ncdmd.member.memberDiabetesDto.followUpDate = ncdmd.member.memberHypertensionDto.followUpDate;
                    if (!ncdmd.member.memberDiabetesDto.memberId) {
                        ncdmd.member.memberDiabetesDto.memberId = Number(ncdmd.patientId);
                    }
                    if (ncdmd.member.memberDiabetesDto.startTreatment === 'true' || ncdmd.showDiabetesMedicines) {
                        ncdmd.member.memberDiabetesDto.status = 'TREATMENT_STARTED';
                    }
                    if (ncdmd.member.memberDiabetesDto.referralDto?.isReferred) {
                        ncdmd.member.memberDiabetesDto.status = 'REFERRED';
                        ncdmd.member.memberDiabetesDto.reason = ncdmd.member.memberDiabetesDto.referralDto.reason;
                        if (ncdmd.member.memberDiabetesDto.referralDto.healthInfraId === ncdmd.member.memberDiabetesDto.referredFromHealthInfrastructureId) {
                            toaster.pop('warning', 'Cannot refer to same health infrastructure!');
                            ncdmd.onChange('SAME_INFRA');
                            return;
                        } else if (!!ncdmd.member.memberDiabetesDto.referralDto.pvtHealthInfraName) {
                            ncdmd.member.memberDiabetesDto.pvtHealthInfraName = ncdmd.member.memberDiabetesDto.referralDto.pvtHealthInfraName;
                        } else {
                            ncdmd.member.memberDiabetesDto.healthInfraId = ncdmd.member.memberDiabetesDto.referralDto.healthInfraId;
                        }
                    }
                    const modalInstance = $uibModal.open({
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
                        let promiseArray = [];
                        if (!ncdmd.hypertensionAlreadyFilled) {
                            promiseArray.push(NcdDnhddDAO.saveHyperTension(ncdmd.member.memberHypertensionDto));
                        }
                        if (!ncdmd.diabetesAlreadyFilled) {
                            promiseArray.push(NcdDnhddDAO.saveDiabetes(ncdmd.member.memberDiabetesDto));
                        }

                        Promise.all(promiseArray).then(function () {
                            toaster.pop('success', "Hypertension and Diabetes Details saved successfully");
                            ncdmd.retrieveMemberDetails(true);
                            ncdmd.hypertensionForm.$setPristine();
                            ncdmd.member.memberHypertensionDto.medicineDetail = [];
                            ncdmd.member.memberHypertensionDto.medicines = [];
                        }, GeneralUtil.showMessageOnApiCallFailure);
                    });
                }
            }
        };

        ncdmd.saveCervical = function () {
            if (ncdmd.cervicalForm.$valid) {
                if (!!ncdmd.userHealthInfras) {
                    let referredFromInstitute = ncdmd.isSingleInfra ? ncdmd.userHealthInfras : JSON.parse(ncdmd.member.memberCervicalDto.referredFromInstitute);
                    ncdmd.member.memberCervicalDto.referredFromHealthInfrastructureId = referredFromInstitute.id;
                }
                if (!ncdmd.member.memberCervicalDto.memberId) {
                    ncdmd.member.memberCervicalDto.memberId = Number(ncdmd.patientId);
                }
                if (ncdmd.member.memberCervicalDto.referralDto?.isReferred) {
                    ncdmd.member.memberCervicalDto.reason = ncdmd.member.memberCervicalDto.referralDto.reason;
                    if (ncdmd.member.memberCervicalDto.referralDto.healthInfraId === ncdmd.member.memberCervicalDto.referredFromHealthInfrastructureId) {
                        toaster.pop('warning', 'Cannot refer to same health infrastructure!');
                        ncdmd.onChange('SAME_INFRA');
                        return;
                    } else if (!!ncdmd.member.memberCervicalDto.referralDto.pvtHealthInfraName) {
                        ncdmd.member.memberCervicalDto.pvtHealthInfraName = ncdmd.member.memberCervicalDto.referralDto.pvtHealthInfraName;
                    } else {
                        ncdmd.member.memberCervicalDto.healthInfraId = ncdmd.member.memberCervicalDto.referralDto.healthInfraId;
                    }
                }
                if (ncdmd.member.memberCervicalDto.referralDto?.isReferred && ncdmd.member.memberCervicalDto.status != 'SUSPECTED' && ncdmd.member.memberCervicalDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberCervicalDto.status = 'REFERRED';
                }
                if(!!ncdmd.hmisId && !!ncdmd.lastRecordOfCbac.id) {
                    ncdmd.member.memberCervicalDto.cbacId = ncdmd.lastRecordOfCbac.id;
                    ncdmd.member.memberCervicalDto.hmisId = Number(ncdmd.hmisId);
                }
                NcdDnhddDAO.saveCervical(ncdmd.member.memberCervicalDto).then(function () {
                    toaster.pop('success', "Cervical Details saved successfully");
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.cervicalForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ncdmd.saveOral = function () {
            if (ncdmd.oralForm.$valid) {
                _.each(ncdmd.member.memberOralDto.oralList, function (value, key) {
                    ncdmd.member.memberOralDto[key] = [];
                    _.each(value, function (v, k) {
                        if (value[k]) {
                            ncdmd.member.memberOralDto[key].push(k);
                        }
                    })
                    ncdmd.member.memberOralDto[key] = ncdmd.member.memberOralDto[key].join();
                })
                if (!!ncdmd.userHealthInfras) {
                    let referredFromInstitute = ncdmd.isSingleInfra ? ncdmd.userHealthInfras : JSON.parse(ncdmd.member.memberOralDto.referredFromInstitute);
                    ncdmd.member.memberOralDto.referredFromHealthInfrastructureId = referredFromInstitute.id;
                }
                if (!ncdmd.member.memberOralDto.memberId) {
                    ncdmd.member.memberOralDto.memberId = Number(ncdmd.patientId);
                }
                if (ncdmd.member.memberOralDto.referralDto?.isReferred) {
                    if (ncdmd.member.memberOralDto.referralDto.healthInfraId === ncdmd.member.memberOralDto.referredFromHealthInfrastructureId) {
                        toaster.pop('warning', 'Cannot refer to same health infrastructure!');
                        ncdmd.onChange('SAME_INFRA');
                        return;
                    } else if (!!ncdmd.member.memberOralDto.referralDto.pvtHealthInfraName) {
                        ncdmd.member.memberOralDto.pvtHealthInfraName = ncdmd.member.memberOralDto.referralDto.pvtHealthInfraName;
                    } else {
                        ncdmd.member.memberOralDto.healthInfraId = ncdmd.member.memberOralDto.referralDto.healthInfraId;
                    }
                    ncdmd.member.memberOralDto.reason = ncdmd.member.memberOralDto.referralDto.reason;
                }
                if (ncdmd.member.memberOralDto.referralDto?.isReferred && ncdmd.member.memberOralDto.status != 'SUSPECTED' && ncdmd.member.memberOralDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberOralDto.status = 'REFERRED';
                }
                if(!!ncdmd.hmisId && !!ncdmd.lastRecordOfCbac.id) {
                    ncdmd.member.memberOralDto.cbacId = ncdmd.lastRecordOfCbac.id;
                    ncdmd.member.memberOralDto.hmisId = Number(ncdmd.hmisId);
                }
                NcdDnhddDAO.saveOral(ncdmd.member.memberOralDto).then(function () {
                    toaster.pop('success', "Oral Details saved successfully");
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.oralForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ncdmd.saveBreast = function () {
            if (ncdmd.breastForm.$valid) {
                if (!!ncdmd.userHealthInfras) {
                    let referredFromInstitute = ncdmd.isSingleInfra ? ncdmd.userHealthInfras : JSON.parse(ncdmd.member.memberBreastDto.referredFromInstitute);
                    ncdmd.member.memberBreastDto.referredFromHealthInfrastructureId = referredFromInstitute.id;
                }
                if (!ncdmd.member.memberBreastDto.memberId) {
                    ncdmd.member.memberBreastDto.memberId = Number(ncdmd.patientId);
                }
                let points = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];
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
                if (ncdmd.member.memberBreastDto.referralDto?.isReferred) {
                    if (ncdmd.member.memberBreastDto.referralDto.healthInfraId === ncdmd.member.memberBreastDto.referredFromHealthInfrastructureId) {
                        toaster.pop('warning', 'Cannot refer to same health infrastructure!');
                        ncdmd.onChange('SAME_INFRA');
                        return;
                    } else if (!!ncdmd.member.memberBreastDto.referralDto.pvtHealthInfraName) {
                        ncdmd.member.memberBreastDto.pvtHealthInfraName = ncdmd.member.memberBreastDto.referralDto.pvtHealthInfraName;
                    } else {
                        ncdmd.member.memberBreastDto.healthInfraId = ncdmd.member.memberBreastDto.referralDto.healthInfraId;
                    }
                    ncdmd.member.memberBreastDto.reason = ncdmd.member.memberBreastDto.referralDto.reason;
                }
                if (ncdmd.member.memberBreastDto.referralDto?.isReferred && ncdmd.member.memberBreastDto.status != 'SUSPECTED' && ncdmd.member.memberBreastDto.status != 'CONFIRMATION_PENDING') {
                    ncdmd.member.memberBreastDto.status = 'REFERRED';
                }
                if(!!ncdmd.hmisId && !!ncdmd.lastRecordOfCbac.id) {
                    ncdmd.member.memberBreastDto.cbacId = ncdmd.lastRecordOfCbac.id;
                    ncdmd.member.memberBreastDto.hmisId = Number(ncdmd.hmisId);
                }
                NcdDnhddDAO.saveBreast(ncdmd.member.memberBreastDto).then(function () {
                    toaster.pop('success', "Breast Details saved successfully");
                    ncdmd.retrieveMemberDetails(true);
                    ncdmd.breastForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ncdmd.setAllOralTest = function () {
            ncdmd.subMenuSelected = 'all';
            if (!ncdmd.member.memberOralDto?.displayList) {
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

        ncdmd.calculateMedicineQuantity = function () {
            if (ncdmd.member.memberHypertensionDto.medicine.duration && ncdmd.member.memberHypertensionDto.medicine.frequency) {
                ncdmd.member.memberHypertensionDto.medicine.duration = Number(ncdmd.member.memberHypertensionDto.medicine.duration);
                ncdmd.member.memberHypertensionDto.medicine.frequency = Number(ncdmd.member.memberHypertensionDto.medicine.frequency);
                ncdmd.member.memberHypertensionDto.medicine.quantity = ncdmd.member.memberHypertensionDto.medicine.duration * ncdmd.member.memberHypertensionDto.medicine.frequency;
            }
        }

        const setOralTestsForDisplay = function () {
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
        const setBreastVisual = function () {
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
                if (ncdmd.member.memberBreastDto.visualLumpInBreast) {
                    let res = ncdmd.member.memberBreastDto.visualLumpInBreast.split(",");
                    ncdmd.member.memberBreastDto.breastList.visualLumpInBreast = {};
                    _.each(res, function (point) {
                        ncdmd.member.memberBreastDto.breastList.visualLumpInBreast[point] = true;

                    })
                }
            }
        };

        ncdmd.navigateToTab = function (id) {
            if (id == "oral-main") {
                ncdmd.subMenuSelected = 'whitePatches'
                ncdmd.oralForm.$setPristine();
            } else if (id == "breast-main") {
                ncdmd.breastForm.$setPristine();
            } else if (id == "cervical-main") {
                ncdmd.cervicalForm.$setPristine();
            }
            $('#' + id).tab('show');
        };

        ncdmd.retrieveComplaints = function () {
            Mask.show();
            NcdDnhddDAO.retrieveComplaintsByMemberId(ncdmd.patientId).then(function (res) {
                ncdmd.member.complaints = res;
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

        ncdmd.deleteMedicine = function (index, item) {
            ncdmd.member.memberHypertensionDto.medicineDetail.splice(index, 1);
            ncdmd.member.memberHypertensionDto.medicines.splice(index,1);
        }

        ncdmd.editMedicineChange = function (item) {
            item.quantityNew = item.frequencyNew * item.durationNew
            if (item.quantityNew < item.quantity) {
                item.isEnableReturnButton = true
            }
            else {
                item.isEnableReturnButton = false
            }
        }

        ncdmd.addMedicine = function () {
            if (!!ncdmd.member.memberHypertensionDto.medicine && !!ncdmd.member.memberHypertensionDto.medicine.frequency && ncdmd.member.memberHypertensionDto.medicine.duration && ncdmd.member.memberHypertensionDto.medicine.quantity
                && ncdmd.member.memberHypertensionDto.medicine.startDate && ncdmd.member.memberHypertensionDto.medicine.frequency > 0 && ncdmd.member.memberHypertensionDto.medicine.duration > 0
                && ncdmd.member.memberHypertensionDto.medicine.frequency <= 5 && ncdmd.member.memberHypertensionDto.medicine.duration <= 60) {

                ncdmd.member.memberHypertensionDto.medicines.push(
                    ncdmd.member.memberHypertensionDto.medicine.id
                )

                ncdmd.member.memberHypertensionDto.medicineDetail.push({
                    medicineName: ncdmd.member.memberHypertensionDto.medicine.name,
                    medicineId: ncdmd.member.memberHypertensionDto.medicine.id,
                    startDate: ncdmd.member.memberHypertensionDto.medicine.startDate,
                    frequency: ncdmd.member.memberHypertensionDto.medicine.frequency,
                    duration: ncdmd.member.memberHypertensionDto.medicine.duration,
                    quantity: ncdmd.member.memberHypertensionDto.medicine.quantity,
                    specialInstruction: ncdmd.member.memberHypertensionDto.medicine.specialInstruction
                })
                ncdmd.medicines.sort(function(a, b) {
                    return a.name.localeCompare(b.name, undefined, { sensitivity: 'base' });
                });
                ncdmd.clearMedicineForm();
                ncdmd.isSubmit = true;
            }
            else {
                toaster.pop('error', "Please make sure medicine name and start date is selected. Also,select frequency between 1 to 5 and duration between 1 to 60.")
            }
        }

        ncdmd.clearMedicineForm = function () {
            // ncdmd.member.memberHypertensionDto.medicine = null,
            // ncdmd.member.memberHypertensionDto.medicine.name = null,
            // ncdmd.member.memberHypertensionDto.medicine.medicineId = null,
            // ncdmd.member.memberHypertensionDto.medicine.startDate = null,
            // ncdmd.member.memberHypertensionDto.medicine.frequency = null,
            // ncdmd.member.memberHypertensionDto.medicine.duration = null,
            // ncdmd.member.memberHypertensionDto.medicine.quantity = null,
            // ncdmd.member.memberHypertensionDto.medicine.specialInstruction = null
        }

        ncdmd.addMedicineToEdit = function (index, item) {
            item.quantityNew = item.frequencyNew * item.durationNew
            let med = [];
            let today = new Date()
            let diffDays = Math.round(Math.abs((today.getTime() - new Date(item.startDate).getTime()) / (24 * 60 * 60 * 1000)))
            med = ncdmd.medicines.filter(function (data) {
                return data.medicineId === item.medicineId
            })
            if (item.durationNew > item.duration && (item.quantityNew - item.quantity) > med[0].balanceInHand) {
                toaster.pop('error', "Available Medicine Quantity is " + med[0].balanceInHand)
            }
            else if (today > new Date(item.startDate) && item.durationNew < diffDays) {
                toaster.pop('error', "You can not assign new duration less than " + diffDays + " days. Already " + diffDays + " days are completed for the medicine.")
            }
            else {
                if (item.quantityNew && item.quantityNew != null && item.quantityNew > 0
                    && item.frequencyNew && item.frequencyNew != null && item.frequencyNew > 0) {
                    item.expiryDate = null
                    ncdmd.editData.push({
                        id: item.id,
                        memberId: ncdmd.patientId,
                        medicineName: item.medicineName,
                        medicineId: item.medicineId,
                        startDate: item.startDate,
                        isReturn: item.isReturn,
                        frequency: item.frequencyNew,
                        duration: item.durationNew,
                        quantity: item.quantityNew,
                        specialInstruction: item.specialInstruction
                    })
                    item.duration = item.durationNew
                    item.quantity = item.quantityNew
                    item.frequency = item.frequencyNew
                    item.isEdit = !item.isEdit
                }
                else {
                    toaster.pop('error', "Enter valid quantity / frequency")
                }
            }
        }

        ncdmd.retrieveTreatmentHistory = function (diseaseCode) {
            return NcdDnhddDAO.retrieveTreatmentHistory(Number(ncdmd.patientId), diseaseCode).then(function (res) {
                switch (diseaseCode) {
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
                    if (ncdmd.member.memberHypertensionDto.systolicBloodPressure >= ncdmd.maxSysBP ||
                        ncdmd.member.memberHypertensionDto.systolicBloodPressure <= ncdmd.minAllowedSysBP ||
                        ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= ncdmd.maxDiaBP ||
                        ncdmd.member.memberHypertensionDto.diastolicBloodPressure <= ncdmd.minAllowedDiaBP) {
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
                    if (ncdmd.member.memberDiabetesDto.fastingBloodSugar >= ncdmd.maxBloodSugar || ncdmd.member.memberDiabetesDto.postPrandialBloodSugar >= ncdmd.maxPP2BS) {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'Confirmed';
                    } else {
                        ncdmd.member.memberDiabetesDto.diagnosisDto.diagnosis = 'No Abnormality Detected';
                    }
                }
            }
        };

        ncdmd.openFollowupModal = function () {
            let followup = {
                userHealthInfras: ncdmd.userHealthInfras,
                followup: {}
            };
            followup.followup.memberId = Number(ncdmd.patientId);
            followup.followup.referralFrom = $state.params.type
            followup.followup.isFemale = ncdmd.member.basicDetails.gender != 'M'
            const modalInstance = $uibModal.open({
                templateUrl: 'app/ncd/refferedpatients/views/followup.modal.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    followup: function () {
                        return followup;
                    }
                },
                controller: function ($scope, followup, NcdDnhddDAO, $uibModalInstance) {
                    $scope.followup = followup.followup;
                    $scope.userHealthInfras = followup.userHealthInfras;
                    if ($scope.userHealthInfras && $scope.userHealthInfras.length == 1) {
                        $scope.followup.healthInfraId = $scope.userHealthInfras[0].id;
                        $scope.followup.healthInfraName = $scope.userHealthInfras[0].name;
                    }
                    $scope.ok = function (followupForm) {
                        followupForm.$setSubmitted();
                        if (followupForm.$valid) {
                            NcdDnhddDAO.saveFollowup($scope.followup).then(function (res) {
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
                QueryDao.execute({
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
            const header = "<div class='print_header display-none'><img class='print_logo' src=\"img/dnhdd/web_logo.png\" ><h2 class='page_title'>" + "Patient Summary " + "</h2></div>";
            ncdmd.footer = "Generated by " + ncdmd.loggedInUser.name + " at " + new Date().toLocaleString();
            _setSymptoms();
            let promiseList = [];
            promiseList.push(ncdmd.retrieveTreatmentHistory('HT'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('O'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('C'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('B'));
            promiseList.push(ncdmd.retrieveTreatmentHistory('D'));
            promiseList.push(ncdmd.retrieveReffForToday(ncdmd.patientId));
            promiseList.push(ncdmd.retrieveNextFollowup(ncdmd.patientId));
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
            return NcdDnhddDAO.retrieveReffForToday(memberid).then(function (res) {
                ncdmd.member.reffForToday = res;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
        }
        ncdmd.retrieveNextFollowup = function (memberid) {
            Mask.show()
            return NcdDnhddDAO.retrieveNextFollowup(memberid).then(function (res) {
                ncdmd.member.nextFollowup = {};
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
                    ncdmd.isUserHealthFetch = true;
                    ncdmd.isSingleInfra = res.result.length === 1;
                    ncdmd.userHealthInfras = ncdmd.isSingleInfra ? res.result[0] : res.result;
                })
            }
        };

        ncdmd.hypertensionExaminedValuesChanged = function () {
            ncdmd.member.memberHypertensionDto.startTreatment = null;
            ncdmd.member.memberHypertensionDto.referralDto = null;

            const sysBP = ncdmd.member.memberHypertensionDto.systolicBloodPressure;
            const diaBP = ncdmd.member.memberHypertensionDto.diastolicBloodPressure;

            if (!ncdmd.showHypertensionSubType) {
                if (sysBP >= ncdmd.maxSysBP || diaBP >= ncdmd.maxDiaBP ||
                    sysBP <= ncdmd.minAllowedSysBP || diaBP <= ncdmd.minAllowedDiaBP) {
                    ncdmd.member.memberHypertensionDto.status = 'CONFIRMED'
                    return;
                }
                ncdmd.member.memberHypertensionDto.status = 'NO_ABNORMALITY';
            } else if (!(!!sysBP && !!diaBP)) {
                return;
            }

            if (sysBP < ncdmd.maxSysBP && diaBP < ncdmd.maxDiaBP &&
                sysBP > ncdmd.minAllowedSysBP && diaBP > ncdmd.minAllowedDiaBP) {
                ncdmd.member.memberHypertensionDto.subType = 'CONTROLLED'
            } else {
                ncdmd.member.memberHypertensionDto.subType = 'UNCONTROLLED'
            }
        }

        ncdmd.diabetesExaminedValuesChanged = function () {
            ncdmd.member.memberDiabetesDto.startTreatment = null;
            ncdmd.member.memberDiabetesDto.referralDto = null;

            const diabetesDto = ncdmd.member.memberDiabetesDto;

            if (!ncdmd.showDiabetesSubType) {
                if (diabetesDto.fastingBloodSugar >= ncdmd.maxFastingBloodSugar
                    || diabetesDto.postPrandialBloodSugar >= ncdmd.maxPP2BS
                    || diabetesDto.bloodSugar >= ncdmd.maxBloodSugar
                    || diabetesDto.dka
                    || diabetesDto.hba1c >= 6.5) {
                    ncdmd.member.memberDiabetesDto.status = 'CONFIRMED';
                    return;
                } 
                ncdmd.member.memberDiabetesDto.status = 'NO_ABNORMALITY';
            } else {
                if (diabetesDto.fastingBloodSugar < ncdmd.maxFastingBloodSugar
                    && diabetesDto.postPrandialBloodSugar < ncdmd.maxPP2BS
                    && diabetesDto.bloodSugar < ncdmd.maxBloodSugar
                    && !diabetesDto.dka
                    && diabetesDto.hba1c < 7) {
                    ncdmd.member.memberDiabetesDto.subType = 'CONTROLLED';
                } else {
                    ncdmd.member.memberDiabetesDto.subType = 'UNCONTROLLED';
                }
            }
        }

        ncdmd.retrieveHypertensionDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDnhddDAO.retrieveHypertensionDetailsByMemberAndDate(ncdmd.patientId, moment(ncdmd.member.memberHypertensionDto.screeningDate).valueOf()).then(function (response) {
                ncdmd.maxHypertensionDiabetesFollowUpDate = moment(ncdmd.member.memberHypertensionDto.screeningDate).add(60, 'days').format('YYYY-MM-DD');
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
                    ncdmd.member.memberHypertensionDto.subType = null;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveDiabetesDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDnhddDAO.retrieveDiabetesDetailsByMemberAndDate(ncdmd.patientId, moment(ncdmd.member.memberHypertensionDto.screeningDate).valueOf()).then(function (response) {
                ncdmd.maxHypertensionDiabetesFollowUpDate = moment(ncdmd.member.memberHypertensionDto.screeningDate).add(60, 'days').format('YYYY-MM-DD');
                if (response.id != null) {
                    ncdmd.diabetesAlreadyFilled = true;
                    ncdmd.member.memberDiabetesDto.fastingBloodSugar = response.fastingBloodSugar
                    ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = response.postPrandialBloodSugar
                    ncdmd.member.memberDiabetesDto.bloodSugar = response.bloodSugar
                    ncdmd.member.memberDiabetesDto.dka = response.dka
                    ncdmd.member.memberDiabetesDto.hba1c = response.hba1c
                } else {
                    ncdmd.diabetesAlreadyFilled = false;
                    ncdmd.member.memberDiabetesDto.fastingBloodSugar = null;
                    ncdmd.member.memberDiabetesDto.postPrandialBloodSugar = null;
                    ncdmd.member.memberDiabetesDto.bloodSugar = null;
                    ncdmd.member.memberDiabetesDto.dka = null;
                    ncdmd.member.memberDiabetesDto.hba1c = null;
                    ncdmd.member.memberDiabetesDto.subType = null;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveOralDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDnhddDAO.retrieveOralDetailsByMemberAndDate(ncdmd.patientId, moment(ncdmd.member.memberOralDto.screeningDate).valueOf()).then(function (response) {
                ncdmd.maxOralFollowUpDate = moment(ncdmd.member.memberOralDto.screeningDate).add(60, 'days').format('YYYY-MM-DD');
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
                    ncdmd.minOralFollowUpDate = moment(ncdmd.member.memberOralDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveBreastDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDnhddDAO.retrieveBreastDetailsByMemberAndDate(ncdmd.patientId, moment(ncdmd.member.memberBreastDto.screeningDate).valueOf()).then(function (response) {
                ncdmd.maxBreastFollowUpDate = moment(ncdmd.member.memberBreastDto.screeningDate).add(60, 'days').format('YYYY-MM-DD');
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
                    ncdmd.member.memberBreastDto.sizeChange = null;
                    ncdmd.member.memberBreastDto.sizeChangeLeft = null;
                    ncdmd.member.memberBreastDto.sizeChangeRight = null;
                    ncdmd.member.memberBreastDto.nippleNotOnSameLevel = null;
                    ncdmd.member.memberBreastDto.anyRetractionOfNipple = null;
                    ncdmd.member.memberBreastDto.retractionOfLeftNipple = null;
                    ncdmd.member.memberBreastDto.retractionOfRightNipple = null;
                    ncdmd.member.memberBreastDto.lymphadenopathy = null;
                    ncdmd.member.memberBreastDto.leftLymphadenopathy = null;
                    ncdmd.member.memberBreastDto.rightLymphadenopathy = null;
                    ncdmd.member.memberBreastDto.dischargeFromNipple = null;
                    ncdmd.member.memberBreastDto.dischargeFromLeftNipple = null;
                    ncdmd.member.memberBreastDto.dischargeFromRightNipple = null;
                    ncdmd.member.memberBreastDto.visualSkinDimplingRetraction = null;
                    ncdmd.member.memberBreastDto.visualNippleRetractionDistortion = null;
                    ncdmd.member.memberBreastDto.visualLumpInBreast = null;
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
            NcdDnhddDAO.retrieveCervicalDetailsByMemberAndDate(ncdmd.patientId, moment(ncdmd.member.memberCervicalDto.screeningDate).valueOf()).then(function (response) {
                ncdmd.maxCervicalFollowUpDate = moment(ncdmd.member.memberCervicalDto.screeningDate).add(60, 'days').format('YYYY-MM-DD');
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ncdmd.member.memberCervicalDto.papsmearTest = response.papsmearTest === null ? 'null' : response.papsmearTest;
                    ncdmd.member.memberCervicalDto.viaTest = response.viaTest;
                    ncdmd.cervicalAlreadyFilled = true;
                } else {
                    ncdmd.cervicalAlreadyFilled = false;
                    ncdmd.member.memberCervicalDto.papsmearTest = undefined;
                    ncdmd.member.memberCervicalDto.viaTest = undefined;
                    ncdmd.minCervicalFollowUpDate = moment(ncdmd.member.memberCervicalDto.screeningDate).add(1, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.showTreatmentHistory = function (diseaseCode) {
            if (!diseaseCode) return;
            const queryDto = {
                parameters: { memberId:Number(ncdmd.patientId) }
            };
            switch (diseaseCode) {
                case 'HT':
                    $scope.disease = 'Hypertension';
                    queryDto.code = 'ncd_hypertension_treatment_history'
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
                    toaster.pop('warning', 'No treatment History!');
                } else {
                    const modalInstance = $uibModal.open({
                        templateUrl: 'app/ncd/refferedpatients/views/treatment-history.modal.html',
                        windowClass: 'cst-modal',
                        size: 'lg',
                        resolve: {
                            diseaseCode: function () {
                                return diseaseCode
                            },
                            disease: function () {
                                return $scope.disease;
                            }
                        },
                        controller: function ($scope, $uibModalInstance, disease, diseaseCode) {
                            $scope.diseaseCode = diseaseCode;
                            $scope.disease = disease
                            $scope.treatmentHistory = res.result;
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
            },function () {
                toaster.pop('error', 'Something went wrong while retrieving history!');
            }).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveLastCbacDetails = function (userId) {
            QueryDAO.execute({
                code: 'ncd_retrieve_last_cbac_details',
                parameters: {
                    userId: Number(userId)
                }
            }).then(function (res) {
                if (res?.result?.length > 0) {
                    ncdmd.isCbacDone = true;
                    ncdmd.lastRecordOfCbac = res.result[0];
                    ncdmd.hmisId = ncdmd.lastRecordOfCbac.hmisId;
                    ncdmd.lastRecordOfCbac.bmiCategory = getCategoryByBmi(ncdmd.lastRecordOfCbac.bmi);
                } else {
                    ncdmd.isCbacDone = false;
                }
            }, GeneralUtil.showMessageOnApiCallFailure);
        };

        const getCategoryByBmi = function (bmi) {
            for (let category in bmiCategories) {
                let range = bmiCategories[category];
                if (bmi >= range.min && bmi <= range.max) {
                    return category;
                }
            }
            return 'N.A.';
        }

        ncdmd.onChange = function (type) {
            switch (type) {
                case 'HT':
                    if (ncdmd.member.memberHypertensionDto?.medicineDetail) {
                        ncdmd.member.memberHypertensionDto.medicineDetail = [];
                    }
                    ncdmd.member.memberHypertensionDto.referralDto = {};
                    break;
                case 'D':
                    if (ncdmd.member.memberDiabetesDto?.medicineDetail) {
                        ncdmd.member.memberDiabetesDto.medicineDetail = null;
                    }
                    ncdmd.member.memberDiabetesDto.referralDto = {};
                    if (ncdmd.member.memberHypertensionDto?.medicineDetail) {
                        ncdmd.member.memberHypertensionDto.medicineDetail = [];
                    }
                    ncdmd.member.memberHypertensionDto.referralDto = {};

                    break;
                case 'SAME_INFRA':
                    if (ncdmd.member.memberHypertensionDto.referralDto?.isReferred) {
                        ncdmd.member.memberHypertensionDto.referralDto = {};
                    }
                    if (ncdmd.member.memberDiabetesDto.referralDto?.isReferred) {
                        ncdmd.member.memberDiabetesDto.referralDto = {};
                    }
                    if (ncdmd.member.memberCervicalDto.referralDto?.isReferred) {
                        ncdmd.member.memberCervicalDto.referralDto = {};
                    }
                    if (ncdmd.member.memberOralDto.referralDto?.isReferred) {
                        ncdmd.member.memberOralDto.referralDto = {};
                    }
                    if (ncdmd.member.memberBreastDto.referralDto?.isReferred) {
                        ncdmd.member.memberBreastDto.referralDto = {};
                    }
                    break;
                case 'HSTATUS':
                    ncdmd.member.memberHypertensionDto.startTreatment = null;
                    ncdmd.member.memberHypertensionDto.followUpDate = null;
                    break;
                case 'DSTATUS':
                    ncdmd.member.memberDiabetesDto.startTreatment = null;
                    ncdmd.member.memberHypertensionDto.followUpDate = null;
                    break;
                case 'OSTATUS':
                    ncdmd.member.memberOralDto.followUpDate = null;
                    break;
                case 'BSTATUS':
                    ncdmd.member.memberBreastDto.followUpDate = null;
                    break;
                case 'CSTATUS':
                    ncdmd.member.memberCervicalDto.followUpDate = null;
                    break;
            }
        }

        ncdmd.getInitialLetterAndValue = function (readings) {
            let finalReadings = readings.split(',');
            let initialLetters = ['RBS-', 'FBS-', 'PP2BS-']
            let results = [];
            finalReadings.forEach((reading, idx) => {
                reading != 0 && results.push(initialLetters[idx] + reading);
            });
            return results.join(', ');
        };

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMemberDetailDnhddController', NcdMemberDetailDnhddController);
})(window.angular);
