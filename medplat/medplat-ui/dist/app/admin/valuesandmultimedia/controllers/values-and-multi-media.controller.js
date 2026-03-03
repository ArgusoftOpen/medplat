(function (angular) {
    function ValueNMultimediaController($uibModal, Mask, APP_CONFIG, QueryDAO, GeneralUtil, $rootScope, toaster, AuthenticateService, $filter) {
        var vnm = this;
        vnm.accessToken = $rootScope.authToken;
        vnm.apiPath = APP_CONFIG.apiPath;

        var init = function () {
            vnm.reports = [];
            vnm.menuTypes = APP_CONFIG.menuTypes;
            vnm.retrieveForms();
            AuthenticateService.getLoggedInUser().then(function (res) {
                vnm.currentUser = res.data;
            });
            vnm.allowedImageExtensions = ['gif', 'jpg', 'png', 'psd', 'tif', 'jpeg'];
            vnm.allowedVideoExtensions = ['mp4', 'avi', 'mpg', '3gp', 'wmv', 'mov'];
            vnm.allowedAudioExtensions = ['amr', 'wma', 'mp3'];
        };

        vnm.deleteModal = function (object) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the status of this value?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                var dto = {
                    code: 'update_active_inactive_listvalues',
                    parameters: {
                        isActive: !object.is_active,
                        id: object.id
                    }
                };
                QueryDAO.execute(dto).then(function (res) {
                    toaster.pop('success', "Value status changed successfully.");
                    vnm.retrieveValuesForField(vnm.newValue.field.field_key);
                }).catch(function () {
                }).then(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        vnm.openMultiMediaPlayer = function (object) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/admin/valuesandmultimedia/views/multi-media-player.html',
                controller: 'MultimediaPlayerController as mmplayer',
                backdrop: 'static',
                keyboard: false,
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    object: function () {
                        return object;
                    }
                }
            });
            modalInstance.result.then(function () { }, function () { });
        };

        vnm.retrieveValuesForField = function (field) {
            var dto = {
                code: 'retrieve_list_values_by_field_key',
                parameters: {
                    fieldKey: field || null
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                vnm.valuesForSelectedFields = res.result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        vnm.retrieveFieldsForForm = function (form) {
            var dto = {
                code: 'retrival_listvalue_active_fields_acc_form',
                parameters: {
                    formKey: form
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                vnm.fieldsForForm = res.result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        vnm.retrieveForms = function () {
            var dto = {
                code: 'retrival_listvalue_active_forms',
                parameters: {
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                vnm.forms = res.result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        vnm.uploadFile = {
            singleFile: true,
            testChunks: false,
            allowDuplicateUploads: true,
            chunkSize: 10 * 1024 * 1024,
            headers: {
                Authorization: 'Bearer ' + vnm.accessToken
            },
            uploadMethod: 'POST'
        };

        vnm.upload = function ($file, $event, $flow) {
            vnm.responseMessage = {};
            delete vnm.newValue.value;
            if ((vnm.newValue.mulitmediaType == 'audio' && vnm.allowedAudioExtensions.indexOf($file.getExtension()) < 0) || (vnm.newValue.mulitmediaType == 'video' && vnm.allowedVideoExtensions.indexOf($file.getExtension()) < 0) || (vnm.newValue.mulitmediaType == 'image' && vnm.allowedImageExtensions.indexOf($file.getExtension()) < 0)) {
                vnm.isError = true;
                vnm.errorMessage = 'Please select correct file to upload!';
                $flow.cancel();
                return false;
            }
            delete vnm.errorMessage;
            vnm.isError = false;
            vnm.isFormatError = false;
            $flow.opts.target = APP_CONFIG.apiPath + '/values_and_multimedia/upload';
        };

        vnm.uploadFn = function ($files, $event, $flow) {
            Mask.show();
            vnm.flow = ($flow);

            if (!vnm.isFormatError) {
                $flow.upload();
            } else {
                $flow.cancel();
            }
            Mask.hide();
        };

        vnm.getUploadResponse = function ($file, $message, $flow) {
            var fileName = $message;
            vnm.newValue.fileSize = $file.size / 1000;
            vnm.newValue.value = fileName;
        };

        vnm.openAddEditModal = function (value) {
            vnm.addForm.$setSubmitted();
            if (vnm.addForm.$valid) {
                if (value) {
                    vnm.editMode = true;
                    var formCopy = angular.copy(vnm.newValue.formId);
                    vnm.newValue = angular.copy(value);
                    vnm.newValue.field = _.find(vnm.fieldsForForm, function (field) {
                        return field.field_key == vnm.newValue.field_key;
                    });
                    vnm.newValue.fileSize = value.file_size;
                    vnm.newValue.formId = formCopy;
                    vnm.newValue.mulitmediaType = value.multimedia_type;
                } else {
                    vnm.editMode = false;
                }
                vnm.toggleFilter();
            }
        };

        vnm.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        vnm.addValue = function () {
            vnm.addModalForm.$setSubmitted();
            if (vnm.addModalForm.$valid && vnm.newValue.value) {
                var dto = {};
                if (vnm.editMode) {
                    dto = {
                            code: 'update_listvalues',
                        parameters: {
                            value: vnm.newValue.value,
                            fileSize: parseInt(vnm.newValue.fileSize),
                            id: vnm.newValue.id,
                            multimediaType: vnm.newValue.mulitmediaType
                        }
                    };
                } else {
                    dto = {
                        code: 'insert_listvalues',
                        parameters: {
                            value: vnm.newValue.value,
                            fieldKey: vnm.newValue.field.field_key,
                            fileSize: parseInt(vnm.newValue.fileSize || 0),
                            multimediaType: vnm.newValue.mulitmediaType || null
                        }
                    };
                }

                Mask.show();
                QueryDAO.execute(dto).then(function (res) {
                    if (vnm.editMode) {
                        toaster.pop('success', 'Value updated successfully');
                    } else {
                        toaster.pop('success', "Value added successfully");
                    }
                    vnm.retrieveValuesForField(vnm.newValue.field.field_key);
                    vnm.clearValues();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else if (vnm.addModalForm.$valid && !vnm.newValue.value) {
                if (vnm.isToastVisible) {
                    return;
                } else {
                    vnm.isToastVisible = true;
                    toaster.pop('error', "Please upload the file");
                    setTimeout(function () {
                        vnm.isToastVisible = false;
                    }, 4000)
                }
            }
        };

        vnm.clearValues = function () {
            var valueCopy = angular.copy(vnm.newValue);
            vnm.newValue = {
                formId: valueCopy.formId,
                field: valueCopy.field
            };
            vnm.addModalForm.$setPristine();
            vnm.flow = {};
            vnm.isError = false;
            delete vnm.errorMessage;
            vnm.toggleFilter();

        };

        init();
    }
    angular.module('imtecho.controllers').controller('ValueNMultimediaController', ValueNMultimediaController);
})(window.angular);
