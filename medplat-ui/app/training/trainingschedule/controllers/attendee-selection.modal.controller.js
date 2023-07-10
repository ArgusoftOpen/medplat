(function () {
    var AttendeeModalController = function ($uibModalInstance, Attendees, Unchecked, SelectedAll) {
        var ctrl = this;
        ctrl.attendees = [];
        ctrl.excluded = Unchecked;
        ctrl.selectedAll = SelectedAll;
        if (Attendees) {
            ctrl.attendees = Attendees;
        }

        ctrl.isUnchecked = function (id) {
            if (ctrl.excluded.includes(id)) {
                return false;
            }
            return true;
        }

        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        }

        ctrl.check = function (flag, id) {
            if (flag) {
                var index = ctrl.excluded.indexOf(id);
                if (index >= 0) {
                    ctrl.excluded.splice(index, 1);
                }
            } else {
                ctrl.excluded.push(id);
            }
            if(ctrl.excluded.length > 0){
                ctrl.selectedAll = false;
            }
            else{
                ctrl.selectedAll = true;
            }
        }

        ctrl.checkAll = function() {
            if (ctrl.selectedAll) {
                angular.forEach(this.attendees, function(attendee){
                    ctrl.check(true,attendee.id)
                });
            } else {
                angular.forEach(this.attendees, function(attendee){
                    ctrl.check(false,attendee.id)
                });
            }
        }

        ctrl.submit = function () {
            $uibModalInstance.close(ctrl.excluded);
        }

    };
    angular.module('imtecho.controllers').controller('AttendeeModalController', AttendeeModalController);
})();
