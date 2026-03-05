(function () {
    'use strict';
    angular.module('imtecho.services')
        .service('NotificationService', NotificationService);

    function NotificationService($timeout, toaster) {
        var self = this;

        self.startSimulating = function () {
            // Periodically show random notifications for demo purposes
            $timeout(function () {
                toaster.pop('info', 'New Ticket', 'A new high priority ticket has been assigned to you: #9876');
                self.startSimulating(); // Continue simulating
            }, 60000); // Every 60 seconds
        };
    }
})();
