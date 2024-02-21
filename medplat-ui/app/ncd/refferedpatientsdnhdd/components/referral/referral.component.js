angular.module("imtecho.components")
    .component("referral", {
        templateUrl: 'app/ncd/refferedpatientsdnhdd/components/referral/referral.html',
        bindings: {
            referralDto: '=',
            singleComponentForm: '=',
            institutionType: '<'
        },
        controllerAs: 'referral',
        controller: function ($scope, GeneralUtil, QueryDAO, Mask) {
            const referral = this;
            referral.typesRetrieved = false;

            referral.fetchDetails = function () {
                let queryDtos = [];
                let q = {
                    code: 'retrival_listvalue_values_acc_field',
                    parameters: {
                        fieldKey: 'infra_type' 
                    },
                    sequence: 1
                };
                queryDtos.push(q);

                let q1 = {
                    code: 'retrieve_last_location_level',
                    sequence: 2
                };
                queryDtos.push(q1);

                Mask.show();
                QueryDAO.executeAll(queryDtos).then(function (res) {
                    referral.lfvdRecords = res[0].result;
                    referral.fetchLevel = res[1].result[0].level;
                    let pvtHospitalRecord = referral.lfvdRecords.find(record => {
                        return record.value === 'Private Hospital';
                    });
                    referral.pvtHospitalType = pvtHospitalRecord.id;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            }

            referral.retrieveHealthInfras = function (type, locationId) {
                referral.showHealthInfrastructures = false;
                let selectedType = referral.healthInfraTypes.find(infra => infra.health_infra_type_id === type);
                referral.fetchLevel = !!selectedType && !!selectedType.location_level ? selectedType.location_level : referral.fetchLevel;

                if (type && !locationId) {
                    referral.selectedLocation = {};
                    referral.showLocation = type !== referral.pvtHospitalType;
                }
                if (type && locationId) {
                    referral.showLocation = true;
                    referral.showHealthInfrastructures = true;
                    Mask.show()
                    QueryDAO.execute({
                        code: 'health_infra_retrival_by_type_for_ncd',
                        parameters: {
                            type: type,
                            locationId: locationId
                        }
                    }).then(function (res) {
                        referral.healthInfras = res.result;
                        referral.showHealthInfrastructures = true;
                        Mask.hide()
                    });
                }
            };

            // SESSION STORAGE IS USED: TO USE OF 'referral' COMPONENT FOR HYPERTENSION AND DIABETES IN SAME VIEW
            // THIS IS TO PREVENT SIMULTANEOUS API CALLS FROM BOTH COMPONENT
            $scope.$watch('referral.institutionType', function(newVal) {
                referral.referralDto = {};
                if (!sessionStorage.getItem('isAPICalled') && referral.institutionType) {
                    if (newVal) {
                        sessionStorage.setItem('isAPICalled', true);
                        try {
                            referral.healthInfra = typeof referral.institutionType === 'string' ? JSON.parse(referral.institutionType) : referral.institutionType;
                        } catch (error) {
                            console.log('Error while parsing JSON referral.institutionType :: ', error);
                            referral.healthInfra = {};
                        }
                        Mask.show();
                        QueryDAO.execute({
                            code: 'retrieve_field_values_for_form_field',
                            parameters: {
                                form: 'WEB',
                                fieldKey: 'infra_type'
                            }
                        }).then(function (res) {
                            referral.types = res.result;
                            referral.typesRetrieved = true;
                            QueryDAO.execute({
                                code: 'retrieve_health_infra_by_level',
                                parameters: {
                                    filter: 'U',
                                    infrastructureType: referral.healthInfra != null ? referral.healthInfra.type : null
                                }
                            }).then(function (res) {
                                referral.types = referral.types.filter(function (institute) {
                                    return res.result.some(function (result) {
                                        return institute.id == result.health_infra_type_id
                                    });
                                });
                                referral.healthInfraTypes = res.result;
                            }, GeneralUtil.showMessageOnApiCallFailure);
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                            sessionStorage.removeItem('isAPICalled');
                        })
                    }
                }
            });

            $scope.$watch("referral.selectedLocationId", function (newVal) {
                if (referral.referralDto?.referredToId && newVal) {
                    referral.retrieveHealthInfras(referral.referralDto.referredToId, newVal)
                }
            });

            $scope.$watch("referral.referralDto.isReferred", function () {
                if (!!referral.referralDto.isReferred) {
                    referral.fetchDetails();
                }
                angular.forEach(referral.referralDto, function(value, key) {
                    if (key !== 'isReferred') {
                        delete referral.referralDto[key];
                    }
                    referral.selectedLocationId = null;
                })
            });
        }
    });
