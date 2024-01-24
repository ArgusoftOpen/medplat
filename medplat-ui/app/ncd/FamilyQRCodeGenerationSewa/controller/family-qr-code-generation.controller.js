(function (angular) {
    function FamilyQRCodeGenerationSewaController(FamilyQRCodeDAO, AuthenticateService, PagingService, $scope, Mask, GeneralUtil, $rootScope) {
        const ctrl = this;
        $scope.ctrl = ctrl;
        ctrl.pagingService = PagingService.initialize();
        ctrl.requiredLevel = 6;
        ctrl.selectedIndex = null;
        ctrl.qrCodeImage = {};
        ctrl.today = moment();

        AuthenticateService.getLoggedInUser().then(function (user) {
            ctrl.loggedInUser = user.data;
        });

        ctrl.submit = function () {
            ctrl.locationForm.$setSubmitted();
            if (ctrl.selectedLocation && ctrl.requiredLevel <= ctrl.selectedLocation.finalSelected.level && ctrl.locationForm.$valid) {
                ctrl.getFamilyList(false);
                ctrl.toggleFilter();
            }
        };

        ctrl.collapse = function (index, familyId) {
            if (ctrl.selectedIndex == index) ctrl.selectedIndex = null
            else {
                ctrl.selectedIndex = index;
                if (ctrl.qrCodeImage[index] == null) {
                    Mask.show();
                    FamilyQRCodeDAO.generateQrCode(familyId).then((res) => {
                        ctrl.qrCodeImage[index] = URL.createObjectURL(res.data);
                    }).catch((error) => {
                        GeneralUtil.showMessageOnApiCallFailure(error);
                    }).finally(Mask.hide);
                }
            }
        }

        ctrl.getFamilyList = function (reset) {
            Mask.show();
            if (!reset) {
                ctrl.familyList = [];
                ctrl.pagingService.resetOffSetAndVariables();
            }
            ctrl.selectedIndex = null;
            if (ctrl.selectedLocationId) {
                let dto = {
                    location: ctrl.selectedLocationId,
                    fromDate: moment(ctrl.fromDate).format('YYYY-MM-DD'),
                    toDate: moment(ctrl.toDate).format('YYYY-MM-DD'),
                    limit: ctrl.pagingService.limit,
                    offset: ctrl.pagingService.offSet
                }
                let familyList = ctrl.familyList;
                PagingService.getNextPage(FamilyQRCodeDAO.getFamiliesSewa, dto, familyList, null).then(function (res) {
                    ctrl.familyList = res;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        }

        ctrl.printAllFamily = function (familyId) {
            Mask.show();
            if (familyId == -1) {
                let dto = {
                    location: ctrl.selectedLocationId,
                    fromDate: moment(ctrl.fromDate).format('YYYY-MM-DD'),
                    toDate: moment(ctrl.toDate).format('YYYY-MM-DD')
                }
                FamilyQRCodeDAO.generatePdfSewa(dto).then(res => {
                    if (res.data !== null && navigator.msSaveBlob) {
                        return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                    }
                    let a = $("<a style='display: none;'/>");
                    let url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
                    a.attr("href", url);
                    a.attr("download", $rootScope.currentState.title + "_" + new Date().getTime() + ".pdf");
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();
                }).catch(error => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            } else {
                let dto = {
                    familyId: familyId
                }
                FamilyQRCodeDAO.generatePdfSewa(dto).then(res => {
                    if (res.data !== null && navigator.msSaveBlob) {
                        return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                    }
                    let a = $("<a style='display: none;'/>");
                    let url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
                    a.attr("href", url);
                    a.attr("download", $rootScope.currentState.title + "_" + new Date().getTime() + ".pdf");
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();
                }).catch(error => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(() => {
                    Mask.hide();
                });
            }
        };

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };
    }
    angular.module('imtecho.controllers').controller('FamilyQRCodeGenerationSewaController', FamilyQRCodeGenerationSewaController);
})(window.angular);