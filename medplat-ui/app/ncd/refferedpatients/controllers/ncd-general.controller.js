(function (angular) {
    function NcdGeneral($state, GeneralUtil, Mask, NcdDAO, toaster, AuthenticateService, $scope, QueryDAO, NdhmHipDAO, $uibModal, $filter) {
        var ctrl = this;
        ctrl.today = new Date();
        ctrl.medicineDetail = [];
        ctrl.minGeneralFollowUpDate = moment(ctrl.today).add(1, 'days');
        ctrl.maxGeneralFollowUpDate = moment(ctrl.today).add(90, 'days');
        ctrl.generalForm = {};
        ctrl.editedMedicineDetail = [];
        ctrl.deletedMedicineDetails = [];
        ctrl.medicineFormSubmit = true;
        ctrl.comment;
        ctrl.htComment;
        ctrl.mhComment;
        ctrl.dbComment;
        ctrl.showIndividualComment = false;


        var init = function () {
            ctrl.showIndividualComment = false;
            ctrl.getCommentByClinicOrHomeVisit();
            ctrl.type = $state.params.type
            if ($scope.ncdmd.dateinfra) {
                if ($scope.ncdmd.dateinfra.screeningDate) {
                    ctrl.generalForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate;
                    ctrl.retrieveGeneralDetailsByMemberAndDate();
                }
                if ($scope.ncdmd.dateinfra.healthInfraId) {
                    $scope.ncdmd.retrieveMedicinesByHealthInfra($scope.ncdmd.dateinfra.healthInfraId)
                }
                AuthenticateService.getLoggedInUser().then(function (res) {
                    ctrl.loggedInUser = res.data;
                    ctrl.retrieveLoggedInUserFeatureJson();
                })
            }
            ctrl.fetchConfirmedDisease()
        }

        ctrl.getCommentByClinicOrHomeVisit = () => {
            QueryDAO.execute({
                code: 'get_comment_from_clinic_or_home_visit',
                parameters: {
                    memberId: Number($state.params.id)
                }
            }).then(function (res) {
                ctrl.comment = res.result.length == 0 ? ctrl.getCommentIndividual() : res.result[0].comment; var hypertensionData = res.result.find(row => row.h_flag !== null);

            })
        }

        ctrl.getCommentIndividual = () => {
            ctrl.showIndividualComment = true;
            QueryDAO.execute({
                code: 'get_comment_from_ncd_screening',
                parameters: {
                    memberId: Number($state.params.id)
                }
            }).then(function (res) {
                for (r of res.result) {
                    if (r.tbl == 'ht') {
                        ctrl.htComment = r.note == 'null' ? 'Not Available' : r.note;
                    }
                    else if (r.tbl == 'mh') {
                        ctrl.mhComment = r.note == 'null' ? 'Not Available' : r.note;
                    }
                    else if (r.tbl == 'db') {
                        ctrl.dbComment = r.note == 'null' ? 'Not Available' : r.note;
                    }
                }
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

        ctrl.retrieveGeneralDetailsByMemberAndDate = function () {
            Mask.show();
            ctrl.generalForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate;
            NcdDAO.retrieveGeneralDetailsByMemberAndDate($state.params.id, moment($scope.ncdmd.dateinfra.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.generalAlreadyFilled = true;
                    ctrl.generalForm.symptoms = response.symptoms
                    ctrl.generalForm.clinicalObservation = response.clinicalObservation
                    ctrl.generalForm.diagnosis = response.diagnosis
                    ctrl.generalForm.remarks = response.remarks
                    ctrl.generalForm.otherDetails = response.otherDetails
                    ctrl.generalForm.healthInfraId = response.healthInfraId
                    ctrl.generalForm.doesRequiredRef = response.doesRequiredRef
                    ctrl.generalForm.refferralReason = response.refferralReason
                    ctrl.generalForm.refferralPlace = response.refferralPlace
                    ctrl.generalForm.followupPlace = response.followupPlace
                    ctrl.generalForm.followUpDate = response.followUpDate
                    ctrl.generalForm.comment = response.comment
                    ctrl.generalForm.markReview = response.markReview
                    ctrl.generalForm.category = response.category
                    ctrl.generalForm.followUpDisease = response.followUpDisease
                } else {
                    ctrl.generalAlreadyFilled = false;
                    ctrl.clearForm();
                    ctrl.minGeneralFollowUpDate = moment($scope.ncdmd.dateinfra.screeningDate).add(1, 'days');
                    ctrl.maxGeneralFollowUpDate = moment(ctrl.minGeneralFollowUpDate).add(90, 'days');
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.saveGeneral = function () {
            if (ctrl.form.$valid) {
                if (ctrl.medicineFormSubmit) {
                    ctrl.generalForm.healthInfraId = ctrl.generalForm.healthInfraId;
                    ctrl.generalForm.referredFromHealthInfrastructureId = ctrl.generalForm.healthInfraId;
                    ctrl.generalForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                    ctrl.generalForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                    if (ctrl.medicineDetail.length > 0) {
                        ctrl.medicineDetail.forEach(element => {
                            element.expiryDate = moment(element.startDate).add(element.duration, 'days')
                        });
                        ctrl.generalForm.medicineDetail = ctrl.medicineDetail
                    }
                    else {
                        ctrl.generalForm.medicineDetail = [];
                    }
                    ctrl.generalForm.takeMedicine = ctrl.takeMedicine
                    ctrl.medicineDetail = [];
                    // Add edited medicines
                    if (ctrl.editedMedicineDetail.length > 0) {
                        ctrl.generalForm.editedMedicineDetail = ctrl.editedMedicineDetail
                    }
                    else {
                        ctrl.generalForm.editedMedicineDetail = []
                    }
                    ctrl.editedMedicineDetail = [];
                    // Add deleted medicines
                    if (ctrl.deletedMedicineDetails.length > 0) {
                        ctrl.generalForm.deletedMedicineDetail = ctrl.deletedMedicineDetails
                    }
                    else {
                        ctrl.generalForm.deletedMedicineDetail = []
                    }
                    ctrl.deletedMedicineDetails = [];
                    if (!ctrl.generalForm.memberId) {
                        ctrl.generalForm.memberId = Number($state.params.id);
                    }
                    if (ctrl.type === 'CONSULTANT') {
                        ctrl.generalForm.doesRequiredRef = true;
                    }
                    // if (ncdmd.member.memberGeneralDto.startTreatment || ncdmd.showDiabetesMedicines) {
                    //     ncdmd.member.memberGeneralDto.status = 'TREATMENT_STARTED';
                    // }
                    // if (ncdmd.member.memberGeneralDto.referralDto != null && ncdmd.member.memberGeneralDto.referralDto.isReferred) {
                    //     ncdmd.member.memberGeneralDto.status = 'REFERRED';
                    //     ncdmd.member.memberGeneralDto.reason = ncdmd.member.memberGeneralDto.referralDto.reason;
                    //     ncdmd.member.memberGeneralDto.healthInfraId = ncdmd.member.memberGeneralDto.referralDto.healthInfraId;

                    // }
                    // if (ncdmd.member.memberGeneralDto.doesRequiredRef) {
                    //     ncdmd.member.memberGeneralDto.status = "REFER_NO_VISIT"
                    // }

                    ctrl.generalForm.doneBy = ctrl.type
                    ctrl.generalForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate
                    ctrl.generalForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId
                    NcdDAO.saveGeneral(ctrl.generalForm).then(function (res) {
                        toaster.pop('success', "General Details saved successfully");
                        if (ctrl.rights.isShowHIPModal) {
                            ctrl.saveHealthIdDetails(ctrl.generalForm.screeningDate, res.id, ctrl.generalForm.healthInfraId);
                        }
                        // ncdmd.retrieveMemberDetails(true);
                        ctrl.clearForm();
                        ctrl.generalForm.screeningDate = ctrl.today
                        ctrl.minGeneralFollowUpDate = moment(ctrl.today).add(1, 'days');
                        ctrl.maxGeneralFollowUpDate = moment(ctrl.today).add(90, 'days');
                        ctrl.fetchPrescribedMedicines($state.params.id);
                        ctrl.generalAlreadyFilled = true;
                    }, GeneralUtil.showMessageOnApiCallFailure)
                }
                else {
                    toaster.pop('warning', "You have filled the medicine form, but not submitted");
                }
            }
        }

        ctrl.saveHealthIdDetails = (screeningDate, serviceId, healthInfraId) => {
            let memberObj = {
                memberId: ctrl.generalForm.memberId
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
                            serviceType: "NCD_DIAGNOSTIC_GENERAL",
                            diseaseCode: "G",
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
            ctrl.generalForm.symptoms = null
            ctrl.generalForm.clinicalObservation = null
            ctrl.generalForm.diagnosis = null
            ctrl.generalForm.remarks = null
            ctrl.generalForm.otherDetails = null
            ctrl.generalForm.referredFromInstitute = null
            ctrl.generalForm.doesRequiredRef = false
            ctrl.generalForm.refferralReason = null
            ctrl.generalForm.refferralPlace = null
            ctrl.generalForm.followupPlace = null
            ctrl.generalForm.followUpDate = null
            ctrl.generalForm.comment = null
            ctrl.generalForm.markReview = null
            ctrl.generalForm.healthInfraId = null
            ctrl.generalForm.category = null
            ctrl.generalForm.followUpDisease = []
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

        ctrl.fetchConfirmedDisease = function () {
            var querydto = {
                code: 'ncd_fetch_confirmed_diseases',
                parameters: {
                    memberId: Number($state.params.id)
                }
            }
            QueryDAO.execute(querydto).then(function (res) {
                if (res.result.length > 0 && res.result[0].diseases != null) {
                    var disease = res.result[0].diseases.split(',')
                    ctrl.generalForm.followUpDisease = disease
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdGeneral', NcdGeneral);
})(window.angular);
