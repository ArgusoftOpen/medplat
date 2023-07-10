(function () {
    function SystemCodeController(QueryDAO,ServerManageDAO,toaster, Mask, GeneralUtil,$scope, AuthenticateService, $uibModal) {
        let ctrl = this;

        const _init = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(user => {
                ctrl.loggedInUser = user.data;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            ctrl.tables = [{key: 'OPD_LAB_TEST', name: 'Opd Lab Test'}, {key: 'LIST_VALUE', name: 'List Value'}];
            ctrl.table = ctrl.tables[1];
            ctrl.retrieveForms();
            ctrl.retrieveSystemCodes();

            // ServerManageDAO.retrieveKeyValueBySystemKey('WHO_ICD_CODE_10_DEFAULT_START_CATEGOY').then((res) => {
            //     ctrl.startingCodeCategory = res.keyValue;
            // });

            // ServerManageDAO.retrieveKeyValueBySystemKey('WHO_ICD_CODE_10_DEFAULT_END_CATEGOY').then((res) => {
            //     ctrl.endingCodeCategory = res.keyValue;
            // });
        };

        ctrl.retrieveSystemCodes = function () {
            var dto = {
                code: 'retrieve_system_codes_supported_types',
                parameters: {}
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                ctrl.systemCodes = res.result;
                ctrl.noOfColumns = ctrl.systemCodes.length;
                ctrl.columnNames = ctrl.systemCodes.map(val => val.codeType);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };
        ctrl.onChangeTable = function (table) {
            if (table.key === 'LIST_VALUE') {
                ctrl.retrieveForms();
            } else if (table.key === 'OPD_LAB_TEST') {
                ctrl.retrieveOpdLabTest();
            }
        };

        ctrl.retrieveFieldsForForm = function (form) {
            var dto = {
                code: 'retrival_listvalue_active_fields_acc_form',
                parameters: {
                    formKey: form
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                ctrl.fieldsForForm = res.result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.retrieveForms = function () {
            var dto = {
                code: 'retrival_listvalue_active_forms',
                parameters: {
                }
            };
            Mask.show();
            QueryDAO.execute(dto).then(function (res) {
                ctrl.forms = res.result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        ctrl.retrieveActiveListValueDetails = function (field) {
            ctrl.field = field;
            let queryDto = {
                code: 'retrieve_system_code_mapping_by_field_id',
                parameters: {
                    fieldId: field
                }
            };
            Mask.show();
            QueryDAO.execute(queryDto).then((res) => {

                ctrl.activeListValueDetails = res.result.map((data) => {
                    data.SYSTEM_CODE = JSON.parse(data.SYSTEM_CODE);
                    return data;
                });
                if (res.result.length > 0) {
                    ctrl.noOfColumns = res.result[0].codecount;
                    ctrl.columnNames = res.result[0].codelist.map(val => val.codeType);
                }

            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };
        ctrl.toggleIsEditable = function (listValueDetail) {
            listValueDetail.isEditable = !listValueDetail.isEditable;
        };

        ctrl.findSystemCode = function (codeType, systemCodes) {
            return systemCodes.find(val => val.codeType === codeType);
        };

        ctrl.manageIcd = function() {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/systemcode/views/manage-icd-snomed.model.html',
                controllerAs : 'manageIcdCtrl',
                controller: function($uibModalInstance,SystemCodesService,startingCodeCategory,endingCodeCategory,Mask,toaster,AuthenticateService,APP_CONFIG) {
                    let manageIcdCtrl = this;
                    manageIcdCtrl.startCodeFromSystem = startingCodeCategory;
                    manageIcdCtrl.endCodeFromSystem = endingCodeCategory;


                    manageIcdCtrl.startingCodeCategory = startingCodeCategory  || 'A00'
                    manageIcdCtrl.endingCodeCategory = endingCodeCategory || 'Z99'

                    manageIcdCtrl.isDataInserting = false;
                    manageIcdCtrl.version = 10;
                    manageIcdCtrl.releaseYear = 2016;



                    manageIcdCtrl.process = function() {
                        manageIcdCtrl.isDataInserting = true;
                        if (manageIcdCtrl.manageIcdForm.$valid) {
                            manageIcdCtrl.manageIcdForm.$setSubmitted();
                            Mask.show();
                            SystemCodesService.processICDCode(manageIcdCtrl.version,manageIcdCtrl.releaseYear,manageIcdCtrl.startingCodeCategory.toUpperCase(),manageIcdCtrl.endingCodeCategory.toUpperCase())
                                .then((res) => {
                                    toaster.pop('success',`${res["Total Processed"]} Data Inserted Successfully and took ${res["Time Taken"]}`);
                                    $uibModalInstance.dismiss('cancel');
                                    Mask.hide();
                                    manageIcdCtrl.isDataInserting = false;
                                })
                                .catch((err) => {
                                    toaster.pop('danger','An error Ocuured',err.data.message);
                                    $uibModalInstance.dismiss('cancel');
                                    Mask.hide();
                                    manageIcdCtrl.isDataInserting = false;
                                });
                        }
                    }

                    manageIcdCtrl.cancel = function() {
                        $uibModalInstance.dismiss('cancel');
                    }

                    manageIcdCtrl.upload = function ($file, $event, $flow) {
                        manageIcdCtrl.responseMessage = {};
                        if (($file.getExtension() === 'txt' || $file.getExtension() === 'csv')
                            // && manageIcdCtrl.uploadFile.dateFormate
                        ) {
                            delete manageIcdCtrl.errorMessage;
                            manageIcdCtrl.isError = false;

                            manageIcdCtrl.isFormatError = false;
                            $flow.opts.target = APP_CONFIG.apiPath + '/systemcode/uploadcsv?codeType=SNOMED_CT';
                        } else {
                            manageIcdCtrl.isError = true;
                            // if (!manageIcdCtrl.uploadFile.dateFormate) {
                            //     toaster.pop('error', 'Please select Date format');
                            // }
                            // else {
                            toaster.pop('error', 'Please select correct file to upload!');
                            // }
                            $flow.cancel();
                            return false;
                        }
                    };

                    manageIcdCtrl.uploadFile = {
                        singleFile: true,
                        testChunks: false,
                        allowDuplicateUploads: false,
                        chunkSize: 1 * 1024 * 1024 * 1024, //1 GB
                        headers: {
                            Authorization: 'Bearer ' + AuthenticateService.getToken()
                        },
                        uploadMethod: 'POST',
                        permanentErrors: [404, 500, 501, 400]
                    };

                    manageIcdCtrl.filesStack = [];

                    manageIcdCtrl.uploadFn = function ($files, $event, $flow) {
                        if (manageIcdCtrl.filesStack.includes($flow.files[0].uniqueIdentifier)) {
                            toaster.pop('error', 'Duplicate files not allowed');
                            $flow.cancel();
                            Mask.hide();
                        } else {
                            manageIcdCtrl.filesStack.push($flow.files[0].uniqueIdentifier);
                            Mask.show();
                            manageIcdCtrl.flow = ($flow);
                            if (!manageIcdCtrl.isFormatError) {
                                $flow.upload();
                                manageIcdCtrl.isUploading = true;
                            } else {
                                $flow.cancel();
                                Mask.hide();
                            }
                            Mask.hide()
                        }
                    };

                    manageIcdCtrl.getUploadResponse = function ($file, $message, $flow) {
                        Mask.hide();
                        if ($message !== null && ($message.length === 0 || $message !== '')) {
                            manageIcdCtrl.error = false;
                            manageIcdCtrl.operationDone = true;
                        } else {
                            manageIcdCtrl.error = true;
                            manageIcdCtrl.operationDone = false;
                        }
                        manageIcdCtrl.isUploading = false;
                    };

                    manageIcdCtrl.uploadError = function ($file, $message, $flow) {
                        toaster.pop('error', JSON.parse($message).message);
                    }


                },
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    startingCodeCategory : function() {
                        return ctrl.startingCodeCategory;
                    },
                    endingCodeCategory : function() {
                        return ctrl.endingCodeCategory;
                    }
                }
            });
            modalInstance.result.then(function (val) {
            }, function () { });
        }

        ctrl.editCode = function (listValueDetail) {
            listValueDetail.tableType = ctrl.table.key;
            listValueDetail.codeTypes = ctrl.systemCodes;

            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/systemcode/views/edit-code.modal.html',
                controller: 'EditCodeModalController',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    listValueDetail: function () {
                        return listValueDetail;
                    }
                }
            });
            modalInstance.result.then(function (val) {
//                ctrl.onChangeTable(ctrl.table);

                if (ctrl.table.key === 'LIST_VALUE') {
//                    ctrl.retrieveForms();
                    ctrl.retrieveActiveListValueDetails (ctrl.field);


                } else if (ctrl.table.key === 'OPD_LAB_TEST') {
//                    ctrl.retrieveOpdLabTest();
                    ctrl.retrieveOpdLabTest();
                }

            }, function () { });

        };
        ctrl.clearValues = function () {
            ctrl.activeListValueDetails = [];
            ctrl.forms = [];
            ctrl.fieldsForForm = [];
            delete ctrl.newValue;
        };

        ctrl.retrieveOpdLabTest = function () {
            let queryDto = {
                code: 'retrieve_system_code_mapping_by_opd_lab_test',
                parameters: {}
            };
            Mask.show();
            QueryDAO.execute(queryDto).then((res) => {
                ctrl.activeListValueDetails = res.result.map((data) => {
                    data.SYSTEM_CODE = JSON.parse(data.SYSTEM_CODE);
                    return data;
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }
;
        _init();
    }
    angular.module('imtecho.controllers').controller('SystemCodeController', SystemCodeController);
})();
