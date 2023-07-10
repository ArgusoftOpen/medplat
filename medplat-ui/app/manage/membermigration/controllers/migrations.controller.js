(function () {
    function MigrationsController($state, QueryDAO, Mask, toaster, $uibModal, GeneralUtil, AuthenticateService, MigrationDAO) {
        var migrations = this;
        migrations.search = {};
        migrations.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        var retrieveAll = function () {
            if (!migrations.pagingService.pagingRetrivalOn && !migrations.pagingService.allRetrieved) {
                migrations.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'lfu_migrated_membbers_retrival',
                    parameters: {
                        limit: migrations.pagingService.limit,
                        offset: migrations.pagingService.offSet,
                        userId: migrations.currentUser.id,
                        locationId: migrations.search.locationId
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result.length === 0 || resForData.result.length < 100) {
                        migrations.pagingService.allRetrieved = true;
                        if (migrations.pagingService.index === 1) {
                            migrations.listOfUsers = resForData.result;
                        }
                    } else {
                        migrations.pagingService.allRetrieved = false;
                        if (migrations.pagingService.index > 1) {
                            migrations.listOfUsers = migrations.listOfUsers.concat(resForData.result);
                        } else {
                            migrations.listOfUsers = resForData.result;
                        }
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    migrations.pagingService.allRetrieved = true;
                }).finally(function () {
                    migrations.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        migrations.searchData = function (reset) {
            if (reset) {
                migrations.pagingService.index = 0;
                migrations.pagingService.allRetrieved = false;
                migrations.pagingService.pagingRetrivalOn = false;
                migrations.listOfUsers = [];
            }
            retrieveAll();
        };

        var setOffsetLimit = function () {
            migrations.pagingService.limit = 100;
            migrations.pagingService.offSet = migrations.pagingService.index * 100;
            migrations.pagingService.index = migrations.pagingService.index + 1;
        };

        const _setLocationIdFromParam = function () {
            if ($state.params.locationId) {
                migrations.search.locationId = Number($state.params.locationId)
            }
        }

        migrations.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }

            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        migrations.close = function () {
            migrations.toggleFilter();
        };

        var init = function () {
            migrations.pageLoaded = false;
            AuthenticateService.getLoggedInUser().then(function (res) {
                migrations.currentUser = res.data;
                migrations.pageLoaded = true;
                if ($state.params.selectedIndex) {
                    migrations.selectedIndex = Number($state.params.selectedIndex);
                } else {
                    migrations.selectedIndex = 0;
                }
                _setLocationIdFromParam();
            });
            AuthenticateService.getAssignedFeature('techo.manage.healthinfrastructures').then(function (res) {
                migrations.user = res.featureJson;
            });
        };

        migrations.markAsLFU = function (migration) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to mark the member as LFU?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                QueryDAO.execute({
                    code: 'mark_as_lfu',
                    parameters: {
                        userid: migrations.currentUser.id,
                        id: migration.migrationid
                    }
                }).then(function () {
                    toaster.pop('success', 'Member marked as LFU');
                    migrations.searchData(true);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        migrations.markAsRollback = function (migration) {
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
                        userid: migrations.currentUser.id,
                        id: migration.migrationid,
                        memberid: migration.memberid
                    }
                }).then(function () {
                    toaster.pop('success', 'Member Rolledback Successfully');
                    migrations.searchData(true);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        migrations.openLocationModal = function (migrationObj) {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/membermigration/views/location-update.modal.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    migration: function () {
                        return { id: migration.migrationid, userid: migrations.currentUser.id, memberid: migration.memberid };
                    }
                },
                controller: ['$scope', 'migration', '$uibModalInstance', function ($scope, migration, $uibModalInstance) {
                    var $ctrl = this;
                    $ctrl.update = function (locationId) {
                        if ($ctrl.roleUpdateForm.$valid) {
                            QueryDAO.execute({
                                code: 'mark_migrated_location',
                                parameters: {
                                    userid: migration.userid,
                                    locationid: locationId,
                                    id: migration.id,
                                    memberid: migration.memberid
                                }
                            }).then(function () {
                                migrations.searchData(true);
                                toaster.pop('success', 'Member Location Updated Successfully')
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
            modalInstance.result.then(function () { }, function () { });
        };

        migrations.lfumembers = function (reset) {
            if (reset) {
                migrations.pagingService.index = 0;
                migrations.pagingService.allRetrieved = false;
                migrations.pagingService.pagingRetrivalOn = false;
                migrations.listOfLFU = [];
            }

            if (!migrations.pagingService.pagingRetrivalOn && !migrations.pagingService.allRetrieved) {
                migrations.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'member_marked_lfu_retrival',
                    parameters: {
                        limit: migrations.pagingService.limit,
                        offset: migrations.pagingService.offSet,
                        userid: migrations.currentUser.id,
                        locationId: migrations.search.locationId
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result.length === 0 || resForData.result.length < 100) {
                        migrations.pagingService.allRetrieved = true;
                        if (migrations.pagingService.index === 1) {
                            migrations.listOfLFU = resForData.result;
                        }
                    } else {
                        migrations.pagingService.allRetrieved = false;
                        if (migrations.pagingService.index > 1) {
                            migrations.listOfLFU = migrations.listOfLFU.concat(resForData.result);
                        } else {
                            migrations.listOfLFU = resForData.result;

                        }
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    migrations.pagingService.allRetrieved = true;
                }).finally(function () {
                    migrations.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        }

        migrations.retrievedResolvedMembers = function (reset) {
            if (reset) {
                migrations.pagingService.index = 0;
                migrations.pagingService.allRetrieved = false;
                migrations.pagingService.pagingRetrivalOn = false;
                migrations.resolvedMembers = [];
            }

            if (!migrations.pagingService.pagingRetrivalOn && !migrations.pagingService.allRetrieved) {
                migrations.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'migration_resolved_tasks',
                    parameters: {
                        limit: migrations.pagingService.limit,
                        offset: migrations.pagingService.offSet,
                        userid: migrations.currentUser.id,
                        locationId: migrations.search.locationId
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result.length === 0 || resForData.result.length < 100) {
                        migrations.pagingService.allRetrieved = true;
                        if (migrations.pagingService.index === 1) {
                            migrations.resolvedMembers = resForData.result;
                        }
                    } else {
                        migrations.pagingService.allRetrieved = false;
                        if (migrations.pagingService.index > 1) {
                            migrations.resolvedMembers = migrations.resolvedMembers.concat(resForData.result);
                        } else {
                            migrations.resolvedMembers = resForData.result;
                        }
                    }

                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    migrations.pagingService.allRetrieved = true;
                }).finally(function () {
                    migrations.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        migrations.retrieveMigrationIn = function (reset) {
            if (reset) {
                migrations.pagingService.index = 0;
                migrations.pagingService.allRetrieved = false;
                migrations.pagingService.pagingRetrivalOn = false;
                migrations.migrationIn = [];
            }
            if (!migrations.pagingService.pagingRetrivalOn && !migrations.pagingService.allRetrieved) {
                migrations.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'migrated_in_members',
                    parameters: {
                        limit: migrations.pagingService.limit,
                        offset: migrations.pagingService.offSet,
                        userid: migrations.currentUser.id,
                        locationId: migrations.search.locationId
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result.length === 0 || resForData.result.length <= 100) {
                        _.each(resForData.result, function (res) {
                            res.mobile_data = JSON.parse(res.mobile_data);
                        })
                        migrations.pagingService.allRetrieved = true;
                        if (migrations.pagingService.index === 1) {
                            migrations.migrationIn = resForData.result;
                        }
                    } else {
                        migrations.pagingService.allRetrieved = false;
                        if (migrations.pagingService.index > 1) {
                            migrations.migrationIn = migrations.migrationIn.concat(resForData.result);
                        } else {
                            migrations.migrationIn = resForData.result;
                        }
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    migrations.pagingService.allRetrieved = true;
                }).finally(function () {
                    migrations.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        migrations.retrieveFilteredMembers = function () {
            $state.go('.', {
                selectedIndex: migrations.selectedIndex,
                locationId: migrations.search.locationId
            }, {
                notify: false
            });
            if (migrations.selectedIndex == 0) {
                migrations.searchData(true);
            } else if (migrations.selectedIndex == 1) {
                migrations.lfumembers(true);
            } else if (migrations.selectedIndex == 2) {
                migrations.retrievedResolvedMembers(true)
            } else if (migrations.selectedIndex == 3) {
                migrations.retrieveMigrationIn(true)
            }
        };

        migrations.createNewTemp = function (migration) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to create new temporary member for the migration?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                MigrationDAO.createNewTemp(migration.id).then(function (res) {
                    toaster.pop('success', 'New Temporary Member Created');
                    migrations.migrationIn.forEach(function (member, index) {
                        if (member.id === migration.id) {
                            migrations.migrationIn.splice(index, 1);
                        }
                    })
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        }

        migrations.openMemberInfo = function (id) {
            $uibModal.open({
                templateUrl: 'app/manage/membermigration/views/migration-member-info.html',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    memberId: function () {
                        return id
                    }
                },
                controller: function ($scope, memberId, $uibModalInstance) {
                    $scope.memberNotFound = true;
                    QueryDAO.execute({
                        code: 'retrieve_member_details_for_mig',
                        parameters: {
                            memberId: memberId
                        }
                    }).then(function (res) {
                        $scope.member = res.result[0];
                    }, GeneralUtil.showMessageOnApiCallFailure)
                    $scope.confirm = function () {
                        Mask.show
                        MigrationDAO.confirmMember(migration.id, $scope.member).then(function () {
                            toaster.pop('success', "Member Confirmed Successfully");
                            migrations.retrieveMigrationIn(true);
                            $uibModalInstance.close();
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                        });
                    };
                    $scope.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    };
                }
            });
        };

        migrations.searchMembers = function (migrationId) {
            Mask.show();
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/search-members.html',
                controller: 'SearchMembersController',
                controllerAs: 'searchMembers',
                size: 'xl',
                resolve: {
                    migrationId: function () {
                        return migrationId;
                    },
                    beneficiaryName: function () {
                        return '';
                    }
                }
            });
            modalInstance.result.then(function (res) {
                if (res == 1) {
                    migrations.migrationIn.forEach(function (member, index) {
                        if (member.id === migrationId) {
                            migrations.migrationIn.splice(index, 1);
                        }
                    })
                }
                if (!(res === 'cancel' || res === 'escape key press')) {
                    throw res;
                }
            }, function () {
            });
            Mask.hide();
        };
        init();
    }
    angular.module('imtecho.controllers').controller('MigrationsController', MigrationsController);
})();
