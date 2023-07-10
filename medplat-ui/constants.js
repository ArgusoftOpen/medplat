angular.module('imtecho').constant('States', {
    states: [{stateKey: "trainingDashboard", stateName: 'imtecho.trainerdashboard', label: 'Training Dashboard', icon: 'fa fa-tachometer'},
        {stateKey: "trainingSchedule", stateName: 'imtecho.scheduletraining', label: 'Training Schedule', icon: 'fa fa-calendar'},
        {stateKey: "traineeStatus", stateName: 'imtecho.traineestatus', label: 'Trainee Status', icon: 'fa fa-address-card-o'},
        {stateKey: "fhsDashboard", stateName: 'imtecho.fhsdashboard', label: 'FHS Dashboard', icon: 'fa fa-tachometer'},
        {stateKey: "manageUsers", stateName: 'imtecho.manage.user', label: 'Manage Users', icon: 'fa fa-users'},
        {stateKey: "fhsReverification", stateName: 'imtecho.familymembers', label: 'FHS Re-Verification', icon: 'fa fa-check-square-o'},
        {stateKey: "fhsSupervisorReverification", stateName: 'imtecho.fhsrverification', label: 'Supervisor Re-Verification', icon: 'fa fa-check-square-o'},
        {stateKey: "createCourse", stateName: 'imtecho.createcourse', label: 'Create Course', icon: 'fa fa-book'}
    ]
});

angular.module('imtecho').constant("Roles", {
    "trainer": "Trainer",
    "asha": "Asha"
});

angular.module('imtecho').constant("StateConstant", {
    "trainingSubmitted": "com.argusoft.imtecho.training.training.state.submitted",
    "villageType": "V",
    "state": "S",
    "district": "D",
    "block": "B",
    "phc": "P",
    "subcenter": "SC",
    "area": "A",
    "corporation": "C",
    "zone": "Z",
    "uphc": "U",
    "urbanAreaType": "UA",
    "urbanSubArea": "com.argusoft.imtecho.organizationunit.type.urban.subarea"
});

angular.module('imtecho').constant('_',
        window._
        );

angular.module('imtecho').constant("CourseConstant", {
    "type" : "com.argusoft.imtecho.course.course.type",
    "activeState" : "com.argusoft.imtecho.course.topic.state.active",
    "topicType" :  "com.argusoft.imtecho.course.topic.type"
});

