(function (angular) {
    function NcdDiabetes($state, GeneralUtil, Mask, NcdDAO, toaster, $scope, AuthenticateService, QueryDAO, NdhmHipDAO, $uibModal, $filter) {
        var ctrl = this;
        ctrl.today = new Date();
        ctrl.medicineDetail = [];
        ctrl.diabetesForm = {};
        ctrl.medicineFormSubmit = true;

        var init = function () {
            ctrl.type = $state.params.type
            if ($scope.ncdmd.dateinfra) {
                if ($scope.ncdmd.dateinfra.screeningDate) {
                    ctrl.diabetesForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate;
                    ctrl.retrieveDiabetesDetailsByMemberAndDate();
                }
                if ($scope.ncdmd.dateinfra.healthInfraId) {
                    $scope.ncdmd.retrieveMedicinesByHealthInfra($scope.ncdmd.dateinfra.healthInfraId)
                }
            }
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.retrieveLoggedInUserFeatureJson();
                if ($scope.ncdmd.member.memberAdditionalInfo != null) {
                    ctrl.diabetesForm.weight = $scope.ncdmd.member.memberAdditionalInfo.weight;
                    ctrl.diabetesForm.height = $scope.ncdmd.member.memberAdditionalInfo.height;
                    ctrl.diabetesForm.bmi = $scope.ncdmd.member.memberAdditionalInfo.bmi?.toFixed(2);
                }
                if(!ctrl.diabetesForm.bmi){
                    ctrl.calculateBmi();
                }
            })
            ctrl.fetchLastRecords();
        }

        ctrl.retrieveLoggedInUserFeatureJson = function () {
            Mask.show()
            AuthenticateService.getAssignedFeature("techo.ncd.members").then(function (res) {
                ctrl.rights = res.featureJson;
                if (!ctrl.rights) {
                    ctrl.rights = {};
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        };

        ctrl.retrieveDiabetesDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveDiabetesDetailsByMemberAndDate($state.params.id, moment($scope.ncdmd.dateinfra.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.diabetesAlreadyFilled = true;
                    ctrl.diabetesForm.bloodSugar = response.bloodSugar
                    ctrl.diabetesForm.peripheralPulses = response.peripheralPulses
                    ctrl.diabetesForm.urineSugar = response.urineSugar.toString()
                    ctrl.diabetesForm.healthInfraId = response.healthInfraId
                    ctrl.diabetesForm.status = response.status
                    ctrl.diabetesForm.callusesFeet = response.callusesFeet
                    ctrl.diabetesForm.gangreneFeet = response.gangreneFeet
                    ctrl.diabetesForm.ulcersFeet = response.ulcersFeet
                    ctrl.diabetesForm.prominentVeins = response.prominentVeins
                    ctrl.diabetesForm.edema = response.edema
                    ctrl.diabetesForm.anyInjuries = response.anyInjuries
                    ctrl.diabetesForm.regularRythmCardio = response.regularRythmCardio
                    ctrl.diabetesForm.sensoryLoss = response.sensoryLoss
                    ctrl.diabetesForm.doesSuffering = response.doesSuffering
                    ctrl.diabetesForm.htn = response.htn
                    ctrl.diabetesForm.measurementType = response.measurementType
                } else {
                    ctrl.diabetesAlreadyFilled = false;
                    ctrl.clearForm();
                    //ctrl.fetchPrefilledDetails();
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.diabetesExaminedValuesChanged = function () {
            // ncdmd.member.memberDiabetesDto.startTreatment = null;
            // ncdmd.member.memberDiabetesDto.referralDto = null;
            // if (!ncdmd.showDiabetesSubType) {
            //     if (ncdmd.member.memberDiabetesDto.fastingBloodSugar >= 126
            //         || ncdmd.member.memberDiabetesDto.postPrandialBloodSugar >= 200
            //         || ncdmd.member.memberDiabetesDto.bloodSugar >= 200
            //         || ncdmd.member.memberDiabetesDto.dka
            //         || ncdmd.member.memberDiabetesDto.hba1c >= 6.5) {
            //         ncdmd.member.memberDiabetesDto.status = 'UNCONTROLLED';
            //     } else {
            //         ncdmd.member.memberDiabetesDto.status = 'CONTROLLED';
            //     }
            // } else if (ncdmd.showDiabetesSubType) {
            //     if (ncdmd.member.memberDiabetesDto.fastingBloodSugar < 130
            //         && ncdmd.member.memberDiabetesDto.postPrandialBloodSugar < 200
            //         && ncdmd.member.memberDiabetesDto.bloodSugar < 200
            //         && ncdmd.member.memberDiabetesDto.hba1c < 7) {
            //         ncdmd.member.memberDiabetesDto.subType = 'CONTROLLED';
            //     } else {
            //         ncdmd.member.memberDiabetesDto.subType = 'UNCONTROLLED';
            //     }
            // }
            if (ctrl.diabetesForm.bloodSugar && ctrl.diabetesForm.measurementType) {
                if (ctrl.diabetesForm.measurementType == 'FBS') {
                    ctrl.diabetesForm.status = ctrl.diabetesForm.bloodSugar >= 126 ? "UNCONTROLLED" : "CONTROLLED";
                }
                if (ctrl.diabetesForm.measurementType == 'PP2BS' || ctrl.diabetesForm.measurementType == 'RBS') {
                    ctrl.diabetesForm.status = ctrl.diabetesForm.bloodSugar >= 200 ? "UNCONTROLLED" : "CONTROLLED";
                }
            }
        }

        //Fetch last record
        ctrl.fetchLastRecords = function () {
            NcdDAO.retrieveLastRecordForDiabetesByMemberId($state.params.id).then(function (response) {
                if (response) {
                    //If already diagnosed as Diabetes no need to ask que again
                    ctrl.diabetesForm.doesSuffering = response.doesSuffering;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.saveDiabetes = function () {
            if (ctrl.form.$valid) {
                if (ctrl.medicineFormSubmit) {
                    Mask.show();
                    ctrl.diabetesForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                    ctrl.diabetesForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                    if (ctrl.medicineDetail.length > 0) {
                        ctrl.medicineDetail.forEach(element => {
                            element.expiryDate = moment(element.startDate).add(element.duration, 'days')
                        });
                        ctrl.diabetesForm.medicineDetail = ctrl.medicineDetail
                    }
                    else {
                        ctrl.diabetesForm.medicineDetail = []
                    }
                    ctrl.diabetesForm.takeMedicine = ctrl.takeMedicine
                    ctrl.medicineDetail = [];
                    //adding necesaary fields for save form
                    ctrl.diabetesForm.referredFromHealthInfrastructureId = ctrl.diabetesForm.healthInfraId;
                    if (!ctrl.diabetesForm.memberId) {
                        ctrl.diabetesForm.memberId = Number($state.params.id);
                    }
                    // ctrl.diabetesForm.doesSuffering = ctrl.isAlreadyDiagnosedDiabetes === true ? true : ctrl.diabetesForm.doesSuffering
                    // if (ncdmd.member.memberDiabetesDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    //     ncdmd.member.memberDiabetesDto.status = 'TREATMENT_STARTED';
                    // }
                    // if (ncdmd.member.memberDiabetesDto.referralDto != null && ncdmd.member.memberDiabetesDto.referralDto.isReferred) {
                    //     ncdmd.member.memberDiabetesDto.status = 'REFERRED';
                    //     ncdmd.member.memberDiabetesDto.reason = ncdmd.member.memberDiabetesDto.referralDto.reason;
                    //     ncdmd.member.memberDiabetesDto.healthInfraId = ncdmd.member.memberDiabetesDto.referralDto.healthInfraId;

                    // }
                    ctrl.diabetesForm.doneBy = ctrl.type
                    NcdDAO.saveDiabetes(ctrl.diabetesForm).then(function (res) {
                        toaster.pop('success', "Diabetes Details saved successfully");
                        if (ctrl.rights.isShowHIPModal) {
                            ctrl.saveHealthIdDetails(ctrl.diabetesForm.screeningDate, res.id, ctrl.diabetesForm.healthInfraId);
                        }
                        //clear form
                        ctrl.diabetesForm.screeningDate = null;
                        ctrl.clearForm();
                        ctrl.fetchLastRecords();
                        ctrl.fetchPrescribedMedicines($state.params.id);
                        ctrl.diabetesAlreadyFilled = true;
                        //$scope.ncdmd.retrieveMemberDetails(true);
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    })
                }
                else {
                    toaster.pop('warning', "You have filled the medicine form, but not submitted");
                }
            }
        }

        ctrl.saveHealthIdDetails = (screeningDate, serviceId, healthInfraId) => {
            let memberObj = {
                memberId: ctrl.diabetesForm.memberId
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
                            serviceType: "NCD_DIAGNOSTIC_DIABETES",
                            diseaseCode: "D",
                            serviceId: serviceId,
                            isTokenGenerate: true,
                            careContextName: "Non-communicable diseases",
                            healthInfraId: healthInfraId
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('techo.ncd.memberdetails');
            }, () => {
            });
        }

        ctrl.clearForm = function () {
            ctrl.diabetesForm.bloodSugar = null
            ctrl.diabetesForm.peripheralPulses = true
            ctrl.diabetesForm.healthInfraId = null
            ctrl.diabetesForm.urineSugar = null
            ctrl.diabetesForm.status = null
            ctrl.diabetesForm.callusesFeet = null
            ctrl.diabetesForm.gangreneFeet = null
            ctrl.diabetesForm.ulcersFeet = null
            ctrl.diabetesForm.prominentVeins = null
            ctrl.diabetesForm.edema = null
            ctrl.diabetesForm.anyInjuries = null
            ctrl.diabetesForm.regularRythmCardio = true
            ctrl.diabetesForm.sensoryLoss = false
            // ctrl.diabetesForm.doesSuffering = null
            ctrl.diabetesForm.htn = null
            ctrl.diabetesForm.measurementType = null
            ctrl.form.$setPristine()
        }

        //Fetch prescribed medicines
        ctrl.fetchPrescribedMedicines = function (userId) {
            NcdDAO.retrievePrescribedMedicineForUser(userId).then(function (res) {
                ctrl.prescribedMedicine = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error)
            })
        }

        ctrl.showConfirmationModalForSuffering = function () {
            if (ctrl.diabetesForm.doesSuffering == true) {
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/confirmation.modal.html',
                    controller: 'ConfirmModalController',
                    windowClass: 'cst-modal',
                    size: 'med',
                    resolve: {
                        message: function () {
                            return "Are you sure you want to confirm this member for respective disease?";
                        }
                    }
                });
                modalInstance.result.then(function () {
                    ctrl.diabetesForm.doesSuffering = true
                }).catch(function () {
                    ctrl.diabetesForm.doesSuffering = false
                });
            }
        }

        ctrl.calculateBmi = function () {
            if (ctrl.diabetesForm.weight && ctrl.diabetesForm.height) {
                let heightInM = ctrl.diabetesForm.height / 100;
                let heightInM2 = heightInM * heightInM;
                ctrl.diabetesForm.bmi = (ctrl.diabetesForm.weight / heightInM2).toFixed(2);
            }
        }

        ctrl.fetchPrefilledDetails = function () {
            var querydto = {
                code: 'ncd_initial_assessment_prefilled_data',
                parameters: {
                    memberId: Number($state.params.id)
                }
            }
            QueryDAO.execute(querydto).then(function (res) {
                if (res.result.length > 0) {
                    // ctrl.diabetesForm.height = res.result[0].height
                    // ctrl.diabetesForm.weight = res.result[0].weight
                    // ctrl.diabetesForm.bmi = res.result[0].bmi
                    res.result.forEach((item) => {
                        switch (item.key) {
                            case 'weight':
                                ctrl.diabetesForm.weight = ctrl.diabetesForm.weight || item.value
                                break;
                            case 'height':
                                ctrl.diabetesForm.height = ctrl.diabetesForm.height || item.value
                                break;
                            case 'bmi':
                                ctrl.diabetesForm.bmi = ctrl.diabetesForm.bmi || item.value
                                break;
                            default:
                                break;
                        }
                    })
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdDiabetes', NcdDiabetes);
})(window.angular);