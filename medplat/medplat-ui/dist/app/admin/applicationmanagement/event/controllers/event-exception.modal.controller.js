(function () {
    function EventExceptionModalController($scope, eventConfigId, QueryDAO, $uibModalInstance, GeneralUtil, Mask) {

        $scope.close = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init = function () {
            $scope.eventExceptionList = {};
            Mask.show();
            QueryDAO.execute({
                code: 'event_config_retrieve_exceptions_by_config_id',
                parameters: {
                    configId: Number(eventConfigId)
                }
            }).then(function (res) {
                if(res.result.length > 0){
                    $scope.eventException = res.result[0];                    
                }
                else{
                    $scope.eventException = null;
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        $scope.init();

    }
    angular.module('imtecho.controllers').controller('EventExceptionModalController', EventExceptionModalController);
})();
