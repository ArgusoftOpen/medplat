(function () {
    function SchoolController(QueryDAO, Mask, GeneralUtil, $state) {
        let ctrl = this;
        ctrl.searchLocationId = null;

        ctrl.pagingService = {
            offset: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        if ($state.current.searchLocationId != null) {
            ctrl.searchLocationId = $state.current.searchLocationId;
        }

        let retrieveAll = function () {
            if (!ctrl.pagingService.pagingRetrivalOn && !ctrl.pagingService.allRetrieved) {
                ctrl.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'school_retrieval',
                    parameters: {
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offset,
                        locationId: ctrl.searchLocationId || null
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (response) {
                    if (response.result.length === 0) {
                        ctrl.pagingService.allRetrieved = true;
                        if (ctrl.pagingService.index === 1) {
                            ctrl.schools = response.result;
                        }
                    } else {
                        ctrl.pagingService.allRetrieved = false;
                        if (ctrl.pagingService.index > 1) {
                            ctrl.schools = ctrl.schools.concat(response.result);
                        } else {
                            ctrl.schools = response.result;
                        }
                    }
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    ctrl.pagingService.allRetrieved = true;
                }).finally(function () {
                    ctrl.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        let setOffsetLimit = function () {
            ctrl.pagingService.limit = 100;
            ctrl.pagingService.offset = ctrl.pagingService.index * 100;
            ctrl.pagingService.index = ctrl.pagingService.index + 1;
        };

        ctrl.searchData = function (reset) {
            if (reset) {
                ctrl.searchForm.$setSubmitted();
                if (!ctrl.searchForm.$valid) {
                    return;
                }
            }
            if (reset) {
                ctrl.pagingService.index = 0;
                ctrl.pagingService.allRetrieved = false;
                ctrl.pagingService.pagingRetrivalOn = false;
                ctrl.toggleFilter();
            }
            retrieveAll();
        };

        ctrl.navigateToEdit = function (id) {
            if (ctrl.searchLocationId != null) {
                $state.current.searchLocationId = ctrl.searchLocationId;
            } else {
                $state.current.searchLocationId = null;
            }
            $state.go("techo.manage.school", { id: id });
        }

        ctrl.navigateToAdd = function () {
            if (ctrl.searchLocationId != null) {
                $state.current.searchLocationId = ctrl.searchLocationId;
            } else {
                $state.current.searchLocationId = null;
            }
            $state.go("techo.manage.school");
        }

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }

            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.close = function () {
            ctrl.searchForm.$setPristine();
            ctrl.toggleFilter();
        };

        let _init = function () {
            retrieveAll();
        }

        _init();
    }
    angular.module('imtecho.controllers').controller('SchoolController', SchoolController);
})();
