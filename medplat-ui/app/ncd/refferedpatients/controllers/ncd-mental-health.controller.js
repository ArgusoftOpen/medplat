(function (angular) {
    function NcdMentalHealth($state, NcdDAO, GeneralUtil, Mask, toaster, MENTAL_HEALTH_STATUS, $scope, AuthenticateService, QueryDAO, NdhmHipDAO, $uibModal, $filter) {
        var ctrl = this;
        ctrl.mentalDropDownOptions = [1, 2, 3, 4];
        ctrl.mentalHealthStatus = Object.values(MENTAL_HEALTH_STATUS);
        ctrl.mentalHealthDto = {};
        // ctrl.mentalHealthAlreadyExists = false;
        ctrl.medicineDetail = [];
        ctrl.medicineFormSubmit = true;

        const init = function () {
            ctrl.type = $state.params.type
            if ($scope.ncdmd.dateinfra) {
                if ($scope.ncdmd.dateinfra.screeningDate) {
                    ctrl.mentalHealthDto.screeningDate = $scope.ncdmd.dateinfra.screeningDate;
                    ctrl.retrieveMentalHealthDetailsByMemberAndDate();
                }
                if ($scope.ncdmd.dateinfra.healthInfraId) {
                    $scope.ncdmd.retrieveMedicinesByHealthInfra($scope.ncdmd.dateinfra.healthInfraId)
                }
            }
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.retrieveLoggedInUserFeatureJson();
            })
            ctrl.retrieveLastRecordForMentalHealthByMemberId();
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

        ctrl.retrieveLastRecordForMentalHealthByMemberId = () => {
            NcdDAO.retrieveLastRecordForMentalHealthByMemberId($state.params.id).then(function (response) {
                ctrl.lastRecordOfMentalHealth = response;
                ctrl.mentalHealthDto.doesSuffering = ctrl.lastRecordOfMentalHealth.doesSuffering;
                if (ctrl.lastRecordOfMentalHealth.doesSuffering) {
                    ctrl.mentalHealthDto.doesSuffering = true;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.retrieveMentalHealthDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveMentalHealthDetailsByMemberAndDate($state.params.id, moment($scope.ncdmd.dateinfra.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.mentalHealthAlreadyFilled = true;
                    ctrl.mentalHealthDto.talk = response.talk
                    ctrl.mentalHealthDto.ownDailyWork = response.ownDailyWork
                    ctrl.mentalHealthDto.socialWork = response.socialWork
                    ctrl.mentalHealthDto.understanding = response.understanding
                    ctrl.mentalHealthDto.healthInfraId = response.healthInfraId
                    ctrl.mentalHealthDto.doesSuffering = response.doesSuffering;
                    ctrl.mentalHealthDto.htn = response.isHTN
                    // if (ctrl.mentalHealthDto.doesSuffering == true) {
                    //     ctrl.mentalHealthAlreadyExists = true;
                    // }
                } else {
                    ctrl.mentalHealthAlreadyFilled = false;
                    ctrl.mentalHealthDto.talk = null;
                    ctrl.mentalHealthDto.ownDailyWork = null;
                    ctrl.mentalHealthDto.socialWork = null;
                    ctrl.mentalHealthDto.understanding = null;
                    ctrl.mentalHealthDto.healthInfraId = null;
                    ctrl.mentalHealthDto.doesSuffering = null;
                    // ctrl.minMentalHealthFollowUpDate = moment(ctrl.mentalHealthDto.screeningDate).add(1, 'days');
                }
                ctrl.mentalHealthExaminedValuesChanged();
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.saveMentalHealth = function () {
            ctrl.mentalHealthForm.$setSubmitted();
            if (ctrl.mentalHealthForm.$valid) {
                if (ctrl.medicineFormSubmit) {
                    ctrl.mentalHealthDto.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                    ctrl.mentalHealthDto.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                    if (ctrl.medicineDetail.length > 0) {
                        ctrl.medicineDetail.forEach(element => {
                            element.expiryDate = moment(element.startDate).add(element.duration, 'days')
                        });
                        ctrl.mentalHealthDto.medicineDetail = ctrl.medicineDetail
                    }
                    else {
                        ctrl.mentalHealthDto.medicineDetail = []
                    }
                    ctrl.mentalHealthDto.takeMedicine = ctrl.takeMedicine
                    ctrl.medicineDetail = [];
                    // ctrl.mentalHealthDto.referredFromInstitute = JSON.parse(ctrl.mentalHealthDto.referredFromInstitute);
                    ctrl.mentalHealthDto.referredFromHealthInfrastructureId = ctrl.mentalHealthDto.healthInfraId;
                    if (!ctrl.mentalHealthDto.memberId) {
                        ctrl.mentalHealthDto.memberId = Number($state.params.id);
                    }
                    // if (ctrl.mentalHealthDto.doesSuffering == true) {
                    //     ctrl.mentalHealthAlreadyExists = true;
                    // }
                    // ctrl.mentalHealthDto.doesSuffering = ctrl.mentalHealthAlreadyExists === true ? true : ctrl.mentalHealthDto.doesSuffering
                    // if (ctrl.mentalHealthDto.startTreatment || ctrl.showDiabetesMedicines) {
                    //     ctrl.mentalHealthDto.status = 'TREATMENT_STARTED';
                    // }
                    // if (ctrl.mentalHealthDto.referralDto != null && ctrl.mentalHealthDto.referralDto.isReferred) {
                    //     ctrl.mentalHealthDto.status = 'REFERRED';
                    //     ctrl.mentalHealthDto.reason = ctrl.mentalHealthDto.referralDto.reason;
                    //     ctrl.mentalHealthDto.healthInfraId = ctrl.mentalHealthDto.referralDto.healthInfraId;

                // }
                
                ctrl.mentalHealthDto.doneBy = ctrl.type
                ctrl.mentalHealthDto.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                ctrl.mentalHealthDto.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                NcdDAO.saveMentalHealth(ctrl.mentalHealthDto).then(function (res) {
                    toaster.pop('success', "MentalHealth Details saved successfully");
                    if (ctrl.rights.isShowHIPModal) {
                        ctrl.saveHealthIdDetails(ctrl.mentalHealthDto.screeningDate, res.id, ctrl.mentalHealthDto.healthInfraId);
                    }

                    ctrl.clearForm();
                    ctrl.fetchPrescribedMedicines($state.params.id);
                    ctrl.mentalHealthAlreadyFilled = true;
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
                else{
                    toaster.pop('warning', "You have filled the medicine form, but not submitted");
                }
            }
        };

        ctrl.saveHealthIdDetails = (screeningDate, serviceId, healthInfraId) => {
            let memberObj = {
                memberId: ctrl.mentalHealthDto.memberId
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
                            serviceType: "NCD_DIAGNOSTIC_MENTAL_HEALTH",
                            diseaseCode: "MH",
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
            ctrl.mentalHealthDto.screeningDate = null;
            ctrl.mentalHealthDto.healthInfraId = null;
            ctrl.mentalHealthDto.talk = null;
            ctrl.mentalHealthDto.ownDailyWork = null;
            ctrl.mentalHealthDto.socialWork = null;
            ctrl.mentalHealthDto.understanding = null;
            // ctrl.mentalHealthDto.doesSuffering = false;
            ctrl.mentalHealthDto.status = null;
            ctrl.mentalHealthDto.htn = null;
            ctrl.mentalHealthForm.$setPristine();
        }

        ctrl.mentalHealthExaminedValuesChanged = function () {
            ctrl.mentalHealthDto.startTreatment = null;
            ctrl.mentalHealthDto.referralDto = null;
            if (ctrl.mentalHealthDto.talk > 1
                || ctrl.mentalHealthDto.ownDailyWork > 1
                || ctrl.mentalHealthDto.socialWork > 1
                || ctrl.mentalHealthDto.understanding > 1) {
                ctrl.mentalHealthDto.todayResult = MENTAL_HEALTH_STATUS.UNCONTROLLED;
                ctrl.mentalHealthDto.status = MENTAL_HEALTH_STATUS.UNCONTROLLED;
            } else {
                ctrl.mentalHealthDto.todayResult = MENTAL_HEALTH_STATUS.CONTROLLED;
                ctrl.mentalHealthDto.status = MENTAL_HEALTH_STATUS.CONTROLLED;
            }
        }

        //Fetch prescribed medicines
        ctrl.fetchPrescribedMedicines = function (userId) {
            NcdDAO.retrievePrescribedMedicineForUser(userId).then(function (res) {
                ctrl.prescribedMedicine = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error)
            })
        }

        ctrl.showConfirmationModalForSuffering = function(){
            if(ctrl.mentalHealthDto.doesSuffering == true){
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
                    ctrl.mentalHealthDto.doesSuffering = true
                }).catch(function() {
                    ctrl.mentalHealthDto.doesSuffering = false
                });
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMentalHealth', NcdMentalHealth);
})(window.angular);