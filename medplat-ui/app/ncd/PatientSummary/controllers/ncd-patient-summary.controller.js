(function (angular) {
    function NcdPatientSummary($state, NcdDAO, Mask, GeneralUtil, QueryDAO, AuthenticateService, $q) {
        var ctrl = this;

        var init = function () {
            ctrl.memberId = $state.params.id
            ctrl.retrieveMemberDetails();
            ctrl.retrieveVisitData();
            ctrl.retrieveMedicineData();
            ctrl.retrieveInvestigationData();
            ctrl.diseases = ['IA', 'HT', 'D', 'MH', 'O', 'B', 'C', 'G',];
        }

        ctrl.retrieveMemberDetails = function () {
            Mask.show();
            return NcdDAO.retrieveDetails(ctrl.memberId).then(function (res) {
                ctrl.member = {};
                ctrl.member = res;
                ctrl.member.basicDetails.age = getAge(ctrl.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }

        var getAge = function (DOB) {
            var birthDate = new Date(DOB);
            var age = new Date().getFullYear() - birthDate.getFullYear();
            var m = new Date().getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && new Date().getDate() < birthDate.getDate())) {
                age = age - 1;
            }

            return age;
        }

        ctrl.retrieveVisitData = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'ncd_patient_visit_history_by_member_id',
                parameters: {
                    memberId: Number(ctrl.memberId)
                }
            }).then(function (res) {
                ctrl.setTreatmentDetails(res.result);
            }).finally(() => {
                Mask.hide()
            })
        }

        ctrl.setTreatmentDetails = function (data) {
            const diseaseWiseData = data.reduce(function (r, a) {
                r[a.disease_code] = r[a.disease_code] || { history: [] };
                r[a.disease_code].history.push(a);
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
            ctrl.visitData = diseaseWiseData;

            angular.forEach(ctrl.visitData, function(item){
                if(item.name === 'Initial Assessment'){
                    angular.forEach(item.history, function(data){
                        data.reading= data.reading.replaceAll('null','N.A')
                    })
                }
            })
        }

        ctrl.retrieveMedicineData = function () {
            Mask.show();
            NcdDAO.retrievePrescribedMedicineHistoryForUser(ctrl.memberId).then(function (res) {
                ctrl.prescribedMedicine = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error)
            }).finally(() => {
                Mask.hide();
            })
        }

        ctrl.retrieveInvestigationData = function () {
            Mask.show();
            NcdDAO.retreiveInvestigationDetailByMemberId(ctrl.memberId).then((res) => {
                ctrl.investigationList = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })
        }

        ctrl.printPatientSummary = function () {
            //let imagesPath = GeneralUtil.getImagesPath();
            //var header = `<div class='print_header display-none'><img class='print_logo' src="${imagesPath}web_logo.png" ><h2 class='page_title'>Patient Summary</h2></div>`;
            var header = `<div class='print_header display-none'><h2 class='page_title'>Patient Summary</h2></div>`;
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.footer = "Generated by " + ctrl.loggedInUser.name + " at " + new Date().toLocaleString();
                let promiseList = [];
                promiseList.push(ctrl.retrieveVisitData());
                promiseList.push(ctrl.retrieveMedicineData());
                promiseList.push(ctrl.retrieveInvestigationData());
                $q.all(promiseList).then(function (res) {
                    $('#printableDiv').printThis({
                        debug: false,
                        importCSS: false,
                        loadCSS: ['styles/css/printable.css', 'styles/css/ncd_printable.css'],
                        header: header,
                        printDelay: 333,
                        base: "./",
                        pageTitle: ctrl.appName
                    });
                })
            })

        };

        init();
    }
    angular.module('imtecho.controllers').controller('NcdPatientSummary', NcdPatientSummary);
})(window.angular);