(function (angular) {
    function CardiologistMemberController($state, toaster, GeneralUtil, AuthenticateService, Mask, QueryDAO, $window, NcdCardiologistDAO) {
        var ctrl = this;
        ctrl.ecgFormData = {}
        ctrl.today = moment(new Date()).format("YYYY-MM-DD");
        ctrl.screeningDate = moment(new Date()).format("MM/DD/YYYY");

        var init = function () {
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
            })
            if ($state.params.id) {
                ctrl.memberId = $state.params.id
                ctrl.type = $state.params.type
                ctrl.fetchMemberDetails(ctrl.memberId)
            }
            ctrl.fetchAllData(ctrl.today);
            ctrl.fetchDatesForECG(ctrl.memberId)
        };

        ctrl.screeningDateChange = function () {
            var date = moment(ctrl.screeningDate).format("YYYY-MM-DD");
            ctrl.fetchAllData(date);
        }

        ctrl.fetchAllData = function (date) {
            Mask.show();
            NcdCardiologistDAO.retrieveCardiologistReponse(ctrl.memberId, moment(ctrl.screeningDate).valueOf()).then((res) => {
                if (res.id) {
                    ctrl.ecgFormData = res
                    ctrl.isEcgFormSubmitted = true
                }
                else {
                    ctrl.isEcgFormSubmitted = false
                    ctrl.ecgFormData = {}
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

        ctrl.save = function () {
            if (ctrl.ecgForm.$valid) {
                Mask.show();
                ctrl.ecgFormData.screeningDate = ctrl.screeningDate
                ctrl.ecgFormData.memberId = ctrl.memberId
                NcdCardiologistDAO.saveCardiologistResponse(ctrl.ecgFormData).then((res) => {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isEcgFormSubmitted = true
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
            }
        };

        ctrl.showECGGraphModal = function () {
            let url = $state.href('techo.ncd.ecgmember', { id: ctrl.memberId });
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

        init();
    }
    angular.module('imtecho.controllers').controller('CardiologistMemberController', CardiologistMemberController);
})(window.angular);