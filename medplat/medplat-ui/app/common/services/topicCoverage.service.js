'use strict';
(function () {
    function TopicCoverageService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/topiccoverage/:action/:subaction/:query/:id', {}, {
            getTopicCoverageByTrainingId: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'training'
                }
            },
            getTopicCoveragesByTrainingAndDate: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'retrieve'
                }
            },
            saveTopicCoverage: {
                method: 'PUT',
                params: {
                    action: 'update'
                }
            },
            submitTopicCoverages: {
                method: 'PUT',
                params: {
                    action: 'state',
                    subaction: 'change'
                }
            }
        });
        return {
            getTopicCoverageByTrainingId: function (trainingId) {
                let params = {
                    trainingId: trainingId
                };
                return api.getTopicCoverageByTrainingId(params).$promise;
            },
            getTopicCoveragesByTrainingAndDate: function (trainingId, date) {
                let params = {
                    trainingId: trainingId,
                    beforeDate: date
                };
                return api.getTopicCoveragesByTrainingAndDate(params).$promise;
            },
            saveTopicCoverage: function (topic) {
                return api.saveTopicCoverage(topic).$promise;
            },
            submitTopicCoverages: function (topicIds) {
                let params = {
                    topicCoverageIds: topicIds
                };
                return api.submitTopicCoverages(params).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('TopicCoverageService', TopicCoverageService);
})();
