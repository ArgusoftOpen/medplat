(function () {
    function SystemConstraintService($resource, APP_CONFIG) {
        let api = $resource(APP_CONFIG.apiPath + '/systemConstraint/:action/:subAction/:id', {}, {
            getSystemConstraintForms: {
                method: 'GET',
                isArray: true
            },
            getSystemConstraintFormByUuid: {
                method: 'GET'
            },
            getSystemConstraintFormConfigByUuid: {
                method: 'GET'
            },
            getSystemConstraintFieldsByFormMasterUuid: {
                method: 'GET',
                isArray: true
            },
            getSystemConstraintStandardFields: {
                method: 'GET',
                isArray: true
            },
            getSystemConstraintStandardFieldByUuid: {
                method: 'GET'
            },
            createOrUpdateSystemConstraintForm: {
                method: 'POST'
            },
            createOrUpdateSystemConstraintFieldConfig: {
                method: 'POST'
            },
            deleteSystemConstraintFieldConfig: {
                method: 'DELETE'
            },
            getSystemConstraintStandardConfigById: {
                method: 'GET'
            },
            createOrUpdateSystemConstraintStandardFieldConfig: {
                method: 'POST'
            },
            createOrUpdateSystemConstraintStandardField: {
                method: 'POST'
            },
        });
        return {
            getSystemConstraintForms: function () {
                return api.getSystemConstraintForms({
                    action: 'forms'
                }, {}).$promise;
            },
            getSystemConstraintFormsByMenuConfigId: function (menuConfigId) {
                return api.getSystemConstraintForms({
                    action: 'forms',
                    subAction: menuConfigId
                }, {}).$promise;
            },
            getSystemConstraintFormByUuid: function (uuid) {
                return api.getSystemConstraintFormByUuid({
                    action: 'form',
                    subAction: uuid
                }, {}).$promise;
            },
            getSystemConstraintFormConfigByUuid: function (uuid, appName) {
                return api.getSystemConstraintFormConfigByUuid({
                    action: 'formConfig',
                    subAction: uuid,
                    appName: appName
                }, {}).$promise;
            },
            getSystemConstraintFieldsByFormMasterUuid: function (formMasterUuid) {
                return api.getSystemConstraintFieldsByFormMasterUuid({
                    action: 'fields',
                    subAction: formMasterUuid
                }, {}).$promise;
            },
            getSystemConstraintStandardFields: function (fetchOnlyActive = false) {
                return api.getSystemConstraintStandardFields({
                    action: 'standard',
                    subAction: 'fields',
                    fetchOnlyActive
                }, {}).$promise;
            },
            getSystemConstraintStandardFieldByUuid: function (uuid) {
                return api.getSystemConstraintStandardFieldByUuid({
                    action: 'standard',
                    subAction: 'field',
                    id: uuid
                }, {}).$promise;
            },
            createOrUpdateSystemConstraintForm: function (dto, type) {
                return api.createOrUpdateSystemConstraintForm({
                    action: 'form',
                    id: type
                }, dto).$promise;
            },
            createOrUpdateSystemConstraintFieldConfig: function (formMasterUuid, dto) {
                return api.createOrUpdateSystemConstraintFieldConfig({
                    action: 'fieldConfig',
                    subAction: formMasterUuid
                }, dto).$promise;
            },
            deleteSystemConstraintFieldConfig: function (uuid) {
                return api.deleteSystemConstraintFieldConfig({
                    action: 'fieldConfig',
                    subAction: uuid
                }, {}).$promise;
            },
            getSystemConstraintStandardConfigById: function (id) {
                return api.getSystemConstraintStandardConfigById({
                    action: 'standardConfig',
                    subAction: id
                }, {}).$promise;
            },
            createOrUpdateSystemConstraintStandardFieldConfig: function (dto) {
                return api.createOrUpdateSystemConstraintStandardFieldConfig({
                    action: 'standard',
                    subAction: 'fieldConfig'
                }, dto).$promise;
            },
            createOrUpdateSystemConstraintStandardField: function (dto) {
                return api.createOrUpdateSystemConstraintStandardField({
                    action: 'standard',
                    subAction: 'field'
                }, dto).$promise;
            },
        };
    }
    angular.module('imtecho.service')
        .factory('SystemConstraintService', ['$resource', 'APP_CONFIG', SystemConstraintService]);
})();
