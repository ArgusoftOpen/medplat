(function (angular) {
    var MaternalDeathVerificationModalMo = function ($scope, $uibModalInstance, message, task, QueryDAO) {
        $scope.ok = function () {
            $scope.maternalDeathVerificationMoForm.$setSubmitted();
            if ($scope.maternalDeathVerificationMoForm.$valid) {
                QueryDAO.executeQuery({
                    code: 'maternal_death_verification_mo_verbal_autospy_updation',
                    parameters: {
                        taskId: $scope.maternalDeathVerificationMo.task.taskId,
                        verbalAutopsy: $scope.maternalDeathVerificationMo.verbalAutopsy
                    }
                }).then(function (res) {
                    $scope.task.action = $scope.maternalDeathVerificationMo.actionKey;
                    $uibModalInstance.close($scope.task);
                })
            }
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init = function () {
            $scope.message = message;
            $scope.task = task;
            $scope.maternalDeathVerificationMo = {};
            $scope.maternalDeathVerificationMo.task = task
            QueryDAO.executeQuery({
                code: 'mo_death_verification_mo_actions',
                parameters: {}
            }).then(function (res) {
                $scope.actionsList = res.result;
            })
        }

        $scope.init();
    };
    angular.module('imtecho.controllers').controller('MaternalDeathVerificationModalMo', MaternalDeathVerificationModalMo);
})(window.angular);
