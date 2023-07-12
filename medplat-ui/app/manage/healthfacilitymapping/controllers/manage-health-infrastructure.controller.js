(function () {
    function ManageHealthInfrastructure($state, QueryDAO, $q, Mask, toaster, GeneralUtil, AuthenticateService, LocationService, $uibModal) {
        var hi = this;
        hi.env = GeneralUtil.getEnv();
        hi.listState = 'techo.manage.healthinfrastructures';
        hi.allowedFacilities = [];

        var init = function () {
            if ($state.current.name === 'techo.manage.covid19healthinfrastructure') {
                hi.listState = 'techo.manage.covid19healthinfrastructures';
            }
            hi.infrastructure = {
                name: '',
                address: '',
                fundedBy: '',
                nameingujarati: '',
                postalcode: '',
                latitude: '',
                longitude: '',
                landlinenumber: '',
                mobilenumber: '',
                nin: '',
                email: '',
                contactPersonName: '',
                contactNumber: '',
                facilities: {}
            };
            hi.labTestList = [];
            hi.wardDetails = [];
            let dtoList = [];
            let fetchInfraTypes = {
                code: 'retrieve_field_values_for_form_with_health_infra_type',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'infra_type'
                },
                sequence: 1
            };
            dtoList.push(fetchInfraTypes);
            let fetchWardTypes = {
                code: 'retrieve_field_values_for_form_field',
                parameters: {
                    form: 'WEB',
                    fieldKey: 'health_infrastructure_ward_types'
                },
                sequence: 2
            }
            dtoList.push(fetchWardTypes);
            Mask.show();
            QueryDAO.executeAll(dtoList).then(function (responses) {
                hi.types = responses[0].result;
                hi.wardTypes = responses[1].result;
            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
            AuthenticateService.getLoggedInUser().then(function (res) {
                hi.loggedInUser = res.data;
            })

            let promises = [];
            if (!$state.params.id && hi.listState == 'techo.manage.healthinfrastructures') {
                promises.push(AuthenticateService.getAssignedFeature('techo.manage.healthinfrastructures'));
                Mask.show();
                $q.all(promises).then(function (responses) {
                    hi.currentUserRights = responses[0].featureJson;
                    Mask.hide();
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    Mask.hide();
                });
            }

            hi.editMode = false;
            if ($state.params.id) {
                hi.editMode = true;
                hi.retrieveHealthInfraType().then(function () {
                    hi.retrieveById().then(function () {
                        hi.infrastructureTypeChanged(false);
                    })
                })
            } else {
                hi.retrieveHealthInfraType();
            }
            AuthenticateService.getAssignedFeature(hi.listState).then(function (res) {
                hi.user = res.featureJson;
            });
        };

        const isWardDetailsFormValid = function () {
            if (!hi.infrastructure.facilities.isCovidHospital || (hi.infrastructure.facilities.isCovidHospital && hi.wardDetails && hi.wardDetails.length > 0 && hi.manageWardDetailsForm.$valid)) {
                return true;
            } else if (hi.infrastructure.facilities.isCovidHospital && (!hi.wardDetails || hi.wardDetails.length === 0 || !hi.manageWardDetailsForm.$valid)) {
                toaster.pop('error', 'Enter Ward details');
                return false;
            }
        }

        hi.save = function () {
            hi.manageHealthInfrastructureForm.$setSubmitted();
            hi.manageWardDetailsForm.$setSubmitted();
            if (isWardDetailsFormValid() && hi.manageHealthInfrastructureForm.$valid) {
                hi.infrastructure = { ...hi.infrastructure, ...hi.infrastructure.facilities };
                if (hi.infrastructure.emamtaid == '') {
                    delete hi.infrastructure.emamtaid;
                }
                if (hi.infrastructure.iscpconfirmationcenter) {
                    QueryDAO.execute({
                        code: 'health_infrastructure_cp_confirmation_count',
                        parameters: {
                            locationid: hi.infrastructure.locationid,
                            id: hi.infrastructure.id
                        }
                    }).then(function (res) {
                        if (res.result.length == 0 || res.result[0].count == 0) {
                            saveUpdateInfrastructure();
                        } else {
                            toaster.pop('error', 'Health Infrastructure ' + res.result[0].name + ' already has CP Confirmation Center');
                        }
                    })
                } else {
                    saveUpdateInfrastructure();
                }
            }
        };

        var saveUpdateInfrastructure = function () {

            QueryDAO.execute({
                code: 'check_health_infra_assignable_location',
                parameters: {
                    locationId: hi.infrastructure.locationid,
                    healthInfraType: hi.infrastructure.type
                }
            }).then(function (response) {
                let result = response.result[0];

                if (result && (result.loc_types == null || result.loc_types.split(',').indexOf(result.type) !== -1)) {
                    if (hi.infrastructure.latitude != null && hi.infrastructure.latitude != '') {
                        hi.infrastructure.latitude = Number(hi.infrastructure.latitude).toFixed(6);
                    }
                    if (hi.infrastructure.longitude != null && hi.infrastructure.longitude != '') {
                        hi.infrastructure.longitude = Number(hi.infrastructure.longitude).toFixed(6);
                    }
                    if (hi.infrastructure.noOfBeds === "") {
                        hi.infrastructure.noOfBeds = null;
                    } else {
                        hi.infrastructure.noOfBeds = Number(hi.infrastructure.noOfBeds);
                    }
                    if (hi.infrastructure.noOfPpe === "") {
                        hi.infrastructure.noOfPpe = null;
                    } else {
                        hi.infrastructure.noOfPpe = Number(hi.infrastructure.noOfPpe);
                    }
                    hi.saveLabTestValue();
                    hi.saveCategoryValue();

                    if (hi.editMode) {
                        hi.infrastructure.modifiedBy = hi.loggedInUser.id
                        // below fields are not used in query
                        delete hi.infrastructure.created_on
                        delete hi.infrastructure.locationtype
                        delete hi.infrastructure.typeOfHospitalName
                        delete hi.infrastructure.child_id
                        delete hi.infrastructure.locationname
                        let payload = angular.copy(hi.infrastructure)
                        delete payload.facilities;
                        Mask.show();
                        QueryDAO.execute({
                            code: 'health_infrastructure_update',
                            parameters: payload
                        }).then(function (res) {
                            if (!hi.user.canEditCovid19Hospital || !hi.infrastructure.isCovidHospital) {
                                toaster.pop('success', 'Updated Successfully')
                                $state.go(hi.listState);
                                Mask.hide();
                                return;
                            }
                            let healthInfraId = res.result[0].id;
                            if (!healthInfraId) {
                                return Promise.reject({ data: { message: 'Something went wrong. Please try again.' } });
                            }
                            LocationService.createOrUpdateHealthInfrastructureWards({
                                isCovidHospital: hi.infrastructure.isCovidHospital ? true : false,
                                healthInfraId,
                                healthInfrastructureWardDetails: hi.wardDetails
                            }).then(function () {
                                toaster.pop('success', 'Updated Successfully')
                                $state.go(hi.listState);
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
                        }, function () {
                            GeneralUtil.showMessageOnApiCallFailure();
                            Mask.hide();
                        });
                    } else if (!hi.locationNotAllowed) {
                        hi.infrastructure.createdBy = hi.loggedInUser.id
                        // below fields are not used in query
                        delete hi.infrastructure.created_on
                        delete hi.infrastructure.locationtype
                        delete hi.infrastructure.typeOfHospitalName
                        delete hi.infrastructure.child_id
                        delete hi.infrastructure.locationname
                        delete hi.infrastructure.nameingujarati
                        let payload = angular.copy(hi.infrastructure);
                        delete payload.facilities;
                        Mask.show();
                        QueryDAO.execute({
                            code: 'health_infrastructure_create',
                            parameters: payload
                        }).then(function (res) {
                            let healthInfraId = res.result[0].id;
                            hi.healthInfraId = healthInfraId;
                            if (!hi.user.canEditCovid19Hospital || !hi.infrastructure.isCovidHospital) {
                                toaster.pop('success', 'Saved Successfully')
                                Mask.hide();
                                if (hi.listState == 'techo.manage.covid19healthinfrastructures') {
                                    $state.go(hi.listState);
                                } else if (hi.listState == 'techo.manage.healthinfrastructures') {
                                    if (hi.currentUserRights.canLinkHfrFacility) {
                                        hi.showModal();
                                    } else {
                                        $state.go(hi.listState);
                                    }
                                }
                                return;
                            }

                            if (!healthInfraId) {
                                // return Promise.reject({ data: { message: 'Something went wrong. Please try again.' } });
                            }
                            LocationService.createOrUpdateHealthInfrastructureWards({
                                isCovidHospital: hi.infrastructure.isCovidHospital ? true : false,
                                healthInfraId,
                                healthInfrastructureWardDetails: hi.wardDetails
                            }).then(function () {
                                toaster.pop('success', 'Saved Successfully')
                                Mask.hide();
                                if (hi.listState == 'techo.manage.covid19healthinfrastructures') {
                                    $state.go(hi.listState);
                                } else if (hi.listState == 'techo.manage.healthinfrastructures') {
                                    if (hi.currentUserRights.canLinkHfrFacility) {
                                        hi.showModal();
                                    } else {
                                        $state.go(hi.listState);
                                    }
                                }
                            }, GeneralUtil.showMessageOnApiCallFailure).finally(Mask.hide);
                        }, function () {
                            GeneralUtil.showMessageOnApiCallFailure();
                            Mask.hide();
                        });
                    }
                } else {
                    toaster.pop('error', 'The facility can allowed to assign to the following location types - ' + result.loc_value);
                }
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide();
            });
        };

        hi.saveLabTestValue = function () {
            let labTestIds = hi.labTestList.filter(x => {
                if (x.isChecked) {
                    return x
                }
            }).map(x => {
                return x.id
            }).join();
            labTestIds = "{" + labTestIds + "}";
            if (labTestIds.length > 0) {
                Mask.show();
                QueryDAO.execute({
                    code: 'insert_in_health_infra_lab_test_mapping',
                    parameters: {
                        health_infra_id: Number($state.params.id),
                        ref_ids: labTestIds,
                        type: 'OPD_LAB_TEST'
                    }
                }).then(function (res) {
                    Mask.hide();
                    return
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
            else {
                Mask.show();
                QueryDAO.execute({
                    code: 'delete_all_lab_test_by_health_infra_id',
                    parameters: {
                        health_infra_id: Number($state.params.id),
                        type: 'OPD_LAB_TEST'
                    }
                }).then(function (res) {
                    Mask.hide();
                    return
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }
        }

        hi.saveCategoryValue = function () {
            if (hi.categoryList != null && hi.categoryList.length > 0) {
                let categoryIds = hi.categoryList.filter(x => {
                    if (x.isChecked) {
                        return x
                    }
                }).map(x => {
                    return x.id
                }).join();
                categoryIds = "{" + categoryIds + "}";
                if (categoryIds.length > 0) {
                    Mask.show();
                    QueryDAO.execute({
                        code: 'insert_in_health_infra_lab_test_mapping',
                        parameters: {
                            health_infra_id: Number($state.params.id),
                            ref_ids: categoryIds,
                            type: 'OPD_CATEGORY'
                        }
                    }).then(function (res) {
                        Mask.hide();
                        return
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                }
                else {
                    Mask.show();
                    QueryDAO.execute({
                        code: 'delete_all_lab_test_by_health_infra_id',
                        parameters: {
                            health_infra_id: Number($state.params.id),
                            type: 'OPD_CATEGORY'
                        }
                    }).then(function (res) {
                        Mask.hide();
                        return
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                }
            }
        }

        hi.retrieveHealthInfraType = function () {
            Mask.show();
            return QueryDAO.execute({
                code: 'retrieve_location_level_for_infra_type'
            }).then(function (res) {
                hi.locationHealthTypeMap = {};
                _.map(res.result, function (result) {
                    hi.locationHealthTypeMap[result.infraid] = result.level;
                });
            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide()
            });
        };

        hi.checkForLabTest = function (category) {
            if (category.isChecked) {
                let labTests = hi.labTests.filter(x => x.category === category.id)
                hi.labTestList = hi.labTestList.concat(labTests)
            } else {
                hi.labTests.forEach(element => {
                    if (element.category == category.id) {
                        element.isChecked = false
                    }
                });
                hi.labTestList = hi.labTestList.filter(x => x.category !== category.id)
            }
        }

        hi.retrieveById = function () {
            let dtoList = [];
            let fetchHealthInfrastructure = {
                code: 'health_infrastructure_retrieve_by_id',
                parameters: {
                    id: Number($state.params.id)
                },
                sequence: 1
            }
            dtoList.push(fetchHealthInfrastructure);
            let fetchWardDetails = {
                code: 'get_health_infrastructure_ward_details',
                parameters: {
                    healthInfraId: Number($state.params.id)
                },
                sequence: 2
            }
            dtoList.push(fetchWardDetails);
            let healthInfrastructureFacilities = {
                code: 'retrieve_health_infrastructure_facilities_by_id',
                parameters: {
                    healthInfraId: Number($state.params.id)
                },
                sequence: 3
            }
            dtoList.push(healthInfrastructureFacilities);
            Mask.show();
            return QueryDAO.executeAll(dtoList).then(function (responses) {
                hi.infrastructure = responses[0].result[0];
                hi.infrastructure.latitude = Number(hi.infrastructure.latitude);
                hi.infrastructure.longitude = Number(hi.infrastructure.longitude);
                hi.locationIdCopy = hi.infrastructure.locationid;
                hi.infrastructureLevel = hi.locationHealthTypeMap[hi.infrastructure.type];
                hi.wardDetails = responses[1].result;
                hi.infrastructure.facilities = responses[2].result[0] || {}
                hi.getCategoryDetails()

            }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                Mask.hide()
            });
        }

        hi.locationChange = function () {
            if ((hi.selectedLocation && hi.selectedLocation.finalSelected) &&
                ((hi.selectedLocation.finalSelected.optionSelected && hi.selectedLocation.finalSelected.level == 2)
                    || (!hi.selectedLocation.finalSelected.optionSelected && hi.selectedLocation.finalSelected.level - 1 <= 2))) {
                hi.infrastructure.facilities.iscpconfirmationcenter = false;
            }

        }

        hi.infrastructureTypeChanged = function (resetFacilities) {
            hi.selectedLocation = {};
            if (hi.infrastructure != null && hi.infrastructure.type != null) {
                if (hi.locationHealthTypeMap != null) {
                    hi.infrastructureLevel = hi.locationHealthTypeMap[hi.infrastructure.type];
                }

                let selectedInfra = hi.types.filter(hf => hf.id == hi.infrastructure.type)
                selectedInfra = selectedInfra ? selectedInfra[0] : null;
                hi.requiredUpToLevel = null;

                if (selectedInfra.location_level) {

                    // sorting all possible assignable location level
                    const levels = selectedInfra.location_level.split(",").map(Number).sort(function (a, b) { return a - b });
                    // setting max location as fetch up to
                    hi.fetchUpToLevel = levels[levels.length - 1];
                    // setting min location as required location
                    hi.requiredUpToLevel = levels[0]

                } else if (hi.infrastructure.type === 1062) {
                    hi.fetchUpToLevel = 6
                } else if (hi.infrastructure.type === 1061 || hi.infrastructure.type === 1063) {
                    hi.fetchUpToLevel = 5
                } else if (hi.infrastructure.type === 1009 || hi.infrastructure.type === 1008 || hi.infrastructure.type === 1013 || hi.infrastructure.type === 1010 || hi.infrastructure.type === 1064 || hi.infrastructure.type === 1084) {
                    hi.fetchUpToLevel = 4
                } else if (hi.infrastructure.type === 1007 || hi.infrastructure.type === 1012) {
                    hi.fetchUpToLevel = 3
                } else {
                    hi.fetchUpToLevel = hi.infrastructureLevel
                }
            }
            if (resetFacilities) {
                hi.infrastructure.facilities = {};
            }
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_health_infrastructure_type_allowed_facilities',
                parameters: {
                    healthInfraTypeId: hi.infrastructure.type
                }
            }).then((response) => {
                if (response.result != null && response.result.length > 0) {
                    hi.allowedFacilities = response.result[0].allowed_facilities.split(',')
                } else {
                    hi.allowedFacilities = []
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        hi.getCategoryDetails = function () {
            QueryDAO.execute({
                code: 'fetch_listvalue_detail_from_field',
                parameters: {
                    field: 'OPD Lab Test Category'
                }
            }).then(function (res) {
                hi.categoryList = res.result;
                // hi.getLabTestDetails()
            }, GeneralUtil.showMessageOnApiCallFailure);
        }

        // hi.getLabTestDetails = function () {
        //     QueryDAO.execute({
        //         code: 'get_lab_test'
        //     }).then(function (res) {
        //         hi.labTests = res.result
        //         hi.setLabTestDetails()
        //     }, GeneralUtil.showMessageOnApiCallFailure);
        // }

        hi.setLabTestDetails = function (type) {
            QueryDAO.execute({
                code: 'fetch_health_infra_lab_test_mapping',
                parameters: {
                    healthInfraId: Number($state.params.id),
                    healthInfraType: hi.infrastructure.type
                }
            }).then(function (response) {
                if (hi.labTests.length > 0) {
                    hi.labTests.forEach(element => {
                        let labTest = response.result.find(x => x.ref_id === element.id && x.permission_type === 'OPD_LAB_TEST');
                        if (labTest) {
                            response.result.push({
                                'ref_id': element.category,
                                'permission_type': 'OPD_CATEGORY'
                            })
                            element.isChecked = true
                        } else {
                            element.isChecked = false
                        }
                        element.permissionType = type
                    });
                }
                if (hi.categoryList.length > 0) {
                    hi.categoryList.forEach(element => {
                        let labTest = response.result.find(x => x.ref_id === element.id && x.permission_type === 'OPD_CATEGORY');
                        if (labTest) {
                            element.isChecked = true
                        } else {
                            element.isChecked = false
                        }
                        element.permissionType = type
                    });
                }
                hi.categoryList.forEach(element => {
                    hi.checkForLabTest(element)
                });
            })
        }

        hi.addWard = function () {
            hi.wardDetails.push({
                wardName: null,
                wardType: null,
                numberOfBeds: null,
                numberOfVentilatorsType1: null,
                numberOfVentilatorsType2: null,
                numberOfO2: null,
                status: null
            });
        }



        hi.onChangeIsCovidHospital = function () {
            if (hi.infrastructure.facilities.isCovidHospital && (!hi.wardDetails || hi.wardDetails.length === 0)) {
                hi.addWard();
            }
        }

        hi.nutritionChange = () => {
            if (!hi.infrastructure.facilities.iscmtc && !hi.infrastructure.facilities.isnrc) {
                hi.infrastructure.fundedBy = null;
            }
        }

        hi.goBack = function () {
            $state.go(hi.listState);
        }

        hi.showModal = function () {
            $("#linkFacility").modal({ backdrop: 'static', keyboard: false });
        }


        hi.cancel = function () {
            hi.closeModal();
            $state.go(hi.listState);

        }

        hi.closeModal = function () {
            $("#linkFacility").modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }

        hi.showHpModal = () => {
            //close previous modal
            hi.closeModal();

            let modalInstance = $uibModal.open({
                templateUrl: 'app/manage/healthfacilitymapping/views/facility-linking.modal.html',
                controller: 'FacilityLinkingController',
                controllerAs: 'ctrl',
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    response: () => {
                        return { id: hi.healthInfraId };
                    },
                    menu: () => {
                        return 1;
                    },
                    manageState: () => {
                        return hi.listState;
                    },
                    types: () => {
                        return hi.types;
                    }
                }
            });

        };


        init();
    }
    angular.module('imtecho.controllers').controller('ManageHealthInfrastructure', ManageHealthInfrastructure);
})();
