/* global moment */
(function (angular) {
    function AncFormQuestionsController($q, toaster, QueryDAO, Mask, AncService, $state, GeneralUtil, AuthenticateService, $filter) {
        var ctrl = this;
        const FEATURE = 'techo.manage.ancSearch';
        const ANC_FORM_CONFIGURATION_KEY = 'RCH_FACILITY_ANC';
        ctrl.formData = {};
        ctrl.previousAncInfo = [];
        ctrl.previousVaccinations = [];
        ctrl.today = moment().startOf('day');

        ctrl.init = () => {
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_anc_member_details',
                parameters: {
                    id: Number($state.params.id)
                }
            }).then((response) => {
                if (response && Array.isArray(response.result) && response.result.length) {
                    ctrl.memberDetails = response.result[0];
                    ctrl.memberDetails.accountNumber = ctrl.memberDetails.accountNumber ? 'Available' : null;
                    ctrl.memberDetails.ifsc = ctrl.memberDetails.ifsc ? 'Available' : null;
                    ctrl.memberDetails.memberName = `${ctrl.memberDetails.firstName} ${ctrl.memberDetails.middleName} ${ctrl.memberDetails.lastName}`;
                    ctrl.memberDetails.benefits = [];
                    if (ctrl.memberDetails.isChiranjeeviYojnaBeneficiary) {
                        ctrl.memberDetails.benefits.push('Chiranjeevi Yojna')
                    }
                    if (ctrl.memberDetails.isIayBeneficiary) {
                        ctrl.memberDetails.benefits.push('IAY')
                    }
                    if (ctrl.memberDetails.isJsyBeneficiary) {
                        ctrl.memberDetails.benefits.push('JSY')
                    }
                    if (ctrl.memberDetails.isKpsyBeneficiary) {
                        ctrl.memberDetails.benefits.push('KPSY')
                    }
                    if (ctrl.memberDetails.benefits.length === 0) {
                        ctrl.memberDetails.benefits.push('None')
                    }
                    ctrl.memberDetails.benefits = ctrl.memberDetails.benefits.join();
                    let additionalInfo = JSON.parse(ctrl.memberDetails.additionalInfo);
                    if (additionalInfo && additionalInfo.lastServiceLongDate) {
                        if (moment(additionalInfo.lastServiceLongDate).isSame(moment(), 'day')) {
                            Promise.reject({ data: { message: 'ANC visit for this member has already been done today' } });
                        } else {
                            ctrl.minServiceDate = moment(additionalInfo.lastServiceLongDate).add(1, 'days');
                        }
                    } else {
                        ctrl.minServiceDate = ctrl.memberDetails.lmpDate
                    }
                    if (ctrl.minServiceDate && ctrl.minServiceDate < (moment().subtract(15, 'days'))) {
                        ctrl.minServiceDate = moment().subtract(15, 'days')
                    }
                    let promiseList = [];
                    promiseList.push(AuthenticateService.getLoggedInUser());
                    promiseList.push(AuthenticateService.getAssignedFeature(FEATURE));
                    return $q.all(promiseList);
                } else {
                    Promise.reject({ data: { message: 'Member not found. Please try again' } });
                }
            }).then((response) => {
                ctrl.loggedInUser = response[0].data;
                ctrl.rights = response[1].featureJson;
                ctrl.formConfigurations = response[1].systemConstraintConfigs[ANC_FORM_CONFIGURATION_KEY];
                ctrl.webTemplateConfigs = response[1].webTemplateConfigs[ANC_FORM_CONFIGURATION_KEY];
                let dtoList = [];
                dtoList.push({
                    code: 'retrieve_previous_anc_information',
                    parameters: {
                        healthid: ctrl.memberDetails.uniqueHealthId,
                        curPregRegDetId: ctrl.memberDetails.curPregRegDetId
                    },
                    sequence: 1
                });
                dtoList.push({
                    code: 'retrieve_anc_vaccination_given',
                    parameters: {
                        id: Number($state.params.id),
                        pregRegDetId: ctrl.memberDetails.curPregRegDetId
                    },
                    sequence: 2
                });
                return QueryDAO.executeAll(dtoList);
            }).then((response) => {
                ctrl.previousAncInfo = response[0].result;
                ctrl.previousAncInfo.map((previousInfo) => {
                    previousInfo.lmp = ctrl.memberDetails.lmpDate;
                });
                ctrl.previousVaccinations = response[1].result;
                Mask.show();
                // NdhmHealthIdCreateDAO.getHealthIdCardByMemberId(Number($state.params.id), 'member_id').then(function (res) {
                //     if (res && res.healthIdNumber) {
                //         ctrl.healthIdNumber = res.healthIdNumber;
                //     }
                // }).catch((error) => {
                //     ctrl.healthIdCardImage = null;
                //     GeneralUtil.showMessageOnApiCallFailure(error);
                // }).finally(Mask.hide);
                ctrl.createAncObj();
                Mask.hide();
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
                $state.go(FEATURE);
            }).finally(() => {
                Mask.hide();
            });
        };

        ctrl.createAncObj = () => {
            Object.assign(ctrl.formData, {
                mobileStartDate: moment(),
                memberId: $state.params.id,
                familyId: ctrl.memberDetails.fid,
                locationId: ctrl.memberDetails.areaId ? ctrl.memberDetails.areaId : ctrl.memberDetails.locationId,
                memberStatus: 'AVAILABLE',
                lmp: moment(ctrl.memberDetails.lmpDate),
                bloodGroup: ctrl.memberDetails.bloodGroup,
                lastDeliveryOutcome: ctrl.memberDetails.lastDeliveryOutcome,
                jsyBeneficiary: ctrl.memberDetails.isJsyBeneficiary,
                isJsyAvailable: ctrl.memberDetails.isJsyBeneficiary !== null ? true : false,
                jsyPaymentDone: ctrl.memberDetails.isJsyPaymentDone,
                isJsyPaymentAvailable: ctrl.memberDetails.isJsyPaymentDone !== null ? true : false,
                kpsyBeneficiary: ctrl.memberDetails.isKpsyBeneficiary,
                isKpsyAvailable: ctrl.memberDetails.isKpsyBeneficiary !== null ? true : false,
                iayBeneficiary: ctrl.memberDetails.isIayBeneficiary,
                isIayAvailable: ctrl.memberDetails.isIayBeneficiary !== null ? true : false,
                isMobileNumberAvailable: ctrl.memberDetails.mobileNumber ? true : false,
                isAccountNumberAvailable: ctrl.memberDetails.accountNumber ? true : false,
                isIfscAvailable: ctrl.memberDetails.ifsc ? true : false,
                eligibilityCriteria: ctrl.memberDetails.bplFlag ? 'Family is BPL' : 'Family is not BPL',
                bpl: ctrl.memberDetails.bplFlag ? 'Yes' : 'No',
                showLastDeliveryOutcome: ctrl.memberDetails.currentGravida && ctrl.memberDetails.currentGravida > 1 ? true : false,
                lmpDateIsOutside140: ctrl.today > moment(ctrl.memberDetails.lmpDate).add('days', 140) ? true : false,
                visitmorethan3: ctrl.today > moment(ctrl.memberDetails.lmpDate).add('days', 190) ? true : false,
                pregnantInLast3Years: ctrl.today < moment(ctrl.memberDetails.lastDeliveryDate).add('years', 3) ? true : false,
                showCalcium: moment().diff(moment(ctrl.memberDetails.lmpDate), 'days') > 90,
                showIfa: moment().diff(moment(ctrl.memberDetails.lmpDate), 'days') > 90,
                isApplicableForInjCorticosteroid: ctrl.memberDetails.lmpDate && moment().diff(ctrl.memberDetails.lmpDate, 'week') > 24 && moment().diff(ctrl.memberDetails.lmpDate, 'week') < 34,
                childDetails: [{}],
                vaccinations: [{}]
            });
            if (ctrl.memberDetails.immunisationGiven) {
                Object.assign(ctrl.formData, {
                    ...ctrl.formData,
                    isTT1Given: ctrl.memberDetails.immunisationGiven.includes('TT1') ? true : false,
                    isTT2Given: ctrl.memberDetails.immunisationGiven.includes('TT2') ? true : false,
                    isTTBoosterGiven: ctrl.memberDetails.immunisationGiven.includes('TT_BOOSTER') ? true : false
                })
            } else {
                Object.assign(ctrl.formData, {
                    ...ctrl.formData,
                    isTT1Given: false,
                    isTT2Given: false,
                    isTTBoosterGiven: false
                })
            }
            if (!ctrl.formData.pregnantInLast3Years && ctrl.formData.isTT1Given && !ctrl.formData.isTT2Given) {
                ctrl.formData.immunisations = ctrl.memberDetails.immunisationGiven.split(',');
                ctrl.formData.immunisations.forEach((immunisation) => {
                    if (immunisation.includes('TT1')) {
                        ctrl.formData.minTT2Date = moment(immunisation.split('#')[1], 'DD/MM/YYYY').add(28, 'days');
                    }
                });
            }
        };

        ctrl.saveAnc = () => {
            if (ctrl.ancForm.$valid) {
                ctrl.formData.mobileEndDate = moment();
                ctrl.formData.uniqueHealthID = ctrl.memberDetails.uniqueHealthId;
                ctrl.formData.memberStatus = ctrl.formData.isAlive ? 'AVAILABLE' : 'DEATH';
                ctrl.formData.typeOfHospital = ctrl.formData.institutionType;
                if (ctrl.formData.deliveryPlace === 'HOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByType;
                } else if (ctrl.formData.deliveryPlace === 'THISHOSP') {
                    ctrl.formData.healthInfrastructureId = ctrl.formData.hospitalByUser;
                }
                ctrl.formData.immunisationDetails = [];
                if (ctrl.formData.tt1Given) {
                    ctrl.formData.immunisationDetails.push({
                        immunisationGiven: "TT1",
                        immunisationDate: ctrl.formData.tt1date
                    });
                }
                if (ctrl.formData.tt2Given) {
                    ctrl.formData.immunisationDetails.push({
                        immunisationGiven: "TT2",
                        immunisationDate: ctrl.formData.tt2date
                    });
                }
                if (ctrl.formData.ttBoosterGiven) {
                    ctrl.formData.immunisationDetails.push({
                        immunisationGiven: "TT_BOOSTER",
                        immunisationDate: ctrl.formData.ttBoosterDate
                    });
                }
                ctrl.formData.referralInfraId = ctrl.formData.referralHospitalByType || null;
                Mask.show();
                AncService.createOrUpdate(ctrl.formData).then((res) => {
                    toaster.pop('success', 'Anc Registered Successfully');
                    if (ctrl.formData.isAlive && res && res.result && ctrl.rights.isShowHIPModal) {
                        ctrl.fetchDefaultHealthId(Number($state.params.id), Number(res.result));
                    } else {
                        $state.go(FEATURE);// If mother is not alive then no need to link wellness record to ndhm
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        };

        ctrl.fetchDefaultHealthId = (memberId, serviceId) => {
            // NdhmHipDAO.getAllCareContextMasterDetails(memberId).then((res) => {
            //     if (res.length > 0) {
            //         ctrl.healthIdsData = res;
            //         ctrl.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
            //         let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
            //         ctrl.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
            //     }
            //     ctrl.handleLinkRecordInNdhm(memberId, serviceId);
            // }).catch((error) => {
            // })
        }

        ctrl.handleLinkRecordInNdhm = (memberId, serviceId) => {
            let memberObj = {
                memberId: memberId,
                mobileNumber: ctrl.memberDetails.mobileNumber,
                name: ctrl.memberDetails.firstName + " " + ctrl.memberDetails.middleName + " " + ctrl.memberDetails.lastName,
                preferredHealthId: ctrl.prefferedHealthId,
                healthIdsData: ctrl.healthIdsData
            }
            let dataForConsentRequest = {
                title: "Link ANC Wellness Record To ABHA Address",
                memberObj: memberObj,
                consentRecord: "(ANC Wellness Record " + $filter('date')(ctrl.formData.serviceDate, "dd-MM-yyyy") + ")",
                serviceType: "ANC_WELLNESS_RECORD",
                serviceId: serviceId,
                isTokenGenerate: true,
                careContextName: "ANC Wellness Record"
            }
            // NdhmHipUtilService.handleLinkRecordInNdhm(dataForConsentRequest, FEATURE);
        }

        ctrl.deliveryPlaceChanged = () => {
            ctrl.formData.hospitalByUserList.data = [];
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
            ctrl.formData.hospitalByType = null;
            ctrl.formData.hospitalByTypeList.data = [];
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

        ctrl.retrieveReferralHospitalsByInstitutionType = () => {
            ctrl.formData.referralHospitalByType = null;
            ctrl.formData.referralHospitalByTypeList.data = [];
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: ctrl.formData.referralInfraType
                }
            }).then((response) => {
                if (response.result.length) {
                    ctrl.formData.referralHospitalByTypeList.data = ctrl.transformArrayToKeyValue(response.result, 'id', 'name');
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

        ctrl.serviceDateChanged = () => {
            Object.assign(ctrl.formData, {
                ...ctrl.formData,
                ironDefAnemiaInjDueDate: null,
                tt1Given: null,
                tt1date: null,
                tt2Given: null,
                tt2date: null,
                ttBoosterGiven: null,
                ttBoosterDate: null,
            });
            ctrl.minInjDate = moment(ctrl.formData.serviceDate).add('days', 1);
            ctrl.maxInjDate = moment(ctrl.formData.serviceDate).add('days', 30);
        }

        ctrl.ironDefAnemiaInjChanged = () => {
            ctrl.formData.ironDefAnemiaInjDueDate = null
        }

        ctrl.isAliveChanged = () => {
            ctrl.resetForm();
            ctrl.ancForm.$setPristine();
        }

        ctrl.onChangeIsMultipleBirthCase = (iteratorIndicesMap) => {
            if (ctrl.formData.childDetails[iteratorIndicesMap['formData.childDetails']].isMultipleBirthCase === 'yes') {
                ctrl.formData.childDetails.push({});
                return;
            }
            ctrl.formData.childDetails.splice(iteratorIndicesMap['formData.childDetails'] + 1, ctrl.formData.childDetails.length - 1);
        }

        ctrl.bloodSugarTestChanged = () => {
            ctrl.formData.sugarTestAfterFoodValue = null;
            ctrl.formData.sugarTestBeforeFoodValue = null;
        };

        ctrl.sickleCellTestChanged = () => {
            ctrl.formData.ifaTabletsGiven = null;
        }

        ctrl.urineTestDoneChanged = () => {
            ctrl.formData.urineAlbumin = null;
            ctrl.formData.urineSugar = null;
        }

        ctrl.tt1GivenChanged = () => {
            ctrl.formData.tt1date = null;
        }

        ctrl.tt2GivenChanged = () => {
            ctrl.formData.tt2date = null;
        }

        ctrl.ttBoosterGivenChanged = () => {
            ctrl.formData.ttBoosterDate = null;
        }

        ctrl.jsyBeneficiaryChanged = () => {
            ctrl.formData.jsyPaymentDone = null;
        }

        ctrl.goBack = () => {
            window.history.back();
        };

        ctrl.dangerSignsChanged = () => {
            Object.assign(ctrl.formData, {
                ...ctrl.formData,
                showOtherDangerSign: ctrl.formData.dangerousSignIds.some(dangerSign => dangerSign == -1),
                otherDangerousSign: null
            })
        }

        ctrl.referralDoneChanged = () => {
            ctrl.formData.referralInfraType = null;
            ctrl.formData.referralHospitalByType = null;
        }

        ctrl.transformArrayToKeyValue = (array, keyProperty, valueProperty) => {
            return array.map((element) => {
                return {
                    key: element[keyProperty],
                    value: element[valueProperty]
                }
            });
        }

        ctrl.resetForm = () => {
            Object.assign(ctrl.formData, {
                deathDate: null,
                placeOfDeath: null,
                deathReason: null,
                jsyBeneficiary: null,
                jsyPaymentDone: null,
                kpsyBeneficiary: null,
                iayBeneficiary: null,
                hbsagTest: null,
                vdrlTest: null,
                hivTest: null,
                sickleCellTest: null,
                ifaTabletsGiven: null,
                faTabletsGiven: null,
                calciumTabletsGiven: null,
                albendazoleGiven: null,
                ttBoosterGiven: null,
                ttBoosterDate: null,
                tt1Given: null,
                tt1date: null,
                tt2Given: null,
                tt2date: null,
                bloodTransfusion: null,
                ironDefAnemiaInj: null,
                ironDefAnemiaInjDueDate: null,
                referralDone: null,
                expectedDeliveryPlace: null,
                memberHeight: null,
                weight: null,
                haemoglobinCount: null,
                systolicBp: null,
                diastolicBp: null,
                foetalMovement: null,
                foetalHeight: null,
                foetalHeartSound: null,
                foetalPosition: null,
                bloodSugarTest: null,
                sugarTestAfterFoodValue: null,
                sugarTestBeforeFoodValue: null,
                urineTestDone: null,
                urineAlbumin: null,
                urineSugar: null,
                lastDeliveryOutcome: null,
                pregnencyComplication: null,
                dangerousSignIds: null,
                otherDangerousSign: null,
                examinedByGynecologist: null,
                isInjCorticosteroidGiven: null
            });
        }

        ctrl.init();
    }
    angular.module('imtecho.controllers').controller('AncFormQuestionsController', AncFormQuestionsController);
})(window.angular);
