(function () {
    function HealthInfrastructureController(QueryDAO, Mask, GeneralUtil, AuthenticateService, $state, $q, $uibModal) {
        var healthInfrastructure = this;
        healthInfrastructure.manageState = 'techo.manage.healthinfrastructure';
        healthInfrastructure.search = {};
        healthInfrastructure.isToShowPending = false;
        healthInfrastructure.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        if ($state.current.healthFacilityLocation != null) {
            healthInfrastructure.search.locationId = $state.current.healthFacilityLocation;
        }

        if ($state.current.healthFacilityType != null) {
            healthInfrastructure.search.type = $state.current.healthFacilityType;
        }

        var retrieveAll = function () {
            if (!healthInfrastructure.pagingService.pagingRetrivalOn && !healthInfrastructure.pagingService.allRetrieved) {
                healthInfrastructure.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                let queryDto;
                if (healthInfrastructure.selectedTab === 0) {
                    queryDto = {
                        code: 'health_infrastructure_retrieval',
                        parameters: {
                            limit: healthInfrastructure.pagingService.limit,
                            offset: healthInfrastructure.pagingService.offSet,
                            locationId: healthInfrastructure.search.locationId,
                            type: healthInfrastructure.search.type
                        }
                    };
                } else if (healthInfrastructure.selectedTab === 1) {
                    queryDto = {
                        code: 'my_health_infrastructure_retrieval',
                        parameters: {
                            limit: healthInfrastructure.pagingService.limit,
                            offset: healthInfrastructure.pagingService.offSet,
                        }
                    };
                }
                if (queryDto) {
                    Mask.show();
                    QueryDAO.execute(queryDto).then(function (resForData) {
                        if (resForData.result.length === 0) {
                            healthInfrastructure.pagingService.allRetrieved = true;
                            if (healthInfrastructure.pagingService.index === 1) {
                                healthInfrastructure.listOfHeathInfrastructures = resForData.result;
                            }
                        } else {
                            healthInfrastructure.pagingService.allRetrieved = false;
                            if (healthInfrastructure.pagingService.index > 1) {
                                healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.concat(resForData.result);
                            } else {
                                healthInfrastructure.listOfHeathInfrastructures = resForData.result;
                            }
                        }
                        //show pending hfr linking filter
                        // if (healthInfrastructure.isToShowPending) {
                        //     healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.filter(e => {
                        //         return e.hfrfacilityid == null || !e.hfrfacilityid;
                        //     });
                        // }

                    }, function (err) {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                        healthInfrastructure.pagingService.allRetrieved = true;
                    }).finally(function () {
                        healthInfrastructure.pagingService.pagingRetrivalOn = false;
                        Mask.hide();
                    });
                }
            }
        };

        healthInfrastructure.searchData = function (reset) {
            if (reset) {
                healthInfrastructure.pagingService.index = 0;
                healthInfrastructure.pagingService.allRetrieved = false;
                healthInfrastructure.pagingService.pagingRetrivalOn = false;
                healthInfrastructure.listOfHeathInfrastructures = [];
                if (!healthInfrastructure.search.locationId) {
                    healthInfrastructure.search.locationId = healthInfrastructure.currentUser.minLocationId;
                }
            }
            retrieveAll();
        };

        var setOffsetLimit = function () {
            healthInfrastructure.pagingService.limit = 100;
            healthInfrastructure.pagingService.offSet = healthInfrastructure.pagingService.index * 100;
            healthInfrastructure.pagingService.index = healthInfrastructure.pagingService.index + 1;
        };

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


        // healthInfrastructure.filterList = function () {
        //     if (healthInfrastructure.isToShowPending) {
        //         healthInfrastructure.listOfHeathInfrastructures = healthInfrastructure.listOfHeathInfrastructures.filter(e => {
        //             return e.hfrfacilityid == null || !e.hfrfacilityid;
        //         });
        //     } else {
        //         healthInfrastructure.searchData(true);
        //     }

        // }

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
