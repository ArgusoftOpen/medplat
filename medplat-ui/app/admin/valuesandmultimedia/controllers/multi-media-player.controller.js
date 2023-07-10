(function () {
    var MultimediaPlayerController = function ($uibModalInstance, object, $rootScope, APP_CONFIG) {
        var mmplayer = this;
        mmplayer.apiPath = APP_CONFIG.apiPath;
        mmplayer.accessToken = $rootScope.authToken;
        mmplayer.object = angular.copy(object)
        mmplayer.params = {
            id: '',
            password: ''
        };

        mmplayer.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('MultimediaPlayerController', MultimediaPlayerController);
})();
