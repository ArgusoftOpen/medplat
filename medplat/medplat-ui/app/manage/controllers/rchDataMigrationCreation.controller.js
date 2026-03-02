(function () {
    function RchDataMigrationCreationCtrl(QueryDAO, Mask, $stateParams, $uibModal, GeneralUtil, toaster, $state) {
        var ctrl = this;
        ctrl.requiredUptoLevel = 4;
        ctrl.create = function (form) {
            if (form.$valid) {
                var dto = {
                    code: 'rch_data_migration_fetchdetails',
                    parameters: {
                        locationId: ctrl.locationId,
                    }
                }
                Mask.show();
                QueryDAO.executeQuery(dto).then(function (res) {
                    var modalInstance = $uibModal.open({
                        templateUrl: 'app/manage/views/rchDataMigrationconfirmation.modal.html',
                        controller: 'RchDataConfirmationModal',
                        controllerAs: 'rchModal',
                        windowClass: 'cst-modal',
                        size: 'md',
                        resolve: {
                            PrevData: function () {
                                return res.result;
                            }
                        }
                    });
                    modalInstance.result.then(function (response) {
                        if (response.length > 0) {
                            toaster.pop('warning', 'Request for data sync is already pending..!');
                        } else {
                            ctrl.createSyncRequest();
                        }
                    }, function () { });
                }).catch(function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                }).finally(function () {
                    Mask.hide();
                })
            }
        }

        ctrl.createSyncRequest = function () {
            var dto = {
                code: 'rch_data_migration_creation',
                parameters: {
                    locationId: ctrl.locationId,
                    user_id: $stateParams.userId
                }
            }
            Mask.show();
            QueryDAO.executeQuery(dto).then(function (res) {
                toaster.pop('success', 'Request for RCH Data Sync successfully submitted');
                // $state.go('techo.manage.rchdatamigration')
            }).catch(function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
            }).finally(function () {
                Mask.hide();
            })
        }
    }
    angular.module('imtecho.controllers').controller('RchDataMigrationCreationCtrl', RchDataMigrationCreationCtrl);
})();
