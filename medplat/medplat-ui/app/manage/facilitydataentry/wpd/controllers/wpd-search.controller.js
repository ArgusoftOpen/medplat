(function (angular) {
    function WpdSearchController(Mask, toaster, PagingService, ManageWpdDAO, AuthenticateService, QueryDAO, GeneralUtil, IMMUNISATIONS, $state, $q, SEARCH_TERM) {
        var queform = this;
        queform.selectedTab = 'wpd-search-list';
        queform.pendingDischargeList = [];
        queform.search = {
            searchBy: 'member id'
        };
        queform.minLmpSearchDate = moment().subtract('days', 308);
        queform.minEddSearchDate = moment().subtract('days', 30);
        queform.selectedLocation = {}
        queform.selectedLocationId = null;
        queform.terms = [
            { name: SEARCH_TERM.memberId, order: 1 },
            { name: SEARCH_TERM.familyId, order: 2 },
            { name: SEARCH_TERM.mobileNumber, order: 3 },
            { name: SEARCH_TERM.orgUnit, order: 4, config: {requiredUptoLevel: 3, isFetchAoi: false} },
            { name: SEARCH_TERM.villageName, order: 5 },
            { name: SEARCH_TERM.name, order: 6, config: {requiredUptoLevel: 3, isFetchAoi: false} },
            { name: SEARCH_TERM.lmp, order: 7, config: {minDate: queform.minLmpSearchDate, requiredUptoLevel: 1, isFetchAoi: false} },
            { name: SEARCH_TERM.edd, order: 8, config: {minDate: queform.minEddSearchDate, requiredUptoLevel: 1, isFetchAoi: false} },
            { name: SEARCH_TERM.abhaNumber, order: 9 },
            { name: SEARCH_TERM.abhaAddress, order: 10 }
        ];
        queform.noRecordsFound = true;
        queform.pagingService = PagingService.initialize();
        queform.today = moment();
        queform.immunisations = IMMUNISATIONS;

        queform.init = () => {
            queform.memberDetails = [];
            let promiseList = [];
            promiseList.push(AuthenticateService.getLoggedInUser());
            promiseList.push(AuthenticateService.getAssignedFeature("techo.manage.wpdSearch"));
            Mask.show();
            $q.all(promiseList).then((response) => {
                queform.loggedInUserId = response[0].data.id;
                queform.rights = response[1].featureJson;
                if (!queform.rights) {
                    queform.rights = {};
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        queform.toggleFilter = () => {
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

        queform.markAsDischarged = (dischargeId, deliveryDate, childDetails) => {
            $("#dischargeModal").modal({ backdrop: 'static', keyboard: false });
            let childDetailsCopy = angular.copy(childDetails);
            childDetailsCopy = childDetailsCopy && childDetailsCopy.filter((childDetail, index) => {
                try {
                    childDetailsCopy[index].givenImmunisations = JSON.parse(childDetail.givenImmunisations) || [];
                    if (childDetailsCopy[index].givenImmunisations.length === 4) return false
                } catch (error) {
                    GeneralUtil.showMessageOnApiCallFailure();
                }
                return true;
            })

            /**
             * For vaccine of BCG: 0 to 365 days of period to give vaccine
             * For vaccine of OPV_0: 0 to 15 days of period to give vaccine
             * For vaccine of Hepatitis_B and Vitamin_K: 0 to 1 day of period to give vaccine
             *
             * If, current date is lower than end date of vaccine to be given
             * Then, keep the max vaccine date as current date
             * Else, keep the max vaccine date as end date
             */
            queform.maxBCGDate = queform.today < moment(deliveryDate).add(366, 'days') ? queform.today : moment(deliveryDate).add(365, 'days');
            queform.maxOPVDate = queform.today < moment(deliveryDate).add(16, 'days') ? queform.today : moment(deliveryDate).add(15, 'days');
            queform.maxHepatitisBDate = queform.maxVitaminKDate = queform.today < moment(deliveryDate).add(2, 'days') ? queform.today : moment(deliveryDate).add(1, 'days');
            /**
             * If, current date is higher than 30 days from delivery date
             * Then, keep the max discharge date as 30 days from delivery date
             * Else, keep the max discharge date as current date
             */
            const maxDischargeDate = queform.today > moment(deliveryDate).add(31, 'days') ? moment(deliveryDate).add(30, 'days') : queform.today;

            queform.dischargeObject = {
                deliveryDate,
                id: dischargeId,
                childDetails: childDetailsCopy,
                maxDischargeDate
            };
        }

        queform.cancel = () => {
            $("#dischargeModal").modal('hide');
            queform.dischargeForm.$setPristine();
        }

        queform.checkImmunisationValidation = (dob, givenDate, currentVaccine) => {
            Mask.show();
            ManageWpdDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(givenDate).valueOf(), currentVaccine, "").then(function (response) {
                if (response.result != "" && response.result != null) {
                    if (currentVaccine === queform.immunisations.IMMUNISATION_BCG) {
                        queform.dischargeForm.bcgDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_OPV_0) {
                        queform.dischargeForm.opvDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_HEPATITIS_B_0) {
                        queform.dischargeForm.hepatitisBDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_VITAMIN_K) {
                        queform.dischargeForm.vitaminKDate.$setValidity('vaccine', false);
                    }
                } else {
                    if (currentVaccine === queform.immunisations.IMMUNISATION_BCG) {
                        queform.dischargeForm.bcgDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_OPV_0) {
                        queform.dischargeForm.opvDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_HEPATITIS_B_0) {
                        queform.dischargeForm.hepatitisBDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === queform.immunisations.IMMUNISATION_VITAMIN_K) {
                        queform.dischargeForm.vitaminKDate.$setValidity('vaccine', true);
                    }
                }
            }).catch(function (error) {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        queform.saveDischargeDetails = () => {
            queform.dischargeForm.$setSubmitted();
            if (queform.dischargeForm.$valid) {
                let payload = angular.copy(queform.dischargeObject);
                payload.isDischarged = true;
                if (Array.isArray(payload.childDetails) && payload.childDetails.length) {
                    payload.childDetails.forEach(childDetail => {
                        childDetail.immunisationDetails = [];
                        delete childDetail.givenImmunisations;
                        if (childDetail.isBcgGiven) {
                            childDetail.immunisationDetails.push({
                                immunisationGiven: queform.immunisations.IMMUNISATION_BCG,
                                immunisationDate: childDetail.bcgDate
                            });
                        }
                        if (childDetail.isOpvGiven) {
                            childDetail.immunisationDetails.push({
                                immunisationGiven: queform.immunisations.IMMUNISATION_OPV_0,
                                immunisationDate: childDetail.opvDate
                            })
                        }
                        if (childDetail.isHepatitisBGiven) {
                            childDetail.immunisationDetails.push({
                                immunisationGiven: queform.immunisations.IMMUNISATION_HEPATITIS_B_0,
                                immunisationDate: childDetail.hepatitisBDate
                            });
                        }
                        if (childDetail.isVitaminKGiven) {
                            childDetail.immunisationDetails.push({
                                immunisationGiven: queform.immunisations.IMMUNISATION_VITAMIN_K,
                                immunisationDate: childDetail.vitaminKDate
                            });
                        }
                    });
                }
                Mask.show();
                ManageWpdDAO.saveDischargeDetails(payload).then(function (response) {
                    toaster.pop('success', 'Details saved successfully');
                    $("#dischargeModal").modal('hide');
                    queform.dischargeForm.$setPristine();
                    Mask.show();
                    NdhmHipDAO.updateWpdFhirJsonByServiceId(queform.dischargeObject.id).then((res) => {
                    }).catch((error) => {
                    }).finally(Mask.hide);
                    return ManageWpdDAO.retrievePendingDischargeList(queform.loggedInUserId);
                }).then(function (response) {
                    queform.pendingDischargeList = response;
                }).catch(function (error) {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                })
            }
        }

        queform.onClickFillForm = (uniqueHealthId) => {
            $state.go('techo.manage.wpd', { id: uniqueHealthId });
        }

        // queform.onClickHealthIdForm = (member) => {
        //     NdhmHealthIdUtilService.onClickHealthIdForm(member, "WPD_WEB");
        // }

        queform.retrieveFilteredMembers = (reset) => {
            if ((queform.search.searchBy === 'village name' || queform.search.searchBy === 'organization unit') && queform.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                queform.noRecordsFound = true;
                if (queform.searchForm.$valid) {
                    if (reset) {
                        queform.pagingService.resetOffSetAndVariables();
                        queform.memberDetails = [];
                    }
                    var search = {};
                    search.byId = false;
                    search.byMemberId = (queform.search.searchBy === 'member id' && queform.search.searchString !== '') ? true : false;
                    search.byWomanId = (queform.search.searchBy === 'woman id' && queform.search.searchString !== '') ? true : false;
                    search.byFamilyId = (queform.search.searchBy === 'family id' && queform.search.searchString !== '') ? true : false;
                    search.byMobileNumber = (queform.search.searchBy === 'mobile number' && queform.search.searchString !== '') ? true : false;
                    // search.byAadharNumber = (queform.search.searchBy === 'aadhar number' && queform.search.searchString !== '') ? true : false;
                    search.byName = (queform.search.searchBy === 'name' && queform.search.searchString !== '') ? true : false;
                    search.byLmp = (queform.search.searchBy === 'lmp' && queform.search.searchString !== '') ? true : false;
                    search.byEdd = (queform.search.searchBy === 'edd' && queform.search.searchString !== '') ? true : false;
                    search.byOrganizationUnit = ((queform.search.searchBy === 'organization unit' || queform.search.searchBy === 'village name') && queform.search.searchString !== '') ? true : false;
                    search.byAbhaNumber = (queform.search.searchBy === 'abha number' && queform.search.searchString !== '') ? true : false;
                    search.byAbhaAddress = (queform.search.searchBy === 'abha address' && queform.search.searchString !== '') ? true : false;
                    search.byFamilyMobileNumber = queform.search.familyMobileNumber;
                    search.locationId = queform.selectedLocationId;
                    if (search.byLmp || search.byEdd) {
                        let eddDate = new Date(queform.search.searchString);
                        eddDate.setDate(eddDate.getDate() + 1);
                        search.searchString = moment(eddDate).format('DD-MM-YYYY');
                    } else {
                        search.searchString = queform.search.searchString;
                    }
                    search.limit = queform.pagingService.limit;
                    search.offSet = queform.pagingService.offSet;
                    Mask.show();
                    PagingService.getNextPage(ManageWpdDAO.searchMembers, search, queform.memberDetails).then(function (res) {
                        queform.memberDetails = res;
                        if (queform.memberDetails.length > 0) {
                            queform.noRecordsFound = false;
                            queform.memberDetails.forEach(function (member) {
                                member.memberName = [member.firstName, member.middleName, member.lastName].filter(Boolean).join(' ');
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

        queform.locationSelectizeWpd = {
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

        queform.wpdPendingDischargeTabSelected = () => {
            Mask.show();
            ManageWpdDAO.retrievePendingDischargeList(queform.loggedInUserId).then((response) => {
                queform.pendingDischargeList = response;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        queform.resetSearchString = () => {
            if (queform.search.searchString != null) {
                queform.search.searchString = null;
            }
            queform.search.familyMobileNumber = null;
            queform.selectedLocation = {};
        }

        queform.init();
    }
    angular.module('imtecho.controllers').controller('WpdSearchController', WpdSearchController);
})(window.angular);
