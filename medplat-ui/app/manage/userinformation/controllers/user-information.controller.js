(function () {
    function UserInfoController(Mask, GeneralUtil, QueryDAO, toaster) {
        var ctrl = this;
        ctrl.userName = null;
        ctrl.userObj = null;

        ctrl.resetField = function () {
            ctrl.userObj = null;
        }

        var init = function () {
            if (ctrl.userName == null) {
                ctrl.toggleFilter();
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
            ctrl.getUserData();
            ctrl.toggleFilter();
        };

        ctrl.enterSubmit = function () {
            ctrl.getUserData();
        };

        ctrl.getUserData = function () {
            Mask.show();
            ctrl.resetField();
            var dtoList = [];

            var queryDto1 = {
                code: 'retrieve_all_information_of_user',
                parameters: {
                    username: ctrl.userName
                },
                sequence: 1
            };
            dtoList.push(queryDto1);

            var queryDto2 = {
                code: 'retrieve_user_login_details',
                parameters: {
                    username: ctrl.userName
                },
                sequence: 2
            };
            dtoList.push(queryDto2);

            QueryDAO.executeAll(dtoList).then(function (res) {
                if (res[0].result.length < 1) {
                    toaster.pop("error", "This userName is not valid. Please enter valid userName");
                    ctrl.toggleFilter();
                    return;
                }
                ctrl.userObj = res[0].result;
                ctrl.userLoginDetail = res[1].result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('UserInfoController', UserInfoController);
})();
