(function (angular) {
    var MarkAsLfuController = function ($scope, $uibModalInstance, Mask, QueryDAO, migration, GeneralUtil, toaster) {

        $scope.ok = function (outOfState) {
            Mask.show();
            QueryDAO.execute({
                code: 'mark_as_lfu',
                parameters: {
                    userid: migration.user.id,
                    id: Number(migration.id),
                    outOfState: outOfState || null
                }
            }).then(function () {
                toaster.pop('success', 'Member marked as LFU');
                $uibModalInstance.close();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('MarkAsLfuController', MarkAsLfuController);
})(window.angular);
