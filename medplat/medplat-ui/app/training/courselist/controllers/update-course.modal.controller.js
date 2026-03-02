(function (angular) {
    var UpdateModalController = function ($uibModalInstance, topics, toaster) {
        let $ctrl = this;
        $ctrl.topicForUpdate = {};
        $ctrl.topicsList = topics[0];
        if (topics[1] === true) {
            $ctrl.addFlag = topics[1];
        } else {
            $ctrl.topicForUpdate = angular.copy(topics[1]);
        }

        $ctrl.ok = () => {
            $ctrl.topicUpdateForm.$setSubmitted();
            if ($ctrl.topicUpdateForm.$valid) {
                // if ($ctrl.topicForUpdate.topicDescription === null || $ctrl.topicForUpdate.topicDescription === '') {
                //     $ctrl.topicForUpdate.topicDescription = $ctrl.topicForUpdate.topicName;
                // }
                if (Array.isArray($ctrl.topicsList) && $ctrl.topicsList.length) {
                    let index = $ctrl.topicsList.findIndex((topic) => {
                        if ($ctrl.addFlag) {
                            return Number($ctrl.topicForUpdate.topicOrder) === Number(topic.topicOrder) && Number($ctrl.topicForUpdate.topicDay) === Number(topic.topicDay);
                        } else {
                            return $ctrl.topicForUpdate.topicId !== topic.topicId && Number($ctrl.topicForUpdate.topicOrder) === Number(topic.topicOrder) && Number($ctrl.topicForUpdate.topicDay) === Number(topic.topicDay);
                        }
                    });
                    if (index === -1) {
                        if ($ctrl.addFlag) {
                            $ctrl.topicsList.push($ctrl.topicForUpdate);
                            toaster.pop('success', 'Topic added successfully');
                            $uibModalInstance.close($ctrl.topicsList);
                        } else {
                            $uibModalInstance.close($ctrl.topicForUpdate);
                        }
                    } else {
                        toaster.pop('error', 'Topic with existing order and day cannot be added. Change order or day of topic');
                    }
                } else {
                    $ctrl.topicsList.push($ctrl.topicForUpdate);
                    toaster.pop('success', 'Topic added successfully');
                    $uibModalInstance.close($ctrl.topicsList);
                }
            }
        };

        $ctrl.cancel = () => {
            $uibModalInstance.dismiss('cancel');
        };
    };
    angular.module('imtecho.controllers').controller('UpdateModalController', UpdateModalController);
})(window.angular);
