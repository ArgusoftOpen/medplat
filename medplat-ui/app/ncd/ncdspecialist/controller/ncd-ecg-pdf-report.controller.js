(function (angular) {
    var NcdEcgPdfReport = function (QueryDAO, Mask, SohElementConfigurationDAO, GeneralUtil, $state, $sce) {
        var ctrl = this;
        ctrl.showOptions = false

        ctrl.init = function () {
            ctrl.memberId = $state.params.id
            ctrl.fetchMemberDetails(ctrl.memberId)
            ctrl.fetchDatesForECGPDF(ctrl.memberId)
        }

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
                ctrl.ecgData = res.result
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.showGraph = function () {
            if (ctrl.doc.doc_id) {
                Mask.show();
                SohElementConfigurationDAO.getFileById(ctrl.doc.doc_id).then(res => {
                    if (ctrl.doc.doc_type == 'pdf') {
                        var file = new Blob([res.data], { type: 'application/pdf' });
                        var fileURL = URL.createObjectURL(file);
                        ctrl.pdfReport = $sce.trustAsResourceUrl(fileURL);
                        ctrl.showOptions = true
                    }
                    if (ctrl.doc.doc_type == 'image') {
                        ctrl.overviewImage = URL.createObjectURL(res.data)
                        ctrl.showOptions = true
                    }
                }).catch(err => {
                    if (err.status === 404) {
                        toaster.pop('error', "Image not found!");
                    } else {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                    }
                    // coursePreviewModalCtrl.attachmentImage = null;
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.init();
    };
    angular.module('imtecho.controllers').controller('NcdEcgPdfReport', NcdEcgPdfReport);
})(window.angular);