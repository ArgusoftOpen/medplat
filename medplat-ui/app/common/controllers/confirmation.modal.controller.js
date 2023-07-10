(function (angular) {
    var confirmModalController = function ($scope, $uibModalInstance, message) {
        if (angular.isString(message)) {
            $scope.message = message;
        } else if (angular.isObject(message)) {
            $scope.message = message.body;
            $scope.title = message.title;
        }

        $scope.ok = function () {
            $uibModalInstance.close();
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('ConfirmModalController', confirmModalController);
})(window.angular);
