(function (angular) {
    function NcdCFSListingController(PagingService, NcdDAO, Mask, AuthenticateService, $state, $filter, GeneralUtil) {
        var ncdfsl = this;

        var init = function () {
            AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
                ncdfsl.rights = res.featureJson;
                if (!ncdfsl.rights) {
                    ncdfsl.rights = {};
                }
                ncdfsl.search = {};
                ncdfsl.modalClosed = true
                ncdfsl.pagingService = PagingService.initialize();

                ncdfsl.retrieveMemberList('consultant', true)
                ncdfsl.type = $state.params.type;
            });
        };

        ncdfsl.downloadExcel = () => {
            ncdfsl.excelMembers = [];
            let criteria = {
                limit: null,
                offset: 0,
                type: 'consultant'
            };
            Mask.show();
            NcdDAO.retrieveMembers(criteria).then((response) => {
                if (response != null && response.length > 0) {
                    response.forEach((member) => {
                        let statusForExcel = null;
                        let diseasesForExcel = null;
                        // if (member.subStatus != null) {
                        //     if (member.subStatus === 'REFERRED_NO_VISIT' || member.subStatus === 'REFERRED_BACK' || member.subStatus === 'REFERRED_CON' || member.subStatus === 'REFERRED_OTHER') {
                        //         statusForExcel = 'Referred'
                        //     }
                        //     else if (member.subStatus === 'FOLLOWUP') {
                        //         statusForExcel = 'Followup'
                        //     }
                        //     else {
                        //         statusForExcel = member.subStatus
                        //     }
                        // }
                        // else {
                        //     if (member.status === 'UNDERTREATEMENT_CONTROLLERD' || member.status === 'UNDERTREATEMENT_UNCONTROLLED' || member.status === 'UNDERTREATEMENT') {
                        //         statusForExcel = 'Undertreatement'
                        //     }
                        //     else {
                        //         statusForExcel = member.status
                        //     }
                        // }
                        if (member.diseases) {
                            diseasesForExcel = member.diseases.replace("HT", " Hypertension");
                            diseasesForExcel = diseasesForExcel.replace("B", " Breast_cancer");
                            diseasesForExcel = diseasesForExcel.replace("C", " Cervical_cancer");
                            diseasesForExcel = diseasesForExcel.replace("O", " Oral_cancer");
                            diseasesForExcel = diseasesForExcel.replace("MH", " Mental Health");
                            diseasesForExcel = diseasesForExcel.replace("D", " Diabetes");
                            diseasesForExcel = diseasesForExcel.replace("G", "");
                            diseasesForExcel = diseasesForExcel.replace("IA", "");
                        }
                        ncdfsl.excelMembers.push({
                            "Name": member.name,
                            "Age": member.age,
                            "Gender": member.gender,
                            "Village": member.village,
                            "Sub Center": member.subCenter,
                            "Status": $filter('titlecase')(member.status),
                            "Disease(s)" : diseasesForExcel,
                        });
                    });
                    ncdfsl.processAndDownloadExcel(ncdfsl.excelMembers);
                } else {
                    return Promise.reject({ data: { message: 'No record found' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ncdfsl.processAndDownloadExcel = (data) => {
            let mystyle = {
                headers: true,
                column: { style: { Font: { Bold: "1" } } }
            };
            let fileName = "Consultant Patients List";
            let dataCopy = [];
            dataCopy = data;
            dataCopy = JSON.parse(JSON.stringify(dataCopy));
            alasql('SELECT * INTO XLSX("' + fileName + '",?) FROM ?', [mystyle, dataCopy]);
        }

        ncdfsl.retrieveMemberList = function (type, reset) {
            if (reset) {
                ncdfsl.membersList = [];
                ncdfsl.pagingService.resetOffSetAndVariables();
            }
            let criteria = {
                limit: ncdfsl.pagingService.limit,
                offset: ncdfsl.pagingService.offSet,
                type: type
            };
            PagingService.getNextPage(NcdDAO.retrieveMembers, criteria, ncdfsl.membersList, null).then(function (res) {
                ncdfsl.membersList = res;
                angular.forEach(ncdfsl.membersList, function (item, index) {
                    // if (item.subStatus != null) {
                    //     if (item.subStatus === 'REFERRED_NO_VISIT' || item.subStatus === 'REFERRED_BACK' || item.subStatus === 'REFERRED_CON' || item.subStatus === 'REFERRED_OTHER') {
                    //         ncdfsl.membersList[index].statusDisplay = 'Referred'
                    //     }
                    //     else if (item.subStatus === 'FOLLOWUP') {
                    //         ncdfsl.membersList[index].statusDisplay = 'Followup'
                    //     }
                    //     else {
                    //         ncdfsl.membersList[index].statusDisplay = item.subStatus
                    //     }
                    // }
                    // else {
                    //     if (item.status === 'UNDERTREATEMENT_CONTROLLERD' || item.status === 'UNDERTREATEMENT_UNCONTROLLED' || item.status === 'UNDERTREATEMENT') {
                    //         ncdfsl.membersList[index].statusDisplay = 'Undertreatement'
                    //     }
                    //     else {
                    //         ncdfsl.membersList[index].statusDisplay = item.status
                    //     }
                    // }
                    if (item.diseases) {
                        ncdfsl.membersList[index].diseaseSummary = item.diseases.replace("HT", " Hypertension");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("B", " Breast_cancer");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("C", " Cervical_cancer");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("O", " Oral_cancer");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("MH", " Mental Health");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("D", " Diabetes");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("G", "");
                        ncdfsl.membersList[index].diseaseSummary = ncdfsl.membersList[index].diseaseSummary.replace("IA", "");
                    }
                })
            }).finally(function (res) {
                Mask.hide();
            });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdCFSListingController', NcdCFSListingController);
})(window.angular);
