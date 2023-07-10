(function () {
    function LocationController(Mask, LocationService, $timeout, $uibModal, AuthenticateService, $state) {
        var ctrl = this;
        ctrl.selectedLocation = null;
        ctrl.locationList = [];

        ctrl.submit = function () {
            if (ctrl.locationId != null) {
                Mask.show();
                LocationService.retrieveNextLevelOfGivenLocationId(ctrl.locationId).then(function (res) {
                    ctrl.locationList = res;
                    ctrl.toggleFilter();
                }).finally(function () {
                    $timeout(function () {
                        $(".header-fixed").tableHeadFixer();
                    });
                    Mask.hide();
                })
            }
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

        ctrl.editLocation = function (id, location) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/locations/views/edit-location.html',
                controller: 'EditLocationController',
                controllerAs: 'editlocationctrl',
                windowClass: 'cst-modal',
                size: 'lg',
                resolve: {
                    location: function () {
                        return location;
                    },
                    id: function () {
                        return id;
                    },
                    rights: function () {
                        return ctrl.rights;
                    }
                }
            });
            modalInstance.result.then(function () { }, function (e) {
                if (e == 'success') {
                    ctrl.fetch();
                }
            });

            ctrl.fetch = function () {
                if (ctrl.selectedLocation.finalSelected.optionSelected != null) {
                    Mask.show();
                    LocationService.retrieveNextLevelOfGivenLocationId(ctrl.selectedLocation.finalSelected.optionSelected.id).then(function (res) {
                        ctrl.locationList = res;
                    }).finally(function () {
                        $timeout(function () {
                            $(".header-fixed").tableHeadFixer();
                        });
                        Mask.hide();
                    })
                }
            }
        };

        AuthenticateService.getAssignedFeature($state.current.name).then(function (res) {
            ctrl.rights = res.featureJson;
            if (!ctrl.rights) {
                ctrl.rights = {};
            }
        });
    }
    angular.module('imtecho.controllers').controller('LocationController', LocationController);
})();
