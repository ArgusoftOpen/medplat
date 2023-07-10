(function () {
    function ExcelGenereatorController(toaster, GeneralUtil, ReportDAO, Mask) {
        var ctrl = this;

        ctrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                ctrl.modalClosed = true;
                angular.element('body').css("overflow", "auto");
            } else {
                ctrl.modalClosed = false;
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        ctrl.downloadExcel = function () {
            if (!!ctrl.queryValue) {
                var paramObject = {
                    query: ctrl.queryValue
                }
                var reportExcelDto = {
                    paramObj: paramObject
                }
                Mask.show();
                ReportDAO.downloadExcel(208, reportExcelDto).then(function (res) {
                    if (res.data !== null && navigator.msSaveBlob) {
                        return navigator.msSaveBlob(new Blob([res.data], { type: '' }));
                    }
                    var a = $("<a style='display: none;'/>");
                    var url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/vnd.ms-excel' }));
                    a.attr("href", url);
                    a.attr("download", (!!ctrl.fileName ? ctrl.fileName : "") + "_" + new Date().getTime() + ".xlsx");
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();
                }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
                    Mask.hide();
                });
            } else {
                toaster.pop('error', 'Query can not be null');
            }

        };
    }
    angular.module('imtecho.controllers').controller('ExcelGenereatorController', ExcelGenereatorController);
})();
