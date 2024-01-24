(function (angular) {
    function NcdInvestigation($state, toaster, NcdDAO, GeneralUtil, Mask, $scope, AuthenticateService, QueryDAO, NdhmHipDAO, $uibModal, $filter) {
        var ctrl = this;
        ctrl.selectedReports = []
        ctrl.investigationForm ={}

        var init = function () {
            ctrl.type = $state.params.type;
            ctrl.memberId = $state.params.id;
            ctrl.getInvestigationDetailByMemberId(ctrl.memberId);
            ctrl.clinicalReports = ['Hemoglobin Estimation (Hb)', 'Total Leukocyte Count (TLC)', 'Differrential Leukocyte Count (DLC)',
                'MP (Slide Method)', 'ESR', 'Prothrombin Time Test and INR (Clotting time)', 'Blood Group (ABO-RH typing)', 'Platelet count by cell counter'];
            ctrl.bioChemistry = ['Random Blood Sugar', 'Fasting Blood Sugar', 'Postprandial Blood Sugar', 'HbA1C', 'Serum Creatinine', 'S. Bilirubin (T)',
                'S. Bilirubin (D)', 'Lipid Profile'];
            ctrl.serology = ['Rapid Plasma Reagin (RPR) Kit Test', 'HIV Rapid Test -ELISA', 'Dengue (Rapid test)', 'Malaria (Rapid test',
                'Sputum for AFB'];
            ctrl.urineAnalysis = ['Urine Sugar And Albumin', 'Urine Pregnancy test (UPT)', 'Proteinuria', 'Urine Creatinine'];
            ctrl.radiology = ['Chest X-Ray'];
            ctrl.stoolAnalysis = ['Stool for OVA and cyst'];
            ctrl.cardiology = ['ECG'];
        }

        ctrl.getInvestigationDetailByMemberId = () => {
            Mask.show();
            NcdDAO.retreiveInvestigationDetailByMemberId(ctrl.memberId).then((res) => {
                ctrl.investigationList = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            })

        }
        ctrl.toggleSelection = function (reportName, type) {
            var inx = -1;
            angular.forEach(ctrl.selectedReports, function (item, index) {
                if (item.report === reportName) {
                    inx = index;
                }
            })
            if (inx >= 0) {
                ctrl.selectedReports.splice(inx, 1)
            }
            else {
                ctrl.selectedReports.push({
                    report: reportName,
                    type: type
                });
            }
        };

        ctrl.saveInvestigation = function () {
            Mask.show();
            if (ctrl.form.$valid) {
                if (ctrl.selectedReports.length > 0) {
                    ctrl.investigationForm.reports = ctrl.selectedReports;
                    ctrl.investigationForm.doneBy = ctrl.type;
                    if (!ctrl.investigationForm.memberId) {
                        ctrl.investigationForm.memberId = Number($state.params.id);
                    }
                    ctrl.investigationForm.screeningDate = $scope.ncdmd.dateinfra.screeningDate 
                    ctrl.investigationForm.healthInfraId = $scope.ncdmd.dateinfra.healthInfraId 
                    NcdDAO.saveInvestigation(ctrl.investigationForm).then(function (res) {
                        toaster.pop('success', "Investigation Details saved successfully");
                        ctrl.clearForm();
                        ctrl.form.$setPristine()
                        ctrl.getInvestigationDetailByMemberId();
                    })
                    .catch(function(error){
                        toaster.pop('danger', 'Selected report(s) already prescribed for the same day.');
                    })
                }
                else {
                    toaster.pop('error', 'No Reports are selected');
                }
            }
            Mask.hide();
        }

        ctrl.clearForm = function(){
            ctrl.investigationForm.screeningDate = null
            ctrl.investigationForm.healthInfraId = null
            angular.forEach(ctrl.clinicalReports, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.bioChemistry, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.serology, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.urineAnalysis, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.radiology, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.stoolAnalysis, function(item,index){
                ctrl.report[item] = null;
            })
            angular.forEach(ctrl.cardiology, function(item,index){
                ctrl.report[item] = null;
            })
            ctrl.selectedReports = []
        }

        init();
    }
    angular.module('imtecho.controllers').controller('NcdInvestigation', NcdInvestigation);
})(window.angular);