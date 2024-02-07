(function (angular) {
    function NcdMOReviewPatientSummaryController($state, NcdDAO, toaster, GeneralUtil, AuthenticateService, Mask, $q, QueryDAO, NdhmHipDAO, $window) {
        var ncdps = this;

        ncdps.appName = GeneralUtil.getAppName();

        ncdps.today = new Date();
        ncdps.diseases = ['IA', 'HT', 'D', 'MH', 'O', 'B', 'C', 'G',];

        var init = function () {
            ncdps.isUserHealthFetch = false
            ncdps.setLimit = 1;
            ncdps.setLimitForOther = 3;
            AuthenticateService.getLoggedInUser().then(function (res) {
                ncdps.loggedInUser = res.data;
            })

            if ($state.params.id) {
                ncdps.retrieveMbbsMoReviewD();
                ncdps.retrieveMemberDetails();
                // ncdps.getMemberTretment(1, 'MO');
                ncdps.memberId = $state.params.id
                ncdps.retrieveVisitData();
                ncdps.retrieveMedicineData();
                ncdps.retrieveInvestigationData();
            }

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

        ncdps.retrieveMemberDetails = function (isSaveAction) {
            Mask.show();
            return NcdDAO.retrieveDetails($state.params.id).then(function (res) {
                ncdps.member = {};
                ncdps.member = res;
                ncdps.member.basicDetails.age = getAge(ncdps.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }
        ncdps.retrieveMbbsMoReviewD = function () {

            NcdDAO.retrieveMbbsMOReviewByMemberId($state.params.id).then(function (response) {
                if (response.id) {
                    ncdps.MbbsMoReviewRecord = response;
                }

            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })

        }

        ncdps.saveMbbsMOReview = function () {
            ncdps.MbbsMOReviewForm.$setSubmitted();
            if (ncdps.MbbsMOReviewForm.$valid) {
                ncdps.MbbsMOReviewDto.memberId = Number($state.params.id);
                ncdps.MbbsMOReviewDto.doneBy = 'MO';
                NcdDAO.saveMbbsMOReview(ncdps.MbbsMOReviewDto).then(function () {
                    toaster.pop('success', "Mbbs MO Review Details saved successfully");
                    ncdps.MbbsMOReviewForm.$setPristine();
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        // ncdps.setTreatmentDetails = function (data) {
        //     const diseaseWiseData = data.reduce(function (r, a) {
        //         r[a.diseaseCode] = r[a.diseaseCode] || { history: [] };
        //         r[a.diseaseCode].history.push(a);
        //         return r;
        //     }, Object.create(null));

        //     if (diseaseWiseData['IA'] == null) {
        //         diseaseWiseData['IA'] = {};
        //     }
        //     if (diseaseWiseData['HT'] == null) {
        //         diseaseWiseData['HT'] = {};
        //     }
        //     if (diseaseWiseData['D'] == null) {
        //         diseaseWiseData['D'] = {};
        //     }
        //     if (diseaseWiseData['MH'] == null) {
        //         diseaseWiseData['MH'] = {};
        //     }
        //     if (diseaseWiseData['O'] == null) {
        //         diseaseWiseData['O'] = {};
        //     }
        //     if (diseaseWiseData['B'] == null) {
        //         diseaseWiseData['B'] = {};
        //     }
        //     if (diseaseWiseData['C'] == null) {
        //         diseaseWiseData['C'] = {};
        //     }
        //     if (diseaseWiseData['G'] == null) {
        //         diseaseWiseData['G'] = {};
        //     }
        //     diseaseWiseData['IA'].name = 'Initial Assessment';
        //     diseaseWiseData['HT'].name = 'Hypertension';
        //     diseaseWiseData['D'].name = 'Diabetes';
        //     diseaseWiseData['MH'].name = 'Mental Health';
        //     diseaseWiseData['O'].name = 'Oral';
        //     diseaseWiseData['B'].name = 'Breast';
        //     diseaseWiseData['C'].name = 'Cervical';
        //     diseaseWiseData['G'].name = 'General';
        //     ncdps.diseaseData = diseaseWiseData;
        // }

        ncdps.showFullHistory = () => {
            ncdps.history = !ncdps.history;
            if (ncdps.history) {
                //ncdps.getMemberTretment(10, null);
                ncdps.setLimit = null;
                ncdps.setLimitForOther = null;
            } else {
                //ncdps.getMemberTretment(1, 'MO');
                ncdps.setLimit = 1;
                ncdps.setLimitForOther = 3;
            }
        }

        // ncdps.getMemberTretment = function (limit, doneBy) {
        //     Mask.show();
        //     QueryDAO.execute({
        //         code: 'ncd_treatment_history_by_member_id',
        //         parameters: {
        //             memberId: Number($state.params.id),
        //             limit: limit,
        //             doneBy: doneBy
        //         }
        //     }).then(function (res) {
        //         ncdps.setTreatmentDetails(res.result)
        //     }).finally(() => {
        //         Mask.hide()
        //     })
        // };


        ncdps.printPatientSummary = function () {
            let url = $state.href('techo.ncd.patientSummary', { id: $state.params.id });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        };

        ncdps.retrieveVisitData = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'ncd_patient_visit_history_by_member_id',
                parameters: {
                    memberId: Number(ncdps.memberId)
                }
            }).then(function (res) {
                ncdps.setTreatmentDetails(res.result);
            }).finally(() => {
                Mask.hide()
            })
        }

        ncdps.setTreatmentDetails = function (data) {
            const diseaseWiseData = data.reduce(function (r, a) {
                r[a.diseaseCode] = r[a.diseaseCode] || { history: [] };
                r[a.diseaseCode].history.push(a);
                return r;
            }, Object.create(null));

            if (diseaseWiseData['IA'] == null) {
                diseaseWiseData['IA'] = {};
            }
            if (diseaseWiseData['HT'] == null) {
                diseaseWiseData['HT'] = {};
            }
            if (diseaseWiseData['D'] == null) {
                diseaseWiseData['D'] = {};
            }
            if (diseaseWiseData['MH'] == null) {
                diseaseWiseData['MH'] = {};
            }
            if (diseaseWiseData['O'] == null) {
                diseaseWiseData['O'] = {};
            }
            if (diseaseWiseData['B'] == null) {
                diseaseWiseData['B'] = {};
            }
            if (diseaseWiseData['C'] == null) {
                diseaseWiseData['C'] = {};
            }
            if (diseaseWiseData['G'] == null) {
                diseaseWiseData['G'] = {};
            }
            diseaseWiseData['IA'].name = 'Initial Assessment';
            diseaseWiseData['HT'].name = 'Hypertension';
            diseaseWiseData['D'].name = 'Diabetes';
            diseaseWiseData['MH'].name = 'Mental Health';
            diseaseWiseData['O'].name = 'Oral';
            diseaseWiseData['B'].name = 'Breast';
            diseaseWiseData['C'].name = 'Cervical';
            diseaseWiseData['G'].name = 'General';
            ncdps.visitData = diseaseWiseData;

            angular.forEach(ncdps.visitData, function(item){
                if(item.name === 'Initial Assessment'){
                    angular.forEach(item.history, function(data){
                        data.reading= data.reading.replaceAll('null','N.A')
                    })
                }
            })
        }

        ncdps.retrieveMedicineData = function () {
            Mask.show();
            NcdDAO.retrievePrescribedMedicineForUser(ncdps.memberId).then(function (res) {
                ncdps.prescribedMedicine = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error)
            }).finally(() => {
                Mask.hide();
            })
        }

        ncdps.retrieveInvestigationData = function () {
            Mask.show();
            NcdDAO.retreiveInvestigationDetailByMemberId(ncdps.memberId).then((res) => {
                ncdps.investigationList = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMOReviewPatientSummaryController', NcdMOReviewPatientSummaryController);
})(window.angular);