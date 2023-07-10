(function (angular) {
    function MarkChiranjeevi(Mask, toaster, QueryDAO, $uibModal, GeneralUtil) {
        var markchiranjeevi = this;
        markchiranjeevi.search = {};
        markchiranjeevi.noRecordsFound = true;
        markchiranjeevi.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        var init = function () {
            markchiranjeevi.memberDetails = [];
        };

        markchiranjeevi.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                markchiranjeevi.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                markchiranjeevi.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
            if (CKEDITOR.instances) {
                for (var ck_instance in CKEDITOR.instances) {
                    CKEDITOR.instances[ck_instance].destroy();
                }
            }
        };

        markchiranjeevi.retrieveFilteredMembers = function () {
            if (markchiranjeevi.selectedLocationId == null) {
                toaster.pop('error', 'Please select Location')
            } else {
                if (!markchiranjeevi.pagingService.pagingRetrivalOn && !markchiranjeevi.pagingService.allRetrieved) {
                    markchiranjeevi.pagingService.pagingRetrivalOn = true;
                    setOffsetLimit();
                    markchiranjeevi.noRecordsFound = true;
                    if (markchiranjeevi.searchForm.$valid) {
                        var queryDto = {
                            code: 'mark_as_chiranjeevi_list',
                            parameters: {
                                locationId: markchiranjeevi.selectedLocationId,
                                limit: markchiranjeevi.pagingService.limit,
                                offSet: markchiranjeevi.pagingService.offSet
                            }
                        };
                        Mask.show();
                        QueryDAO.executeQuery(queryDto).then(function (response) {
                            Mask.hide();
                            if (response.result.length == 0 || response.result.length < markchiranjeevi.pagingService.limit) {
                                markchiranjeevi.pagingService.allRetrieved = true;
                                if (markchiranjeevi.pagingService.index === 1) {
                                    markchiranjeevi.memberDetails = response.result;
                                }
                            } else {
                                markchiranjeevi.pagingService.allRetrieved = false;
                                if (markchiranjeevi.pagingService.index > 1) {
                                    markchiranjeevi.memberDetails = markchiranjeevi.memberDetails.concat(response.result);
                                } else {
                                    markchiranjeevi.memberDetails = response.result;
                                }
                            }
                        }, function () {
                            Mask.hide();
                            toaster.pop('error', 'Unable to retrieve members list');
                            markchiranjeevi.pagingService.allRetrieved = true;
                        }).finally(function () {
                            markchiranjeevi.pagingService.pagingRetrivalOn = false;
                            Mask.hide();
                        });
                    }
                }
            }
        };

        markchiranjeevi.searchData = function (reset) {
            markchiranjeevi.searchForm.$setSubmitted();
            if (markchiranjeevi.searchForm.$valid) {
                if (reset) {
                    markchiranjeevi.toggleFilter();
                    markchiranjeevi.pagingService.index = 0;
                    markchiranjeevi.pagingService.allRetrieved = false;
                    markchiranjeevi.pagingService.pagingRetrivalOn = false;
                    markchiranjeevi.memberDetails = [];
                }
                markchiranjeevi.retrieveFilteredMembers();
            }
        };

        var setOffsetLimit = function () {
            markchiranjeevi.pagingService.limit = 100;
            markchiranjeevi.pagingService.offSet = markchiranjeevi.pagingService.index * 100;
            markchiranjeevi.pagingService.index = markchiranjeevi.pagingService.index + 1;
        };

        markchiranjeevi.markAsChiranjeevi = function (wpdId) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/common/views/confirmation.modal.html',
                controller: 'ConfirmModalController',
                windowClass: 'cst-modal',
                size: 'md',
                resolve: {
                    message: function () {
                        return "Are you sure you want to mark this delivery as Chiranjeevi?";
                    }
                }
            });
            modalInstance.result.then(function () {
                var queryDto = {
                    code: 'mark_as_chiranjeevi',
                    parameters: {
                        wpdId: wpdId
                    }
                };
                Mask.show();
                QueryDAO.executeQuery(queryDto).then(function () {
                    Mask.hide();
                    toaster.pop('success', "Delivery marked as Chiranjeevi");
                    markchiranjeevi.pagingService.index = 0;
                    markchiranjeevi.pagingService.allRetrieved = false;
                    markchiranjeevi.pagingService.pagingRetrivalOn = false;
                    markchiranjeevi.memberDetails = [];
                    markchiranjeevi.retrieveFilteredMembers();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                })
            }, function () { });
        }

        init();
    }
    angular.module('imtecho.controllers').controller('MarkChiranjeevi', MarkChiranjeevi);
})(window.angular);
