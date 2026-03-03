(function () {
    function ManageSimilarMembersController($state, QueryDAO, Mask, toaster, $uibModal, GeneralUtil, AuthenticateService) {
        var sm = this;
        sm.search = {};
        sm.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        var retrieveMemberInformation = function (memberId) {
            QueryDAO.execute({
                code: 'retrieve_member_details_for_mig',
                parameters: {
                    memberId: memberId ? Number(memberId) : null
                }
            }).then(function (res) {
                sm.member = res.result[0];
                sm.member.child = JSON.parse(sm.member.child);
            }, GeneralUtil.showMessageOnApiCallFailure)
        }

        var init = function () {
            sm.pageLoaded = false;
            AuthenticateService.getLoggedInUser().then(function (res) {
                sm.currentUser = res.data;
                sm.pageLoaded = true;
                retrieveMemberInformation($state.params.memberId);
            });
        };

        sm.markAsLFU = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/membermigration/views/mark-as-lfu.modal.html',
                controller: 'MarkAsLfuController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    migration: function () {
                        return { id: $state.params.migrationId, user: sm.currentUser };
                    }
                }
            });
            modalInstance.result.then(function () {
                $state.go('techo.manage.migrations', { selectedIndex: 0 });
            }, function () { });
        };

        sm.markAsRollback = function (migration) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to Rollback the member?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                QueryDAO.execute({
                    code: 'mark_as_rollback',
                    parameters: {
                        userid: sm.currentUser.id,
                        id: Number($state.params.migrationId),
                        memberid: Number($state.params.memberId)
                    }
                }).then(function () {
                    toaster.pop('success', 'Member Rolledback Successfully');
                    $state.go('techo.manage.migrations', { selectedIndex: 0 });
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        sm.openLocationModal = function (migrationObj) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/membermigration/views/location-update.modal.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    migration: function () {
                        return { id: $state.params.migrationId, userid: sm.currentUser.id, memberid: $state.params.memberId };
                    }
                },
                controller: ['$scope', 'migration', '$uibModalInstance', 'LocationService', function ($scope, migration, $uibModalInstance, LocationService) {
                    var $ctrl = this;
                    $ctrl.fetchVillages = function () {
                        $ctrl.villages = [];
                        delete $ctrl.villageId;
                        if ($ctrl.selectedLocation.finalSelected.level >= 4) {
                            QueryDAO.execute({
                                code: 'retrieve_location_by_level_parent',
                                parameters: {
                                    parentId: $ctrl.selectedLocation.finalSelected.optionSelected.id,
                                    level: 7
                                }
                            }).then(function (res) {
                                $ctrl.villages = res.result;
                            }, GeneralUtil.showMessageOnApiCallFailure);
                        }
                    }

                    $ctrl.fetchAshaArea = function () {
                        var selectedParentId = null;
                        $ctrl.ashaAreas = [];
                        delete $ctrl.areaparentid;
                        if ($ctrl.selectedLocation.finalSelected.level >= 4) {
                            if (!!$ctrl.villageId) {
                                selectedParentId = $ctrl.villageId
                            } else {
                                selectedParentId = $ctrl.selectedLocation.finalSelected.optionSelected.id
                            }
                            QueryDAO.execute({
                                code: 'retrieve_location_by_level_parent',
                                parameters: {
                                    parentId: selectedParentId,
                                    level: 8
                                }
                            }).then(function (res) {
                                $ctrl.ashaAreas = res.result;
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                Mask.hide();
                            });
                        }
                    }

                    $ctrl.selecteVillageBasedOnArea = function () {
                        if (!!$ctrl.villages && !!$ctrl.ashaAreaId) {
                            $ctrl.villageId = $ctrl.ashaAreaId;
                        }
                    }

                    $ctrl.update = function (locationId) {
                        if ($ctrl.roleUpdateForm.$valid && locationId) {
                            QueryDAO.execute({
                                code: 'mark_migrated_location',
                                parameters: {
                                    userid: migration.userid,
                                    locationid: locationId,
                                    id: Number(migration.id),
                                    memberid: Number(migration.memberid)
                                }
                            }).then(function () {
                                toaster.pop('success', 'Member Location Updated Successfully');
                                $state.go('techo.manage.migrations', { selectedIndex: 0 })
                                $uibModalInstance.dismiss('close');
                            }, GeneralUtil.showMessageOnApiCallFailure);
                        }
                    };

                    $ctrl.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    };
                }],
                controllerAs: '$ctrl'
            });
            modalInstance.result.then(function () {
            }, function () { });
        };

        sm.goBack = function () {
            $state.go('techo.manage.migrations', {
                selectedIndex: 0,
                locationId: $state.params.locationId != undefined ? Number($state.params.locationId) : undefined
            });
        }
        init();
    }
    angular.module('imtecho.controllers').controller('ManageSimilarMembersController', ManageSimilarMembersController);
})();
