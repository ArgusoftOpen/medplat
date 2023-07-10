(function (angular) {
    function InfoController(InfoService, Mask, toaster, GeneralUtil) {
        var ctrl = this;
        ctrl.appName = GeneralUtil.getAppName();
        ctrl.imagesPath = GeneralUtil.getImagesPath();
        ctrl.comprehensivePrimaryHealthCareSolutionPpt = `${ctrl.appName} Comprehensive Primary Health Care Solution.pptx`;
        ctrl.submitInfo = function () {
            if (!!ctrl.memberName && !!ctrl.email && !!ctrl.mobileNumber && !!ctrl.designation) {
                var queryDto = {
                    code: "insert_techo_interested_detail",
                    parameters: {
                        full_name: ctrl.memberName,
                        email: ctrl.email,
                        mobile_number: ctrl.mobileNumber,
                        designation: ctrl.designation
                    }
                }
                InfoService.submit(queryDto).then(function (res) {
                    toaster.pop('success', 'You have submitted record successfully');
                }).finally(function () {
                    ctrl.resetInfo();
                    Mask.hide();
                });
            } else {
                toaster.pop('error', 'Enter valid Information');
            }
        };

        ctrl.resetInfo = function () {
            ctrl.memberName = null;
            ctrl.email = null;
            ctrl.mobileNumber = null;
            ctrl.designation = null;
        }

        ctrl.checkMobileNumber = function (mobilenumber) {
            if (!(mobilenumber == null || mobilenumber == "" || mobilenumber.toString().length == 10)) {
                ctrl.checkPhoneNolength = true;
            } else {
                ctrl.checkPhoneNolength = false;
            }
        }

        ctrl.checkEmailAddress = function (email) {
            var reg = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
            if (!!email && !reg.test(email.trim())) {
                toaster.pop('error', 'Please enter valid alternative email address: ' + cccEmail);
                ctrl.emailError = true;
                return false;
            }
        }
    }
    angular.module('imtecho.controllers').controller('InfoController', InfoController);
})(window.angular);
