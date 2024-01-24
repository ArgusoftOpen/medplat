(function (angular) {
    function NcdMoSpecialistReviewController($state, toaster, GeneralUtil, AuthenticateService, Mask, QueryDAO, $window, NcdMoSpecialistMemberDAO) {
        var ctrl = this;
        ctrl.ecgFormData = {}
        ctrl.strokeFormData = {}
        ctrl.amputationFormData = {}
        ctrl.renalFormData = {}
        ctrl.today = moment(new Date()).format("YYYY-MM-DD");
        ctrl.date = moment(new Date()).format("MM/DD/YYYY");
        ctrl.screeningDate = ctrl.date;

        var init = function () {
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
            })
            if ($state.params.id) {
                ctrl.memberId = $state.params.id
                ctrl.fetchMemberDetails(ctrl.memberId)
            }
            ctrl.fetchAllData(ctrl.today);
            ctrl.fetchDatesForECG(ctrl.memberId)
            ctrl.fetchDatesForECGPDF(ctrl.memberId)
            ctrl.fetchCardiologistRejection(ctrl.memberId)
        };

        ctrl.screeningDateChange = function () {
            ctrl.screeningDate = moment(ctrl.date).format("YYYY-MM-DD");
            ctrl.fetchAllData(ctrl.screeningDate);
        }

        ctrl.fetchAllData = function (date) {
            Mask.show();
            let dtolist = [];
            var dto1 = {
                code: 'get_ecg_form_data',
                parameters: {
                    member_id: ctrl.memberId,
                    screening_date: date
                },
                sequence: 1
            }
            dtolist.push(dto1);
            var dto2 = {
                code: 'get_stroke_form_data',
                parameters: {
                    member_id: ctrl.memberId,
                    screening_date: date
                },
                sequence: 2
            }
            dtolist.push(dto2);
            var dto3 = {
                code: 'get_amputation_form_data',
                parameters: {
                    member_id: ctrl.memberId,
                    screening_date: date
                },
                sequence: 3
            }
            dtolist.push(dto3);
            var dto4 = {
                code: 'get_renal_form_data',
                parameters: {
                    member_id: ctrl.memberId,
                    screening_date: date
                },
                sequence: 4
            }
            dtolist.push(dto4);

            QueryDAO.executeAll(dtolist).then(function (res) {
                // ecg data
                if (res[0].result.length > 0) {
                    ctrl.isEcgFormSubmitted = true;
                    ctrl.ecgFormData = res[0].result[0]
                    toaster.pop("warning", "Form already submitted")
                }
                else {
                    ctrl.isEcgFormSubmitted = false;
                    ctrl.ecgFormData.needsRetake = null;
                    ctrl.ecgFormData.oldMI = null;
                    ctrl.ecgFormData.lvh = null;
                    ctrl.ecgFormData.type = null;
                }

                //stroke data
                if (res[1].result.length > 0) {
                    ctrl.isStrokeFormSubmitted = true;
                    ctrl.strokeFormData = res[1].result[0]
                }
                else {
                    ctrl.isStrokeFormSubmitted = false;
                    ctrl.strokeFormData.strokePresent = null;
                }

                //amputation data
                if (res[2].result.length > 0) {
                    ctrl.isAmputationFormSubmitted = true;
                    ctrl.amputationFormData = res[2].result[0]
                }
                else {
                    ctrl.isAmputationFormSubmitted = false;
                    ctrl.amputationFormData.amputationPresent = null;
                }

                //renal data
                if (res[3].result.length > 0) {
                    ctrl.isRenalFormSubmitted = true;
                    ctrl.renalFormData = res[3].result[0]
                }
                else {
                    ctrl.isRenalFormSubmitted = false;
                    ctrl.renalFormData.isSCreatinineDone = null;
                    ctrl.renalFormData.sCreatinineValue = null;
                    ctrl.renalFormData.isRenalComplicationPresent = null;
                }

            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });


        };

        ctrl.fetchMemberDetails = function (memberId) {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_member_detail_for_specialist_role',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.memberDetails = res.result[0]
                    ctrl.memberAdditionalDetails = JSON.parse(ctrl.memberDetails.additional_info.replace(/\\/g, ''))
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
            Mask.hide();
        }

        ctrl.loadStrokeData = function () {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_stroke_data',
                    parameters: {
                        member_id: ctrl.memberId
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.isStrokePresent = res.result[0].isStrokePresent;
                    ctrl.researcherRoleInput = res.result[0].researcherRoleInput;
                    ctrl.careCoordinatorInput = res.result[0].careCoordinatorInput;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.loadAmputationData = function () {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_amputation_data',
                    parameters: {
                        member_id: ctrl.memberId
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.isAmputation = res.result[0].isAmputation;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.loadRenalData = function () {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_renal_data',
                    parameters: {
                        member_id: ctrl.memberId
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.albuminLevelInUrine = res.result[0].albuminLevelInUrine;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.tab = function (tab) {
            ctrl.selectedTab = tab;
            switch (tab) {
                case 'ecg':
                    if (ctrl.isEcgFormSubmitted) {
                        toaster.pop("warning", "Form submitted successfully")
                    }
                    break;
                case 'stroke':
                    ctrl.loadStrokeData();
                    if (ctrl.isStrokeFormSubmitted) {
                        toaster.pop("warning", "Form submitted successfully")
                    }
                    break;
                case 'amputation':
                    ctrl.loadAmputationData();
                    if (ctrl.isAmputationFormSubmitted) {
                        toaster.pop("warning", "Form submitted successfully")
                    }
                    break;
                case 'renal':
                    ctrl.loadRenalData();
                    if (ctrl.isRenalFormSubmitted) {
                        toaster.pop("warning", "Form submitted successfully")
                    }
                    break;
            }
        };

        ctrl.saveEcgData = function (data) {
            ctrl.ecgForm.$setSubmitted();
            if (ctrl.ecgForm.$valid) {
                ctrl.ecgFormData.screeningDate = ctrl.date
                ctrl.ecgFormData.memberId = ctrl.memberId
                NcdMoSpecialistMemberDAO.saveEcgData(ctrl.ecgFormData).then(function (res) {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isEcgFormSubmitted = true;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
                ctrl.ecgForm.$setPristine();
            }
        };

        ctrl.saveStrokeData = function (data) {
            ctrl.strokeForm.$setSubmitted();
            if (ctrl.strokeForm.$valid) {
                ctrl.strokeFormData.screeningDate = ctrl.date
                ctrl.strokeFormData.memberId = ctrl.memberId
                NcdMoSpecialistMemberDAO.saveStrokeData(ctrl.strokeFormData).then(function (res) {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isStrokeFormSubmitted = true;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
                ctrl.strokeForm.$setPristine();
            }
        };

        ctrl.saveAmputationData = function (data) {
            ctrl.amputationForm.$setSubmitted();
            if (ctrl.amputationForm.$valid) {
                ctrl.amputationFormData.screeningDate = ctrl.date
                ctrl.amputationFormData.memberId = ctrl.memberId
                NcdMoSpecialistMemberDAO.saveAmputationData(ctrl.amputationFormData).then(function (res) {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isAmputationFormSubmitted = true;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
                ctrl.amputationForm.$setPristine();
            }
        };

        ctrl.saveRenalData = function (data) {
            ctrl.renalForm.$setSubmitted();
            if (ctrl.renalForm.$valid) {
                ctrl.renalFormData.screeningDate = ctrl.date
                ctrl.renalFormData.memberId = ctrl.memberId
                NcdMoSpecialistMemberDAO.saveRenalData(ctrl.renalFormData).then(function (res) {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isRenalFormSubmitted = true;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
                ctrl.renalForm.$setPristine();
            }
        };

        ctrl.save = function () {
            Mask.show();
            var data = {
                memberId: ctrl.memberId,
                userId: ctrl.loggedInUser.id,
                screeningDate: moment(ctrl.screeningDate).format("YYYY-MM-DD")
            };
            switch (ctrl.selectedTab) {
                case 'ecg':
                    ctrl.saveEcgData(data);
                    break;
                case 'stroke':
                    ctrl.saveStrokeData(data);
                    break;
                case 'amputation':
                    ctrl.saveAmputationData(data);
                    break;
                case 'renal':
                    ctrl.saveRenalData(data);
                    break;
            }
            Mask.hide();
        };

        ctrl.showECGGraphModal = function () {
            let url = $state.href('techo.ncd.ecgmember', { id: ctrl.memberId});
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        }

        ctrl.showECGPDFReportModal = function () {
            let url = $state.href('techo.ncd.ecgpdfreport', { id: ctrl.memberId});
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        }

        ctrl.fetchDatesForECG = function (memberId) {
            ctrl.ecgData = []
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_ecg_dates_by_member',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.showEcgButton = true
                }
                else {
                    ctrl.showEcgButton = false
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.fetchDatesForECGPDF = function (memberId) {
            ctrl.ecgData = []
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_ecg_dates_by_member_pdf',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.showEcgPdfButton = true
                }
                else {
                    ctrl.showEcgPdfButton = false
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.fetchCardiologistRejection = function (memberId) {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_cardiologist_rejection_by_member_id',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.isRejected = true
                    ctrl.rejectionNote = res.result[0].note
                }
                else {
                    ctrl.isRejected = false
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMoSpecialistReviewController', NcdMoSpecialistReviewController);
})(window.angular);