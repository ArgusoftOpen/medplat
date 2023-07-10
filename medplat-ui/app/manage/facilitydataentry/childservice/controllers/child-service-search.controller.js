(function (angular) {
    function ChildServiceSearchController(Mask, AnganwadiService, toaster, PagingService, ManageChildServiceDAO, AuthenticateService, QueryDAO, GeneralUtil, SEARCH_TERM) {
        var childservicesearchcontroller = this;
        childservicesearchcontroller.selectedTab = 'childservice-search-list';
        childservicesearchcontroller.search = {};
        childservicesearchcontroller.selectedLocation = {}
        childservicesearchcontroller.minLmpSearchDate = moment().subtract('days', 308);
        childservicesearchcontroller.minEddSearchDate = moment().subtract('days', 30);
        childservicesearchcontroller.selectedLocationId = null;
        childservicesearchcontroller.terms = [
            { name: SEARCH_TERM.memberId, order: 1 },
            { name: SEARCH_TERM.familyId, order: 2 },
            { name: SEARCH_TERM.dob, order: 3, config: {requiredUptoLevel: 4, isFetchAoi: false} },
            { name: SEARCH_TERM.mobileNumber, order: 4 },
            { name: SEARCH_TERM.orgUnit, order: 5, config: {requiredUptoLevel: 4, isFetchAoi: false} },
            { name: SEARCH_TERM.villageName, order: 6 },
            { name: SEARCH_TERM.name, order: 7, config: {requiredUptoLevel: 4, isFetchAoi: true} },
            { name: SEARCH_TERM.abhaNumber, order: 8 },
            { name: SEARCH_TERM.abhaAddress, order: 9 }
        ];
        childservicesearchcontroller.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };
        childservicesearchcontroller.search.searchBy = 'member id';
        childservicesearchcontroller.noRecordsFound = true;
        childservicesearchcontroller.pagingService = PagingService.initialize();

        childservicesearchcontroller.init = function () {
            childservicesearchcontroller.memberDetails = [];
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (user) {
                childservicesearchcontroller.loggedInUserId = user.data.id;
            }).catch(function (error) {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        };

        childservicesearchcontroller.toggleFilter = function () {
            childservicesearchcontroller.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                childservicesearchcontroller.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                childservicesearchcontroller.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
            if (CKEDITOR.instances) {
                for (var ck_instance in CKEDITOR.instances) {
                    CKEDITOR.instances[ck_instance].destroy();
                }
            }
        };

        childservicesearchcontroller.retrieveFilteredMembers = function () {
            if ((childservicesearchcontroller.search.searchBy === 'village name' || childservicesearchcontroller.search.searchBy === 'organization unit') && childservicesearchcontroller.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                if (!childservicesearchcontroller.pagingService.pagingRetrivalOn && !childservicesearchcontroller.pagingService.allRetrieved) {
                    childservicesearchcontroller.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    childservicesearchcontroller.selectedTab = 'childservice-search-list';
                    childservicesearchcontroller.noRecordsFound = true;
                    if (childservicesearchcontroller.searchForm.$valid) {
                        var search = {};
                        search.byId = false;
                        search.byMemberId = (childservicesearchcontroller.search.searchBy === 'member id' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byFamilyId = (childservicesearchcontroller.search.searchBy === 'family id' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byDob = (childservicesearchcontroller.search.searchBy === 'dob' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byMobileNumber = (childservicesearchcontroller.search.searchBy === 'mobile number' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        // search.byAadharNumber = (childservicesearchcontroller.search.searchBy === 'aadhar number' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byName = (childservicesearchcontroller.search.searchBy === 'name' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byLmp = (childservicesearchcontroller.search.searchBy === 'lmp' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byEdd = (childservicesearchcontroller.search.searchBy === 'edd' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byOrganizationUnit = ((childservicesearchcontroller.search.searchBy === 'organization unit' || childservicesearchcontroller.search.searchBy === 'village name') && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byAbhaNumber = (childservicesearchcontroller.search.searchBy === 'abha number' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byAbhaAddress = (childservicesearchcontroller.search.searchBy === 'abha address' && childservicesearchcontroller.search.searchString !== '') ? true : false;
                        search.byFamilyMobileNumber = childservicesearchcontroller.search.familyMobileNumber;
                        search.searchString = childservicesearchcontroller.search.searchString;
                        search.locationId = childservicesearchcontroller.selectedLocationId;
                        let queryDto;
                        if (search.byMemberId) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_member_id',
                                parameters: {
                                    memberId: search.searchString,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byFamilyId) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_family_id',
                                parameters: {
                                    familyId: search.searchString,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byDob) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_dob',
                                parameters: {
                                    dob: moment(search.searchString).format('DD-MM-YYYY HH:mm:mm'),
                                    locationId: search.locationId,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byOrganizationUnit) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_location_id',
                                parameters: {
                                    locationId: Number(search.locationId),
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byMobileNumber) {
                            if (search.byFamilyMobileNumber) {
                                queryDto = {
                                    code: 'child_service_retrieve_child_list_by_family_mobile_number',
                                    parameters: {
                                        mobileNumber: (search.searchString).toString(),
                                        userId: childservicesearchcontroller.loggedInUserId,
                                        limit: childservicesearchcontroller.pagingService.limit,
                                        offSet: childservicesearchcontroller.pagingService.offSet
                                    }
                                };
                            } else {
                                queryDto = {
                                    code: 'child_service_retrieve_child_list_by_mobile_number',
                                    parameters: {
                                        mobileNumber: (search.searchString).toString(),
                                        userId: childservicesearchcontroller.loggedInUserId,
                                        limit: childservicesearchcontroller.pagingService.limit,
                                        offSet: childservicesearchcontroller.pagingService.offSet
                                    }
                                };
                            }
                        } else if (search.byName) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_name',
                                parameters: {
                                    name: search.searchString,
                                    locationId: search.locationId,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        }
                        else if (search.byAbhaNumber) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_abha_number',
                                parameters: {
                                    abhaNumber: search.searchString,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        }
                        else if (search.byAbhaAddress) {
                            queryDto = {
                                code: 'child_service_retrieve_child_list_by_abha_address',
                                parameters: {
                                    abhaAddress: search.searchString,
                                    limit: childservicesearchcontroller.pagingService.limit,
                                    offSet: childservicesearchcontroller.pagingService.offSet
                                }
                            };
                        }
                        Mask.show();
                        QueryDAO.execute(queryDto).then(function (response) {
                            if (response.result.length == 0 || response.result.length < childservicesearchcontroller.pagingService.limit) {
                                childservicesearchcontroller.pagingService.allRetrieved = true;
                                if (childservicesearchcontroller.pagingService.index === 1) {
                                    childservicesearchcontroller.memberDetails = response.result;
                                }
                            } else {
                                childservicesearchcontroller.pagingService.allRetrieved = false;
                                if (childservicesearchcontroller.pagingService.index > 1) {
                                    childservicesearchcontroller.memberDetails = childservicesearchcontroller.memberDetails.concat(response.result);
                                } else {
                                    childservicesearchcontroller.memberDetails = response.result;

                                }
                            }
                        }).catch(function (error) {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                            childservicesearchcontroller.pagingService.allRetrieved = true;
                        }).finally(function () {
                            childservicesearchcontroller.pagingService.pagingRetrivalOn = false;
                            Mask.hide();
                        });
                    }
                }
            }
        };

        childservicesearchcontroller.searchData = function (reset) {
            if (childservicesearchcontroller.searchForm.$valid) {
                if (reset) {
                    childservicesearchcontroller.toggleFilter();
                    childservicesearchcontroller.pagingService.index = 0;
                    childservicesearchcontroller.pagingService.allRetrieved = false;
                    childservicesearchcontroller.pagingService.pagingRetrivalOn = false;
                    childservicesearchcontroller.memberDetails = [];
                }
                childservicesearchcontroller.retrieveFilteredMembers();
            }
        };

        var setOffsetLimit = function () {
            childservicesearchcontroller.pagingService.limit = 100;
            childservicesearchcontroller.pagingService.offSet = childservicesearchcontroller.pagingService.index * 100;
            childservicesearchcontroller.pagingService.index = childservicesearchcontroller.pagingService.index + 1;
        };


        childservicesearchcontroller.locationSelectizeCs = {
            create: false,
            valueField: 'id',
            labelField: 'hierarchy',
            dropdownParent: 'body',
            highlight: true,
            searchField: ['_searchField'],
            maxItems: 1,
            render: {
                item: function (location, escape) {
                    var returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                },
                option: function (location, escape) {
                    var returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                }
            },
            onFocus: function () {
                this.onSearchChange("");
            },
            onBlur: function () {
                var selectize = this;
                var value = this.getValue();
                setTimeout(function () {
                    if (!value) {
                        selectize.clearOptions();
                        selectize.refreshOptions();
                    }
                }, 200);
            },
            load: function (query, callback) {
                var selectize = this;
                var value = this.getValue();
                if (!value) {
                    selectize.clearOptions();
                    selectize.refreshOptions();
                }
                var promise;
                var queryDto = {
                    code: 'location_search_for_web',
                    parameters: {
                        locationString: query,
                    }
                };
                if (query !== null && query !== '') {
                    promise = QueryDAO.execute(queryDto);
                    promise.then(function (res) {
                        angular.forEach(res.result, function (result) {
                            result._searchField = query;
                        });
                        callback(res.result);
                    }, function () {
                        callback();
                    });
                }
            }
        }

        childservicesearchcontroller.resetSearchString = function () {
            if (childservicesearchcontroller.search.searchString != null) {
                childservicesearchcontroller.search.searchString = null;
            }
            childservicesearchcontroller.search.familyMobileNumber = null;
            childservicesearchcontroller.selectedLocation = null;
        }

        childservicesearchcontroller.init();
    }
    angular.module('imtecho.controllers').controller('ChildServiceSearchController', ChildServiceSearchController);
})(window.angular);
