'use strict';
angular.module('imtecho.service').factory('CommonService', ['$state',
    function ($state) {
        var commonService = this;
        commonService.groupTopicsByDate = function (topics) {
            var groupedResults = _.chain(topics)
                .groupBy('effectiveDate')
                .map(function (value, key) {
                    return {
                        date: new Date(parseInt(key, 10)),
                        trainingTopic: _.pluck(value, 'topicCoverageName'),
                        submittedList: _.pluck(value, 'submittedOn'),
                        status: _.pluck(value, 'topicCoverageState')[0]
                    };
                })
                .value();
            groupedResults.sort(function (a, b) {
                return new Date(a.date) - new Date(b.date);
            });
            var count = 1;
            angular.forEach(groupedResults, function (res) {
                res.day = count;
                count++;
            });
            return groupedResults;
        };
        return commonService;
    }
]);
