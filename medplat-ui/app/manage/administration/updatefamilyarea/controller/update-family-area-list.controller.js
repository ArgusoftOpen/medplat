(function () {
    function UpdateFamilyAreaList(QueryDAO, Mask, GeneralUtil, AuthenticateService, $uibModal) {
        let ctrl = this;
        ctrl.selectedLocationId = null;
        ctrl.searchLocationId = null;
        ctrl.families = [];

        ctrl.pagingService = {
            offset: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        let setOffsetLimit = function () {
            ctrl.pagingService.limit = 100;
            ctrl.pagingService.offset = ctrl.pagingService.index * 100;
            ctrl.pagingService.index = ctrl.pagingService.index + 1;
        };

        let _retrieveAll = function () {
            if (ctrl.selectedLocationId && !ctrl.pagingService.pagingRetrivalOn && !ctrl.pagingService.allRetrieved) {
                ctrl.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                var queryDto = {
                    code: 'retrieve_families_to_update_area',
                    parameters: {
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offset,
                        locationId: ctrl.selectedLocationId
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (response) {
                    if (response.result.length === 0 || response.result.length < ctrl.pagingService.limit) {
                        ctrl.pagingService.allRetrieved = true;
                        ctrl.families = ctrl.families.concat(response.result);
                    } else {
                        ctrl.pagingService.allRetrieved = false;
                        ctrl.families = ctrl.families.concat(response.result);
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

        ctrl.searchData = function (toReset, toToggle, isAssignedLocation) {
            ctrl.searchForm.$setSubmitted();
            if (ctrl.searchForm.$valid || (isAssignedLocation && ctrl.selectedLocationId)) {
                if (toReset) {
                    ctrl.pagingService.index = 0;
                    ctrl.pagingService.allRetrieved = false;
                    ctrl.pagingService.pagingRetrivalOn = false;
                    ctrl.families = [];
                }
                if (toToggle) {
                    ctrl.selectedLocationId = ctrl.searchLocationId
                    ctrl.toggleFilter();
                }
                _retrieveAll();
            }
        };


        ctrl.onUpdateFamilyArea = familyObj => {
            let modalInstance = $uibModal.open({
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                templateUrl: 'app/manage/administration/updatefamilyarea/views/update-family-area.modal.html',
                controllerAs: 'ctrl',
                controller: function ($uibModalInstance, family, loggedInUser) {
                    let modalCtrl = this;
                    modalCtrl.family = family;
                    modalCtrl.loggedInUser = loggedInUser;
                    modalCtrl.updateAreaId = null;

                    modalCtrl.locationSelectizeConfig = {
                        create: false,
                        valueField: 'id',
                        labelField: 'name',
                        dropdownParent: 'body',
                        highlight: true,
                        searchField: ['_searchField'],
                        maxItems: 1,
                        render: {
                            item: function (location, escape) {
                                var returnString = "<div>" + location.name + "</div>";
                                return returnString;
                            },
                            option: function (location, escape) {
                                var returnString = "<div>" + location.name + "</div>";
                                return returnString;
                            }
                        },
                        onFocus: function () {
                            this.onSearchChange("");
                        },
                        onBlur: function () {
                            var selectize = this;
                            var value = this.getValue();
                            setTimeout(function () {
                                if (!value) {
                                    selectize.clearOptions();
                                    selectize.refreshOptions();
                                }
                            }, 200);
                        },
                        load: function (query, callback) {
                            var selectize = this;
                            var value = this.getValue();
                            if (!value) {
                                selectize.clearOptions();
                                selectize.refreshOptions();
                            }
                            var promise;
                            var queryDto = {
                                code: 'retrieve_location_by_level_parent',
                                parameters: {
                                    parentId: modalCtrl.family.location_id,
                                    level: 8
                                }
                            };
                            promise = QueryDAO.execute(queryDto);
                            promise.then(function (res) {
                                angular.forEach(res.result, function (result) {
                                    result._searchField = query;
                                });
                                callback(res.result);
                            }, function () {
                                callback();
                            });
                        }
                    }

                    modalCtrl.submit = function () {
                        modalCtrl.updateFamilyAreaForm.$setSubmitted();
                        if (modalCtrl.updateFamilyAreaForm.$valid) {
                            let dtoList = [];
                            let updateFamilyAreaDto = {
                                code: 'update_family_area',
                                parameters: {
                                    id: modalCtrl.family.id,
                                    areaId: Number(modalCtrl.updateAreaId)
                                },
                                sequence: 1
                            }
                            dtoList.push(updateFamilyAreaDto);
                            Mask.show();
                            QueryDAO.executeAll(dtoList).then(function () {
                                $uibModalInstance.close();
                                toaster.pop('success', 'Family area updated successfully.');
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
                        }
                    }

                    modalCtrl.cancel = function () {
                        $uibModalInstance.dismiss();
                    }
                },
                resolve: {
                    family: function () {
                        return familyObj
                    },
                    loggedInUser: function () {
                        return ctrl.loggedInUser
                    }
                }
            });
            modalInstance.result.then(() => {
                ctrl.searchData(true, false, true);
            }, () => { })
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
            Mask.show();
            AuthenticateService.getLoggedInUser().then(user => {
                ctrl.loggedInUser = user.data;
                if (ctrl.loggedInUser.minLocationId) {
                    ctrl.selectedLocationId = ctrl.loggedInUser.minLocationId;
                    _retrieveAll();
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
        }

        _init();
    }
    angular.module('imtecho.controllers').controller('UpdateFamilyAreaList', UpdateFamilyAreaList);
})();
