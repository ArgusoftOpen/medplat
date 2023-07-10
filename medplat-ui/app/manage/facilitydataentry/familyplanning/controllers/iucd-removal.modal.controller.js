(function (angular) {
    var IucdRemovalModal = function ($scope, $uibModalInstance, memberData, toaster, QueryDAO, Mask, GeneralUtil) {
        $scope.memberId = memberData.memberId;
        $scope.fpOperateDate = memberData.fpOperateDate;
        $scope.memberCurrentFpValue = memberData.memberCurrentFpValue;
        $scope.maxDate = moment();
        $scope.minDate = moment($scope.fpOperateDate);
        $scope.iucdObject = {};

        $scope.ok = function () {
            $scope.iucdRemovalForm.$setSubmitted();
            if ($scope.iucdRemovalForm.$valid) {
                var queryDto = {
                    code: 'update_iucd_removal',
                    parameters: {
                        memberId: $scope.memberId,
                        iucdRemovalDate: moment($scope.getDate($scope.iucdObject.iucdRemovalDate)).format('DD-MM-YYYY HH:mm:ss'),
                        iucdRemovalReason: $scope.iucdObject.iucdRemovalReason
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (response) {
                    Mask.hide();
                    toaster.pop('success', "Date added successfully");
                    $uibModalInstance.close({
                        iucdRemovalDate: $scope.iucdObject.iucdRemovalDate,
                        iucdRemovalReason: $scope.iucdObject.iucdRemovalReason
                    });
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                    $uibModalInstance.close();
                });
            }
        };

        $scope.getDate = (date) => {
            return new Date(
                date.getFullYear(),
                date.getMonth(),
                date.getDate(),
                00,
                00
            );
        }

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('IucdRemovalModal', IucdRemovalModal);
})(window.angular);
