'use strict';
(function () {
    function TrainingService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/training/:action/:subaction/:query/:id', {}, {
            createTraining: {
                method: 'POST',
                params: {
                    action: 'create'
                }
            },
            rescheduleTraining: {
                method: 'PUT',
                params: {
                    action: 'reschedule'
                }
            },
            retrieveUpcomingTrainings: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'retrieve'
                }
            },
            retrieveCurrentTraining: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'retrieve'
                }
            },
            getTrainingsByUserLocation: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'dashboard'
                }
            },
            getTrainingStatusesByTrainerAndDate: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'status'
                }
            },
            getTrainingStatusesByTrainingId: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'status'
                }
            },
            updateTraineeStatus: {
                method: 'PUT',
                params: {
                    action: 'status'
                }
            },
            getTrainerByLocation: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'users'
                }
            },
            searchForCertifiedUserIds: {
                method: 'GET',
                params: {
                    action: 'certificates',
                    subaction: 'user',
                    query: 'ids'
                }
            },
            getAshasByPhc: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'users'
                }
            },
            getPendingTrainingStatus: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'pending'
                }
            },
            getCurrentTrainings: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'current'
                }
            },
            getCount: {
                method: 'GET',
                params: {
                    action: 'count'
                }
            },
            getTodaysTraining: {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'today'
                }
            }
        });
        return {
            createTraining: function (training) {
                return api.createTraining({}, training).$promise;
            },
            rescheduleTraining: function (trainingId, oldDate, newDate, isFirst) {
                var params = {
                    query: trainingId,
                    oldDate: oldDate,
                    newDate: newDate,
                    isFirst: isFirst
                };
                return api.rescheduleTraining(params, {}).$promise;
            },
            retrieveUpcomingTrainings: function (date, trainerId) {
                var params = {
                    afterDate: date,
                    trainerId: trainerId
                };
                return api.retrieveUpcomingTrainings(params).$promise;
            },
            retrieveCurrentTraining: function (date, trainerId) {
                var params = {
                    onDate: date,
                    trainerId: trainerId
                };
                return api.retrieveCurrentTraining(params).$promise;
            },
            getTrainingsByUserLocation: function (params) {
                return api.getTrainingsByUserLocation(params).$promise;
            },
            getTrainingStatusesByTrainerAndDate: function (date, trainerId) {
                var params = {
                    beforeDate: date,
                    trainerId: trainerId
                };
                return api.getTrainingStatusesByTrainerAndDate(params).$promise;
            },
            getTrainingStatusesByTrainingId: function (trainingId) {
                var params = {
                    trainingId: trainingId,
                };
                return api.getTrainingStatusesByTrainingId(params).$promise;
            },
            updateTraineeStatus: function (trainingStatus) {
                return api.updateTraineeStatus(trainingStatus).$promise;
            },
            getTrainerByLocation: function (locationId, roleId) {
                if (locationId !== null && roleId !== null) {
                    var params = {
                        locationId: locationId,
                        roleId: roleId
                    };
                    return api.getTrainerByLocation(params).$promise;
                }
            },
            searchForCertifiedUserIds: function (roleIds, courseId, locationId, type) {
                var params = {
                    roleIds: roleIds,
                    courseId: courseId,
                    locationId: locationId,
                    type: type
                };
                return api.searchForCertifiedUserIds(params).$promise;
            },
            getAshasByPhc: function (phcId, courseIds, roleId) {
                if (phcId !== null && courseIds !== null && roleId !== null) {
                    var params = {
                        locationId: phcId,
                        roleId: roleId,
                        courseId: courseIds
                    };
                    return api.getAshasByPhc(params).$promise;
                }
            },
            getPendingTrainingStatus: function () {
                return api.getPendingTrainingStatus().$promise;
            },
            getCurrentTrainings: function (trainerId) {
                var params = {
                    trainerId: trainerId
                }
                return api.getCurrentTrainings(params).$promise;
            },
            getCount: function (role, course, locations) {
                var params = {
                    roleIds: role,
                    courseId: course,
                    locationIds: locations
                }
                return api.getCount(params).$promise;
            },
            getTodaysTraining: function (userId) {
                var params = {
                    userId: userId
                }
                return api.getTodaysTraining(params).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('TrainingService', TrainingService);
})();
