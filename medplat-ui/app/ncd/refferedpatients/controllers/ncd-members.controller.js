(function (angular) {
    function NcdMembersController(PagingService, NcdDAO, Mask, AuthenticateService, $state, QueryDAO, GeneralUtil, toaster, $filter, $window, $q, $uibModal) {
        var mlisting = this;

        var init = function () {
            AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
                mlisting.rights = res.featureJson;
                if (!mlisting.rights) {
                    mlisting.rights = {};
                }
                mlisting.isSearch = false
                mlisting.search = {};
                mlisting.modalClosed = true
                mlisting.pagingService = PagingService.initialize();
                //mlisting.retrieveMembers();
                mlisting.retrieveMemberList('patientList', true);
                //mlisting.retrieveSC();
                mlisting.type = $state.params.type;
                mlisting.showSubStatus = false
            });
        };

        var getAge = function (DOB) {
            var birthDate = new Date(DOB);
            var age = new Date().getFullYear() - birthDate.getFullYear();
            var m = new Date().getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && new Date().getDate() < birthDate.getDate())) {
                age = age - 1;
            }

            return age;
        }

        mlisting.retrieveMembers = function (reset) {
            if (!reset) {
                //mlisting.members = [];
                mlisting.membersList = [];
                mlisting.pagingService.resetOffSetAndVariables();
                mlisting.isListRetrieved = false;
            }
            Mask.show();
            let criteria = {
                limit: mlisting.pagingService.limit,
                offset: mlisting.pagingService.offSet,
                healthInfrastructureType: $state.params.type,
                searchString: mlisting.search.searchString,
                searchBy: mlisting.search.searchBy
            };
            switch (criteria.searchBy) {
                case 'villageName':
                    criteria.searchString = mlisting.selectedLocationId
                    break;
                case 'mobileNumber':
                    if (mlisting.search.familyMobileNumber)
                        criteria.searchBy = "familyMobileNumber"
                    break;
                case 'name':
                    criteria.searchString = mlisting.search.locationToSearchByName + "#" + mlisting.search.firstName + "#" + mlisting.search.lastName;
                    if (mlisting.search.middleName)
                        criteria.searchString = criteria.searchString + "#" + mlisting.search.middleName;
                    break;
                case 'abha number':
                    criteria.searchString = mlisting.selectedAbhaNumber
                    break;
                case 'abha address':
                    criteria.searchString = mlisting.selectedAbhaNumber
                    break;
                default:
                    // do nothing;
                    break;
            }
            mlisting.membersdemo = [];
            PagingService.getNextPage(NcdDAO.retrieveAll, criteria, mlisting.membersList, null).then(function (res) {

                mlisting.membersList = mlisting.getDiseaseSummary(res);

                for (let i = 0; i < mlisting.membersList.length; i++) {
                    const element = mlisting.membersList[i];
                    element.age = getAge(element.dob);
                    mlisting.membersdemo.push(element);
                }
                if (!mlisting.modalClosed) {
                    mlisting.toggleFilter();
                }
                mlisting.isListRetrieved = true;
            }).finally(function (res) {
                Mask.hide();
            });
        };

        mlisting.searchMembers = function (reset) {
            mlisting.isSearch = true
            mlisting.listType = null
            mlisting.searchForm.$setSubmitted();
            mlisting.showSubStatus = false
            Mask.show();
            if (mlisting.searchForm.$valid) {
                //mlisting.retrieveMembers();
                //set flag
                mlisting.isSearch = true
                //set criteria
                if (reset) {
                    mlisting.membersSearchList = [];
                    mlisting.pagingService.resetOffSetAndVariables();
                }
                let criteria = {
                    limit: mlisting.pagingService.limit,
                    offset: mlisting.pagingService.offSet,
                    searchString: mlisting.search.searchString,
                    searchBy: mlisting.search.searchBy,
                    flag: mlisting.search.flag ? mlisting.search.flag : false,
                    review: mlisting.search.review ? mlisting.search.review : false
                };
                if (criteria.searchBy === 'name') {
                    criteria.searchString = mlisting.search.locationToSearchByName + "#" + mlisting.search.firstName + "#" + mlisting.search.lastName;
                    if (mlisting.search.middleName)
                        criteria.searchString = criteria.searchString + "#" + mlisting.search.middleName;
                }
                if (criteria.searchBy === 'orgUnit') {
                    if (mlisting.search.orgUnitStatus) {
                        criteria.searchString = criteria.searchString + "#" + mlisting.search.orgUnitStatus
                    }
                    else {
                        criteria.searchString = criteria.searchString + "#" + ''
                    }
                    if (mlisting.search.orgUnitDisease) {
                        criteria.searchString = criteria.searchString + "#" + mlisting.search.orgUnitDisease
                    }
                    else {
                        criteria.searchString = criteria.searchString + "#" + ''
                    }
                }
                if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_CHANGE') {
                    criteria.searchBy = 'medicinechange'
                }
                if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_NO_CHANGE') {
                    criteria.searchBy = 'nomedicinechange'
                }
                //call backend api
                mlisting.membersdemo = [];
                PagingService.getNextPage(NcdDAO.retreiveSearchedMembers, criteria, mlisting.membersSearchList, null).then(function (res) {
                    //store response
                    mlisting.membersSearchList = res;
                    angular.forEach(mlisting.membersSearchList, function (item, index) {
                        // if (mlisting.search.searchBy === 'status') {
                        //     mlisting.membersSearchList[index].statusDisplay = mlisting.membersSearchList[index].subStatus ? mlisting.membersSearchList[index].subStatus : mlisting.membersSearchList[index].status;
                        //     angular.forEach(mlisting.search.searchString, function (item1, index1) {
                        //         if (item1 === mlisting.membersSearchList[index].subStatus) {
                        //             mlisting.membersSearchList[index].statusDisplay = mlisting.membersSearchList[index].subStatus;
                        //             if (mlisting.membersSearchList[index].subStatus === 'REFERRED_CON') {
                        //                 mlisting.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //             }
                        //         }
                        //         else{
                        //             mlisting.membersSearchList[index].statusDisplay = mlisting.membersSearchList[index].status;
                        //             if (mlisting.membersSearchList[index].status === 'SCREENED') {
                        //                 mlisting.membersSearchList[index].statusDisplay = "Suspected"
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
                        // else if (mlisting.search.searchBy === 'orgUnit' && mlisting.search.orgUnitStatus && mlisting.search.orgUnitStatus.length>0) {
                        //     // mlisting.membersSearchList[index].statusDisplay = mlisting.search.orgUnitStatus
                        //     // if (mlisting.search.orgUnitStatus === 'REFERRED_CON') {
                        //     //     mlisting.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //     // }
                        //     // else if (mlisting.search.orgUnitStatus === 'SCREENED') {
                        //     //     mlisting.membersSearchList[index].statusDisplay = "Suspected"
                        //     // }
                        //     angular.forEach(mlisting.search.orgUnitStatus, function (item1, index1) {
                        //         if (item1 ===  mlisting.membersSearchList[index].subStatus) {
                        //             mlisting.membersSearchList[index].statusDisplay  =  mlisting.membersSearchList[index].subStatus;
                        //             if ( mlisting.membersSearchList[index].subStatus === 'REFERRED_CON') {
                        //                 mlisting.membersSearchList[index].statusDisplay  = "Referred Consultant"
                        //             }
                        //         }
                        //         else{
                        //             mlisting.membersSearchList[index].statusDisplay  =  mlisting.membersSearchList[index].status;
                        //             if ( mlisting.membersSearchList[index].status === 'SCREENED') {
                        //                 mlisting.membersSearchList[index].statusDisplay  = "Suspected"
                        //             }
                        //         }
                        //     })
                        // }
                        // else {
                        //     if (item.subStatus != null) {
                        //         mlisting.membersSearchList[index].statusDisplay = item.subStatus
                        //         if (item.subStatus === 'REFERRED_CON') {
                        //             mlisting.membersSearchList[index].statusDisplay = "Referred Consultant"
                        //         }
                        //     }
                        //     else if (item.status === 'SCREENED') {
                        //         mlisting.membersSearchList[index].statusDisplay = "Suspected"
                        //     }
                        //     else {
                        //         mlisting.membersSearchList[index].statusDisplay = item.status
                        //     }
                        // }
                        if (item.diseases) {
                            mlisting.membersSearchList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("B", " Breast cancer");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("C", " Cervical cancer");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("O", " Oral cancer");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("MH", " Mental Health");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("D", " Diabetes");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("G,", "");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("IA,", "");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace(",G", "");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace(",IA", "");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("G", "");
                            mlisting.membersSearchList[index].diseaseSummary = mlisting.membersSearchList[index].diseaseSummary.replace("IA", "");
                        }
                    })
                    if (!mlisting.modalClosed) {
                        mlisting.toggleFilter();
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
            else {
                mlisting.isSearch = false
                //toaster.pop("error", "Invalid search");
            }
            Mask.hide();
        };

        mlisting.retrieveMembersOnScroll = function (reset) {
            if (mlisting.isListRetrieved && mlisting.members.length < 100) return;
            mlisting.retrieveMembers(reset);
        };

        mlisting.retrieveMembersForFollowup = function (reset, status) {
            if (!reset) {
                mlisting.membersForFollowup = [];
                mlisting.pagingService.resetOffSetAndVariables();
                mlisting.isListRetrieved = false;
            }
            Mask.show();
            let criteria = {
                limit: mlisting.pagingService.limit,
                offset: mlisting.pagingService.offSet,
                healthInfrastructureType: $state.params.type,
                status: status
            }
            PagingService.getNextPage(NcdDAO.retrieveAllForFollowup, criteria, mlisting.membersForFollowup, null).then(function (res) {
                mlisting.membersForFollowup = mlisting.getDiseaseSummary(res);
                mlisting.isListRetrieved = true;
            }).finally(function (res) {
                Mask.hide();
            });
        };

        mlisting.retrieveMembersForFollowupOnScroll = function (reset, status) {
            if (mlisting.isListRetrieved && mlisting.membersForFollowup.length < 100) return;
            mlisting.retrieveMembersForFollowup(reset, status);
        }

        mlisting.toggleFilter = function () {
            mlisting.searchForm.$setPristine();
            if (angular.element('.filter-div').hasClass('active')) {
                mlisting.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                mlisting.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        mlisting.locationSelectizeWpd = {
            create: false,
            valueField: 'id',
            labelField: 'hierarchy',
            dropdownParent: 'body',
            highlight: true,
            searchField: ['_searchField'],
            maxItems: 1,
            render: {
                item: function (location, escape) {
                    let returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                },
                option: function (location, escape) {
                    let returnString = "<div>" + location.hierarchy + "</div>";
                    return returnString;
                }
            },
            onFocus: function () {
                this.onSearchChange("");
            },
            onBlur: function () {
                let selectize = this;
                let value = this.getValue();
                setTimeout(function () {
                    if (!value || value === '') {
                        selectize.clearOptions();
                        selectize.refreshOptions();
                    }
                }, 200);
            },
            load: function (query, callback) {
                let selectize = this;
                let value = this.getValue();
                if (!value) {
                    selectize.clearOptions();
                    selectize.refreshOptions();
                }
                let promise;
                let queryDto = {
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

        mlisting.downloadExcel = () => {
            mlisting.excelMembers = [];
            Mask.show();
            if (mlisting.isSearch) {
                if (mlisting.searchForm.$valid) {
                    let criteria = {
                        limit: null,
                        offset: 0,
                        searchString: mlisting.search.searchString,
                        searchBy: mlisting.search.searchBy,
                        flag: mlisting.search.flag ? mlisting.search.flag : false,
                        review: mlisting.search.review ? mlisting.search.review : false
                    };
                    if (criteria.searchBy === 'name') {
                        criteria.searchString = mlisting.search.locationToSearchByName + "#" + mlisting.search.firstName + "#" + mlisting.search.lastName;
                        if (mlisting.search.middleName)
                            criteria.searchString = criteria.searchString + "#" + mlisting.search.middleName;
                    }
                    if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_CHANGE') {
                        criteria.searchBy = 'medicinechange'
                    }
                    if (criteria.searchBy === 'status' && criteria.searchString === 'MEDICINE_NO_CHANGE') {
                        criteria.searchBy = 'nomedicinechange'
                    }
                    if (criteria.searchBy === 'orgUnit') {
                        if (mlisting.search.orgUnitStatus) {
                            criteria.searchString = criteria.searchString + "#" + mlisting.search.orgUnitStatus
                        }
                        else {
                            criteria.searchString = criteria.searchString + "#" + ''
                        }
                        if (mlisting.search.orgUnitDisease) {
                            criteria.searchString = criteria.searchString + "#" + mlisting.search.orgUnitDisease
                        }
                        else {
                            criteria.searchString = criteria.searchString + "#" + ''
                        }
                    }
                    NcdDAO.retreiveSearchedMembers(criteria).then(function (response) {
                        if (response != null && response.length > 0) {
                            response.forEach((member) => {
                                let statusForExcel = null;
                                let diseasesForExcel = null;
                                // if (mlisting.search.searchBy === 'status') {
                                //     // statusForExcel = mlisting.search.searchString
                                //     // if (mlisting.search.searchString === 'REFERRED_CON') {
                                //     //     statusForExcel = "Referred Consultant"
                                //     // }
                                //     // else if (mlisting.search.searchString === 'SCREENED') {
                                //     //     statusForExcel = "Suspected"
                                //     // }
                                //     angular.forEach(mlisting.search.searchString, function (item1, index1) {
                                //         if (item1 === member.subStatus) {
                                //             statusForExcel = member.subStatus;
                                //             if (member.subStatus === 'REFERRED_CON') {
                                //                 statusForExcel = "Referred Consultant"
                                //             }
                                //         }
                                //         else{
                                //             statusForExcel = member.status;
                                //             if (member.status === 'SCREENED') {
                                //                 statusForExcel = "Suspected"
                                //             }
                                //         }
                                //     })
                                // }
                                // else if (mlisting.search.searchBy === 'orgUnit' && mlisting.search.orgUnitStatus && mlisting.search.orgUnitStatus.length>0) {
                                //     angular.forEach(mlisting.search.orgUnitStatus, function (item1, index1) {
                                //         if (item1 === member.subStatus) {
                                //             statusForExcel = member.subStatus;
                                //             if (member.subStatus === 'REFERRED_CON') {
                                //                 statusForExcel = "Referred Consultant"
                                //             }
                                //         }
                                //         else{
                                //             statusForExcel = member.status;
                                //             if (member.status === 'SCREENED') {
                                //                 statusForExcel = "Suspected"
                                //             }
                                //         }
                                //     })
                                //     // statusForExcel = mlisting.search.orgUnitStatus
                                //     // if (mlisting.search.orgUnitStatus === 'REFERRED_CON') {
                                //     //     statusForExcel = "Referred Consultant"
                                //     // }
                                //     // else if (mlisting.search.orgUnitStatus === 'SCREENED') {
                                //     //     statusForExcel = "Suspected"
                                //     // }
                                // }
                                // else {
                                //     if (member.subStatus != null) {
                                //         statusForExcel = member.subStatus
                                //         if (member.subStatus === 'REFERRED_CON') {
                                //             statusForExcel = "Referred Consultant"
                                //         }
                                //     }
                                //     else if (member.status === 'SCREENED') {
                                //         statusForExcel = "Suspected"
                                //     }
                                //     else {
                                //         statusForExcel = member.status
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
                                mlisting.excelMembers.push({
                                    "Name": member.name,
                                    "Age": member.age,
                                    "Gender": member.gender,
                                    "Village": member.village,
                                    "Sub Center": member.subCenter,
                                    "Status": $filter('titlecase')(member.status),
                                    "Diseases(s)": diseasesForExcel,
                                });
                            });
                            mlisting.processAndDownloadExcel(mlisting.excelMembers);
                        } else {
                            return Promise.reject({ data: { message: 'No record found' } });
                        }
                    }).finally(function (res) {
                        Mask.hide();
                    });
                }
                else {
                    toaster.pop("error", "Invalid search");
                }
            }
            else {
                let criteria = {
                    limit: null,
                    offset: 0,
                    type: mlisting.listType
                };
                NcdDAO.retrieveMembers(criteria).then(function (response) {
                    if (response != null && response.length > 0) {
                        if (mlisting.listType != 'CVCList') {
                            response.forEach((member) => {
                                let statusForExcel = null;
                                let diseasesForExcel = null;
                                switch (mlisting.listType) {
                                    case 'patientList':
                                        statusForExcel = 'Suspected';
                                        break;
                                    case 'followupTreatement':
                                        statusForExcel = member.status;
                                        break;
                                    default:
                                        if (member.subStatus != null) {
                                            statusForExcel = member.subStatus
                                            if (member.subStatus === 'REFERRED_CON') {
                                                statusForExcel = "Referred Consultant"
                                            }
                                        }
                                        else {
                                            statusForExcel = member.status
                                        }
                                        break;
                                }

                                if (member.diseases) {
                                    diseasesForExcel = member.diseases.replace("HT", " Hypertension");
                                    diseasesForExcel = diseasesForExcel.replace("B", " Breast cancer");
                                    diseasesForExcel = diseasesForExcel.replace("C", " Cervical cancer");
                                    diseasesForExcel = diseasesForExcel.replace("O", " Oral cancer");
                                    diseasesForExcel = diseasesForExcel.replace("MH", " Mental Health");
                                    diseasesForExcel = diseasesForExcel.replace("D", " Diabetes");
                                    diseasesForExcel = diseasesForExcel.replace("G", "");
                                    diseasesForExcel = diseasesForExcel.replace("IA", "");
                                }
                                mlisting.excelMembers.push({
                                    "Name": member.name,
                                    "Age": member.age,
                                    "Gender": member.gender,
                                    "Village": member.village,
                                    "Sub Center": member.subCenter,
                                    "Status": $filter('titlecase')(statusForExcel),
                                    "Disease(s)": diseasesForExcel,
                                });

                            });
                            mlisting.processAndDownloadExcel(mlisting.excelMembers);
                        }
                        if (mlisting.listType == 'CVCList') {
                            response.forEach((member) => {
                                mlisting.excelMembers.push({
                                    "Name": member.name,
                                    "Age": member.age,
                                    "Gender": member.gender,
                                    "Village": member.village,
                                    "Sub Center": member.subCenter
                                });
                            });
                            mlisting.processAndDownloadExcel(mlisting.excelMembers);
                        }
                    } else {
                        return Promise.reject({ data: { message: 'No record found' } });
                    }
                }).finally(function (res) {
                    Mask.hide();
                });
            }
        }

        mlisting.processAndDownloadExcel = (data) => {
            let mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            let fileName = "Referred Patients List";
            let dataCopy = [];
            dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        }

        mlisting.getDiseaseSummary = (members) => {
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

        mlisting.retrieveMemberList = function (type, reset) {
            if (type != null) {
                mlisting.listType = type;
            }
            mlisting.isSearch = false;
            if (reset) {
                mlisting.membersList = [];
                mlisting.pagingService.resetOffSetAndVariables();
            }
            let criteria = {
                limit: mlisting.pagingService.limit,
                offset: mlisting.pagingService.offSet,
                type: type != null ? type : mlisting.listType
            };
            PagingService.getNextPage(NcdDAO.retrieveMembers, criteria, mlisting.membersList, null).then(function (res) {
                //store response
                mlisting.membersList = res;
                angular.forEach(mlisting.membersList, function (item, index) {
                    switch (criteria.type) {
                        case 'patientList':
                            mlisting.membersList[index].statusDisplay = 'Suspected';
                            break;
                        case 'followupTreatement':
                            // case 'CVCList':
                            mlisting.membersList[index].statusDisplay = item.status;
                            break;
                        default:
                            if (item.subStatus != null) {
                                mlisting.membersList[index].statusDisplay = item.subStatus
                                if (item.subStatus === 'REFERRED_CON') {
                                    mlisting.membersList[index].statusDisplay = "Referred Consultant"
                                }
                            }
                            else {
                                mlisting.membersList[index].statusDisplay = item.status
                            }
                            break;
                    }

                    if (item.diseases) {
                        mlisting.membersList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("B", " Breast cancer");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("C", " Cervical cancer");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("O", " Oral cancer");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("MH", " Mental Health");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("D", " Diabetes");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("G", "");
                        mlisting.membersList[index].diseaseSummary = mlisting.membersList[index].diseaseSummary.replace("IA", "");
                    }
                })
            }).finally(function (res) {
                Mask.hide();
            });
        }

        mlisting.retrieveSC = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (res) {
                mlisting.userId = res.data.id;
                var queryDto = {
                    code: 'retrieve_all_sub_centers',
                    parameters: {
                        userId: mlisting.userId
                    }
                };
                QueryDAO.execute(queryDto).then(function (res) {
                    mlisting.subCenters = res.result
                }).catch(function (error) {
                    $timeout(function () {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    });
                }).finally(function () {
                    Mask.hide();
                });
            })
        }

        mlisting.resetFilter = function () {
            const temp = mlisting.search.searchBy
            mlisting.search = {}
            mlisting.search.searchBy = temp
            mlisting.searchForm.$setPristine()
        }

        mlisting.fillExamineForm = function (id, type) {
            let url = $state.href('techo.ncd.memberdetails.initialassessment', { id: id, type: type });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        }

        mlisting.fillCVCForm = function (id, type) {
            let url = $state.href('techo.ncd.cvcform', { id: id, type: type });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        }

        mlisting.webcam = function () {
            let defer = $q.defer();

            $uibModal.open({
                windowClass: 'cst-modal',
                backdrop: 'static',
                keyboard: false,
                size: 'm',
                templateUrl: 'app/common/views/qr-code-scan.modal.html',
                controller: 'QRCodeScanModalController',
                resolve: {
                    validInput: function () {
                        return 'FM/2'
                    }
                }
            }).result.then(function (data) {
                if (data) {
                    let parsedData = JSON.parse(data);
                    mlisting.search.searchString = parsedData.familyId;
                    mlisting.searchForm.$valid = true; //needs replacement
                    mlisting.searchMembers(true);
                    defer.resolve();
                }
            }, function () {
                defer.reject();
            });
            return defer.promise;
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMembersController', NcdMembersController);
})(window.angular);
