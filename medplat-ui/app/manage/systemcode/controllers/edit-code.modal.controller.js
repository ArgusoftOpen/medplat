(function () {
    function EditCodeModalController($scope, listValueDetail, toaster, QueryDAO, SystemCodesService, $uibModalInstance, GeneralUtil, Mask ,$timeout) {

        $scope.init = function () {
            delete $scope.code;
            $scope.isValidCode = true;
            $scope.listValueDetail = listValueDetail;
            $scope.code = {};
            $scope.code.codeType = $scope.listValueDetail.codeTypes[0].codeType;
            $scope.code.tableId = $scope.listValueDetail.id;
            $scope.code.tableType = $scope.listValueDetail.tableType;
            $scope.onChangeCodeType($scope.code.codeType);
        };

        $scope.isDisplayed = function() {
            $scope.code.isDisplayed = !$scope.code.isDisplayed;
            // If need to clear
            // if (!$scope.code.isDisplayed){}
        };


        $scope.getCodeOptions = function () {
            Mask.show();
            SystemCodesService.getCodeByRefText($scope.code.searchedTextCode,null,null,$scope.code.codeType)
            .then((res) => {
                $scope.codeList = res;
                Mask.hide();
            })
            .catch((err) => {
                Mask.hide();
            });
        };

        $scope.verifyCode = function(){
            if ($scope.code.code && $scope.code.code.length >= 3) {
                if (!$scope.isFindingCodeDetails) {
                    $scope.isFindingCodeDetails = true;
                    Mask.show();
                    SystemCodesService.getCodeDetailsByCodeAndType($scope.code.code,$scope.code.codeType).then((res) => {
                            $scope.isValidCode = res.length > 0 ? true : false;
                            $scope.isFindingCodeDetails = false;
                            Mask.hide();
                        });
                    }
            }
            else{
                $scope.isValidCode = false;
            }

        }

        $scope.changeCodeOptions = function() {
            $scope.code.code = $scope.code.selectedCode.code;
            $scope.code.description = $scope.code.selectedCode.description || null;
            $scope.isValidCode = true;
        };

        $scope.close = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.onChangeCodeType = function (codeType) {

            // will hide need help section on change type
            $scope.code.isDisplayed = false;
            $scope.code.searchedTextCode = null;
            $scope.codeList = undefined;

            $timeout(function () {
                let tmp = $scope.listValueDetail.SYSTEM_CODE.find((val) => {
                    return val.codeType === codeType;
                });
                if (!tmp) {
                    tmp = {};
                    tmp.codeType = codeType;
                    tmp.tableId = $scope.listValueDetail.id;

                }
                tmp.tableType = $scope.listValueDetail.tableType;
                $scope.code = angular.copy(tmp);
            });

        };

        $scope.update = function () {
            // If we will verify code here then will call API two time as on-blur it is already doing
                if ($scope.code.codeType) {
                    Mask.show();
                    SystemCodesService.saveOrUpdate($scope.code).then(() => {
                        toaster.pop('success', 'code updated successfully !');
                        $uibModalInstance.close('updated');
                    }).finally(Mask.hide);
                }
        };

        $scope.init();

    }
    angular.module('imtecho.controllers').controller('EditCodeModalController', EditCodeModalController);
})();
