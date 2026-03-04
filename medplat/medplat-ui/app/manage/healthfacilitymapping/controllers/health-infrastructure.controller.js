(function () {
    function HealthInfrastructureController(QueryDAO, Mask, GeneralUtil, AuthenticateService, HealthInfraService, toaster, $state, $q, $uibModal, PagingForQueryBuilderService) {
        var healthInfrastructure = this;
        healthInfrastructure.manageState = 'techo.manage.healthinfrastructure';
        healthInfrastructure.search = {};
        healthInfrastructure.state = ['ACTIVE'];
        healthInfrastructure.flag = false;
        healthInfrastructure.isToShowPending = false;
        healthInfrastructure.state_based = false;
        healthInfrastructure.pagingService = PagingForQueryBuilderService.initialize();

        if ($state.current.healthFacilityLocation != null) {
            healthInfrastructure.search.locationId = $state.current.healthFacilityLocation;
        }

        if ($state.current.healthFacilityType != null) {
            healthInfrastructure.search.type = $state.current.healthFacilityType;
        }

        var retrieveAll = function () {
            if (!healthInfrastructure.pagingService.allRetrieved) {
                // healthInfrastructure.pagingService.pagingRetrivalOn = true;
                if (!healthInfrastructure.flag) {
                    healthInfrastructure.pagingService.getOffset();
                    healthInfrastructure.offSet = healthInfrastructure.pagingService.offSet;
                }
                let queryDto;
                if (healthInfrastructure.selectedTab === 0) {
                    if (healthInfrastructure.state_based) {
                        queryDto = {
                            code: 'health_infrastructure_retrieval_state_based',
                            parameters: {
                                limit: healthInfrastructure.pagingService.limit,
                                offset: healthInfrastructure.offSet,
                                locationId: healthInfrastructure.search.locationId,
                                type: healthInfrastructure.search.type,
                                state: healthInfrastructure.state
                            }
                        };
                    } else {
                        queryDto = {
                            code: 'health_infrastructure_retrieval',
                            parameters: {
                                limit: healthInfrastructure.pagingService.limit,
                                offset: healthInfrastructure.offSet,
                                locationId: healthInfrastructure.search.locationId,
                                type: healthInfrastructure.search.type
                            }
                        };
                    }

                } else if (healthInfrastructure.selectedTab === 1) {
                    if (healthInfrastructure.state_based) {
                        queryDto = {
                            code: 'my_health_infrastructure_retrieval_state_based',
                            parameters: {
                                limit: healthInfrastructure.pagingService.limit,
                                offset: healthInfrastructure.offSet,
                                state: healthInfrastructure.state
                            }
                        };
                    } else {
                        queryDto = {
                            code: 'my_health_infrastructure_retrieval',
                            parameters: {
                                limit: healthInfrastructure.pagingService.limit,
                                offset: healthInfrastructure.pagingService.offSet,
                            }
                        };
                    }
                }
                if (queryDto) {
                    Mask.show();
                    healthInfrastructure.pagingService.getNextPage(QueryDAO.execute, queryDto, healthInfrastructure.listOfHeathInfrastructures, null).then(function (resForData) {
                        if (resForData.length === 0) {
                            healthInfrastructure.pagingService.allRetrieved = true;
                        } else {
                            healthInfrastructure.pagingService.allRetrieved = false;
                        }
                        //     if (PagingForQueryBuilderService.pageNumber === 1) {
                        //         healthInfrastructure.listOfHeathInfrastructures = resForData.result;
                        //     }
                        // } else {
                        //     healthInfrastructure.pagingService.allRetrieved = false;
                        //     if (PagingForQueryBuilderService.pageNumber  > 1) {
                        //         healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.concat(resForData.result);
                        //     } else {
                        //         healthInfrastructure.listOfHeathInfrastructures = resForData;
                        //     }
                        // }
                        healthInfrastructure.listOfHeathInfrastructures = resForData;
                        //show pending hfr linking filter
                        if (healthInfrastructure.isToShowPending) {
                            healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.filter(e => {
                                return e.hfrfacilityid == null || !e.hfrfacilityid;
                            });
                        }

                    }, function (err) {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                        // healthInfrastructure.pagingService.allRetrieved = true;
                    }).finally(function () {
                        healthInfrastructure.pagingService.pagingRetrivalOn = false;
                        // healthInfrastructure.toShow();
                        Mask.hide();
                    });
                }
            }
        };

        healthInfrastructure.searchData = function (reset) {
            if (reset) {
                healthInfrastructure.pagingService.pageNumber = 0;
                healthInfrastructure.pagingService.resetOffSetAndVariables();
                healthInfrastructure.listOfHeathInfrastructures = [];
                if (!healthInfrastructure.search.locationId) {
                    healthInfrastructure.search.locationId = healthInfrastructure.currentUser.minLocationId;
                }
            }
            retrieveAll();
        };

        // var setOffsetLimit = function () {
        //     healthInfrastructure.pagingService.limit = 100;
        //     healthInfrastructure.pagingService.offSet = healthInfrastructure.pagingService.index * 100;
        //     healthInfrastructure.pagingService.index = healthInfrastructure.pagingService.index + 1;
        // };

        healthInfrastructure.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }

            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        healthInfrastructure.close = function () {
            healthInfrastructure.toggleFilter();
        };

        healthInfrastructure.navigateToEdit = function (id) {
            if (healthInfrastructure.search.locationId != null) {
                $state.current.healthFacilityLocation = healthInfrastructure.search.locationId;
            } else {
                $state.current.healthFacilityLocation = null;
            }
            if (healthInfrastructure.search.type != null) {
                $state.current.healthFacilityType = healthInfrastructure.search.type
            } else {
                $state.current.healthFacilityType = null;
            }
            $state.go(healthInfrastructure.manageState, { id: id });
        }

        healthInfrastructure.navigateToAdd = function () {
            if (healthInfrastructure.search.locationId != null) {
                $state.current.healthFacilityLocation = healthInfrastructure.search.locationId;
            } else {
                $state.current.healthFacilityLocation = null;
            }
            if (healthInfrastructure.search.type != null) {
                $state.current.healthFacilityType = healthInfrastructure.search.type
            } else {
                $state.current.healthFacilityType = null;
            }
            $state.go(healthInfrastructure.manageState);
        }

        var init = function () {
            if ($state.current.name === 'techo.manage.covid19healthinfrastructures') {
                healthInfrastructure.manageState = 'techo.manage.covid19healthinfrastructure';
            }
            let promises = [];
            promises.push(AuthenticateService.getLoggedInUser());
            promises.push(AuthenticateService.getAssignedFeature($state.current.name));
            promises.push(QueryDAO.execute({
                code: 'retrieve_field_values_for_form_field',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'infra_type'
                }
            }))
            Mask.show();
            $q.all(promises).then(function (responses) {
                healthInfrastructure.currentUser = responses[0].data;
                healthInfrastructure.currentUserRights = responses[1].featureJson;
                healthInfrastructure.types = responses[2].result;
                if (healthInfrastructure.currentUserRights.manageBasedOnLocation) {
                    healthInfrastructure.selectedTab = 0;
                } else if (healthInfrastructure.currentUserRights.manageBasedOnAssignment) {
                    healthInfrastructure.selectedTab = 1;
                }
                Mask.hide();
                healthInfrastructure.searchData(true);
            }, function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
                Mask.hide();
            });
        };

        healthInfrastructure.toggleActive = function (healthInfra) {
            var changedState = 'INACTIVE';
            if (healthInfra.state == 'INACTIVE' || healthInfra.state === null) {
                changedState = 'ACTIVE';
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state from " + healthInfra.state + ' to ' + changedState + '? ';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                console.log(healthInfra.id, healthInfra.state);
                HealthInfraService.toggleActive(parseInt(healthInfra.id), healthInfra.state === null ? "null" : healthInfra.state).then(function () {
                    healthInfrastructure.flag = true;
                    healthInfrastructure.listOfHeathInfrastructures = []

                    toaster.pop('success', 'State is successfully changed from ' + healthInfra.state + ' to ' + changedState);
                }).finally(function () {
                    Mask.hide();
                    retrieveAll();
                    healthInfrastructure.flag = false;
                });

            }, function () { });

        };


        healthInfrastructure.toShow = function () {
            healthInfrastructure.listOfHeathInfrastructures = [];
            healthInfrastructure.pagingService.resetOffSetAndVariables();
            healthInfrastructure.state_based = true;
            if (healthInfrastructure.showInactive === true && healthInfrastructure.showActive === true) {
                healthInfrastructure.state = ['ACTIVE', 'INACTIVE']

            } else if (healthInfrastructure.showActive === true) {
                healthInfrastructure.state = ['ACTIVE']

            } else if (healthInfrastructure.showInactive === true) {
                healthInfrastructure.state = ['INACTIVE'];

            } else {
                healthInfrastructure.state_based = false;

            }
            retrieveAll();
        }

        healthInfrastructure.filterList = function () {
            if (healthInfrastructure.isToShowPending) {
                healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.filter(e => {
                    return e.hfrfacilityid == null || !e.hfrfacilityid;
                });
            } else {
                healthInfrastructure.searchData(true);
            }

        }

        healthInfrastructure.showModal = (response) => {
            // let facilityType = '';
            // healthInfrastructure.types.forEach(e => {
            //     if(e.id == response.type || e.id == response.hospitaltype){
            //         facilityType = e.value;
            //     }
            // });

            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/healthfacilitymapping/views/facility-linking.modal.html',
                controller: 'FacilityLinkingController',
                controllerAs: 'ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    response: () => {
                        return response;
                    },
                    menu: () => {
                        return 0;
                    },
                    manageState: () => {
                        return healthInfrastructure.manageState;
                    },
                    types: () => {
                        return healthInfrastructure.types;
                    }
                }
            });

            //to load new data
            modalInstance.result.then(function () {
                healthInfrastructure.searchData(true);
            });
        };

        init();
    }
    angular.module('imtecho.controllers').controller('HealthInfrastructureController', HealthInfrastructureController);
})();
