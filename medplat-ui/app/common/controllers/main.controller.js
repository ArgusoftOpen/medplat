(function () {
    var mainController = function ($scope, $rootScope, APP_CONFIG, ENV) {
        $scope.setAppName = () => {
            switch (ENV.implementation) {
                case 'sewa_rural':
                    $rootScope.appName = 'Sewa Rural';
                    break;
                case 'telangana':
                    $rootScope.appName = 'Amma-Kosam';
                    break;
                case 'uttarakhand':
                    $rootScope.appName = 'Chardham';
                    break;
                default:
                    $rootScope.appName = 'MEDplat';
                    break;
            }
        }
        $rootScope.toastOptions = {
            'time-out': 4000,
            'close-button': true,
            'body-output-type': 'trustedHtml'
        };
        $rootScope.familyIdFormat = APP_CONFIG.familyIdFormat;
        $scope.$on('invalid_grant', function () {
            $rootScope.logOut();
        });
        $scope.$on('invalid_auth', function () {
            $rootScope.logOut();
        });
        $scope.setAppName();

        // --- TASK #97: AI CHATBOT LOGIC START ---
        $scope.userMsg = "";
        $scope.chatMessages = [
            { text: "Namaste! Nenu meeku ela sahayapadagalanu?", type: "bot" }
        ];

        $scope.aiAssistantResponse = function() {
            if ($scope.userMsg.trim() !== "") {
                // User message add cheyadam
                $scope.chatMessages.push({ text: $scope.userMsg, type: "user" });
                
                // Dummy AI Response (Real-time support simulation)
                var currentInput = $scope.userMsg;
                $scope.userMsg = ""; // Input clear cheyadam

                setTimeout(function() {
                    $scope.$apply(function() {
                        $scope.chatMessages.push({ 
                            text: "AI Assistant (AI ANM): Processing your request about '" + currentInput + "'. Issue report chestunnanu...", 
                            type: "bot" 
                        });
                    });
                }, 1000);
            }
        };
        // --- TASK #97: AI CHATBOT LOGIC END ---
    };
    angular.module('imtecho.controllers').controller('MainController', mainController);
})();