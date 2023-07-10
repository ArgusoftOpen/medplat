(function () {
    function ConfigureDynamicTemplate(QueryDAO, $state, toaster, Mask, GeneralUtil, SystemConstraintService, $q) {
        let ctrl = this;
        const defaultConfigObject = {
            cssStyles: null,
            cssClasses: null,
            isRepeatable: false,
            showAddRemoveButton: false,
            ngModel: null,
            isConditional: false,
            ngIf: null
        };

        const _prepareFormFieldList = function (formFields) {
            return formFields.map(field => ({
                type: "TECHO_FORM_FIELD",
                fieldKey: field.fieldKey,
                name: field.fieldName,
                fieldType: field.fieldType
            })).sort((a, b) => {
                let fieldKeyA = a.fieldKey.toUpperCase();
                let fieldKeyB = b.fieldKey.toUpperCase();
                if (fieldKeyA < fieldKeyB) {
                    return -1;
                }
                if (fieldKeyA > fieldKeyB) {
                    return 1;
                }
                return 0;
            });
        }

        const _init = function () {
            ctrl.editMode = false;
            ctrl.formMasterUuid = $state.params.uuid ? $state.params.uuid : null;
            if (ctrl.formMasterUuid) {
                ctrl.editMode = true;
            } else {
                ctrl.goBack();
                return;
            }
            if (ctrl.editMode) {
                let promises = [];
                let dtoList = [];
                let getMenuItemsDto = {
                    code: 'get_all_menus_for_system_constraint_form_config',
                    parameters: {},
                    sequence: 1
                };
                dtoList.push(getMenuItemsDto);
                promises.push(QueryDAO.executeAll(dtoList));
                promises.push(SystemConstraintService.getSystemConstraintFormByUuid(ctrl.formMasterUuid));
                promises.push(SystemConstraintService.getSystemConstraintFieldsByFormMasterUuid(ctrl.formMasterUuid));
                Mask.show();
                $q.all(promises).then(responses => {
                    ctrl.menuItems = responses[0][0].result;
                    ctrl.systemConstraintFormMasterDto = responses[1];
                    ctrl.formFieldList = _prepareFormFieldList(responses[2]);
                    try {
                        ctrl.webTemplateConfig = ctrl.systemConstraintFormMasterDto.webTemplateConfig ? JSON.parse(ctrl.systemConstraintFormMasterDto.webTemplateConfig) : [];
                    } catch (error) {
                        ctrl.webTemplateConfig = [];
                        console.error('Error while parsing JSON ::: ', error);
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        };

        ctrl.addWebTemplateComponent = function (componentType) {
            let obj;
            switch (componentType) {
                case 'Card':
                    obj = {
                        "type": 'CARD',
                        "config": {
                            "title": null,
                            ...defaultConfigObject,
                            isCollapsible: false
                        },
                        "elements": []
                    };
                    break;
                case 'Row':
                    obj = {
                        "type": 'ROW',
                        "config": {
                            "size": "12",
                            ...defaultConfigObject
                        },
                        "elements": []
                    };
                    break;
                case 'Col':
                default:
                    break;
            }
            ctrl.webTemplateConfig.push(obj);
        }

        const _addChildWebComponent = function (childWebComponent, elements) {
            switch (childWebComponent.type) {
                case 'CARD':
                    elements.push({
                        "type": childWebComponent.type,
                        "config": {
                            "title": null,
                            ...defaultConfigObject,
                            isCollapsible: false
                        },
                        "elements": []
                    })
                    break;

                case 'ROW':
                    elements.push({
                        "type": childWebComponent.type,
                        "config": {
                            "size": childWebComponent.size,
                            ...defaultConfigObject
                        },
                        "elements": []
                    })
                    break;

                case 'COL':
                    let childElements = [];
                    for (let index = 1; index <= 12; index += Number(childWebComponent.size)) {
                        childElements.push({
                            "type": null,
                            "config": {},
                            "elements": []
                        })
                    }
                    elements.push({
                        "type": childWebComponent.type,
                        "config": {
                            "size": childWebComponent.size,
                            ...defaultConfigObject
                        },
                        "elements": childElements
                    })
                    break;

                default:
                    break;
            }
        }

        ctrl.onChildWebComponentChanged = function (childWebComponent, elements, element, parentComponentType) {
            if (!childWebComponent)
                return;

            switch (parentComponentType) {
                case 'CARD':
                case 'ROW':
                    _addChildWebComponent(childWebComponent, elements);
                    break;

                case 'COL':
                    element.type = childWebComponent.type;
                    element.elements = [];
                    switch (element.type) {
                        case 'CARD':
                            element.config = {
                                "title": null,
                                ...defaultConfigObject,
                                isCollapsible: false
                            }

                            break;
                        case 'ROW':
                            element.config = {
                                "size": childWebComponent.size,
                                ...defaultConfigObject
                            }
                            break;

                        case 'COL':
                            let childElements = [];
                            for (let index = 1; index <= 12; index += Number(childWebComponent.size)) {
                                childElements.push({
                                    "type": null,
                                    "config": {},
                                    "elements": []
                                })
                            }
                            element.config = {
                                "size": childWebComponent.size,
                                ...defaultConfigObject
                            }
                            element.elements = childElements;
                            break;

                        case 'CUSTOM_HTML':
                            element.config = {
                                "html": null
                            }
                            break;

                        case 'TECHO_FORM_FIELD':
                            element.config = {
                                "fieldKey": childWebComponent.fieldKey,
                                "fieldName": childWebComponent.name,
                                "fieldType": childWebComponent.fieldType,
                                ...defaultConfigObject
                            }
                            break;

                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }

        ctrl.save = function () {
            ctrl.manageWebDynamicTemplate.$setSubmitted();
            if (ctrl.manageWebDynamicTemplate.$valid) {
                Mask.show();
                ctrl.systemConstraintFormMasterDto.webTemplateConfig = angular.toJson(ctrl.webTemplateConfig);
                SystemConstraintService.createOrUpdateSystemConstraintForm(ctrl.systemConstraintFormMasterDto,'WEB').then(() => {
                    ctrl.goBack();
                    toaster.pop("success", `Web Dynamic Template Configured Successfully.`);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        }

        ctrl.goBack = function () {
            $state.go("techo.admin.systemConstraints", { selectedTab: 'manage-form-configs' });
        };

        _init();
    }
    angular.module('imtecho.controllers').controller('ConfigureDynamicTemplate', ConfigureDynamicTemplate);
})();
