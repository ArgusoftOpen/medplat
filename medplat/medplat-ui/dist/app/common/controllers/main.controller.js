(function () {
    var mainController = function ($scope, $rootScope, APP_CONFIG, ENV) {
        $scope.setAppName = () => {
            switch (ENV.implementation) {
                case 'sewa_rural':
                    $rootScope.appName = 'Sewa Rural';
                    break;
                case 'telangana':
                    $rootScope.appName = 'Amma-Kosam';
                    break;
                case 'uttarakhand':
                    $rootScope.appName = 'Chardham';
                    break;
                default:
                    $rootScope.appName = 'MEDplat';
                    break;
            }
        }
        $rootScope.toastOptions = {
            'time-out': 4000,
            'close-button': true,
            'body-output-type': 'trustedHtml'
        };
        $rootScope.familyIdFormat = APP_CONFIG.familyIdFormat;
        $scope.$on('invalid_grant', function () {
            $rootScope.logOut();
        });
        $scope.$on('invalid_auth', function () {
            $rootScope.logOut();
        });
        $scope.setAppName();
    };
    angular.module('imtecho.controllers').controller('MainController', mainController);
})();
