/* global moment, toster */

(function () {
    function ManagePncController(ManagePncDAO, QueryDAO, Mask, toaster, AnganwadiService, $state, GeneralUtil, AuthenticateService, $q) {
        var ctrl = this;
        const FEATURE = 'techo.manage.pncSearch';
        const PNC_FORM_CONFIGURATION_KEY = 'RCH_FACILITY_PNC';
        ctrl.formData = {};
        ctrl.formData.childDetails = [];
        ctrl.formUuids = {};
        ctrl.today = moment().startOf('day');

        ctrl.init = () => {
            ctrl.formData.motherDetails = {};
            ctrl.formData.mobileStartDate = moment();
            Mask.show();
            AnganwadiService.searchMembers($state.params.id).then((response) => {
                ctrl.formData.uniqueHealthId = response.uniqueHealthId;
                ctrl.formData.isMobileNumberAvailable = response.mobileNumber ? true : false;
                ctrl.formData.memberId = response.id;
                ctrl.formData.motherDetails.motherId = response.id;
                ctrl.formData.motherDetails.familyPlanningMethod = response.lastMethodOfContraception;
                ctrl.formData.familyId = response.fid;
                ctrl.formData.familyUniqueId = response.familyId;
                ctrl.formData.locationId = response.areaId ? response.locationId : response.areaId;
                ctrl.formData.areaId = response.areaId ? response.areaId : null;
                ctrl.formData.memberStatus = "AVAILABLE";
                ctrl.formData.isFromWeb = true;
                ctrl.formData.memberName = `${response.firstName} ${response.middleName} ${response.lastName}`;
                ctrl.formData.locationHierarchy = response.locationHierarchy;
                ctrl.formData.bpl = response.bplFlag;
                ctrl.formData.caste = response.caste;
                ctrl.formData.additionalInfo = JSON.parse(response.additionalInfo);
                ctrl.formData.motherDetails.dateOfDelivery = response.lastDeliveryDate;
                if (ctrl.formData.additionalInfo && ctrl.formData.additionalInfo.lastServiceLongDate) {
                    ctrl.minDeathDate = moment(ctrl.formData.additionalInfo.lastServiceLongDate);
                    if (moment(ctrl.formData.motherDetails.dateOfDelivery).isSame(moment(ctrl.formData.additionalInfo.lastServiceLongDate).format("YYYY-MM-DD"))) {
                        ctrl.minServiceDate = moment(ctrl.formData.additionalInfo.lastServiceLongDate);
                    } else {
                        ctrl.minServiceDate = moment(ctrl.formData.additionalInfo.lastServiceLongDate).add(1, 'days');
                        if (moment(ctrl.formData.additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                            return Promise.reject({ data: { message: 'PNC Visit for this member has already been done today.' } });
                        }
                    }
                } else {
                    ctrl.minServiceDate = ctrl.formData.motherDetails.dateOfDelivery
                    ctrl.minDeathDate = ctrl.minServiceDate;
                }
                if (ctrl.minServiceDate && ctrl.minServiceDate < (moment().subtract(15, 'days'))) {
                    ctrl.minServiceDate = moment().subtract(15, 'days')
                }
                ctrl.formData.benefits = [];
                if (response.isChiranjeeviYojnaBeneficiary) {
                    ctrl.formData.benefits.push('Chiranjeevi Yojna');
                }
                if (response.isIayBeneficiary) {
                    ctrl.formData.benefits.push('IAY');
                }
                if (response.isJsyBeneficiary) {
                    ctrl.formData.benefits.push("JSY");
                }
                if (response.isKpsyBeneficiary) {
                    ctrl.formData.benefits.push("KPSY");
                }
                if (ctrl.formData.benefits.length === 0) {
                    ctrl.formData.benefits.push("None");
                }
                ctrl.formData.benefits = ctrl.formData.benefits.join();
                let dtoList = [];
                if (response.locationId) {
                    dtoList.push({
                        code: 'retrieve_worker_info_by_location_id',
                        parameters: {
                            locationId: response.locationId
                        },
                        sequence: 1
                    });
                }
                if (response.areaId) {
                    dtoList.push({
                        code: 'retrieve_worker_info_by_location_id',
                        parameters: {
                            locationId: Number(response.areaId)
                        },
                        sequence: 2
                    });
                }
                return QueryDAO.executeAll(dtoList);
            }).then((response) => {
                if (response[0] != null && response[0].result[0].workerDetails != null) {
                    let fhwJson = JSON.parse(response[0].result[0].workerDetails);
                    ctrl.formData.anmInfo = `${fhwJson[0].name} (${fhwJson[0].mobileNumber})`;
                }
                if (response[1] != null && response[1].result[0].workerDetails != null) {
                    let ashaJson = JSON.parse(response[1].result[0].workerDetails);
                    ctrl.formData.ashaInfo = `${ashaJson[0].name} (${ashaJson[0].mobileNumber})`;
                }
                return QueryDAO.execute({
                    code: 'pnc_retrieve_childs_by_member_id',
                    parameters: {
                        memberId: Number($state.params.id)
                    }
                });
            }).then((response) => {
                ctrl.formData.childDetails = response.result;
                ctrl.formData.childDetails.forEach((child) => {
                    child.immunisation = {};
                    child.immunisationGiven = child.immunisation_given ? child.immunisation_given : "";
                    child.childId = child.id
                    child.memberStatus = 'AVAILABLE';
                });
                ctrl.getDueImmunisation();
                ctrl.calculatePncVisitNo();
                let promiseList = [];
                promiseList.push(AuthenticateService.getLoggedInUser());
                promiseList.push(AuthenticateService.getAssignedFeature(FEATURE));
                return $q.all(promiseList);
            }).then((response) => {
                ctrl.loggedInUser = response[0].data;
                ctrl.formConfigurations = response[1].systemConstraintConfigs[PNC_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response[1].webTemplateConfigs[PNC_FORM_CONFIGURATION_KEY];
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go('techo.manage.pncSearch');
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.getDueImmunisation = () => {
            if (Array.isArray(ctrl.formData.childDetails) && ctrl.formData.childDetails.length) {
                ctrl.formData.childDetails.forEach((child) => {
                    Mask.show();
                    ManagePncDAO.getDueImmunisationsForChild(moment(child.dob).valueOf(), child.immunisationGiven).then((response) => {
                        child.dueImmunisations = response;
                        child.dueImmunisations.forEach((immu) => {
                            child.immunisation[immu] = {};
                        });
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                });
            }
        }

        ctrl.calculatePncVisitNo = () => {
            if (ctrl.formData.motherDetails.dateOfDelivery != null) {
                let dateDiff = moment().diff(moment(ctrl.formData.motherDetails.dateOfDelivery), 'day');
                if (dateDiff < 7) {
                    ctrl.formData.pncNo = "1";
                } else if (dateDiff < 14) {
                    ctrl.formData.pncNo = "2";
                } else if (dateDiff < 21) {
                    ctrl.formData.pncNo = "3";
                } else if (dateDiff < 28) {
                    ctrl.formData.pncNo = "4";
                } else if (dateDiff < 42) {
                    ctrl.formData.pncNo = "5";
                } else if (dateDiff < 61) {
                    ctrl.formData.pncNo = "6";
                }
            }
        }

        ctrl.serviceDateChanged = () => {
            ctrl.formData.motherDetails.deathDate = null;
            if (Array.isArray(ctrl.formData.childDetails) && ctrl.formData.childDetails.length) {
                ctrl.formData.childDetails.map(child => child.deathDate = null);
            }
            ctrl.formData.motherDetails.ironDefAnemiaInjDueDate = null
            ctrl.minInjDate = moment(ctrl.formData.serviceDate).add(1, 'days');
            ctrl.maxInjDate = moment(ctrl.formData.serviceDate).add(30, 'days');
        }

        ctrl.deliveryPlaceChanged = () => {
            ctrl.formData.hospitalByUserList.data = [];
            ctrl.formData.hospitalByTypeList.data = [];
            ctrl.formData.hospitalByType = null;
            ctrl.formData.hospitalByUser = null;
            ctrl.formData.institutionType = null;
            if (ctrl.formData.deliveryPlace === 'THISHOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: ctrl.loggedInUser.id
                    }
                }).then((response) => {
                    ctrl.formData.hospitalByUserList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    if (ctrl.formData.hospitalByUserList.data.length == 1) {
                        ctrl.formData.hospitalByUser = ctrl.formData.hospitalByUserList.data[0].key;
                    } else if (ctrl.formData.hospitalByUserList.data.length == 0) {
                        return Promise.reject({ data: { message: 'No health infrastructure assigned' } });
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.retrieveHospitalsByInstitutionType = () => {
            ctrl.formData.hospitalByUserList.data = [];
            ctrl.formData.hospitalByTypeList.data = [];
            ctrl.formData.hospitalByType = null;
            ctrl.formData.hospitalByUser = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.institutionType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.hospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.isMotherAliveChanged = () => {
            ctrl.formData.motherDetails.deathDate = null;
            ctrl.formData.motherDetails.placeOfDeath = null;
            ctrl.formData.motherDetails.deathInfrastructureType = null;
            ctrl.formData.motherDetails.deathHospitalByType = null;
            ctrl.formData.motherDetails.deathReason = null;
            ctrl.formData.motherDetails.otherDeathReason = null;
            ctrl.formData.motherDetails.isIfaGiven = null;
            ctrl.formData.motherDetails.ifaTabletsGiven = null;
            ctrl.formData.motherDetails.isCalciumGiven = null;
            ctrl.formData.motherDetails.calciumGiven = null;
            ctrl.formData.motherDetails.motherDangerSigns = null;
            ctrl.formData.motherDetails.otherDangerSign = null;
            ctrl.formData.motherDetails.motherReferralDone = null;
            ctrl.formData.motherDetails.referralInfraId = null;
            ctrl.formData.motherDetails.familyPlanningMethod = null;
            ctrl.formData.motherDetails.fpInsertOperateDate = null;
            ctrl.formData.motherDetails.bloodTransfusion = null;
            ctrl.formData.motherDetails.ironDefAnemiaInj = null;
            ctrl.formData.motherDetails.ironDefAnemiaInjDueDate = null;
        }

        ctrl.placeOfDeathChanged = () => {
            ctrl.formData.motherDetails.deathInfrastructureType = null;
            ctrl.formData.motherDetails.deathHospitalByType = null;
        }

        ctrl.retrieveDeathHospitalsByInstitutionType = () => {
            ctrl.formData.motherDetails.deathHospitalByTypeList.data = [];
            ctrl.formData.motherDetails.deathHospitalByType = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.motherDetails.deathInfrastructureType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.motherDetails.deathHospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.motherDeathReasonChanged = () => ctrl.formData.motherDetails.otherDeathReason = null;

        ctrl.ifaGivenChanged = () => ctrl.formData.motherDetails.ifaTabletsGiven = null;

        ctrl.calciumGivenChanged = () => ctrl.formData.motherDetails.calciumGiven = null;

        ctrl.motherDangerSignsChanged = () => {
            ctrl.formData.motherDetails.otherDangerSign = null;
            if (ctrl.formData.motherDetails.motherDangerSigns.length === 0) {
                ctrl.formData.motherDetails.motherReferralDone = null;
                ctrl.motherReferralDoneChanged();
            }

        }

        ctrl.motherReferralDoneChanged = () => {
            ctrl.formData.motherDetails.motherReferralInfraType = null;
            ctrl.formData.motherDetails.motherReferralHospitalByType = null;
        }

        ctrl.retrieveMotherReferralHospitalsByInstitutionType = () => {
            ctrl.formData.motherDetails.motherReferralHospitalByTypeList.data = [];
            ctrl.formData.motherDetails.motherReferralHospitalByType = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.motherDetails.motherReferralInfraType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.motherDetails.motherReferralHospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.familyPlanningMethodChanged = () => ctrl.formData.motherDetails.fpInsertOperateDate = null;

        ctrl.ironDefAnemiaInjChanged = () => ctrl.formData.motherDetails.ironDefAnemiaInjDueDate = null;

        ctrl.isChildAliveChanged = (iteratorIndicesMap) => {
            let child = ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']];
            child.deathDate = null;
            child.placeOfDeath = null;
            child.deathReason = null;
            child.otherDeathReason = null;
            child.childDangerSigns = null;
            child.otherDangerSign = null;
            child.childReferralDone = null;
            child.childWeight = null;
            Object.keys(child.immunisation).forEach((key) => {
                child.immunisation[key].given = null;
                child.immunisation[key].date = null;
            });
        }

        ctrl.childDeathReasonChanged = (iteratorIndicesMap) => ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].otherDeathReason = null;

        ctrl.childDangerSignsChanged = (iteratorIndicesMap) => {
            let child = ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']];
            child.otherDangerSign = null;
            if (child.childDangerSigns.length === 0) {
                child.childReferralDone = null;
                ctrl.childReferralDoneChanged(iteratorIndicesMap);
            }
        }

        ctrl.childReferralDoneChanged = (iteratorIndicesMap) => {
            ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralInfraType = null;
            ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralHospitalByType = null;
        }

        ctrl.retrieveChildReferralHospitalsByInstitutionType = (iteratorIndicesMap) => {
            ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralHospitalByTypeList.data = [];
            ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralHospitalByType = null;
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralInfraType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].childReferralHospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
                    // var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(ctrl.otherInstitutes);
                    // ctrl.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
                } else {
                    return Promise.reject({ data: { message: 'No health infrastructure found for the selected type' } });
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.hepatitisB0GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'HEPATITIS_B_0');

        ctrl.hepatitisB0DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}HEPATITIS_B_0`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'HEPATITIS_B_0');
        }

        ctrl.vitaminKGivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'VITAMIN_K');

        ctrl.vitaminKDateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}VITAMIN_K`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'VITAMIN_K');
        }

        ctrl.bcgGivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'BCG');

        ctrl.bcgDateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}BCG`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'BCG');
        }

        ctrl.opv0GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'OPV_0');

        ctrl.opv0DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}OPV_0`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'OPV_0');
        }

        ctrl.opv1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'OPV_1');

        ctrl.opv1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}OPV_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'OPV_1');
        }

        ctrl.opv2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'OPV_2');

        ctrl.opv2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}OPV_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'OPV_2');
        }

        ctrl.opv3GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'OPV_3');

        ctrl.opv3DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}OPV_3`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'OPV_3');
        }

        ctrl.opvBoosterGivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'OPV_BOOSTER');

        ctrl.opvBoosterDateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}OPV_BOOSTER`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'OPV_BOOSTER');
        }

        ctrl.penta1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'PENTA_1');

        ctrl.penta1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}PENTA_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'PENTA_1');
        }

        ctrl.penta2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'PENTA_2');

        ctrl.penta2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}PENTA_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'PENTA_2');
        }

        ctrl.penta3GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'PENTA_3');

        ctrl.penta3DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}PENTA_3`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'PENTA_3');
        }

        ctrl.dpt1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'DPT_1');

        ctrl.dpt1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}DPT_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'DPT_1');
        }

        ctrl.dpt2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'DPT_2');

        ctrl.dpt2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}DPT_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'DPT_2');
        }

        ctrl.dpt3GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'DPT_3');

        ctrl.dpt3DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}DPT_3`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'DPT_3');
        }

        ctrl.dptBoosterGivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'DPT_BOOSTER');

        ctrl.dptBoosterDateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}DPT_BOOSTER`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'DPT_BOOSTER');
        }

        ctrl.rotaVirus1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'ROTA_VIRUS_1');

        ctrl.rotaVirus1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}ROTA_VIRUS_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'ROTA_VIRUS_1');
        }

        ctrl.rotaVirus2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'ROTA_VIRUS_2');

        ctrl.rotaVirus2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}ROTA_VIRUS_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'ROTA_VIRUS_2');
        }

        ctrl.rotaVirus3GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'ROTA_VIRUS_3');

        ctrl.rotaVirus3DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}ROTA_VIRUS_3`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'ROTA_VIRUS_3');
        }

        ctrl.measles1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'MEASLES_1');

        ctrl.measles1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}MEASLES_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'MEASLES_1');
        }

        ctrl.measles2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'MEASLES_2');

        ctrl.measles2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}MEASLES_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'MEASLES_2');
        }

        ctrl.measlesRubella1GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'MEASLES_RUBELLA_1');

        ctrl.measlesRubella1DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}MEASLES_RUBELLA_1`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'MEASLES_RUBELLA_1');
        }

        ctrl.measlesRubella2GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'MEASLES_RUBELLA_2');

        ctrl.measlesRubella2DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}MEASLES_RUBELLA_2`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'MEASLES_RUBELLA_2');
        }

        ctrl.fIpv101GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'F_IPV_1_01');

        ctrl.fIpv101DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}F_IPV_1_01`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'F_IPV_1_01');
        }

        ctrl.fIpv201GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'F_IPV_2_01');

        ctrl.fIpv201DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}F_IPV_2_01`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'F_IPV_2_01');
        }

        ctrl.fIpv205GivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'F_IPV_2_05');

        ctrl.fIpv205DateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}F_IPV_2_05`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'F_IPV_2_05');
        }

        ctrl.vitaminAGivenChanged = (iteratorIndicesMap) => ctrl.immunisationGivenChanged(iteratorIndicesMap, 'VITAMIN_A');

        ctrl.vitaminADateChanged = (iteratorIndicesMap, uuid) => {
            let index = iteratorIndicesMap['formData.childDetails'];
            ctrl.formUuids[`${index}VITAMIN_A`] = uuid;
            ctrl.immunisationDateChanged(iteratorIndicesMap, 'VITAMIN_A');
        }

        ctrl.immunisationGivenChanged = (iteratorIndicesMap, immunisation) => {
            let child = ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']];
            let childIndex = iteratorIndicesMap['formData.childDetails'];
            child.immunisation[immunisation].date = null;
            Object.keys(child.immunisation).forEach((key) => {
                if (!child.immunisation[key].given) {
                    if (child.immunisationGiven.includes(key)) {
                        let givenImmunisationsMap = child.immunisationGiven.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        child.immunisationGiven = givenImmunisationsMap.join(',');
                    }
                } else if (child.immunisation[key].given && child.immunisation[key].date !== null) {
                    let givenImmunisation = child.immunisationGiven;
                    if (givenImmunisation.includes(key)) {
                        let givenImmunisationsMap = givenImmunisation.split(',');
                        givenImmunisationsMap.forEach((imm, index) => {
                            if (imm.includes(key)) {
                                givenImmunisationsMap.splice(index, 1);
                            }
                        });
                        givenImmunisation = givenImmunisationsMap.join(',');
                    }
                    Mask.show();
                    ManagePncDAO.vaccinationValidationChild(moment(child.dob).valueOf(), moment(child.immunisation[key].date).valueOf(), key, givenImmunisation).then((response) => {
                        if (response.result !== "" && response.result !== null) {
                            ctrl.pncForm[ctrl.formUuids[`${childIndex}${key}`]].$setValidity('Invalid vaccine date', false);
                        } else {
                            ctrl.pncForm[ctrl.formUuids[`${childIndex}${key}`]].$setValidity('Invalid vaccine date', true);
                        }
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(() => {
                        Mask.hide();
                    });
                }
            });
        }

        ctrl.immunisationDateChanged = (iteratorIndicesMap, immunisation) => {
            let child = ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']];
            let childIndex = iteratorIndicesMap['formData.childDetails'];
            if (!child.immunisationGiven) {
                child.immunisationGiven = "";
            }
            if (child.immunisationGiven.includes(immunisation)) {
                let givenImmunisationsMap = child.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((imm, i) => {
                    if (imm.includes(immunisation)) {
                        givenImmunisationsMap.splice(i, 1);
                    }
                });
                child.immunisationGiven = givenImmunisationsMap.join(',');
            }
            Mask.show();
            ManagePncDAO.vaccinationValidationChild(moment(child.dob).valueOf(), moment(child.immunisation[immunisation].date).valueOf(), immunisation, child.immunisationGiven).then((response) => {
                if (response.result !== "" && response.result !== null) {
                    ctrl.pncForm[ctrl.formUuids[`${childIndex}${immunisation}`]].$setValidity('Invalid vaccine date', false);
                } else {
                    ctrl.pncForm[ctrl.formUuids[`${childIndex}${immunisation}`]].$setValidity('Invalid vaccine date', true);
                    ctrl.setCurrentlyGivenVaccines(child, immunisation);
                }
                Object.keys(child.immunisation).forEach((key) => {
                    let givenImmunisations = child.immunisationGiven
                    if (child.immunisation[key].given && child.immunisation[key].date !== null) {
                        if (givenImmunisations.includes(key)) {
                            let givenImmunisationsMap = givenImmunisations.split(',');
                            givenImmunisationsMap.forEach((imm, i) => {
                                if (imm.includes(key)) {
                                    givenImmunisationsMap.splice(i, 1);
                                }
                            });
                            givenImmunisations = givenImmunisationsMap.join(',');
                        }
                        Mask.show();
                        ManagePncDAO.vaccinationValidationChild(moment(child.dob).valueOf(), moment(child.immunisation[key].date).valueOf(), key, givenImmunisations).then((res) => {
                            if (res.result !== "" && res.result !== null) {
                                ctrl.pncForm[ctrl.formUuids[`${childIndex}${key}`]].$setValidity('Invalid vaccine date', false);
                            } else {
                                ctrl.pncForm[ctrl.formUuids[`${childIndex}${key}`]].$setValidity('Invalid vaccine date', true);
                            }
                        }).catch((error) => {
                            GeneralUtil.showMessageOnApiCallFailure(error);
                        }).finally(() => {
                            Mask.hide();
                        });
                    }
                });
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        }

        ctrl.setCurrentlyGivenVaccines = (child, immunisation) => {
            if (child.immunisationGiven.includes(immunisation)) {
                let givenImmunisationsMap = child.immunisationGiven.split(',');
                givenImmunisationsMap.forEach((imm, index) => {
                    if (imm.includes(immunisation)) {
                        givenImmunisationsMap.splice(index, 1);
                    }
                });
                child.immunisationGiven = givenImmunisationsMap.join(',');
            }

            if (child.immunisationGiven === null || child.immunisationGiven === "") {
                child.immunisationGiven = immunisation + "#" + moment(child.immunisation[immunisation].date).format("DD/MM/YYYY");
            } else {
                child.immunisationGiven += "," + immunisation + "#" + moment(child.immunisation[immunisation].date).format("DD/MM/YYYY");
            }
        }

        ctrl.savePnc = () => {
            if (ctrl.pncForm.$valid) {
                ctrl.formData.mobileEndDate = moment();
                ctrl.formData.typeOfHospital = ctrl.formData.institutionType;
                if (ctrl.formData.deliveryPlace === 'HOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByType;
                } else if (ctrl.formData.deliveryPlace === 'THISHOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByUser;
                }
                ctrl.formData.motherDetails.memberStatus = "AVAILABLE";
                ctrl.formData.motherDetails.serviceDate = ctrl.formData.serviceDate;
                ctrl.formData.motherDetails.referralInfraId = ctrl.formData.motherDetails.motherReferralDone ? ctrl.formData.motherDetails.motherReferralHospitalByType : null;
                ctrl.formData.motherDetails.motherReferralDone = ctrl.formData.motherDetails.motherReferralDone ? 'YES' : 'NO';
                ctrl.formData.motherDetails.deathInfrastructureId = ctrl.formData.motherDetails.deathHospitalByType;
                ctrl.formData.childDetails.forEach((child) => {
                    child.referralInfraId = child.childReferralDone ? child.childReferralHospitalByType : null;
                    child.childReferralDone = child.childReferralDone ? 'YES' : 'NO';
                    if (Array.isArray(child.dueImmunisations) && child.dueImmunisations.length) {
                        child.dueImmunisations.forEach((immunisation) => {
                            child.immunisationDtos = [];
                            if (child.immunisation !== null && child.immunisation[immunisation].given) {
                                child.immunisationDtos.push({
                                    immunisationGiven: immunisation,
                                    immunisationDate: moment(child.immunisation[immunisation].date).toDate()
                                });
                            }
                        });
                    }
                });
                Mask.show();
                ManagePncDAO.createOrUpdate(ctrl.formData).then((response) => {
                    toaster.pop('success', 'PNC visit added');
                    $state.go('techo.manage.pncSearch');
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.transformArrayToKeyValue = (array, keyProperty, valueProperty) => {
            return array.map((element) => {
                return {
                    key: element[keyProperty],
                    value: element[valueProperty]
                }
            });
        }

        ctrl.goBack = () => {
            window.history.back();
        }

        ctrl.init();

    }
    angular.module('imtecho.controllers').controller('ManagePncController', ManagePncController);
})();
