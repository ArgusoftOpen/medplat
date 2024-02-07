(function (angular) {
    function NcdHypertension($state, GeneralUtil, Mask, toaster, NcdDAO, $scope, $uibModal, AuthenticateService,  QueryDAO, NdhmHipDAO, $filter) {
        var ctrl = this;
        ctrl.today = new Date();
        ctrl.hypertensionRecords = [];
        ctrl.medicineDetail = [];
        ctrl.hypertensionForm = {}
        ctrl.medicineFormSubmit = true;

        var init = function () {
            ctrl.type = $state.params.type
            if ($scope.ncdmd.dateinfra) {
                if ($scope.ncdmd.dateinfra.screeningDate) {
                    ctrl.hypertensionForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate;
                    ctrl.retrieveHypertensionDetailsByMemberAndDate();
                }
                if ($scope.ncdmd.dateinfra.healthInfraId) {
                    $scope.ncdmd.retrieveMedicinesByHealthInfra($scope.ncdmd.dateinfra.healthInfraId)
                }
            }
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.retrieveLoggedInUserFeatureJson();
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

        //Fetch previous records
        ctrl.fetchLastRecords = function () {
            NcdDAO.retrieveLastRecordForHypertensionByMemberId($state.params.id).then(function (response) {
                ctrl.hypertensionRecords = response;
                if (response.length > 0) {
                    //If already diagnosed as Hypertension no need to ask que again
                    ctrl.hypertensionForm.doesSuffering = response[0].doesSuffering;
                    ctrl.hypertensionExaminedValuesChanged();
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        //Fetch details if for particular date for the member form is already filled
        ctrl.retrieveHypertensionDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveHypertensionDetailsByMemberAndDate($state.params.id, moment($scope.ncdmd.dateinfra.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.hypertensionAlreadyFilled = true;
                    ctrl.hypertensionForm.systolicBloodPressure = response.systolicBp;
                    ctrl.hypertensionForm.diastolicBloodPressure = response.diastolicBp;
                    ctrl.hypertensionForm.heartRate = response.pulseRate;
                    ctrl.hypertensionForm.isRegularRythm = response.isRegularRythm;
                    ctrl.hypertensionForm.murmur = response.murmur;
                    ctrl.hypertensionForm.bilateralClear = response.bilateralClear;
                    ctrl.hypertensionForm.bilateralBasalCrepitation = response.bilateralBasalCrepitation;
                    ctrl.hypertensionForm.rhonchi = response.rhonchi;
                    ctrl.hypertensionForm.healthInfraId = response.healthInfraId;
                    ctrl.hypertensionForm.htn = response.htn;
                    ctrl.hypertensionForm.pedalOedema = response.pedalOedema;
                    ctrl.hypertensionExaminedValuesChanged();
                } else {
                    ctrl.hypertensionAlreadyFilled = false;
                    ctrl.clearForm();
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        //Check the status based on BP values changed
        ctrl.hypertensionExaminedValuesChanged = function () {
            // if (!ctrl.showHypertensionSubType) {
            //     if (ctrl.member.memberHypertensionDto.systolicBloodPressure >= 140 || ncdmd.member.memberHypertensionDto.diastolicBloodPressure >= 90) {
            //         ctrl.member.memberHypertensionDto.status = 'UNCONTROLLED'
            //     } else {
            //         ctrl.member.memberHypertensionDto.status = 'CONTROLLED'
            //     }
            // } else if (ctrl.showHypertensionSubType) {
            //     if (ctrl.member.memberHypertensionDto.systolicBloodPressure < 140 && ncdmd.member.memberHypertensionDto.diastolicBloodPressure < 90) {
            //         ctrl.member.memberHypertensionDto.subType = 'CONTROLLED'
            //     } else {
            //         ctrl.member.memberHypertensionDto.subType = 'UNCONTROLLED'
            //     }
            // }
            if (ctrl.hypertensionForm.systolicBloodPressure && ctrl.hypertensionForm.diastolicBloodPressure) {
                if (ctrl.hypertensionForm.systolicBloodPressure < 140 && ctrl.hypertensionForm.diastolicBloodPressure < 90) {
                    ctrl.hypertensionForm.status = 'CONTROLLED'
                } else {
                    ctrl.hypertensionForm.status = 'UNCONTROLLED'
                }
            }
        }

        //Save the form data
        ctrl.saveHyperTension = function (form) {
            if (form.$valid) {
                if (ctrl.medicineFormSubmit) {
                    //Modal to take confirmation
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
                        ctrl.hypertensionForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                        ctrl.hypertensionForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                        if (ctrl.medicineDetail.length > 0) {
                            ctrl.medicineDetail.forEach(element => {
                                element.expiryDate = moment(element.startDate).add(element.duration, 'days')
                            });
                            ctrl.hypertensionForm.medicineDetail = ctrl.medicineDetail
                        }
                        else {
                            ctrl.hypertensionForm.medicineDetail = []
                        }
                        ctrl.hypertensionForm.takeMedicine = ctrl.takeMedicine
                        ctrl.medicineDetail = [];
                        ctrl.hypertensionForm.referredFromHealthInfrastructureId = ctrl.hypertensionForm.healthInfraId;
                        ctrl.hypertensionForm.healthInfraId = ctrl.hypertensionForm.healthInfraId;
                        if (!ctrl.hypertensionForm.memberId) {
                            ctrl.hypertensionForm.memberId = Number($state.params.id);
                        }
                        // ctrl.hypertensionForm.doesSuffering = ctrl.isAlreadyDiagnosedHypertension === true ? true : ctrl.hypertensionForm.doesSuffering
                        ctrl.hypertensionForm.doneBy = ctrl.type
                        NcdDAO.saveHyperTension(ctrl.hypertensionForm).then((res) => {
                            toaster.pop('success', "Hypertension Details saved successfully");
                            if (ctrl.rights.isShowHIPModal) {
                                ctrl.saveHealthIdDetails(ctrl.hypertensionForm.screeningDate, res.id, ctrl.hypertensionForm.healthInfraId);
                            }
                            ctrl.hypertensionForm.screeningDate = null;
                            //clear form
                            ctrl.clearForm();
                            //Retrieve data to show latest value in side profile. Note, this method is from parent
                            // $scope.ncdmd.retrieveMemberDetails();
                            //Update previous record table
                            ctrl.fetchLastRecords();
                            ctrl.fetchPrescribedMedicines($state.params.id);
                            ctrl.hypertensionAlreadyFilled = true;
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        })
                    });
                } else {
                    toaster.pop('warning', "You have filled the medicine form, but not submitted");
                }
            }
        }


        ctrl.saveHealthIdDetails = (screeningDate, serviceId, healthInfraId) => {
            let memberObj = {
                memberId: ctrl.hypertensionForm.memberId
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
                            serviceType: "NCD_DIAGNOSTIC_HYPERTENSION",
                            diseaseCode: "HT",
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

        //clear form and set defaults
        ctrl.clearForm = function () {
            ctrl.hypertensionForm.systolicBloodPressure = null
            ctrl.hypertensionForm.diastolicBloodPressure = null
            ctrl.hypertensionForm.heartRate = null
            ctrl.hypertensionForm.isRegularRythm = true
            ctrl.hypertensionForm.murmur = false
            ctrl.hypertensionForm.bilateralClear = true
            ctrl.hypertensionForm.bilateralBasalCrepitation = false
            ctrl.hypertensionForm.rhonchi = false
            // ctrl.hypertensionForm.doesSuffering = null
            ctrl.hypertensionForm.healthInfrastructure = null
            ctrl.hypertensionForm.healthInfraId = null
            ctrl.hypertensionForm.htn = null
            ctrl.hypertensionForm.pedalOedema = false
            ctrl.form.$setPristine();
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
            if(ctrl.hypertensionForm.doesSuffering == true){
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
                    ctrl.hypertensionForm.doesSuffering = true
                }).catch(function() {
                    ctrl.hypertensionForm.doesSuffering = false
                });
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdHypertension', NcdHypertension);
})(window.angular);