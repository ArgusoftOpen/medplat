(function () {
    function FacilityPerformanceController(FacilityPerformanceDAO, toaster, AuthenticateService, QueryDAO, Mask, GeneralUtil) {
        var ctrl = this;
        var currentDate = new Date();
        ctrl.facilityPerformanceDto = {};

        /**
         * For temporary feature (https://argusgit.argusoft.com/mhealth-projects/imtecho/issues/2172) request
         * i.e. to allow user to insert old entries by entering a past date but not allowed to edit
         *      in period of dates between 14 Oct 2019 to 21 Oct 2019
         */
        const currentDateVal = moment().valueOf();
        const startDateVal = moment("2019-10-14").valueOf();
        const endDateVal = moment("2019-10-26").valueOf();
        ctrl.isTemporaryFeatureEnable = (startDateVal < currentDateVal) && (currentDateVal < endDateVal);

        ctrl.minDate = ctrl.isTemporaryFeatureEnable ? null : new Date(moment(currentDate).subtract('days', 4));
        ctrl.maxDate = currentDate;

        /**
         * Get Health Infrastructure assigned to user
         */
        var init = function () {
            Mask.show();
            AuthenticateService.getLoggedInUser().then(function (user) {
                ctrl.loggedInUser = user.data;
                QueryDAO.execute({
                    code: 'retrieve_health_infra_for_user',
                    parameters: {
                        userId: ctrl.loggedInUser.id
                    }
                }).then(function (res) {
                    Mask.hide();
                    ctrl.institutes = res.result;
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            });
        };

        /**
         * Get performance details
         */
        ctrl.getData = function (form, showMessage) {
            if (ctrl.facilityPerformanceDto.performanceDate != null && ctrl.facilityPerformanceDto.healthInfrastructureId != null) {
                ctrl.isUpdateMode = false;
                Mask.show();
                ctrl.hid = ctrl.facilityPerformanceDto.healthInfrastructureId;
                ctrl.backupDate = new Date(ctrl.facilityPerformanceDto.performanceDate);

                /**
                 *  Get Facility Performance data
                 */
                ctrl.isProcessing = true;
                FacilityPerformanceDAO.getFacilityPerformaceByHidAndDate(ctrl.facilityPerformanceDto.healthInfrastructureId, moment(ctrl.facilityPerformanceDto.performanceDate).valueOf()).then(function (res) {
                    ctrl.isProcessing = false;
                    if (res.id != null) {
                        if (showMessage) {
                            toaster.pop('warning', 'Facility performance already recorded for the selected date');
                        }
                        ctrl.isUpdateMode = true;
                        ctrl.facilityPerformanceDto = res;
                        ctrl.facilityPerformanceDto.healthInfrastructureId = ctrl.hid;
                        ctrl.facilityPerformanceDto.performanceDate = ctrl.backupDate;
                    } else {
                        ctrl.facilityPerformanceDto = {};
                        form.$setPristine();
                        ctrl.facilityPerformanceDto.healthInfrastructureId = ctrl.hid;
                        ctrl.facilityPerformanceDto.performanceDate = ctrl.backupDate;
                    }

                    /**
                     *  Get no of deliveries conducted
                    */
                    QueryDAO.execute({
                        code: 'no_of_deliveries_conducted',
                        parameters: {
                            hid: Number(ctrl.facilityPerformanceDto.healthInfrastructureId),
                            performanceDate: moment(ctrl.facilityPerformanceDto.performanceDate).format('DD-MM-YYYY HH:mm:mm')
                        }
                    }).then(function (noOfDeliveryRes) {
                        Mask.hide();
                        ctrl.facilityPerformanceDto.noOfDeliveresConducted = noOfDeliveryRes.result[0].count;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });

                    /**
                    * Get no of caesarean deliveries conducted
                    */
                    QueryDAO.execute({
                        code: 'no_of_caesarean_deliveries_conducted',
                        parameters: {
                            hid: Number(ctrl.facilityPerformanceDto.healthInfrastructureId),
                            performanceDate: moment(ctrl.facilityPerformanceDto.performanceDate).format('DD-MM-YYYY HH:mm:mm')
                        }
                    }).then(function (noOfSectionRes) {
                        Mask.hide();
                        ctrl.facilityPerformanceDto.noOfSectionConducted = noOfSectionRes.result[0].count;
                    }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                        Mask.hide();
                    });
                }).catch(function (err) {
                    ctrl.facilityPerformanceDto.performanceDate = null
                    GeneralUtil.showMessageOnApiCallFailure(err);
                }).finally(function () {
                    Mask.hide();
                })
            }
        }

        /**
         * Insert or Update Facility Performance
         */
        ctrl.createOrUpdateFacilitiyPerformance = function (form) {
            if (form.$valid) {
                Mask.show();
                const performanceDto = angular.copy(ctrl.facilityPerformanceDto);
                performanceDto.performanceDate = moment(ctrl.facilityPerformanceDto.performanceDate).format('YYYY-MM-DD');
                FacilityPerformanceDAO.createOrUpdate(performanceDto).then(function (res) {
                    if (ctrl.isUpdateMode)
                        toaster.pop('success', 'Facility Performance Updated Successfully');
                    else
                        toaster.pop('success', 'Facility Performance Saved Successfully');
                    ctrl.getData(facilityPerformanceForm, false)
                }).catch(function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                }).finally(function () {
                    Mask.hide();
                })
            }
        }

        init();

    }
    angular.module('imtecho.controllers').controller('FacilityPerformanceController', FacilityPerformanceController);
})();
