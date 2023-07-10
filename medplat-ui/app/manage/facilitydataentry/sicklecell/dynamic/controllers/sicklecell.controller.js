(function () {
    function SicklecellController(Mask, QueryDAO, toaster, $state, AuthenticateService, GeneralUtil, SicklecellService, $q) {
        var ctrl = this;
        const FEATURE = 'techo.manage.sicklecellSearch';
        const SICKLE_CELL_FORM_CONFIGURATION_KEY = 'FACILITY_SICKLE_CELL';
        ctrl.formData = {};

        ctrl.init = () => {
            Mask.show();
            QueryDAO.execute({
                code: 'sickle_cell_search_by_member_id',
                parameters: {
                    uniqueHealthId: $state.params.id,
                    limit: 1,
                    offSet: 0
                }
            }).then((response) => {
                if (response.result.length > 0) {
                    ctrl.formData = response.result[0];
                    ctrl.formData.age = ctrl.formData.age.replace("mons", "months");
                } else {
                    return Promise.reject({ data: { message: 'Record not found' } });
                }
                let promiseList = [];
                promiseList.push(AuthenticateService.getLoggedInUser());
                promiseList.push(AuthenticateService.getAssignedFeature(FEATURE));
                return $q.all(promiseList);
            }).then((response) => {
                ctrl.loggedInUser = response[0].data;
                ctrl.formConfigurations = response[1].systemConstraintConfigs[SICKLE_CELL_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response[1].webTemplateConfigs[SICKLE_CELL_FORM_CONFIGURATION_KEY];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go("techo.manage.sicklecellSearch");
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.saveScreening = () => {
            ctrl.sicklecellForm.$setSubmitted();
            if (ctrl.sicklecellForm.$valid) {
                Mask.show();
                SicklecellService.create(ctrl.formData).then(() => {
                    toaster.pop("success", "Form submitted successfully");
                    $state.go("techo.manage.sicklecellSearch");
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            }
        }

        ctrl.anemiaTestDoneChanged = () => {
            ctrl.formData.dttTestResult = null;
            ctrl.formData.hplcTestDone = null;
            ctrl.formData.hplcTestResult = null;
        }

        ctrl.dttTestResultChanged = () => {
            ctrl.formData.hplcTestDone = null;
            ctrl.formData.hplcTestResult = null;
        }

        ctrl.hplcTestDoneChanged = () => {
            ctrl.formData.hplcTestResult = null;
        }

        ctrl.goBack = () => $state.go("techo.manage.sicklecellSearch");

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('SicklecellController', SicklecellController);
})();
