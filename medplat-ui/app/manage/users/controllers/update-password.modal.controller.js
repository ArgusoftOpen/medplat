(function () {
    var ResetPasswordModalController = function ($uibModalInstance, user, UserDAO, toaster, GeneralUtil, QueryDAO, Mask) {
        var resetPassword = this;
        resetPassword.changePass = user.changePassword;
        resetPassword.params = {
            id: '',
            password: ''
        };
        resetPassword.user = user;
        resetPassword.params.id = user.id;
        resetPassword.checkPassword = () => {
            if(resetPassword.password && resetPassword.password.length <8){
                resetPassword.passwordForm.password.$setValidity('minlength', false);
            } else {
                resetPassword.passwordForm.password.$setValidity('minlength', true);
            }
            GeneralUtil.checkPassword('password', resetPassword.password, resetPassword.user.userName);  
        }

        resetPassword.save = function (oldPassword, newPassword) {
            var passwordMatched;
            //            if (resetPassword.passwordForm.oldpassword !== null) {
            //                resetPassword.verifyOldPassword();
            //            }
            passwordMatched = resetPassword.password === resetPassword.cpassword;
            if (resetPassword.passwordForm.$valid && passwordMatched) {
                if (resetPassword.changePass) {
                    UserDAO.changePasswordOldtoNew(oldPassword, newPassword).then(function () {
                        toaster.pop("success", "Password Changed Successfully");
                        $uibModalInstance.close();
                    }, GeneralUtil.showMessageOnApiCallFailure);
                } else {
                    if (!user.editProfile) {
                        resetPassword.params.adminFlag = true;
                    }
                    Mask.show();
                    UserDAO.changePassword(resetPassword.password, user.id).then(function () {
                        toaster.pop("success", "Password Changed Successfully");
                        if (resetPassword.user.isFirstTimePasswordChangeModal) {
                            QueryDAO.execute({
                                code: 'update_first_login_user_personal_details',
                                parameters: {
                                    userId: resetPassword.user.id
                                }
                            }).then((response) => {
                            }).catch((error) => {
                                GeneralUtil.showMessageOnApiCallFailure(error);
                            }).finally(() => {
                                Mask.hide();
                            });
                        }
                        $uibModalInstance.close();
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });

                }
            }
        };

        resetPassword.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        //        resetPassword.verifyOldPassword = function () {
        //            if (resetPassword.oldpassword !== '' && typeof resetPassword.oldpassword !== 'undefined') {
        //                UserDAO.verifyPassword(resetPassword.oldpassword, user.id).then(function (res) {
        //                    if (res.result == "true") {
        //
        //                        resetPassword.passwordForm.oldpassword.$setValidity('verified', true);
        //                    } else if (res.result == "false") {
        //                        resetPassword.passwordForm.oldpassword.$setValidity('verified', false);
        //                    }
        //                }, function () {
        //
        //                });
        //            } else {
        //                resetPassword.passwordForm.oldpassword.$setValidity('verified', true);
        //
        //            }
        //        };
    };
    angular.module('imtecho.controllers').controller('ResetPasswordModalController', ResetPasswordModalController);
})();
