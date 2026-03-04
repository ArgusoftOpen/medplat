(function (angular) {
    var RescheduleModalController = function ($uibModalInstance, $filter, trainingId, toaster, GeneralUtil, Mask, CommonService, TrainingService, TopicCoverageService) {
        var $ctrl = this;
        $ctrl.rescheduleTrainingId = angular.copy(trainingId);
        var previousDay = new Date();
        previousDay.setDate(previousDay.getDate() - 1);
        $ctrl.now = new Date();
        $ctrl.todayFormatted = $filter('date')($ctrl.now, 'yyyy-MM-dd');
        $ctrl.text = false;
        $ctrl.selectedTrainingSchedule = [];

        TopicCoverageService.getTopicCoverageByTrainingId(trainingId).then(function (response) {
            var topics = response;

            var topicsByDate = CommonService.groupTopicsByDate(topics);

            angular.forEach(topicsByDate, function (training) {
                training.formattedDate = $filter('date')(training.date, 'yyyy-MM-dd');
            });
            $ctrl.selectedTrainingSchedule = topicsByDate;
        });

        $ctrl.setDate = function (index) {
            if (index === 0) {
                return $ctrl.todayFormatted;
            } else {
                var day = new Date($ctrl.selectedTrainingSchedule[index - 1].formattedDate).getTime() + (24 * 60 * 60 * 1000);
                return $filter('date')(new Date(day), 'yyyy-MM-dd') > $ctrl.todayFormatted ? $filter('date')(new Date(day), 'yyyy-MM-dd') : $ctrl.todayFormatted;
            }
        };

        $ctrl.checkForPrevious = function (oldDate) {
            return $filter('date')(oldDate, 'yyyy-MM-dd') >= $ctrl.todayFormatted;
        };

        $ctrl.textReschedule = function (last, newFormattedDate) {
            $ctrl.dateNew = newFormattedDate;
            if (last === true)
                $ctrl.text = false;
            else
                $ctrl.text = true;
        };

        $ctrl.ok = function (oldDate, newDate, index) {
            var trainingId = $ctrl.rescheduleTrainingId;
            var oldDateTimestamp = new Date(oldDate).getTime();
            var newDateTimestamp = new Date(newDate).getTime();
            var isFirst = false;
            if (index === 0) {
                isFirst = true;
            }
            Mask.show();
            TrainingService.rescheduleTraining(trainingId, oldDateTimestamp, newDateTimestamp, isFirst)
                .then(function (res) {
                    toaster.pop('success', 'Training Rescheduled successfully!');
                }, function (error) {
                    GeneralUtil.showMessageOnApiCallFailure(error.data.data, error.data.data[0].message);
                }).finally(function () {
                    Mask.hide();
                    $uibModalInstance.close();
                });
        };

        $ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('RescheduleModalController', RescheduleModalController);
})(window.angular);
