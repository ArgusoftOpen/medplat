(function () {
    function NcdDAO($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/ncd/:action/:subaction/:id', {}, {
            retrieveDetails: {
                method: 'GET'
            },
            retrieveAll: {
                method: 'GET',
                params: {
                    action: 'members'
                },
                isArray: true
            },
            retrieveAllCFS: {
                method: 'GET',
                params: {
                    action: 'cfsmembers'
                },
                isArray: true
            },
            retrieveAllForStatus: {
                method: 'GET',
                params: {
                    action: 'mrsmembers'
                },
                isArray: true
            },
            retrieveComplaintsByMemberId: {
                method: 'GET',
                params: {
                    action: 'complaints'
                },
                isArray: true
            },
            retrieveChildHealthInfraByParentId: {
                method: 'GET',
                params: {
                    action: 'retrieveHealthInfraByParentId'
                },
                isArray: true
            },
            retrieveDrugReceivedByInfraId: {
                method: 'GET',
                params: {
                    action: 'drugReceivedByInfraId'
                },
                isArray: true
            },
            retriveAllMedicines: {
                method: 'GET',
                params: {
                    action: 'medicines'
                },
                isArray: true
            },
            retriveDrugList: {
                method: 'GET',
                params: {
                    action: 'druglist'
                },
                isArray: true
            },
            retriveAllGeneralDrugs: {
                method: 'GET',
                params: {
                    action: 'generaldrug'
                },
                isArray: true
            },
            retriveAllDrugInventory: {
                method: 'GET',
                params: {
                    action: 'drugInventory'
                },
                isArray: true
            },
            retrieveTreatmentHistory: {
                method: 'GET',
                params: {
                    action: 'treatmentHistory'
                },
                isArray: true
            },
            retrieveAllForFollowup: {
                method: 'GET',
                params: {
                    action: 'membersForFollowup'
                },
                isArray: true
            },
            retrieveReffForToday: {
                method: 'GET',
                params: {
                    action: 'reffForToday',

                },
                isArray: true
            },
            retrieveNextFollowup: {
                method: 'GET',
                params: {
                    action: 'nextFollowup',

                },
                isArray: true
            },
            retrieveHypertensionDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'hypertensionbydate',
                },
            },
            retrieveInitialAssessmentDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'initialAssessmentbydate',
                },
            },
            retrieveGeneralDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'generalbydate',
                },
            },
            retrieveMentalHealthDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'mentalHealthbydate',
                },
            },
            retrieveDiabetesDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'diabetesbydate',
                },
            },
            retrieveOralDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'oralbydate',
                },
            },
            retrieveBreastDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'breastbydate',
                },
            },
            retrieveCervicalDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'cervicalbydate',
                },
            },
            retrieveLastRecordForHypertensionByMemberId: {
                method: 'GET',
                isArray: true
            },
            retrieveLastRecordForInitialAssessmentByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForGeneralByMemberId: {
                method: 'GET'
            },
            retrieveMbbsMOReviewByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForMentalHealthByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForDiabetesByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForOralByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForBreastByMemberId: {
                method: 'GET'
            },
            retrieveLastRecordForCervicalByMemberId: {
                method: 'GET'
            },
            retrievePrescribedMedicineForUser: {
                method: 'GET',
                params: {
                    action: 'retrievePrescribedMedicineForUser'
                },
                isArray: true
            },
            retrievePrescribedMedicineHistoryForUser: {
                method: 'GET',
                params: {
                    action: 'retrievePrescribedMedicineHistoryForUser'
                },
                isArray: true
            },
            retrieveMembers: {
                method: 'GET',
                params: {
                    action: 'retrieveMembers'
                },
                isArray: true
            },
            retreiveSearchedMembers: {
                method: 'GET',
                params: {
                    action: 'retreiveSearchedMembers'
                },
                isArray: true
            },
            retreiveInvestigationDetailByMemberId: {
                method: 'GET',
                isArray: true
            },
            retrieveLastCommentForGeneralByMemberIdAndType:{
                method: 'GET'
            },
            retrieveLastCommentByMBBS:{
                method: 'GET'
            },
            retrieveMoReviewByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'moreviewbydate',
                },
            },
            retrieveMoReviewFollowupByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'moreviewfollowupbydate',
                },
            },
            retreiveSearchedMembersMOReview: {
                method: 'GET',
                params: {
                    action: 'retreiveSearchedMembersMOReview'
                },
                isArray: true
            },
            retrieveLastCommentByMOReview:{
                method: 'GET'
            },
            retrieveLastCommentByMOReviewFollowup:{
                method: 'GET'
            },
            retrieveCVCDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'cvcByDate',
                },
            },
        });
        return {
            retrieveDetails: function (memberId) {
                return api.retrieveDetails({ memberId: memberId }).$promise;
            },
            save: function (req) {
                return api.save({ 'action': 'member' }, req).$promise;
            },
            saveHyperTension: function (req) {
                return api.save({ 'action': 'hypertension' }, req).$promise;
            },
            saveMbbsMOReview: function (req) {
                return api.save({ 'action': 'mbbsmoreview' }, req).$promise;
            },
            saveDiabetes: function (req) {
                return api.save({ 'action': 'diabetes' }, req).$promise;
            },
            saveInitialAssessment: function (req) {
                return api.save({ 'action': 'initialAssessment' }, req).$promise;
            },
            saveGeneral: function (req) {
                return api.save({ 'action': 'general' }, req).$promise;
            },
            saveDrugInventory: function (req) {
                return api.save({ 'action': 'drugIssued' }, req).$promise;
            },
            saveMentalHealth: function (req) {
                return api.save({ 'action': 'mentalHealth' }, req).$promise;
            },
            saveCervical: function (req) {
                return api.save({ 'action': 'cervical' }, req).$promise;
            },
            saveOral: function (req) {
                return api.save({ 'action': 'oral' }, req).$promise;
            },
            saveBreast: function (req) {
                return api.save({ 'action': 'breast' }, req).$promise;
            },
            retrieveAll: function (criteria) {
                return api.retrieveAll(criteria).$promise;
            },
            retrieveAllCFS: function (criteria) {
                return api.retrieveAllCFS(criteria).$promise;
            },
            retrieveAllForStatus: function (criteria) {
                return api.retrieveAllForStatus(criteria).$promise;
            },
            saveComplaint: function (complaint) {
                return api.save({ 'action': 'complaint' }, complaint).$promise;
            },
            retrieveComplaintsByMemberId: function (memberId) {
                return api.retrieveComplaintsByMemberId({ 'subaction': memberId }).$promise
            },
            retrieveChildHealthInfraByParentId: function (healthId) {
                return api.retrieveChildHealthInfraByParentId({ 'subaction': healthId }).$promise;
            },
            retrieveDrugReceivedByInfraId: function (healthId) {
                return api.retrieveDrugReceivedByInfraId({ 'subaction': healthId }).$promise;
            },
            retriveAllMedicines: function () {
                return api.retriveAllMedicines().$promise;
            },
            retriveDrugList: function () {
                return api.retriveDrugList().$promise;
            },
            retriveAllGeneralDrugs: function () {
                return api.retriveAllGeneralDrugs().$promise;
            },
            retriveAllDrugInventory: function (healthId, duration) {
                return api.retriveAllDrugInventory({ subaction: healthId, duration: duration }).$promise;
            },
            saveDiagnosis: function (diagnosisDto) {
                return api.save({ 'action': 'diagnosis' }, diagnosisDto).$promise;
            },
            retrieveTreatmentHistory: function (memberId, diseaseCode) {
                return api.retrieveTreatmentHistory({ subaction: memberId, diseaseCode: diseaseCode }).$promise;
            },
            saveReferral: function (referralDto) {
                return api.save({ action: 'referral' }, referralDto).$promise;
            },
            saveFollowup: function (followupDto) {
                return api.save({ action: 'followup' }, followupDto).$promise;
            },
            retrieveAllForFollowup: function (criteria) {
                return api.retrieveAllForFollowup(criteria).$promise;
            },
            retrieveReffForToday: function (memberId) {
                return api.retrieveReffForToday({ subaction: memberId }).$promise;
            },
            retrieveNextFollowup: function (memberId) {
                return api.retrieveNextFollowup({ subaction: memberId }).$promise;
            },
            retrieveHypertensionDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveHypertensionDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveInitialAssessmentDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveInitialAssessmentDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveGeneralDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveGeneralDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveMentalHealthDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveMentalHealthDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveDiabetesDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveDiabetesDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveOralDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveOralDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveBreastDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveBreastDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveCervicalDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveCervicalDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type: type }).$promise;
            },
            retrieveLastRecordForHypertensionByMemberId: function (memberId) {
                return api.retrieveLastRecordForHypertensionByMemberId({ action: 'lastrecordforhypertension', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForInitialAssessmentByMemberId: function (memberId) {
                return api.retrieveLastRecordForInitialAssessmentByMemberId({ action: 'lastrecordforinitialAssessment', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForGeneralByMemberId: function (memberId) {
                return api.retrieveLastRecordForGeneralByMemberId({ action: 'lastrecordforgeneral', memberId: memberId }, {}).$promise;
            },
            retrieveMbbsMOReviewByMemberId: function (memberId) {
                return api.retrieveMbbsMOReviewByMemberId({ action: 'getmbbsmoreview', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForMentalHealthByMemberId: function (memberId) {
                return api.retrieveLastRecordForMentalHealthByMemberId({ action: 'lastrecordforMentalHealth', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForDiabetesByMemberId: function (memberId) {
                return api.retrieveLastRecordForDiabetesByMemberId({ action: 'lastrecordfordiabetes', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForOralByMemberId: function (memberId) {
                return api.retrieveLastRecordForOralByMemberId({ action: 'lastrecordfororal', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForBreastByMemberId: function (memberId) {
                return api.retrieveLastRecordForBreastByMemberId({ action: 'lastrecordforbreast', memberId: memberId }, {}).$promise;
            },
            retrieveLastRecordForCervicalByMemberId: function (memberId) {
                return api.retrieveLastRecordForCervicalByMemberId({ action: 'lastrecordforcervical', memberId: memberId }, {}).$promise;
            },
            retrievePrescribedMedicineForUser: function (memberId) {
                return api.retrievePrescribedMedicineForUser({ action: 'retrievePrescribedMedicineForUser', memberId: memberId }, {}).$promise;
            },
            retrievePrescribedMedicineHistoryForUser: function (memberId) {
                return api.retrievePrescribedMedicineHistoryForUser({ action: 'retrievePrescribedMedicineHistoryForUser', memberId: memberId }, {}).$promise;
            },
            retrieveMembers: function (criteria) {
                return api.retrieveMembers(criteria).$promise;
            },
            retreiveSearchedMembers: function (criteria) {
                return api.retreiveSearchedMembers(criteria).$promise;
            },
            saveInvestigation: function (req) {
                return api.save({ 'action': 'investigation' }, req).$promise;
            },
            retreiveInvestigationDetailByMemberId: function (memberId) {
                return api.retreiveInvestigationDetailByMemberId({ action: 'retreiveInvestigationDetailByMemberId', memberId: memberId }).$promise;
            },
            retrieveLastCommentForGeneralByMemberIdAndType: function (memberId,type) {
                return api.retrieveLastCommentForGeneralByMemberIdAndType({ action: 'lastcommentforgeneralbytype', memberId: memberId , type:type}, {}).$promise;
            },
            retrieveLastCommentByMBBS: function (memberId) {
                return api.retrieveLastCommentByMBBS({ action: 'getLastMBBSComment', memberId: memberId }, {}).$promise;
            },
            saveMOReview: function (req) {
                return api.save({ 'action': 'moreview' }, req).$promise;
            },
            saveMOReviewFollowup: function (req) {
                return api.save({ 'action': 'moreviewfollowup' }, req).$promise;
            },
            retrieveMoReviewByMemberAndDate: function (memberId, date, type) {
                return api.retrieveMoReviewByMemberAndDate({ memberId: memberId, screeningDate: date}).$promise;
            },
            retrieveMoReviewFollowupByMemberAndDate: function (memberId, date, type) {
                return api.retrieveMoReviewFollowupByMemberAndDate({ memberId: memberId, screeningDate: date}).$promise;
            },
            retreiveSearchedMembersMOReview: function (criteria) {
                return api.retreiveSearchedMembersMOReview(criteria).$promise;
            },
            retrieveLastCommentByMOReview: function (memberId) {
                return api.retrieveLastCommentByMOReview({ action: 'getLastMOReviewComment', memberId: memberId }, {}).$promise;
            },
            retrieveLastCommentByMOReviewFollowup: function (memberId) {
                return api.retrieveLastCommentByMOReviewFollowup({ action: 'getLastMOReviewFollowupComment', memberId: memberId }, {}).$promise;
            },
            saveCVCForm: function (req) {
                return api.save({ 'action': 'cvcform' }, req).$promise;
            },
            retrieveCVCDetailsByMemberAndDate: function (memberId, date, type) {
                return api.retrieveCVCDetailsByMemberAndDate({ memberId: memberId, screeningDate: date, type:type}).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NcdDAO', NcdDAO);
})();
