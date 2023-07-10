(function (angular) {
    function PncSearchController(Mask, AnganwadiService, toaster, PagingService, ManagePncDAO, AuthenticateService, QueryDAO, GeneralUtil, SEARCH_TERM) {
        var pncsearchcontroller = this;
        pncsearchcontroller.selectedTab = 'pnc-search-list';
        pncsearchcontroller.search = {};
        pncsearchcontroller.search.searchBy = 'member id';
        pncsearchcontroller.minLmpSearchDate = moment().subtract('days', 308);
        pncsearchcontroller.minEddSearchDate = moment().subtract('days', 30);
        pncsearchcontroller.selectedLocation = {}
        pncsearchcontroller.selectedLocationId = null;
        pncsearchcontroller.terms = [
            { name: SEARCH_TERM.memberId, order: 1 },
            { name: SEARCH_TERM.familyId, order: 2 },
            { name: SEARCH_TERM.mobileNumber, order: 3 },
            { name: SEARCH_TERM.orgUnit, order: 4, config: {requiredUptoLevel: 3, isFetchAoi: false} },
            { name: SEARCH_TERM.villageName, order: 5 },
            { name: SEARCH_TERM.name, order: 6, config: {requiredUptoLevel: 3, isFetchAoi: true} },
            { name: SEARCH_TERM.abhaNumber, order: 7 },
            { name: SEARCH_TERM.abhaAddress, order: 8 }
        ];
        pncsearchcontroller.noRecordsFound = true;
        pncsearchcontroller.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        pncsearchcontroller.init = function () {
            pncsearchcontroller.memberDetails = [];
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (user) {
                pncsearchcontroller.loggedInUserId = user.data.id;
            }).catch(function (error) {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        };

        pncsearchcontroller.toggleFilter = function () {
            pncsearchcontroller.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                pncsearchcontroller.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                pncsearchcontroller.modalClosed = false;
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

        pncsearchcontroller.retrieveFilteredMembers = function () {
            if ((pncsearchcontroller.search.searchBy === 'village name' || pncsearchcontroller.search.searchBy === 'organization unit') && pncsearchcontroller.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                if (!pncsearchcontroller.pagingService.pagingRetrivalOn && !pncsearchcontroller.pagingService.allRetrieved) {
                    pncsearchcontroller.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    pncsearchcontroller.selectedTab = 'pnc-search-list';
                    pncsearchcontroller.noRecordsFound = true;
                    if (pncsearchcontroller.searchForm.$valid) {
                        var search = {};
                        search.byId = false;
                        search.byMemberId = (pncsearchcontroller.search.searchBy === 'member id' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byFamilyId = (pncsearchcontroller.search.searchBy === 'family id' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byMobileNumber = (pncsearchcontroller.search.searchBy === 'mobile number' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        // search.byAadharNumber = (pncsearchcontroller.search.searchBy === 'aadhar number' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byName = (pncsearchcontroller.search.searchBy === 'name' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byLmp = (pncsearchcontroller.search.searchBy === 'lmp' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byEdd = (pncsearchcontroller.search.searchBy === 'edd' && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byOrganizationUnit = ((pncsearchcontroller.search.searchBy === 'organization unit' || pncsearchcontroller.search.searchBy === 'village name') && pncsearchcontroller.search.searchString !== '') ? true : false;
                        search.byAbhaNumber = (pncsearchcontroller.search.searchBy === 'abha number' && pncsearchcontroller.searchString !== '') ? true : false;
                    search.byAbhaAddress = (pncsearchcontroller.search.searchBy === 'abha address' && pncsearchcontroller.searchString !== '') ? true : false;
                        search.byFamilyMobileNumber = pncsearchcontroller.search.familyMobileNumber;
                        search.searchString = pncsearchcontroller.search.searchString;
                        search.locationId = pncsearchcontroller.selectedLocationId;
                        let queryDto;
                        if (search.byMemberId) {
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_member_id',
                                parameters: {
                                    memberId: search.searchString,
                                }
                            };
                        } else if (search.byFamilyId) {
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_family_id',
                                parameters: {
                                    familyId: search.searchString,
                                    limit: pncsearchcontroller.pagingService.limit,
                                    offSet: pncsearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byOrganizationUnit) {
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_location_id',
                                parameters: {
                                    locationId: Number(search.locationId),
                                    limit: pncsearchcontroller.pagingService.limit,
                                    offSet: pncsearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byAbhaNumber) {
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_abha_number',
                                parameters: {
                                    abhaNumber: search.searchString,
                                    limit: pncsearchcontroller.pagingService.limit,
                                    offSet: pncsearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byAbhaAddress) {
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_abha_address',
                                parameters: {
                                    abhaAddres: search.searchString,
                                    limit: pncsearchcontroller.pagingService.limit,
                                    offSet: pncsearchcontroller.pagingService.offSet
                                }
                            };
                        } else if (search.byMobileNumber) {
                            if (search.byFamilyMobileNumber) {
                                queryDto = {
                                    code: 'pnc_retrieve_mother_list_by_family_mobile_number',
                                    parameters: {
                                        mobileNumber: search.searchString.toString(),
                                        limit: pncsearchcontroller.pagingService.limit,
                                        offSet: pncsearchcontroller.pagingService.offSet
                                    }
                                };
                            } else {
                                queryDto = {
                                    code: 'pnc_retrieve_mother_list_by_mobile_number',
                                    parameters: {
                                        mobileNumber: search.searchString.toString(),
                                        limit: pncsearchcontroller.pagingService.limit,
                                        offSet: pncsearchcontroller.pagingService.offSet
                                    }
                                };
                            }
                        } else if (search.byName) {
                            let name = search.searchString.split(' ').map(e => `%${e}%`).join('');
                            queryDto = {
                                code: 'pnc_retrieve_mother_list_by_name',
                                parameters: {
                                    name: name,
                                    locationId: Number(search.locationId),
                                    limit: pncsearchcontroller.pagingService.limit,
                                    offSet: pncsearchcontroller.pagingService.offSet
                                }
                            };
                        }
                        Mask.show();
                        QueryDAO.execute(queryDto).then(function (response) {
                            if (response.result.length == 0 || response.result.length < pncsearchcontroller.pagingService.limit) {
                                pncsearchcontroller.pagingService.allRetrieved = true;
                                if (pncsearchcontroller.pagingService.index === 1) {
                                    pncsearchcontroller.memberDetails = response.result;
                                }
                            } else {
                                pncsearchcontroller.pagingService.allRetrieved = false;
                                if (pncsearchcontroller.pagingService.index > 1) {
                                    pncsearchcontroller.memberDetails = pncsearchcontroller.memberDetails.concat(response.result);
                                } else {
                                    pncsearchcontroller.memberDetails = response.result;

                                }
                            }
                        }).catch(function (error) {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                            pncsearchcontroller.pagingService.allRetrieved = true;
                        }).finally(function () {
                            pncsearchcontroller.pagingService.pagingRetrivalOn = false;
                            Mask.hide();
                        });
                    }
                }
            }
        };

        pncsearchcontroller.searchData = function (reset) {
            if (pncsearchcontroller.searchForm.$valid) {
                if (reset) {
                    pncsearchcontroller.toggleFilter();
                    pncsearchcontroller.pagingService.index = 0;
                    pncsearchcontroller.pagingService.allRetrieved = false;
                    pncsearchcontroller.pagingService.pagingRetrivalOn = false;
                    pncsearchcontroller.memberDetails = [];
                }
                pncsearchcontroller.retrieveFilteredMembers();
            }
        };

        var setOffsetLimit = function () {
            pncsearchcontroller.pagingService.limit = 100;
            pncsearchcontroller.pagingService.offSet = pncsearchcontroller.pagingService.index * 100;
            pncsearchcontroller.pagingService.index = pncsearchcontroller.pagingService.index + 1;
        };

        pncsearchcontroller.locationSelectizePnc = {
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

        pncsearchcontroller.resetSearchString = function () {
            if (pncsearchcontroller.search.searchString != null) {
                pncsearchcontroller.search.searchString = null;
            }
            pncsearchcontroller.search.familyMobileNumber = null;
            pncsearchcontroller.selectedLocation = null;
        }

        pncsearchcontroller.init();
    }
    angular.module('imtecho.controllers').controller('PncSearchController', PncSearchController);
})(window.angular);
