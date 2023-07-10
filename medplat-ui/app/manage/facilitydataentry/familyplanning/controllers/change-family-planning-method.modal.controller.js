(function (angular) {
    var ChangeFpMethodModal = function ($scope, $uibModalInstance, memberData, toaster, QueryDAO, Mask, GeneralUtil, AuthenticateService) {
        $scope.memberId = memberData.memberId;
        $scope.fpOperateDate = memberData.fpOperateDate;
        $scope.memberCurrentFpValue = memberData.memberCurrentFpValue;
        $scope.lastDeliveryDate = memberData.lastDeliveryDate;
        $scope.todayDate = moment();
        if ($scope.lastDeliveryDate != null) {
            $scope.minDate = moment($scope.lastDeliveryDate);
        } else {
            $scope.minDate = null;
        }
        $scope.familyPlanningObject = {};

        $scope.init = () => {
            Mask.show();
            AuthenticateService.getLoggedInUser().then((response) => {
                return QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: response.data.id
                    }
                });
            }).then((response) => {
                $scope.assignedHealthInfrastructures = response.result;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $scope.cancel();
            }).finally(() => {
                Mask.hide();
            })
        }

        $scope.ok = function () {
            $scope.familyPlanningMethodForm.$setSubmitted();
            if ($scope.familyPlanningMethodForm.$valid) {
                var queryDto = {
                    code: 'update_fp_method',
                    parameters: {
                        memberId: $scope.memberId,
                        familyPlanningMethod: $scope.familyPlanningObject.familyPlanningMethod,
                        fpInsertOperateDate: $scope.familyPlanningObject.fpInsertOperateDate && moment($scope.getDate($scope.familyPlanningObject.fpInsertOperateDate)).format('DD-MM-YYYY HH:mm:ss') || null,
                        healthInfrastructure: $scope.familyPlanningObject.healthInfrastructure != null ? $scope.familyPlanningObject.healthInfrastructure.id : null
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (response) {
                    Mask.hide();
                    toaster.pop('success', "Record Updated Successfully");
                    $uibModalInstance.close({
                        familyPlanningMethod: $scope.familyPlanningObject.familyPlanningMethod,
                        fpInsertOperateDate: $scope.familyPlanningObject.fpInsertOperateDate || null,
                        healthInfrastructure: $scope.familyPlanningObject.healthInfrastructure != null ? $scope.familyPlanningObject.healthInfrastructure.id : null
                    });
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                    $uibModalInstance.close();
                });
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.familyPlanningMethodChanged = function () {
            $scope.familyPlanningObject.fpInsertOperateDate = null;
        }

        $scope.getDate = (date) => {
            return new Date(
                date.getFullYear(),
                date.getMonth(),
                date.getDate(),
                00,
                00
            );
        }

        $scope.init()
    };
    angular.module('imtecho.controllers').controller('ChangeFpMethodModal', ChangeFpMethodModal);
})(window.angular);
