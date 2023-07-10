(function () {
    function ReportLayoutController($state, Mask, GeneralUtil, toaster, ReportDAO, REPORTS, LAYOUTCONFIG, $uibModal, APP_CONFIG, GroupDAO, AuthenticateService, $q) {
        var rl = this;
        rl.menuTypes = APP_CONFIG.menuTypes;
        var copyOfSubGroup;
        var dynamicReportType = 'DYNAMIC';
        var selectedField = {};
        rl.minNumberOfColumnPerPage = 1;

        var init = function () {
            rl.pageval = {};
            rl.reportObj = { reportType: dynamicReportType };
            rl.layouts = REPORTS.layouts;
            rl.availableImplicitParameters = ["#loggedInUser#"];
            rl.retrieveAllReports();
            rl.dateRangeOptions = [];
            rl.dummyConfig = {
                availableContainers: [],
                layout: rl.layouts[0].key,
                templateType: rl.layouts[0].templateType,
                containers: LAYOUTCONFIG[rl.layouts[0].key],
                data: { tableData: [{ label1: 'value1', label2: 'value2', label3: 'value3', label4: 'value4' }] }
            };
            rl.reportObj.configJson = {
                layout: rl.layouts[0].key,
                templateType: rl.layouts[0].templateType,
                containers: {},
                isPrintOption: true,
                isExcelOption: true,
                isLandscape: false,
                numberOfRecordsPerPage: 20,
                numberOfColumnPerPage: 10
            };
            var index = 0;
            angular.forEach(rl.dummyConfig.containers, function (container, key) {
                rl.reportObj.configJson.containers[key] = [];
                if (key === 'tableContainer') {
                    rl.reportObj.configJson.containers[key] = [{ fieldName: 'tableField' }];
                }
                index++;
            });
            rl.inputtypes = [
                {
                    key: 'boolean',
                    value: 'Boolean'
                },
                {
                    key: 'date',
                    value: 'Date'
                },
                {
                    key: 'comboForReport',
                    value: 'Combo'
                },
                {
                    key: 'number',
                    value: 'Number'
                },
                {
                    key: 'multiselectForReport',
                    value: 'Multi Select'
                },
                {
                    key: 'text',
                    value: 'Text'
                },
                {
                    key: 'email',
                    value: 'Email'
                },
                {
                    key: 'location',
                    value: 'Location'
                },
                {
                    key: 'selectizeForCombo',
                    value: 'Selectize for combo'
                },
                {
                    key: 'dateNTime',
                    value: 'Date and time'
                },
                {
                    key: 'onlyMonthFromTo',
                    value: 'Between Months'
                },
                {
                    key: 'onlyMonth',
                    value: 'Single Month'
                },
                {
                    key: 'dateFromTo',
                    value: 'Between dates'
                },
                {
                    key: 'dateRangePicker',
                    value: 'Date Range'
                },
                {
                    key: 'singleCheckbox',
                    value: 'Single Checkbox'
                }
            ];
            if ($state.params.id && $state.params.id !== '') {
                rl.retrieveGroups();
                let promises = [];
                promises.push(ReportDAO.getReportDetails({ id: $state.params.id }));
                promises.push(AuthenticateService.getAssignedFeature("techo.report.all"));
                Mask.show();
                $q.all(promises).then(function (responses) {
                    rl.reportObj = responses[0];
                    rl.featureJson = responses[1].featureJson;
                    if (!rl.featureJson.basic && !rl.featureJson.advanced) {
                        $state.go('techo.report.all');
                        toaster.pop('error', 'No rights has been assigned to the user!')
                        return;
                    }
                    rl.availableImplicitParameters = ["#loggedInUser#"];
                    _.each(rl.reportObj.configJson.containers.fieldsContainer, function (field) {
                        if (!field.isInvalid && field.fieldName) {
                            if (field.fieldType === 'onlyMonthFromTo' || field.fieldType === 'dateFromTo' || field.fieldType === 'dateRangePicker') {
                                rl.availableImplicitParameters.push('#from_' + field.fieldName + '#');
                                rl.availableImplicitParameters.push('#to_' + field.fieldName + '#');
                            } else {
                                rl.availableImplicitParameters.push('#' + field.fieldName + '#');
                                if (!!field.demographicFilterRequired && field.demographicFilterRequired) {
                                    rl.availableImplicitParameters.push('#demographic_' + field.fieldName + '#');
                                }
                            }
                        }
                    });
                    if (rl.reportObj.configJson.layout && rl.reportObj.configJson.layout.includes("WithPagination")) {
                        rl.availableImplicitParameters.push('#limit_offset#');
                    }
                    if (rl.reportObj.configJson.isPrintOption == undefined)
                        rl.reportObj.configJson.isPrintOption = true;
                    if (rl.reportObj.configJson.isExcelOption == undefined)
                        rl.reportObj.configJson.isExcelOption = true;
                    if (!rl.reportObj.configJson.isLandscape)
                        rl.reportObj.configJson.isLandscape = false;
                    if (!rl.reportObj.configJson.numberOfRecordsPerPage)
                        rl.reportObj.configJson.numberOfRecordsPerPage = 20;
                    if (!rl.reportObj.configJson.numberOfColumnPerPage)
                        rl.reportObj.configJson.numberOfColumnPerPage = 10;

                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else {
                let promises = [];
                promises.push(AuthenticateService.getAssignedFeature("techo.report.all"));
                Mask.show();
                $q.all(promises).then(function (responses) {
                    rl.featureJson = responses[0].featureJson;
                    if (!rl.featureJson.basic && !rl.featureJson.advanced) {
                        $state.go('techo.report.all');
                        toaster.pop('error', 'No rights has been assigned to the user!')
                        return;
                    } else if (rl.featureJson.basic && !rl.featureJson.advanced) {
                        $state.go('techo.report.all');
                        toaster.pop('error', 'No rights has been assigned to the user to create Report!')
                        return;
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        };

        rl.addFieldToContainer = function () {
            rl.newFieldForm.$setSubmitted();
            if (!rl.reportObj.configJson.containers.fieldsContainer) {
                rl.reportObj.configJson.containers.fieldsContainer = [];
            }
            if (rl.newField) {
                var fieldExists = checkFieldNameExist(rl.newField.fieldName, rl.isEdit);
            }

            if (rl.newField.fieldName === 'date_range') {
                rl.newField.dateRangeOptions = rl.dateRangeOptions;
            }

            if (!fieldExists && rl.newFieldForm.$valid) {
                if (rl.editMode) {
                    rl.reportObj.configJson.containers.fieldsContainer.splice(selectedField.index, 1, rl.newField);
                    //                    rl.reportObj.configJson.containers.fieldsContainer = _.without(rl.reportObj.configJson.containers.fieldsContainer, selectedField);
                } else {
                    rl.reportObj.configJson.containers.fieldsContainer.push(rl.newField);
                }
                rl.availableImplicitParameters = ["#loggedInUser#"];
                _.each(rl.reportObj.configJson.containers.fieldsContainer, function (field) {
                    if (!field.isInvalid && field.fieldName) {
                        if (field.fieldType === 'onlyMonthFromTo' || field.fieldType === 'dateFromTo' || field.fieldType === 'dateRangePicker') {
                            rl.availableImplicitParameters.push('#from_' + field.fieldName + '#');
                            rl.availableImplicitParameters.push('#to_' + field.fieldName + '#');
                        } else {
                            rl.availableImplicitParameters.push('#' + field.fieldName + '#');
                            if (!!field.demographicFilterRequired && field.demographicFilterRequired) {
                                rl.availableImplicitParameters.push('#demographic_' + field.fieldName + '#');
                            }
                        }
                    }
                });
                if (rl.reportObj.configJson.layout && rl.reportObj.configJson.layout.includes("WithPagination")) {
                    rl.availableImplicitParameters.push('#limit_offset#');
                }
                rl.newFieldForm.$setPristine();
                rl.newField = {};
                rl.toggleFilter();

            } else if (fieldExists) {
                rl.newFieldForm.fieldName.$setValidity('nameExist', false);
            } else {
                rl.newFieldForm.fieldName.$setValidity('nameExist', true);
            }
        };
        rl.addFieldToTableContainer = function () {
            rl.newFieldForm.$setSubmitted();
            if (!rl.reportObj.configJson.containers.tableFieldContainer) {
                rl.reportObj.configJson.containers.tableFieldContainer = [];
            }
            if (rl.newField) {
                var fieldExists = checkFieldNameExist(rl.newField.fieldName, rl.editMode);
            }
            if (!fieldExists && rl.newFieldForm.$valid) {
                if (rl.newField.navigationState) {
                    rl.newField.customState = rl.newField.navigationState;
                }
                if (rl.newField.customParam) {
                    var customState = '' + (rl.newField.customState);
                    rl.newField.customState = (customState.slice(0, rl.newField.customState.indexOf('}')) + "," + rl.newField.customParam + customState.slice(rl.newField.customState.indexOf('}')));
                }
                if (rl.editMode) {
                    rl.reportObj.configJson.containers.tableFieldContainer.splice(selectedField.index, 1, rl.newField);
                } else {
                    rl.reportObj.configJson.containers.tableFieldContainer.push(rl.newField);
                }
                rl.newFieldForm.$setPristine();
                rl.newField = {};
                rl.toggleFilter();

            } else if (fieldExists) {
                rl.newFieldForm.fieldName.$setValidity('nameExist', false);
            } else {
                rl.newFieldForm.fieldName.$setValidity('nameExist', true);
            }
        };

        rl.saveReport = function () {
            if (isFormValid()) {
                assignSequence();
                _.each(rl.reportObj.configJson.containers, function (container) {
                    _.each(container, function (field) {
                        delete field.errorMsg;
                    });
                });
                Mask.show();
                ReportDAO.saveConfigDynamicDetail(rl.reportObj).then(function (response) {
                    toaster.pop('success', "Report Configuration saved successfully");
                    $state.go("techo.report.all");
                }, function (error) {
                    var exceptionMap = error.data.data;
                    _.each(exceptionMap, function (exception, fieldName) {
                        if (fieldName === 'tableField') {
                            rl.reportObj.configJson.containers.tableContainer[0].errorMsg = 'Invalid Sql Query:' + exception;
                            rl.reportObj.configJson.selectedContainer = 'tableContainer';
                            rl.containerChange();
                        } else {
                            _.each(rl.selectedContainerFields, function (field) {
                                if (field.fieldName === fieldName) {
                                    field.errorMsg = 'Invalid Sql Query:' + exception;
                                }
                            });
                        }

                    });
                    toaster.pop("error", error.data.message);
                }).finally(function () {
                    Mask.hide();
                });
            }
        };

        rl.selectizeOptions = {
            persist: false,
            valueField: 'id',
            labelField: 'text',
            searchField: 'text',
            delimiter: '|',
            create: true,
        };

        var assignSequence = function () {
            if (rl.selectedContainerFields) {
                for (var index = 0; index < rl.selectedContainerFields.length; index++) {
                    rl.selectedContainerFields[index].sequence = index + 1;
                }
            }
        };

        rl.retrieveGroups = function () {
            Mask.show();
            GroupDAO.getReportGroups({ subGroupRequired: true, groupType: rl.reportObj.menuType }).then(function (res) {
                rl.parentGroup = [];
                rl.subGroup = [];
                angular.forEach(res, function (itr) {
                    if (angular.isDefined(itr.parentGroup)) {
                        rl.subGroup.push(itr);
                    } else {
                        rl.parentGroup.push(itr);
                    }
                });
                copyOfSubGroup = angular.copy(rl.subGroup);
                rl.onSubGroupChanged();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        rl.onParentGroupChanged = function () {
            if (rl.reportObj.parentGroupId) {
                rl.subGroup = [];
                angular.forEach(copyOfSubGroup, function (itr) {
                    if (itr.parentGroup === rl.reportObj.parentGroupId) {
                        rl.subGroup.push(itr);
                    }
                });
            } else {
                rl.reportObj.subGroupId = null;
                rl.subGroup = angular.copy(copyOfSubGroup);
            }
        };

        rl.onSubGroupChanged = function () {
            if (rl.reportObj.subGroupId) {
                angular.forEach(copyOfSubGroup, function (itr) {
                    if (itr.id === rl.reportObj.subGroupId) {
                        rl.reportObj.parentGroupId = itr.parentGroup;
                    }
                });
            }
            rl.onParentGroupChanged(rl.reportObj.parentGroupId);
        };

        rl.onQueryChanged = function (config, $index) {
            var textAreaFormElement = rl.manageFieldConfigForm['rl.fieldConfigForm']['querytextarea' + $index];
            if (config.query) {
                if (config.query.toLowerCase().indexOf("select") < 0) {
                    textAreaFormElement.$setValidity("invalidSelectQuery", false);
                } else {
                    textAreaFormElement.$setValidity("invalidSelectQuery", true);
                }
            }
        };

        var checkFieldNameExist = function (fieldName, isEdit) {
            var matchedField;
            var fieldListCopy = angular.copy(rl.selectedContainerFields);
            if (fieldName) {
                matchedField = _.filter(fieldListCopy, function (field) {
                    return field.fieldName === fieldName;
                });
                if (matchedField.length === 1 && isEdit) {
                    return false;
                } else if (matchedField.length > 0 && !isEdit) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        };

        rl.deleteField = function (index, fieldName, isTable) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                size: 'med',
                resolve: {
                    message: function () {
                        return 'Are you sure you want to delete the field?';
                    }
                }
            });
            modalInstance.result.then(function () {
                if (!isTable) {
                    rl.reportObj.configJson.containers.fieldsContainer.splice(index, 1);
                    rl.availableImplicitParameters.splice(index + 1, 1);
                } else {
                    rl.reportObj.configJson.containers.tableFieldContainer.splice(index, 1);
                }
            });
        };

        var isFormValid = function () {
            var isFieldConfigFormValid, isTableFieldValid;
            rl.manageTemplateForm.$setPristine();
            rl.manageTemplateForm.$setSubmitted();
            var isTemplateFormValid = rl.manageTemplateForm.$valid;
            if (!isTemplateFormValid) {
                rl.activeTab = 'info';
            }
            if (!rl.reportObj.configJson.containers.tableContainer[0].query) {
                isTableFieldValid = false;
                toaster.pop('error', 'Please enter table query in - Table container');
            } else {
                isTableFieldValid = true;
            }

            let isNumberOfColumnPerPageValid = false;
            let referenceColumns = [];
            if (!rl.reportObj.configJson.containers.tableFieldContainer) {
                rl.reportObj.configJson.containers.tableFieldContainer = [];
            }
            let tableFieldContainer = rl.reportObj.configJson.containers.tableFieldContainer;
            _.each(tableFieldContainer, function (tableField) {
                if (tableField.isReferenceColumn) {
                    referenceColumns.push(tableField.fieldName);
                }
            });
            if (rl.reportObj.configJson.numberOfColumnPerPage && (rl.reportObj.configJson.numberOfColumnPerPage > referenceColumns.length)) {
                isNumberOfColumnPerPageValid = true;
            } else {
                toaster.pop('error', 'Number of Column Per Page should be greater than ' + referenceColumns.length);
            }

            return isTemplateFormValid && isTableFieldValid && isNumberOfColumnPerPageValid;
        };

        rl.toggleFilter = function (field, index) {
            if (field) {
                if (field.fieldName == 'date_range') {
                    rl.dateRangeOptions = field.dateRangeOptions;
                    rl.lastWeek = field.dateRangeOptions.includes('lastWeek');
                    rl.lastMonth = field.dateRangeOptions.includes('lastMonth');
                    rl.lastYear = field.dateRangeOptions.includes('lastYear');
                    rl.financialYear = field.dateRangeOptions.includes('financialYear');
                }
                selectedField = field;
                selectedField.index = index;
                rl.newField = angular.copy(field);
                rl.editMode = true;
            } else {
                rl.newField = {};
                rl.editMode = false;
            }
            rl.newFieldForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        rl.retrieveAllReports = function () {
            // Mask.show();
            // ReportDAO.getReports().then(function (res) {
            //     rl.navigationStates = res;
            // }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
            //     Mask.hide();
            // });
        };

        rl.layoutChanged = function () {
            if (rl.reportObj.configJson.layout && rl.reportObj.configJson.layout.includes("WithPagination")) {
                if (!rl.availableImplicitParameters) {
                    rl.availableImplicitParameters = ["#loggedInUser#"];
                }
                rl.availableImplicitParameters.push('#limit_offset#');
                this.searchOptionChanged();
            } else {
                if (!rl.availableImplicitParameters) {
                    rl.availableImplicitParameters = ["#loggedInUser#"];
                }
                if (rl.availableImplicitParameters.indexOf("#limit_offset#") >= 0) {
                    rl.availableImplicitParameters.splice(rl.availableImplicitParameters.indexOf("#limit_offset#"), 1)
                }
            }
        };

        rl.onDateRangeChanges = function (flag, str) {
            if (flag) {
                rl.newField.dateRangeOptions.push(str);
            } else {
                rl.newField.dateRangeOptions.splice(rl.dateRangeOptions.indexOf(str), 1);
            }
        };

        rl.searchOptionChanged = function(){
            if (rl.reportObj.configJson.layout && rl.reportObj.configJson.layout.includes("WithPagination")){
                if (rl.reportObj.configJson.isProvideSearch == true) {
                    rl.availableImplicitParameters.push('#searchstring#');
                }
                else{
                    if (rl.availableImplicitParameters.indexOf("#searchstring#") >= 0) {
                        rl.availableImplicitParameters.splice(rl.availableImplicitParameters.indexOf("#searchstring#"), 1)
                    }
                }
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('ReportLayoutController', ReportLayoutController);
})();
