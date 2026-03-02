(function () {
    function ManageHealthInfrastructureType($rootScope, $state, QueryDAO, Mask, toaster, GeneralUtil, AuthenticateService, LocationService, $q, $uibModal) {
        var ctrl = this;

        ctrl.search = {};
        ctrl.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        var retrieveAll = function () {
            if (!ctrl.pagingService.pagingRetrivalOn && !ctrl.pagingService.allRetrieved) {
                ctrl.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                let queryDto;
                queryDto = {
                    code: 'health_infra_type_mapping',
                    parameters: {
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offSet,
                        type: ctrl.search.type
                    }
                };
                if (queryDto) {
                    Mask.show();
                    QueryDAO.execute(queryDto).then(function (resForData) {
                        ctrl.pagingService.allRetrieved = true;
                        ctrl.listOfHeathInfrastructures = resForData.result;
                    }, function (err) {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                        ctrl.pagingService.allRetrieved = true;
                    }).finally(function () {
                        ctrl.pagingService.pagingRetrivalOn = false;
                        Mask.hide();
                    });
                }
            }
        };

        ctrl.searchData = function (reset) {
            if (reset) {
                ctrl.pagingService.index = 0;
                ctrl.pagingService.allRetrieved = false;
                ctrl.pagingService.pagingRetrivalOn = false;
                ctrl.listOfHeathInfrastructures = [];
                if (!ctrl.search.locationId) {
                    ctrl.search.locationId = ctrl.currentUser.minLocationId;
                }
            }
            retrieveAll();
        };

        var setOffsetLimit = function () {
            ctrl.pagingService.limit = 100;
            ctrl.pagingService.offSet = ctrl.pagingService.index * 100;
            ctrl.pagingService.index = ctrl.pagingService.index + 1;
        };

        ctrl.close = function () {
            ctrl.toggleFilter();
        };

        var openAddMappingModal = function (feature) {
            var defer = $q.defer();

            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/healthfacilitymapping/views/update-health-infrastructure-mapping.modal.html',
                controller: 'UpdateHealthInfraMappingeModalController',
                controllerAs: 'ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    mapping: function () {
                        return angular.copy(feature);
                    }
                }
            });
            modalInstance.result.then(function (type) {
                defer.resolve();

            }, function () {
                defer.reject();
            });
            return defer.promise;
        };

        ctrl.saveMapping = function (feature) {
            openAddMappingModal(feature).then(function () {
                ctrl.searchData(true);
            });
        }

        var init = function () {
            let promises = [];
            promises.push(AuthenticateService.getLoggedInUser());
            Mask.show();
            $q.all(promises).then(function (responses) {
                ctrl.currentUser = responses[0].data;
                Mask.hide();
                ctrl.searchData(true);
            }, function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
                Mask.hide();
            });
        };

        ctrl.deleteMapping = function (id, is_active, is_archive) {
            var queryDto = {
                code: 'delete_health_infra_mapping',
                parameters: {
                    id: id,
                    is_active: is_active,
                    is_archive: is_archive,
                    userId: $rootScope.loggedInUserId || -1
                }
            };

            Mask.show();
            QueryDAO.execute(queryDto).then(function (res) {
                toaster.pop('success', 'Mapping Deleted Successfully.');
                ctrl.searchData(true);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('ManageHealthInfrastructureType', ManageHealthInfrastructureType);
})();
