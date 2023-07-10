(function () {
    function FamilyMovingController(Mask, AuthenticateService, $uibModal, AnganwadiService, GeneralUtil, QueryDAO, toaster, $state) {
        var fm = this;
        fm.selectedLocation = null;
        fm.selectedMoveLocation = null;
        fm.searchBy = 'FAMILYID';
        fm.familyList = [];
        fm.selectedFamilyIds = [];
        fm.selectedLocationIds = [];
        fm.isArchivedFamily = false;
        fm.isVerifiedFamily = false;
        fm.familiesToBePushed = [];

        fm.resetLocationHierarchy = function () {
            fm.selectedLocationIds = [];
            fm.selectedMoveLocation = null;
        }

        var init = function () {
            AuthenticateService.getLoggedInUser().then(function (res) {
                fm.currentUser = res.data;
            });
            if ($state.current.name === 'techo.manage.familymoving') {
                AuthenticateService.getAssignedFeature('techo.manage.familymoving').then(function (res) {
                    //According to Feature assign to user Search option will be display to the user
                    if (res && res.featureJson) {
                        fm.canSearchByLocation = res.featureJson.canSearchByLocation;
                        fm.canSearchByFamilyId = res.featureJson.canSearchByFamilyId;
                        fm.canSearchByMemberHealthId = res.featureJson.canSearchByMemberHealthId;
                        fm.canSearchArchived = res.featureJson.canSearchArchived;
                        fm.canMarkUnverified = res.featureJson.canMarkUnverified;
                    } else {
                        fm.canSearchByLocation = false;
                        fm.canSearchByFamilyId = false;
                        fm.canSearchByMemberHealthId = true;
                        fm.canSearchArchived = false;
                        fm.isVerifiedFamily = false;
                        fm.canMarkUnverified = false;
                    }
                    //For Default Option selection
                    if (!fm.canSearchByFamilyId) {
                        if (fm.canSearchByMemberHealthId) {
                            fm.searchBy = 'MEMBERID';
                        } else {
                            if (fm.canSearchByLocation) {
                                fm.searchBy = 'LOCATION';
                            }
                        }
                    }
                });
            } 
            // else if ($state.current.name === 'techo.manage.verifiedfamilymoving') {
            //     AuthenticateService.getAssignedFeature('techo.manage.verifiedfamilymoving').then(function (res) {
            //         //According to Feature assign to user Search option will be display to the user
            //         if (res && res.featureJson) {
            //             fm.canSearchByLocation = res.featureJson.canSearchByLocation;
            //             fm.canSearchByFamilyId = res.featureJson.canSearchByFamilyId;
            //             fm.canSearchByMemberHealthId = res.featureJson.canSearchByMemberHealthId;
            //             fm.isVerifiedFamily = true;
            //         } else {
            //             fm.canSearchByLocation = false;
            //             fm.canSearchByFamilyId = false;
            //             fm.canSearchByMemberHealthId = true;
            //             fm.isVerifiedFamily = false;
            //             fm.canMarkUnverified = false;
            //             fm.canSearchArchived = false;
            //         }
            //         //For Default Option selection
            //         if (!fm.canSearchByFamilyId) {
            //             if (fm.canSearchByMemberHealthId) {
            //                 fm.searchBy = 'MEMBERID';
            //             } else {
            //                 if (fm.canSearchByLocation) {
            //                     fm.searchBy = 'LOCATION';
            //                 }
            //             }
            //         }
            //     });
            // }
        }

        //This is to search by familyId /Member Unique HealthId/Location according to assign feature
        fm.searchFamily = function () {
            if (fm.searchBy === 'LOCATION') {
                fm.searchForm.$setSubmitted();
                //If Final selected Location Level is not 6 then return
                if (!fm.selectedLocation || !fm.selectedLocation.finalSelected ||
                    !(fm.selectedLocation.finalSelected.level == 7 || fm.selectedLocation.finalSelected.level == 8)) {
                    return;
                } else {
                    if (fm.selectedLocationIds.indexOf(fm.selectedLocation.finalSelected.optionSelected.id) >= 0) {
                        toaster.pop("warning", "This Location's Families are already added!");
                        return;
                    }
                }
            }
            if (fm.searchForm.$valid) {
                fm.message = '';
                fm.assignedFamilies = [];
                var searchByFamilyId = fm.searchBy === 'FAMILYID' ? true : false;
                var searchByLocationId = fm.searchBy === 'LOCATION' ? true : false;
                var searchString = fm.searchBy === 'LOCATION' ? fm.selectedLocation.finalSelected.optionSelected.id : (fm.searchString);
                if (fm.searchBy !== 'LOCATION') {
                    for (var i = 0; i < fm.searchString.length; i++)
                        fm.searchString[i] = fm.searchString[i].replace(' ', '');
                } else {
                    fm.familiesToBePushed = [];
                }
                Mask.show();
                fm.familyList = [];
                fm.selectedLocationIds = [];
                AnganwadiService.searchFamily(fm.currentUser.id, searchString, searchByFamilyId, searchByLocationId, fm.isArchivedFamily, fm.isVerifiedFamily).then(function (res) {
                    delete res.$promise;
                    delete res.$resolved;
                    var mapList = res;
                    if (res.length < 1) {
                        toaster.pop('warning', "No Families Found");
                        fm.familyList = [];
                        return;
                    }
                    _.each(mapList, function (message) {
                        var familymap = Object.keys(message)[0];
                        if (message[familymap] !== null) {
                            _.each(message[familymap], function (familyByLocationId) {
                                if (fm.searchBy === 'LOCATION') {
                                    familyByLocationId.location = fm.selectedLocation.finalSelected.optionSelected.name;
                                    familyByLocationId.locationIdToCheck = fm.selectedLocation.finalSelected.optionSelected.id;
                                    fm.selectedLocationIds.push(fm.selectedLocation.finalSelected.optionSelected.id);
                                }
                                if (fm.familiesToBePushed.length > 0) {
                                    var assignedFamily = _.find(fm.familiesToBePushed, { familyId: familyByLocationId.familyId });
                                    if (assignedFamily) {
                                        fm.message += assignedFamily.familyId + " Family is already added !<br>";
                                        fm.assignedFamilies.push(familyByLocationId);
                                    } else {
                                        _.each(familyByLocationId.members, function (member) {
                                            if (member.familyHeadFlag) {
                                                familyByLocationId.hof = member;
                                            }
                                        });
                                        fm.familiesToBePushed.push(familyByLocationId);
                                    }
                                } else {
                                    _.each(familyByLocationId.members, function (member) {
                                        if (member.familyHeadFlag) {
                                            familyByLocationId.hof = member;
                                        }
                                    });
                                    fm.familiesToBePushed.push(familyByLocationId);
                                }
                            });
                        } else {
                            fm.message += familymap + '<br>';
                        }
                    });
                    fm.familyList = fm.familyList.concat(fm.familiesToBePushed);
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    // At last remove search data
                    fm.searchString = '';
                    fm.searchForm.$setPristine();
                    fm.changeSelection();
                    Mask.hide();
                });
            }
        };

        fm.changeSelection = function () {
            fm.selectedFamilyIds = [];
            var count = 0;
            _.each(fm.familyList, function (family) {
                if (family.isSelected) {
                    count++;
                    fm.selectedFamilyIds.push(family);
                }
            });
            if (count === fm.familyList.length && count !== 0) {
                fm.allSelected = true;
            } else {
                fm.allSelected = false;
            }
        };

        fm.selectAll = function () {
            fm.selectedFamilyIds = [];
            if (fm.allSelected) {
                _.each(fm.familyList, function (family) {
                    family.isSelected = true;
                    fm.selectedFamilyIds.push(family);
                });
            } else {
                _.each(fm.familyList, function (family) {
                    family.isSelected = false;
                });
            }
        };
        //this is to move Family location By Location Id that user has selected
        fm.moveFamilyLocation = function () {
            fm.locationForm.$setSubmitted();

            if (fm.isVerifiedFamily && fm.selectedMoveLocation.finalSelected.level === 7) {
                toaster.pop("warning", fm.selectedMoveLocation.level6.name + " is not proper Location to move family");
                return;
            }
            if (fm.locationForm.$valid && fm.selectedFamilyIds.length > 0) {
                if (!fm.selectedMoveLocation.finalSelected || !fm.selectedMoveLocation.finalSelected.optionSelected) {
                    return;
                }
                var selectedFamilyIdList = [];
                var selectedFamilyIdArray = [];
                var sameLocationIdFamilyList = [];
                _.each(fm.selectedFamilyIds, function (family) {
                    if (family.locationIdToCheck === fm.selectedMoveLocation.finalSelected.optionSelected.id) {
                        sameLocationIdFamilyList.push(family);
                    }
                });
                if (sameLocationIdFamilyList.length === 0) {
                    //This is for Conforming and showing modal with selected Family List
                    var modalInstanceProperties = {
                        controllerAs: 'fmconf',
                        controller: ['$scope', 'type', '$uibModalInstance', function ($scope, type, $uibModalInstance) {
                            var fmconf = this;
                            fmconf.familyDetails = [];
                            fmconf.familyDetails = fm.selectedFamilyIds;
                            fmconf.message = 'This is the Family Details,Are You sure you want to move This Family List?';
                            fmconf.ok = function () {
                                $uibModalInstance.close();
                                fm.searchString = '';
                                fm.searchForm.$setPristine();
                                _.each(fm.selectedFamilyIds, function (family) {
                                    family.isSelected = true;
                                    selectedFamilyIdList.push("'" + family.familyId + "'");
                                    selectedFamilyIdArray.push(family.familyId);
                                });
                            };
                            fmconf.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                                fm.searchString = '';
                                fm.searchForm.$setPristine();
                            };
                        }],
                        windowClass: 'cst-modal',
                        backdrop: 'static',
                        size: 'md',
                        templateUrl: 'app/manage/familymoving/views/family-moving-detail-show.modal.html',
                        resolve: {
                            type: function () {
                                return {};
                            }
                        }
                    };
                    var modalInstance = $uibModal.open(modalInstanceProperties);
                    //If confirm by user then move family list
                    modalInstance.result.then(function () {
                        if (!fm.isVerifiedFamily) {
                            var dto = {
                                code: 'family_move_location_by_familyids',
                                parameters: {
                                    family_ids: selectedFamilyIdArray,
                                    user_id: fm.currentUser.id
                                }
                            };
                            if (fm.selectedMoveLocation && fm.selectedMoveLocation.finalSelected && fm.selectedMoveLocation.finalSelected.optionSelected !== null) {
                                dto.parameters.location_id = fm.selectedMoveLocation.finalSelected.optionSelected.id;
                                fm.temp1 = fm.selectedMoveLocation;
                                fm.temp2 = fm.selectedLocationId;
                            }
                            Mask.show();
                            QueryDAO.execute(dto).then(function (res) {
                                toaster.pop('success', "Family Moved Successfully");
                                //Delete family from FamilyList which are moved
                                _.each(fm.selectedFamilyIds, function (selectedFamily) {
                                    fm.familiesToBePushed = _.filter(fm.familiesToBePushed, function (familyPushed) {
                                        return selectedFamily.familyId !== familyPushed.familyId;
                                    })
                                });
                                _.each(fm.selectedFamilyIds, function (selectedFamily) {
                                    fm.familyList = _.filter(fm.familyList, function (family) {
                                        return selectedFamily.familyId !== family.familyId;
                                    })
                                });
                                fm.selectedMoveLocation = [];
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                fm.searchString = '';
                                fm.allSelected = false;
                                fm.locationForm.$setPristine();
                                fm.selectedMoveLocation = fm.temp1;
                                fm.selectedLocationId = fm.temp2;
                                fm.selectedLocationIds = _.without(fm.selectedLocationIds, dto.parameters.location_id);
                                fm.selectedMoveLocation.finalSelected.optionSelected.id = dto.parameters.location_id;
                                fm.selectedFamilyIds = [];
                                Mask.hide();
                            });
                        } else {
                            Mask.show();
                            AnganwadiService.updateVerifiedFamilyLocation(selectedFamilyIdList, fm.selectedMoveAnganwadiAreaId, fm.selectedLocationId).then(function (res) {
                                toaster.pop('success', "Family Moved Successfully");
                                _.each(fm.selectedFamilyIds, function (selectedFamily) {
                                    fm.familiesToBePushed = _.filter(fm.familiesToBePushed, function (familyPushed) {
                                        return selectedFamily.familyId !== familyPushed.familyId;
                                    })
                                });
                                _.each(fm.selectedFamilyIds, function (selectedFamily) {
                                    fm.familyList = _.filter(fm.familyList, function (family) {
                                        return selectedFamily.familyId !== family.familyId;
                                    })
                                });
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                                fm.searchString = '';
                                fm.allSelected = false;
                                fm.locationForm.$setPristine();
                                fm.selectedFamilyIds = [];
                                Mask.hide();
                            });
                        }
                    });

                } else {
                    var data = '';
                    //if Searched Family is already in List then Show That family ids In message
                    if (sameLocationIdFamilyList.length > 0) {
                        _.each(sameLocationIdFamilyList, function (family) {
                            data += '(Family Id-' + family.familyId + ')<br>';
                        });
                    }
                    toaster.pop({ type: "danger", title: "This Family's current locaion and moved location is same!", body: data });
                    return;
                }
            }
        };

        fm.markAsUnverified = function () {
            var fids = [];
            angular.forEach(fm.selectedFamilyIds, function (family) {
                fids.push( family.familyId)
            })
            if (!fm.selectedLocationId) {
                let dto = {
                    code: 'family_unverified_marking',
                    parameters: {
                        locationId: null,
                        familyIds: fm.selectedFamilyIds
                    }
                }
                Mask.show();
                QueryDAO.execute(dto).then(function (res) {
                    toaster.pop('success', "Family Marked Unverified");
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                })
            } else {
                fm.locationForm.$setSubmitted();
                if (fm.locationForm.$valid) {
                    let dto = {
                        code: 'family_unverified_marking',
                        parameters: {
                            locationId: fm.selectedLocationId,
                            familyIds: fids,
                            userId: fm.currentUser.id
                        }
                    }
                    Mask.show();
                    QueryDAO.execute(dto).then(function (res) {
                        toaster.pop('success', "Family Marked Unverified");
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                }
            }
        };

        fm.locationChange = function () {
            if (!!fm.selectedMoveLocation) {
                fm.selectedLocationId = fm.selectedMoveLocation.finalSelected.optionSelected.id;
                if (fm.selectedMoveLocation.finalSelected.level === 7) {
                    fm.selectedMoveAnganwadiAreaId = fm.selectedMoveLocation.finalSelected.optionSelected.id;
                }
            }
        }

        init();
    }

    angular.module('imtecho.controllers').controller('FamilyMovingController', FamilyMovingController);
})();
