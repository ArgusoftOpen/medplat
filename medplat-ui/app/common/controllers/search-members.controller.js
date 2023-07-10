(function (angular) {
    function SearchMembersController(PagingService, MigrationDAO, Mask, $uibModal, GeneralUtil, $state, toaster, $uibModalInstance, migrationId, beneficiaryName, QueryDAO) {
        var searchMembers = this;
        searchMembers.beneficiaryName = beneficiaryName;
        var init = function () {
            searchMembers.search = {};
            searchMembers.modalClosed = true
            searchMembers.pagingService = PagingService.initialize();
            searchMembers.type = $state.params.type;
        };

        searchMembers.retrieveMembers = function (reset) {
            if (searchMembers.search.searchString) {
                if (!reset) {
                    searchMembers.members = [];
                    searchMembers.pagingService.resetOffSetAndVariables();
                }
                Mask.show();
                let criteria = {
                    limit: searchMembers.pagingService.limit,
                    offset: searchMembers.pagingService.offSet,
                    action: 'searchMembers',
                    searchString: searchMembers.search.searchString,
                    searchBy: searchMembers.search.searchBy

                }
                if (criteria.searchBy == 'mobileNumber' && searchMembers.search.familyMobileNumber) {
                    criteria.searchBy = "familyMobileNumber"
                }
                if (criteria.searchBy === 'name') {
                    if (searchMembers.search.middleName) {
                        criteria.searchString = searchMembers.search.firstName + " " + searchMembers.search.lastName + " " + searchMembers.search.middleName;
                    } else if (searchMembers.search.firstName && searchMembers.search.lastName) {
                        criteria.searchString = searchMembers.search.firstName + " " + searchMembers.search.lastName;
                    }
                }
                if (criteria.searchBy === 'orgUnit') {
                    if (searchMembers.ashaAreaSelected) {
                        criteria.searchString = searchMembers.ashaAreaId;
                    } else if (searchMembers.villageSelected) {
                        criteria.searchString = searchMembers.villageId
                    }
                }
                PagingService.getNextPage(MigrationDAO.searchMembers, criteria, searchMembers.members, null).then(function (res) {
                    searchMembers.members = res;
                    if (!searchMembers.modalClosed) {
                        searchMembers.toggleFilter();
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
        };

        searchMembers.searchMembersInModalInstance = function () {
            if (searchMembers.searchForm.$valid) {
                searchMembers.retrieveMembers();
            }
        }

        searchMembers.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                searchMembers.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                searchMembers.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        searchMembers.confirm = function (member) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to confirm this member?";
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                MigrationDAO.confirmMember(migrationId, member).then(function () {
                    toaster.pop('success', "Member Confirmed Successfully");
                    $state.go('techo.manage.migrations', { selectedIndex: 3 });
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });

            }, function () { });
        };

        searchMembers.updateUser = function (member) {
            if (!!member) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/confirmation.modal.html',
                    controller: 'ConfirmModalController',
                    windowClass: 'cst-modal',
                    size: 'med',
                    resolve: {
                        message: function () {
                            return "Are you sure you want to confirm this member?";
                        }
                    }
                });
                modalInstance.result.then(function () {
                    Mask.show();
                    MigrationDAO.confirmMember(migrationId, member).then(function () {
                        toaster.pop('success', "Member Confirmed Successfully");
                        $uibModalInstance.close(1);
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                    $uibModalInstance.close(1);
                }, function () { });
            } else {
                toaster.pop('error', "Please select the member");
            }
        };

        searchMembers.fetchVillages = function () {
            delete searchMembers.villageId;
            searchMembers.villageSelected = false;
            if (searchMembers.selectedLocation && searchMembers.selectedLocation.finalSelected.level >= 4 && searchMembers.selectedLocation.finalSelected.optionSelected) {
                QueryDAO.execute({
                    code: 'retrieve_location_by_level_parent',
                    parameters: {
                        parentId: searchMembers.selectedLocation.finalSelected.optionSelected.id,
                        level: 7
                    }
                }).then(function (res) {
                    searchMembers.villages = res.result;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        searchMembers.fetchAshaArea = function () {
            let selectedParentId = null;
            searchMembers.ashaAreaSelected = false;
            delete searchMembers.ashaAreaId;
            if (searchMembers.selectedLocation && searchMembers.selectedLocation.finalSelected.level >= 4 && searchMembers.selectedLocation.finalSelected.optionSelected) {
                if (searchMembers.villageId) {
                    selectedParentId = searchMembers.villageId
                } else {
                    selectedParentId = searchMembers.selectedLocation.finalSelected.optionSelected.id
                }
                QueryDAO.execute({
                    code: 'retrieve_location_by_level_parent',
                    parameters: {
                        parentId: selectedParentId,
                        level: 8
                    }
                }).then(function (res) {
                    searchMembers.ashaAreas = res.result;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        searchMembers.resetValidation = function () {
            searchMembers.searchForm.$setPristine();
        }

        searchMembers.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        init();
    }
    angular.module('imtecho.controllers').controller('SearchMembersController', SearchMembersController);
})(window.angular);
