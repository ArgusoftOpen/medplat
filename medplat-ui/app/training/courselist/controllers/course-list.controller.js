(function () {
    function CourseListController(CourseService, Mask, $uibModal, toaster, QueryDAO, GeneralUtil) {
        var ctrl = this;
        ctrl.courseList = ctrl.filteredCourseList = [];
        ctrl.onlineSearch = ctrl.offlineSearch = true;

        ctrl.retrieveAllCourses = function () {
            Mask.show();
            CourseService.retrieveAllCourse().then(function (res) {
                res.forEach((r) => {
                    let module = ctrl.moduleConstants.find(e => e.id === r.courseModuleId);
                    r.moduleName = module ? module.fieldValue : null;
                    r.trainerRole = r.courseType === 'ONLINE' ? '-' : r.trainerRole;
                });
                ctrl.courseList = res;
                ctrl.filterCourseType();
            }).finally(function () {
                Mask.hide();
            })
        }

        ctrl.filterCourseType = () => {
            ctrl.filteredCourseList = ctrl.courseList.filter((course) => {
                if(course.courseType == "ONLINE") {
                    Mask.show();
                    QueryDAO.execute({
                        code: 'get_course_media_size_by_id',
                        parameters: {
                            courseId: Number(course.courseId)
                        }
                    }).then((response) => {
                        course.courseSize = (response.result[0].size/1000000).toFixed(2) + " MB";
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                } else {
                    course.courseSize = '-';
                }
                if (Array.isArray(ctrl.moduleSearch) && ctrl.moduleSearch.length) {
                    return ctrl.moduleSearch.includes(course.moduleName);
                } else {
                    return true;
                }
            }).filter((course) => {
                if (ctrl.onlineSearch && !ctrl.offlineSearch) {
                    return course.courseType === 'ONLINE'
                } else if (!ctrl.onlineSearch && ctrl.offlineSearch) {
                    return course.courseType === 'OFFLINE'
                } else if (!ctrl.onlineSearch && !ctrl.offlineSearch) {
                    return false;
                } else {
                    return true;
                }
            });
        }

        ctrl.toggleActive = function (course) {
            var changedState = 'INACTIVE';
            var isActive = false;
            if (course.courseState == 'INACTIVE') {
                changedState = 'ACTIVE';
                isActive = true;
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: function () {
                        return "Are you sure you want to change the state from " + course.courseState + ' to ' + changedState + '? ';
                    }
                }
            });
            modalInstance.result.then(function () {
                Mask.show();
                CourseService.toggleActive(course.courseId, isActive).then(function () {
                    ctrl.retrieveAllCourses();
                    toaster.pop('success', 'State is successfully changed from ' + course.courseState + ' to ' + changedState);
                }).finally(function () {
                    Mask.hide();
                });
            }, function () { });
        };

        ctrl.init = function () {
            Mask.show();
            QueryDAO.execute({
                code: 'get_course_module_name'
            }).then((response) => {
                ctrl.moduleConstants = response.result;
                ctrl.retrieveAllCourses();
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('CourseListController', CourseListController);
})();
