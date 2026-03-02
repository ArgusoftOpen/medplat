(function () {
    function ManageSystemConstraintStandardField($state, Mask, toaster, GeneralUtil, $q, QueryDAO, SystemConstraintService) {
        let ctrl = this;
        ctrl.systemConstraintStandardField = {};

        const _init = function () {
            ctrl.systemConstraintStandardFieldUuid = $state.params.uuid || null;
            if (ctrl.systemConstraintStandardFieldUuid) {
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
                promises.push(SystemConstraintService.getSystemConstraintStandardFieldByUuid(ctrl.systemConstraintStandardFieldUuid));
            }
            Mask.show();
            $q.all(promises).then(responses => {
                ctrl.systemConstraintStandardFieldCategories = responses[0].result;
                if (ctrl.editMode) {
                    ctrl.systemConstraintStandardField = responses[1];
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        };

        ctrl.save = function () {
            ctrl.ManageSystemConstraintStandardField.$setSubmitted();
            if (ctrl.ManageSystemConstraintStandardField.$valid) {
                Mask.show();
                SystemConstraintService.createOrUpdateSystemConstraintStandardField(ctrl.systemConstraintStandardField).then(() => {
                    ctrl.goBack();
                    toaster.pop("success", `Form Standard Field ${ctrl.editMode ? 'Updated' : 'Created'}  Successfully.`);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            }
        };

        ctrl.goBack = function () {
            $state.go("techo.admin.systemConstraints", { selectedTab: 'manage-standard-field-configs' });
        }

        _init();
    }
    angular.module('imtecho.controllers').controller('ManageSystemConstraintStandardField', ManageSystemConstraintStandardField);
})();
