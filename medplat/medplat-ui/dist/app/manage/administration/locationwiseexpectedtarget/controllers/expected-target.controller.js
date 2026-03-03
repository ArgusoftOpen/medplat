(function () {
    function ExpectedTargetController(LocationService, ExpectedTargetDAO, QueryDAO, toaster, Mask, AuthenticateService, GeneralUtil, $scope, $q, $state, $uibModal) {
        let ctrl = this;
        ctrl.appName = GeneralUtil.getAppName();
        const MOTHER_REGISTERED = "MOTHER_REGISTERED";
        const DELIVERY_REGISTERED = "DELIVERY_REGISTERED";
        const VACCINATION_1 = "VACCINATION_1";
        const VACCINATION_2 = "VACCINATION_2";
        ctrl.draftState = 'DRAFT';
        ctrl.lockedState = "LOCKED";
        ctrl.unlockedState = 'UNLOCKED';
        ctrl.disableState = true;
        ctrl.disableCurrentLocationLevel = true
        ctrl.isPrintClicked = false;
        ctrl.expectedTargetDto = {};
        ctrl.financialYearRanges = [];
        ctrl.selectedLocationLevel = null;
        ctrl.locationList = [];
        ctrl.selectedLocationIdFromFilter = null;
        ctrl.selectedLocation = null;
        ctrl.locationHierarchy = "";
        ctrl.total = {
            motherRegistered: 0,
            deliveryRegistered: 0,
            vaccinationDose: 0,
            year2ndVaccinationDose: 0
        }
        ctrl.difference = {
            motherRegistered: 0,
            deliveryRegistered: 0,
            vaccinationDose: 0,
            year2ndVaccinationDose: 0
        };

        ctrl.init = () => {
            let promises = [];
            promises.push(AuthenticateService.getLoggedInUser());
            promises.push(AuthenticateService.getAssignedFeature($state.current.name));
            promises.push(QueryDAO.execute({
                code: 'get_last_n_financial_year_range',
                parameters: {
                    count: 1
                }
            }));
            Mask.show();
            $q.all(promises).then((responses) => {
                ctrl.loggedInUser = responses[0].data;
                ctrl.rights = responses[1].featureJson;
                ctrl.financialYearRanges = responses[2].result;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        ctrl.saveMotherRegistered = () => {
            if (ctrl.isFormValid(MOTHER_REGISTERED)) {
                if (ctrl.totalMatched(MOTHER_REGISTERED)) {
                    let expectedTargetDtos = [];
                    ctrl.locationList.forEach(location => {
                        expectedTargetDtos.push({
                            locationId: location.id,
                            financialYear: ctrl.expectedTargetDto.financialYear,
                            Expected_Mthr_Reg: location.Expected_Mthr_Reg,
                            state: ctrl.draftState
                        });
                    });
                    ctrl.saveExpectedTarget(expectedTargetDtos);
                } else {
                    toaster.pop('error', 'Total should match with the given targets');
                }
            } else {
                toaster.pop('error', 'Please fill up all the values first');
            }
        }

        ctrl.saveDeliveryRegistered = () => {
            if (ctrl.isFormValid(DELIVERY_REGISTERED)) {
                if (ctrl.totalMatched(DELIVERY_REGISTERED)) {
                    let expectedTargetDtos = [];
                    ctrl.locationList.forEach(location => {
                        expectedTargetDtos.push({
                            locationId: location.id,
                            financialYear: ctrl.expectedTargetDto.financialYear,
                            Expected_Del_Reg: location.Expected_Del_Reg,
                            state: ctrl.draftState
                        });
                    });
                    ctrl.saveExpectedTarget(expectedTargetDtos);
                } else {
                    toaster.pop('error', 'Total should match with the given targets');
                }
            } else {
                toaster.pop('error', 'Please fill up all the values first');
            }
        }

        ctrl.saveVaccination1 = () => {
            if (ctrl.isFormValid(VACCINATION_1)) {
                if (ctrl.totalMatched(VACCINATION_1)) {
                    let expectedTargetDtos = [];
                    ctrl.locationList.forEach(location => {
                        expectedTargetDtos.push({
                            locationId: location.id,
                            financialYear: ctrl.expectedTargetDto.financialYear,
                            ELA_DPT_OPV_Mes_VitA_1Dose: location.ELA_DPT_OPV_Mes_VitA_1Dose,
                            state: ctrl.draftState
                        });
                    });
                    ctrl.saveExpectedTarget(expectedTargetDtos);
                } else {
                    toaster.pop('error', 'Total should match with the given targets');
                }
            } else {
                toaster.pop('error', 'Please fill up all the values first');
            }
        }

        ctrl.saveVaccination2 = () => {
            if (ctrl.isFormValid(VACCINATION_2)) {
                if (ctrl.totalMatched(VACCINATION_2)) {
                    let expectedTargetDtos = [];
                    ctrl.locationList.forEach(location => {
                        expectedTargetDtos.push({
                            locationId: location.id,
                            financialYear: ctrl.expectedTargetDto.financialYear,
                            ELA_DPT_OPV_Mes_VitA_2Dose: location.ELA_DPT_OPV_Mes_VitA_2Dose,
                            state: ctrl.draftState
                        });
                    });
                    ctrl.saveExpectedTarget(expectedTargetDtos);
                } else {
                    toaster.pop('error', 'Total should match with the given targets');
                }
            } else {
                toaster.pop('error', 'Please fill up all the values first');
            }
        }

        ctrl.saveExpectedTarget = (expectedTargetDtos) => {
            if (Array.isArray(expectedTargetDtos) && expectedTargetDtos.length) {
                Mask.show();
                ExpectedTargetDAO.createOrUpdate(expectedTargetDtos).then(() => {
                    toaster.pop('success', 'Records Saved Successfully');
                    ctrl.searchLocation(false);
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.saveAndLockExpectedTarget = () => {
            ctrl.expectedTargetForm.$setSubmitted();
            if (ctrl.isFormValid(MOTHER_REGISTERED) && ctrl.isFormValid(DELIVERY_REGISTERED) && ctrl.isFormValid(VACCINATION_1) && ctrl.isFormValid(VACCINATION_2)) {
                if (ctrl.totalMatched(MOTHER_REGISTERED) && ctrl.totalMatched(DELIVERY_REGISTERED) && ctrl.totalMatched(VACCINATION_1) && ctrl.totalMatched(VACCINATION_2)) {
                    let expectedTargetDtos = [];
                    ctrl.locationList.forEach(location => {
                        expectedTargetDtos.push({
                            locationId: location.id,
                            financialYear: ctrl.expectedTargetDto.financialYear,
                            Expected_Mthr_Reg: location.Expected_Mthr_Reg,
                            Expected_Del_Reg: location.Expected_Del_Reg,
                            ELA_DPT_OPV_Mes_VitA_1Dose: location.ELA_DPT_OPV_Mes_VitA_1Dose,
                            ELA_DPT_OPV_Mes_VitA_2Dose: location.ELA_DPT_OPV_Mes_VitA_2Dose,
                            state: ctrl.lockedState
                        });
                    });
                    Mask.show();
                    ExpectedTargetDAO.createOrUpdate(expectedTargetDtos).then(() => {
                        toaster.pop('success', 'Records Saved Successfully');
                        ctrl.searchLocation(false);
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                } else {
                    toaster.pop('error', 'Total should match with the given targets');
                }
            } else {
                toaster.pop('error', 'Please fill up all the values first');
            }
        }

        ctrl.saveStateTarget = (state) => {
            ctrl.targetDistributionForm.$setSubmitted();
            if (ctrl.targetDistributionForm.$valid) {
                let expectedTargetDtos = [];
                expectedTargetDtos.push({
                    locationId: ctrl.currentLocation.id, // always Gujarat (2)
                    financialYear: ctrl.expectedTargetDto.financialYear,
                    Expected_Mthr_Reg: ctrl.currentLocation.Expected_Mthr_Reg,
                    Expected_Del_Reg: ctrl.currentLocation.Expected_Del_Reg,
                    ELA_DPT_OPV_Mes_VitA_1Dose: ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_1Dose,
                    ELA_DPT_OPV_Mes_VitA_2Dose: ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_2Dose,
                    state
                });
                Mask.show();
                ExpectedTargetDAO.createOrUpdate(expectedTargetDtos).then(() => {
                    toaster.pop('success', 'Records Saved Successfully');
                    ctrl.searchLocation(false);
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.unlockStateTarget = () => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: () => {
                        return "Are you sure you want to unlock the data for editing?";
                    }
                }
            });
            modalInstance.result.then(() => {
                let unlockDtos = [];
                unlockDtos.push({
                    locationId: ctrl.currentLocation.id, // always Gujarat (2),
                    financialYear: ctrl.expectedTargetDto.financialYear,
                    state: ctrl.unlockedState
                });
                Mask.show();
                ExpectedTargetDAO.unlockLocations(unlockDtos).then(() => {
                    toaster.pop('success', 'Data unlocked successfully');
                    ctrl.searchLocation(false);
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            });
        }

        ctrl.unlockExpectedTarget = () => {
            let modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'med',
                resolve: {
                    message: () => {
                        return "Are you sure you want to unlock the data for editing?";
                    }
                }
            });
            modalInstance.result.then(() => {
                let unlockDtos = [];
                ctrl.locationList.forEach(location => {
                    unlockDtos.push({
                        locationId: location.id,
                        financialYear: ctrl.expectedTargetDto.financialYear,
                        state: ctrl.unlockedState
                    });
                });
                Mask.show();
                ExpectedTargetDAO.unlockLocations(unlockDtos).then(() => {
                    toaster.pop('success', 'Data unlocked successfully');
                    ctrl.searchLocation(false);
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            });
        }

        ctrl.resetFields = () => {
            ctrl.disableState = true;
            ctrl.disableCurrentLocationLevel = true;
        }
        
        ctrl.searchLocation = (toggleFilter) => {
            ctrl.resetFields();
            // Setting location hierarchy
            ctrl.locationHierarchy = "";
            for(let i=0; i<ctrl.selectedLocation.finalSelected.level; i++) {
                let currentLevel = ctrl.selectedLocation["level" + (i+1)];
                if(currentLevel == null){
                    break;
                }
                if(i == 0)
                    ctrl.locationHierarchy += currentLevel.name;
                else
                    ctrl.locationHierarchy += ">" + currentLevel.name;
            }

            ctrl.locationForm.$setSubmitted();
            if (ctrl.locationForm.$valid) {
                let promises = [];
                ctrl.selectedLocationLevel = ctrl.locationLevel;
                promises.push(LocationService.retrieveById(ctrl.selectedLocationIdFromFilter));
                promises.push(LocationService.retrieveNextLevelOfGivenLocationId(ctrl.selectedLocationIdFromFilter));
                promises.push(ExpectedTargetDAO.expectedTargetByLocationIdAndYear(ctrl.selectedLocationIdFromFilter, ctrl.expectedTargetDto.financialYear));
                Mask.show();
                $q.all(promises).then((responses) => {
                    ctrl.currentLocation = responses[0];
                    Object.assign(ctrl.currentLocation, {
                        Expected_Mthr_Reg: responses[2].Expected_Mthr_Reg || 0,
                        Expected_Del_Reg: responses[2].Expected_Del_Reg || 0,
                        ELA_DPT_OPV_Mes_VitA_1Dose: responses[2].ELA_DPT_OPV_Mes_VitA_1Dose || 0,
                        ELA_DPT_OPV_Mes_VitA_2Dose: responses[2].ELA_DPT_OPV_Mes_VitA_2Dose || 0,
                        state: responses[2].state || ctrl.draftState,
                        noOfTimesUnlocked: responses[2].noOfTimesUnlocked || 0
                    });
                    if (ctrl.selectedLocationLevel === 0 && (ctrl.currentLocation.state === ctrl.draftState || ctrl.currentLocation.state === ctrl.unlockedState)) {
                        ctrl.disableState = false;
                    }
                    ctrl.locationList = responses[1];
                    ExpectedTargetDAO.expectedTargetByLocationIdAndYearOfParent(ctrl.selectedLocationIdFromFilter, ctrl.expectedTargetDto.financialYear).then((expectedTargetDataList) => {
                        ctrl.locationList.forEach((location) => {
                            ctrl.expectedTarget = expectedTargetDataList.find(loc => loc.locationId === location.id);
                            if (ctrl.expectedTarget) {
                                location.Expected_Mthr_Reg = ctrl.expectedTarget.Expected_Mthr_Reg || 0;
                                location.Expected_Del_Reg = ctrl.expectedTarget.Expected_Del_Reg || 0;
                                location.ELA_DPT_OPV_Mes_VitA_1Dose = ctrl.expectedTarget.ELA_DPT_OPV_Mes_VitA_1Dose || 0;
                                location.ELA_DPT_OPV_Mes_VitA_2Dose = ctrl.expectedTarget.ELA_DPT_OPV_Mes_VitA_2Dose || 0;
                                location.state = ctrl.expectedTarget.state;
                                location.noOfTimesUnlocked = ctrl.expectedTarget.noOfTimesUnlocked;
                            } else {
                                location.Expected_Mthr_Reg = 0;
                                location.Expected_Del_Reg = 0;
                                location.ELA_DPT_OPV_Mes_VitA_1Dose = 0;
                                location.ELA_DPT_OPV_Mes_VitA_2Dose = 0;
                                location.state = ctrl.draftState;
                                location.noOfTimesUnlocked = 0;
                            }
                        });
                        ctrl.currentLocationLevelState = ctrl.locationList[0].state;
                        if (ctrl.currentLocation.state === ctrl.lockedState && (ctrl.currentLocationLevelState === ctrl.draftState || ctrl.currentLocationLevelState === ctrl.unlockedState)) {
                            ctrl.disableCurrentLocationLevel = false;
                        }
                        ctrl.calculateTotalForAllFields();
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    });
                    if (toggleFilter) {
                        ctrl.toggleFilter();
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.calculateTotal = (field) => {
            switch (field) {
                case MOTHER_REGISTERED:
                    ctrl.total.motherRegistered = 0;
                    ctrl.locationList.forEach(location => {
                        if (location.Expected_Mthr_Reg != null) {
                            ctrl.total.motherRegistered += location.Expected_Mthr_Reg;
                            ctrl.difference.motherRegistered = ctrl.currentLocation.Expected_Mthr_Reg - ctrl.total.motherRegistered;
                        }
                    });
                    break;
                case DELIVERY_REGISTERED:
                    ctrl.total.deliveryRegistered = 0;
                    ctrl.locationList.forEach(location => {
                        if (location.Expected_Del_Reg != null) {
                            ctrl.total.deliveryRegistered += location.Expected_Del_Reg;
                            ctrl.difference.deliveryRegistered = ctrl.currentLocation.Expected_Del_Reg - ctrl.total.deliveryRegistered;
                        }
                    });
                    break;
                case VACCINATION_1:
                    ctrl.total.vaccinationDose = 0;
                    ctrl.locationList.forEach(location => {
                        if (location.ELA_DPT_OPV_Mes_VitA_1Dose != null) {
                            ctrl.total.vaccinationDose += location.ELA_DPT_OPV_Mes_VitA_1Dose;
                            ctrl.difference.vaccinationDose = ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_1Dose - ctrl.total.vaccinationDose;
                        }
                    });
                    break;
                case VACCINATION_2:
                    ctrl.total.year2ndVaccinationDose = 0;
                    ctrl.locationList.forEach(location => {
                        if (location.ELA_DPT_OPV_Mes_VitA_2Dose != null) {
                            ctrl.total.year2ndVaccinationDose += location.ELA_DPT_OPV_Mes_VitA_2Dose;
                            ctrl.difference.year2ndVaccinationDose = ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_2Dose - ctrl.total.year2ndVaccinationDose;
                        }
                    });
                    break;
            }
        }

        ctrl.totalMatched = (field) => {
            switch (field) {
                case MOTHER_REGISTERED:
                    return ctrl.total.motherRegistered === ctrl.currentLocation.Expected_Mthr_Reg;
                case DELIVERY_REGISTERED:
                    return ctrl.total.deliveryRegistered === ctrl.currentLocation.Expected_Del_Reg;
                case VACCINATION_1:
                    return ctrl.total.vaccinationDose === ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_1Dose;
                case VACCINATION_2:
                    return ctrl.total.year2ndVaccinationDose === ctrl.currentLocation.ELA_DPT_OPV_Mes_VitA_2Dose;
            }
        }

        ctrl.isFormValid = (field) => {
            let isFormValid = true;
            ctrl.locationList.forEach(location => {
                switch (field) {
                    case MOTHER_REGISTERED:
                        if (location.Expected_Mthr_Reg == null) {
                            isFormValid = false;
                        }
                        break;
                    case DELIVERY_REGISTERED:
                        if (location.Expected_Del_Reg == null) {
                            isFormValid = false;
                        }
                        break;
                    case VACCINATION_1:
                        if (location.ELA_DPT_OPV_Mes_VitA_1Dose == null) {
                            isFormValid = false;
                        }
                        break;
                    case VACCINATION_2:
                        if (location.ELA_DPT_OPV_Mes_VitA_2Dose == null) {
                            isFormValid = false;
                        }
                        break;
                }
            });
            return isFormValid;
        }

        ctrl.calculateTotalForAllFields = () => {
            ctrl.calculateTotal(MOTHER_REGISTERED);
            ctrl.calculateTotal(DELIVERY_REGISTERED);
            ctrl.calculateTotal(VACCINATION_1);
            ctrl.calculateTotal(VACCINATION_2);
        }

        ctrl.printReport = () => {
            ctrl.isPrintClicked = true;
            ctrl.footer = "Generated by " + ctrl.loggedInUser.name + " at " + new Date().toLocaleString();
            let header = `<h2>Location Wise Expected Target</h2><center><strong>Location :</strong> ${ctrl.locationHierarchy} <strong>Financial Year :</strong> ${ctrl.expectedTargetDto.financialYear}</center>`;
            $("thead tr th").css("position", "inherit");
            $('#printableDiv').printThis({
                importCSS: false,
                loadCSS: 'styles/css/printable.css',
                header: header,
                base: "./",
                printDelay: 0,
                pageTitle: ctrl.appName,
                afterPrint: function () {
                    $scope.$apply(function () {
                        ctrl.isPrintClicked = false;
                    })
                }
            })
        }

        ctrl.saveExcel = () => {
            Mask.show();
            let parametersList = [];
            let locationListExcel = [];
            parametersList.push({
                "Location Name": "Location",
                "No Of Mother Registered": "Financial Year",
                "No Of Expected Delivery Registered": "",
                "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": "",
                "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": ""
            }, {
                "Location Name": ctrl.locationHierarchy,
                "No Of Mother Registered": ctrl.expectedTargetDto.financialYear,
                "No Of Expected Delivery Registered": "",
                "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": "",
                "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": ""
            }, {
                "Location Name": "",
                "No Of Mother Registered": "",
                "No Of Expected Delivery Registered": "",
                "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": "",
                "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": ""
            });
            locationListExcel.push({
                "Location Name": "Location Name",
                "No Of Mother Registered": "No Of Mother Registered",
                "No Of Expected Delivery Registered": "No Of Expected Delivery Registered",
                "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)",
                "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)"
            });
            for (let index = 0; index < ctrl.locationList.length; index++) {
                let excelObj = {
                    "Location Name": ctrl.locationList[index].name,
                    "No Of Mother Registered": ctrl.locationList[index].Expected_Mthr_Reg,
                    "No Of Expected Delivery Registered": ctrl.locationList[index].Expected_Del_Reg,
                    "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": ctrl.locationList[index].ELA_DPT_OPV_Mes_VitA_1Dose,
                    "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": ctrl.locationList[index].ELA_DPT_OPV_Mes_VitA_2Dose
                }
                locationListExcel.push(excelObj);
            }
            locationListExcel.push({
                "Location Name": "Total",
                "No Of Mother Registered": ctrl.total.motherRegistered,
                "No Of Expected Delivery Registered": ctrl.total.deliveryRegistered,
                "No Of expected vaccination dose(ELA,DPT,OPV,Measles,Vitamin A)": ctrl.total.vaccinationDose,
                "2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose)": ctrl.total.year2ndVaccinationDose
            });
            let mystyle = {
                headers: false,
                column: { style: { Font: { Bold: "1" } } }
            };
            let excelList = [...parametersList, ...locationListExcel];
            alasql('SELECT * INTO XLSX("' + "Location Wise Expected Target" + '",?) FROM ?', [mystyle, excelList]);
            Mask.hide();
        }

        ctrl.toggleFilter = () => {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('ExpectedTargetController', ExpectedTargetController);
})();
