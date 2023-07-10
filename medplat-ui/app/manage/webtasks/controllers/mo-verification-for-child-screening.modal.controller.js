(function (angular) {
    var MoVerificationForChildScreening = function ($scope, $uibModalInstance, message, childId, childName, areaName, villageName, fhwName, fhwContactNumber, motherName, motherContact, ChildScreeningService, AuthenticateService, toaster, QueryDAO) {
        $scope.message = message;
        $scope.showApetiteTest = true;
        $scope.ok = function () {
            $scope.moVerificationForChildScreeningForm.$setSubmitted();
            if ($scope.moVerificationForChildScreeningForm.$valid) {
                if ($scope.moVerificationForChildScreening.childScreeningObject.midUpperArmCircumference > 11.5
                    && $scope.moVerificationForChildScreening.childScreeningObject.sdScore !== 'SD3' && $scope.moVerificationForChildScreening.childScreeningObject.sdScore !== 'SD4'
                    && $scope.moVerificationForChildScreening.childScreeningObject.bilateralPittingOedema === 'NOTPRESENT') {
                    toaster.pop('error', 'Child is not SAM. Entry will be removed');
                    $uibModalInstance.close();
                } else {
                    if ($scope.moVerificationForChildScreening.screeningCenter == null) {
                        toaster.pop('error', 'Child is not SAM. Entry will be removed');
                        $uibModalInstance.close();
                    } else {
                        ChildScreeningService.createChildScreening($scope.moVerificationForChildScreening).then(function () {
                            toaster.pop("success", "Details saved successfully");
                            ChildScreeningService.createMoVerification($scope.moVerificationForChildScreening.childScreeningObject).then(function () {
                                $uibModalInstance.close();
                            });
                        }, function (error) {
                            if (error.data.errorcode === 101) {
                                toaster.pop("error", "Child is already added");
                            }
                            $uibModalInstance.close();
                        })
                    }
                }
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.calculateSdScore = function () {
            if ($scope.moVerificationForChildScreening.gender != null) {
                if ($scope.moVerificationForChildScreening.childScreeningObject.height != null && $scope.moVerificationForChildScreening.childScreeningObject.weight != null) {
                    if (Number.isInteger($scope.moVerificationForChildScreening.childScreeningObject.height)) {
                        ChildScreeningService.retrieveSdScore($scope.moVerificationForChildScreening.gender, $scope.moVerificationForChildScreening.childScreeningObject.height, $scope.moVerificationForChildScreening.childScreeningObject.weight).then(function (response) {
                            if (response.sdScore == 'SD4') {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "SD4";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "Less than -4";
                            } else if (response.sdScore == 'SD3') {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "SD3";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "-4 to -3";
                            } else if (response.sdScore == 'SD2') {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "SD2";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "-3 to -2";
                            } else if (response.sdScore == 'SD1') {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "SD1";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "-2 to -1";
                            } else if (response.sdScore == 'MEDIAN') {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "MEDIAN";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "MEDIAN";
                            } else {
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScore = "NONE";
                                $scope.moVerificationForChildScreening.childScreeningObject.sdScoreDisplay = "NONE";
                            }
                        })
                        $scope.validateSamChild();
                    } else {
                        toaster.pop('error', 'Height not allowed in decimal value');
                        $scope.moVerificationForChildScreening.childScreeningObject.height = null;
                    }
                }
            } else {
                toaster.pop('error', 'Gender Not Found');
            }
        }

        $scope.init = function () {
            $scope.moVerificationForChildScreening = {};
            $scope.moVerificationForChildScreening.childId = childId
            $scope.moVerificationForChildScreening.childName = childName
            $scope.moVerificationForChildScreening.areaName = areaName
            $scope.moVerificationForChildScreening.villageName = villageName
            $scope.moVerificationForChildScreening.fhwName = fhwName
            $scope.moVerificationForChildScreening.fhwContactNumber = fhwContactNumber
            $scope.moVerificationForChildScreening.motherName = motherName;
            $scope.moVerificationForChildScreening.motherContact = motherContact;
            $scope.moVerificationForChildScreening.screenedOn = new Date();
            $scope.moVerificationForChildScreening.state = 'ACTIVE';
            $scope.moVerificationForChildScreening.childScreeningObject = {};
            $scope.moVerificationForChildScreening.childScreeningObject.childId = $scope.moVerificationForChildScreening.childId;
            AuthenticateService.getLoggedInUser().then(function (user) {
                $scope.loggedInUserId = user.data.id;
                QueryDAO.executeQuery({
                    code: 'retrieve_cmtc_nrc_centers_for_rbsk',
                    parameters: {
                        userId: $scope.loggedInUserId
                    }
                }).then(function (res) {
                    $scope.screeningCenters = res.result;
                })
            });
            // AnganwadiService.searchMembers($scope.moVerificationForChildScreening.childId).then(function (response) {
            //     if (response.areadId != null) {
            //         $scope.moVerificationForChildScreening.locationId = response.areaId;
            //     } else {
            //         $scope.moVerificationForChildScreening.locationId = response.locationId;
            //     }
            //     $scope.moVerificationForChildScreening.gender = response.gender;
            // });
            ChildScreeningService.retrieveRchChildServiceDetailsByMemberId($scope.moVerificationForChildScreening.childId).then(function (response) {
                $scope.rchChildServicedetails = response;
                if ($scope.rchChildServicedetails.sdScore == 'SD4') {
                    $scope.rchChildServicedetails.sdScore = "Less than -4";
                } else if ($scope.rchChildServicedetails.sdScore == 'SD3') {
                    $scope.rchChildServicedetails.sdScore = "-4 to -3";
                } else if ($scope.rchChildServicedetails.sdScore == 'SD2') {
                    $scope.rchChildServicedetails.sdScore = "-3 to -2";
                } else if ($scope.rchChildServicedetails.sdScore == 'SD1') {
                    $scope.rchChildServicedetails.sdScore = "-2 to -1";
                } else if ($scope.rchChildServicedetails.sdScore == 'MEDIAN') {
                    $scope.rchChildServicedetails.sdScore = "MEDIAN";
                } else {
                    $scope.rchChildServicedetails.sdScore = "NONE";
                }
                ChildScreeningService.retrieveMedicalComplications($scope.moVerificationForChildScreening.childId).then(function (response1) {
                    $scope.rchChildServicedetails.medicalComplications = response1.medicalComplications;
                });
            });
        }

        $scope.validateSamChild = function () {
            if ($scope.moVerificationForChildScreening.childScreeningObject.midUpperArmCircumference != null
                && $scope.moVerificationForChildScreening.childScreeningObject.midUpperArmCircumference > 11.5
                && $scope.moVerificationForChildScreening.childScreeningObject.sdScore != null
                && $scope.moVerificationForChildScreening.childScreeningObject.sdScore !== 'SD3' && $scope.moVerificationForChildScreening.childScreeningObject.sdScore !== 'SD4'
                && $scope.moVerificationForChildScreening.childScreeningObject.bilateralPittingOedema != null
                && $scope.moVerificationForChildScreening.childScreeningObject.bilateralPittingOedema === 'NOTPRESENT') {
                $scope.showApetiteTest = false;
                $scope.moVerificationForChildScreening.apetiteTest = null;
            } else {
                $scope.showApetiteTest = true;
                $scope.moVerificationForChildScreening.apetiteTest = null;
            }
        }

        $scope.init();
    };
    angular.module('imtecho.controllers').controller('MoVerificationForChildScreening', MoVerificationForChildScreening);
})(window.angular);
