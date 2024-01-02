(function (angular) {
    function LoginController(AuthenticateService, $rootScope, $state, States, Mask, GeneralUtil) {
        var login = this;
        login.user = {};
        login.as = { user: {} };
        login.showSystemNotice = false;
        login.hideAppLink = false;
        login.env =  GeneralUtil.getEnv();
        
        login.init = () => {
            [login.imagesPath, login.logoImages] = GeneralUtil.getLogoImages();
            Mask.show();
            AuthenticateService.getSystemNotice().then((response) => {
                if (response.data != null && response.data.keyValue != null) {
                    login.showSystemNotice = true;
                    login.systemNotice = response.data.keyValue;
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
            AuthenticateService.getApkInfo().then((response) => {
                if(response.data.length === 0){
                    login.hideAppLink = true;
                }
                if(response.data.length > 0){
                    login.link = response.data[0];
                }
                if(response.data.length > 1){
                    login.version = response.data[1];
                }
                if(response.data.length > 2){
                    login.release_date = response.data[2];
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        //reset flag to retrieve baskets also reset all scheduled timers
        $rootScope.timeoutScheduled = false;
        var doLoginLocal = function (user) {
            Mask.show();
            AuthenticateService.getKeyAndIV().then(function (res) {
                AuthenticateService.login(user.username, user.password, function (data) {
                    AuthenticateService.getLoggedInUser(true, user.username).then(function (res) {
                        Mask.hide();
                        login.userDetail = res.data;
                        if (!res.forcePasswordReset) {
                            $rootScope.forcePasswordReset = false;
                            if (!!login.userDetail.features && login.userDetail.features.dashboard && login.userDetail.features.dashboard.length > 0) {
                                //as navigation state consist of the params too we need to split them and use them
                                //to navigate through state.go instead of ui-sref
                                res = login.userDetail.features.dashboard[0].navigationState.split("{");
                                if (res[1]) {
                                    res[1].length = res[1].length - 1;
                                    res[0].length = res[0].length - 1;
                                    var params = res[1].substring(0, res[1].length - 2);
                                    params = JSON.parse('{' + params + '}');
                                    var state = res[0].substring(0, res[0].length - 1);
                                    $state.go(state, params);
                                } else {
                                    $state.go(login.userDetail.features.dashboard[0].navigationState);
                                }
                            }
                            //current default state if feature doesnt match with constant states
                            else {
                                $state.go('techo.dashboard.webtasks');
                            }
                        } else {
                            $rootScope.forcePasswordReset = true;
                            $state.go("resetpassword");
                        }
                    });
                }, function (error) {
                    Mask.hide();
                    var data = error.data;
                    user.hasAuthError = true;
                    if (data.message === "Bad credentials") {
                        user.errorMessage = "Password Incorrect";
                    } else {
                        user.errorMessage = data.message;
                    }
                });
            }, function (error) {
                Mask.hide();
                var data = error.data;
                user.hasAuthError = true;
                if (data.message === "Bad credentials" || data.message === "Invalid username") {
                    user.errorMessage = "Incorrect username or password";
                } else {
                    user.errorMessage = data.message;
                }
            });
            
        };

        login.doLogin = function () {
            login.user.hasAuthError = false;
            if (login.loginForm.$valid) {
                doLoginLocal(login.user);
            }
        };

        login.init();
    }
    angular.module('imtecho.controllers').controller('LoginController', LoginController);
})(window.angular);
