(function (angular) {
    function FpChangeSearch(Mask, toaster, AuthenticateService, QueryDAO, $uibModal, PagingForQueryBuilderService, GeneralUtil) {
        let fpchangesearch = this;

        fpchangesearch.init = () => {
            fpchangesearch.search = {
                searchBy: 'member id'
            };
            fpchangesearch.memberDetails = [];
            fpchangesearch.pagingService = PagingForQueryBuilderService.initialize();
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                fpchangesearch.loggedInUserId = user.data.id;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        fpchangesearch.toggleFilter = () => {
            if (angular.element('.filter-div').hasClass('active')) {
                fpchangesearch.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                fpchangesearch.modalClosed = false;
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

        fpchangesearch.cancel = () => {
            $("#dischargeModal").modal('hide');
            fpchangesearch.dischargeForm.$setPristine();
        }

        fpchangesearch.retrieveFilteredMembers = (reset, toggleFilter) => {
            if (reset) {
                fpchangesearch.pagingService = PagingForQueryBuilderService.initialize();
                fpchangesearch.memberDetails = [];
            }
            if ((fpchangesearch.search.searchBy === 'village name' || fpchangesearch.search.searchBy === 'organization unit') && fpchangesearch.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                if (fpchangesearch.searchForm.$valid) {
                    if (toggleFilter) {
                        fpchangesearch.toggleFilter();
                    }
                    let search = {};
                    search.byId = false;
                    search.byMemberId = (fpchangesearch.search.searchBy === 'member id' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byFamilyId = (fpchangesearch.search.searchBy === 'family id' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byMobileNumber = (fpchangesearch.search.searchBy === 'mobile number' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byName = (fpchangesearch.search.searchBy === 'name' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byLmp = (fpchangesearch.search.searchBy === 'lmp' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byEdd = (fpchangesearch.search.searchBy === 'edd' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byOrganizationUnit = ((fpchangesearch.search.searchBy === 'organization unit' || fpchangesearch.search.searchBy === 'village name') && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byAbhaNumber = (fpchangesearch.search.searchBy === 'abha number' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byAbhaAddress = (fpchangesearch.search.searchBy === 'abha address' && fpchangesearch.search.searchString !== '') ? true : false;
                    search.byFamilyMobileNumber = fpchangesearch.search.familyMobileNumber;
                    search.searchString = fpchangesearch.search.searchString;
                    search.locationId = fpchangesearch.selectedLocationId;
                    let queryDto;
                    if (search.byMemberId) {
                        queryDto = {
                            code: 'fp_search_by_member_id',
                            parameters: {
                                uniqueHealthId: search.searchString,
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    } else if (search.byFamilyId) {
                        queryDto = {
                            code: 'fp_search_by_family_id',
                            parameters: {
                                familyId: search.searchString,
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    } else if (search.byAbhaNumber) {
                        queryDto = {
                            code: 'fp_search_by_abha_number',
                            parameters: {
                                abhaNumber: search.searchString,
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    } else if (search.byAbhaAddress) {
                        queryDto = {
                            code: 'fp_search_by_abha_address',
                            parameters: {
                                abhaAddress: search.searchString,
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    } else if (search.byOrganizationUnit) {
                        queryDto = {
                            code: 'fp_search_by_location_id',
                            parameters: {
                                locationId: Number(search.locationId),
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    } else if (search.byMobileNumber) {
                        if (search.byFamilyMobileNumber) {
                            queryDto = {
                                code: 'fp_search_by_family_mobile_number',
                                parameters: {
                                    mobileNumber: search.searchString.toString(),
                                    limit: fpchangesearch.pagingService.limit,
                                    offSet: fpchangesearch.pagingService.offSet
                                }
                            };
                        } else {
                            queryDto = {
                                code: 'fp_search_by_mobile_number',
                                parameters: {
                                    mobileNumber: search.searchString.toString(),
                                    limit: fpchangesearch.pagingService.limit,
                                    offSet: fpchangesearch.pagingService.offSet
                                }
                            };
                        }
                    } else if (search.byName) {
                        queryDto = {
                            code: 'fp_search_by_name',
                            parameters: {
                                firstName: fpchangesearch.search.firstName,
                                middleName: fpchangesearch.search.middleName,
                                lastName: fpchangesearch.search.lastName,
                                locationId: search.locationId,
                                limit: fpchangesearch.pagingService.limit,
                                offSet: fpchangesearch.pagingService.offSet
                            }
                        };
                    }
                    let memberDetails = fpchangesearch.memberDetails;
                    Mask.show();
                    PagingForQueryBuilderService.getNextPage(QueryDAO.execute, queryDto, memberDetails, null).then((response) => {
                        fpchangesearch.memberDetails = response;
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                }
            }
        };

        fpchangesearch.iucdRemoval = (member) => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/facilitydataentry/familyplanning/views/iucd-removal.modal.html',
                controller: 'IucdRemovalModal',
                windowClass: 'cst-modal',
                size: 'md',
                resolve: {
                    memberData: () => {
                        return {
                            memberId: member.memberId,
                            fpOperateDate: member.memberFpOperateDate,
                            memberCurrentFpValue: member.memberCurrentFp
                        }
                    }
                }
            });
            modalInstance.result.then((iucdObject) => {
                if (Object.keys(iucdObject).length) {
                    member.memberCurrentFp = null;
                    member.memberFpOperateDate = null;
                } else {
                    toaster.pop('error', 'Error in updating data');
                }
            }, () => {
            });
        }

        fpchangesearch.changeFpMethod = (member) => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/facilitydataentry/familyplanning/views/change-family-planning-method.modal.html',
                controller: 'ChangeFpMethodModal',
                windowClass: 'cst-modal',
                size: 'md',
                resolve: {
                    memberData: () => {
                        return {
                            memberId: member.memberId,
                            fpOperateDate: member.memberFpOperateDate,
                            memberCurrentFpValue: member.memberCurrentFp,
                            lastDeliveryDate: member.lastDeliveryDate
                        }
                    }
                }
            });
            modalInstance.result.then((familyPlanningObject) => {
                if (Object.keys(familyPlanningObject).length) {
                    member.memberCurrentFp = familyPlanningObject.familyPlanningMethod;
                    member.memberFpOperateDate = familyPlanningObject.fpInsertOperateDate;
                } else {
                    toaster.pop('error', 'Error in updating family planning method');
                }
            }, () => {
            });
        }

        fpchangesearch.locationSelectizePnc = {
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

        fpchangesearch.resetSearchString = () => {
            if (fpchangesearch.search.searchString != null) {
                fpchangesearch.search.searchString = null;
            }
            fpchangesearch.search.familyMobileNumber = null;
            fpchangesearch.selectedLocation = null;
        }

        fpchangesearch.init();
    }
    angular.module('imtecho.controllers').controller('FpChangeSearch', FpChangeSearch);
})(window.angular);
