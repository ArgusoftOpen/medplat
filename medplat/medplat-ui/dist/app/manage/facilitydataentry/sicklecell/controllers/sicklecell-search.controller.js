(function (angular) {
    function SicklecellSearchController(Mask, toaster, AuthenticateService, QueryDAO, GeneralUtil) {
        var sicklecellsearch = this;
        sicklecellsearch.search = {};
        sicklecellsearch.search.searchBy = 'member id';
        sicklecellsearch.noRecordsFound = true;
        sicklecellsearch.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        sicklecellsearch.init = () => {
            sicklecellsearch.memberDetails = [];
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                sicklecellsearch.loggedInUser = user.data;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        sicklecellsearch.toggleFilter = () => {
            if (angular.element('.filter-div').hasClass('active')) {
                sicklecellsearch.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                sicklecellsearch.modalClosed = false;
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

        sicklecellsearch.cancel = function () {
            $("#dischargeModal").modal('hide');
            sicklecellsearch.dischargeForm.$setPristine();
        }

        sicklecellsearch.retrieveFilteredMembers = () => {
            if ((sicklecellsearch.search.searchBy === 'village name' || sicklecellsearch.search.searchBy === 'organization unit') && sicklecellsearch.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                if (!sicklecellsearch.pagingService.pagingRetrivalOn && !sicklecellsearch.pagingService.allRetrieved) {
                    sicklecellsearch.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    sicklecellsearch.noRecordsFound = true;
                    if (sicklecellsearch.searchForm.$valid) {
                        var search = {};
                        search.byId = false;
                        search.byMemberId = (sicklecellsearch.search.searchBy === 'member id' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byFamilyId = (sicklecellsearch.search.searchBy === 'family id' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byMobileNumber = (sicklecellsearch.search.searchBy === 'mobile number' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byName = (sicklecellsearch.search.searchBy === 'name' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byOrganizationUnit = ((sicklecellsearch.search.searchBy === 'organization unit' || sicklecellsearch.search.searchBy === 'village name') && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byAbhaNumber = (sicklecellsearch.search.searchBy === 'abha number' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byAbhaAddress = (sicklecellsearch.search.searchBy === 'abha address' && sicklecellsearch.search.searchString !== '') ? true : false;
                        search.byFamilyMobileNumber = sicklecellsearch.search.familyMobileNumber;
                        search.searchString = sicklecellsearch.search.searchString;
                        search.locationId = sicklecellsearch.selectedLocationId;
                        let queryDto = {};
                        if (search.byMemberId) {
                            queryDto = {
                                code: 'sickle_cell_search_by_member_id',
                                parameters: {
                                    uniqueHealthId: search.searchString,
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        } else if (search.byFamilyId) {
                            queryDto = {
                                code: 'sickle_cell_search_by_family_id',
                                parameters: {
                                    familyId: search.searchString,
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        } else if (search.byOrganizationUnit) {
                            queryDto = {
                                code: 'sickle_cell_search_by_location_id',
                                parameters: {
                                    locationId: Number(search.locationId),
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        } else if (search.byAbhaAddress) {
                            queryDto = {
                                code: 'sickle_cell_search_by_abha_address',
                                parameters: {
                                    abhaAddress: search.searchString,
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        } else if (search.byAbhaNumber) {
                            queryDto = {
                                code: 'sickle_cell_search_by_abha_number',
                                parameters: {
                                    abhaNumber: search.searchString,
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        } else if (search.byMobileNumber) {
                            if (search.byFamilyMobileNumber) {
                                queryDto = {
                                    code: 'sickle_cell_search_by_family_mobile_number',
                                    parameters: {
                                        mobileNumber: search.searchString.toString(),
                                        limit: sicklecellsearch.pagingService.limit,
                                        offSet: sicklecellsearch.pagingService.offSet
                                    }
                                };
                            } else {
                                queryDto = {
                                    code: 'sickle_cell_search_by_mobile_number',
                                    parameters: {
                                        mobileNumber: search.searchString.toString(),
                                        limit: sicklecellsearch.pagingService.limit,
                                        offSet: sicklecellsearch.pagingService.offSet
                                    }
                                };
                            }
                        } else if (search.byName) {
                            queryDto = {
                                code: 'sickle_cell_search_by_name',
                                parameters: {
                                    firstName: sicklecellsearch.search.firstName,
                                    middleName: sicklecellsearch.search.middleName,
                                    lastName: sicklecellsearch.search.lastName,
                                    locationId: search.locationId,
                                    limit: sicklecellsearch.pagingService.limit,
                                    offSet: sicklecellsearch.pagingService.offSet
                                }
                            };
                        }
                        Mask.show();
                        QueryDAO.execute(queryDto).then((response) => {
                            if (response.result.length == 0 || response.result.length < sicklecellsearch.pagingService.limit) {
                                sicklecellsearch.pagingService.allRetrieved = true;
                                if (sicklecellsearch.pagingService.index === 1) {
                                    sicklecellsearch.memberDetails = response.result;
                                }
                            } else {
                                sicklecellsearch.pagingService.allRetrieved = false;
                                if (sicklecellsearch.pagingService.index > 1) {
                                    sicklecellsearch.memberDetails = sicklecellsearch.memberDetails.concat(response.result);
                                } else {
                                    sicklecellsearch.memberDetails = response.result;
                                }
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                            sicklecellsearch.pagingService.allRetrieved = true;
                        }).finally(() => {
                            sicklecellsearch.pagingService.pagingRetrivalOn = false;
                            Mask.hide();
                        });
                    }
                }
            }
        };

        sicklecellsearch.searchData = (reset) => {
            if (sicklecellsearch.searchForm.$valid) {
                if (reset) {
                    sicklecellsearch.toggleFilter();
                    sicklecellsearch.pagingService.index = 0;
                    sicklecellsearch.pagingService.allRetrieved = false;
                    sicklecellsearch.pagingService.pagingRetrivalOn = false;
                    sicklecellsearch.memberDetails = [];
                }
                sicklecellsearch.retrieveFilteredMembers();
            }
        };

        var setOffsetLimit = () => {
            sicklecellsearch.pagingService.limit = 100;
            sicklecellsearch.pagingService.offSet = sicklecellsearch.pagingService.index * 100;
            sicklecellsearch.pagingService.index = sicklecellsearch.pagingService.index + 1;
        };

        sicklecellsearch.locationSelectizeSicklecell = {
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

        sicklecellsearch.resetSearchString = () => {
            if (sicklecellsearch.search.searchString != null) {
                sicklecellsearch.search.searchString = null;
            }
            sicklecellsearch.search.familyMobileNumber = null;
            sicklecellsearch.selectedLocation = null;
            sicklecellsearch.search.firstName = null;
            sicklecellsearch.search.middleName = null;
            sicklecellsearch.search.lastName = null;
        }

        sicklecellsearch.init();
    }
    angular.module('imtecho.controllers').controller('SicklecellSearchController', SicklecellSearchController);
})(window.angular);
