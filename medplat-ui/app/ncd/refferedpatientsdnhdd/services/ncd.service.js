(function () {
    function NcdDnhddDAO($resource, APP_CONFIG) {
        const api = $resource(APP_CONFIG.apiPath + '/ncd-dnhdd/:action/:subaction/:id', {}, {
            retrieveMembers: {
                method: 'GET',
                params: {
                    action: 'members'
                },
                isArray: true
            },
            retrieveDetails: {
                method: 'GET'
            },
            retrieveLastRecordForHypertensionByMemberId: {
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
            retrieveFirstRecordForDiseaseByMemberId: {
                method: 'GET'
            },
            retrieveHypertensionDetailsByMemberAndDate: {
                method: 'GET',
                params: {
                    action: 'hypertensionbydate',
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
            retriveAllMedicines: {
                method: 'GET',
                params: {
                    action: 'medicines'
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
            registerNewMember: {
                method: 'POST'
            }
        });
        return {
            retrieveMembers: function (criteria) {
                return api.retrieveMembers(criteria).$promise;
            },
            retrieveDetails: function (memberId) {
                return api.retrieveDetails({ memberId: memberId }).$promise;
            },
            retrieveLastRecordForHypertensionByMemberId: function (memberId) {
                return api.retrieveLastRecordForHypertensionByMemberId({ action: 'lastrecordforhypertension', memberId: memberId }, {}).$promise;
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
            retrieveFirstRecordForDiseaseByMemberId: function (memberId, diseaseCode) {
                return api.retrieveFirstRecordForDiseaseByMemberId({ action: 'firstrecordfordisease', memberId: memberId , diseaseCode: diseaseCode}, {}).$promise;
            },            
            saveHyperTension: function (req) {
                return api.save({ 'action': 'hypertension' }, req).$promise;
            },
            saveDiabetes: function (req) {
                return api.save({ 'action': 'diabetes' }, req).$promise;
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
            saveFollowup: function (followupDto) {
                return api.save({ action: 'followup' }, followupDto).$promise;
            },
            retrieveAllMedicines: function () {
                return api.retriveAllMedicines().$promise;
            },
            retrieveTreatmentHistory: function (memberId, diseaseCode) {
                return api.retrieveTreatmentHistory({ subaction: memberId, diseaseCode: diseaseCode }).$promise;
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
            retrieveHypertensionDetailsByMemberAndDate: function (memberId, date) {
                return api.retrieveHypertensionDetailsByMemberAndDate({ memberId: memberId, screeningDate: date }).$promise;
            },
            retrieveDiabetesDetailsByMemberAndDate: function (memberId, date) {
                return api.retrieveDiabetesDetailsByMemberAndDate({ memberId: memberId, screeningDate: date }).$promise;
            },
            retrieveOralDetailsByMemberAndDate: function (memberId, date) {
                return api.retrieveOralDetailsByMemberAndDate({ memberId: memberId, screeningDate: date }).$promise;
            },
            retrieveBreastDetailsByMemberAndDate: function (memberId, date) {
                return api.retrieveBreastDetailsByMemberAndDate({ memberId: memberId, screeningDate: date }).$promise;
            },
            retrieveCervicalDetailsByMemberAndDate: function (memberId, date) {
                return api.retrieveCervicalDetailsByMemberAndDate({ memberId: memberId, screeningDate: date }).$promise;
            },
            registerNewMember: function(ncdMember) {
                return api.registerNewMember(ncdMember).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('NcdDnhddDAO', NcdDnhddDAO);
})();
