'use strict';
(function () {
    function CourseService($resource, APP_CONFIG) {
        var api = $resource(APP_CONFIG.apiPath + '/course/:action/:query/:id', {},
            {
                createCourse: {
                    method: 'POST',
                    params: {
                        action: 'create'
                    }
                },
                retrieveAllCourse: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'all'
                    }
                },
                retrieveAllRoles: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'allRoles'
                    }
                },
                getCoursesByRoles:{
                    method: 'GET',
                    isArray: true,
                    params:{
                        action: 'roles'
                    }
                },
                getCourseCompletionDate: {
                    method: 'GET',
                    params: {
                        action: 'completiondate'
                    },
                    transformResponse: function (res) {
                        return { result: res };
                    }
                },
                toggleActive: {
                    method: 'GET',
                    params: {
                        action: 'toggleactive'
                    }
                },
                getCourseById: {
                    method: 'GET'
                },
                updateCourse: {
                    method: 'PUT'
                },
                getRolesByCourses: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'rolesbycourse'
                    }
                },
                getTrainerRolesByCourses: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'rolesbycourse',
                        query: 'trainer'
                    }
                },
                getCoursesByModuleIds: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'module'
                    }
                }
            });
        return {
            createCourse: function (course) {
                return api.createCourse({}, course).$promise;
            },
            retrieveAllCourse: function (isActive) {
                if (isActive != undefined) {
                    var params = {
                        isActive: isActive
                    }
                    return api.retrieveAllCourse(params).$promise;
                }
                else {
                    return api.retrieveAllCourse().$promise;
                }
            },
            retrieveAllRoles: function () {
                return api.retrieveAllRoles().$promise;
            },
            getCourseCompletionDate: function (startDate, courseId) {
                var params = {
                    trainingStartDate: startDate,
                    courseId: courseId
                };
                return api.getCourseCompletionDate(params).$promise;
            },
            toggleActive: function (courseId, isActive) {
                var params = {
                    courseId: courseId,
                    isActive: isActive
                }
                return api.toggleActive(params).$promise;
            },
            getCourseById: function (courseId) {
                var params = {
                    action: courseId
                }
                return api.getCourseById(params).$promise;
            },
            updateCourse: function (courseObj) {
                return api.updateCourse({}, courseObj).$promise;
            },
            getRolesByCourses: function (course) {
                var params = {
                    courseId: course
                }
                return api.getRolesByCourses(params).$promise;
            },
            getCoursesByRoles: function (roleIdList) {
                var params = {
                    roles: roleIdList
                }
                return api.getCoursesByRoles(params).$promise;
            },
            getTrainerRolesByCourses: function (course) {
                var params = {
                    courseId: course
                }
                return api.getTrainerRolesByCourses(params).$promise;
            },
            getCoursesByModuleIds: function (moduleIds) {
                var params = {
                    moduleIds: moduleIds
                }
                return api.getCoursesByModuleIds(params).$promise;
            }
        };
    }
    angular.module('imtecho.service').factory('CourseService', CourseService);
})();