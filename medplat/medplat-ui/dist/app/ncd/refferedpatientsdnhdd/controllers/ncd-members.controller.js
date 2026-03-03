(function (angular) {
    function NcdMembersDnhddController(PagingService, NcdDnhddDAO, Mask, AuthenticateService, $state, $q, QueryDAO, GeneralUtil, toaster) {
        const mlisting = this;
        mlisting.requiredLocationLevel = 6;
        mlisting.fetchLocationLevelUpto = 7;
        mlisting.lmpMinDate = moment().subtract(9, 'months').format("YYYY-MM-DD");
        mlisting.lmpMaxDate = moment().subtract(28, 'days').format("YYYY-MM-DD");
        mlisting.maxDob = moment().subtract(30, 'years').format("YYYY-MM-DD");
        mlisting.isError = false;
        mlisting.canRegister = false;
        mlisting.isInitialLoad = true;
        mlisting.relations = [];
        mlisting.maritalStatusMapper = {
            630: 'Unmarried',
            629: 'Married',
            641: 'Widow',
            642: 'Abandoned',
            643: 'Widower'
        }

        const init = function () {
            let promises = [];
            mlisting.relations = ['BROTHER','NIECE','DAUGHTER_IN_LAW','FATHER','NEPHEW',
            'GRANDMOTHER','HUSBAND','SON','MOTHER','SON_IN_LAW','SISTER','GRANDSON','GRANDFATHER','DAUGHTER-IN-LAW','WIFE',
            'SON-IN-LAW','OTHER','SISTER_IN_LAW','FATHER_IN_LAW','BROTHER_IN_LAW','ROOMMATES','GRANDDAUGHTER','DAUGHTER'];
            promises.push(AuthenticateService.getLoggedInUser());
            promises.push(AuthenticateService.getAssignedFeature($state.current.name));
            Mask.show();
            $q.all(promises).then(res => {
                mlisting.loggedInUser = res[0].data;
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: mlisting.loggedInUser.id,
                    }
                }).then((res) => {
                    if (res.result.length > 0) {
                        mlisting.userHealthInfraList = res.result;
                        if (sessionStorage.userHealthInfraId && sessionStorage.userHealthInfraId !== "null" 
                            && sessionStorage.userHealthInfraName !== "null") {
                            mlisting.hasSessionStorage = true;
                            mlisting.userHealthInfra = Number(sessionStorage.userHealthInfraId);
                            mlisting.userHealthInfraName = sessionStorage.userHealthInfraName;
                        } else {
                            mlisting.hasSessionStorage = false;
                            if (mlisting.userHealthInfraList.length === 1) {
                                mlisting.userHealthInfra = mlisting.userHealthInfraList[0].id;
                                mlisting.userHealthInfraName = mlisting.userHealthInfraList[0].name;
                            }
                        }                        
                        mlisting.noHealthInfra = false;
                    } else {
                        toaster.pop('error', 'No Health Infrastructure Assigned. Please contact administration.');
                    }
                }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide());
                mlisting.rights = res[1].featureJson;
                if (!mlisting.rights) {
                    mlisting.rights = {};
                }
                mlisting.search = {};
                mlisting.pagingService = PagingService.initialize();
                mlisting.toggleFilter();
                mlisting.retrieveMembers(true, true);
                mlisting.type = $state.params.type;
            });
        };

        mlisting.openInNewTab = function(memberId, listingType) {
            sessionStorage.setItem('linkClick', 'true');
            window.open($state.href('techo.ncd.memberdetailsdnhdd', { id: memberId, type: listingType }));
        };

        mlisting.retrieveMembers = function (reset, forSuspected) {
            mlisting.isSuspectedOrExaminedNormalTab = true;
            mlisting.suspected = forSuspected;
            if (!reset) {
                mlisting.members = [];
                mlisting.pagingService.resetOffSetAndVariables();
                mlisting.isListRetrieved = false;
            }
            Mask.show();
            let criteria = {
                limit: mlisting.pagingService.limit,
                offset: mlisting.pagingService.offSet,
                healthInfrastructureType: $state.params.type,
                searchString: mlisting.search.searchString,
                searchBy: mlisting.search.searchBy,
                isSus: forSuspected
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
                default:
                    // do nothing;
                    break;
            }
            PagingService.getNextPage(NcdDnhddDAO.retrieveMembers, criteria, mlisting.members, null).then(function (res) {
                if (forSuspected) {
                    mlisting.members = mlisting.getDiseaseSummary(res);
                } else {
                    let uniqueIds = [];
                    let uniqueMembers = [];
                    res.forEach(function (member) {
                        if (uniqueIds.indexOf(member.id) === -1) {
                          uniqueIds.push(member.id);
                          uniqueMembers.push(member);
                        }
                    });
                    mlisting.members = angular.copy(uniqueMembers);
                }
                mlisting.canRegister = mlisting.members.length === 0 && reset === false;
                mlisting.isListRetrieved = true;
            }).finally(function (res) {
                mlisting.isInitialLoad = false;
                Mask.hide();
            });
        };

        mlisting.searchMembers = function (reset) {
            mlisting.searchForm.$setSubmitted();
            if (mlisting.searchForm.$valid) {
                mlisting.toggleFilter();
                mlisting.retrieveMembers(reset, mlisting.selectedTab === 0);
            }
        };

        mlisting.retrieveMembersOnScroll = function (reset, isSuspected) {
            if (mlisting.isListRetrieved && mlisting.members.length < 100) return;
            mlisting.retrieveMembers(reset, isSuspected);
        };

        mlisting.retrieveMembersForFollowup = function (reset, status) {
            mlisting.followupTabStatus = status;
            mlisting.isSuspectedOrExaminedNormalTab = false;
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
            PagingService.getNextPage(NcdDnhddDAO.retrieveAllForFollowup, criteria, mlisting.membersForFollowup, null).then(function (res) {
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
            if (mlisting.isSuspectedOrExaminedNormalTab){
                let criteria = {
                    limit: null,
                    offset: null,
                    healthInfrastructureType: $state.params.type,
                    searchString: mlisting.search.searchString,
                    searchBy: mlisting.search.searchBy,
                    isSus: mlisting.suspected
    
                };
                NcdDnhddDAO.retrieveMembers(criteria).then((response) => {
                    if (response != null && response.length > 0) {
                        mlisting.excelRetrievedMembers = response;
                        mlisting.processAndDownloadExcel();
                    } else {
                        return Promise.reject({ data: { message: 'No record found' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
            else {
                let criteria = {
                    limit: null,
                    offset: null,
                    healthInfrastructureType: $state.params.type,
                    status: mlisting.followupTabStatus
                }
                NcdDnhddDAO.retrieveAllForFollowup(criteria).then((response) => {
                    if (response != null && response.length > 0) {
                        mlisting.excelRetrievedMembers = response;
                        mlisting.processAndDownloadExcel();
                    } else {
                        return Promise.reject({ data: { message: 'No record found' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
            
        }

        mlisting.processAndDownloadExcel = () => {
            mlisting.excelRetrievedMembers.forEach((member) => {
                let fieldFindingsDet = member.referredForDiseases.split('<br>');
                member.diseaseSummary = member.referredForDiseases;
                if (member.gender === 'M')
                {
                    member.diseaseSummary = '';
                    member.diseaseSummary = member.diseaseSummary.concat(fieldFindingsDet[0],"<br>",fieldFindingsDet[1],"<br>",fieldFindingsDet[2]);
                }
                let fieldFinding = member.diseaseSummary.replaceAll("<br>", ", ").replaceAll("<b style=\"color:red;\">", "").replaceAll("</b>", "");
                let excelBasicDto = ({
                    "Patient Name": member.name.concat(' (', member.uniqueHealthId, ')'),
                    "Family Id": mlisting.isSuspectedOrExaminedNormalTab ? member.familyId : member.famId,
                    "Address": member.locationName,
                    "Contact Number": member.mobileNumber ? member.mobileNumber : 'NA.',
                    "Field findings": fieldFinding,
                    "HMIS ID": member.hmisId ? member.hmisId : 'NA.'
                });
                if (member.followUpDate != null) {
                    excelBasicDto["Follow Up Date"]= member.followUpDate;
                }
                mlisting.excelMembers.push(excelBasicDto);
            });
            let mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            let fileName = "Referred Patients List";
            let dataCopy = [];
            dataCopy = mlisting.excelMembers;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        }

        mlisting.getDiseaseSummary = (members) => {
            members.map((member) => {
                member.diseaseSummary = "";
                mlisting.fieldFindingsDet = member.referredForDiseases.split('<br>');
                member.diseaseSummary = member.referredForDiseases;
                if (member.gender === 'M')
                {
                    member.diseaseSummary = '';
                    member.diseaseSummary = member.diseaseSummary.concat(mlisting.fieldFindingsDet[0],"<br>",mlisting.fieldFindingsDet[1],"<br>",mlisting.fieldFindingsDet[2]);
                }
            });
            return members;
        }

        mlisting.onRegister = () => {
            $('#memberRegistration').modal({ backdrop: 'static', keyboard: false, focus: true });
        }

        mlisting.onChange = (type) => {
            if(type) {
                switch(type) {
                    case 'GENDER':
                        mlisting.formData.member.rchId = null;
                        mlisting.formData.member.isPregnantFlag = null;
                        mlisting.formData.member.lmpDate = null;
                        mlisting.formData.member.maritalStatus = null;
                        break;
                    case 'PREGNANT':
                        mlisting.formData.member.lmpDate = null;
                        break;
                    case 'FORM':
                        mlisting.formData = {};
                        mlisting.newMemberForm.$setPristine();
                        mlisting.selectedFamLocation = {};
                        break;
                    case 'DOB':
                        mlisting.formData.member.age = new Date(Date.now() - mlisting.formData.member.dob.getTime()).getUTCFullYear() - 1970;
                        mlisting.formData.member.isPregnantFlag = null;
                        break;
                    case 'HEALTH_INFRA':
                        if (mlisting.userHealthInfra) {
                            let healthInfra = mlisting.userHealthInfraList.find(e => e.id == mlisting.userHealthInfra);
                            sessionStorage.userHealthInfraId = healthInfra.id;
                            sessionStorage.userHealthInfraName = healthInfra.name;
                            mlisting.userHealthInfra = Number(sessionStorage.userHealthInfraId);
                            mlisting.userHealthInfraName = sessionStorage.userHealthInfraName;
                        } else {
                            sessionStorage.userHealthInfraId = null;
                            sessionStorage.userHealthInfraName = null;
                            mlisting.userHealthInfra = null;
                            mlisting.userHealthInfraName = null;
                        }
                        break;
                    case 'IS_HOF':
                        mlisting.formData.hof = null;
                        break;
                }
            }
        }

        // mlisting.onClickHealthIdForm = () => {
        //     $('#memberRegistration').hide();
        //     let member = mlisting.formData.member;
        //     member.uniqueHealthId = "";
        //     member.id = -1;
        //     member.createdFrom = 'NCD_WEB';
        //     if(!member.hasOwnProperty('middleName')){
        //         member.middleName = '';
        //     }
        //     NdhmHealthIdUtilService.onClickHealthIdForm(member).finally(
        //         () => {
        //             $('#memberRegistration').show();
        //         }
        //     );
        // }

        // mlisting.handleChangeEvent = (abhaId) => {
        //     let newValue = abhaId.split('-').join('').slice(2);
        //     let finalValue = abhaId.slice(0, 2);

        //     if (newValue.length > 0) {
        //         newValue = newValue.match(/.{1,4}/g).join('-');
        //         finalValue = finalValue + '-' + newValue;
        //         mlisting.formData.member.healthIdNumber = finalValue;
        //     }
        // }

        mlisting.switchHealthInfrastructure = () => {
            if (mlisting.hasSessionStorage) {
                mlisting.hasSessionStorage = false;
                sessionStorage.userHealthInfraId = null;
                sessionStorage.userHealthInfraName = null;
                mlisting.userHealthInfra = null;
                mlisting.userHealthInfraName = null;
            }
        }

        mlisting.registerNewMember = () => {
            if (mlisting.hasSessionStorage && mlisting.newMemberForm) {
                mlisting.userHealthInfra = Number(sessionStorage.userHealthInfraId);
                mlisting.userHealthInfraName = sessionStorage.userHealthInfraName;
            } else {
                if (mlisting.userHealthInfraList.length > 1 && mlisting.healthInfraForm && mlisting.newMemberForm) {
                    mlisting.healthInfraForm.$setSubmitted();
                    mlisting.isError = (mlisting.healthInfraForm.$valid && mlisting.newMemberForm.$valid) ? false : true;
                }
            }
            mlisting.newMemberForm.$setSubmitted();
            if(!mlisting.isError && mlisting.newMemberForm.$valid) {
                mlisting.formData.locationId = mlisting.selectedFamLocation.level6.id;
                mlisting.formData.referredFromInfraId = mlisting.userHealthInfra;
                if (mlisting.formData.isHof === false) {
                    mlisting.formData.hof.familyHeadFlag = true;
                    mlisting.formData.hof.lastName = mlisting.formData.member.lastName;
                } else if (mlisting.formData.isHof === true) {
                    mlisting.formData.member.familyHeadFlag = true;
                }
                $('#memberRegistration').modal('hide');
                $('.modal-backdrop.in').remove();
                Mask.show();
                NcdDnhddDAO.registerNewMember(mlisting.formData).then((res) => {
                    mlisting.onChange('FORM');
                    toaster.pop('success', 'Member Registered Successfully!');
                    $state.go('techo.ncd.memberdetailsdnhdd', { id: Number(res.id) });
                }, GeneralUtil.showMessageOnApiCallFailure).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally( () => {
                    Mask.hide();
                });
            }
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMembersDnhddController', NcdMembersDnhddController);
})(window.angular);
