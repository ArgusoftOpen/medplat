(function () {
    function EditCodeModalController($scope, listValueDetail, toaster, QueryDAO, SystemCodesService, $uibModalInstance, GeneralUtil, Mask ,$timeout) {

        $scope.init = function () {
            delete $scope.code;
            $scope.isValidCode = true;
            $scope.isValidCodeDesc = true;
            $scope.listValueDetail = listValueDetail;
            $scope.code = {};
            $scope.code.tableId = $scope.listValueDetail.id;
            $scope.code.codeType = $scope.listValueDetail.codeTypes[0];
            $scope.code.tableType = $scope.listValueDetail.tableType;
            $scope.onChangeCodeType($scope.code.codeType);
        };

        $scope.isDisplayed = function(data) {
            
            $scope.code.isDisplayed = false;
            var url = '';
            var messge = '';
            if(data === 'SNOMED_CT'){
                url = 'https://mlds.ihtsdotools.org';
            } else if(data=== 'ICD_10'){
                url = 'https://icd.who.int/browse10/2019/en';
            } else if(data=== 'ICD_11'){
                url = 'https://icd.who.int/browse11/l-m/en';
            } else if(data=== 'LOINC'){
                url = 'https://loinc.org/search/';
            }
            
            if(url === ''){
                message = 'Search URL not configured';
            }else{
                messge = 'You are about to navigate to the new tab ' + url+" . Do you want to continue?";
            }
            
            if (confirm(messge)) {
                window.open(url);
            } else {
                // OK;
            }
            
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

        $scope.verifyCodeDesc = function () {
            delete $scope.descErrorMsg;
            if (!!$scope.code.description && $scope.code.description.length >= 255) {
                $scope.descErrorMsg = 'Description length can be a maximum of 255';
                $scope.isValidCodeDesc = false;
            } else {
                $scope.isValidCodeDesc = true;
            }

        };
        
        $scope.verifyCode = function () {
            delete $scope.codeErrorMsg;
            if ($scope.code.code) {
                if ($scope.code.code.length >= 4 && $scope.code.code.length <= 30) {
                    $scope.isValidCode = true;
                } else {
                    if ($scope.code.code.length < 4) {
                        $scope.codeErrorMsg = 'Code length can be a minimum of 4';
                    }

                    if ($scope.code.code.length > 30) {
                        $scope.codeErrorMsg = 'Code length can be a maximum of 30';
                    }
                    $scope.isValidCode = false;
                }
                
                
//                if (!$scope.isFindingCodeDetails) {
//                    $scope.isFindingCodeDetails = true;
//                    Mask.show();
//                    SystemCodesService.getCodeDetailsByCodeAndType($scope.code.code,$scope.code.codeType).then((res) => {
//                            $scope.isValidCode = res.length > 0 ? true : false;
//                            $scope.isFindingCodeDetails = false;
//                            Mask.hide();
//                        });
//                    }
            }
            else{
               
                $scope.codeErrorMsg = 'Code can not be empty';
                 $scope.isValidCode = false;
                    
            }
        }

        $scope.changeCodeOptions = function() {
            $scope.code.code = $scope.code.selectedCode.code;
            $scope.code.description = $scope.code.selectedCode.description || null;
            $scope.isValidCode = true;
            delete $scope.codeErrorMsg;
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

        $scope.update = function (editCodeForm) {
            $scope.verifyCode();
            $scope.verifyCodeDesc();
            // If we will verify code here then will call API two time as on-blur it is already doing
            if (editCodeForm.$valid) {
                if ($scope.code.codeType && $scope.isValidCode && $scope.isValidCodeDesc) {
                    Mask.show();
                    SystemCodesService.saveOrUpdate($scope.code).then(() => {
                        toaster.pop('success', 'code updated successfully !');
                        $uibModalInstance.close('updated');
                    }).finally(Mask.hide);
                }
            }
        };

        $scope.init();

    }
    angular.module('imtecho.controllers').controller('EditCodeModalController', EditCodeModalController);
})();
