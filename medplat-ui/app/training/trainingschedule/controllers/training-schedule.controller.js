(function () {
    function TrainingScheduleCtrl(RoleDAO, CourseService, toaster, $filter, TrainingService, GeneralUtil, Mask, $state, QueryDAO, $uibModal) {
        var ctrl = this;
        ctrl.options = {
            minDate: new Date(),
            showWeeks: true
        };
        var previousDay = new Date();
        ctrl.todayFormatted = $filter('date')(previousDay, 'yyyy-MM-dd');
        ctrl.isGetTrainees = false;
        ctrl.trainingObj = {};
        ctrl.additionalParticipantSelected = [];
        ctrl.trainingObj = { date: { startDate: null, endDate: null }, noOfAttendees: 0, district: null, role: null, block: null };
        ctrl.locationList = [];
        ctrl.organizationUnits = [];
        ctrl.eligibleAttendees = [];
        ctrl.excludedAttendees = [];
        ctrl.trainingObj.noOfAttendees = 0;
        ctrl.noOfAttendeesCompleted = 0;
        ctrl.parentLocations = {};
        ctrl.isDisable = false;
        ctrl.tempIds = [];
        ctrl.trainingObj.typeOfCourse = "Online";

        ctrl.disabled = function (data, mode) {
            let date = data;
            return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
        };

        ctrl.retrieveRoleList = function () {
            Mask.show();
            RoleDAO.retireveAll(true).then(function (res) {
                ctrl.trainerRoleList = res;
                ctrl.excludedRoleList = res;
            }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.getCompletionDate = function (startDate, courseId) {
            if (!!startDate && !!courseId) {
                ctrl.trainingObj.date.endDate = '';
                var date = (new Date(startDate)).getTime();
                CourseService.getCourseCompletionDate(date, courseId).then(function (response) {
                    ctrl.trainingObj.date.endDate = response.result;
                }, function () { });
            }
        };

        ctrl.getEligibleAttendees = function (role, course, flag) {
            if (flag) {
                ctrl.trainingObj.noOfAttendees = 0;
                ctrl.noOfAttendeesCompleted = 0;
                ctrl.eligibleAttendees = [];
                ctrl.totalAttendeesList = [];
            }
            if (!flag && ctrl.eligibleAttendees.length > 0) {
                ctrl.selectAttendees();
            }
            var count = 0;
            angular.forEach(course, function (c) {
                if (role && c && ctrl.organizationUnits && ctrl.eligibleAttendees.length == 0) {
                    var roleIds = _.pluck(role, 'id');
                    Mask.show();
                    var queryDto = {
                        code: "training_eligible_count",
                        parameters: {
                            roleId: roleIds.length > 0 ? roleIds.map(x => +x) : null,
                            locationId: ctrl.organizationUnits.length > 0 ? ctrl.organizationUnits.map(x => +x) : null,
                            courseId: Number(c.courseId)
                        }
                    }
                    QueryDAO.execute(queryDto).then(function (res) {
                        if (ctrl.eligibleAttendees.length == 0 && count == 0) {
                            count = count + 1;
                            ctrl.eligibleAttendees = _.where(res.result, { type: 'pending' });
                            ctrl.trainingObj.noOfAttendees = res.result.length;
                            ctrl.totalAttendeesList = res.result;
                            ctrl.noOfAttendeesCompleted = _.where(res.result, { type: 'completed' }).length;
                        }
                        else {
                            ctrl.tempEligibleAttendees = _.where(res.result, { type: 'pending' });
                            ctrl.eligibleAttendees = ctrl.eligibleAttendees.filter(item1 => ctrl.tempEligibleAttendees.some(item2 => item1.id === item2.id))
                            ctrl.tempTotalAttendeesList = res.result;
                            ctrl.totalAttendeesList = Object.values(ctrl.tempTotalAttendeesList.concat(ctrl.totalAttendeesList).reduce((r, o) => {
                                r[o.id] = o;
                                return r;
                            }, {}));
                            //ctrl.totalAttendeesList = ctrl.totalAttendeesList.filter(item1 => ctrl.tempTotalAttendeesList.some(item2 => item1.id === item2.id))
                            ctrl.trainingObj.noOfAttendees = ctrl.totalAttendeesList.length;
                            ctrl.noOfAttendeesCompleted = ctrl.totalAttendeesList.length - ctrl.eligibleAttendees.length;
                        }
                    }).finally(function () {
                        Mask.hide();
                    })
                }
            })
        };

        ctrl.scheduleTraining = function (form) {
            form.$setSubmitted();
            ctrl.formSubmitted = true;
            let trainingSchedules = [];
            if (form.$valid) {
                angular.forEach(ctrl.trainingObj.course, function (course) {
                    var trainingSchedule = {};
                    if (ctrl.trainingObj.noOfAttendees - ctrl.noOfAttendeesCompleted - ctrl.excludedAttendees.length <= 0) {
                        toaster.pop('warning', 'No Attendees selected for training');
                        return;
                    }
                    if (ctrl.eligibleAttendees) {
                        trainingSchedule.attendees = _.reject(_.pluck(_.where(ctrl.eligibleAttendees, { type: 'pending' }), 'id'), function (id) {
                            return ctrl.excludedAttendees.includes(id);
                        })
                    }
                    trainingSchedule.primaryTargetRole = []
                    angular.forEach(ctrl.trainingObj.targetRoles, function (val) {
                        trainingSchedule.primaryTargetRole.push(val.id);
                    })
                    trainingSchedule.effectiveDate = new Date(ctrl.trainingObj.date.startDate).getTime();
                    trainingSchedule.expirationDate = ctrl.trainingObj.date.endDate;
                    trainingSchedule.organizationUnits = ctrl.organizationUnits;
                    trainingSchedule.courses = [];
                    trainingSchedule.courses.push(course.courseId);
                    if (course.courseType !== 'ONLINE') {
                        trainingSchedule.primaryTrainers = [ctrl.trainingObj.bigot];
                    } else {
                        trainingSchedule.primaryTrainers = [];
                    }
                    trainingSchedule.optionalTrainers = ctrl.trainingObj.addBinot;
                    trainingSchedule.location = ctrl.trainingObj.location;
                    trainingSchedule.additionalAttendees = [];
                    trainingSchedule.excludedAttendees = ctrl.excludedAttendees;
                    angular.forEach(ctrl.additionalParticipantSelected, function (selected) {
                        trainingSchedule.additionalAttendees.push(selected.id);
                    });
                    trainingSchedule.courseType = course.courseType || 'OFFLINE';
                    trainingSchedules.push(trainingSchedule);
                });
                Mask.show();
                ctrl.isDisable = true;
                TrainingService.createTraining(trainingSchedules).then(function (res) {
                    toaster.pop('success', 'Training schedule saved!');
                    ctrl.resetScheduleForm(form);
                    $state.go('techo.training.scheduled');
                }, function (error) {
                    GeneralUtil.showMessageOnApiCallFailure(error.data.data, error.data.data[0].message);
                }).finally(function () {
                    Mask.hide();
                    ctrl.isDisable = false;
                });
            }
        }

        ctrl.resetScheduleForm = function (form) {
            ctrl.formSubmitted = false;
            form.$setPristine(true);
            ctrl.trainingObj = {};
            ctrl.trainingObj = { date: { startDate: null, endDate: null }, noOfAttendees: 0, district: null, role: null, block: null };
            ctrl.completionDate = '';
            ctrl.additionalParticipantSelected = [];
            ctrl.allLocExceptCurrent = [];
            ctrl.selectedPhc = null;
            ctrl.ashasByPHC = [];
            ctrl.addBinotList = [];
            ctrl.noOfAttendeesCompleted = 0;
            ctrl.trainerRole = null;
            ctrl.parentLocations = {};
        };

        ctrl.onRoleChange = function (role) {
            var roleIds = [];
            angular.forEach(role, function (val) {
                roleIds.push(val.id);
            })
            if (!!ctrl.trainingObj.course && !!ctrl.trainingObj.role) {
                ctrl.getEligibleAttendees(roleIds, ctrl.trainingObj.course, true);
                if (ctrl.trainingObj.course[0].courseType !== 'ONLINE') {
                    this.getCompletionDate(ctrl.trainingObj.date.startDate, ctrl.trainingObj.course[0].courseId)
                }
            }
            if (!!ctrl.selectedPhc && !!ctrl.trainingObj.course && !!ctrl.trainingObj.role) {
                ctrl.additionalParticipantSelected = [];
                ctrl.getRoleByPhc(ctrl.selectedPhc, ctrl.trainingObj.course[0].courseId, ctrl.trainingObj.role.id);
            }
            if (!!ctrl.trainingObj.role) {
                var list = angular.copy(ctrl.roleList);
                var filteredList = [];
                angular.forEach(list, function (res) {
                    if (res.id && res.id !== role.id) {
                        filteredList.push(res);
                    }
                });
                ctrl.trainerRoleList = angular.copy(filteredList);
            } else {
                ctrl.trainerRoleList = angular.copy(ctrl.roleList);
            }
        };

        ctrl.addAdditionalParticipant = function (selected) {
            angular.forEach(selected, function (val) {
                if (!_.where(ctrl.additionalParticipantSelected, { id: val.id }).length > 0) {
                    val.phc = ctrl.selectedPhc.name;
                    ctrl.additionalParticipantSelected.push(val);
                }
            })
        };

        ctrl.removeAdditionalParticipant = function (item) {
            angular.forEach(ctrl.additionalParticipantSelected, function (value, index) {
                if (value.id === item.id) {
                    ctrl.additionalParticipantSelected.splice(index, 1);
                }
            });
            angular.forEach(ctrl.addPatricipant, function (participant, index) {
                if (participant.id === item.id) {
                    ctrl.addPatricipant.splice(index, 1);
                }
            });
        };

        ctrl.getRoleByPhc = function (phc, course, role) {
            ctrl.ashasByPHC = [];
            angular.forEach(role, function (val) {
                TrainingService.getAshasByPhc(phc, course, val.id).then(function (res) {
                    ctrl.ashasByPHC = ctrl.ashasByPHC.concat(res);
                });
            })

        };

        ctrl.getBigotsList = function () {
            ctrl.bigotList = [];
            if (ctrl.organizationUnits.length != 0 && ctrl.trainerRole) {
                angular.forEach(ctrl.organizationUnits, function (val) {
                    TrainingService.getTrainerByLocation(val, ctrl.trainerRole.id).then(function (res) {
                        ctrl.bigotList = _.uniq(_.union(ctrl.bigotList, res), false, function (item, key, id) { return item.id; });
                    });
                });
            }
        };

        ctrl.primaryBinotSelected = function (bigot) {
            var list = angular.copy(ctrl.bigotList);
            var filteredList = [];
            var addBinotSelectedList = angular.copy(ctrl.trainingObj.addBinot);
            if (bigot !== null && bigot !== "") {
                angular.forEach(list, function (res) {
                    if (res.id && res.id !== bigot) {
                        filteredList.push(res);
                    }
                });
                angular.forEach(addBinotSelectedList, function (res) {
                    if (res && res === bigot) {
                        var index = addBinotSelectedList.indexOf(res);
                        addBinotSelectedList.splice(index, 1);
                    }
                });
            }
            ctrl.addBinotList = angular.copy(filteredList);
            ctrl.trainingObj.addBinot = addBinotSelectedList;
        };

        ctrl.allLocExceptCurrent = [];

        ctrl.changeFunction = function () {
            if (ctrl.selectedlocation) {
                if (ctrl.selectedlocation.finalSelected.optionSelected) {
                    ctrl.locationLabel = ctrl.selectedlocation.finalSelected.locationLabel;
                    ctrl.locationId = ctrl.selectedlocation.finalSelected.optionSelected.id;
                    angular.copy(ctrl.selectedlocation.finalSelected.locationDetails, ctrl.allLocExceptCurrent);
                } else if (ctrl.selectedlocation.finalSelected.optionSelected === null) {
                    ctrl.locationLabel = ctrl.selectedlocation.finalSelected.previousLevelLabel;
                    var str1 = "level";
                    var res = str1.concat(ctrl.selectedlocation.finalSelected.level - 1);
                    ctrl.locationId = ctrl.selectedlocation[res].id;
                    angular.copy(ctrl.selectedlocation.finalSelected.previousLevelOptions, ctrl.allLocExceptCurrent);
                }
            }
            this.getBigotsList();
            ctrl.getEligibleAttendees(ctrl.trainingObj.targetRoles, ctrl.trainingObj.course);
        };

        ctrl.addLocation = function () {
            var maxLevel = ctrl.selectedlocation.finalSelected.level;
            var level = 1;
            var parent = [];
            while (true) {
                if (ctrl.selectedlocation['level' + level])
                    break;
                else
                    level++;
            }
            var locationString = '';
            var lastlocation = null;
            while (level <= maxLevel) {
                if (!ctrl.selectedlocation['level' + level])
                    break;
                if (ctrl.organizationUnits.includes(ctrl.selectedlocation['level' + level].id)) {
                    toaster.pop('warning', 'Location already included');
                    return;
                }
                locationString += ctrl.selectedlocation['level' + level].name;
                lastlocation = ctrl.selectedlocation['level' + level].id;
                parent.push(lastlocation);
                locationString += '>';
                level++;
            }
            if (lastlocation != null && ctrl.locationList.indexOf(locationString.substring(0, locationString.length - 1)) < 0) {
                parent.splice(parent.indexOf(lastlocation), 1);
                if (ctrl.checkParent(lastlocation)) {
                    toaster.pop('warning', 'Child Location already included');
                    return;
                }
                ctrl.parentLocations[lastlocation] = parent;
                ctrl.locationList.push(locationString.substring(0, locationString.length - 1));
                ctrl.organizationUnits.push(lastlocation);
                ctrl.trainingObj.targetRoles = null;
                ctrl.trainingObj.course = null;
                ctrl.filteredLocations = ctrl.allLocExceptCurrent.filter(location => !ctrl.organizationUnits.includes(location.id));
                ctrl.selectedPhc = null;
            }
        }

        ctrl.removeSelectedArea = function (index) {
            var key = ctrl.organizationUnits[index];
            delete ctrl.parentLocations[key];
            ctrl.locationList.splice(index, 1);
            ctrl.organizationUnits.splice(index, 1);
            ctrl.trainingObj.targetRoles = null;
            ctrl.trainingObj.course = null;
            ctrl.trainingObj.noOfAttendees = 0;
            ctrl.noOfAttendeesCompleted = 0;
            ctrl.eligibleAttendees = [];
            ctrl.excludedAttendees = [];
            ctrl.filteredLocations = ctrl.allLocExceptCurrent.filter(location => !ctrl.organizationUnits.includes(location.id));
            ctrl.selectedPhc = null;
        }

        ctrl.selectAttendees = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/training/trainingschedule/views/attendee-selection.modal.html',
                controller: 'AttendeeModalController',
                controllerAs: 'attendeeModal',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    Attendees: function () {
                        return ctrl.eligibleAttendees;
                    },
                    Unchecked: function () {
                        return ctrl.excludedAttendees;
                    },
                    SelectedAll: function () {
                        if (ctrl.excludedAttendees.length == 0) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
            });
            modalInstance.result.then(function (res) {
                ctrl.excludedAttendees = res;
            }, function () { });
        }

        ctrl.checkParent = function (lastlocation) {
            var flag = false;
            angular.forEach(ctrl.parentLocations, function (val, key) {
                if (!flag) {
                    if (val.includes(lastlocation)) {
                        flag = true;
                    }
                }
            })
            return flag;
        }

        ctrl.retrieveAllCourses = function () {
            Mask.show();
            CourseService.retrieveAllCourse(true).then(function (res) {
                ctrl.courseList = res;
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.retrieveAllRoles = function () {
            Mask.show();
            CourseService.retrieveAllRoles().then(function (res) {
                ctrl.roleList = res;
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.getRolesByCourse = function () {
            if (ctrl.trainingObj.course) {
                Mask.show();
                ctrl.eligibleAttendees = [];
                ctrl.excludedAttendees = [];
                ctrl.trainingObj.noOfAttendees = 0;
                ctrl.noOfAttendeesCompleted = 0;
                CourseService.getRolesByCourses(ctrl.trainingObj.course.courseId).then(function (res) {
                    ctrl.roleList = res;
                }).finally(function () {
                    Mask.hide();
                });
            }
        }

        ctrl.getCoursesByRole = function () {
            if (ctrl.trainingObj.targetRoles) {
                ctrl.tempIds = [];
                angular.forEach(ctrl.trainingObj.targetRoles, function (role) {
                    ctrl.tempIds.push(role.id);
                });
                Mask.show();
                ctrl.eligibleAttendees = [];
                ctrl.excludedAttendees = [];
                ctrl.trainingObj.noOfAttendees = 0;
                ctrl.noOfAttendeesCompleted = 0;
                CourseService.getCoursesByRoles(ctrl.tempIds).then(function (res) {
                    ctrl.courseList = res;
                    ctrl.filteredCourses = res;
                    ctrl.filterCourse();
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.filterCourse = function () {
            ctrl.filteredCourses = [];
            if (ctrl.trainingObj.typeOfCourse === 'Online') {
                angular.forEach(ctrl.courseList, function (course) {
                    if (course.courseType === 'ONLINE') {
                        ctrl.filteredCourses.push(course);
                    }
                });
            }
            else if (ctrl.trainingObj.typeOfCourse === 'Offline') {
                angular.forEach(ctrl.courseList, function (course) {
                    if (course.courseType === 'OFFLINE') {
                        ctrl.filteredCourses.push(course);
                    }
                });
            }
            else {
                ctrl.filteredCourses = ctrl.courseList;
            }
        }

        ctrl.onCourseChange = function () {
            if (ctrl.trainingObj.course) {
                if (ctrl.trainingObj.course[0].courseType === 'OFFLINE') {
                    var temp = ctrl.trainingObj.course[0];
                    ctrl.filteredCourses = [];
                    ctrl.filteredCourses.push(temp);
                }
                else {
                    ctrl.filteredCourses = [];
                    angular.forEach(ctrl.courseList, function (course) {
                        if (course.courseType === 'ONLINE') {
                            ctrl.filteredCourses.push(course);
                        }
                    });
                }
            }
            else {
                ctrl.filterCourse();
            }
        }

        ctrl.getTrainerRolesByCourse = function () {
            if (ctrl.trainingObj.course) {
                Mask.show();
                CourseService.getTrainerRolesByCourses(ctrl.trainingObj.course[0].courseId).then(function (res) {
                    if (res.length == 0) {
                        ctrl.retrieveRoleList();
                    } else {
                        ctrl.trainerRoleList = res;
                    }
                    Mask.hide();
                })
            }
        }

        ctrl.getTotalCandidatesList = () => {
            $("#totalAttendeesList").modal({ backdrop: 'static', keyboard: false });
        }

        ctrl.closeModal = () => {
            $("#totalAttendeesList").modal('hide');
        }

        this.initPage = function () {
            ctrl.retrieveAllRoles();
            ctrl.retrieveRoleList();
        };

        ctrl.initPage();
    }
    angular.module('imtecho.controllers').controller('TrainingScheduleCtrl', TrainingScheduleCtrl);
})();