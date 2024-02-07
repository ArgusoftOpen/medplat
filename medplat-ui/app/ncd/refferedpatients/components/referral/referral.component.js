angular.module("imtecho.components")
    .component("referral", {
        templateUrl: 'app/ncd/refferedpatients/components/referral/referral.html',
        bindings: {
            referralDto: '=',
            singleComponentForm: '=',
            institutionType: '<'
        },
        controllerAs: 'referral',
        controller: function ($scope, GeneralUtil, QueryDAO, Mask) {
            let referral = this;
            referral.typesRetrieved = false;

            referral.retrieveHealthInfras = function (type, locationId) {
                referral.showHealthInfrastructures = false;
                referral.showLocation = false;
                if (type && !locationId) {
                    referral.selectedLocation = {};
                    referral.showLocation = true;
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

            referral.$onChanges = function (changes) {
                if (changes.institutionType && referral.institutionType) {
                    try {
                        referral.institutionType = typeof referral.institutionType === 'string' ? JSON.parse(referral.institutionType) : referral.institutionType;
                    } catch (error) {
                        console.log('Error while parsing JSON referral.institutionType :: ', error);
                        referral.institutionType = {};
                    }
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
                                infrastructureType: referral.institutionType != null ? referral.institutionType.type : null
                            }
                        }).then(function (healthInfra) {
                            referral.types = referral.types.filter(function (institute) {
                                return healthInfra.result.some(function (result) {
                                    return institute.id == result.health_infra_type_id
                                });
                            })
                            Mask.hide();
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                        });
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    })
                }
            }

            $scope.$watch("referral.selectedLocationId", function (newVal) {
                if (referral.referralDto && referral.referralDto.referredToId && newVal) {
                    referral.retrieveHealthInfras(referral.referralDto.referredToId, newVal)
                }
            });
        }
    });
