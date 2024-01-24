(function (angular) {
    function NcdCVCFormController($state, toaster, NcdDAO, GeneralUtil, Mask, QueryDAO, $window) {
        var ctrl = this;
        ctrl.cvcForm = {}
        ctrl.medicineFormSubmit = true;
        ctrl.medicineDetail = [];

        var init = function () {
            ctrl.type = $state.params.type;
            ctrl.memberId = $state.params.id;
            NcdDAO.retrieveDetails(ctrl.memberId).then(function (res) {
                ctrl.member = {};
                ctrl.member = res;
                ctrl.fetchComplications()
                if (ctrl.dateinfra) {
                    if (ctrl.dateinfra.screeningDate) {
                        ctrl.cvcForm.screeningDate = ctrl.dateinfra.screeningDate;
                        ctrl.retrieveCVCDetailsByMemberAndDate();
                    }
                    // if (ctrl.dateinfra.healthInfraId) {
                    //     ctrl.retrieveMedicinesByHealthInfra(ctrl.dateinfra.healthInfraId)
                    // }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }

        ctrl.retrieveMedicinesByHealthInfra = function (healthInfraId) {
            if (healthInfraId) {
                NcdDAO.retriveAllDrugInventory(healthInfraId, 'CFY').then(function (res) {
                    ctrl.medicineData = res
                    ctrl.medicineDetail = [];
                })
            }
        }

        ctrl.retrieveMBBSMOReview = function (member_id) {
            NcdDAO.retrieveLastCommentByMBBS(member_id).then(function (response) {
                ctrl.MBBSComment = response.comment;
                ctrl.MBBSCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.retrieveConsultantComment = function (memberId) {
            NcdDAO.retrieveLastCommentForGeneralByMemberIdAndType(memberId, "CONSULTANT").then(function (response) {
                ctrl.ConComment = response.comment;
                ctrl.ConCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.retrieveMOComment = function (memberId) {
            NcdDAO.retrieveLastCommentForGeneralByMemberIdAndType(memberId, "MO").then(function (response) {
                ctrl.MOComment = response.comment;
                ctrl.MOCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.retrieveLastCommentByMOReview = function (memberId) {
            NcdDAO.retrieveLastCommentByMOReview(memberId).then(function (response) {
                ctrl.MOReviewComment = response.comment;
                ctrl.MOReviewCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.retrieveLastCommentByMOReviewFollowup = function (memberId) {
            NcdDAO.retrieveLastCommentByMOReviewFollowup(memberId).then(function (response) {
                ctrl.MOReviewFollowupComment = response.comment;
                ctrl.MOReviewFollowupCommentBy = response.commentBy;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.saveCVCForm = function () {
            if (ctrl.form.$valid) {
                if (ctrl.medicineFormSubmit) {
                    Mask.show();
                    ctrl.cvcForm.screeningDate = ctrl.dateinfra.screeningDate
                    ctrl.cvcForm.healthInfraId = ctrl.dateinfra.healthInfraId
                    if (ctrl.medicineDetail.length > 0) {
                        ctrl.medicineDetail.forEach(element => {
                            element.expiryDate = moment(ctrl.cvcForm.screeningDate).add(element.duration, 'days')
                        });
                        ctrl.cvcForm.medicineDetail = ctrl.medicineDetail
                    }
                    else {
                        ctrl.cvcForm.medicineDetail = []
                    }
                    ctrl.cvcForm.takeMedicine = ctrl.takeMedicine
                    ctrl.medicineDetail = [];
                    ctrl.cvcForm.memberId = ctrl.memberId
                    ctrl.cvcForm.doneBy = ctrl.type
                    NcdDAO.saveCVCForm(ctrl.cvcForm).then(function (res) {
                        toaster.pop('success', "CVC Details saved successfully");
                        ctrl.cvcFormAlreadyFilled = true;
                        ctrl.fetchPrescribedMedicines(ctrl.memberId);
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

        ctrl.retrieveCVCDetailsByMemberAndDate = function () {
            Mask.show();
            NcdDAO.retrieveCVCDetailsByMemberAndDate(ctrl.memberId, moment(ctrl.dateinfra.screeningDate).valueOf(), ctrl.type).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.cvcFormAlreadyFilled = true;
                } else {
                    ctrl.cvcFormAlreadyFilled = false;
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.fetchComplications = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'get_complications_for_ncd_from_known_history',
                parameters: {
                    memberId: Number(ctrl.memberId)
                }
            }).then(function (res) {
                if(res.result.length > 0){
                    ctrl.historyDisease = res.result[0];                    
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.fetchPrescribedMedicines = function (userId) {
            NcdDAO.retrievePrescribedMedicineForUser(userId).then(function (res) {
                ctrl.prescribedMedicine = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error)
            })
        }

        ctrl.printPatientSummary = function () {
            let url = $state.href('techo.ncd.patientSummary', { id: ctrl.memberId });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        };

        init();
    }
    angular.module('imtecho.controllers').controller('NcdCVCFormController', NcdCVCFormController);
})(window.angular);