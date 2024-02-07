(function (angular) {
    function NcdMemberDetailController($state, NcdDAO, $filter, toaster, $uibModal, GeneralUtil, AuthenticateService, Mask, $q, QueryDAO, NdhmHipDAO, $window) {
        var ncdmd = this;
        ncdmd.keyFn = Object.keys;
        ncdmd.today = new Date();
        
        var init = function () {
            ncdmd.activeClass = 'initialassessment';
            ncdmd.isUserHealthFetch = false
            AuthenticateService.getLoggedInUser().then(function (res) {
                ncdmd.loggedInUser = res.data;
                ncdmd.retrieveLoggedInUserFeatureJson();
            })

            if ($state.params.id) {
                ncdmd.memberId = $state.params.id;
                ncdmd.retrieveMemberDetails();
                ncdmd.retrieveMBBSMOReview(ncdmd.memberId)
                ncdmd.retrieveConsultantComment(ncdmd.memberId)
                ncdmd.retrieveMOComment(ncdmd.memberId)
                ncdmd.retrieveLastCommentByMOReview(ncdmd.memberId)
                ncdmd.retrieveLastCommentByMOReviewFollowup(ncdmd.memberId)
                ncdmd.fetchComplications(ncdmd.memberId)
            }
            ncdmd.diseases = ['IA', 'HT', 'D', 'MH', 'O', 'B', 'C', 'G',];
        };

        var getAge = function (DOB) {
            var birthDate = new Date(DOB);
            var age = new Date().getFullYear() - birthDate.getFullYear();
            var m = new Date().getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && new Date().getDate() < birthDate.getDate())) {
                age = age - 1;
            }

            return age;
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
                        if (response[0]) {
                            if (response[0].id != null && (response[0].doneBy === 'FHW' || response[0].doneBy === 'MPHW' || response[0].doneBy === 'CHO')) {
                                ncdmd.hypertensionAlreadyFilled = true;
                                ncdmd.member.memberHypertensionDto.screeningDate = moment(response[0].screeningDate)
                                ncdmd.member.memberHypertensionDto.systolicBloodPressure = response[0].systolicBloodPressure
                                ncdmd.member.memberHypertensionDto.diastolicBloodPressure = response[0].diastolicBloodPressure
                                ncdmd.member.memberHypertensionDto.heartRate = response[0].heartRate
                            } else {
                                ncdmd.hypertensionAlreadyFilled = false;
                            }
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
                ncdmd.member.basicDetails.age = getAge(ncdmd.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }

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

        ncdmd.printPatientSummary = function () {
            let url = $state.href('techo.ncd.patientSummary', { id: ncdmd.memberId });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
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
                _.map(res, function (followup) {
                    ncdmd.member.nextFollowup[followup.diseaseCode] = followup.followupDate;
                })


            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();

            })
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
                            toaster.pop('warning', 'No data found!');
                        } else {
                            $scope.treatmentHistory = res.result;
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

        ncdmd.retrieveMedicinesByHealthInfra = function(healthInfraId) {
            if(healthInfraId){
                NcdDAO.retriveAllDrugInventory(healthInfraId,'CFY').then(function (res){
                    ncdmd.medicineData = res
                })
            }
        }

        ncdmd.retrieveMBBSMOReview = function(member_id){
            NcdDAO.retrieveLastCommentByMBBS(member_id).then(function (response) {
                ncdmd.MBBSComment = response.comment;
                ncdmd.MBBSCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveConsultantComment = function(memberId){
            NcdDAO.retrieveLastCommentForGeneralByMemberIdAndType(memberId,"CONSULTANT").then(function (response) {
                ncdmd.ConComment = response.comment;
                ncdmd.ConCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveMOComment = function(memberId){
            NcdDAO.retrieveLastCommentForGeneralByMemberIdAndType(memberId,"MO").then(function (response) {
                ncdmd.MOComment = response.comment;
                ncdmd.MOCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveLastCommentByMOReview = function(memberId){
            NcdDAO.retrieveLastCommentByMOReview(memberId).then(function (response) {
                ncdmd.MOReviewComment = response.comment;
                ncdmd.MOReviewCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.retrieveLastCommentByMOReviewFollowup = function(memberId){
            NcdDAO.retrieveLastCommentByMOReviewFollowup(memberId).then(function (response) {
                ncdmd.MOReviewFollowupComment = response.comment;
                ncdmd.MOReviewFollowupCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ncdmd.fetchComplications = function (memberId) {
            Mask.show();
            QueryDAO.execute({
                code: 'get_complications_for_ncd_from_known_history',
                parameters: {
                    memberId: Number(memberId)
                }
            }).then(function (res) {
                if(res.result.length > 0){
                    ncdmd.historyDisease = res.result[0];                    
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMemberDetailController', NcdMemberDetailController);
})(window.angular);
