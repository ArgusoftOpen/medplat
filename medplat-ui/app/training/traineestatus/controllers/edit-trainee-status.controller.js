(function () {
    function EditTraineeStatusCtrl($state, TrainingService, $stateParams, toaster, $timeout, GeneralUtil, Mask) {
        var ctrl = this;
        ctrl.isDisable = false;
        ctrl.trainingId = $stateParams.trainingId;
        ctrl.trainingStatusList = [];

        var init = function () {
            Mask.show();
            TrainingService.getTrainingStatusesByTrainingId(ctrl.trainingId).then(function (res) {
                if (res === undefined || res.length < 1) {
                    toaster.pop('warning', 'Trainee list for this TrainingId is empty');
                } else {
                    ctrl.trainingStatusList = res[0];
                }
            }, function (error) { }).finally(function () {
                Mask.hide();
            });
        };

        ctrl.saveTraineeStatus = function (participantList) {
            var certificates = [];
            if (!!participantList) {
                for (var i = 0; i < participantList.length; i++) {
                    certificates.push({
                        certificateId: participantList[i].certificateId,
                        userId: participantList[i].userId,
                        trained: participantList[i].trained,
                        remark: participantList[i].remark
                    });
                }
            }
            var trainingStatus = {
                traineeCertificates: certificates,
                trainingId: ctrl.trainingId,
                isSubmit: false
            };
            TrainingService.updateTraineeStatus(trainingStatus).then(function (res) {
                toaster.pop('success', 'Trainee Status Updated for this Training!');
                $state.go("techo.training.scheduled");
            });
        };

        ctrl.submitTraineeStatus = function (participantList) {
            ctrl.isDisable = true;
            var certificates = [];
            if (!!participantList) {
                for (var i = 0; i < participantList.length; i++) {
                    if (participantList[i].certificateId == null || (participantList[i].certificateId != null && participantList[i].certificateType != 'COURSECOMPLETION') && participantList[i].trained) {
                        certificates.push({
                            certificateId: participantList[i].certificateId,
                            userId: participantList[i].userId,
                            trained: participantList[i].trained,
                            remark: participantList[i].remark
                        });
                    }
                }
            }
            var trainingStatus = {
                traineeCertificates: certificates,
                trainingId: ctrl.trainingId,
                isSubmit: true
            };
            Mask.show();
            TrainingService.updateTraineeStatus(trainingStatus).then(function (res) {
                toaster.pop('success', 'Trainee Status Submitted for this Training!');
                $state.go("techo.training.scheduled");
                ctrl.isDisable = false;
            }).catch(function (err) {
                GeneralUtil.showMessageOnApiCallFailure(err);
            }).finally(function () {
                Mask.hide();
            });
        };

        init();

        $timeout(function () {
            $(".header-fixed").tableHeadFixer();
        });
    }
    angular.module('imtecho.controllers').controller('EditTraineeStatusCtrl', EditTraineeStatusCtrl);
})();
