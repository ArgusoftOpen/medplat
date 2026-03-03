(function () {
    function SystemConstraints(Mask, GeneralUtil, $state, SystemConstraintService, QueryDAO) {
        let ctrl = this;
        ctrl.withoutTranslationLabelComponents = ['INFORMATION_DISPLAY', 'INFORMATION_TEXT', 'CUSTOM_HTML', 'TABLE'];

        const _init = function () {
            ctrl.selectedTab = $state.params.selectedTab || 'manage-form-configs';
            switch (ctrl.selectedTab) {
                case 'manage-standard-configs':
                    ctrl.getSystemConstraintStandards();
                    break;
                case 'manage-standard-field-configs':
                    ctrl.getSystemConstraintStandardFields();
                    break;
                case 'manage-form-configs':
                default:
                    ctrl.getSystemConstraintForms();
                    break;
            }
        };

        ctrl.getSystemConstraintForms = function () {
            Mask.show();
            $state.go('.', { selectedTab: ctrl.selectedTab }, { notify: false });
            SystemConstraintService.getSystemConstraintForms().then(function (response) {
                ctrl.systemConstraintForms = response;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.getSystemConstraintStandards = function () {
            Mask.show();
            let dtoList = [];
            $state.go('.', { selectedTab: ctrl.selectedTab }, { notify: false });
            dtoList.push({
                code: 'retrieve_system_configuration_by_key',
                parameters: {
                    key: 'SYSTEM_CONSTRAINT_ACTIVE_STANDARD_ID'
                },
                sequence: 1
            });
            dtoList.push({
                code: 'retrieve_list_values_by_field_key',
                parameters: {
                    fieldKey: 'system_codes_supported_types'
                },
                sequence: 2
            });
            QueryDAO.executeAll(dtoList).then(function (responses) {
                ctrl.systemsActiveStandard = responses[0].result[0] || {};
                ctrl.systemConstraintStandards = responses[1].result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.getSystemConstraintStandardFields = function () {
            Mask.show();
            $state.go('.', { selectedTab: ctrl.selectedTab }, { notify: false });
            SystemConstraintService.getSystemConstraintStandardFields(false).then(function (response) {
                ctrl.systemConstraintStandardFields = response;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.downloadFieldConfigurations = (form) => {
            Mask.show();
            ctrl.configuredQuery = "";
            let queryList = [{
                code: 'retrieve_system_constraint_field_masters',
                parameters: {
                    form_uuid: form.uuid
                },
                sequence: 1
            }, {
                code: 'retrieve_system_constraint_field_value_masters',
                parameters: {
                    form_uuid: form.uuid
                },
                sequence: 2
            }];
            QueryDAO.executeAll(queryList).then((response) => {
                ctrl.fieldMasters = response[0].result;
                ctrl.fieldValueMasters = response[1].result;
                ctrl.fieldNames = ctrl.fieldMasters.map(fieldMaster => fieldMaster.field_name);
                ctrl.translationKeys = ctrl.fieldNames.map(name => `${name}_${form.formCode}`);
                return QueryDAO.execute({
                    code: 'retrieve_system_constraint_translation_labels',
                    parameters: {
                        fieldKeys: ctrl.translationKeys
                    }
                });
            }).then((response) => {
                ctrl.translationMasters = response.result;
                ctrl.configuredQuery = ctrl.configuredQuery.concat(
                    `with field_value_master_deletion as (
                        delete from system_constraint_field_value_master
                        where field_master_uuid in (
                            select uuid
                            from system_constraint_field_master
                            where form_master_uuid = cast('${form.uuid}' as uuid)
                        )
                        returning uuid
                    )
                    delete from system_constraint_field_master
                    where form_master_uuid = cast('${form.uuid}' as uuid);\n\n\n\n`
                );
                ctrl.fieldMasters.forEach((fieldMaster) => {
                    ctrl.configuredQuery = ctrl.configuredQuery.concat(
                        `insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('${fieldMaster.uuid}' as uuid),cast('${fieldMaster.form_master_uuid}' as uuid),${fieldMaster.field_key ? "'" + fieldMaster.field_key.replace(/'/g, "''") + "'" : null},${fieldMaster.field_name ? "'" + fieldMaster.field_name.replace(/'/g, "''") + "'" : null},${fieldMaster.field_type ? "'" + fieldMaster.field_type.replace(/'/g, "''") + "'" : null},${fieldMaster.ng_model ? "'" + fieldMaster.ng_model.replace(/'/g, "''") + "'" : null},${fieldMaster.app_name ? "'" + fieldMaster.app_name.replace(/'/g, "''") + "'" : null},`
                    );
                    if (fieldMaster.standard_field_master_uuid !== null) {
                        ctrl.configuredQuery = ctrl.configuredQuery.concat(
                            `\n(
                                select case when count(1) = 1 then cast('${fieldMaster.standard_field_master_uuid}' as uuid) else null end
                                from system_constraint_standard_field_master
                                where uuid = cast('${fieldMaster.standard_field_master_uuid}' as uuid)
                            ),\n`
                        );
                    } else {
                        ctrl.configuredQuery = ctrl.configuredQuery.concat(`null,`);
                    }
                    ctrl.configuredQuery = ctrl.configuredQuery.concat(
                        `${fieldMaster.created_by},now(),${fieldMaster.modified_by},now());\n\n`
                    );
                    let translations = ctrl.translationMasters.filter(translationMaster => translationMaster.key === `${fieldMaster.field_name}_${form.formCode}`);
                    if (Array.isArray(translations) && translations.length && !ctrl.withoutTranslationLabelComponents.includes(fieldMaster.field_type)) {
                        let apps = null;
                        switch (fieldMaster.app_name) {
                            case 'WEB':
                                apps = `'WEB'`;
                                break;
                            case 'MOBILE':
                                apps = `'TECHO_MOBILE_APP'`;
                                break;
                            case 'ALL':
                                apps = `'WEB','TECHO_MOBILE_APP'`;
                                break;
                        }
                        ctrl.configuredQuery = ctrl.configuredQuery.concat(
                            `delete from internationalization_label_master where key = '${fieldMaster.field_name}_${form.formCode}' and app_name in (${apps});\n\n`
                        )
                        translations.forEach((translation) => {
                            ctrl.configuredQuery = ctrl.configuredQuery.concat(
                                `insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('${translation.country}','${translation.key}','${translation.language}','${translation.created_by}',now(),${translation.custom3b},'${translation.text}',${translation.translation_pending},now(),'${translation.app_name}');\n\n`
                            );
                        });
                    }
                });
                ctrl.fieldValueMasters.forEach((fieldValue) => {
                    ctrl.configuredQuery = ctrl.configuredQuery.concat(
                        `insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('${fieldValue.uuid}' as uuid),cast('${fieldValue.field_master_uuid}' as uuid),${fieldValue.value_type ? "'" + fieldValue.value_type.replace(/'/g, "''") + "'" : null},${fieldValue.key ? "'" + fieldValue.key.replace(/'/g, "''") + "'" : null},${fieldValue.value ? "'" + fieldValue.value.replace(/'/g, "''") + "'" : null},${fieldValue.default_value ? "'" + fieldValue.default_value.replace(/'/g, "''") + "'" : fieldValue.default_value},${fieldValue.created_by},now(),${fieldValue.modified_by},now());\n\n`
                    );
                })
                let a = window.document.createElement('a');
                a.href = window.URL.createObjectURL(new Blob([ctrl.configuredQuery], { type: 'text/plain' }));
                let name = `${form.formCode}_field_configuration.sql`;
                a.download = name;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.navigateToAdd = function () {
            switch (ctrl.selectedTab) {
                case 'manage-form-configs':
                    $state.go("techo.admin.manageSystemConstraintForm");
                    break;
                case 'manage-standard-configs':
                    $state.go("techo.admin.manageSystemConstraintStandard");
                    break;
                case 'manage-standard-field-configs':
                    $state.go("techo.admin.manageSystemConstraintStandardField");
                    break;
                default:
                    break;
            }
        };

        ctrl.navigateToEdit = function (idOrUuid) {
            switch (ctrl.selectedTab) {
                case 'manage-form-configs':
                    $state.go("techo.admin.manageSystemConstraintForm", { uuid: idOrUuid });
                    break;
                case 'manage-standard-configs':
                    $state.go("techo.admin.manageSystemConstraintStandard", { id: idOrUuid });
                    break;
                case 'manage-standard-field-configs':
                    $state.go("techo.admin.manageSystemConstraintStandardField", { uuid: idOrUuid });
                    break;
                default:
                    break;
            }
        };

        ctrl.navigateToEditDynamicTemplate = function (uuid) {
            $state.go("techo.admin.configureDynamicTemplate", { uuid });
        };

        ctrl.navigateToEditMobileTemplate = function (uuid) {
            $state.go("techo.admin.configureMobileTemplate", { uuid });
        };

        _init();
    }
    angular.module('imtecho.controllers').controller('SystemConstraints', SystemConstraints);
})();
