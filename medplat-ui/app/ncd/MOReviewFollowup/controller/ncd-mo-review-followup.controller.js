(function (angular) {
    function NcdMOReviewFollowupController(PagingService, NcdDAO, Mask, $filter, $state, GeneralUtil) {
        var ctrl = this;

        var init = function () {
            ctrl.search = {};
            ctrl.modalClosed = true
            ctrl.pagingService = PagingService.initialize();
            //ctrl.retrieveMembers();    
            ctrl.retrieveMemberList('moReviewFollowup', true);

        };

        ctrl.retrieveMembers = function (reset) {
            if (!reset) {
                ctrl.members = [];
                ctrl.pagingService.resetOffSetAndVariables();
                ctrl.isListRetrieved = false;
            }
            Mask.show();
            let criteria = {
                limit: ctrl.pagingService.limit,
                offset: ctrl.pagingService.offSet,
                healthInfrastructureType: $state.params.type,
                searchString: ctrl.search.searchString,
                searchBy: ctrl.search.searchBy,
                searchStatus: ctrl.search.searchStatus,
            };
            switch (criteria.searchBy) {
                case 'villageName':
                    criteria.searchString = ctrl.selectedLocationId
                    break;
                case 'mobileNumber':
                    if (ctrl.search.familyMobileNumber)
                        criteria.searchBy = "familyMobileNumber"
                    break;
                case 'name':
                    criteria.searchString = ctrl.search.locationToSearchByName + "#" + ctrl.search.firstName + "#" + ctrl.search.lastName;
                    if (ctrl.search.middleName)
                        criteria.searchString = criteria.searchString + "#" + ctrl.search.middleName;
                    break;
                case 'abha number':
                    criteria.searchString = ctrl.selectedAbhaNumber
                    break;
                case 'abha address':
                    criteria.searchString = ctrl.selectedAbhaNumber
                    break;
                default:
                    // do nothing;
                    break;
            }
            PagingService.getNextPage(NcdDAO.retrieveAllForStatus, criteria, ctrl.members, null).then(function (res) {
                ctrl.members = ctrl.getDiseaseSummary(res);
                if (!ctrl.modalClosed) {
                    ctrl.toggleFilter();
                }
                ctrl.isListRetrieved = true;
            }).finally(function (res) {
                Mask.hide();
            });
        };

        ctrl.searchMembers = function (reset) {
            ctrl.searchForm.$setSubmitted();
            Mask.show()
            if (ctrl.searchForm.$valid) {
                //set flag
                ctrl.isSearch = true
                //set criteria
                if (reset) {
                    ctrl.membersSearchList = [];
                    ctrl.pagingService.resetOffSetAndVariables();
                }
                let criteria = {
                    limit: ctrl.pagingService.limit,
                    offset: ctrl.pagingService.offSet,
                    searchString: ctrl.search.searchString,
                    searchBy: ctrl.search.searchBy,
                    //flag: ctrl.search.flag ? ctrl.search.flag : false
                };
                // if (criteria.searchBy === 'status' && (criteria.searchString === 'REFERRED_BACK' || criteria.searchString === 'REFERRED_NO_VISIT' || criteria.searchString === 'REFERRED_CON' || criteria.searchString === 'REFERRED_OTHER' || criteria.searchString === 'REFERRED_MO' || criteria.searchString === 'FOLLOWUP')) {
                //     criteria.searchBy = 'subStatus'
                // }
                // if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_CHANGE') {
                //     criteria.searchBy = 'medicinechange'
                // }
                // if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_NO_CHANGE') {
                //     criteria.searchBy = 'nomedicinechange'
                // }
                //call backend api
                ctrl.membersdemo = [];
                PagingService.getNextPage(NcdDAO.retreiveSearchedMembers, criteria, ctrl.membersSearchList, null).then(function (res) {
                    //store response
                    ctrl.membersSearchList = res;
                    angular.forEach(ctrl.membersSearchList, function (item, index) {
                        // if (ctrl.search.searchBy === 'status') {
                        //     ctrl.membersSearchList[index].statusDisplay = ctrl.search.searchString
                        //     if (ctrl.search.searchString === 'REFERRED_CON') {
                        //         ctrl.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //     }
                        // }
                        // else {
                        //     if (item.subStatus != null) {
                        //         ctrl.membersSearchList[index].statusDisplay = item.subStatus
                        //         if (item.subStatus === 'REFERRED_CON') {
                        //             ctrl.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //         }
                        //     }
                        //     else {
                        //         ctrl.membersSearchList[index].statusDisplay = item.status
                        //     }
                        // }
                        if (item.diseases) {
                            ctrl.membersSearchList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("B", " Breast cancer");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("C", " Cervical cancer");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("O", " Oral cancer");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("MH", " Mental Health");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("D", " Diabetes");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("G,", "");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("IA,", "");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace(",G", "");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace(",IA", "");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("G", "");
                            ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("IA", "");
                        }
                    })
                    if (!ctrl.modalClosed) {
                        ctrl.toggleFilter();
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
            else {
                ctrl.isSearch = false
                //toaster.pop("error", "Invalid search");
            }
            Mask.hide();
        };

        ctrl.retrieveMembersOnScroll = function (reset) {
            if (ctrl.isListRetrieved && ctrl.members.length < 100) return;
            ctrl.retrieveMembers(reset);
        };

        ctrl.toggleFilter = function () {
            ctrl.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                ctrl.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                ctrl.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };


        ctrl.downloadExcel = () => {
            ctrl.excelMembers = [];
            Mask.show();
            if (ctrl.isSearch) {
                if (ctrl.searchForm.$valid) {
                    let criteria = {
                        limit: null,
                        offset: 0,
                        searchString: ctrl.search.searchString,
                        searchBy: ctrl.search.searchBy,
                        flag: ctrl.search.flag ? ctrl.search.flag : false
                    };
                    //call backend api
                    NcdDAO.retreiveSearchedMembers(criteria).then(function (response) {
                        if (response != null && response.length > 0) {
                            response.forEach((member) => {
                                let statusForEsxcel = null;
                                let diseasesForExcel = null;
                                if (ctrl.search.searchBy === 'status') {
                                    statusForEsxcel = ctrl.search.searchString
                                    if (ctrl.search.searchString === 'REFERRED_CON') {
                                        statusForEsxcel = "Referred Consultant"
                                    }
                                }
                                else {
                                    if (member.subStatus != null) {
                                        statusForEsxcel = member.subStatus
                                        if (member.subStatus === 'REFERRED_CON') {
                                            statusForEsxcel = "Referred Consultant"
                                        }
                                    }
                                    else {
                                        statusForEsxcel = member.status
                                    }
                                }
                                if (member.diseases) {
                                    diseasesForExcel = member.diseases.replace("HT", " Hypertension");
                                    diseasesForExcel = diseasesForExcel.replace("B", " Breast cancer");
                                    diseasesForExcel = diseasesForExcel.replace("C", " Cervical cancer");
                                    diseasesForExcel = diseasesForExcel.replace("O", " Oral cancer");
                                    diseasesForExcel = diseasesForExcel.replace("MH", " Mental Health");
                                    diseasesForExcel = diseasesForExcel.replace("D", " Diabetes");
                                    diseasesForExcel = diseasesForExcel.replace("G,", "");
                                    diseasesForExcel = diseasesForExcel.replace("IA,", "");
                                    diseasesForExcel = diseasesForExcel.replace(",G", "");
                                    diseasesForExcel = diseasesForExcel.replace(",IA", "");
                                    diseasesForExcel = diseasesForExcel.replace("G", "");
                                    diseasesForExcel = diseasesForExcel.replace("IA", "");
                                }
                                ctrl.excelMembers.push({
                                    "Name": member.name,
                                    "Age": member.age,
                                    "Gender": member.gender,
                                    "Village": member.village,
                                    "Sub Center": member.subCenter,
                                    "Status": $filter('titlecase')(member.status),
                                    "type": $filter('titlecase')(member.subStatus),
                                    "Disease(s)": diseasesForExcel,
                                });
                            });
                            ctrl.processAndDownloadExcel(ctrl.excelMembers);
                        } else {
                            return Promise.reject({ data: { message: 'No record found' } });
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(function (res) {
                        Mask.hide();
                    });
                }
            }
            else {
                let criteria = {
                    limit: null,
                    offset: 0,
                    type: 'moReviewFollowup'
                };
                NcdDAO.retrieveMembers(criteria).then(function (response) {
                    if (response != null && response.length > 0) {
                        response.forEach((member) => {
                            let diseasesForExcel = null;
                            if (member.diseases) {
                                diseasesForExcel = member.diseases.replace("HT", " Hypertension");
                                diseasesForExcel = diseasesForExcel.replace("B", " Breast cancer");
                                diseasesForExcel = diseasesForExcel.replace("C", " Cervical cancer");
                                diseasesForExcel = diseasesForExcel.replace("O", " Oral cancer");
                                diseasesForExcel = diseasesForExcel.replace("MH", " Mental Health");
                                diseasesForExcel = diseasesForExcel.replace("D", " Diabetes");
                                diseasesForExcel = diseasesForExcel.replace("G,", "");
                                diseasesForExcel = diseasesForExcel.replace("IA,", "");
                                diseasesForExcel = diseasesForExcel.replace(",G", "");
                                diseasesForExcel = diseasesForExcel.replace(",IA", "");
                                diseasesForExcel = diseasesForExcel.replace("G", "");
                                diseasesForExcel = diseasesForExcel.replace("IA", "");
                            }
                            ctrl.excelMembers.push({
                                "Name": member.name,
                                "Age": member.age,
                                "Gender": member.gender,
                                "Village": member.village,
                                "Sub Center": member.subCenter,
                                "Status": $filter('titlecase')(member.status),
                                "Type": $filter('titlecase')(member.subStatus),
                                "Disease(s)": diseasesForExcel,
                            });
                        });
                        ctrl.processAndDownloadExcel(ctrl.excelMembers);
                    } else {
                        return Promise.reject({ data: { message: 'No record found' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function (res) {
                    Mask.hide();
                });
            }
        }

        ctrl.processAndDownloadExcel = (data) => {
            let mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            let fileName = "MO review followup list";
            let dataCopy = [];
            dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        }

        ctrl.getDiseaseSummary = (members) => {
            members.map((member) => {
                member.diseaseSummary = "";
                if (member.referredForHypertension != null && member.referredForHypertension.includes("Hypertension")) {
                    member.diseaseSummary = member.diseaseSummary.concat(member.referredForHypertension).concat("<br>");
                }
                if (member.referredForDiabetes != null && member.referredForDiabetes.includes("Diabetes")) {
                    member.diseaseSummary = member.diseaseSummary.concat(member.referredForDiabetes).concat("<br>");
                }
                if (member.referredForOral != null && member.referredForOral.includes("t")) {
                    member.diseaseSummary = member.diseaseSummary.concat('Oral cancer').concat("<br>");
                }
                if (member.referredForBreast != null && member.referredForBreast.includes("t")) {
                    member.diseaseSummary = member.diseaseSummary.concat('Breast cancer').concat("<br>");
                }
                if (member.referredForCervical != null && member.referredForCervical.includes("t")) {
                    member.diseaseSummary = member.diseaseSummary.concat('Cervical cancer')
                }
            });
            return members;
        }

        ctrl.retrieveMemberList = function (type, reset) {
            ctrl.isSearch = false;
            if (reset) {
                ctrl.membersSearchList = [];
                ctrl.pagingService.resetOffSetAndVariables();
            }
            let criteria = {
                limit: ctrl.pagingService.limit,
                offset: ctrl.pagingService.offSet,
                type: type
            };
            PagingService.getNextPage(NcdDAO.retrieveMembers, criteria, ctrl.membersSearchList, null).then(function (res) {
                //store response
                ctrl.membersSearchList = res;
                angular.forEach(ctrl.membersSearchList, function (item, index) {
                    switch (type) {
                        case 'patientList':
                            ctrl.membersSearchList[index].statusDisplay = item.status;
                            break;
                        case 'followupTreatement':
                            ctrl.membersSearchList[index].statusDisplay = item.status;
                            break;
                        default:
                            if (item.subStatus != null) {
                                ctrl.membersSearchList[index].statusDisplay = item.subStatus
                                if (item.subStatus === 'REFERRED_CON') {
                                    ctrl.membersSearchList[index].statusDisplay = "Referred Consultant"
                                }
                            }
                            else {
                                ctrl.membersSearchList[index].statusDisplay = item.status
                            }
                            break;
                    }

                    if (item.diseases) {
                        ctrl.membersSearchList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("B", " Breast cancer");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("C", " Cervical cancer");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("O", " Oral cancer");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("MH", " Mental Health");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("D", " Diabetes");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("G,", "");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("IA,", "");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace(",G", "");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace(",IA", "");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("G", "");
                        ctrl.membersSearchList[index].diseaseSummary = ctrl.membersSearchList[index].diseaseSummary.replace("IA", "");
                    }
                })
            }).finally(function (res) {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMOReviewFollowupController', NcdMOReviewFollowupController);
})(window.angular);