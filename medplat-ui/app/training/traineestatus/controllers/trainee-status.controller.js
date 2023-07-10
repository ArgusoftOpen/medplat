(function () {
    function TraineeStatusCtrl(Mask, QueryDAO, $state, GeneralUtil) {
        var ctrl = this;

        ctrl.offlineTabSelected = () => {
            ctrl.courseType = 'OFFLINE';
            ctrl.retrievePendingTrainingStatus();
        }

        ctrl.onlineTabSelected = () => {
            ctrl.courseType = 'ONLINE';
            ctrl.retrievePendingTrainingStatus();
        }

        ctrl.retrievePendingTrainingStatus = () => {
            Mask.show();
            QueryDAO.execute({
                code: "training_dashboard_pending_list",
                parameters: {
                    locationId: ctrl.selectedLocationId,
                    courseType: ctrl.courseType,
                    trainingId: ctrl.trainingId || null
                }
            }).then((response) => {
                ctrl.pendingTrainingStatus = response.result;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

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
            ctrl.selectedTab === 'offline-tab' ? ctrl.offlineTabSelected() : ctrl.onlineTabSelected();
            ctrl.toggleFilter();
        }

        ctrl.sort = function (field) {
            if (field === ctrl.field)
                ctrl.reverse = !ctrl.reverse;
            else {
                ctrl.reverse = false;
                ctrl.field = field;
            }
        }

        ctrl.drillDown = (code, course) => {
            $state.current.backTab = ctrl.selectedTab;
            $state.current.selectedLocationId = ctrl.selectedLocationId;
            $state.go('techo.report.view', {
                id: code, type: 'code', queryParams: {
                    role_id: course.roleId,
                    course_id: course.id,
                    training_id: ctrl.trainingId || null,
                    location_id: `${Array.isArray(ctrl.selectedLocationId) && ctrl.selectedLocationId.length ? "'" + ctrl.selectedLocationId.join() + "'" : ctrl.selectedLocationId}`
                }
            });
        }

        ctrl.initPage = function () {
            if ($state.params.queryParams) {
                ctrl.trainingId = $state.params.queryParams.trainingId;
                ctrl.selectedTab = $state.params.queryParams.courseType === 'ONLINE' ? 'online-tab' : 'offline-tab';
            } else {
                ctrl.selectedTab = $state.current.backTab || 'offline-tab';
            }
            ctrl.selectedLocationId = $state.current.selectedLocationId || null;
        };

        ctrl.initPage();
    }
    angular.module('imtecho.controllers').controller('TraineeStatusCtrl', TraineeStatusCtrl);
})();
