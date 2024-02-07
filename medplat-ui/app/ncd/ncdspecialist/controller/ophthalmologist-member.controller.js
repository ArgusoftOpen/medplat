(function (angular) {
    function ophthalmologistMemberController($state, toaster, GeneralUtil, AuthenticateService, Mask, QueryDAO, NcdophthalmologistDAO) {
        var ctrl = this;
        ctrl.formData = {}
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
            ctrl.fetchResearcherInputs(ctrl.memberId)
        };

        ctrl.screeningDateChange = function () {
            var date = moment(ctrl.screeningDate).format("YYYY-MM-DD");
            ctrl.fetchAllData(date);
        }

        ctrl.fetchAllData = function (date) {
            Mask.show();
            NcdophthalmologistDAO.retrieveOphthalmologistReponse(ctrl.memberId, moment(ctrl.screeningDate).valueOf()).then((res) => {
                if (res.id) {
                    ctrl.formData = res
                    ctrl.isFormSubmitted = true
                    toaster.pop("warning", "Form already submitted for today")
                }
                else {
                    ctrl.isFormSubmitted = false
                    ctrl.formData = {}
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
            if (ctrl.ophthalmologistForm.$valid) {
                Mask.show();
                ctrl.formData.screeningDate = ctrl.screeningDate
                ctrl.formData.memberId = ctrl.memberId
                NcdophthalmologistDAO.saveOphthalmologistResponse(ctrl.formData).then((res) => {
                    toaster.pop("success", "Form submitted successfully")
                    ctrl.isFormSubmitted = true
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });
            }
        };

        ctrl.fetchResearcherInputs = function (memberId) {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_researcher_input_for_ophthalmologist',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.researcherRoleInput = res.result[0]
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
            Mask.hide();
        }

        init();
    }
    angular.module('imtecho.controllers').controller('ophthalmologistMemberController', ophthalmologistMemberController);
})(window.angular);