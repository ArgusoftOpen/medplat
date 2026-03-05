(function () {
    'use strict';
    angular.module('imtecho')
        .service('ChatbotService', ChatbotService);

    ChatbotService.$inject = ['$http', 'AUTHENTICATION_URL', 'GENERAL_CONFIG'];

    function ChatbotService($http, AUTHENTICATION_URL, GENERAL_CONFIG) {
        var service = this;

        service.sendMessage = function (message) {
            // Mocking AI response for now
            // In real scenario: return $http.post(GENERAL_CONFIG.apiUrl + '/chatbot/message', { text: message });
            return new Promise(function (resolve) {
                setTimeout(function () {
                    var responses = [
                        "I understand. Let me help you with that.",
                        "I've noted the issue. Would you like to create a ticket?",
                        "Hello! I am AI ANM, your assistant. How can I help you today?",
                        "Please provide more details so I can assist you better.",
                        "That's interesting! I'll look into it."
                    ];
                    var randomResponse = responses[Math.floor(Math.random() * responses.length)];
                    resolve({ data: { response: randomResponse } });
                }, 1000);
            });
        };

        service.getTickets = function (params) {
            // Mocking ticket retrieval
            return new Promise(function (resolve) {
                setTimeout(function () {
                    resolve({
                        data: {
                            content: [
                                { id: 'TKT-001', title: 'Login Issue', status: 'OPEN', priority: 'HIGH', createdAt: new Date() },
                                { id: 'TKT-002', title: 'Data Not Syncing', status: 'IN_PROGRESS', priority: 'MEDIUM', createdAt: new Date() },
                                { id: 'TKT-003', title: 'New Feature Request', status: 'CLOSED', priority: 'LOW', createdAt: new Date() }
                            ],
                            totalElements: 3
                        }
                    });
                }, 500);
            });
        };

        service.reportIssue = function (issueData) {
            // Mocking issue reporting
            return new Promise(function (resolve) {
                setTimeout(function () {
                    resolve({ data: { ticketId: 'TKT-' + Math.floor(1000 + Math.random() * 9000) } });
                }, 1000);
            });
        };
    }
})();
