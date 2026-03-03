/**
 *
 * @author Dhaval
 *
 * Infrastructure Directive
 *
 * Attributes:
 * ngModel {Object} model of directive contains properties viz. deliveryPlace, institutionType, institute
 * disabled {Boolean} property to disable the all fields
 * showDeliveryPlace {Boolean} property to show Delivery Place select field
 * typeOfDelivery {String} value of already selected Type of Delivery
 * institutesToFetch {String} constant string to fetch This Institutes or Other Institutes
 *
 */

(function () {
    let infrastructureDirective = function (QueryDAO, Mask, GeneralUtil, AuthenticateService, SelectizeGenerator) {
        return {
            restrict: 'E',
            scope: {
                ngModel: '=?',
                disabled: '=?',
                showDeliveryPlace: '=?',
                typeOfDelivery: '=?',
                institutesToFetch: '=?'
            },
            templateUrl: 'app/common/directives/infrastructure/template.html',
            link: function ($scope) {
                $scope.form = {};
                if (!$scope.ngModel) {
                    $scope.ngModel = {};
                }
                $scope.showThisHospitals = false;
                $scope.showOtherHospitals = false;

                let _init = function () {
                    switch ($scope.institutesToFetch) {
                        case 'ANOTHER':
                            $scope.showOtherHospitals = true;
                            break;
                        case 'THIS':
                        default:
                            $scope.showThisHospitals = true;
                            break;
                    }
                    if ($scope.ngModel.institute && $scope.showOtherHospitals) {
                        $scope.tempOtherInstitute = $scope.ngModel.institute;
                        $scope.onInstitutionTypeChange();
                    }

                    Mask.show();
                    AuthenticateService.getLoggedInUser().then(function (user) {
                        $scope.loggedInUser = user.data;
                        let dtoList = [];
                        let fetchInstitutionTypes = {
                            code: 'fetch_listvalue_detail_from_field',
                            parameters: {
                                field: 'Health Infrastructure Type'
                            },
                            sequence: 1
                        };
                        dtoList.push(fetchInstitutionTypes);
                        let fetchAssignedInstitutes = {
                            code: 'retrieve_health_infra_for_user',
                            parameters: {
                                userId: $scope.loggedInUser.id
                            },
                            sequence: 2
                        };
                        dtoList.push(fetchAssignedInstitutes);
                        QueryDAO.executeAll(dtoList).then(function (responses) {
                            $scope.institutionTypes = responses[0].result;
                            $scope.thisInstitutes = responses[1].result;
                            if (!$scope.showDeliveryPlace && $scope.showThisHospitals) {
                                toaster.pop('error', 'No health infrastructure assigned!');
                            }
                        }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                            Mask.hide();
                        });
                    }, function () {
                        GeneralUtil.showMessageOnApiCallFailure();
                        Mask.hide();
                    })
                };

                $scope.onDeliveryPlaceChange = function () {
                    $scope.ngModel.institute = null;
                    $scope.ngModel.institutionType = null;
                    if ($scope.showThisHospitals) {
                        if ($scope.thisInstitutes.length === 1) {
                            $scope.ngModel.institute = $scope.thisInstitutes[0].id;
                        } else if ($scope.thisInstitutes.length === 0) {
                            toaster.pop('error', 'No health infrastructure assigned!');
                        }
                    }
                }

                $scope.onInstitutionTypeChange = function () {
                    $scope.retrievingOtherHospitals = true;
                    $scope.ngModel.institute = null;
                    Mask.show();
                    QueryDAO.execute({
                        code: 'retrieve_hospitals_by_infra_type',
                        parameters: {
                            infraType: Number($scope.ngModel.institutionType)
                        }
                    }).then(function (res) {
                        $scope.retrievingOtherHospitals = false;
                        $scope.otherInstitutes = res.result;
                        $scope.selectizeObjectForOtherInstitute = SelectizeGenerator.getOtherInstitute($scope.otherInstitutes).config;
                        $scope.ngModel.institute = $scope.tempOtherInstitute ? $scope.tempOtherInstitute : null;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                }

                _init();
            }
        };
    };
    angular.module('imtecho.directives').directive('infrastructureDirective', infrastructureDirective);
})();
