(function () {
    function ConfigureMobileTemplate(QueryDAO, $state, toaster, Mask, GeneralUtil, SystemConstraintService, $q) {
        let ctrl = this;

        const _filterAndsortFormFieldList = function (formFields) {
            if (Array.isArray(formFields) && formFields.length) {
                formFields = formFields.filter((formField) => {
                    if (formField.appName == 'TECHO_APP' || formField.appName == 'ALL') {
                        return true;
                    }
                    return false;
                })
                return formFields.sort((a, b) => {
                    let fieldNameA = a.fieldName.toUpperCase();
                    let fieldNameB = b.fieldName.toUpperCase();
                    if (fieldNameA < fieldNameB) {
                        return -1;
                    }
                    if (fieldNameA > fieldNameB) {
                        return 1;
                    }
                    return 0;
                });
            }
        }

        const _init = function () {
            ctrl.editMode = false;
            ctrl.formMasterUuid = $state.params.uuid || null;
            if (ctrl.formMasterUuid) {
                ctrl.editMode = true;
            } else {
                ctrl.goBack();
                return;
            }
            if (ctrl.editMode) {
                Mask.show();
                SystemConstraintService.getSystemConstraintFormConfigByUuid(ctrl.formMasterUuid).then(responses => {
                    ctrl.systemConstraintFormMasterDto = responses.systemConstraintFormMasterDto;
                    ctrl.formFieldList = _filterAndsortFormFieldList(responses.systemConstraintFieldMasterDtos);
                    try {
                        ctrl.mobileTemplateConfig = ctrl.systemConstraintFormMasterDto.mobileTemplateConfig ? JSON.parse(ctrl.systemConstraintFormMasterDto.mobileTemplateConfig) : [];
                        ctrl.mobileTemplateConfig.forEach((page, pageIndex) => {
                            if (Array.isArray(page.questions) && page.questions.length) {
                                page.questions.forEach((question, questionIndex) => {
                                    if (question.isEndForm) {
                                        ctrl.lastEndFormPageIndex = pageIndex;
                                        ctrl.lastEndFormQuestionIndex = questionIndex
                                    }
                                    let fieldIndex = ctrl.formFieldList.findIndex(element => element.uuid === question.uuid);
                                    if (fieldIndex !== -1) {
                                        ctrl.formFieldList[fieldIndex].isAddedInMobileTemplate = true;
                                    }
                                });
                            }
                        });
                    } catch (error) {
                        ctrl.mobileTemplateConfig = [];
                        toaster.pop('error', 'Error while parsing JSON');
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        };

        ctrl.addPage = () => {
            ctrl.mobileTemplateConfig.push({
                questions: []
            });
        }

        ctrl.removePage = (pageIndex) => {
            if (Array.isArray(ctrl.mobileTemplateConfig[pageIndex].questions) && ctrl.mobileTemplateConfig[pageIndex].questions.length) {
                ctrl.mobileTemplateConfig[pageIndex].questions.forEach(question => {
                    let fieldIndex = ctrl.formFieldList.findIndex(element => element.uuid === question.uuid);
                    ctrl.formFieldList[fieldIndex].isAddedInMobileTemplate = false;
                });
            }
            ctrl.mobileTemplateConfig.splice(pageIndex, 1);
        }

        ctrl.addQuestion = (field) => {
            ctrl.mobileTemplateConfig.forEach((page, index) => {
                let mobileTemplateConfigFieldIndex = page.questions.findIndex(element => element.uuid === field.uuid);
                if (mobileTemplateConfigFieldIndex !== -1) {
                    field.isAddedInMobileTemplate = true;
                    ctrl.fieldChanged(ctrl.mobileTemplateConfig[index].questions[mobileTemplateConfigFieldIndex]);
                    return;
                }
            })
        }

        ctrl.removeQuestion = (pageIndex, questionIndex) => {
            let fieldIndex = ctrl.formFieldList.findIndex(element => element.uuid === ctrl.mobileTemplateConfig[pageIndex].questions[questionIndex].uuid);
            ctrl.formFieldList[fieldIndex].isAddedInMobileTemplate = false;
            ctrl.mobileTemplateConfig[pageIndex].questions.splice(questionIndex, 1);
        }

        ctrl.fieldChanged = (field) => {
            field.selectedFieldIndex = ctrl.formFieldList.findIndex(element => element.uuid === field.uuid);
            if (ctrl.formFieldList[field.selectedFieldIndex].fieldType === 'DROPDOWN') {
                field.nextFieldBy = null;
            } else {
                field.nextFieldBy = 'BY_FIELD';
            }
            ctrl.nextFieldByChanged(field);
        }

        ctrl.nextFieldByChanged = (field) => {
            field.selectedFieldIndex = ctrl.formFieldList.findIndex(element => element.uuid === field.uuid);
            field.nextField = null;
            field.nextFieldJson = null;
            if (field.nextFieldBy === 'BY_OPTIONS') {
                ctrl.formFieldList[field.selectedFieldIndex].systemConstraintFieldValueMasterDtos.forEach((fieldValue) => {
                    if (fieldValue.key === 'staticOptions') {
                        try {
                            field.nextFieldJson = JSON.parse(fieldValue.defaultValue);
                        } catch (error) {
                            toaster.pop('error', `Error parsing JSON for field ${ctrl.formFieldList[field.selectedFieldIndex].fieldName}`);
                        }
                    } else if (fieldValue.key === 'listValueField') {
                        Mask.show();
                        QueryDAO.execute({
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: fieldValue.defaultValue
                            }
                        }).then((response) => {
                            try {
                                field.nextFieldJson = response.result.map((element) => {
                                    return {
                                        key: element.id,
                                        value: element.value
                                    }
                                });
                            } catch (error) {
                                toaster.pop('error', `Error parsing JSON for field ${ctrl.formFieldList[field.selectedFieldIndex].fieldName}`);
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        });
                    }
                });
            }
        }

        ctrl.saveConfiguration = () => {
            ctrl.mobileTemplateConfigForm.$setSubmitted();
            if (ctrl.mobileTemplateConfigForm.$valid) {
                ctrl.mobileTemplateConfig.forEach(page => {
                    page.questions.forEach(question => {
                        question.field = question.uuid
                    });
                })
                ctrl.systemConstraintFormMasterDto.mobileTemplateConfig = JSON.stringify(ctrl.mobileTemplateConfig);
                Mask.show();
                SystemConstraintService.createOrUpdateSystemConstraintForm(ctrl.systemConstraintFormMasterDto,'MOBILE',).then(() => {
                    ctrl.goBack();
                    toaster.pop("success", `Mobile Dynamic Template Configured Successfully.`);
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            }
        }

        ctrl.goBack = function () {
            $state.go("techo.admin.systemConstraints", { selectedTab: 'manage-form-configs' });
        };
        ctrl.updateEndForm = function (pageIndex, questionIndex, value) {
            if (value == true) {
                if (ctrl.lastEndFormPageIndex != null 
                    && ctrl.lastEndFormQuestionIndex != null 
                    && (ctrl.lastEndFormPageIndex != pageIndex ||
                     ctrl.lastEndFormQuestionIndex !=questionIndex)) {
                    ctrl.mobileTemplateConfig[ctrl.lastEndFormPageIndex]
                        .questions[ctrl.lastEndFormQuestionIndex].isEndForm = false;
                }
                ctrl.lastEndFormPageIndex = pageIndex;
                ctrl.lastEndFormQuestionIndex = questionIndex;
            }
        }
        ctrl.isHidden = function (field) {
            let isHidden = false;
            if(field.systemConstraintFieldValueMasterDtos){
                for (let systemConstraintFieldValueMasterDto of field.systemConstraintFieldValueMasterDtos) {
                    if (systemConstraintFieldValueMasterDto.key == 'isHidden'
                        && systemConstraintFieldValueMasterDto.defaultValue == "true") {
                            isHidden= true;
                            break;
                    }
                };
            }
            return isHidden;
        }
        $(window).on("scroll", function () {
            $("#allFields").stop().animate({ "marginTop": ($(window).scrollTop()) + "px", "marginLeft": ($(window).scrollLeft()) + "px" }, "slow");
        });

        _init();
    }
    angular.module('imtecho.controllers').controller('ConfigureMobileTemplate', ConfigureMobileTemplate);
})();
