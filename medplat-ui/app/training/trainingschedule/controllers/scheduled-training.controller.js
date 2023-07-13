(function () {
    function ScheduledTrainingCtrl(ConstantService, AuthenticateService, $filter, $uibModal, Mask, $timeout, QueryDAO, $state, CourseService, PagingForQueryBuilderService, GeneralUtil, $sessionStorage) {
        $('.search').on('click', function () {
            $(this).parent().find('.text').toggleClass('active');
            $(this).parent().find('.search-box').toggleClass('active');
        });
        var ctrl = this;
        var todayInTime = new Date().getTime();
        ctrl.todayDate = new Date();
        ctrl.scheduleFilter = {};
        ctrl.trainingScheduleListOriginal = [];
        ctrl.isShowPast = false;
        ctrl.canMarkAttendance = null;
        ctrl.pagingService = PagingForQueryBuilderService.initialize();

        AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
            ctrl.canMarkAttendance = res.featureJson.canMarkAttendance;
        })

        //To filter Schedules
        ctrl.filterSchedules = function () {
            var scheduleParams = {};
            this.activeFilters = angular.copy(this.scheduleFilter);
            angular.forEach(ctrl.scheduleFilter, function (param, key) {
                if (param !== '') {
                    scheduleParams[key] = param;
                }
            });
            ctrl.trainingScheduleList = $filter('filter')(ctrl.trainingScheduleListOriginal, scheduleParams);
        };

        // ctrl.getModuleConstants = function () {
        //     Mask.show();
        //     QueryDAO.execute({ code: 'get_course_module_name' }).then(function (res) {
        //         ctrl.moduleConstants = res.result;
        //         ctrl.moduleConstants.unshift({ id: -1, fieldValue: 'All Modules' })
        //     }).finally(function () {
        //         Mask.hide();
        //     });
        // }

        ctrl.getTrainingStateConstants = function () {
            ctrl.trainingStateConstants = null;
            Mask.show();
            QueryDAO.execute({ code: 'get_training_states', parameters: { courseType: ctrl.courseType ? ctrl.courseType : null, } }).then(function (res) {
                ctrl.trainingStateConstants = res.result;
                if (ctrl.courseType && ctrl.courseType == 'ONLINE') {
                    ctrl.trainingStateConstants.unshift({ id: -1, training_state: 'Pending Schedule' });
                    ctrl.trainingStateConstants.filter(item => {
                        item.training_state = item.training_state == 'DRAFT' ? 'Scheduled' : item.training_state;
                    });
                } else {
                    let filteredTrainingStateConstants = [];
                    let trainingStateConstantSet = new Set();
                    ctrl.trainingStateConstants.forEach(item => {
                        item.training_state = (item.training_state == 'DRAFT' || item.training_state == 'SAVED') ? 'Update Pending' : (item.training_state == 'SUBMITTED' ? 'Completed' : item.training_state);
                        if(trainingStateConstantSet.has(item.training_state)) {
                            return;
                        }
                        trainingStateConstantSet.add(item.training_state);
                        filteredTrainingStateConstants.push(item);
                    });
                    ctrl.trainingStateConstants = filteredTrainingStateConstants;
                }
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.filterCourseType = function () {
            ctrl.courseList = null;
            ctrl.courseType = ctrl.type == "ONLINE" ? 'ONLINE' : (ctrl.type == "OFFLINE" ? 'OFFLINE' : null);
            ctrl.getTrainingStateConstants();
            ctrl.getCourses();
        }

        ctrl.getCourses = function () {
            Mask.show();
            QueryDAO.execute({ 
                code: 'get_course_by_type',
                parameters: {
                    userId: ctrl.userDetail.id,
                    locationId: ctrl.selectedLocationId != undefined ? ctrl.selectedLocationId : null,
                    courseType: ctrl.courseType,
                }
             }).then(function (res) {
                ctrl.courseList = res.result;
            }).finally(function () {
                Mask.hide();
            });
        }


        ctrl.trainingScheduleList = [];

        ctrl.getTrainingStatuses = function () {
            AuthenticateService.getLoggedInUser().then(function (res) {
                ctrl.userDetail = res.data;
                switch (ctrl.statusOfTraining) {
                    case 'Scheduled':
                        ctrl.trainingState = 'DRAFT';
                        break;
                    case 'Pending Schedule':
                        ctrl.trainingState = 'NOT_DRAFT';
                        break;
                    case 'Update Pending':
                        ctrl.trainingState = 'DRAFT_SAVED';
                        break;
                    case 'Completed':
                        ctrl.trainingState = 'SUBMITTED';
                        break;
                    default:
                        ctrl.trainingState = null;
                }
                // console.log(ctrl.dateRange);
                // if(!(ctrl.dateRange === undefined)){
                    ctrl.dateFrom = ctrl.dateRange.from_date_range ? moment(ctrl.dateRange.from_date_range).format('DD/MM/YYYY') : '01/01/1970';
                    ctrl.dateTo = ctrl.dateRange.to_date_range == '01/01/1970' ? moment(ctrl.todayDate).format('DD/MM/YYYY') : moment(ctrl.dateRange.to_date_range).format('DD/MM/YYYY');
                // }
                Mask.show();
                var queryDto = {
                    code: "tr_scheduled_trainings",
                    parameters: {
                        userId: ctrl.userDetail.id,
                        locationId: ctrl.selectedLocationId != undefined ? ctrl.selectedLocationId : null,
                        // isShowPast: ctrl.isShowPast,
                        // moduleId: ctrl.courseModuleId != undefined && ctrl.courseModuleId != -1 ? ctrl.courseModuleId : null,
                        courseId: ctrl.courseId != undefined ? ctrl.courseId : null,
                        courseType: ctrl.courseType ? ctrl.courseType : null,
                        dateFrom: ctrl.dateFrom,
                        dateTo: ctrl.dateTo,
                        trainingState: ctrl.trainingState ? ctrl.trainingState : null,
                        limit: ctrl.pagingService.limit,
                        offSet: ctrl.pagingService.offSet
                    }
                }
                PagingForQueryBuilderService.getNextPage(QueryDAO.execute, queryDto, ctrl.trainingScheduleList, null).then((response) => {
                    ctrl.trainingScheduleList = response;
                    if (ctrl.courseType == 'ONLINE') {
                        ctrl.invisible = true;
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            });
        };

        ctrl.rescheduleTraining = function (training) {
            if (ctrl.getStatus(training) == 4 || ctrl.getStatus(training) == 6) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'app/training/trainingschedule/views/reschedule-training.modal.html',
                    controller: 'RescheduleModalController',
                    controllerAs: '$ctrl',
                    windowClass: 'cst-modal',
                    size: '',
                    resolve: {
                        trainingId: function () {
                            return training.trainingId;
                        }
                    }
                });
                modalInstance.result.then(function () {
                    ctrl.pagingService = PagingForQueryBuilderService.initialize();
                    ctrl.getTrainingStatuses();
                }, function () { });
            }
            // else if (ctrl.getStatus(training) == 2) {
            //     $state.go("techo.training.dashboardDetails", { trainingId: training.trainingId, trainingDate: training.effectiveDate, trainerId: training.trainerId });
            // }
            else if (ctrl.getStatus(training) == 3 || ctrl.getStatus(training) == 7) {
                $state.go("techo.training.editTraineeStatus", { trainingId: training.trainingId });
            }
        };

        ctrl.showHideFilter = function (value) {
            ctrl.scheduleFilter = {};
            ctrl.filterSchedules();
            if (ctrl.showFilter === value) {
                ctrl.showFilter = undefined;
                return;
            }
            ctrl.showFilter = value;
        };

        ctrl.orderByField = 'course';
        ctrl.reverseSort = false;
        ctrl.sortBy = function (propertyName) {
            ctrl.reverseSort = (ctrl.orderByField === propertyName) ? !ctrl.reverseSort : false;
            ctrl.orderByField = propertyName;
        };

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }

        ctrl.submit = function () {
            ctrl.pagingService = PagingForQueryBuilderService.initialize();
            ctrl.getTrainingStatuses();
            ctrl.toggleFilter();
        }

        // ctrl.getCoursesByModuleId = function (moduleId) {
        //     let moduleIds;
        //     if (moduleId == -1) {
        //         moduleIds = _.pluck(ctrl.moduleConstants, 'id')
        //     } else {
        //         moduleIds = [moduleId];
        //     }
        //     CourseService.getCoursesByModuleIds(moduleIds).then(function (res) {

        //         ctrl.courseList = res;
        //     })
        // }

        this.initPage = function () {
            ctrl.pagingService = PagingForQueryBuilderService.initialize();
            // ctrl.getTrainingStatuses();
            // ctrl.getModuleConstants();
        };

        ctrl.getStatus = function (training) {
            if (training.trainingState == 'SUBMITTED' && training.total != training.completed) {
                return 7;
            } else if (training.trainingState == 'SUBMITTED') {
                return 1;
            } else if (training.pending == 'PENDING' && training.expirationDate + 3 * 24 * 60 * 60 * 1000 >= todayInTime && training.expirationDate < todayInTime) {
                return 2;
            } else if (training.pending == 'SUBMITTED' && training.trainingState == 'DRAFT') {
                return 3;
            } else if (todayInTime >= training.effectiveDate && todayInTime <= training.expirationDate) {
                return 4;
            } else if (training.effectiveDate > todayInTime) {
                return 6;
            } else {
                if (training.courseType == 'ONLINE') {
                    return 2;
                } else {
                    if (ctrl.canMarkAttendance) {
                        return 2;
                    }
                    return 5;
                }
            }
        }

        ctrl.navigateToTraineeStatus = (training) => {
            $state.go('techo.training.traineeStatus', {
                queryParams: {
                    trainingId: training.trainingId,
                    courseType: training.courseType
                }
            });
        }

        ctrl.drillDown = (training) => {
            const reportJson = {
                id: 'user_training_sch',
                type: 'code',
                queryParams: {
                    training_id: training.trainingId
                }
            };
            const state = `techo.report.view/${angular.toJson(reportJson)}`
            if ($sessionStorage.asldkfjlj) {
                $sessionStorage.asldkfjlj[state] = true;
            } else {
                $sessionStorage.asldkfjlj = {};
                $sessionStorage.asldkfjlj[state] = true;
            }
            $sessionStorage.$apply()
            const url = $state.href('techo.report.view', reportJson, { inherit: false, absolute: false })
            window.open(url, '_blank');
        }

        ctrl.initPage();

        $timeout(function () {
            $(".header-fixed").tableHeadFixer();
        });
    }
    angular.module('imtecho.controllers').controller('ScheduledTrainingCtrl', ScheduledTrainingCtrl);
})();