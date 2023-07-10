(function () {
    function ForgetPasswordController(UserDAO, toaster, $state, $timeout, GeneralUtil) {
        var forgetpassword = this;
        forgetpassword.generateOtpFlag = true;
        forgetpassword.verifyOtpFlag = false;
        forgetpassword.changePasswordFlag = false;
        forgetpassword.errorFlag = false;
        forgetpassword.noOfAttempts = 0;
        forgetpassword.incorrectOtp =false;
        [forgetpassword.imagesPath, forgetpassword.logoImages] = GeneralUtil.getLogoImages();

        forgetpassword.generateOTP = function (username) {
            forgetpassword.hasAuthError = false;
            forgetpassword.regenerateOtpButton = false;
            $timeout(function () {
                forgetpassword.regenerateOtpButton = true;
            }, 30000);
            if (username !== undefined) {
                UserDAO.validateAndGenerateOtp(username).then(function (res) {
                    forgetpassword.generateOtpFlag = false;
                    forgetpassword.verifyOtpFlag = true;
                    forgetpassword.validate = res;
                }, function (error) {
                    forgetpassword.hasAuthError = true;
                    forgetpassword.errorMessage = error.data.message;
                });
            }
        };

        forgetpassword.verifyOtp = function (username, otp) {
            if (forgetpassword.noOfAttempts <= 3) {
                if (otp !== undefined) {
                    UserDAO.verifyOtp(username, otp, forgetpassword.noOfAttempts).then(function (res) {
                        forgetpassword.validate = res;
                        forgetpassword.verifyOtpFlag = false;
                        forgetpassword.changePasswordFlag = true;
                        forgetpassword.errorFlag = false;
                        forgetpassword.hasAuthError = false;
                    }, function (error) {
                        var data = error.data;
                        forgetpassword.hasAuthError = true;
                        forgetpassword.errorMessage = data.message;
                        forgetpassword.noOfAttempts++;
                        if(forgetpassword.noOfAttempts > 3){
                            forgetpassword.incorrectOtp = true;
                        }
                    });
                } else {
                    forgetpassword.errorFlag = true;
                }
            }
        };

        forgetpassword.resetPassword = function (otp, password, confirmPassword, username) {
            if (password !== undefined) {
                if (password == confirmPassword) {
                    UserDAO.resetPassword(username, otp, password).then(function (res) {
                        toaster.pop('success', 'Password Reset Successfull!');
                        $state.go('login');
                    }, function (error) {
                        toaster.pop('warning', 'Oops! Something Went Wrong! Please Retry.');
                    });
                }
            } else {
                forgetpassword.errorFlag = true;
            }
        };

        forgetpassword.checkPassword = () => {
            if(forgetpassword.newPassword && forgetpassword.newPassword.length <8){
                forgetpassword.forgetpassForm.newPassword.$setValidity('minlength', false);
            } else {
                forgetpassword.forgetpassForm.newPassword.$setValidity('minlength', true);
            }
            GeneralUtil.checkPassword('newPassword', forgetpassword.newPassword, forgetpassword.username);  
        }

    }
    angular.module('imtecho.controllers').controller('ForgetPasswordController', ForgetPasswordController);
})();
