(function () {
    function FamilyInfoController(Mask, GeneralUtil, QueryDAO, $stateParams, toaster) {
        var ctrl = this;
        ctrl.familyObj = null;
        ctrl.paramDataFlag = false;

        ctrl.resetField = function () {
            ctrl.familyObj = null;
        }

        var init = function () {
            ctrl.toggleFilter();
            if ($stateParams.familyId != null) {
                ctrl.familyId = $stateParams.familyId;
                console.log(ctrl.familyId)
                ctrl.submit();
                ctrl.paramDataFlag = true;
            } else {
                ctrl.paramDataFlag = false;
            }
        };

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.submit = function () {
            ctrl.getFamilyData();
        };

        ctrl.enterSubmit = function () {
            ctrl.getFamilyData();
            ctrl.toggleFilter();
        };

        ctrl.getFamilyData = function () {
            Mask.show();
            ctrl.resetField();
            var queryDto = {
                code: 'retrieve_family_and_member_info',
                parameters: {
                    familyid: ctrl.familyId
                }
            }
            QueryDAO.executeQuery(queryDto).then(function (res) {
                if (res.result.length < 1) {
                    toaster.pop("error", "This familyId is not valid. Please enter valid familyId");
                    return;
                }
                ctrl.familyObj = res.result;
                ctrl.toggleFilter();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('FamilyInfoController', FamilyInfoController);
})();
