(function (angular) {
    function ANCSearchController(Mask, toaster, PagingService, AncService, AuthenticateService, QueryDAO, GeneralUtil, $q, $state, SEARCH_TERM) {
        var queform = this;
        queform.selectedTab = 'anc-search-list';
        queform.minLmpSearchDate = moment().subtract('days', 308);
        queform.maxLmpSearchDate = moment();
        queform.minEddSearchDate = moment().subtract('days', 30);
        queform.selectedLocation = {}
        queform.selectedLocationId = null;
        queform.terms = [
            { name: SEARCH_TERM.memberId, order: 1 },
            { name: SEARCH_TERM.familyId, order: 2 },
            { name: SEARCH_TERM.mobileNumber, order: 3 },
            { name: SEARCH_TERM.orgUnit, order: 4, config: {requiredUptoLevel: 3, isFetchAoi: false} },
            { name: SEARCH_TERM.villageName, order: 5 },
            { name: SEARCH_TERM.name, order: 6, config: {requiredUptoLevel: 3, isFetchAoi: true } },
            { name: SEARCH_TERM.lmp, order: 7, config: {minDate: queform.minLmpSearchDate, maxDate: queform.maxLmpSearchDate,requiredUptoLevel: 1, isFetchAoi: true } },
            { name: SEARCH_TERM.edd, order: 8, config: {minDate: queform.minEddSearchDate, requiredUptoLevel: 1, isFetchAoi: true } },
            { name: SEARCH_TERM.abhaNumber, order: 9 },
            { name: SEARCH_TERM.abhaAddress, order: 10 }
        ];
        queform.search = {};
        queform.noRecordsFound = true;
        queform.pagingService = PagingService.initialize();

        queform.init = function () {
            queform.memberDetails = [];
            let promiseList = [];
            promiseList.push(AuthenticateService.getLoggedInUser());
            promiseList.push(AuthenticateService.getAssignedFeature("techo.manage.ancSearch"));
            Mask.show();
            $q.all(promiseList).then((response) => {
                queform.loggedInUserId = response[0].data.id;
                queform.rights = response[1].featureJson;
                if (!queform.rights) {
                    queform.rights = {};
                }
                queform.consents = response[2];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        queform.toggleFilter = function () {
            queform.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                queform.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                queform.modalClosed = false;
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

        queform.retrieveFilteredMembers = function (reset) {
            if ((queform.search.searchBy === 'village name' || queform.search.searchBy === 'organization unit') && queform.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                queform.selectedTab = 'anc-search-list';
                queform.noRecordsFound = true;
                if (queform.searchForm.$valid) {
                    if (reset) {
                        queform.pagingService.resetOffSetAndVariables();
                        queform.memberDetails = [];
                    }
                    var search = {};
                    search.byId = false;
                    search.byMemberId = (queform.search.searchBy === 'member id' && queform.search.searchString !== '') ? true : false;
                    search.byFamilyId = (queform.search.searchBy === 'family id' && queform.search.searchString !== '') ? true : false;
                    search.byMobileNumber = (queform.search.searchBy === 'mobile number' && queform.search.searchString !== '') ? true : false;
                    // search.byAadharNumber = (queform.search.searchBy === 'aadhar number' && queform.search.searchString !== '') ? true : false;
                    search.byName = (queform.search.searchBy === 'name' && queform.search.searchString !== '') ? true : false;
                    search.byLmp = (queform.search.searchBy === 'lmp' && queform.search.searchString !== '') ? true : false;
                    search.byEdd = (queform.search.searchBy === 'edd' && queform.search.searchString !== '') ? true : false;
                    search.byOrganizationUnit = ((queform.search.searchBy === 'organization unit' || queform.search.searchBy === 'village name') && queform.search.searchString !== '') ? true : false;
                    // search.byAbhaNumber = (queform.search.searchBy === 'abha number' && queform.search.searchString !== '') ? true : false;
                    // search.byAbhaAddress = (queform.search.searchBy === 'abha address' && queform.search.searchString !== '') ? true : false;
                    search.byFamilyMobileNumber = queform.search.familyMobileNumber;
                    search.locationId = queform.selectedLocationId;
                    search.isAncOnly = true;
                    if (search.byLmp || search.byEdd) {
                        search.searchString = moment(queform.getDate(queform.search.searchString)).format('DD-MM-YYYY HH:mm:ss');
                    } else {
                        search.searchString = queform.search.searchString;
                    }
                    search.limit = queform.pagingService.limit;
                    search.offSet = queform.pagingService.offSet;
                    Mask.show();
                    PagingService.getNextPage(AncService.searchMembers, search, queform.memberDetails).then(function (res) {
                        queform.memberDetails = res;
                        if (queform.memberDetails.length > 0) {
                            queform.noRecordsFound = false;
                            queform.memberDetails.forEach(function (member) {
                                member.benefits = [];
                                if (member.isChiranjeeviYojnaBeneficiary) {
                                    member.benefits.push("Chiranjeevi Yojna");
                                }
                                if (member.isIayBeneficiary) {
                                    member.benefits.push("IAY");
                                }
                                if (member.isJsyBeneficiary) {
                                    member.benefits.push("JSY");
                                }
                                if (member.isKpsyBeneficiary) {
                                    member.benefits.push("KPSY");
                                }
                                if (member.benefits.length === 0) {
                                    member.benefits = "None"
                                } else {
                                    member.benefits = member.benefits.join();
                                }
                                if (!member.formatChanged) {
                                    member.edd = moment(member.edd).format("DD-MM-YYYY");
                                    member.lmpDate = moment(member.lmpDate).format("DD-MM-YYYY");
                                    member.bplFlag = member.bplFlag ? "Yes" : "No";
                                    member.formatChanged = true;
                                }
                            })
                        } else {
                            toaster.pop('danger', 'No record found')
                        }
                        if (reset) {
                            queform.toggleFilter();
                        }
                    }).catch(function (error) {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(function () {
                        Mask.hide();
                    });
                }
            }
        };

        queform.locationSelectizeAnc = {
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

        queform.resetSearchString = function () {
            if (queform.search.searchString != null) {
                queform.search.searchString = null;
            }
            queform.search.familyMobileNumber = null;
            queform.selectedLocation = {};
        }

        queform.getDate = (date) => {
            return new Date(
                date.getFullYear(),
                date.getMonth(),
                date.getDate(),
                00,
                00
            );
        }

        queform.onClickFillForm = function (memberId) {
            $state.go('techo.manage.ancformquestionsdynamic', { id: memberId });
        }

        // queform.onClickHealthIdForm = function (member) {
        //     NdhmHealthIdUtilService.onClickHealthIdForm(member, "ANC_WEB");
        // }

        // queform.onClickLinkPreviousRecord = function (member) {
        //     NdhmHipUtilService.handleLinkPreviousRecordInNdhm(member, 'ANC');
        // }

        queform.init();
    }
    angular.module('imtecho.controllers').controller('ANCSearchController', ANCSearchController);
})(window.angular);
