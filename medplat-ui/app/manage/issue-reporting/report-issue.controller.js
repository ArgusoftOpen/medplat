(function () {
    'use strict';
    angular.module('imtecho.controllers')
        .controller('ReportIssueController', ReportIssueController);

    function ReportIssueController($state, toaster) {
        var ctrl = this;
        ctrl.step = 1;
        ctrl.issue = {
            type: null,
            title: "",
            description: "",
            attachments: []
        };

        ctrl.issueTypes = [
            { id: 'tech', name: 'Technical', icon: 'fa-laptop', desc: 'Software bugs, login issues, etc.' },
            { id: 'data', name: 'Data Entry', icon: 'fa-database', desc: 'Incorrect data, missing records.' },
            { id: 'other', name: 'Other', icon: 'fa-question-circle', desc: 'General queries and feedback.' }
        ];

        ctrl.selectType = function (id) {
            ctrl.issue.type = id;
        };

        ctrl.nextStep = function () {
            if (ctrl.step < 3) ctrl.step++;
        };

        ctrl.prevStep = function () {
            if (ctrl.step > 1) ctrl.step--;
        };

        ctrl.getIssueTypeName = function () {
            var type = ctrl.issueTypes.find(t => t.id === ctrl.issue.type);
            return type ? type.name : "";
        };

        ctrl.submit = function () {
            // Mock submission
            toaster.pop('success', 'Success', 'Your issue has been reported successfully. Ticket ID: #12345');
            $state.go('techo.dashboard.webtasks');
        };
    }
})();
