(function () {
    function FacilityLinkingController($uibModalInstance, types, response, menu, manageState, Mask, GeneralUtil, toaster, $state, HealthInfraService, QueryDAO) {
        let ctrl = this;
        ctrl.response = response;
        ctrl.types = types;


        ctrl.onNextAction = function () {
            if (ctrl.wayToLinkRecord == 'y') {
                ctrl.fetchFacilityDetail();
            } else if (ctrl.wayToLinkRecord == 'n') {
                ctrl.navigateToCreateOrLogin();
            }
        }

        ctrl.fetchFacilityDetail = function () {
            let fetchHealthInfrastructure = {
                code: 'fetch_all_lgd_code',
                parameters: {
                    childid:ctrl.response.location_id? Number(ctrl.response.location_id) :Number(ctrl.response.locationid)
                },
                sequence: 1
            };
            Mask.show();
            QueryDAO.execute(fetchHealthInfrastructure).then(function (response) {
                ctrl.lgdcodeArr = response.result[0].lgdcode.split(' , ');
                ctrl.searchFacilityDetail();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.searchFacilityDetail = function () {
            ctrl.isAlreadyLinked = false;
            ctrl.infrastructure = null;
            account = {
                facilityId: ctrl.facilityId,
                page: 1,
                resultsPerPage: 1
            }
            // Mask.show();
            // HfrSearchDAO.searchForFacility(account)
            //     .then((res) => {
            //         ctrl.matchFacilityRecord(res.facilities[0]);
            //     }).catch((error) => {
            //         GeneralUtil.showMessageOnApiCallFailure(error);
            //     }).finally(Mask.hide);
        }

        ctrl.matchFacilityRecord = function (res) {
            let isValid = true;
            // Compare locations
            if (ctrl.lgdcodeArr.length > 0) {
                ctrl.lgdcodeArr.forEach(e => {
                    if (e.startsWith('S/')) {
                        if (res.stateLGDCode != Number(e.split('/')[1]).toString()) {
                            isValid = false;
                        }
                    } else if (e.startsWith('D/')) {
                        if (res.districtLGDCode != Number(e.split('/')[1]).toString()) {
                            isValid = false;
                        }
                    } else if (e.startsWith('B/')) {
                        if (res.subDistrictLGDCode != Number(e.split('/')[1]).toString()) {
                            isValid = false;
                        }
                    }
                });
            } else {
                isValid = false;
            }

            if(isValid){
                ctrl.saveAndLinkHFRId();
            } else {
                toaster.pop('error', 'The given HFR Facility ID details don\'t match with this facility details.');
            }
        }


        ctrl.saveAndLinkHFRId = function () {
            Mask.show();
            HealthInfraService.saveAndLinkHFRId(ctrl.response.id, ctrl.facilityId)
                .then((res) => {
                    if (res.hfrFacilityId) {
                        //facilityid already linked with other user
                        ctrl.isAlreadyLinked = true;
                        ctrl.infrastructure = res;

                    } else {
                        toaster.pop('success', 'Facility linked succesfully')
                        ctrl.cancel();
                    }

                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(Mask.hide);
        }

        ctrl.navigateToHFR = function () {
            ctrl.reset();
            Mask.show();
            HprCreateDAO.CheckHpIDRegisterAndLogin()
                .then((res) => {
                    ctrl.hpIdResponse = res;
                    if (res.isRegister && res.isLogin) {
                        ctrl.getProfile();
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(Mask.hide);
        }

        ctrl.getProfile = function () {
            Mask.show();
            HprProfileDAO.getProfile(false)
                .then((res) => {
                    //session not expired, so navigating to hfr registration screen
                    ctrl.cancel();
                    $state.go('techo.manage.basicHfrId', { id: ctrl.response.id });

                }).catch((error) => {
                    //session has expired, so navigating to login screen
                    if (error.data.errorcode == 401) {
                        toaster.pop('warning', "Please login to link facility with HFR");
                        ctrl.cancel();
                        $state.go('techo.manage.loginHpId', { id: ctrl.response.id });

                    }
                }).finally(Mask.hide);
        }

        ctrl.navigateToCreateOrLogin = function () {
            ctrl.cancel();
            if (ctrl.selectedOption == 'create') {
                $state.go('techo.manage.createHpId', { id: ctrl.response.id });
            } else if (ctrl.selectedOption == 'login') {
                $state.go('techo.manage.loginHpId', { id: ctrl.response.id });
            }
        }

        ctrl.cancel = () => {
            if (menu == 1) {
                $state.go(manageState)
            }
            $uibModalInstance.close();
        };

        ctrl.reset = function () {
            ctrl.facilityId = null;
            ctrl.selectedOption = null;
            ctrl.isAlreadyLinked = false;
        }

    }
    angular.module('imtecho.controllers').controller('FacilityLinkingController', FacilityLinkingController);
})();