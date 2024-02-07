(function (angular) {
    function NcdMOReviewMemberController($state, NcdDAO, toaster, GeneralUtil, AuthenticateService, Mask, $q, QueryDAO, NdhmHipDAO, $window) {
        var ctrl = this;

        ctrl.appName = GeneralUtil.getAppName();
        ctrl.MOReviewDto = {}

        ctrl.today = new Date();
        ctrl.today.setHours(0, 0, 0, 0)
        ctrl.diseases = ['IA', 'HT', 'D', 'MH', 'O', 'B', 'C', 'G',];
        ctrl.minGeneralFollowUpDate = moment(ctrl.today).add(1, 'days');
        ctrl.maxGeneralFollowUpDate = moment(ctrl.today).add(90, 'days');
        ctrl.isCvc=false
        ctrl.editedMedicineDetail = [];
        ctrl.deletedMedicineDetails = [];
        ctrl.medicineFormSubmit = true;
        ctrl.medicineDetail = [];

        var init = function () {
            ctrl.isUserHealthFetch = false
            ctrl.setLimit = null;
            ctrl.setLimitForOther = null;
            ctrl.prescribedMedicineAdded = []
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.loggedInUser = res.data;
                ctrl.retrieveLoggedInUserHealthInfra(res.data.id);
            })

            if ($state.params.id) {
                ctrl.retrieveMbbsMoReviewD();
                ctrl.retrieveMemberDetails();
                ctrl.memberId = $state.params.id
                ctrl.retrieveVisitData();
                ctrl.retrieveInvestigationData();
                ctrl.retrieveLastScheduledVisitData();
                ctrl.getPatientSummaryData();
                ctrl.fetchComplications();
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

        ctrl.getPatientSummaryData = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'ncd_moreview_patient_disease_summary_data',
                parameters: {
                    memberId: Number(ctrl.memberId)
                }
            }).then(function (res) {
                ctrl.diseaseStatus=res.result
                ctrl.bpl='No'
                if(res.result[0]?.is_bpl){
                    ctrl.bpl='Yes'
                }
                ctrl.medicineCcompliant=res.result[0]?.regular_medicine_intake
                ctrl.diseaseTypes=[]
                angular.forEach(ctrl.diseaseStatus, function (item, index) {
                    ctrl.diseaseTypes=item.type.split(",")
                    angular.forEach(ctrl.diseaseTypes,function(itemForTypes, indexForTypes){
                        if(itemForTypes){
                            ctrl.diseaseTypes[indexForTypes]=ctrl.diseaseTypes[indexForTypes].replace("no-visit","No Visit from last 1 year");
                            ctrl.diseaseTypes[indexForTypes]=ctrl.diseaseTypes[indexForTypes].replace("missed_visit","Missed visit");
                            ctrl.diseaseTypes[indexForTypes]=ctrl.diseaseTypes[indexForTypes].replace("uncontrol","Uncontrolled");
                            ctrl.diseaseTypes[indexForTypes]=ctrl.diseaseTypes[indexForTypes].replace("refer_mo","Referred MO");
                            if(itemForTypes=="CVC"){
                                ctrl.isCvc=true;
                            }
                        }
                    })
                    if(!item.treatment_status){
                        ctrl.diseaseStatus[index].treatment_status='-'
                    }
                    
                    ctrl.diseaseStatus[index].type=ctrl.diseaseTypes.toString();
                    if (item.disease) {
                        ctrl.diseaseStatus[index].disease = item.disease.replace("HT", " Hypertension");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("B", " Breast cancer");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("C", " Cervical cancer");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("O", " Oral cancer");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("MH", " Mental Health");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("D", " Diabetes");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("G,", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("IA,", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace(",G", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace(",IA", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("G", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("IA", "");
                        ctrl.diseaseStatus[index].disease = ctrl.diseaseStatus[index].disease.replace("-", "NA");
                    }
                })
            }).finally(() => {
                Mask.hide()
            })
        }

        ctrl.fetchComplications = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'get_complications_for_ncd_from_known_history',
                parameters: {
                    memberId: Number(ctrl.memberId)
                }
            }).then(function (res) {
                if(res.result.length > 0){
                    ctrl.historyDisease = res.result[0];                    
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        }

        ctrl.retrieveMemberDetails = function (isSaveAction) {
            Mask.show();
            return NcdDAO.retrieveDetails($state.params.id).then(function (res) {
                ctrl.member = {};
                ctrl.member = res;
                ctrl.member.basicDetails.age = getAge(ctrl.member.basicDetails.dob);
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function (res) {
                Mask.hide();
            })
        }
        ctrl.retrieveMbbsMoReviewD = function () {

            NcdDAO.retrieveMbbsMOReviewByMemberId($state.params.id).then(function (response) {
                if (response.id) {
                    ctrl.MbbsMoReviewRecord = response;
                }

            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })

        }

        ctrl.saveMOReview = function () {
            ctrl.MbbsMOReviewForm.$setSubmitted();
            if (ctrl.MbbsMOReviewForm.$valid) {
                ctrl.MOReviewDto.memberId = Number($state.params.id);
                if (ctrl.medicineDetail.length > 0) {
                    ctrl.medicineDetail.forEach(element => {
                        element.expiryDate = moment(element.startDate).add(element.duration, 'days')
                    });
                    ctrl.MOReviewDto.medicineDetail = ctrl.medicineDetail
                }
                else {
                    ctrl.MOReviewDto.medicineDetail = [];
                }
                ctrl.medicineDetail = [];
                // Add edited medicines
                if (ctrl.editedMedicineDetail.length > 0) {
                    ctrl.MOReviewDto.editedMedicineDetail = ctrl.editedMedicineDetail
                }
                else {
                    ctrl.MOReviewDto.editedMedicineDetail = []
                }
                ctrl.editedMedicineDetail = [];
                // Add deleted medicines
                if (ctrl.deletedMedicineDetails.length > 0) {
                    ctrl.MOReviewDto.deletedMedicineDetail = ctrl.deletedMedicineDetails
                }
                else {
                    ctrl.MOReviewDto.deletedMedicineDetail = []
                }
                ctrl.deletedMedicineDetails = [];
                NcdDAO.saveMOReview(ctrl.MOReviewDto).then(function () {
                    toaster.pop('success', "MO Review Details saved successfully");
                    ctrl.MbbsMOReviewForm.$setPristine();
                    ctrl.clearForm();
                    ctrl.formAlreadyFilled = true;
                    ctrl.minGeneralFollowUpDate = moment(ctrl.today).add(1, 'days');
                    ctrl.maxGeneralFollowUpDate = moment(ctrl.today).add(90, 'days');
                    $state.go("techo.ncd.moreview")
                }, GeneralUtil.showMessageOnApiCallFailure)
            }
        };

        ctrl.showFullHistory = () => {

            if (ctrl.history) {
                ctrl.setLimit = null;
                ctrl.setLimitForOther = null;
            } else {
                ctrl.setLimit = 1;
                ctrl.setLimitForOther = 3;
            }
            ctrl.history = !ctrl.history;
        }

        ctrl.printPatientSummary = function () {
            let url = $state.href('techo.ncd.patientSummary', { id: $state.params.id });
            sessionStorage.setItem('linkClick', 'true')
            $window.open(url, '_blank');
        };

        ctrl.retrieveLastScheduledVisitData = function(){
            Mask.show();
            QueryDAO.execute({
                code: 'ncd_last_scheduled_visit_details',
                parameters: {
                    memberId: Number(ctrl.memberId)
                }
            }).then(function (res) {
                ctrl.lastScheduledVisit=res.result[0];

            }).finally(() => {
                Mask.hide()
            })


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
                        data.reading= data.reading?.replaceAll('null','N.A')
                    })
                }
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

        ctrl.retrieveLoggedInUserHealthInfra = function (userId) {
            if (userId) {
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: userId
                    }
                }).then(function (res) {
                    ctrl.HealthInfras = res.result;
                    //ctrl.defaultHealthInfra = ctrl.HealthInfras[0].id;
                    ctrl.MOReviewDto.healthInfraId = ctrl.HealthInfras[0]?.id;
                    ctrl.retrieveMedicinesByHealthInfra();
                })
            }
        }

        ctrl.retrieveMedicinesByHealthInfra = function () {
            if (ctrl.MOReviewDto.healthInfraId) {
                NcdDAO.retriveAllDrugInventory(ctrl.MOReviewDto.healthInfraId, 'CFY').then(function (res) {
                    ctrl.medicineData = res
                })
            }
        }

        ctrl.onChange = function () {
            Mask.show();
            NcdDAO.retrieveMoReviewByMemberAndDate($state.params.id, moment(ctrl.MOReviewDto.screeningDate).valueOf()).then(function (response) {
                if (response.id != null) {
                    toaster.pop('warning', 'Details already filled');
                    ctrl.formAlreadyFilled = true;
                    ctrl.MOReviewDto.healthInfraId = response.healthInfraId;
                    ctrl.MOReviewDto.doesRequiredRef = response.doesRequiredRef;
                    ctrl.MOReviewDto.refferralReason = response.refferralReason;
                    ctrl.MOReviewDto.refferralPlace = response.refferralPlace;
                    ctrl.MOReviewDto.followupPlace = response.followupPlace;
                    ctrl.MOReviewDto.followUpDate = response.followUpDate;
                    ctrl.MOReviewDto.comment = response.comment;
                    ctrl.MOReviewDto.followUpDisease = response.diseases;
                    ctrl.MOReviewDto.otherReason = response.otherReason;
                } else {
                    ctrl.formAlreadyFilled = false;
                    ctrl.clearForm();
                    ctrl.minGeneralFollowUpDate = moment(ctrl.MOReviewDto.screeningDate).add(1, 'days');
                    ctrl.maxGeneralFollowUpDate = moment(ctrl.minGeneralFollowUpDate).add(90, 'days');
                }
                Mask.hide();
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            })
        }

        ctrl.clearForm = function () {
            ctrl.MOReviewDto.doesRequiredRef = false
            ctrl.MOReviewDto.refferralReason = null
            ctrl.MOReviewDto.refferralPlace = null
            ctrl.MOReviewDto.followupPlace = null
            ctrl.MOReviewDto.followUpDate = null
            ctrl.MOReviewDto.comment = null
            ctrl.MOReviewDto.followUpDisease = []
            ctrl.MOReviewDto.otherReason = null
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdMOReviewMemberController', NcdMOReviewMemberController);
})(window.angular);