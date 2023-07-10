(function (angular) {
    var HelpDeskEdit = function ($scope, $uibModalInstance, data, save, QueryDAO, Mask, GeneralUtil, toaster,
        AuthenticateService, APP_CONFIG, TimelineConfigDAO, $uibModal, WrongActionType) {
        $scope.data = data;
        $scope.save = save;
        var editwpdcontroller = this;
        $scope.init = function () {
            if (data.isDeliveryUpdateLocation) {
                editwpdcontroller.retrieveData();
                editwpdcontroller.retrieveHealthInfraForUser();
            }
            if (data.isWrongMarkAction) {
                $scope.wrongActionType = WrongActionType;
                editwpdcontroller.retrieveWrongMarkReasons();
            }
        }

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.saveData = function () {
            $scope.save($scope.data.id, $scope.data.date, $uibModalInstance);
        }

        $scope.updateGender = function () {
            $scope.data.genderChange.$setSubmitted();
            if ($scope.data.genderChange.$valid) {
                Mask.show();
                QueryDAO.execute({
                    code: 'helpdesk_update_member_gender',
                    parameters: {
                        gender: $scope.data.gender,
                        memberId: $scope.data.id
                    }
                }).then(() => {
                    toaster.pop("success", "Gender changed successfully");
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    $uibModalInstance.close($scope.data.gender);
                    Mask.hide();
                });
            }
        }
// Upload file function starts
        $scope.uploadFile = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: true,
            chunkSize: 10 * 1024 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + AuthenticateService.getToken()
            },
            uploadMethod: 'POST'
        };

        $scope.upload = function ($file, $event, $flow) {
            $scope.responseMessage = {};
            $scope.isError = false;
            $scope.allowedExts = ['pdf', 'doc', 'jpg', 'png', 'jpeg'];

            if ($scope.allowedExts.indexOf($file.getExtension()) < 0) {
                toaster.pop('danger', $file.getExtension() + ' format is not supported. Please upload a file'
                    + ' having extensions ' + $scope.allowedExts.toString());
                $scope.isError = true;
            }
            if ($scope.isError) {
                $flow.cancel();
                return false;
            }
            $flow.opts.target = APP_CONFIG.apiPath + '/document/uploaddocument/TECHO/false';
        };

        $scope.uploadFn = function ($files, $event, $flow) {
            if (!$scope.isError) {
                Mask.show();
                AuthenticateService.refreshAccessToken().then(function () {
                    $flow.opts.headers.Authorization = 'Bearer ' + AuthenticateService.getToken();
                    $scope.flow = ($flow);
                    $flow.upload();
                    $scope.isUploading = true;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        $scope.getUploadResponse = function ($file, $message, $flow) {
            $scope.isUploading = false;
            $scope.data.documentId = JSON.parse($message).id;
            $scope.data.originalMediaName = $file.name;
        };

        $scope.fileError = function ($file, $message) {
            $scope.flow.files = [];
            $scope.isUploading = false;
            toaster.pop('danger', 'Error in file upload.');
        };
// Using remove function from TimelineConfigDAO
        $scope.removeFile = function () {
            if ($scope.data.documentId) {
                $scope.isRemove = true;
                TimelineConfigDAO.removeFile($scope.data.documentId)
                    .then(function () {
                        if (!!$scope.flow && !!$scope.flow.files) {
                            $scope.flow.files = [];
                        }
                        $scope.data.documentId = undefined;
                        $scope.data.originalMediaName = undefined;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                        $scope.isRemove = false;
                    });
            }
        }
// Upload file function end

        $scope.markAsWrongAction = function () {
            $scope.data.markAsWrongAction.$setSubmitted();
            if ($scope.data.markAsWrongAction.$valid && $scope.data.documentId) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/confirmation.modal.html',
                    controller: 'ConfirmModalController',
                    windowClass: 'cst-modal',
                    backdrop: 'static',
                    size: 'med',
                    resolve: {
                        message: function () {
                            return "Are you sure you want to " + $scope.data.actionType.value + ' ? ';
                        }
                    }
                });
                modalInstance.result.then(function () {
                    sendWrongActionData();
                });
            } else if (!$scope.data.documentId) {
                toaster.pop("error", "Please upload a letter");
            }
        }

        function sendWrongActionData() {
            Mask.show();
            var dtoList = [];
            var queryDto1 = {
                code: 'insert_member_audit_log_for_wpd',
                parameters: {
                    wpdMotherId: $scope.data.wrongMarkObj.wpdMotherId,
                    documentId: $scope.data.documentId,
                    reason: Number($scope.data.reason),
                    remarks: $scope.data.remarks,
                },
                sequence: 1
            };
            dtoList.push(queryDto1);
            if ($scope.data.actionType.key == $scope.wrongActionType.WRONG_DELIVERY.key) {
                var queryDto2 = {
                    code: 'mark_as_false_delivery',
                    parameters: {
                        action_by: $scope.data.loggedInUser.id,
                        member_id: $scope.data.wrongMarkObj.memberId,
                        pregnancy_reg_det_id: $scope.data.wrongMarkObj.pregnancyRegDetId,
                        wpd_mother_id: $scope.data.wrongMarkObj.wpdMotherId
                    },
                    sequence: 2
                };
                dtoList.push(queryDto2);
            }
            QueryDAO.executeAll(dtoList).then(function (res) {
            }).then(() => {
                toaster.pop("success", "Data saved successfully");
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                $uibModalInstance.close($scope.data.gender);
                Mask.hide();
            });
        }

        editwpdcontroller.retrieveHealthInfraForUser = function () {
            editwpdcontroller.loggedInUser = data.loggedInUser;
            editwpdcontroller.currentInstitution = editwpdcontroller.loggedInUser.rchInstitutionId;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_health_infra_for_user',
                parameters: {
                    userId: editwpdcontroller.loggedInUser.id
                }
            }).then(function (res) {
                editwpdcontroller.institutes = res.result
                Mask.hide()
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        editwpdcontroller.retrieveData = function () {
            var dto = {
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'Health Infrastructure Type'
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                Mask.hide();
                editwpdcontroller.institutionTypes = res.result;
            })
        }

        editwpdcontroller.retrieveWrongMarkReasons = function () {
            var dto = {
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'Mark as Wrong Delivery Reason'
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                Mask.hide();
                $scope.wrongMarkReasons = res.result;
            })
        }

        editwpdcontroller.deliveryPlaceChanged = function () {
            editwpdcontroller.wpdObject.institute = null;
            if (editwpdcontroller.wpdObject.deliveryPlace === 'HOSP') {
                if (editwpdcontroller.institutes.length == 1) {
                    editwpdcontroller.wpdObject.institute = editwpdcontroller.institutes[0];
                    editwpdcontroller.showDeliveryDoneBy = true;
                } else if (editwpdcontroller.institutes.length == 0) {
                    toaster.pop('error', 'No health infrastructure assigned');
                    editwpdcontroller.showDeliveryDoneBy = false;
                }
            }
        }

        editwpdcontroller.retrieveHospitalsByInstitutionType = function () {
            Mask.show();
            editwpdcontroller.showOtherInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(editwpdcontroller.wpdObject.institutionType)
                }
            }).then(function (res) {
                editwpdcontroller.showOtherInstitutes = true;
                editwpdcontroller.otherInstitutes = res.result;
                editwpdcontroller.wpdObject.institute = null;
                Mask.hide();
            })
        }

        editwpdcontroller.updateDeliveryPlace = function (deliveryPlaceChangeForm) {
            if (deliveryPlaceChangeForm.$valid) {
                if (editwpdcontroller.wpdObject.deliveryPlace === 'HOSP') {
                    editwpdcontroller.wpdObject.typeOfHospital = editwpdcontroller.wpdObject.institutionType;
                    editwpdcontroller.wpdObject.healthInfrastructureId = editwpdcontroller.wpdObject.institute;
                } else {
                    delete editwpdcontroller.wpdObject.typeOfHospital;
                    delete editwpdcontroller.wpdObject.healthInfrastructureId;
                }
                editwpdcontroller.institutionTypes.forEach(function (institution, index) {
                    if (institution.id == editwpdcontroller.wpdObject.typeOfHospital) {
                        editwpdcontroller.wpdObject.typeOfHospitalValue = institution.value;
                    }
                })
                $scope.save($scope.data.id, editwpdcontroller.wpdObject.deliveryPlace, editwpdcontroller.wpdObject.healthInfrastructureId,
                    editwpdcontroller.wpdObject.typeOfHospital, editwpdcontroller.wpdObject.institutionType,
                    editwpdcontroller.wpdObject.typeOfHospitalValue, $uibModalInstance);
            }
        }

        $scope.init();
    };
    angular.module('imtecho.controllers').controller('HelpDeskEdit', HelpDeskEdit);
})(window.angular);
