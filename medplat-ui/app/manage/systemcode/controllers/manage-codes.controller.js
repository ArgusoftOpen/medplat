(function () {
    function SystemCodeController(QueryDAO, ServerManageDAO, toaster, Mask, GeneralUtil, $scope, AuthenticateService, $uibModal) {
        let ctrl = this;

        const _init = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(user => {
                ctrl.loggedInUser = user.data;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            ctrl.tables = [{ key: 'OPD_LAB_TEST', name: 'Opd Lab Test' }, { key: 'LIST_VALUE', name: 'List Value' }];
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
            ctrl.clearValues();
            if (table.key === 'LIST_VALUE') {
                ctrl.retrieveForms();
            } 
            // else if (table.key === 'OPD_LAB_TEST') {
            //     ctrl.retrieveOpdLabTest();
            // }
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
                    if (res.result[0].codelist && res.result[0].codelist.length) {
                        ctrl.columnNames = res.result[0].codelist.map(val => val.codeType);
                    }
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.toggleIsEditable = function (listValueDetail) {
            listValueDetail.isEditable = !listValueDetail.isEditable;
        };

        ctrl.findSystemCode = function (codeType, systemCodes) {
            return systemCodes.find(val => val.codeType === codeType);
        };

        ctrl.showFlywayQueryOption = (systemCodes) => {
            return systemCodes.filter(sc => sc.id).length ? true : false;
        }

        ctrl.showFlywayQuery = function (listValueDetail) {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/systemcode/views/show-flyway-query.modal.html',
                controller: 'ShowFlywayQueryController',
                windowClass: 'cst-modal',
                controllerAs: 'flywayCtrl',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    listValueDetail: function () {
                        return listValueDetail;
                    },
                    fieldKey: function () {
                        return ctrl.newValue.field.field_key;
                    }
                }
            });
            modalInstance.result.then(() => {
                console.log('Show Flyway Query Modal closed.');
            }, () => {
                console.log('Show Flyway Query Modal dismissed.');
            });
        };

        ctrl.manageIcd = function () {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/systemcode/views/manage-icd-snomed.model.html',
                controllerAs: 'manageIcdCtrl',
                controller: function ($uibModalInstance, SystemCodesService, startingCodeCategory, endingCodeCategory, Mask, toaster, AuthenticateService, APP_CONFIG) {
                    let manageIcdCtrl = this;
                    manageIcdCtrl.startCodeFromSystem = startingCodeCategory;
                    manageIcdCtrl.endCodeFromSystem = endingCodeCategory;
                    // manageIcdCtrl.startingCodeCategory = startingCodeCategory || 'A00'
                    // manageIcdCtrl.endingCodeCategory = endingCodeCategory || 'Z99'
                    manageIcdCtrl.isDataInserting = false;
                    manageIcdCtrl.version = 10;
                    manageIcdCtrl.releaseYear = 2016;

                    // manageIcdCtrl.process = function () {
                    //     manageIcdCtrl.isDataInserting = true;
                    //     if (manageIcdCtrl.manageIcdForm.$valid) {
                    //         manageIcdCtrl.manageIcdForm.$setSubmitted();
                    //         Mask.show();
                    //         SystemCodesService.processICDCode(manageIcdCtrl.version, manageIcdCtrl.releaseYear, manageIcdCtrl.startingCodeCategory.toUpperCase(), manageIcdCtrl.endingCodeCategory.toUpperCase())
                    //             .then((res) => {
                    //                 toaster.pop('success', `${res["Total Processed"]} Data Inserted Successfully and took ${res["Time Taken"]}`);
                    //                 $uibModalInstance.dismiss('cancel');
                    //                 Mask.hide();
                    //                 manageIcdCtrl.isDataInserting = false;
                    //             })
                    //             .catch((err) => {
                    //                 toaster.pop('danger', 'An error Ocuured', err.data.message);
                    //                 $uibModalInstance.dismiss('cancel');
                    //                 Mask.hide();
                    //                 manageIcdCtrl.isDataInserting = false;
                    //             });
                    //     }
                    // }

                    manageIcdCtrl.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    }

                    manageIcdCtrl.upload = function ($file, $event, $flow) {
                        manageIcdCtrl.responseMessage = {};
                        if (($file.getExtension() === 'txt' || $file.getExtension() === 'csv')) {
                            delete manageIcdCtrl.errorMessage;
                            manageIcdCtrl.isError = false;
                            manageIcdCtrl.isFormatError = false;
                            $flow.opts.target = APP_CONFIG.apiPath + '/systemcode/uploadcsv?codeType=SNOMED_CT';
                        } else {
                            manageIcdCtrl.isError = true;
                            toaster.pop('error', 'Please select correct file to upload!');
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
                }
                // ,
                // windowClass: 'cst-modal',
                // size: 'lg',
                // resolve: {
                //     startingCodeCategory: function () {
                //         return ctrl.startingCodeCategory;
                //     },
                //     endingCodeCategory: function () {
                //         return ctrl.endingCodeCategory;
                //     }
                // }
            });
            modalInstance.result.then(function () { }, function () { });
        }

        ctrl.editCode = function (listValueDetail) {
            listValueDetail.tableType = ctrl.table.key;
            listValueDetail.codeTypes = ctrl.systemCodes.map(val => val.codeType);
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
                if (ctrl.table.key === 'LIST_VALUE') {
                    ctrl.retrieveActiveListValueDetails(ctrl.field);
                } 
                // else if (ctrl.table.key === 'OPD_LAB_TEST') {
                //     ctrl.retrieveOpdLabTest();
                // }
            }, function () { });
        };

        ctrl.clearValues = function () {
            ctrl.activeListValueDetails = [];
            ctrl.forms = [];
            ctrl.fieldsForForm = [];
            delete ctrl.newValue;
        };

        // ctrl.retrieveOpdLabTest = function () {
        //     let queryDto = {
        //         code: 'retrieve_system_code_mapping_by_opd_lab_test',
        //         parameters: {}
        //     };
        //     Mask.show();
        //     QueryDAO.execute(queryDto).then((res) => {
        //         ctrl.activeListValueDetails = res.result.map((data) => {
        //             data.SYSTEM_CODE = JSON.parse(data.SYSTEM_CODE);
        //             return data;
        //         });
        //     }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        // };

        _init();
    }
    angular.module('imtecho.controllers').controller('SystemCodeController', SystemCodeController);
})();


(function () {
    function ShowFlywayQueryController(listValueDetail, fieldKey, $uibModalInstance, toaster) {
        let flywayCtrl = this;
        flywayCtrl.listValueDetail = listValueDetail;
        flywayCtrl.fieldKey = fieldKey;

        flywayCtrl.init = function () {
            flywayCtrl.query = '';
            flywayCtrl.listValueDetail.SYSTEM_CODE.forEach(sc => {
                flywayCtrl.query += `
-- Flyway for ${sc.codeType} System Code of ${flywayCtrl.listValueDetail.name};

DELETE FROM system_code_master WHERE id = '${sc.id}';

INSERT INTO system_code_master
    (id, table_id, table_type, code_type, code, parent_code, description, created_by, created_on, modified_by, modified_on)
VALUES
    ('${sc.id}', (
        select id
        from listvalue_field_value_detail
        where is_active = true
            and field_key = '${flywayCtrl.fieldKey}'
            and value = '${flywayCtrl.listValueDetail.name}'
        limit 1
    ), '${sc.tableType}', '${sc.codeType}', '${sc.code}', ${sc.parentCode ? `'${sc.parentCode}'`  : null}, ${sc.description ? `'${sc.description}'`  : null}, ${sc.createdBy}, now(), ${sc.createdBy}, now());

`;
            })
        };

        flywayCtrl.copyQuery = function () {
            const selBox = document.createElement('textarea');
            selBox.style.position = 'fixed';
            selBox.style.left = '0';
            selBox.style.top = '0';
            selBox.style.opacity = '0';
            selBox.value = flywayCtrl.query;
            document.body.appendChild(selBox);
            selBox.focus();
            selBox.select();
            document.execCommand('copy');
            document.body.removeChild(selBox);
            $uibModalInstance.close();
            toaster.pop('success', 'Query Copied');
        };

        flywayCtrl.downloadQuery = function () {
            var a = window.document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob([flywayCtrl.query], { type: 'text/plain' }));
            var name = "V" + new Date().getTime() + "__CHANGE_IN_" + flywayCtrl.listValueDetail.code + '.sql';
            a.download = name;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            $uibModalInstance.close();
        };

        flywayCtrl.cancel = function () {
            $uibModalInstance.close();
        };

        flywayCtrl.init();
    }
    angular.module('imtecho.controllers').controller('ShowFlywayQueryController', ShowFlywayQueryController);
})();
