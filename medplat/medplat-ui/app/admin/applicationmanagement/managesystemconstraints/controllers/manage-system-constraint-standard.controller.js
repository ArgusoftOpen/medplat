(function () {
    function ManageSystemConstraintStandard($state, Mask, toaster, GeneralUtil, $q, QueryDAO, SystemConstraintService) {
        let ctrl = this;
        let commonFieldValues = [
            {
                key: "label",
                valueType: "TEXT"
            }, {
                key: "tooltip",
                valueType: "TEXT"
            }, {
                key: "isHidden",
                valueType: "BOOLEAN"
            }
        ];
        let otherCommonFieldValue = [
            {
                key: "placeholder",
                valueType: "TEXT"
            },
            {
                key: "isRequired",
                valueType: "BOOLEAN"
            },
            {
                key: "requiredMessage",
                valueType: "TEXT"
            },
            {
                key: "isDisabled",
                valueType: "BOOLEAN"
            }
        ];
        ctrl.systemConstraintStandardConfig = {
            standardId: null,
            standardName: null,
            systemConstraintStandardFieldMasterDtos: []
        };
        ctrl.fieldValueMap = {
            "SHORT_TEXT": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "minLength",
                    valueType: "NUMBER"
                }, {
                    key: "maxLength",
                    valueType: "NUMBER"
                }
            ],
            "LONG_TEXT": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "minLength",
                    valueType: "NUMBER"
                }, {
                    key: "maxLength",
                    valueType: "NUMBER"
                }, {
                    key: "rows",
                    valueType: "NUMBER"
                }, {
                    key: "cols",
                    valueType: "NUMBER"
                }
            ],
            "DROPDOWN": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "isMultiple",
                    valueType: "BOOLEAN"
                }, {
                    key: "optionsType",
                    valueType: "DROPDOWN"
                }
            ],
            "CHECKBOX": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "staticOptions",
                    valueType: "JSON"
                }
            ],
            "RADIO": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "staticOptions",
                    valueType: "JSON"
                }
            ],
            "DATE": [
                ...commonFieldValues,
                ...otherCommonFieldValue
            ],
            "NUMBER": [
                ...commonFieldValues,
                ...otherCommonFieldValue,
                {
                    key: "isDecimalAllowed",
                    valueType: "BOOLEAN"
                }, {
                    key: "minLength",
                    valueType: "NUMBER"
                }, {
                    key: "maxLength",
                    valueType: "NUMBER"
                }, {
                    key: "min",
                    valueType: "NUMBER"
                }, {
                    key: "max",
                    valueType: "NUMBER"
                }
            ],
            "LOCATION_DIRECTIVE": [
                ...commonFieldValues, {
                    key: "fetchUptoLevel",
                    valueType: "NUMBER"
                }, {
                    key: "requiredUptoLevel",
                    valueType: "NUMBER"
                }, {
                    key: "fetchAccordingToUserAoi",
                    valueType: "BOOLEAN"
                }
            ]
        };
        ctrl.fieldValueDropdownOptionsByKey = {
            "optionsType": [
                {
                    key: 'staticOptions',
                    value: 'Static Options',
                    valueType: 'JSON'
                }, {
                    key: 'listValueField',
                    value: 'List Value Field Name',
                    valueType: 'TEXT'
                }, {
                    key: 'queryBuilder',
                    value: 'Query Builder Code',
                    valueType: 'TEXT'
                }
            ]
        };
        ctrl.keyNameMap = {
            label: 'Label Key',
            tooltip: 'Tooltip',
            isHidden: 'Is Hidden?',
            placeholder: 'Placeholder',
            isRequired: 'Is Required?',
            requiredMessage: 'Required Message',
            isDisabled: 'Is Disabled?',
            isDecimalAllowed: 'Is Decimal Allowed?',
            minLength: 'Min Length',
            maxLength: 'Max Length',
            min: 'Min',
            max: 'Max',
            rows: 'Rows',
            cols: 'Cols',
            isMultiple: 'Is Multiple?',
            optionsType: 'Options Type',
            staticOptions: 'Static Options',
            queryBuilder: 'Query Builder Code',
            listValueField: 'List Value Field Name',
            fetchUptoLevel: 'Fetch Upto Level?',
            requiredUptoLevel: 'Required Upto Level?',
            fetchAccordingToUserAoi: 'Fetch According To User AOI?'
        };
        ctrl.fieldValueMapByKey = {};
        for (const property in ctrl.fieldValueMap) {
            ctrl.fieldValueMapByKey[property] = {};
            ctrl.fieldValueMap[property].forEach((config, index) => ctrl.fieldValueMapByKey[property][config.key] = { ...config, order: index + 1 })
        }

        const _init = function () {
            ctrl.systemConstraintStandardId = $state.params.id ? Number($state.params.id) : null;
            if (ctrl.systemConstraintStandardId) {
                ctrl.editMode = true;
            }
            let promises = [];
            promises.push(QueryDAO.execute({
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'Categories of System Constraint Standard Fields'
                }
            }))
            if (ctrl.editMode) {
                promises.push(SystemConstraintService.getSystemConstraintStandardConfigById(ctrl.systemConstraintStandardId));
            }
            Mask.show();
            $q.all(promises).then(responses => {
                ctrl.systemConstraintStandardFieldCategories = responses[0].result;
                if (ctrl.editMode) {
                    ctrl.systemConstraintStandardConfig = responses[1];
                    _processFormConfig();
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        const _processFormFieldConfig = function (fieldMasterDto) {
            let standardName = ctrl.systemConstraintStandardConfig.standardName;
            fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.forEach(fieldValueMasterDto => {
                fieldValueMasterDto.overrideValue = false;
                fieldValueMasterDto.order = ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key] ? ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key].order : null;
                if (fieldValueMasterDto.value) {
                    fieldValueMasterDto.overrideValue = true;
                }
                if (fieldValueMasterDto.key === 'label' && !fieldValueMasterDto.defaultValue) {
                    fieldValueMasterDto.defaultValue = standardName ? `${standardName}_${fieldMasterDto.fieldKey || ''}` : null;
                }
            });
            let notPresentFieldValueKeys = _.difference(_.pluck(ctrl.fieldValueMap[fieldMasterDto.fieldType], "key"), _.pluck(fieldMasterDto.systemConstraintStandardFieldValueMasterDtos, "key"));
            notPresentFieldValueKeys.forEach(fieldValueKey => {
                let fieldValueConfig = ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueKey]
                fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.push({
                    key: fieldValueKey,
                    valueType: fieldValueConfig.valueType,
                    value: null,
                    defaultValue: fieldValueKey === 'label' && standardName ? `${standardName}_${fieldMasterDto.fieldKey || ''}` : null,
                    overrideValue: false,
                    order: fieldValueConfig.order
                })
            })
            fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.sort(function (a, b) {
                return a.order - b.order;
            });
            return fieldMasterDto;
        }

        const _processFormConfig = function () {
            if (!ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos || ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos.length === 0) {
                ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos = [];
                return;
            }

            ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos.forEach(fieldMasterDto => {
                if (!fieldMasterDto.systemConstraintStandardFieldValueMasterDtos || fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.length === 0) {
                    fieldMasterDto.systemConstraintStandardFieldValueMasterDtos = [];
                    ctrl.fieldValueMap[fieldMasterDto.fieldType].forEach(fieldConfig => fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.push({
                        key: fieldConfig.key,
                        valueType: fieldConfig.valueType,
                        value: null,
                        defaultValue: fieldConfig.key === 'label' && ctrl.systemConstraintStandardConfig.standardName ? `${ctrl.systemConstraintStandardConfig.standardName}_${fieldMasterDto.fieldKey || ''}` : null,
                        overrideValue: false
                    }))
                    return;
                }
                fieldMasterDto = _processFormFieldConfig(fieldMasterDto);
            });
        };

        ctrl.saveFieldConfig = function (index, fieldConfig) {
            ctrl.manageSystemConstraintStandardField.$setSubmitted();
            if (ctrl.manageSystemConstraintStandardField.$valid) {
                Mask.show();
                SystemConstraintService.createOrUpdateSystemConstraintStandardFieldConfig(fieldConfig).then(response => {
                    ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index] = _processFormFieldConfig(response);
                    ctrl.manageSystemConstraintStandardField.$setPristine();
                    toaster.pop("success", `Form Standard Field Config Saved Successfully.`);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        };

        ctrl.onFieldTypeChange = function (index, fieldType) {
            let fieldMasterDto = ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index];
            let fieldValueMasterDtos = ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index].systemConstraintStandardFieldValueMasterDtos;
            if (fieldValueMasterDtos && fieldValueMasterDtos.length) {
                fieldValueMasterDtos.forEach(fieldValueMasterDto => {
                    if (fieldValueMasterDto.uuid) {
                        if (!fieldMasterDto.standardFieldValueUuidsToBeRemoved) {
                            fieldMasterDto.standardFieldValueUuidsToBeRemoved = [];
                        }
                        fieldMasterDto.standardFieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                    }
                })
            }
            fieldMasterDto.systemConstraintStandardFieldValueMasterDtos = [];
            if (fieldType) {
                ctrl.fieldValueMap[fieldMasterDto.fieldType].forEach(fieldConfig => fieldMasterDto.systemConstraintStandardFieldValueMasterDtos.push({
                    key: fieldConfig.key,
                    valueType: fieldConfig.valueType,
                    value: null,
                    defaultValue: fieldConfig.key === 'label' && ctrl.systemConstraintStandardConfig.standardName ? `${ctrl.systemConstraintStandardConfig.standardName}_${fieldMasterDto.fieldKey || ''}` : null,
                    overrideValue: false
                }))
            }
        };

        ctrl.onFieldValueDropdownOptionChange = function (index, fieldValueConfig) {
            let fieldMasterDto = ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index];
            let fieldValueMasterDtos = ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index].systemConstraintStandardFieldValueMasterDtos;
            switch (fieldValueConfig.key) {
                case 'optionsType':
                    ctrl.fieldValueDropdownOptionsByKey[fieldValueConfig.key].forEach(option => {
                        if (fieldValueConfig.value === option.key || fieldValueConfig.defaultValue === option.key) {
                            if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === option.key)) {
                                fieldValueMasterDtos.push({
                                    key: option.key,
                                    valueType: option.valueType
                                })
                            }
                        } else {
                            fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                                if (fieldValueMasterDto.key !== option.key) {
                                    return true;
                                }
                                if (fieldValueMasterDto.uuid) {
                                    if (!fieldMasterDto.standardFieldValueUuidsToBeRemoved) {
                                        fieldMasterDto.standardFieldValueUuidsToBeRemoved = [];
                                    }
                                    fieldMasterDto.standardFieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                                }
                                return false;
                            });
                        }
                    })
                    ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos[index].systemConstraintStandardFieldValueMasterDtos = fieldValueMasterDtos;
                    break;
                default:
                    break;
            }
        };

        ctrl.onOverrideValueSelection = function (fieldValueConfig, fieldConfig) {
            if (!fieldValueConfig.overrideValue) {
                fieldValueConfig.value = null;
                if (fieldValueConfig.key === 'label') {
                    fieldValueConfig.enTranslationOfLabel = null;
                }
                return;
            }
            if (fieldValueConfig.key === 'label') {
                fieldValueConfig.value = ctrl.systemConstraintStandardConfig.standardName ? `${ctrl.systemConstraintStandardConfig.standardName}_${fieldConfig.fieldKey || ''}` : null;
            }
        };

        ctrl.onClickOfCollapsibleHeader = function (index, fieldConfig) {
            ctrl.systemConstraintStandardConfig.systemConstraintStandardFieldMasterDtos.forEach(fieldMasterDto => fieldMasterDto.showContent = false);
            if (!angular.element('#collapseConfiguration' + index).hasClass('show')) {
                fieldConfig.showContent = true;
            }
        };

        ctrl.goBack = function () {
            $state.go("techo.admin.systemConstraints", { selectedTab: 'manage-standard-configs' });
        }

        _init();
    }
    angular.module('imtecho.controllers').controller('ManageSystemConstraintStandard', ManageSystemConstraintStandard);
})();
