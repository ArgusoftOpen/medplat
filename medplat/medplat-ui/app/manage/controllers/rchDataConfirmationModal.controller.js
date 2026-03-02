(function () {
    function RchDataConfirmationModal($uibModalInstance, PrevData) {
        var ctrl = this;
        ctrl.pending = _.where(PrevData, {
            state: "PENDING"
        });
        ctrl.lastSync = _.where(PrevData, {
            state: "EXECUTED"
        })

        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        }

        ctrl.submit = function () {
            $uibModalInstance.close(ctrl.pending);
        }
    }
    angular.module('imtecho.controllers').controller('RchDataConfirmationModal', RchDataConfirmationModal);
})();
