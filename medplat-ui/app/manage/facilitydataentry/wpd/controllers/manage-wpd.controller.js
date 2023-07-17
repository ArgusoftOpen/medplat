/* global moment */
(function () {
    function ManageWpdController($stateParams,$state, $uibModal, Mask, QueryDAO, ManageWpdDAO, toaster, AuthenticateService, GeneralUtil, SelectizeGenerator, IMMUNISATIONS, $filter) {
        var managewpdcontroller = this;
        managewpdcontroller.immunisations = IMMUNISATIONS;
        managewpdcontroller.motherReferralPlaces = [];
        managewpdcontroller.childReferralPlaces = [];
        managewpdcontroller.counter = 1;
        managewpdcontroller.MS_PER_DAY = 1000 * 60 * 60 * 24;
        managewpdcontroller.showDischargeSection = true;
        managewpdcontroller.maxDeliveryDate = new Date();
        managewpdcontroller.manageWpdObject = {};
        managewpdcontroller.invalidDeliveryDate = null;

        managewpdcontroller.isMotherDangerSignOther = () => {
            managewpdcontroller.showOtherMotherDangerSign = managewpdcontroller.manageWpdObject.motherDangerSigns.some(dangerSign => dangerSign === -1);
            managewpdcontroller.manageWpdObject.motherOtherDangerSign = null;
            if (Array.isArray(managewpdcontroller.manageWpdObject.motherDangerSigns) && (managewpdcontroller.manageWpdObject.motherDangerSigns.length === 0 || (managewpdcontroller.manageWpdObject.motherDangerSigns.length === 1 && managewpdcontroller.manageWpdObject.motherDangerSigns[0] === 'NONE'))) {
                managewpdcontroller.manageWpdObject.isMotherReferralDone = null;
            }
        };

        managewpdcontroller.isChildDangerSignOther = (childDetail) => {
            childDetail.showOtherChildDangerSign = childDetail.childDangerSigns.some(dangerSign => dangerSign === -1);
            childDetail.childOtherDangerSign = null;
            if (Array.isArray(childDetail.childDangerSigns) && (childDetail.childDangerSigns.length === 0 || (childDetail.childDangerSigns.length === 1 && childDetail.childDangerSigns[0] === 'NONE'))) {
                childDetail.childReferral = null;
            }
        };

        managewpdcontroller.isCongenitalDeformityOther = (childDetail) => {
            childDetail.showOtherCongenitalDeformity = childDetail.congenitalDeformityPresent.some(congenitalDeformity => congenitalDeformity === -1);
            childDetail.congenitalDeformityOther = null;
        };

        managewpdcontroller.changeNumberOfChilds = (childDetail, index) => {
            if (childDetail.multipleBirthValue === 'yes') {
                if (index === (managewpdcontroller.manageWpdObject.childDetails.length - 1)) {
                    managewpdcontroller.counter++;
                    managewpdcontroller.manageWpdObject.childDetails.push({
                        counter: managewpdcontroller.counter,
                        name: managewpdcontroller.childName
                    });
                }
            } else if (childDetail.multipleBirthValue === 'no') {
                if (index === (managewpdcontroller.manageWpdObject.childDetails.length - 1)) {
                    return;
                } else {
                    managewpdcontroller.manageWpdObject.childDetails.splice(index + 1, managewpdcontroller.manageWpdObject.childDetails.length - 1);
                    managewpdcontroller.counter--;
                }
            }
        };

        managewpdcontroller.validateDeliveryDate = () => {
            if (managewpdcontroller.dateDiffInDays(managewpdcontroller.originalLmp, new Date(managewpdcontroller.manageWpdObject.deliveryDate)) > 305) {
                managewpdcontroller.invalidDeliveryDate = true;
            } else {
                managewpdcontroller.invalidDeliveryDate = false;
            }
        }

        managewpdcontroller.calculateVaccinesToBeGiven = () => {
            // reset Death date
            if (managewpdcontroller.manageWpdObject.deathDate && !(managewpdcontroller.manageWpdObject.deathDate <= managewpdcontroller.manageWpdObject.deliveryDate)) {
                managewpdcontroller.manageWpdObject.deathDate = null;
            }

            var oneDay = 24 * 60 * 60 * 1000;
            var deliveryDate = managewpdcontroller.manageWpdObject.deliveryDate;
            var currentDate = new Date();
            const today = moment();
            managewpdcontroller.ageInDays = Math.round(Math.abs((currentDate.getTime() - deliveryDate.getTime()) / (oneDay)));
            managewpdcontroller.minVaccinationDate = managewpdcontroller.manageWpdObject.deliveryDate;
            /**
             * For vaccine of BCG: 0 to 365 days of period to give vaccine
             * For vaccine of OPV_0: 0 to 15 days of period to give vaccine
             * For vaccine of Hepatitis_B and Vitamin_K: 0 to 1 day of period to give vaccine
             *
             * If, current date is lower than end date of vaccine to be given
             * Then, keep the max vaccine date as current date
             * Else, keep the max vaccine date as end date
             */
            managewpdcontroller.maxBCGDate = today < moment(deliveryDate).add(366, 'days') ? today : moment(deliveryDate).add(365, 'days');
            managewpdcontroller.maxOPVDate = today < moment(deliveryDate).add(16, 'days') ? today : moment(deliveryDate).add(15, 'days');
            managewpdcontroller.maxHepatitisBDate = managewpdcontroller.maxVitaminKDate = today < moment(deliveryDate).add(2, 'days') ? today : moment(deliveryDate).add(1, 'days');
            /**
             * If, current date is higher than 30 days from delivery date
             * Then, keep the max discharge date as 30 days from delivery date
             * Else, keep the max discharge date as current date
             */
            managewpdcontroller.maxDischargeDate = today > moment(deliveryDate).add(31, 'days') ? moment(deliveryDate).add(30, 'days') : today;
            managewpdcontroller.showCorticoSteroid = managewpdcontroller.dateDiffInDays(managewpdcontroller.manageWpdObject.lmpDateCalculation, new Date(deliveryDate)) >= 238 ? false : true;
            if (managewpdcontroller.dateDiffInDays(managewpdcontroller.originalLmp, new Date(deliveryDate)) < 140) {
                managewpdcontroller.isMtpTerm = true;
                managewpdcontroller.isAbortionTerm = true;
                managewpdcontroller.isLiveBirthTerm = false;
                managewpdcontroller.isStillBirthTerm = false;
            } else if (managewpdcontroller.dateDiffInDays(managewpdcontroller.originalLmp, new Date(deliveryDate)) < 168) {
                managewpdcontroller.isAbortionTerm = true;
                managewpdcontroller.isMtpTerm = false;
                managewpdcontroller.isLiveBirthTerm = false;
                managewpdcontroller.isStillBirthTerm = false;
            } else if (managewpdcontroller.dateDiffInDays(managewpdcontroller.originalLmp, new Date(deliveryDate)) < 306) {
                managewpdcontroller.isLiveBirthTerm = true;
                managewpdcontroller.isStillBirthTerm = true;
                managewpdcontroller.isMtpTerm = false;
                managewpdcontroller.isAbortionTerm = false;
            } else {
                managewpdcontroller.isLiveBirthTerm = false;
                managewpdcontroller.isStillBirthTerm = false;
                managewpdcontroller.isMtpTerm = false;
                managewpdcontroller.isAbortionTerm = false;
            }
            if (!!managewpdcontroller.manageWpdObject.childDetails) {
                managewpdcontroller.manageWpdObject.childDetails = [];
                managewpdcontroller.counter = 1;
                managewpdcontroller.manageWpdObject.childDetails.push({
                    counter: managewpdcontroller.counter,
                    name: managewpdcontroller.childName
                });
            }
            if (!!managewpdcontroller.manageWpdObject.dischargeDate) {
                managewpdcontroller.manageWpdObject.dischargeDate = null;
            }
            if (!!managewpdcontroller.manageWpdObject.deliveryDate) {
                managewpdcontroller.manageWpdObject.deliveryDate = new Date(
                    managewpdcontroller.manageWpdObject.deliveryDate.getFullYear(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getMonth(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getDate(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getHours(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMinutes(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMilliseconds());
                if (managewpdcontroller.manageWpdObject.deliveryDate > today) {
                    toaster.pop('error', "Delivery time is incorrect");
                }
            } else {
                toaster.pop('error', 'Please select delivery date');
            }
        };

        managewpdcontroller.saveWpd = (managewpdForm) => {
            if (managewpdcontroller.managewpdForm.$valid) {
                managewpdcontroller.manageWpdObject.deliveryDate = new Date(
                    managewpdcontroller.manageWpdObject.deliveryDate.getFullYear(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getMonth(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getDate(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getHours(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMinutes(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMilliseconds());
                if (!!managewpdcontroller.manageWpdObject.dischargeDate) {
                    managewpdcontroller.manageWpdObject.dischargeDate = new Date(
                        managewpdcontroller.manageWpdObject.dischargeDate.getFullYear(),
                        managewpdcontroller.manageWpdObject.dischargeDate.getMonth(),
                        managewpdcontroller.manageWpdObject.dischargeDate.getDate(),
                        managewpdcontroller.manageWpdObject.dischargeTime.getHours(),
                        managewpdcontroller.manageWpdObject.dischargeTime.getMinutes(),
                        managewpdcontroller.manageWpdObject.dischargeTime.getMilliseconds());
                }
                if (managewpdcontroller.manageWpdObject.deliveryDate > moment()) {
                    toaster.pop('error', "Delivery time is incorrect");
                    managewpdcontroller.manageWpdObject.deliveryTime.$valid = false;
                    return;
                }
                managewpdcontroller.manageWpdObject.endDate = new Date();
                if (managewpdcontroller.manageWpdObject.deliveryPlace === 'HOSP') {
                    managewpdcontroller.manageWpdObject.typeOfHospital = managewpdcontroller.manageWpdObject.institutionType;
                    managewpdcontroller.manageWpdObject.healthInfrastructureId = managewpdcontroller.manageWpdObject.institute;
                } else if (managewpdcontroller.manageWpdObject.deliveryPlace === 'THISHOSP' || managewpdcontroller.manageWpdObject.deliveryPlace === 'ON_THE_WAY') {
                    managewpdcontroller.manageWpdObject.typeOfHospital = managewpdcontroller.manageWpdObject.institute.type;
                    managewpdcontroller.manageWpdObject.healthInfrastructureId = managewpdcontroller.manageWpdObject.institute.id;
                } else {
                    delete managewpdcontroller.manageWpdObject.typeOfHospital;
                    delete managewpdcontroller.manageWpdObject.healthInfrastructureId;
                }
                managewpdcontroller.manageWpdObject.isDeliveryDone = true;
                managewpdcontroller.manageWpdObject.isFromWeb = true;
                managewpdcontroller.manageWpdObject.memberStatus = "AVAILABLE";
                if (!!managewpdcontroller.manageWpdObject.highRiskSymptomsDuringDelivery && managewpdcontroller.manageWpdObject.highRiskSymptomsDuringDelivery[0] === 'NONE') {
                    delete managewpdcontroller.manageWpdObject.highRiskSymptomsDuringDelivery;
                }
                if (!!managewpdcontroller.manageWpdObject.treatmentsDuringDelivery && managewpdcontroller.manageWpdObject.treatmentsDuringDelivery[0] === 'NONE') {
                    delete managewpdcontroller.manageWpdObject.treatmentsDuringDelivery;
                }
                if (!!managewpdcontroller.manageWpdObject.motherDangerSigns && managewpdcontroller.manageWpdObject.motherDangerSigns[0] === 'NONE') {
                    delete managewpdcontroller.manageWpdObject.motherDangerSigns;
                }
                if (managewpdcontroller.manageWpdObject.highRiskSymptomsDuringDelivery && managewpdcontroller.manageWpdObject.highRiskSymptomsDuringDelivery.length > 0) {
                    managewpdcontroller.manageWpdObject.isHighRiskCase = true;
                }
                if (managewpdcontroller.manageWpdObject.motherDangerSigns && managewpdcontroller.manageWpdObject.motherDangerSigns.length > 0) {
                    managewpdcontroller.manageWpdObject.isHighRiskCase = true;
                }
                managewpdcontroller.manageWpdObject.childDetails.forEach((childDetail) => {
                    if (!!childDetail.congenitalDeformityPresent && childDetail.congenitalDeformityPresent[0] === 'NONE') {
                        delete childDetail.congenitalDeformityPresent;
                    }
                    if (!!childDetail.childDangerSigns && childDetail.childDangerSigns[0] === 'NONE') {
                        delete childDetail.childDangerSigns;
                    }
                    childDetail.immunisationDetails = [];
                    if (childDetail.isBcgGiven) {
                        childDetail.immunisationDetails.push({
                            immunisationGiven: managewpdcontroller.immunisations.IMMUNISATION_BCG,
                            immunisationDate: childDetail.bcgDate
                        });
                    }
                    if (childDetail.isOpvGiven) {
                        childDetail.immunisationDetails.push({
                            immunisationGiven: managewpdcontroller.immunisations.IMMUNISATION_OPV_0,
                            immunisationDate: childDetail.opvDate
                        });
                    }
                    if (childDetail.isHepatitisBGiven) {
                        childDetail.immunisationDetails.push({
                            immunisationGiven: managewpdcontroller.immunisations.IMMUNISATION_HEPATITIS_B_0,
                            immunisationDate: childDetail.hepatitisBDate
                        });
                    }
                    if (childDetail.isVitaminKGiven) {
                        childDetail.immunisationDetails.push({
                            immunisationGiven: managewpdcontroller.immunisations.IMMUNISATION_VITAMIN_K,
                            immunisationDate: childDetail.vitaminKDate
                        });
                    }
                });
                Mask.show();
                ManageWpdDAO.createOrUpdate(managewpdcontroller.manageWpdObject).then((response) => {
                    Mask.hide();
                    toaster.pop('success', 'Delivery details added');
                    if (response.length > 0) {
                        let generatedChildrenDetails = "<table class=\"table table-sm table-striped table-bordered filter-table table-fixed header-fixed\"><tr><th>Child Name</th><th>Unique Health ID</th>";
                        response.forEach((child) => {
                            generatedChildrenDetails = generatedChildrenDetails.concat("<tr><td>")
                                .concat(child.firstName)
                                .concat("</td><td>")
                                .concat(child.uniqueHealthId)
                                .concat("</td></tr>");
                        });
                        generatedChildrenDetails = generatedChildrenDetails.concat("</table>");
                        let modalInstance = $uibModal.open({
                            templateUrl: 'app/common/views/alert.modal.html',
                            controller: 'alertModalController',
                            windowClass: 'cst-modal',
                            backdrop: 'static',
                            keyboard: false,
                            title: '',
                            size: 'med',
                            resolve: {
                                message: () => {
                                    return {
                                        title: `Generated Unique Health Id${response.length > 1 ? 's' : ''}`,
                                        body: generatedChildrenDetails
                                    };
                                }
                            }
                        });
                        // modalInstance.result.then(() => {
                        //     managewpdcontroller.handleLinkRecordInNdhm(response[0].motherId)
                        // });
                    } else {
                        // managewpdcontroller.handleLinkRecordInNdhm(managewpdcontroller.manageWpdObject.memberId);
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                    Mask.hide();
                });
            }
        };

        // managewpdcontroller.handleLinkRecordInNdhm = (memberId) => {
        //     if (managewpdcontroller.rights && managewpdcontroller.rights.isShowHIPModal) {
        //         Mask.show();
        //         managewpdcontroller.showDeathInstitutes = false;
        //         QueryDAO.execute({
        //             code: 'get_wpd_id_by_member_id_and_delivery_date',
        //             parameters: {
        //                 memberId: memberId
        //             }
        //         }).then((res) => {
        //             let memberObj = {
        //                 memberId: managewpdcontroller.manageWpdObject.memberId,
        //                 mobileNumber: managewpdcontroller.manageWpdObject.mobileNumber,
        //                 name: managewpdcontroller.manageWpdObject.name,
        //                 preferredHealthId: managewpdcontroller.prefferedHealthId,
        //                 healthIdsData: managewpdcontroller.healthIdsData
        //             }
        //             let dataForConsentRequest = {
        //                 title: "Link WPD Discharge Summary To PHR Address",
        //                 memberObj: memberObj,
        //                 consentRecord: "(WPD Discharge Summary " + $filter('date')(managewpdcontroller.manageWpdObject.deliveryDate, "dd-MM-yyyy") + ")",
        //                 serviceType: "WPD_DISCHARGE_SUMMARY",
        //                 serviceId: res.result[0].id,
        //                 isTokenGenerate: true,
        //                 careContextName: "WPD Discharge Summary"
        //             }
        //             NdhmHipUtilService.handleLinkRecordInNdhm(dataForConsentRequest, 'techo.manage.wpdSearch');
        //         }).catch((error) => {
        //             GeneralUtil.showMessageOnApiCallFailure(error);
        //         }).finally(() => {
        //             Mask.hide();
        //         });
        //     } else {
        //         $state.go('techo.manage.wpdSearch');
        //     }
        // }

        managewpdcontroller.deliveryPlaceChanged = () => {
            managewpdcontroller.manageWpdObject.institute = null;
            if (managewpdcontroller.manageWpdObject.deliveryPlace === 'THISHOSP' || managewpdcontroller.manageWpdObject.deliveryPlace === 'ON_THE_WAY') {
                if (managewpdcontroller.institutes.length === 1) {
                    managewpdcontroller.manageWpdObject.institute = managewpdcontroller.institutes[0];
                    managewpdcontroller.instituteChanged();
                    managewpdcontroller.showDeliveryDoneBy = true;
                } else if (managewpdcontroller.institutes.length === 0) {
                    toaster.pop('error', 'No health infrastructure assigned');
                    managewpdcontroller.showDeliveryDoneBy = false;
                }
            }
            managewpdcontroller.showDeliveryPerson = false;
            managewpdcontroller.manageWpdObject.deliveryDoneBy = null;
            managewpdcontroller.manageWpdObject.deliveryPerson = null;
            managewpdcontroller.referralInstituteTypes = [];
            managewpdcontroller.manageWpdObject.motherReferralInstitutionType = null;
            managewpdcontroller.motherReferralHospitals = [];
            managewpdcontroller.showMotherReferralHospitals = false;
            managewpdcontroller.manageWpdObject.motherReferralInfraId = null;
            managewpdcontroller.manageWpdObject.childDetails.forEach((child) => {
                child.childReferralInstitutionType = null;
                child.childReferralHospitals = [];
                child.childReferralInfraId = null;
                child.showReferralHospitals = false;
            });
        };

        managewpdcontroller.pregnancyOutcomeChanged = (index, pregnancyOutcome) => {
            var childDetails = managewpdcontroller.manageWpdObject.childDetails[index];
            if (pregnancyOutcome === 'SBIRTH') {
                managewpdcontroller.manageWpdObject.childDetails[index] = {};
                managewpdcontroller.manageWpdObject.childDetails[index].name = childDetails.name;
                managewpdcontroller.manageWpdObject.childDetails[index].pregnancyOutcome = "SBIRTH";
                managewpdcontroller.manageWpdObject.childDetails[index].multipleBirthValue = "";
            }
            if (index === 0) {
                if (pregnancyOutcome === 'MTP' || pregnancyOutcome === 'ABORTION') {
                    managewpdcontroller.manageWpdObject.childDetails.length = 1;
                    managewpdcontroller.manageWpdObject.childDetails[0].multipleBirthValue = "";
                }
            }
        };

        managewpdcontroller.goBack = () => {
            window.history.back();
        };
        managewpdcontroller.init = () => {
            let selectizeObject = SelectizeGenerator.generateUserSelectize();
            managewpdcontroller.selectizeOptions = selectizeObject.config;
            managewpdcontroller.manageWpdObject.startDate = new Date();
            managewpdcontroller.manageWpdObject.deliveryTime = new Date(2000, 0, 1, 12, 00, 00);
            managewpdcontroller.manageWpdObject.dischargeTime = new Date(2000, 0, 1, 12, 00, 00);
            let search = {
                byId: false,
                byMemberId: true,
                searchString: $stateParams.id,
                limit: 1,
                offset: 0
            };
            managewpdcontroller.manageWpdObject.childDetails = [];
            Mask.show();
            AuthenticateService.getLoggedInUser().then((user) => {
                managewpdcontroller.loggedInUser = user.data;
                return ManageWpdDAO.searchMembers(search);
            }).then((response) => {
                if (response.length > 0) {
                    if (response[0].isPregnantFlag === null || !response[0].isPregnantFlag) {
                        return Promise.reject({ data: { message: 'Member is not marked as Pregnant' } });
                    } else {
                        if (!!response[0].edd) {
                            managewpdcontroller.manageWpdObject.edd = moment(new Date(moment(response[0].edd).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                        } else {
                            managewpdcontroller.manageWpdObject.edd = "Not available";
                        }
                        if (!!response[0].lmpDate) {
                            managewpdcontroller.manageWpdObject.lmpDate = moment(new Date(moment(response[0].lmpDate).format("YYYY-MM-DD"))).format("DD/MM/YYYY");
                            managewpdcontroller.originalLmp = managewpdcontroller.manageWpdObject.lmpDateCalculation = new Date(response[0].lmpDate);
                        } else {
                            managewpdcontroller.manageWpdObject.lmpDate = "Not available";
                        }
                        if (managewpdcontroller.manageWpdObject.lmpDateCalculation !== null && managewpdcontroller.manageWpdObject.lmpDateCalculation < (moment().subtract(9, 'months'))) {
                            managewpdcontroller.manageWpdObject.lmpDateCalculation = new Date(moment().subtract(9, 'months'));
                        }
                        managewpdcontroller.manageWpdObject.uniqueHealthId = response[0].uniqueHealthId;
                        managewpdcontroller.manageWpdObject.motherMobileNumber = response[0].mobileNumber;
                        managewpdcontroller.manageWpdObject.memberId = response[0].id;
                        managewpdcontroller.manageWpdObject.familyId = response[0].fid;
                        managewpdcontroller.manageWpdObject.familyUniqueId = response[0].familyId;
                        managewpdcontroller.manageWpdObject.memberName = response[0].firstName + " " + response[0].middleName + " " + response[0].lastName;
                        managewpdcontroller.childName = "B/o " + response[0].firstName;
                        managewpdcontroller.manageWpdObject.childDetails.push({
                            counter: managewpdcontroller.counter,
                            name: managewpdcontroller.childName
                        });
                        managewpdcontroller.manageWpdObject.hierarchy = response[0].locationHierarchy;
                        managewpdcontroller.manageWpdObject.locationId = response[0].areaId === null ? response[0].locationId : response[0].areaId;
                        managewpdcontroller.manageWpdObject.bplFlag = response[0].bplFlag ? 'Yes' : 'No';
                        managewpdcontroller.manageWpdObject.caste = response[0].caste;
                        managewpdcontroller.manageWpdObject.isMotherAlive = true;
                        managewpdcontroller.manageWpdObject.additionalInfo = JSON.parse(response[0].additionalInfo);
                        if (!!managewpdcontroller.manageWpdObject.additionalInfo && !!managewpdcontroller.manageWpdObject.additionalInfo.lastServiceLongDate) {
                            managewpdcontroller.minDeathDate = moment(managewpdcontroller.manageWpdObject.additionalInfo.lastServiceLongDate);
                        } else {
                            managewpdcontroller.minDeathDate = managewpdcontroller.manageWpdObject.lmpDateCalculation;
                        }
                        managewpdcontroller.manageWpdObject.benefits = [];
                        if (response[0].isChiranjeeviYojnaBeneficiary) {
                            managewpdcontroller.manageWpdObject.benefits.push("Chiranjeevi Yojna");
                        }
                        if (response[0].isIayBeneficiary) {
                            managewpdcontroller.manageWpdObject.benefits.push("IAY");
                        }
                        if (response[0].isJsyBeneficiary) {
                            managewpdcontroller.manageWpdObject.benefits.push("JSY");
                        }
                        if (response[0].isKpsyBeneficiary) {
                            managewpdcontroller.manageWpdObject.benefits.push("KPSY");
                        }
                        managewpdcontroller.manageWpdObject.benefits = managewpdcontroller.manageWpdObject.benefits.join();
                        managewpdcontroller.manageWpdObject.isAccountNumberAvailable = !!response[0].accountNumber ? true : false;
                        managewpdcontroller.manageWpdObject.isIfscAvailable = !!response[0].ifsc ? true : false;
                        if (!managewpdcontroller.manageWpdObject.isAccountNumberAvailable) {
                            managewpdcontroller.manageWpdObject.memberAccount = response[0].accountNumber;
                        }
                        if (!managewpdcontroller.manageWpdObject.isIfscAvailable) {
                            managewpdcontroller.manageWpdObject.memberIfsc = response[0].ifsc;
                        }
                        let retrieveDataList = [];
                        retrieveDataList.push({
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'institutionsListFhwWpd'
                            },
                            sequence: 1
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'deathReasonsFhwAnc'
                            },
                            sequence: 2
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'deliveryDangerSignsFhwWpd'
                            },
                            sequence: 3
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'congenitalDeformityFhwWpd'
                            },
                            sequence: 4
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'highRiskSymptomsDuringDelivery'
                            },
                            sequence: 5
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'treatmentsDuringDelivery'
                            },
                            sequence: 6
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'wpdDangerSigns'
                            },
                            sequence: 7
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'Health Infrastructure Type'
                            },
                            sequence: 8
                        }, {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'WPD child referral reasons'
                            },
                            sequence: 9
                        }, {
                            code: 'retrieve_worker_info_by_location_id',
                            parameters: {
                                locationId: Number(response[0].locationId) || -100
                            },
                            sequence: 10
                        }, {
                            code: 'retrieve_worker_info_by_location_id',
                            parameters: {
                                locationId: Number(response[0].areaId) || -100
                            },
                            sequence: 11
                        }, {
                            code: 'retrieve_health_infra_for_user',
                            parameters: {
                                userId: managewpdcontroller.loggedInUser.id
                            },
                            sequence: 12
                        });
                        return QueryDAO.executeAll(retrieveDataList);
                    }
                } else {
                    return Promise.reject({ data: { message: 'No record found' } });
                }
            }).then((response) => {
                [managewpdcontroller.typeOfHospitals, managewpdcontroller.mtpPlaces] = [response[0].result, response[0].result];
                managewpdcontroller.motherDeathReasons = response[1].result;
                managewpdcontroller.motherDangerSigns = response[2].result;
                managewpdcontroller.congenitalDeformities = response[3].result;
                managewpdcontroller.highRiskSymptomsDuringDelivery = response[4].result;
                managewpdcontroller.treatmentsDuringDelivery = response[5].result;
                managewpdcontroller.childDangerSigns = response[6].result;
                managewpdcontroller.institutionTypes = response[7].result.filter((institute) => {
                    return institute.id !== 1062 && institute.id !== 1064 && institute.id !== 1010;
                });
                managewpdcontroller.childReferralReason = response[8].result;
                if (!!response[9].result[0].workerDetails) {
                    var fhwJson = JSON.parse(response[9].result[0].workerDetails);
                    managewpdcontroller.manageWpdObject.fhwName = fhwJson[0].name;
                    managewpdcontroller.manageWpdObject.fhwNumber = fhwJson[0].mobileNumber;
                }
                if (!!response[10].result[0].workerDetails) {
                    var ashaJson = JSON.parse(response[10].result[0].workerDetails);
                    managewpdcontroller.manageWpdObject.ashaName = ashaJson[0].name;
                    managewpdcontroller.manageWpdObject.ashaNumber = ashaJson[0].mobileNumber;
                }
                managewpdcontroller.institutes = response[11].result;
                // NdhmHipDAO.getAllCareContextMasterDetails(managewpdcontroller.manageWpdObject.memberId).then((res) => {
                //     if (res.length > 0) {
                //         managewpdcontroller.healthIdsData = res;
                //         managewpdcontroller.healthIds = res.filter(notFrefferedData => notFrefferedData.isPreferred === false).map(healthData => healthData.healthId).toString();
                //         let prefferedHealthIdData = res.find(healthData => healthData.isPreferred === true);
                //         managewpdcontroller.prefferedHealthId = prefferedHealthIdData && prefferedHealthIdData.healthId;
                //     }
                // }).catch((error) => {
                // })
                AuthenticateService.getAssignedFeature("techo.manage.wpdSearch").then((res) => {
                    managewpdcontroller.rights = res.featureJson;
                    if (!managewpdcontroller.rights) {
                        managewpdcontroller.rights = {};
                    }
                })
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.retrieveHospitalsByInstitutionType = () => {
            Mask.show();
            managewpdcontroller.showOtherInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managewpdcontroller.manageWpdObject.institutionType)
                }
            }).then((res) => {
                managewpdcontroller.showOtherInstitutes = true;
                managewpdcontroller.otherInstitutes = res.result;
                managewpdcontroller.referralInstituteTypes = [];
                managewpdcontroller.manageWpdObject.motherReferralInstitutionType = null;
                managewpdcontroller.motherReferralHospitals = [];
                managewpdcontroller.showMotherReferralHospitals = false;
                managewpdcontroller.manageWpdObject.motherReferralInfraId = null;
                managewpdcontroller.manageWpdObject.childDetails.forEach((child) => {
                    child.childReferralInstitutionType = null;
                    child.childReferralHospitals = [];
                    child.childReferralInfraId = null;
                    child.showReferralHospitals = false;
                });
                var selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute(managewpdcontroller.otherInstitutes);
                managewpdcontroller.selectizeObjectForOtherInstitute = selectizeObjectForOtherInstitute.config;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.retrieveDeathHospitalsByInstitutionType = () => {
            Mask.show();
            managewpdcontroller.showDeathInstitutes = false;
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managewpdcontroller.manageWpdObject.deathInfrastructureType)
                }
            }).then((res) => {
                managewpdcontroller.showDeathInstitutes = true;
                managewpdcontroller.deathInstitutes = res.result;
                managewpdcontroller.manageWpdObject.deathInfrastructureId = null;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.retrieveMotherReferralHospitals = () => {
            Mask.show();
            managewpdcontroller.showMotherReferralHospitals = false;
            managewpdcontroller.motherReferralHospitals = [];
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(managewpdcontroller.manageWpdObject.motherReferralInstitutionType)
                }
            }).then((res) => {
                managewpdcontroller.showMotherReferralHospitals = true;
                managewpdcontroller.motherReferralHospitals = res.result;
                managewpdcontroller.manageWpdObject.motherReferralInfraId = null;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.retrieveChildReferralHospitals = (childDetail) => {
            Mask.show();
            childDetail.showReferralHospitals = false;
            childDetail.childReferralHospitals = [];
            QueryDAO.execute({
                code: 'retrieve_hospitals_by_infra_type',
                parameters: {
                    infraType: Number(childDetail.childReferralInstitutionType)
                }
            }).then((res) => {
                childDetail.childReferralHospitals = res.result;
                childDetail.childReferralInfraId = null;
                childDetail.showReferralHospitals = true;
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.instituteChanged = () => {
            if ((managewpdcontroller.manageWpdObject.deliveryPlace === 'HOSP' && !!managewpdcontroller.manageWpdObject.institute)
                || (managewpdcontroller.manageWpdObject.deliveryPlace === 'THISHOSP' && !!managewpdcontroller.manageWpdObject.institute && Object.keys(managewpdcontroller.manageWpdObject.institute).length > 0)) {
                managewpdcontroller.showDeliveryDoneBy = true;
                managewpdcontroller.showDeliveryPerson = false;
                managewpdcontroller.manageWpdObject.deliveryDoneBy = null;
                managewpdcontroller.manageWpdObject.deliveryPerson = null;
            } else {
                managewpdcontroller.showDeliveryDoneBy = false;
                managewpdcontroller.showDeliveryPerson = false;
                managewpdcontroller.manageWpdObject.deliveryDoneBy = null;
                managewpdcontroller.manageWpdObject.deliveryPerson = null;
            }
            var infrastructureType = null;
            if (managewpdcontroller.manageWpdObject.deliveryPlace === 'HOSP') {
                infrastructureType = managewpdcontroller.manageWpdObject.institutionType;
            } else if (managewpdcontroller.manageWpdObject.deliveryPlace === 'THISHOSP' || managewpdcontroller.manageWpdObject.deliveryPlace === 'ON_THE_WAY') {
                infrastructureType = managewpdcontroller.manageWpdObject.institute.type;
            }
            Mask.show();
            QueryDAO.execute({
                code: 'retrieve_health_infra_by_level',
                parameters: {
                    filter: 'U',
                    infrastructureType: Number(infrastructureType)
                }
            }).then((res) => {
                managewpdcontroller.referralInstituteTypes = managewpdcontroller.institutionTypes.filter((institute) => {
                    return res.result.some((result) => {
                        return institute.id === result.health_infra_type_id;
                    });
                });
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
            managewpdcontroller.referralInstituteTypes = [];
            managewpdcontroller.manageWpdObject.motherReferralInstitutionType = null;
            managewpdcontroller.motherReferralHospitals = [];
            managewpdcontroller.showMotherReferralHospitals = false;
            managewpdcontroller.manageWpdObject.motherReferralInfraId = null;
            managewpdcontroller.manageWpdObject.childDetails.forEach((child) => {
                child.childReferralInstitutionType = null;
                child.childReferralHospitals = [];
                child.childReferralInfraId = null;
                child.showReferralHospitals = false;
            });
        };
        managewpdcontroller.setUsersForInstitute = () => {
            if (!!managewpdcontroller.manageWpdObject.deliveryDoneBy) {
                managewpdcontroller.showDeliveryPerson = true;
                managewpdcontroller.manageWpdObject.deliveryPerson = null;
            } else {
                managewpdcontroller.showDeliveryPerson = false;
                managewpdcontroller.manageWpdObject.deliveryPerson = null;
            }
            managewpdcontroller.usersForInstitute = [];
            delete managewpdcontroller.manageWpdObject.deliveryPerson;
            if (managewpdcontroller.manageWpdObject.deliveryPlace === 'HOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managewpdcontroller.manageWpdObject.institute),
                        code: managewpdcontroller.manageWpdObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managewpdcontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            } else if (managewpdcontroller.manageWpdObject.institute && managewpdcontroller.manageWpdObject.deliveryPlace === 'THISHOSP') {
                Mask.show();
                QueryDAO.execute({
                    code: 'retrieve_users_for_infra_role',
                    parameters: {
                        healthInfraId: Number(managewpdcontroller.manageWpdObject.institute.id),
                        code: managewpdcontroller.manageWpdObject.deliveryDoneBy || null
                    }
                }).then((res) => {
                    managewpdcontroller.usersForInstitute = res.result;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        };

        managewpdcontroller.deathDateChanged = () => {
            if (managewpdcontroller.manageWpdObject.deathPlace === 'HOSP' && !!managewpdcontroller.manageWpdObject.deathDate) {
                managewpdcontroller.manageWpdObject.isDischarged = true;
                managewpdcontroller.manageWpdObject.dischargeDate = managewpdcontroller.manageWpdObject.deathDate;
            }
        };

        managewpdcontroller.deathPlaceChanged = () => {
            if (managewpdcontroller.manageWpdObject.deathPlace === 'HOSP' && !!managewpdcontroller.manageWpdObject.deathDate) {
                managewpdcontroller.manageWpdObject.isDischarged = true;
                managewpdcontroller.manageWpdObject.dischargeDate = managewpdcontroller.manageWpdObject.deathDate;
            } else {
                managewpdcontroller.manageWpdObject.isDischarged = null;
                managewpdcontroller.manageWpdObject.dischargeDate = null;
            }
        };

        managewpdcontroller.checkImmunisationValidation = (dob, givenDate, currentVaccine) => {
            Mask.show();
            ManageWpdDAO.vaccinationValidationChild(moment(dob).valueOf(), moment(givenDate).valueOf(), currentVaccine, "").then((response) => {
                if (!!response.result && response.result !== "") {
                    if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_BCG) {
                        managewpdcontroller.managewpdForm.bcgDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_OPV_0) {
                        managewpdcontroller.managewpdForm.opvDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_HEPATITIS_B_0) {
                        managewpdcontroller.managewpdForm.hepatitisBDate.$setValidity('vaccine', false);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_VITAMIN_K) {
                        managewpdcontroller.managewpdForm.vitaminKDate.$setValidity('vaccine', false);
                    }
                } else {
                    if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_BCG) {
                        managewpdcontroller.managewpdForm.bcgDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_OPV_0) {
                        managewpdcontroller.managewpdForm.opvDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_HEPATITIS_B_0) {
                        managewpdcontroller.managewpdForm.hepatitisBDate.$setValidity('vaccine', true);
                    } else if (currentVaccine === managewpdcontroller.immunisations.IMMUNISATION_VITAMIN_K) {
                        managewpdcontroller.managewpdForm.vitaminKDate.$setValidity('vaccine', true);
                    }
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(() => {
                Mask.hide();
            });
        };

        managewpdcontroller.checkTimeValidity = () => {
            if (!!managewpdcontroller.manageWpdObject.deliveryDate) {
                managewpdcontroller.manageWpdObject.deliveryDate = new Date(
                    managewpdcontroller.manageWpdObject.deliveryDate.getFullYear(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getMonth(),
                    managewpdcontroller.manageWpdObject.deliveryDate.getDate(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getHours(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMinutes(),
                    managewpdcontroller.manageWpdObject.deliveryTime.getMilliseconds());
                if (managewpdcontroller.manageWpdObject.deliveryDate > moment()
                    || (!!managewpdcontroller.manageWpdObject.dischargeDate && managewpdcontroller.manageWpdObject.dischargeDate < managewpdcontroller.manageWpdObject.deliveryDate)) {
                    toaster.pop('error', "Delivery time is incorrect");
                    managewpdcontroller.managewpdForm.deliveryTime.$setValidity('time', false);
                }
            } else {
                toaster.pop('error', 'Please select delivery date');
            }
        };

        managewpdcontroller.checkDischargeTimeValidity = () => {
            if (!!managewpdcontroller.manageWpdObject.deliveryDate && !!managewpdcontroller.manageWpdObject.dischargeDate) {
                managewpdcontroller.manageWpdObject.dischargeDate = new Date(
                    managewpdcontroller.manageWpdObject.dischargeDate.getFullYear(),
                    managewpdcontroller.manageWpdObject.dischargeDate.getMonth(),
                    managewpdcontroller.manageWpdObject.dischargeDate.getDate(),
                    managewpdcontroller.manageWpdObject.dischargeTime.getHours(),
                    managewpdcontroller.manageWpdObject.dischargeTime.getMinutes(),
                    managewpdcontroller.manageWpdObject.dischargeTime.getMilliseconds());
                if (managewpdcontroller.manageWpdObject.dischargeDate < managewpdcontroller.manageWpdObject.deliveryDate) {
                    toaster.pop('error', "Discharge time is incorrect");
                    managewpdcontroller.managewpdForm.dischargeTime.$setValidity('time', false);
                }
            }
        };

        managewpdcontroller.bcgGivenChanged = (childDetail) => childDetail.bcgDate = null;
        managewpdcontroller.opvGivenChanged = (childDetail) => childDetail.opvDate = null;
        managewpdcontroller.hepatitisBGivenChanged = (childDetail) => childDetail.hepatitisBDate = null;
        managewpdcontroller.vitaminKGivenChanged = (childDetail) => childDetail.vitaminKDate = null;

        managewpdcontroller.dischargeChanged = () => {
            managewpdcontroller.manageWpdObject.dischargeDate = null;
            managewpdcontroller.manageWpdObject.freeMedicines = null;
            managewpdcontroller.manageWpdObject.freeDiet = null;
            managewpdcontroller.manageWpdObject.freeLabTest = null;
            managewpdcontroller.manageWpdObject.freeBloodTransfusion = null;
            managewpdcontroller.manageWpdObject.freeDropTransport = null;
        };

        managewpdcontroller.motherAliveChanged = () => {
            if (managewpdcontroller.manageWpdObject.isMotherAlive) {
                managewpdcontroller.showDischargeSection = true;
            } else {
                managewpdcontroller.showDischargeSection = false;
            }
            managewpdcontroller.resetDischargeSection();
        };

        managewpdcontroller.motherReferralChanged = () => {
            if (managewpdcontroller.manageWpdObject.isMotherReferralDone) {
                managewpdcontroller.showDischargeSection = false;
            } else {
                managewpdcontroller.showDischargeSection = true;
            }
            managewpdcontroller.resetDischargeSection();
        };

        managewpdcontroller.resetDischargeSection = () => {
            managewpdcontroller.manageWpdObject.isDischarged = null;
            managewpdcontroller.manageWpdObject.dischargeDate = null;
            managewpdcontroller.manageWpdObject.freeMedicines = null;
            managewpdcontroller.manageWpdObject.freeDiet = null;
            managewpdcontroller.manageWpdObject.freeLabTest = null;
            managewpdcontroller.manageWpdObject.freeBloodTransfusion = null;
            managewpdcontroller.manageWpdObject.freeDropTransport = null;
        };

        managewpdcontroller.dateDiffInDays = (a, b) => {
            var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
            var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

            return Math.floor((utc2 - utc1) / managewpdcontroller.MS_PER_DAY);
        };

        managewpdcontroller.init();
    }
    angular.module('imtecho.controllers').controller('ManageWpdController', ManageWpdController);
})();
