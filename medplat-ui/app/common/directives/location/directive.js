/**
 *
 * @author Vaishali
 *
 * Location Directive
 *
 * E.g.
 *
 * Attributes:
 * selectedLocation: object in which you want the selection
 * templateType: type of template--> inline/ twoPart are currently supported(type:string)
 * fetchAccordingToUserAoi: for fetching location according to logged in user aoi
 * requiredUptoLevel: for setting mandatory upto which level
 * fetchUptoLevel: for fetching till which level
 *
 */

(function () {
    var locationDirective = function (LocationService, GeneralUtil, AuthenticateService, toaster, Mask) {
        return {
            restrict: 'E',
            scope: {
                selectedLocation: '=?',
                templateType: '<',
                fetchAccordingToUserAoi: '<',
                demographicFilterRequired: '<',
                requiredUptoLevel: '<',
                fetchUptoLevel: '<',
                multipalOnLevel: '<',
                onChange: '&',
                ngModel: '=?',
                selectedDemographic: '=?',
                allDistrictReq: '=?',
                passedLocationLevel: '=?',
                disabled: '=?',
                allBlockReq: '=?',
            },
            templateUrl: 'app/common/directives/location/location-template.html',
            link: function ($scope, element, attrs) {
                if ($scope.multipalOnLevel) {
                    // if ($scope.fetchUptoLevel > $scope.multipalOnLevel) {
                    //     $scope.fetchUptoLevel = $scope.multipalOnLevel;
                    // }
                }
                $scope.form = {};
                const allRegion = { id: -1, locType: "Region", name: "All District", type: "R" };
                const allBlocks = { id: -2, locType: "District", name: "All Block", type: "D" };
                var minLevel = 1;
                var init = function () {
                    $scope.noLocationInHierarchyMessage = null;
                    Mask.show();
                    LocationService.retrieveLocationTypes().then((response) => {
                        $scope.locationTypes = response.filter((r) => r.isActive);
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                    if (!$scope.selectedLocation) {
                        $scope.selectedLocation = {};
                    }
                    if (!$scope.templateType) {
                        $scope.templateType = 'inline';
                    }
                    if (!$scope.demographicFilterRequired || $scope.fetchAccordingToUserAoi === 'false') {
                        $scope.demographicFilterRequired = false;
                    } else if ($scope.demographicFilterRequired === "true") {
                        $scope.demographicFilterRequired = true;
                    }
                    if (!$scope.allDistrictReq || $scope.allDistrictReq === 'false') {
                        $scope.allDistrictReq = false;
                    } else if ($scope.demographicFilterRequired === "true") {
                        $scope.allDistrictReq = true;
                    }
                    if (!$scope.allBlockReq || $scope.allBlockReq === 'false') {
                        $scope.allBlockReq = false;
                    } else if ($scope.demographicFilterRequired === "true") {
                        $scope.allBlockReq = true;
                    }
                    if (!$scope.fetchAccordingToUserAoi || $scope.fetchAccordingToUserAoi === 'false') {
                        $scope.fetchAccordingToUserAoi = false;
                    } else if ($scope.fetchAccordingToUserAoi) {
                        $scope.fetchAccordingToUserAoi = true;
                    } else {
                        $scope.fetchAccordingToUserAoi = false;
                    }
                    $scope.locations = [];
                    AuthenticateService.getLoggedInUser().then(function (res) {
                        $scope.loggedInUser = res.data;
                        $scope.onLocationChange(null, true);
                        $scope.onFirstRunTemp = true;
                    })
                };

                $scope.onFirstRun = function (location) {
                    if($scope.onFirstRunTemp && location.locationDetails.length==1){
                        $scope.selectedLocation['level' + location.level] = location.locationDetails[0];
                        $scope.selectedLocation.level = location.level;
                        $scope.onLocationChange($scope.selectedLocation, false);
                        $scope.onFirstRunTemp = false;
                    }
                }

                $scope.onDemographicChange = function () {
                    $scope.selectedDemographic = $scope.selectedLocation.selectedDemographic;
                };

                $scope.onLocationChange = function (location, loadInit) {
                    var locationIds = [], level;
                    // clear lower levels
                    if (location || $scope.selectedLocation.finalSelected && !$scope.selectedLocation.finalSelected.optionSelected.id) {
                        clearLowerLevelLocation(location.level);
                        level = location.level + 1;
                    }

                    if ($scope.selectedLocation && location && $scope.selectedLocation['level' + location.level]) {
                        if ($scope.multipalOnLevel == location.level) {
                            locationIds = $scope.selectedLocation['level' + location.level].map(selected => selected.id);
                        }
                        else {
                            locationIds.push($scope.selectedLocation['level' + location.level].id);
                        }
                    }
                    var locationSelected = angular.copy(location);
                    if (location) {
                        locationSelected.optionSelected = $scope.selectedLocation['level' + location.level];
                        locationSelected['level' + location.level + 'options'] = $scope.locations[$scope.locations.length - 1].locationDetails;
                    }
                    $scope.selectedLocation.finalSelected = locationSelected;
                    if ((($scope.fetchUptoLevel && location && location.level + 1 <= $scope.fetchUptoLevel)
                        || loadInit || !$scope.fetchUptoLevel)
                        && $scope.selectedLocation.finalSelected &&
                        ($scope.multipalOnLevel != location.level &&
                            $scope.selectedLocation.finalSelected.optionSelected
                            || $scope.multipalOnLevel == location.level &&
                            $scope.selectedLocation.finalSelected.optionSelected.length > 0) || loadInit) {
                        // when selected location is not all blocks set relative parent
                        let level3Id
                        if (level === 4 && $scope.multipalOnLevel == location.level) {
                            level3Id = $scope.selectedLocation.level3 && $scope.selectedLocation.level3.length > 0 ? $scope.selectedLocation.level3[0].id : -2;
                        } else if (level === 4) {
                            level3Id = $scope.selectedLocation.level3.id
                        }
                        if (level === 4 && level3Id !== -2) {
                            LocationService.getParent(level3Id, $scope.loggedInUser.languagePreference).then(function (res) {
                                $scope.selectedLocation.level2 = res;
                                $scope.locations.pop();
                                addLocationHierchy([res.id], 3, loadInit).then(function () {
                                    addLocationHierchy(locationIds, level, loadInit);
                                });
                            }, GeneralUtil.showMessageOnApiCallFailure);
                        }
                        // added -2 for all blocks when selected location is all blocks set parent all districts
                        else if (level === 4 && level3Id === -2) {
                            $scope.selectedLocation.level2 = allRegion;
                            $scope.locations.pop();
                            addLocationHierchy([-1], 3, loadInit).then(function () {
                                addLocationHierchy(locationIds, level, loadInit);
                            });
                        }
                        else if (level === 2 && $scope.fetchUptoLevel != level) {
                            addLocationHierchy(locationIds, level, loadInit).then(function () {
                                addLocationHierchy(locationIds, level + 1, loadInit);
                            });
                        } else {
                            addLocationHierchy(locationIds, level, loadInit);
                        }
                    }
                    // to get selected parent (Region) automatically on selection of child (District) when (option is selected and fetch upto level is given and current level is last level to fetch as 3)
                    else if ($scope.selectedLocation.finalSelected &&
                        $scope.selectedLocation.finalSelected.optionSelected &&
                        $scope.fetchUptoLevel &&
                        location &&
                        location.level === 3 &&
                        location.level == $scope.fetchUptoLevel) {

                        let level3Id;
                        if ($scope.multipalOnLevel) {
                            level3Id = $scope.selectedLocation.level3 && $scope.selectedLocation.level3.length > 0 ? $scope.selectedLocation.level3[0].id : -2;
                        } else {
                            level3Id = $scope.selectedLocation.level3.id
                        }
                        LocationService.getParent(level3Id, $scope.loggedInUser.languagePreference).then(function (res) {
                            $scope.selectedLocation.level2 = res;
                            $scope.locations.pop();
                            addLocationHierchy([res.id], 3, loadInit);
                        }, GeneralUtil.showMessageOnApiCallFailure);
                    }
                    // to get previous level details when option is unselected;
                    else if ($scope.multipalOnLevel != location.level &&
                        !$scope.selectedLocation.finalSelected.optionSelected
                        || $scope.multipalOnLevel == location.level &&
                        $scope.selectedLocation.finalSelected.optionSelected.length == 0) {
                        level = location.level - 1;
                        if (level === 1) {
                            addLocationHierchy([2], 3, loadInit);
                        }
                        var previousLevel = _.find($scope.locations, function (loc) {
                            return loc.level === level;
                        });

                        if (previousLevel) {
                            $scope.selectedLocation.finalSelected.previousLevelLabel = previousLevel.locationLabel;
                            $scope.selectedLocation.finalSelected.previousLevelOptions = previousLevel.locationDetails;
                        }
                        if ($scope.selectedLocation["level" + level]) {
                            if ($scope.multipalOnLevel == level) {
                                $scope.ngModel = $scope.selectedLocation["level" + level].map(selected => selected.id);
                            } else {
                                if ($scope.multipalOnLevel) {
                                    let ids = [];
                                    ids.push($scope.selectedLocation["level" + level].id);
                                    $scope.ngModel = ids;

                                } else {
                                    $scope.ngModel = $scope.selectedLocation["level" + level].id
                                }
                            }
                            $scope.passedLocationLevel = level - 1;
                        } else {
                            delete $scope.ngModel;
                            delete $scope.passedLocationLevel;
                        }
                    }

                    if (!loadInit && $scope.selectedLocation && ($scope.multipalOnLevel != location.level &&
                        $scope.selectedLocation.finalSelected.optionSelected
                        || $scope.multipalOnLevel == location.level &&
                        $scope.selectedLocation.finalSelected.optionSelected.length > 0)) {
                        if ($scope.multipalOnLevel) {
                            let finalLocationIds = [];
                            if (Array.isArray($scope.selectedLocation.finalSelected.optionSelected)) {
                                finalLocationIds = $scope.selectedLocation.finalSelected.optionSelected.map(selected => selected.id)
                            } else {
                                finalLocationIds.push($scope.selectedLocation.finalSelected.optionSelected.id);
                            }
                            $scope.ngModel = finalLocationIds;

                        } else {
                            $scope.ngModel = $scope.selectedLocation.finalSelected.optionSelected.id;
                        }
                        $scope.passedLocationLevel = level - 1;
                    }
                    if (angular.isFunction($scope.onChange)) {
                        $scope.onChange();
                    }
                };

                var clearLowerLevelLocation = function (selectedLocationLevel) {
                    // remove if selected and levels above the final selected
                    if ($scope.selectedLocation.finalSelected && $scope.selectedLocation.finalSelected.level > selectedLocationLevel) {
                        $scope.locations = _.filter($scope.locations, function (location) {
                            if ($scope.selectedLocation['level' + location.level] && location.level > selectedLocationLevel) {
                                $scope.selectedLocation['level' + location.level] = {};
                                delete $scope.selectedLocation['level' + location.level];
                            }
                            return location.level <= selectedLocationLevel;
                        });
                    }
                    // remove higher level also
                    $scope.locations = _.filter($scope.locations, function (location) {
                        return location.level <= selectedLocationLevel;
                    });
                };

                $scope.$watch('selectedLocation', function (newValue) {
                    $scope.noLocationInHierarchyMessage = null;
                    if (Object.keys(newValue).length === 0) {
                        clearLowerLevelLocation(minLevel);
                        $scope.form.locationForm.$setPristine();
                    }
                }, true);

                var addLocationHierchy = function (locationIds, level, loadInit) {
                    return LocationService.retrieveNextLevel(locationIds, level, $scope.fetchAccordingToUserAoi, $scope.loggedInUser.languagePreference).then(function (res) {
                        if (res && res.length > 0) {
                            if ($scope.allDistrictReq && level === 2) {
                                res[0].locationDetails.unshift(allRegion);
                            } else if ($scope.allBlockReq && level === 3) {
                                res[0].locationDetails.unshift(allBlocks);
                            }
                            $scope.locations.push(res[0]);
                            if (loadInit) {
                                minLevel = res[0].level;
                                if ($scope.requiredUptoLevel == 1) {
                                    $scope.requiredUptoLevel = minLevel;
                                }
                            }
                        } else if ($scope.requiredUptoLevel >= level) {
                            let nextLevelNames = Array.isArray($scope.locationTypes) && $scope.locationTypes.length ? $scope.locationTypes.filter((type) => type.level === level).map((type) => type.name) : [];
                            $scope.noLocationInHierarchyMessage = Array.isArray(nextLevelNames) && nextLevelNames.length ? nextLevelNames.join('/') : null;
                            toaster.pop('error', 'Could not find any location under the selected hierarchy');
                        }
                        $scope.selectedLocation.optionsLists = [];
                        _.each($scope.locations, function (location) {
                            $scope.selectedLocation.optionsLists['level' + location.level] = location.locationDetails;
                        });
                    }, GeneralUtil.showMessageOnApiCallFailure);
                };

                init();
            }
        };
    };
    angular.module('imtecho.directives').directive('locationDirective', locationDirective);
})();
