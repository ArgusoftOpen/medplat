(function () {
    function ManageSystemConstraintForm($state, Mask, toaster, GeneralUtil, $q, QueryDAO, SystemConstraintService) {
        let ctrl = this;
        ctrl.fieldConfigPlatform="BOTH";
        ctrl.fieldTypes = [{
            key: 'SHORT_TEXT',
            value: 'Short Text (ALL)'
        }, {
            key: 'LONG_TEXT',
            value: 'Long Text (ALL)'
        }, {
            key: 'DROPDOWN',
            value: 'Dropdown (ALL)'
        }, {
            key: 'CHECKBOX',
            value: 'Checkbox (ALL)'
        }, {
            key: 'RADIO',
            value: 'Radio (ALL)'
        }, {
            key: 'DATE',
            value: 'Date (ALL)'
        }, {
            key: 'TIME',
            value: 'Time (ALL)'
        }, {
            key: 'NUMBER',
            value: 'Number (ALL)'
        }, {
            key: 'PASSWORD',
            value: 'Password (WEB)'
        }, {
            key: 'LOCATION_DIRECTIVE',
            value: 'Location Directive (WEB)'
        }, {
            key: 'ADDED_LOCATIONS',
            value: 'Added Locations (WEB)'
        }, {
            key: 'INFORMATION_DISPLAY',
            value: 'Information Display (ALL)'
        }, {
            key: 'INFORMATION_TEXT',
            value: 'Information Text(WEB)'
        }, {
            key: 'CUSTOM_HTML',
            value: 'Custom HTML (WEB)'
        }, {
            key: 'CUSTOM_COMPONENT',
            value: 'Custom Component (WEB)'
        }, {
            key: 'TABLE',
            value: 'Table (WEB)'
        }, {
            key: 'BUTTON',
            value: 'Button (WEB)'
        }, {
            key: 'FILE_UPLOAD',
            value: 'File Upload (WEB)'
        }, {
            key: 'CB',
            value: 'COMBO (TECHO_APP)'
        }, {
            key: 'L',
            value: 'Label (TECHO_APP)'
        }, {
            key: 'AB',
            value: 'Age Box (TECHO_APP)'
        }, {
            key: 'AD',
            value: 'Age Box with Display (TECHO_APP)'
        }, {
            key: 'TBWA',
            value: 'Textbox with audio (TECHO_APP)'
        }, {
            key: 'TBCL',
            value: 'Textbox Change listener (TECHO_APP)'
        }, {
            key: 'PP',
            value: 'Photopicker (TECHO_APP)'
        }, {
            key: 'SV',
            value: 'Show video (TECHO_APP)'
        }, {
            key: 'SVM',
            value: 'Show video mandatory (TECHO_APP)'
        }, {
            key: 'WB',
            value: 'Weight box (TECHO_APP)'
        }, {
            key: 'WD',
            value: 'Weight box display (TECHO_APP)'
        }, {
            key: 'TEMB',
            value: 'Temperature box (TECHO_APP)'
        }, {
            key: 'LF',
            value: 'Label formula (TECHO_APP)'
        }, {
            key: 'MS',
            value: 'Multiselect (TECHO_APP)'
        }, {
            key: 'BPM',
            value: 'Blood pressure measurement (TECHO_APP)'
        }, {
            key: 'PA',
            value: 'Play audio (TECHO_APP)'
        }, {
            key: 'CALL',
            value: 'Call (TECHO_APP)'
        }, {
            key: 'ML',
            value: 'Members list component (TECHO_APP)'
        }, {
            key: 'MFN',
            value: 'Member full name component (TECHO_APP)'
        }, {
            key: 'MCR',
            value: 'Member child relationship (TECHO_APP)'
        }, {
            key: 'HWR',
            value: 'Husband wife relationship (TECHO_APP)'
        }, {
            key: 'OS',
            value: 'Oral screening component (TECHO_APP)'
        }, {
            key: 'BS',
            value: 'Breast screening component (TECHO_APP)'
        }, {
            key: 'BMI',
            value: 'BMI component (TECHO_APP)'
        }, {
            key: 'HIC',
            value: 'Health infrastructure component (TECHO_APP)'
        }, {
            key: 'CGCC',
            value: 'Child growth chart component (TECHO_APP)'
        }, {
            key: 'IGC',
            value: 'Immunisation given component (TECHO_APP)'
        }, {
            key: 'SC',
            value: 'School component (TECHO_APP)'
        },{
            key: 'SCB',
            value: 'Single Checkbox component (TECHO_APP)'
        },{
            key: 'OBVC',
            value: 'OTP based verification component (TECHO_APP)'
        },{
            key: 'NDPC',
            value: 'NDHM data push conf component (TECHO_APP)'
        },{
            key: 'FSC',
            value: 'Form submission component (TECHO_APP)'
        },{
            key: 'ORDT',
            value: 'ORDT component (TECHO_APP)'
        },{
            key: 'HMC',
            value: 'Health ID management component (TECHO_APP)'
        },{
            key: 'LIC',
            value: 'List in color component (TECHO_APP)'
        },{
            key: 'RBD',
            value: 'Vaccinations type component (TECHO_APP)'
        },{
            key: 'MDC',
            value: 'Member Details component (TECHO_APP)'
        },{
            key: 'SRBD',
            value: 'Simple radio date component (TECHO_APP)'
        },{
            key: 'CTB',
            value: 'Checkbox textbox component (TECHO_APP)'
        },{
            key: 'QRS',
            value: 'QR scan component (TECHO_APP)'
        }];
        ctrl.allFields = ["SHORT_TEXT", "LONG_TEXT", "NUMBER", "DROPDOWN", "CHECKBOX", "RADIO", "DATE", "TIME", "INFORMATION_DISPLAY"];
        ctrl.onlyWebFields = ["PASSWORD", "LOCATION_DIRECTIVE", "ADDED_LOCATIONS", "CUSTOM_HTML", "CUSTOM_COMPONENT", "TABLE", "BUTTON", "FILE_UPLOAD", "INFORMATION_TEXT"];
        ctrl.onlyTechoAppFields = ["L","CB", "AB", "AD", "TBWA", "TBCL", "PP", "SV", "SVM", "WB", "WD", "TEMB", "LF", "MS", "BPM", "PA", "CALL", "ML", "MFN", "MCR", "HWR", "OS", "BS", "BMI", "HIC", "CGCC", "IGC", "SC","SCB","OBVC","NDPC","FSC","ORDT","HMC","LIC","RBD","MDC","SRBD","CTB","QRS"];
        let option = {
            type: "",
            operator: "",
            value1: "",
            value2: "",
            fieldName: "",
            queryCode: ""
        }
        let event = [{
            type: "",
            value: ""
        }];

        ctrl.systemConstraintFormConfig = {
            systemConstraintFormMasterDto: {},
            systemConstraintFieldMasterDtos: []
        };
        ctrl.fieldValueDropdownOptionsByKey = {
            "mobileEvent": [{
                key: 'SAVE',
                value: 'Save',
                valueType: 'TEXT',
                required: true
            }, {
                key: 'SAVEFORM',
                value: 'Save Form',
                valueType: 'TEXT',
                required: true
            },
            {
                key: 'LOOP',
                value: 'Loop',
                valueType: 'TEXT',
                required: true
            },
            {
                key: 'DEFAULTLOOP',
                value: 'DefaultLoop',
                valueType: 'TEXT',
                required: true
            },
            {
                key: 'SUBMIT',
                value: 'Submit',
                valueType: 'TEXT',
                required: true
            },
            {
                key: 'OKAY',
                value: 'Okay',
                valueType: 'TEXT',
                required: true
            }],
            "optionsType": [{
                key: 'staticOptions',
                value: 'Static Options',
                valueType: 'JSON',
                required: true
            }, {
                key: 'listValueField',
                value: 'List Value Field Name',
                valueType: 'TEXT',
                required: true
            }, {
                key: 'queryBuilder',
                value: 'Query Builder Code',
                valueType: 'TEXT',
                required: true
            }],
            "templateType": [{
                key: 'twoPart',
                value: 'Two Part',
                valueType: 'TEXT'
            }, {
                key: 'inline',
                value: 'Inline',
                valueType: 'TEXT'
            }],
            "displayType": [{
                key: 'text',
                value: 'Text',
                valueType: 'TEXT'
            }, {
                key: 'boolean',
                value: 'Boolean',
                valueType: 'TEXT'
            }, {
                key: 'date',
                value: 'Date',
                valueType: 'TEXT'
            }, {
                key: 'dateAndTime',
                value: 'Date and Time',
                valueType: 'TEXT'
            }, {
                key: 'gender',
                value: 'Gender',
                valueType: 'TEXT'
            }, {
                key: 'familyPlanning',
                value: 'Family Planning',
                valueType: 'TEXT'
            }, {
                key: 'sdScore',
                value: 'SD Score',
                valueType: 'TEXT'
            }],
            "buttonType": [{
                key: 'button',
                value: 'Button',
                valueType: 'TEXT'
            }, {
                key: 'submit',
                value: 'Submit',
                valueType: 'TEXT'
            }, {
                key: 'reset',
                value: 'Reset',
                valueType: 'TEXT'
            }],
            "buttonLabelType": [{
                key: 'text',
                value: 'Label with text',
                valueType: 'TEXT'
            }, {
                key: 'icon',
                value: 'Label with icon',
                valueType: 'TEXT'
            }, {
                key: 'textAndIcon',
                value: 'Label with text and icon',
                valueType: 'TEXT'
            }],
            "fileUploadType": [{
                key: 'IMAGE',
                value: 'Image',
                valueType: 'TEXT'
            }, {
                key: 'VIDEO',
                value: 'Video',
                valueType: 'TEXT'
            }, {
                key: 'AUDIO',
                value: 'Audio',
                valueType: 'TEXT'
            }, {
                key: 'OTHER',
                value: 'Other',
                valueType: 'TEXT'
            }]
        };
        ctrl.keyNameMap = {
            label: 'Label Key',
            tooltip: 'Tooltip',
            isHidden: 'Is Hidden?',
            placeholder: 'Placeholder',
            isRequired: 'Is Required?',
            requiredMessage: 'Required Message',
            isDisabled: 'Is Disabled?',
            minLength: 'Min Length',
            maxLength: 'Max Length',
            min: 'Min',
            max: 'Max',
            minDateField: 'Min Date Field',
            maxDateField: 'Max Date Field',
            rows: 'Rows',
            cols: 'Cols',
            isMultiple: 'Is Multiple?',
            isBoolean: 'Is Boolean?',
            optionsType: 'Options Type',
            staticOptions: 'Static Options',
            listValueField: 'List Value Field Name',
            additionalStaticOptionsRequired: 'Additional Static Options Required?',
            queryBuilder: 'Query Builder Code',
            fetchUptoLevel: 'Fetch Upto Level?',
            requiredUptoLevel: 'Required Upto Level?',
            fetchAccordingToUserAoi: 'Fetch According To User AOI?',
            mobileQuestionNumber: 'Question Number (Mobile)',
            mobileTitle: 'Title (Mobile)',
            mobileSubtitle: 'Sub Title (Mobile)',
            mobileInstruction: 'Instruction (Mobile)',
            mobileValidation: 'Validation (Mobile)',
            mobileFormulas: 'Formulas (Mobile)',
            mobileDataMap: 'DataMap (Mobile)',
            mobileRelatedPropertyName: 'Related Property Name (Mobile)',
            mobileBinding: 'Mobile Binding (Mobile)',
            mobileTooltip: 'Tooltip Mobile (Mobile)',
            mobileHelpVideoField: 'Help Video Field (Mobile)',
            mobileEvent: 'Event (Mobile)',
            mobileHint: 'Hint (Mobile)',
            customHTML: 'Custom HTML (Mobile)',
            templateType: 'Template Type',
            displayValue: 'Display Value',
            displayType: 'Display Type',
            tableObject: 'Table Object',
            tableConfig: 'Table Config',
            hasPattern: 'Has Pattern?',
            pattern: 'Pattern',
            patternMessage: 'Pattern Message',
            buttonType: 'Button Type',
            buttonLabelType: 'Button Label Type',
            cssStyle: 'CSS Style',
            cssClass: 'CSS Class',
            icon: 'Icon',
            fileUploadType: 'File Upload Type',
            allowedExtensions: 'Allowed Extensions (comma separated)',
            isClickable: 'Is Clickable',
            relativeFilePath: 'Relative File Path'
        };

        ctrl.addEvent = (fieldConfig) => {
            if (fieldConfig.events) {
                fieldConfig.events.push({ ...event });
            } else {
                fieldConfig.events = [{ ...event }];
            }
        }

        ctrl.deleteEvent = (fieldConfig, index) => {
            fieldConfig.events.splice(index, 1);
        }

        ctrl.addCondition = (array, fieldConfig) => {
            fieldConfig[array].conditions.options.push({ ...option });
        }

        ctrl.addGroup = (array, fieldConfig, rule) => {
            if (fieldConfig[array] && fieldConfig[array].conditions) {
                fieldConfig[array].conditions.options.push({
                    conditions: {
                        rule,
                        options: [{ ...option }]
                    }
                })
            } else {
                fieldConfig[array] = {
                    conditions: {
                        rule,
                        options: [{ ...option }]
                    }
                }
            }
        }

        ctrl.invertGroup = (array, fieldConfig) => {
            fieldConfig[array].conditions.rule = fieldConfig[array].conditions.rule === 'AND' ? 'OR' : 'AND';
        }

        ctrl.deleteGroup = (array, fieldConfig) => {
            delete fieldConfig[array];
        }

        ctrl.configurationTypeChanged = (changedOption) => {
            changedOption.value1 = "";
            changedOption.value2 = "";
            changedOption.queryCode = "";
            changedOption.fieldName = "";
        }

        ctrl.configurationOperatorChanged = (changedOption) => {
            changedOption.value2 = "";
        }

        ctrl.modifyfieldConfigUi = (fieldConfig, configurations) => {
            if (Array.isArray(configurations) && configurations.length) {
                if (configurations.includes('WEB') && configurations.includes('TECHO_APP')) {
                    fieldConfig.systemConstraintFieldValueMasterDtos.map((element) => {
                        element.showInUI = true;
                    });
                } else if (configurations.includes('WEB')) {
                    fieldConfig.systemConstraintFieldValueMasterDtos.map((element) => {
                        element.showInUI = ctrl.webConfigurationFields.includes(element.key);
                    });
                } else if (configurations.includes('TECHO_APP')) {
                    fieldConfig.systemConstraintFieldValueMasterDtos.map((element) => {
                        element.showInUI = ctrl.mobileConfigurationFields.includes(element.key);
                    });
                }
            } else {
                fieldConfig.systemConstraintFieldValueMasterDtos.map((element) => {
                    element.showInUI = false;
                });
            }
        }

        ctrl.webConfigurationChanged = (fieldConfig) => {
            if (fieldConfig.webConfiguration && fieldConfig.mobileConfiguration) {
                fieldConfig.bothConfiguration = true;
                ctrl.modifyfieldConfigUi(fieldConfig, ['WEB', 'TECHO_APP']);
            } else if (fieldConfig.webConfiguration) {
                ctrl.modifyfieldConfigUi(fieldConfig, ['WEB']);
            } else {
                ctrl.modifyfieldConfigUi(fieldConfig, []);
            }
        }

        ctrl.mobileConfigurationChanged = (fieldConfig) => {
            if (fieldConfig.webConfiguration && fieldConfig.mobileConfiguration) {
                fieldConfig.bothConfiguration = true;
                ctrl.modifyfieldConfigUi(fieldConfig, ['WEB', 'TECHO_APP']);
            } else if (fieldConfig.mobileConfiguration) {
                ctrl.modifyfieldConfigUi(fieldConfig, ['TECHO_APP']);
            } else {
                ctrl.modifyfieldConfigUi(fieldConfig, []);
            }
        }

        ctrl.bothConfigurationChanged = (fieldConfig) => {
            if (fieldConfig.bothConfiguration) {
                fieldConfig.webConfiguration = true;
                fieldConfig.mobileConfiguration = true;
                ctrl.modifyfieldConfigUi(fieldConfig, ['WEB', 'TECHO_APP']);
            } else if (!fieldConfig.bothConfiguration) {
                fieldConfig.webConfiguration = false;
                fieldConfig.mobileConfiguration = false;
                ctrl.modifyfieldConfigUi(fieldConfig, []);
            }
        }

        const _init = function () {
            if ($state.current.name === 'techo.admin.manageSystemConstraintForm') {
                ctrl.generateFieldValueMap();
                ctrl.systemConstraintFormUuid = $state.params.uuid || null;
                if (ctrl.systemConstraintFormUuid) ctrl.editMode = true;
                ctrl.manageSingleFormConfig = true;
                ctrl.getSystemConstraintFormConfig();
            } else if ($state.current.name === 'techo.admin.manageSystemConstraintForms') {
                ctrl.menuConfigId = $state.params.menuConfigId ? Number($state.params.menuConfigId) : null;
                ctrl.manageSingleFormConfig = false;
                ctrl.selectedFormUuid = null;
                ctrl.getSystemConstraintFormsByMenuConfigId();
            }
        };

        const _processFormFieldConfig = function (fieldMasterDto) {
            fieldMasterDto.systemConstraintFieldValueMasterDtos.forEach(fieldValueMasterDto => {
                fieldValueMasterDto.overrideValue = false;
                fieldValueMasterDto.order = ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key] ? ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key].order : null;
                fieldValueMasterDto.required = ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key] ? ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueMasterDto.key].required : false;
                if (fieldValueMasterDto.value) {
                    fieldValueMasterDto.overrideValue = true;
                }
                switch (fieldValueMasterDto.key) {
                    case 'events':
                        let events = JSON.parse(fieldValueMasterDto.defaultValue);
                        fieldMasterDto.events = _.isEmpty(events) ? null : events;
                        break;
                    case 'visibility':
                        let visibility = JSON.parse(fieldValueMasterDto.defaultValue);
                        fieldMasterDto.visibility = _.isEmpty(visibility) ? null : visibility;
                        break;
                    case 'requirable':
                        let requirable = JSON.parse(fieldValueMasterDto.defaultValue);
                        fieldMasterDto.requirable = _.isEmpty(requirable) ? null : requirable;
                        break;
                    case 'disability':
                        let disability = JSON.parse(fieldValueMasterDto.defaultValue);
                        fieldMasterDto.disability = _.isEmpty(disability) ? null : disability;
                        break;
                }
            });
            let notPresentFieldValueKeys = _.difference(_.pluck(ctrl.fieldValueMap[fieldMasterDto.fieldType], "key"), _.pluck(fieldMasterDto.systemConstraintFieldValueMasterDtos, "key"));
            notPresentFieldValueKeys.forEach(fieldValueKey => {
                let fieldValueConfig = ctrl.fieldValueMapByKey[fieldMasterDto.fieldType][fieldValueKey]
                fieldMasterDto.systemConstraintFieldValueMasterDtos.push({
                    key: fieldValueKey,
                    valueType: fieldValueConfig.valueType,
                    value: null,
                    defaultValue: null,
                    overrideValue: false,
                    order: fieldValueConfig.order,
                    required: fieldValueConfig.required
                })
            })
            fieldMasterDto.systemConstraintFieldValueMasterDtos.sort((a, b) => (a.order === null) - (b.order === null) || (a.order - b.order));
            switch (fieldMasterDto.appName) {
                case 'ALL':
                    fieldMasterDto.bothConfiguration = fieldMasterDto.webConfiguration = fieldMasterDto.mobileConfiguration = true;
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['WEB', 'TECHO_APP']);
                    break;
                case 'WEB':
                    fieldMasterDto.webConfiguration = true;
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['WEB']);
                    break;
                case 'TECHO_APP':
                    fieldMasterDto.mobileConfiguration = true;
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['TECHO_APP']);
                    break;
            }
            return fieldMasterDto;
        }

        const _processFormConfig = function () {
            if (!ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos || ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.length === 0) {
                ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos = [];
                return;
            }
            ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.forEach(fieldMasterDto => {
                fieldMasterDto = _processFormFieldConfig(fieldMasterDto);
            })
        };

        ctrl.getSystemConstraintFormConfig = function () {
            let promises = [];
            let dtoList = [];
            let getMenuItemsDto = {
                code: 'get_all_menus_for_system_constraint_form_config',
                parameters: {},
                sequence: 1
            };
            dtoList.push(getMenuItemsDto);
            promises.push(QueryDAO.executeAll(dtoList));
            promises.push(SystemConstraintService.getSystemConstraintStandardFields(true));
            if (ctrl.editMode) {
                promises.push(SystemConstraintService.getSystemConstraintFormConfigByUuid(ctrl.systemConstraintFormUuid));
            }
            Mask.show();
            $q.all(promises).then(responses => {
                ctrl.menuItems = responses[0][0].result;
                ctrl.SystemConstraintStandardFields = responses[1];
                if (ctrl.editMode) {
                    ctrl.systemConstraintFormConfig = responses[2];
                    _processFormConfig();
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        ctrl.getSystemConstraintFormConfigByUuid = function () {
            Mask.show();
            SystemConstraintService.getSystemConstraintFormConfigByUuid(ctrl.systemConstraintFormUuid).then(response => {
                ctrl.systemConstraintFormConfig = response;
                ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto.menuConfigId = ctrl.menuConfigId;
                _processFormConfig();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        ctrl.getSystemConstraintFormsByMenuConfigId = function () {
            let promises = [];
            let dtoList = [];
            let getMenuItemsDto = {
                code: 'get_all_menus_for_system_constraint_form_config',
                parameters: {},
                sequence: 1
            };
            dtoList.push(getMenuItemsDto);
            promises.push(QueryDAO.executeAll(dtoList));
            promises.push(SystemConstraintService.getSystemConstraintStandardFields(true));
            promises.push(SystemConstraintService.getSystemConstraintFormsByMenuConfigId(ctrl.menuConfigId));
            Mask.show();
            $q.all(promises).then(responses => {
                ctrl.menuItems = responses[0][0].result;
                ctrl.SystemConstraintStandardFields = responses[1];
                ctrl.systemConstraintForms = responses[2];
                ctrl.systemConstraintForms.unshift({
                    uuid: 'NEW',
                    formName: '--Add New Form--'
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        ctrl.onFeatureMenuSelection = function () {
            $state.go('.', { menuConfigId: ctrl.menuConfigId }, { notify: false });
            Mask.show();
            SystemConstraintService.getSystemConstraintFormsByMenuConfigId(ctrl.menuConfigId).then(response => {
                ctrl.systemConstraintForms = response;
                ctrl.systemConstraintForms.unshift({
                    uuid: 'NEW',
                    formName: '--Add New Form--'
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        ctrl.onFormSelection = function () {
            ctrl.editMode = false;
            ctrl.systemConstraintFormUuid = null;
            ctrl.systemConstraintFormConfig = {
                systemConstraintFormMasterDto: {},
                systemConstraintFieldMasterDtos: []
            };
            if (ctrl.selectedFormUuid && ctrl.selectedFormUuid !== 'NEW') {
                ctrl.systemConstraintFormUuid = ctrl.selectedFormUuid;
                ctrl.editMode = true;
                ctrl.getSystemConstraintFormConfigByUuid();
            }
        }

        ctrl.saveFormConfig = function () {
            ctrl.manageSystemConstraintForm.$setSubmitted();
            if (ctrl.manageSystemConstraintForm.$valid) {
                Mask.show();
                SystemConstraintService.createOrUpdateSystemConstraintForm(ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto,'ALL').then(response => {
                    ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto = response;
                    ctrl.manageSystemConstraintForm.$setPristine();
                    toaster.pop("success", `Form Config Saved Successfully.`);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        };

        ctrl.saveFieldConfig = function (index, fieldConfig) {
            ctrl.manageSystemConstraintField.$setSubmitted();
            if (ctrl.manageSystemConstraintField.$valid) {
                Mask.show();
                if (fieldConfig.bothConfiguration) {
                    fieldConfig.appName = 'ALL';
                } else if (fieldConfig.mobileConfiguration) {
                    fieldConfig.appName = 'TECHO_APP';
                } else if (fieldConfig.webConfiguration) {
                    fieldConfig.appName = 'WEB';
                }
                ctrl.setConfigurations(fieldConfig);
                SystemConstraintService.createOrUpdateSystemConstraintFieldConfig(ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto.uuid, fieldConfig).then(response => {
                    ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index] = _processFormFieldConfig(response);
                    ctrl.manageSystemConstraintField.$setPristine();
                    toaster.pop("success", `Field Config Saved Successfully.`);
                }).catch(GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        };

        ctrl.addFieldConfig = function () {
            if (!ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos) {
                ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos = [];
            }
            ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.push({
                fieldKey: null,
                fieldName: null,
                fieldType: null,
                standardFieldMasterId: null,
                systemConstraintFieldValueMasterDtos: []
            }); 
        };

        ctrl.removeFieldConfig = function (index, fieldConfigUuid) {
            if (!fieldConfigUuid) {
                ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.splice(index, 1);
                return;
            }
            Mask.show();
            SystemConstraintService.deleteSystemConstraintFieldConfig(fieldConfigUuid).then(() => {
                ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.splice(index, 1);
                toaster.pop("success", `Field Config Removed Successfully.`);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.onFieldNameChanged = (index, fieldName) => {
            const replacedString = fieldName ? fieldName.replace(/(?:^\w|[A-Z]|\b\w)/g, (word, i) => {
                return i == 0 ? word.toLowerCase() : word.toUpperCase();
            }).replace(/\s+/g, '') : null;
            let labelKey = replacedString.concat('_' + ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto.formCode);
            let field = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index];
            let labelIndex = field.systemConstraintFieldValueMasterDtos.findIndex(e => e.key === 'label');
            let label = field.systemConstraintFieldValueMasterDtos[labelIndex];
            if (label) label.defaultValue = labelKey;
        }

        ctrl.onFieldTypeChange = function (index, fieldType) {
            let formMasterDto = ctrl.systemConstraintFormConfig.systemConstraintFormMasterDto;
            let fieldMasterDto = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index];
            let fieldValueMasterDtos = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos;
            fieldMasterDto.fieldKey = fieldMasterDto.fieldName = null;
            if (fieldValueMasterDtos && fieldValueMasterDtos.length) {
                fieldValueMasterDtos.forEach(fieldValueMasterDto => {
                    if (fieldValueMasterDto.uuid) {
                        if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                            fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                        }
                        fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                    }
                })
            }
            fieldMasterDto.systemConstraintFieldValueMasterDtos = [];
            if (fieldType) {
                ctrl.fieldValueMap[fieldMasterDto.fieldType].forEach(fieldValueConfig => fieldMasterDto.systemConstraintFieldValueMasterDtos.push({
                    key: fieldValueConfig.key,
                    valueType: fieldValueConfig.valueType,
                    required: fieldValueConfig.required,
                    value: null,
                    defaultValue: null,
                    overrideValue: false
                }));
                if (ctrl.onlyTechoAppFields.includes(fieldType)) {
                    fieldMasterDto.mobileConfiguration = ctrl.configurationSelectionDisabled = true;
                    fieldMasterDto.webConfiguration = fieldMasterDto.bothConfiguration = false;
                } else if (ctrl.onlyWebFields.includes(fieldType)) {
                    fieldMasterDto.webConfiguration = ctrl.configurationSelectionDisabled = true;
                    fieldMasterDto.mobileConfiguration = fieldMasterDto.bothConfiguration = false;
                } else {
                    ctrl.configurationSelectionDisabled = false;
                }
                if (fieldMasterDto.webConfiguration && fieldMasterDto.mobileConfiguration) {
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['WEB', 'TECHO_APP']);
                } else if (fieldMasterDto.webConfiguration) {
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['WEB']);
                } else if (fieldMasterDto.mobileConfiguration) {
                    ctrl.modifyfieldConfigUi(fieldMasterDto, ['TECHO_APP']);
                } else {
                    ctrl.modifyfieldConfigUi(fieldMasterDto, []);
                }
            }
        };

        ctrl.onFieldValueDropdownOptionChange = function (index, fieldValueConfig) {
            let fieldMasterDto = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index];
            let fieldValueMasterDtos = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos;
            switch (fieldValueConfig.key) {
                case 'optionsType':
                    ctrl.fieldValueDropdownOptionsByKey[fieldValueConfig.key].forEach(option => {
                        if (fieldValueConfig.value === option.key || fieldValueConfig.defaultValue === option.key) {
                            if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === option.key)) {
                                fieldValueMasterDtos.push({
                                    key: option.key,
                                    valueType: option.valueType,
                                    showInUI: true,
                                    required: option.required
                                })
                            }
                        } else {
                            fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                                if (fieldValueMasterDto.key !== option.key) {
                                    return true;
                                }
                                if (fieldValueMasterDto.uuid) {
                                    if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                        fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                    }
                                    fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                                }
                                return false;
                            });
                            if (["DROPDOWN","RADIO","CB","SCB","MS","SRBD"].includes(fieldMasterDto.fieldType)) {
                                let additionalStaticOptionsRequired = fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'additionalStaticOptionsRequired');
                                additionalStaticOptionsRequired.defaultValue = 'false'
                                additionalStaticOptionsRequired.value = null;
                            }
                        }
                    })
                    ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos = fieldValueMasterDtos;
                    break;
                case 'buttonLabelType':
                    if (fieldValueConfig.value === 'text' || fieldValueConfig.defaultValue === 'text') {
                        if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'label')) {
                            fieldValueMasterDtos.push({
                                key: 'label',
                                valueType: 'TEXT',
                                showInUI: true,
                                required: true
                            })
                        }
                        fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                            if (fieldValueMasterDto.key !== 'icon') {
                                return true;
                            }
                            if (fieldValueMasterDto.uuid) {
                                if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                    fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                }
                                fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                            }
                            return false;
                        });
                    } else if (fieldValueConfig.value === 'icon' || fieldValueConfig.defaultValue === 'icon') {
                        if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'icon')) {
                            fieldValueMasterDtos.push({
                                key: 'icon',
                                valueType: 'TEXT',
                                showInUI: true,
                                required: true
                            })
                        }
                        fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                            if (fieldValueMasterDto.key !== 'label') {
                                return true;
                            }
                            if (fieldValueMasterDto.uuid) {
                                if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                    fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                }
                                fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                            }
                            return false;
                        });
                    } else if (fieldValueConfig.value === 'textAndIcon' || fieldValueConfig.defaultValue === 'textAndIcon') {
                        if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'label')) {
                            fieldValueMasterDtos.push({
                                key: 'label',
                                valueType: 'TEXT',
                                showInUI: true,
                                required: true
                            })
                        }
                        if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'icon')) {
                            fieldValueMasterDtos.push({
                                key: 'icon',
                                valueType: 'TEXT',
                                showInUI: true,
                                required: true
                            })
                        }
                    } else {
                        fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                            if (fieldValueMasterDto.key !== 'label' && fieldValueMasterDto.key !== 'icon') {
                                return true;
                            }
                            if (fieldValueMasterDto.uuid) {
                                if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                    fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                }
                                fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                            }
                            return false;
                        });
                    }
                    ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos = fieldValueMasterDtos;
                    break;
                default:
                    break;
            }
        };

        ctrl.onFieldValueRadioOptionChanged = function (index, fieldValueConfig) {
            let fieldMasterDto = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index];
            let fieldValueMasterDtos = ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos;
            switch (fieldMasterDto.fieldType) {
                case 'SHORT_TEXT':
                case 'LONG_TEXT':
                case 'PASSWORD':
                case 'NUMBER':
                    if (fieldValueConfig.key === 'hasPattern') {
                        if (fieldValueConfig.value === 'true' || fieldValueConfig.defaultValue === 'true') {
                            if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'pattern')) {
                                fieldValueMasterDtos.push({
                                    key: 'pattern',
                                    valueType: 'TEXT',
                                    showInUI: true
                                });
                            }
                            if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'patternMessage')) {
                                fieldValueMasterDtos.push({
                                    key: 'patternMessage',
                                    valueType: 'TEXT',
                                    showInUI: true
                                })
                            }
                        } else {
                            fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                                if (fieldValueMasterDto.key !== 'pattern' && fieldValueMasterDto.key !== 'patternMessage') {
                                    return true;
                                }
                                if (fieldValueMasterDto.uuid) {
                                    if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                        fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                    }
                                    fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                                }
                                return false;
                            });
                        }
                    }
                    ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos = fieldValueMasterDtos;
                    break;
                case 'DROPDOWN':
                case 'RADIO':
                case 'CB':
                case 'SCB':
                case 'MS':
                case 'SRBD':
                    if (fieldValueConfig.key === 'additionalStaticOptionsRequired') {
                        if (fieldValueConfig.value === 'true' || fieldValueConfig.defaultValue === 'true') {
                            if (!fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'staticOptions')) {
                                fieldValueMasterDtos.push({
                                    key: 'staticOptions',
                                    valueType: 'JSON',
                                    showInUI: true
                                });
                            }
                        } else {
                            let optionsType = fieldValueMasterDtos.find(filedValueMasterDto => filedValueMasterDto.key === 'optionsType');
                            if (optionsType.value !== 'staticOptions' && optionsType.defaultValue !== 'staticOptions') {
                                fieldValueMasterDtos = fieldValueMasterDtos.filter(fieldValueMasterDto => {
                                    if (fieldValueMasterDto.key !== 'staticOptions') {
                                        return true;
                                    }
                                    if (fieldValueMasterDto.uuid) {
                                        if (!fieldMasterDto.fieldValueUuidsToBeRemoved) {
                                            fieldMasterDto.fieldValueUuidsToBeRemoved = [];
                                        }
                                        fieldMasterDto.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                                    }
                                    return false;
                                });
                            }
                        }
                    }
                    ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos[index].systemConstraintFieldValueMasterDtos = fieldValueMasterDtos;
                    break;
                default:
                    break;
            }
        }

        ctrl.onOverrideValueSelection = function (fieldValueConfig, fieldConfig) {
            if (!fieldValueConfig.overrideValue) {
                fieldValueConfig.value = null;
                if (fieldValueConfig.key === 'label') {
                    fieldValueConfig.enTranslationOfLabel = null;
                }
                return;
            }
        };

        ctrl.onClickOfCollapsibleHeader = function (index, fieldConfig) {
            ctrl.systemConstraintFormConfig.systemConstraintFieldMasterDtos.forEach(fieldMasterDto => fieldMasterDto.showContent = false);
            if (!angular.element('#collapseConfiguration' + index).hasClass('show')) {
                fieldConfig.showContent = true;
            }
        };

        ctrl.setConfigurations = (fieldConfig) => {
            let events = fieldConfig.systemConstraintFieldValueMasterDtos.find(e => e.key === 'events');
            let visibility = fieldConfig.systemConstraintFieldValueMasterDtos.find(e => e.key === 'visibility');
            let requirable = fieldConfig.systemConstraintFieldValueMasterDtos.find(e => e.key === 'requirable');
            let disability = fieldConfig.systemConstraintFieldValueMasterDtos.find(e => e.key === 'disability');
            if (events) {
                Object.assign(events, {
                    defaultValue: JSON.stringify(fieldConfig.events) || "[]",
                    showInUI: true
                });
            } else {
                fieldConfig.systemConstraintFieldValueMasterDtos.push({
                    defaultValue: JSON.stringify(fieldConfig.events) || "[]",
                    key: "events",
                    valueType: "EVENTS",
                    showInUI: true
                });
            }
            if (visibility) {
                Object.assign(visibility, {
                    defaultValue: JSON.stringify(fieldConfig.visibility) || "{}",
                    showInUI: true
                });
            } else {
                fieldConfig.systemConstraintFieldValueMasterDtos.push({
                    defaultValue: JSON.stringify(fieldConfig.visibility) || "{}",
                    key: "visibility",
                    valueType: "VISIBILITY",
                    showInUI: true
                });
            }
            if (requirable) {
                Object.assign(requirable, {
                    defaultValue: JSON.stringify(fieldConfig.requirable) || "{}",
                    showInUI: true
                });
            } else {
                fieldConfig.systemConstraintFieldValueMasterDtos.push({
                    defaultValue: JSON.stringify(fieldConfig.requirable) || "{}",
                    key: "requirable",
                    valueType: "REQUIRABLE",
                    showInUI: true
                });
            }
            if (disability) {
                Object.assign(disability, {
                    defaultValue: JSON.stringify(fieldConfig.disability) || "{}",
                    showInUI: true
                });
            } else {
                fieldConfig.systemConstraintFieldValueMasterDtos.push({
                    defaultValue: JSON.stringify(fieldConfig.disability) || "{}",
                    key: "disability",
                    valueType: "DISABILITY",
                    showInUI: true
                });
            }
            fieldConfig.systemConstraintFieldValueMasterDtos.forEach(fieldValueMasterDto => {
                if (!fieldValueMasterDto.defaultValue || !fieldValueMasterDto.defaultValue.trim().length) {
                    if (!fieldConfig.fieldValueUuidsToBeRemoved) {
                        fieldConfig.fieldValueUuidsToBeRemoved = [];
                    }
                    fieldConfig.fieldValueUuidsToBeRemoved.push(fieldValueMasterDto.uuid);
                }
            })
            fieldConfig.systemConstraintFieldValueMasterDtos = fieldConfig.systemConstraintFieldValueMasterDtos.filter(e => e.showInUI && e.defaultValue);
        }

        ctrl.configureJson = (fieldConfig, fieldValueConfig, category) => {
            ctrl.currentField = fieldConfig;
            ctrl.currentFieldValue = fieldValueConfig;
            ctrl.currentCategory = category;
            ctrl.mobileValidationJson = false;
            if (ctrl.currentCategory === 'default') {
                ctrl.currentFieldValue.staticOptions = ctrl.currentFieldValue.defaultValue ? JSON.parse(ctrl.currentFieldValue.defaultValue) : [{}];
            } else if (ctrl.currentCategory === 'override') {
                ctrl.currentFieldValue.staticOptions = ctrl.currentFieldValue.value ? JSON.parse(ctrl.currentFieldValue.value) : [{}];
            }
            if (ctrl.currentField.fieldType === 'TABLE') {
                $("#configureTable").modal({ backdrop: 'static', keyboard: false });
            } else if(["mobileFormulas","mobileValidation"].includes(ctrl.currentFieldValue.key)){
                if (ctrl.currentFieldValue.key === 'mobileValidation') ctrl.mobileValidationJson = true;
                $("#configureMobileValidationsAndFormulas").modal({ backdrop: 'static', keyboard: false });
            } else {
                $("#configureStaticOptions").modal({ backdrop: 'static', keyboard: false });      
            }
        }

        ctrl.addJsonOption = (staticOptions) => staticOptions.push({});

        ctrl.removeJsonOption = (staticOptions, index) => staticOptions.splice(index, 1);

        ctrl.shiftOptionUp = (staticOptions, index) => {
            let staticOption = staticOptions[index];
            staticOptions.splice(index, 1);
            staticOptions.splice(index - 1, 0, staticOption);
        }

        ctrl.shiftOptionDown = (staticOptions, index) => {
            let staticOption = staticOptions[index];
            staticOptions.splice(index, 1);
            staticOptions.splice(index + 1, 0, staticOption);
        }

        ctrl.saveJsonConfiguration = (fieldValueConfig) => {
            ctrl.staticConfigurationForm.$setSubmitted();
            ctrl.configureMobileValidationsForm.$setSubmitted();
            if ((ctrl.currentField.fieldType === 'TABLE' && ctrl.tableConfigurationForm.$valid)
                || ctrl.staticConfigurationForm.$valid || ctrl.configureMobileValidationsForm.$valid) {
                if (Array.isArray(fieldValueConfig.staticOptions) && fieldValueConfig.staticOptions.length) {
                    if (ctrl.currentCategory === 'default') {
                        fieldValueConfig.defaultValue = angular.toJson(fieldValueConfig.staticOptions);
                    } else {
                        fieldValueConfig.value = angular.toJson(fieldValueConfig.staticOptions);
                    }
                    ctrl.cancelJsonConfiguration();
                    toaster.pop('success', 'Options configured successfully');
                } else {
                    toaster.pop('error', 'Atleast one option needs to be configured');
                }
            }
        }

        ctrl.cancelJsonConfiguration = () => {
            $("#configureStaticOptions").modal('hide');
            $("#configureTable").modal('hide');
            $("#configureMobileValidationsAndFormulas").modal('hide');
            ctrl.staticConfigurationForm.$setPristine();
            ctrl.currentField = null;
            ctrl.currentFieldValue = null;
            ctrl.currentCategory = null;
        }

        ctrl.goBack = function () {
            $state.go("techo.admin.systemConstraints", { selectedTab: 'manage-form-configs' });
        };

        ctrl.fieldConfigsFilter = function (item) {
            let result = false;
            switch (ctrl.fieldConfigPlatform) {
                case 'WEB':
                    result = item.webConfiguration && !item.mobileConfiguration && !item.bothConfiguration;
                    break;
                case 'MOBILE':
                    result = item.mobileConfiguration && !item.webConfiguration && !item.bothConfiguration;
                    break;
                case 'BOTH':
                    result = true;
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        };

        ctrl.getFieldTypeName =(fieldType) =>{
            return ctrl.fieldTypes.find(fieldTypeObj => fieldTypeObj.key === fieldType).value;
        };
        
        ctrl.isHiddenFieldAvailable=(fieldConfig) =>{
            let hiddenFieldFound= fieldConfig.systemConstraintFieldValueMasterDtos.find(dto=> {
               return dto.key === 'isHidden' && dto.defaultValue === 'true'
            });
            return hiddenFieldFound ? true:false
        };
        //** START  This block is used to show/hide field based on conditions and this will check condition 
        // in particular fields systemConstraintFieldValueMasterDtos
        const getKeyPath = (path, obj)=> {
            try {
                return obj.find(o=>o.key === path).defaultValue;
            } catch (e) {
                return null;
            }
        };

        const _getRuleResult= (rule, data)=> {
            var result;
    
            var sourceValue = getKeyPath(rule[0], data);
            if (sourceValue == null || sourceValue == undefined) return false;
    
            const isNumber = (val) => {
                try {
                    parseFloat(val);
                    return true;
                } catch (e) {
                    return false;
                }
            };
    
            switch (rule[1]) {
                case "eq":
                    if (Object.prototype.toString.call(sourceValue) == "[object Array]") {
                        if (sourceValue.indexOf(rule[2]) >= 0) {
                            result = true;
                        } else {
                            result = false;
                        }
                    } else {
                        result = sourceValue == rule[2];
                    }
                    break;
                case "ne":
                case "neq":
                    if (Object.prototype.toString.call(sourceValue) == "[object Array]") {
                        if (sourceValue.indexOf(rule[2]) < 0) {
                            result = true;
                        } else {
                            result = false;
                        }
                    } else {
                        result = sourceValue != rule[2];
                    }
                    break;
                case "gt":
                    if (!isNumber(sourceValue)) {
                        result = false;
                    } else {
                        result = parseFloat(sourceValue) > parseFloat(rule[2]);
                    }
                    break;
                case "lt":
                    if (!isNumber(sourceValue)) {
                        result = false;
                    } else {
                        result = parseFloat(sourceValue) < parseFloat(rule[2]);
                    }
                    break;
                case 'gte':
                    if (!isNumber(sourceValue)) {
                        result = false;
                    } else {
                        result = parseFloat(sourceValue) >= parseFloat(rule[2]);
                    }
                    break;
                case 'lte':
                    if (!isNumber(sourceValue)) {
                        result = false;
                    } else {
                        result = parseFloat(sourceValue) <= parseFloat(rule[2]);
                    }
                    break;
                default:
                    result = false;
                    break
            }
    
            return result;
        };
    
        ctrl.checkFieldCondition = (fieldKey, config)=> {
            let conditionObject = ctrl.fieldConditions[fieldKey];
            if (conditionObject && conditionObject.conditional_bool == true) {
                var result = false;
    
                if (!conditionObject.conditions) return true;
                if (!conditionObject.conditions.rules) return true;
    
                if (["ANY", "any"].indexOf(conditionObject.conditions.application) >= 0) {
                    // Only one of the rules has to pass
    
                    for (var conIdx in conditionObject.conditions.rules) {
                        var rule = conditionObject.conditions.rules[conIdx];
    
                        var tmpResult = _getRuleResult(rule, config);
                        if (result != true && tmpResult == true) result = true;
                    }
    
                } else {
                    var ruleCount = conditionObject.conditions.rules.length;
                    var rulePassCount = 0;
    
                    for (var ruleIdx in conditionObject.conditions.rules) {
                        var rule = conditionObject.conditions.rules[ruleIdx];
                        var ruleResult = _getRuleResult(rule, config);
                        if (ruleResult) rulePassCount++;
                    }
    
                    if (ruleCount == rulePassCount) result = true;
    
                }
                return result;
            } else {
                return true;
            }
        };
        ctrl.fieldConditions = {
            requiredMessage: {
                conditional_bool: true,
                conditions: {
                    condition: "all",
                    rules: [
                        ['isRequired', 'eq', 'true']
                    ]
                }
            }
        };
        //** END
        ctrl.generateFieldValueMap = () => {
            ctrl.fieldValueMap = {};
            let commonFieldValues = [{
                key: "label",
                valueType: "TEXT",
                required: true
            }, {
                key: "tooltip",
                valueType: "TEXT",
                required: true
            }, {
                key: "mobileQuestionNumber",
                valueType: "TEXT",
                required: true
            }, {
                key: "mobileTitle",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileSubtitle",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileInstruction",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileValidation",
                valueType: "JSON",
                required: false
            }, {
                key: "mobileFormulas",
                valueType: "JSON",
                required: false,
            }, {
                key: "mobileDataMap",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileRelatedPropertyName",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileBinding",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileTooltip",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileHelpVideoField",
                valueType: "TEXT",
                required: false
            }, {
                key: "mobileEvent",
                valueType: "DROPDOWN",
                required: false
            }, {
                key: "mobileHint",
                valueType: "TEXT",
                required: false
            }, {
                key: "isHidden",
                valueType: "BOOLEAN",
                required: true
            }];
            let otherCommonFieldValue = [{
                key: "placeholder",
                valueType: "TEXT",
                required: true
            }, {
                key: "isRequired",
                valueType: "BOOLEAN",
                required: true
            }, {
                key: "requiredMessage",
                valueType: "TEXT",
                required: true
            }, {
                key: "isDisabled",
                valueType: "BOOLEAN",
                required: true
            }];
            let shortTextAdditionalFields = [{
                key: "minLength",
                valueType: "NUMBER",
                required: true
            }, {
                key: "maxLength",
                valueType: "NUMBER",
                required: true
            }, {
                key: "hasPattern",
                valueType: "BOOLEAN",
                required: true
            }];
            let longTextAdditionalFields = [{
                key: "minLength",
                valueType: "NUMBER",
                required: true
            }, {
                key: "maxLength",
                valueType: "NUMBER",
                required: true
            }, {
                key: "rows",
                valueType: "NUMBER",
                required: true
            }, {
                key: "cols",
                valueType: "NUMBER",
                required: true
            }, {
                key: "hasPattern",
                valueType: "BOOLEAN",
                required: true
            }];
            let dropdownAdditionalFields = [{
                key: "optionsType",
                valueType: "DROPDOWN",
                required: true
            }, {
                key: "additionalStaticOptionsRequired",
                valueType: "BOOLEAN",
                required: true
            }, {
                key: "isMultiple",
                valueType: "BOOLEAN",
                required: true
            }];
            let dynamicOptionsAdditionalFields = [{
                key: "optionsType",
                valueType: "DROPDOWN",
                required: true
            }, {
                key: "additionalStaticOptionsRequired",
                valueType: "BOOLEAN",
                required: true
            }];
            let checkboxAdditionalFields = [{
                key: "staticOptions",
                valueType: "JSON",
                required: true
            }];
            let radioAdditionalFields = [{
                key: "isBoolean",
                valueType: "BOOLEAN",
                required: true
            }];
            let numberAdditionalFields = [{
                key: "min",
                valueType: "NUMBER",
                required: true
            }, {
                key: "max",
                valueType: "NUMBER",
                required: true
            }, {
                key: "hasPattern",
                valueType: "BOOLEAN",
                required: true
            }];
            let dateAdditionalFields = [{
                key: "minDateField",
                valueType: "TEXT",
                required: true
            }, {
                key: "maxDateField",
                valueType: "TEXT",
                required: true
            }]
            let locationAdditionalFields = [{
                key: 'templateType',
                valueType: 'DROPDOWN',
                required: true
            }, {
                key: "fetchUptoLevel",
                valueType: "NUMBER",
                required: true
            }, {
                key: "requiredUptoLevel",
                valueType: "NUMBER",
                required: true
            }, {
                key: "fetchAccordingToUserAoi",
                valueType: "BOOLEAN",
                required: true
            }];
            ctrl.fieldValueMap = {
                "SHORT_TEXT": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...shortTextAdditionalFields
                ],
                "LONG_TEXT": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...longTextAdditionalFields
                ],
                "DROPDOWN": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dropdownAdditionalFields
                ],
                "CHECKBOX": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...checkboxAdditionalFields
                ],
                "RADIO": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...radioAdditionalFields,
                    ...dynamicOptionsAdditionalFields
                ],
                "DATE": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dateAdditionalFields
                ],
                "TIME": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue
                ],
                "NUMBER": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...numberAdditionalFields
                ],
                "PASSWORD": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...shortTextAdditionalFields
                ],
                "LOCATION_DIRECTIVE": [
                    ...commonFieldValues,
                    ...locationAdditionalFields
                ],
                "ADDED_LOCATIONS": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue
                ],
                "INFORMATION_DISPLAY": [{
                    key: "displayValue",
                    valueType: "TEXT",
                    required: true
                }, {
                    key: 'displayType',
                    valueType: 'DROPDOWN',
                    required: true
                }],
                "INFORMATION_TEXT": [{
                    key: 'isClickable',
                    valueType: 'BOOLEAN',
                    required: true
                }],
                "CUSTOM_HTML": [{
                    key: "customHTML",
                    valueType: "LONG_TEXT",
                    required: true
                }],
                "CUSTOM_COMPONENT": [{
                    key: 'relativeFilePath',
                    valueType: 'TEXT',
                    required: true
                }],
                "TABLE": [{
                    key: "tableObject",
                    valueType: "TEXT",
                    required: true
                }, {
                    key: "tableConfig",
                    valueType: "JSON",
                    required: true
                }],
                "BUTTON": [{
                    key: "buttonType",
                    valueType: "DROPDOWN",
                    required: true
                }, {
                    key: "buttonLabelType",
                    valueType: "DROPDOWN",
                    required: true
                }, {
                    key: "tooltip",
                    valueType: "TEXT",
                    required: true
                }, {
                    key: "cssStyle",
                    valueType: "TEXT",
                    required: false
                }, {
                    key: "cssClass",
                    valueType: "TEXT",
                    required: false
                }],
                "FILE_UPLOAD": [
                    ...commonFieldValues,
                    {
                        key: 'fileUploadType',
                        valueType: 'DROPDOWN',
                        required: true
                    }, {
                        key: 'allowedExtensions',
                        valueType: 'TEXT',
                        required: true
                    }
                ],
                "L": [
                    ...commonFieldValues,
                ],
                "CB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dynamicOptionsAdditionalFields
                ],
                "AB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "AD": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "TBWA": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "TBCL": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "PP": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "SV": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "SVM": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "WB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "WD": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "TEMB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "LF": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "MS": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dynamicOptionsAdditionalFields
                ],
                "BPM": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "PA": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "CALL": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "ML": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "MFN": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "MCR": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "HWR": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "OS": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "BS": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "BMI": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "HIC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "CGCC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "IGC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "SC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "SCB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dynamicOptionsAdditionalFields
                ],
                "OBVC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "NDPC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "FSC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "ORDT": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "HMC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "LIC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "RBD": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "MDC": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "SRBD": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                    ...dynamicOptionsAdditionalFields
                ],
                "CTB": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ],
                "QRS": [
                    ...commonFieldValues,
                    ...otherCommonFieldValue,
                ]
            };
            ctrl.fieldValueMapByKey = {};
            for (const property in ctrl.fieldValueMap) {
                ctrl.fieldValueMapByKey[property] = {};
                ctrl.fieldValueMap[property].forEach((config, index) => ctrl.fieldValueMapByKey[property][config.key] = { ...config, order: index + 1 })
            }
            ctrl.commonConfigurationFields = [
                'label',
                'isHidden',
                'isRequired',
                'requiredMessage',
                'isDisabled',
                'optionsType',
                'staticOptions',
                'listValueField',
                'additionalStaticOptionsRequired',
                'queryBuilder',
                'maxLength',
                'rows',
                'cols',
                'isMultiple',
                'fetchUptoLevel',
                'requiredUptoLevel',
                'fetchAccordingToUserAoi',
                'templateType',
                'hasPattern',
                'pattern',
                'patternMessage'
            ]
            ctrl.webConfigurationFields = [
                'tooltip',
                'placeholder',
                'isMultiple',
                'isBoolean',
                'minLength',
                'min',
                'max',
                'minDateField',
                'maxDateField',
                'customHTML',
                'relativeFilePath',
                'displayValue',
                'displayType',
                'tableObject',
                'tableConfig',
                'buttonType',
                'buttonLabelType',
                'cssStyle',
                'cssClass',
                'fileUploadType',
                'allowedExtensions',
                'isClickable',
                ...ctrl.commonConfigurationFields
            ];
            ctrl.mobileConfigurationFields = [
                'mobileQuestionNumber',
                'mobileTitle',
                'mobileSubtitle',
                'mobileInstruction',
                'mobileValidation',
                'mobileFormulas',
                'mobileDataMap',
                'mobileRelatedPropertyName',
                'mobileBinding',
                'mobileTooltip',
                'mobileHelpVideoField',
                'mobileEvent',
                'mobileHint',
                ...ctrl.commonConfigurationFields
            ];
        }

        _init();
    }
    angular.module('imtecho.controllers').controller('ManageSystemConstraintForm', ManageSystemConstraintForm);
})();