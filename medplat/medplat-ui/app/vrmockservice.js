(function () {
    var VRMockService = function ($httpBackend, $filter) {
        this.person = {
            personId: "1",
            firstName: "Admin 1",
            lastName: "Mehta",
            skillSet: ["1", "4"]
        };

        this.personDashboardDetails = {
            personId: "1",
            firstName: "Admin 1",
            lastName: "Mehta",
            callTarget: 45,
            callsCompleted: 35,
            taskTarget: 150,
            tasksCompleted: 120
        };

        this.personToVerify1 = {
            personId: "11",
            firstName: "Madhu",
            lastName: "Nanda",
            contactNumber: "+919876543210",
            personType: "Asha"
        };

        //fetched on start call, updated on end call/no call
        this.personPersonToVerify1CallDetails = {
            personToVerifyId: "11",
            contactNumber: "+919876543210",
            personId: "1",
            callStatus: null,
            callStartTime: null,
            callEndTime: null,
            callId: "550",
            callDropTime: null
        };

        this.personToVerify1BeneficiarytaskList = {
            beneficiaryTaskList: [
                {
                    beneficiaryName: "Chirag Unadkat",
                    beneficiaryId: "1001",
                    // what details here?
                    taskList: [
                        {
                            taskId: "2001",
                            taskName: "ANC Home Visit 1"
                            //what details will be non-dynamic, what details will be dynamic ?
                        },
                        {
                            taskId: "2002",
                            taskName: "ANC Home Visit 2"
                        }
                    ],
                },
                {
                    beneficiaryName: "Radha Singhal",
                    beneficiaryId: "1002",
                    taskList: [
                        {
                            taskId: "2001",
                            taskName: "ANC Home Visit 1"
                        },
                        {
                            taskId: "2002",
                            taskName: "ANC Home Visit 2"
                        }
                    ],
                },

            ]
        };
    };
    angular.module('imtecho').run(["$httpBackend", "$filter", VRMockService]);
})();
