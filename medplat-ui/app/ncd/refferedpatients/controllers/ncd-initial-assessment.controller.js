(function (angular) {
    function NcdInitialAssessment($state, GeneralUtil, NcdDAO, toaster, AuthenticateService, Mask, $scope, QueryDAO, NdhmHipDAO, $uibModal, $filter) {
        var ctrl = this;
        ctrl.today = new Date();
        ctrl.initialAssessmentForm = {}
        ctrl.selectedHistoryDisease = []

        var init = function () {
            ctrl.type = $state.params.type
            ctrl.today1 = new Date();
            ctrl.today1.setHours(0, 0, 0, 0);
            ctrl.initialAssessmentForm.screeningDate = ctrl.today1;
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.retrieveLoggedInUserFeatureJson();
                ctrl.retrieveHistoryDiseaseList();
                ctrl.retrieveInitialAssessmentDetailsByMemberAndDate();
            })
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

        ctrl.retrieveInitialAssessmentDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveInitialAssessmentDetailsByMemberAndDate($state.params.id, moment($scope.ncdmd.dateinfra ? $scope.ncdmd.dateinfra.screeningDate : ctrl.initialAssessmentForm.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.initialAssessmentAlreadyFilled = true;
                    ctrl.initialAssessmentForm.height = response.height
                    ctrl.initialAssessmentForm.weight = response.weight
                    ctrl.initialAssessmentForm.waistCircumference = response.waistCircumference
                    ctrl.initialAssessmentForm.bmi = response.bmi
                    ctrl.initialAssessmentForm.excessUrination = response.excessUrination;
                    ctrl.initialAssessmentForm.excessThirst = response.excessThirst;
                    ctrl.initialAssessmentForm.excessHunger = response.excessHunger;
                    ctrl.initialAssessmentForm.changeInDietaryHabits = response.changeInDietaryHabits;
                    ctrl.initialAssessmentForm.suddenVisualDisturbances = response.suddenVisualDisturbances;
                    ctrl.initialAssessmentForm.significantEdema = response.significantEdema;
                    ctrl.initialAssessmentForm.breathlessness = response.breathlessness;
                    ctrl.initialAssessmentForm.angina = response.angina;
                    ctrl.initialAssessmentForm.intermittentClaudication = response.intermittentClaudication;
                    ctrl.initialAssessmentForm.limpness = response.limpness;
                    ctrl.initialAssessmentForm.formType = response.formType;
                    ctrl.initialAssessmentForm.recurrentSkinGUI = response.recurrentSkinGUI;
                    ctrl.initialAssessmentForm.delayedHealingOfWounds = response.delayedHealingOfWounds
                    ctrl.initialAssessmentForm.healthInfraId = response.healthInfraId
                    let historyDisease = response.historyDisease.replace('[', '').replace(']', '').replace(' ', '').split(',')
                    angular.forEach(historyDisease, function (item) {
                        disease = _.find(ctrl.diseaseHistoryList, function (diseaseHistoryList) {
                            if (Number(item) == diseaseHistoryList.id) {
                                diseaseHistoryList.selected = true
                                ctrl.selectedHistoryDisease.push(diseaseHistoryList.id+'')
                            }
                        })
                        switch (item) {
                            case 'NONE':
                                ctrl.None = true;
                                break;
                            case 'OTHER':
                                ctrl.Other = true;
                                ctrl.initialAssessmentForm.otherDisease = response.otherDisease
                                break;
                        }
                    })

                } else {
                    ctrl.initialAssessmentAlreadyFilled = false;
                    ctrl.clearForm();
                    ctrl.fetchPrefilledDetails();
                }
                //setting height,weight,bmi based on memberAdditionalInfo as priority rather then last initial assessment data, as it may be null.
                if ($scope.ncdmd.member.memberAdditionalInfo != null) {
                    ctrl.initialAssessmentForm.weight = $scope.ncdmd.member.memberAdditionalInfo.weight;
                    ctrl.initialAssessmentForm.height = $scope.ncdmd.member.memberAdditionalInfo.height;
                    ctrl.initialAssessmentForm.bmi = $scope.ncdmd.member.memberAdditionalInfo.bmi;
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.calculateBmi = function () {
            if (ctrl.initialAssessmentForm.weight && ctrl.initialAssessmentForm.height) {
                let heightInM = ctrl.initialAssessmentForm.height / 100;
                let heightInM2 = heightInM * heightInM;
                ctrl.initialAssessmentForm.bmi = (ctrl.initialAssessmentForm.weight / heightInM2).toFixed(2);
            }
        }

        ctrl.saveInitialAssessment = function () {
            if (ctrl.form.$valid) {
                ctrl.initialAssessmentForm.referredFromHealthInfrastructureId = ctrl.initialAssessmentForm.healthInfraId;
                //ctrl.initialAssessmentForm.healthInfraId = ctrl.initialAssessmentForm.healthInfrastructure.id
                if (!ctrl.initialAssessmentForm.memberId) {
                    ctrl.initialAssessmentForm.memberId = Number($state.params.id);
                }
                ctrl.initialAssessmentForm.doneBy = ctrl.type
                ctrl.initialAssessmentForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                ctrl.initialAssessmentForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                ctrl.initialAssessmentForm.selectedHistoryDisease = ctrl.selectedHistoryDisease.toString()
                NcdDAO.saveInitialAssessment(ctrl.initialAssessmentForm).then(function (res) {
                    toaster.pop('success', "InitialAssessment Details saved successfully");
                    let screeningDate = ctrl.initialAssessmentForm.screeningDate;
                    let healthInfraId = ctrl.initialAssessmentForm.healthInfraId;
                    ctrl.initialAssessmentForm.screeningDate = null;
                    ctrl.clearForm();
                    ctrl.initialAssessmentAlreadyFilled = true;
                    $scope.ncdmd.fetchComplications($state.params.id)
                    if (ctrl.rights.isShowHIPModal) {
                        ctrl.saveHealthIdDetails(screeningDate, res.id, healthInfraId);
                    }
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        }

        ctrl.saveHealthIdDetails = (screeningDate, serviceId, healthInfraId) => {
            let memberObj = {
                memberId: ctrl.initialAssessmentForm.memberId
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
                            serviceType: "NCD_DIAGNOSTIC_INITIAL_ASSESSMENT",
                            diseaseCode: "IA",
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
            ctrl.initialAssessmentForm.referredFromHealthInfrastructureId = null;
            ctrl.initialAssessmentForm.height = null;
            ctrl.initialAssessmentForm.weight = null;
            ctrl.initialAssessmentForm.waistCircumference = null;
            ctrl.initialAssessmentForm.bmi = null;
            ctrl.initialAssessmentForm.excessUrination = false;
            ctrl.initialAssessmentForm.excessThirst = false;
            ctrl.initialAssessmentForm.excessHunger = false;
            ctrl.initialAssessmentForm.changeInDietaryHabits = false;
            ctrl.initialAssessmentForm.suddenVisualDisturbances = false;
            ctrl.initialAssessmentForm.significantEdema = false;
            ctrl.initialAssessmentForm.breathlessness = false;
            ctrl.initialAssessmentForm.angina = false;
            ctrl.initialAssessmentForm.intermittentClaudication = false;
            ctrl.initialAssessmentForm.limpness = false;
            ctrl.initialAssessmentForm.formType = 'OPD';
            ctrl.initialAssessmentForm.recurrentSkinGUI = false;
            ctrl.initialAssessmentForm.delayedHealingOfWounds = false;
            ctrl.initialAssessmentForm.healthInfraId = null;
            // ctrl.selectedHistoryDisease = [];
            ctrl.Stroke = null;
            ctrl.vcd = null;
            ctrl.kd = null;
            ctrl.Diabetes = null;
            ctrl.diabeticRetinopathy = null;
            ctrl.Other = null;
            ctrl.None = null;
            ctrl.diabetesFoot = null;
            ctrl.initialAssessmentForm.otherDisease = null;
            ctrl.form.$setPristine();
        }

        ctrl.toggleSelection = function (disease) {
            var inx = -1;
            if (disease == 'NONE' && ctrl.None) {
                ctrl.selectedHistoryDisease = [];
                ctrl.diseaseHistoryList.forEach((item) => {
                    item.selected = false
                })
                ctrl.selectedHistoryDisease.push(disease);
            }
            else {
                if (ctrl.None) {
                    ctrl.None = null
                    angular.forEach(ctrl.selectedHistoryDisease, function (item, index) {
                        if (item === 'NONE') {
                            inx = index;
                        }
                    })
                    if (inx >= 0) {
                        ctrl.selectedHistoryDisease.splice(inx, 1)
                        var inx = -1;
                    }
                }
                angular.forEach(ctrl.selectedHistoryDisease, function (item, index) {
                    if (item === disease) {
                        inx = index;
                    }
                })
                if (inx >= 0) {
                    ctrl.selectedHistoryDisease.splice(inx, 1)
                }
                else {
                    ctrl.selectedHistoryDisease.push(disease);
                }
            }
            // console.log(ctrl.selectedHistoryDisease);
        };

        ctrl.fetchPrefilledDetails = function () {
            var querydto = {
                code: 'ncd_initial_assessment_prefilled_data',
                parameters: {
                    memberId: Number($state.params.id)
                }
            }
            QueryDAO.execute(querydto).then(function (res) {
                if (res.result.length > 0) {
                    // ctrl.initialAssessmentForm.height = res.result[0].height
                    // ctrl.initialAssessmentForm.weight = res.result[0].weight
                    // ctrl.initialAssessmentForm.bmi = res.result[0].bmi
                    // ctrl.initialAssessmentForm.waistCircumference = res.result[0].waist
                    res.result.forEach((item) => {
                        switch (item.key) {
                            // case 'weight':
                            //     ctrl.initialAssessmentForm.weight = ctrl.initialAssessmentForm.weight || item.value
                            //     break;
                            // case 'height':
                            //     ctrl.initialAssessmentForm.height = ctrl.initialAssessmentForm.height || item.value
                            //     break;
                            // case 'bmi':
                            //     ctrl.initialAssessmentForm.bmi = ctrl.initialAssessmentForm.bmi || item.value
                            //     break;
                            case 'waist':
                                ctrl.initialAssessmentForm.waistCircumference = ctrl.initialAssessmentForm.waistCircumference || item.value
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

        ctrl.retrieveHistoryDiseaseList = function () {
            QueryDAO.execute({
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'diseaseHistoryList'
                }
            }).then((response) => {
                ctrl.diseaseHistoryList = response.result;
                if ($scope.ncdmd.member.ncdMemberEntity != null && $scope.ncdmd.member.ncdMemberEntity.diseaseHistory!=null) {
                    var diseases = $scope.ncdmd.member.ncdMemberEntity.diseaseHistory.replace('[', '').replace(']', '').replace(' ', '').split(',')
                    angular.forEach(diseases, function (item) {
                        disease = _.find(ctrl.diseaseHistoryList, function (diseaseHistoryList) {
                            if (Number(item) == diseaseHistoryList.id) {
                                diseaseHistoryList.selected = true
                                ctrl.selectedHistoryDisease.push(diseaseHistoryList.id+'')
                            }
                        })
                        switch (item) {
                            case 'NONE':
                                ctrl.None = true;
                                break;
                            case 'OTHER':
                                ctrl.Other = true;
                                ctrl.initialAssessmentForm.otherDisease = $scope.ncdmd.member.ncdMemberEntity.otherDiseaseHistory
                                break;
                        }
                    })
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdInitialAssessment', NcdInitialAssessment);
})(window.angular);