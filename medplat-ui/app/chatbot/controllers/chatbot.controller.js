(function () {
    'use strict';
    angular.module('imtecho')
        .controller('ChatbotController', ChatbotController);

    ChatbotController.$inject = ['$scope', 'ChatbotService', '$state'];

    function ChatbotController($scope, ChatbotService, $state) {
        var vm = this;

        vm.messages = [
            { sender: 'AI ANM', text: 'Hello! I am your AI assistant. How can I help you today?', time: new Date() }
        ];
        vm.newMessage = '';
        vm.isTyping = false;
        vm.selectedLanguage = 'en';
        vm.languages = [
            { code: 'en', name: 'English' },
            { code: 'hi', name: 'Hindi' },
            { code: 'gu', name: 'Gujarati' }
        ];

        vm.sendMessage = function () {
            if (!vm.newMessage.trim()) return;

            var userMsg = { sender: 'You', text: vm.newMessage, time: new Date() };
            vm.messages.push(userMsg);
            var textToSend = vm.newMessage;
            vm.newMessage = '';
            vm.isTyping = true;

            ChatbotService.sendMessage(textToSend).then(function (response) {
                $scope.$apply(function () {
                    vm.messages.push({
                        sender: 'AI ANM',
                        text: response.data.response,
                        time: new Date()
                    });
                    vm.isTyping = false;
                });
            });
        };

        vm.changeLanguage = function () {
            // Toggle language logic here
            console.log('Language changed to: ' + vm.selectedLanguage);
        };

        vm.reportIssue = function () {
            vm.messages.push({
                sender: 'AI ANM',
                text: 'Sure, I can help you report an issue. What type of issue is it?',
                isFlow: true,
                options: ['Technical', 'Clinical', 'Administrative', 'Other']
            });
        };

        vm.selectOption = function (option) {
            vm.messages.push({ sender: 'You', text: option, time: new Date() });
            vm.messages.push({
                sender: 'AI ANM',
                text: 'I\'ve started a report for a ' + option + ' issue. Please describe the issue in detail.',
                time: new Date()
            });
        };

        vm.closeChat = function () {
            $state.go('techo.dashboard.webtasks');
        };
    }
})();
