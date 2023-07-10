// (function (angular) {
//     function OutPatientTreatmentSearch(Mask, toaster, AuthenticateService, QueryDAO, $uibModal, GeneralUtil, $q, $state, NdhmHipDAO, SEARCH_TERM) {
//         var outPatientTreatmentSearch = this;
//         outPatientTreatmentSearch.isHiddenSeachResult = true;
//         outPatientTreatmentSearch.search = {};
//         outPatientTreatmentSearch.search.searchBy = 'member id';
//         outPatientTreatmentSearch.searchLocation = {}
//         outPatientTreatmentSearch.searchLocationId = null;
//         outPatientTreatmentSearch.terms = [
//             { name: SEARCH_TERM.memberId, order: 1 },
//             { name: SEARCH_TERM.familyId, order: 2 },
//             { name: SEARCH_TERM.mobileNumber, order: 3 },
//             { name: SEARCH_TERM.pmjay, order: 4 },
//             { name: SEARCH_TERM.ration, order: 5 },
//             { name: SEARCH_TERM.maaVatsalya, order: 6 },
//             { name: SEARCH_TERM.dob, order: 7 },
//             { name: SEARCH_TERM.orgUnit, order: 8, config: {requiredUptoLevel: 4, isFetchAoi: false} },
//             { name: SEARCH_TERM.villageName, order: 9 },
//             { name: SEARCH_TERM.abhaNumber, order: 10 },
//             { name: SEARCH_TERM.abhaAddress, order: 11 }
//         ];
//         outPatientTreatmentSearch.noRecordsFound = true;
//         outPatientTreatmentSearch.selectedDate = outPatientTreatmentSearch.searchDate = moment();
//         outPatientTreatmentSearch.pagingService = {
//             offSet: 0,
//             limit: 100,
//             index: 0,
//             allRetrieved: false,
//             pagingRetrivalOn: false
//         };
//         outPatientTreatmentSearch.pagingService2 = {
//             offSet: 0,
//             limit: 100,
//             index: 0,
//             allRetrieved: false,
//             pagingRetrivalOn: false
//         };

//         outPatientTreatmentSearch.init = () => {
//             outPatientTreatmentSearch.searchedMemberDetails = [];
//             outPatientTreatmentSearch.registeredPatientsDetails = [];
//             outPatientTreatmentSearch.patientsDetailsForMedicines = [];
//             outPatientTreatmentSearch.referredPatientsCounts = {};
//             let promises = [];
//             promises.push(AuthenticateService.getLoggedInUser());
//             promises.push(AuthenticateService.getAssignedFeature($state.current.name));
//             Mask.show();
//             $q.all(promises).then(responses => {
//                 outPatientTreatmentSearch.loggedInUser = responses[0].data;
//                 outPatientTreatmentSearch.featureJson = responses[1].featureJson;
//                 outPatientTreatmentSearch.canManageRegistration = outPatientTreatmentSearch.featureJson.canManageRegistration;
//                 outPatientTreatmentSearch.canManageTreatment = outPatientTreatmentSearch.featureJson.canManageTreatment;
//                 outPatientTreatmentSearch.canManageMedicines = outPatientTreatmentSearch.featureJson.canManageMedicines;
//                 if (!outPatientTreatmentSearch.canManageRegistration &&
//                     !outPatientTreatmentSearch.canManageTreatment &&
//                     !outPatientTreatmentSearch.canManageMedicines) {
//                     toaster.pop('error', 'You don\'t have any authorisations to access this feature.');
//                 }
//                 if (outPatientTreatmentSearch.canManageRegistration) {
//                     outPatientTreatmentSearch.retrieveOpdRegisteredPatients();
//                     outPatientTreatmentSearch.previousSelectedTab = outPatientTreatmentSearch.selectedTab = 0;
//                 } else if (outPatientTreatmentSearch.canManageTreatment) {
//                     outPatientTreatmentSearch.retrieveOpdPatientsForTreatment();
//                     outPatientTreatmentSearch.previousSelectedTab = outPatientTreatmentSearch.selectedTab = 1;
//                 } else if (outPatientTreatmentSearch.canManageMedicines) {
//                     outPatientTreatmentSearch.retrieveOpdPatientsForMedicines();
//                     outPatientTreatmentSearch.previousSelectedTab = outPatientTreatmentSearch.selectedTab = 2;
//                 }
//             }, GeneralUtil.showMessageOnApiCallFailure)
//                 .finally(Mask.hide);
//         };

//         outPatientTreatmentSearch.retrieveOpdRegisteredPatients = (searchDate) => {
//             let dtoList = [];
//             let getRegisteredPatientsDto = {
//                 code: 'retrieve_opd_registered_patients',
//                 parameters: {
//                     userId: outPatientTreatmentSearch.loggedInUser.id,
//                     searchDate: searchDate ? moment(searchDate).format('DD-MM-YYYY') : moment(outPatientTreatmentSearch.searchDate).format('DD-MM-YYYY'),
//                     fetchPendingOnly: false
//                 },
//                 sequence: 1
//             }
//             dtoList.push(getRegisteredPatientsDto);
//             Mask.show();
//             QueryDAO.executeAll(dtoList).then(response => {
//                 outPatientTreatmentSearch.registeredPatientsDetails = response[0].result;
//             }, GeneralUtil.showMessageOnApiCallFailure)
//                 .finally(Mask.hide);
//         }

//         outPatientTreatmentSearch.retrieveOpdPatientsForTreatment = () => {
//             let dtoList = [];
//             let getRegisteredPatientsDto = {
//                 code: 'retrieve_opd_patients_for_treatment',
//                 parameters: {
//                     userId: outPatientTreatmentSearch.loggedInUser.id,
//                     fetchPendingOnly: true
//                 },
//                 sequence: 1
//             }
//             dtoList.push(getRegisteredPatientsDto);
//             Mask.show();
//             QueryDAO.executeAll(dtoList).then(response => {
//                 outPatientTreatmentSearch.registeredPatientsDetails = response[0].result;
//             }, GeneralUtil.showMessageOnApiCallFailure)
//                 .finally(Mask.hide);
//         }

//         outPatientTreatmentSearch.retrieveOpdPatientsForMedicines = () => {
//             let dtoList = [];
//             let getRegisteredPatientsDto = {
//                 code: 'retrieve_opd_patients_for_medicines',
//                 parameters: {
//                     userId: outPatientTreatmentSearch.loggedInUser.id
//                 },
//                 sequence: 1
//             }
//             dtoList.push(getRegisteredPatientsDto);
//             Mask.show();
//             QueryDAO.executeAll(dtoList).then(response => {
//                 outPatientTreatmentSearch.patientsDetailsForMedicines = response[0].result;
//             }, GeneralUtil.showMessageOnApiCallFailure)
//                 .finally(Mask.hide);
//         }

//         outPatientTreatmentSearch.toggleFilter = () => {
//             outPatientTreatmentSearch.searchForm.$setPristine();
//             if (angular.element('.filter-div').hasClass('active')) {
//                 outPatientTreatmentSearch.modalClosed = true;
//                 angular.element('body').css("overflow", "auto");
//             } else {
//                 outPatientTreatmentSearch.modalClosed = false;
//                 angular.element('body').css("overflow", "hidden");
//             }
//             angular.element('.cst-backdrop').fadeToggle();
//             angular.element('.filter-div').toggleClass('active');
//             if (CKEDITOR.instances) {
//                 for (var ck_instance in CKEDITOR.instances) {
//                     CKEDITOR.instances[ck_instance].destroy();
//                 }
//             }
//         };

//         var setOffsetLimit = (pagingService) => {
//             pagingService.limit = 100;
//             pagingService.offSet = pagingService.index * 100;
//             pagingService.index = pagingService.index + 1;
//         };

//         outPatientTreatmentSearch.retrieveFilteredMembers = (pagingService, idspReferredQueryCode) => {
//             if (['village name', 'organization unit'].includes(outPatientTreatmentSearch.search.searchBy) && !outPatientTreatmentSearch.selectedLocationId) {
//                 toaster.pop('error', 'Please select Location.')
//             } else {
//                 if (!pagingService.pagingRetrivalOn && !pagingService.allRetrieved) {
//                     pagingService.pagingRetrivalOn = true;
//                     setOffsetLimit(pagingService);
//                     var search = {};
//                     search.byMemberId = (outPatientTreatmentSearch.search.searchBy === 'member id' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byFamilyId = (outPatientTreatmentSearch.search.searchBy === 'family id' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byMobileNumber = (outPatientTreatmentSearch.search.searchBy === 'mobile number' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byPmjayNumber = (outPatientTreatmentSearch.search.searchBy === 'pmjay' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byRationNumber = (outPatientTreatmentSearch.search.searchBy === 'ration' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byMaaVatsalyaNumber = (outPatientTreatmentSearch.search.searchBy === 'maaVatsalya' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byOrganizationUnit = (['village name', 'organization unit'].includes(outPatientTreatmentSearch.search.searchBy) && outPatientTreatmentSearch.selectedLocationId) ? true : false;
//                     search.byAbhaNumber = (outPatientTreatmentSearch.search.searchBy === 'abha number' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byAbhaAddress = (outPatientTreatmentSearch.search.searchBy === 'abha address' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.byDob = (outPatientTreatmentSearch.search.searchBy === 'dob' && outPatientTreatmentSearch.search.searchString) ? true : false;
//                     search.searchString = outPatientTreatmentSearch.search.searchString;
//                     search.locationId = outPatientTreatmentSearch.selectedLocationId;
//                     let queryDtoList = [];
//                     let queryDto = {};
//                     if (search.byMemberId) {
//                         queryDto = {
//                             code: 'opd_search_by_member_id',
//                             parameters: {
//                                 uniqueHealthId: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byFamilyId) {
//                         queryDto = {
//                             code: 'opd_search_by_family_id',
//                             parameters: {
//                                 familyId: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byOrganizationUnit) {
//                         let queryCode;
//                         switch (outPatientTreatmentSearch.selectedTab) {
//                             case 0:
//                                 queryCode = 'opd_search_by_location_id';
//                                 break;
//                             case 3:
//                                 switch (outPatientTreatmentSearch.referredPatientsType) {
//                                     case 'IDSP_1':
//                                         queryCode = 'opd_search_idsp_referred_patients_by_location_id';
//                                         break;
//                                     case 'IDSP_2':
//                                         queryCode = 'opd_search_idsp_2_referred_patients_by_location_id';
//                                         break;
//                                     case 'IDSP_ALL':
//                                         queryCode = idspReferredQueryCode;
//                                         break;
//                                     case 'MYTECHO':
//                                         queryCode = 'opd_search_mytecho_referred_patients_by_location_id';
//                                         break;
//                                     case 'TRAVELLERS':
//                                         queryCode = 'opd_search_referred_travellers_covid_19_by_location_id';
//                                         break;
//                                 }
//                                 break;
//                         }
//                         queryDto = {
//                             code: queryCode,
//                             parameters: {
//                                 locationId: Number(search.locationId),
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                         if (outPatientTreatmentSearch.selectedTab === 3) {
//                             let referredPatientsCountsDto = {
//                                 code: 'opd_search_referred_patients_counts_by_location_id',
//                                 parameters: {
//                                     locationId: Number(search.locationId)
//                                 },
//                                 sequence: 2
//                             };
//                             queryDtoList.push(referredPatientsCountsDto);
//                         }
//                     }else if (search.byAbhaNumber) {
//                         queryDto = {
//                             code: 'opd_search_by_abha_number',
//                             parameters: {
//                                 abhaNumber: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } 
//                     else if (search.byAbhaAddress) {
//                         queryDto = {
//                             code: 'opd_search_by_abha_address',
//                             parameters: {
//                                 abhaAddress: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byMobileNumber) {
//                         queryDto = {
//                             code: 'opd_search_by_mobile_number',
//                             parameters: {
//                                 mobileNumber: search.searchString.toString(),
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byPmjayNumber) {
//                         queryDto = {
//                             code: 'opd_search_by_pmjay_number',
//                             parameters: {
//                                 pmjayNumber: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byRationNumber) {
//                         queryDto = {
//                             code: 'opd_search_by_ration_number',
//                             parameters: {
//                                 rationNumber: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byMaaVatsalyaNumber) {
//                         queryDto = {
//                             code: 'opd_search_by_maa_vatsalya_number',
//                             parameters: {
//                                 maavatsalyaNumber: search.searchString,
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     } else if (search.byDob) {
//                         queryDto = {
//                             code: 'opd_search_by_dob',
//                             parameters: {
//                                 dob: moment(search.searchString).format("DD-MM-YYYY"),
//                                 limit: pagingService.limit,
//                                 offSet: pagingService.offSet
//                             },
//                             sequence: 1
//                         };
//                         queryDtoList.push(queryDto);
//                     }
//                     Mask.show();
//                     QueryDAO.executeAll(queryDtoList).then(responses => {
//                         let membersDetails = responses[0].result;
//                         if (membersDetails.length == 0 || membersDetails.length < pagingService.limit) {
//                             pagingService.allRetrieved = true;
//                             outPatientTreatmentSearch.searchedMemberDetails = outPatientTreatmentSearch.searchedMemberDetails.concat(membersDetails);
//                         } else {
//                             pagingService.allRetrieved = false;
//                             outPatientTreatmentSearch.searchedMemberDetails = outPatientTreatmentSearch.searchedMemberDetails.concat(membersDetails);
//                         }
//                         if (outPatientTreatmentSearch.selectedTab === 3) {
//                             outPatientTreatmentSearch.referredPatientsCounts = responses[1].result[0];
//                         }
//                     }).catch((error) => {
//                         GeneralUtil.showMessageOnApiCallFailure(error);
//                         pagingService.allRetrieved = true;
//                     }).finally(() => {
//                         pagingService.pagingRetrivalOn = false;
//                         Mask.hide();
//                     });
//                 }
//             }
//         };

//         outPatientTreatmentSearch.resetSearchedResult = () => {
//             outPatientTreatmentSearch.pagingService.index = 0;
//             outPatientTreatmentSearch.pagingService.allRetrieved = false;
//             outPatientTreatmentSearch.pagingService.pagingRetrivalOn = false;
//             outPatientTreatmentSearch.pagingService2.index = 0;
//             outPatientTreatmentSearch.pagingService2.allRetrieved = false;
//             outPatientTreatmentSearch.pagingService2.pagingRetrivalOn = false;
//             outPatientTreatmentSearch.searchedMemberDetails = [];
//         };

//         outPatientTreatmentSearch.resetSearchString = () => {
//             if (outPatientTreatmentSearch.search.searchString) {
//                 outPatientTreatmentSearch.search.searchString = null;
//             }
//             outPatientTreatmentSearch.searchLocation = {};
//             outPatientTreatmentSearch.searchLocationId = null;
//         };

//         outPatientTreatmentSearch.searchMembers = (reset, isAssignedLocation) => {
//             if (outPatientTreatmentSearch.searchForm.$valid || (isAssignedLocation && outPatientTreatmentSearch.selectedTab === 3 && outPatientTreatmentSearch.selectedLocationId)) {
//                 if (reset) {
//                     outPatientTreatmentSearch.resetSearchedResult();
//                     outPatientTreatmentSearch.isHiddenSeachResult = false;
//                     outPatientTreatmentSearch.toggleFilter();
//                     outPatientTreatmentSearch.selectedLocationId = outPatientTreatmentSearch.searchLocationId;
//                 }
//                 outPatientTreatmentSearch.retrieveFilteredMembers(outPatientTreatmentSearch.pagingService, 'opd_search_idsp_referred_patients_by_location_id');
//                 if (outPatientTreatmentSearch.selectedTab === 3 && outPatientTreatmentSearch.referredPatientsType === 'IDSP_ALL') {
//                     outPatientTreatmentSearch.retrieveFilteredMembers(outPatientTreatmentSearch.pagingService2, 'opd_search_idsp_2_referred_patients_by_location_id');
//                 }
//             }
//         };

//         outPatientTreatmentSearch.locationSelectizeoutPatient = {
//             create: false,
//             valueField: 'id',
//             labelField: 'hierarchy',
//             dropdownParent: 'body',
//             highlight: true,
//             searchField: ['_searchField'],
//             //                options: [],
//             maxItems: 1,
//             render: {
//                 item: function (location, escape) {
//                     var returnString = "<div>" + location.hierarchy + "</div>";
//                     return returnString;
//                 },
//                 option: function (location, escape) {
//                     var returnString = "<div>" + location.hierarchy + "</div>";
//                     return returnString;
//                 }
//             },
//             onFocus: function () {
//                 this.onSearchChange("");
//             },
//             onBlur: function () {
//                 var selectize = this;
//                 var value = this.getValue();
//                 setTimeout(function () {
//                     if (!value) {
//                         selectize.clearOptions();
//                         selectize.refreshOptions();
//                     }
//                 }, 200);
//             },
//             load: function (query, callback) {
//                 var selectize = this;
//                 var value = this.getValue();
//                 if (!value) {
//                     selectize.clearOptions();
//                     selectize.refreshOptions();
//                 }
//                 var promise;
//                 var queryDto = {
//                     code: 'location_search_for_web',
//                     parameters: {
//                         locationString: query,
//                     }
//                 };
//                 promise = QueryDAO.execute(queryDto);
//                 promise.then(function (res) {
//                     angular.forEach(res.result, function (result) {
//                         result._searchField = query;
//                     });
//                     callback(res.result);
//                 }, function () {
//                     callback();
//                 });
//             }
//         }

//         outPatientTreatmentSearch.getReferredPatients = () => {
//             if (outPatientTreatmentSearch.loggedInUser.minLocationId) {
//                 outPatientTreatmentSearch.search.searchBy = 'organization unit';
//                 outPatientTreatmentSearch.selectedLocationId = outPatientTreatmentSearch.loggedInUser.minLocationId;
//                 outPatientTreatmentSearch.searchMembers(true, true);
//             }
//         }

//         outPatientTreatmentSearch.resetSearchStringAndResult = () => {
//             if (outPatientTreatmentSearch.previousSelectedTab === outPatientTreatmentSearch.selectedTab) {
//                 return;
//             }
//             outPatientTreatmentSearch.resetSearchString();
//             outPatientTreatmentSearch.resetSearchedResult();
//             outPatientTreatmentSearch.isHiddenSeachResult = true;
//             if (outPatientTreatmentSearch.selectedTab === 3) {
//                 outPatientTreatmentSearch.referredPatientsType = 'IDSP_1';
//                 outPatientTreatmentSearch.getReferredPatients();
//             }
//             outPatientTreatmentSearch.previousSelectedTab = outPatientTreatmentSearch.selectedTab;
//         }

//         outPatientTreatmentSearch.onSelectReferredSourceType = () => {
//             if (!outPatientTreatmentSearch.referredPatientsType) {
//                 return;
//             }
//             if (!outPatientTreatmentSearch.selectedLocationId && outPatientTreatmentSearch.loggedInUser.minLocationId) {
//                 outPatientTreatmentSearch.search.searchBy = 'organization unit';
//                 outPatientTreatmentSearch.selectedLocationId = outPatientTreatmentSearch.loggedInUser.minLocationId;
//             }
//             outPatientTreatmentSearch.searchMembers(true, true);
//         }

//         outPatientTreatmentSearch.registerOpdPatient = patientDetailObj => {
//             let modalInstance = $uibModal.open({
//                 windowClass: 'cst-modal',
//                 backdrop: 'static',
//                 size: 'xl',
//                 templateUrl: 'app/manage/facilitydataentry/outpatienttreatment/views/register-patient-modal.html',
//                 controllerAs: 'ctrl',
//                 controller: function ($uibModalInstance, patientDetail, loggedInUser) {
//                     let ctrl = this;
//                     ctrl.patientDetail = patientDetail;
//                     ctrl.today = new Date();
//                     ctrl.registrationDate = new Date();

//                     const _init = function () {
//                         let dtoList = [];
//                         let getHealthInfrastructuresDto = {
//                             code: 'retrieve_health_infra_for_user',
//                             parameters: {
//                                 userId: loggedInUser.id
//                             },
//                             sequence: 1
//                         }
//                         Mask.show();
//                         dtoList.push(getHealthInfrastructuresDto);
//                         QueryDAO.executeAll(dtoList).then(responses => {
//                             ctrl.healthInfrastructuresList = responses[0].result;
//                             setTimeout(function () {
//                                 $('#healthInfrastructure').trigger("chosen:updated");
//                             });
//                         }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);

//                         Mask.show()
//                         AuthenticateService.getAssignedFeature("techo.manage.outPatientTreatmentSearch").then(function (res) {
//                             ctrl.rights = res.featureJson;
//                             if (!ctrl.rights) {
//                                 ctrl.rights = {};
//                             }
//                         }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
//                             Mask.hide();
//                         })

//                         /**
//                          * Get preferred PHR Address and other PHR Address assigned to member in order to display in modal
//                          */
//                         Mask.show();
//                         NdhmHipDAO.getAllCareContextMasterDetails(patientDetailObj.memberId).then((res) => {
//                             if (res.length > 0) {
//                                 ctrl.healthIdsData = res;
//                                 ctrl.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
//                                 let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
//                                 ctrl.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
//                             }
//                         }).catch((error) => {
//                             GeneralUtil.showMessageOnApiCallFailure(error);
//                         }).finally(Mask.hide);
//                     }

//                     ctrl.submit = function () {
//                         ctrl.opdRegistrationForm.$setSubmitted();
//                         if (ctrl.opdRegistrationForm.$valid) {
//                             let currentDate = new Date();
//                             let registrationDate = new Date(ctrl.registrationDate);
//                             registrationDate.setHours(currentDate.getHours());
//                             registrationDate.setMinutes(currentDate.getMinutes());
//                             registrationDate.setSeconds(currentDate.getSeconds());

//                             let referenceId = null;
//                             let referenceType = null;

//                             switch (outPatientTreatmentSearch.selectedTab) {
//                                 case 0:
//                                     referenceType = 'DIRECT';
//                                     break;
//                                 case 3:
//                                     switch (outPatientTreatmentSearch.referredPatientsType) {
//                                         case 'IDSP_1':
//                                         case 'IDSP_2':
//                                         case 'IDSP_ALL':
//                                             referenceId = ctrl.patientDetail.idspMemberScreeningId;
//                                             referenceType = ctrl.patientDetail.idspRound === 2 ? 'IDSP_REF_2' : 'IDSP_REF';
//                                             break;
//                                         case 'MYTECHO':
//                                             referenceId = ctrl.patientDetail.mtCovidSymptomCheckerId;
//                                             referenceType = 'MYTECHO_REF';
//                                             break;
//                                         case 'TRAVELLERS':
//                                             referenceId = ctrl.patientDetail.travellersScreeningInfoId;
//                                             referenceType = 'COVID_TRAVELLERS_SCREENING';
//                                             break;
//                                     }
//                                     break;
//                             }

//                             let dtoList = [];
//                             let updateRchLocation = {
//                                 code: 'register_opd_patient',
//                                 parameters: {
//                                     memberId: ctrl.patientDetail.memberId,
//                                     registrationDate: moment(registrationDate).format("DD-MM-YYYY HH:mm:mm"),
//                                     healthInfrastructureId: ctrl.healthInfrastructureId,
//                                     referenceId,
//                                     referenceType
//                                 },
//                                 sequence: 1
//                             }
//                             dtoList.push(updateRchLocation);
//                             Mask.show();
//                             QueryDAO.executeAll(dtoList).then(function (res) {
//                                 let serviceId = res[0]["result"][0]["id"];
//                                 $uibModalInstance.close();
//                                 toaster.pop('success', 'Registration completed successfully.');
//                                 let memberObj = {
//                                     memberId: patientDetailObj.memberId,
//                                     mobileNumber: patientDetailObj.mobileNumber,
//                                     name: patientDetailObj.name,
//                                     preferredHealthId: ctrl.prefferedHealthId,
//                                     healthIdsData: ctrl.healthIdsData
//                                 }
//                                 // Generate token in order to link records to PHR Address
//                                 if (ctrl.rights.isShowHIPModal) {
//                                     outPatientTreatmentSearch.saveHealthIdDetailse(memberObj, serviceId);
//                                 }
//                             }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
//                         }
//                     }
//                     ctrl.cancel = function () {
//                         $uibModalInstance.dismiss();
//                     }

//                     _init();
//                 },
//                 resolve: {
//                     patientDetail: function () {
//                         return patientDetailObj
//                     },
//                     loggedInUser: function () {
//                         return outPatientTreatmentSearch.loggedInUser
//                     }
//                 }
//             });
//             modalInstance.result.then(() => {
//                 if (outPatientTreatmentSearch.selectedTab === 0) {
//                     outPatientTreatmentSearch.resetSearchedResult();
//                     outPatientTreatmentSearch.retrieveOpdRegisteredPatients();
//                     outPatientTreatmentSearch.isHiddenSeachResult = true;
//                 } else if (outPatientTreatmentSearch.selectedTab === 3) {
//                     outPatientTreatmentSearch.searchMembers(true, true);
//                 }
//             }, () => { })
//         }

//         // Generate token in rder to add care context
//         outPatientTreatmentSearch.saveHealthIdDetailse = function (memberObj, serviceId) {
//             let modalInstance = $uibModal.open({
//                 templateUrl: 'app/manage/ndhm/hip/views/ndhm.consent.request.modal.html',
//                 controller: 'ndhmConsentRequestModalController',
//                 controllerAs: 'ctrl',
//                 windowClass: 'cst-modal',
//                 backdrop: 'static',
//                 size: 'xl',
//                 resolve: {
//                     dataForConsentRequest: () => {
//                         return {
//                             title: "Link OPD Record To ABHA Address",
//                             memberObj: memberObj,
//                             consentRecord: "Your OPD record will be linked to your ABHA Address in OPD treatment form. Remember your consent will be expired in next 24 hours.",
//                             serviceType: "OPD",
//                             serviceId: serviceId,
//                             isTokenGenerate: true,
//                             careContextName: "Out Patient Visit"
//                         }
//                     }
//                 }
//             });
//             modalInstance.result.then(function (data) {
//             }, () => {
//             });
//         }


//         outPatientTreatmentSearch.init();
//     }
//     angular.module('imtecho.controllers').controller('OutPatientTreatmentSearch', OutPatientTreatmentSearch);
// })(window.angular);
