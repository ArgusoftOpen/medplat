(function (angular) {
    function NcdMOReviewScreenController(PagingService, NcdDAO, Mask, $filter, $state, toaster, GeneralUtil) {
        var ncdrs = this;

        var init = function () {
            ncdrs.search = {};
            ncdrs.modalClosed = true
            ncdrs.pagingService = PagingService.initialize();
            //ncdrs.retrieveMembers();    
            ncdrs.retrieveMemberList('patientList', true);

        };

        ncdrs.retrieveMembers = function (reset) {
            if (!reset) {
                ncdrs.members = [];
                ncdrs.pagingService.resetOffSetAndVariables();
                ncdrs.isListRetrieved = false;
            }
            Mask.show();
            let criteria = {
                limit: ncdrs.pagingService.limit,
                offset: ncdrs.pagingService.offSet,
                healthInfrastructureType: $state.params.type,
                searchString: ncdrs.search.searchString,
                searchBy: ncdrs.search.searchBy,
                searchStatus: ncdrs.search.searchStatus,
            };
            switch (criteria.searchBy) {
                case 'villageName':
                    criteria.searchString = ncdrs.selectedLocationId
                    break;
                case 'mobileNumber':
                    if (ncdrs.search.familyMobileNumber)
                        criteria.searchBy = "familyMobileNumber"
                    break;
                case 'name':
                    criteria.searchString = ncdrs.search.locationToSearchByName + "#" + ncdrs.search.firstName + "#" + ncdrs.search.lastName;
                    if (ncdrs.search.middleName)
                        criteria.searchString = criteria.searchString + "#" + ncdrs.search.middleName;
                    break;
                case 'abha number':
                    criteria.searchString = ncdrs.selectedAbhaNumber
                    break;
                case 'abha address':
                    criteria.searchString = ncdrs.selectedAbhaNumber
                    break;
                default:
                    // do nothing;
                    break;
            }
            PagingService.getNextPage(NcdDAO.retrieveAllForStatus, criteria, ncdrs.members, null).then(function (res) {
                ncdrs.members = ncdrs.getDiseaseSummary(res);
                if (!ncdrs.modalClosed) {
                    ncdrs.toggleFilter();
                }
                ncdrs.isListRetrieved = true;
            }).finally(function (res) {
                Mask.hide();
            });
        };

        ncdrs.searchMembers = function (reset) {
            ncdrs.searchForm.$setSubmitted();
            Mask.show()
            if (ncdrs.searchForm.$valid) {
                //set flag
                ncdrs.isSearch = true
                //set criteria
                if (reset) {
                    ncdrs.membersSearchList = [];
                    ncdrs.pagingService.resetOffSetAndVariables();
                }
                let criteria = {
                    limit: ncdrs.pagingService.limit,
                    offset: ncdrs.pagingService.offSet,
                    searchString: ncdrs.search.searchString,
                    searchBy: ncdrs.search.searchBy,
                    flag: ncdrs.search.flag ? ncdrs.search.flag : false
                };
                // if (criteria.searchBy === 'status' && (criteria.searchString === 'REFERRED_BACK' || criteria.searchString === 'REFERRED_NO_VISIT' || criteria.searchString === 'REFERRED_CON' || criteria.searchString === 'REFERRED_OTHER' || criteria.searchString === 'REFERRED_MO' || criteria.searchString === 'FOLLOWUP')) {
                //     criteria.searchBy = 'subStatus'
                // }
                if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_CHANGE') {
                    criteria.searchBy = 'medicinechange'
                }
                if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_NO_CHANGE') {
                    criteria.searchBy = 'nomedicinechange'
                }
                //call backend api
                ncdrs.membersdemo = [];
                PagingService.getNextPage(NcdDAO.retreiveSearchedMembers, criteria, ncdrs.membersSearchList, null).then(function (res) {
                    //store response
                    ncdrs.membersSearchList = res;
                    angular.forEach(ncdrs.membersSearchList, function (item, index) {
                        // if (ncdrs.search.searchBy === 'status') {
                        //     angular.forEach(ncdrs.search.searchString, function (item1, index1) {
                        //         if (item1 === ncdrs.membersSearchList[index].subStatus) {
                        //             ncdrs.membersSearchList[index].statusDisplay = ncdrs.membersSearchList[index].subStatus;
                        //             if (ncdrs.membersSearchList[index].subStatus === 'REFERRED_CON') {
                        //                 ncdrs.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //             }
                        //         }
                        //         else{
                        //             ncdrs.membersSearchList[index].statusDisplay = ncdrs.membersSearchList[index].status;
                        //             if (ncdrs.membersSearchList[index].status === 'SCREENED') {
                        //                 ncdrs.membersSearchList[index].statusDisplay = "Suspected"
                        //             }
                        //         }
                        //     })
                        //     // if (mlisting.search.searchString === 'REFERRED_CON') {
                        //     //     mlisting.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //     // }
                        //     // else if(mlisting.search.searchString === 'SCREENED') {
                        //     //     mlisting.membersSearchList[index].statusDisplay = "Suspected"
                        //     // }
                        // }
                        // else {
                        //     if (item.subStatus != null) {
                        //         ncdrs.membersSearchList[index].statusDisplay = item.subStatus
                        //         if (item.subStatus === 'REFERRED_CON') {
                        //             ncdrs.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //         }
                        //     }
                        //     else if (item.status === 'SCREENED') {
                        //         ncdrs.membersSearchList[index].statusDisplay = "Suspected"
                        //     }
                        //     else {
                        //         ncdrs.membersSearchList[index].statusDisplay = item.status
                        //     }
                        // }
                        if (item.diseases) {
                            ncdrs.membersSearchList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("B", " Breast cancer");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("C", " Cervical cancer");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("O", " Oral cancer");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("MH", " Mental Health");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("D", " Diabetes");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("G,", "");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("IA,", "");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace(",G", "");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace(",IA", "");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("G", "");
                            ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("IA", "");
                        }
                    })
                    if (!ncdrs.modalClosed) {
                        ncdrs.toggleFilter();
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
            else {
                ncdrs.isSearch = false
                //toaster.pop("error", "Invalid search");
            }
            Mask.hide();
        };

        ncdrs.retrieveMembersOnScroll = function (reset) {
            if (ncdrs.isListRetrieved && ncdrs.members.length < 100) return;
            ncdrs.retrieveMembers(reset);
        };

        ncdrs.toggleFilter = function () {
            ncdrs.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                ncdrs.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                ncdrs.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };


        ncdrs.downloadExcel = () => {
            ncdrs.excelMembers = [];
            Mask.show();
            if (ncdrs.isSearch) {
                if (ncdrs.searchForm.$valid) {
                    let criteria = {
                        limit: null,
                        offset: 0,
                        searchString: ncdrs.search.searchString,
                        searchBy: ncdrs.search.searchBy,
                        flag: ncdrs.search.flag ? ncdrs.search.flag : false
                    };
                    // if (criteria.searchBy === 'status' && (criteria.searchString === 'REFERRED_BACK' || criteria.searchString === 'REFERRED_NO_VISIT' || criteria.searchString === 'REFERRED_CON' || criteria.searchString === 'REFERRED_OTHER' || criteria.searchString === 'REFERRED_MO' || criteria.searchString === 'FOLLOWUP')) {
                    //     criteria.searchBy = 'subStatus'
                    //}
                    if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_CHANGE') {
                        criteria.searchBy = 'medicinechange'
                    }
                    if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_NO_CHANGE') {
                        criteria.searchBy = 'nomedicinechange'
                    }
                    //call backend api
                    NcdDAO.retreiveSearchedMembers(criteria).then(function (response) {
                        if (response != null && response.length > 0) {
                            response.forEach((member) => {
                                let statusForEsxcel = null;
                                let diseasesForExcel = null;
                                // if (ncdrs.search.searchBy === 'status') {
                                //     // statusForEsxcel = ncdrs.search.searchString
                                //     // if (ncdrs.search.searchString === 'REFERRED_CON') {
                                //     //     statusForEsxcel = "Referred Consultant"
                                //     // }
                                //     // else if (ncdrs.search.searchString === 'SCREENED') {
                                //     //     statusForEsxcel = "Suspected"
                                //     // }
                                //     angular.forEach(ncdrs.search.searchString, function (item1, index1) {
                                //         if (item1 === member.subStatus) {
                                //             statusForEsxcel = member.subStatus;
                                //             if (member.subStatus === 'REFERRED_CON') {
                                //                 statusForEsxcel = "Referred Consultant"
                                //             }
                                //         }
                                //         else{
                                //             statusForEsxcel = member.status;
                                //             if (member.status === 'SCREENED') {
                                //                 statusForEsxcel = "Suspected"
                                //             }
                                //         }
                                //     })
                                // }
                                // else {
                                //     if (member.subStatus != null) {
                                //         statusForEsxcel = member.subStatus
                                //         if (member.subStatus === 'REFERRED_CON') {
                                //             statusForEsxcel = "Referred Consultant"
                                //         }
                                //     }
                                //     else if (member.status === 'SCREENED') {
                                //         statusForEsxcel = "Suspected"
                                //     }
                                //     else {
                                //         statusForEsxcel = member.status
                                //     }
                                // }
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
                                ncdrs.excelMembers.push({
                                    "Name": member.name,
                                    "Age": member.age,
                                    "Gender": member.gender,
                                    "Village": member.village,
                                    "Sub Center": member.subCenter,
                                    "Status": $filter('titlecase')(member.status),
                                    "Disease(s)": diseasesForExcel,
                                });
                            });
                            ncdrs.processAndDownloadExcel(ncdrs.excelMembers);
                        } else {
                            return Promise.reject({ data: { message: 'No record found' } });
                        }
                    }).finally(function (res) {
                        Mask.hide();
                    });
                }
            }
            else {
                let criteria = {
                    limit: null,
                    offset: 0,
                    type: 'patientList'
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
                            ncdrs.excelMembers.push({
                                "Name": member.name,
                                "Age": member.age,
                                "Gender": member.gender,
                                "Village": member.village,
                                "Sub Center": member.subCenter,
                                "Status": $filter('titlecase')("Suspected"),
                                "Disease(s)": diseasesForExcel,
                            });
                        });
                        ncdrs.processAndDownloadExcel(ncdrs.excelMembers);
                    } else {
                        return Promise.reject({ data: { message: 'No record found' } });
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
        }

        ncdrs.processAndDownloadExcel = (data) => {
            let mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            let fileName = "MO review List";
            let dataCopy = [];
            dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        }

        ncdrs.getDiseaseSummary = (members) => {
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

        ncdrs.retrieveMemberList = function (type, reset) {
            ncdrs.isSearch = false;
            if (reset) {
                ncdrs.membersSearchList = [];
                ncdrs.pagingService.resetOffSetAndVariables();
            }
            let criteria = {
                limit: ncdrs.pagingService.limit,
                offset: ncdrs.pagingService.offSet,
                type: type
            };
            PagingService.getNextPage(NcdDAO.retrieveMembers, criteria, ncdrs.membersSearchList, null).then(function (res) {
                //store response
                ncdrs.membersSearchList = res;
                angular.forEach(ncdrs.membersSearchList, function (item, index) {
                    switch (type) {
                        case 'patientList':
                            ncdrs.membersSearchList[index].statusDisplay = "Suspected";
                            break;
                        case 'followupTreatement':
                            ncdrs.membersSearchList[index].statusDisplay = item.status;
                            break;
                        default:
                            if (item.subStatus != null) {
                                ncdrs.membersSearchList[index].statusDisplay = item.subStatus
                                if (item.subStatus === 'REFERRED_CON') {
                                    ncdrs.membersSearchList[index].statusDisplay = "Referred Consultant"
                                }
                            }
                            else if (item.status === 'SCREENED') {
                                ncdrs.membersSearchList[index].statusDisplay = "Suspected"
                            }
                            else {
                                ncdrs.membersSearchList[index].statusDisplay = item.status
                            }
                            break;
                    }

                    if (item.diseases) {
                        ncdrs.membersSearchList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("B", " Breast cancer");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("C", " Cervical cancer");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("O", " Oral cancer");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("MH", " Mental Health");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("D", " Diabetes");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("G,", "");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("IA,", "");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace(",G", "");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace(",IA", "");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("G", "");
                        ncdrs.membersSearchList[index].diseaseSummary = ncdrs.membersSearchList[index].diseaseSummary.replace("IA", "");
                    }
                })
            }).finally(function (res) {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMOReviewScreenController', NcdMOReviewScreenController);
})(window.angular);
