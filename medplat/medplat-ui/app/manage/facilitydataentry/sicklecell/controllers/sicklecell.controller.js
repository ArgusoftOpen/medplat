(function () {
    function SicklecellController($stateParams, Mask, QueryDAO, toaster, $state, AuthenticateService, GeneralUtil, SicklecellConstants, HplcTestValues, SicklecellService) {
        var sicklecell = this;
        sicklecell.constants = SicklecellConstants;
        sicklecell.hplcTestValues = HplcTestValues;

        sicklecell.init = () => {
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                sicklecell.loggedInUser = user.data;
                return QueryDAO.execute({
                    code: 'sickle_cell_search_by_member_id',
                    parameters: {
                        uniqueHealthId: $stateParams.id,
                        limit: 1,
                        offSet: 0
                    }
                });
            }).then((response) => {
                if (response.result.length > 0) {
                    sicklecell.sickleObject = response.result[0];
                } else {
                    return Promise.reject();
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go("techo.manage.sicklecellSearch");
            }).finally(() => {
                Mask.hide();
            });
        };

        sicklecell.saveScreening = () => {
            sicklecell.sicklecellForm.$setSubmitted();
            if (sicklecell.sicklecellForm.$valid) {
                Mask.show();
                SicklecellService.create(sicklecell.sickleObject).then(() => {
                    toaster.pop("success", "Form submitted successfully");
                    $state.go("techo.manage.sicklecellSearch");
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            }
        }

        sicklecell.anemiaTestDoneChanged = () => {
            sicklecell.resetDttTestResult();
            sicklecell.resetHplcTestDone();
            sicklecell.resetHplcTestResult();
        }

        sicklecell.dttTestResultChanged = () => {
            sicklecell.resetHplcTestDone();
            sicklecell.resetHplcTestResult();
        }

        sicklecell.hplcTestDoneChanged = () => {
            sicklecell.resetHplcTestResult();
        }

        sicklecell.resetDttTestResult = () => sicklecell.sickleObject.dttTestResult = null;

        sicklecell.resetHplcTestDone = () => sicklecell.sickleObject.hplcTestDone = null;

        sicklecell.resetHplcTestResult = () => sicklecell.sickleObject.hplcTestResult = null;

        sicklecell.goBack = () => $state.go("techo.manage.sicklecellSearch");

        sicklecell.init();
    }
    angular.module('imtecho.controllers').controller('SicklecellController', SicklecellController);
})();
