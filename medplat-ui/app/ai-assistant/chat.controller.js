(function () {
    'use strict';
    angular.module('imtecho.controllers')
        .controller('ChatController', ChatController);

    function ChatController($timeout, $translate) {
        var chat = this;
        chat.showChat = false;
        chat.messages = [
            { text: "Hello! I am AI ANM, your assistant. How can I help you today?", sender: "ai", timestamp: new Date() }
        ];
        chat.newMessage = "";

        chat.toggleChat = function () {
            chat.showChat = !chat.showChat;
            if (chat.showChat) {
                $timeout(scrollBottom, 100);
            }
        };

        chat.sendMessage = function () {
            if (!chat.newMessage.trim()) return;

            chat.messages.push({
                text: chat.newMessage,
                sender: "user",
                timestamp: new Date()
            });

            var userMsg = chat.newMessage;
            chat.newMessage = "";
            $timeout(scrollBottom, 100);

            // Mock AI response
            $timeout(function () {
                var response = "I understand you're asking about: " + userMsg + ". I'm currently in testing mode, but I can help you report issues in the 'Manage' section.";
                chat.messages.push({
                    text: response,
                    sender: "ai",
                    timestamp: new Date()
                });
                $timeout(scrollBottom, 100);
            }, 1000);
        };

        chat.checkEnter = function (event) {
            if (event.which === 13) {
                chat.sendMessage();
            }
        };

        function scrollBottom() {
            var chatBox = document.getElementById('chat-messages');
            if (chatBox) {
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }
    }
})();
