(function () {
    function OutPatientTreatment($stateParams, Mask, QueryDAO, toaster, $state, AuthenticateService, GeneralUtil, outPatientTreatmentConstants, status, OutPatientService, $uibModal,  $filter, NdhmHipDAO) {
        var outPatientTreatment = this;
        outPatientTreatment.constants = outPatientTreatmentConstants;
        outPatientTreatment.status = status;
        outPatientTreatment.medicineDetails = [];
        outPatientTreatment.today = moment();
        outPatientTreatment.minServiceDate = moment().subtract(2, 'days');
        outPatientTreatment.isTreatmentForm = false;
        outPatientTreatment.advisedLabTests = false;
        outPatientTreatment.edlMap = {};
        outPatientTreatment.resultOfLabTestAndCategory = [];
        outPatientTreatment.labTestsList = [];
        outPatientTreatment.showOtherStateSpecificDisease = false;
        outPatientTreatment.showUnusualSyndromes = false;
        outPatientTreatment.provisionalDiagnosisMap = {};
        outPatientTreatment.alreadySelectedDrugList = [];

        outPatientTreatment.init = () => {
            if ($state.current.name === 'techo.manage.optTreatmentForm') {
                outPatientTreatment.isTreatmentForm = true;
            }
            Mask.show();
            AuthenticateService.getLoggedInUser().then(user => {
                outPatientTreatment.loggedInUser = user.data;
                return QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: outPatientTreatment.loggedInUser.id
                    }
                });
            }).then((response) => {
                outPatientTreatment.healthInfrastructuresList = response.result;
                setTimeout(function () {
                    $('#healthInfrastructure').trigger("chosen:updated");
                });
                let dto;
                if (outPatientTreatment.isTreatmentForm) {
                    dto = {
                        code: 'opd_search_by_opd_member_registration_id',
                        parameters: {
                            opdMemberRegistrationId: Number($stateParams.id),
                            userId: outPatientTreatment.loggedInUser.id
                        }
                    }
                } else {
                    dto = {
                        code: 'opd_search_by_opd_member_master_id',
                        parameters: {
                            opdMemberMasterId: Number($stateParams.id),
                            userId: outPatientTreatment.loggedInUser.id
                        }
                    }
                }
                return QueryDAO.execute(dto);
            }).then((response) => {
                if (response.result.length > 0) {
                    outPatientTreatment.outPatientObject = response.result[0];
                    if (outPatientTreatment.isTreatmentForm) {
                        outPatientTreatment.outPatientObject.serviceDate = moment();
                        if (outPatientTreatment.outPatientObject.healthInfraId) {
                            outPatientTreatment.outPatientObject.healthInfrastructure = {
                                id: outPatientTreatment.outPatientObject.healthInfraId,
                                name: outPatientTreatment.outPatientObject.healthInfraName,
                                type: outPatientTreatment.outPatientObject.healthInfraType
                            }
                        }
                    } else {
                        try {
                            outPatientTreatment.medicineDetails = outPatientTreatment.outPatientObject.opdEdlDetails ? JSON.parse(outPatientTreatment.outPatientObject.opdEdlDetails) : [];
                        } catch (error) {
                            outPatientTreatment.medicineDetails = [];
                            console.error('Error while parsing JSON outPatientTreatment.outPatientObject.opdEdlDetails ::: ', outPatientTreatment.outPatientObject.opdEdlDetails);
                        }
                        if (outPatientTreatment.outPatientObject.medicinesGivenOn) {
                            outPatientTreatment.medicinesGivenOnAvailable = true;
                        } else {
                            outPatientTreatment.medicinesGivenOnAvailable = false;
                        }
                    }
                    if (outPatientTreatment.outPatientObject.lastDeliveryDate) {
                        outPatientTreatment.gestationWeekNumber = moment().diff(moment(outPatientTreatment.outPatientObject.lastDeliveryDate), 'week')
                    }
                    let dtoList = [];
                    let fetchOpdProvisionalDiagnosisDto = {
                        code: 'fetch_listvalue_detail_from_field',
                        parameters: {
                            field: 'opdProvisionalDiagnosis'
                        },
                        sequence: 1
                    }
                    dtoList.push(fetchOpdProvisionalDiagnosisDto);
                    const alreadySelectedEdlIds = outPatientTreatment.medicineDetails.map(e => e.edlId)
                    if (alreadySelectedEdlIds.length) {
                        let fetchOpdEssentialDrugsDto = {
                            code: 'fetch_listvalue_detail_by_ids',
                            parameters: {
                                ids: alreadySelectedEdlIds
                            },
                            sequence: 2
                        }
                        dtoList.push(fetchOpdEssentialDrugsDto);
                    }
                    return QueryDAO.executeAll(dtoList);
                } else {
                    return Promise.reject({ data: { message: 'No member found.' } });
                }
            }).then((responses) => {
                outPatientTreatment.provisionalDiagnosisList = responses[0].result;
                outPatientTreatment.provisionalDiagnosisList.forEach(e => outPatientTreatment.provisionalDiagnosisMap[e.id] = e.value);
                if (responses[1]) {
                    outPatientTreatment.alreadySelectedDrugList = responses[1].result;
                    outPatientTreatment.alreadySelectedDrugList.forEach(e => outPatientTreatment.edlMap[e.id] = e.value);
                }
                setTimeout(function () {
                    $('#provisionalDiagnosis').trigger("chosen:updated");
                });
            }).catch((error) => {
                $state.go("techo.manage.outPatientTreatmentSearch");
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
            // Get preferred PHR Address and other PHR Address assign to member
            Mask.show();
            NdhmHipDAO.getCareContextTokenByRequestId(parseInt($stateParams.id)).then((res) => {
                outPatientTreatment.careContextRequest = res;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(Mask.hide);
        };

        outPatientTreatment.healthInfrastructureChanged = () => {
            outPatientTreatment.categoriesList = [];
            if (outPatientTreatment.outPatientObject.healthInfrastructure != null) {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_opd_lab_tests_and_category_by_health_infrastructure',
                    parameters: {
                        healthInfrastructureId: outPatientTreatment.outPatientObject.healthInfrastructure.id,
                        healthInfrastructureType: outPatientTreatment.outPatientObject.healthInfrastructure.type,
                        type: 'OPD_LAB_TEST'
                    }
                }).then((response) => {
                    if (response.result.length > 0) {
                        outPatientTreatment.resultOfLabTestAndCategory = response.result;
                        let categoryList = response.result.map(x => {
                            return {
                                categoryId: x.categoryId,
                                categoryName: x.categoryName
                            }
                        })
                        var result = categoryList.reduce((unique, o) => {
                            if (!unique.some(obj => obj.categoryId === o.categoryId && obj.categoryName === o.categoryName)) {
                                unique.push(o);
                            }
                            return unique;
                        }, []);
                        outPatientTreatment.categoriesList = result;
                        setTimeout(function () {
                            $('#categories').trigger("chosen:updated");
                        });
                    } else {
                        return Promise.reject({ data: { message: 'This HealthInfrastructure Type is not configured to perform any Laboratory Tests.' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                })
            }
            outPatientTreatment.outPatientObject.categories = null;
            outPatientTreatment.outPatientObject.labTests = null;
            setTimeout(function () {
                $('#categories').trigger("chosen:updated");
            });
        }

        outPatientTreatment.onChangeProvisionalDiagnosis = () => {
            outPatientTreatment.showOtherStateSpecificDisease = false;
            outPatientTreatment.showUnusualSyndromes = false;
            if (!outPatientTreatment.outPatientObject.provisionalIds || outPatientTreatment.outPatientObject.provisionalIds.length === 0) {
                return;
            }
            outPatientTreatment.outPatientObject.provisionalIds.forEach(e => {
                if (outPatientTreatment.provisionalDiagnosisMap[e] === 'Any other State-Specific Disease (Specify)') {
                    outPatientTreatment.showOtherStateSpecificDisease = true;
                } else if (outPatientTreatment.provisionalDiagnosisMap[e] === 'Unusual Syndromes NOT Captured Above') {
                    outPatientTreatment.showUnusualSyndromes = true;
                }
            });
        }

        outPatientTreatment.categoriesChanged = () => {
            outPatientTreatment.labTestsList = []
            if (outPatientTreatment.outPatientObject.categories && outPatientTreatment.outPatientObject.categories.length > 0) {
                outPatientTreatment.outPatientObject.categories.forEach(category => {
                    let labList = outPatientTreatment.resultOfLabTestAndCategory.filter(x => {
                        return x.categoryId === category
                    })
                    outPatientTreatment.labTestsList = [...outPatientTreatment.labTestsList, ...labList]
                })
            } else {
                outPatientTreatment.labTestsList = [];
            }
            outPatientTreatment.outPatientObject.labTests = null;
            setTimeout(function () {
                $('#labTests').trigger("chosen:updated");
            });
        }

        outPatientTreatment.drugsSelectizeConfig = {
            create: false,
            valueField: 'id',
            labelField: 'value',
            dropdownParent: 'body',
            highlight: true,
            searchField: ['_searchField'],
            maxItems: 1,
            render: {
                item: function (drug, escape) {
                    var returnString = "<div>" + drug.value + "</div>";
                    return returnString;
                },
                option: function (drug, escape) {
                    var returnString = "<div>" + drug.value + "</div>";
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
                    code: 'fetch_listvalue_detail_from_field_on_debounce',
                    parameters: {
                        searchString: query,
                        field: 'opdEssentialDrugs'
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
            },
            onItemAdd: function (value, $item) {
                outPatientTreatment.edlMap[value] = $item[0].textContent;
            }
        }

        outPatientTreatment.saveScreening = () => {
            outPatientTreatment.outPatientTreatmentForm.$setSubmitted();
            if (outPatientTreatment.outPatientTreatmentForm.$valid && outPatientTreatment.validateForm()) {
                if (outPatientTreatment.isTreatmentForm) {
                    let payload = {
                        memberId: outPatientTreatment.outPatientObject.memberId,
                        opdMemberRegistrationId: $stateParams.id,
                        serviceDate: outPatientTreatment.outPatientObject.serviceDate,
                        medicinesGivenOn: outPatientTreatment.outPatientObject.medicinesGivenOn,
                        instructions: outPatientTreatment.outPatientObject.instructions,
                        isMedicinesGiven: false,
                        healthInfrastructureId: outPatientTreatment.outPatientObject.healthInfrastructure.id,
                        provisionalIds: outPatientTreatment.outPatientObject.provisionalIds,
                        labTestIds: outPatientTreatment.outPatientObject.labTestIds || [],
                        status: outPatientTreatment.status.pending,
                        opdMedicineDetails: outPatientTreatment.medicineDetails,
                        anyOtherStateSpecificDisease: outPatientTreatment.showOtherStateSpecificDisease ? outPatientTreatment.outPatientObject.otherStateSpecificDisease : null,
                        unusualSyndromes: outPatientTreatment.showUnusualSyndromes ? outPatientTreatment.outPatientObject.unusualSyndromes : null,
                        locationId: outPatientTreatment.outPatientObject.locationId
                    }
                    Mask.show();
                    OutPatientService.create(payload).then(() => {
                        toaster.pop("success", "Form submitted successfully");
                        if(outPatientTreatment.careContextRequest.requestId){
                            outPatientTreatment.handleNdhmConsentRequest();
                        } else{
                            $state.go("techo.manage.outPatientTreatmentSearch");
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(Mask.hide);
                } else {
                    outPatientTreatment.medicineDetails.forEach(e => {
                        if (e.createdOn) e.createdOn = moment(e.createdOn);
                    });
                    Mask.show();
                    OutPatientService.update({
                        opdMemberMasterId: $stateParams.id,
                        isMedicinesGiven: true,
                        opdMedicineDetails: outPatientTreatment.medicineDetails,
                        medicinesGivenOn: outPatientTreatment.outPatientObject.medicinesGivenOn
                    }).then(() => {
                        toaster.pop("success", "Form submitted successfully");
                        $state.go("techo.manage.outPatientTreatmentSearch");
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(Mask.hide);
                }
            }
        }

        /**
         * Generate consent request for OPD
         */
        outPatientTreatment.handleNdhmConsentRequest = function () {
            Mask.show();
            OutPatientService.getRchOpdMemberRegistration(parseInt($stateParams.id)).then((res) => {
                let memberObj = {
                    memberId: outPatientTreatment.outPatientObject.memberId,
                    mobileNumber: outPatientTreatment.outPatientObject.mobileNumber,
                    name: outPatientTreatment.outPatientObject.name
                }
                outPatientTreatment.registrationDate = res.registrationDate;
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/manage/ndhm/hip/views/ndhm.consent.request.modal.html',
                    controller: 'ndhmConsentRequestModalController',
                    controllerAs: 'ctrl',
                    windowClass: 'cst-modal',
                    backdrop: 'static',
                    size: 'med',
                    resolve: {
                        dataForConsentRequest: () => {
                            return {
                                title: "Link OPD Record To ABHA Address",
                                consentRecord: "(OPD "+ $filter('date')(outPatientTreatment.registrationDate, "dd-MM-yyyy")+")",
                                isTokenGenerate: false,
                                memberObj: memberObj,
                                serviceId: $stateParams.id,
                                serviceType: "OPD",
                                requestId: outPatientTreatment.careContextRequest.requestId,
                                healthId: outPatientTreatment.careContextRequest.healthId
                            }
                        }
                    }
                });
                modalInstance.result.then(function(data) {
                    $state.go("techo.manage.outPatientTreatmentSearch");
                    outPatientTreatment.disableOpdForm = true;
                    outPatientTreatment.isDataLinked = data.isCareContextAdded;
                    outPatientTreatment.healthId = data.healthId;
                }, () => {
                });
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(Mask.hide);
        }

        outPatientTreatment.addMedicineDetail = function () {
            outPatientTreatment.medicineDetails.push({
                id: null,
                memberId: outPatientTreatment.outPatientObject.memberId,
                opdMemberMasterId: outPatientTreatment.isTreatmentForm ? null : $stateParams.id,
                edlId: null,
                frequency: null,
                quantityBeforeFood: null,
                quantityAfterFood: null,
                numberOfDays: null
            });
        }

        outPatientTreatment.removeMedicineDetail = function (index) {
            outPatientTreatment.medicineDetails.splice(index, 1);
        }

        outPatientTreatment.showTreatmentHistory = (toShowAllTreatments) => {
            let modalInstance = $uibModal.open({
                windowClass: 'cst-modal',
                backdrop: 'static',
                size: 'xl',
                templateUrl: 'app/manage/facilitydataentry/outpatienttreatment/views/treatment-history.modal.html',
                controllerAs: 'ctrl',
                controller: function ($uibModalInstance) {
                    let ctrl = this;
                    const _init = function () {
                        let dtoList = [];
                        let getTreatmentHistory = {
                            code: 'opd_member_treatment_history',
                            parameters: {
                                uniqueHealthId: outPatientTreatment.outPatientObject.uniqueHealthId,
                                limit: toShowAllTreatments ? null : 1,
                                offset: 0
                            },
                            sequence: 1
                        }
                        dtoList.push(getTreatmentHistory);
                        Mask.show();
                        QueryDAO.executeAll(dtoList).then(response => {
                            ctrl.treatmentHistory = response[0].result;
                            ctrl.treatmentHistory.forEach(treatment => {
                                try {
                                    treatment.parsedOpdEdlDetails = treatment.opdEdlDetails ? JSON.parse(treatment.opdEdlDetails) : [];
                                } catch (error) {
                                    treatment.parsedOpdEdlDetails = [];
                                    console.error('Error while parsing JSON treatment.opdEdlDetails ::: ', treatment.opdEdlDetails);
                                }
                                try {
                                    treatment.parsedLabTestResults = treatment.labTestResults ? JSON.parse(treatment.labTestResults) : [];
                                } catch (error) {
                                    treatment.parsedLabTestResults = [];
                                    console.error('Error while parsing JSON treatment.labTestResults ::: ', treatment.labTestResults);
                                }
                                treatment.parsedLabTestResults.forEach(labTest => {
                                    labTest.preparedResult = [];
                                    if (labTest.result) {
                                        try {
                                            labTest.parsedFormConfig = labTest.formConfigJson ? JSON.parse(labTest.formConfigJson) : [];
                                        } catch (error) {
                                            labTest.parsedFormConfig = [];
                                            console.error('Error while parsing JSON labTest.formConfigJson ::: ', labTest.formConfigJson);
                                        }
                                        if (labTest.parsedFormConfig && labTest.parsedFormConfig.length) {
                                            let parsedFormConfigMap = {};
                                            labTest.parsedFormConfig.forEach(config => {
                                                if (['RB', 'CB', 'MS'].includes(config.type)) {
                                                    let optionsMap = {};
                                                    config.options.forEach(option => optionsMap[option.key] = option.value);
                                                    parsedFormConfigMap[config.id] = {
                                                        question: config.question,
                                                        isOptionsTypeField: true,
                                                        optionsMap: optionsMap
                                                    }
                                                } else {
                                                    parsedFormConfigMap[config.id] = {
                                                        question: config.question,
                                                        isOptionsTypeField: false
                                                    }
                                                }
                                            });
                                            try {
                                                labTest.parsedResult = labTest.result ? JSON.parse(labTest.result) : [];
                                            } catch (error) {
                                                labTest.parsedResult = [];
                                                console.error('Error while parsing JSON labTest.result ::: ', labTest.result);
                                            }
                                            for (let key in labTest.parsedResult) {
                                                let parsedKey = Number(key);
                                                if (labTest.parsedResult.hasOwnProperty(key) && parsedKey > 0 && parsedFormConfigMap[parsedKey]) {
                                                    if (parsedFormConfigMap[parsedKey].isOptionsTypeField) {
                                                        labTest.preparedResult.push({
                                                            question: parsedFormConfigMap[parsedKey].question,
                                                            answer: parsedFormConfigMap[parsedKey].optionsMap[labTest.parsedResult[key]]
                                                        })
                                                    } else {
                                                        labTest.preparedResult.push({
                                                            question: parsedFormConfigMap[parsedKey].question,
                                                            answer: labTest.parsedResult[key]
                                                        })
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            });
                        }, GeneralUtil.showMessageOnApiCallFailure)
                            .finally(Mask.hide);
                    }

                    ctrl.ok = function () {
                        $uibModalInstance.close();
                    }
                    ctrl.cancel = function () {
                        $uibModalInstance.dismiss();
                    }

                    _init();
                },
                resolve: {}
            });
            modalInstance.result.then(() => { }, () => { })
        }

        outPatientTreatment.validateForm = () => {
            let selectedLabTests = [];
            let selectedLabTestsCategories = [];
            let allCategoriesIncluded;
            if (Array.isArray(outPatientTreatment.outPatientObject.categories)
                && outPatientTreatment.outPatientObject.categories.length
                && Array.isArray(outPatientTreatment.outPatientObject.labTestIds)
                && outPatientTreatment.outPatientObject.labTestIds.length) {
                selectedLabTests = outPatientTreatment.resultOfLabTestAndCategory.filter(labtest => outPatientTreatment.outPatientObject.labTestIds.includes(labtest.labTestId));
                selectedLabTestsCategories = [...new Set(selectedLabTests.map(labtest => labtest.categoryId))];
                allCategoriesIncluded = JSON.stringify([...outPatientTreatment.outPatientObject.categories].sort()) === JSON.stringify([...selectedLabTestsCategories].sort());
            } else {
                allCategoriesIncluded = true;
            }
            if (!allCategoriesIncluded) {
                toaster.pop('error', 'Please select atleast one labtest from each category selected');
                return false;
            } else {
                return true;
            }
        }

        outPatientTreatment.goBack = () => $state.go("techo.manage.outPatientTreatmentSearch");

        outPatientTreatment.init();
    }
    angular.module('imtecho.controllers').controller('OutPatientTreatment', OutPatientTreatment);
})();
