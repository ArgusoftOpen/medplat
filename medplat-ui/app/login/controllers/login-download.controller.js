(function (angular) {
    function LoginDownloadController(toaster, APP_CONFIG, GeneralUtil) {
        var login = this;
        login.apiPath = APP_CONFIG.apiPath;
        login.username = "superuser";
        login.password = "argusadmin";
        login.user = {};
        [login.imagesPath, login.logoImages] = GeneralUtil.getLogoImages();

        login.doLogin = function () {
            if (login.loginForm.$valid) {
                if (login.user.username === login.username && login.user.password === login.password) {
                    var file_path = login.apiPath + '/mobile/downloadmctsdb?username=' + login.user.username + '&password=' + login.user.password + '&fileName=fhs_pundhara_pg9point5.backup';
                    var a = document.createElement('A');
                    a.href = file_path;
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                } else {
                    toaster.pop('error', "Invalid username or password");
                }
            }
        };
    }
    angular.module('imtecho.controllers').controller('LoginDownloadController', LoginDownloadController);
})(window.angular);
