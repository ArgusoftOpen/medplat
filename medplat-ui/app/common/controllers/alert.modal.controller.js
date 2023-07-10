(function (angular) {
    var alertModalController = function ($scope, $uibModalInstance, message) {
        if (angular.isString(message)) {
            $scope.message = message;
        } else if (angular.isObject(message)) {
            $scope.message = message.body;
            $scope.title = message.title;
        }

        $scope.ok = function () {
            $uibModalInstance.close();
        };
    };
    angular.module('imtecho.controllers').controller('alertModalController', alertModalController);
})(window.angular);
